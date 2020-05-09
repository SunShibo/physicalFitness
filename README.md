#### 框架已经实现的通用功能说明
1. 通过redis实现了分布式锁，主要用于存在多线程并发。使用方式：调用RedisLockService.java类的中得到锁和释放锁。

2. 验证码+限制登录错误次数 功能
   2.1 应用场景  登录界面、表单提交界面
	
3. 日志过程跟踪	

4. 原生sql输出(spy)
   4.1 应用场景  想知道某个查询功能执行的sql语句是什么。
	
5. 对从登录页面提交的密码加密传输到后台，后台解密后再对用户名和密码进行校验。	

6. 基于excel模板通过第三方包itext导出PDF文件（模板中提供了实现实例，项目中使用时参照使用即可）。

7. 生成ZIP压缩文件的工具包。

8. 百度富文本编辑框(对文件上传功能做了适应spring boot框架的改造)。

#### 常用环境设置方法
##### eclipse查看jar包中class的中文注释乱码问题的解决
1. 问题来源是在eclipse中直接查看springside的class(由eclipse自动反编译)里面注释的乱码问题:

Preferences-General-Workspace-Text file encoding 设置为uft-8

最后重启一下eclipse通常就ok了。
 
2. 若是关联jar的源码出现乱码,则使用以下方法尝试:

将Eclipse的Preferences中的General》ContentTypes中的Java Class File和Text的default encoding都设置成了UTF-8（或者是其他你需要设置的编码格式）
重新关联一次！如果还不行,尝试把源码jar包换了地方重新关联一次.

##### maven 添加本地jar包到maven库中（添加sqlserver包为例）
> 使用场景：
http://search.maven.org/中没有4.2及以上版本的sqlserver的jdbc驱动，所以需要本地安装sqljdbc的jar包，然后再在pom里面引入

第一步：

　　在微软官网下载sqljdbc的jar包：http://www.microsoft.com/en-us/download/details.aspx?displaylang=en&id=11774
　　本次下载了4.2版本 ，可以支持jre8

第二步：

    通过maven命令将jar包安装到本地。
　　在有sqljdbc4.jar包的文件夹下，通过shift+右键的方式--》此处打开命令窗口，然后执行以下maven命令
　　mvn install:install-file -Dfile=sqljdbc42.jar -Dpackaging=jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=4.2　　
　　命令解释：mvn install:install-file -Dfile="jar包的绝对路径" -Dpackaging="文件打包方式" -DgroupId=groupid名 -DartifactId=artifactId名 -Dversion=jar版本

第三步：

    在pom.xml中引入本地jar包
    <dependency>
		<groupId>com.microsoft.sqlserver</groupId>
		<artifactId>sqljdbc4</artifactId>
		<version>4.2</version>
	</dependency>

##### xxx.properties 属性文件中的汉字被自动转为ASC码问题
原因是因为eclipse中 XXXX.properties文件默认的编码方式是iso-8859-1

1、window --> General --> Content Types -->(右边框中) 选择Text --> Java Properties Files(点击)。

2、会看见了该界面中的File association 中出现了 *.properties(locked) (点击选中) 。

3、会看见了该界面最下方的 Default encoding: iso-8859-1。

4、将iso-8859-1 手动改成 UTF-8。

5、点击右边的 update，点击 Apply and Close 按钮。

##### 命令行下maven工程打jar包或war包
* mvn package
* mvn clean package
```
E:\apache-maven-3.2.3\bin\mvn clean package spring-boot:repackage
#java -jar -Xms1024M -Xmx2048M upms-1.0.jar   --spring.profiles.active=dev  --server.port=
java -jar -Xms1024M -Xmx2048M upms-1.0.jar    --server.port=8089
```

#### 常用的 lombok 注解

@Data   ：注解在类上；包含了@ToString，@EqualsAndHashCode，@Getter / @Setter和@RequiredArgsConstructor的功能，提供类所有属性的 getter 和 setter 方法，此外还提供了equals、canEqual、hashCode、toString 方法

@Setter：注解在属性上；为属性提供 setter 方法

@Getter：注解在属性上；为属性提供 getter 方法

@ToString：注解在类上；生成toString()方法，默认情况下，它会按顺序（以逗号分隔）打印你的类名称以及每个字段。可以这样设置不包含哪些字段@ToString(exclude = "id") / @ToString(exclude = {"id","name"})

如果继承的有父类的话，可以设置callSuper 让其调用父类的toString()方法，例如：@ToString(callSuper = true)

