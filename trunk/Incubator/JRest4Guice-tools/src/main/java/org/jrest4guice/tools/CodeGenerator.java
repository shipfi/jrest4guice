package org.jrest4guice.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * 代码生成器
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
public class CodeGenerator {
	private VelocityContext context;

	private String templatePath;
	private String packageNamePath;
	private String projectName;

	private boolean useJPA;

	public CodeGenerator() {
		this.templatePath = System.getProperty("user.dir") + File.separator
				+ "templates";
		this.initVelocity();
	}

	public void createSimpleProject(String targetPath, String projectName,
			String packageName) {
		this.createProject(targetPath, projectName, packageName, false);
	}

	public void createJPAProject(String targetPath, String projectName,
			String packageName) {
		this.createProject(targetPath, projectName, packageName, true);
	}

	private void createProject(String targetPath, String projectName,
			String packageName, boolean useJPA) {
		this.packageNamePath = packageName.replace(".", File.separator);

		this.projectName = projectName.substring(0, 1).toUpperCase()
				+ projectName.substring(1);

		this.useJPA = useJPA;

		// 往上下文中填入数据
		this.context = new VelocityContext();
		Map<String, String> contextMap = new HashMap<String, String>();
		contextMap.put("projectName", this.projectName);
		contextMap.put("packageName", packageName);
		if (this.useJPA)
			contextMap.put("entityName", this.projectName);

		context.put("context", contextMap);

		File projectRoot = new File(this.templatePath);
		File target = new File(targetPath + File.separator + projectName);
		this.generateFile(target, projectRoot, true, this.projectName);
	}

	public void createResource(String targetPath, String packageName,
			String entityName) {
		this.useJPA = true;

		this.packageNamePath = packageName.replace(".", File.separator);

		// 往上下文中填入数据
		this.context = new VelocityContext();
		Map<String, String> contextMap = new HashMap<String, String>();
		contextMap.put("packageName", packageName);
		contextMap.put("entityName", entityName);
		context.put("context", contextMap);

		File mainTarget = new File(targetPath+(".src.main.java").replace(".", File.separator));
		File testTarget = new File(targetPath+(".src.test.java").replace(".", File.separator));

		File file = new File(this.templatePath + File.separator
				+ ("src.main.java.Service").replace(".", File.separator)
				+ ".java");
		this.generateFile(mainTarget, file, false, entityName);

		file = new File(this.templatePath + File.separator
				+ ("src.main.java.ServiceBean").replace(".", File.separator)
				+ ".java");
		this.generateFile(mainTarget, file, false, entityName);

		file = new File(this.templatePath + File.separator
				+ ("src.main.java.Entity").replace(".", File.separator)
				+ ".java");
		this.generateFile(mainTarget, file, false, entityName);

		file = new File(this.templatePath + File.separator
				+ ("src.main.java.Resource").replace(".", File.separator)
				+ ".java");
		this.generateFile(mainTarget, file, false, entityName);

		file = new File(this.templatePath + File.separator
				+ ("src.test.java.ServiceTest").replace(".", File.separator)
				+ ".java");
		this.generateFile(testTarget, file, false, entityName);
	}

