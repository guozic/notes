# Markdown的简单使用

## 一.概述

### 0.创始人简介

- 创始人


- 创造灵感来源

- >最大灵感来源于 纯文本电子邮件的格式,借鉴其他语法


### 1.Markdown简介

- 简介
  - mark down   标记写下
  - Markdown的语法有一些特殊的符号组成


- 目标


- >让写作更方便

- 特点

  >- 所见即所得，易读易写易改。即可读性强，编写修改方便。
  >- 解决了写作中排版麻烦的问题

  ​

### 2.与HTML之间

- 格式上的区别

  - HTML是一种发布式格式
  - MD是一种书写格式

- > md的语法格式只涵盖纯文本可以涵盖的范围，不在纯文本之间的标签克之间在文档里面编写HTML，且无需额外标注是HTML还是md.
  >
  > - 特例
  >
  >   - HTML块级标签内的md语法失效
  >
  >   - >HTML的区块元素 例如：<div> <table> <p> 等等标签，在其前后需要加上空行，与其他内容区隔开，且他们的开始与结束标签不能够用制表符 或空格来 缩进

- 特殊字符转化

## 二.工具的介绍

## 三.区块元素

### 1.段落

Typora自动解析段落

### 2.标题	

 * 类Setext()
* 类atx


* >- 格式： # 1-6个 分别表示标题一至标题六
  >
  >  - >- `# H1 #` 
  >    >
  >    >- `## H2 ##`
  >    >
  >    >- `### H3 ###`
  >    >
  >    >  ...
  >
  >- 格式简写：左边的# 可以省略
  >
  >  - >- `# H1 `
  >    >
  >    >- `## H2 `
  >    >
  >    >- `### H3 `
  >    >
  >    >  ...

### 3.区块引用

- >- 格式： >
  >
  >  - > `>`符号前面不能有字符或三个以上的空格，否则语法失效
  >
  >- 区块引用可以嵌套使用
  >
  >- 区块应用内可以使用md语法

### 4.列表

- >无序列表
  >
  >- >格式：  * 或 + 或 -  
  >
  >  ​

- >有序列表
  >
  >- >格式：  数字.
  >
  >  ​

### 5.代码区块

- >```
  >
  >```
  >
  >​

### 6.生成目录大纲

```
[toc]
```

### 7.表情

:joy:

:joy_cat:

:black_joker:

...

### 8.生成勾选框

```
- []  选择按钮
```

- [x] ​

- []

  ​

### 10.数学相关

```
$$
```

$$
x+y2=1
$$

### 11.表格

```
|   |   |
```

|  h   |  h   |
| :--: | :--: |
|  2   |  2   |
|      |      |

## 

## 四.解释

- 不同的编辑器，其语法分析程序不同，故有的功能在不同的编辑器上语法不同
- Typora不支持HTML。HTML片段能被识别，但不会进行语法上的解析识别。

[^1]: 书写语言
[^a]: 编程语言
[^刀]: 扛把子