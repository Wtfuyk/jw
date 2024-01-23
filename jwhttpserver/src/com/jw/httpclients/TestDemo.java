package com.jw.httpclients;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TestDemo {
    private int port;
    private String host;
    //    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private Map<String, Date> FileCache = new HashMap<>();

    public TestDemo(String host, int port) {
//        socket = new Socket();
        this.host = host;
        this.port = port;
    }

    private Date StringToDate(String datetime) {
        SimpleDateFormat ft = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        try {
            return ft.parse(datetime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String WriteGetRequest(String path) {
        String getRequest;
        if (FileCache.get(path) != null) {
            getRequest = "GET " + path + " HTTP/1.1\r\n" +
                    "Host: " + this.host + "\r\n" + "If Modified Since: " + FileCache.get(path) + "\r\n\r\n";
        } else {
            getRequest = "GET " + path + " HTTP/1.1\r\n" +
                    "Host: " + this.host + "\r\n\r\n";
        }
        return getRequest;
    }

    //get index.html
    public void sendGet() throws IOException {
        String path = "/";
        SocketAddress dest = new InetSocketAddress(this.host, this.port);
        Socket socket = new Socket();
        socket.connect(dest);

        // Output stream for writing bytes
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        // Write the GET request
        String getRequest = WriteGetRequest(path);
        dataOutputStream.write(getRequest.getBytes());
        dataOutputStream.flush();

        // Input stream for reading bytes
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);


        // Read and print the response
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] responsebuffer = new byte[1024];
        int length;
        while ((length = inputStream.read(responsebuffer)) != -1) {
            result.write(responsebuffer, 0, length);
        }

        String[] responseLines = result.toString("UTF-8").split("\r\n");
        int responseCode = Integer.parseInt(responseLines[0].split(" ")[1]);

        if (responseCode == 200) {
            String dateTime = responseLines[3].substring(12);
            if (FileCache.containsKey(path)) {
                FileCache.replace(path, StringToDate(dateTime));
            } else {
                FileCache.put(path, StringToDate(dateTime));
            }
        }
        if (responseCode == 304) {
            String dateTime = responseLines[3].substring(12).trim();
            FileCache.replace(path, StringToDate(dateTime));
        }

        System.out.println("sendGet:Response from the server:");
        /*
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = dataInputStream.read(buffer)) != -1) {
            String responseLine = new String(buffer, 0, bytesRead, "UTF-8");
            System.out.print(responseLine);
        }
        */
        System.out.println(result);
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

    //post log in
    public void sendPost() throws IOException {
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
        String getRequest = WriteGetRequest(path);
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
        String getRequest = WriteGetRequest(path);
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
    public void send304() throws IOException {
        //FileCache.put("/",StringToDate("Mon Jan 22 23:28:17 CST 2024"));
        String path = "/";
        SocketAddress dest = new InetSocketAddress(this.host, this.port);
        Socket socket = new Socket();
        socket.connect(dest);

        // Output stream for writing bytes
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        // Write the GET request
        String getRequest = WriteGetRequest(path);
        dataOutputStream.write(getRequest.getBytes());
        dataOutputStream.flush();

        // Input stream for reading bytes
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);


        // Read and print the response
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] responsebuffer = new byte[1024];
        int length;
        while ((length = inputStream.read(responsebuffer)) != -1) {
            result.write(responsebuffer, 0, length);
        }

        String[] responseLines = result.toString("UTF-8").split("\r\n");
        int responseCode = Integer.parseInt(responseLines[0].split(" ")[1]);

        if (responseCode == 200) {
            String dateTime = responseLines[3].substring(12);
            if (FileCache.containsKey(path)) {
                FileCache.replace(path, StringToDate(dateTime));
            } else {
                FileCache.put(path, StringToDate(dateTime));
            }
        }

        System.out.println("sendGet:Response from the server:");
        /*
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = dataInputStream.read(buffer)) != -1) {
            String responseLine = new String(buffer, 0, bytesRead, "UTF-8");
            System.out.print(responseLine);
        }
        */
        System.out.println(result);
        System.out.println();

        // Close resources
        dataOutputStream.close();
        dataInputStream.close();
        socket.close();
    }

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
        String getRequest = WriteGetRequest(path);
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
        String getRequest = WriteGetRequest(path);
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

    public static void main(String[] args) {
        TestDemo td = new TestDemo("localhost", 8080);
        try {
            //send HTTP GET Request
            td.sendGet();

            //td.send304();
            //send HTTP GET Log in Request
            td.sendGetLogIn();
            //test3.html GUI Log in
            //send HTTP POST Log in Request
            td.sendPost();
            //301 Moved Permanently
            td.send301();
            //302 Found
            td.send302();
            //304 Not Modified
            td.send304();
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
    }

}
