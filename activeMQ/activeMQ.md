# activeMQ

## 1.什么是JMS

- 定义：
  - JMS即Java消息服务（Java Message Service）应用程序接口是一个Java平台中关于面向消息中间件（MOM）的API，用于在两个应用程序之间，或分布式系统中发送消息，进行异步通信。
  - **Java消息服务是一个与具体平台无关的API，绝大多数MOM提供商都对JMS提供支持。**
- 简介：
  - JMS是一种与厂商无关的 API，用来访问消息收发服务（消息中间件）。
  - **它类似于JDBC(Java DatabaseConnectivity)：这里，JDBC 是可以用来访问许多不同关系数据库的API，而JMS同样提供与厂商无关的访问方式来访问消息收发服务**。
- JMS 使您能够通过消息收发服务从一个 JMS 客户机向另一个JMS客户机发送消息。
- 消息是 JMS 中的一种类型对象，由两部分组成：报头和消息主体。
  - 报头由路由信息以及有关该消息的元数据组成。
  - 消息主体则携带着应用程序的数据或有效负载。
- 根据有效负载的类型来划分，可以将消息分为几种类型，它们分别携带：
  - 简单文本(TextMessage)、
  - 可序列化的对象 (ObjectMessage)、
  - 属性集合 (MapMessage)、
  - 字节流 (BytesMessage)、
  - 原始值流 (StreamMessage)，
  - 还有无有效负载的消息 (Message)


- ​

## 2.什么是MQ

- 定义：
  - MQ全称为消息队列（Message Queue）, MQ是一种应用程序与应用程序之间的通信方法。
  - 应用程序通过消息队列来通信，而无需专用连接来链接它们。
  - 消息传递指的是程序通过在消息队列中收发数据进行通信，而不是通过直接调用彼此来通信。
  - 队列的使用除去了接收和发送应用程序同时执行的要求。
  - 其中较为成熟的MQ产品有IBM的WEBSPHERE MQ。
- MQ特点：
  - MQ的**消费-生产者模型**的一个典型的代表，一端往消息队列中不断的写入消息，而另一端则可以读取或者订阅队列中的消息。
- 使用场景：在项目中，将一些无需即时返回且耗时的操作提取出来，进行了异步处理，而这种异步处理的方式大大的节省了服务器的请求响应时间，从而提高了系统的吞吐量。



## 3.JMS、MQ及ActiveMQ的关系

- ***JMS是一个用于提供消息服务的技术规范***，它制定了在整个消息服务提供过程中的所有数据结构和交互流程

- MQ则是消息队列服务，是面向消息中间件（MOM）的***最终实现的统称***，是真正的消息服务提供者。

  - MQ的实现可以基于JMS，也可以基于其他规范或标准，其中**ActiveMQ就是基于JMS规范实现的消息队列。**

  常见的基于JMS的MQ：

  - jbossmq(jboss 4)
  - jboss messaging (jboss 5)
  - joram-4.3.21 2006-09-22
  - openjms-0.7.7-alpha-3.zipDecember 26,2005
  - mantamq
  - ubermq
  - SomnifugiJMS 2005-7-27
  - ActiveMQ，
  - RabbitMQ，
  - Kafka（用于大数据的框架）
  - MetaMQ





## 4.activeMQ

### 1.简介

- ActiveMQ是Apache出品，最流行的，能力强劲的开源消息队列。
- ActiveMQ是一个**完全支持JMS 1.1和J2EE 1.4规范**的 JMS Provider实现，尽管JMS规范出台已经是很久的事情了,但是JMS在当今的J2EE应用中间仍然扮演着特殊的地位。



### 2.主要作用

#### 特点

- 多种语言和协议编写客户端：Java, C, C++, C#, Ruby, Perl, Python, PHP；应用协议: OpenWire,Stomp REST,WSNotification,XMPP,AMQP。
- 完全支持JMS 1.1和J2EE 1.4规范 (持久化，XA消息，事务)。
- 对Spring的支持，ActiveMQ可以很容易内嵌到使用Spring的系统里面去，而且也支持Spring 2.0的特性。
- 通过了常见J2EE服务器(如 Geronimo,JBoss 4, GlassFish,WebLogic)的测试,其中通过JCA 1.5 resource adaptors的配置，可以让ActiveMQ可以自动的部署到任何兼容J2EE 1.4 商业服务器上。
- 支持多种传送协议:in-VM,TCP,SSL,NIO,UDP,JGroups,JXTA。
- 支持通过JDBC和journal提供高速的消息持久化。
- 从设计上保证了高性能的集群，客户端-服务器，点对点。
- 支持Ajax。
- 支持与Axis的整合。
- 可以很容易得调用内嵌JMS provider，进行测试。
- ActiveMQ速度非常快；一般要比jbossMQ快10倍

