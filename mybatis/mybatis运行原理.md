# mybatis 运行原理

## SqlSessionFactoryBean的初始化

### 目的：

- 解析`mybatis-application.xml` 全局配置文件
- 解析 `mapper.xml` sql映射文件
- 将以上 解析内容 存放进 org.apache.ibatis.session.Configuration中
- mappedStatement --  代表一个详细的 增删查改 的 详细信息
- 返回 包含 此Configuration 的 DefaultSqlSessionFactory

### org.apache.ibatis.session.SqlSessionFactoryBuilder

- ```java
  public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Reader reader) {
      return build(reader, null, null);
    }

    public SqlSessionFactory build(Reader reader, String environment) {
      return build(reader, environment, null);
    }

    public SqlSessionFactory build(Reader reader, Properties properties) {
      return build(reader, null, properties);
    }

    public SqlSessionFactory build(Reader reader, String environment, Properties properties) {
      try {
        XMLConfigBuilder parser = new XMLConfigBuilder(reader, environment, properties);
        return build(parser.parse());
      } catch (Exception e) {
        throw ExceptionFactory.wrapException("Error building SqlSession.", e);
      } finally {
        ErrorContext.instance().reset();
        try {
          reader.close();
        } catch (IOException e) {
          // Intentionally ignore. Prefer previous error.
        }
      }
    }

    
    // -------  参数流   ------------------------------------------------------
    public SqlSessionFactory build(InputStream inputStream) {
      return build(inputStream, null, null);
    }

    public SqlSessionFactory build(InputStream inputStream, String environment) {
      return build(inputStream, environment, null);
    }

    public SqlSessionFactory build(InputStream inputStream, Properties properties) {
      return build(inputStream, null, properties);
    }

    public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
      try {
        XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
        
        // ---  parser.parse()  返回  Configuration
       //  build(parser.parse())
        return build(parser.parse());
      } catch (Exception e) {
        throw ExceptionFactory.wrapException("Error building SqlSession.", e);
      } finally {
        ErrorContext.instance().reset();
        try {
          inputStream.close();
        } catch (IOException e) {
          // Intentionally ignore. Prefer previous error.
        }
      }
    }
      
    
    // --- 返回 DefaultSqlSessionFactory
    public SqlSessionFactory build(Configuration config) {
      return new DefaultSqlSessionFactory(config);
    }

  }
  ```





### org.apache.ibatis.builder.xml.XMLConfigBuilder



- 不管执行 `SqlSessionFactoryBuilder.build()` 那个方法 ，最终都会执行代码

- ```java
  XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
        return build(parser.parse());
  ```

  ​

-  `XMLConfigBuilder parser`   文件解析器

- `parser.evalNode("/configuration")`  -->  解析 mybatis 的全局配置文件中的 `configuration` 节点

- ` parseConfiguration(XNode root)` --->  挨个的解析 `configuration`  下的每一个节点

  - `properties`
  - `settings`
  - `typeAliases`
  - `plugins`
  - `objectFactory`
  - `objectWrapperFactory`
  - `reflectorFactory`
  - `environments`
  - `databaseIdProvider`
  - `typeHandlers`
  - `mappers`

