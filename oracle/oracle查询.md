# ORACLE

## 1.范例：老师笔记

### day01-基本查询、单行函数，多行函数

```sql
/*
  Oracle体系结构:        
     数据库  --->  实例orcl --->  表空间(逻辑)(用户) ---> 数据文件(物理)
     地球    --->  中国China ---> 省份(逻辑)(雄安新区区长)   ----> 土地,山川,风景(物理)
  
     相亲项目:
        mysql:
          1. 创建用户
          2. 创建数据库
          3. 用户去建表
       Oracle:
          1.创建用户
          2.创建表空间
          3.用户在表空间创建表   
          
     SQL: Structured Query Language 结构化查询语言
     
     SQL分类:
           四类:
           DDL: 数据定义语言, 表结构相关,create , alter, drop , truncate
           DML: 数据操纵语言, 跟数据相关, insert , update , delete
           DQL: 数据查询语言 查询 : select
           DCL: 数据控制语言 控制数据库权限,安全, grant , revoke
           
     SQL语句编写顺序/基本结构
           select 显示列名 from 表名 [where 条件 group by 分组 having 分组之后过滤 order by 排序]   
*/

/*
   dual : 伪表/虚表, 系统提供的表, 用于补齐语法结构
*/
select 1+1 from dual;
select * from dual;

/*
     别名查询 : as 关键字, 可以省略
     
     如果包含特殊符号,请使用双引号
     
     单引号表示的字符串
     insert into emp values('')
*/
select ename as 姓名 from emp;
select ename "姓 名" from emp;

select ename '姓 名' from emp;

/*
       去除表中重复记录 distinct
       注意: 所有列数据都相同,才算重复的记录 
*/
select distinct job,mgr from emp;

/*
       四则运算不能和null值进行运算
               null 代表不确定的值     
*/
-- 查询员工年薪
select emp.*,sal*12 from emp;

-- 查询员工年薪 + 奖金
select emp.*,sal*12,sal*12+nvl(comm,1) from emp;

-- nvl(p1,p2) 如果p1 为null ,则返回p2 ,否则返回p1
select nvl(null,5) from dual; 
select nvl(2,5) from dual; 

-- 字符串拼接 姓名:SMITH
select ename from emp;
select concat('姓名:',ename) from emp;
/*
       oracle特有的字符串拼接方法:   ||  相当于java + 号拼接
*/
select '姓名:'||ename from emp;

/*
     条件查询:
         比较运算符: > >= = < <= != <>
         逻辑运算符: and or not    
         其它运算符:
                is null
                is not null
                between .. and ..
                in(集合) 
                like 模糊查询
                exists 
*/
-- 查询员工工资在1500 - 3000之间
select * from emp where sal between 1500 and 3000;

select * from emp where sal >= 1500 and sal <=3000;

-- 查询有奖金的员工信息
select * from emp where comm is not null;

-- 注意: 区分大小写  mysql : 指定校对规则collate utf8_bin 
-- 查询名字在某个范围的员工信息 'ALLEN','MARTIN','TURNER'
select * from emp where ename in('ALLEN','MARTIN','TURNER');
select * from emp where ename in('allen','MARTIN','TURNER');
select * from emp where ename = 'allen';

/*模糊查询
       like 
         %  : 匹配任意个数的字符
         _  : 匹配单个字符 
         
      escape : 相当于是告诉Oracle转义字符是哪个
*/
-- 查询名称中第三个字符是 L 的所有的员工信息
select * from emp where ename like '__L%';

-- 查询名称中包含  % 的员工信息
update emp set ename='SMI%TH' where empno=7369;

select * from emp where ename like '%\%%' escape '\';


/*
       排序 : order by  字段名 排序规则
          asc   : 升序 ascend 默认的排序规则
          desc  : 降序 descend  
       
       
       排序中的空值问题
          nulls first | last
*/
-- 按照工资升序排序
select * from emp order by sal;
insert into emp(empno,ename) values(9527,'HUAAN');
-- 降序排序
select * from emp order by sal desc nulls last;

-- 按照部门升序,工资降序排序
select * from emp order by deptno asc, sal desc;



/*
      单行函数: Math
         数值函数 : round,trunc,ceil,floor mod
         字符函数
         日期函数
         转换函数
         常用函数
*/
-- 四舍五入
select round(45.926,2) from dual; -- 45.93
select round(45.926,1) from dual; -- 45.9
select round(45.926,0) from dual; -- 46
select round(45.926,-1) from dual; -- 50
select round(45.926,-2) from dual; -- 0

-- trunc 截断
select trunc(45.926,2) from dual; -- 45.92
select trunc(45.926,1) from dual; -- 45.9
select trunc(45.926,0) from dual; -- 45
select trunc(45.926,-1) from dual; -- 40
select trunc(45.926,-2) from dual; -- 0

-- ceil 向上取整  向大值取整
select ceil(-12.9) from dual;  -- -12
-- floor 向下取整  向小值
select floor(-12.1) from dual; -- -13

-- mod 
select mod(9,2) from dual;

-- abs 
select abs(-12) from dual;


--  字符函数
select length('abcdefg') from dual;

-- 截取 , 不管是0,还1 都是从第一个字符开始截取
select substr('abcdefg',0,3) from dual; -- abc
select substr('abcdefg',1,3) from dual; -- abc
select substr('abcdefg',2,3) from dual; -- bcd

-- concat

-- 替换 replace
select replace('abcdefg','c','123') from dual;

-- trim 去除两端空格
select '                 hello             ' from dual;
select trim('                 hello             ') from dual;

select TRIM('X' from 'XXXgao qian jingXXXX') from dual;

select * from emp;


-- 日期函数 
-- 2017/10/19 17:46:35
select sysdate from dual;
select sysdate + 1/24 from dual;

-- 查询员工入职的天数
select emp.*,sysdate - hiredate from emp;

-- 查询员工入职的周数
select emp.*,(sysdate - hiredate)/7 from emp;

-- 查询员工入职的月数
select emp.*,months_between(sysdate,hiredate) from emp;

-- 查询员工入职的年数
select emp.*,months_between(sysdate,hiredate)/12 from emp;


-- 计算几个月后的今天
select add_months(sysdate,3) from dual;

/*
   数字函数,日期函数,字符函数     
*/
-- 字符转数值
select 10+'24' from dual; -- 1024 34
select 10+to_number('24') from dual;

-- 数值转字符 : 格式化数值
select to_char(sal,'$9999.999') from emp;
select to_char(1234567,'$9,999,999.99') from dual;

-- 日期转字符
select sysdate from dual;
-- 
select to_char(sysdate,'yyyy-mm-dd hh:mi:ss') from dual;
select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') from dual;
select to_char(sysdate,'yyyy') from dual;

select to_char(sysdate,'d') from dual; -- 一个周过了多少天
select to_char(sysdate,'dd') from dual;  -- 一个月过了多少天
select to_char(sysdate,'ddd') from dual;  -- 一个年过了多少天

select to_char(sysdate,'day') from dual;  -- 星期几

select to_char(sysdate,'dy') from dual;  -- 星期的简写



-- 字符转日期
-- 查询 1980年 - 1983年入职的员工信息
select * from emp where hiredate between to_date('1980-01-01','yyyy-mm-dd') and to_date('1983-12-31','yyyy-mm-dd');


-- nvl2(p1,p2,p3)  相当于三元运算符
select nvl2(null,5,6) from dual;
select nvl2(1,5,6) from dual;



/*
      条件表达式
      通用的   
         case 列名
            when 值1 then 内容
            when 值2 then 内容2
            else
              
            end
      Oracle特有的
            decode(列,if1,then1,if2,then2,if3,then3,default)
            
*/
-- 给员工表中的员工,取中文名
select ename,ename from emp;

select ename,case ename  
                  when 'HUAAN' then '华安'
                  when 'ALLEN' then '冠西哥哥'
                  when 'JONES' then '柏芝姐姐'
                  else
                    '吃瓜群众'
                  end  
 from emp;
 
 select ename, decode(ename,'HUAAN','华安','ALLEN','诸葛村夫','路人甲') from emp;



/*
  多行函数:
        max min sum count avg
        多行函数忽略空值
        
*/
-- 查询奖金总和  2200
select sum(comm) from emp;

-- 查询员工总人数 15
select count(*) from emp;

-- 所有员工的平均奖金 550
select avg(comm) from emp;
select sum(comm)/count(*) from emp;


/*
       分组查询: group by
              先对记录进行排序,排序之后再做分之后的操作
         select 分组查询的条件, 分组之后的操作 from 表名 group by 分组条件 having 过滤                 

*/
-- 查询每个部门的工资总和
select * from emp order by deptno;

select deptno,sum(sal) from emp group by deptno;

select deptno,sum(sal) from emp group by deptno having sum(sal) > 10000;


/*
       having 和 where 区别
       having : 接聚合函数      , 分组之后执行
       where  : 不可以接聚合函数 , 分组之前执行的

       编写顺序:
              select 输出列 from 表名 where 条件  group by 分组　having 分组之后过滤　order by 排序
              
       执行顺序:
            from .. where .. group by .. having ... select ... order by..  
       
*/

-- 17,统计薪资 大于 薪资最高的员工 所在部门 的平均工资 和 薪资最低的员工 所在部门 的平均工资 的平均工资 的员工信息。
--------  sal  >                      10      100      and                  20       200      (100+200)/2
--- sal > 150



select * from emp;
```





