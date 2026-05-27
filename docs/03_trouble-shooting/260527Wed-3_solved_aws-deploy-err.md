> Last login: Wed May 27 10:07:27 2026 from 211.181.213.51
ubuntu@ip-172-31-32-238:~$ sudo fallocate -l 2G /swapfile
ubuntu@ip-172-31-32-238:~$ sudo chmod 600 /swapfile
ubuntu@ip-172-31-32-238:~$ sudo mkswap /swapfile
Setting up swapspace version 1, size = 2 GiB (2147479552 bytes)
no label, UUID=53b66aff-bb83-4583-92af-26631c635dd1
ubuntu@ip-172-31-32-238:~$ sudo swapon /swapfile
ubuntu@ip-172-31-32-238:~$  echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
/swapfile none swap sw 0 0
ubuntu@ip-172-31-32-238:~$ free -h
total        used        free      shared  buff/cache   available
Mem:           908Mi       600Mi       237Mi       2.8Mi       194Mi       308Mi
Swap:          2.0Gi          0B       2.0Gi
ubuntu@ip-172-31-32-238:~$ docker ps
CONTAINER ID   IMAGE                                                COMMAND
CREATED              STATUS                    PORTS
NAMES
ddc2282c7726   ghcr.io/person3113/2026_14th_final_study_be:latest   "java -jar -
Dspring.…"   About a minute ago   Up 53 seconds             0.0.0.0:8080->8080/tcp,
[::]:8080->8080/tcp              final-app
cb2cf0c8665b   mysql:8.0                                            "docker-
entrypoint.s…"   About a minute ago   Up 59 seconds (healthy)   0.0.0.0:3306->3306/tcp
[::]:3306->3306/tcp, 33060/tcp   final-db
ubuntu@ip-172-31-32-238:~$ ls
final-project  get-docker.sh
ubuntu@ip-172-31-32-238:~$ ls final-project/
docker-compose.yml
ubuntu@ip-172-31-32-238:~$ docker logs final-app
Picked up JAVA_TOOL_OPTIONS: -Xmx256m -Xms128m

    .   ____          _            __ _ _
/\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
\\/  ___)| |_)| | | | | || (_| |  ) ) ) )
'  |____| .__|_| |_|_| |_\__, | / / / /
=========|_|==============|___/=/_/_/_/

:: Spring Boot ::                (v4.0.6)

2026-05-27 10:34:51.255 [main] INFO  com.example.demo.FinalApplication - Starting
FinalApplication v0.0.1-SNAPSHOT using Java 25.0.3 with PID 1 (/app/app.jar started by
root in /app)
2026-05-27 10:34:51.263 [main] INFO  com.example.demo.FinalApplication - The following 1
profile is active: "prod"
2026-05-27 10:34:52.596 [background-preinit] INFO  o.h.validator.internal.util.Version -
HV000001: Hibernate Validator 9.0.1.Final
2026-05-27 10:34:54.058 [main] INFO  o.s.d.r.c.RepositoryConfigurationDelegate -
Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2026-05-27 10:34:54.162 [main] INFO  o.s.d.r.c.RepositoryConfigurationDelegate -
Finished Spring Data repository scanning in 85 ms. Found 2 JPA repository interfaces.
2026-05-27 10:34:55.485 [main] INFO  o.s.boot.tomcat.TomcatWebServer - Tomcat
initialized with port 8080 (http)
2026-05-27 10:34:55.515 [main] INFO  o.a.coyote.http11.Http11NioProtocol - Initializin
ProtocolHandler ["http-nio-8080"]
2026-05-27 10:34:55.522 [main] INFO  o.a.catalina.core.StandardService - Starting
service [Tomcat]
2026-05-27 10:34:55.523 [main] INFO  o.a.catalina.core.StandardEngine - Starting Servlet
engine: [Apache Tomcat/11.0.21]
2026-05-27 10:34:55.591 [main] INFO  o.s.b.w.c.s.WebApplicationContextInitializer - Root
WebApplicationContext: initialization completed in 4186 ms
2026-05-27 10:34:56.092 [main] INFO  org.hibernate.orm.jpa - HHH008540: Processing
PersistenceUnitInfo [name: default]
2026-05-27 10:34:56.290 [main] INFO  org.hibernate.orm.core - HHH000001: Hibernate ORM
core version 7.2.12.Final
2026-05-27 10:34:57.577 [main] INFO  o.s.o.j.p.SpringPersistenceUnitInfo - No
LoadTimeWeaver setup: ignoring JPA class transformer
2026-05-27 10:34:57.662 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 -
Starting...
2026-05-27 10:34:58.131 [main] INFO  com.zaxxer.hikari.pool.HikariPool - HikariPool-1
Added connection com.mysql.cj.jdbc.ConnectionImpl@37fffef3
2026-05-27 10:34:58.137 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 -
Start completed.
2026-05-27 10:34:58.390 [main] INFO  o.hibernate.orm.connections.pooling - HHH10001005
Database info:
Database JDBC URL
[jdbc:mysql://db:3306/final?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=
UTF-8&serverTimezone=Asia/Seoul]
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
2026-05-27 10:35:00.671 [main] INFO  org.hibernate.orm.core - HHH000489: No JTA platform
available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration
2026-05-27 10:35:01.220 [main] INFO  o.s.o.j.LocalContainerEntityManagerFactoryBean -
Initialized JPA EntityManagerFactory for persistence unit 'default'
2026-05-27 10:35:01.774 [main] INFO  o.s.d.j.r.q.QueryEnhancerFactories - Hibernate is
in classpath; If applicable, HQL parser will be used.
2026-05-27 10:35:02.823 [main] WARN  o.s.b.j.a.JpaBaseConfiguration$JpaWebConfiguration -
spring.jpa.open-in-view is enabled by default. Therefore, database queries may be
performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable
this warning
2026-05-27 10:35:02.976 [main] WARN  o.s.b.s.a.UserDetailsServiceAutoConfiguration -

