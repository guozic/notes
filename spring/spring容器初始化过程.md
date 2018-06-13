# spring容器初始化过程

## web.xml 项目配置 一

```xml
<listener>
  <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

- 进行加载ContextLoaderListener



### org.springframework.web.context.ContextLoaderListener

- 实现了接口ServletContextListener（）
- 继承了 类 ContextLoader

```java
package org.springframework.web.context;

public class ContextLoaderListener extends ContextLoader implements ServletContextListener {


  // ----  在容器初始化上下文的时候调用的  -----
  @Override
  public void contextInitialized(ServletContextEvent event) {
    initWebApplicationContext(event.getServletContext());
  }

  //  ----   容器销毁上下文的时候调用的  ------
  @Override
  public void contextDestroyed(ServletContextEvent event) {
    closeWebApplicationContext(event.getServletContext());
    ContextCleanupListener.cleanupAttributes(event.getServletContext());
  }


}
```



###  javax.servlet.ServletContextListener

```java
package javax.servlet;

public interface ServletContextListener extends EventListener {
    void contextInitialized(ServletContextEvent var1);

    void contextDestroyed(ServletContextEvent var1);
}
```



### org.springframework.web.context.ContextLoader

```java
package org.springframework.web.context;
public class ContextLoader {



   // --   获取默认策略的值。值放在ContextLoader.properties文件中。    
  //  -- spring-web-4.3.7.RELEASE.jar包-org.springframework.web.context.ContextLoader.properties
  
  static {
		// Load default strategy implementations from properties file.
		// This is currently strictly internal and not meant to be customized
		// by application developers.
		try {
			ClassPathResource resource = new ClassPathResource(DEFAULT_STRATEGIES_PATH, ContextLoader.class);
			defaultStrategies = PropertiesLoaderUtils.loadProperties(resource);
		}
		catch (IOException ex) {
			throw new IllegalStateException("Could not load 'ContextLoader.properties': " + ex.getMessage());
		}
	}

}
```





### org.springframework.web.context.ContextLoader.properties

- ​

```properties
<-- WebApplicationContext的实例化对象为XmlWebApplicationContext类。  !-->
org.springframework.web.context.WebApplicationContext=org.springframework.web.context.support.XmlWebApplicationContext

```



## web.xml 项目通过配置 二

```xml
<!-- 配置让servlet容器去加载spring的核心配置文件 -->
<context-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>classpath:spring/spring.xml</param-value>
</context-param>
```