## 2.自己练习

### 基本查询

```sql

//别名查询
select empno 部门 , ename 姓名 from emp;

//去重复查询
select distinct mgr from emp;

//查询年薪
select ename 姓名,sal*12 年薪 from emp;

//自付字符串连接连接
select concat('--姓名是', ename ) from emp;
select '-姓名是：' || ename || '-编号是：' || empno from emp;

//条件查询和排序
//非空查询 
select * from emp where comm is not null;
select * from emp where comm is  null;
//and
select * from emp where comm is not null and sal > 1500;
select * from emp where comm is not null and sal >= 1500;
//or
select * from emp where comm is not null or sal >= 1500;

//查询工资 不大于1500  且  没有奖金的   人 ---> not(查询条件)
select * from emp where comm is null and not(sal > 1500);

//范围限制
//查询基本工资 大于1500  但是  小于 3000  的人
select * from emp where sal >=1500 and sal <= 3000;
select * from emp where sal between 1500 and 3000; 
//between  不仅可以使用在数值之间 也可以使用在  日期 之间 （？？日期格式）
select * from emp where hiredate between '1-1月-1981' and '31-12月-1981';
//查询 姓名 叫 smith 的员工
//oracle 区分大小写
select * from emp where ename = 'smith';
select * from emp where ename = 'SMITH';

//查询编号是  7369 7499 7521 的员工
//or  查询  可以是数值 也可以是字符串 
select * from emp where empno ='7369' or empno = 7499 or empno = 7521; 
//in  查询  可以是数值 也可以是字符串 
select * from emp where empno in ('7369', 7499, 7521);
select * from emp where empno not in ('7369', 7499, 7521);
select * from emp where ename in ('SMITH', 'JONES', 'BLAKE');

//模糊 查询
select * from emp where ename like '%S%';
//没有关键字的话 表示 查询 全部
select * from emp where ename like '%%';
//oracle  中 不等号 有两种方式 
select * from emp where empno != 7369;
select *from emp where empno <> 7369;

//order by  对结果进行排序
  //升序 默认
select * from emp order by sal asc; 
 //降序 
select * from emp order by sal desc;  
//多个排序字段 间 用 逗号 分割开
select * from emp order by sal asc, hiredate desc;
//排序中的空值 问题 
//排序时存在 null  的话 就存在 问题
select * from emp order by sal , comm desc;
//解决方法
select * from emp order by mgr  desc nulls last;
select * from emp order by mgr  desc nulls first;

select * from emp;
```



### 单行函数

```sql

//单行 函数
// 字符串连接  可以用concat  也可以用‘||’  建议使用 '||'
select 'hello' || 'word' from dual;
select concat('hello','word') from dual;
//字符串的截取 substr(p1, p2, p3) p1：源字符串   p2:截取的起始位置  （0 和  1 效果一样  ）   p3:截取多少个
select substr('helloword', 1, 3) from dual;
//获取字符串的长度 length
select length('helloword') from dual;
//字符串的替换 replace(p1,p2,p3)  p1：源字符串   p2:需要替换掉 的 字符串    p3:替换后 的字符串
select replace('helloword','e','123') from dual;
//数值 函数 
//四舍五入 round(p1,p2 ) p1:源数字  p2 :保留几位小数（可以为 负数）  
select round(1928.43234,2) from dual;
//截断   trunc
select trunc(327184.4321,3) from dual;
//求余 mod 
select mod(1000,3) from dual;
//日起函数
//oracle 中日期型数据 包含 两个值 ；  日期 和 时间 
         //  默认格式:  DD-MON-RR
    //日期的数学运算
         //在日期上 加上  或   减去 一个 数字（1 表示 24小时 ，0。5表示 12小时）  ，结果 任然为 日期 
         //两个日期 相减 ，得到间隔 的 天数 
         
 //查询 所有  员工 进入公司的  周数--->员工进入的 天数  /  7  四舍五入
 select ename , round((sysdate - hiredate )/7,  0 ) from emp;
 //查询 所有  员工 进入 公司 的 月数 
 select ename,months_between(sysdate , hiredate ) from emp;
 select emp.*,months_between(sysdate,hiredate) from emp;
//求3个月 后 的日期
select add_months(sysdate,3)from dual;


 //转换函数
   //字符串-->数字==to_number 
   //字符串-->时间 ===  to_date
   // 数字/时间 --->字符串 ==to_char
   
 //时间转 字符串  
   //注意:单引号 --> to_char(hiredate, 'yyyy-mm-dd')  
 select empno,ename,to_char(hiredate, 'yyyy-mm-dd') from emp;
    //月份 中 去掉 前导 0  1980-2-20  -->'fm'
 select empno,ename,to_char(hiredate, 'fmyyyy-mm-dd') from emp;

//字符串 转 数值 
select to_number(10+'10') from emp;
//字符串 转 日期
select to_date('1982-9-21','yyyy/mm/dd')from emp;
//通用函数   适用于任何 数据类型  包括空值
//例如  查询 所有 员工的年薪 （月薪*12  +  奖金）
//-->结果 ：包含 null  的 计算  结果 位  null 
select ename,(sal*12+comm) from emp;
//-->解决 ： nvl(p1,p2)-->表示  p1 为null 时  按p2 算
       --> :  nvl2(p1, p2, p3)
       --->: nullif(p1, p2)
       --->coalesce(p1, p2, p3, ..  pn )
select ename,nvl(comm, 0), (sal*12+ nvl(comm, 0)) from emp;

//条件表达式 
//根据 10  号 部门 员工 的 工资 ，显示 税率 
select ename, sal, 
              decode(trunc (sal/2000, 0),
                                0, 0.00,
                                1, 0.09,
                                2, 0.20,
                                3, 0.30,
                                4, 0.40,
                                5, 0.42,
                                6, 0.44,
                                   0.45
                      ) tax_rate
                      from emp where deptno = 10;
//给员工取相应  的中文 名 
//通用 -->  MYSQL   ORACLE
select ename, case ename
                   when 'SMITH' then '斯密斯'
                   when 'CLARK' then '哈哈'
                    else 
                        '路人' 
                         end   from emp;
select ename,case ename  
                  when 'HUAAN' then '华安'
                  when 'ALLEN' then '冠西哥哥'
                  when 'JONES' then '柏芝姐姐'
                  else
                    '吃瓜群众'
                  end  
 from emp;
//oracle  特有                                  
select  ename, decode(ename, 'SMITH', '111', 'ALLEN', '22', 'WARD', '33', '其他人' ) from emp;
```