```java
package org.apache.ibatis.builder.xml.XMLConfigBuilder {

 private final XPathParser    parser;   
//  --  返回 的 configuration  包含 所有的信息
//  ---------  mapper 全局的配置信息
//  --------  所有的 mapper.xml 中  对应的 信息
//  ---------  每一个 mapper 接口对应的 的mapperProxyFactory  -- 此接口的 实现 由 此工厂产生
  public Configuration parse() {
    if (parsed) {
      throw new BuilderException("Each XMLConfigBuilder can only be used once.");
    }
    parsed = true;
    
    // ----   parser.evalNode("/configuration")---> 解析 mybatis 的 全局配置文件  configuration 节点下的 信息
    parseConfiguration(parser.evalNode("/configuration"));
    return configuration;
  }
  


   private void parseConfiguration(XNode root) {
    try {
      //issue #117 read properties first
      propertiesElement(root.evalNode("properties"));
      Properties settings = settingsAsProperties(root.evalNode("settings"));
      loadCustomVfs(settings);
      typeAliasesElement(root.evalNode("typeAliases"));
      pluginElement(root.evalNode("plugins"));
      objectFactoryElement(root.evalNode("objectFactory"));
      objectWrapperFactoryElement(root.evalNode("objectWrapperFactory"));
      reflectorFactoryElement(root.evalNode("reflectorFactory"));
      // ---  解析 每一个标签 ---------
      settingsElement(settings);
      
      
      // read it after objectFactory and objectWrapperFactory issue #631
      environmentsElement(root.evalNode("environments"));
      databaseIdProviderElement(root.evalNode("databaseIdProvider"));
      typeHandlerElement(root.evalNode("typeHandlers"));
      
      // ----------  mepper 元素解析 -------------
      mapperElement(root.evalNode("mappers"));
    } catch (Exception e) {
      throw new BuilderException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
    }
  }


// ---  解析 每一个标签 ---------
private void settingsElement(Properties props) throws Exception {
    configuration.setAutoMappingBehavior(AutoMappingBehavior.valueOf(props.getProperty("autoMappingBehavior", "PARTIAL")));
    configuration.setAutoMappingUnknownColumnBehavior(AutoMappingUnknownColumnBehavior.valueOf(props.getProperty("autoMappingUnknownColumnBehavior", "NONE")));
    configuration.setCacheEnabled(booleanValueOf(props.getProperty("cacheEnabled"), true));
    configuration.setProxyFactory((ProxyFactory) createInstance(props.getProperty("proxyFactory")));
    configuration.setLazyLoadingEnabled(booleanValueOf(props.getProperty("lazyLoadingEnabled"), false));
    configuration.setAggressiveLazyLoading(booleanValueOf(props.getProperty("aggressiveLazyLoading"), false));
    configuration.setMultipleResultSetsEnabled(booleanValueOf(props.getProperty("multipleResultSetsEnabled"), true));
    configuration.setUseColumnLabel(booleanValueOf(props.getProperty("useColumnLabel"), true));
    configuration.setUseGeneratedKeys(booleanValueOf(props.getProperty("useGeneratedKeys"), false));
    configuration.setDefaultExecutorType(ExecutorType.valueOf(props.getProperty("defaultExecutorType", "SIMPLE")));
    configuration.setDefaultStatementTimeout(integerValueOf(props.getProperty("defaultStatementTimeout"), null));
    configuration.setDefaultFetchSize(integerValueOf(props.getProperty("defaultFetchSize"), null));
    configuration.setMapUnderscoreToCamelCase(booleanValueOf(props.getProperty("mapUnderscoreToCamelCase"), false));
    configuration.setSafeRowBoundsEnabled(booleanValueOf(props.getProperty("safeRowBoundsEnabled"), false));
    configuration.setLocalCacheScope(LocalCacheScope.valueOf(props.getProperty("localCacheScope", "SESSION")));
    configuration.setJdbcTypeForNull(JdbcType.valueOf(props.getProperty("jdbcTypeForNull", "OTHER")));
    configuration.setLazyLoadTriggerMethods(stringSetValueOf(props.getProperty("lazyLoadTriggerMethods"), "equals,clone,hashCode,toString"));
    configuration.setSafeResultHandlerEnabled(booleanValueOf(props.getProperty("safeResultHandlerEnabled"), true));
    configuration.setDefaultScriptingLanguage(resolveClass(props.getProperty("defaultScriptingLanguage")));
    @SuppressWarnings("unchecked")
    Class<? extends TypeHandler> typeHandler = (Class<? extends TypeHandler>)resolveClass(props.getProperty("defaultEnumTypeHandler"));
    configuration.setDefaultEnumTypeHandler(typeHandler);
    configuration.setCallSettersOnNulls(booleanValueOf(props.getProperty("callSettersOnNulls"), false));
    configuration.setUseActualParamName(booleanValueOf(props.getProperty("useActualParamName"), true));
    configuration.setReturnInstanceForEmptyRow(booleanValueOf(props.getProperty("returnInstanceForEmptyRow"), false));
    configuration.setLogPrefix(props.getProperty("logPrefix"));
    @SuppressWarnings("unchecked")
    Class<? extends Log> logImpl = (Class<? extends Log>)resolveClass(props.getProperty("logImpl"));
    configuration.setLogImpl(logImpl);
    configuration.setConfigurationFactory(resolveClass(props.getProperty("configurationFactory")));
  }


// ----------  mepper.xml 解析 -------------
private void mapperElement(XNode parent) throws Exception {
    if (parent != null) {
      for (XNode child : parent.getChildren()) {
        
        // --11---  如果是 包扫描  ------------
        if ("package".equals(child.getName())) {
          String mapperPackage = child.getStringAttribute("name");
          configuration.addMappers(mapperPackage);
        } else {
           // --22---  否则就是 具体资源  映射  ------------
          //  -- 331 拿到  resource属性  对应的 值
          String resource = child.getStringAttribute("resource");
          //  -- 332 如果是  url---拿到  url  对应的 值
          String url = child.getStringAttribute("url");
          //  -- 333 如果是  class---拿到  class  对应的 值
          String mapperClass = child.getStringAttribute("class");
          
          // ----331  这种情况  --------------
          if (resource != null && url == null && mapperClass == null) {
            ErrorContext.instance().resource(resource);
            
            //  -----44  拿到 这个流 
            InputStream inputStream = Resources.getResourceAsStream(resource);
            
            // -- 55  用 mybatis  的自己的解析器解析
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
            mapperParser.parse();
            
            
          } else if (resource == null && url != null && mapperClass == null) {
            ErrorContext.instance().resource(url);
            InputStream inputStream = Resources.getUrlAsStream(url);
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, url, configuration.getSqlFragments());
            mapperParser.parse();
            
            
          } else if (resource == null && url == null && mapperClass != null) {
            Class<?> mapperInterface = Resources.classForName(mapperClass);
            configuration.addMapper(mapperInterface);
          } else {
            throw new BuilderException("A mapper element may only specify a url, resource or class, but not more than one.");
          }
        }
      }
    }
  }
  
}
```





