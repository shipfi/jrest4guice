package org.jrest4guice.commons.lang;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;

@SuppressWarnings("unchecked")
public class ParameterNameDiscoverer {
	
	private static final char PACKAGE_SEPARATOR = '.';
	public static final String CLASS_FILE_SUFFIX = ".class";
	
	private static Log logger = LogFactory.getLog(ParameterNameDiscoverer.class);
	
	private final Map parameterNamesCache = new HashMap(16);
	
	private final Map classReaderCache = new HashMap();
	
	public String[] getParameterNames(Method method) {
		String[] paramNames = (String[]) parameterNamesCache.get(method);
		if (paramNames == null) {
			try {
				ParameterNameDiscoveringVisitor visitor = visitMethod(method);
				if (visitor.foundTargetMember()) {
					paramNames = visitor.getParameterNames();
					parameterNamesCache.put(method, paramNames);
				}
			} catch (IOException ex) {
				if (logger.isDebugEnabled()) {
					logger.debug("IOException whilst attempting to read '.class' file for class ["
					        + method.getDeclaringClass().getName()
					        + "] - unable to determine parameter names for method: " + method, ex);
				}
			}
		}
		return paramNames;
	}
	
	public String[] getParameterNames(Constructor ctor) {
		String[] paramNames = (String[]) parameterNamesCache.get(ctor);
		if (paramNames == null) {
			try {
				ParameterNameDiscoveringVisitor visitor = visitConstructor(ctor);
				if (visitor.foundTargetMember()) {
					paramNames = visitor.getParameterNames();
					parameterNamesCache.put(ctor, paramNames);
				}
			} catch (IOException ex) {
				if (logger.isDebugEnabled()) {
					logger.debug("IOException whilst attempting to read '.class' file for class ["
					        + ctor.getDeclaringClass().getName()
					        + "] - unable to determine parameter names for constructor: " + ctor, ex);
				}
			}
		}
		return paramNames;
	}
	
	private ParameterNameDiscoveringVisitor visitMethod(Method method) throws IOException {
		ClassReader classReader = getClassReader(method.getDeclaringClass());
		FindMethodParameterNamesClassVisitor classVisitor = new FindMethodParameterNamesClassVisitor(method);
		classReader.accept(classVisitor, false);
		return classVisitor;
	}
	
	private ParameterNameDiscoveringVisitor visitConstructor(Constructor ctor) throws IOException {
		ClassReader classReader = getClassReader(ctor.getDeclaringClass());
		FindConstructorParameterNamesClassVisitor classVisitor = new FindConstructorParameterNamesClassVisitor(ctor);
		classReader.accept(classVisitor, false);
		return classVisitor;
	}
	
	private ClassReader getClassReader(Class clazz) throws IOException {
		synchronized (classReaderCache) {
			ClassReader classReader = (ClassReader) classReaderCache.get(clazz);
			if (classReader == null) {
				InputStream is = clazz.getResourceAsStream(getClassFileName(clazz));
				if (is == null)
					throw new FileNotFoundException("Class file for class [" + clazz.getName() + "] not found");
				try {
					classReader = new ClassReader(is);
					classReaderCache.put(clazz, classReader);
				} finally {
					is.close();
				}
			}
			return classReader;
		}
	}
	
	private String getClassFileName(Class clazz) {
		String className = clazz.getName();
		int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
		return className.substring(lastDotIndex + 1) + CLASS_FILE_SUFFIX;
	}
	
	private static abstract class ParameterNameDiscoveringVisitor extends EmptyVisitor {
		
		private String methodNameToMatch;
		
		private String descriptorToMatch;
		
		private int numParamsExpected;
		
		private int[] lvtSlotIndex;
		
		private boolean foundTargetMember = false;
		
		private String[] parameterNames;
		
		public ParameterNameDiscoveringVisitor(String name, boolean isStatic, Class[] paramTypes) {
			methodNameToMatch = name;
			numParamsExpected = paramTypes.length;
			computeLVTSlotIndices(isStatic, paramTypes);
		}
		
