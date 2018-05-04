# docker

## 1 **Docker介绍**

### 1.1 docker是什么

- 云计算\云服务
- IAAS（基础设施即服务）、PAAS（平台即服务）、SAAS（软件即服务）
- Docker到底是什么呢？
  - Docker就是一种虚拟化容器技术。
  - 开发项目：
    - Tomcat
    - MySQL
- 通过Docker这种虚拟化技术，我们可以物理机的资源进行更加合理有效的利用，可以将一台物理机器虚拟化出很多个拥有完整操作系统，并且相互独立的“虚拟计算机”。
- 基于操作系统创建出一些相互独立的、功能虚拟化技术有多种实现方式，有基于硬件进行虚拟化的技术，而Docker只是针对操作系统进行虚拟化，对于硬件资源的使用率更低。
- 相对于VMware这种虚拟化技术，Docker拥有着显著的优势：
  1. 启动速度快，Docker容器启动操作系统在秒级就可以完成，而VMware却是达到分钟级。
  2. 系统资源消耗低，一台Linux服务器可以运行成千上百个Docker容器，而VMware大概只能同时运行10个左右。
  3. 更轻松的迁移和扩展，由于Docker容器比VMware占用更少的硬盘空间，在需要搭建几套软件环境的情况下，对安装好的Docker容器进行迁移会更快捷，更方便。而且Docker容器几乎可以在任意的平台上运行，包括虚拟机、物理机、公有云，私有云，个人电脑等，这种兼容性，可以让用户将一个应用程序从一个平台直接迁移到另一个平台。

### 1.2 Docker能做什么

- docker可以给开发人员和运维人员带来帮助。
- 项目拥有多套环境：开发环境、测试环境、预生产环境、生成环境。
- 环境（中间件，具体比如Tomcat、MySQL）不一致
- 使用Docker，开发人员只需要关注于软件应用开发，运维人员只需要关心如何管理容器。这样的话，就可以避免在以往开发环境中经常出现的环境不一致问题，比如开发人员在开发环境运行应用程序是正常的，但是同样的代码部署到生产环境就不行了，这种问题出现的最大原因就是环境的不一致造成的。而现在通过Docker容器，开发人员在开发&测试时使用的Docker容器环境，可以直接提交给运维人员，由运维人员在生产环境中，直接拿着开发人员搭建好的Docker容器，并在其中部署应用，避免开发环境和生产环境的不一致性。


- 当然，Docker的强大远远不至于此，而作为软件开发人员的我们，更关注的是如何利用它来搭建一套统一的软件开发环境。



**对比传统虚拟机总结**：

