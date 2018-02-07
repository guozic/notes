# hibernate

## 0. 框架简介

![1_ssh](E:\学习文件\学习教材\hibernate_day01\讲义\hi_day01_picture\1_ssh.png)

## 1.hibernate框架简介

### 	1.0版本简介

​		4.0不兼容3.0 ，5.0兼容3.0。

### 	1.1是持久层框架（dao层）

​		a.什么是持久化？

​		--就是将数据保存到硬盘或云盘。区别于内存中的数据

### 	1.2是 ORM  框架 （object ralational mapping）

​		ORM:对象关系映射，java对象与关系型数据库的映射

​			   即数据库表中的一条数据  对应  一个javaBean的对象

​		映射：指的是对应关系

​		相较以前：增加了一个M，即对  java对象与数据库表之间的映射

​					运用对象的思想，对数据进行操作。

![img](file:///E:/%E5%AD%A6%E4%B9%A0%E6%96%87%E4%BB%B6/%E5%AD%A6%E4%B9%A0%E6%95%99%E6%9D%90/hibernate_day01/%E8%AE%B2%E4%B9%89/hi_day01_picture/2_orm.png?lastModify=1505699608)

### 	1.3本质是对JDBC封装

​		底层：将本来对数据库操作的 JDBC的固定操作  通过反射，配置文件等技术进行封装

### 	1.4用对象来操作数据库（面向对象）

​		获取session对象，然后对数据进行 CRUD  操作

​		获取Transcation事务对象，对事物进行操作

## 2. 两个xml配置文件

###2.1 映射配置文件

		#### a. 作用

​	*处理javaBean与表格之间的映射关系*

####b. 位置

​	建议 与对应javaBean在同一包下

####c. 标签介绍

<!-- 引入约束 -->

```xml-dtd
<!DOCTYPE hibernate-mapping PUBLIC 
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
    "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
```
<!-- 配置 -->
```xml
<hibernate-mapping>
		<!-- class 处理javaBean 与 表的映射  关系
			a. 作用
				表与javaBean的映射关系
		
              b. 常见属性
                  name: javaBean的全路径名称
                  table:对应的表的表名（细节：如与javaBean名称相同，可省略）
                  catalog: 指定表所在数据库的名字

              c. 重点：name, table
		 -->
	<class name="com.guozicheng.bean.Customer" table="cst_customer">
	
        <!-- id -设置主键 
        	2. id标签
                  a. 作用
                	  完成主键字段的映射
                  b. 常见属性
                    name: 类中表示主键的属性的名称
                    column:表中主键的字段名称（细节：如和name名称相同，可省略）
                    type: 类型，java类型或者hibernate类型
                    以int为例， java类型:java.lang.Integer  hibernate类型:integer
                    length:表中长度
                  c. 重点：name, column

        -->
        <id name="cust_id" column="cust_id">
            <!-- 主键生成策略 -->
            <generator class="native"></generator>
        </id>
        
        
        <!-- property  设置普通键 
                a. 作用
         		   完成普通字段的映射
                b. 常见属性
                    name: 类中表示普通字段的属性的名称
                    column: 表中普通字段的字段名称（细节：如果和name名称相同，可以省略）
                    type: 类型，java类型或者hibernate类型（默认）
                         java:java.lang.Integer  hibernate:integer
                    length:长度
                    not-null: 表中该字段，是否增加非空约束
                    unique: 表中该字段，是否增加唯一约束
                c. 重点：name,column
        -->
        <property name="cust_name" column="cust_name"></property>
        <property name="cust_user_id" column="cust_user_id"></property>
        ......
	</class>
</hibernate-mapping>
```

###2.2 hibernate核心配置文件

#### a. 作用

 * 关联数据库
 * 确定数据库类型
 * 关联映射配置文件

#### b. 位置

​	src目录下

#### c. 标签介绍

*<!-- 引入约束  -->* 

``` xml-dtd
<!DOCTYPE hibernate-configuration PUBLIC
	"-//Hibernate/Hibernate Configuration DTD 3.0//EN"
	"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
```

<!--  配置  -->

```xml
<hibernate-configuration>
<session-factory>
	<!-- 1.配置数据库连接参数(必选参数)
		name属性值 是hibernate固定的，
			具体可查阅b. 名称的来历：hibernate-release-5.0.7.Final\project\etc\hibernate.properties
	-->
	<property name="hibernate.connection.driver_class">com.mysql.jdbc.Driver</property>
	<property name="hibernate.connection.url">jdbc:mysql://localhost:3306/hibernate_01</property>
	<property name="hibernate.connection.username">root</property>
	<property name="hibernate.connection.password">123</property>

	<!-- 2.配置hibernate自身的属性：方言 : 制定底层的数据库类型 (必选参数)
    	* 确定要操作的数据库的类型 oracle  mysql
        * hibernate的跨数据库  更换数据库库时，只需指定数据库，其他代码无需修改
        *  org.hibernate.dialect.MySQLDialect 中   MySQL 的改变确定数据库库的类型
    -->
	<property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>
	
	<!-- 2.1 显示sql语句 （可选参数）
    	==》可知 hibernate的底层，也是通过sql语句，来操作数据库。
    -->
	<!-- 控制台显示 该操作对应的  sql   语句-->
    <property name="hibernate.show_sql">true</property>
    <!-- 控制台显示 该操作对应的  sql   语句时 对语句进行 格式化-->
    <property name="hibernate.format_sql">true</property>
    
    
    <!-- 2.2 是否自动建表 （可选参数）
    	* 取值范围
			(1) none:默认值
			(2) create:启动时，自动建表（测试）
			(3) create-drop:启动时，自动建表，关闭时，自动销毁表（测试）
         	 (4) update:没有表的时候，自动建表。有表，使用原来的表。如果表结构不匹配，更新表结构（测试）
          	 (5) validate:不会自动建表。如果有表，检查表的结构，如果，不匹配，报错。（测试）
         * 实际开发中数据库表都是提前设计好的，故此配置很少使用
    -->
    <property name="hibernate.hbm2ddl.auto">none</property>
    
    
	<!-- 3. 加载映射文件  (必选参数)   -->
	<mapping resource="com/guozicheng/bean/Customer.hbm.xml"/>

</session-factory>
</hibernate-configuration>
```





## 3.四个对象

###configuration

> 1. 作用即概述
>     * 加载核心配置文件
> 2. 常用API
>    * configure（）  ： 空参  默认加载 src下文件名为 "hibernate.cfg.xml"  的核心配置文件
>    * 注：默认名注意书写  前后不能有空格
>    * configure（String  resource） : 带参 加载自定义名字的  核心配置文件
>    * addResource(String resourceName)：  加载 指定的 映射文件
>    * buildSessionFactory(): 用于创建SessionFactory对象  返回SessionFactory

###SessionFactory

> 1. 作用及概述
>
>    * **作用：创建session对象**
>    * 注意：SessionFactory不是轻量级的，即创建此对象的内存消耗很大，故一个项目只需要一个即可
>    * **本质：对JDBC中数据库连接池的封装（C3P0，DBCP，druid等等)**
>
> 2. 常用API
>
>    ``` 
>    openSession（）：获取Session对象
>    ```
>
> 3. 抽取工具类
>
>    ```java
>    package com.guozicheng.utils;
>
>    import org.hibernate.Session;
>    import org.hibernate.SessionFactory;
>    import org.hibernate.cfg.Configuration;
>    /**
>    * 获取SessionFactory的成本很高，且一个项目只需要一个，故抽取成工具类
>    * SessionFactory 不用释放资源的原因：每次创建完session后 面可能还需要用到
>    * 故项目中不可关闭，等项目结束时关不关就无所谓了
>    * 
>    * 用于获取session对象
>    * @author 张理
>    *
>    */
>    public class HibernateUtils {
>       	//创建configuration
>       	private final static  SessionFactory  sessionFactory;
>       	static {
>       		Configuration configuration = new Configuration();
>       		configuration.configure();
>       		//获取sessiong工厂
>       		sessionFactory=configuration.buildSessionFactory();
>       	}
>       	//向外界提供获取session对象的方法
>       	public static Session getSession() {
>       		return sessionFactory.openSession();
>       	}
>     }
>    ```
>
> 4. 整合C3P0连接池
>
>
>    ```
>
>    1. 整合的前提
>
>       - hibernate有一个内置的数据连接池，因此不使用其他连接池也可以正常使用hibernate，但性能较差。
>       - 为来了改善性能，可使用其他性能较好的连接池。hibernate 与 C3P0进行了深度整合，故官方推荐连接池C3P0。
>
>    2. 使用log4j日志
>
>       - 增加JAR包  （3个）
>       - 增加配置文件，与hibernate核心配置文件在一起
>       - 增加配置文件
>       		<!-- 控制台显示 该操作对应的  sql   语句-->
>   		  <property name="hibernate.show_sql">true</property>
>    	 		<!-- 控制台显示 该操作对应的  sql   语句时 对语句进行 格式化-->
>    		 <property name="hibernate.format_sql">true</property>
>     
>
>    3. 增加C3P0的JAR包（3个）
>
>    4. 增加C3P0的配置
>
>       - 在hibernate核心配置文件下添加C3P0配置
>    ```
> ```xml-dtd
>   <property name="connection.provider_class">org.hibernate.connection.C3P0ConnectionProvider</property>
> ```
>
> ```xml
>   <!--在连接池中可用的数据库连接的最少数目 -->
>   <property name="c3p0.min_size">10</property>
>
>   <!--在连接池中所有数据库连接的最大数目  -->
>   <property name="c3p0.max_size">30</property>
>
>   <!--设定数据库连接的过期时间,以秒为单位,
>   	如果连接池中的某个数据库连接处于空闲状态的时间超过了timeout时间,就会从连接池中清除 -->
>   <property name="c3p0.timeout">120</property>
>
>   <!--每500秒检查所有连接池中的空闲连接 以秒为单位-->
>   <property name="c3p0.idle_test_period">500</property> 
> ```
> ```
>
> ```

