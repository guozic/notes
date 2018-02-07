#  hibernate 级联

## 一.一对多 导航查询 （配置基本的关联映射）

### a.定义及作用与原因

- 作用及定义
  - 查询一个对象，其对应的对象也随之查询出来

- 原因
  - hibernate  1对多的配置，为了实现一个功能：可以“自动”的操作部门所在的员工。
    - 查询部门的时候，可以自动的把部门所有的员工查询出来。
    - 为了解决department.getEmployees()，实现的机制：是通过映射文件的<set>  的配置。
      -  **这个映射文件，就是告诉hibernate如何“自动”的操作。**

### b.配置文件（含示例）

- 一  的一方

  ```xml
  <!-- 设置配对关系 
    set标签：javaBean中使用set集合
   	  name: 多的一方(员工)，在1的一方（部门）对应类中属性的名字
   	  -(在一的一方中，用于存放多的一方的集合的名字)-

    key标签:查询的关键。知道了部门信息，查询员工的语句：
        参考：select * from employee where fk_did =?
      column: 多的一方对应的表中，指向1的一方外键的名称

    one-to-many标签: 从1的一方查询多的一方。
    	 class: 查询到的结果，要封装的javabean类型。即多的一方对应类的全路径名称
    -->
  <set name="setEmployee">
  		<key column="fk_did"></key>
  		<one-to-many  class="com.guozicheng.hibernate.bean.Employee"/>
  </set>
  ```

- 多的一方

  ```xml
  <!-- many-to-one标签：从多的一方(部门)查询1的一方（员工）
  		* name: 1的一方（部门），在多的一方(员工)的持久化类中，属性的名字
  		* class: 查询到的结果，要封装的javabean类型。即 1的一方（部门）对应的类的全路径名称
  		* column: 多的一方(员工)，对应的表中，外键所在字段的名称。
   -->
  		<many-to-one name="department"  						class="com.guozicheng.hibernate.bean.Department"  column="fk_did">
  		</many-to-one>
  ```

## 二.一对多  级联  保存/更新/删除  （在查询的基础上添加配置）

### a.定义及作用与原因

- 定义及作用
  - 当主控方执行保存、更新或者删除操作时，其关联对象（被控方）也执行相同的操作。
    - 在部门和员工的关系中，配置了级联操作，保存部门的时候，会自动把该部门的所有员工保存。

### b.配置文件（含示例）

