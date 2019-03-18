# springboot + mybatis

> 将mybatis整合到springboot，参见[demo](./)

## 整合步骤

+ pom.xml

  ```xml
  <!-- 创建springboot项目时选择上下面的依赖 -->
  <dependency>
      <groupId>org.mybatis.spring.boot</groupId>
      <artifactId>mybatis-spring-boot-starter</artifactId>
      <version>1.3.2</version>
  </dependency>
  <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <scope>runtime</scope>
  </dependency>
  ```

+ mybatis相关配置

  mybatis相关配置的配置方式我知道的有如下3种，推荐第一种

  + 全部在`application.properties`中配置

    ```properties
    # 指定mybatis的xml文件
    mybatis.mapper-locations=classpath:mapper/*.xml
    # 下划线与驼峰命名法转换
    mybatis.configuration.map-underscore-to-camel-case=true
    # 使用jdbc的getGeneratedKeys自动获取主键主键策略
    mybatis.configuration.use-generated-keys=true
    # 打印执行的sql；其中 com.rjs.industry.map 是自己项目的包路径
    logging.level.com.rjs.industry.map=debug
    
    # 指定数据源
    spring.datasource.driverClassName = com.mysql.cj.jdbc.Driver
    spring.datasource.url = jdbc:mysql://localhost:3306/map?useUnicode=true&characterEncoding=utf-8
    spring.datasource.username = root
    spring.datasource.password = rootroot
    ```

  + mybatis-config.xml

    在`application.properties`中指定mybatis配置文件的位置，将`mybatis.configuration`下的配置都转移到该配置文件中

    application.properties:

    ```properties
    # 指定mybatis配置文件
    mybatis.config-location=classpath:mybatis-config.xml
    # 指定mybatis的xml文件
    mybatis.mapper-locations=classpath:mapper/*.xml
    
    # 指定数据源
    spring.datasource.driverClassName = com.mysql.cj.jdbc.Driver
    spring.datasource.url = jdbc:mysql://localhost:3306/map?useUnicode=true&characterEncoding=utf-8
    spring.datasource.username = root
    spring.datasource.password = rootroot
    ```

    mybatis-config.xml

    ```xml
    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-config.dtd">
    
    <configuration>
        <!--数据库的字段名到pojo类的属性名的自动映射-->
        <settings>
            <!-- 使用jdbc的getGeneratedKeys自动获取主键主键策略:默认false -->
            <setting name="useGeneratedKeys" value="true"/>
            <!-- 开启自动驼峰-下划线命名规则,默认false: Table(create_time) -> Entity(createTime) -->
            <setting name="mapUnderscoreToCamelCase" value="true"/>
        </settings>
    </configuration>
    ```

  + 代码中进行配置

    ```java
    @Configuration
    @PropertySource("classpath:application.properties")
    public class MyBatisConfig {
        private static final Logger logger = LoggerFactory.getLogger(MyBatisConfig.class);
        private final Environment env;
    
        @Autowired
        public MyBatisConfig(Environment env) {
            this.env = env;
        }
    
        @Bean
        public SqlSessionFactory sqlSessionFactory(DataSource dataSource)
                throws Exception {
            logger.info("-------配置[mapperLocations]START-------");
            VFS.addImplClass(SpringBootVFS.class);
            String mapperLocations = env.getProperty("mybatis.mapper-locations");
    
            org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
            //使用jdbc的getGeneratedKeys获取数据库自增主键值
            configuration.setUseGeneratedKeys(true);
            //-自动使用驼峰命名属性映射字段   userId    user_id
            configuration.setMapUnderscoreToCamelCase(true);
            final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
            sessionFactory.setConfiguration(configuration);
            sessionFactory.setDataSource(dataSource);
            sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
            logger.info("-------配置[mapperLocations]END-------");
            return sessionFactory.getObject();
        }
    }
    ```

+ mybatis-config.xml

  在上述`mybatis.config-location`指定的文件中对mybatis进行配置，一般使用如下配置即可

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-config.dtd">
  
  <configuration>
      <!--数据库的字段名到pojo类的属性名的自动映射-->
      <settings>
          <!-- 使用jdbc的getGeneratedKeys自动获取主键主键策略:默认false -->
          <setting name="useGeneratedKeys" value="true"/>
          <!-- 开启自动驼峰-下划线命名规则,默认false: Table(create_time) -> Entity(createTime) -->
          <setting name="mapUnderscoreToCamelCase" value="true"/>
      </settings>
  </configuration>
  ```

+ @MapperScan

  在启动类上加上如下`@MapperScan`注解，用于指定mapper接口类的扫描路径

  ```java
  @SpringBootApplication
  @MapperScan("com.shuyan.mybatis.**.mapper")
  public class MybatisApplication {
      public static void main(String[] args) {
          SpringApplication.run(MybatisApplication.class, args);
      }
  }
  ```

## 知识点

### @MapperScan作用

+ @Mapper

  该注解标注在mapper接口类上，用于被spring发现，他就能被spring管理起来了

+ @MapperScan

  使用`@Mapper`注解时每个接口类上都要使用`@Mapper`注解，太麻烦

  `@MapperScan`用于将指定的包路径下的类都注册为mapper接口类，被spring管理起来，这样就不用每个接口类上都加`@Mapper`注解了

+ 接口类上不需要使用`@Repository`注解，spring不是靠该注解管理这些接口类的

### 日志中打印sql

+ application.properties

  ```properties
  # 打印执行的sql；其中 com.rjs.industry.map 是自己项目的包路径
  logging.level.com.rjs.industry.map=debug
  ```
