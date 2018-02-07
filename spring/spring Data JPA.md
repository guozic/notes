# spring Data JPA

># JPA
>
>## JPA概述
>
>- Java Persistence API。是SUN公司推出的一套基于ORM的规范。hibernate框架中提供了JPA的实现
>- JPA通过JDK 5.0注解或XML描述对象－关系表的映射关系，并将运行期的实体对象持久化到数据库中。
>
>## JPA的优势
>
>### 标准化
>
>- JPA 是 JCP 组织发布的 Java EE 标准之一，因此任何声称符合 JPA 标准的框架都遵循同样的架构，提供相同的访问API，这保证了基于JPA开发的企业应用能够经过少量的修改就能够在不同的JPA框架下运行。
>
>### 容器级特性的支持
>
>- JPA框架中支持大数据集、事务、并发等容器级事务，这使得 JPA 超越了简单持久化框架的局限，在企业应用发挥更大的作用。
>
>### 简单方便
>
>- JPA的主要目标之一就是提供更加简单的编程模型：在JPA框架下创建实体和创建Java 类一样简单，没有任何的约束和限制，只需要使用 javax.persistence.Entity进行注释，JPA的框架和接口也都非常简单，没有太多特别的规则和设计模式的要求，开发者可以很容易的掌握。JPA基于非侵入式原则设计，因此可以很容易的和其它框架或者容器集成。
>
>### 查询能力
>
>- JPA的查询语言是面向对象而非面向数据库的，它以面向对象的自然语法构造查询语句，可以看成是Hibernate HQL的等价物。JPA定义了独特的JPQL（Java Persistence Query Language），JPQL是EJB QL的一种扩展，它是针对实体的一种查询语言，操作对象是实体，而不是关系数据库的表，而且能够支持批量更新和修改、JOIN、GROUP BY、HAVING 等通常只有 SQL 才能够提供的高级查询特性，甚至还能够支持子查询。
>
>### 高级特性
>
>- JPA 中能够支持面向对象的高级特性，如类之间的继承、多态和类之间的复杂关系，这样的支持能够让开发者最大限度的使用面向对象的模型设计企业应用，而不需要自行处理这些特性在关系数据库的持久化。
>
>## JPA和Hibernate的关系
>
>- JPA是一套ORM规范，hibernate实现了JPA规范
>- hibernate中有自己的独立ORM操作数据库方式，也有JPA规范实现的操作数据库方式。
>
>## 常用注解
>
>### @Entity
>
>- 指定当前类是实体类。写上此注解用于在创建SessionFactory/EntityManager时，加载映射配置
>
>### @Table
>
>- 指定实体类和表之间的对应关系
>- 属性name：指定数据库表的名称
>
>### @Id
>
>- 指定当前字段是主键
>
>### @GeneratedValue
>
>- 指定主键的生成方式
>
>#### 属性
>
>- strategy ：指定主键生成策略.策略为GenerationType类定义的常量
>- IDENTITY：主键由数据库自动生成（主要是自动增长型） 
>- SEQUENCE : 根据底层数据库的序列来生成主键，条件是数据库支持序列
>- AUTO : 主键由程序控制
>- TABLE : 使用一个特定的数据库表格来保存主键
>
>### @Column
>
>- 指定实体类属性和数据库表之间的对应关系
>
>#### 属性
>
>- name：指定数据库表的列名称
>- unique：是否唯一  
>- nullable：是否可以为空  
>- inserttable：是否可以插入  
>- updateable：是否可以更新  
>- columnDefinition: 定义建表时创建此列的DDL  
>- secondaryTable: 从表名。如果此列不建在主表上（默认建在主表），该属性定义该列所在从表的名字
>
>## 一对多关系注解
>
>### @OneToMany
>
>- 建立一对多的关系映射
>
>#### 属性
>
>- targetEntityClass：指定多的多方的类的字节码
>- mappedBy：指定从表实体类中引用主表对象的名称
>- cascade：指定要使用的级联操作
>- fetch：指定是否采用延迟加载
>- orphanRemoval：是否使用孤儿删除
>
>## 多对一关系注解
>
>### @ManyToOne
>
>- 建立多对一的关系
>
>#### 属性
>
>- targetEntityClass：指定一的一方实体类字节码
>
>- cascade：指定要使用的级联操作
>
>- fetch：指定是否采用延迟加载
>
>- optional：关联是否可选。如果设置为false，则必须始终存在非空关系
>
>  ### @JoinColumn
>
>- 定义主键字段和外键字段的对应关系
>
>#### 属性
>
>- name：指定外键字段的名称
>- referencedColumnName：指定引用主表的主键字段名称
>- unique：是否唯一。默认值不唯一
>- nullable：是否允许为空。默认值允许
>- insertable：是否允许插入。默认值允许
>- updatable：是否允许更新。默认值允许
>- columnDefinition：列的定义信息
>
>#### 示例代码
>
>- ![Logo](static/i01.png)
>- ![Logo](static/i02.png)
>
>## 多对多关系注解
>
>### @ManyToMany
>
>- 用于映射多对多关系
>
>#### 属性
>
>- cascade：配置级联操作。
>- fetch：配置是否采用延迟加载
>- targetEntity：配置目标的实体类。映射多对多的时候不用写
>
>### @JoinTable
>
>- 针对中间表的配置
>
>#### 属性
>
>- name：配置中间表的名称
>- joinColumns：中间表的外键字段关联当前实体类所对应表的主键字段
>- inverseJoinColumns：中间表的外键字段关联对方表的主键字段
>
>### @JoinColumn
>
>- 用于定义主键字段和外键字段的对应关系
>
>#### 属性
>
>- name：指定外键字段的名称
>- referencedColumnName：指定引用主表的主键字段名称
>- unique：是否唯一。默认值不唯一
>- nullable：是否允许为空。默认值允许。
>- insertable：是否允许插入。默认值允许。
>- updatable：是否允许更新。默认值允许。
>- columnDefinition：列的定义信息。
>
>#### 示例代码
>
>- ![Logo](static/i03.png)
>- ![Logo](static/i04.png)





