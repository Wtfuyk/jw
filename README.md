# SimpleHTTP Server and Client

## 小组信息
|  姓名  |   学号    |     分工     |
| :----: | :-------: | :----------: |
| 张博阳 | 211840196 |              |
| 张雨姗 | 211503026 |              |
| 胡志杰 | 201830160 | 代码整合，撰写说明文档 |
| 翟志阳 | 221250161 |              |
| 邹玉麟 | 221830073 |              |

## 项目概述
基于Java Socket API，我们搭建了一个简单的HTTP客户端与服务器端程序，并实现了以下功能：
1.  HTTP客户端可以发送请求报文
2.  HTTP客户端可以通过命令行方式呈现响应报文
3.  HTTP客户端可以对301，303，304状态码进行相应处理
4.  HTTP服务器端支持GET与POST请求
5.  HTTP服务器端支持200、301、302、304、404、405、500的状态码
6.  HTTP服务器端支持长连接
7.  MIME支持text/html、text/css、application/javascript、image/png、image/jpeg、image/gif六种类型
8.  支持以网页形式进行用户注册与登陆

## 目录结构
项目目录结构如下：
```
│  .gitignore
│  httpserver01.iml
│  README.md
│  
├─.idea
│      misc.xml
│      modules.xml
│      vcs.xml
│      workspace.xml
│      
└─jwhttpserver
    │  jwhttpserver.iml
    │  
    └─src
        │  1.jpg
        │  2.jpg
        │  index.html
        │  test1.html
        │  test2.html
        │  test3.html
        │  
        └─com
            └─jw
                │  Test.java
                │  
                ├─httpclient
                │      httpclient.java
                │      
                └─httpserver
                        httpserver.java
                        loginservlet.java
                        servlet.java
                        signupservlet.java
                        user_map.java
```
 
目录结构简要说明：src中存放了文件资源以及源代码，其中文件资源有html与jpg两种格式，源代码分为三部分：客户端httpclient、服务器端httpserver、测试用例Test.java


## 代码说明
### *1. 模块描述*  
* httpclient.java实现客户端程序
* httpserver.java实现服务器端程序
* servlet.java作为抽象类为httpserver.java提供login与signup服务的接口
* loginservlet.java与signupservlet.java均继承servlet.java，并对相关方法进行重写
* user_map.java存储用户数据，支持用户登陆与注册

### *2. 接口描述*

2. *对实现的7个功能逐一进行代码说明*

## 测试用例
*展示并说明测试用例结果*
在Test.java中我们对以下功能进行了测试：