#### 优点

- 是一个快速的开源消息组件(框架)
- 支持集群，同等网络，自动检测，TCP，SSL，广播，持久化，XA，和J2EE1.4容器无缝结合，并且支持轻量级容器和大多数跨语言客户端上的Java虚拟机
- 消息异步接受，减少软件多系统集成的耦合度。
- 消息可靠接收，确保消息在中间件可靠保存，多个消息也可以组成原子事务。



#### 缺点

- ActiveMQ默认的配置性能偏低，需要优化配置，但是配置文件复杂，ActiveMQ本身不提供管理工具；
- 示例代码少；主页上的文档看上去比较全面，但是缺乏一种有效的组织方式，文档只有片段，用户很难由浅入深进行了解，文档整体的专业性太强。
- 在研究阶段可以通过查maillist、看Javadoc、分析源代码来了解。



#### 应用场景

- 不同语言应用集成

  >- ActiveMQ 中间件用Java语言编写，因此自然提供Java客户端 API。但是ActiveMQ也为C/C++、.NET、Perl、PHP、Python、Ruby和一些其它语言提供客户端。在你考虑如何集成不同平台不同语言编写应用的时候，ActiveMQ拥有巨大优势。在这样的例子中，多种客户端API通过ActiveMQ 发送和接受消息成为可能，无论使用的是什么语言。
  >- 此外，ActiveMQ还提供交叉语言功能，该功能整合这种功能，无需使用远程过程调用（RPC）确实是个优势，因为消息协助应用解耦。

- 作为RPC的替代。

  > 使用RPC同步调用的应用十分普遍。假设大多数客户端服务器应用使用RPC，包括ATM、大多数WEB应用、信用卡系统、销售点系统等等。尽管很多系统很成功，但是转换使用异步消息可以带来很多好处，而且也不会放弃响应保证。使用同步请求的系统在规模上有较大的限制，因为请求会被阻塞，从而导致整个系统变慢。如果使用异步消息替代，可以很容易增加额外的消息接收者，使得消息能被并发消耗，从而加快请求处理。当然，你的系统应用间应该是解耦的。

- 应用之间解耦。

  > 正如之前讨论的，紧耦合架构可以导致很多问题，尤其是如果他们是分布的。松耦合架构，在另一方面，证实了更少的依赖性，能够更好地处理不可预见的改变。不仅可以在系统中改变组件而不影响整个系统，而且组件交互也相当的简单。相比使用同步的系统（调用者必须等待被调用者返回信息），异步系统（调用方发送消息后就不管，即fire-and-forget）能够给我们带来事件驱动架构（event-driven architecture EDA）。

- 作为事件驱动架构的主干。

  > 解耦，异步架构的系统允许通过代理器自己配置更多的客户端，内存等（即vertical scalability）来扩大系统，而不是增加更多的代理器（即horizontal scalability）。考虑如亚马逊这样繁忙的电子商务系统。当用户购买物品，事实上系统需要很多步骤去处理，包括下单，创建发票，付款，执行订单，运输等。但是用户下单后，会立即返回“谢谢你下单”的界面。不只是没有延迟，而且用户还会受到一封邮件表明订单已经收到。在亚马逊下单的例子就是一个多步处理的例子。每一步都由单独的服务去处理。当用户下单是，有一个同步的体积表单动作，但整个处理流程并不通过浏览器同步处理。相反地，订单马上被接受和反馈。而剩下的步骤就通过异步处理。如果在处理过程中出错，用户会通过邮件收到通知。这样的异步处理能提供高负载和高可用性。

