package com.jw;
import com.jw.httpserver.httpserver;
import com.jw.httpclients.httpclient;

import java.io.IOException;

public class Test {

    //send HTTP GET Request
    public void testSendGet(httpclient httpclient) throws IOException {
        //httpserver.start();
        //httpclient client = new httpclient("localhost",8080);
        httpclient.sendGet("/");
    }

    //301 Moved Permanently
    public void testSend301(httpclient httpclient) throws IOException {
        //httpserver.start();
        //httpclient client = new httpclient("localhost",8080);
        httpclient.sendGet("/test5.html");
    }

    //302 Foundsername is not exis
    public void testSend302(httpclient httpclient) throws IOException {
        //httpserver.start();
        //httpclient client = new httpclient("localhost",8080);
        httpclient.sendGet("/test4.html");
    }

    //TODO:304 Not Modified
    public void testSend304(httpclient httpclient) throws IOException {

    }

    //404 Not Found
    public void testSend404(httpclient httpclient) throws IOException {
        //httpserver.start();
        //httpclient client = new httpclient("localhost",8080);
        httpclient.sendGet("/test6.html");
    }

    //405 Method Not Allowed
    public void testSend405(httpclient httpclient) throws IOException {
        //httpserver.start();
        //httpclient client = new httpclient("localhost",8080);
        httpclient.wrongMethodSend("/");
    }

    //500 Internal Server Error
    public void testSend500(httpclient httpclient) throws IOException {
        //httpserver.start();
        //httpclient client = new httpclient("localhost",8080);
        httpclient.sendGet("/servlet/logins");
    }

    public void testSendGetLogIn(httpclient httpclient) throws IOException {
        //httpserver.start();
        //httpclient client = new httpclient("localhost",8080);
        httpclient.sendGet("/servlet/login?username=admin&password=123456");
    }

    public void testSignupPost(httpclient httpclient) throws IOException {
        //httpserver.start();
        //httpclient client = new httpclient("localhost",8080);
        httpclient.sendPost("/servlet/signup","username=admin&password=123456");
    }

    public void testLoginPost(httpclient httpclient) throws IOException {
        //httpserver.start();
        //httpclient client = new httpclient("localhost",8080);
        httpclient.sendPost("/servlet/login","username=admin&password=123456");
    }

    public static void main(String[] args) throws IOException {
        Test test = new Test();
        httpserver.start();
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
    }


}
