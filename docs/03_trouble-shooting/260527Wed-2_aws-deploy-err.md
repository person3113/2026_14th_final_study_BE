> ubuntu@ip-172-31-32-238:~$ ls
final-project  get-docker.sh
ubuntu@ip-172-31-32-238:~$ cd final-project
ubuntu@ip-172-31-32-238:~/final-project$ ls
docker-compose.yml
ubuntu@ip-172-31-32-238:~/final-project$ docker ps
CONTAINER ID   IMAGE                                                COMMAND                  CREATED              STATUS
PORTS                                                    NAMES
293ec56f142a   ghcr.io/person3113/2026_14th_final_study_be:latest   "java -jar -Dspring.…"   About a minute ago   Up 4 seconds
0.0.0.0:8080->8080/tcp, [::]:8080->8080/tcp              final-app
a4859cea14d4   mysql:8.0                                            "docker-entrypoint.s…"   23 minutes ago       Up 14 seconds
(health: starting)   0.0.0.0:3306->3306/tcp, [::]:3306->3306/tcp, 33060/tcp   final-db
ubuntu@ip-172-31-32-238:~/final-project$ docker compose down -v
WARN[0000] /home/ubuntu/final-project/docker-compose.yml: the attribute `version` is obsolete, it will be ignored, please remove i
to avoid potential confusion
[+] down 4/4
✔ Container final-app             Removed                                                     14.0s
✔ Container final-db              Removed                                                      0.9s
✔ Volume final-project_mysql_data Removed                                                      0.0s
✔ Network final-project_default   Removed                                                      0.1s
ubuntu@ip-172-31-32-238:~/final-project$ docker ps
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
ubuntu@ip-172-31-32-238:~/final-project$ ls
docker-compose.yml
ubuntu@ip-172-31-32-238:~/final-project$ docker ps
CONTAINER ID   IMAGE                                                COMMAND                  CREATED              STATUS
PORTS                                                    NAMES
ae44f4fd564c   ghcr.io/person3113/2026_14th_final_study_be:latest   "java -jar -Dspring.…"   About a minute ago   Up 10 seconds
0.0.0.0:8080->8080/tcp, [::]:8080->8080/tcp              final-app
5c7178d33c03   mysql:8.0                                            "docker-entrypoint.s…"   About a minute ago   Up 9 seconds
(healthy)   0.0.0.0:3306->3306/tcp, [::]:3306->3306/tcp, 33060/tcp   final-db
ubuntu@ip-172-31-32-238:~/final-project$ docker logs final-app -f

    .   ____          _            __ _ _
/\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
\\/  ___)| |_)| | | | | || (_| |  ) ) ) )
'  |____| .__|_| |_|_| |_\__, | / / / /
=========|_|==============|___/=/_/_/_/

:: Spring Boot ::                (v4.0.6)

2026-05-27 10:11:38.175 [main] INFO  com.example.demo.FinalApplication - Starting FinalApplication v0.0.1-SNAPSHOT using Java 25.0.3
with PID 1 (/app/app.jar started by root in /app)
2026-05-27 10:11:38.180 [main] INFO  com.example.demo.FinalApplication - The following 1 profile is active: "prod"
2026-05-27 10:11:40.042 [background-preinit] INFO  o.h.validator.internal.util.Version - HV000001: Hibernate Validator 9.0.1.Final
2026-05-27 10:11:42.628 [main] INFO  o.s.d.r.c.RepositoryConfigurationDelegate - Bootstrapping Spring Data JPA repositories in
DEFAULT mode.
2026-05-27 10:11:42.783 [main] INFO  o.s.d.r.c.RepositoryConfigurationDelegate - Finished Spring Data repository scanning in 111 ms.
Found 2 JPA repository interfaces.
2026-05-27 10:11:45.025 [main] INFO  o.s.boot.tomcat.TomcatWebServer - Tomcat initialized with port 8080 (http)
2026-05-27 10:11:45.083 [main] INFO  o.a.coyote.http11.Http11NioProtocol - Initializing ProtocolHandler ["http-nio-8080"]
2026-05-27 10:11:45.091 [main] INFO  o.a.catalina.core.StandardService - Starting service [Tomcat]
2026-05-27 10:11:45.092 [main] INFO  o.a.catalina.core.StandardEngine - Starting Servlet engine: [Apache Tomcat/11.0.21]
2026-05-27 10:11:45.172 [main] INFO  o.s.b.w.c.s.WebApplicationContextInitializer - Root WebApplicationContext: initialization
completed in 6792 ms
2026-05-27 10:11:46.072 [main] INFO  org.hibernate.orm.jpa - HHH008540: Processing PersistenceUnitInfo [name: default]
2026-05-27 10:11:46.286 [main] INFO  org.hibernate.orm.core - HHH000001: Hibernate ORM core version 7.2.12.Final
2026-05-27 10:11:48.384 [main] INFO  o.s.o.j.p.SpringPersistenceUnitInfo - No LoadTimeWeaver setup: ignoring JPA class transformer
2026-05-27 10:11:48.570 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Starting...
2026-05-27 10:11:51.518 [main] INFO  com.zaxxer.hikari.pool.HikariPool - HikariPool-1 - Added connection
com.mysql.cj.jdbc.ConnectionImpl@6a116354
2026-05-27 10:11:51.555 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Start completed.
2026-05-27 10:12:04.085 [main] INFO  o.hibernate.orm.connections.pooling - HHH10001005: Database info:
Database JDBC URL [jdbc:mysql://db:3306/final?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-
8&serverTimezone=Asia/Seoul]
Database driver: MySQL Connector/J
Database dialect: MySQLDialect
Database version: 8.0.46
Default catalog/schema: final/undefined
Autocommit mode: undefined/unknown
Isolation level: REPEATABLE_READ [default REPEATABLE_READ]
JDBC fetch size: none
Pool: DataSourceConnectionProvider
Minimum pool size: undefined/unknown
Maximum pool size: undefined/unknown
2026-05-27 10:12:06.805 [main] INFO  org.hibernate.orm.core - HHH000489: No JTA platform available (set
'hibernate.transaction.jta.platform' to enable JTA platform integration)
2026-05-27 10:12:06.994 [main] WARN  com.zaxxer.hikari.pool.PoolBase - HikariPool-1 - Failed to validate connection
com.mysql.cj.jdbc.ConnectionImpl@6a116354 (No operations allowed after connection closed.). Possibly consider using a shorter
maxLifetime value.
2026-05-27 10:12:07.010 [main] WARN  com.zaxxer.hikari.pool.PoolBase - HikariPool-1 - Failed to validate connection
com.mysql.cj.jdbc.ConnectionImpl@46067a74 (No operations allowed after connection closed.). Possibly consider using a shorter
maxLifetime value.
2026-05-27 10:12:07.014 [main] WARN  com.zaxxer.hikari.pool.PoolBase - HikariPool-1 - Failed to validate connection
com.mysql.cj.jdbc.ConnectionImpl@2d26c6a2 (No operations allowed after connection closed.). Possibly consider using a shorter
maxLifetime value.
2026-05-27 10:12:07.016 [main] WARN  com.zaxxer.hikari.pool.PoolBase - HikariPool-1 - Failed to validate connection
com.mysql.cj.jdbc.ConnectionImpl@362e6fe (No operations allowed after connection closed.). Possibly consider using a shorter
maxLifetime value.
2026-05-27 10:12:37.012 [main] WARN  org.hibernate.orm.jdbc.error - HHH000247: ErrorCode: 0, SQLState: 08S01
2026-05-27 10:12:37.013 [main] WARN  org.hibernate.orm.jdbc.error - HikariPool-1 - Connection is not available, request timed out
after 30016ms (total=0, active=0, idle=0, waiting=0)
2026-05-27 10:12:37.014 [main] WARN  org.hibernate.orm.jdbc.error - HHH000247: ErrorCode: 0, SQLState: 08S01
2026-05-27 10:12:37.014 [main] WARN  org.hibernate.orm.jdbc.error - HikariPool-1 - Connection is not available, request timed out
after 30016ms (total=0, active=0, idle=0, waiting=0)
2026-05-27 10:12:37.032 [main] ERROR o.s.o.j.LocalContainerEntityManagerFactoryBean - Failed to initialize JPA EntityManagerFactory:
Unable to build Hibernate SessionFactory  [persistence unit: default] ; nested exception is
org.hibernate.exception.JDBCConnectionException: Unable to open JDBC Connection for DDL execution [HikariPool-1 - Connection is no
available, request timed out after 30016ms (total=0, active=0, idle=0, waiting=0)] [n/a]
2026-05-27 10:12:37.034 [main] WARN  o.s.b.w.s.s.c.AnnotationConfigServletWebServerApplicationContext - Exception encountered during
context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean
with name 'entityManagerFactory' defined in class path resource
[org/springframework/boot/hibernate/autoconfigure/HibernateJpaConfiguration.class]: Unable to build Hibernate SessionFactory
[persistence unit: default] ; nested exception is org.hibernate.exception.JDBCConnectionException: Unable to open JDBC Connection
for DDL execution [HikariPool-1 - Connection is not available, request timed out after 30016ms (total=0, active=0, idle=0,
waiting=0)] [n/a]
2026-05-27 10:12:37.035 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Shutdown initiated...
2026-05-27 10:12:40.800 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Shutdown completed.
2026-05-27 10:12:40.838 [main] INFO  o.a.catalina.core.StandardService - Stopping service [Tomcat]
2026-05-27 10:12:40.889 [main] INFO  o.s.b.a.l.ConditionEvaluationReportLogger -

Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
2026-05-27 10:12:40.945 [main] ERROR o.s.boot.SpringApplication - Application run failed
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'entityManagerFactory' defined in class pat
resource [org/springframework/boot/hibernate/autoconfigure/HibernateJpaConfiguration.class]: Unable to build Hibernate
SessionFactory  [persistence unit: default] ; nested exception is org.hibernate.exception.JDBCConnectionException: Unable to open
JDBC Connection for DDL execution [HikariPool-1 - Connection is not available, request timed out after 30016ms (total=0, active=0,
idle=0, waiting=0)] [n/a]
at
org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:
1817)
at
org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:60
3)
     at