### org.apache.ibatis.parsing.XPathParser

- ​

```java
package org.apache.ibatis.parsing;

public class XPathParser {

 public List<XNode> evalNodes(String expression) {
    return evalNodes(document, expression);
  }

  public List<XNode> evalNodes(Object root, String expression) {
    List<XNode> xnodes = new ArrayList<XNode>();
    NodeList nodes = (NodeList) evaluate(expression, root, XPathConstants.NODESET);
    for (int i = 0; i < nodes.getLength(); i++) {
      xnodes.add(new XNode(this, nodes.item(i), variables));
    }
    return xnodes;
  }
  
 }
```





- `parser.evalNode("/mapper")` --->  获取 `"/mapper"` 下的所有节点
- ​

```java
package org.apache.ibatis.builder.xml;

public class XMLMapperBuilder extends BaseBuilder {
 
 public void parse() {
    if (!configuration.isResourceLoaded(resource)) {
      configurationElement(parser.evalNode("/mapper"));
      configuration.addLoadedResource(resource);
      bindMapperForNamespace();
    }

    parsePendingResultMaps();
    parsePendingCacheRefs();
    parsePendingStatements();
  }
  
   private void configurationElement(XNode context) {
    try {
      
      // --- 拿到这个 `namespace`  对应的值
      String namespace = context.getStringAttribute("namespace");
      if (namespace == null || namespace.equals("")) {
        throw new BuilderException("Mapper's namespace cannot be empty");
      }
      builderAssistant.setCurrentNamespace(namespace);
      
      //-- 解析 cache-ref  缓存 节点
      cacheRefElement(context.evalNode("cache-ref"));
      cacheElement(context.evalNode("cache"));
      
      // --- 解析 parameterMap  resultMap   sql 的所有节点
      parameterMapElement(context.evalNodes("/mapper/parameterMap"));
      resultMapElements(context.evalNodes("/mapper/resultMap"));
      sqlElement(context.evalNodes("/mapper/sql"));
      
      // --- 解析 select|insert|update|delete 的所有节点
      buildStatementFromContext(context.evalNodes("select|insert|update|delete"));
      
      
    } catch (Exception e) {
      throw new BuilderException("Error parsing Mapper XML. The XML location is '" + resource + "'. Cause: " + e, e);
    }
  }
  
  
  // --- 解析 增删查改  标签
  private void buildStatementFromContext(List<XNode> list) {
    
    // --  判断是否 多数据库 配置
    if (configuration.getDatabaseId() != null) {
      buildStatementFromContext(list, configuration.getDatabaseId());
    }
    buildStatementFromContext(list, null);
  }
  
  
   private void buildStatementFromContext(List<XNode> list, String requiredDatabaseId) {
    for (XNode context : list) {
      
      // --  解析 增删改查  标签的 解析器  ---  解析 SQL语句
      final XMLStatementBuilder statementParser = new XMLStatementBuilder(configuration, builderAssistant, context, requiredDatabaseId);
      try {
        statementParser.parseStatementNode();
      } catch (IncompleteElementException e) {
        configuration.addIncompleteStatement(statementParser);
      }
    }
  }
  
  
}
```



### org.apache.ibatis.builder.xml.XMLStatementBuilder

- 增删改查 解析器