	/**
	 * 根据模板输出实际文件
	 * 
	 * @param target
	 * @param file
	 */
	private void generateFile(File target, File file, boolean isRoot,
			String entityName) {
		if (file.isDirectory()) {
			if (file.getName().equalsIgnoreCase(".svn"))
				return;
			File[] files = file.listFiles();
			File _target = new File(target.getPath()
					+ (isRoot ? "" : (File.separator + file.getName())));

			if (!_target.exists())
				_target.mkdirs();

			for (File f : files) {
				this.generateFile(_target, f, false, entityName);
			}
		} else {
			String packagePath;
			String outputFileName;
			try {
				outputFileName = file.getName();
				// 获取模板
				String tPath = file.getAbsolutePath().replace(
						this.templatePath, "");

				Template template = Velocity.getTemplate(tPath, "UTF-8");

				if (file.getName().equalsIgnoreCase("ContextListener.java")) {
					packagePath = target.getPath() + File.separator
							+ this.packageNamePath + File.separator
							+ "listener" + File.separator;
					checkDirExist(packagePath);
				} else if (file.getName().equalsIgnoreCase("Simple.java")
						|| file.getName().equalsIgnoreCase("WithVelocity.java")) {
					if (this.useJPA)
						return;
					packagePath = target.getPath() + File.separator
							+ this.packageNamePath + File.separator
							+ "resource" + File.separator;
					checkDirExist(packagePath);

					outputFileName = entityName + outputFileName;

				} else if (file.getName().equalsIgnoreCase("Entity.java")) {
					if (!this.useJPA)
						return;
					packagePath = target.getPath() + File.separator
							+ this.packageNamePath + File.separator + "entity"
							+ File.separator;
					checkDirExist(packagePath);
					outputFileName = entityName + ".java";
				} else if (file.getName().equalsIgnoreCase("Resource.java")) {
					if (!this.useJPA)
						return;
					packagePath = target.getPath() + File.separator
							+ this.packageNamePath + File.separator
							+ "resource" + File.separator;
					checkDirExist(packagePath);
					outputFileName = entityName + outputFileName;
				} else if (file.getName().equalsIgnoreCase("Service.java")
						|| file.getName().equalsIgnoreCase("ServiceTest.java")) {
					if (!this.useJPA)
						return;
					packagePath = target.getPath() + File.separator
							+ this.packageNamePath + File.separator + "service"
							+ File.separator;
					checkDirExist(packagePath);
					outputFileName = entityName + outputFileName;
				} else if (file.getName().equalsIgnoreCase("ServiceBean.java")) {
					if (!this.useJPA)
						return;
					packagePath = target.getPath() + File.separator
							+ this.packageNamePath + File.separator + "service"
							+ File.separator + "impl" + File.separator;
					checkDirExist(packagePath);
					outputFileName = entityName + outputFileName;
				} else
					packagePath = target.getPath() + File.separator;

				if (file.getName().equalsIgnoreCase("velocity.vm")) {
					outputFileName = entityName + ".vm";
				}

				// 输出到用户端
				OutputStreamWriter fileWriter = new OutputStreamWriter(
						new FileOutputStream(packagePath + outputFileName),
						"UTF-8");
				template.merge(context, fileWriter);
				fileWriter.flush();
				fileWriter.close();
			} catch (Exception e) {
			}
		}
	}

	private void checkDirExist(String parentPath) {
		File dir = new File(parentPath);
		if (!dir.exists())
			dir.mkdirs();
	}

	private void initVelocity() {
		Velocity.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH,
				this.templatePath);
		Velocity.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS,
				"org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
		try {
			Velocity.init();
		} catch (Exception e) {
			throw new RuntimeException("初始化Velocity引擎失败", e);
		}
	}

	public static void main(String[] args) {
		String targetPath = null, codeGenerateType = null, projectName = null, packageName = null, entityName = null;

		System.out.println("欢迎使用JRest4Guice代码生成工具\n");
		System.out.println("可用命令");
		System.out.println("===========================");
		System.out.println("  1     创建helloworld的Web工程");
		System.out.println("  2     创建带JPA实现的Web工程");
		System.out.println("  3     创建新资源");
		System.out.println("  q     退出");
		System.out.println("===========================");
		System.out.print("\n请选择输入你的操作命令:(1) ");
		codeGenerateType = readUserInput();
		if (codeGenerateType.equals(""))
			codeGenerateType = "1";
		System.out.println(codeGenerateType);
		
		if (codeGenerateType.equals("1") || codeGenerateType.equals("2")) {
			System.out.print("\n请输入你要创建的项目名称:(helloWorld) ");
			projectName = readUserInput();
			if (projectName.equals(""))
				projectName = "helloWorld";
			System.out.println(projectName);
		} else {
			System.out.print("\n请输入你要创建的资源名称:(User) ");
			entityName = readUserInput();
			if (entityName.equals(""))
				entityName = "User";
			System.out.println(entityName);
		}

		if (codeGenerateType.equals("1") || codeGenerateType.equals("2"))
			System.out.print("\n请输入代码生成的目标路径:(Generators) ");
		else
			System.out.print("\n请输入目标项目的根路径:(Generators) ");
			
		targetPath = readUserInput();
		if (targetPath.equals(""))
			targetPath = "Generators";
		System.out.println(targetPath);

		System.out.print("\n请输入包的名称:(org.cnoss.rs) ");
		packageName = readUserInput();
		if (packageName.equals(""))
			packageName = "org.cnoss.rs";
		System.out.println(packageName);

		File targetPathFile = new File(targetPath);
		if (!targetPathFile.exists())
			targetPathFile.mkdirs();

		CodeGenerator generator = new CodeGenerator();
		if (codeGenerateType.equals("1")) {
			generator.createSimpleProject(targetPath, projectName, packageName);
		} else if (codeGenerateType.equals("2")) {
			generator.createJPAProject(targetPath, projectName, packageName);
		} else if (codeGenerateType.equals("3")) {
			generator.createResource(targetPath, packageName, entityName);
		}
		
		System.out.println("\n\n代码生成成功,请查看 "+targetPath+" 目录");
	}

	private static String readUserInput() {
		String value = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			value = br.readLine().trim();
			if (value.equalsIgnoreCase("q")) {
				System.exit(-1);
			}
		} catch (IOException e) {
		}
		return value;
	}
}