- >```xml
  ><set name="setEmployee" cascade="save-update，delete">
  >  <key column="fk_did"></key>
  >  <one-to-many  class="com.guozicheng.hibernate.bean.Employee"/>
  ></set>
  >```
  >
  >a.  **映射文件中通过对cascade属性的设定来控制是否对关联对象，采用级联操作。**
  >
  >	* 映射文件中，<set>和<many-to-one>标签上配置级联属性：cascade
  >
  >b. 级联的常见取值
  >	none：        默认值。所有情况下均不进行关联操作
  >	save-update： 在执行save/update/saveOrUpdate时进行关联操作。
  >	delete：      在执行delete时进行关联操作
  >	all：         所有情况下均进行关联操作。
  >
  > c.  **保存**
  >
  >- 单向级联保存部门
  >  1.  **仅仅**是只在部门映射配置文件中添加  级联配置
  >  2.  将 员工对象  保存进  部门的集合属性中
  >  3.  执行保存 部门对象操作==》此时员工会被保存
  >- 单向级联保存员工
  >  1.  **仅仅**是只在员工映射配置文件中添加 级联配置
  >  2.  将  部门添加进 员工对应的属性
  >  3.  执行保存 员工对象操作==》此时部门也会被保存
  >- 双向级联保存员工  
  >  1. 在部门 与 员工配置文件中都 添加上 级联配置
  >  2. 将要保存的几个员工对象存入 部门集合属性，给要执行保存操作的员工对象的部门属性赋值
  >  3. 执行保存员工对象操作  ==》此时先保存此员工，在保存部门，然后保存其他没有执行保存操作的员工
  >- 在部门里面添加了员工后，想保存部门，报错
  >  - org.hibernate.TransientObjectException: object references an unsaved transient instance - save the transient instance before flushing: com.guozicheng.hibernate.bean.Employee
  >  - 原因：当  session.save（department）时，
  >    - 此时部门为持久态，而加入部门内的员工还是瞬时态？
  >  - 解决办法(方便简单 )
  >    - 保存部门，需要级联保存其员工。故在Department.hbm.xml的<set>标签上配置：cascade="save-update"
  >
  >d.删除
  >
  >- 配置
  >
  >  * cascade="delete"
  >
  >-  演示单向级联删除
  >  1. 不配置级联删除下，删除部门  
  >
  >  - - 注：在JDBC中  在 部门表作为 员工表中的主键时是不能删除的。
  >
  >  * - 在hibernate中，在 部门表作为 员工表中的主键时 可以删除。删除该部门时，其所有关联的员工的外键全部置空
  >
  >  2. 不配置级联删除下，删除员工
  >     - 在hibernate中与JDBC中一样，只删除员工
  >  3. 配置级联删除，删除部门（单向配置）（仅仅在部门映射文件中配置）
  >
  >  * - 在jdbc中  在 部门表作为 员工表中的主键时是不能删除的。
  >
  >    * 在hibernate中，在 部门表作为 员工表中的主键时 可以删除。删除该部门时，其所有关联的员工全部删除
  >
  >  4. 员工级联删除部门（单向配置）（仅仅在员工映射文件中配置）
  >     - hibernate中，此时删除员工，对应的员工删除了，对应的部门也删除了，其他的员工的外键置空
  >  5. 员工级联删除部门（双向配置）（在员工及部门的映射文件中配置）
  >     - 此时删除任何一个员工，或删除部门时，员工与部门表中的数据全部删除
  >
  >-  总结
  >  * 实际工作中，不会使用级联删除。

### c.级联的方向性

- 级联配置存在方向性

- > a. 保存部门，级联保存员工，配置在部门对应映射文件的<set>标签上
  >
  > b. 保存员工，级联保存部门，配置在员工对应映射文件的<many-to-one>标签上

- 错误示例：

  - 若只在部门上配置了级联保存，但是将 员工保存进部门，然后只保存员工时，也会与惨案一样的报错

    >		// 1. 创建对象：1个部门，1个员工
    >		Department department = new Department();
    >		department.setName("人事部");
    >		
    >		Employee employee=new Employee();
    >		employee.setName("黎明1号");
    >	
    >	     // 2.建立关系
    >		employee.setDepartment(department); // 语句1
    >		
    >		// 3. 保存
    >		session.save(employee);  

- 总结

- > - 关系也是有方向性的。部门到员工的关系，和员工到部门的关系，有不同的意义。
  > - 在部门上配置了级联保存，说明，保存部门的同时，可以自动保存员工。反过来不可以。


## 三.放弃外键维护权(更新操作)

### a.放弃 外键维护权的原因及作用

- 示例

  >- 某个员工更换部门，例如：研发部转入市场部
  >
  >- 代码操作
  >
  >- ```java
  >  // 1. 查询员工和更换后的部门
  >  Employee employee= session.get(Employee.class, 9);
  >  Department department = session.get(Department.class,7);
  >
  >  // 2. 更换部门
  >  employee.setDepartment(department);  // 语句1
  >  department.getEmployees().add(employee);  // 语句2
  >  ```
  >
  >- 产生的结果
  >
  >  - 最后的结果正确，但是产生的了多余的sql语句（多了不必要的操作）

- 产生的原因

- - 部门bean和员工bean之间的关系，都是通过数据库的外键来表达。

      * 语句1，员工更换成新的部门，需要在员工表的对应记录上，更新外键(update语句)
         * 语句2，部门新增一个员工，也需要在员工表的对应记录上，更新外键(update语句)

- 对应产生的问题

  - >* 原因1：hibernate框架本身的性能不够好。
    >  * 因为，hibernate是对jdbc的封装，提高了开发效率，但降低了运行效率
    >  * 框架是提高了开发效率，降低了运行效率
    >
    >* 原因2：多余的sql语句，进一步降低hibernate的效率。

### b.解决方案

