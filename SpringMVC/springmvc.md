# [Spring MVC学习](http://www.cnblogs.com/zhuxiaojie/p/4619804.html)

# SpringMVC框架

 



## 一：配置SpringMVC开发环境

```
任何mvc的框架，都是需要在web.xml中进行核心的配置的，struts2是一个过滤器，而SpringMVC则是一个servlet，但是配置方法大同小异，都是在web.xml中进行配置，配置方法如下：

```



```xml
<servlet>
  <servlet-name>springMVC</servlet-name>
  <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  <!-- 配置DispatcherServlet的一个初始化参数：配置SpringMVC配置文件位置和名称，这个也可以不配置，如果不配置，则默认是WEB-INFO下的springMVC-servlet.xml -->
  <init-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:abc.xml</param-value>
  </init-param>
  <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
  <servlet-name>springMVC</servlet-name>
  <!-- 拦截所有url -->
  <url-pattern>/</url-pattern>
</servlet-mapping>
```



再然后就是配置springMVC-servlet.xml，这个文件名之前说过，可以自定义路径与名称，如果不自定义，则一定要放在WEB-INFO下，注意引入正确的命名空间，然后加入核心分解解析器，就是解析url的请求，类似于stuts的配置文件，配置如下：

 



```xml
<!-- 视图分解解析器 -->
    <bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <!-- 这是前缀 -->
        <property name="prefix" value="/"></property>
        <!-- 这是后缀 -->
        <property name="suffix" value=".jsp"></property>
        <!-- 在spring的控制器中，返回的是一个字符串，那么请求的路径则是，前缀+返回字符串+后缀 -->
    </bean>
```



然后就是正式使用SpringMVC了，我们通常是用注解的方式，因为注解真的太好用了，省事好多，但是这里也简单介绍一下使用xml配置文件的方式来写一个简单的helloworld

### 1.1.配置文件的helloworld

 首先新建一个类，这个类要实现Controller接口，然后实现它的一个ModelAndView方法，代码如下：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
public class HelloWroldController implements Controller {
    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        System.out.println("spring ModelAndView");
        return new ModelAndView("/welcome");
    }
}
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

然后在springMVC-servlet.xml中配置一个bean：配置方法如下：

```
<bean name="/test" class="controller.HelloWroldController"></bean>
```

这种配置和struts2其实是一样的，name属性就是浏览器请求的url，而class就是它处理的类，而运行的方法，就是上面所继承的接口，然后在浏览器输入http://localhost:8080/springMVC/test，就可以跳转到welcome.jsp下，这是根据之前配置的视图分解解析器的前后缀前该方法返回的字符串拼接而成的。

###  1.2.基于注解的helloworld

使用注解就要简单得多了，首先是在springMVC-servlet.xml中配置扫描的包，这样spring就会自动根据注解来描述一些特定的注解，然后把这些bean装载进入spring容器中，配置如下：

 

```
    <!-- 定义扫描的包 -->
    <context:component-scan base-package="zxj"></context:component-scan>
```

然后，在指定的包，或者其子包下新建一个类，代码如下：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @Controller是控制器的注解
 * @author jie
 */
@Controller
public class Control {
    /**
     * 这里配置了RequestMapping注解，就相当于配置了struts2的action，也就相当于url地址
     * 其返回的地址为spingMVC-servlet.xml中，前缀+spinrg+后缀
     * @return
     */
    @RequestMapping("/helloworld")
    public String helloworld(){
        return "spring";
    }
}
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

这样就不用再写配置文件，只要加上相应的注解就行，比如说这里的helloworld方法，只要在浏览器输入http://localhost:8080/project/hellowrold，就可以进入spring.jsp页面

 

## 二：@RequestMapping详解

### 2.1.类的@RequestMapping注解

刚才看到了@RequestMapping这个注解是用于给一个方法加上url的请求地址，不过，这个注解不仅仅可以加在方法上面，也可以加在类上面，代码如下图所示：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/springMVC")
@Controller
public class SpringMVCTest {
    private static final String SUCCESS = "success";
    
    @RequestMapping("/testRequestMapping")
    public String testRequestMapping(){
        System.out.println("testRequestMapping");
        return "spring";
    }
}
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

如上代码如示，如果同时在类与方法上面同时加上@RequestMapping注解的话，那么URL肯定不会是之前的那种写法了，现在要写成类的注解加上方法的注解，就是有点类似struts2中，<package>的nameplace属性，那如如上代码，URL应该为http://localhost:8080/project/springMVC/testReuqestMapping

 

### 2.2.请求方式的设置