@EqualsAndHashCode：注解在类上；生成hashCode()和equals()方法，默认情况下，它将使用所有非静态，非transient字段。但可以通过在可选的exclude参数中来排除更多字段。或者，通过在parameter参数中命名它们来准确指定希望使用哪些字段。

@NonNull：  注解在属性上；标识属性是不能为空，为空则抛出异常。

@Slf4j ：注解在类上；根据用户实际使用的日志框架生成log日志对象。

@Log4j ：注解在类上；为类提供一个 属性名为log 的 log4j 日志对象

@NoArgsConstructor：注解在类上；为类提供一个无参的构造方法。当类中有final字段没有被初始化时，编译器会报错，此时可用@NoArgsConstructor(force = true)，然后就会为没有初始化的final字段设置默认值 0 / false / null。对于具有约束的字段（例如@NonNull字段），不会生成检查或分配，因此请注意，正确初始化这些字段之前，这些约束无效。

@AllArgsConstructor：注解在类上；为类提供一个全参的构造方法

默认生成的方法是public的，如果要修改方法修饰符可以设置AccessLevel的值，例如：@Getter(access = AccessLevel.PROTECTED)

@RequiredArgsConstructor：注解在类上；会生成构造方法（可能带参数也可能不带参数），如果带参数，这参数只能是以final修饰的未经初始化的字段，或者是以@NonNull注解的未经初始化的字段@RequiredArgsConstructor(staticName = "of")会生成一个of()的静态方法，并把构造方法设置为私有的。

#### OAuth认证测试过程
1、获取授权码code
浏览器访问
http://localhost:8080/oauth/authorize?response_type=code&client_id=client-test&redirect_uri=http://localhost:8080

2、用code换取access_token
POST数据到 http://localhost:8080/oauth/token 
参数 grant_type=authorization_code&code=75260p，注意code每次是不一样的
将client_id:client_secret进行BASE64编码本范例中的client_id是client-test，密码是secret，即base64(client-test:secret)，得到的结果是Y2xpZW50LXRlc3Q6c2VjcmV0
放入Header中 Header: Authorization: Basic Y2xpZW50LXRlc3Q6c2VjcmV0

curl -H "Authorization: Basic Y2xpZW50LXRlc3Q6c2VjcmV0" -d "grant_type=authorization_code&code=R3V0BI&redirect_uri=http://localhost:8080" -X POST http://localhost:8080/oauth/token

返回：
{"access_token":"75cd3b5a-6f53-4faf-ad09-953a50b777fd","token_type":"bearer","refresh_token":"fadeb7ec-dd15-4513-a563-3ba3d90fed48","expires_in":7775999,"scope":"user_info_get user_info_update user_pwd_update"}

//spring boot 已经实现的 response head，供参考
//包目录在：org.springframework.security.web.header.writers 

#### spring boot 2.0后，控制上传文件大小的配置方式
spring.servlet.multipart.max-file-size = 10MB  
spring.servlet.multipart.max-request-size=100MB

#### 模板运行说明
第一步，在mysql中创建名为 project_tpl的数据库（utf-8）。

第二步，用navicat连接第一步创建的数据库；

第三步，导入SQL文件  project_tpl.sql。

未升级的初始的账号为  admin  密码为 111111
升级后初始的账号为  admin1  密码为 111111

#### maven 打包并执行
```$xslt
E:\apache-maven-3.2.3\bin\mvn clean package spring-boot:repackage
#java -jar -Xms1024M -Xmx2048M upms-1.0.jar   --spring.profiles.active=dev  --server.port=
java -jar -Xms1024M -Xmx2048M upms-1.0.jar    --server.port=8089
```

#### nest jar包
框架中的百度文本编辑器上传的类和读取yml中的sql语句的代码，以及一些操作excel的封装的代码，已移到nest项目单独维护。使用时，可以将nest项目的打包成jar包，放到公司和本地仓库，然后通过maven引入使用。
```cmd
# maven将jar包添加到本地仓库命令（jar包的路径自己修改）
mvn install:install-file -Dfile=D:\Project\javaWorkspace\chzh.cn\PROJECT_TPL\nest\target\nest-1.0.jar -Dpackaging=jar -DgroupId=com.ichzh -DartifactId=nest -Dversion=1.0
```

```xml
<!-- nest的jar包，公司仓库 -->
<dependency>
    <groupId>com.ichzh</groupId>
    <artifactId>nest</artifactId>
    <version>1.0</version>
</dependency>
```