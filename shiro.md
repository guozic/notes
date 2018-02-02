# ssm整合shiro

## 一、基本使用步骤

### 1、在pom文件中引入shiro坐标

```xml
<!-- 权限控制 -->
<dependency>
  <groupId>org.apache.shiro</groupId>
  <artifactId>shiro-all</artifactId>
  <version>1.3.2</version>
</dependency>
```



### 2、web.xml

```xml
<!-- 配置Spring框架提供的过滤器,用于整合shiro框架 -->
<!-- 该过滤器必须在Struts过滤器之前 -->
<!-- 指定的filter-name,必须在spring框架配置文件中有声明
 即注册bean时声明的id一定要和filter-name中声明的值保持一致
-->
<!-- Tomcat启动时,会调用这个过滤器的初始化方法
 此时会从Sping工厂中获取id为filter-name的bean,
 如果spring框架配置文件没有注册这个bean,将导致NoSuchBeanDefinitionException -->
<filter>
  <filter-name>shirofilter</filter-name>
  <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>
<filter-mapping>
  <filter-name>shirofilter</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>
```

### 3、applicationContext.xml

```xml
<!-- 注册shiro框架的过滤器 id一定要和web.xml中声明的DelegatingFilterProxy过滤器名字一致-->
<bean id="shirofilter" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean">
  <!-- 注入shiro框架核心对象,安全管理器 -->
  <property name="securityManager" ref="securityManager" />
  <!-- 指定要验证的页面 -->
  <property name="loginUrl" value="/login.html" />
  <!-- 指定权限验证成功后,跳转页面 -->
  <property name="successUrl" value="/index.html" />
  <!-- 指定权限不足页面 -->
  <property name="unauthorizedUrl" value="/unauthorized.html" />

  <!--================自定义过滤器===========================================-->
  <property name="filters">
    <map>
      <entry key="MyAuthenticatingFilter">
        <bean class="com.ybkj.happyGo.shiro.filter.MyAuthenticatingFilter" />
      </entry>
    </map>
  </property>

   <!-- ==================指定URL拦截规则======================================= -->
  <!--
  authc:代表shiro框架提供的一个过滤器，这个过滤器用于判断当前用户是否已经完成认证，
  如果当前用户已经认证，就放行
  如果当前用户没有认证，跳转到登录页面
  anon:代表shiro框架提供的一个过滤器，允许匿名访问
 -->
  <property name="filterChainDefinitions">
    <value>
      /adminUser/login = anon
      /** = MyAuthenticatingFilter 
      /** = authc
    </value>
  </property>
</bean>


<!-- 注册安全管理器 -->
<bean id="securityManager" class="org.apache.shiro.web.mgt.DefaultWebSecurityManager"></bean>
```

### 4、realm

```java
/**
 * 
 */
package com.ybkj.happyGo.service.realm;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ybkj.happyGo.bean.AdminResourceBean;
import com.ybkj.happyGo.bean.AdminRoleBean;
import com.ybkj.happyGo.bean.AdminUserBean;
import com.ybkj.happyGo.dao.AdminResourceMapper;
import com.ybkj.happyGo.dao.AdminRoleMapper;
import com.ybkj.happyGo.dao.AdminUserMapper;

/**
 * 权限控制
 * 
 * @author guozi 2018年1月29日
 */
@Service
public class HgRealm extends AuthorizingRealm {

  @Autowired
  private AdminUserMapper adminUserMapper;

  @Autowired
  private AdminRoleMapper adminRoleMapper;

  @Autowired
  private AdminResourceMapper adminResourceMapper;

  // 授权
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    // 为用户授权,只需把用户需要的权限添加到info中就可以了
    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    // 获取用户
    Subject subject = SecurityUtils.getSubject();
    // 获取用户
    AdminUserBean user = (AdminUserBean) subject.getPrincipal();
    AdminRoleBean adminRole = null;
    String authoritys = null;
    String[] split = null;
    List<String> list = new ArrayList<>();

    if (null != user) 
      // 根据用户获取对应的角色
      adminRole = adminRoleMapper.selectByPrimaryKey(user.getRoleid());

    // 根据角色获取对应的权限id字符串
    if (null != adminRole) 
      authoritys = adminRole.getAuthority();

    if (null != authoritys) 
      split = authoritys.split(",");

    if (null != split) {
      for (String authorityId : split) {
        AdminResourceBean adminResource = adminResourceMapper.selectByPrimaryKey(Integer.parseInt(authorityId));
        if (null != adminResource) {
          list.add(adminResource.getPermissions());
        }
      }
    }
    info.addStringPermissions(list);
    return info;
  }

  // 认证
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken paramAuthenticationToken)
    throws AuthenticationException {
    // 强转令牌
    UsernamePasswordToken token = (UsernamePasswordToken) paramAuthenticationToken;
    // 从令牌中获取用户名
    String username = token.getUsername();
    // 从数据库中根据用户名查询用户
    AdminUserBean user = adminUserMapper.findByUsername(username);
    if (user == null) {
      // 未找到用户,直接返回空
      return null;
    }
    // xxx======传入的参数  user 就是  subject.getPrincipal()时返回的对象=======xxxxxxx
    AuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    return info;
  }
}

```

