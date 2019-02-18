# mybatis-redis

> 整合redis作为mybatis的二级缓存

## mybatis缓存

> 资料参见
>
> + [mybatis的缓存机制（一级缓存二级缓存和刷新缓存）和mybatis整合ehcache](https://blog.csdn.net/u012373815/article/details/47069223) 
> + [Mybatis二级缓存原理](https://www.jianshu.com/p/5ff874fa696f) 

+ mybatis默认开启全局缓存，全局缓存开关可以通过`mybatis.configuration.cache-enabled=true`进行设置

+ mybatis一级缓存是`sqlsession`级别的缓存，默认开启

+ mybatis二级缓存是mapper级别的缓存，可以使用`Ehcache`、`redis`等

+ 二级缓存是通过`CacheExecutor`使用静态代理模式实现的

+ 当执行sql时，缓存调用顺序为

  二级缓存 → 一级缓存 → 数据源

## 实现

> 参考资料：
>
> + [Java Web现代化开发：Spring Boot + Mybatis + Redis二级缓存](http://kissyu.org/2017/05/29/Java%20Web%E7%8E%B0%E4%BB%A3%E5%8C%96%E5%BC%80%E5%8F%91%EF%BC%9ASpring%20Boot%20+%20Mybatis%20+%20Redis%E4%BD%9C%E4%B8%BA%E4%BA%8C%E7%BA%A7%E7%BC%93%E5%AD%98/) 
> + [SpringBoot普通类中如何获取其他bean例如Service、Dao](https://blog.csdn.net/majunzhu/article/details/79199716) 

+ 将springboot和mybatis进行整合，参见[springboot-mybatis](../springboot-mybatis/README.md)

+ 整合redis，并开启`spring cache`，参见[redis](../../redis/README.md)

+ 自定义`org.apache.ibatis.cache.Cache`接口实现类`RedisCache`

  该类用于mybatis操作redis

  ```java
  public class RedisCache implements Cache {
      private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);
      private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
      private final String id;
      private RedisTemplate<Object,Object> redisTemplate;
      private static final long EXPIRE_TIME_IN_MINUTES = 30;
      public RedisCache(String id) {
          if (id == null) {
              throw new IllegalArgumentException("Cache instances require an ID");
          }
          this.id = id;
      }
      @Override
      public String getId() {
          return id;
      }
      /**
       * Put query result to redis
       */
      @Override
      public void putObject(Object key, Object value) {
          RedisTemplate<Object,Object> redisTemplate = getRedisTemplate();
          ValueOperations<Object,Object> opsForValue = redisTemplate.opsForValue();
          opsForValue.set(key, value, EXPIRE_TIME_IN_MINUTES, TimeUnit.MINUTES);
          logger.debug("Put query result to redis");
      }
      /**
       * Get cached query result from redis
       */
      @Override
      public Object getObject(Object key) {
          RedisTemplate redisTemplate = getRedisTemplate();
          ValueOperations opsForValue = redisTemplate.opsForValue();
          logger.debug("Get cached query result from redis");
          return opsForValue.get(key);
      }
      /**
       * Remove cached query result from redis
       */
      @Override
      @SuppressWarnings("unchecked")
      public Object removeObject(Object key) {
          RedisTemplate redisTemplate = getRedisTemplate();
          redisTemplate.delete(key);
          logger.debug("Remove cached query result from redis");
          return null;
      }
      /**
       * Clears this cache instance
       */
      @Override
      @SuppressWarnings("all")
      public void clear() {
          RedisTemplate redisTemplate = getRedisTemplate();
          redisTemplate.execute((RedisCallback) connection -> {
              connection.flushDb();
              return null;
          });
          logger.debug("Clear all the cached query result from redis");
      }
      @Override
      public int getSize() {
          return 0;
      }
      @Override
      public ReadWriteLock getReadWriteLock() {
          return readWriteLock;
      }
      @SuppressWarnings("unchecked")
      private RedisTemplate<Object,Object> getRedisTemplate() {
          if (redisTemplate == null) {
              redisTemplate= SpringContextHandler.getBean("redisTemplate",RedisTemplate.class);
          }
          return redisTemplate;
      }
  }
  ```

  该类可以直接复制粘贴使用

  其中常量`EXPIRE_TIME_IN_MINUTES`用于指定mybatis存入redis的数据的过期时间

+ 普通类中获取`Bean`

  自定义类`RedisCache`是1个没有被spring管理的普通类，但是里面需要使用被spring管理的对象`redisTemplate`，所以需要实现1个类，用于在普通类中获取`Bean`

  ```java
  @Component
  @SuppressWarnings("all")
  public class SpringContextHandler implements ApplicationContextAware {
  
      private static ApplicationContext applicationContext;
  
      @Override
      public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
          SpringContextHandler.applicationContext = applicationContext;
      }
  
      public static ApplicationContext getApplicationContext() {
          return applicationContext;
      }
  
      public static Object getBean(String name) {
          return getApplicationContext().getBean(name);
      }
  
      public static <T> T getBean(Class<T> clazz) {
          return getApplicationContext().getBean(clazz);
      }
  
      public static <T> T getBean(String name, Class<T> clazz) {
          return getApplicationContext().getBean(name, clazz);
      }
  }
  ```

+ 使用二级缓存

  在mybatis的`mapper.xml`文件中加入如下标签，`type`属性指向自定义的`RedisCache`

  ```xml
  <cache type="com.shuyan.mybatis.mybatisredis.commons.util.RedisCache"/>
  ```

  此时该`mapper.xml`文件中的所有sql都会经过二级缓存了

  如果某个sql不想进行缓存，可以指定sql标签的`useCache`属性为`false`（默认为`true`）

  ```xml
  <select id="getAllRegion" useCache="false" resultType="com.shuyan.mybatis.mybatisredis.business.bean.RegionEntity">
      select * from region
  </select>
  ```