### 多行函数 

```sql

//多行 函数 
-->avg()
-->sum()
-->count()  null  也会 统计 在内 
-->max()
-->min()
select count(*) from emp;
select count(mgr) from emp;

select avg(sal) from emp;
select min(sal) from emp;
select max(sal) from emp;
select sum(sal) from emp;

//分组函数 
//查询每个部门 的人数 
select  deptno, count(ename) from emp group by deptno; 
//查询每个部门的平均 工资
select deptno,avg(sal) from emp group by deptno;

select deptno,avg(sal) from emp //-->报错   （sql 中 不 会 报错 ）
select deptno, ename,avg(sal) from emp group by deptno //-->报错  不能有分组外的 字段显示 


//过滤分组  函数
select deptno, avg(sal) from emp where deptno > 10 group by deptno having avg(sal) > 1000 ;


```

- where  和 having  的 区别  
  - ​


### 第一天习题 

- （以oracle 自带 账号 hr  为原本  )

```sql
//1. 查询工资大于12000的员工姓名和工资
select first_name, salary from Employees   where salary > 12000; 

//2. 查询员工号为176的员工的姓名和部门号
select  first_name, job_id from employees where employee_id = 176;

//3. 选择工资不在5000到12000的员工的姓名和工资
select first_name, salary from employees where salary < 5000 or salary > 12000;

//4. 选择雇用时间在1998-02-01到1998-05-01之间的员工姓名，job_id和雇用时间
select first_name, job_id, hire_date from employees where hire_date between to_date('1998-02-01','yyyy-mm-dd') and to_date('1998-05-01', 'yyyy-mm-dd');

5. 选择在20或50号部门工作的员工姓名和部门号
select first_name, job_id from employees where department_id in (20, 50);

//6. 选择在1994年雇用的员工的姓名和雇用时间 ??
select first_name,hire_date from employees where Hire_date = to_date('1994', 'yyyy');

//7. 选择公司中没有管理者的员工姓名及job_id
select first_name,job_id from employees where manager_id is null;

//8. 选择公司中有奖金的员工姓名，工资和奖金级别
select first_name, salary, commission_pct from employees where commission_pct is not null;

//9. 选择员工姓名的第三个字母是a的员工姓名
select  first_name from employees where first_name || last_name like '__a%'; 

//10. 选择姓名中有字母a和e的员工姓名
select first_name from employees where  first_name || last_name like '%a%' or first_name || last_name like '%e%' ; 

//11. 显示系统时间
select sysdate from dual;

//12. 查询员工号，姓名，工资，以及工资提高百分之20%后的结果（new salary）
select Employee_id, first_name, salary, salary*(1.2) from employees;

//13. 将员工的姓名按首字母排序，并写出姓名的长度（length）
select first_name, length(first_name || last_name) from employees order by first_name||last_name;


//14. 查询各员工的姓名，并显示出各员工在公司工作的月份数
select first_name, months_between(sysdate, hire_date) from employees;

//15. 查询员工的姓名，以及在公司工作的月份数（worked_month），并按月份数降序排列
select first_name, months_between(sysdate, hire_date) from employees order by months_between(sysdate, hire_date) desc;
```





### *多表查询

- (以用户scott为基础)

#### 1.ORACLE方言 与  SQL99标准

- SQL99   ----->  sql查询中通用的格式，其他数据库中也可以使用


- oracle   ----->  oracle  的  方言



| 查询方式  | ORACLE |             SQL99             |
| :---: | :----: | :---------------------------: |
| 等值连接  |        |          Cross joins          |
| 不等值连接 |        |         Natural joins         |
|  外连接  |        |         Using clause          |
|  自连接  |        | Full or two sided outer joins |



#### 2.笛卡尔积

- 产生的条件
  1. 省略连接条件
  2. 连接条件无效
  3. 所有表中的所有行互相连接
- 避免的方式
- 1. 在where中加入有效的连接条件
- 注意：
- 1. 在实际工作中，应避免使用笛卡尔全集

#### 3.内连接

- 注：使用连接在多个表中查询数据
- 1. 在where 字句后面添加  连接条件
  2. 在表中有相同列时，在列名前加上 表名前缀
- 语法格式:

```sql lite
select table1.column, table2.column
from table1, table2
where table1.column1 = table2.column2;
```

##### 等值连接

- eg:查询员工信息：员工号， 姓名， 月薪， 和部门名称 
- 分析：
- 1. 要查询两张表
  2. 两张表的连接条件：员工表中 员工的部门与部门表中的部门**相同**

```sql lite
select e.empno, e.ename, e.sal, d.dname
from emp e, dept d
where e.depten = d.depten
```

1. 连接多个表
   1. 连接n张表，则需要（n-1）个连接条件
2. 表的别名
   1. 如果使用了表的别名，则不能再使用表的真名
   2. 使用表的别名可以简化查询
   3. 使用表的前缀可以提高执行查询效率

##### 不等值连接

- eg：查询员工信息：员工号，姓名，月薪，和工资级别

- 分析：

- 1. 要查询两张表
  2. 员工的工资，在工资表中的那个级别，**是个范围值**

  ```sql lite
  select e.empno, e.ename, e.sal ,s.grade
  from emp e, salgrade s
  where e.sal between s.losal and s.hisal
  ```

##### 自连接

- 自连接: 通过表的别名，将同一张表视为多张表