- 动态授权，即根据用户获取对应的角色，根据角色获取对应的权限，然后进行授权
- ​

### 5、认证

- 登录

```java
/**
	 * 登录
	 * @return
	 */
@RequestMapping(value="adminUser/login", method=RequestMethod.POST)
@ResponseBody
public ResultBean login(AdminUserBean user, HttpSession session) {
  ResultBean resultBean = new ResultBean();

  Subject subject = SecurityUtils.getSubject();

  AuthenticationToken token =
    new UsernamePasswordToken(user.getUsername(), user.getPassword());
  try {
    // 执行登录
    subject.login(token);
    // 登录成功后,获取User
    AdminUserBean AdminUser = (AdminUserBean) subject.getPrincipal();
    // 存入session
    session.setAttribute("user", AdminUser);
    // 返回成功
    resultBean.setStatus(ResultCode.SUCCESS);
  } catch (UnknownAccountException e) {
    // 登录过程中发生异常,说明登录失败
    resultBean.setStatus(ResultCode.FAIL);
    resultBean.setData("账号不存在");
    //        	e.printStackTrace();
  } catch (IncorrectCredentialsException e) {
    resultBean.setStatus(ResultCode.FAIL);
    resultBean.setData("密码有误");
    //        	e.printStackTrace();
  } 
  return resultBean;
}
```

- 第一次请求登陆成功后就会将用户存进了 subject  对象中  new SimpleAuthenticationInfo(user, user.getPassword(), getName());
- 后面每一次请求过来，都先判断是否登录  
  - 判断方法可以是 默认的过滤器
  - 也可以是自己实现的  过滤器


- 退出

```java
/**
	 * 退出
	 * @return
	 */
@RequestMapping(value="adminUser/logout", method=RequestMethod.GET)
@ResponseBody
public ResultBean logout() {
  ResultBean resultBean = new ResultBean();
  Subject subject = SecurityUtils.getSubject();
  try {
    // 执行退出
    subject.logout();
    // 返回成功
    resultBean.setStatus("SUCCESS");
  } catch (Exception e) {
    resultBean.setStatus("FAIL");
    e.printStackTrace();
  }
  return resultBean;
}
```





### 6、权限控制方式

#### 6.1、url

- 基于过滤器
- 即applicationContext.xml中注入的过滤器中的

```xml
<!--
  authc:代表shiro框架提供的一个过滤器，这个过滤器用于判断当前用户是否已经完成认证，
  如果当前用户已经认证，就放行
  如果当前用户没有认证，跳转到登录页面
  anon:代表shiro框架提供的一个过滤器，允许匿名访问
 -->
  <property name="filterChainDefinitions">
    <value>
      /adminUser/login = anon
      /** = MyAuthenticatingFilter 
      /** = authc
    </value>
  </property>
```

#### 6.2、注解

##### 6.2.1、实例

- 基于代理技术
- 无需过滤器,在Service的方法上增加注解,如下面的示例代码
- 代码在执行时,会为该服务创建代理对象
- 代理对象负责权限检查,如果检查通过,说明拥有权限,就正常执行
- 如果检查不通过,说明没有权限,就抛出权限不足异常

```java
@RequiresPermissions("buyingRecord:addBuyingRecord")
@Override
public ServiceResult addBuyingRecord(BuyingRecordBean buyingRecord) {
  ServiceResult result = new ServiceResult();
  try {
    buyingRecordMapper.addBuyingRecord(buyingRecord);
    result.setStatus(0);
    result.setMessage("成功");
  } catch (Exception e) {
    result.setStatus(1);
    result.setMessage("失败");
    e.printStackTrace();
  }
  return result;
}
```



##### 6.2.2、开启注解

- shiro框架的注解方式权限控制底层基于cglib,它会为Service类创建代理对象,由代理对象进行权限检查

- 在serviceImpl的方法上添加注解

- applicationContext.xml中

  - 修改以下配置,强制使用cglib

  ```xml
  <tx:annotation-driven proxy-target-class="true" transaction-manager="transactionManager" />
  ```

  - 增加配置

  ```xml
  <!-- 基于spring自动代理方式为Service类创建代理方式,实现权限控制 -->
  <bean
  	class="org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator">
  	<!-- 强制使用cglib方式创建代理对象 -->
  	<property name="proxyTargetClass" value="true"></property>
  </bean>
  <!-- 配置切面 -->
  <bean
  	class="org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor">
  	<property name="securityManager" ref="securityManager"></property>
  </bean>
  ```

##### 





#### 6.3、标签方式

- 创建jsp页面,导入标签库`<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>`

