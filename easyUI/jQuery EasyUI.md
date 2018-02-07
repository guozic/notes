## jQuery EasyUI

### JavaScript的UI框架
* jQuery UI
  * [官方主页](http://jqueryui.com/)
  * [中文页面](http://www.jqueryui.org.cn/)
  * jQuery官方出品. 在jQuery的基础之上设计的插件.提供了一些常用的界面元素, 包含底层用户交互、动画、特效和可更换主题的可视控件.免费
* jQuery EasyUI
  * [官方主页](http://www.jeasyui.com/)
  * [中文页面](http://www.jeasyui.net/)
  * 第三方公司出品. 同样提供了一些常用的界面元素, 商业项目要收费

### 目录结构

	* demo：easyui普通网页演示页面代码库
	* demo-mobile：easyui手机端网页演示页面代码库
	* extension：easyui第三方插件库
	* locale：easyui国际化资源文件库
	* plugins：easyui核心功能组件分解后的独立插件库
	* src：easyui部分非核心组件的源代码库
	* themes：easyui的皮肤库
	* changelog.txt：easyui版本官方更新日志文件
	* easyloader.js：easyui组件加载器（easyui提供了2种组件加载方式，这就是其中一种，当使用该方式的时候可以不必引入jquery.easyui.min.js文件，具体用法请参看api文档）
	* jquery.easyui.min.js：easyui的完整组件包文件（当使用了该文件的时候就可以不必引入easyloader.js文件)
	* jquery.min.js：jQuery框架库文件 
	* licence_gpl.txt：GNU开源协议文档
	* readme.txt：官方的说明

### EasyUI环境搭建

```jsp
一.环境的搭建
1.js及css文件导入
导入jquery核心类库
导入easyui类库 
themes文件夹的导入（css样式及图标）
2.jQuery的要先于easyUI的js文件导入，easyUI依赖jQuery 
```

* 拷贝所有页面资源
* 在使用EasyUI的页面中引入以下内容

   <link rel="stylesheet" type="text/css"
   ```jsp
   	href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
   <link rel="stylesheet" type="text/css"
   	href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
   <script type="text/javascript"
   	src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
   <script type="text/javascript"
   	src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
   ```


### layout页面布局

```
二.layout的布局
1.示例代码复制
2.适应容器 ，添加data-options="fit:'true'" 填满所在的整个框体               
3.split:true  不能随意拖动边框
4.style="padding:5px;background:#eee;"  框之间的小间隔
```



```jsp
<body class="easyui-layout">
	<div data-options="region:'north',title:'North Title'"
		style="height: 100px;"></div>
	<div data-options="region:'south',title:'South Title'"
		style="height: 100px;"></div>
	<div data-options="region:'east',title:'East Title'"
		style="width: 100px;"></div>
	<div data-options="region:'west',title:'West Title'"
		style="width: 100px;"></div>
	<div data-options="region:'center',title:'center Title'"
		style="padding: 5px;"></div>
</body>
```

### accordion折叠面板

```jsp
三.分类控件
1.selected:true属性默认选中状态，所有的分类都没有则默认第一个
2. data-options="iconCls:'icon-reload'"  居中分类标题
3.data-options="fit:true"拖拉边框，自动适应容器
```



```jsp
<div class="easyui-accordion" data-options="fit:'true'">
	<div title="Title1" data-options="iconCls:'icon-save'">第一个折叠面板</div>
	<div title="Title2" data-options="iconCls:'icon-reload'">第2个折叠面板</div>
	<div title="Title3" data-options="iconCls:'icon-search'">第3个折叠面板</div>
</div>
```
### tabs选项卡面板

```jsp
	四.table 选项卡
			1.data-options="fit:true"  铺满容器
			2.data-options="closable:true"  窗口是否能被关闭
		
```



```jsp
<div id="tt" class="easyui-tabs" data-options="fit:'true'">
	<div title="Tab1" style="padding: 20px;">tab1</div>
	<div title="Tab2" data-options="closable:true"
			style="overflow: auto; padding: 20px;">tab2</div>
	<div title="Tab3" data-options="iconCls:'icon-reload',closable:true"
			style="padding: 20px;">tab3</div>
</div>
```
### 动态添加选项卡

```jsp
五.动态添加选线卡
			步骤一：添加按钮	
				<a onclick="addTable()" id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加按钮</a>
			步骤二：绑定点击事件，调用方法
				easyUI调用方法的规律：
					$().控件名字（控件方法 ，方法的参数）
					<script>
						function addTable() {
							$('#tt').tabs('add', {
								title: 'New Tab',
								content: 'Tab Body',
								closable: true,
								tools: [{
									iconCls: 'icon-mini-refresh',
									handler: function() {
										alert('refresh');
									}
								}]
							});
						}
					</script>
			步骤三：添加优化
				在添加选项卡之前先判断要添加的那个选项卡是否存在
				存在就选中即可，不存在才添加
					<script>
						function addTable() {
							var flag = $('#tt').tabs('exists','New Tab');
							alert(flag)
							if(flag){
								/*存在就选中*/
								$('#tt').tabs('select','New Tab');
							}else{
								/*不存在就添加*/
								$('#tt').tabs('add', {
								title: 'New Tab',
								content: 'Tab Body',
								closable: true,
								tools: [{
									iconCls: 'icon-mini-refresh',
									handler: function() {
										alert('refresh');
									}
								}]
							});
							}
						}
					</script>
```



	<a id="id_btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">addTabs</a>
	<script type="text/javascript">
		$(function() {
			// 为按钮绑定点击事件
			$("#id_btn").click(function() {
			// 判断Tab是否存在
			var exists = $("#id_tabs").tabs('exists', "New Tab")
			if(exists) {
				// 如果Tab存在，就选中
				$("#id_tabs").tabs('select', "New Tab")
			} else {
				// 如果Tab不存在，就添加新Tab
				$("#id_tabs").tabs('add', {
					title: 'New Tab',
					content: 'Tab Body',
					closable: true,
					style: {
						padding: '20px'
					}
				});
				}
			})
		})
	</script>
## jQuery ztree插件使用

### ztree介绍

```jsp
			一.环境的搭建
				jquery类库
				ztree文件夹
			二.添加  <ul id="treeDemo" class="ztree"></ul>
			三.添加入口函数
				$(function(){
					var setting = {};
					var zNodes = [
						{name:"节点1",
						children: [
								{ name:"叶子节点11"},
								{ name:"叶子节点12"},
								{ name:"叶子节点13"},
								{ name:"叶子节点14"}
						]},
						{name:"节点2",
						children: [
								{ name:"叶子节点21"},
								{ name:"叶子节点22"},
								{ name:"叶子节点23"},
								{ name:"叶子节点24"}
						]
						},
						{name:"节点3",
						children: [
								{ name:"叶子节点31"},
								{ name:"叶子节点32"},
								{ name:"叶子节点33"},
								{ name:"叶子节点34"}
						]
						}
					];
					$.fn.zTree.init($("#treeDemo"), setting, zNodes);
				});
			四.简单json树
				1.添加入口函数
				2.设置属性setting
					var setting = {
						data: {
							simpleData: {->表示简单json树
								enable: true
							}
						}
					};
				3.设置属性zNodes
					var zNodes = [
						{name:"节点1",id:1,pid:0},
						{name:"节点2",id:2,pid:1},
						{name:"节点3",id:3,pid:2},
					];
			五.AJXA获取数据
				1.将生成树的函数   放入 异步获取的数据的回调函数中
				入口函数
				$(function(){
					
					var setting = {
						data: {
							simpleData: {
								enable: true
							}
						}
					};
					
					/*var zNodes = [
						{name:"节点1",id:1,pid:0},
						{name:"节点2",id:2,pid:1},
						{name:"节点3",id:3,pid:2},
					];*/
					/*AJAX获取数据*/
					$.get("../data/menu.json", function(zNodes){
						
					    // 生成树
						$.fn.zTree.init($("#treeDemo"), setting, zNodes);
						
					},"json");
					
				});
```



* [官网](http://www.treejs.cn/v3/main.php#_zTreeInfo)
* [托管地址](https://github.com/zTree/zTree_v3)
* ztree是jquery的一个开源树形目录的插件，用来快速构建网站的树形目录结构，并且提供了功能丰富，利于扩展的API。
* 只要引入jquery和ztree的库js，然后给ztree提供需要的json数据，并且设置好ztress的属性，就可以即刻展示出树形目录。

### 环境搭建

* 在使用ztree的页面引入以下内容

   ​
   	<script type="text/javascript"
   	src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
   	<link rel="stylesheet"
   		href="${pageContext.request.contextPath }/js/ztree/zTreeStyle.css"
   		type="text/css">
   	<script type="text/javascript"
   		src="${pageContext.request.contextPath }/js/ztree/jquery.ztree.all.min.js"></script>

### 基于标准json数据构造ztree

* 创建ul标签`<ul id="id_ul" class="ztree"></ul>`
* 在入口函数中增加以下代码

        //定义一个变量，用来声明ztree的属性
        var setting = {};
        // 定义一个json数据，用来表示节点
        var zNodes = [{
            name: "父节点1",
            children: [{
                name: "叶子节点11"
            }, {
                name: "叶子节点12"
            }, {
                name: "叶子节点13"
            }, {
                name: "叶子节点14"
            }]
        }, {
            name: "父节点2",
            children: [{
                name: "叶子节点21"
            }, {
                name: "叶子节点22"
            }, {
                name: "叶子节点23"
            }, {
                name: "叶子节点24"
            }]
        }, {
            name: "父节点3"
        }]
        // 生成树
        $.fn.zTree.init($("#id_ul"), setting, zNodes);
### 基于简单json数据构造ztree

        //定义一个变量，用来声明ztree的属性
        var setting2 = {
            data: {
                simpleData: {
                    // 允许使用简单JSON
                    enable: true
                }
            }
        };
        // 声明一个简单JSON数据
        var treeNodes2 = [{
            id: 1,
            pId: 0,
            name: "test1"
        }, {
            id: 11,
            pId: 1,
            name: "test11"
        }, {
            id: 111,
            pId: 11,
            name: "test111"
        }];
        // 生成树
        $.fn.zTree.init($("#id_ul2"), setting2, treeNodes2);
### 基于ajax加载远程json数据构造ztree

        //定义一个变量，用来声明ztree的属性
        var setting4 = {
            data: {
                simpleData: {
                    // 允许使用简单JSON
                    enable: true
                }
            }
        };
    	// AJAX请求
        $.get(//
        "${pageContext.request.contextPath }/data/menu.json",// URL
        function(data) {
            // 生成树
            $.fn.zTree.init($("#id_ul4"), setting4, data);
        }, //回调函数
        "json"// 返回的数据类型
        );
### 为ztree节点绑定事件动态添加选项卡

        //定义一个变量，用来声明ztree的属性
        var setting4 = {
            data: {
                simpleData: {
                    // 允许使用简单JSON
                    enable: true
                }
            },
            callback: {
                // 增加点击事件
                // treeId : 节点ID
                // treeNode : 节点对象
                onClick: function (event, treeId, treeNode) {
                    // 当被点击的节点中page属性有值的时候，才创建新Tab
                    if(treeNode.page!=undefined){
                     // 判断Tab是否存在
                        var exists = $("#id_tabs").tabs('exists', treeNode.name)
                        if (exists) {
                            // 如果Tab存在，就选中
                            $("#id_tabs").tabs('select', treeNode.name)
                        } else {
                            // 如果Tab不存在，就添加新Tab
                            $("#id_tabs").tabs('add', {
                                title: treeNode.name,
                                content: '<iframe style="width: 100%;height: 100%"  frameborder="no"  src="${pageContext.request.contextPath }/'+treeNode.page+'"></iframe>',
                                closable: true
                            });
                        }
                    }
                }
            }
        };
    
        $.post(//
        "${pageContext.request.contextPath }/data/menu.json",// URL
        function(data) {
            // 生成树
            $.fn.zTree.init($("#id_ul4"), setting4, data);
        }, //回调函数
        "json"// 返回的数据类型
        );

### menubutton按钮

- 示例代码

```jsp
<!--
					icon-edit：图标
					menu:'#mm'：实现隐藏下拉
					
				-->
				<a href="javascript:void(0)" id="mb" class="easyui-menubutton" data-options="menu:'#mm',iconCls:'icon-edit'">Edit</a>
				<div id="mm" style="width:150px;">
					<div data-options="iconCls:'icon-undo'">Undo</div>
					
					<!--分隔线-->
					<div class="menu-sep"></div>
					
					<div data-options="iconCls:'icon-redo'">Redo</div>
					<div class="menu-sep"></div>
					<div>Cut</div>
					<div>Copy</div>
					<div>Paste</div>
					<div class="menu-sep"></div>
					<div data-options="iconCls:'icon-remove'">Delete</div>
					<div>Select All</div>
				</div>
```

### messager控件

- 示例代码

```javascript
/*info改变弹出框的消息前面的图标*/
$.messager.alert('我的消息','这是一个提示信息！','info');

$.messager.alert('警告', '警告消息');
$.messager.confirm('确认', '您确认想要删除记录吗？', function(r) {
  if(r) {
    alert('确认删除');
  }
});
```

