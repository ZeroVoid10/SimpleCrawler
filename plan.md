# Simplecrawler Project

## 项目目标

- 开篇废话
  - 如果老师要求只是实现爬虫功能,可以使用别人框架,那么开源参考项目中Java实现的,拿一个出来,用Jsoup写个html的解析,基本就好了.
  - 当然写一个爬虫框架,也是重复造轮子,还是一个没什么价值比较简单的轮子,没什么软用.
  - **!!!后面所有内容仅做作业实现参考且待补充!!!**
- 基本描述
  - 核心
    - 使用Java完成一个简单爬虫框架.
    - 并使用该框架完成几个爬虫示例.
    - 能够实现简单静态爬虫需求.
    - 单线程运行, 可多线程下载提高性能.
    - 实现`Tomcat`服务器部署
      - `Html，CSS`网页设计
      - 网页API设计(具体功能的爬虫API, 爬虫配置API等)
  - 完善（如果有时间有精力提升性能的话）
    - 动态爬虫实现
    - 多线程分布式(大坑)
      - 这一块, 我看的比较少,还没有一个完整的思路.可以考虑一些`Java`并发框架[akka](https://akka.io/).
      - 多线程: 简单实现应该可以通过`Java JDK`自带的`concurrent`包来完成 进一步的话就是考虑一些开源框架[RxJava(ReactiveX的Java实现)](http://reactivex.io/)等)
      - 分布式: 
        - 服务器,客户端通信可以考虑`netty`框架通信
        - `RPC(Remote Procedure Call Protocol`远程过程调用框架,可能需要了解或者使用
  - 注意事项
    - 反爬虫技术
- 可参考开源项目
  - [webmagic](https://github.com/code4craft/webmagic)：`Java` 一个国人业余开发的爬虫框架(参考Scrapy写的)
  - `crawler4j`：`Java`
  - `nutch`：`Java`
    - 搜索引擎定制的爬虫, 粗放爬取数据.
  - `scrapy`：`Python` GitHub Start最多的开源爬虫框架
  - [pyspider](https://github.com/binux/pyspider): `Python`
  - `you-get`：`Python`
  - `Jsoup`：`Java`实现，`Html`解析器
  - **大雾(滑稽)**: [awesome-spider](https://github.com/facert/awesome-spider)一些**精致**的精确爬虫.
  
### 项目相关细节提议

- 建议开源代码，并使用`GPLv3`开源协议
  - 可参考[GitHub协议选择](https://choosealicense.com/licenses/)(禁止闭源；修改发布必须同协议且文档说明)
- 代码仓库：GitHub，[我的公开仓库](https://github.com/ZeroVoid10/SimpleCrawler/blob/master/LICENSE)：`SimpleCrawler`
  - 使用`Gradle`项目管理(Eclipse可导入`Gradle`项目)
  - 需要使用git进行代码合并(仅需要git的简单多人合作使用能力)
  - `push`到`develop`分支
- Java版本：1.8（我使用的openJDK 1.8.0）
  - 我们使用范围内和Oracle JDK同版本之间应该基本没有区别
- `Log`
  - 使用`slf4j - Simple Logging Facade for Java`日志接口
  - 使用`LogBack - Logging Framework for Java`日志框架
  - 日志等级设置`debug`等级
- 爬取数据质量
  - 这个应该属于数据分析了吧.就不考虑了吧.

## 项目实现/TODO

### 组成

- `Spider` 程序入口，实现分布式与多线程。
- `Engine` 系统组件之间调度协同，数据流控制，与事件触发
- `Parser` 网页数据解析提取parse/extract Item
  - `Parser Middleware` (可完善)`scrapy`框架中有的,没用过.
- `Downloader` 下载器，实现与互联网通信，实现初始信息获取
  - `Downloader Middleware` (可完善)也没用过
- `Scheduler` 从Engine获取URL，调度URL给Downloader，维护URL池
- `Item pipeline` 数据存储。本地文件存储，或与数据库链接，将Spider解析后的数据保存到数据库中.(建议可使用非关系数据库(NoSql),当然什么关系数据库(MySql Mariadb)也是可以的)
- `Item` 解析后的数据封装类
- `Config` 各组件配置数据封装类
- `Request` 包含`URL`等信息的数据封装
- `Response/Page` 网页下载后`html`等信息的数据数据封装类
- `Utils` 一些工具类
  - `url`处理
  - `selector` `html`解析工具（可选）。`Jsoup`可以完全实现。

### Spider

- 单Engine,单线程 -- 基础完整的爬虫(目前主要考虑)
  - 进行对爬虫/Engine(其组件)的配置(Config)
  - 运行入口
- 单Engine,多线程
- 多Engine多线程
- 多线程与分布式可能需要主要考虑的组件,协调爬虫主控服务器与爬虫节点等

### `Engine` 爬虫核心

处理各组件之间通信与调度。多线程设计时,应该会涉及到Java并发编程(jdk中`concurrent`包或者`akka`框架).

[注] 本节内容中所有有关多线程与分布式内容均为(可能无法简单实现,需要重构的[卒])设想.关于实现多线程和分布式对目前的框架实现思路的耦合影响没有过多的深入思考.

- 主要组件 `Parser, Downloader, Scheduler, Pipeline`
- 主要数据封装类 `Request，Response，Item, EngineConfig`
- 对`Parser`类型个数的想法(详见`Parser`部分)(或者都实现)
  - 一种 `SignalParserEngine`, 一种`Parser`但可以有多个, 多个为多线程时使用.
  - 多种 `MultiParserEngine`, 每种`Parser`也可以有多个. 
    - 可多线程,也可单线程.需要通过`Spider Middleware`进行粗解析,进而选择`Parser`类型
    - 通过`HashMap<String, ArrayList<Parser>>`存储,关键在于实现`Parser`指引的正确向下转型
- 一般含有多种`Pipeline`, 对`Parser`处理后的不同类型`Item`

### `Parser`

- 关键功能
  - 决定爬虫爬取数据质量的结构或非结构化的数据提取.
- 结构描述
  - 包含`static final`的唯一的`name/uid`,用于识别解析类
  - 一般按照`Response`要求,对`Response`中`html`进行解析.可能的实现方法:
    - 通过抽象类中定义`parse`方法(具体实现需要重载), 来识别`Response`要求(粗/初解析),然后调用类中特定`parse`方法进行精/再解析.
  - 对`html`的解析, 可以先由通过`Jsoup`来实现.
    - 之后有时间再慢慢用`java.util.regex`的正则包来提供解析工具
  - 解析后获得的`URI`需要通过`uriRender`获取可下载的`url`,并根据配置信息,生产新的`Request`
  - 一般返回一个`(Item, ArrayList<Request>)`对.(可以通过`Apache Commons Collections`包中的`DefaultKeyValue`实现, 或者设计一个通过泛型实现的封装类)
  - 可能需要使用`Jsoup, java.util.regex`等来实现解析
  - 正则

### `Downloader`

- 可线程池多线程并发下载网页,提高性能,减少`I/O`阻塞问题
- 使用`HttpClient`实现对网页下载
- ip代理功能(可选,反爬虫技术)
- `Java.net` `apache.httpcomponents中HttpClient`等

### `Schedule`

- 关键功能
  - `URL`去重
    - `HashSet`实现
    - `Bloom Filter`实现等
  
### `Item Pipeline`

- 结构描述
  - 提供`Media Pipeline`: 对图片,音频,视频等`URI`的Item,进行下载存放.(还考虑过这种`URI`放在`Request`当做`Request`处理,由`Downloader`下载让后通过`Engine`交由`Pipeline`存储)
  - 可以使用线程池,提高性能减少`I/O`阻塞
  - 一些本地文件存储(Json, XML格式等)
  - 一些数据库操作实现
    - 建表,插入数据等
- `java.sql`,文件读写
- 数据库操作

### `Request`

- `URL`
- `uriReader`所需信息

### `Response`

- 包含获取指定`Parser`类的方法(返回`Parser 的 name`)
- `html`内容
- 提供`uriRender`所需的信息
- `http`协议中`response`的一些`Header`信息

### `Config`

- 应包含生成组件的所有配置信息.
- 应能获得组件默认配置信息

## 关于`SimpleSpider`的示例说明

就是一个粗糙的(非常粗糙以至于我都有点不忍直视),最简单的,实现过程.需要大量的抽象等过程,来产生可复用代码.而且有许多实现地方过去简单,非常难以使用.

- `SimpleParser`实现了页面标题提取,与主要内容中的`URL`获取.
- 并设置初始`URL`为萌娘百科的*卡罗尔与星期二*页面
- `SimpleDownloader`啥都没有设置(不对设置了一个`Agent`)的粗暴下载器
- `SimpleEngine` 粗糙到难以入目的组件结合过程
- `SimplePiepeline`仅是将数据在`STDOUT`上打印而已(我都看不下去了)
- `SimpleSpider` 是的这个看起来就是个没有的东西,仿佛仅仅只是`Engine`的包装[卒]
