# Linux

## rz/sz 安装及 使用

- >如下操作如果遇到权限问题，请切换到root账号下进行
  >
  >- 下载：
  >
  >```
  >wget http://www.ohse.de/uwe/releases/lrzsz-0.12.20.tar.gz1
  >```
  >
  >- 解压：
  >
  >```
  >tar zxvf lrzsz-0.12.20.tar.gz1
  >```
  >
  >- make：
  >
  >```
  >cd lrzsz-0.12.20
  >./configure && make && make install12
  >```
  >
  >安装过程默认把lsz和lrz安装到了/usr/local/bin/目录下，现在我们并不能直接使用，下面创建软链接，并命名为rz/sz
  >
  >- 设置软链接：
  >
  >```
  >cd /usr/bin
  >sudo ln -s /usr/local/bin/lrz rz
  >sudo ln -s /usr/local/bin/lsz sz123
  >```
  >
  >- yum安装：
  >
  >```
  >yum install -y lrzsz
  >```

  ​

## jdk安装

### 方法一：解压压缩包，设置环境变量

- >​
  >
  >1.在/usr/目录下创建java目录
  >
  >```
  >[root@localhost ~]# mkdir/usr/java
  >
  >[root@localhost ~]# cd /usr/java
  >
  >```
  >
  >2.下载jdk,然后解压
  >
  >```
  >[root@localhost java]# curl -O http://download.Oracle.com/otn-pub/java/jdk/7u79-b15/jdk-7u79-linux-x64.tar.gz 
  >
  >[root@localhost java]# tar -zxvf jdk-7u79-linux-x64.tar.gz
  >
  >```
  >
  >3.设置环境变量
  >
  >```
  >[root@localhost java]# vi /etc/profile
  >```
  >
  >在profile中添加如下内容:
  >
  >```
  >#set java environment
  >
  >JAVA_HOME=/usr/java/jdk1.7.0_79
  >
  >JRE_HOME=/usr/java/jdk1.7.0_79/jre
  >
  >CLASS_PATH=.:JAVA_HOME/lib/dt.jar:JAVA_HOME/lib/tools.jar:$JRE_HOME/lib
  >
  >PATH=PATH:JAVA_HOME/bin:$JRE_HOME/bin
  >
  >export JAVA_HOME JRE_HOME CLASS_PATH PATH
  >
  >```
  >
  >让修改生效:
  >
  >```
  >[root@localhost java]# source /etc/profile
  >```
  >
  >4.验证JDK有效性
  >
  >```
  >[root@localhost java]# java -version
  >
  >java version "1.7.0_79"
  >
  >Java(TM) SE Runtime Environment (build 1.7.0_79-b15)
  >
  >Java HotSpot(TM) 64-Bit Server VM (build 24.79-b02, mixed mode)
  >
  >```

  ​

### 方法二：用yum安装JDK

