启用Tomcat的JAAS:
  1 在Tomcat的lib目录放入以下依赖包
    JRest4Guice/target/Jrest4Guice-client-xxx.jar
    JRest4Guice/target/Jrest4Guice-security-xxx.jar
    JRest4Guice-sample/target/Jrest4Guice-sample-security-loginModule.jar
    
    commons-beanutils-xxx.jar
    commons-codec-xxx.jar
    commons-collections-xxx.jar
    commons-httpclient-xxx.jar
    commons-io-xxx.jar
    commons-lang-xxx.jar
    commons-logging-xxx.jar
    
  2 在启动参数中添加以下段
	-Djava.security.auth.login.config="xxx\jaas.config"
	
  3 在server.xml中添加
  	<Realm
		className="org.apache.catalina.realm.UserDatabaseRealm"
		resourceName="UserDatabase" />
	<Realm className="org.apache.catalina.realm.JAASRealm"
		appName="JRestRealm"
		userClassNames="org.jrest4guice.security.User"
		roleClassNames="org.jrest4guice.security.Role" />
  