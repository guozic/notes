# plsql编程语言

## 什么是PL/SQL

- PL/SQL 是ORACLE 对sql语言进行的一种过程化扩展。
  - 即在sql命令语言中增加了过程处理语句。例if判断   for循环
  - 将sql语言的数据操纵能力  与   过程语言的数据处理能力  相结合，使得PL/SQL面向过程。

## PL/SQL语法

```plsql
declare
	说明部分   （变量说明， 光标申明， 例外说明）
begin
	语句序列   （DML）
exception
	异常处理语句
end;
```

​

## 变量及变量的定义

### 说明变量

- char, varchar2, darte, number, booolean, long
- eg:
  - varl     char(15);
    - 声明变量，用分号结尾
  - married      boolean  :=  true
    - 初始化变量  使用  ‘:=’   进行赋值

### 引用型变量

- my_name   emp.ename%type

  - 引用型变量，即my_name  的 数据类型  与 emp表中ename字段的类型一样

- 语法：在sql中使用  into  来赋值

  ```plsql
  declare
  	my_name   emp.ename%type;
  begin
  	select e.ename into my_name from emp e where e.empno= 7369
  end;
  ```

  ​

### 记录型变量（将一整条数据赋值给变量（类似于对象））

- emp_rec  emp%rowtype

- 记录 变量分量  的引用

- emp_rec.ename = "ADAMS";

  ```plsql
  declare
  	emp_rec  emp%rowtype;
  begin
  	select * into emp_rec  from  emp e where e.empno = 7369;
  	--以下是输出语句
  	--此时  emp_rec  包含   e.empno = 7369  这个人所有的数据 （*），直接调用即可
  	dbms_output.put_line(emp_rec.ename ||  emp_rec.sal)
  end;
  ```

## if语句

- ### 语法一

  ```plsql
  if  条件  then 
  	语句1
  	语句2
   end if;
   ----------------------------------------------
   --示例：若键盘输入1， 则控制台输出  ‘我是1’
  declare
     pnum number := &num;
   begin
   	if pnum = 1 then
   		dbms_output.put_line('我是1');
    end if;
   end;
  ```



- ### 语法二

  ```plsql
  if  条件  then
  	语句1
  else
  	语句2
  end if；
  --------------------------------------------------------------
  --示例：若键盘输入1， 则控制台输出  ‘我是1’，否则输出  ‘我不是1’
   declare
     pnum number := &num;
   begin
   	if pnum = 1 then
   		dbms_output.put_line('我是1');
    else
       dbms_output.put_line('我不是1');
    end if;
   end;
  ```

  ​

- ### 语法三

  ```plsql
  if  条件1  then
  	语句1
  elsif 条件2  then 
  	语句2
  elsif 条件3  then 
  	语句3
  else
  	语句2
  end if；
  ----------------------------------------------------
  --示例：根据输入的不同的年龄，打印出不同的语句
  --注意：使用单引号
   declare
    p number := &num;
  begin 
    if p < 18 then
      dbms_output.put_line('未成年！');
    elsif p < 24 then
      dbms_output.put_line('青少年！');
    elsif p < 48 then
      dbms_output.put_line('中年！');
    else
      dbms_output.put_line('老年！');
    end if;
  end;
  ```


## 循环

- ### 方式一：while循环

  ```plsql
  --初始化total
  total number := 1;
  -----------循环体
  while total <= 2500  loop 
  ...
  total := total + salary;
  end loop；

  -----------------------------------------------------------
  --示例：输出1到10
  declare
  	step number :=  1;
  begin 
  	while step <= 10   loop
        dbms_output.put_line(step);
        step := step + 1;
  	end loop;
  end;
  ```



- ### 方式二

  ```plsql
  --初始化  step
  step number := 1;
  ------------循环体
  loop eit when  step > 10;
  	dbms_output.put_line('--' || step);
  		step := step + 1;
  	end loop;
  ---------------------------------
  --示例：输出1到10
  declare
  	step number := 1;
  begin
  	loop exit when step > 10;
  		dbms_output.put_line('--' || step);
  		step := step + 1;
  	end loop;
  end;
  ```

  ​