## 0.Hibernate与Jpa的关系

```
我知道Jpa是一种规范，而Hibernate是它的一种实现。除了Hibernate，还有EclipseLink(曾经的toplink)，OpenJPA等可供选择，所以使用Jpa的一个好处是，可以更换实现而不必改动太多代码。
在play中定义Model时，使用的是jpa的annotations，比如javax.persistence.Entity, Table, Column, OneToMany等等。但它们提供的功能基础，有时候想定义的更细一些，难免会用到Hibernate本身的annotation。我当时想，jpa这 么弱还要用它干什么，为什么不直接使用hibernate的？反正我又不会换成别的实现。
因为我很快决定不再使用hibernate，这个问题就一直放下了。直到我现在在新公司，做项目要用到Hibernate。
我想抛开jpa，直接使用hibernate的注解来定义Model，很快发现了几个问题：
jpa中有Entity, Table，hibernate中也有，但是内容不同
jpa中有Column,OneToMany等，Hibernate中没有，也没有替代品
我原以为hibernate对jpa的支持，是另提供了一套专用于jpa的注解，但现在看起来似乎不是。一些重要的注解如Column, OneToMany等，hibernate没有提供，这说明jpa的注解已经是hibernate的核心，hibernate只提供了一些补充，而不是两 套注解。要是这样，hibernate对jpa的支持还真够足量，我们要使用hibernate注解就必定要使用jpa。
实际情况是不是这样？在被群里(Scala交流群132569382)的朋友鄙视一番却没有给出满意答案的时候，我又想起了万能的stackoverflow，上去提了两个问：
http://stackoverflow.com/questions/8306742/if-i-want-to-use-hibernate-with-annotation-do-i-have-to-use-javax-persistence
http://stackoverflow.com/questions/8306793/why-jpa-and-hibernate-both-have-entity-and-table-annotations
第一个是问如果想用hibernate注解，是不是一定会用到jpa的。网友的回答：“是。如果hibernate认为jpa的注解够用，就直接用。否则会弄一个自己的出来作为补充”
第二个是问，jpa和hibernate都提供了Entity，我们应该用哪个，还是说可以两个一起用？网友回答说“Hibernate的Entity是继承了jpa的，所以如果觉得jpa的不够用，直接使用hibernate的即可”
```