- eg:--查询员工信息：员工姓名  老板姓名

- 分析：

- 1. 查询员工信息----》以员工为主
  2. 查询出每一个员工对应的老板
  3. 员工对应的mgr  ==   其老板对应的员工编号

  ```sql lite
  select e1.ename 员工姓名, e2.ename 老板姓名
  from  emp e1,emp e2.empno
  where e1.mgr = e2.empno
  ```

- 自连接可以，但**不适合**操作大表

- 可以使用层次连接查询

##### 层次连接（了解）

- 表中数据隐藏成树状结构


- 每张表都隐藏了一个列 level
- ...


- 标准语法格式

```sql lite
 select level,empno,ename,mgr
 from emp
 connect by prior empno=mgr
 start with mgr is null
 order by 1;
```



#### 4.外连接

>- 希望把某些不成立的记录（40号部门），任然包含在最后的结果中 ---> 外连接
>- 左外连接: 当where e.deptno=d.deptno不成立的时候，等号左边的表任然被包含在最后的结果中
>  -  写法:where e.deptno=d.deptno(+)
>- 右外连接: 当where e.deptno=d.deptno不成立的时候，等号右边的表任然被包含在最后的结果中
>  - 写法:where e.deptno(+)=d.deptno

- eg:--按部门统计员工信息：部门号 部门名称  人数

- 1. 如果用普通连接查询
  2. 分析：
     1. 按部门统计---》查询出所有的部门

  ```sql lite
  select d.dname 部门号, d.dname 部门名称， count(e.empno) 部门人数
  from dept d, emp e
  where d.depto = e.deptno
  group by d.dname， d.dname
  --需要添加  group by d.dname， d.dname  -->显示中有多行函数 和 常字段，否则报错
  --根据员工表的 id 来查询数量，保证数据的准确性 
  ```

  1. 错误点：
     - 其实有四个部门，但是只显示出三个部门
     - 因为最后一个部门在员工表中没有员工，故漏掉
  2. 解决方法：外连接查询（左外连接/右外连接）

##### 左外链接

- oracle 格式：
  - 左外连接: 当where e.deptno=d.deptno不成立的时候，等号左边的表任然被包含在最后的结果中
    SQL>     写法:where e.deptno=d.deptno(+)

- 通用格式

  ```sql lite
  select * 
  from a表 left [outer] join b表 
  on 条件
  ```

- eg:

```sql lite
--oracle 格式
select d.dname 部门号, d.dname 部门名称， count(e.empno) 部门人数
from dept d, emp e
where d.depto = e.deptno(+)
group by d.dname， d.dname
```

##### 右外连接

- ORACLE 格式
  - 右外连接: 当where e.deptno=d.deptno不成立的时候，等号右边的表任然被包含在最后的结果中
    SQL>     写法:where e.deptno(+)=d.deptno

- 通用格式

  ```sql lite
  select *
  from 表a right [outer] join 表b 
  on 条件
  ```

##### 全外连接（oracle特有）

- eg:

```sql lite
select d.dname 部门号, d.dname 部门名称， count(e.empno) 部门人数
from dept d, emp e
where d.depto(+) = e.deptno
group by d.dname， d.dname
```



### *子查询

>SQL> 注意的问题：1
>SQL> 1、括号
>SQL> 2、合理的书写风格
>SQL> 3、可以在主查询的where select having  from 后面使用子查询
>SQL> 4、不可以在group by使用子查询
>SQL> 5、强调from后面的子查询
>SQL> 6、主查询和子查询可以不是同一张表；只有子查询返回的结果 主查询可以使用 即可
>SQL> 7、一般不在子查询中排序；但在top-n分析问题中 必须对子查询排序
>SQL> 8、一般先执行子查询，再执行主查询；但相关子查询例外
>SQL> 9、单行子查询只能使用单行操作符；多行子查询只能使用多行操作符
>SQL> 10、子查询中的null

#### 按查询结果(*)

- 单行子查询
  - 常用操作符：> ,  >=,  <,   <=,  <>,  !=

  ```sql lite
  ----1---- 查询出 比 雇员7654的工资 高,同时 和7788从事相同工作 的员工信息(scott)
  --分析：
  --
  	select *
  	from emp
  	where sal > (雇员7654工资)
        	and job = （7788从事的工作）
  --
  --雇员7654工资
  select sal from emp where empno = 7654;
  --7788从事的工作
  select job from emp where empno = 7788;
  --结果
  select *
  from emp
  where sal > (select sal from emp where empno = 7654)
        and job = (select job from emp where empno = 7788);

  --============================================================================================
  --============================================================================================

  -----2--- 查询每个部门最低工资的员工信息 和 他所在的部门信息(scott)
                 
  --每个部门最低工资的员工信息
  select deptno, min(sal) from emp group by deptno;
  -- 查询每个部门最低工资的员工信息 和 他所在的部门信息     
  select *
  from dept d1, (select deptno, min(sal) from emp group by deptno) t1
  where d1.deptno = t1.deptno;

  ```

  ​

- 多行子查询

  - 常用操作符
    - in （多个值）
    - not in  (做非空限制)
    - any （或）
    - all  （且）
    - exists （是否有结果  true  false）

  ```sql lite

  -- 查询出比10号部门  任意 =any   员工薪资高的员工信息
  -- 1. 10号部门所有工资
  select sal from emp where deptno = 10;
  select * from emp where sal >any(select sal from emp where deptno = 10);
    -- 查询出比10号部门  所有 = all  员工薪资高的员工信息

    select * from emp where sal >all(select sal from emp where deptno = 10);

    -- 查询是领导的员工信息

    -- 1. 所有领导的编号

    select distinct mgr from emp;

    -- 2. 是领导的员工  任意一个满足 -->  ||  或的关系  

    select * from emp where empno =any(select distinct mgr from emp);

    select * from emp where empno in(select distinct mgr from emp);

    -- 不是领导的员工信息   所有都满足  --> ||  且的关系

    select * from emp where empno not in(select distinct mgr from emp where mgr is not null);

    select * from emp where empno <>all(select distinct mgr from emp);

    -- 注意: 子查询中包含空值

  ```



  ```

exists

- ```sql lite
  exists(查询语句): 存在

  如果查询语句,有结果,则返回true,否则返回false 

  in和exists 是可以替换去使用,如果数据量小,使用in高效, 如果数据量大,用exists效率高一些     

  select * from emp where exists(select * from emp where empno=1234567);
  select * from emp where 1=2;

  select * from emp where exists(select * from emp where empno=7369);
  select * from emp where 1=1;

  -- 查询有员工的部门信息
  select * from dept d where exists(select * from emp e where e.deptno=d.deptno);
  ```

#### 按查询语句位置

1. select   -->当 显示  值 用
2. from  -->  当表用---> 注意取别名
3. where   -->  当条件用  分 单行，多行
4. having



#### 关联子查询 与 非关联子查询

>关联子查询: 子查询依赖外层查询
>
>​			 先执行外层查询,然后再执行内层查询
>
>   非关联子查询: 子查询可以单独执行,不依赖外层查询条件
>
>​			 先执行子查询,再执行外层查询,内层子查询只执行一次  



### *分页查询

#### 伪列

- rownum  
- rowid： 伪列,代表的每行记录在磁盘中存放的真实的物理地址
  - 作用：   rowid,用在**索引查询** ，**去重** 

#### 分页

```sql lite
-- 查询第5-10条记录
-----先查询出前 10  条数据
select rownum, emp.*
from emp
where rownum <= 10;
-------再对查询出来的结果进行 条件判断
select *
from (select rownum line, emp.*
      from emp
      where rownum <= 10) t1
