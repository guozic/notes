# SSH整合步骤

## 1.整合思想

### 将所有的对象交给Spring统一管理

1.将struts2中的action对象交给Spring管理

2.将hibernate中的sessionFactory（session，transaction）对象交给Spring管理

## 2.整合步骤

### 1.导包

#### 1.hibernate的JAR包

#### 2.struts2的JAR包

#### 3.Spring的JAR包

### 2.单独配置Spring容器

#### 1.创建配置文件applicationContext.xml

##### 1.导入约束 

#### 2.配置Spring随项目启动

1.在web.xml中配置Spring随web启动而创建工厂的监听器

- 记住 ContextLoaderListener
- 记住 contextConfigLocation

2.配置Spring配置文件的位置

```xml
<!-- 配置Sring随项目的启动而创建  的容器 -->
  <listener>
  	<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>
  <!-- 配置appactionContext.xml -->
  <context-param>
  	<param-name>contextConfigLocation</param-name>
  	<param-value>classpath:applicationContext.xml</param-value>
  </context-param>
```

### 3.单独配置struts2

#### 1.创建配置文件struts.xml

导入约束，配置package标签

#### 2在web.xml中.配置struts2核心过滤器

- 注意区分：struts2的核心是  过滤器，而Spring的创建工厂的配置的监听器
- 记住：StrutsPrepareAndExecuteFilter

```xml
 <!-- 配置struts2的核心过滤器 -->
  <filter>
  	<filter-name>struts2</filter-name>
  	<filter-class>org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter</filter-class>
  </filter>
  <filter-mapping>
  	<filter-name>struts2</filter-name>
  	<url-pattern>/*</url-pattern>
  </filter-mapping>
```

### 4.Spring与struts2整合

#### 1.导包 (struts2与Sping整合包)

在strut2中  struts2-spring-xxx

#### 2.配置常量（在struts.xml中）（可省略）

##### -->此步骤可省略的原因

- struts-spring-xxx-JAR中的 struts-plugin.xml中已经配置了这个常量！！！


- 常量配置的数据来源：struts2-core-xxx.jar     ==》org-apache.struts  ==》static ==》  default.properties中
- struts.objectFactory = spring  ---->   将action的创建交给Spring  需要手动配置
  -  ==》  struts2默认关闭 
- struts.objectFactory.spring.autoWire = name    --->Spring负责Action依赖属性（依赖注入）
  - 例如 action中需要的serviceImpl   ，就用Spring来创建注入
  - ==》struts2默认打开 （但是当上面一个关闭，这个打开也没有什么卵用）

#### 3.配置Action由谁来创建的两种方式

#### 方式一(推荐)：Spring创建action，并组装依赖属性

- 注意：需要手动组装依赖属性


- struts.xml中

```xml
<?xml version="1.0" encoding="UTF-8"?>
  <!DOCTYPE struts PUBLIC
	"-//Apache Software Foundation//DTD Struts Configuration 2.3//EN"
	"http://struts.apache.org/dtds/struts-2.3.dtd">
	
<struts>
	<!-- #  struts.objectFactory = spring	将action的创建交给spring容器	
			struts.objectFactory.spring.autoWire = name spring负责装配Action依赖属性
			-->
	<constant name="struts.objectFactory" value="spring"></constant>

	<package name="crm" namespace="/" extends="struts-default" >
		<global-exception-mappings>
			<exception-mapping result="error" exception="java.lang.RuntimeException"></exception-mapping>
		</global-exception-mappings>
	
		<!-- 方式二:class属性上仍然配置action的完整类名
				struts2仍然创建action,由spring负责组装Action中的依赖属性
		 -->
		 <!-- 
		 	方式一:class属性上填写spring中action对象的Bean标签的Name属性值
		 		完全由spring管理action生命周期,包括Action的创建
		 		注意:需要手动组装依赖属性
		  -->
		<action name="UserAction_*" class="userAction" method="{1}" >
			<result name="toHome" type="redirect" >/index.htm</result>
			<result name="error" >/login.jsp</result>
		</action>
	</package>
</struts>
	
```

- applicationContex.xml中