		public void setDescriptorToMatch(String descriptor) {
			descriptorToMatch = descriptor;
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			if (name.equals(methodNameToMatch) && desc.equals(descriptorToMatch)) {
				foundTargetMember = true;
				return new LocalVariableTableVisitor(isStatic(access), this, numParamsExpected, lvtSlotIndex);
			} else
				return null;
		}
		
		private boolean isStatic(int access) {
			return ((access & Opcodes.ACC_STATIC) > 0);
		}
		
		public boolean foundTargetMember() {
			return foundTargetMember;
		}
		
		public String[] getParameterNames() {
			if (!foundTargetMember())
				throw new IllegalStateException("Can't ask for parameter names when target member has not been found");
			
			return parameterNames;
		}
		
		public void setParameterNames(String[] names) {
			parameterNames = names;
		}
		
		private void computeLVTSlotIndices(boolean isStatic, Class[] paramTypes) {
			lvtSlotIndex = new int[paramTypes.length];
			int nextIndex = (isStatic ? 0 : 1);
			for (int i = 0; i < paramTypes.length; i++) {
				lvtSlotIndex[i] = nextIndex;
				if (isWideType(paramTypes[i])) {
					nextIndex += 2;
				} else {
					nextIndex++;
				}
			}
		}
		
		private boolean isWideType(Class aType) {
			return (aType == Long.TYPE || aType == Double.TYPE);
		}
	}
	
	private static class FindMethodParameterNamesClassVisitor extends ParameterNameDiscoveringVisitor {
		
		public FindMethodParameterNamesClassVisitor(Method method) {
			super(method.getName(), Modifier.isStatic(method.getModifiers()), method.getParameterTypes());
			setDescriptorToMatch(Type.getMethodDescriptor(method));
		}
	}
	
	private static class FindConstructorParameterNamesClassVisitor extends ParameterNameDiscoveringVisitor {
		
		public FindConstructorParameterNamesClassVisitor(Constructor cons) {
			super("<init>", false, cons.getParameterTypes());
			Type[] pTypes = new Type[cons.getParameterTypes().length];
			for (int i = 0; i < pTypes.length; i++) {
				pTypes[i] = Type.getType(cons.getParameterTypes()[i]);
			}
			setDescriptorToMatch(Type.getMethodDescriptor(Type.VOID_TYPE, pTypes));
		}
	}
	
	private static class LocalVariableTableVisitor extends EmptyVisitor {
		
		private boolean isStatic;
		private ParameterNameDiscoveringVisitor memberVisitor;
		private int numParameters;
		private int[] lvtSlotIndices;
		private String[] parameterNames;
		private boolean hasLVTInfo = false;
		
		public LocalVariableTableVisitor(boolean isStatic, ParameterNameDiscoveringVisitor memberVisitor,
		        int numParams, int[] lvtSlotIndices) {
			this.isStatic = isStatic;
			numParameters = numParams;
			parameterNames = new String[numParameters];
			this.memberVisitor = memberVisitor;
			this.lvtSlotIndices = lvtSlotIndices;
		}
		
		@Override
		public void visitLocalVariable(String name, String description, String signature, Label start, Label end,
		        int index) {
			hasLVTInfo = true;
			if (isMethodArgumentSlot(index)) {
				parameterNames[parameterNameIndexForSlot(index)] = name;
			}
		}
		
		@Override
		public void visitEnd() {
			if (hasLVTInfo || isStatic && numParameters == 0) {
				memberVisitor.setParameterNames(parameterNames);
			}
		}
		
		private boolean isMethodArgumentSlot(int index) {
			for (int element : lvtSlotIndices) {
				if (element == index)
					return true;
			}
			return false;
		}
		
		private int parameterNameIndexForSlot(int slot) {
			for (int i = 0; i < lvtSlotIndices.length; i++) {
				if (lvtSlotIndices[i] == slot)
					return i;
			}
			throw new IllegalStateException("Asked for index for a slot which failed the isMethodArgumentSlot test: "
			        + slot);
		}
	}
	
}