```java
package org.apache.ibatis.builder.xml;

public class XMLStatementBuilder extends BaseBuilder {

  
   private final MapperBuilderAssistant builderAssistant;

  public void parseStatementNode() {
    // -- 拿到 单个 增删改查  的 标签的 id 值
    String id = context.getStringAttribute("id");
    
    String databaseId = context.getStringAttribute("databaseId");
    if (!databaseIdMatchesCurrent(id, databaseId, this.requiredDatabaseId)) {
      return;
    }

    //  --  及 能写 的其他的 所有的 标签 值
    Integer fetchSize = context.getIntAttribute("fetchSize");
    Integer timeout = context.getIntAttribute("timeout");
    String parameterMap = context.getStringAttribute("parameterMap");
    String parameterType = context.getStringAttribute("parameterType");
    Class<?> parameterTypeClass = resolveClass(parameterType);
    String resultMap = context.getStringAttribute("resultMap");
    String resultType = context.getStringAttribute("resultType");
    String lang = context.getStringAttribute("lang");
    LanguageDriver langDriver = getLanguageDriver(lang);

    Class<?> resultTypeClass = resolveClass(resultType);
    String resultSetType = context.getStringAttribute("resultSetType");
    StatementType statementType = StatementType.valueOf(context.getStringAttribute("statementType", StatementType.PREPARED.toString()));
    ResultSetType resultSetTypeEnum = resolveResultSetType(resultSetType);

    String nodeName = context.getNode().getNodeName();
    SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
    boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
    boolean flushCache = context.getBooleanAttribute("flushCache", !isSelect);
    boolean useCache = context.getBooleanAttribute("useCache", isSelect);
    boolean resultOrdered = context.getBooleanAttribute("resultOrdered", false);

    // Include Fragments before parsing
    XMLIncludeTransformer includeParser = new XMLIncludeTransformer(configuration, builderAssistant);
    includeParser.applyIncludes(context.getNode());

    // Parse selectKey after includes and remove them.
    processSelectKeyNodes(id, parameterTypeClass, langDriver);
    
    // Parse the SQL (pre: <selectKey> and <include> were parsed and removed)
    SqlSource sqlSource = langDriver.createSqlSource(configuration, context, parameterTypeClass);
    String resultSets = context.getStringAttribute("resultSets");
    String keyProperty = context.getStringAttribute("keyProperty");
    String keyColumn = context.getStringAttribute("keyColumn");
    KeyGenerator keyGenerator;
    String keyStatementId = id + SelectKeyGenerator.SELECT_KEY_SUFFIX;
    keyStatementId = builderAssistant.applyCurrentNamespace(keyStatementId, true);
    if (configuration.hasKeyGenerator(keyStatementId)) {
      keyGenerator = configuration.getKeyGenerator(keyStatementId);
    } else {
      keyGenerator = context.getBooleanAttribute("useGeneratedKeys",
          configuration.isUseGeneratedKeys() && SqlCommandType.INSERT.equals(sqlCommandType))
          ? Jdbc3KeyGenerator.INSTANCE : NoKeyGenerator.INSTANCE;
    }

    
    // --- 上面把 该增删查改标签 值 拿出来  然后调用此方法  --- 返回一个  MappedStatement
    // ---  每一个增删改查 标签 就代表一个 MappedStatement
    builderAssistant.addMappedStatement(id, sqlSource, statementType, sqlCommandType,
        fetchSize, timeout, parameterMap, parameterTypeClass, resultMap, resultTypeClass,
        resultSetTypeEnum, flushCache, useCache, resultOrdered, 
        keyGenerator, keyProperty, keyColumn, databaseId, langDriver, resultSets);
  }
  
  
}
```





### org.apache.ibatis.builder.MapperBuilderAssistant

- ​

```java
package org.apache.ibatis.builder;

public class MapperBuilderAssistant extends BaseBuilder {

  public MappedStatement addMappedStatement(
      String id,
      SqlSource sqlSource,
      StatementType statementType,
      SqlCommandType sqlCommandType,
      Integer fetchSize,
      Integer timeout,
      String parameterMap,
      Class<?> parameterType,
      String resultMap,
      Class<?> resultType,
      ResultSetType resultSetType,
      boolean flushCache,
      boolean useCache,
      boolean resultOrdered,
      KeyGenerator keyGenerator,
      String keyProperty,
      String keyColumn,
      String databaseId,
      LanguageDriver lang,
      String resultSets) {

    if (unresolvedCacheRef) {
      throw new IncompleteElementException("Cache-ref not yet resolved");
    }

    id = applyCurrentNamespace(id, false);
    boolean isSelect = sqlCommandType == SqlCommandType.SELECT;

    MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, id, sqlSource, sqlCommandType)
        .resource(resource)
        .fetchSize(fetchSize)
        .timeout(timeout)
        .statementType(statementType)
        .keyGenerator(keyGenerator)
        .keyProperty(keyProperty)
        .keyColumn(keyColumn)
        .databaseId(databaseId)
        .lang(lang)
        .resultOrdered(resultOrdered)
        .resultSets(resultSets)
        .resultMaps(getStatementResultMaps(resultMap, resultType, id))
        .resultSetType(resultSetType)
        .flushCacheRequired(valueOrDefault(flushCache, !isSelect))
        .useCache(valueOrDefault(useCache, isSelect))
        .cache(currentCache);

    ParameterMap statementParameterMap = getStatementParameterMap(parameterMap, parameterType, id);
    if (statementParameterMap != null) {
      statementBuilder.parameterMap(statementParameterMap);
    }

    
    // -- 构建 出来 MappedStatement  --->  此  MappedStatement  就包含 此时增删查改中 某个标签 的 所有信息   那个mapper.xml   对应的id  对应的 SQL 等等
    MappedStatement statement = statementBuilder.build();
    
    // -- 将 MappedStatement  添加 进 全局 配置
    configuration.addMappedStatement(statement);
    return statement;
  }
  
}
```





## 获取 sqlsession 对象

- 上一步获取  DefaultSqlSessionFactory  后 调用 



### org.apache.ibatis.session.defaults.DefaultSqlSessionFactory

