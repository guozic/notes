# tomcat的安裝

## 一、下载zip压缩包

- 下载 [地址](http://tomcat.apache.org/download-80.cgi)
- 下载操作系统对应的版本的zip文件

## 二、解压

- 至安装目录

## 三、配置系统环境变量

- TOMCAT_HOME = C:\software\installation\ApacheTomcat\apache-tomcat-9.0.4
- CATALINA_BASE = C:\software\installation\ApacheTomcat\apache-tomcat-9.0.4
- CATALINA_HOME = C:\software\installation\ApacheTomcat\apache-tomcat-9.0.4
- PATH = ;%TOMCAT_HOME%\bin
- CLASSPATH = ;%TOMCAT_HOME%\bin


## 四、修改配置文件

- C:\software\installation\ApacheTomcat\apache-tomcat-9.0.4\bin  文件下    startup.bat文件中添加

  ```basic
  setlocal
  #-----start---------------------------------------------------#
  #jdk 安装路径
  SET JAVA_HOME=C:\software\installation\jdk_1.8\jdk
  Tomcat解压放置的路径
  SET CATALINA_HOME=C:\software\installation\ApacheTomcat\apache-tomcat-9.0.4 
  #-------end--------------------------------------------------#
      rem Guess CATALINA_HOME if not defined
        set "CURRENT_DIR=%cd%"
          if not "%CATALINA_HOME%" == "" goto gotHome
            set "CATALINA_HOME=%CURRENT_DIR%"
              if exist "%CATALINA_HOME%\bin\catalina.bat" goto okHome
                cd ..
                set "CATALINA_HOME=%cd%"
                  cd "%CURRENT_DIR%"
  ```

  ​


## 五、启动

- 运行C:\software\installation\ApacheTomcat\apache-tomcat-9.0.4\bin 目录下双击startup.bat
- 访问  http://localhost:8080/  出现Tomcat猫即可