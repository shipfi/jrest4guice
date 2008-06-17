package org.jrest4guice.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * 代码生成器
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

		// 往上下文中填入数据
		this.context = new VelocityContext();
		Map<String, String> contextMap = new HashMap<String, String>();
		contextMap.put("packageName", packageName);
		contextMap.put("entityName", entityName);
		context.put("context", contextMap);

		File target = new File(targetPath);
		File file = new File(this.templatePath + File.separator
				+ ("src.main.java.Service").replace(".", File.separator)
				+ ".java");
		this.generateFile(target, file, false, entityName);

		file = new File(this.templatePath + File.separator
				+ ("src.main.java.ServiceBean").replace(".", File.separator)
				+ ".java");
		this.generateFile(target, file, false, entityName);

		file = new File(this.templatePath + File.separator
				+ ("src.main.java.Entity").replace(".", File.separator)
				+ ".java");
		this.generateFile(target, file, false, entityName);

		file = new File(this.templatePath + File.separator
				+ ("src.main.java.Resource").replace(".", File.separator)
				+ ".java");
		this.generateFile(target, file, false, entityName);
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
			if(file.getName().equalsIgnoreCase(".svn"))
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
				} else if (file.getName().equalsIgnoreCase("Service.java") ||
						file.getName().equalsIgnoreCase("ServiceTest.java")) {
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
		try {
			Velocity.init();
		} catch (Exception e) {
			throw new RuntimeException("初始化Velocity引擎失败", e);
		}
	}

	public static void main(String[] args) {
		CodeGenerator generator = new CodeGenerator();
		generator.createJPAProject("F:\\Temp\\Generators", "user",
				"org.cnoss.rs.user");
	}
}