```xml
<!-- action -->
	<!-- 注意:Action对象作用范围一定是多例的.这样才符合struts2架构 -->
	<bean name="userAction" class="cn.itcast.web.action.UserAction" scope="prototype" >
		<property name="userService" ref="userService" ></property>
	</bean>
	<!-- service -->
	<bean name="userService" class="cn.itcast.service.impl.UserServiceImpl" >
		<property name="ud" ref="userDao" ></property>
	</bean>
	<!-- dao -->
	<bean name="userDao" class="cn.itcast.dao.impl.UserDaoImpl" >
		<!-- 注入sessionFactory -->
		<property name="sessionFactory" ref="sessionFactory" ></property>
	</bean>
```

#### 方式二：struts2创建Action，Spring组装依赖属性

- 依赖属性由Spring自动配置，不需要手动组装
- 方式二:class属性上仍然配置action的完整类名，struts2仍然创建action,由spring负责组装Action中的依赖属性

### 5.单独整合hibernate

#### 1.表格对应的实体类及其映射文件

1. javaBean类（全限定名）对应的表格名 ==》 标签 class【name  table】
2. 主键，代理主键，增长类型  ==》id【name column】    generator【class】
3. 类属性，表字段对应   ==》property【name column】

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-mapping PUBLIC 
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
    "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
    
<hibernate-mapping>
	<class name="com.guozicheng.bean.User" table="crm_user">
		<id name="user_id" column="crm_id">
			<generator class="native"></generator>
		</id>
		<!-- 
		private Long user_id;
		private String user_code;
		private String user_name;
		private String user_password;
		private Character user_state;
		 -->
		<property name="user_code" column="crm_code"></property>
		<property name="user_name" column="crm_name"></property>
		<property name="user_password" column="crm_password"></property>
		<property name="user_state" column="crm_state"></property>
	</class>
</hibernate-mapping>
```



#### 2.核心配置文件

1. 核心配置（数据库链接）  ==》session-factory
2. 显示执行语句 ，格式化sql ,是否自动建表
3. - 注意建表配置
4. 导入映射

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
	"-//Hibernate/Hibernate Configuration DTD 3.0//EN"
	"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
	<session-factory>
		<!-- 1. 核心必须配置 连数据库 -->
		<property name="hibernate.connection.driver_class">com.mysql.jdbc.Driver</property>
		<property name="hibernate.connection.url">jdbc:mysql:///spring</property>
		<property name="hibernate.connection.username">root</property>
		<property name="hibernate.connection.password">123</property>
		
		<!-- 2. 可选配置 方言 ， 显示执行语句 ，格式化sql ,是否自动建表  -->
		<property name="hibernate.dialect">org.hibernate.dialect.MySQL5Dialect</property>
		<property name="hibernate.show_sql">true</property>
		<property name="hibernate.format_sql">true</property>
		<property name="hibernate.hbm2ddl.auto">update</property>
		
		<!-- 
			hibernate.hbm2ddl.auto==》
			create ： 永远都会建表 
			update ： 如果有表就使用原来的表，没有就建表。
			none: 不管如何，都不会自动建表
 		-->	
		<!-- 3. 映射文件导入 -->
		<mapping resource="com/guozicheng/bean/User.hbm.xml"/>
	</session-factory>
</hibernate-configuration>
```

- 测试

```java
package com.guozicheng.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.guozicheng.bean.User;


public class HibernateTest {
	
	@Test
	public void Test() {
		//获取hibernate 工厂
		Configuration configuration=new Configuration().configure();
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		User user =new User();
		user.setUser_name("辛弃疾");
		user.setUser_password("aaa");
		
		session.save(user);
		transaction.commit();
		session.close();
	}
}
```

### 6.Spring整合hibernate

#### 整合原理

- 将sessionFactory对象交给spring容器管理

#### 在spring中配置sessionFactory

##### 方式一：仍然使用hibernate.cfg.xml文件

##### 方式二（推荐）：在applicationContext.xml中配置hibernate所需要的信息

