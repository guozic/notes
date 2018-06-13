# spring注解关键字

## spring注解：

### (1)@Controller 

- 控制器

### (2)@Autowired    

- 按照类型匹配，可以完成对类成员变量，方法及构造函数进行标注，完成自动装配的工作   @Autowired(required=false) 在找不到匹配Bean时也不报错

### (3)@Qualifier   

- 注释指定注入bean的名称，防止冲突，可以对成员变量、方法入参、构造函数入参进行标注
  - 若spring容器中 某个service接口中有多个实现类，
    - 在引用的时候需要用此注解(@Qualifier("service"))指定某个实现，否则会报错

### (4)@Component    

- 通过使用@Component注释类就可以完成bean的定义，使用@Component("beanname") 来指定bean的名称

### (5)@Service      

- 通常作用在业务层，但是目前该功能与@Component相同

### (6)@Scope        

- @scope完成bean的作用域配置默认是单例模式（singleton）
- 如果需要设置的话可以修改对应值与以上提到的一致例如：@scope(“prototype”)

### (7)@Repository   

- 该注解不只是将类识别为Bean，同时它还能将锁标注的类中抛出的数据访问异常封装为spring的数据访问异常类型

## spring MVC注解：

### (1)@RestController  

- @Controller的子类，在开发rest服务时不需要使用@Controller而专门使用@RestController

### (2)@RequestMapping

- 映射位置，并带参数,既可以作用在类级别，也可以作用在方法级别

#### 属性

##### value

- value指向请求的实际地址
- - 例如：
    - @RequestMapping(value="get/{id}")   

##### method

- method指定请求的method类型，GET、POST、PUT、DELETE  
  - 例如：@RequestMapping(value="/{day}", method = RequestMethod.GET) 

##### consumes

- consumes方法指定处理request Content-Type的请求，
  - 例如：
  - @RequestMapping(value = "/pets", method = RequestMethod.POST, consumes="application/json")，方法仅处理request Content-Type为“application/json”类型的请求

##### produces

- produces方法指定处理request请求中Accept头中包含的数据，
  - 例如：
    - @RequestMapping(value = "/pets/{petId}", method = RequestMethod.GET, produces="application/json")
    - 方法仅处理request请求中Accept头中包含了"application/json"的请求，同时暗示了返回的内容类型为application/json;

##### params

- 处理请求中包含的某个参数，
  - 例如：
  - @RequestMapping(value = "/pets/{petId}", method = RequestMethod.GET, params="myParam=myValue")，仅处理请求中包含了名为“myParam”，值为“myValue”的请求；

##### headers

- 仅处理request的header中的某个请求，
- 例如：
  - @RequestMapping(value = "/pets", method = RequestMethod.GET, headers="Referer=<http://www.ifeng.com/>")，仅处理request的header中包含了指定“Refer”请求头和对应值为“<http://www.ifeng.com/>”的请求；

### (3)@PathVariable 

- 通过@PathVariable注解来绑定@RequestMapping传过来的值到方法的参数上。

- 例如：

  - @RequestMapping("/pets/{petId}")  
    - 将@RequestMapping变量中的petId的值绑定到方法的petId上

  ```java
  @RequestMapping("/pets/{petId}")  
  public void findPet( @PathVariable String petId, Model model) {      

      // implementation omitted  

  } 

  ```

  ​

### (4)@RequestHeader 

-  可以把Request请求header部分的值绑定到方法的参数上 

- 例如：

  ```java
  public void displayHeaderInfo(
    @RequestHeader("Accept-Encoding") String encoding,
    @RequestHeader("Keep-Alive") long keepAlive
    )
  ```

  ​

- 一个request的header部分：

  - Host                    localhost:8080  
  - Accept                  text/html,application/xhtml+xml,application/xml;q=0.9  
  - Accept-Language         fr,en-gb;q=0.7,en;q=0.3  
  - Accept-Encoding         gzip,deflate  
  - Accept-Charset          ISO-8859-1,utf-8;q=0.7,*;q=0.7  
  - Keep-Alive              300

### (5)@CookieValue

- 可以把Request header中关于cookie的值绑定到方法的参数上 

- 例如：

  ```java
  public void displayHeaderInfo(@CookieValue("JSESSIONID") String cookie)
  ```

  - 即把JSESSIONID的值绑定到参数cookie上

### (6)@RequestParam

- 常用来处理简单类型的绑定，默认为必填参数，类似于request.getParameter(""),

### (7)@RequestBody  

-  将 HTTP 请求正文插入方法中,使用适合的HttpMessageConverter将请求体写入某个对象。

### (8)@ResponseBody 

-  将内容或对象作为 HTTP 响应正文返回，使用@ResponseBody将会跳过视图处理部分，而是调用适合HttpMessageConverter，将返回值写入输出流。

(9)@ModelAttribute

-  这个注解和@SessionAttributes配合一起使用，可以将ModelMap中属性的值通过该注解自动赋值给指定变量

### (10)@SessionAttributes  

-  将modelMap中指定的属性放到session中。
- ​

## spring支持由JSR-250规范定义的注解

### (1)@Resource 

-  作用与@Autowired类似，只不过@Autowired是按byType自动注入，而@Resource 默认按byName自动注入罢了 
- @Resource 注释的name属性解析为Bean的名字，而type属性则解析为Bean的类型

### (2)@PostConstruct

-  用于方法上，注释的方法将在类初始化后调用，类似于配置文件中的 init-method

### (3)@PreDestroy     

- 用于方法上，标注了@PreDestroy的方法将在类销毁前调用

## spring 支持由JSR-330规范定义的注解

### (1)@Inject

-  等价于默认的@Autowired，只是没有required属性

### (2)@Name  

- 指定Bean名字，对应于Spring自带@Qualifier中的缺省的根据Bean名字注入情况。

### (3)@Qualifier

-  只对应于spring自带@Qualifier限定描述符注解，即只能扩展使用，没有value属性