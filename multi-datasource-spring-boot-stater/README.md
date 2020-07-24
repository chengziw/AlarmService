<p align="center">
	<strong>
	抄的mybatis-plus的基于SpringBoot的快速集成多数据源的启动器<br/>
	去掉了几个用不到的功能(spel也想去掉来着)<br/>
	替换了加密方式<br/>
	jdk和spring版本我们也不需要适配那么旧的<br/>
	为了和原版不冲突改了包名
	</strong>
</p>

# 简介

mds-spring-boot-starter 是一个基于 SpringBoot 的快速集成多数据源的启动器。

其支持 **Jdk 1.8+,    SpringBoot 2.3.x+**。

# 特性

1. 数据源分组，适用于多种场景（纯粹多库 读写分离 一主多从 混合模式）。
2. 内置敏感参数加密。
3. 提供对 Mybatis-Plus 的快速集成。
4. 简化 HikariCp 配置，提供全局参数配置。
5. 提供自定义数据源来源接口(默认使用yml配置)。
6. 提供项目启动后增减数据源方案。
7. 提供 Mybatis 环境下的  **纯读写分离** 方案。
8. 使用 spel 动态参数解析数据源。（多租户架构神器）
9. 提供多层数据源嵌套切换。（ServiceA >>>  ServiceB >>> ServiceC，每个Service都是不同的数据源）
10. 提供 **不使用注解**  而   **使用 正则 或 spel**    来切换数据源方案（实验性功能）。

# 约定

1. 本框架只做 **切换数据源** 这件核心的事情，并**不限制你的具体操作**，切换了数据源可以做任何 CRUD。
2. 配置文件所有以下划线 `_` 分割的数据源 **首部** 即为组的名称，相同组名称的数据源会放在一个组下。
3. 切换数据源可以是组名，也可以是具体数据源名称。组名则切换时采用负载均衡算法切换。
4. 默认的数据源名称为  **master** ，你可以通过 `spring.datasource.dynamic.primary` 修改。
5. 方法上的注解优先于类上注解。

# 使用方法

1. 引入 mds-spring-boot-starter。

```xml
<dependency>
  <groupId>org.nom</groupId>
  <artifactId>mds-spring-boot-starter</artifactId>
  <version>${version}</version>
</dependency>
```
2. 配置数据源。

```yaml
spring:
  datasource:
    dynamic:
      primary: master #设置默认的数据源或者数据源组,默认值即为master
      strict: false #设置严格模式,默认false不启动. 启动后在未匹配到指定数据源时候回抛出异常,不启动会使用默认数据源.
      datasource:
        master:
          url: jdbc:mysql://xx.xx.xx.xx:3306/dynamic
          username: root
          password: 123456
          driver-class-name: com.mysql.jdbc.Driver
        slave_1:
          url: jdbc:mysql://xx.xx.xx.xx:3307/dynamic
          username: root
          password: 123456
          driver-class-name: com.mysql.jdbc.Driver
        slave_2:
          url: ENC(xxxxx) # 内置加密,使用请看文末介绍
          username: ENC(xxxxx)
          password: ENC(xxxxx)
          driver-class-name: com.mysql.jdbc.Driver
          separator: ";" # sql默认分号分隔符
          
       #......省略
       #以上会配置一个默认库master，一个组slave下有两个子库slave_1,slave_2
```

```yaml
# 多主多从                      纯粹多库（记得设置primary）                   混合配置
spring:                               spring:                               spring:
  datasource:                           datasource:                           datasource:
    dynamic:                              dynamic:                              dynamic:
      datasource:                           datasource:                           datasource:
        master_1:                             mysql:                                master:
        master_2:                             oracle:                               slave_1:
        slave_1:                              sqlserver:                            slave_2:
        slave_2:                              postgresql:                           oracle_1:
        slave_3:                              h2:                                   oracle_2:
```

3. 使用  **@DS**  切换数据源。

**@DS** 可以注解在方法上和类上，**同时存在方法注解优先于类上注解**。

强烈建议只注解在service实现上。

|     注解      |                   结果                   |
| :-----------: | :--------------------------------------: |
|    没有@DS    |                默认数据源                |
| @DS("dsName") | dsName可以为组名也可以为具体某个库的名称 |

```java
@Service
@DS("slave")
public class UserServiceImpl implements UserService {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public List<Map<String, Object>> selectAll() {
    return  jdbcTemplate.queryForList("select * from user");
  }
  
  @Override
  @DS("slave_1")
  public List<Map<String, Object>> selectByCondition() {
    return  jdbcTemplate.queryForList("select * from user where age >10");
  }
}
```

# 加密方式介绍

 加密使用jasypt，通过环境变量 MY_DB_K 来加盐
 
```bash
$ export MY_DB_K="hello world"
$ java -cp mds-spring-boot-starter-0.1.0.jar:jasypt-1.9.3.jar org.nom.mds.Enc "mypassword"
```
 
 获得输出
```text
 encode ==> yXLsqwH3wdib6RzP+kcu+NhBlpCri8MeTsz5roqMgU+GbBmcjpnnGxZl16UdNCKG
```
 把
```text
 password=mypassword
``` 
 替换成
```text
 password=ENC(yXLsqwH3wdib6RzP+kcu+NhBlpCri8MeTsz5roqMgU+GbBmcjpnnGxZl16UdNCKG)
```

---