- 放弃外键维护权。

  * 例如：部门放弃外键维护权，部门到员工的关系，就不要修改外键来表达。

- 答疑？为啥不能只执行一次操作？即只执行 语句1  或  语句2

  ```
  employee.setDepartment(department);       // 语句1
  department.getEmployees().add(employee);  // 语句2
  ```

  - 原因一：
    - **存在潜在的隐患** （在同一个事务中）
      - 当只写语句1 ，不写语句2 时，此时事务还未提交 ，且需要执行查询部门人数
      - 此时查询部门人数中就有问题了（不包含新人）
      - 因语句1 事务还没有提交，数据库记录没有变化
  - 原因二：
    - 作为hibernate框架的设计者，应对的开发者，思维模式是不同的。
    - 当用户非要写两条的时候，也能解决这个问题，这个框架肯定就完美了

### c.操作

- ```
  1. 定义
  	a. 某方放弃外键维护权，即某方开始的关系，无需维护外键，不更改数据库的记录。
  		* 例如：上面的案例，如果部门配置了放弃外键维护权，
  			- 部门到员工的关系，即：department.getEmployees().add(employee); 不会生成对应的sql语句，来维护外键。
  			- 员工到部门的关系，employee.setDepartment(department); 会生成sql语句，来维护外键。
  			
  2. 使用方法
  	a. 配置inverse属性，设置成true, 表示放弃外键维护权。
  	b. hibernate规范：1对多的关系中，1的一方放弃外键维护权，多的一方维护
  ```

### d.本质

* 部门上配置放弃外键维护权，就是放弃部门到员工关系的表达，即不生成sql语句，修改外键。
   * 例子： 父母管教小孩，一个说往东，一个说往西。

## 四.级联和放弃外键维护权的区别

>1. 概念：cascade和inverse的区别
>
>   a. cascade:级联，强调操作对象的时候，是否操作其关联对象
>   b. inverse:放弃外键的维护权，强调的某方放弃外键维护权，从该方到关联对象的关系，无需生成sql语句，来维护外键。
>
>2. 总结
>
>- - (1) 前提：部门上配置了级联保存和放弃外键维护权
>
>  - (2) 现象是：部门的数据保存了，员工的数据保存了，但是员工记录中的**外键是null**
>
>    - 这个问题是避免不了的，只能自己去添加维护
>
>    - 执行保存操作的时候就需要保存  执行双向保存
>
>      ```
>      employee.setDepartment(department);       // 语句1
>      department.getEmployees().add(employee);  // 语句2
>      ```
>
>      ​
>
>    (3) 放弃外键维护权本质：放弃对双方关系的表达。
>
>    - * 在一对多中，通过外键字段体现关系，放弃外键维护权，即不负责维护外键字段。

## 五.多对多 

### 1.实战

#### 1.搭建环境

>* 先不处理关系
>
>  1. 创建2个表
>
>  - - 学生表和课程表
>
>  2. 创建2个JavaBean
>  3. 编写2个映射文件
>  4. 编写核心配置文件

#### 2.建立关系（双向多对多）

- 数据库的变化:添加中间表（两个外键分别指向各自的主键）

- ```sql
  --学生选课表
  create table student_course(
    sc_id int primary key auto_increment,
    fk_sid int,
    fk_cid int,
    foreign key (fk_sid) references student(sid),
    foreign key (fk_cid) references course(cid)
  );
  ```

- javaBean的变化：两个类中添加集合保存对应的类

- ```java
  public class Student {
    // 新增
    private Set<Course> courses= new HashSet<>();
  }
  public class Course {
    // 新增
    private Set<Student> students=new HashSet<>();
  }
  ```

- 映射文件的变化

- - Student.hbm.xml中

- ```xml
  <!-- 设置配对关系 
  set标签：javaBean是set集合
  * name:（此配置文件对应的类中集合属性的名字）
  关联的多的一方，在此配置文件对应的类中属性的名字 
  * table: 中间表的名字

  key标签:查询的关键。知道了学生的信息，查询选修了该学生选修的所有课程
  参考：　select * from student_course where sc_sid =?
  * column: 自身在中间表中对应的外键的字段名称

  many-to-many标签: 多对多
  * class: 多对多的查询，查询到的结果，封装的类型的全路径名称
  * column :关联的多的一方，在中间表中，外键字段的名称
  -->
  <set name="courses" table="student_course">
  	<key column="fk_sid"></key>
  	<many-to-many
  		class="com.guozicheng.hibernate.bean.Course" 
  		column="fk_cid">
  	</many-to-many>
  </set>
  ```