```java
package org.apache.ibatis.session.defaults;

public class DefaultSqlSessionFactory implements SqlSessionFactory {
  
  @Override
  public SqlSession openSession() {
    return openSessionFromDataSource(configuration.getDefaultExecutorType(), null, false);
  }
  
  private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, boolean autoCommit) {
    Transaction tx = null;
    try {
      final Environment environment = configuration.getEnvironment();
      final TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
      tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
      final Executor executor = configuration. (tx, execType);
      return new DefaultSqlSession(configuration, executor, autoCommit);
    } catch (Exception e) {
      closeTransaction(tx); // may have fetched a connection so lets call close()
      throw ExceptionFactory.wrapException("Error opening session.  Cause: " + e, e);
    } finally {
      ErrorContext.instance().reset();
    }
  }
  
  
  
}
```

- openSession(...) 方法中 
  - 调用openSessionFromDataSource（）方法 ，传入参数
    - 执行器类型  --->  在全局配置文件中 有一个配置项  DefaultExecutorType 
    - configuration.getDefaultExecutorType()  ---->  默认 SIMPLE
      - SIMPLE  简单类型
      - REUSE  可复用类型
      - BATCH 批量操作类型
- openSessionFromDataSource(...)  方法中
  - 获取 Environment
  - 创建事物
  - 根据 事物及 执行器类型 获取一个  Executor --> configuration.newExecutor(tx, execType);
- return new DefaultSqlSession(configuration, executor, autoCommit);
  - 将获取的 执行器 Executor 又创建  DefaultSqlSession 
    - 此 DefaultSqlSession  包含了 所有的配置信息 及 执行器 ，及 autoCommit



### org.apache.ibatis.session.Configuration

```java
package org.apache.ibatis.session;

public class Configuration {
  
  public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
    
    // --------  获取 执行器类型 executorType
    executorType = executorType == null ? defaultExecutorType : executorType;
    executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
    Executor executor;
    
    // --------  根据 执行器类型 executorType 创建对应的 执行器
    if (ExecutorType.BATCH == executorType) {
      executor = new BatchExecutor(this, transaction);
    } else if (ExecutorType.REUSE == executorType) {
      executor = new ReuseExecutor(this, transaction);
    } else {
      executor = new SimpleExecutor(this, transaction);
    }
    if (cacheEnabled) {
      executor = new CachingExecutor(executor);
    }
    executor = (Executor) interceptorChain.pluginAll(executor);
    return executor;
  }
  
}
```

- 获取 执行器类型 executorType
- 根据 执行器类型 executorType 创建对应的 执行器
- if 判断是否配置了 二级 缓存
  - 若配置了二级缓存 ，则用  CachingExecutor  将executor 进行包装  --->  装饰器 模式
  - 例如 查询query（...）  方法的增强  -->  先查缓存 不行再进行数据库 查询
- interceptorChain.pluginAll(executor);  
  - 拦截器链  ---->  如果有实现拦截器，则用拦截器对 执行器 进行 包装 返回包装后的拦截器
  - 也与 插件的 实现 有关

## 获取接口的代理对象（mapperProxy）

- 上一步 返回了 包含 configuration 及 executor 的  DefaultSqlSession  对象


### apache.ibatis.session.defaults.DefaultSqlSession


```java
package org.apache.ibatis.session.defaults;

public class DefaultSqlSession implements SqlSession {
  
  @Override
  public <T> T getMapper(Class<T> type) {
    return configuration.<T>getMapper(type, this);
  }
  
}
```

- getMapper(Class<T> type)方法

  - 目的：

    - 获取mapper接口的代理类

  - 实现：

    - configuration.<T>getMapper(type, this);

      ​



### org.apache.ibatis.session.Configuration

```java
package org.apache.ibatis.session;

public class Configuration {

  protected final MapperRegistry mapperRegistry = new MapperRegistry(this);
  
  //  .....
  
  public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
    return mapperRegistry.getMapper(type, sqlSession);
  }

}
```

- getMapper(Class<T> type, SqlSession sqlSession) 方法

  - 目的：

    - 获取mapper接口的代理类

  - 实现：

    - mapperRegistry.getMapper(type, sqlSession);

      ​

### org.apache.ibatis.binding.MapperRegistry

```java
package org.apache.ibatis.binding;

public class MapperRegistry {

    @SuppressWarnings("unchecked")
  public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
    
    final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
    
    if (mapperProxyFactory == null) {
      throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
    }
    
    try {
      return mapperProxyFactory.newInstance(sqlSession);
    } catch (Exception e) {
      throw new BindingException("Error getting mapper instance. Cause: " + e, e);
    }
  }

}
```

- getMapper(Class<T> type, SqlSession sqlSession)   方法

  - 目的：

    - 获取 mapper 接口的代理类

  - 实现：

    - MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);

      - 获取到 MapperProxyFactory<T>
        - 在 sqlSession 中的 全局 configuration 中 有个两个属性
          - mapperRegistry 
            - knownMappers  -----> 保存了  每一个 mapper接口  对应的 MapperProxyFactory 

    -  return mapperProxyFactory.newInstance(sqlSession);

      - newInstance(SqlSession sqlSession)   

        - 构建 返回的 MapperProxy   final MapperProxy<T> mapperProxy = new MapperProxy<T>(sqlSession, mapperInterface, methodCache);

        - MapperProxy    实现了 jdk的  InvocationHandler接口，通过jdk的 动态代理生成代理了类

          ​


