# KindEditor



## 1.KindEditor是什么？

- HTML编辑器
  - 所见即所得的HTML编辑器
- 常见的所见即所得的html内容编辑器工具有：
  - KindEditor
  - ueditor
  - fckeditor（ckeditor）
- 官网文档
  - http://kindeditor.net/doc.php

## 2.KindEditor使用案例

### 入门使用(控件的初始化 )

#### 1.导入文件

- lang
- plugins
- themes
- kindeditor.js

#### 2.引入约束

```jsp
<!-- ===========html编辑器================================= -->
<script charset="utf-8" src="../../js/kindEditor/lang/zh_CN.js"></script>
<script charset="utf-8" src="../../js/kindEditor/kindeditor.js"></script>
```

#### 3.添加textarea输入框及对应脚本

- HTML / jsp页面

```jsp
<tr>
  <td>宣传内容(活动描述信息):</td>
  <td colspan="3">
    <textarea id="description" name="description" style="width:80%" rows="20"></textarea>
  </td>
</tr>
```

- js
  - 与文本编辑框中的id项对应

```javascript
/* ====文本编辑器============= */
KindEditor.ready(function(K) {
  window.editor = K.create('#description');
});
```





### 图片的上传

#### 网络图片上传

#### 本地图片上传



```java
package com.itheima.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONObject;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class ImageAction extends ActionSupport {

	
	/*注:
	 * struts2 属性驱动获取文件内容
	 * 约定，在struts2内部的的FileUploadInterceptor完成的 
	 * String contentTypeName = inputName + "ContentType";//默认就是input名称+ContentType 
        String fileNameName = inputName + "FileName";//默认就是input名称+FileName 
	 * 
	 * */
	// 属性驱动获取文件
	private File imgFile;
	// 属性驱动获取文件类型  
	private String imgFileContentType;
	// 属性驱动获取文件名
	private String imgFileFileName;
	
	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}
	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}
	public void setImgFileContentType(String imgFileContentType) {
		this.imgFileContentType = imgFileContentType;
	}
	
	/**
	 * 查找所有菜单数据 分页
	 */
	@Action(value = "imageAction_upload")
	public String upload() throws IOException {
		
		//用于 响应页面的数据的封装
		Map<String, Object> map = new HashMap<>();
		
		try {
			// 获取保存文件的文件夹的绝对路径
			String saveDir = "uploadImage";
			
			String saveDirPath = ServletActionContext.getServletContext().getRealPath(saveDir);
			
			//获取文件的后缀名
			String lastName = imgFileFileName.substring(imgFileFileName.lastIndexOf("."));
			
			//生成随机文件名
			String fileName = UUID.randomUUID().toString()+lastName;
			
			//保存文件--项目路径下
			FileUtils.copyFile(imgFile, new File(saveDirPath+"/"+fileName));
          
	//=========================================================================================
			//获取项目的路径
			String pojectPath = ServletActionContext.getServletContext().getContextPath();
			
			//项目路径+保存的新建的文件夹+文件名
			String relativePath = pojectPath + saveDirPath + fileName;
			
			map.put("error", 0);
			map.put("url", relativePath);//上传成功后，展示在编辑区
		} catch (Exception e) {
			
			map.put("error", 1);
			map.put("message", e.getMessage());
			e.printStackTrace();
		}
		
		
		//上传成功后， 向页面响应信息map
		/**
		 * 
		 * 返回格式(JSON)
			//成功时
			{
			        "error" : 0,
			        "url" : "http://www.example.com/path/to/file.ext"
			}
			//失败时
			{
			        "error" : 1,
			        "message" : "错误信息"
			}
		 * 
		 * */
		String json = JSONObject.fromObject(map).toString();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);

		return NONE;
	}

}
```



#### 图片空间的回显

- ### 图片空间

  - 保存以前上传过的图片