###session

> 1. 作用及概述
>
>    - **作用：是执行CUDR的对象，Session 是 hibernate与数据库交互的桥梁**
>    - **本质：Session对象是对JDBC的Connection对象的封装** 
>
> 2. 常用API （操作数据）
>
>    - 增加数据
>
>      > Serializable  save(Ojbect obj);  增加数据
>
>
> - - 查询数据
>
>   - >T get(Class<T> class, Serializable id); --查询
>     >
>     >T load(Class<T> class, Serializable id);--查询
>
>     >get()与load()的异同
>     >
>     >- 同  ：都可以查询数据
>     >
>     >- 异  
>     >
>     >  - get（）：立即加载策略，不管后面会不会调用此方法返回的数据，都会执行sql语句进行查询
>     >
>     >  - load（）：延迟加载策略，当调用此方法时，暂时先不执行sql语句查询，等需要调用此查询返回的数据时才执行sql语句进行查询
>     >
>     >  - **延迟加载**
>     >
>     >    - **机制原理**
>     >
>     >      - load机制的延迟加载，是通过继承持久化对象，产生一个代理对象实现的
>     >
>     >    - **意义**：
>     >
>     >      - 执行sql语句是从数据库（文件系统硬盘）中读取数据，相对CPU而言，速度非常慢，故能少执行一次sql语句就能提高程序的性能，这就是延迟加载的价值。
>     >
>     >      ​
>
>
>    - 修改数据
>
>      > void update(Object obj);--修改
>
>    - 删除数据
>
>      > void delete(Object obj);--删除
>
>    - 开启事务 ，并获取事务对象
>
>      > Transcation beginTransaction(); --开启事务，获得事务对象
>
> 3. 注意：此session与服务器请求响应中的 session 没有任何关系

