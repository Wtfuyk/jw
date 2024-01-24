package com.jw;
import com.jw.httpserver.httpserver;
import com.jw.httpclients.httpclient;

import java.io.IOException;
import java.net.Socket;

public class Test {

    //send HTTP GET Request
    public void testSendGet(httpclient httpclient) throws IOException {
        //httpserver.start();
        //httpclient client = new httpclient("localhost",8080);
        System.out.println(">>>> ---- Test Send Get ---- <<<<");
        Socket testSocket = httpclient.socketConnect();
        httpclient.sendGet(testSocket,"/","close");
        System.out.println(">>>> ---- Test Finished ---- <<<<");
        System.out.println();
    }

    //301 Moved Permanently
    public void testSend301(httpclient httpclient) throws IOException {
        //httpserver.start();
        //httpclient client = new httpclient("localhost",8080);
        System.out.println(">>>> ---- Test Send 301 ---- <<<<");
        Socket testSocket = httpclient.socketConnect();
        httpclient.sendGet(testSocket,"/test5.html","close");
        System.out.println(">>>> ---- Test Finished ---- <<<<");
        System.out.println();
    }

    //302 Foundsername is not exis
    public void testSend302(httpclient httpclient) throws IOException {
        //httpserver.start();
        //httpclient client = new httpclient("localhost",8080);
        System.out.println(">>>> ---- Test Send 302 ---- <<<<");
        Socket testSocket = httpclient.socketConnect();
        httpclient.sendGet(testSocket,"/test4.html","close");
        System.out.println(">>>> ---- Test Finished ---- <<<<");
        System.out.println();
    }

    //TODO: 304 Not Modified
    public void testSend304(httpclient httpclient) throws IOException {

    }

    //404 Not Found
    public void testSend404(httpclient httpclient) throws IOException {
        //httpserver.start();
        //httpclient client = new httpclient("localhost",8080);
        System.out.println(">>>> ---- Test Send 404 ---- <<<<");
        Socket testSocket = httpclient.socketConnect();
        httpclient.sendGet(testSocket,"/test6.html","close");
        System.out.println(">>>> ---- Test Finished ---- <<<<");
        System.out.println();
    }

    //405 Method Not Allowed
    public void testSend405(httpclient httpclient) throws IOException {
        //httpserver.start();
        //httpclient client = new httpclient("localhost",8080);
        System.out.println(">>>> ---- Test Send 405 ---- <<<<");
        Socket testSocket = httpclient.socketConnect();
        httpclient.wrongMethodSend(testSocket,"/","close");
        System.out.println(">>>> ---- Test Finished ---- <<<<");
        System.out.println();
    }

    //500 Internal Server Error
    public void testSend500(httpclient httpclient) throws IOException {
        //httpserver.start();
        //httpclient client = new httpclient("localhost",8080);
        System.out.println(">>>> ---- Test Send 500 ---- <<<<");
        Socket testSocket = httpclient.socketConnect();
        httpclient.sendGet(testSocket,"/servlet/logins","close");
        System.out.println(">>>> ---- Test Finished ---- <<<<");
        System.out.println();
    }

    public void testSendGetLogIn(httpclient httpclient) throws IOException {
        //httpserver.start();
        //httpclient client = new httpclient("localhost",8080);
        System.out.println(">>>> ---- Test Send Get Login ---- <<<<");
        Socket testSocket = httpclient.socketConnect();
        httpclient.sendGet(testSocket,"/servlet/login?username=admin&password=123456","close");
        System.out.println(">>>> ---- Test Finished ---- <<<<");
        System.out.println();
    }

    public void testSignupPost(httpclient httpclient) throws IOException {
        //httpserver.start();
        //httpclient client = new httpclient("localhost",8080);
        System.out.println(">>>> ---- Test Post Sign Up ---- <<<<");
        Socket testSocket = httpclient.socketConnect();
        httpclient.sendPost(testSocket,"/servlet/signup","username=admin&password=123456","close");
        System.out.println(">>>> ---- Test Finished ---- <<<<");
        System.out.println();
    }

    public void testLoginPost(httpclient httpclient) throws IOException {
        //httpserver.start();
        //httpclient client = new httpclient("localhost",8080);
        System.out.println(">>>> ---- Test Post Login ---- <<<<");
        Socket testSocket = httpclient.socketConnect();
        httpclient.sendPost(testSocket,"/servlet/login","username=admin&password=123456","close");
        System.out.println(">>>> ---- Test Finished ---- <<<<");
        System.out.println();
    }

    public void testSendKeepAlive(httpclient httpclient) throws IOException{
        System.out.println(">>>> ---- Test Long Connection ---- <<<<");
        Socket testSocket = httpclient.socketConnect();
        httpclient.sendGet(testSocket,"/test1.html","keep-alive");
        httpclient.sendGet(testSocket,"/test2.html","close");
        System.out.println(">>>> ---- Test Finished ---- <<<<");
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        //httpserver.start();
        Test test = new Test();
        httpclient client = new httpclient("localhost",8080);
        test.testSendGet(client);
        test.testSend301(client);
        test.testSend302(client);
        //test.testSend304(client);
        test.testSend404(client);
        test.testSend405(client);
        test.testSend500(client);
        test.testSendGetLogIn(client);
        test.testSignupPost(client);
        test.testLoginPost(client);
        test.testSendKeepAlive(client);
    }


}