### org.apache.ibatis.binding.MapperProxyFactory\<T>

```java
package org.apache.ibatis.binding;

public class MapperProxyFactory<T> {

  private final Class<T> mapperInterface;
  private final Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<Method, MapperMethod>();

  public MapperProxyFactory(Class<T> mapperInterface) {
    this.mapperInterface = mapperInterface;
  }

  public Class<T> getMapperInterface() {
    return mapperInterface;
  }

  public Map<Method, MapperMethod> getMethodCache() {
    return methodCache;
  }

  @SuppressWarnings("unchecked")
  protected T newInstance(MapperProxy<T> mapperProxy) {
    return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);
  }

  public T newInstance(SqlSession sqlSession) {
    final MapperProxy<T> mapperProxy = new MapperProxy<T>(sqlSession, mapperInterface, methodCache);
    return newInstance(mapperProxy);
  }

}
```

- newInstance(SqlSession sqlSession) 方法
  - 目的：
    - 获取 InvocationHandler 的实现类  MapperProxy   ，为下一步 走 jdk 的代理作准备
  - 实现
    - 构建 MapperProxy   ，调用 newInstance(mapperProxy)  方法
- newInstance(MapperProxy<T> mapperProxy)  方法
  - 目的：
    - 调用jdk的动态代理  生成mapperProxy 代理类
  - 实现：
    - Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);







## 执行mapper的抽象方法，运行sql语句

- 调用 MapperProxy  的   invoke(Object proxy, Method method, Object[] args) 方法
  - 目的：
    - 执行方法，运行 sql 语句
  - 实现：
    - 判断执行的方法是否是 继承自 Object 的方法，是则执行
    - 判断需要执行的方法是否是  mapper接口中的 默认实现的方法 ， 是则执行
    - 用 MapperMethod mapperMethod = cachedMapperMethod(method);  返回经过 缓存包装后MapperMethod
    - return mapperMethod.execute(sqlSession, args);  执行此方法，返回值 Object

### org.apache.ibatis.binding.MapperProxy\<T>

```java
package org.apache.ibatis.binding;

public class MapperProxy<T> implements InvocationHandler, Serializable {

  private static final long serialVersionUID = -6424540398559729838L;
  private final SqlSession sqlSession;
  private final Class<T> mapperInterface;
  private final Map<Method, MapperMethod> methodCache;

  public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map<Method, MapperMethod> methodCache) {
    this.sqlSession = sqlSession;
    this.mapperInterface = mapperInterface;
    this.methodCache = methodCache;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {

      if (Object.class.equals(method.getDeclaringClass())) {
        return method.invoke(this, args);

      } else if (isDefaultMethod(method)) {
        return invokeDefaultMethod(proxy, method, args);
      }

    } catch (Throwable t) {
      throw ExceptionUtil.unwrapThrowable(t);
    }


    final MapperMethod mapperMethod = cachedMapperMethod(method);
        return mapperMethod.execute(sqlSession, args);
  }

  private MapperMethod cachedMapperMethod(Method method) {
    MapperMethod mapperMethod = methodCache.get(method);
    if (mapperMethod == null) {
      mapperMethod = new MapperMethod(mapperInterface, method, sqlSession.getConfiguration());
      methodCache.put(method, mapperMethod);
    }
    return mapperMethod;
  }

  @UsesJava7
  private Object invokeDefaultMethod(Object proxy, Method method, Object[] args)
      throws Throwable {
    final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
        .getDeclaredConstructor(Class.class, int.class);
    if (!constructor.isAccessible()) {
      constructor.setAccessible(true);
    }
    final Class<?> declaringClass = method.getDeclaringClass();
    return constructor
        .newInstance(declaringClass,
            MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED
                | MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC)
        .unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(args);
  }

  /**
   * Backport of java.lang.reflect.Method#isDefault()
   */
  private boolean isDefaultMethod(Method method) {
    return (method.getModifiers()
        & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) == Modifier.PUBLIC
        && method.getDeclaringClass().isInterface();
  }
}

```