## 1.简介

### 1.Spring Data是什么

Spring Data是一个用于简化数据库访问，并支持云服务的开源框架。其主要目标是使得对数据的访问变得方便快捷，并支持map-reduce框架和云计算数据服务。 Spring Data 包含多个子项目：

**Commons - 提供共享的基础框架，适合各个子项目使用，支持跨数据库持久化**

**JPA - 简化创建 JPA 数据访问层和跨存储的持久层功能**

Hadoop - 基于 Spring 的 Hadoop 作业配置和一个 POJO 编程模型的 MapReduce 作业

Key-Value  - 集成了 Redis 和 Riak ，提供多个常用场景下的简单封装

Document - 集成文档数据库：CouchDB 和 MongoDB 并提供基本的配置映射和资料库支持

Graph - 集成 Neo4j 提供强大的基于 POJO 的编程模型

Graph Roo AddOn - Roo support for Neo4j

JDBC Extensions - 支持 Oracle RAD、高级队列和高级数据类型

Mapping - 基于 Grails 的提供对象映射框架，支持不同的数据库

Examples - 示例程序、文档和图数据库

Guidance - 高级文档

Spring Data JPA是什么

由Spring提供的一个用于简化JPA开发的框架

nSpring Data JPA能干什么

可以极大的简化JPA的写法，可以在几乎不用写实现的情况下，实现对数据的访问和操作。除了CRUD外，还包括如分页、排序等一些常用的功能。

### 2.Spring Data JPA有什么

Spring Data JPA提供的接口，也是Spring Data JPA的核心概念,见核心接口

## 2.核心接口



主要来看看Spring Data JPA提供的接口，也是Spring Data JPA的核心概念：

1：Repository：最顶层的接口，是一个空的接口，目的是为了统一所有Repository的类型，且能让组件扫描的时候自动识别。

2：CrudRepository ：是Repository的子接口，提供CRUD的功能

3：PagingAndSortingRepository：是CrudRepository的子接口，添加分页和排序的功能

4：JpaRepository：是PagingAndSortingRepository的子接口，增加了一些实用的功能，比如：批量操作等。

5：JpaSpecificationExecutor：用来做负责查询的接口

6：Specification：是Spring Data JPA提供的一个查询规范，要做复杂的查询，只需围绕这个规范来设置查询条件即可

## 3.快速入门

### 1.导入坐标

```XML
<springdatajpa.version>1.10.4.RELEASE</springdatajpa.version>


<!-- spring data jpa 数据库持久层 -->
		<dependency>
			<groupId>org.springframework.data</groupId>
			<artifactId>spring-data-jpa</artifactId>
			<version>${springdatajpa.version}</version>
		</dependency>
```

### 2.在applicationContext.xml文件中导入约束的头

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:jdbc="http://www.springframework.org/schema/jdbc" xmlns:tx="http://www.springframework.org/schema/tx"
	xmlns:jpa="http://www.springframework.org/schema/data/jpa" xmlns:task="http://www.springframework.org/schema/task"
	xsi:schemaLocation="
						http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
						http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
						http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
						http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc.xsd
						http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
						http://www.springframework.org/schema/data/jpa 
						http://www.springframework.org/schema/data/jpa/spring-jpa.xsd">