org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:525)
at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:333)
at
org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:371)
at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:331)
at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:201)
at
org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:977
at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:621)
at
org.springframework.boot.web.server.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.ja
va:143)
at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:756)
at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:445)
at org.springframework.boot.SpringApplication.run(SpringApplication.java:321)
at org.springframework.boot.SpringApplication.run(SpringApplication.java:1365)
at org.springframework.boot.SpringApplication.run(SpringApplication.java:1354)
at com.example.demo.FinalApplication.main(FinalApplication.java:10)
at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(Unknown Source)
at java.base/java.lang.reflect.Method.invoke(Unknown Source)
at org.springframework.boot.loader.launch.Launcher.launch(Launcher.java:106)
at org.springframework.boot.loader.launch.Launcher.launch(Launcher.java:64)
at org.springframework.boot.loader.launch.JarLauncher.main(JarLauncher.java:40)
Caused by: jakarta.persistence.PersistenceException: Unable to build Hibernate SessionFactory  [persistence unit: default] ; neste
exception is org.hibernate.exception.JDBCConnectionException: Unable to open JDBC Connection for DDL execution [HikariPool-1 -
Connection is not available, request timed out after 30016ms (total=0, active=0, idle=0, waiting=0)] [n/a]
at
org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(AbstractEntityManagerFactoryBean.java:4
48)
    at
org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.afterPropertiesSet(AbstractEntityManagerFactoryBean.java:411)
at
org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.afterPropertiesSet(LocalContainerEntityManagerFactoryBean.java:41
9)
     at
