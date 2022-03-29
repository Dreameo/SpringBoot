# SpringBoot2



## 1、springboot-helloworld

> 官网默认方式

```java
@RestController // 相当于@Controller 和 @ResponseBody 复合注解
@EnableAutoConfiguration
public class MyApplication {

    @RequestMapping("/hello")
    public String index() {
        return "Hello SpringBoot2";
    }

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```



> 一个SpringBoot启动类，其他还是跟web应用一样写controller的写法

![image-20220325183150994](https://raw.githubusercontent.com/Dreameo/JavaLine/master/7_SpringBoot2/imgs/springboot-helloworld-error1.png)

正确包的情况为：作为子包存在

![image-20220325183322990](https://raw.githubusercontent.com/Dreameo/JavaLine/master/7_SpringBoot2/imgs/springboot-helloworld-success.png)

> 疑问：两种方式的区别？
>
> ​	其实是一样的，@SpringBootApplication是由三个注解共同组成的注解。



> 全部配置可以都设置在一个配置文件中

>可以打包成一个可执行jar包，包含运行环境，直接运行jar就行。



## 2、SpringBoot自动配置原理

### 2.1 SpringBoot特点

#### 2.1.1 依赖管理

父项目作为依赖管理

![image-20220325185531990](https://raw.githubusercontent.com/Dreameo/JavaLine/master/7_SpringBoot2/imgs/pom_dependency.png)

```xml
1. 父项目作为依赖管理，几乎声明了所有开发中常用的依赖的版本号,自动版本仲裁机制
2. 不需要关注版本，自动版本仲裁
3. maven特性，就近原则，如果要使用自己的特定的版本，可以通过在pom文件中重写配置。 从spring-boot-dependencies里查看依赖的版本 用的key：
比如：mysql （key为<mysql.version>）
<properties>
        <mysql.version>5.1.43</mysql.version>
</properties>
4. 如果引入非版本仲裁的jar，要写版本号。
```



#### 2.1.2 自动配置

- 自动配置tomcat

  - 引入tomcat包

  ```xml
  web-start的父容器完成了
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-tomcat</artifactId>
      <version>2.6.5</version>
      <scope>compile</scope>
  </dependency>
  ```

  - 配置tomcat

- 自动配置Springmvc

  - SpringMVC全套组件

  ```xml
  web-start的父容器完成
  <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-web</artifactId>
      <version>5.3.17</version>
      <scope>compile</scope>
  </dependency>
  <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>5.3.17</version>
      <scope>compile</scope>
  </dependency>
  ```

  ```java
   public static void main(String[] args) {
          // 1. 返回IOC容器
          ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);
  
          // 2. 查看容器里面的组件
          String[] beanDefinitionNames = run.getBeanDefinitionNames();
          for (String bean : beanDefinitionNames) {
              System.out.println(bean);
          }
     }
  // 配置SpringMVC一般会配置DispatcherServlet、编码过滤器之类的
  ```

  ![image-20220325191547702](https://raw.githubusercontent.com/Dreameo/JavaLine/master/7_SpringBoot2/imgs/beans.png)

  

  - 自动配置好了Web常用功能，如字符编码、文件上传等

- 默认的包结构（只要是与主程序(启动程序)同包或者子包都可扫描到）

  - 如果非要改变扫描路径，可以通过扩大包来进行扫描@SpringBootApplication(scanBasePackages=**"com.yfh"**)或者使用ComponentScan();
  - @SpringBootApplication默认扫描与主程序以下的包
  - @SpringBootApplication相当于三个注解： @SpringBootConfiguration  @EnableAutoConfiguration  @ComponentScan("com.yfh.boot")

- 各种默认配置
  - 默认配置最终都是映射到某个类上，如：MultipartPropertie （文件上传类）
  - 配置文件的值最终会绑定到每个类上，每个类会在容器中创建对象

- 按需加载所有的自动配置项

  - 有很多启动场景（starter）
  - 可以根据需求自己引入哪些场景，引入的才会进行自动配置
  - springboot的所有自动配置功能都在spring-boot-autoconfigure中


### 2.2 、容器功能

> 用一些注解将组件加入到容器中WEB中常用的注解以及SpringBoot中的注解。 

#### 添加组件

##### @Configuration

- 基本使用

![image-20220325195441626](https://raw.githubusercontent.com/Dreameo/JavaLine/master/7_SpringBoot2/imgs/configuration-usage.png)

```java
// 配置类
/**
 * 1、配置类里面使用@Bean标注在方法上给容器注册组件，默认也是单实例的
 * 2、配置类本身也是组件
 * 3、proxyBeanMethods：代理bean的方法
 *      Full(proxyBeanMethods=true)、【保证每个@Bean方法被调用多少次返回的组件都是单实例的】
 *      Lite(proxyBeanMethods=false)【每个@Bean方法被调用多少次返回的组件都是新创建的】
 *      组件依赖必须使用Full模式默认。其他默认是否Lite模式
 */


@Configuration(proxyBeanMethods = false)
public class MyConfig {
//    Full:外部无论对配置类中的这个组件注册方法调用多少次获取的都是之前注册容器中的单实例对象

    @Bean // 给容器中添加组件。以方法名作为组件的id。返回类型就是组件类型。返回的值，就是组件在容器中的实例
    public User user1() {
        User zhangsan = new User("zhangsan", 19);
        zhangsan.setPet(pet()); // proxyBeanMethods = false 但是不影响运行！！

        /**
         * user组件依赖了Pet组件
         */

        return zhangsan;
    }

    @Bean("tompet")
    public Pet pet() {
        return new Pet("petty");
    }
}


// 测试类：

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        // 1. 返回IOC容器
        ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);

        // 2. 查看容器里面的组件
        String[] beanDefinitionNames = run.getBeanDefinitionNames();
        for (String bean : beanDefinitionNames) {
            System.out.println(bean);
        }

        // 3. 从容器中获取组件
        /**
         * 默认单例对象，因此不管多少次获取组件，都是从容器中获取同一个。
         */
        User user1 = run.getBean("user1", User.class);
        User user2 = run.getBean("user1", User.class);
        System.out.println(user1==user2);

        /**
         * 但是SpringBoot2在@Configuration注解中添加了proxyBeanMethods属性。
         * 如果@Configuration(proxyBeanMethods = true)代理对象调用方法。SpringBoot总会检查这个组件是否在容器中有。
         *
         */
        User user01 = run.getBean("user1", User.class);
        Pet tom = run.getBean("tompet", Pet.class);

        System.out.println("用户的宠物："+(user01.getPet() == tom));


    }
}

```

> 2、这些WEB常用注解 @Bean、@Component、@Controller、@Service、@Repository同样也可以用来添加组件

> 3、 @ComponentScan、@Import 包扫描以及引入类组件

#####  @ComponentScan、@Import

```java
@Import({User.class, Pet.class, Filter.class}) // 给容器中自动创建出这两个类型的组件、默认组件的名字就是全类名
public class MyConfig {
...

// 获取组件
String[] beanNamesForType = run.getBeanNamesForType(User.class);
System.out.println("============User=================");
for (String s : beanNamesForType) {
    System.out.println(s);
}

```

![image-20220325202817454](https://raw.githubusercontent.com/Dreameo/JavaLine/master/7_SpringBoot2/imgs/import-annotation.png)

#### @Conditional

> 4、条件装配：满足Conditional指定的条件，则进入组件注入,在Springboot低层有很多这样的注解(自动配置包)
>
> @Conditional有很多子注解



![image-20220325203643610](https://raw.githubusercontent.com/Dreameo/JavaLine/master/7_SpringBoot2/imgs/conditionalonbean.png)



#### 原生配置文件引入

> 原生配置文件引入，如果程序编写了配置文件，可以使用注解将其引入，将组件加入到容器中。

#### @ImportResource("classpath:beans.xml")	

比如导入Spring的配置文件。

![image-20220326113844528](https://raw.githubusercontent.com/Dreameo/JavaLine/master/7_SpringBoot2/imgs/importResource.png)

####  配置绑定

> 1、利用@ConfigurationProperties + @Component进行配置绑定 只有

![image-20220325211404254](https://raw.githubusercontent.com/Dreameo/JavaLine/master/7_SpringBoot2/imgs/mycar-configuration-properties.png)

> 2、第二种方式：@EnableConfigurationProperties(Car.class)开启Car的属性配置功能，将指定的这个Car组件自动注入容器中+@ConfigurationProperties()

![image-20220326120714907](https://raw.githubusercontent.com/Dreameo/JavaLine/master/7_SpringBoot2/imgs/EnableConfiguration.png)

```java
//1、开启Car配置绑定功能
//2、把这个Car这个组件自动注册到容器中

@EnableConfigurationProperties(Car.class) // 开启 Car 的属性配置并自动注入到容器中
public class MyConfiguration {...}

@ConfigurationProperties(prefix = "mycar")
public class Car {...}
```





### 2.4 自动配置原理入门

#### 2.4.1 引导加载自动配置类@SpringBootApplication

```java
// @SpringBootApplication是
 //		@SpringBootConfiguration
 //		@EnableAutoConfiguration
 //		@ComponentScan 这三个的合成注解

```

1. @SpringBootConfiguration

```java
// 点进去查看 还是一个@Configuration
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@Indexed
public @interface SpringBootConfiguration {
    @AliasFor(
        annotation = Configuration.class
    )
    boolean proxyBeanMethods() default true;
}
```

> @Configuration代表的是一个配置类

2. @Component是扫描组件类，Spring注解
3. EnableAutoConfiguration

```java
// 点进去，发现还是合成注解
@AutoConfigurationPackage
@Import({AutoConfigurationImportSelector.class}) 
public @interface EnableAutoConfiguration {
}
```

- @AutoConfigrationPackage

  > 自动配置包：指定默认的包规则

```java
// 给容器中导入一个组件（Registrar.class）, 点进去查看源代码
@Import(AutoConfigurationPackages.Registrar.class)
public @interface AutoConfigurationPackage { //利用Registrar给容器中导入一系列组件
//将指定的一个包下的所有组件导入进来？MainApplication 所在包下。
```

```java
// 利用Registrar导入一系列组件：
public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
    AutoConfigurationPackages.register(registry, (String[])(new AutoConfigurationPackages.PackageImports(metadata)).getPackageNames().toArray(new String[0])); // 将指定一个包的所有组件导入进来
    
}
```

![image-20220328122917779](https://raw.githubusercontent.com/Dreameo/JavaLine/master/5_SSM/2_SpringMVC/imgs/%2540autoConfigurationPage.png)

![image-20220325214526397](https://raw.githubusercontent.com/Dreameo/JavaLine/master/5_SSM/2_SpringMVC/imgs/autoconfigurationPackage.png)

- @Import({AutoConfigurationImportSelector.class})  // 导入组件

```text
1、利用getAutoConfigurationEntry(annotationMetadata);给容器中批量导入一些组件
2、调用List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes)获取到所有需要导入到容器中的配置类
3、利用工厂加载 Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader)；得到所有的组件
4、从META-INF/spring.factories位置来加载一个文件。
	默认扫描我们当前系统里面所有META-INF/spring.factories位置的文件
    spring-boot-autoconfigure-2.3.4.RELEASE.jar包里面也有META-INF/spring.factories
```

```java
@Override
	public String[] selectImports(AnnotationMetadata annotationMetadata) {
		if (!isEnabled(annotationMetadata)) {
			return NO_IMPORTS;
		}
		AutoConfigurationEntry autoConfigurationEntry = getAutoConfigurationEntry(annotationMetadata);
		return StringUtils.toStringArray(autoConfigurationEntry.getConfigurations());
	}
// 批量导入组件
```

![image-20220325215837243](https://raw.githubusercontent.com/Dreameo/JavaLine/master/7_SpringBoot2/imgs/import(AutoConfigurationImportSelector).png)

> 默认全部加载127个写死的组件。META/INF/ fatories.  但最终会按需加载。
>
> 虽然我们127个场景的所有自动配置启动的时候默认全部加载。xxxxAutoConfiguration
> 按照条件装配规则（@Conditional），最终会按需配置。

> 修改默认配置(文件上传解析器)

```java
@Bean
@ConditionalOnBean(MultipartResolver.class)  //容器中有这个类型组件
@ConditionalOnMissingBean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME) //容器中没有这个名字 multipartResolver 的组件
public MultipartResolver multipartResolver(MultipartResolver resolver) { // 默认去容器中找，给@Bean标注的方法传入了对象参数，这个参数的值就会从容器中找。 找MultipartResolver的对象传入进去。
    //给@Bean标注的方法传入了对象参数，这个参数的值就会从容器中找。
    //SpringMVC multipartResolver。防止有些用户配置的文件上传解析器不符合规范
    // Detect if the user has created a MultipartResolver but named it incorrectly
    return resolver;
}
// 给容器中加入了文件上传解析器；

```

总结：

- SpringBoot先加载所有的自动配置类  xxxxxAutoConfiguration
- 每个自动配置类按照条件进行生效，默认都会绑定配置文件指定的值(@EnableConfigurationProperties)。xxxxProperties(@ConfigurationProperties(prefix=""))里面拿。xxxProperties和配置文件进行了绑定
- 生效的配置类就会给容器中装配很多组件
- 只要容器中有这些组件，相当于这些功能就有了
- 定制化配置

- - **用户直接自己@Bean替换底层的组件**
  - 用户去看这个组件是获取的配置文件什么值就去修改。

<font color='red'>**xxxxxAutoConfiguration ---> 组件  --->** **xxxxProperties里面拿值  ----> application.properties**</font>



> 欢迎页：

HandlerMapping:SpringMVC核心组件,处理器映射。保存了每一个Handler能处理哪些请求。

> Restful风格

```java
@Bean
@ConditionalOnMissingBean(HiddenHttpMethodFilter.class)
@ConditionalOnProperty(prefix = "spring.mvc.hiddenmethod.filter", name = "enabled") // 要在配置文件中手动开启
public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
   return new OrderedHiddenHttpMethodFilter();
}

```

```yaml
spring:
	mvc:
		hiddenmethod:
			filter:
				enabled: true # 开启页面表单RestFul风格
```

Rest原理（表单提交要使用REST的时候）

- 表单提交会带_b_method=PUT
- 请求过来被HiddenHttpMethodFilter拦截
  - 请求是否正常，并且是POST
  - 兼容以下请求；PUT.DELETE.PATCH
  - 原生request (post) ,包装模式requesWrapper（HttpMethodRequestWrapper）重写了getMethod方法，返回的是 传入的值。
  - 过滤器链放行的时候用wrapper。以后的方法调用getMethod是调用 requesWrapper的。 filterChain.doFilter(requestToUse, response);

```java
if ("POST".equals(request.getMethod()) && request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE) == null) {
   String paramValue = request.getParameter(this.methodParam);
   if (StringUtils.hasLength(paramValue)) {
      String method = paramValue.toUpperCase(Locale.ENGLISH);
      if (ALLOWED_METHODS.contains(method)) {
         requestToUse = new HttpMethodRequestWrapper(request, method);
      }
   }
}
```

> Rest使用客户端工具:

如：PostMan直接发送Put、 delete等方式请求，无需Filter。



> 请求映射原理 SpringMVC功能分析都是从DispatcherSevlet  --> doDispatch() 每个请求都会调用doDispatch()

![image-20220328143250526](https://raw.githubusercontent.com/Dreameo/JavaLine/master/5_SSM/2_SpringMVC/imgs/DispatcherServlet-doDispatch.png)



RequestMappingHandlerMapping保存了所有@RequestMapping 和handler的映射规则

![image-20220328152025661](https://raw.githubusercontent.com/Dreameo/JavaLine/master/7_SpringBoot2/imgs/image-20220328152025661.png)

```text
1、HandlerMapping作用(有很多类型的HandlerMapping)：根据请求的url、method等信息查找Handler，即控制器方法 ,将请求跟controller方法映射
2、doDispath方法中得到当前请求的Handler(控制器)/在DispatcherServlet的控制下Handler对具体的用户请求进行处理
3、通过HandlerAdapter对处理器（控制器方法）进行执行
4、执行完成之后返回一个ModeAndView

SpringBoot自动配置欢迎页的 WelcomePageHandlerMapping 。访问 /能访问至!Jindex.html;
SpringBoot自动配置了默认的 RequestMappingHandlerMapping
请求进来，挨个尝试所有的HandlerMapping看是否有请求信息。
	如果有就找这个请求对应的handler
	如果没有就是下一个 HandlerMapping
我们需要一些自定义的映射处理，我们也可以自己给容器中放HandlerMapping。自定义HandlerMapping
```

> 参数注解：
>
> @PathVariablev @RequestHeaderN @ModelAttribute、 @RequestParams @MatrixVariableN @CookieValue、@RequestBody



> @PathVariable 如果方法参数是Map，那么会将路径中的所有变量添加到map中。

![image-20220328164449779](https://raw.githubusercontent.com/Dreameo/JavaLine/master/7_SpringBoot2/imgs/pathvariable-map.png)

> @RequestParams 同理 

![](https://raw.githubusercontent.com/Dreameo/JavaLine/master/7_SpringBoot2/imgs/%2540CookieValue.png)

<font color='red'>问题：谷歌浏览器一直出不了cookie</font>

> 矩阵参数@MatrixVariable

<font color='red'>面试题：</font>
页面开发，cookie禁用了，session里面的内容怎么使用；

路径重写传递jsessionid, 把cookie的值使用矩阵变量的方式传递。分号前面是真正的请求，分号后面是携带的是矩阵变量。

```text
/cars/{path}?xxx=xxx&aaa=ccc    queryString</ 查询字符串。@RequestParam；<br/>
/cars/sell;low=34;brand=byd,audi,yd  ；矩阵变量 <br/>
页面开发，cookie禁用了，session里面的内容怎么使用；
session.set(a,b)---> jsessionid ---> cookie ----> 每次发请求携带。
url重写：/abc;jsesssionid=xxxx 把cookie的值使用矩阵变量的方式进行传递.
```



要实现MatrixVariable我们要定制化组件（加入组件）：两种写法

```java
@Configuration(proxyBeanMethods = false)
public class WebConfig implements WebMvcConfigurer {

    @Bean // 改变默认的隐藏HTTP参数
    public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
        HiddenHttpMethodFilter methodFilter = new HiddenHttpMethodFilter();
        methodFilter.setMethodParam("_m");
        return methodFilter;
    }
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        // 不移除；后面的内容。矩阵变量功能就可以生效
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
}

```



    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
    UrlPathHelper urlPathHelper = new UrlPathHelper();
    // 不移除；后面的内容。矩阵变量功能就可以生效
    urlPathHelper.setRemoveSemicolonContent(false);
    configurer.setUrlPathHelper(urlPathHelper);
    }







```java
//1、语法： 请求路径：/cars/sell;low=34;brand=byd,audi,yd
//2、SpringBoot默认是禁用了矩阵变量的功能
		//手动开启：原理。对于路径的处理。UrlPathHelper进行解析。
				//(移除分号内容）支持矩阵变量的removeSemicolonContent
		//3、矩阵变量必须有url路径变量才能被解析
    
@GetMapping("/cars/{path}")
public Map carsSell(@MatrixVariable("low")Integer low,
@MatrixVariable("brand")List〈String〉brand,
@PathVariable("path")String path){

```



> 参数处理原理：(@RequstMapping例子)

- HandlerMapping中找到能处理请求的Handler (Controller.methodO) 
- 为当前Handler 找一个适配器 HandlerAdapter; <font color='red'>RequestMappingHandlerAdapter</font>
-  适配器执行目标方法并确定方法参数的每一个值

```JAVA
//1. 找到能处理的适配器（循环找到）RequestMappingHandlerAdapter

// 2. Actually invoke the handler.真正处理
mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

// 3. RequestMappingHandlerAdapter类中处理Handler方法
mav = invokeHandlerMethod(request, response, handlerMethod);

// 调用
invocableMethod.invokeAndHandle(webRequest, mavContainer);

// 3. 真正执行方法： ServletInvocableHandlerMethod.java类 确定每个参数的值是多少
Object returnValue = invokeForRequest(webRequest, mavContainer, providedArgs);

```



> 确定将要执行的目标方法的每一个参数的值是什么； SpringMVC目标方法能写多少种参数类型。取决于参数解析器。

参数解析器：

![image-20220328184010857](https://raw.githubusercontent.com/Dreameo/JavaLine/master/7_SpringBoot2/imgs/parameter_resolver.png)



> 当前解析器是否支持解析这种参数 • 支持就调用 resolveArgument



> Servlet API   WebRequest、ServletRequest、MultipartRequest、 HttpSession、 javax.servlet.http.PushBuilder、Principal、InputStream、Reader、HttpMethod、 Locale、 TimeZone、Zoneld

ServletRequestMethodArgumentResolver 以上的部分参

```java
@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> paramType = parameter.getParameterType();
		return (WebRequest.class.isAssignableFrom(paramType) ||
				ServletRequest.class.isAssignableFrom(paramType) ||
				MultipartRequest.class.isAssignableFrom(paramType) ||
				HttpSession.class.isAssignableFrom(paramType) ||
				(pushBuilder != null && pushBuilder.isAssignableFrom(paramType)) ||
				(Principal.class.isAssignableFrom(paramType) && !parameter.hasParameterAnnotations()) ||
				InputStream.class.isAssignableFrom(paramType) ||
				Reader.class.isAssignableFrom(paramType) ||
				HttpMethod.class == paramType ||
				Locale.class == paramType ||
				TimeZone.class == paramType ||
				ZoneId.class == paramType);
	}
```





> 响应json

```java
	<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
        
    //web场景自动引入了json场景
    <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-json</artifactId>
    <version>2.3.4.RELEASE</version>
    <scope>compile</scope>
    </dependency>
```







### 2.5 开发小技巧

> 安装lombok插件,简化JavaBean开发, 还能引入slf4j日志

各注解功能：

https://www.cnblogs.com/ooo0/p/12448096.html

```xml
<!--pom文件中添加依赖-->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

> dev-tools : 热更新，但是只是重启，项目或者页面修改以后CTRL + F9

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <optional>true</optional>
</dependency>
```

> 创建新项目的时候可以选择springboot initializr项目初始化向导，按需要加载场景，比如web、数据库、缓存。

## SpringBoot2核心功能

### 3、配置文件

#### 1、文件类型

> 有properties，yaml（可以简写yml都能识别）

#### 2、yaml

> AML 是 "YAML Ain't Markup Language"（YAML 不是一种标记语言）的递归缩写。在开发的这种语言时，YAML 的意思其实是："Yet Another Markup Language"（仍是一种标记语言）。 

非常适合用来做以<font color='red'>数据为中心</font>的配置文件

1. 基本语法
   - key：value； kv之间有空格
   - 大小写敏感
   - 使用缩进表示层级关系
   - 缩进不允许使用tab，只允许空格(idea使用tab也没事)
   - 缩进的空格数不重要，只要相同层级的元素左对齐即可
   - '#'表示注释
   - 字符串无需加引号，如果要加，<font color='red'>''与""表示字符串内容 会被 转义/不转义</font>

2. 数据类型

​		<font color='red'>字面量</font>：单个的、不可再分的值。<font color='red'>date、boolean、string、number、null</font>

```yaml
k:v
```

​		<font color='red'>对象</font>：键值对的集合。<font color='red'>map、hash、set、object </font>

```yaml
#行内写法： 
k: {k1:v1,k2:v2,k3:v3}
#或
k: 
  k1: v1
  k2: v2
  k3: v3
```

 	<font color='red'>数组</font>： 一组按次序排列的值。<font color='red'>array、list、queue</font>

```yaml
#行内写法：  
k: [v1,v2,v3]
#或者
k:
 - v1
 - v2
 - v3
```

```java
package com.yfh.springboot03yaml.bean;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Data
@ToString
@ConfigurationProperties(prefix = "person") // 记得要与配置文件前缀绑定
public class Person {
    private String userName;
    private Boolean boss;
    private Date birth;
    private Pet pet;
    private String[] interest;
    private List<String> animal;
    private Map<String, Object> score;
    private Set<Double> salarys;
    private Map<String, List<Pet>> allPets;
}

```

```yaml
# person对象测试
person:
  userName: zhangsan
  boss: true
  birth: 2020/09/08
  age: 20
  pet:
    name: xiaoming
    weight: 22.0
  interest: [篮球,羽毛球]
  animal:
    - jerry
    - morry
  score:
    english: [12,34,56] #数组
    math:
      first: 30
      second: 20
      third: 33
    chinese: {first: 20, second: 20}
  salarys: [2222,344.23,44324]
  allPets:
    sick:
      - {name: tom22, weight: 20.0}
      - {name: tom23, weight: 90.0}
    health: [{name: tom24, weight: 22.0},{name: ttt}]

```

> 自己写的类，与配置绑定时，写yaml配置未见，没有自动提示，而springboot内置都有提示

复制依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

### 4、Web开发

#### 4.1 静态资源访问

```text
只要静态资源放在类路径下： /static (or /public or /resources or /META-INF/resources
访问 ： 当前项目根路径/ + 静态资源名
```

>  原理： 静态映射/**。请求进来，先去找Controller看能不能处理。不能处理的所有请求又都交给静态资源处理器。静态资源也找不到则响应404页面

![image-20220326175157441](https://raw.githubusercontent.com/Dreameo/JavaLine/master/7_SpringBoot2/imgs/static-images.png)



> 也可以改变默认的静态资源路径

```yaml
spring:
  mvc:
    static-path-pattern: /res/** #改变默认请求映射
    # 当前项目 + static-path-pattern + 静态资源名 = 静态资源文件夹下找
  web:
    resources:
      static-locations: # 改变静态资源存放位置，可以放多个位置，所以是一个数组配置
        [classpath:/test/]
```

> 自动映射 /[webjars](http://localhost:8080/webjars/jquery/3.5.1/jquery.js)/**
>
> 将一些前端常用资源以jar的形式来使用



> 建议：静态资源路径可以不自定义设置，使用默认，但是尽量配置一个访问前缀

#### 4.2 欢迎页

- 静态资源路径下  index.html

- - 可以配置静态资源路径
  - 但是不可以配置静态资源的访问前缀。否则导致 index.html不能被默认访问

- > favicon.ico 放在静态资源目录下即可,页面标题前面的图标

```yaml
spring:
#  mvc:
#    static-path-pattern: /res/**   这个会导致 Favicon 功能失效
```







#### 4.5 视图解析和模板引擎

> 因为Springboot默认打包方式是jar，jsp是不支持在压缩包里编译的，因此Springboot默认不支持jsp，需要引入第三方模板引擎技术实现页面实现

> AdminEx后台管理模板，基于SpringBoot实现功能

##### 4.5.1 引入静态资源

> 下载网址：https://gitee.com/coderdlg/adminexhttps://gitee.com/coderdlg/adminex

##### 4.5.2 登录与首页

> 是否登录，一般使用拦截器或者过滤器

thymeleaf文本行内写法：<p>Hello, [[${session.user.name}]]!</p>

##### 4.5.3 抽取公共部分

> 分析公共部分-->进行抽取



拦截器：

> 使用步骤：1. 配置拦截器要拦截哪些请求  2. 把这些配置放在容器中

> 拦截器配了/**，静态资源请求也会被篮球

SpringBoot中使用拦截器：

1. 编写一个拦截器实现HandlerInterceptor接口
2. 拦截器注册到容器中（实现webmvcconfigurer的addInterceptor方法）
3. 指定拦截规则，如果是拦截所有，静态资源也会被拦截，



文件上传：(多文件上传在input中加上属性multiple)

> ```html
> <form role="form" th:action="@{/upload}" method="post" enctype="multipart/form-data">
> ```

> 文件上传有默认最大限制，所以可以在配置文件中进行配置。



> 异常处理机制

![image-20220327212412432](https://raw.githubusercontent.com/Dreameo/JavaLine/master/5_SSM/2_SpringMVC/imgs/error.png)

> SpringBoot在error目录下4xx/5xx可以自定义页面





web三大原生注解注入(Servlet/Filter/Listener)

第一种方式：

@ComponetScan和各组件的组件的原生注解一起使用

> 拦截请求路径区别/*是servlet的写法，而/**是Spring的写法，表示拦截所有

第二中方式：

RegistrationBean

![image-20220327214552350](https://raw.githubusercontent.com/Dreameo/JavaLine/master/5_SSM/2_SpringMVC/imgs/RegistBean.png)

> 细节：
>
> 1. 在这个配置类上写上（proxyBeanMethods = false）功能不会出现问题，但是会出现会出现冗余的组件因此应该保证单实例（proxyBeanMethods=true)
> 2. 为什么我们自己写的原生Servlet请求，没有经过Spring拦截器？

![image-20220327220054012](https://raw.githubusercontent.com/Dreameo/JavaLine/master/5_SSM/2_SpringMVC/imgs/DispatcherServlet-TomcatServlet.png)





定制开发：









### 5、数据访问

#### 5.1 数据源

> SpringBoot默认数据源

> Druid数据源



#### 5.2 mybatis

加入mybatis-spring starter

> 配置模式

```java
// Mybatis自动配置类，因此再全局配置文件中以前缀mybatis进行配置
@EnableConfigurationProperties(MybatisProperties.class) ： MyBatis配置项绑定类。
@AutoConfigureAfter({ DataSourceAutoConfiguration.class, MybatisLanguageDriverAutoConfiguration.class })
public class MybatisAutoConfiguration{}

@ConfigurationProperties(prefix = "mybatis")
public class MybatisProperties
```



```yaml
# 可以修改配置文件中 mybatis 开始的所有；
mybatis:
#  config-location: classpath:mybatis/mybatis-config.xml  # 这个mybatis配置文件可以忽略不写，再configuration中进行配置（比如驼峰、懒加载之类的配置可以再configuration中进行配置）
  mapper-locations: classpath:mybatis/mapper/*.xml
  configuration:
    map-underscore-to-camel-case: true
  type-aliases-package: com.yfh.boot.pojo.*
```



```java
@Mapper // Mapper接口需要注明Mapper注解
public interface UserMapper {
    User getUserById(Integer id);
}

```

```java
// 测试，service根据mapper接口写
@Autowired
UserService userService;

@Test
public void testUserMapper() {
    User user = userService.getUserById(1);
    System.out.println(user);
}
```





> 注解模式 ， 查询条件简单可以使用这种方式

```java
@Mapper
public interface UserMapper {

    @Select("select * from t_user where id=#{id}")
    public User getById(Integer id);

    public void insert(User user);

}
```



> 混合模式，当查询语句有简单有复杂时，可以使用这种方式，就是mapper映射文件也存在，注解模式也存在。

如果接口比较多，可以在主程序中加入注解简化：@MapperScan("com.yfh.boot.mapper") 



#### 5.3 mybatis-plus

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.4.1</version>
</dependency>
```

- MybatisPlusAutoConfiguration 配置类，MybatisPlusProperties 配置项绑定。**mybatis-plus：xxx 就是对****mybatis-plus的定制**
- **SqlSessionFactory 自动配置好。底层是容器中默认的数据源**
- **mapperLocations 自动配置好的。有默认值。****classpath\*:/mapper/\**/\*.xml；任意包的类路径下的所有mapper文件夹下任意路径下的所有xml都是sql映射文件。  建议以后sql映射文件，放在 mapper下**
- **容器中也自动配置好了** **SqlSessionTemplate**
- **@Mapper 标注的接口也会被自动扫描；建议直接** @MapperScan(**"com.atguigu.admin.mapper"**) 批量扫描就行
- 只需要我们的Mapper继承 **BaseMapper** 就可以拥有crud能力



**classpath和classpath*区别：**

参考：https://www.cnblogs.com/vickylinj/p/9475990.html

```xm
<bean id="sessionFactorySaas" class="org.mybatis.spring.SqlSessionFactoryBean">  
        <property name="dataSource" ref="dataSourceSaasdb"/>
        <!-- mapper和resultmap配置路径 -->
        <property name="mapperLocations" value="classpath*:mapper/**/saas.*.xml" />
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
</bean>
```

classpath：

- 只会到你的class路径中查找找文件；
- 有多个classpath路径，并同时加载多个classpath路径的情况下，只会从第一个classpath中加载。

classpath*：

- 不仅包含class路径，还包括jar文件中（class路径）进行查找；
- 有多个classpath路径，并同时加载多个classpath路径的情况下，会从所有的classpath中加载；
- 用classpath*:需要遍历所有的classpath，所以加载速度是很慢的；因此，在规划的时候，应该尽可能规划好资源文件所在的路径，尽量避免使用classpath*。

#### 5.4 NoSQL

