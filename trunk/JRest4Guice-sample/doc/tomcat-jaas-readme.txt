启用Tomcat的JAAS:
  1 在Tomcat的lib目录放入以下依赖包
    JRest4Guice/dist/Jrest4Guice-client.jar
    JRest4Guice/dist/Jrest4Guice-security.jar
    JRest4Guice-sample/dist/Jrest4Guice-security-loginModule.jar
    
    commons-httpclient以及其相关的依赖包
    
  2 在启动参数中添加以下段
	-Djava.security.auth.login.config="xxxx\jaas.config"
	
  3 在server.xml中添加
  	<Realm
		className="org.apache.catalina.realm.UserDatabaseRealm"
		resourceName="UserDatabase" />
	<Realm className="org.apache.catalina.realm.JAASRealm"
		appName="JRestRealm"
		userClassNames="org.jrest4guice.security.User"
		roleClassNames="org.jrest4guice.security.Role" />
  