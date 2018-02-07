# Quartz

## 1.Quartz是什么

- Quartz是一个开源的作业调度框架,由java编写,在.NET平台为Quartz.Net,通过Quart可以快速完成任务调度的工作
- 类似于 定时器

## 2.Quartz有什么引用场景

- 如网页游戏中挂机自动修炼如8个小时,人物相关数值进行成长,当使用某道具后,时间减少到4个小时,人物对应获得成长值.这其中就涉及到了Scheduler的操作，定时对人物进行更新属性操作，更改定时任务执行时间.
- 网页游戏中会大量涉及到Scheduler的操作,有兴趣的朋友可自行联想.
- 企业中如每天凌晨2点触发数据同步、发送Email等操作



## 3.同类框架对比

- TimeTask TimeTask在Quartz前还是显得过于简单、不完善，不能直接满足开发者的较为复杂的应用场景.



## 4.资源下载

1. 官网:http://www.quartz-scheduler.org/
2. 下载:http://www.quartz-scheduler.org/downloads
3. 依赖

```xml
<dependency>
    <groupId>org.quartz-scheduler</groupId>
    <artifactId>quartz</artifactId>
    <version>2.2.1</version>
</dependency>
<dependency>
    <groupId>org.quartz-scheduler</groupId>
    <artifactId>quartz-jobs</artifactId>
    <version>2.2.1</version>
</dependency>
```







## 5.框架分析

1. 接口
2. 类



## 6.**涉及到的设计模式**

1. 1. Builder模式

      1. 所有关键组件都有Builder模式来构建  <Builder> 如:JobBuilder、TriggerBuilder

   2. Factory模式

      1. 最终由Scheduler的来进行组合各种组件  <Factory> 如SchedulerFactory

   3. Quartz项目中大量使用组件模式，插件式设计，可插拔，耦合性低，易扩展，开发者可自行定义自己的Job、Trigger等组件

   4. 链式写法,Quartz中大量使用链式写法，与jQuery的写法有几分相似，实现也比较简单，如：

      1.  $(this).addClass("divCurrColor").next(".divContent").css("display","block");  

      2. *newTrigger*().withIdentity( "trigger3", "group1").startAt( startTime)

         ​        .withSchedule(*simpleSchedule*().withIntervalInSeconds(10).withRepeatCount(10)).build();



## 7.框架核心分析

1. SchedulerFactory    -- 调度程序工厂
   1. StdSchedulerFactory   -- Quartz默认的SchedulerFactory
   2. DirectSchedulerFactory  --   DirectSchedulerFactory是对SchedulerFactory的直接实现,通过它可以直接构建Scheduler、threadpool 等
      1. ThreadExecutor / DefaultThreadExecutor   -- 内部线程操作对象
2. JobExecutionContext -- JOB上下文,保存着Trigger、 JobDeaitl 等信息,JOB的execute方法传递的参数就是对象的实例
   1. JobExecutionContextImpl
3. Scheduler    -- 调度器
   1. StdScheduler    -- Quartz默认的Scheduler
   2. RemoteScheduler  -- 带有RMI功能的Scheduler
4. JOB --任务对象
   1. JobDetail  -- 他是实现轮询的一个的回调类，可将参数封装成JobDataMap对象,Quartz将任务的作业状态保存在JobDetail中.
   2. JobDataMap --  JobDataMap用来报错由JobDetail传递过来的任务实例对象
5. Trigger
   1. SimpleTrigger <普通的Trigger> --  SimpleScheduleBuilder
   2. CronTrigger  <带Cron Like 表达式的Trigger> -- CronScheduleBuilder
   3. CalendarIntervalTrigger <带日期触发的Trigger> -- CalendarIntervalScheduleBuilder
   4. DailyTimeIntervalTrigger <按天触发的Trigger> -- DailyTimeIntervalScheduleBuilder
6. ThreadPool  --  为Quartz运行任务时提供了一些线程
   1. SimpleThreadPool  --一个Quartz默认实现的简单线程池，它足够健壮，能够应对大部分常用场景
7. -----以上是Quartz涉及到的一些关键对象，详细的内容如有机会会在后续的文章中展开!