###Transactiom

> 1. 作用及概述
>    - Transaction对象是对  处理事务的对象的  封装
>    - 通过Session对象获取，可知与Session对象密切相关
> 2. 常用API
>    - commit()   事务 提交
>    - rollback()  事务回滚

## 4.hibernate总结![img](file:///E:/%E5%AD%A6%E4%B9%A0%E6%96%87%E4%BB%B6/%E5%AD%A6%E4%B9%A0%E6%95%99%E6%9D%90/hibernate_day01/%E8%AE%B2%E4%B9%89/hi_day01_picture/5_jdbcvshi.png?lastModify=1505713725?lastModify=1505713731)

> Configuration对象是对JDBC的封装
>
> SessionFactory对象是对数据库连接池的 封装
>
> Session对象是对连接池中Connection的封装

![img](file:///E:/%E5%AD%A6%E4%B9%A0%E6%96%87%E4%BB%B6/%E5%AD%A6%E4%B9%A0%E6%95%99%E6%9D%90/hibernate_day01/%E8%AE%B2%E4%B9%89/hi_day01_picture/4c3p0vssf.png?lastModify=1505713717)

![img](file:///E:/%E5%AD%A6%E4%B9%A0%E6%96%87%E4%BB%B6/%E5%AD%A6%E4%B9%A0%E6%95%99%E6%9D%90/hibernate_day01/%E8%AE%B2%E4%B9%89/hi_day01_picture/5_jdbcvshi.png?lastModify=1505713725?lastModify=1505713731?lastModify=1505713731)





## 5.客户保存的案例

> ```java
> package com.guozicheng.dao.impl;
>
>
> import com.guozicheng.bean.Customer;
> import com.guozicheng.dao.CustomerDao;
> import com.guozicheng.utils.HibernateUtils;
>
> import org.hibernate.Session;
> import org.hibernate.SessionFactory;
> import org.hibernate.Transaction;
> import org.hibernate.cfg.Configuration;
>
> public class CustomerDaoImpl implements CustomerDao {
>   
> 	@Override
> 	public void saveCustomer(Customer customer) {
> 		/*//获取对象
> 		Configuration configuration=new Configuration().configure();
> 		//获取session工厂
> 		SessionFactory sessionFactory = configuration.buildSessionFactory();
> 		//获取session
> 		Session session = sessionFactory.openSession();*/
> 		Session session = HibernateUtils.getSession();
> 		//开启事务  并获取事务对象
> 		Transaction transaction = session.beginTransaction();
> 		//执行业务
> 		session.save(customer);
> 		//提交事务
> 		transaction.commit();
> 		//释放资源
> 		session.close();
> 	}
> }
> ```

## 6.实战案例入门

> 1. 创建工程（java工程或者web工程都可以）
>
>    - 使用java工程
>
> 2. 导入hibernate的所需jar包
>    a. hibernate所需的：资料\jar包整理\hibernate所需最小的jar集合\*.jar
>    b. mysql的jdbc驱动
>
> 3. 创建表
>
>    - user表 
>
> 4. 创建javaBean
>
>    - User类
>
> 5. **创建映射文件**
>    a. 作用：处理表与类的映射(对应)关系
>    b. 位置：推荐：和对应的类放在同一个包下
>    c. 名字：推荐：类名.hbm.xml -->User.hbm.xml
>    d. 约束：hibernate-core-5.0.7.Final.jar\org.hibnerate\hibernate-mapping-3.0.dtd
>
> 6. **创建核心配置文件（hiberante自身的配置文件）**
>    a. 作用：配置hibernate本身
>    b. 位置：推荐：src下
>    c. 名字：推荐：hibernate.cfg.xml
>    d. 约束：hibernate-core-5.0.7.Final.jar\org.hibnerate\hibernate-configuration-3.0.dtd
>
> 7. __hibernate的使用（7步曲）__
>    // 参考
>    //1. 创建配置对象
>    Configuration configuration = new Configuration();
>    configuration.configure(); // 加载hibernate.cfg.xml
>
>    //2. 通过Configuration对象，获得SessionFactory对象
>    SessionFactory  sf= configuration.buildSessionFactory();
>
>    //3.  通过SessionFactory对象，获得session对象
>    Session session =sf.openSession();
>
>    //4. 通过Session对象，开启事务，并获得transaction事务对象
>    Transaction  transaction=  session.beginTransaction();
>
>    //5. 实际的业务（模拟用户注册）
>    User user = new User();
>    user.setName("admin");
>    user.setPassword("123");
>
>    session.save(user);
>
>    //6. 事务提交
>    transaction.commit();
>
>    //7. 释放资源
>    session.close();
>    sf.close();
>
> - 注：sf.close();无需关闭。
>   - 原因：
>     1. SessionFactory 创建的代价很大
>     2. 一个项目只需要一个Session工厂

## 7.hibernate 相关

> 1. jar的来历
>   a. 查看：hibernate-release-5.0.7.Final.zip文件
>
>   b. 看看
>   ```java
>   (1) antlr-2.7.7.jar
>   	* 语言转换工具，Hibernate利用它实现 HQL 到 SQL的转换 
>   (2) dom4j-1.6.1.jar
>   	* dom4j XML 解析器
>   (3) geronimo-jta_1.1_spec-1.1.1.jar
>   	* jta(Java Transaction API),JAVA 事务处理规范
>   (4) hibernate-commons-annotations-5.0.1.Final.jar
>   	* hibernate注解配置
>   (5) hibernate-core-5.0.7.Final.jar
>   	* hibernate的核心包
>   (6) hibernate-jpa-2.1-api-1.0.0.Final.jar
>   	* hibernate实现jta规范
>   (7) jandex-2.0.0.Final.jar
>   	* 用来索引annotation注解
>   (8) javassist-3.18.1-GA.jar
>   	* 用于操作字节码增强
>   (9) jboss-logging-3.3.0.Final.jar
>   	* 使用注解解释器实现带注释的接口
>   ```
>
> 2. 导入约束文件
>   见《eclipse导入约束的操作.docx》
>
>   - 作用：产生xml提示的效果
>
> 3. 易错点
>   0. 写错单词
>         a. Transaction对象，导入错误的包
>         b. 没有配置主键增长策略：<generator class="native"></generator>
>         c. 建表语句，没有使用auto_increment
>         d. 映射文件中，class--name配置类的全路径名称："cn.itcast.domian.User" 不是"cn/itcast/domain/User.java"
>         e. 核心配置文件中，加载映射文件，使用文件路径："cn/itcast/domain/User.java"

## 8.hibernate类型对照表(部分)

	Hibernate映射类型	java类型					标准SQL类型
	integer				java.lang.Integer		integer
	long				java.lang.Long			bigint
	short				java.lang.Short			smallint
	float				java.lang.Float			float
	double				java.lang.Float			double
	big_decimal			java.math.BigDecimal	numeric
	character			java.lang.String		char(1)
	string				java.lang.String		varchar
### 9.SQL语言的分类

> * DDL（Data Definition Language）：数据定义语言，用来定义数据库对象：库database、表table,列column等。关键字：创建create, 修改alter, 删除drop等（结构）
> * DML（Data Manipulation Language）：数据操作语言，用来对数据库中表的记录进行更新。关键字：插入insert, 删除delete, 更新update等（数据）
> * DQL（Data Query Language）：数据查询语言，用来查询数据库中表的记录。关键字：select, from ,where等。
> * DCL（Data Control Language）：数据控制语言，用来定义数据库的访问权限和安全级别及创建用户;关键字：grant等

###  10 javaBean的编写

- ### 多表之间，对应的javaBean的表达方式

- - #### 一对多

    - 建表原则：多的一方创建外键指向1的一方的主键。

  - >表中的外键 ，在对应的javaBean类中用外键对应的主键的对应的javaBean对象来表示

    - 具体示例

  - >- 数据库
    >
    >  ```sql
    >  	create table department(
    >  		did  int primary key auto_increment,
    >  		dname char(20)
    >  	);
    >  			
    >  	create table employee(
    >        eid int primary key auto_increment,
    >        ename char(20),
    >        e_did int,   --标识员工所在部门的id
    >        foreign key (e_did) references department(did)
    >      );
    >  ```
    >
    >  ​
    >
    >- java代码
    >
    >  ```java
    >  // 每个部门，有多个员工
    >  	class c{
    >  		Integer  id;
    >  		String   name;
    >   	}
    >  			
    >      // 每个员工归属于一个部门
    >  	class Employee{
    >  		Integer  id;
    >  		String   name;
    >  		Department department
    >  	 }	
    >  ```
    >
    >  ​

    ​

  - #### 多对多

    -  建表原则：创建一个中间表，新增一个没有意义的字段，作为主键；有两个字段作为外键，分别指向多对多双方的主键

  - > 在该表对应的javaBean中  添加一个set集合来放置与该表对应的另外一个表对应的javaBean的对象

    - 具体示例

    - >- 数据库
      >
      >  ```sql
      >  -- 学生选课表
      >  create table student_course(
      >    sc_id int primary key auto_increment, --没有意义的字段，做单独主键
      >    fk_sid int,
      >    fk_cid int,
      >    foreign key (fk_sid) references student(sid),
      >    foreign key (fk_cid) references course(cid)
      >  );
      >  ```
      >
      >  ​
      >
      >- java代码
      >
      >  ```sql
      >  // 每个部门，有多个员工
      >  	class c{
      >  		Integer  id;
      >  		String   name;
      >   	}
      >  			
      >      // 每个员工归属于一个部门
      >  	class Employee{
      >  		Integer  id;
      >  		String   name;
      >  		Department department
      >  	 }	
      >  ```

- ​

- ​

- 多表之间，对应的javaBean的表达方式具有方向性

  - ​

