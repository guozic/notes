# shiro异常处理返回json数据

## 一、项目背景

- springmvc+mybatis+spring+shiro+MySQL



## 二、需求

- 项目前后台分离
- 对不同的**权限异常** 需要返回对应的json数据

 

## 三、解决方案

### 分析思路：

权限异常主要分四类

- 未登陆异常
- 登陆账户不存在
- 登陆密码错误
- 没有权限

### 方案一：利用父类进行异常判断

>- **登陆账户不存在** 
>- **密码错误的异常**
>- 在登录的方法中可以进行捕捉
>
>![捕获异常](C:\my_file\notes\shiro\img\4.png)
>
>```java
>/**
>	 * 登录
>	 * @return
>	 */
>@RequestMapping(value="adminUser/login", method=RequestMethod.GET)
>@ResponseBody
>public ResultBean login(AdminUserBean user, String verificationCode, HttpServletRequest request) {
>  ResultBean resultBean = new ResultBean();
>  Subject subject = SecurityUtils.getSubject();
>  //		org.apache.catalina.session.StandardSessionFacade@ec7dd1b
>  HttpSession session = request.getSession();
>  String code = (String) session.getAttribute("validateCode");
>  //		if (StringUtils.isEmpty(verificationCode) || StringUtils.equals(verificationCode, code)) {
>  AuthenticationToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
>  //	        try {
>  // 执行登录
>  subject.login(token);
>  // 登录成功后,获取User
>  AdminUserBean adminUser = (AdminUserBean) subject.getPrincipal();
>  /*// 存入session
>	            session.setAttribute("user", AdminUser);*/
>  // 返回成功
>
>  List<ParentMenu> handleMenu = MenuUtil.handleMenu(adminUser.getList());
>
>  resultBean.setStatus(ResultCode.SUCCESS);
>  resultBean.setData(handleMenu);
>  //	        } catch (UnknownAccountException e) {
>  //	            // 登录过程中发生异常,说明登录失败
>  //	        	resultBean.setStatus(ResultCode.LOGIN_FAIL_USER);
>  //	//        	e.printStackTrace();
>  //	        } catch (IncorrectCredentialsException e) {
>  //	        	resultBean.setStatus(ResultCode.LOGIN_FAIL_PASSWORD);
>  //	//        	e.printStackTrace();
>  //			} catch (AuthenticationException e) {
>  //				resultBean.setStatus(ResultCode.LOGIN_FAIL_NOT_PERMISSIONS);
>  //	//        	e.printStackTrace();
>  //	        }catch (AuthorizationException e) {
>  //				resultBean.setStatus(ResultCode.LOGIN_FAIL_NOT_PERMISSIONS);
>  //	//        	e.printStackTrace();
>  //	        }
>  //		}else {
>  //			resultBean.setStatus(ResultCode.LOGIN_FAIL_CODE);
>  //		}
>  return resultBean;
>}
>```
>
>
>
>
>
>- **未登录异常**
>
>- **权限不足异常**
>
>- 写一个父类，进行异常判断，所有的从controller继承此父类
>
>  ```java
>  package com.ybkj.happyGo.controller;
>
>  import java.io.IOException;
>  import java.io.PrintWriter;
>  import java.util.HashMap;
>  import java.util.Map;
>
>  import javax.servlet.http.HttpServletRequest;
>  import javax.servlet.http.HttpServletResponse;
>
>  import org.apache.shiro.authc.AuthenticationException;
>  import org.apache.shiro.authz.AuthorizationException;
>  import org.apache.shiro.authz.UnauthenticatedException;
>  import org.apache.shiro.authz.UnauthorizedException;
>  import org.springframework.web.bind.annotation.ExceptionHandler;
>
>  import com.alibaba.fastjson.JSONObject;
>
>  /**
>   * @author guozi
>   *2018年1月30日
>   */
>  public abstract class BaseController {
>
>    /**
>       * 登录认证异常
>       */
>    @ExceptionHandler({ UnauthenticatedException.class, AuthenticationException.class })
>    public String authenticationException(HttpServletRequest request, HttpServletResponse response) {
>      // 输出JSON
>      Map<String,Object> map = new HashMap<>();
>      map.put("status", "-999");
>      map.put("info", "未登录");
>      writeJson(map, response);
>      //        return "redirect:/system/401";
>      return "未登陆";
>    }
>
>    /**
>       * 权限异常
>       */
>    @ExceptionHandler({ UnauthorizedException.class, AuthorizationException.class })
>    public String authorizationException(HttpServletRequest request, HttpServletResponse response) {
>      // 输出JSON
>      Map<String,Object> map = new HashMap<>();
>      map.put("status", "-998");
>      map.put("info", "权限不足");
>      writeJson(map, response);
>      return "权限不足";
>    }
>
>    /**
>       * 输出JSON
>       */
>    private void writeJson(Map<String,Object> map, HttpServletResponse response) {
>      PrintWriter out = null;
>      try {
>        response.setCharacterEncoding("UTF-8");
>        response.setContentType("application/json; charset=utf-8");
>        out = response.getWriter();
>        out.write(JSONObject.toJSON(map).toString());
>      } catch (IOException e) {
>        e.printStackTrace();
>      } finally {
>        if (out != null) {
>          out.close();
>        }
>      }
>    }
>  }
>
>  ```
>
>- 所有的controller 继承此父类
>
>  ```java
>  package com.ybkj.happyGo.controller;
>
>  import java.io.File;
>  import java.util.UUID;
>
>  import org.springframework.beans.factory.annotation.Autowired;
>  import org.springframework.beans.factory.annotation.Value;
>  import org.springframework.stereotype.Controller;
>  import org.springframework.web.bind.annotation.RequestMapping;
>  import org.springframework.web.bind.annotation.RequestMethod;
>  import org.springframework.web.bind.annotation.RequestParam;
>  import org.springframework.web.bind.annotation.ResponseBody;
>  import org.springframework.web.multipart.MultipartFile;
>
>  import com.ybkj.happyGo.bean.AdInfoBean;
>  import com.ybkj.happyGo.bean.PageBean;
>  import com.ybkj.happyGo.service.AdInfoService;
>  import com.ybkj.happyGo.util.ResultBean;
>  import com.ybkj.happyGo.util.ServiceResult;
>
>  /**
>   * 广告 ad_info
>   * @author guozi
>   *2018年1月4日
>   */
>  /**
>   * @author guozi
>   *2018年1月30日
>   */
>  @Controller
>  public class AdInfoController extends BaseController{
>
>    @Autowired
>    private AdInfoService adInfoService;
>
>    @Value("${save.path}")
>    private String savePath;
>
>    @Value("${access.path}")
>    private String accessPath;
>
>    @Value("${adImg.path}")
>    private String imgPath;
>
>    /**
>  	 * 根据id删除
>  	 * @return
>  	 */
>    @RequestMapping(value="/deleteAdInfoById", method=RequestMethod.GET)
>    @ResponseBody
>    public ResultBean deleteAdInfoById(int adInfoId) {
>      ResultBean resultBean = new ResultBean();
>      ServiceResult result = adInfoService.deleteAdInfoById(adInfoId);
>      if (0 == result.getStatus()) {
>        resultBean.setStatus("SUCCESS");
>      }else {
>        resultBean.setStatus("FAIL");
>      }
>      return resultBean;
>    }
>
>    /**
>  	 * 根据id查找
>  	 * @return
>  	 */
>    @RequestMapping(value="/findAdInfoById", method=RequestMethod.GET)
>    @ResponseBody
>    public ResultBean findAdInfoById(int adInfoId) {
>      ResultBean resultBean = new ResultBean();
>      ServiceResult result = adInfoService.selectAdInfoById(adInfoId);
>      if (0 == result.getStatus()) {
>        resultBean.setStatus("SUCCESS");
>        resultBean.setData(result.getData());
>      }else {
>        resultBean.setStatus("FAIL");
>      }
>      return resultBean;
>    }
>
>    /**
>  	 * 编辑之更新
>  	 * @return
>  	 */
>    @RequestMapping(value="/updateAdInfoById", method=RequestMethod.POST)
>    @ResponseBody
>    public ResultBean updateAdInfoById(AdInfoBean adinfo, @RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile) {
>      ResultBean resultBean = new ResultBean();
>
>      if (null != uploadFile) {
>        try {
>          String fileName = uploadFile.getOriginalFilename();  
>          String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
>
>          String name = UUID.randomUUID().toString();
>
>          //				保存路径
>          File targetFile = new File(savePath+imgPath, name+"."+suffix);  
>          if (!targetFile.exists()) {  
>            targetFile.mkdirs();  
>            Runtime.getRuntime().exec("chmod 0755 -R " + savePath+imgPath);  
>          }  
>          uploadFile.transferTo(targetFile);
>          Runtime.getRuntime().exec("chmod 0755 -R " + targetFile);  
>          //				访问路径
>          String fileUrl = accessPath+imgPath+name+"."+suffix;
>          adinfo.setImg(fileUrl);
>        } catch (Exception e) {
>          e.printStackTrace();
>          resultBean.setStatus("UPLOADIMG_FAIL");
>          return resultBean;
>        }
>      }
>
>      ServiceResult result = adInfoService.updateAdInfoById(adinfo);
>      if (0 == result.getStatus()) {
>        resultBean.setStatus("SUCCESS");
>        resultBean.setData(result.getData());
>      }else {
>        resultBean.setStatus("FAIL");
>      }
>      return resultBean;
>    }
>
>    /**
>  	 * 获取所有广告
>  	 * @return
>  	 */
>    @RequestMapping(value="/findAdInfoList", method=RequestMethod.GET)
>    @ResponseBody
>    public ResultBean findAdInfoList(PageBean<AdInfoBean> pageBean) {
>      ResultBean resultBean = new ResultBean();
>      ServiceResult result = adInfoService.findAdInfoList(pageBean);
>      if (0 == result.getStatus()) {
>        resultBean.setStatus("SUCCESS");
>        resultBean.setData(result.getData());
>      }else {
>        resultBean.setStatus("FAIL");
>      }
>      return resultBean;
>    }
>
>    /**
>  	 * 保存广告
>  	 * @param adinfo
>  	 * @return
>  	 */
>    @RequestMapping(value="/addAdInfo", method = RequestMethod.POST)
>    @ResponseBody
>    public ResultBean addAdInfo(AdInfoBean adinfo, @RequestParam(value = "uploadFile", required = true) MultipartFile uploadFile) {
>      //		----------------图片保存--------------------------
>      ResultBean resultBean = new ResultBean();
>
>      try {
>        String fileName = uploadFile.getOriginalFilename();  
>        String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
>
>        String name = UUID.randomUUID().toString();
>        //			保存路径
>        File targetFile = new File(savePath+imgPath, name+"."+suffix);  
>        if (!targetFile.exists()) {  
>          targetFile.mkdirs();  
>          Runtime.getRuntime().exec("chmod 0755 -R " + savePath+imgPath);  
>        }  
>        uploadFile.transferTo(targetFile);
>        Runtime.getRuntime().exec("chmod 0755 -R " + targetFile);  
>        //			访问路径
>        String fileUrl = accessPath+imgPath+name+"."+suffix;
>        adinfo.setImg(fileUrl);
>
>        ServiceResult serviceResult = adInfoService.addAdInfo(adinfo);
>        if (0 == serviceResult.getStatus()) {
>          resultBean.setStatus("SUCCESS");
>          resultBean.setData(serviceResult.getData());
>        }else {
>          resultBean.setStatus("ADDADINFO_FAIL");
>        }
>
>      } catch (Exception e) {
>        resultBean.setStatus("UPLOADIMG_FAIL");
>        e.printStackTrace();
>      }
>
>      return resultBean;
>    }
>  }
>
>  ```
>
>

