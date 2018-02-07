# Windows及Linux下请求Tomcat中文的乱码问题

## 请求Tomcat乱码

### 原因分析

>- 请求带过来的参数的字符编码集**是**UTF-8
>
>- Windows下Tomcat默认的字符编码集是ISO8859-1
>
>- 请求的参数过来经过Tomcat进行编码转换，将UTF-8编码的字符转换成了乱码，此时乱码的内容是  ISO8859-1类型字符
>
>  #### 分析
>
>  - 将此时为ISO8859-1的乱码通过ISO8859-1转换回去
>  - 再通过UTF-8来进行转换
>  - UTF-8编码->UTF-8(iso-8859-1)编码->iso-8859-1解码->UTF-8解码
>  - 编码和解码的过程是对称的，所以不会出现乱码。

## 解决方案

#### post请求

```xml
<!-- 解决post乱码 -->
<filter>
  <filter-name>SetCharacterEncoding</filter-name>
  <filter-class>
    org.springframework.web.filter.CharacterEncodingFilter
  </filter-class>
  <init-param>
    <param-name>encoding</param-name>
    <param-value>UTF-8</param-value>
  </init-param>
</filter>
<filter-mapping>
  <filter-name>SetCharacterEncoding</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>
```

#### get请求

##### 方案一：修改Tomcat配置

- C:\software\installation\ApacheTomcat\apache-tomcat-9.0.4\conf 文件夹下server.xml文件中修改配置

  将

  ```xml
  <Connector port="8080" protocol="HTTP/1.1"
                 connectionTimeout="20000"
                 redirectPort="8443" />
  ```

  添加

  ```xml
  useBodyEncodingForURI="true" 
  URIEncoding="UTF-8"
  ```

  修改为如下，

  ```xml
  <Connector port="8080" protocol="HTTP/1.1"
                 connectionTimeout="20000"
                 redirectPort="8443"  
                 useBodyEncodingForURI="true" 
                 URIEncoding="UTF-8"/>
  ```

##### 方案二：java代码

- ```java
  /**
  	 * 角色列表、有搜索条件就搜查
  	 * @return
  	 * @throws UnsupportedEncodingException 
  	 */
  @RequestMapping(value="adminRole/findAll", method=RequestMethod.GET)
  @ResponseBody
  public ResultBean findAll(PageBean<AdminRoleBean> pageBean, @RequestParam(value="name", required=false ) String name){
  /*===============开始==========================*/
    if (null != name) {
  			try {
  				name = new String(name.getBytes("iso8859-1"), "utf-8");
  			} catch (UnsupportedEncodingException e) {
  				e.printStackTrace();
  			}
  		}
  /*===============结束==========================*/
    ResultBean resultBean = new ResultBean();
    ServiceResult serviceResult = adminRoleService.findAll(pageBean, name);
    if (0 == serviceResult.getStatus()) {
      resultBean.setStatus("SUCCESS");
      resultBean.setData(serviceResult.getData());
    }else {
      resultBean.setStatus("FAIL");
    }
    return resultBean;
  }
  ```

  ​

## 注意

- 开发环境为Windows，部署环境为Linux的情况下
- **因为Windows下的Tomcat默认字符集为ISO8859-1，而Linux下的Tomcat默认的字符集不一样的**
- 所以个人觉得还是修改两个Tomcat的配置更优