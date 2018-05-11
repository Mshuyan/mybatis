# mybatis(原生)
## 1. 配置文件介绍
+ SqlMapConfig.xml<br/>
   >mybatis全局配置文件<br/>
    用于配置数据库驱动、连接、账号、密码等信息
   
+ mapper.xml<br/>
   >映射文件<br/>
    在该文件中配置sql语句<br/>
    文件名不固定是`mapper.xml`，这只是个统称，名称可以是：`User.xml`
    
## 2. mybatis使用步骤
1. 导入jar包
2. 在resources目录下创建配置文件<br/>
    + 创建`SqlMapConfig`文件，并加入如下内容
        ```xml
        <?xml version="1.0" encoding="UTF-8" ?>
        <!DOCTYPE configuration
                PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
                "http://mybatis.org/dtd/mybatis-3-config.dtd">
        <configuration>
            <!-- 和spring整合后 environments配置将废除-->
            <environments default="development">
                <environment id="development">
                    <!-- 使用jdbc事务管理-->
                    <transactionManager type="JDBC"/>
                    <!-- 数据库连接池，由mybatis管理-->
                    <dataSource type="POOLED">
                        <property name="driver" value="com.mysql.jdbc.Driver"/>
                        <property name="url" value="jdbc:mysql://localhost:3306/test1?characterEncoding=utf-8"/>
                        <property name="username" value="root"/>
                        <property name="password" value="root"/>
                    </dataSource>
                </environment>
            </environments>
        </configuration>
        ```
    + 在`resources/sqlmap`下创建`mapper.xml`文件，加入如下内容：
        ```xml
        <?xml version="1.0" encoding="UTF-8"?>
        <!DOCTYPE mapper
              PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
              "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
        
        <!-- 通过命令空间对sql进行分类管理 -->
        <mapper namespace="test">
          
        </mapper>
        ```
    + __注意__<br/>
        本程序中的`resources`目录就是`config`目录，
        将`config`目录右键 -> Mark Directory as -> Resources Root后，
        在程序中才能找到他下面的配置文件
3. 编写各表的PO类（javabean）
4. 编写mapper.xml文件<br/>
    在mapper标签中配置sql语句
    如：
    ```xml
    <select id="findUserById" parameterType="int" resultType="com.shuyan.po.User">
        select * from user where id = #{id}
    </select>
    ```
5. 在`SqlMapConfig.xml`中加载`mapper.xml`
    ```xml
    <mappers>
        <mapper resource="sqlmap/User.xml"/>
    </mappers>
    ```
6. 编写java代码<br/>
## 3. 知识点
1. 原生mybatis自动提交事务是关闭的，所以对数据库内容进行更改的操作需要手动提交事务

## 问题
1. 配置mapper时，输入参数类型只能指定1种，但是如果需要输入多个不同类型的参数时，该如何处理
2. 向sql中传入参数时不需要指定参数名称，传入多个参数时如何处理