- 去掉hibernate.cfg.xml文件
- applicationContext.xml配置中----->

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:tx="http://www.springframework.org/schema/tx" 
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:aop="http://www.springframework.org/schema/aop" 
	xmlns="http://www.springframework.org/schema/beans"
	xsi:schemaLocation="http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.2.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.2.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.2.xsd http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.2.xsd ">

	<bean id="userAction" class="com.guozicheng.web.action.UserAction">
		<property name="userServiceImpl" ref="userServiceImpl"></property>
	</bean>
	<bean id="userServiceImpl" class="com.guozicheng.service.Impl.UserServiceImpl">
		<!-- <property name="userDaoImpl" ref="userDaoImpl"></property> -->
	</bean>
	<!-- <bean id="userDaoImpl" class="com.guozicheng.dao.Impl.UserDaoImpl">
		注入sessionFactory
		<property name="sessionFactory" ref="sessionFactory" ></property>
	</bean> -->

<!-- 配置hibernate.cfg.xml文件中所的配置，删除hibernate.cfg.xml文件 -->
  
	<!-- 加载配置方案一:仍然使用外部的hibernate.cfg.xml配置信息 -->
	<!-- <bean name="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean" >
		<property name="configLocation" value="classpath:hibernate.cfg.xml" ></property>
	</bean> -->
	
	<!-- 加载配置方案二（推荐）  :在applicationContext.xml中配置hibernate所需要的信息-->
	<bean name="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
		<!-- 配置hibernate的基本信息 -->
		<property name="hibernateProperties">
			<props>
			<!-- 核心配置 -->
				<prop key="hibernate.connection.driver_class">com.mysql.jdbc.Driver</prop>
				<prop key="hibernate.connection.url">jdbc:mysql:///spring</prop>
				<prop key="hibernate.connection.username">root</prop>
				<prop key="hibernate.connection.password">123</prop>
			<!-- 可选配置 -->
				<prop key="hibernate.dialect">org.hibernate.dialect.MySQL5Dialect</prop>
				<prop key="hibernate.show_sql">true</prop>
				<prop key="hibernate.format_sql">true</prop>
				<prop key="hibernate.hbm2ddl.auto">update</prop>
			</props>
		</property>
		<!-- 导入映射文件 -->
      
		<!-- 导入映射文件方式一  推荐 -->
		<property name="mappingDirectoryLocations" value="classpath:com/guozicheng/bean"></property>
		<!-- 导入映射文件方式二 -->
		<!-- 
			<property name="mappingResources">
			<array>
				<value>com/itheima/bean/User.hbm.xml</value>
			</array>
		</property>
		 -->
	</bean>
</beans>
```

### 7.配置C3P0连接池

#### 1.配置db.properties

- db.properties

  ```properties
  jdbc.driverClass=com.mysql.jdbc.Driver
  jdbc.jdbcUrl=jdbc:mysql:///spring
  jdbc.user=root
  jdbc.password=123
  ```


- 注意：properties错乱会报错

- ```
  java.lang.IllegalStateException: Failed to load ApplicationContext

  Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'sessionFactory' defined in class path resource [applicationContext.xml]: Invocation of init method failed; nested exception is 
  Caused by: org.hibernate.tool.schema.spi.SchemaManagementException: Unable to open JDBC connection for schema management target

  Caused by: java.sql.SQLException: Connections could not be acquired from the underlying database!

  Caused by: com.mchange.v2.resourcepool.CannotAcquireResourceException: A ResourcePool could not acquire a resource from its primary factory or source.
  ```


#### 2.引入连接池到applicationContext.xml中

- applicationContext.xml中部分文件

```xml
<!-- 配置C3P0连接池 -->
	<!-- 配置读取properties文件 -->
	<context:property-placeholder location="classpath:db.properties" />
	<!-- 读取文件 -->
	<bean name="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource" >
		<property name="jdbcUrl" value="${jdbc.jdbcUrl}" ></property>
		<property name="driverClass" value="${jdbc.driverClass}" ></property>
		<property name="user" value="${jdbc.user}" ></property>
		<property name="password" value="${jdbc.password}" ></property>
	</bean>