org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.ja
va:1864)
at
org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:
1813)
... 20 common frames omitted
Caused by: org.hibernate.exception.JDBCConnectionException: Unable to open JDBC Connection for DDL execution [HikariPool-1 -
Connection is not available, request timed out after 30016ms (total=0, active=0, idle=0, waiting=0)] [n/a]
at org.hibernate.exception.internal.SQLExceptionTypeDelegate.convert(SQLExceptionTypeDelegate.java:49)
at org.hibernate.exception.internal.StandardSQLExceptionConverter.convert(StandardSQLExceptionConverter.java:34)
at org.hibernate.engine.jdbc.spi.SqlExceptionHelper.convert(SqlExceptionHelper.java:115)
at org.hibernate.engine.jdbc.spi.SqlExceptionHelper.convert(SqlExceptionHelper.java:101)
at
org.hibernate.resource.transaction.backend.jdbc.internal.DdlTransactionIsolatorNonJtaImpl.getIsolatedConnection(DdlTransactionIsolat
orNonJtaImpl.java:71)
at
org.hibernate.resource.transaction.backend.jdbc.internal.DdlTransactionIsolatorNonJtaImpl.getIsolatedConnection(DdlTransactionIsolat
orNonJtaImpl.java:37)
at
org.hibernate.tool.schema.internal.exec.ImprovedExtractionContextImpl.getJdbcConnection(ImprovedExtractionContextImpl.java:61)
at
org.hibernate.tool.schema.internal.exec.ImprovedExtractionContextImpl.getJdbcDatabaseMetaData(ImprovedExtractionContextImpl.java:68)
at
org.hibernate.tool.schema.extract.internal.InformationExtractorJdbcDatabaseMetaDataImpl.getJdbcDatabaseMetaData(InformationExtractor
JdbcDatabaseMetaDataImpl.java:38)
at
org.hibernate.tool.schema.extract.internal.InformationExtractorJdbcDatabaseMetaDataImpl.processTableResultSet(InformationExtractorJd
bcDatabaseMetaDataImpl.java:76)
at
org.hibernate.tool.schema.extract.internal.AbstractInformationExtractorImpl.getTables(AbstractInformationExtractorImpl.java:496)
at org.hibernate.tool.schema.extract.internal.DatabaseInformationImpl.getTablesInformation(DatabaseInformationImpl.java:124)
at org.hibernate.tool.schema.internal.GroupedSchemaMigratorImpl.performTablesMigration(GroupedSchemaMigratorImpl.java:72)
at org.hibernate.tool.schema.internal.AbstractSchemaMigrator.performMigration(AbstractSchemaMigrator.java:220)
at org.hibernate.tool.schema.internal.AbstractSchemaMigrator.doMigration(AbstractSchemaMigrator.java:96)
at
org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.performDatabaseAction(SchemaManagementToolCoordinator.java:269)
at org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.lambda$process$1(SchemaManagementToolCoordinator.java:101
at java.base/java.util.HashMap.forEach(Unknown Source)
at org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.process(SchemaManagementToolCoordinator.java:100)
at
org.hibernate.boot.internal.SessionFactoryObserverForSchemaExport.sessionFactoryCreated(SessionFactoryObserverForSchemaExport.java:3
5)
     at org.hibernate.internal.SessionFactoryObserverChain.sessionFactoryCreated(SessionFactoryObserverChain.java:33)
     at org.hibernate.internal.SessionFactoryImpl.<init>(SessionFactoryImpl.java:323)
     at org.hibernate.internal.SessionFactoryRegistry.instantiateSessionFactory(SessionFactoryRegistry.java:64)
     at org.hibernate.boot.internal.SessionFactoryBuilderImpl.build(SessionFactoryBuilderImpl.java:437)
     at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.build(EntityManagerFactoryBuilderImpl.java:1456)
     at
org.springframework.orm.jpa.vendor.SpringHibernateJpaPersistenceProvider.createContainerEntityManagerFactory(SpringHibernateJpaPersi
stenceProvider.java:93)
at
org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.createNativeEntityManagerFactory(LocalContainerEntityManagerFacto
ryBean.java:443)
at
org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(AbstractEntityManagerFactoryBean.java:4
36)
    ... 24 common frames omitted
Caused by: java.sql.SQLTransientConnectionException: HikariPool-1 - Connection is not available, request timed out after 30016ms
(total=0, active=0, idle=0, waiting=0)
at com.zaxxer.hikari.pool.HikariPool.createTimeoutException(HikariPool.java:714)
at com.zaxxer.hikari.pool.HikariPool.getConnection(HikariPool.java:184)
at com.zaxxer.hikari.pool.HikariPool.getConnection(HikariPool.java:142)
at com.zaxxer.hikari.HikariDataSource.getConnection(HikariDataSource.java:127)
at
org.hibernate.engine.jdbc.connections.internal.DataSourceConnectionProvider.getConnection(DataSourceConnectionProvider.java:137)
at
org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator$ConnectionProviderJdbcConnectionAccess.obtainConnection(JdbcEnvironm
entInitiator.java:508)
at
org.hibernate.resource.transaction.backend.jdbc.internal.DdlTransactionIsolatorNonJtaImpl.getIsolatedConnection(DdlTransactionIsolat
orNonJtaImpl.java:44)
... 47 common frames omitted
Caused by: com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
at com.mysql.cj.jdbc.exceptions.SQLError.createCommunicationsException(SQLError.java:165)
at com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:55)
at com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:840)
at com.mysql.cj.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:416)
at com.mysql.cj.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:237)
at com.mysql.cj.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:180)
at com.zaxxer.hikari.util.DriverDataSource.getConnection(DriverDataSource.java:144)
at com.zaxxer.hikari.pool.PoolBase.newConnection(PoolBase.java:373)
at com.zaxxer.hikari.pool.PoolBase.newPoolEntry(PoolBase.java:210)
at com.zaxxer.hikari.pool.HikariPool.createPoolEntry(HikariPool.java:488)
at com.zaxxer.hikari.pool.HikariPool$PoolEntryCreator.call(HikariPool.java:752)
at com.zaxxer.hikari.pool.HikariPool$PoolEntryCreator.call(HikariPool.java:731)
at java.base/java.util.concurrent.FutureTask.run(Unknown Source)
at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(Unknown Source)
at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(Unknown Source)
at java.base/java.lang.Thread.run(Unknown Source)
Caused by: com.mysql.cj.exceptions.CJCommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(Unknown Source)
at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Unknown Source)
at java.base/java.lang.reflect.Constructor.newInstance(Unknown Source)
at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:52)
at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:95)
at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:140)
at com.mysql.cj.exceptions.ExceptionFactory.createCommunicationsException(ExceptionFactory.java:156)
at com.mysql.cj.protocol.a.NativeSocketConnection.connect(NativeSocketConnection.java:79)
at com.mysql.cj.NativeSession.connect(NativeSession.java:142)
at com.mysql.cj.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:964)
at com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:828)
... 13 common frames omitted
Caused by: java.net.ConnectException: Connection refused
at java.base/sun.nio.ch.Net.pollConnect(Native Method)
at java.base/sun.nio.ch.Net.pollConnectNow(Unknown Source)
at java.base/sun.nio.ch.NioSocketImpl.timedFinishConnect(Unknown Source)
at java.base/sun.nio.ch.NioSocketImpl.connect(Unknown Source)
at java.base/java.net.SocksSocketImpl.connect(Unknown Source)
at java.base/java.net.Socket.connect(Unknown Source)
at com.mysql.cj.protocol.StandardSocketFactory.connect(StandardSocketFactory.java:144)
at com.mysql.cj.protocol.a.NativeSocketConnection.connect(NativeSocketConnection.java:53)
... 16 common frames omitted
.   ____          _            __ _ _
/\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
\\/  ___)| |_)| | | | | || (_| |  ) ) ) )
'  |____| .__|_| |_|_| |_\__, | / / / /
=========|_|==============|___/=/_/_/_/

:: Spring Boot ::                (v4.0.6)