- ### 方式三：

  ```plsql
  --循环体(i 不用初始化)
  for i in  1..10  loop
  	dbms_output.put_line('++' ||  i);
  end loop;
    ------------------------------------------
    --示例：输出1到10
    declare
    	--for内部自动初始化
    begin 
    	for i in 1..10 loop
    		dbms_output.put_line('++' ||  i);
       end loop;
    end;

    -----反转------------------------------------
    --示例：输出10到1
    declare
    	--没有初始化
    begin 
    	for i in reverse 1..10 loop
    		dbms_output.put_line('++' ||  i);
       end loop;
    end;
  ```


## 游标

- 对查询结果集的疯转，通过游标对结果集进行遍历
- 类似于JDBC中的ResultSet

### 游标分类

#### 无参游标

```plsql
select * from emp;

--输出所有员工的姓名
declare
  --声明游标
  --cursor 游标名 is  查询语句
  cursor vemps is select * from emp;
    --定义引用型变量
  vrow emp%rowtype;
begin
  --打开游标
  open vemps;

  --提取记录
  loop
         fetch vemps into vrow;
        --判断游标向下移动是否有数据
        exit when vemps%notfound;
        
        dbms_output.put_line('用户的姓名 : '||vrow.ename);
  end loop;
  --关闭游标
  close vemps;
end;

```

#### 带参游标

```
declare
  --声明游标  形参
  cursor vemps(vdep number) is select * from emp where deptno = vdep;
  --声明变量
  x emp%rowtype;
begin
  --打开游标  实参
  open vemps(10);
  
  --提取数据
  loop
      fetch vemps into x;
      --游标移动判断是否有记录
      exit when vemps%notfound;
      
      dbms_output.put_line('姓名： '||x.ename); 
  end loop;
  
  --关闭游标
 close vemps;
end;
```

#### 扩展：for循环(最简便)

```
declare
       --声明游标
       cursor vemps is select * from emp;
       --声明变量    for循环中不用声明此变量，循环体内自动声明
      -- y emp%rowtype;
begin
       for y in vemps loop
         dbms_output.put_line(y.ename);
       end loop;
end;

```



- eg:

```plsql
-- 给所有员工涨工资,总裁涨1000 经理涨800 其他人涨400
-- 1.游标: 所有员工
-- 2.遍历游标
-- 3. 根据职位,判断
-- 4.涨工资
-- 提交记录
declare
   -- 声明游标
   cursor vemps is select * from emp;
   -- 声明记录型变量
   vrow emp%rowtype;
begin
   -- 打开游标
   open vemps;
   
   -- 循环遍历所有记录
   loop
      fetch vemps into vrow;
      exit when vemps%notfound;
      
      -- 根据职位,判断
      if vrow.job ='PRESIDENT' then
         update emp set sal = sal + 1000 where empno=vrow.empno;
      elsif vrow.job ='MANAGER' then
         update emp set sal = sal + 800 where empno=vrow.empno;
      else
         update emp set sal = sal + 400 where empno=vrow.empno;
      end if;
   end loop;
   -- 关闭游标
   close vemps;
   -- 提交
   commit;
end;
```



### 开发步骤

- 声明游标
  - 无参格式：cursor 游标名 as/is 查询语句
  - 带参格式：cursor 游标名（变量名  数据类型） is 查询语句  where   deptno=变量名
- 打开游标
- - open 游标名
- 从游标中提取数据
  - fetch  游标名  into  变量1
  - 【注：这个变量1需要在declare 中声明】
    - exit when 变量名%notfound
  - 【注：游标属性：
    - 游标名%found    表示游标移动后有值
    - 游标名%notfound  表示游标移动后没有值，此时一般配合exit 退出循环使用
  - 】
- 关闭游标
  - close 游标名

## 例外（异常）

- 例外使用格式（与java中 异常使用方式类似）

```plsql
declare
       -- 声明部分
   begin
       -- 业务逻辑
   exception     
       -- 捕获例外
       when 例外1 then
         
       when 例外2 then
         
       when others then  -- 其它所有例外
         
   end; 
```



#### 系统例外

- no_data_found 没有找到数据异常

- too_many_rows 查询出了多行记录

- zero_divide 被零除

- value_error 算术或转换错误

  ​

- eg