## 8.思想流程

####使用流程

>1. 创建调度工厂();    //工厂模式
>2. 根据工厂取得调度器实例();  //工厂模式
>3. Builder模式构建子组件<Job,Trigger>  // builder模式, 如JobBuilder、TriggerBuilder、DateBuilder
>4. 通过调度器组装子组件   调度器.组装<子组件1,子组件2...>  //工厂模式
>5. 调度器.start(); [//工厂模式]()



#### 流程(java代码)

```java
/ 1、工厂模式 构建Scheduler的Factory，其中STD为Quartz默认的Factory  
//    开发者亦可自行实现自己的Factory;Job、Trigger等组件  
SchedulerFactory sf = new StdSchedulerFactory();  
  
// 2、通过SchedulerFactory构建Scheduler对象  
Scheduler sched = sf.getScheduler();  
  
// 3、org.quartz.DateBuilder.evenMinuteDate  -- 通过DateBuilder构建Date  
Date runTime = evenMinuteDate( new Date());  
  
// 4、org.quartz.JobBuilder.newJob <下一分钟> --通过JobBuilder构建Job  
JobDetail job = newJob(HelloJob.class).withIdentity("job1","group1").build();  
  
// 5、通过TriggerBuilder进行构建Trigger  
Trigger trigger = newTrigger().withIdentity("trigger1","group1")  
            .startAt(runTime).build();  
  
// 6、工厂模式，组装各个组件<JOB，Trigger>  
sched.scheduleJob (job, trigger);  
  
// 7、start   
sched.start();  
  
try {  
  Thread.sleep(65L * 1000L);  
} catch (Exception e) {  
}  
  
// 8、通过Scheduler销毁内置的Trigger和Job  
sched.shutdown(true); 
```

- ​

#### 入门案例（整合spring）

##### 任务类

```java
package com.itheima.quartz;

public class SendMessage {

	public void sendMessage() {
		System.out.println("发送邮件");
	}
	
}

```



-

##### **applicationContext.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:jdbc="http://www.springframework.org/schema/jdbc" xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:jpa="http://www.springframework.org/schema/data/jpa" xmlns:task="http://www.springframework.org/schema/task"
       xmlns:jaxws="http://cxf.apache.org/jaxws" xmlns:soap="http://cxf.apache.org/bindings/soap"
       xsi:schemaLocation="
                           http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
                           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
                           http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc.xsd
                           http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
                           http://www.springframework.org/schema/data/jpa 
                           http://www.springframework.org/schema/data/jpa/spring-jpa.xsd
                           http://cxf.apache.org/bindings/soap 
                           http://cxf.apache.org/schemas/configuration/soap.xsd
                           http://cxf.apache.org/jaxws 
                           http://cxf.apache.org/schemas/jaxws.xsd">

  
  <!--注册job -->
  <bean id="sendMessage" class="com.itheima.quartz.SendMessage"></bean>


  <!--通过反射调用job,任务详情, jobdetail -->
  <bean id="jobDetail"
        class="org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean">
    <!--注入任务类 -->
    <property name="targetObject" ref="sendMessage"></property>
    <!-- 指定调用那一个方法 -->
    <property name="targetMethod" value="sendMessage"></property>
  </bean>



  <!-- 注册一个触发器 -->
  <bean id="emailTrigger"
        class="org.springframework.scheduling.quartz.CronTriggerFactoryBean">
    <!-- 注入任务详情 -->
    <property name="jobDetail" ref="jobDetail"></property>
    <!-- 调度规则,每隔5秒执行一次任务 -->
    <property name="cronExpression">
      <value>0/5 * * * * ?</value>
    </property>
  </bean>



  <!-- 注册一个调度工厂 -->
  <bean class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
    <!-- 注入触发器 -->
    <property name="triggers">
      <list>
        <ref bean="emailTrigger" />
      </list>
    </property>
  </bean>
</beans>

```

##### 测试类

```java
package com.itheima.quartz;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestMessage {

	public static void main(String[] args) {
		//加载配置文件
		new ClassPathXmlApplicationContext("applicationContext.xml");
	}
}

```