- 提高系统扩展性。

  > 很多使用事件驱动设计的系统是为了获得高可扩展性，例如电子商务，政府，制造业，线上游戏等。通过异步消息分开商业处理步骤给各个应用，能够带来很多可能性。考虑设计一个应用来完成一项特殊的任务。这就是面向服务的架构（service-oriented architecture SOA）。每一个服务完成一个功能并且只有一个功能。应用就通过服务组合起来，服务间使用异步消息和最终一致性。这样的设计便可以引入一个复杂事件处理概念（complex event processing CEP）。使用CEP，部件间的交互可以被记录追踪。在异步消息系统中，可以很容易在部件间增加一层处理。



### 3.入门案例

#### 安装步骤:

- 1.下载对应版本压缩包，解压到安装目录

- 2.启动${mq}\bin\win64\activemq.bat,
  - 如果是64位操作系统,请选择win64目录下的bat文件

- 3.访问：http://localhost:8161/

  ![界面一](.\图片\i01.png)

  - 如上图点击用户名及密码默认 admin

  ![界面二](.\图片\i02.png)

  - 出现界面即安装成功

    ![界面二](.\图片\Image 3.png)


- 页面简介

  ​		![界面二](.\图片\Image 4.png)

  ​	

- ​

#### HelloWord:

##### 消息消费者

```java
package cn.itcast.activemq.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 消息消费者
 * @author 张理
 *
 */
public class JMSConsumer {
	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;// 默认连接用户名
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;// 默认连接密码
	private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;// 默认连接地址

	public static void main(String[] args) {
		ConnectionFactory connectionFactory;// 连接工厂
		Connection connection = null;// 连接

		Session session;// 会话 接受或者发送消息的线程
		Destination destination;// 消息的目的地

		MessageConsumer messageConsumer;// 消息的消费者

		// 实例化连接工厂
		connectionFactory = new ActiveMQConnectionFactory(JMSConsumer.USERNAME, JMSConsumer.PASSWORD,
				JMSConsumer.BROKEURL);

		try {
			// 通过连接工厂获取连接
			connection = connectionFactory.createConnection();
			// 启动连接
			connection.start();
			// 创建session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// 创建一个连接HelloWorld的消息队列
			destination = session.createQueue("HelloWorld");
			// 创建消息消费者
			messageConsumer = session.createConsumer(destination);

			while (true) {
				TextMessage textMessage = (TextMessage) messageConsumer.receive(100000);
				if (textMessage != null) {
					System.out.println("收到的消息:" + textMessage.getText());
				} else {
					break;
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
```

##### 消息生产者

```java
package cn.itcast.activemq.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 消息生产者
 * @author 张理
 *
 */
public class JMSProducer {
	// 默认连接用户名
	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
	// 默认连接密码
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
	// 默认连接地址
	private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;
	// 发送的消息数量
	private static final int SENDNUM = 10;

	public static void main(String[] args) {
		// 连接工厂
		ConnectionFactory connectionFactory;
		// 连接
		Connection connection = null;
		// 会话 接受或者发送消息的线程
		Session session;
		// 消息的目的地
		Destination destination;
		// 消息生产者
		MessageProducer messageProducer;
		// 实例化连接工厂
		connectionFactory = new ActiveMQConnectionFactory(JMSProducer.USERNAME, JMSProducer.PASSWORD,
				JMSProducer.BROKEURL);

		try {
			// 通过连接工厂获取连接
			connection = connectionFactory.createConnection();
			// 启动连接
			connection.start();
			// 创建session
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			// 创建一个名称为HelloWorld的消息队列
			destination = session.createQueue("HelloWorld");
			// 创建消息生产者
			messageProducer = session.createProducer(destination);
			// 发送消息
			sendMessage(session, messageProducer);

			session.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 发送消息
	 * 
	 * @param session
	 * @param messageProducer
	 *            消息生产者
	 * @throws Exception
	 */
	public static void sendMessage(Session session, MessageProducer messageProducer) throws Exception {
		for (int i = 0; i < JMSProducer.SENDNUM; i++) {
			// 创建一条文本消息
			TextMessage message = session.createTextMessage("ActiveMQ 发送消息" + i);
			System.out.println("发送消息：Activemq 发送消息" + i);
			// 通过消息生产者发出消息
			messageProducer.send(message);
		}

	}
}

```

##### 上述代码的优化

- 因为上叙代码中，消费者中有死循环，处于  一直开启状态，很浪费资源，故对消费者进行优化