```java
package org.apache.ibatis.binding;

public class MapperMethod {

    public Object execute(SqlSession sqlSession, Object[] args) {
    Object result;
    switch (command.getType()) {
      case INSERT: {
    	Object param = method.convertArgsToSqlCommandParam(args);
        result = rowCountResult(sqlSession.insert(command.getName(), param));
        break;
      }
      case UPDATE: {
        Object param = method.convertArgsToSqlCommandParam(args);
        result = rowCountResult(sqlSession.update(command.getName(), param));
        break;
      }
      case DELETE: {
        Object param = method.convertArgsToSqlCommandParam(args);
        result = rowCountResult(sqlSession.delete(command.getName(), param));
        break;
      }
      case SELECT:
        if (method.returnsVoid() && method.hasResultHandler()) {
          executeWithResultHandler(sqlSession, args);
          result = null;
        } else if (method.returnsMany()) {
          result = executeForMany(sqlSession, args);
        } else if (method.returnsMap()) {
          result = executeForMap(sqlSession, args);
        } else if (method.returnsCursor()) {
          result = executeForCursor(sqlSession, args);
        } else {
          
          
          Object param = method.convertArgsToSqlCommandParam(args);
          result = sqlSession.selectOne(command.getName(), param);
          if (method.returnsOptional() &&
              (result == null || !method.getReturnType().equals(result.getClass()))) {
            result = OptionalUtil.ofNullable(result);
         
          }
        }
        break;
      case FLUSH:
        result = sqlSession.flushStatements();
        break;
      default:
        throw new BindingException("Unknown execution method for: " + command.getName());
    }
    if (result == null && method.getReturnType().isPrimitive() && !method.returnsVoid()) {
      throw new BindingException("Mapper method '" + command.getName() 
          + " attempted to return null from a method with a primitive return type (" + method.getReturnType() + ").");
    }
    return result;
  }

}
```

- execute(SqlSession sqlSession, Object[] args) 方法
  - 目的：
    - 执行此方法，获取此方法的返回值
  - 实现：
    - 传入包含了所有的 configuration 及 mapper.xml 的sqlSession ， 及执行此方法的参数
    - 获取 此sql 语句的 类型，执行对应的  INSERT   UPDATE   DELETE  SELECT  FLUSH
      - INSERT   
      - UPDATE   
      - DELETE  
      - FLUSH
      - SELECT  
        - 判断 此方法 返回值的类型
          - 没有返回值
          - 多个返回值
          - 返回map
          - 返回 光标 位置
          - 一个返回值
            - 获取此方法 的参数
            - 执行方法   result = sqlSession.selectOne(command.getName(), param)  -->  获取结果

### apache.ibatis.session.defaults.DefaultSqlSession

```java
package org.apache.ibatis.session.defaults;

public class DefaultSqlSession implements SqlSession {
  
  @Override
  public <T> T selectOne(String statement, Object parameter) {
    // Popular vote was to return null on 0 results and throw exception on too many.
    List<T> list = this.<T>selectList(statement, parameter);
    if (list.size() == 1) {
      return list.get(0);
    } else if (list.size() > 1) {
      throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
    } else {
      return null;
    }
  }
  
  @Override
  public <E> List<E> selectList(String statement, Object parameter) {
    return this.selectList(statement, parameter, RowBounds.DEFAULT);
  }
  
   @Override
  public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
    try {
      MappedStatement ms = configuration.getMappedStatement(statement);
      return executor.query(ms, wrapCollection(parameter), rowBounds, Executor.NO_RESULT_HANDLER);
    } catch (Exception e) {
      throw ExceptionFactory.wrapException("Error querying database.  Cause: " + e, e);
    } finally {
      ErrorContext.instance().reset();
    }
  }
  
}
```

- selectOne(String statement, Object parameter)  方法

  - 目的：

    - 获取结果

  - 实现

    - 调用 selectList(String statement, Object parameter)  获取结果集，中的第一个

      - 已确认只有一个返回值

        ​

- selectList(String statement, Object parameter)   方法

  - 目的：

    - 获取结果集合

  - 实现

    - 添加 RowBounds.DEFAULT 属性    --->    执行selectList(String statement, Object parameter, RowBounds rowBounds)

      - RowBounds.DEFAULT 这个属性是MySQL分页相关的   

        ​

-  selectList(String statement, Object parameter, RowBounds rowBounds)

   - 目的：
     - 获取结果集合
   - 实现：
     - 获取configuration中的 MappedStatement  对象  --->   MappedStatement ms = configuration.getMappedStatement(statement)  
       - 此对象 对应mapper.xml 中的 一个 增/删/查/改  
     - executor.query(ms, wrapCollection(parameter), rowBounds, Executor.NO_RESULT_HANDLER)
       - 目的：
         - 执行SQL，获取结果
       - 实现：
         - 利用执行器，执行query（...）  方法
           - 此时执行器 有三中类型（这篇搜索 执行器 有相关简介）
             - SIMPLE  简单类型
             - REUSE  可复用类型
             - BATCH 批量操作类型
           - 所有执行器 都是继承会被 CachingExecutor 进行 包装
             - 先走  CachingExecutor   执行器


### org.apache.ibatis.executor.CachingExecutor


```java
package org.apache.ibatis.executor;

public class CachingExecutor implements Executor {
  
   private final Executor delegate;

  @Override
  public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
    
    
    BoundSql boundSql = ms.getBoundSql(parameterObject);
    
    //--- 创建缓存的 key
    CacheKey key = createCacheKey(ms, parameterObject, rowBounds, boundSql);
    return query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
  }
  
  @Override
  public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql)
      throws SQLException {
    Cache cache = ms.getCache();
    if (cache != null) {
      flushCacheIfRequired(ms);
      if (ms.isUseCache() && resultHandler == null) {
        ensureNoOutParams(ms, boundSql);
        @SuppressWarnings("unchecked")
        List<E> list = (List<E>) tcm.getObject(cache, key);
        if (list == null) {
          list = delegate.<E> query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
          tcm.putObject(cache, key, list); // issue #578 and #116
        }
        return list;
      }
    }
    return delegate.<E> query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
  }

}
```