![img](file:///C:\Users\guozi\AppData\Local\Temp\ksohtml\wps6B7A.tmp.jpg) 

 

### 1.3 **D**ocker基本概念

- docker包含三个基本概念：
  -  镜像（Image）
  - 容器（Container）
  - 仓库（Repository）

 

- 理解了这三个概念，就理解了docker的整个生命周期了！

 

#### 1.3.1 镜像

- Docker镜像（Image）就是一个只读的模板。
- Docker镜像可以用来创建Docker容器。 
- Docker镜像和Docker容器的关系，类似于java中class类与对象之间的关系。


- Docker提供了一个很简单的机制来创建镜像或者更新已有的镜像，用户甚至可以直接从其他人那里下载一个已经做好的镜像来直接使用。

#### 1.3.2 容器

- Docker利用容器（Container）来运行应用。
- 容器是从镜像创建的运行实例。它可以被启动、开始、停止、删除。每个容器都是相互隔离的、保证安全的平台。 
- 可以把容器看成是一个简易版的Linux环境（包括ROOT用户权限、进程空间、用户空间、网络等）和运行在其中的应用程序。



#### 1.3.3 Registry

- Registry是集中存放镜像文件的场所。
- 仓库（Repository）是对于其中的镜像进行分类管理。

 

- 一个Registry中会有多个Repository。
- 一个Repository中会有多个不同tag的Image。

 

- 比如名称为centos的Repository仓库下，有tag为6或者7的Image镜像。

 

- Registry分为公有（public）和私有（private）两种形式。
- 最大的公有Registry是Docker Hub，存放了数量庞大的镜像供用户下载使用。
- 国内的公开Registry包括USTC、网易云、DaoCloud、AliCloud等，可以供大陆用户更稳当快捷的访问。
- 用户可以在本地创建一个私有Registry。



- 用户创建了自己的镜像之后就可以使用push命令将它上传的公有Registry或者私有Registry中，这样下次在另一台机器上使用这个镜像的时候，只需要从Registry上pull下来运行就可以了。

 

## 2 Docker安装

- 官方网站上有各种环境下的安装指南，比如：CentOS、Ubuntu和Debian系列的安装。

 

- 而我们现在主要讲解的是基于CentOS 7.x上面的安装。

 

- **注意：CentOS 7的安装比较耗费时间，所以不在本教程之内，但是资料中**已经准备了CentOS 7的安装镜像以及安装文档，课下可以参考着文档进行安装

 

![img](file:///C:\Users\guozi\AppData\Local\Temp\ksohtml\wps6B8C.tmp.jpg) 

 

- 安装好CentOS之后，需要通过SSH客户端工具访问CentOS操作系统，所以需要在CentOS的系统界面中查看IP地址，查看方式在7.x后，ipconfig命令被废除，使用**ip addr命令**。

 

- 一切完毕之后，只需要执行以下命令即可完成Docker的安装：

```shell
yum install docker

systemctl start docker

```



 

![img](file:///C:\Users\guozi\AppData\Local\Temp\ksohtml\wps6B8D.tmp.jpg) 

![img](file:///C:\Users\guozi\AppData\Local\Temp\ksohtml\wps6B8E.tmp.jpg) 

![img](file:///C:\Users\guozi\AppData\Local\Temp\ksohtml\wps6B9E.tmp.jpg) 

 

## 3 Docker镜像

### 3.1 拉取镜像

- 我们可以使用命令从一些公用仓库中拉取镜像到本地，下面就列举一些常用的公用仓库拉取镜像的方式！

 

#### 3.1.1 从docker hub拉取

- <https://hub.docker.com/>

 

- 下面的例子将从Docker Hub仓库下载一个CentOS 7版本的操作系统镜像。

 ```shell
docker pull centos:7
docker pull tomcat:8
## 8 表示版本，也是镜像的tab 可以去官网查对应的 tomcat 有哪些tab
 ```

 

这是docker默认的公用仓库，不过缺点是国内下载会比较慢。

![img](file:///C:\Users\guozi\AppData\Local\Temp\ksohtml\wps6B9F.tmp.jpg) 

 

#### 3.1.2 从ustc拉取（建议使用）

在宿主机器编辑文件：

 ```shell
vi /etc/docker/daemon.json
 ```

请在该配置文件中加入（没有该文件的话，请先建一个）：

```json
{
  "registry-mirrors": ["https://docker.mirrors.ustc.edu.cn"]
}
```

最后，需要重启docker服务

```
systemctl restart docker.service
```

之后再使用pull命令拉取镜像，这时候是从ustc获取镜像，而且速度杠杠的。

![img](file:///C:\Users\guozi\AppData\Local\Temp\ksohtml\wps6BA0.tmp.jpg) 

 

### 3.2 列出镜像

```
docker images
```

### 3.3 删除镜像

```
docker rmi 镜像
```

### 3.4 导入导出镜像

导出镜像：

```
docker save 镜像 > /root/xx.tar.gz
```

导入镜像：

```
docker load < /root/xx.tar.gz
```

## 4 Docker容器

### 4.1 启动容器

以 **交互**方式启动容器：

```shell
docker run -it --name 容器名称 镜像 /bin/bash  
# 进入容器
```

以守护进程方式启动容器：

```
docker run -d --name 容器名称 镜像
```

### 4.2 停止容器

```
docker stop 容器名称或者容器ID
```

### 4.3 **重启容器**

```
docker start 容器名称或者容器ID
```

### 4.4 **删除容器**

删除指定容器：

```
docker rm 容器名称或者容器ID 
```

删除所有容器：

```
docker rm ‘docker ps -a -q’ 
```

### 4.5 **查看容器**

```
docker ps ：查看正在运行的容器

docker ps -a：查看历史运行过的容器

docker ps -l：查看最近运行过的容器

```

### 4.6 进入docker 容器

```
docker exec -it mytomcat /bin/bash
```

## 5 **Docker应用**

### 5.1 **搭建Tomcat服务**

启动 tomcat

```
docker run -d --name my-tomcat -p 8888:8080 镜像
```

![img](file:///C:\Users\guozi\AppData\Local\Temp\ksohtml\wps6BA1.tmp.jpg) 

 

![img](file:///C:\Users\guozi\AppData\Local\Temp\ksohtml\wps6BB2.tmp.jpg) 

### 5.2 **部署web应用**

1、 将war包上传到宿主机器

2、 通过docker cp命令将宿主机器的war包上传到容器中tomcat的webapps目录下。

3、 tomat会自动热部署，直接访问web应用的路径即可。

```shell
docker cp    myProject.war     mytomcat:/usr/local/tomcat/webapps
```

4、进入容器 tomcat、进入容器后正常操作即可

```
docker exec -it mytomcat /bin/bash
```

 5、实时显示tomcat日志

```
docker logs -f -t --since="2017-05-31" --tail=10 edu_web_1

-f : 查看实时日志
-t : 查看日志产生的日期
--since : 此参数指定了输出日志开始日期，即只输出指定日期之后的日志。
-tail=10 : 查看最后的10条日志。
edu_web_1 : 容器名称
```





 

 

 

 