2026-05-27 10:13:34.199 [main] INFO  com.example.demo.FinalApplication - Starting FinalApplication v0.0.1-SNAPSHOT using Java 25.0.3
with PID 1 (/app/app.jar started by root in /app)
2026-05-27 10:13:34.211 [main] INFO  com.example.demo.FinalApplication - The following 1 profile is active: "prod"
2026-05-27 10:13:35.953 [background-preinit] INFO  o.h.validator.internal.util.Version - HV000001: Hibernate Validator 9.0.1.Final
2026-05-27 10:13:37.775 [main] INFO  o.s.d.r.c.RepositoryConfigurationDelegate - Bootstrapping Spring Data JPA repositories in
DEFAULT mode.
2026-05-27 10:13:37.929 [main] INFO  o.s.d.r.c.RepositoryConfigurationDelegate - Finished Spring Data repository scanning in 114 ms.
Found 2 JPA repository interfaces.
2026-05-27 10:13:40.945 [main] INFO  o.s.boot.tomcat.TomcatWebServer - Tomcat initialized with port 8080 (http)
2026-05-27 10:13:40.997 [main] INFO  o.a.coyote.http11.Http11NioProtocol - Initializing ProtocolHandler ["http-nio-8080"]
2026-05-27 10:13:41.005 [main] INFO  o.a.catalina.core.StandardService - Starting service [Tomcat]
2026-05-27 10:13:41.006 [main] INFO  o.a.catalina.core.StandardEngine - Starting Servlet engine: [Apache Tomcat/11.0.21]
2026-05-27 10:13:41.206 [main] INFO  o.s.b.w.c.s.WebApplicationContextInitializer - Root WebApplicationContext: initialization
completed in 6814 ms
2026-05-27 10:13:42.369 [main] INFO  org.hibernate.orm.jpa - HHH008540: Processing PersistenceUnitInfo [name: default]
2026-05-27 10:13:42.676 [main] INFO  org.hibernate.orm.core - HHH000001: Hibernate ORM core version 7.2.12.Final
2026-05-27 10:14:06.061 [main] INFO  o.s.o.j.p.SpringPersistenceUnitInfo - No LoadTimeWeaver setup: ignoring JPA class transformer
2026-05-27 10:14:06.657 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Starting...
q2026-05-27 10:14:38.096 [main] WARN  org.hibernate.orm.jdbc.error - HHH000247: ErrorCode: 0, SQLState: 08S01
2026-05-27 10:14:38.098 [main] WARN  org.hibernate.orm.jdbc.error - Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
2026-05-27 10:14:38.102 [main] WARN  org.hibernate.orm.jdbc - HHH100046: Could not obtain connection to query JDBC database metadata
org.hibernate.exception.JDBCConnectionException: Unable to obtain isolated JDBC connection [Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.]
[n/a]
at org.hibernate.exception.internal.SQLStateConversionDelegate.convert(SQLStateConversionDelegate.java:80)
at org.hibernate.exception.internal.StandardSQLExceptionConverter.convert(StandardSQLExceptionConverter.java:34)
at org.hibernate.engine.jdbc.spi.SqlExceptionHelper.convert(SqlExceptionHelper.java:115)
at org.hibernate.engine.jdbc.spi.SqlExceptionHelper.convert(SqlExceptionHelper.java:101)
at
org.hibernate.resource.transaction.backend.jdbc.internal.JdbcIsolationDelegate.delegateWork(JdbcIsolationDelegate.java:51)
at
org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.getJdbcEnvironmentUsingJdbcMetadata(JdbcEnvironmentInitiator.java:36
6)
     at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.getJdbcEnvironment(JdbcEnvironmentInitiator.java:143)
     at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentInitiator.java:120)
     at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentInitiator.java:80)
     at org.hibernate.boot.registry.internal.StandardServiceRegistryImpl.initiateService(StandardServiceRegistryImpl.java:133)
     at org.hibernate.service.internal.AbstractServiceRegistryImpl.createService(AbstractServiceRegistryImpl.java:260)
     at org.hibernate.service.internal.AbstractServiceRegistryImpl.initializeService(AbstractServiceRegistryImpl.java:235)
     at org.hibernate.service.internal.AbstractServiceRegistryImpl.getService(AbstractServiceRegistryImpl.java:212)
     at org.hibernate.boot.model.relational.Database.<init>(Database.java:44)
     at org.hibernate.boot.internal.InFlightMetadataCollectorImpl.getDatabase(InFlightMetadataCollectorImpl.java:251)
     at org.hibernate.boot.internal.InFlightMetadataCollectorImpl.<init>(InFlightMetadataCollectorImpl.java:203)
     at org.hibernate.boot.model.process.spi.MetadataBuildingProcess.complete(MetadataBuildingProcess.java:172)
     at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.metadata(EntityManagerFactoryBuilderImpl.java:1392)
     at
org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.populateSessionFactoryBuilder(EntityManagerFactoryBuilderImpl.java:1
472)
at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.build(EntityManagerFactoryBuilderImpl.java:1454)
at
org.springframework.orm.jpa.vendor.SpringHibernateJpaPersistenceProvider.createContainerEntityManagerFactory(SpringHibernateJpaPersi
stenceProvider.java:93)
at
org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.createNativeEntityManagerFactory(LocalContainerEntityManagerFacto
ryBean.java:443)
at
org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(AbstractEntityManagerFactoryBean.java:4
36)
    at
org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.afterPropertiesSet(AbstractEntityManagerFactoryBean.java:411)
at
org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.afterPropertiesSet(LocalContainerEntityManagerFactoryBean.java:41
9)
     at
org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.ja
va:1864)
at
org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:
1813)
at
org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:60
3)
     at
org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:525)
at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:333)
at
org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:371)
at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:331)
at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:201)
at
org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:977
at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:621)
at
org.springframework.boot.web.server.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.ja
va:143)
at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:756)
at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:445)
at org.springframework.boot.SpringApplication.run(SpringApplication.java:321)
at org.springframework.boot.SpringApplication.run(SpringApplication.java:1365)
at org.springframework.boot.SpringApplication.run(SpringApplication.java:1354)
at com.example.demo.FinalApplication.main(FinalApplication.java:10)
at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(Unknown Source)
at java.base/java.lang.reflect.Method.invoke(Unknown Source)
at org.springframework.boot.loader.launch.Launcher.launch(Launcher.java:106)
at org.springframework.boot.loader.launch.Launcher.launch(Launcher.java:64)
at org.springframework.boot.loader.launch.JarLauncher.main(JarLauncher.java:40)
Caused by: com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
at com.mysql.cj.jdbc.exceptions.SQLError.createCommunicationsException(SQLError.java:165)
at com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:55)
at com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:840)
at com.mysql.cj.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:416)
at com.mysql.cj.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:237)
at com.mysql.cj.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:180)
at com.zaxxer.hikari.util.DriverDataSource.getConnection(DriverDataSource.java:144)
at com.zaxxer.hikari.pool.PoolBase.newConnection(PoolBase.java:373)
at com.zaxxer.hikari.pool.PoolBase.newPoolEntry(PoolBase.java:210)
at com.zaxxer.hikari.pool.HikariPool.createPoolEntry(HikariPool.java:488)
at com.zaxxer.hikari.pool.HikariPool.checkFailFast(HikariPool.java:576)
at com.zaxxer.hikari.pool.HikariPool.<init>(HikariPool.java:97)
at com.zaxxer.hikari.HikariDataSource.getConnection(HikariDataSource.java:111)
at
org.hibernate.engine.jdbc.connections.internal.DataSourceConnectionProvider.getConnection(DataSourceConnectionProvider.java:137)
at
org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator$ConnectionProviderJdbcConnectionAccess.obtainConnection(JdbcEnvironm
entInitiator.java:508)
at
org.hibernate.resource.transaction.backend.jdbc.internal.JdbcIsolationDelegate.delegateWork(JdbcIsolationDelegate.java:48)
... 42 common frames omitted
Caused by: com.mysql.cj.exceptions.CJCommunicationsException: Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(Unknown Source)
at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Unknown Source)
at java.base/java.lang.reflect.Constructor.newInstance(Unknown Source)
at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:52)
at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:95)
at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:140)
at com.mysql.cj.exceptions.ExceptionFactory.createCommunicationsException(ExceptionFactory.java:156)
at com.mysql.cj.protocol.a.NativeSocketConnection.connect(NativeSocketConnection.java:79)
at com.mysql.cj.NativeSession.connect(NativeSession.java:142)
at com.mysql.cj.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:964)
at com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:828)
... 55 common frames omitted
Caused by: java.net.ConnectException: Connection refused
at java.base/sun.nio.ch.Net.pollConnect(Native Method)
at java.base/sun.nio.ch.Net.pollConnectNow(Unknown Source)
at java.base/sun.nio.ch.NioSocketImpl.timedFinishConnect(Unknown Source)
at java.base/sun.nio.ch.NioSocketImpl.connect(Unknown Source)
at java.base/java.net.SocksSocketImpl.connect(Unknown Source)
at java.base/java.net.Socket.connect(Unknown Source)
at com.mysql.cj.protocol.StandardSocketFactory.connect(StandardSocketFactory.java:144)
at com.mysql.cj.protocol.a.NativeSocketConnection.connect(NativeSocketConnection.java:53)
... 58 common frames omitted
2026-05-27 10:14:38.115 [main] ERROR o.s.o.j.LocalContainerEntityManagerFactoryBean - Failed to initialize JPA EntityManagerFactory:
Unable to create requested service [org.hibernate.engine.jdbc.env.spi.JdbcEnvironment] due to: Unable to determine Dialect without
JDBC metadata (please set 'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect' when a custom Dialect
implementation must be provided)
2026-05-27 10:14:38.116 [main] WARN  o.s.b.w.s.s.c.AnnotationConfigServletWebServerApplicationContext - Exception encountered during
context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean
with name 'entityManagerFactory' defined in class path resource
[org/springframework/boot/hibernate/autoconfigure/HibernateJpaConfiguration.class]: Unable to create requested service
[org.hibernate.engine.jdbc.env.spi.JdbcEnvironment] due to: Unable to determine Dialect without JDBC metadata (please set
'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect' when a custom Dialect implementation must be provided)
2026-05-27 10:14:38.127 [main] INFO  o.a.catalina.core.StandardService - Stopping service [Tomcat]
2026-05-27 10:14:38.155 [main] INFO  o.s.b.a.l.ConditionEvaluationReportLogger -

Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
2026-05-27 10:14:38.203 [main] ERROR o.s.boot.SpringApplication - Application run failed
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'entityManagerFactory' defined in class pat
resource [org/springframework/boot/hibernate/autoconfigure/HibernateJpaConfiguration.class]: Unable to create requested service
[org.hibernate.engine.jdbc.env.spi.JdbcEnvironment] due to: Unable to determine Dialect without JDBC metadata (please set
'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect' when a custom Dialect implementation must be provided)
at
org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:
1817)
at
org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:60
3)
     at
