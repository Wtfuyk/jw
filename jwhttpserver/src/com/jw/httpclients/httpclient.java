package com.jw.httpclients;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class httpclient {
    private Map<String,byte[]> cache = new HashMap<>();
    private int port;
    private String host;
//    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public httpclient(String host, int port) {
//        socket = new Socket();
        this.host = host;
        this.port = port;
    }

    public static void replaceStringInFile(String filePath, String oldString, String newString) {
        try {
            // 读取文件内容
            Path path = Paths.get(filePath);
            List<String> lines = Files.readAllLines(path);

            // 替换字符串
            List<String> modifiedLines = lines.stream()
                    .map(line -> line.replace(oldString, newString))
                    .collect(Collectors.toList());

            // 写回文件
            Files.write(path, modifiedLines);

            System.out.println("字符串替换成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String GetRequest(String path,String connection){
        //TODO:根据cache构造不同请求报文
        String getRequest = "GET " + path + " HTTP/1.1\r\n" +
                "Host: " + this.host + "\r\n" +
                "Connection: "+connection+"\r\n\r\n";
        return getRequest;
    }

    private String GetPost(String path,String data,String connection){
        String postRequest = "POST " + path + " HTTP/1.1\r\n" +
                "Host: " + this.host + "\r\n" +
                "Content-Length: " + data.length() + "\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Connection: "+connection+"\r\n\r\n" +
                data;
        return postRequest;
    }

    public Socket socketConnect() throws IOException {
        SocketAddress dest = new InetSocketAddress(this.host, this.port);
        Socket socket = new Socket();
        socket.connect(dest);
        return socket;
    }

    public void sendGet(Socket socket,String path,String connection) throws IOException{
//        SocketAddress dest = new InetSocketAddress(this.host, this.port);
//        Socket socket = new Socket();
//        socket.connect(dest);

        // Output stream for writing bytes
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        // Write the GET request
        //String getRequest = "GET " + path + " HTTP/1.1\r\n" +
        //        "Host: " + this.host + "\r\n\r\n";
        String getRequest = GetRequest(path,connection);
        dataOutputStream.write(getRequest.getBytes());
        dataOutputStream.flush();

        // Input stream for reading bytes
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        // Read and print the response
        System.out.println("sendGet:Response from the server:");
//        if(Objects.equals(connection,"close")){
            byte[] buffer = new byte[1024];
            int bytesRead;
            bytesRead = dataInputStream.read(buffer);
//        while ((bytesRead = dataInputStream.read(buffer)) != -1) {
            String line = new String(buffer, 0, bytesRead, "UTF-8");
            System.out.print(line);
//        }
//       }

        System.out.println();

        // Close resources
        if (Objects.equals(connection, "close")) {
            dataOutputStream.close();
            dataInputStream.close();
            socket.close();
        }
    }

    public void sendPost(Socket socket, String path, String data,String connection) throws IOException{
        //String data = "username=admin&password=123456";
//        SocketAddress dest = new InetSocketAddress(this.host, this.port);
//        Socket socket = new Socket();
//        socket.connect(dest);

        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        String postRequest = GetPost(path,data,connection);
        dataOutputStream.write(postRequest.getBytes());
        dataOutputStream.flush();

        // Input stream for reading bytes
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        // Read and print the response
        System.out.println("sendPost:Response from the server:");
        byte[] buffer = new byte[1024];
        int bytesRead;
        bytesRead = dataInputStream.read(buffer);
//        while ((bytesRead = dataInputStream.read(buffer)) != -1) {
            String line = new String(buffer, 0, bytesRead, "UTF-8");
            System.out.print(line);
//        }
        System.out.println();

        // Close resources
        if (Objects.equals(connection,"close")){
        dataOutputStream.close();
        dataInputStream.close();
        socket.close();}
    }

    public void wrongMethodSend(Socket socket,String path,String connection) throws IOException{
//        SocketAddress dest = new InetSocketAddress(this.host, this.port);
//        Socket socket = new Socket();
//        socket.connect(dest);

        // Output stream for writing bytes
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        // Write the GET request
        String getRequest = "DELETE " + path + " HTTP/1.1\r\n" +
                "Host: " + this.host + "\r\n" +
                "Connection: "+connection+"\r\n\r\n";
        dataOutputStream.write(getRequest.getBytes());
        dataOutputStream.flush();

        // Input stream for reading bytes
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        // Read and print the response
        System.out.println("sendGet:Response from the server:");
        byte[] buffer = new byte[1024];
        int bytesRead;
        bytesRead = dataInputStream.read(buffer);
        //        while ((bytesRead = dataInputStream.read(buffer)) != -1) {
        String line = new String(buffer, 0, bytesRead, "UTF-8");
        System.out.print(line);
//        }
        System.out.println();

        // Close resources
        if(Objects.equals(connection,"close")){
        dataOutputStream.close();
        dataInputStream.close();
        socket.close();}
    }
    //get index.html
    /*
    public void sendGet() throws IOException {
        String path = "/";
        SocketAddress dest = new InetSocketAddress(this.host, this.port);
        Socket socket = new Socket();
        socket.connect(dest);

        // Output stream for writing bytes
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        // Write the GET request
        String getRequest = "GET " + path + " HTTP/1.1\r\n" +
                "Host: " + this.host + "\r\n\r\n";
        dataOutputStream.write(getRequest.getBytes());
        dataOutputStream.flush();

        // Input stream for reading bytes
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        // Read and print the response
        System.out.println("sendGet:Response from the server:");
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = dataInputStream.read(buffer)) != -1) {
            String line = new String(buffer, 0, bytesRead, "UTF-8");
            System.out.print(line);
        }
        System.out.println();

        // Close resources
        dataOutputStream.close();
        dataInputStream.close();
        socket.close();
    }
    //get log in
    public void sendGetLogIn() throws IOException {
        String path = "/servlet/login?username=admin&password=123456";
        SocketAddress dest = new InetSocketAddress(this.host, this.port);
        Socket socket = new Socket();
        socket.connect(dest);

        // Output stream for writing bytes
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        // Write the GET request
        String getRequest = "GET " + path + " HTTP/1.1\r\n" +
                "Host: " + this.host + "\r\n\r\n";
        dataOutputStream.write(getRequest.getBytes());
        dataOutputStream.flush();

        // Input stream for reading bytes
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        // Read and print the response
        System.out.println("sendGetLogIn:Response from the server:");
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = dataInputStream.read(buffer)) != -1) {
            String line = new String(buffer, 0, bytesRead, "UTF-8");
            System.out.print(line);
        }
        System.out.println();


        // Close resources
        dataOutputStream.close();
        dataInputStream.close();
        socket.close();
    }
    public void loginPost() throws IOException {
        String path = "/servlet/login";
        String data = "username=admin&password=123456";
        SocketAddress dest = new InetSocketAddress(this.host, this.port);
        Socket socket = new Socket();
        socket.connect(dest);

        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        String postRequest = "POST " + path + " HTTP/1.1\r\n" +
                "Host: " + this.host + "\r\n" +
                "Content-Length: " + data.length() + "\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n\r\n" +
                data;
        dataOutputStream.write(postRequest.getBytes());
        dataOutputStream.flush();

        // Input stream for reading bytes
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        // Read and print the response
        System.out.println("sendPost:Response from the server:");
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = dataInputStream.read(buffer)) != -1) {
            String line = new String(buffer, 0, bytesRead, "UTF-8");
            System.out.print(line);
        }
        System.out.println();

        // Close resources
        dataOutputStream.close();
        dataInputStream.close();
        socket.close();
    }
    //post log in
    public void signupPost() throws IOException {
        String path = "/servlet/signup";
        String data = "username=admin&password=123456";
        SocketAddress dest = new InetSocketAddress(this.host, this.port);
        Socket socket = new Socket();
        socket.connect(dest);

        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        String postRequest = "POST " + path + " HTTP/1.1\r\n" +
                "Host: " + this.host + "\r\n" +
                "Content-Length: " + data.length() + "\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n\r\n" +
                data;
        dataOutputStream.write(postRequest.getBytes());
        dataOutputStream.flush();

        // Input stream for reading bytes
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        // Read and print the response
        System.out.println("sendPost:Response from the server:");
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = dataInputStream.read(buffer)) != -1) {
            String line = new String(buffer, 0, bytesRead, "UTF-8");
            System.out.print(line);
        }
        System.out.println();

        // Close resources
        dataOutputStream.close();
        dataInputStream.close();
        socket.close();
    }

    //301 Moved Permanently
    public void send301() throws IOException {
        String path = "/test5.html";
        SocketAddress dest = new InetSocketAddress(this.host, this.port);
        Socket socket = new Socket();
        socket.connect(dest);

        // Output stream for writing bytes
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        // Write the GET request
        String getRequest = "GET " + path + " HTTP/1.1\r\n" +
                "Host: " + this.host + "\r\n\r\n";
        dataOutputStream.write(getRequest.getBytes());
        dataOutputStream.flush();

        // Input stream for reading bytes
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        // Read and print the response
        System.out.println("send301:Response from the server:");
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = dataInputStream.read(buffer)) != -1) {
            String line = new String(buffer, 0, bytesRead, "UTF-8");
            System.out.print(line);
        }
        System.out.println();

        // Close resources
        dataOutputStream.close();
        dataInputStream.close();
        socket.close();
    }

    //302 Found
    public void send302() throws IOException {
        String path = "/test4.html";
        SocketAddress dest = new InetSocketAddress(this.host, this.port);
        Socket socket = new Socket();
        socket.connect(dest);

        // Output stream for writing bytes
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        // Write the GET request
        String getRequest = "GET " + path + " HTTP/1.1\r\n" +
                "Host: " + this.host + "\r\n\r\n";
        dataOutputStream.write(getRequest.getBytes());
        dataOutputStream.flush();

        // Input stream for reading bytes
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        // Read and print the response
        System.out.println("send302:Response from the server:");
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = dataInputStream.read(buffer)) != -1) {
            String line = new String(buffer, 0, bytesRead, "UTF-8");
            System.out.print(line);
        }
        System.out.println();

        // Close resources
        dataOutputStream.close();
        dataInputStream.close();
        socket.close();
    }

    //304 Not Modified
    public void send304()throws IOException{
        String path = "/";
        SocketAddress dest = new InetSocketAddress(this.host, this.port);
        Socket socket = new Socket();
        socket.connect(dest);

        // Output stream for writing bytes
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        // Write the GET request
        String getRequest = "GET " + path + " HTTP/1.1\r\n" +
                "Host: " + this.host + "\r\n\r\n";
        dataOutputStream.write(getRequest.getBytes());
        dataOutputStream.flush();

        // Input stream for reading bytes
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        // Read and print the response
        System.out.println("sendGet:Response from the server:");
        byte[] buffer = new byte[2048];
        int bytesRead;
        StringBuilder responsesb = new StringBuilder();
        while ((bytesRead = dataInputStream.read(buffer)) != -1) {
            responsesb.append(new String(buffer, 0, bytesRead, "UTF-8"));
        }
        String response = responsesb.toString();
        System.out.println(response);

        String etag = response.split("ETag: ")[1].split("\r\n")[0];
        byte[] content = response.split("\r\n\r\n")[1].getBytes();
        cache.put(etag, content);

        // Close resources
        dataOutputStream.close();
        dataInputStream.close();
        socket.close();

        //second request
        Socket socket2 = new Socket();
        socket2.connect(dest);

        // Output stream for writing bytes
        OutputStream outputStream2 = socket2.getOutputStream();
        DataOutputStream dataOutputStream2 = new DataOutputStream(outputStream2);

        // Write the GET request
        String getRequest2 = "GET " + path + " HTTP/1.1\r\n" +
                "Host: " + this.host + "\r\n" +
                "If-None-Match: " + etag + "\r\n\r\n";
        dataOutputStream2.write(getRequest2.getBytes());
        dataOutputStream2.flush();

        // Input stream for reading bytes
        InputStream inputStream2 = socket2.getInputStream();
        DataInputStream dataInputStream2 = new DataInputStream(inputStream2);

        // Read and print the response
        System.out.println("send304:Response from the server:");
        byte[] buffer2 = new byte[1024];
        int bytesRead2;
        StringBuilder responsesb2 = new StringBuilder();
        while ((bytesRead2 = dataInputStream2.read(buffer2)) != -1) {
            responsesb2.append(new String(buffer2, 0, bytesRead2, "UTF-8"));
        }
        String response2 = responsesb2.toString();
        System.out.println( response2);

        String status = response2.split(" ")[1];
        if (status.equals("304")) {
            System.out.println("cache: ");
            String content2 = new String(cache.get(etag));
            System.out.println(content2);
        }

        // Close resources
        dataOutputStream2.close();
        dataInputStream2.close();
        socket2.close();
    }

//    //304 Modified
//    public void send304Modified()throws IOException{
//        String path = "/";
//        SocketAddress dest = new InetSocketAddress(this.host, this.port);
//        Socket socket = new Socket();
//        socket.connect(dest);
//
//        // Output stream for writing bytes
//        OutputStream outputStream = socket.getOutputStream();
//        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
//
//        // Write the GET request
//        String getRequest = "GET " + path + " HTTP/1.1\r\n" +
//                "Host: " + this.host + "\r\n\r\n";
//        dataOutputStream.write(getRequest.getBytes());
//        dataOutputStream.flush();
//
//        // Input stream for reading bytes
//        InputStream inputStream = socket.getInputStream();
//        DataInputStream dataInputStream = new DataInputStream(inputStream);
//
//        // Read and print the response
//        System.out.println("sendGet:Response from the server:");
//        byte[] buffer = new byte[2048];
//        int bytesRead;
//        StringBuilder responsesb = new StringBuilder();
//        while ((bytesRead = dataInputStream.read(buffer)) != -1) {
//            responsesb.append(new String(buffer, 0, bytesRead, "UTF-8"));
//        }
//        String response = responsesb.toString();
//        System.out.println(response);
//
//        String etag = response.split("ETag: ")[1].split("\r\n")[0];
//        byte[] content = response.split("\r\n\r\n")[1].getBytes();
//        cache.put(etag, content);
//
//        // Close resources
//        dataOutputStream.close();
//        dataInputStream.close();
//        socket.close();
//
//        //modify the file
//        replaceStringInFile(getClass().getClassLoader().getResource("index.html").getPath(), "Hello", "Hello World");
//
//        //second request
//        Socket socket2 = new Socket();
//        socket2.connect(dest);
//
//        // Output stream for writing bytes
//        OutputStream outputStream2 = socket2.getOutputStream();
//        DataOutputStream dataOutputStream2 = new DataOutputStream(outputStream2);
//
//        // Write the GET request
//        String getRequest2 = "GET " + path + " HTTP/1.1\r\n" +
//                "Host: " + this.host + "\r\n" +
//                "If-None-Match: " + etag + "\r\n\r\n";
//        dataOutputStream2.write(getRequest2.getBytes());
//        dataOutputStream2.flush();
//
//        // Input stream for reading bytes
//        InputStream inputStream2 = socket2.getInputStream();
//        DataInputStream dataInputStream2 = new DataInputStream(inputStream2);
//
//        // Read and print the response
//        System.out.println("send304:Response from the server:");
//        byte[] buffer2 = new byte[1024];
//        int bytesRead2;
//        StringBuilder responsesb2 = new StringBuilder();
//        while ((bytesRead2 = dataInputStream2.read(buffer2)) != -1) {
//            responsesb2.append(new String(buffer2, 0, bytesRead2, "UTF-8"));
//        }
//        String response2 = responsesb2.toString();
//        System.out.println( response2);
//
//        String etag2 = response2.split("ETag: ")[1].split("\r\n")[0];
//        byte[] content2 = response2.split("\r\n\r\n")[1].getBytes();
//        cache.put(etag2, content2);
//
//        // Close resources
//        dataOutputStream2.close();
//        dataInputStream2.close();
//        socket2.close();
//    }

    //404 Not Found
    public void send404() throws IOException {
        String path = "/test6.html";
        SocketAddress dest = new InetSocketAddress(this.host, this.port);
        Socket socket = new Socket();
        socket.connect(dest);

        // Output stream for writing bytes
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        // Write the GET request
        String getRequest = "GET " + path + " HTTP/1.1\r\n" +
                "Host: " + this.host + "\r\n\r\n";
        dataOutputStream.write(getRequest.getBytes());
        dataOutputStream.flush();

        // Input stream for reading bytes
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        // Read and print the response
        System.out.println("send404:Response from the server:");
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = dataInputStream.read(buffer)) != -1) {
            String line = new String(buffer, 0, bytesRead, "UTF-8");
            System.out.print(line);
        }
        System.out.println();

        // Close resources
        dataOutputStream.close();
        dataInputStream.close();
        socket.close();
    }

    //405 Method Not Allowed
    public void send405() throws IOException {
        String path = "/";
        SocketAddress dest = new InetSocketAddress(this.host, this.port);
        Socket socket = new Socket();
        socket.connect(dest);

        // Output stream for writing bytes
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        // Write the DELETE request
        String getRequest = "DELETE " + path + " HTTP/1.1\r\n" +
                "Host: " + this.host + "\r\n\r\n";
        dataOutputStream.write(getRequest.getBytes());
        dataOutputStream.flush();

        // Input stream for reading bytes
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        // Read and print the response
        System.out.println("send405:Response from the server:");
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = dataInputStream.read(buffer)) != -1) {
            String line = new String(buffer, 0, bytesRead, "UTF-8");
            System.out.print(line);
        }
        System.out.println();

        // Close resources
        dataOutputStream.close();
        dataInputStream.close();
        socket.close();
    }

    //500 Internal Server Error
    public void send500() throws IOException {
        String path = "/servlet/logins";
        SocketAddress dest = new InetSocketAddress(this.host, this.port);
        Socket socket = new Socket();
        socket.connect(dest);

        // Output stream for writing bytes
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        // Write the GET request
        String getRequest = "GET " + path + " HTTP/1.1\r\n" +
                "Host: " + this.host + "\r\n\r\n";
        dataOutputStream.write(getRequest.getBytes());
        dataOutputStream.flush();

        // Input stream for reading bytes
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        // Read and print the response
        System.out.println("send500:Response from the server:");
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = dataInputStream.read(buffer)) != -1) {
            String line = new String(buffer, 0, bytesRead, "UTF-8");
            System.out.print(line);
        }
        System.out.println();

        // Close resources
        dataOutputStream.close();
        dataInputStream.close();
        socket.close();
    }
    */
    /*
    public static void start() {
        httpclient td = new httpclient("localhost",8080);
        try {
            //send HTTP GET Request
            td.sendGet();
            //send HTTP GET Log in Request
            td.sendGetLogIn();
            //test3.html GUI Log in
            //send HTTP POST Log in Request
            td.loginPost();
            td.signupPost();
            td.loginPost();
            //301 Moved Permanently
            td.send301();
            //302 Foundsername is not exis
            td.send302();
            //304 Not Modified
            td.send304();
            //304 Modified
//            td.send304Modified();
            //404 Not Found
            td.send404();
            //405 Method Not Allowed
            td.send405();
            //500 Internal Server Error
            td.send500();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }*/

}