```plsql
declare
   i number;
   vrow emp%rowtype;
begin
   -- i := 5/0;
   -- i := 'aaa';
   -- select * into vrow from emp where empno=1234567;

   select * into vrow from emp;
exception
   when too_many_rows then
      dbms_output.put_line('找到了多行记录,赋值给了单行变量'); 
   when no_data_found then
      dbms_output.put_line('没有找到数据'); 
   when value_error then
      dbms_output.put_line('类型错误'); 
   when zero_divide then
       dbms_output.put_line('除零例外');
   when others then
      dbms_output.put_line('发生了其它的例外');
end;
```





#### 自定义例外

- 自定义例外格式

  ```plsql
  /*
  自定义例外:
               列外名称  exception;
  抛出自定义例外:
               raise 例外名
  */            
            
  declare
     -- 声明例外
     no_emp_found exception;
  begin
     -- 抛出自定义例外
     raise no_emp_found;
  exception 
    when no_emp_found then
        dbms_output.put_line('发生了no_emp_found');  
  end;
  ```

  - eg:

  ```plsql
  -- 查询指定编号的员工, 如果没有找到,则抛出自定义的例外
  declare
     -- 声明例外
     no_emp_found exception;
     -- cursor游标
     cursor vemp is select * from emp where empno=1234567;
     -- 记录型变量
     vrow emp%rowtype;
  begin
     -- 1.打开游标       
     open vemp;
     
     -- 2. 提取记录
     fetch vemp into vrow;
     -- 判断有没有记录
     if vemp%notfound then
       raise no_emp_found;
     end if;
     -- 关闭游标
     close vemp;
  exception
     when no_emp_found then
       dbms_output.put_line('捕获了自定义的例外');
  end;
  ```

  ​

## 存储过程

#### 简介

- 类似java中方法的封装
- ORACLE 将 一段已经编译好的PL/SQL代码片段，封装在数据库中

#### 作用

- 类似java中封装的作用
- 提高复用性
- 提高执行效率

#### 语法

- 格式

```plsql
create   [or replace]   procedure 存储过程的名称（参数1  in/out   参数类型，参数2  in/out   参数类型 ， ...）

is / as

  --声明部分

begin

-- 业务部分

end；

```



- eg1  没有返回值，参数都为in

```plsql
---创建 -----存储过程----------------
create or replace procedure proc_update(n in number, m in number)
                                          -- n 员工编号  m  工资更新的数据
is
       --声明一个变量，记录数据
       x number;
       y number;
begin
       --先查询出更新工资前的工资
       select sal into x from emp where empno = n;
       dbms_output.put_line('更新前工资：'||x);
       update emp set sal = sal + m where empno = n;
       
       --查询更新后的工资
        select sal into y from emp where empno = n;
        dbms_output.put_line('更新后工资：'||y);
end;

------调用过程
--方式一
call proc_update(7369, 1000);
--方式二
declare

begin 
  proc_update(7369, 1000);
end;
```

- 当过程编译成功后，右边的文件procedures 中就会出现对应的过程表示，若有误图标上则会出现红叉
- eg2 有返回值  也有参数

```plsql
--获取员工年薪   带返回值
create or replace procedure pro_getYearSal(n in number, m out number)
is
begin
  select sal*12+nvl(comm, 0) into m from emp where empno = n;
end;

--调用过程 获取员工年薪
declare
   --声明变量，接收返回值
   x number;
begin
  --调用过程，并接收返回值x
  pro_getYearSal(7369, x);
  
  dbms_output.put_line('年薪为：'||x);
end;

```





## 存储函数

#### 简介

- 类似java中方法的封装
- ORACLE 将 一段已经编译好的PL/SQL代码片段，封装在数据库中

#### 作用

- 类似java中封装的作用
- 提高复用性
- 提高执行效率

#### 语法

- 格式

```plsql
create [or replace] function 函数名（参数1 in/out 数据类型，..）return 返回值的数据类型
is/as
	--声明变量
begin
	--进行业务操作
end;
```

- eg

```plsql
-----创建-----函数------------------------------
create or replace function func_getYearSal(n number) return number
is
--声明变量
       m number;
begin
       select sal*12+nvl(comm,0) into m from emp where empno = n;
       return m;
end;

--------------------调用函数
declare
 -- 声明变量
    x number;
begin
  x := func_getYearSal(7369);
  dbms_output.put_line('年薪为：'||x);
end;
```