org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:525)
at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:333)
at
org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:371)
at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:331)
at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:201)
at
org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:977
at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:621)
at
org.springframework.boot.web.server.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.ja
va:143)
at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:756)
at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:445)
at org.springframework.boot.SpringApplication.run(SpringApplication.java:321)
at org.springframework.boot.SpringApplication.run(SpringApplication.java:1365)
at org.springframework.boot.SpringApplication.run(SpringApplication.java:1354)
at com.example.demo.FinalApplication.main(FinalApplication.java:10)
at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(Unknown Source)
at java.base/java.lang.reflect.Method.invoke(Unknown Source)
at org.springframework.boot.loader.launch.Launcher.launch(Launcher.java:106)
at org.springframework.boot.loader.launch.Launcher.launch(Launcher.java:64)
at org.springframework.boot.loader.launch.JarLauncher.main(JarLauncher.java:40)
Caused by: org.hibernate.service.spi.ServiceException: Unable to create requested service
[org.hibernate.engine.jdbc.env.spi.JdbcEnvironment] due to: Unable to determine Dialect without JDBC metadata (please set
'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect' when a custom Dialect implementation must be provided)
at org.hibernate.service.internal.AbstractServiceRegistryImpl.createService(AbstractServiceRegistryImpl.java:273)
at org.hibernate.service.internal.AbstractServiceRegistryImpl.initializeService(AbstractServiceRegistryImpl.java:235)
at org.hibernate.service.internal.AbstractServiceRegistryImpl.getService(AbstractServiceRegistryImpl.java:212)
at org.hibernate.boot.model.relational.Database.<init>(Database.java:44)
at org.hibernate.boot.internal.InFlightMetadataCollectorImpl.getDatabase(InFlightMetadataCollectorImpl.java:251)
at org.hibernate.boot.internal.InFlightMetadataCollectorImpl.<init>(InFlightMetadataCollectorImpl.java:203)
at org.hibernate.boot.model.process.spi.MetadataBuildingProcess.complete(MetadataBuildingProcess.java:172)
at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.metadata(EntityManagerFactoryBuilderImpl.java:1392)
at
org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.populateSessionFactoryBuilder(EntityManagerFactoryBuilderImpl.java:1
472)
at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.build(EntityManagerFactoryBuilderImpl.java:1454)
at
org.springframework.orm.jpa.vendor.SpringHibernateJpaPersistenceProvider.createContainerEntityManagerFactory(SpringHibernateJpaPersi
stenceProvider.java:93)
at
org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.createNativeEntityManagerFactory(LocalContainerEntityManagerFacto
ryBean.java:443)
at
org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(AbstractEntityManagerFactoryBean.java:4
36)
    at
org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.afterPropertiesSet(AbstractEntityManagerFactoryBean.java:411)
at
org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.afterPropertiesSet(LocalContainerEntityManagerFactoryBean.java:41
9)
     at
org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.ja
va:1864)
at
org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:
1813)
... 20 common frames omitted
Caused by: org.hibernate.HibernateException: Unable to determine Dialect without JDBC metadata (please set
'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect' when a custom Dialect implementation must be provided)
at org.hibernate.engine.jdbc.dialect.internal.DialectFactoryImpl.determineDialect(DialectFactoryImpl.java:190)
at org.hibernate.engine.jdbc.dialect.internal.DialectFactoryImpl.buildDialect(DialectFactoryImpl.java:86)
at
org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.getJdbcEnvironmentWithDefaults(JdbcEnvironmentInitiator.java:200)
at
org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.getJdbcEnvironmentUsingJdbcMetadata(JdbcEnvironmentInitiator.java:45
2)
     at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.getJdbcEnvironment(JdbcEnvironmentInitiator.java:143)
     at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentInitiator.java:120)
     at org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator.initiateService(JdbcEnvironmentInitiator.java:80)
     at org.hibernate.boot.registry.internal.StandardServiceRegistryImpl.initiateService(StandardServiceRegistryImpl.java:133)
     at org.hibernate.service.internal.AbstractServiceRegistryImpl.createService(AbstractServiceRegistryImpl.java:260)
     ... 36 common frames omitted
