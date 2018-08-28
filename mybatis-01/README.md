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
2. `#{}`与`${}`
    + `#{}`<br/>
       占位符，用来接收输入参数，接收参数类型可以是：简单类型、POJO、hashMap
       + 简单类型<br/>
          参数名称可以随便起
       + POJO<br/>
          参数名称为POJO对象中的属性名<br/>
          如果POJO对象中的属性仍然是1个POJO对象，可以使用`.`获取内部POJO对象中的属性<br/>
          如：`#{son.name}`
       + hashMap<br/>
          与POJO相同，只不过这里传入的是map的key
    + `${}`<br/>
       + 拼接符号，用于将接受的参数和`${}`两边的字符串进行拼接
       + 接受参数与`#{}`差不多，唯一不同的是：如果参数是简单类型，参数名称必须为`value`
       + `${}`会引起sql注入，不建议使用
3. `sqlSessionFactory`是线程安全的，所以该对象可以是单例的<br/>
   `sqlSession`是线程不安全的，所以通过`sqlSessionFactory`获取`sqlSession`时，必须在函数内获取
   

## 问题
1. 配置mapper时，输入参数类型只能指定1种，但是如果需要输入多个不同类型的参数时，该如何处理
   使用POJO对象传入
2. 向sql中传入参数时不需要指定参数名称，传入多个参数时如何处理
   输入多个参数时，使用POJO对象传入，#{}取POJO对象中的属性名称获取多个参数
   ```xml
    <update id="updateUserById" parameterType="com.shuyan.po.User">
        update user set username=#{username},birthday=#{birthday},sex=#{sex},address=#{address} where id=#{id}
    </update>
   ```

## 4. dao开发-mapper代理
> 上面的开发方式是最原始的开发方法，将java代码部分分离成1个接口和1个实现类，即可实现原始的dao开发；<br/>
  下面开始介绍mapper代理方式的dao开发
  
### 4.1. mapper代理dao开发步骤
1. 编写PO类<br/>
   参见`com.shuyan.po.User`
2. 编写`SwlMapConfig.xml`<br/>
   与原始dao开发中内容相同
3. 编写dao接口<br/>
   与原始dao开发中内容相同，参见`com.shuyan.demo2_mapperDao.UserMapper`
4. 编写`mapper.xml`文件
   + 原始dao开发中`mapper.xml`文件命名为`User.xml`；这里的命名为`UserMapper.xml`
   + 原来`mapper.xml`文件中`mapper`标签的`namespace`属性值可以随便起；这里必须为`UserMapper.java`接口的全路径：`com.shuyan.demo2_mapperDao.UserMapper`
   + statement的id属性值必须为接口中方法名
   + statement的parameterType属性值必须与接口中方法的参数类型相同
   + statement的resultType属性值必须与接口中方法的返回值类型相同
   >参见`config/mapper/UserMapper.xml`
5. 在`SqlMapConfig.xml`中加载`UserMapper.xml`文件
6. 此时，不用自己编写接口的实现类了，调用接口中的方法，mybatis会自动执行对应的sql语句<br/>
   参见`com.shuyan.demo2_mapperDao.UserMapperDaoTest`
## 5. SqlMapConfig.XML
### 5.1. `properties`
> 用于从`properties`文件中读取配置信息
1. 在resources目录下创建properties配置文件：`db.properties`
2. 在`SqlMapConfig.xml`中的`configuration`标签中使用`properties`标签加载`db.properties`<br/>
    ```xml
    <properties resource="db.properties"></properties>
    ```
3. 在`SqlMapConfig.xml`中使用`db.properties`中的配置
    ```xml
    <property name="driver" value="${jdbc.driver}"/>
    <property name="url" value="${jdbc.url}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
    ```
    
### 5.2. settings
> 用于全局参数配置
```xml
<settings></settings>
```
### 5.3. typeAliases（别名）
> 配置`mapper.xml`时，parameterType和resultType的属性值一般会很长，可以为这些`类型`指定别名，方便开发

