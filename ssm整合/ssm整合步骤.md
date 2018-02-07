# ssm整合步骤

## 1.开发环境

- eclipse:mars2
- Jdk:1.7.0_80
- 数据库：mysql
- 框架：mybatis（3.2.7）、spring4.2.4、springmvc（4.2.4）

## 2.数据库

- sql脚本

## 3.工程搭建

### 1.整合思路

- jar

  >- 数据库：数据库驱动，连接池，
  >- dao：mybatis核心包，整合spring的整合包
  >- service：spring的jar包(5个)
  >- controller：SpringMVC的jar包
  >- jsp：jstl,servlet-api
  >- 公共jar：commons-lang(Stringutils)

  ​

- 配置文件

  >dao：
  >
  >- SqlMapConfig.xml
  >  - 别名的配置
  >  - 加载映射文件（可选）
  >  - ​
  >- applicationContext-dao.xml
  >  - 数据源
  >  - sessionFactory
  >  - mapper的扫描
  >
  >service:
  >
  >- applicationContext-service.xml
  >  - service组件注解扫描
  >  - 事务
  >
  >controller
  >
  >- springmvc.xml
  >  - controller组件注解扫描
  >  - 视图解析器
  >
  >web.xml
  >
  >- spring 容器的初始化(?)
  >- 前端控制器的初始化（DispatchServlet）
  >- 配置URL
  >- POST 乱码处理的过滤器
  >
  >日志配置文件
  >
  >- ​

- java类

>数据库--->dao--->service--->controller--->jsp