### 方案二：利用springmvc的异常统一处理，返回json

>- 编写类
>
>  ```java
>  package com.ybkj.happyGo.resolver;
>
>  import java.util.HashMap;
>  import java.util.Map;
>
>  import javax.servlet.http.HttpServletRequest;
>  import javax.servlet.http.HttpServletResponse;
>
>  import org.apache.shiro.authc.IncorrectCredentialsException;
>  import org.apache.shiro.authc.UnknownAccountException;
>  import org.apache.shiro.authz.AuthorizationException;
>  import org.apache.shiro.authz.UnauthenticatedException;
>  import org.springframework.web.servlet.HandlerExceptionResolver;
>  import org.springframework.web.servlet.ModelAndView;
>
>  import com.alibaba.fastjson.support.spring.FastJsonJsonView;
>
>  /**
>   * @author guozi 2018年2月26日
>   */
>  public class CustomExceptionResolver implements HandlerExceptionResolver {
>
>    @Override
>    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
>                                         Exception ex) {
>      ModelAndView modelAndView = new ModelAndView();
>
>      FastJsonJsonView view = new FastJsonJsonView();
>      Map<String, Object> attributes = new HashMap<String, Object>();
>
>      if (ex instanceof UnauthenticatedException ) {
>        attributes.put("status", "-999");
>        attributes.put("info", "未登录 ,请先登录");
>      }else if (ex instanceof UnknownAccountException ) {
>        attributes.put("status", "-998");
>        attributes.put("info", "登录失败 ，帐号不存在");
>      } else if (ex instanceof IncorrectCredentialsException) {
>        attributes.put("status", "-997");
>        attributes.put("info", "登录失败 ，密码有误");
>      }  else if (ex instanceof AuthorizationException) {
>        attributes.put("status", "-996");
>        attributes.put("info", "权限不足");
>      }
>      view.setAttributesMap(attributes);
>      modelAndView.setView(view);
>      return modelAndView;
>    }
>
>  }
>
>  ```
>
>  - spring-mvc.xml中注入类
>
>    ```xml
>    <?xml version="1.0" encoding="UTF-8"?>
>    <beans xmlns="http://www.springframework.org/schema/beans"
>           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
>           xmlns:context="http://www.springframework.org/schema/context"
>           xmlns:tx="http://www.springframework.org/schema/tx"
>           xmlns:mvc="http://www.springframework.org/schema/mvc"
>           xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
>           xmlns:p="http://www.springframework.org/schema/p"
>           xmlns:aop="http://www.springframework.org/schema/aop"
>           xsi:schemaLocation="http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-4.2.xsd
>                               http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.2.xsd
>                               http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-2.5.xsd
>                               http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.5.xsd
>                               http://code.alibabatech.com/schema/dubbo
>                               http://code.alibabatech.com/schema/dubbo/dubbo.xsd
>                               http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.5.xsd">
>
>      <context:component-scan base-package="com.ybkj.happyGo.controller"/>
>
>      <context:property-placeholder location="classpath:config.properties" />
>
>      <mvc:annotation-driven> 
>        <!-- 自定义json转换 -->
>        <mvc:message-converters> 
>          <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter"> 
>            <property name="objectMapper"> 
>              <bean class="com.ybkj.happyGo.util.JsonObjectMapper" />
>            </property> 
>          </bean> 
>        </mvc:message-converters> 
>      </mvc:annotation-driven>
>
>      <!-- 定义文件上传解析器 -->
>      <bean id="multipartResolver"
>            class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
>        <!-- 设定默认编码 -->
>        <property name="defaultEncoding" value="UTF-8"></property>
>        <!-- 设定文件上传的最大值5MB，5*1024*1024 -->
>        <property name="maxUploadSize" value="5242880"></property>
>      </bean>
>
>      <!-- 异常处理 -->
>      <bean class="com.ybkj.happyGo.resolver.CustomExceptionResolver"></bean> 
>    </beans>
>
>    ```
>
>    ​

