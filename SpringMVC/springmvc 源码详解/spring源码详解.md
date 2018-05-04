---
title: springmvc源码详解
---

# springmvc源码详解

## 一、web容器（Tomcat） 与 springmvc  的联系

- ![Image 1](/Image 14.png)


- 都实现了 Javax.servlet 接口
- 启动web容器（Tomcat），获取请求（request），创建servlet实例
- request -->  tomcat  -->   servlet实例    --->进入springmvc 


- web容器获取request请求后，是如何传入springmvc的 ？
  - 通过继承关系  将request 请求 传入springmvc中处理





## 二、初始化 init()

- 第一步   初始化  是在 HTTPServletBean 的 init 方法中完成

  - 完成了 对接 servlet容器 （Tomcat ） 。将servlet中的参数传入

    ![Image 1](/Image 16.png)





### 传入参数实现的细节

#### 继承关系

- ![Image 1](/Image 1.png)

#### GenericServlet 

```java
package javax.servlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;

public abstract class GenericServlet implements Servlet, ServletConfig, Serializable {
```

- ​
- 抽象类   GenericServlet 中规范  servlet   三大方法
  -  init（），
  -  service（request，response），
  -  destroy（）

#### HttpServlet   

```java
public abstract class HttpServlet extends GenericServlet {
```

- ​

- 抽象类   HttpServlet   继承  GenericServlet 

- ```java
   public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
          HttpServletRequest request;
          HttpServletResponse response;
          try {
              request = (HttpServletRequest)req;
              response = (HttpServletResponse)res;
          } catch (ClassCastException var6) {
              throw new ServletException("non-HTTP request or response");
          }
     //======================================================================================
     //实现父类方法，此方法中调用本类  service(HttpServletRequest, HttpServletResponse)  方法
     //===================================================================================
          this.service(request, response);
     //=========================================================================================
       
      }
   ```
  ```java

  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
          String method = req.getMethod();
          long lastModified;
          if (method.equals("GET")) {
              lastModified = this.getLastModified(req);
              if (lastModified == -1L) {
                  this.doGet(req, resp);
              } else {
                  long ifModifiedSince;
                  try {
                      ifModifiedSince = req.getDateHeader("If-Modified-Since");
                  } catch (IllegalArgumentException var9) {
                      ifModifiedSince = -1L;
                  }

                  if (ifModifiedSince < lastModified / 1000L * 1000L) {
                      this.maybeSetLastModified(resp, lastModified);
                      this.doGet(req, resp);
                  } else {
                      resp.setStatus(304);
                  }
              }
          } else if (method.equals("HEAD")) {
              lastModified = this.getLastModified(req);
              this.maybeSetLastModified(resp, lastModified);
              this.doHead(req, resp);
          } else if (method.equals("POST")) {
              this.doPost(req, resp);
          } else if (method.equals("PUT")) {
              this.doPut(req, resp);
          } else if (method.equals("DELETE")) {
              this.doDelete(req, resp);
          } else if (method.equals("OPTIONS")) {
              this.doOptions(req, resp);
          } else if (method.equals("TRACE")) {
              this.doTrace(req, resp);
          } else {
              String errMsg = lStrings.getString("http.method_not_implemented");
              Object[] errArgs = new Object[]{method};
              errMsg = MessageFormat.format(errMsg, errArgs);
              resp.sendError(501, errMsg);
          }

      }
  ```



#### HttpServletBean

```
public abstract class HttpServletBean extends HttpServlet implements EnvironmentCapable, EnvironmentAware {
```

- ​
- HttpServletBean  继承    HttpServlet   
- init（）对应web.xml中的配置
- 接入  spring   框架


##### init()

######      		 this.initServletBean()   

```java
 public final void init() throws ServletException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Initializing servlet '" + this.getServletName() + "'");
        }

        try {
          
          //====servlet（Tomcat实现） 中封装的参数  封装到 PropertyValues 中
          //====requiredProperties : 必须的参数属性   如果没有就会报  异常
            PropertyValues pvs = new HttpServletBean.ServletConfigPropertyValues(this.getServletConfig(), this.requiredProperties);
          
          //=====属相编辑器： 用来控制 修改  getServletConfig()  中的属性
          //=====this  当前类 --->
            BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
            ResourceLoader resourceLoader = new ServletContextResourceLoader(this.getServletContext());
            bw.registerCustomEditor(Resource.class, new ResourceEditor(resourceLoader, this.getEnvironment()));
            this.initBeanWrapper(bw);
            bw.setPropertyValues(pvs, true);
        } catch (BeansException var4) {
            this.logger.error("Failed to set bean properties on servlet '" + this.getServletName() + "'", var4);
            throw var4;
        }

   //=====模板方法  子类   FrameworkServlet  来具体实现
        this.initServletBean();
   
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Servlet '" + this.getServletName() + "' configured successfully");
        }

    }
```