where line >= 5;
```



### 集合运算

- 交集：

  - intersect

- 并集 ：

- - union     自动去重，默认按照第一列 升序 排序
  - union all   不去重，不排序

- 差集

  - minus

- **注意事项**

  >1. 列的数量要一致
  >2. 列的类型要保持一致
  >3. 列的顺序要一致
  >4. 如果不足,可以用null补齐

  ​

  ​

### *sql 语句执行顺序

### 习题

- 课外习题
  - 练习一: 找到员工表中工资最高的前三名(只要前三条)

    ```sql lite
    --1+对工资进行排序
    select * from emp order by sal;
    --2+获取排序后的表中的前三名
    select rownum, t1.*
    from (select * from emp order by sal) t1
    where rownum < 4;
    ```

    ​


  - 练习二:找到员工表中薪水大于本部门平均薪水的员工

    ```sql lite
    -----各个部门的平均薪水
    select deptno, avg(sal) avg
    from emp
    group by deptno;

    select e1.*
    from emp e1, (select deptno, avg(sal) avg
                    from emp
                    group by deptno) t1
    where e1.deptno = t1.deptno
          and e1.sal > t1.avg;
    ```

    ​

  - 练习三:统计每年入职员工的个数  (结果竖起来)

    ```sql lite
    ----求出每个员工入职的年份
    select emp.*, to_char(hiredate, 'yyyy') yy
    from emp;

    ---求出每个年份对应的入职人数
    select yy, count(*) cc
    from (select emp.*, to_char(hiredate, 'yyyy') yy
                 from emp) t1
    group by yy;

    ----竖起来
    select   cc "Totol", 
             case yy when '1987' then cc end "1987",
             case yy when '1980' then cc end "1980",
             case yy when '1982' then cc end "1982",
             case yy when '1981' then cc end "1981"
    from (select yy, count(*) cc
          from (select emp.*, to_char(hiredate, 'yyyy') yy
                from emp) t1
          group by yy) t1;
          
    ---去掉空格   
    select   sum(cc) "Totol", 
             sum(case yy when '1987' then cc end) "1987",
             sum(case yy when '1980' then cc end) "1980",
             sum(case yy when '1982' then cc end) "1982",
             sum(case yy when '1981' then cc end) "1981"
    from (select yy, count(*) cc
          from (select emp.*, to_char(hiredate, 'yyyy') yy
                from emp) t1
          group by yy) t1;
    ​```
    ```


  - 华为扩展：-- 使用一条语句删除表中重复的记录, 只保留rowid最小的记录 

    ```sql lite
    ------------------------------方式一
    -- 1.分组查询出所有最小的rowid
    select pname,min(rowid) from pp group by pname;

    -- 2. 删除rowid不在范围内数据
    delete from pp where rowid not in(select min(rowid) from pp group by pname);
    ------------------------方式二----------------------
    delete from pp p1 where rowid > (select min(rowid) from pp p2 where p2.pname=p1.pname);
    ```


- 第二天习题

  ```sql lite

  --1.列出至少有三个员工的所有部门和部门信息。
  select e1.deptno, count(*)
  from dept d1, emp e1
  group by e1.deptno
  having count(*) > 2;

  -------------加上部门信息
  select *
  from dept  d1, (select e1.deptno, count(*)
                  from dept d1, emp e1
                  group by e1.deptno
                  having count(*) > 2) t1
   where t1.deptno = d1.deptno;
  ```


  --2.列出受雇日期早于直接上级的所有员工的编号，姓名，部门名称
  --------------员工的受雇时间  <  经理的受雇时间
  ---------------每个员工对应的直接上级
  select empno, ename, mgr, hiredate
  from emp e1
  --------------每个员工对应的时间，及直接上级对应的时间
  select e1.*, e1.hiredate h1,e2.hiredate h2
  from emp e1,emp e2
  where e1.mgr = e2.empno ;
