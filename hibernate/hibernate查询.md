# hibernate查询

## 1.Query查询（HQL查询）

### 1.介绍

- Query是一个面向对象的查询方式。
- 核心是HQL语句

### 2.相关API

- >1. session.createQuery(String hql)  
  >
  >   - 创建Query对象
  >
  >2. list（）
  >
  >   - 查询结果是多个
  >
  >3. uniqueResult（）
  >
  >   - 查询结果为一个
  >
  >4. setXXX（int sql语句中的问号下标，XXX value）
  >
  >   - 设置查询参数
  >   - hql语句中的问号下标从0开始
  >   - XXX  value为与set相同的类型
  >
  >5. setXXX（“abcd”,"值"）
  >
  >   - 设置查询参数
  >
  >
  >   - hql语句中  问号用“:abcd”表示
  >
  >6. 分页查询
  >
  >   1. setFirstResult(int firstResult):
  >      - 设置第一个结果的起始位置，从0开始
  >   2. setMaxResults(int maxResults)
  >      - 设置结果的总数
  >   3. setFirstResult()和setMaxResults()搭配使用

  ​

### 3.使用步骤（案例）

#### 1.编写hql语句

- hql语句与sql语句类似
-  **注意  里面的表为对应的对象   列对应类中的字段**

#### 2.session对象通过hql语句创建Query对象

- session.createQuery(String hql)

#### 3.设置查询参数

#### 4.执行query的查询方法获取想要的结果

- 多条数据用list（）
- 单条数据用uniqueResult（）

#### 5.案例

>- 全表查询
>
>- 单条件查询
>
>  ```java
>  //条件查询
>  @Test
>  public void test2() {
>    //获取session
>    Session session = HibernateUtils.getSession();
>    //事务
>    Transaction transaction = session.beginTransaction();
>    //创建hql语句
>    String hql="from Employee where name like ?";
>    //创建query对象
>    Query query = session.createQuery(hql);
>    //设置参数
>    query.setString(0, "%柳%");
>    //执行查询
>    Object uniqueResult = query.uniqueResult();
>    System.out.println(uniqueResult+"-=---");
>
>    transaction.commit();
>    session.close();
>  }
>  ```
>
>  ​
>
>- 多条件查询
>
>  - ```java
>    //条件查询
>    @Test
>    public void test2() {
>      //获取session
>      Session session = HibernateUtils.getSession();
>      //事务
>      Transaction transaction = session.beginTransaction();
>      //创建hql语句
>      String hql="from Employee where id>:id and name like :like";
>      //创建query对象
>      Query query = session.createQuery(hql);
>      //设置参数
>      query.setInteger("id", 29);
>      query.setString("like", "%苏%");
>      //执行查询
>      List<Employee> list = query.list();
>      for (Employee employee : list) {
>        System.out.println(employee);
>      }
>
>      transaction.commit();
>      session.close();
>    }
>    ```
>
>  - ```java
>    //条件查询
>    @Test
>    public void test2() {
>      //获取session
>      Session session = HibernateUtils.getSession();
>      //事务
>      Transaction transaction = session.beginTransaction();
>      //创建hql语句
>      String hql="from Employee where id>? and name like ?";
>      //创建query对象
>      Query query = session.createQuery(hql);
>      //设置参数
>      query.setInteger(0, 29);
>      query.setString(1, "%苏%");
>      //执行查询
>      List<Employee> list = query.list();
>      for (Employee employee : list) {
>        System.out.println(employee);
>      }
>      transaction.commit();
>      session.close();
>    }
>    ```
>
>- 分页查询
>
>  ```java
>  //分页查询
>  @Test
>  public void test1() {
>  //获取session
>  Session session = HibernateUtils.getSession();
>  //事务
>  Transaction transaction = session.beginTransaction();
>  //创建hql语句
>  String hql="from Employee";
>  //创建query对象
>  Query query = session.createQuery(hql);
>  //设置参数
>  query.setFirstResult(0);
>  query.setMaxResults(3);
>  //执行查询
>  List<Employee> list = query.list();
>  for (Employee employee : list) {
>  System.out.println(employee);
>  }
>  transaction.commit();
>  session.close();
>  }
>  ```
>
>  ​
>
>- 查询结果为一个
>
>- 查询结果为多个
>
>- or查询的使用方式
>
>  ```JAVA
>  //or查询
>  @Test
>  public void test4() {
>    //获取session
>    Session session = HibernateUtils.getSession();
>    //事务
>    Transaction transaction = session.beginTransaction();
>    //创建hql语句
>    String hql="from Employee where id>? or name like ?";
>    //创建query对象
>    Query query = session.createQuery(hql);
>    //设置参数
>    query.setInteger(0, 32);
>    query.setString(1, "%苏%");
>    //执行查询
>    List<Employee> list = query.list();
>    for (Employee employee : list) {
>      System.out.println(employee);
>    }
>
>    transaction.commit();
>    session.close();
>  }
>  ```
>
>  ​
>
>- 排序查询
>
>- >a. 使用Order类
>  >
>  >* 通过Order类的静态方法，生成Order对象（排序对象）
>  >
>  >b. Order的API介绍
>  >
>  >1. static Order desc(String propertyName):--Order类的静态方法，返回值是Order的实例对象。按照propertyName（类的属性名）进行降序排列。
>  >2. static Order asc(String propertyName):--同上，升序排列
>  >3. criteria.addOrder(Order order); --Criteria对象方法，增加排序对象。
>  >
>  >
>  >c. 演示代码
>  >* 按照id从大到下排序
>  >* 按照“性别”降序，如果“性别”相同，按照id升序。
>
>  ​
>
>  ```JAVA
>  @Test
>  // 排序查询：先根据性别排序，性别相同，根据eid降序
>  public void test3(){
>    Session session =HibernateUtils.openSession();
>    Transaction transaction = session.beginTransaction();
>    //1. 根据hql语句，创建Query对象
>    // sql: select * from employee order by esex,  eid desc; 
>    String hql="from Employee order by sex, id desc";
>    Query query = session.createQuery(hql);
>    //2. 进行查询
>    List<Employee> list =query.list();
>    for (Employee employee : list) {
>    	System.out.println(employee);
>    }	
>    transaction.commit();
>    session.close();
>  }
>  ```
>
>- 分组统计查询
>
>- ```JAVA
>  @Test
>  // 分组统计查询：根据性别分组，统计每个性别的人数
>  public void test4(){
>    Session session =HibernateUtils.openSession();
>    Transaction transaction = session.beginTransaction();
>    //1. 根据hql语句，创建Query对象
>    // sql: select count(*) from employee group by esex;
>    String hql="select count(*) from Employee group by sex";
>    Query query = session.createQuery(hql);
>    //2. 进行查询
>    List<Object> list =query.list();
>    for (Object x : list) {
>    System.out.println(x);
>    }
>    transaction.commit();
>    session.close();
>  }
>  ```
>
>- ​
>
>