```java
package cn.itcast.activemq.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class JMSConsumerListenerTest {
	@Test
	public void testConsumer() throws JMSException {
		ConnectionFactory connectionFactory;// 连接工厂
		Connection connection = null;// 连接

		Session session;// 会话 接受或者发送消息的线程
		Destination destination;// 消息的目的地

		MessageConsumer messageConsumer;// 消息的消费者

		// 实例化连接工厂
		connectionFactory = new ActiveMQConnectionFactory(BROKEURL);

		// 通过连接工厂获取连接
		connection = connectionFactory.createConnection();
		// 启动连接
		connection.start();
		// 创建session
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 创建一个连接HelloWorld的消息队列
		destination = session.createQueue("HelloWorld");
		// 创建消息消费者
		messageConsumer = session.createConsumer(destination);

		messageConsumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				try {
					System.out.println(((TextMessage) message).getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		//这个死循环是因为测试，在实际开发中不需要的
		while (true) {
		}
	}
	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;// 默认连接用户名
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;// 默认连接密码
	private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;// 默认连接地址

}

```



#### 补充:

- ActiveMQ默认采用61616端口提供JMS服务，使用8161端口提供管理控制台服务
- ​

### 4.整合spring

#### 整合注意事项

- 使用Maven,创建war工程

- 指定ActiveMQ 连接工厂有两种方式

  ```xml
  	<!--
  		================= 指定ActiveMQ 连接工厂 ======
  		真正可以产生Connection的ConnectionFactory，由对应的 JMS服务厂商提供
  	-->

  	<!-- 方式二： 上面和下面两种方式是同样的效果  -->

  	<bean id="amqConnectionFactory" class="org.apache.activemq.ActiveMQConnectionFactory">
  		<constructor-arg index="0" value="admin"></constructor-arg>
  		<constructor-arg index="1" value="admin"></constructor-arg>
  		<constructor-arg index="2" value="tcp://localhost:61616"></constructor-arg>
  	</bean>

  	<!-- 方式一： 上面和下面两种方式是同样的效果  -->
  	<amq:connectionFactory id="xx" userName="admin"
  		password="admin" brokerURL="tcp://localhost:61616"></amq:connectionFactory> 
  ```


  ```

  ​


- 配置约束的导入

  ```xml
  xmlns:amq="http://activemq.apache.org/schema/core" xmlns:jms="http://www.springframework.org/schema/jms"

  <--============================================-->
    
  http://www.springframework.org/schema/jms
  http://www.springframework.org/schema/jms/spring-jms.xsd
  http://activemq.apache.org/schema/core
  http://activemq.apache.org/schema/core/activemq-core-5.8.0.xsd 
  ```

  ​


- spring依赖的注解方式---->扫描的包的路径

  ```xml
  <!-- 指定Spring框架扫描的包 -->
  	<context:component-scan base-package="com.itheima" />
  ```

  ​

- 生产者的开发注意:

  - 传送  消息队列 的名字及传送的消息

- 消费者开发注意：

  - 开发者需要实现import javax.jms.MessageListener接口，并实现其onMessage（）方法

- 测试中 填写的  队列名  与 applicationContext.xml中指定消费者中的名字要一致

- - 配置 指定消费者  

  ```xml
  <jms:listener destination="helloWord" ref="consumer" />
  ```

  - 发送消息

  ```java
  producer.sendMessage("helloWord", "你好，世界");
  ```

- ​

#### 开发模板

##### 依赖导入

```xml
<dependencies>
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>4.12</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-core</artifactId>
			<version>4.1.7.RELEASE</version>
		</dependency>
		<!-- spring整合jms -->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-jms</artifactId>
			<version>4.1.7.RELEASE</version>
		</dependency>
		<!-- ActiveMQ的坐标 -->
		<dependency>
			<groupId>org.apache.activemq</groupId>
			<artifactId>activemq-all</artifactId>
			<version>5.2.0</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-test</artifactId>
			<version>4.1.7.RELEASE</version>
		</dependency>
		<!-- 使用ActiveMQ的时候,也要导入这个坐标 -->
		<dependency>
			<groupId>org.apache.xbean</groupId>
			<artifactId>xbean-spring</artifactId>
			<version>4.2</version>
		</dependency>
	</dependencies>