```



### 3.在applicationContext.xml中注册对象

- 见下小节

### 4.在applicationContext.xml指定框架要扫描的包

```XML
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:jdbc="http://www.springframework.org/schema/jdbc" xmlns:tx="http://www.springframework.org/schema/tx"
	xmlns:jpa="http://www.springframework.org/schema/data/jpa" xmlns:task="http://www.springframework.org/schema/task"
	xsi:schemaLocation="
						http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
						http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
						http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
						http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc.xsd
						http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
						http://www.springframework.org/schema/data/jpa 
						http://www.springframework.org/schema/data/jpa/spring-jpa.xsd">

	<!--
		#########################################################
		指定连接池配置
		#########################################################
	-->
	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		<property name="driverClass" value="oracle.jdbc.driver.OracleDriver" />
		<property name="jdbcUrl" value="jdbc:oracle:thin:@192.168.133.9:1521:ORCL" />
		<property name="user" value="heima20" />
		<property name="password" value="heima20" />
	</bean>
	<!-- spring整合JPA -->
	<bean id="entityManagerFactory"
		class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
		<property name="dataSource" ref="dataSource" />
		<!--
			#########################################################
			指定JPA扫描的实体类所在的包
			#########################################################
		-->
		<property name="packagesToScan" value="com.itheima.bos.domain" />
		<!-- 指定持久层提供者为Hibernate -->
		<property name="persistenceProvider">
			<bean class="org.hibernate.ejb.HibernatePersistence" />
		</property>
		<property name="jpaVendorAdapter">
			<bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter">
				<!-- 自动建表 -->
				<property name="generateDdl" value="true" />
				<property name="database" value="ORACLE" />
				<property name="databasePlatform" value="org.hibernate.dialect.Oracle10gDialect" />
				<property name="showSql" value="true" />
			</bean>
		</property>
		<property name="jpaDialect">
			<bean class="org.springframework.orm.jpa.vendor.HibernateJpaDialect" />
		</property>
	</bean>

	<!-- 配置事务管理器 -->
	<bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
		<property name="entityManagerFactory" ref="entityManagerFactory" />
	</bean>

	<!--
		#########################################################
		开启IOC注解.
		指定Spring进行扫描的包，如果该包中的类使用了@Component @Controller@Service等注解，会把这些类注册为bean
		！！！！不要扫描DAO ！！！！
		DAO包下的类要使用Spring Data JPA框架进行扫描
		#########################################################
	-->
	<context:component-scan base-package="com.itheima.bos.service,com.itheima.bos.web" />

	<!-- 开启事务注解 -->
	<tx:annotation-driven transaction-manager="transactionManager" />
	<!--
		#########################################################
		指定Spring Data JPA要进行扫描的包,该包中的类框架会自动为其创建代理
		#########################################################
	-->
	<jpa:repositories base-package="com.itheima.bos.dao" />


</beans>
```

### 5.创建接口,即可实现基本的增删改查操作

#### 5.1基本增删查改  5.2自定义增删查改

- 接口

```java
package com.itheima.bos.dao.test;

import java.util.List;