### 4.分析

-  2个现象

  		* Query对象，是通过session对象来创建。而session对象是对connection对象的封装。
  		* 控制台打印出了sql语句

- 如何理解Query查询，是面向对象的查询，其本质是？

  			* query查询的本质：就是通过jdbc的Connection对象，使用sql语句进行查询。只是封装了一次。
  			* 用面向对象的特点封装：
  				* 把表,改成：javabean的名字
  				* 把表中字段改成：javabean对应属性（成员变量）的名字

### 5. 总结

	a. 编写hql语句，先编写sql语句。
		* 把sql语句中使用的表，换成对应的持久化类的名字。
		* 把sql语句的字段名称，换成类中属性的名称。
		
	b.吐槽
		* hql的查询非常的坑：
			* 我们编写的时候，先写sql语句，翻译成：hql
			* hiberntae执行的时候，把hql，翻译成：sql
			
	c. 感受到映射文件的存在
## 2.criteria查询

### 1.介绍

> a. Criteria:完全面向对象的查询。又称为QBC查询，query by criteria.
> b. 不管底层数据库实现和SQL语句的编写。

### 2.相关API

>a. session.createCriteria(Class persistentClass): --创建Criteria对象。persistentClass:需要查询对象的字节码对象名称。
>
> b. list(): --查询结果是多个
>  c. uniqueResult(): --查询结果是一个
>  e. setFirstResult(int firstResult):--分页查询1，设置第一个结果的起始位置，从0开始
>  f. setMaxResults(int maxResults)： --分页查询2，设置结果的总数
>  g. add(Criterion criterion): --添加查询条件对象（支持添加多个，互相是and关系）	

### 3.使用步骤（案例）

### 4.分析

## 3.SQLQuery查询

### 1.介绍

### 2.相关API

### 3.使用步骤（案例）

### 4.分析