1. mybatis默认别名
    <table>
        <tr>
            <td>别名</td>
            <td>映射的类型</td>
        </tr>
        <tr>
            <td>_byte</td>
            <td>byte</td>
        </tr>
        <tr>
            <td>_long</td>
            <td>long</td>
        </tr>
        <tr>
            <td>_short</td>
            <td>short</td>
        </tr>
        <tr>
            <td>_int</td>
            <td>int</td>
        </tr>
        <tr>
            <td>_integer</td>
            <td>int</td>
        </tr>
        <tr>
            <td>_double</td>
            <td>double</td>
        </tr>
        <tr>
            <td>_float</td>
            <td>float</td>
        </tr>
        <tr>
            <td>_boolean</td>
            <td>boolean</td>
        </tr>
        <tr>
            <td>string</td>
            <td>String</td>
        </tr>
        <tr>
            <td>byte</td>
            <td>Byte</td>
        </tr>
        <tr>
            <td>long</td>
            <td>Long</td>
        </tr>
        <tr>
            <td>short</td>
            <td>Short</td>
        </tr>
        <tr>
            <td>int</td>
            <td>Integer</td>
        </tr>
        <tr>
            <td>integer</td>
            <td>Integer</td>
        </tr>
        <tr>
            <td>double</td>
            <td>Double</td>
        </tr>
        <tr>
            <td>float</td>
            <td>Float</td>
        </tr>
        <tr>
            <td>boolean</td>
            <td>Boolean</td>
        </tr>
        <tr>
            <td>date</td>
            <td>Date</td>
        </tr>
        <tr>
            <td>decimal</td>
            <td>BigDecimal</td>
        </tr>
        <tr>
            <td>bigdecimal</td>
            <td>BigDecimal</td>
        </tr>
    </table>
2. 自定义别名<br/>
```xml
<typeAliases>
    <!-- 单个别名定义 -->
    <typeAlias type="com.shuyan.po.User" alias="user"/>

    <!-- 
        批量别名定义 
        这个包下的PO类会被自动定义别名，别名就是类名，首字母大小写均能找到这个PO类
    -->
    <package name="com.shuyan.po"/>
</typeAliases>
```

### 5.4. typeHandlers（类型处理器）
> mybatis通过类型处理器完成jdbc类型与java类型的转换<br/>
  一般mybatis内置的类型处理器已经够用了，不需要自定义
  
### 5.5. mappers（映射配置）
1. 单个映射文件加载
    ```xml
    <mappers>
        <mapper resource="sqlmap/User.xml"/>
    </mappers>
    ```
2. 通过mapper接口加载映射文件
> 适用于使用mapper代理方式使用mybatis<br/>
  要求mapper接口与mapper.xml放在同一个目录下，并且接口名与xml文件名一致<br/>
  参见`com.shuyan.demo3_mappersLoad`
  + `SqlMapConfig.xml`中通过mapper接口加载单个映射文件
    ```xml
    <mapper class="com.shuyan.demo3_mappersLoad.UserMapper"/>
    ```
3. 批量加载映射文件
> 在遵循`通过mapper接口加载映射文件`的要求的前提下，使用该方法批量加载映射文件
  + `SqlMapConfig.xml`中批量加载映射文件
    ```xml
    <package name="com.shuyan.demo3_mappersLoad"/>
    ```
## 6. 输入映射
1. pojo包装类型
> 当需要进行复杂查询时，如：多表联查、查询名字包含a的记录；这些查询需要的条件在PO对象中是没有这些字段的，此时就需要单独定义POJO类来包装一些现有PO类和在PO类中不存在的一些字段来进行复杂查询

## 7. 输出映射
1. resultType
> 当sql语句的查询结果的列名与POJO类中属性名对应时，使用resultType设置输出类型

2. resultMap<br/>
适用于如下情况：
+ 当sql语句的查询结果的列名无法与POJO类中属性名对应时
    > 将列名与POJO中属性名设置映射关系
+ 当输出结果的PO类中包含另外1个PO类对象时
    > 使用`association`标签将结果与PO对象中的对象进行映射
+ 当查询结果存在一对多关系时（list集合）<br/>
    > 使用`collection`标签将结果中主键重复的记录映射到PO对象中的集合中

## 8. 动态sql
> 通过表达式进行判断，对sql进行灵活拼接、组装

参见`com.shuyan.demo4_dynamicSql`
### 8.1. if判断
+ `<where/>、<if/>`标签
### 8.2. sql片段
> 一句sql中的某个片段可能是会被其他语句重复使用的，将这样的片段抽离出来，让其可以被其他语句使用，这样的片段叫做sql片段
+ `<sql/>`定义sql片段
+ `<include/>`引用sql片段
### 8.3. foreach标签