- Course.hbm.xml中

- ```xml
  <set name="students" table="student_course">
    <key column="fk_cid"></key>
    <many-to-many
                  class="com.guozicheng.hibernate.bean.Student" 
                  column="fk_sid">
    </many-to-many>
  </set>
  ```

#### 3.级联保存 

```java
@Test
public void test20() {
  //获取session
  Session session = HibernateUtils.getSession();
  //开启事务
  Transaction transaction = session.beginTransaction();

  //学生
  Student s1=new Student();
  s1.setName("苏轼");
  Student s2=new Student();
  s2.setName("黄庭坚");

  //课程
  Course c1=new Course();
  c1.setName("诗");
  Course c2=new Course();
  c2.setName("词");
  Course c3=new Course();
  c3.setName("曲");

  //相互添加，建立关系  学生到 课程
  s1.getCourses().add(c1);
  s1.getCourses().add(c2);

  s2.getCourses().add(c2);
  s2.getCourses().add(c3);

  //执行保存操作  ==》在学生一方配置级联保存
  session.save(s1);
  session.save(s2);

  //事务提交
  transaction.commit();
  //释放资源
  session.close();
}
```

#### 4.导航查询

```java
/**
	*导航 查询
*/
@Test
public void test21() {
  //获取session
  Session session = HibernateUtils.getSession();
  //开启事务
  Transaction transaction = session.beginTransaction();

  //查询
  Student student = session.load(Student.class, 5);
  System.out.println(student+"=="+student.getCourses().size());

  //事务提交
  transaction.commit();
  //释放资源
  session.close();
}
```

#### 5.放弃外键维护权

- 错误示例

  ```java
  //1. 创建对象
  Student student1=new Student();
  student1.setName("刘能");

  Student student2=new Student();
  student2.setName("赵四");

  Course course1= new Course();
  course1.setName("二人转");

  Course course2= new Course();
  course2.setName("相声");

  Course course3= new Course();
  course3.setName("小品");

  //2. 建立关系（学生选课）
  // 学生到课程
  student1.getCourses().add(course1);
  student1.getCourses().add(course2);

  student2.getCourses().add(course1);
  student2.getCourses().add(course3);

  // 课程到学生(第二次演示)
  course1.getStudents().add(student1);
  course1.getStudents().add(student2);

  course2.getStudents().add(student1);
  course3.getStudents().add(student2);	

  //3. 保存操作
  session.save(student1);
  session.save(student2);	
  ```

- 错误原因

  ```
  student1.getCourses().add(course1);
  course1.getStudents().add(student1);
  ```

  - 此时是双向设置，因此 **在中间表中添加了两次数据** ，因此插入的数据是错误的

- 解决错误方案

  - 弃外键维护权， 配置inverse属性为：true，即放弃外键维护权
  - 多对多的关系：主动方维护外键；被动方，放弃外键维护权。
         * 理解主动与被动：学生选课。学生主动选课，学生是主动方。课程是被动方，被动方放弃外键维护权

#### 六.多对多操作中间表

#### 七.一对多  与  多对多   的区别

>最显著的区别：是放弃外键维护权。
>
>(1). 关系的表达
>
>	“1对多”：双方关系，通过外键字段来表达。
>	“多对多”：双方关系，通过中间表的一条完整的记录表达。
>
>(2). 不配置
>	“1对多”：有多余的sql语句，数据是正确的，不过，影响效率
>	“多对多”：数据库记录出现错误，导致数据库不可用。
>
>(3). 如何配置
>	“1对多”：1的一方放弃，多的一方维护
>	“多对多”：主动方维护，被动方放弃。例如：学生选课，学生主动选课，是主动方。
>		* 原因：符合业务的逻辑。