ubuntu@ip-172-31-32-238:~/final-project$ docker logs final-db
2026-05-27 10:11:20+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.46-1.el9 started.
2026-05-27 10:11:21+00:00 [Note] [Entrypoint]: Switching to dedicated user 'mysql'
2026-05-27 10:11:21+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.46-1.el9 started.
2026-05-27 10:11:21+00:00 [Note] [Entrypoint]: Initializing database files
2026-05-27T10:11:21.246591Z 0 [Warning] [MY-011068] [Server] The syntax '--skip-host-cache' is deprecated and will be removed in a
future release. Please use SET GLOBAL host_cache_size=0 instead.
2026-05-27T10:11:21.246698Z 0 [System] [MY-013169] [Server] /usr/sbin/mysqld (mysqld 8.0.46) initializing of server in progress as
process 79
2026-05-27T10:11:21.264194Z 1 [System] [MY-013576] [InnoDB] InnoDB initialization has started.
2026-05-27T10:11:21.913645Z 1 [System] [MY-013577] [InnoDB] InnoDB initialization has ended.
2026-05-27T10:11:24.142022Z 6 [Warning] [MY-010453] [Server] root@localhost is created with an empty password ! Please consider
switching off the --initialize-insecure option.
2026-05-27 10:11:28+00:00 [Note] [Entrypoint]: Database files initialized
2026-05-27 10:11:28+00:00 [Note] [Entrypoint]: Starting temporary server
2026-05-27T10:11:28.671493Z 0 [Warning] [MY-011068] [Server] The syntax '--skip-host-cache' is deprecated and will be removed in a
future release. Please use SET GLOBAL host_cache_size=0 instead.
2026-05-27T10:11:28.674230Z 0 [System] [MY-010116] [Server] /usr/sbin/mysqld (mysqld 8.0.46) starting as process 127
2026-05-27T10:11:28.708762Z 1 [System] [MY-013576] [InnoDB] InnoDB initialization has started.
2026-05-27T10:11:29.393257Z 1 [System] [MY-013577] [InnoDB] InnoDB initialization has ended.
2026-05-27T10:11:30.229374Z 0 [Warning] [MY-010068] [Server] CA certificate ca.pem is self signed.
2026-05-27T10:11:30.229460Z 0 [System] [MY-013602] [Server] Channel mysql_main configured to support TLS. Encrypted connections ar
now supported for this channel.
2026-05-27T10:11:30.238469Z 0 [Warning] [MY-011810] [Server] Insecure configuration for --pid-file: Location '/var/run/mysqld' in
the path is accessible to all OS users. Consider choosing a different directory.
2026-05-27T10:11:30.324727Z 0 [System] [MY-011323] [Server] X Plugin ready for connections. Socket: /var/run/mysqld/mysqlx.sock
2026-05-27T10:11:30.326070Z 0 [System] [MY-010931] [Server] /usr/sbin/mysqld: ready for connections. Version: '8.0.46'  socket:
'/var/run/mysqld/mysqld.sock'  port: 0  MySQL Community Server - GPL.
2026-05-27 10:11:30+00:00 [Note] [Entrypoint]: Temporary server started.
'/var/lib/mysql/mysql.sock' -> '/var/run/mysqld/mysqld.sock'
Warning: Unable to load '/usr/share/zoneinfo/iso3166.tab' as time zone. Skipping it.
Warning: Unable to load '/usr/share/zoneinfo/leap-seconds.list' as time zone. Skipping it.
Warning: Unable to load '/usr/share/zoneinfo/leapseconds' as time zone. Skipping it.
Warning: Unable to load '/usr/share/zoneinfo/tzdata.zi' as time zone. Skipping it.
Warning: Unable to load '/usr/share/zoneinfo/zone.tab' as time zone. Skipping it.
Warning: Unable to load '/usr/share/zoneinfo/zone1970.tab' as time zone. Skipping it.
2026-05-27 10:11:34+00:00 [Note] [Entrypoint]: Creating database final
2026-05-27 10:11:35+00:00 [Note] [Entrypoint]: Creating user user
2026-05-27 10:11:35+00:00 [Note] [Entrypoint]: Giving user user access to schema final

2026-05-27 10:11:35+00:00 [Note] [Entrypoint]: Stopping temporary server
2026-05-27T10:11:35.370651Z 14 [System] [MY-013172] [Server] Received SHUTDOWN from user root. Shutting down mysqld (Version:
8.0.46).
2026-05-27T10:11:37.133274Z 0 [System] [MY-010910] [Server] /usr/sbin/mysqld: Shutdown complete (mysqld 8.0.46)  MySQL Community
Server - GPL.
2026-05-27 10:11:37+00:00 [Note] [Entrypoint]: Temporary server stopped

2026-05-27 10:11:37+00:00 [Note] [Entrypoint]: MySQL init process done. Ready for start up.