像我们平常提交一个表单，肯定会有get,与post这两个常用的请求，那么在springMVC中要如何设置呢？很简单，也是在@RequestMapping中使用，不过需要在方法的注解上面使用，代码如下：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
    /**
     * 使用method属性来指定请求方式
     * @return
     */
    @RequestMapping(value="/testMethod",method=RequestMethod.POST)
    public String testMethod(){
        System.out.println("testMethod");
        return "spring";
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

这下在from表单中，或者异步请求中，就需要以post做为请求，不然会报错的，因为这里已经设置为了post，如果客户端再请求get，将会报错。

 

### 2.3.参数的规定与请求头的规定设置

我们都知道http请求会有着请求参数与请求头消息，那么在springMVC里面，是可以规范这些信息的，首先给一段代码示例：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
    /**
     * 代表着这个请求中参数必须包含username参数，而且age不能等于10
     * 而且请求头中，请求的语言为中文，时区为东八区，否则就报错，不允许请求
     * @return
     */
    @RequestMapping(value="testParamsAndHeader",params={"username","age!=10"},headers={"Accept-Language=zh-CN,zh;q=0.8"})
    public String testParamsAndHeader(){
        System.out.println("testParamHeader");
        return "spring";
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

无论是params，还是headers，都可以包含多个参数，以逗号相隔就行，如果不满足写了的条件，则就会报错，不允许请求资源。其次这两个属性还支持一些简单的表达式：

```
user  表示请求中，必须包含user的参数
!user 表示请求中，不能包含user的参数
user!=admin    表示请示中，user参数不能为admin
user,age=10    表示请求中必须包含user,age这两个参数，而且age要等于10
```

 

### 2.4.@RequestMapping映射中的通配符

在@RequestMapping的value属性中，还支持Ant格式的能配符：

```
?        匹配url中的任意一个字符
*        匹配url中的任意多个字符
**       匹配url中的多层路径
```

下面举几个例子：

```
/user/*/createUser    匹配/user/abcd/createUser
/user/**/createUser   匹配/user/aa/bb/cc/createUser
/user/?/createUser    匹配/user/a/createUser
```

这里就不作代码演示了，相信大家一看就懂，因为这种通配符真的太常见了

 

### 2.5.@RequestMapping与@PathVariable注解的一起使用

springMVC很灵活，它可以获取URL地址中的值，然后当作变量来输出，这里要使用@PathVariable注解，故名思意，就是路径变量的意思，通常的话，@PathVariable要使用，是一定要与@RequestMapping一起使用的，下面给出代码示例：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
    /**
     * 先在方法上面映射URL，这里面可以用一个占位符，然后在参数中用@PathVariable来获取此占位符的值
     * @param id
     * @return
     */
    @RequestMapping("testPathVariable/{id}")
    public String testPathVariable(@PathVariable("id") Integer id){
        System.out.println("PathVariable:"+id);
        return "spring";
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

先在括号中加上注解，其中value就是@RequestMapping中占位符的声明，然后加上数据类型和定义的变量，这样就可以对其进行使用了

 

### 2.6.Rest风格的URL

通常的话，表单有着post，与get提交方式，而rest风格的URL，则有着get,post,put,delete这四种请求方式，但是浏览器却是只支持get与post，所以我们可以使用springMVC，把它进行转换，我们要利用org.springframework.web.filter.HiddenHttpMethodFilter这个类，这是一个过滤器，我们首先要在web.xml中配置它，请配置在第一个位置，不然的话，可能会先进入其它的过滤器，配置代码如下：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
<!-- 配置org.springframework.web.filter.HiddenHttpMethodFilter，可以把post请求转为delete或put请求 -->
    <filter>
        <filter-name>HiddenHttpMethodFilter</filter-name>
        <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>HiddenHttpMethodFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

如上的配置，这个过滤器则会拦截所有的请求，我们可以看一下org.springframework.web.filter.HiddenHttpMethodFilter的部分源代码：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String paramValue = request.getParameter(this.methodParam);//this.methodParam是一个final常量，为_method
        if (("POST".equals(request.getMethod()))
                && (StringUtils.hasLength(paramValue))) {
            String method = paramValue.toUpperCase(Locale.ENGLISH);
            HttpServletRequest wrapper = new HttpMethodRequestWrapper(request,
                    method);
            filterChain.doFilter(wrapper, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

当这个过滤器拦截到一个请求时，就会先拿到这个请求的参数，它要满足两个条件，第一，浏览器发出的请求为post请示，第二，它还要有一个参数，参数名为_method，而它的值，则可以为get,post,delete,put，此时过滤器就会把post请求转换成相应的请求，不然的话就不进行转换，直接请求。至于添加_method参数的话，则可以使用hidden隐藏域，或者使用?拼接参数都可以。

下面就该是控制器的方法了，在本处第2小点中，有讲到@RequestMapping的请求方式的设置，只要把这个请求方式设置成对应的请求就行，比如说转换成了DELETE请求，则@RequestMapping也要写成对应的DELETE请求，不然会出错，示例代码如下：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
    @RequestMapping(value="/testRest/{id}",method=RequestMethod.DELETE)
    public String testRestDelete(@PathVariable("id") Integer id){
        System.out.println("test DELETE:"+id);
        return "spring";
    }
    @RequestMapping(value="/testRest/{id}",method=RequestMethod.PUT)
    public String testRestPut(@PathVariable("id") Integer id){
        System.out.println("test put:"+id);
        return "spring";
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

此时，就可以正确转换请求方式了。

 

 

## 三：获取http请求中的信息

### 3.1.获取请求中的参数，@RequestParam

在获取类似这种:http://localhost:8080/project/test?user=a&password=2这种请求参数时，就需要用@RequestParam这个注解来获取，代码如下：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
    /**
     * 使用@RequestParam注解来获取请求参数
     *         value属性       参数名
     *         required       是否非空，默认为true，如果请求中无此参数，则报错，可以设置为false
     *         defaultValue 默认值，当浏览器没有带此参数时，该值会有一个默认值
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value="/testRequestParam")
    public String testRequestParam(@RequestParam("username") String username,
            @RequestParam(value="password",required=false,defaultValue="我是默认值") String password
            ){
        System.out.println(username);
        System.out.println(password);
        return "spring";
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

如上面注释所写，它常用这三个属性，value是参数名，但是如果只写了参数名的话，请求时，就必须带此参数，不然就会报错。如果把required属性设置为false，就可以使得该参数不传，还有defaultValue属性，此属性可以当浏览器没有传此参数时，给这个参数一个默认值

 

### 3.2.获取请求头的信息,@RequestHeader

之前也有提到过http的请求头的信息，那么这里就可以使用@RequestHeader注解来获取请求头的信息，它的使用方法与上面的@RequestParam是完全一样的，同样拥有value,requied,defaultValue这三个属性，而有代表的作用也是一样的，下面给出代码示例：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
    /**
     * 使用@RequestHeader注解来获取请求头的信息
     * 它与@RequestParam的使用方法是一样的，都有着三个参数,value,required,defaultValue
     * @param header
     * @return
     */
    @RequestMapping("testRequestHeader")
    public String testRequestHeader(@RequestHeader(value="Accept-Language",required=false,defaultValue="null") String header){
        System.out.println(header);
        return "spring";
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

因为它的用法与本章第一点的获取请求参数的用法一样，所以这里就不作过多的说明，详细可以查看@RequestParam的用法

 

### 3.3.获取Cookie的信息，@CookieValue

在开发中，有很多情况都会用到Cookie，这里可以使用@CookieValue注解来获取，其使用方法与@RequestParam与@RequestHeader一样，这里就不过多叙述，给出示例代码：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
    /**
     * 使用@CookieValue注解来获取浏览器传过来的Cookie
     * 它与@RequestHeader与@RequestParam的用法一样，也是有着required与default两个属性来指定是否为空与默认值
     * @param value
     * @return
     */
    @RequestMapping("testCookieValue")
    public String testCookieValue(@CookieValue(value="JSESSIONID",required=true,defaultValue="no") String value){
        System.out.println("CookieValue:"+value);
        return "spring";
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

它也有着三个属性，value,required,defaultValue，分别对应Cookie名，是否非空，默认值。

 

### 3.4.使用Pojo来获取请求中的大量参数

如果http请求中只有一两个参数，那么使用@RequestParam还可以，但是如果一个请求中带有着大量的参数，那么这样就有点麻烦了，那么springMVC就可以使用Pojo对象来获取这次请求中的所有参数，并且全部封装到这个对象里面，这种方式类似struts2的ModelDriver<T>，相信使用过struts2的同学都清楚，这种方式极其简便，下面一边给代码，一边解释，首先给出请求的处理方法：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
    /**
     * 使用Pojo对象来传递参数，因为如果一个请求中包含了大量的参数，那么全部用@RequestParam来做肯定太麻烦<br>
     * 这里可以在参数中定义一个实体类，实体类中对应着属性，springMVC就会把从浏览器获取到的参数全部封装到这个对象里面<br>
     * 而且这里面的参数可以为空，而且还支持级联参数（就是指下面User类中的属性还对应了一个Address的类）
     * @param user
     * @return
     */
    @RequestMapping("testPojo")
    public String testPojo(User user){
        System.out.println(user);
        return "spring";
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

这里无需使用其它的注解，只需要在这个处理方法中加上一个类就行，那么springMVC就会自动把请求参数封装到你写好的类中，而且这种封装还支持级联操作，什么是级联操作呢？就是User类中的属性还有着另外的一个类，下面给出User类的代码：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
public class User {
    private String username;
    private String password;
    private String email;
    private String age;
    private Address address;
    
    ...省略get,set方法
}
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

由上面可见，其中不仅有普通的属性，还有着一个Address的类，我们再来看一下Address类的代码：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
public class Address {
    private String province;
    private String city;
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    ...省略get,set方法
}
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

可以很清楚的看清User类与Address类的关系，那么像这种关系的对象，在浏览器form表单中的name属性该如何写呢？Address类中的字段，要加上address，比如address.province，或者address.city，其它的属性，就直接写User类中的属性就可以了。而这里为什么Address变成了小写的呢？其实这并不是什么命名规则，而是我在User类中就是这么定义的

这下没有什么问题了吧，我们再来看一下浏览器表单是怎么写的:

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
    <form action="${pageContext.request.contextPath}/testPojo" method="post">
          username:<input type="text" name="username"/><br/>
          password:<input type="password" name="password"/><br/>
          email:<input type="text" name="email"/><br/>
          age:<input type="text" name="age"/><br/>
          <!-- POJO支持级联属性，所以name属性是如下的写法，address.province，意思就是有一个address的类，类里面有province属性 -->
          province:<input type="text" name="address.province"/><br/>
          city:<input type="text" name="address.city"/><br/>
          <input type="submit" value="提交" />
      </form>
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 如上表单元素就可以看到表单的name属性是如何与User类对应的，其Address类中的属性，就以address.city。

下面来说一下这种请求方式的特点：

　　1.简便，不需要大量的@RequestParam注解。

　　2.与struts2的ModelDriver的用法差不多，只不过ModelDriver是接口，整个类里面所有的方法都可以使用。而springMVC中这个Pojo对象的作用仅仅是当前的处理方法中。

　　3.这种Pojo的使用中，浏览器的参数可以为空，就是可以不传参数，也不会报错，不像@RequestParam，如果不指定requried=false的话，还会报错。

 

## 四：Servlet原生API

### 4.1.使用Servlet原生API

在我们平常使用springMVC中，虽然说springMVC已经帮我们做了很多工作了，但是我们实际中还是会要用到Servlet的原生API，那这个时候要如何得到Servlet的原生对象呢？这里与struts2不同，springMVC是在方法中声明对应的参数来使用这些对象，而struts2则是调用相应的方法来得到这些对象。当然，对于没有学过struts2的同学，可以忽略，下面给出代码示例：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
    @RequestMapping("testServletAPI")
    public String testServletAPI(HttpServletRequest request,HttpServletResponse response,User user){
        System.out.println(request+"  "+response);
        System.out.println(user);
        System.out.println("testServletAPI");
        return "spring";
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

如上代码所示，直接在对应的处理方法里面声明这些需要使用的对象就可以了，那如果同时要使用Pojo来获得请求参数怎么办呢？这个不用担心，照常使用就行了，如上代码所示，同样声明了一个User类来接收参数，并不会有任何的影响。

 

### 4.2.使用Servlet原生API的原理（部分springMVC的源代码）

如果你想问，springMVC中的处理方法，里面可以支持哪些Servlet的原生API对象呢？或者你又想问，为什么可以照常的使用Pojo来获取请求参数呢？那么这里，我们先来看一下springMVC的源代码，然后再作解释：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
protected Object resolveStandardArgument(Class<?> parameterType,
                NativeWebRequest webRequest) throws Exception {
            HttpServletRequest request = (HttpServletRequest) webRequest
                    .getNativeRequest(HttpServletRequest.class);
            HttpServletResponse response = (HttpServletResponse) webRequest
                    .getNativeResponse(HttpServletResponse.class);

            if ((ServletRequest.class.isAssignableFrom(parameterType))
                    || (MultipartRequest.class.isAssignableFrom(parameterType))) {
                Object nativeRequest = webRequest
                        .getNativeRequest(parameterType);
                if (nativeRequest == null) {
                    throw new IllegalStateException(
                            "Current request is not of type ["
                                    + parameterType.getName() + "]: " + request);
                }
                return nativeRequest;
            }
            if (ServletResponse.class.isAssignableFrom(parameterType)) {
                this.responseArgumentUsed = true;
                Object nativeResponse = webRequest
                        .getNativeResponse(parameterType);
                if (nativeResponse == null) {
                    throw new IllegalStateException(
                            "Current response is not of type ["
                                    + parameterType.getName() + "]: "
                                    + response);
                }
                return nativeResponse;
            }
            if (HttpSession.class.isAssignableFrom(parameterType)) {
                return request.getSession();
            }
            if (Principal.class.isAssignableFrom(parameterType)) {
                return request.getUserPrincipal();
            }
            if (Locale.class.equals(parameterType)) {
                return RequestContextUtils.getLocale(request);
            }
            if (InputStream.class.isAssignableFrom(parameterType)) {
                return request.getInputStream();
            }
            if (Reader.class.isAssignableFrom(parameterType)) {
                return request.getReader();
            }
            if (OutputStream.class.isAssignableFrom(parameterType)) {
                this.responseArgumentUsed = true;
                return response.getOutputStream();
            }
            if (Writer.class.isAssignableFrom(parameterType)) {
                this.responseArgumentUsed = true;
                return response.getWriter();
            }
            return super.resolveStandardArgument(parameterType, webRequest);
        }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

从这里就可以说明了一个问题了，springMVC首先会通过反射技术得到这个方法里面的参数（源代码没有贴上，有兴趣的可以自行查看springMVC的源代码），然后比较这些参数的类型，是否与上面的九个类型想匹配，如果匹配成功，则返回这个对象，请注意，是与对象类型相匹配，而不是与形参名作匹配，所以这样，就不会使得Pojo无法工作了

 

### 4.3.springMVC支持哪些原生API

其实从4.2中的源代码中也是可以看到了，这里支持九种个对象，对应关系分别是：

 

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
ServletRequest          --        ServletRequest
ServletResponse         --　　　　 ServletResponse
HttpSession             --        HttpSession
Principal               --        request.getUserPrincipal()
Locale                  --        RequestContextUtils.getLocale(request);
InputStream             --        request.getInputStream();
Reader                  --        request.getReader();
OutputStream            --        response.getOutputStream();
Writer                  --        response.getWriter();    
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

 

## 五：页面中的传值

### 5.1.简单的使用ModelAndView来向页面传递参数

实际开发中，总要大量的向页面中传递后台的数据，那么存放数据的方法也有多种多样，比如说存到request中，大家都知道是使用request.setAtrribute()来向request域中传递值，但是实际上springMVC给我们封装了更好的方法，那就是使用ModelAndView。

首先，方法的返回值，该由String变成ModelAndView，然后在处理方法中new一个ModelAndView对象，然后返回这个对象就可以了，对象中可以增加返回页面的字符，也可以向这个对象里面传递参数，现在给出一个简单的示例代码：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
    /**
     * 使用ModelAndView来对页面传值<br>
     * 注意：org.springframework.web.servlet.ModelAndView;
     * 而不是org.springframework.web.portlet.ModelAndView;
     * @return
     */
    @RequestMapping("testModelAndView")
    public ModelAndView testModelAndView(){
        String viewName = "spring";
        ModelAndView modelAndView = new ModelAndView(viewName);
        //添加模型数据到ModelAndView中，数据存在request中
        modelAndView.addObject("time", new Date());
        modelAndView.setViewName("spring");
        return modelAndView;
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

如上代码如示，我们可以使用构造方法给它传一个值，那就是它最终要返回的页面的值，或者使用setViewName方法来给它一个返回页面的名字。使用addObject方法来给这个模型添加数据，这是一个键值对的数据，然后返回这个ModelAndView对象。

 

### 5.2.使用参数Map来向页面传值

可以在执行方法中定义一个Map参数，然后在方法中，向map添加内容，然后在页面中根据map的键来取对应的值，也是存在request域中，下面给出代码示例：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
/**
     * 使用参数map来向页面传值<br>
     * 页面中取值的方法${requestScope.names},${requestScope.nation}
     * @param map
     * @return
     */
    @RequestMapping("testMap")
    public String testMap(Map<String, Object> map ){
        map.put("names", Arrays.asList("zxj","lj","ly"));
        map.put("nation", "han");
        return "spring";
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

这里面就是在方法中有着一个Map类型的参数，其实不仅仅可以是Map类型，还可以是Model与ModelMap类型的，因为最终传入的根本就不是Map，而是

org.springframework.validation.support.BindingAwareModelMap

 

### 5.3.使用@SessionAttributes注解，把值放到session域中

其实5.1和5.2所诉的内容，虽然是把后台数据传递到前台，但是全部都是放置到request域中，这里讲诉一下使用@SessionAtrributes注解，把后台数据同时放到session中。

首先介绍一下这个注解，这个注解只能放在类上面，而不能放在方法上面。它有两个属性，一个是value属性，一个是types属性，这两个数据都是数组，所以可以声明多个，其实不论是value属性，还是types属性，都可以把数据同时放到session域中，只不过value属性对应的是执行方法中的map集合的键，只要是对应的键，那么其值，也会同时被传递到session域中（这里之所以加上“同时”这个词，是因为它会同时存到request与session中），而types属性，则是放置类的集合，只要map集合中存的是该类的数据，则也会同时被放到request中，下面给示例代码：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
/**
 * @Controller是控制器的注解
 * @SessionAtrributes是session的注解，使用它，就能把map中对应的值，放到session域中
 * @author 朱小杰
 */
@SessionAttributes(value={"user"},types={String.class})
@Controller
public class Control {
        /**
     * 在类上面使用@SessionAtrributes注解，把map集合中的值放到session域中
     * @param map
     * @return
     */
    @RequestMapping("testSessionAtrributes")
    public String testSessionAtrributes(Map<String,Object> map)    {
        User user = new User("zxj","123","1@qq.com","11");
        map.put("user", user);
        map.put("school", "xinxi");
        return "spring";
    }
}
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

如上代码所示，先是在类上面使用了@SessionScope注解，然后同时使用了value与types属性，第一个value属性的值"user"，则是testSessionAtrributes方法中map集合中的"user"的键，所以这一个键值对会被同时放入session域中，而第二个types的属性中的String.class，则是代表着这个类了，意思就是说，只要是map集合中放的String类型的数据，都会被放到session中。

 注意：使用了value属性后，这个属性也就必须存在，意思就是说，必须有一个map，然后有一个user的键，不然会报错，当然，这个是可以解决的，后面会详细讲到。

 

### 5.4.@SessionAtrribute引发的异常

上一讲说到使用@SessionAtrribute来修饰一个类，就可以把一些值存放到session域中，但是如果找不到对应的值，就会报异常，这里可以用@ModelAtrribute来进行修饰，对它改名就可以了，代码如下：

```
    @RequestMapping("testModelAtrribute")
    public String testModelAtrribute(@ModelAttribute("abc") User user){
        System.out.println("修改:"+user);
        return "spring";
    }
```

或者加上一个有着@ModelAtrribute所修饰的方法，至于@ModelAtrribute注解，将会在第六章讲到。

 

## 六：@ModelAtrribute注解详解

### 6.1.简单介绍@ModelAtrribute及其运行流程

在我们开发中，会有这样一种情况，比如说我要修改一个人的信息，但是用户名是不让修改的，那么我在浏览器页面中肯定会有一个表单，里面有一个隐藏域的id，可以改密码，可以改邮箱，但是用户名不让修改，所以我们不能给用户名的输入框，然后用户修改完数据后，点击提交保存，然后发现这个时候用户名不见了！当然大家都会想到就是重新取出用户名，然后给这个对象赋值，或者先从数据库里面找到这个用户的对象，然后再来修改这个对象，而不是自己来创建对象，@ModelAtrribute注解就是基于这种情况的。

那么这种情况在springMVC中要如何实现呢？

首先给出执行的目标方法的代码：

```
    @RequestMapping("testModelAtrribute")
    public String testModelAtrribute(User user){
        System.out.println("修改:"+user);
        return "spring";
    }
```

这个代码很简单，只是使用Pojo来获取表单的参数，但是User类是不可能从表单得到用户名的，所以这个类就缺少了一个属性，如果这样存到数据库里面，是肯定要出问题的，那么按照之前所说，我们可以先得到这个User对象，然后给这个对象赋值，但是我们有着简化的方法，下面给出@ModelAtrribute注解的用法:

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
/**
     * 有@ModelAtrribute注解的方法，会在每个目标方法执行前被springMVC调用
     * @param id
     * @param map
     */
    @ModelAttribute
    public void getUser(@RequestParam(value="id",required=false) Integer id,Map<String,Object> map){
        if(id!=null){
            System.out.println("每个目录方法调用前，都会先调用我！");
            //模拟从数据库中获取对象
            User user = new User(1L, "tom", "123456", "123456", "12");
            System.out.println("从数据库中获取一个对象 ");
            map.put("user", user);
        }
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

可以发现，上面的类是没有返回值的，但是经有一个map集合，我们把这个从数据库查出来的user对象放到了Map集合中，然后就不需要做什么了，然后上面的

testModelAtrribute方法执行的时候，就会自动把用户名给填充进去。

 

下面讲一个@ModelAtrribute注解的执行流程

1.执行@ModelAtrribute注解修饰的方法：从数据库中取出对象，并把对象放到了Map中，键为user

2.springMVC从Map集合中取出User对象，并把表单的请求参数赋值给user对象相应的属性

3.springMVC把上述对象传入目标方法的参数

4.这个user对象是存在request中，如果jsp表单中有对应的字段，还会自动填充表单

注意：在@ModelAtrribute修饰的方法中，放入Map时的键要和目标方法的参数名一致

 

### 6.2.@ModelAtrribute源代码分析

可以一边调试一边查看源代码，这里的源代码有点多，我就不贴出来了，有兴趣的同学可以自己看，我这里讲诉一下原理：

1.调用@ModelAtrribute注解修饰的方法，实际上是把@ModelAtrribute中的Map放到了implicitModel中。

2.解析请求处理器的标参数，实际上目标参数来自于WebDataBinder对象的target属性

（1）.创建WebDataBinder对象

　　>确定objectName属性，若传入的attrName属性为""，则objectName为类名第一个字母小写

​      >注意：atrributeName，若目标方法的Pojo属性使用了@ModelAtrribute注解来修饰，则atrributeName值即为@ModelAtrribute的value属性值

（2）.确定target属性

　　>在implicitModel中查找atrrName对应的属性值，若存在ok，若不存在，则验证Hander，是否使用了@SessionAtrributes进行修饰，若使用了，则尝试从session中获取attrName所对应的属性值，若session中没有对应的值，则抛出异常

　　>若Hander没有使用@SessionAtrributes进行修饰，或@SessionAtrributes中没有使用value值指定的键和attrName相互匹配，则通过反射创建了Pojo对象，这个时候target就创建好了。

3.springMVC把表单的请求参数赋给了WebDataBinder的target属性

4.springMVC会把WebDataBinder的attrName和target给到implicitModel

5.把WebDataBinder的target作为参数传递给目标方法的入参

 

### 63.解决@ModelAtrribute注解中，map集合的键与执行目标方法的参数名不一致的情况

其实我们可以在目标方法里面的参数中，定义一个@ModelAtrribute注解，并把其值指定为@ModelAtrribute注解修饰的方法中的map的键，下面给出代码示例：

```
    @RequestMapping("testModelAtrribute")
    public String testModelAtrribute(@ModelAttribute("abc") User user){
        System.out.println("修改:"+user);
        return "spring";
    }
```

可以看到，目标方法的参数中，有着@ModelAtrribute修饰，其value属性为"abc"，再来看一下@ModelAtrribute所修饰的方法:

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
/**
     * 有@ModelAtrribute注解的方法，会在每个目标方法执行前被springMVC调用
     * @param id
     * @param map
     */
    @ModelAttribute
    public void getUser(@RequestParam(value="id",required=false) Integer id,Map<String,Object> map){
        if(id!=null){
            System.out.println("每个目录方法调用前，都会先调用我！");
            //模拟从数据库中获取对象
            User user = new User(1L, "tom", "123456", "123456", "12");
            System.out.println("从数据库中获取一个对象 ");
            map.put("abc", user);
        }
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

在这里就可是很显示的看到map是存放了一个"abc"的键。

 

 

## 七：转发与重定向

### 7.1<mvc:view-controller>标签

在springMVC的配置文件中使用<mvc:view-controller>标签可以使得url不需要进入handler处理方法，就可以直接跳转页面，配置方法如下

```
    <!-- 配置直接进行转发的页面，无须进入handler方法 -->
    <mvc:view-controller path="good" view-name="spring"/>
```

根据如上的配置，就可以直接在浏览器中输入http://localhost:8080/project/good，就可以跳转至success.jsp页面，而无需进入handler处理方法，更不需要进行@RequestMapping映射。

但是如果仅仅是这样的配置，会有一个很大的问题，就是之前所写的handler处理方法全部都不能使用了，全部会进行报错，那么要怎么解决呢？可以在springMVC的配置文件中，写一个下面的标签，就不会有这样的问题了:

```
    <!-- 这个标签在实际开发中，通常要进行配置 -->
    <mvc:annotation-driven/>
```

只要写上这样的一个标签，那么就可以解决上面的问题，而且也不要写任何参数。不过这个标签具体有什么用呢？后面会作介绍。

 

### 7.2.自定义视图

下面来讲一下自定义视图，使用它可以很好的和jfreechar或excel整合，下面来具体说明。

首先新建一个视图，新建一个类，继承view接口，然后覆盖里面的方法，代码如下：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
@Component
public class HelloView implements View {
    @Override
    public String getContentType() {
        //这个方法是设置返回的类型，根据自己的需要设置
        return "text/html";
    }
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        //这里显示要打印的内容
        response.getWriter().print("hello view ,time:"+new Date());
    }

}
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

如上所示，写一个类，继承View接口，然后覆盖里面的方法，就可以自己自定义视图了，但是目前这个视图还没有用，需要在springMVC的配置文件中进行配置，才能使用这个视图，配置方法如下：

```
<!-- 视图解析器 BeanNameViewResolver:使用视图的名字来解析视图 -->
    <!-- 定义property属性来定义视力的优先级，order值越小，越优先，InternalResourceViewResolver视图的order最高为int最大值 -->
    <bean class="org.springframework.web.servlet.view.BeanNameViewResolver">
        <property name="order" value="10"></property>
    </bean>
```

只要在springMVC的配置文件中写如上的配置，那么这个视图就可以使用了，然后我们写一个handler处理方法，代码如下：

```
    @RequestMapping("testView")
    public String testView(){
        System.out.println("hello view");
        return "helloView";
    }
```

然后的话，我们输入如下url，http://localhost:8080/project/testView，就不会进行helloView.jsp，因为配置的解析视图的order值为最高，也就代表着它的优先级是最低的，所以会先执行我们自定义的视图，那么就会在浏览器中显示之前视图中向浏览器写的数据。

 

### 7.4.视图的重定向操作

上面所说的全部都是视图的转发，而不是重定向，这次我来讲一下重定向是怎么操作的。

只要字符串中以forward或者redirect开头，那么springMVC就会把它解析成关键字，然后进行相应的转发，或者重定向操作，下面给出示例代码：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
/**
     * 测试视图的重定向
     * 只要在字符串中加了foword或者redirect开头，springMVC就会把它解析成关键字，进行相应原转发重定向操作
     * @return
     */
    @RequestMapping("testRedirect")
    public String testRedirect(){
        return "redirect:/spring.jsp";
    }
    /**
     * 测试视图的转发
     * 只要在字符串中加了foword或者redirect开头，springMVC就会把它解析成关键字，进行相应原转发重定向操作
     * @return
     */
    @RequestMapping("testForward")
    public String testForward(){
        return "forward:/spring.jsp";
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

上面就分别是重定向与转发操作，其实不止java代码，<mvc:view-controller>标签中的返回视图，也可以加上redirect或者forward字符串，也会进行相应的操作。

 

 

## 八：数据的格式化

### 8.1.日期的格式化

form表单向后台处理方法提交一个参数的时候，如果提交一个日期的数据，而后台接收的数据类型则是Date类型，那么springMVC肯定无法将其转换成，因为springMVC不知道你传的数据的格式是怎么样的，所以需要为接收的字段指定日期的格式，使用@DateTimeFormat注解，使用方法如下：

使用前提：需要在springMVC-servlet.xml的配置文件中配置<mvc:annotation-driven/>，这个在开发中肯定会配置的，因为它有好多作用，如果不配置，则下面代码无效：

```
<mvc:annotation-driven/>
```

下面是目标方法的代码：

```
@RequestMapping("dateFormat")
    public void initBinder(@RequestParam("date") @DateTimeFormat(pattern="yyyy-MM-dd") Date date){
        System.out.println(date);
    }
```

上面就是在接收的参数前面加了一个@DateTimeFormat注解，注解中写明pattern属性，写上日期的格式，然后在浏览器输入:http://localhost:8080/project/dateFormat?date=19951029，这样springMVC就可以把这个字符串转成Date日期了。

如果是使用Pojo，使用一个对象来接收参数，那么也是一样的，同样是在字段的上方，加上一个@DateTimeFormat注解，如下：

 

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String age;
    private Address address;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birthday;
    ..省略get,set方法
}
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

### 8.2.数字的格式化

除了日期的格式化，我们可能还会遇到数字的格式化，比如会计人员作账时，数字喜欢这样写 1,234,567.8 因为这样简单明了，但是如果由java直接来解析的话，肯定是不行的，因为这根本就不是一个Float类型的数据，肯定是要报错的，那么要如何呢？我们可以使用@NumberFormat()注解，这个注解的使用方式，和使用前提和8.1章节，日期的格式化是一样的，请先查看8.1章节，再看本章。

和之前一样，<mvc:annotation-driven/>是肯定要配置的，不过这里就不详细说明了，下面给出执行方法的代码：

```
@RequestMapping("numFormat")
    public String numFormat(@RequestParam("num") @NumberFormat(pattern="#,###,###.#") Float f){
        System.out.println(f);
        return "spring";
    }
```

其使用方法，其实是和@DateTimeFormat是一样的，但是这里的参数有必要说明一样，”#“是代表数字，那么这个时候，就可以对其进行解析了。如果你传入的是一个正确的Float类型的数据，那么它会被正确的接收这个数字，如果不是一个Float类型的数据，那么springMVC会尝试使用@NumberFoamat注解的参数来尝试解析。

 如输入的是http://locathost:8080/project?num=123，那么springMVC会解析成123.0，如果是http://locathost:8080/project?num=123.1，则会正常显示成123.1，那如果是http://locathost:8080/project?num=1,234,567.8这种特殊的写法，则也会正确的解析成1234567.8

 

### 8.3.数据的校验

数据的检验是使用Hibernate-validator的扩展注解，它是javaee6.0提出的JSR303的实现，使用它，可以用注解对数据进行校验，下面来说一下使用方法。

1.首先要导入jar包，这不是spring的jar包，需要下载Hibernate-validator的jar包，然后添加到项目工程中

2.其次是要在springMVC的配置文件中，即springMVC-servlet.xml中配置<mvc:annotation-driven/>，不需要写其它的属性：

```
<mvc:annotation-driven/>
```

3.然后在字段面前加上对应的校验注解，下面以一个bean作一个简单的例子:

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
public class User {
    private Long id;
    //用户名不能为空
    @NotEmpty
    private String username;
    private String password;
    //必须是email格式
    @Email
    private String email;
    private String age;
    private Address address;
    //生日必须是以前的时间，而不能是未来的时间
    @Past
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birthday;
        
        ..当然，省去了get,set方法
}
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

这样就会对使用了注解的字段进行校验，然后这样还不行，还需要指定目标执行方法，所以需要在执行方法上面也加上一个注解@Valid，这样加了这个注解的执行方法就会校验数据，下面给出目标方法的注解:

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
@RequestMapping("testPojo")
    public String testPojo(@Valid User user,BindingResult result){
        //程序较验的结果，以衣类型转换的结果，都会存放在bindingResult对象中，可以把这个参数定义到方法中使用
        //当从这个对象中得到的错误的个数大于0的时候进行操作
        if(result.getErrorCount()>0){
            //遍历所有的错误，把错误字段和错误消息打印出来 
            for(FieldError error : result.getFieldErrors()){
                System.out.println(error.getField()+"  "+error.getDefaultMessage() );
            }
        }
        System.out.println(user);
        return "spring";
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

只要在这个方法上面加上@Valid注解，然后这个执行方法就会校验数据，校验的结果可以使用BindingResult对象显示，这个对象会不仅仅会保存数据校验的结果，还会保存数据类型转换的结果，所以都可以使用这个对象得到相应的信息，显示的方法如下面的代码如示。

注意：需要校验的bean对象与其绑定结果对象或错误对象是成对出现的，他们中间不允许声明其它的参数

 

### 8.4.JSR303的验证类型

通过上面的例子我们知道可以使用注解来验证数据类型，但是具体可以使用哪些注解呢，下面给出说明：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
@Null    　　　　　　　　　　　　 被注释的元素必须为 null
@NotNull    　　　　　　　　　　 被注释的元素必须不为 null
@AssertTrue    　　　　　　　　  被注释的元素必须为 true
@AssertFalse   　　　　　　　　  被注释的元素必须为 false
@Min(value)    　　　　　　　　  被注释的元素必须是一个数字，其值必须大于等于指定的最小值
@Max(value)       　　　　　　   被注释的元素必须是一个数字，其值必须小于等于指定的最大值
@DecimalMin(value)    　　　　  被注释的元素必须是一个数字，其值必须大于等于指定的最小值
@DecimalMax(value)    　　　　  被注释的元素必须是一个数字，其值必须小于等于指定的最大值
@Size(max, min)        　　　　 被注释的元素的大小必须在指定的范围内
@Digits (integer, fraction)    被注释的元素必须是一个数字，其值必须在可接受的范围内
@Past    　　　　　　　　　　　　　被注释的元素必须是一个过去的日期
@Future    　　　　　　　　　　　　被注释的元素必须是一个将来的日期
@Pattern(value)    　　　　　　　被注释的元素必须符合指定的正则表达式
//-----------------下面是hibernate-valitor新增加的
@Email    　　　　　　　　　　　　 被注释的元素必须是电子邮箱地址
@Length    　　　　　　　　　　　　被注释的字符串的大小必须在指定的范围内
@NotEmpty    　　　　　　　　　　　被注释的字符串的必须非空
@Range    　　　　　　　　　　　　　被注释的元素必须在合适的范围内
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

 

### 8.5.传递json类型的数据

而在springMVC中，使用json非常的简单，但是首先需要引进其它的一些jar包，那就是jackson，这是一个解析json的jar包，然后就可以直接使用了，下面给出代码示例：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
    /**
     * 打印json字符串
     * @return
     */
    @ResponseBody
    @RequestMapping("testjson")
    public List testJson(){
        List list = new ArrayList();
        list.add("good");
        list.add("12");
        list.add("dgdgd");
        list.add("99999999999");
        return list;
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

如上如示，只要在执行方法的上面加上@ResponseBody注解，然后定义目标方法的返回值，其返回值可以是任意集合，也可以是任意对象，然后springMVC会自动将其转换成json

 

### 8.6.文件上传

springMVC也封装了文件上传，变的极其简单，但是需要引入common-io.jar与common.upload.jar包，然后需要在spinrgMVC-serlvet.xml中作如下的配置:

```
<!-- 配置nultipartresolver,注意：id名必须这样写，不然会报错 -->
    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <property name="defaultEncoding" value="UTF-8"></property>
        <property name="maxInMemorySize" value="10240000"></property>
    </bean>
```

 

```
<!-- 配置nultipartresolver,注意：id名必须这样写，不然会报错 -->
<!--     defaultEncoding：表示用来解析request请求的默认编码格式，当没有指定的时候根据Servlet规范会使用默认值ISO-8859-1。当request自己指明了它的编码格式的时候就会忽略这里指定的defaultEncoding。 -->
<!--     uploadTempDir：设置上传文件时的临时目录，默认是Servlet容器的临时目录。 -->
<!--     maxUploadSize：设置允许上传的最大文件大小，以字节为单位计算。当设为-1时表示无限制，默认是-1。 -->
<!--     maxInMemorySize：设置在文件上传时允许写到内存中的最大值，以字节为单位计算，默认是10240。 -->
```

 

 

 

注意：id必须如上写法，不然会报错

然后给出处理方法的代码：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
@RequestMapping("testFileUpload")
    public String testFileUpload(@RequestParam(value="desc",required=false) String desc,
                @RequestParam(value="file",required=false) MultipartFile files[]) throws IOException{
        for(MultipartFile file : files){
            System.out.println(desc);
            System.out.println(file.getOriginalFilename());//得到文件的原始名字
            System.out.println(file.getName());//得到文件的字段的名字”file
            InputStream in = file.getInputStream();
            OutputStream out = new FileOutputStream("d:/"+file.getOriginalFilename());
            int len=0;
            byte[] buf =new byte[1024];
            while((len=in.read(buf))!=-1){
                out.write(buf);
                out.flush();
            }
            out.close();
            in.close();
        }
        return "spring";
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

如果是多个文件上传，则改为数组，如果是单个，方式也是一样，与struts2的文件的上传极其的类似。

 

 

## 九：拦截器

### 9.1.第一个拦截器

编写拦截器极其简单，只要编写一个类，实现HandlerInterceptor的方法，然后在springMVC的配置文件中配置这个类，就可以使用这个拦截器了。

首先给出配置文件的写法：

```
    <!-- 配置自定义的拦截器 -->
    <mvc:interceptors>
    <!-- 这个bean就是自定义的一个类，拦截器 -->
        <bean class="zxj.intercepter.FirstInterceptor"></bean>
    </mvc:interceptors>
```

然后再来写FirstInterceptor这个拦截器，代码如下：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
/**
 * 写一个拦截器，需要实现HandlerInterceptor接口
 * @author jie
 *
 */
public class FirstInterceptor implements HandlerInterceptor {

    /**
     * 当目标方法执行之前，执行此方法，如果返回false，则不会执行目标方法，同样的，后面的拦截器也不会起作用
     * 可以用来做权限，日志等
     */
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        System.out.println("这个方法会最先执行..");
        return true;
    }

    /**
     * 执行目标方法之后调用，但是在渲染视图之前，就是转向jsp页面之前
     * 可以对请求域中的属性，或者视图进行修改
     */
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        System.out.println("执行目标方法之后调用，但是在渲染视图之前，就是转向jsp页面之前");
    }

    /**
     * 在渲染视图之后被调用
     * 释放资源
     */
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        System.out.println("在渲染视图之后被调用");
    }

}
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

然后在每个执行方法调用之前，都会先进拦截器，这就是一个简单的拦截器的写法了。

 

### 9.2.拦截器的指定范围

在使用拦截器时候，并不一定要对所有的目标方法都进行拦截，所以我们可以只对指定的方法进行拦截，这就需要更改配置文件了，下面给出配置文件的写法：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
<!-- 配置自定义的拦截器 -->
    <mvc:interceptors>
    <!-- 这个bean就是自定义的一个类，拦截器 -->
        <bean class="zxj.intercepter.FirstInterceptor"></bean>
        <!-- 这个配置可以配置拦截器作用（不作用）的路径,不起作用的用<mvc:exclude-mapping path=""/> -->
        <mvc:interceptor>
            <!-- 这个path就是起作用的路径，可以使用通配符 -->
            <mvc:mapping path="/test*"/>
            <bean class="zxj.intercepter.SecondInterceptor"></bean>
        </mvc:interceptor>
    </mvc:interceptors>
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

只需要在<mvc:interceptors>中配置一个<mvc:interceptor>，然后指定其路径，就可以了，这个路径可以指定一个URL，也可以使用通配符。

 

### 9.3.拦截器的使用顺序

当同时定义了多个拦截器的时候，那么它的使用顺序是怎么样的呢？

preHandle是按配置文件中的顺序执行的

postHandle是按配置文件中的倒序执行的

afterCompletion是按配置文件中的倒序执行的

 

## 十：异常的处理

### 10.1.使用springMVC的注解@ExceptionHandler来处理异常

现在来说一下springMVC的处理异常的方法，在目标方法中，可以不处理异常，全部抛出去，交由springMVC来统一处理，而且处理方式要比struts2要简单的多，下面则简单的介绍一下怎么用springMVC来处理异常。

首先定义一个会抛出异常的处理方法:

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
    /**
     * 模拟一个异常，然后交由springMVC来处理
     * @param i
     * @return
     */
    @RequestMapping("byZero")
    public String byZero(@RequestParam("i") int i){
        System.out.println("go.");
        System.out.println(10/i);
        return "spring";
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

看到上面的代码，很显示，如果浏览器提交出来的i是一个0的话，就会抛出一个算术的异常，因为0不能为除数，抛出的异常为ArithmeticException，如果不经过任何处理的话，异常就会往浏览器抛，而浏览器是无法处理的，所以我们要在springMVC中将其进行处理，下面就是处理方式的代码，很简单，只要定义一个方法，使有用@ExceptionHandler注解，代码如下：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
/**
     * 使用@ExceptionHandler注解，其value值是一个Class数组，且这个Class就是要抛出的那个异常的类，然后springMVC只要遇到这个异常，就会自行处理
     * 参数中可以定义一个Exception，里面包含出错的内容
     * 注意：参数中不能定义Map，不然会报错，所以如果要把异常存到request中，要用modelAndView 
     * @param e
     * @return
     */
    @ExceptionHandler({ArithmeticException.class})
    public String hException(Exception e){
        System.out.println(e);
        return "spring";
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

这就是springMVC的处理方式，首先来看一个@ExceptionHandler注解，它的value值是一个Class的数组，存放的就是要抛出的异常的类，比如除数为0就会抛出ArithmeticException异常，那么就在这个注解里面的value值中声明这个Class，那以后只要有出现这个异常，springMVC就会对其进行处理，当然如果觉得写太多异常类太麻烦的话，可以统一用Exception，因为它是所有异常类的父亲类，所以它会处理所有的异常。

在这个异常的处理方法中，可以声明一个Exception参数，这个对象里面封装了引发异常的信息，可以由这个对象查看是哪些异常出现。

 

### 10.2.如何把异常传递到页面显示

很多人会想，得到了Exception对象，然后直接把它存在request中就可以了，但是如果在参数中声明Map，是会报错的。因为@ExceptionHandler修饰的方法中，参数不能有Map，既然不能使用Map往request中存值，那么要如何解决呢？使用ModelAndView，可以参照5.1章节的内容，下面给出示例代码：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
    /**
     * 使用@ExceptionHandler注解，其value值是一个Class数组，且这个Class就是要抛出的那个异常的类，然后springMVC只要遇到这个异常，就会自行处理
     * 参数中可以定义一个Exception，里面包含出错的内容
     * 注意：参数中不能定义Map，不然会报错，所以如果要把异常存到request中，要用modelAndView 
     * @param e
     * @return
     */
    @ExceptionHandler({ArithmeticException.class})
    public ModelAndView toException(Exception e){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error");
        //虽然不能使用Map往request中存值，但是可以使用下面的方法
        mv.addObject("error", e);
        System.out.println(e);
        return mv;
    }
```

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

这样就可以往request中存放错误内容了。

 

### 10.3.处理的优先级

异常是可以声明多个的，那么如果同时声明了多个异常，而且都能匹配上抛出的异常，那么spirngMVC会怎么处理呢？

假设同时定义了两个异常，一个是ArithmeticException，还有一个是Exception，那么肯定会执行ArithmeticException，因为它的匹配度更高一点，或者说离这个异常的真实原因更近一点，但是如果没有定义ArithmeticException，那么就会走Exception异常了。

 

### 10.4.全局异常处理

细心的朋友也会发现，这上面定义的异常处理，都是只能处理一个类的异常，那在实际开发中，肯定会有着很多个处理类，那么要怎么定义一个全局的异常处理方法呢？

我们可以单独写一个类出来，使用@ControllerAdvice修饰这个类，然后再写上异常的处理方法，然后它就是全局的一个异常处理了，代码如下：

[![复制代码](http://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * 定义全局的异常处理
 * 如果在处理方法的当前类找不到异常的处理方法，则会在全局找有着@ControllerAdvice修饰的类中的异常处理方法　　
 * 只要使用@ControllerAdvice修饰这个类，然后再定义异常的处理方法，那么它就是一个全局的异常处理了
 * @author jie
 *
 */
@ControllerAdvice
public class MyExceptionHandler {
    
    /**
     * 使用@ExceptionHandler注解，其value值是一个Class数组，且这个Class就是要抛出的那个异常的类，然后springMVC只要遇到这个异常，就会自行处理
     * 参数中可以定义一个Exception，里面包含出错的内容
     * 注意：参数中不能定义Map，不然会报错，所以如果要把异常存到request中，要用modelAndView 
     * @param e
     * @return
     */
    @ExceptionHandler({ArithmeticException.class})
    public ModelAndView toException(Exception e){
        ModelAndView mv = new ModelAndView();
        System.out.println("gobal handler exception");
        mv.setViewName("error");
        //虽然不能使用Map往request中存值，但是可以使用下面的方法
        mv.addObject("error", e);
        System.out.println(e);
        return mv;
    }
}
```



如上代码可以看到，方法体的内容完全没有变化，只是在类上面加了一个@ControllerAdvice，而且不需要在springMVC的配置文件中作任何的配置，就可以使用这个全局的异常了。

注意：这个类里面的异常的处理的优先级低于直接定义在处理方法的类中

 