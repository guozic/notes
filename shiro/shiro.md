# shiro

## 1.shiro 是什么

### 1.简介

- Apache Shiro是Java的一个安全框架。

- Shiro 功能  没有 Spring Security 做的强大，但使用简单，方便，功能满足基本的需求。

- Shiro 不仅可以用在javaSE项目，也可以用在JavaEE项目。而Security  只适用于spring 项目。

  ​

  ​



## 2.shiro 有什么用

### 功能

- `Authentication`：身份认证/登录，验证用户是不是拥有相应的身份；
- `Authorization`：授权，即权限验证，验证某个已认证的用户是否拥有某个权限；即判断用户是否能做事情，常见的如：验证某个用户是否拥有某个角色。或者细粒度的验证某个用户对某个资源是否具有某个权限；
- Session Manager：会话管理，即用户登录后就是一次会话，在没有退出之前，它的所有信息都在会话中；会话可以是普通JavaSE环境的，也可以是如Web环境的；
- Cryptography：加密，保护数据的安全性，如密码加密存储到数据库，而不是明文存储；
- Web Support：Web支持，可以非常容易的集成到Web环境；
- `Caching`：缓存，比如用户登录后，其用户信息、拥有的角色/权限不必每次去查，这样可以提高效率；
- Concurrency：shiro支持多线程应用的并发验证，即如在一个线程中开启另一个线程，能把权限自动传播过去；
- Testing：提供测试支持；
- Run As：允许一个用户假装为另一个用户（如果他们允许）的身份进行访问；
- Remember Me：记住我，这个是非常常见的功能，即一次登录后，下次再来的话不用登了。



- 注意：Shiro不会去维护用户、维护权限，这些需要我们自己去设计/提供，后通过相应的接口注入给Shiro即可。

## 3.从Shiro外部观察



![执行流程](E:\教材\知识回顾\shiro\img\1.png)



ApplicationCode:引用代码  --及程序员写的代码



### shiro 的执行流程图解

1. 应用代码是通过Shiro 的 Subject对象来实现 验证和授权。
2. 而Subject对象又将  验证和授权等功能  委托给SecurityManager。
3. 我们需要给SecurityManager注入realm，
4. 从而让SecuriryManager得到合法的用户及其权限进行判断，从而实现 验证授权等功能 



### Shiro 中对象的简介

- Subject :主体(抽象概念)
  - 与shiro 框架 交互的任何东西都是Subject。
  - 所有Subject都绑定到SecurityManager
  - 主体与Subject 的所有交互都会再委托给SecurityManager 
- SecurityManager:安全管理器
  - **SecurityManager 是 Shiro 的核心！！！**
  - SecurityManager管理者所有的Subject,所有与安全有关的操作都会与SecurityManager交互
  - 它负责与后边介绍的其他组件进行交互
- realm:
  - 太抽象，词义很难准确 翻译。（“安全数据源”）
  - Shiro的所有的安全数据都是在realm 里获取
  - 例如：
    - SecurityManager要验证用户身份，那么它需要从Realm获取相应的用户进行比较以确定用户身份是否合法；也需要从Realm得到用户相应的角色/权限进行验证用户是否能进行操作；
    - 可以把Realm看成DataSource，即安全数据源



## 4.从Shiro 内部观察

![Shiro](E:\教材\知识回顾\shiro\img\2.png)



- **Subject**：
- - 主体，可以看到主体可以是任何可以与应用交互的“用户”；
- **SecurityManager**：
  - **管理着所有Subject、且负责进行认证和授权、及会话、缓存的管理。**
  - 是Shiro的心脏；所有具体的交互都通过SecurityManager进行控制；
  - 相当于SpringMVC中的DispatcherServlet或者Struts2中的FilterDispatcher；
- **Authenticator**：
- - 认证器，负责主体认证的，这是一个扩展点，
  - 如果用户觉得Shiro默认的不好，可以自定义实现，其需要认证策略（Authentication Strategy），即什么情况下算用户认证通过了。
- **Authrizer**：
- - 授权器，或者访问控制器，用来决定主体是否有权限进行相应的操作；
  - 即控制着用户能访问应用中的哪些功能；
