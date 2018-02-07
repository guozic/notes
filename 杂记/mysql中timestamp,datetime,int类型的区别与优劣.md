# mysql中timestamp,datetime,int类型的区别与优劣

原创 2013年09月16日 16:15:54

- 标签：
- [timestamp](http://so.csdn.net/so/search/s.do?q=timestamp&t=blog) /
- [datetime](http://so.csdn.net/so/search/s.do?q=datetime&t=blog) /
- [mysql](http://so.csdn.net/so/search/s.do?q=mysql&t=blog) /
- [timezone](http://so.csdn.net/so/search/s.do?q=timezone&t=blog)


- **18000

转载请注明来自[souldak](http://blog.csdn.net/souldak)，微博:[@evagle](http://weibo.com/souldak)
以下内容**整合筛选**自互联网：
int
\1. 占用4个字节
\2. 建立索引之后，查询速度快
\3. 条件范围搜索可以使用使用between
\4. 不能使用mysql提供的时间函数
结论：适合需要进行大量时间范围查询的数据表
datetime
\1. 占用8个字节

\3. 实际格式储存（Just stores what you have stored and retrieves the same thing which you have stored.）

\4. 与时区无关（It has nothing to deal with the TIMEZONE and Conversion.）

\5. 可以在指定datetime字段的值的时候使用now()变量来自动插入系统的当前时间。

结论：datetime类型适合用来记录数据的原始的创建时间，因为无论你怎么更改记录中其他字段的值，datetime字段的值都不会改变，除非你手动更改它。

timestamp
\1. 占用4个字节

4.值以UTC格式保存（ it stores the number of milliseconds）

5.时区转化 ，存储时对当前的时区进行转换，检索时再转换回当前的时区。

\7. 数据库会自动修改其值，所以在插入记录时不需要指定timestamp字段的名称和timestamp字段的值，你只需要在设计表的时候添加一个timestamp字段即可，插入后该字段的值会自动变为当前系统时间。

\8. 默认情况下以后任何时间修改表中的记录时，对应记录的timestamp值会自动被更新为当前的系统时间。

\9. 如果需要可以设置timestamp不自动更新。通过设置DEFAULT CURRENT_TIMESTAMP 可以实现。

修改自动更新：
`field_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE
修改不自动更新
`field_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP 

结论：timestamp类型适合用来记录数据的最后修改时间，因为只要你更改了记录中其他字段的值，timestamp字段的值都会被自动更新。（如果需要可以设置timestamp不自动更新）