```

#### 3.3.将连接池注入给SessionFactory

- applicationContext.xml中部分文件

```xml
<property name="dataSource" ref="dataSource"></property>
```

- applicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:tx="http://www.springframework.org/schema/tx" 
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:aop="http://www.springframework.org/schema/aop" 
	xmlns="http://www.springframework.org/schema/beans"
	xsi:schemaLocation="http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.2.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.2.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.2.xsd http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.2.xsd ">

	<!-- 配置C3P0连接池 -->
	<!-- 配置读取properties文件 -->
	<context:property-placeholder location="classpath:db.properties" />
	<!-- 读取文件 -->
	<bean name="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource" >
		<property name="jdbcUrl" value="${jdbc.jdbcUrl}" ></property>
		<property name="driverClass" value="${jdbc.driverClass}" ></property>
		<property name="user" value="${jdbc.user}" ></property>
		<property name="password" value="${jdbc.password}" ></property>
	</bean>


<!-- 》》》》》》》》》》》》》》》》 IOC管理 《《《《《《《《《《《《《《《《《《《《《《-->
	<bean id="userAction" class="com.guozicheng.web.action.UserAction">
		<property name="userServiceImpl" ref="userServiceImpl"></property>
	</bean>
	<bean id="userServiceImpl" class="com.guozicheng.service.Impl.UserServiceImpl">
		<!-- <property name="userDaoImpl" ref="userDaoImpl"></property> -->
	</bean>
	<!-- <bean id="userDaoImpl" class="com.guozicheng.dao.Impl.UserDaoImpl">
		注入sessionFactory
		<property name="sessionFactory" ref="sessionFactory" ></property>
	</bean> -->




<!-- 《《《《《《配置hibernate.cfg.xml文件中所的配置，删除hibernate.cfg.xml文件》》》》 -->
	<!-- 配置方式一 -->
	
	<!-- 加载配置方案1:仍然使用外部的hibernate.cfg.xml配置信息 -->
	<!-- <bean name="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean" >
		<property name="configLocation" value="classpath:hibernate.cfg.xml" ></property>
	</bean> -->
	
	<!-- 配置方式二  :在applicationContext.xml中配置hibernate所需要的信息-->
	<bean name="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
		
		<!-- 配置hibernate的基本信息 -->
		<property name="dataSource" ref="dataSource"></property>
		<property name="hibernateProperties">
			<props>
			<!-- 核心配置 -->
				<!-- <prop key="hibernate.connection.driver_class">com.mysql.jdbc.Driver</prop>
				<prop key="hibernate.connection.url">jdbc:mysql:///spring</prop>
				<prop key="hibernate.connection.username">root</prop>
				<prop key="hibernate.connection.password">123</prop> -->
			<!-- 可选配置 -->
				<prop key="hibernate.dialect">org.hibernate.dialect.MySQL5Dialect</prop>
				<prop key="hibernate.show_sql">true</prop>
				<prop key="hibernate.format_sql">true</prop>
				<prop key="hibernate.hbm2ddl.auto">update</prop>
			</props>
		</property>
		<!-- 导入映射文件 -->
		<!-- 导入映射文件方式一  推荐 -->
		<property name="mappingDirectoryLocations" value="classpath:com/guozicheng/bean"></property>
		<!-- 导入映射文件方式二 -->
		<!-- 
			<property name="mappingResources">
			<array>
				<value>com/itheima/bean/User.hbm.xml</value>
			</array>
		</property>
		 -->
	</bean>




</beans><?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:tx="http://www.springframework.org/schema/tx" 
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:aop="http://www.springframework.org/schema/aop" 
	xmlns="http://www.springframework.org/schema/beans"
	xsi:schemaLocation="http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.2.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.2.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.2.xsd http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.2.xsd ">

	<!-- 配置C3P0连接池 -->
	<!-- 配置读取properties文件 -->
	<context:property-placeholder location="classpath:db.properties" />
	<!-- 读取文件 -->
	<bean name="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource" >
		<property name="jdbcUrl" value="${jdbc.jdbcUrl}" ></property>
		<property name="driverClass" value="${jdbc.driverClass}" ></property>
		<property name="user" value="${jdbc.user}" ></property>
		<property name="password" value="${jdbc.password}" ></property>
	</bean>


<!-- 》》》》》》》》》》》》》》》》 IOC管理 《《《《《《《《《《《《《《《《《《《《《《-->
	<bean id="userAction" class="com.guozicheng.web.action.UserAction">
		<property name="userServiceImpl" ref="userServiceImpl"></property>
	</bean>
	<bean id="userServiceImpl" class="com.guozicheng.service.Impl.UserServiceImpl">
		<!-- <property name="userDaoImpl" ref="userDaoImpl"></property> -->
	</bean>
	<!-- <bean id="userDaoImpl" class="com.guozicheng.dao.Impl.UserDaoImpl">
		注入sessionFactory
		<property name="sessionFactory" ref="sessionFactory" ></property>
	</bean> -->

<!-- 《《《《《《配置hibernate.cfg.xml文件中所的配置，删除hibernate.cfg.xml文件》》》》 -->
	<!-- 配置方式一 -->
	
	<!-- 加载配置方案1:仍然使用外部的hibernate.cfg.xml配置信息 -->
	<!-- <bean name="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean" >
		<property name="configLocation" value="classpath:hibernate.cfg.xml" ></property>
	</bean> -->
	
	<!-- 配置方式二  :在applicationContext.xml中配置hibernate所需要的信息-->
	<bean name="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
		
		<!-- 配置hibernate的基本信息 -->
		<property name="dataSource" ref="dataSource"></property>
		<property name="hibernateProperties">
			<props>
			<!-- 核心配置 -->
				<!-- <prop key="hibernate.connection.driver_class">com.mysql.jdbc.Driver</prop>
				<prop key="hibernate.connection.url">jdbc:mysql:///spring</prop>
				<prop key="hibernate.connection.username">root</prop>
				<prop key="hibernate.connection.password">123</prop> -->
			<!-- 可选配置 -->
				<prop key="hibernate.dialect">org.hibernate.dialect.MySQL5Dialect</prop>
				<prop key="hibernate.show_sql">true</prop>
				<prop key="hibernate.format_sql">true</prop>
				<prop key="hibernate.hbm2ddl.auto">update</prop>
			</props>
		</property>
		<!-- 导入映射文件 -->
		<!-- 导入映射文件方式一  推荐 -->
		<property name="mappingDirectoryLocations" value="classpath:com/guozicheng/bean"></property>
		<!-- 导入映射文件方式二 -->
		<!-- 
			<property name="mappingResources">
			<array>
				<value>com/itheima/bean/User.hbm.xml</value>
			</array>
		</property>
		 -->
	</bean>
</beans>
```