- **Realm**：
- - 可以有1个或多个Realm，可以认为是安全实体数据源，即用于获取安全实体的。
  - 由用户提供。
    - 注意：Shiro不知道你的用户/权限存储在哪及以何种格式存储；所以我们一般在应用中都需要实现自己的Realm；
  - 可以是JDBC实现，也可以是LDAP实现，或者内存实现等等；
- **SessionManager**：
  - Shiro框架 抽象了一个自己的Session来管理主体与应用之间交互的数据
    - 这样的话，比如我们在Web环境用，刚开始是一台Web服务器；接着又上了台EJB服务器；这时想把两台服务器的会话数据放到一个地方，这个时候就可以实现自己的分布式会话（如把数据放到Memcached服务器）；
  - 如果写过Servlet就应该知道Session的概念，Session呢需要有人去管理它的生命周期，这个组件就是SessionManager；而Shiro并不仅仅可以用在Web环境，也可以用在如普通的JavaSE环境、EJB等环境；
- **SessionDAO**：
- - 数据访问对象，用于会话的CRUD
    - 比如我们想把Session保存到数据库，那么可以实现自己的SessionDAO，通过如JDBC写到数据库；
    - 比如想把Session放到Memcached中，可以实现自己的Memcached SessionDAO；
    - 另外SessionDAO中可以使用Cache进行缓存，以提高性能；
- **CacheManager**：
  - 缓存控制器，来管理如用户、角色、权限等的缓存的；
    - 因为这些数据基本上很少去改变，放到缓存中后可以提高访问的性能
- **Cryptography**：
  - 密码模块，Shiro提高了一些常见的加密组件用于如密码加密/解密的





## 5.Shiro的组件

### 1.身份验证