2026-05-27T10:11:37.834998Z 0 [Warning] [MY-011068] [Server] The syntax '--skip-host-cache' is deprecated and will be removed in a
future release. Please use SET GLOBAL host_cache_size=0 instead.
2026-05-27T10:11:37.841622Z 0 [System] [MY-010116] [Server] /usr/sbin/mysqld (mysqld 8.0.46) starting as process 1
2026-05-27T10:11:37.887694Z 1 [System] [MY-013576] [InnoDB] InnoDB initialization has started.
2026-05-27T10:11:38.542945Z 1 [System] [MY-013577] [InnoDB] InnoDB initialization has ended.
2026-05-27T10:11:39.348402Z 0 [Warning] [MY-010068] [Server] CA certificate ca.pem is self signed.
2026-05-27T10:11:39.348473Z 0 [System] [MY-013602] [Server] Channel mysql_main configured to support TLS. Encrypted connections ar
now supported for this channel.
2026-05-27T10:11:39.359720Z 0 [Warning] [MY-011810] [Server] Insecure configuration for --pid-file: Location '/var/run/mysqld' in
the path is accessible to all OS users. Consider choosing a different directory.
2026-05-27T10:11:39.477948Z 0 [System] [MY-010931] [Server] /usr/sbin/mysqld: ready for connections. Version: '8.0.46'  socket:
'/var/run/mysqld/mysqld.sock'  port: 3306  MySQL Community Server - GPL.
2026-05-27T10:11:39.479155Z 0 [System] [MY-011323] [Server] X Plugin ready for connections. Bind-address: '::' port: 33060, socket
/var/run/mysqld/mysqlx.sock
2026-05-27 10:12:05+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.46-1.el9 started.
2026-05-27 10:12:05+00:00 [Note] [Entrypoint]: Switching to dedicated user 'mysql'
2026-05-27 10:12:05+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.46-1.el9 started.
'/var/lib/mysql/mysql.sock' -> '/var/run/mysqld/mysqld.sock'
2026-05-27T10:12:06.547141Z 0 [Warning] [MY-011068] [Server] The syntax '--skip-host-cache' is deprecated and will be removed in a
future release. Please use SET GLOBAL host_cache_size=0 instead.
2026-05-27T10:12:06.580999Z 0 [System] [MY-010116] [Server] /usr/sbin/mysqld (mysqld 8.0.46) starting as process 1
2026-05-27T10:12:06.782459Z 1 [System] [MY-013576] [InnoDB] InnoDB initialization has started.
2026-05-27T10:12:09.437755Z 1 [System] [MY-013577] [InnoDB] InnoDB initialization has ended.
2026-05-27 10:12:16+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.46-1.el9 started.
2026-05-27 10:12:17+00:00 [Note] [Entrypoint]: Switching to dedicated user 'mysql'
2026-05-27 10:12:17+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.46-1.el9 started.
'/var/lib/mysql/mysql.sock' -> '/var/run/mysqld/mysqld.sock'
2026-05-27T10:12:17.918856Z 0 [Warning] [MY-011068] [Server] The syntax '--skip-host-cache' is deprecated and will be removed in a
future release. Please use SET GLOBAL host_cache_size=0 instead.
2026-05-27T10:12:17.960214Z 0 [System] [MY-010116] [Server] /usr/sbin/mysqld (mysqld 8.0.46) starting as process 1
2026-05-27T10:12:18.134521Z 1 [System] [MY-013576] [InnoDB] InnoDB initialization has started.
2026-05-27T10:12:19.520870Z 1 [System] [MY-013577] [InnoDB] InnoDB initialization has ended.
2026-05-27 10:12:21+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.46-1.el9 started.
2026-05-27 10:12:22+00:00 [Note] [Entrypoint]: Switching to dedicated user 'mysql'
2026-05-27 10:12:22+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.46-1.el9 started.
'/var/lib/mysql/mysql.sock' -> '/var/run/mysqld/mysqld.sock'
2026-05-27T10:12:22.870375Z 0 [Warning] [MY-011068] [Server] The syntax '--skip-host-cache' is deprecated and will be removed in a
future release. Please use SET GLOBAL host_cache_size=0 instead.
2026-05-27T10:12:22.899995Z 0 [System] [MY-010116] [Server] /usr/sbin/mysqld (mysqld 8.0.46) starting as process 1
2026-05-27T10:12:23.140761Z 1 [System] [MY-013576] [InnoDB] InnoDB initialization has started.
2026-05-27T10:12:25.627262Z 1 [System] [MY-013577] [InnoDB] InnoDB initialization has ended.
2026-05-27 10:12:28+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.46-1.el9 started.
2026-05-27 10:12:29+00:00 [Note] [Entrypoint]: Switching to dedicated user 'mysql'
2026-05-27 10:12:29+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.46-1.el9 started.
'/var/lib/mysql/mysql.sock' -> '/var/run/mysqld/mysqld.sock'
2026-05-27T10:12:29.901411Z 0 [Warning] [MY-011068] [Server] The syntax '--skip-host-cache' is deprecated and will be removed in a
future release. Please use SET GLOBAL host_cache_size=0 instead.
2026-05-27T10:12:29.918981Z 0 [System] [MY-010116] [Server] /usr/sbin/mysqld (mysqld 8.0.46) starting as process 1
2026-05-27T10:12:30.077140Z 1 [System] [MY-013576] [InnoDB] InnoDB initialization has started.
2026-05-27 10:12:36+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.46-1.el9 started.
2026-05-27 10:12:37+00:00 [Note] [Entrypoint]: Switching to dedicated user 'mysql'
2026-05-27 10:12:37+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.46-1.el9 started.
'/var/lib/mysql/mysql.sock' -> '/var/run/mysqld/mysqld.sock'
2026-05-27T10:12:38.026254Z 0 [Warning] [MY-011068] [Server] The syntax '--skip-host-cache' is deprecated and will be removed in a
future release. Please use SET GLOBAL host_cache_size=0 instead.
2026-05-27T10:12:38.050272Z 0 [System] [MY-010116] [Server] /usr/sbin/mysqld (mysqld 8.0.46) starting as process 1
2026-05-27T10:12:38.206101Z 1 [System] [MY-013576] [InnoDB] InnoDB initialization has started.
2026-05-27 10:12:43+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.46-1.el9 started.
2026-05-27 10:12:43+00:00 [Note] [Entrypoint]: Switching to dedicated user 'mysql'
2026-05-27 10:12:43+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.46-1.el9 started.
'/var/lib/mysql/mysql.sock' -> '/var/run/mysqld/mysqld.sock'
2026-05-27T10:12:44.621709Z 0 [Warning] [MY-011068] [Server] The syntax '--skip-host-cache' is deprecated and will be removed in a
future release. Please use SET GLOBAL host_cache_size=0 instead.
2026-05-27T10:12:44.625104Z 0 [System] [MY-010116] [Server] /usr/sbin/mysqld (mysqld 8.0.46) starting as process 1
2026-05-27T10:12:44.652041Z 1 [System] [MY-013576] [InnoDB] InnoDB initialization has started.
2026-05-27T10:12:45.651936Z 1 [System] [MY-013577] [InnoDB] InnoDB initialization has ended.
2026-05-27T10:12:46.281745Z 0 [System] [MY-010229] [Server] Starting XA crash recovery...
2026-05-27T10:12:46.302732Z 0 [System] [MY-010232] [Server] XA crash recovery finished.
2026-05-27T10:12:46.478317Z 0 [Warning] [MY-010068] [Server] CA certificate ca.pem is self signed.
2026-05-27T10:12:46.478378Z 0 [System] [MY-013602] [Server] Channel mysql_main configured to support TLS. Encrypted connections ar
now supported for this channel.
2026-05-27T10:12:46.487360Z 0 [Warning] [MY-011810] [Server] Insecure configuration for --pid-file: Location '/var/run/mysqld' in
the path is accessible to all OS users. Consider choosing a different directory.
2026-05-27T10:12:46.701374Z 0 [System] [MY-010931] [Server] /usr/sbin/mysqld: ready for connections. Version: '8.0.46'  socket:
'/var/run/mysqld/mysqld.sock'  port: 3306  MySQL Community Server - GPL.
2026-05-27T10:12:46.702971Z 0 [System] [MY-011323] [Server] X Plugin ready for connections. Bind-address: '::' port: 33060, socket
/var/run/mysqld/mysqlx.sock
2026-05-27 10:13:29+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.46-1.el9 started.
2026-05-27 10:13:30+00:00 [Note] [Entrypoint]: Switching to dedicated user 'mysql'
2026-05-27 10:13:30+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.46-1.el9 started.
'/var/lib/mysql/mysql.sock' -> '/var/run/mysqld/mysqld.sock'
2026-05-27T10:13:31.237784Z 0 [Warning] [MY-011068] [Server] The syntax '--skip-host-cache' is deprecated and will be removed in a
future release. Please use SET GLOBAL host_cache_size=0 instead.
2026-05-27T10:13:31.241188Z 0 [System] [MY-010116] [Server] /usr/sbin/mysqld (mysqld 8.0.46) starting as process 1
2026-05-27T10:13:31.263187Z 1 [System] [MY-013576] [InnoDB] InnoDB initialization has started.
2026-05-27T10:13:32.159313Z 1 [System] [MY-013577] [InnoDB] InnoDB initialization has ended.
2026-05-27T10:13:32.658638Z 0 [System] [MY-010229] [Server] Starting XA crash recovery...
2026-05-27T10:13:32.696717Z 0 [System] [MY-010232] [Server] XA crash recovery finished.
2026-05-27T10:13:32.903497Z 0 [Warning] [MY-010068] [Server] CA certificate ca.pem is self signed.
2026-05-27T10:13:32.905258Z 0 [System] [MY-013602] [Server] Channel mysql_main configured to support TLS. Encrypted connections ar
now supported for this channel.
2026-05-27T10:13:32.914831Z 0 [Warning] [MY-011810] [Server] Insecure configuration for --pid-file: Location '/var/run/mysqld' in
the path is accessible to all OS users. Consider choosing a different directory.
2026-05-27T10:13:33.014234Z 0 [System] [MY-011323] [Server] X Plugin ready for connections. Bind-address: '::' port: 33060, socket
/var/run/mysqld/mysqlx.sock
2026-05-27T10:13:33.023973Z 0 [System] [MY-010931] [Server] /usr/sbin/mysqld: ready for connections. Version: '8.0.46'  socket:
'/var/run/mysqld/mysqld.sock'  port: 3306  MySQL Community Server - GPL.
2026-05-27 10:14:37+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.46-1.el9 started.
2026-05-27 10:14:38+00:00 [Note] [Entrypoint]: Switching to dedicated user 'mysql'
2026-05-27 10:14:38+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.46-1.el9 started.
'/var/lib/mysql/mysql.sock' -> '/var/run/mysqld/mysqld.sock'
2026-05-27T10:14:39.015211Z 0 [Warning] [MY-011068] [Server] The syntax '--skip-host-cache' is deprecated and will be removed in a
future release. Please use SET GLOBAL host_cache_size=0 instead.
2026-05-27T10:14:39.020214Z 0 [System] [MY-010116] [Server] /usr/sbin/mysqld (mysqld 8.0.46) starting as process 1
2026-05-27T10:14:39.038042Z 1 [System] [MY-013576] [InnoDB] InnoDB initialization has started.
2026-05-27T10:14:39.939995Z 1 [System] [MY-013577] [InnoDB] InnoDB initialization has ended.
2026-05-27T10:14:40.580933Z 0 [System] [MY-010229] [Server] Starting XA crash recovery...
2026-05-27T10:14:40.639438Z 0 [System] [MY-010232] [Server] XA crash recovery finished.
2026-05-27T10:14:40.861759Z 0 [Warning] [MY-010068] [Server] CA certificate ca.pem is self signed.
2026-05-27T10:14:40.861860Z 0 [System] [MY-013602] [Server] Channel mysql_main configured to support TLS. Encrypted connections ar
now supported for this channel.
2026-05-27T10:14:40.873258Z 0 [Warning] [MY-011810] [Server] Insecure configuration for --pid-file: Location '/var/run/mysqld' in
the path is accessible to all OS users. Consider choosing a different directory.
2026-05-27T10:14:41.005856Z 0 [System] [MY-010931] [Server] /usr/sbin/mysqld: ready for connections. Version: '8.0.46'  socket:
'/var/run/mysqld/mysqld.sock'  port: 3306  MySQL Community Server - GPL.
2026-05-27T10:14:41.006464Z 0 [System] [MY-011323] [Server] X Plugin ready for connections. Bind-address: '::' port: 33060, socket
/var/run/mysqld/mysqlx.sock
2026-05-27 10:14:54+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.46-1.el9 started.
2026-05-27 10:14:55+00:00 [Note] [Entrypoint]: Switching to dedicated user 'mysql'
2026-05-27 10:14:55+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.46-1.el9 started.
'/var/lib/mysql/mysql.sock' -> '/var/run/mysqld/mysqld.sock'
2026-05-27T10:14:55.854197Z 0 [Warning] [MY-011068] [Server] The syntax '--skip-host-cache' is deprecated and will be removed in a
future release. Please use SET GLOBAL host_cache_size=0 instead.
2026-05-27T10:14:55.857579Z 0 [System] [MY-010116] [Server] /usr/sbin/mysqld (mysqld 8.0.46) starting as process 1
2026-05-27T10:14:55.924214Z 1 [System] [MY-013576] [InnoDB] InnoDB initialization has started.
2026-05-27T10:14:56.705336Z 1 [System] [MY-013577] [InnoDB] InnoDB initialization has ended.
2026-05-27T10:14:57.189458Z 0 [System] [MY-010229] [Server] Starting XA crash recovery...
2026-05-27T10:14:57.221807Z 0 [System] [MY-010232] [Server] XA crash recovery finished.
2026-05-27T10:14:57.452576Z 0 [Warning] [MY-010068] [Server] CA certificate ca.pem is self signed.
2026-05-27T10:14:57.452666Z 0 [System] [MY-013602] [Server] Channel mysql_main configured to support TLS. Encrypted connections ar
now supported for this channel.
2026-05-27T10:14:57.465069Z 0 [Warning] [MY-011810] [Server] Insecure configuration for --pid-file: Location '/var/run/mysqld' in
the path is accessible to all OS users. Consider choosing a different directory.
2026-05-27T10:14:57.652327Z 0 [System] [MY-010931] [Server] /usr/sbin/mysqld: ready for connections. Version: '8.0.46'  socket:
'/var/run/mysqld/mysqld.sock'  port: 3306  MySQL Community Server - GPL.
2026-05-27T10:14:57.654971Z 0 [System] [MY-011323] [Server] X Plugin ready for connections. Bind-address: '::' port: 33060, socket
/var/run/mysqld/mysqlx.sock
ubuntu@ip-172-31-32-238:~/final-project$ ls -al
total 16
drwxrwxr-x 2 ubuntu ubuntu 4096 May 27 09:29 .
drwxr-x--- 6 ubuntu ubuntu 4096 May 27 10:00 ..
-rw-rw-r-- 1 ubuntu ubuntu  139 May 27 10:11 .env
-rw-rw-r-- 1 ubuntu ubuntu 1118 May 27 10:11 docker-compose.yml
ubuntu@ip-172-31-32-238:~/final-project$ cat .env
DB_USERNAME=user
DB_PASSWORD=1234
JWT_SECRET_KEY=V9HHKDCQ0WV5ADjm5E7GukkbHS7JDy7cIC4BKABnry7ThJG0plhc3YnnWEkvQz/l
HY0brPX6h7d5ZWfLqmPL8w==
ubuntu@ip-172-31-32-238:~/final-project$