Using generated security password: 6279a853-ef40-42fa-b36c-5b7c5103f311

This generated password is for development use only. Your security configuration must be
updated before running your application in production.

2026-05-27 10:35:04.264 [main] INFO  o.a.coyote.http11.Http11NioProtocol - Starting
ProtocolHandler ["http-nio-8080"]
2026-05-27 10:35:04.313 [main] INFO  o.s.boot.tomcat.TomcatWebServer - Tomcat started on
port 8080 (http) with context path '/'
2026-05-27 10:35:04.369 [main] INFO  com.example.demo.FinalApplication - Started
FinalApplication in 14.756 seconds (process running for 16.267)
2026-05-27 10:35:04.376 [main] WARN  o.s.c.events.SpringDocAppInitializer - SpringDoc
/swagger-ui.html endpoint is enabled by default. To disable it in production, set the
property 'springdoc.swagger-ui.enabled=false'
ubuntu@ip-172-31-32-238:~$ docker logs final-db
2026-05-27 10:34:42+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server
8.0.46-1.el9 started.
2026-05-27 10:34:42+00:00 [Note] [Entrypoint]: Switching to dedicated user 'mysql'
2026-05-27 10:34:42+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server
8.0.46-1.el9 started.
'/var/lib/mysql/mysql.sock' -> '/var/run/mysqld/mysqld.sock'
2026-05-27T10:34:43.062421Z 0 [Warning] [MY-011068] [Server] The syntax '--skip-host-
cache' is deprecated and will be removed in a future release. Please use SET GLOBAL
host_cache_size=0 instead.
2026-05-27T10:34:43.062625Z 0 [System] [MY-010116] [Server] /usr/sbin/mysqld (mysqld
8.0.46) starting as process 1
2026-05-27T10:34:43.074524Z 1 [System] [MY-013576] [InnoDB] InnoDB initialization has
started.
2026-05-27T10:34:43.502239Z 1 [System] [MY-013577] [InnoDB] InnoDB initialization has
ended.
2026-05-27T10:34:43.794148Z 0 [Warning] [MY-010068] [Server] CA certificate ca.pem is
self signed.
2026-05-27T10:34:43.794219Z 0 [System] [MY-013602] [Server] Channel mysql_main
configured to support TLS. Encrypted connections are now supported for this channel.
2026-05-27T10:34:43.801865Z 0 [Warning] [MY-011810] [Server] Insecure configuration for
--pid-file: Location '/var/run/mysqld' in the path is accessible to all OS users.
Consider choosing a different directory.
2026-05-27T10:34:43.861383Z 0 [System] [MY-011323] [Server] X Plugin ready for
connections. Bind-address: '::' port: 33060, socket: /var/run/mysqld/mysqlx.sock
2026-05-27T10:34:43.861519Z 0 [System] [MY-010931] [Server] /usr/sbin/mysqld: ready fo
connections. Version: '8.0.46'  socket: '/var/run/mysqld/mysqld.sock'  port: 3306  MySQL
Community Server - GPL.
ubuntu@ip-172-31-32-238:~$ ls
final-project  get-docker.sh
ubuntu@ip-172-31-32-238:~$ ls final-project/
docker-compose.yml
ubuntu@ip-172-31-32-238:~$

일단 되는 거 같아. 근데 ls하면 소스코드 없는데 정상인거야?

▸ Thought for 8s
The user is asking if it's normal that there's no source code on the EC2 server....
## 🎉 배포 완전 성공!
    Started FinalApplication in 14.756 seconds
정상 작동 중입니다!
──────
### ✅ 소스코드 없는 거 정상입니다
EC2 서버에 소스코드가 없는 게 올바른 Docker 배포 방식입니다.

    GitHub Actions 러너 (빌드 서버)
      └── 소스코드 체크아웃
      └── ./gradlew build (컴파일)
      └── Docker 이미지 생성
      └── GHCR에 이미지 Push

    EC2 서버 (운영 서버)
      └── docker-compose.yml (배포 명세서만 존재)
      └── GHCR에서 이미지 Pull
      └── 컨테이너 실행 ← 소스코드 불필요!

소스코드는 이미 이미지 안에 컴파일되어 들어가 있습니다. EC2는 그냥 이미지를 받아서
실행만 하면 됩니다.
──────
### 🌐 지금 바로 접속해보세요

    http://{EC2_퍼블릭_IP}:8080/swagger-ui.html

{EC2_퍼블릭_IP}  → AWS 콘솔에서 확인한 탄력적 IP (또는 퍼블릭 IP)로 접속하면
Swagger UI가 뜹니다.
──────