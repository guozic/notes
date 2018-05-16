# spring容器中注入mybatis 接口的原理

## 工程代码

```java
<!-- 自动扫描所有的Mapper接口与文件  -->
	<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
		<property name="basePackage" value="com.ybkj.highTide.mapper"></property>
	</bean>
```




## MapperScannerConfigurer

```java
public class MapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware {

  //  ...
  
  
  //--    通过bean的注册信息实例化Mapper扫描类
    @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
    if (this.processPropertyPlaceHolders) {
      processPropertyPlaceHolders();
    }

    ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
    scanner.setAddToConfig(this.addToConfig);
    scanner.setAnnotationClass(this.annotationClass);
    scanner.setMarkerInterface(this.markerInterface);
    scanner.setSqlSessionFactory(this.sqlSessionFactory);
    scanner.setSqlSessionTemplate(this.sqlSessionTemplate);
    scanner.setSqlSessionFactoryBeanName(this.sqlSessionFactoryBeanName);
    scanner.setSqlSessionTemplateBeanName(this.sqlSessionTemplateBeanName);
    scanner.setResourceLoader(this.applicationContext);
    scanner.setBeanNameGenerator(this.nameGenerator);
    scanner.registerFilters();
    scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
  }
  
  }
```



- `MapperScannerConfigurer` 实现了 `BeanDefinitionRegistryPostProcessor`这个接口，并实现了 `postProcessBeanDefinitionRegistry` 这个方法

- 在spring初始化的时候将bean以及bean的一些属性信息保存至 `BeanDefinitionHolder` 中

- 在 执行  ` ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);`  时，内部执行

- ```java
  public class ClassPathMapperScanner extends ClassPathBeanDefinitionScanner {

    private boolean addToConfig = true;

    private SqlSessionFactory sqlSessionFactory;

    private SqlSessionTemplate sqlSessionTemplate;

    private String sqlSessionTemplateBeanName;

    private String sqlSessionFactoryBeanName;

    private Class<? extends Annotation> annotationClass;

    private Class<?> markerInterface;

    private MapperFactoryBean<?> mapperFactoryBean = new MapperFactoryBean<Object>();

    // ------------------------------
    public ClassPathMapperScanner(BeanDefinitionRegistry registry) {
      super(registry, false);
    }
    
    }
  ```

- ​