- 示例代码

  ​

  ```jsp
  <body>
    <!-- 判断当前用户是否已通过认证,认证成功才可以看到标签中的内容 -->
    <shiro:authenticated>
      您已通过认证
    </shiro:authenticated>
    <hr />
    <!-- 判断当前用户是否用于对应的权限,如有则可以看到标签中的内容 -->
    <shiro:hasPermission name="area">
      您拥有area权限
    </shiro:hasPermission>
    <hr />
    <!-- 判断当前用户是否是某个角色,如是则可以看到标签中的内容 -->
    <shiro:hasRole name="admin">
      您是管理员角色
    </shiro:hasRole>
  </body>
  ```

- 如果需要为用户添加角色,需要修改`bos_managment_service`工程com.itheima.bos.service.realm.BosRealm类的doGetAuthorizationInfo()方法

  ```java
  // 为用户添加角色
  info.addRole("admin");
  ```

#### 6.4、编码方式(严重不推荐)

- 因为这种方式会改变原有的代码逻辑,所以并不推荐使用

- 示例代码如下.当执行保存操作时,检查用户是否拥有对应的权限,没有的话直接抛出异常

  ```java
  public String save() {
      // 获取当前用户
      Subject subject= SecurityUtils.getSubject();
      // 检查用户是否拥有对应的权限,如果没有直接抛异常
      subject.checkPermission("courier:save");
      service.save(getModel());
      return SUCCESS;
  }
  ```

### 

### 7、缓存

##### 7.1、添加依赖

```xml
<!-- 权限缓存 -->
<dependency>
  <groupId>net.sf.ehcache</groupId>
  <artifactId>ehcache</artifactId>
  <version>2.10.2.2.21</version>
</dependency>
```

##### 7.2、applicationContext.xml中添加配置

```xml
<!-- 注册缓存管理器  -->
<bean id="cacheManager" class="org.apache.shiro.cache.ehcache.EhCacheManager">
  <property name="cacheManagerConfigFile" value="classpath:ehcache.xml" />
</bean>

<!-- 将缓存管理器注入安全管理器 -->
<!-- 注册安全管理器 -->
<bean id="securityManager" class="org.apache.shiro.web.mgt.DefaultWebSecurityManager">
  <!-- 注册realm -->
  <property name="realm" ref="bosRealm" />
  <!-- 注入缓存管理器 -->
  <property name="cacheManager" ref="cacheManager" />
</bean>
```



## 二、自定义返回json数据

### 1、为类添加一个父类BaseController

#### 1.1、未登录及权限不足时返回json信息

```java
/**
 * 
 */
package com.ybkj.happyGo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.fastjson.JSONObject;

/**
 * @author guozi
 *2018年1月30日
 */
public abstract  class BaseController {

  /**
     * 登录认证异常
     */
  @ExceptionHandler({ UnauthenticatedException.class, AuthenticationException.class })
  public String authenticationException(HttpServletRequest request, HttpServletResponse response) {
    // 输出JSON
    Map<String,Object> map = new HashMap<>();
    map.put("status", "-999");
    map.put("info", "未登录");
    writeJson(map, response);
    //        return "redirect:/system/401";
    return "未登陆";
  }

  /**
     * 权限异常
     */
  @ExceptionHandler({ UnauthorizedException.class, AuthorizationException.class })
  public String authorizationException(HttpServletRequest request, HttpServletResponse response) {
    // 输出JSON
    Map<String,Object> map = new HashMap<>();
    map.put("status", "-998");
    map.put("info", "权限不足");
    writeJson(map, response);
    return "权限不足";
  }

  /**
     * 输出JSON
     *
     * @param response
     * @author SHANHY
     * @create 2017年4月4日
     */
  private void writeJson(Map<String,Object> map, HttpServletResponse response) {
    PrintWriter out = null;
    try {
      response.setCharacterEncoding("UTF-8");
      response.setContentType("application/json; charset=utf-8");
      out = response.getWriter();
      out.write(JSONObject.toJSON(map).toString());
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (out != null) {
        out.close();
      }
    }
  }
}
```

#### 1.2、登录失败返回json数据

- 在登录的方法中捕获异常

```java
/**
	 * 登录
	 * @return
	 */
@RequestMapping(value="adminUser/login", method=RequestMethod.POST)
@ResponseBody
public ResultBean login(AdminUserBean user, HttpSession session) {
  ResultBean resultBean = new ResultBean();

  Subject subject = SecurityUtils.getSubject();

  AuthenticationToken token =
    new UsernamePasswordToken(user.getUsername(), user.getPassword());
  try {
    // 执行登录
    subject.login(token);
    // 登录成功后,获取User
    AdminUserBean AdminUser = (AdminUserBean) subject.getPrincipal();
    // 存入session
    session.setAttribute("user", AdminUser);
    // 返回成功
    resultBean.setStatus(ResultCode.SUCCESS);
  } catch (UnknownAccountException e) {
    // 登录过程中发生异常,说明登录失败
    resultBean.setStatus(ResultCode.FAIL);
    resultBean.setData("账号不存在");
    //        	e.printStackTrace();
  } catch (IncorrectCredentialsException e) {
    resultBean.setStatus(ResultCode.FAIL);
    resultBean.setData("密码有误");
    //        	e.printStackTrace();
  } 
  return resultBean;
}
```





## 三、自定义过滤器