- query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler)  方法
  - 目的：
    - 获取 BoundSql
    - 创建 缓存的 key



- query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql)
  - 目的：
    - 先去缓存中拿  值，若拿到的值 为空 ，则调用  Executor 的 query(ms, parameterObject, rowBounds, resultHandler, key, boundSql)  方法
    - mybatis 默认的 Executor  接口的 实现类为  SimpleExecutor，则是调用 此类的  query(...)  方法
    - SimpleExecutor 中无 此方法 ，则是调用 其 父类 BaseExecutor  的方法



### package org.apache.ibatis.executor.SimpleExecutor

```java
package org.apache.ibatis.executor;

public class SimpleExecutor extends BaseExecutor {

	// --  无  query(...)  方法
  
  
  @Override
  public <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
    Statement stmt = null;
    try {
      
      //--- 获取configuration 
      Configuration configuration = ms.getConfiguration();
      
      // ----  获取StatementHandler
      StatementHandler handler = configuration.newStatementHandler(wrapper, ms, parameter, rowBounds, resultHandler, boundSql);
      
      stmt = prepareStatement(handler, ms.getStatementLog());
      return handler.<E>query(stmt, resultHandler);
      
    } finally {
      closeStatement(stmt);
    }
  }
  
  
  private Statement prepareStatement(StatementHandler handler, Log statementLog) throws SQLException {
    Statement stmt;
    Connection connection = getConnection(statementLog);
    stmt = handler.prepare(connection, transaction.getTimeout());
    handler.parameterize(stmt);
    return stmt;
  }

}
```



### package org.apache.ibatis.executor.BaseExecutor

```java
package org.apache.ibatis.executor;

public abstract class BaseExecutor implements Executor {

    @SuppressWarnings("unchecked")
  @Override
  public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql) throws SQLException {
    
    
    ErrorContext.instance().resource(ms.getResource()).activity("executing a query").object(ms.getId());
    if (closed) {
      throw new ExecutorException("Executor was closed.");
    }
    if (queryStack == 0 && ms.isFlushCacheRequired()) {
      clearLocalCache();
    }
    List<E> list;
    try {
      
      
      queryStack++;
      list = resultHandler == null ? (List<E>) localCache.getObject(key) : null;
      if (list != null) {
        
        
        handleLocallyCachedOutputParameters(ms, key, parameter, boundSql);
      } else {
        
        
        list = queryFromDatabase(ms, parameter, rowBounds, resultHandler, key, boundSql);
      }
      
      
    } finally {
      queryStack--;
    }
    if (queryStack == 0) {
      for (DeferredLoad deferredLoad : deferredLoads) {
        deferredLoad.load();
      }
      // issue #601
      deferredLoads.clear();
      if (configuration.getLocalCacheScope() == LocalCacheScope.STATEMENT) {
        // issue #482
        clearLocalCache();
      }
    }
    return list;
  }
  
  
  private void handleLocallyCachedOutputParameters(MappedStatement ms, CacheKey key, Object parameter, BoundSql boundSql) {
    if (ms.getStatementType() == StatementType.CALLABLE) {
      final Object cachedParameter = localOutputParameterCache.getObject(key);
      if (cachedParameter != null && parameter != null) {
        final MetaObject metaCachedParameter = configuration.newMetaObject(cachedParameter);
        final MetaObject metaParameter = configuration.newMetaObject(parameter);
        for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
          if (parameterMapping.getMode() != ParameterMode.IN) {
            final String parameterName = parameterMapping.getProperty();
            final Object cachedValue = metaCachedParameter.getValue(parameterName);
            metaParameter.setValue(parameterName, cachedValue);
          }
        }
      }
    }
  }
  
  // -----------查找 并存入缓存
   private <E> List<E> queryFromDatabase(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql) throws SQLException {
    List<E> list;
    localCache.putObject(key, EXECUTION_PLACEHOLDER);
    try {
      
      // ---- 执行执行器接口 中的方法  此处有具体的实现类  simple执行器执行
      list = doQuery(ms, parameter, rowBounds, resultHandler, boundSql);
    } finally {
      localCache.removeObject(key);
    }
    localCache.putObject(key, list);
    if (ms.getStatementType() == StatementType.CALLABLE) {
      localOutputParameterCache.putObject(key, parameter);
    }
    return list;
  }


}
```



### org.apache.ibatis.executor.statement.SimpleStatementHandler

```JAVA
package org.apache.ibatis.executor.statement;

public class SimpleStatementHandler extends BaseStatementHandler {

    @Override
  public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
    String sql = boundSql.getSql();
    statement.execute(sql);
    return resultSetHandler.<E>handleResultSets(statement);
  }
  
}
```