```



##### applicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:jdbc="http://www.springframework.org/schema/jdbc" xmlns:tx="http://www.springframework.org/schema/tx"
	xmlns:jpa="http://www.springframework.org/schema/data/jpa" xmlns:task="http://www.springframework.org/schema/task"
	xmlns:amq="http://activemq.apache.org/schema/core" xmlns:jms="http://www.springframework.org/schema/jms"
	xsi:schemaLocation="
       http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.1.xsd
       http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.1.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.1.xsd
       http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc-4.1.xsd
       http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.1.xsd
       http://www.springframework.org/schema/data/jpa 
       http://www.springframework.org/schema/data/jpa/spring-jpa.xsd
       http://www.springframework.org/schema/jms
        http://www.springframework.org/schema/jms/spring-jms.xsd
       http://activemq.apache.org/schema/core
        http://activemq.apache.org/schema/core/activemq-core-5.8.0.xsd ">
	<!-- 指定Spring框架扫描的包 -->
	<context:component-scan base-package="com.itheima" />
	<!--
		======指定ActiveMQ 连接工厂 ===============
		真正可以产生Connection的ConnectionFactory，由对应的 JMS服务厂商提供
	-->
	<bean id="amqConnectionFactory" class="org.apache.activemq.ActiveMQConnectionFactory">
		<constructor-arg index="0" value="admin"></constructor-arg>
		<constructor-arg index="1" value="admin"></constructor-arg>
		<constructor-arg index="2" value="tcp://localhost:61616"></constructor-arg>
	</bean>
	<!-- 上面和下面两种方式是同样的效果 -->
	<!-- <amq:connectionFactory id="xx" userName="admin"
		password="admin" brokerURL="tcp://localhost:61616"></amq:connectionFactory>
 -->
	<!--
		========= 指定Spring Caching连接工厂 ========
	-->
	<bean id="connectionFactory"
		class="org.springframework.jms.connection.CachingConnectionFactory">
		<!-- 目标ConnectionFactory对应真实的可以产生JMS Connection的ConnectionFactory -->
		<property name="targetConnectionFactory" ref="amqConnectionFactory"></property>
		<!-- Session缓存数量 -->
		<property name="sessionCacheSize" value="100" />
	</bean>
	<!--
		========== 指定消息的生产者 =========
	-->
	<bean id="jmsQueueTemplate" class="org.springframework.jms.core.JmsTemplate">
		<!-- 这个connectionFactory对应的是我们定义的Spring提供的那个ConnectionFactory对象 -->
		<constructor-arg ref="connectionFactory" />
		<!-- 定义JmsTemplate的Queue类型 -->
		<!--true : 发布/订阅模式 -->
		<!--false : 队列模式 -->
		<property name="pubSubDomain" value="false" />
	</bean>
	<!--
		=============== 指定消息的消费者 ==========
		destination-type : 消息的类型,queue:队列模式,topic : 发布订阅模式
		acknowledge: 应答机制
	-->

	<jms:listener-container destination-type="queue"
		container-type="default" connection-factory="connectionFactory"
		acknowledge="auto">

		<!-- destination : 消息队列的名字 -->
		<!-- ref : 消息的消费者 -->
		<jms:listener destination="helloWord" ref="consumer" />
	</jms:listener-container>
</beans>

```



##### 生产者

```java
package com.itheima;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class Producer {
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	/**
	 * @param destination  传送到那个队列的 名字
	 * @param msg  要传送的消息
	 */
	@Test
	public void sendMessage(String destination, final String msg) {
		jmsTemplate.send(destination, new MessageCreator() {
			
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(msg);
				return textMessage;
			}
		});
	}
}
```



##### 消费者

```java
package com.itheima;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;

@Component
public class Consumer implements MessageListener  {

	public void onMessage(Message msg) {
		/*
		 * 依据  生产者那边 生产的消息对象类型 将msg 强转
			TextMessage textMessage = session.createTextMessage(msg);
			return textMessage;
		*/
		TextMessage textMessage = (TextMessage) msg;
		//获取对象中的  数据
		try {
			String text = textMessage.getText();
			
			System.out.println("接受到的 消息为---->"+text);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
```



##### 测试

```java
package com.itheima;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试类，发送消息
 * @author 张理
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TestActiveMQ {
	
	@Autowired
	private Producer producer;
	
	@Test
	public void testSendMsg() {
		producer.sendMessage("helloWord", "你好，世界");
	}
}
```