​       
  select *
  from (select e1.*, e1.hiredate h1,e2.hiredate h2
          from emp e1,emp e2
          where e1.mgr = e2.empno)
  where h1 < h2;

  --3.列出职位为“CLERK”的姓名和部门名称，部门人数：
  ---------------1.查出 job 为 “CLERK”的员工
  select * from emp where job = 'CLERK';
  -----------------2.查出这些员工对应的部门的信息
  select *
  from (select * from emp where job = 'CLERK') t1, dept d1
  where d1.deptno = t1.deptno;

  --4.列出和“SCOTT”从事相同工作的所有员工及部门名称：

  --------------1.找出‘SCOTT’ 的工作
  select job from emp where ename = 'SCOTT';
  -----------------2.找出 和 SCOTT 工作相同的员工
  select *
  from emp
  where job = (select job from emp where ename = 'SCOTT');
  ---------------3.找出 和 SCOTT 工作相同的员工 对应的部门信息
  select * 
  from (select *
        from emp
        where job = (select job from emp where ename = 'SCOTT')) t1, dept d1
  where t1.deptno = d1.deptno;

  --5.列出每个部门工作的员工数量、平均工资和平均服务期限（单位为年）
  ----------每个部门工作的员工数量、平均工资
  select deptno, count(*), avg(sal)
  from emp e1
  group by deptno;
  ----------每个部门工作全部时间
  select sum(months_between(sysdate,hiredate)/12),deptno, count(*)
  from emp e1
  group by deptno;
  ----------每个部门工作全部时间 /  部门人数  (未取整)
  select deptno , count(*) , avg(sal) ,
            sum(months_between(sysdate,hiredate)/12) / count(*) 
  from emp e1
  group by deptno;

  select e1.deptno , count(*) , avg(sal) ,
           avg( months_between(sysdate,hiredate)/12) 
  from emp e1
  group by deptno;

  --6、列出各个部门的MANAGER 的最低薪金：
  -------找出所有的 职位为   MANAGER   的员工
  select * from emp where job = 'MANAGER';
  ----------对经理人按部门进行分组 求最小sal
  select t1.deptno, min(sal)
  from (select * from emp where job = 'MANAGER') t1
  group by t1.deptno;

  --7、给任职日期超过10年的人加薪10%；
  ----------找出任期超过 10  年的员工
  select e1.*, months_between(sysdate, e1.hiredate) / 12
  from emp e1
  where (months_between(sysdate, e1.hiredate) / 12)  >  10;
  ----------给任职日期超过10年的人加薪10%；
  select t1.*, sal*1.1
  from (select *
        from emp e1
        where (months_between(sysdate, e1.hiredate) / 12)  >  10) t1;

  -------7-错--->更新

  --8,查询出和SCOTT工资一样的员工信息
  ------------查出SCOTT 的工资
  select sal
  from emp e1
  where e1.ename = 'SCOTT';
  ----------------查询出和SCOTT工资一样的员工信息
  select * 
  from emp 
  where sal = (select sal
                from emp e1
                where e1.ename = 'SCOTT');

  --9,查询出比SCOTT工资高的员工信息
  ------------查出SCOTT 的工资
  select sal
  from emp e1
  where e1.ename = 'SCOTT';
  ----------------查询出比SCOTT工资高的员工信息
  select * 
  from emp 
  where sal > (select sal
                from emp e1
                where e1.ename = 'SCOTT');
   --10.查询出不是领导的员工
   ----------查询出是领导员工的编号(注意 去重)
   select distinct e2.empno
   from emp e1, emp e2
   where e2.empno = e1.mgr;            
   ----------询出不是领导的员工  （编号 不在 领导编号中）
   select * 
   from emp
   where empno != all( select distinct e2.empno
                         from emp e1, emp e2
                         where e2.empno = e1.mgr);   
  ---11,查询出平均工资高于2000的部门编号和该部门平均工资                                    
  select deptno, avg(sal)
  from emp
  group by deptno
  having avg(sal) > 2000;

  --12,查询出平均工资高于2000的部门名称和该部门平均工资
  select d1.*, t1.平均工资
  from dept d1, (select deptno, avg(sal) 平均工资
                    from emp
                    group by deptno
                    having avg(sal) > 2000) t1
  where d1.deptno = t1.deptno;

  --13,查询出有员工的部门【数据量大的时候用exists效率非常高】

  select *
  from dept d1
  where exists (select * from emp e1 where e1.deptno = d1.deptno);

  --14,找到员工表中薪水大于本部门平均工资的员工。
  select e1.*
  from emp e1, (select deptno, avg(sal) avgSal from emp group by deptno) t1
  where e1.sal > t1.avgSal and e1.deptno = t1.deptno;

  --15,统计每年入职的员工个数
  --------获取每个员工入职的年份
  select to_char(hiredate, 'yyyy') yy
  from emp;
  ----------------获取每个员工入职的年份------对年份进行分组求和
  select t1.yy, count(*)
  from (select to_char(hiredate, 'yyyy') yy
              from emp) t1
  group by t1.yy;

  ---16,查询出emp表中工资在第六和第十之间的数据oracle中的分页查询【rownum】
  select rownum, emp.* 
  from emp 
  where rownum <=10;

  select rownum, t1.*
  from (select rownum line, emp.* 
                from emp 
                where rownum <10) t1
  where t1.line > 5;

  ---17,统计薪资大于薪资最高的员工所在部门的平均工资和薪资最低的员工所在部门的平均工资的平均工资的员工信息。
  ------最高的薪资
  select max(sal) from emp; 
  -----最高的薪资的对应的员工
  select * 
  from emp
  where sal = (select max(sal) from emp);
  -----最高的薪资的对应的员工所在的部门
  select d1.deptno
  from dept d1, (select * 
                  from emp
                  where sal = (select max(sal) from emp)) t1
  where d1.deptno = t1.deptno;
  -----最高的薪资的对应的员工所在的部门 的平均工资
  select avg(sal)
  from emp
  where deptno = (select d1.deptno
                  from dept d1, (select * 
                                  from emp
                                  where sal = (select max(sal) from emp)) t1
                  where d1.deptno = t1.deptno)
  group by deptno;
  -----最低的薪资的对应的员工所在的部门 的平均工资
  select avg(sal)
  from emp
  where deptno = (select d1.deptno
                  from dept d1, (select * 
                                  from emp
                                  where sal = (select min(sal) from emp)) t1
                  where d1.deptno = t1.deptno)
  group by deptno;
  -----最高部门与最低部门的平均工资
  select ( (select avg(sal)
            from emp
            where deptno = (select d1.deptno
                            from dept d1, (select * 
                                            from emp
                                            where sal = (select max(sal) from emp)) t1
                            where d1.deptno = t1.deptno)
            group by deptno) 
            + 
            (select avg(sal)
            from emp
            where deptno = (select d1.deptno
                            from dept d1, (select * 
                                            from emp
                                            where sal = (select min(sal) from emp)) t1
                            where d1.deptno = t1.deptno)
            group by deptno) )/2
  from dual; 
  -----大于  最高部门与最低部门的平均工资  的员工
  select * 
  from emp
    where sal > (select ( (select avg(sal)
                          from emp
                          where deptno = (select d1.deptno
                                          from dept d1, (select * 
                                                          from emp
                                                          where sal = (select max(sal) from emp)) t1
                                          where d1.deptno = t1.deptno)
                          group by deptno) 
                        + 
                        (select avg(sal)
                        from emp
                        where deptno = (select d1.deptno
                                        from dept d1, (select * 
                                                        from emp
                                                        where sal = (select min(sal) from emp)) t1
                                        where d1.deptno = t1.deptno)
                        group by deptno) )/2
            from dual);
  --18,查询部门名称不是research,职位是manager,且薪资大于平均薪资的员工(包含ename hiredate loc三个字段)
  ------部门 research  的部门编号
  select deptno
  from dept
  where dname = 'RESEARCH';
  ------部门编号 不为 20  的 员工
  select * 
  from emp
  where deptno != (select deptno
                    from dept
                    where dname = 'RESEARCH');
  ------职位是manager的员工
  select *
  from emp
  where job = 'MANAGER';

  ------薪资大于平均薪资的员工
  select *
  from emp
  where sal > (select avg(sal) from emp);
  ----------取交集
  select *
  from emp
  where sal > (select avg(sal) from emp)
        intersect 
  select *
  from emp
  where job = 'MANAGER'
        intersect 
  select * 
  from emp
  where deptno != (select deptno
                    from dept
                    where dname = 'RESEARCH');

   ----1---- 查询出 比 雇员7654的工资 高,同时 和7788从事相同工作 的员工信息
  select *
  from emp
  where sal > (雇员7654工资)
        and job = （7788从事的工作）
  --雇员7654工资
  select sal from emp where empno = 7654;
  --7788从事的工作
  select job from emp where empno = 7788;
  --结果
  select *
  from emp
  where sal > (select sal from emp where empno = 7654)
        and job = （select job from emp where empno = 7788）

  -----2--- 查询每个部门最低工资的员工信息 和 他所在的部门信息                 