- >1.查看yum库中都有哪些jdk版本(暂时只发现了openjdk)
  >
  >```
  >[root@localhost ~]# yum search java|grep jdk
  >
  >ldapjdk-javadoc.x86_64 : Javadoc for ldapjdk
  >
  >java-1.6.0-openjdk.x86_64 : OpenJDK Runtime Environment
  >
  >java-1.6.0-openjdk-demo.x86_64 : OpenJDK Demos
  >
  >java-1.6.0-openjdk-devel.x86_64 : OpenJDK Development Environment
  >
  >java-1.6.0-openjdk-javadoc.x86_64 : OpenJDK API Documentation
  >
  >java-1.6.0-openjdk-src.x86_64 : OpenJDK Source Bundle
  >
  >java-1.7.0-openjdk.x86_64 : OpenJDK Runtime Environment
  >
  >java-1.7.0-openjdk-demo.x86_64 : OpenJDK Demos
  >
  >java-1.7.0-openjdk-devel.x86_64 : OpenJDK Development Environment
  >
  >java-1.7.0-openjdk-javadoc.noarch : OpenJDK API Documentation
  >
  >java-1.7.0-openjdk-src.x86_64 : OpenJDK Source Bundle
  >
  >java-1.8.0-openjdk.x86_64 : OpenJDK Runtime Environment
  >
  >java-1.8.0-openjdk-demo.x86_64 : OpenJDK Demos
  >
  >java-1.8.0-openjdk-devel.x86_64 : OpenJDK Development Environment
  >
  >java-1.8.0-openjdk-headless.x86_64 : OpenJDK Runtime Environment
  >
  >java-1.8.0-openjdk-javadoc.noarch : OpenJDK API Documentation
  >
  >java-1.8.0-openjdk-src.x86_64 : OpenJDK Source Bundle
  >
  >ldapjdk.x86_64 : The Mozilla LDAP Java SDK
  >
  >```
  >
  >2.选择版本,进行安装
  >
  >```
  >//选择1.7版本进行安装
  >
  >[root@localhost ~]# yum install java-1.8.0-openjdk
  >
  >//安装完之后，默认的安装目录是在: /usr/lib/jvm//usr/lib/jvm/java-1.8.0-openjdk-1.8.0.161-0.b14.el7_4.x86_64
  >
  >
  >```
  >
  >3.设置环境变量
  >
  >```
  >[root@localhost ~]# vi /etc/profile
  >```
  >
  >在profile文件中添加如下内容
  >
  >```
  >#set java environment
  >JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.161-0.b14.el7_4.x86_64
  >JRE_HOME=$JAVA_HOME/jre
  >CLASS_PATH=.:JAVA_HOME/lib/dt.jar:JAVA_HOME/lib/tools.jar:$JRE_HOME/lib
  >PATH=PATH:JAVA_HOME/bin:$JRE_HOME/bin
  >export JAVA_HOME JRE_HOME CLASS_PATH PATH
  >```
  >
  >让修改生效
  >
  >```
  >[root@localhost java]# source /etc/profile
  >```
  >
  >4.验证(同上一方法)

## 安装Tomcat 8

### 方法一：压缩包解压安装

- >去http://tomcat.apache.org/download-80.cgi下载Tomcat8的安装文件apache-tomcat-8.0.26.tar.gz。 
  >
  >将apache-tomcat-8.0.26.tar.gz文件放到/usr/local目录下，执行如下脚本：   
  >
  >```
  ># cd /usr/local    
  ># tar -zxvf apache-tomcat-8.0.26.tar.gz // 解压压缩包    
  ># rm -rf apache-tomcat-8.0.26.tar.gz.tar.gz // 删除压缩包    
  ># mv apache-tomcat-8.0.26 tomcat
  >```
  >
  >**启动Tomcat8**
  >
  >```
  ># /usr/local/tomcat/bin/startup.sh //启动tomcat
  >Using CATALINA_BASE:   /usr/local/tomcat    
  >Using CATALINA_HOME:   /usr/local/tomcat    
  >Using CATALINA_TMPDIR: /usr/local/tomcat/temp    
  >Using JRE_HOME:        /usr/java/jdk1.8.0_60    
  >Using CLASSPATH:       /usr/local/tomcat/bin/bootstrap.jar:/usr/local/tomcat/bin/tomcat-juli.jar    
  >Tomcat started.
  >```
  >
  >​
  >
  >**如果出现下面错误：**
  >
  >```
  >Neither the JAVA_HOME nor the JRE_HOME environment variable is defined   
  >At least one of these environment variable is needed to run this program
  >```
  >
  >**则要注意提前设置java路径**
  >
  >在apache-tomcat-8.0.26/bin/setclasspath.sh中添加一下内容
  >
  >```
  >export JAVA_HOME=/usr/java/jdk1.8.0_60   
  >export JRE_HOME=/usr/java/jdk1.8.0_60/jre    
  >export CLASSPATH=.:JAVA_HOME/lib:JRE_HOME/lib:$CLASSPATH    
  >export PATH=JAVA_HOME/bin:JRE_HOME/bin:$PATH
  >```
  >
  >**防火墙开放8080端口**
  >
  >增加8080端口到防火墙配置中，执行以下操作： 
  >
  >```
  ># vi /etc/sysconfig/iptables
  >#增加以下代码   
  >-A RH-Firewall-1-INPUT -m state --state NEW -m tcp -p tcp --dport 8080 -j ACCEPT
  >```
  >
  >**重启防火墙** 
  >
  >```
  ># service iptables restart
  >```
  >
  >**检验Tomcat8安装运行**
  >
  >通过以下地址查看tomcat是否运行正常：   
  >http://192.168.11.52:8080/    
  >看到tomcat系统界面，说明安装成功！
  >
  >**停止Tomcat8**
  >
  >```
  >#  /usr/local/tomcat/bin/shutdown.sh   //停止tomcat
  >```