#### 函数与过程的区别

>- 过程能实现的功能函数能实现，函数能实现的功能过程也能实现
>- 过程不能直接在SQL语句中调用，函数可以在SQL语句中调用
>- 过程没有返回值，函数有返回值
>- 过程与函数在本质没有区别
>- 建议自己封装过程



## JAVA调用存储过程

#### JDBC操作数据库的基本步骤

1. 导入驱动包
2. 注册驱动
3. 获取连接
4. 编写SQL语句
5. 获取执行SQL语句对象
6. 执行SQL
7. 处理结果
8. 释放资源

## 触发器

#### 简介

- 类似于JAVA 中的监听器


- 当用户执行了 修改数据库的操作（insert、update、delete），通过我们写的触发器，触发一段PL/SQL代码片段，即业务逻辑

#### 作用

- 数据的校验
- 监听数据库中数据的变化

#### 语法

```plsql
create [or replace] trigger 触发器的名称
before / after     --执行操作前触发  /  执行操作后触发
insert / update /delete  --监听那种数据改变的触发
on 表名
[for each row]  --是否行级触发
declare

begin

end;
```

#### 分类

- 语句级触发
  - 触发后，不管影响多少行数据，只会执行一次
- 行级触发
  - 出发后，每影响一行数据，就会执行一次（执行多次）

#### 示例

- eg   向emp插入数据之前,输出一个helloworld

```plsql
-- 向emp插入数据之前,输出一个helloworld
create or replace trigger tri_test1
before
insert
on emp
declare

begin
  dbms_output.put_line('hello world!!');
end;

insert into emp(empno,ename) values(9527,'华安');
```



- eg   校验指定的时间不能插入员工

```plsql
-- 校验指定的时间不能插入员工, 周二不允许办理入职
select trim(to_char(sysdate,'day')) from dual;
-- 触发器
-- 动作:before insert
create or replace trigger tri_test2
before 
insert
on emp
declare
 vday varchar2(20);
begin
  -- 查询当前周几
  select trim(to_char(sysdate,'day')) into vday from dual;
  -- 判断当前是否是周二
  if vday = 'tuesday' then
     -- 抛出例外
     raise_application_error(-20000,'老板不在,周二不允许办理员工入职');
  end if;
end;


insert into emp(empno,ename) values(9527,'华安');
```

- eg   语句级触发器

```plsql
-- 语句级触发器
create or replace trigger tri_test3
before
update
on emp
declare

begin
   dbms_output.put_line('语句级触发器: update');
end;

update emp set sal = sal+1;
```

- eg  行级触发器

```plsql
create or replace trigger tri_test4
before
update
on emp
for each row
declare

begin
  dbms_output.put_line('行级触发器: update'||'新的记录:'||:new.sal||' 旧的记录:'||:old.sal);
end;

update emp set sal = sal+100;

select * from emp;
```

- eg  校验员工 涨工资

```plsql

-- 校验员工 涨工资后的工资一定要 大于 涨工资前的工资
-- 触发器
-- 动作: before update
-- 行级触发器: 
   -- 涨工资后的工资 :new.sal
   -- 涨工资前的工资 :old.sal
--  涨工资后的工资   <  涨工资前的工资
create or replace trigger tri_checksal
before
update
on emp
for each row
declare
    
begin
   --  涨工资后的工资   <  涨工资前的工资
   if :new.sal <= :old.sal then
      raise_application_error(-20001,'坑爹的吧,明明降工资了!'); 
   end if;
end;

update emp set sal = sal-1000;
```

- eg  模拟mysql中auto_increment自动增长属性

```plsql

/*
     模拟mysql中auto_increment自动增长属性
     
     序列: sequence
     
     触发器: before insert 
          行级触发器 :new.id 赋值
     
*/
create table itheima(
    id number primary key,
    name varchar2(20)
);

-- 创建序列
create sequence seq_itheima;


-- 创建触发器
create or replace trigger tri_itheima
before
insert
on itheima
for each row
declare

begin
  -- 给ID赋值
  select seq_itheima.nextval into :new.id from dual;
end;  

-- follow your heart! 

-- 有时候需要自己赋值, 有时候交由数据库自动增长
insert into itheima values(null,'zs');

insert into itheima values(8,'ls');

select * from itheima;
```