-  身份认证，就是判断一个用户是否为合法用户的处理过程。===在应用中能证明他写的登录用户名就他本人。	
-  最常用的简单身份认证方式是系统通过核对用户输入的用户名和口令，看其是否与系统中存储的该用户的用户名和口令一致，来判断用户身份是否正确。
  - 对于采用[指纹](http://baike.baidu.com/view/5628.htm)等系统，则出示指纹；
  - 对于硬件Key等刷卡系统，则需要刷卡。
  - 例如用户名/密码来证明。

#### 1.案例一：Shiro+Maven+spring+注解配置

- 此案例只完成登录

##### 1.执行流程:

1. 页面用户登录填入登录的用户名及密码

2. action中获取用户名及密码

3. 创建Subject对象

   ```java
    Subject subject = SecurityUtils.getSubject();
   ```

   ​

4. 通过带参构造 创建 令牌 token 

5. - 用户token可能不仅仅是用户名/密码，也可能是其他的，例如登录时允许用户使用  用户名/手机号/邮箱   登录

   ```java
   //获取身份验证的token  
   AuthenticationToken token = new UsernamePasswordToken(userName, password);
   ```

6. 通过Subject 进行验证

   ```java
    subject.login(token);
   ```

7. Subject 将此验证委托给SecurityManager

8. 创建realm，并将realm注入SecurityManager

9. 创建realm

   1. 实现接口 AuthorizingRealm，并实现其两个方法中的认证方法（授权方法 / 认证方法）

   2. 从AuthenticationInfo的认证方法带的参数token中获取 用户名 userName

   3. 根据userName 查询 数据库，返回user

   4. 判断user是否为空

   5. 为空返回null，Shiro 框架就知道验证失败，报异常UnknownAccountException

      > 身份验证失败请捕获AuthenticationException或其子类
      >
      > - 常见的如
      >   -  DisabledAccountException（禁用的帐号）、LockedAccountException（锁定的帐号）、UnknownAccountException（错误的帐号）、ExcessiveAttemptsException（登录失败次数过多）、IncorrectCredentialsException （错误的凭证）、ExpiredCredentialsException（过期的凭证）等
      > - 页面的错误消息展示，最好使用如“用户名/密码错误”而不是“用户名错误”/“密码错误”，防止一些恶意用户非法扫描帐号库

      ​

   6. 不为空，则创建AuthenticationInfo的实现，进行密码验证？？

   7. 密码验证不通过报异常：IncorrectCredentialsException

   8. 密码验证通过则返回AuthenticationInfo的实现

##### 2.代码分析

###### 依赖导入

- 核心包shiro-core  囊括了Shiro 所有的模块

```xml
<dependencies>  
   <dependency>  
        <groupId>org.apache.shiro</groupId>  
        <artifactId>shiro-core</artifactId>  
        <version>1.2.2</version>  
    </dependency>  
  
    <dependency>  
        <groupId>junit</groupId>  
        <artifactId>junit</artifactId>  
        <version>4.9</version>  
    </dependency>  
  
    <dependency>  
        <groupId>commons-logging</groupId>  
        <artifactId>commons-logging</artifactId>  
        <version>1.1.3</version>  
    </dependency>  
</dependencies> 
```



###### web.xml

- 这里配置的过滤器 要在applicationContext.xml中实现注册
  - 即-->配置过滤器的时候对应的\<filter-name\>标签的name的值要与 在applicationContext.xml 中的注册shiro框架的工厂类,初始化框架提供的过滤器的name一致
- 这个过滤器要在struts2的核心过滤器之前配置

```xml
<!--过滤器是Spring框架提供,用于整合shiro框架的 -->
<!--而且这个过滤器一定要在struts过滤器之前 -->
<!-- 项目启动过程中,在创建DelegatingFilterProxy过滤器的时候,需要应用一个同名的bean,这个bean必须在spring的配置文件中注册 -->
<filter>
  <filter-name>shiroFilter</filter-name>
  <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>
<filter-mapping>
  <filter-name>shiroFilter</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>
```



###### applicationContext.xml

- 注册---->web.xml中配置的过滤器
- 注册---->注册安全管理器  securityManager

```xml
<!-- 注册shiro框架的工厂类,初始化框架提供的过滤器 -->
<bean id="shiroFilter" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean">
  <!-- 注入安全管理器 -->
  <property name="securityManager" ref="securityManager"></property>
  <!-- 登录页面 -->
  <property name="loginUrl" value="/login.html"></property>
  <!--登录成功以后跳转的页面 -->
  <property name="successUrl" value="/index.html"></property>
  <!-- 登录失败或者权限不足跳转的页面 -->
  <property name="unauthorizedUrl" value="/unauthorized.html"></property>
  <!-- 过滤规则 -->
  <property name="filterChainDefinitions">
    <!-- authc : 框架提供的过滤器,检查权限,如果有权限直接放行,没有权限,拒绝访问 -->
    <!-- anon : 框架提供的过滤器,可以进行匿名访问 -->
    <!-- 格式化代码的时候,规则不允许折行 -->
    <value>
      /css/* =anon
      /images/* =anon
      /js/** =anon
      /validatecode.jsp* = anon
      /** = authc
    </value>
  </property>
</bean>

<!--注册安全管理器 -->
<bean id="securityManager" class="org.apache.shiro.web.mgt.DefaultWebSecurityManager">
  <property name="realm" ref="userRealm"></property>
</bean>
```



###### java代码

**action**

- 通过SecurityUtils.getSubject() 获取Subject对象
- 通过带参构造，传入用户名及密码，创建令牌token对象
- 调用Subject验证用户名及密码-->subject.login(token);

```java
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class UserAction extends CommonAction<User> {

  private static final long serialVersionUID = -885635712659668664L;

  public UserAction() {
    super(User.class);
  }

  // 登录操作
  @Action(value = "userAction_login",
          results = {
            @Result(name = "success", location = "/index.html",
                    type = "redirect"),
            @Result(name = "login", location = "/login.html",
                    type = "redirect")})
  public String login() {

    String serverCode = (String) ServletActionContext.getRequest()
      .getSession().getAttribute("key");

    if (StringUtils.isNotEmpty(serverCode)
        && StringUtils.isNotEmpty(validateCode)
        && serverCode.equals(validateCode)) {
      // subject代表当前的用户
      Subject subject = SecurityUtils.getSubject();
      // 创建令牌
      AuthenticationToken token = new UsernamePasswordToken(
        getModel().getUsername(), getModel().getPassword());
      // 框架提供的登录方法
      try {
        subject.login(token);
        User user = (User) subject.getPrincipal();
        // 把用户存入Session
        ServletActionContext.getRequest().getSession()
          .setAttribute("user", user);
        return SUCCESS;
      } catch (Exception e) {

        e.printStackTrace();
      }
    }
    return LOGIN;
  }
  // 使用属性驱动获取用户输入的验证码
  private String validateCode;

  public void setValidateCode(String validateCode) {
    this.validateCode = validateCode;
  }
}
```



**realm**

- 实现接口doGetAuthenticationInfo，并实现其 验证 方法
- 通过验证方法的参数token ，获取用户名
- 通过用户名查找数据库，获取匹配的用户
  - 若返回的用户为空，则用户名验证失败，抛出对应异常（见上）
- 返回不为空，则通过带参构造，创建AuthenticationInfo的实现一个类，进行密码校验
  - 若校验失败，则抛出对应异常（见上）
- 校验成功，返回创建的实力；

```java
@Component
public class UserRealm extends AuthorizingRealm {

  @Autowired
  private UserRepository userRepository;



  //----------------------- 授权方法
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(
    PrincipalCollection principals) {

    return null;
  }



  //-------------------------- 认证方法
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(
    AuthenticationToken token) throws AuthenticationException {
    UsernamePasswordToken usernamePasswordToken =
      (UsernamePasswordToken) token;
    // 通过用户名查找用户
    String username = usernamePasswordToken.getUsername();
    User user = userRepository.findByUsername(username);

    if (user == null) {
      // 没找到,抛异常
      return null;
    }

    /**
         * @param principal the 'primary' principal associated with the specified realm. 身份,一般出入当前用户
         * @param credentials the credentials that verify the given principal. 凭证,密码(是从数据库中获取到的密码)
         * @param realmName the realm from where the principal and credentials were acquired.
         *        realm的名字
         */
    AuthenticationInfo info = new 	        	SimpleAuthenticationInfo(user,user.getPassword(),getName());
    // 找到,比对密码,
    // 比对失败 : 抛异常
    // 比对成功 :
    return info;
  }

}
```





#### 2案例二：Shiro+Maven+ini配置文件

- 此案例完成登录/退出

##### 1.准备用户身份/凭据（shiro.ini）

```ini
[users]
zhang=123
wang=123
```

- 此处使用ini配置文件，通过[users]指定了两个主体：zhang/123、wang/123。

##### 2.代码测试

- （com.github.zhangkaitao.shiro.chapter2.LoginLogoutTest） 

```java
@Test
public void testHelloworld() {
    //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
    Factory<org.apache.shiro.mgt.SecurityManager> factory =
            new IniSecurityManagerFactory("classpath:shiro.ini");
  
  //------------------------------------------------------------------------------
    //2、得到SecurityManager实例 并绑定给SecurityUtils
    org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
    SecurityUtils.setSecurityManager(securityManager);
  /*>>>>>>>>>>>>>
  		类似于，在案例1中applicationContext.xml中注册安全管理器  securityManager
 >>>>>>>>>>>> */
  
  //-----------------------------------------------------------------------------------
    //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
    Subject subject = SecurityUtils.getSubject();
    UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");

  
  //-------------------------------------------------------------------------------------------
    try {
        //4、登录，即身份验证
        subject.login(token);
    } catch (AuthenticationException e) {
        //5、身份验证失败
    }

    Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录

    //6、退出
    subject.logout();
}
&nbsp;
```



##### 3.身份认证流程

###### 1.自定义Realm实现

- （com.github.zhangkaitao.shiro.chapter2.realm.MyRealm1）

```java
public class MyRealm1 implements Realm {
    @Override
    public String getName() {
        return "myrealm1";
    }
    @Override
    public boolean supports(AuthenticationToken token) {
        //仅支持UsernamePasswordToken类型的Token
        return token instanceof UsernamePasswordToken; 
    }
    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String)token.getPrincipal();  //得到用户名
        String password = new String((char[])token.getCredentials()); //得到密码
        if(!"zhang".equals(username)) {
            throw new UnknownAccountException(); //如果用户名错误
        }
        if(!"123".equals(password)) {
            throw new IncorrectCredentialsException(); //如果密码错误
        }
        //如果身份认证验证成功，返回一个AuthenticationInfo实现；
        return new SimpleAuthenticationInfo(username, password, getName());
    }
}&nbsp;
```



###### 2.ini配置文件指定自定义Realm实现

- (shiro-realm.ini)

```ini
#声明一个realm  
myRealm1=com.github.zhangkaitao.shiro.chapter2.realm.MyRealm1  
myRealm2=com.github.zhangkaitao.shiro.chapter2.realm.MyRealm2  
#指定securityManager的realms实现  
securityManager.realms=$myRealm1,$myRealm2
```

- securityManager会按照realms指定的顺序进行身份认证。
- 此处我们使用显示指定顺序的方式指定了Realm的顺序，如果删除“securityManager.realms=$myRealm1,$myRealm2”，那么securityManager会按照realm声明的顺序进行使用（即无需设置realms属性，其会自动发现）。
- 当我们显示指定realm后，其他没有指定realm将被忽略，如“securityManager.realms=$myRealm1”，那么myRealm2不会被自动设置进去。



- **Shiro**默认提供的Realm

  ![realm](E:\教材\知识回顾\shiro\img\3.png)



>- **自定义realm一般都是直接继承AuthorizingRealm（授权）即可**；
>  - 其继承了AuthenticatingRealm（即身份验证），而且也间接继承了CachingRealm（带有缓存实现）。其中主要默认实现如下：
>    - org.apache.shiro.realm.text.IniRealm：
>      - [users]部分指定用户名/密码及其角色；
>      - [roles]部分指定角色即权限信息；
>    - org.apache.shiro.realm.text.PropertiesRealm：
>      -  user.username=password,role1,role2指定用户名/密码及其角色。
>      -  role.role1=permission1,permission2指定角色及权限信息；
>    - org.apache.shiro.realm.jdbc.JdbcRealm：
>      - 通过sql查询相应的信息-->如
>        - 获取用户密码
>          - “select password from users where username = ?”
>        - 获取用户密码及盐
>          - “select password, password_salt from users where username = ?”；
>        - 获取用户角色
>          - “select role_name from user_roles where username = ?”
>        - 获取角色对应的权限信息
>          - “select permission from roles_permissions where role_name = ?”











>1. 首先通过new IniSecurityManagerFactory并指定一个ini配置文件来创建一个SecurityManager工厂；
>2. 接着获取SecurityManager并绑定到SecurityUtils，这是一个全局设置，设置一次即可；
>3. 通过SecurityUtils得到Subject，其会自动绑定到当前线程；如果在web环境在请求结束时需要解除绑定；然后获取身份验证的Token，如用户名/密码；
>4. 调用subject.login方法进行登录，其会自动委托给SecurityManager.login方法进行登录；
>5. 如果身份验证失败请捕获AuthenticationException或其子类，常见的如： DisabledAccountException（禁用的帐号）、LockedAccountException（锁定的帐号）、**UnknownAccountException**（错误的帐号）、ExcessiveAttemptsException（登录失败次数过多）、**IncorrectCredentialsException** （错误的凭证）、ExpiredCredentialsException（过期的凭证）等，具体请查看其继承关系；对于页面的错误消息展示，最好使用如“用户名/密码错误”而不是“用户名错误”/“密码错误”，防止一些恶意用户非法扫描帐号库；
>6. 最后可以调用subject.logout退出，其会自动委托给SecurityManager.logout方法退出。

###### -->身份验证的步骤

1. 收集用户身份/凭证，即如用户名/密码；
2. 调用Subject.login进行验证登录，如果失败将得到相应的AuthenticationException异常，根据异常提示用户错误信息；否则登录成功；
3. 最后调用Subject.logout进行退出操作。
   1. 自己实现realm，继承 授权的realm -->AuthorizingRealm
   2. 实现  验证的方法 doGetAuthenticationInfo（）
   3. 获取用户名---->获取对应的对象
   4. 判断  获取的对象是否为空 为空则用户不存在，抛出对应的异常
   5. 不为空，则创建  AuthenticationInfo 接口的一个实现simpleAuthenticationInfo（参数一，参数而，参数三）
      1. 参数一：为数据库中获取的用户对象
      2. 参数二：为密码
      3. 参数三：此方法所在的realm（即自己实现的 realm）
   6. 返回创建的 AuthenticationInfo
      1. 若密码不匹配，则会抛出密码错误对应的异常。





### 2.授权

#### 1.权限控制方式

##### 1.realm进行权限控制

- 对分页查询快递员这个请求进行 **权限控制**

```xml
<!-- 权限控制 -->
<!-- 对courierAction_findPage.action这个请求仅限权限控制 -->
/courierAction_findPage.action = perms["courierAction_findPage"]
```

##### 2.注解方式进行权限控制

- 配置applicationContext.xml

  - 方式一：（有问题）

  ```xml
  <bean id="lifecycleBeanPostProcessor"class="org.apache.shiro.spring.LifecycleBeanPostProcessor"/> 
   <!-- 启用shiro 注解 -->  
  <bean class="org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator"  depends-on="lifecycleBeanPostProcessor"/>  
  <bean class="org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor">  
       <property name="securityManager" ref="securityManager"/>  
  </bean>  
  ```


  <!-- ===使用注解方式进行权限控制============================== -->

  <!-- 1.spring框架提供  基于spring 的动态代理 创建service的动态代理对象 -->

```xml
<bean class="org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator">
  <!-- 开启cglib注解 -->
  <property name="proxyTargetClass" value="true"></property></bean>
```
  </bean>
  <!-- 2.配置切面 -->
  <bean class="org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor">
    <property name="securityManager" ref="securityManager"></property>
  </bean>	
  ```

  ​


- - 方式二：

  ```xml
  <!-- Shiro生命周期处理器-->  
  <bean id="lifecycleBeanPostProcessor" class="org.apache.shiro.spring.LifecycleBeanPostProcessor"/>  

  <aop:config proxy-target-class="true"></aop:config>  

  <bean class="org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor">  
    <property name="securityManager" ref="securityManager"/>  
  </bean> 
  ```

  ​


- ​
- 在serviceImpl 层中 需要 权限控制的方法上添加注解@RequiresPermissions("courierAction_findPage")

```java
/*  
	 * 分页
	 */
@Override
@RequiresPermissions("courier_findPage")
public Page<Courier> findPage(Pageable pageable, Specification<Courier> specification) {
  return courierRepository.findAll(specification, pageable);
}
```



##### 3.标签方式进行权限控制   

- 只能在jsp页面进行权限控制

- **没有对应的权限，就不展示  页面中的 该内容**

- 控制步骤

  - 导入标签库

  ```jsp
  <%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
  ```
  - 使用对应的标签

  ```jsp
  <!-- 校验用户是否已经认证通过了,只有认证通过,才能看到标签中间的内容 -->
  <shiro:authenticated>
    您已经认证通过了
  </shiro:authenticated>

  <hr>

  <!-- 校验用户是否拥有对应的权限,如果有对应的权限,才能看到标签中间的内容 -->
  <shiro:hasPermission name="courissser_pageQuery">
    您拥有courier_pageQuery权限
  </shiro:hasPermission>

  <hr>

  <shiro:hasRole name="admin">
    您的角色是admin
  </shiro:hasRole>
  ```



##### 4编码方式进行权限控制

```java
subject.checkPermission("courier:delete");
```





#### 2.权限控制方式总结

|      | realm方式                        | 注解方式                                   | 标签方式         | 编程方式（不推荐）   |
| :--: | :----------------------------- | :------------------------------------- | ------------ | ----------- |
|  技术  | Shiro框架提供的过滤器                  | cglib动态代理                              | 标签           | java代码逻辑    |
| 表现形式 | 在applicationContext.xml中配置拦截规则 | 完成applicationContext.xml中配置-->在方法上配置注解 | 标签           | java代码      |
| 使用场景 | 为登录的状态下保护资源                    | 登录后，对各种功能操作进行校验                        | 动态控制页面元素是否显示 | （修改源码）不推荐使用 |



#### 3给用户添加指定的权限

- 在用户权限控制中的  授权方法里面进行授权

```java
/**
 * 授权
*/
@Override
protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
  SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
  // 进行授权
  // 真实项目的话,权限应该是从数据库中查询出来,动态的进行授权
  info.addStringPermission("courierAction_findPage");
  return info;
}
```