- ​



#### FrameworkServlet

- ```java
  public abstract class FrameworkServlet extends HttpServletBean implements ApplicationContextAware {
  ```


##### initServletBean()

######       this.initWebApplicationContext();  

```java
protected final void initServletBean() throws ServletException {
        this.getServletContext().log("Initializing Spring FrameworkServlet '" + this.getServletName() + "'");
        if (this.logger.isInfoEnabled()) {
            this.logger.info("FrameworkServlet '" + this.getServletName() + "': initialization started");
        }

        long startTime = System.currentTimeMillis();

        try {
          
          //=========初始化  Bean   ,  构造 springmvc  的自己 持有 的上下文 
            this.webApplicationContext = this.initWebApplicationContext();

            this.initFrameworkServlet();
        } catch (ServletException var5) {
            this.logger.error("Context initialization failed", var5);
            throw var5;
        } catch (RuntimeException var6) {
            this.logger.error("Context initialization failed", var6);
            throw var6;
        }

        if (this.logger.isInfoEnabled()) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            this.logger.info("FrameworkServlet '" + this.getServletName() + "': initialization completed in " + elapsedTime + " ms");
        }

    }
```



##### initWebApplicationContext()

###### 		 this.onRefresh(wac);  

```java
 protected WebApplicationContext initWebApplicationContext() {
   
   //===首先拿到 根上下文 
        WebApplicationContext rootContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
   
   //====springmvc : WebApplicationContext
        WebApplicationContext wac = null;
   
   //=====如果  springmvc 的上下文  不为 null --->   已经将基础上下文的设置进来
        if (this.webApplicationContext != null) {
    //=====  将其（构造好的上下文）赋值 给springmvc 上下文      
            wac = this.webApplicationContext;
            if (wac instanceof ConfigurableWebApplicationContext) {
                ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext)wac;
                if (!cwac.isActive()) {
                    if (cwac.getParent() == null) {
                      
   //==== 将  spring容器  设置进  springMVC容器   
   //===================---->   子容器  可以读取 父容器 
   //===================---->   父容器  不可以读取 子容器 
                        cwac.setParent(rootContext);
                    }

   //=   双亲体系
                    this.configureAndRefreshWebApplicationContext(cwac);
                }
            }
        }

        if (wac == null) {
            wac = this.findWebApplicationContext();
        }

        if (wac == null) {
            wac = this.createWebApplicationContext(rootContext);
        }

        if (!this.refreshEventReceived) {
          
  //=======就是根据事件  触发 这个 方法
          //====这个方法就是   构造上下文  的时间点
            this.onRefresh(wac);
        }

        if (this.publishContext) {
            String attrName = this.getServletContextAttributeName();
            this.getServletContext().setAttribute(attrName, wac);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Published WebApplicationContext of servlet '" + this.getServletName() + "' as ServletContext attribute with name [" + attrName + "]");
            }
        }

        return wac;
    }
```



### 初始化  Bean  信息

- 初始化   init（）   的最终目的  就是  初始化  Bean 信息

  - Controller
  - HandlerMapping
  - HandlerAdapter
  - ViewResolver
  - ...

  ​

- ![Image 1](/Image 17.png)




#### DispatcherServlet

```
public class DispatcherServlet extends FrameworkServlet {
```

##### onRefresh(ApplicationContext)

######                   this.initStrategies(context);  

```java
protected void onRefresh(ApplicationContext context) {
        this.initStrategies(context);
    }
```



##### initStrategies