```java
/**
	 * 浏览图片空间内的所有图片
	 */
@Action(value = "imageAction_manager")
public String imageAction_manager() throws IOException {

  //获取文件上传保存的路径 +目录
  String saveDirPath = "uploadNew";
  String saveDirRealPath = ServletActionContext.getServletContext() .getRealPath(saveDirPath);
  File saveDir = new File(saveDirRealPath);
  System.out.println("遍历的路径=="+saveDirRealPath);
  // 图片扩展名
  String[] fileTypes = new String[] {"gif", "jpg", "jpeg", "png", "bmp"};

  // 遍历    文件上传保存的路径 +目录
  List<Hashtable> fileList = new ArrayList<Hashtable>();
  if (saveDir.listFiles() != null) {
    for (File file : saveDir.listFiles()) {
      Hashtable<String, Object> hash = new Hashtable<String, Object>();
      String fileName = file.getName();
      if (file.isDirectory()) {
        //是文件夹的话继续遍历
        hash.put("is_dir", true);
        hash.put("has_file", (file.listFiles() != null));
        hash.put("filesize", 0L);
        hash.put("is_photo", false);
        hash.put("filetype", "");
      } else if (file.isFile()) {
        //是文件的话，就把文件相关的信息 保存进map  最后转成json响应回页面
        String fileExt =fileName.substring(fileName.lastIndexOf(".") + 1) .toLowerCase();
        hash.put("is_dir", false);
        hash.put("has_file", false);
        hash.put("filesize", file.length());

        //判断图片的后缀名是不是上面中的
        hash.put("is_photo",  Arrays.<String>asList(fileTypes).contains(fileExt));
        hash.put("filetype", fileExt);
      }
      hash.put("filename", fileName);
      hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") .format(file.lastModified()));
      fileList.add(hash);
    }
  }

  // 封装写回客户端的数据
  Map<String, Object> map = new HashMap<>();

  // 指定所有文件的信息    
  map.put("file_list", fileList);  
  // 指定保存文件的文件夹的路径
  // 路径格式 : /bos_management_web/upload/
  map.put("current_url",  ServletActionContext.getServletContext().getContextPath()+"/uploadNew/");//返回路径，图片的正常显示

  // 向客户端写回数据
  String json = JSONObject.fromObject(map).toString();
  HttpServletResponse response = ServletActionContext.getResponse();
  response.setContentType("application/json;charset=utf-8");
  response.getWriter().write(json);
  return NONE;

}
```





#### 文件保存目录问题

###### 保存至项目下

![](E:\教材\知识回顾\HTML编辑器\image\保存至工程项目下.png)



###### 保存至eclipse安装路径下

![](E:\教材\知识回顾\HTML编辑器\image\保存至eclipse安装路径下.png)



###### 保存至指定路径

![](E:\教材\知识回顾\HTML编辑器\image\指定位置.png)


































































```javascript
var KE;
KindEditor.ready(function(K) {
  KE = K.create('#Content',{
    uploadJson : 'kindeditor/asp/upload_json.asp', //图片的上传路径
    fileManagerJson : 'kindeditor/asp/file_manager_json.asp',
    allowImageUpload: true, //多图上传
    allowFileManager : true, //浏览图片空间
    filterMode : false, //HTML特殊代码过滤
    afterBlur: function(){ this.sync(); }, //编辑器失去焦点(blur)时执行的回调函数（将编辑器的HTML数据同步到textarea）
    afterUpload : function(url, data, name) { //上传文件后执行的回调函数，必须为3个参数
      if(name=="image" || name=="multiimage"){ //单个和批量上传图片时
        if(K("#pic").length>0){ //文本框存在
          document.getElementById('piclist').options[document.getElementById('piclist').length]=new Option(url,url); //下拉列表框增加一条
          document.getElementById('piclist').selectedIndex+=1; //选定刚新增的这一条
          K("#indexpicimg").html("<img src='" + url + "' width='150' height='95' />"); //重置图片展示区DIV的HTML内容
          K("#pic").val(url); //重置文本框值
        }
      }
    }
  });
});
```