import org.junit.Test;
import com.itheima.bos.dao.base.StandardRepository;
import com.itheima.bos.domain.base.Standard;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class RepositoryTest {

	@Autowired
	private StandardRepository standardRepository;
	
	/**
	 * 保存
	 */
	@Test
	public void testSave() {
		Standard standard= new Standard();
		standard.setMaxLength(400);
		standardRepository.save(standard);
	}
	
	/**
	 *更新
	 */
	@Test
	public void testUpdate() {
		Standard standard= new Standard();
		standard.setId(5L);
		standard.setMaxLength(400);
		standard.setName("yyy");
		standardRepository.save(standard);
	}
	
	/**
	 *删除
	 */
	@Test
	public void testDelete() {
		standardRepository.delete(1L);
	}
	
	
	/**
	 *查询所有
	 */
	@Test
	public void testFindAll() {
		List<Standard> list = standardRepository.findAll();
		for (Standard standard : list) {
			System.out.println(standard);
		}
	}
	
	////////////////////自定义查询/ 接口中自定义自己的方法//////////////////////////////////////////////
	
	/**
	 *根据姓名查询  遵循命名规范
	 */
	@Test
	public void testFindByName() {
		List<Standard> findByName = standardRepository.findByName("yyy");
		for (Standard standard : findByName) {
			System.out.println(standard);
		}
	}
	
	/**
	 *根据姓名查询  不遵循命名规范
	 * @Query("from Standard where name = ?")
	 * Standard findByNamexxxx(String name);
	 */
	@Test
	public void findByNamexxxx() {
		Standard standard = standardRepository.findByNamexxxx("yyy");
		System.out.println(standard);
	}
	
	/**
	 *多条件查询  遵循命名规范
	 */
	@Test
	public void findByNameAndMaxLength() {
		Standard standard = standardRepository.findByNameAndMaxLength("yyy", 400);
		System.out.println(standard);
	}
	

    /**
	 *多条件查询  不遵循命名规范
	 *	 // 自定义多条件查询
	 *  @Query("from Standard where name = ?2 and maxLength = ?1")
	 *	Standard findByNameAndMaxLengthxxx(Integer maxLength, String name);
	 */
	@Test
	public void findByNameAndMaxLengthxxx() {
		Standard standard = standardRepository.findByNameAndMaxLengthxxx(400, "yyy");
		System.out.println(standard);
	}
	
	
	
    /**
   	 *使用sql语句
   	 *	// 使用标准SQL查询
   	 *	@Query(value = "select * from T_STANDARD where C_NAME = ? and C_MAX_LENGTH = ?",
   	 *         nativeQuery = true)
   	 * Standard findByNameAndMaxLengthxx(String name, Integer maxLength);
   	 */
   	@Test
   	public void findByNameAndMaxLengthxx() {
   		Standard standard = standardRepository.findByNameAndMaxLengthxx("yyy", 400);
   		System.out.println(standard);
   	}
	
    /**
   	 *使用模糊查询
   	 */
   	@Test
   	public void findByNameLike() {
   		Standard standard = standardRepository.findByNameLike("%y%");
   		System.out.println(standard);
   	}
	
    /**
   	 *自定义更新操作  增删改
   	 */
   	@Test
   	public void findDelete() {
   		standardRepository.delete(2L);
   	}
   	/**
   	 *自定义更新操作  增删改
   	 */
   	@Test
   	public void findUpdate() {
   		standardRepository.updateByName(9999,"yyy");
   	}
}

```

- 测试

- ```java
  package com.itheima.bos.dao.base;

  import java.util.List;

  import org.springframework.data.jpa.repository.JpaRepository;
  import org.springframework.data.jpa.repository.Modifying;
  import org.springframework.data.jpa.repository.Query;
  import org.springframework.transaction.annotation.Transactional;

  import com.itheima.bos.domain.base.Standard;


  public interface StandardRepository extends JpaRepository<Standard, Long> {
  		
  	List<Standard> findByName(String name);
  	// 遵循命名规范,执行多条件查询
      Standard findByNameAndMaxLength(String name, Integer maxLength);
      //模糊查询
      Standard findByNameLike(String name);
  	
  	 // 自定义查询,没有遵循命名规范
      @Query("from Standard where name = ?")
      Standard findByNamexxxx(String name);
      // 自定义多条件查询
      @Query("from Standard where name = ?2 and maxLength = ?1")
      Standard findByNameAndMaxLengthxxx(Integer maxLength, String name);

      
      // 使用标准SQL查询
      @Query(value = "select * from T_STANDARD where C_NAME = ? and C_MAX_LENGTH = ?",
              nativeQuery = true)
      Standard findByNameAndMaxLengthxx(String name, Integer maxLength);

      @Modifying // 代表本操作是更新操作
      @Transactional // 事务注解
      @Query("delete from Standard where name = ?")
      void deleteByName(String name);
      
      @Modifying // 代表本操作是更新操作
      @Transactional // 事务注解
      @Query("update Standard set maxLength = ? where name = ?")
      void updateByName(Integer maxLength, String name);

  }

  ```

- ​

#### 