```java
protected void initStrategies(ApplicationContext context) {
  
  //用于处理上传请求。处理方法是将普通的request包装成MultipartHttpServletRequest，后者可以直接调用getFile方法获取File.
        this.initMultipartResolver(context);
  
  
  //SpringMVC主要有两个地方用到了Locale：一是ViewResolver视图解析的时候；二是用到国际化资源或者主题的时候。
        this.initLocaleResolver(context);
  
  
  //用于解析主题。SpringMVC中一个主题对应一个properties文件，里面存放着跟当前主题相关的所有资源、
//如图片、css样式等。SpringMVC的主题也支持国际化， 
        this.initThemeResolver(context);
  
  
  //用来查找Handler的。
        this.initHandlerMappings(context);
  
  
  //从名字上看，它就是一个适配器。Servlet需要的处理方法的结构却是固定的，都是以request和response为参数的方法。
//如何让固定的Servlet处理方法调用灵活的Handler来进行处理呢？这就是HandlerAdapter要做的事情
        this.initHandlerAdapters(context);
  
  
  //其它组件都是用来干活的。在干活的过程中难免会出现问题，出问题后怎么办呢？
//这就需要有一个专门的角色对异常情况进行处理，在SpringMVC中就是HandlerExceptionResolver。
        this.initHandlerExceptionResolvers(context);
  
  
  //有的Handler处理完后并没有设置View也没有设置ViewName，这时就需要从request获取ViewName了，
//如何从request中获取ViewName就是RequestToViewNameTranslator要做的事情了。
        this.initRequestToViewNameTranslator(context);
  
  
  //ViewResolver用来将String类型的视图名和Locale解析为View类型的视图。
//View是用来渲染页面的，也就是将程序返回的参数填入模板里，生成html（也可能是其它类型）文件。
        this.initViewResolvers(context);
  
  
  //用来管理FlashMap的，FlashMap主要用在redirect重定向中传递参数。
        this.initFlashMapManager(context);
    }
```



### init（）总结

![Image 1](/Image 18.png)



## 三、服务  service（）

![Image 1](/Image 19.png)

### FrameworkServlet

#### processRequest(HttpServletRequest, HttpServletResponse )

##### this.doService(request, response);

-

```java
protected final void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
  
        long startTime = System.currentTimeMillis();
        Throwable failureCause = null;
  
  
        LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
        LocaleContext localeContext = this.buildLocaleContext(request);
  
  //--------拿到  request 的 一些 属性
        RequestAttributes previousAttributes = RequestContextHolder.getRequestAttributes();
  //---------将这些 属相 封装 到 ServletRequestAttributes  当中
        ServletRequestAttributes requestAttributes = this.buildRequestAttributes(request, response, previousAttributes);
  
  //--------安全检查
        WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
        asyncManager.registerCallableInterceptor(FrameworkServlet.class.getName(), new FrameworkServlet.RequestBindingInterceptor(null));
  
  
        this.initContextHolders(request, localeContext, requestAttributes);

        try {
 //==============处理
            this.doService(request, response);
          
          
          
          
        } catch (ServletException var17) {
            failureCause = var17;
            throw var17;
        } catch (IOException var18) {
            failureCause = var18;
            throw var18;
        } catch (Throwable var19) {
            failureCause = var19;
            throw new NestedServletException("Request processing failed", var19);
        } finally {
            this.resetContextHolders(request, previousLocaleContext, previousAttributes);
            if (requestAttributes != null) {
                requestAttributes.requestCompleted();
            }

            if (this.logger.isDebugEnabled()) {
                if (failureCause != null) {
                    this.logger.debug("Could not complete request", (Throwable)failureCause);
                } else if (asyncManager.isConcurrentHandlingStarted()) {
                    this.logger.debug("Leaving response open for concurrent processing");
                } else {
                    this.logger.debug("Successfully completed request");
                }
            }

            this.publishRequestHandledEvent(request, response, startTime, (Throwable)failureCause);
        }

    }
```





### DispatcherServlet

#### doService(HttpServletRequest request, HttpServletResponse response)