### 8.spring整合hibernate环境操作数据库

#### 1.创建Dao类:继承HibernateDaoSupport

- Dao类中要注入sessionFactory  虽然Dao中没有这个属性，但是HibernateDaoSupport中有，而想要使用HibernateTemplate=getHibernateTemplate(),必须要sessionFactory  

```java
public class UserDaoImpl extends HibernateDaoSupport implements UserDao{...}
```

#### 2.hibernate模板的操作

- getHibernateTemplate()的方法

#### 3.spring中配置dao





### 9.整合中出现的异常

### 10.懒加载问题（扩大session作用域）

- load()
- 对象导航查询中也有懒加载机制

```
Write operations are not allowed in read-only mode (FlushMode.MANUAL): Turn your Session into FlushMode.COMMIT/AUTO or remove 'readOnly' marker from transaction definition.
==》只读权限下不能修改
===============================================
使用HibernateTempalate需要配置事务管理
```



```
Caused by: java.lang.IllegalStateException: Cannot convert value of type [com.sun.proxy.$Proxy36 implementing com.guozicheng.service.UserService,org.springframework.aop.SpringProxy,org.springframework.aop.framework.Advised] to required type [com.guozicheng.service.Impl.UserServiceImpl] for property 'userServiceImpl': no matching editors or conversion strategy found
==》代理类转换成接口异常
===》依赖注入属性不能用实现类，只能用接口来实现
====================================================================
解决方案：定义属性时，要用接口来接受这个属性
private UserService userService;
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

```

