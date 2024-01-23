# SimpleHTTP Server and Client

## 小组信息
|  姓名  |   学号    |     分工     |
| :----: | :-------: | :----------: |
| 张博阳 | 211840196 |              |
| 张雨姗 | 211503026 |              |
| 胡志杰 | 201830160 | 撰写说明文档 |
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
8.  支持用户注册与登陆

## 目录结构
项目目录结构如下：
```
│  .gitignore
│  httpserver01.iml
│  list.txt
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
                ├─httpclients
                │      TestDemo.java
                │      
                └─httpserver
                        httpserver.java
                        loginservlet.java
                        servlet.java
```
*对项目目录结构进行简要说明*

## 代码说明
1. *模块描述、接口描述*
2. *对实现的7个功能逐一进行代码说明*

## 测试用例
*展示并说明测试用例结果*