- ```java
  protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
    
          if (this.logger.isDebugEnabled()) {
            
              String resumed = WebAsyncUtils.getAsyncManager(request).hasConcurrentResult() ? " resumed" : "";
              this.logger.debug("DispatcherServlet with name '" + this.getServletName() + "'" + resumed + " processing " + request.getMethod() + " request for [" + getRequestUri(request) + "]");
            
          }

          Map<String, Object> attributesSnapshot = null;
    
          if (WebUtils.isIncludeRequest(request)) {
            
              attributesSnapshot = new HashMap();
              Enumeration attrNames = request.getAttributeNames();

              label108:
              while(true) {
                
                  String attrName;
                
                  do {
                    
                      if (!attrNames.hasMoreElements()) {
                          break label108;
                      }

                      attrName = (String)attrNames.nextElement();
                  } while(!this.cleanupAfterInclude && !attrName.startsWith("org.springframework.web.servlet"));

                  attributesSnapshot.put(attrName, request.getAttribute(attrName));
              }
          }

   //===========  保存  springMVC的 九大组件  到本地=======================================
   //===========  保存  springMVC的 九大组件  到本地 ：  方便本地去拿=======================
    
    //=------------将  上下文   保存进  request
          request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.getWebApplicationContext());
     //=------------将  本地解析器   保存进  request
          request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, this.localeResolver);
    //=------------将  主题解析器   保存进  request
          request.setAttribute(THEME_RESOLVER_ATTRIBUTE, this.themeResolver);
    
          request.setAttribute(THEME_SOURCE_ATTRIBUTE, this.getThemeSource());
    
          FlashMap inputFlashMap = this.flashMapManager.retrieveAndUpdate(request, response);
    
          if (inputFlashMap != null) {
              request.setAttribute(INPUT_FLASH_MAP_ATTRIBUTE, Collections.unmodifiableMap(inputFlashMap));
          }

          request.setAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE, new FlashMap());
          request.setAttribute(FLASH_MAP_MANAGER_ATTRIBUTE, this.flashMapManager);

          try {
            
            
     //============  业务处理  执行  =======================
              this.doDispatch(request, response);
     //============  业务处理  执行     ====================================
            
             
            
          } finally {
              if (!WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted() && attributesSnapshot != null) {
                  this.restoreAttributesAfterInclude(request, attributesSnapshot);
              }

          }

      }
  ```



####  this.doDispatch(request, response)

```java
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpServletRequest processedRequest = request;
  
  //---------构建  处理请求执行链
        HandlerExecutionChain mappedHandler = null;
  
  //------判断  是否为 上传 请求的标志
        boolean multipartRequestParsed = false;
  //------安全检查
        WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);

        try {
            try {
                ModelAndView mv = null;
                Object dispatchException = null;

                try {
                  
    //===判断 是否  文件上传 请求
                    processedRequest = this.checkMultipart(request);
                    multipartRequestParsed = processedRequest != request;
                  
   //--------------根据  request    找到   HandlerExecutionChain(  处理请求执行链)
                    mappedHandler = this.getHandler(processedRequest);
                    if (mappedHandler == null || mappedHandler.getHandler() == null) {
                        this.noHandlerFound(processedRequest, response);
                        return;
                    }
                  
   //-----------------根据  HandlerExecutionChain(执行链)   找到   HandlerAdapter  适配器
                    HandlerAdapter ha = this.getHandlerAdapter(mappedHandler.getHandler());
                   
   //-----------------处理  get head 请求的 Last-Modified
                  String method = request.getMethod();
                  boolean isGet = "GET".equals(method);
                  //---是否使用 缓存页面
                    if (isGet || "HEAD".equals(method)) {
                        long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Last-Modified value for [" + getRequestUri(request) + "] is: " + lastModified);
                        }
                        if ((new ServletWebRequest(request, response)).checkNotModified(lastModified) && isGet) {
                            return;
                        }
                    }

  //-------------------执行 相应的  interceptor 的  perHandle
                    if (!mappedHandler.applyPreHandle(processedRequest, response)) {
                        return;
                    }
  //-------------------HandlerAdapter 使用  handler处理 请求   返回ModelAndView
                    mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
                 //---若 需要 异步  处理  则 直接 返回
                    if (asyncManager.isConcurrentHandlingStarted()) {
                        return;
                    }
//------------若 返回ModelAndView 为  null  时（比如返回 void） ，则根据request设置 默认 view
                    this.applyDefaultViewName(processedRequest, mv);
                    mappedHandler.applyPostHandle(processedRequest, response, mv);
                  
                } catch (Exception var20) {
                    dispatchException = var20;
                } catch (Throwable var21) {
                    dispatchException = new NestedServletException("Handler dispatch failed", var21);
                }

              
 //--  处理返回结果《包括 处理异常  渲染页面  发出完成通知  触发interceptor 的 afterCompletion
                this.processDispatchResult(processedRequest, response, mappedHandler, mv, (Exception)dispatchException);
              
              
            } catch (Exception var22) {
                this.triggerAfterCompletion(processedRequest, response, mappedHandler, var22);
            } catch (Throwable var23) {
                this.triggerAfterCompletion(processedRequest, response, mappedHandler, new NestedServletException("Handler processing failed", var23));
            }

        } finally {
            if (asyncManager.isConcurrentHandlingStarted()) {
                if (mappedHandler != null) {
                    mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
                }
            } else if (multipartRequestParsed) {
              
   //---------------如果为  上传 请求  ，则执行  删除上传请求的资源
                this.cleanupMultipart(processedRequest);
            }

        }
    }
```