▸ Thought for 57s
Let me analyze this carefully.
로그를 다시 정밀 분석합니다. MySQL이 약 26초마다 재시작하는 패턴이 보입니다. 먼저 Dockerfile을 확인하겠습니다.

● Read(/home/perso/projects/2026_14th_final_study_BE/Dockerfile) (ctrl+o to expand)

▸ Thought for 10s
Now I have the full picture. The Dockerfile uses a fixed ENTRYPOINT without JVM ...
진짜 원인 파악 완료!
──────
## 🎯 근본 원인: t2.micro OOM (메모리 부족)

로그 타임라인:

•  10:11:39  MySQL 준비 완료
•  10:11:51  앱 MySQL 커넥션 성공 ✅
•  10:12:05  MySQL 재시작 ← 약 26초 후 OOM kill

t2.micro는 RAM이 1GB 뿐인데, MySQL 8.0 기본 설정 + Spring Boot JVM이 합쳐서 1GB를 초과합니다.

• MySQL 8.0 기본 innodb_buffer_pool: ~128MB
• MySQL 프로세스 전체: ~400-600MB
• Spring Boot JVM: ~400-600MB
• 합계: 1GB 초과 → Linux OOM Killer가 MySQL 프로세스 종료 →  restart: always 로 재시작 반복
──────
## 🔧 Fix 1: deploy.yml — MySQL + JVM 메모리 제한

JAVA_TOOL_OPTIONS 는 JVM이 자동으로 읽는 환경변수로, Dockerfile 수정 없이 JVM 힙을 제한할 수 있습니다.

● Edit(/home/perso/projects/2026_14th_final_study_BE/.github/workflows/deploy.yml) (ctrl+o to expand)

──────
## 🔧 Fix 2: EC2에서 Swap 파일 추가 (필수! 직접 실행 필요)

코드 수정만으로는 부족합니다. EC2 서버에 swap 공간을 추가해야 합니다. EC2 서버에 SSH로 접속해서 아래 명령을 순서대로 실행해
주세요:

    # 2GB swap 파일 생성 (약 10초 소요)
    sudo fallocate -l 2G /swapfile
    sudo chmod 600 /swapfile
    sudo mkswap /swapfile
    sudo swapon /swapfile

    # 재부팅해도 유지되도록 등록
    echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab

    # 확인 (Swap: 2.0G 라고 나와야 함)
    free -h
──────
## 📋 전체 변경 요약

   항목                                       | 변경 내용
  --------------------------------------------|-----------------------------------------------------------------------------------
    healthcheck                               |  -u root -p${DB_PASSWORD}  제거 →  --silent  단순 ping으로 변경 (패스워드 불필요)
   MySQL memory                               |  innodb-buffer-pool-size=64M ,  performance-schema=OFF ,  max-connections=50
   JVM heap                                   |  JAVA_TOOL_OPTIONS: "-Xmx256m -Xms128m"  추가
   EC2 swap                                   | 2GB swap 파일 추가 (수동 실행 필요)

메모리 사용 예상 (설정 후):

• MySQL: ~200-300MB (기존 ~500MB+)
• Spring Boot JVM: ~256MB (Xmx 제한)
• OS: ~100MB
• 합계: ~560-660MB → 1GB 내 안정적으로 운영 가능 ✅

Swap 추가 후  week8/**  브랜치에 push하시면 됩니다.