​                    

  select * from dept;
  select * from emp;



  -- 有两个以上直接下属的员工信息

  ```



###  *DDL表结构管理

​		

​```plsql
--查询所有用户的表的，视图等
select * from all_tab_comments;
--查询当前用户的所有的表，视图等
select * from user_tab_comments;
--查询所有的用户的表的列名和注释
select * from  all_col_comments;
--查询当前用户表的列名和注释
select * from user_col_comments;
--查询所有用户的表的列的结构信息（没有备注）
select * from  all_tab_columns;
--查询当前用户表的列的结构信息（没有备注）
select * from user_tab_columns;
  ```




- oracle数据库的体系

  - orcl数据库可以创建多个表空间
  - 一个表空间可以创建多个用户
  - 一个用户可以建立多个表

- mysql数据库的体系

  - 一个用户中可以创建多个数据库
  - 一个数据库可以创建多张表

- 创建表空间

  ```sql lite
  --创建表空间
  --指定表空间  --home
  create tablespace home
  --指定表空间对应的数据文件
  datafile 'c:/oracle/guozicheng.dbf'
  --指定表空间的初始大小
  size 100m
  --存储空间自动增长---当表空间存储空间占满时，自动增长  ‘10M’  
  autoextend on 
  next 10m;
  ```


- 创建用户

  ```sql lite
  ---创建用户
  create user guozi
  --设置密码
  identified by "123456"
  --设置对应的表空间
  default tablespace home;
  --1.没有登录权限
  --2.此时创建的用户没有任何权限
  -------------------------------------------------------------------------------------
  --oracle 数据库 与 其他数据库的区别在于，表和其他的数据库对象都是存储在用户下的
  ```

- ​

- ​

- 给用户赋权限

  ```sql lite
  --设置用户登录权限
  -- 用户授权登录 : grant 
  grant create session to zhangsan;
  -- 取消授权登录
  revoke create session from zhangsan;
  -------------------------------------------------------
  --直接赋予用户该角色，拥有给角色的所有权限
  grant dba to guozi;
  --进入system下给用户guozi赋予 DBA权限 
  ```

  - oracle 中存在 三个重要的角色，都有各自不同的权限
  - - connect角色
      - 是授予最终用户的典型权利，最基本的
    - resource角色
    - - 授予开发人员的，权限更高
    - DBA角色
    - - 拥有全部特权，系统最高权限。
      - 只有DBA才有权限创建 数据库结构
      - 系统权限也需要通过DBA授权出去
      - DBA可以操作全体用户的任意基表，包括删除

- 数据类型

  ```sql lite
  --数据类型
  varchar2(size)   --->可变长度字符
  char()           --->不可变长度字符
    ---eg：
          name varchar2（20）  
               填入的字符最长为20，
               当不足20时长度就是实际填写长度
          name char（20）      
               填入的字符最长为20，
               当不足20时长度数据库自动填充空格到20
        
  number(总长度s,小数长度p)--->  数字  
              -->小数长度不能超过总长度 number(5,2)
  date ---> 年月日时分秒     2017/10/22 15:08:40     
             --->注：在 mysql 中
                -- date 年月日  
                -- datetime  年月日时分秒
                -- timestamp  时间戳
  timestamp --> 22-OCT-17 03.09.31.253000 PM +08:00

  Long : -->长字符串, 最大可存2G
             
  CLOB : Character  Large Object ---> 字符大对象  最大4G
             
  BLOB : Binary Large Object -->  二进制大对象  4G 
  ```

  ​

- 创建表

  ```sql lite
  --创建表--方式一
  create table gzc(
         g_id number(10) primary key,
         g_name varchar2(20),
         g_age varchar2(20)
  )
  ------------------------------------------------------------------
  --创建表--方式二：使用子查询创建表
  --将查找的整个表创建到当前用户下，相当于是复制表

  --创建出来的表结构与 Scott 下的emp 表结构一样
  --表中数据与查询出来的数据一样（全部数据）
  create table emp as select * from scott.emp;

  --创建出来的表结构与 Scott 下的emp 表结构一样
  --表中数据与查询出来的数据一样（符合deptno = 10  数据）
  create table emp as select * from scott.emp where deptno = 10;

  -- 复制表, 只要结构,不要数据
  create table emp1 as select * from scott.emp where 1=2;

  ```

  ​

- 修改表

- - 使用 alter 修改表
  - 添加列：alter table  表名  add（列名一  数据类型，  列名二  数据类型， ...）
  - 重定义列：alter   table  表名  modify   （列名一  数据类型，  列名二  数据类型， ...）
  - 删除列： alter  table  表名  drop  column  列名
  - 修改列名： alter table 表名 rename  column  原列名 to  新列名
  - 修改表名：rename person to person2;

- 删除表： drop table 表名

  - 注意：oracle中删除表后，表就在用户的回收站中，课题闪现回（恢复表）
    - 但是管理员是没有回收站的，所以管理员删除表要非常小心

- 表的约束（五大约束）

  - 约束分为：列约束与表约束
  - 使用约束一般都会自己取名，便于出错查找。不取名默认一个约束名 
    - 列名  数据类型  constaraint   约束名   约束
  - primary  key  主键约束   ---->非空唯一
  - unique     唯一约束         ---->唯一可以为空
  - not null   非空约束           --->不能为空
  - check                check（条件）检查约束 ---->用来约束字段值的合法性
  - foreign  key 外键约束          ---->约束从表中的记录，必须是主表中的记录

  >当设置了外键，想要删除主表中的数据的方法：
  >
  >- 方式一：先取消外键，在删除
  >- - ​
  >- 方式二：强制删除--->先取消外键，再删除，一步到位，，慎用
  >  - drop table category cascade constraint;

  ​

### **DML数据管理

- 插入记录

  - 标准格式：insert into 表名（列1， 列2， 列3， ...） values （值1， 值2，值3， ...）；
  - 简单格式：insert into 表名  values （值1， 值2，值3， ...）；（不推荐）
  - 注意：
    - 使用标准写法，列与值一一对应
    - 使用简单写法，必须按照表中字段的顺序来插入值，入到为空的字段用null代替

- 更新记录

  - 全局修改：update 表名  set 列名1=值1， 列名2=值2， ...
  - 局部修改：update 表名  set 列名1=值1， 列名2=值2， ...  where  修改的条件

- 删除记录

  - ​
  - delete from 表名 where 删除条件
  - truncate table 
  - 注意：
    -   delete 和 truncate 区别?
      - delete 是逐条删除，删除所有数据的花效率较低  /  属于 DML数据管理  / 支持事务
      - truncate是先将整个表删除 , 再创建一个一模一样的空表，删除所有数据的花效率较高 /  属于DDL表结构 /不支持事务

- 事务

  - 一组SQL操作，要么全部成功，要么全部失败

  - 事务的特性 【ACID】

    - 原子性：事务不可分割
    - 一致性；事务前后，总值不变
    - 隔离性：两个事务之间互不影响--》隔离级别
    - 持久性：事务提交后，数据永久存在于磁盘中

  - 不考虑隔离级别产生的问题

    - 脏读 ： 一组事务读取到另一组事务 没有提交的数据
    - 不可重复读： 一组事务多次读取，结果不一致（update）
    - 虚度 / 幻读： 一组事务多次读取，结果不一致（insert）

  - 隔离级别

    - oracle
      - READ COMMITTED 默认的     
      - SERIALIZABLE
      - READ ONLY 只读
    - mysql
      - READ UNCOMMITTED 脏读  不可重读  虚读
      - READ COMMITTED 不可重读  虚读
      - REPEATABLE READ 虚读
      - SERIALIZABLE    串行化 

  - - ​

  - oracle 中的事务提交与回滚

    - commit

    - rollback

    - 事务的回滚点，主要用于数据的迁移

      - savepoint   回滚点的名称

      - rollback  to   回滚点的名称

        ```plsql
        --事务回滚点的操作
        --当报错后，事务回滚到回滚到回滚点，回滚点之前的数据依然有效，
        declare

        begin  -- 业务
          insert into lou values(1);
          insert into lou values(2);
          insert into lou values(3);
          savepoint aa;
          
          insert into lou values(4);
          --插入的值非空唯一，此处报错
          insert into lou values(4);
          insert into lou values(6);
          commit;
        exception  -- 捕获异常
          when others then
            rollback to aa;
            commit;
        end;
        ```

### 数据库对象

- 视图

- - 简介
    - 针对查询，不建议通过视图进行 delete update  insert ，因为通过视图进行DML操作，三种不同的操作分别有有不同的限制条件
    - 封装复杂的查询语句，将查询结果形成一张虚表，这张虚表就称为视图
    - 视图本身不存储任何数据，所有的数据都存放原来的表中
    - ​
    - 作用
      - 当一张表中含有一个人需要的数据，也含有敏感的数据时，采用视图，将敏感数据进行隐藏起来，视图中只展现需要的数据（屏蔽敏感细节）
      - 封装复杂的查询语句 
    - 创建视图的语法
        - 创建或替换
      - create [or replace] view 视图名称 as 查询语句 [with read only]
      - [with read only]--->显示此视图只读，不能修改

  ```plsql
  -- 封装查询语句 10号部分所有员工

  create or replace view view_test1 as select * from emp where deptno = 10;
  select * from view_test1;

  -- 隐藏敏感信息（屏蔽表中细节）
  create or replace view view_test2 as select ename,job,mgr,hiredate from emp;
  select * from view_test2;

  -- 创建只读视图
  create or replace view view_test3 as select ename,job,mgr,hiredate from emp with read only;
  select * from view_test3;

  --with check option
  create view view_test4 as select * from emp where deptno = 10;
  --	--在不使用with  check  option 时  可以用视图插入任何数据
  --  --使用了with check option 后，只能插入deptno = 10;  的数据
  ```

- 同义词

  - 同义词就是别名，给表起的别名就叫别名，给视图起的别名就叫同义词

  - 通过同义词也可以进行一样的查询

  - ```plsql
    -- 创建同义词
    create synonym yuangong for view_test2;
    select * from yuangong;
    ```

- 序列

  - 一组有规律的数

  - 作用：

    - 主要用来模拟主键自动增长

  - 语法：

    - ```plsql
      create sequence 序列名称
               start with  从几开始
               increment by  每次递增多少
               minvalue n | nominvalue
               maxvalue n | nomaxvalue
               cycle | nocycle
               cache n; 
               
            nextval :下一值
            currval : 当前值 ,注意: 当前值必须是调用过一次nextval才能使用    
           
      -------------------------------------------------------------------------------------
      -- 创建一个序列: 1,3,5,7,9,1,3,5,7,9..........
      create sequence seq_test1
      start with 1
      increment by 2
      minvalue 1
      maxvalue 9
      cycle
      cache 3;

      select seq_test1.nextval from dual;
      select seq_test1.currval from dual;     

      ---------------------------------------------------------------------------------------
      -- 开发中常用写法(简写)
      create sequence seq_test2;

      select seq_test2.nextval from dual;

      insert into lou values(seq_test2.nextval);
      select * from lou;
               
      ```

      ​

- 索引

  - 相当是一本的书的目录， 一种已经排好顺序的数据结构 
  - sql优化
  - 作用: 提高查询效率, order by排序  
  - 弊端: 反向影响增删改的效率  
  - 实现原理
    - Btree : Balance Tree 平衡多路查找树  平衡多叉查找树（默认）
    - 位图
  - 创建索引的语法
    - create index 索引名称 on 表(列名1,列名2....);  
  - 什么情况需要创建索引 ？
  - 1. 数据量比较大
    2. 哪些列经常作为查询的条件 
    3. 排序的条件  
  - 什么情况不创建索引 ？
  - 1. 数据量比较小
    2. 某列经常做增删改的情况

### 数据的导入导出

- 使用cmd命令，整库导入导出
- - 整库导出
    - 默认备份名方式：exp system/用户名  full=y
      - full=y 参数表示整库导出
      - 命令完成后，在打开cmd目录下生成文件expdat.dmp 文件（默认）
    - (推荐)指定备份文件名称：exp system/用户名 file=c:\A\b.dmp   full=y   
      - 注意文件的路径的  ‘\’
  - 整库导入
    - 执行导入命令前，确保oracle 中无导入对象，否则报错：由于对象已存在下列语句失败
    - imp system/用户名  full=y
      - 此命令没有指定file参数，默认用备份文件expdat.dmp进行导入
    - imp system/用户名  full=y  file=c:\A\b.dmp  
- 使用CMD按用户进行导入及导出
  - 按用户导出
    - exp system/用户名  ower=用户名1   file=c:\A\b.dmp  
  - 按用户导入
    - imp   system/用户名   file=c:\A\b.dmp     fromuser=用户名
- 使用CMD按表导入导出
  - 按表导出
    - exp  用户名/itcast  file=c:\A\b.dmp    tables=t_person,t_student
    - 注：使用tables参数指定需要导出的表名，有多个的话，用逗号分开
  - 按表导入
    - imp 用户名/itcast   file=c:\A\b.dmp    tables=t_person,t_student
- 使用PLSQL developer 导出数据
  - 步骤看文档
  - 注意事项
    - PL/SQL包含三种方式导出ORACLE 表结构及数据
      - ORACLE EXPORT
        - 导出.dmp格式的文件，.dmp是二进制文件，可跨平台，还能包含权限，效率不错，用的最广泛
      - SQL INSERTS
        - 导出.sql格式文件，可用文本编辑器查看，使用小数据量导入导出
        - 注意此方式导入导出，，表中不能有大字段--【blob， clob, long】,否则会提示不能导出
      - PL/SQL Developer
        - 导出.pde格式文件，.pde为PL/SQL Developer自由的格式文件，只能用PL/SQL Developer工具导入导出，不能用文本编辑器查看

### plsql编程语言

- 什么是PL/SQL

  - PL/SQL 是ORACLE 对sql语言进行的一种过程化扩展。
    - 即在sql命令语言中增加了过程处理语句。例if判断   for循环
    - 将sql语言的数据操纵能力  与   过程语言的数据处理能力  相结合，使得PL/SQL面向过程。

- PL/SQL语法

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

- 变量及变量的定义

  - 说明变量

    - char, varchar2, darte, number, booolean, long
    - eg:
      - varl     char(15);
        - 声明变量，用分号结尾
      - married      boolean  :=  true
        - 初始化变量  使用  ‘:=’   进行赋值

  - 引用型变量

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

  - 记录型变量（将一整条数据赋值给变量（类似于对象））

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

      ​

  - if语句

    - 语法一

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



- - - 语法二

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

    - 语法三

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



- - 循环

    - 方式一：while循环

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



- - - 方式二

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

- - - 方式三：

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

