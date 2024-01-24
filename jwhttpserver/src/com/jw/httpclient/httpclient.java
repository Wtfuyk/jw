package com.jw.httpclient;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class httpclient {
    //<eTag ,content>
    private Map<String, byte[]> cache = new HashMap<>();
    //<path,eTag>
    private Map<String,String> cacheofpath = new HashMap<>();
    private int port;
    private String host;

//    private BufferedReader bufferedReader;
//    private BufferedWriter bufferedWriter;

    public httpclient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private String GetRequest(String path, String connection) {
        String getRequest;
        if(cacheofpath.containsKey(path)){
            getRequest = "GET " + path + " HTTP/1.1\r\n" +
                    "Host: " + this.host + "\r\n" +
                    "Connection: close" + "\r\n" +
                    "If-None-Match: " + cacheofpath.get(path) + "\r\n\r\n";
        }else{
            getRequest = "GET " + path + " HTTP/1.1\r\n" +
                    "Host: " + this.host + "\r\n" +
                    "Connection: " + connection + "\r\n\r\n";
        }

        return getRequest;
    }

    private String GetPost(String path, String data, String connection) {
        String postRequest = "POST " + path + " HTTP/1.1\r\n" +
                "Host: " + this.host + "\r\n" +
                "Content-Length: " + data.length() + "\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Connection: " + connection + "\r\n\r\n" +
                data;
        return postRequest;
    }

    public Socket socketConnect() throws IOException {
        SocketAddress dest = new InetSocketAddress(this.host, this.port);
        Socket socket = new Socket();
        socket.connect(dest);
        return socket;
    }

    public void sendGet(Socket socket, String path, String connection) throws IOException {
        // Output stream for writing bytes
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        // Write the GET request
        String getRequest = GetRequest(path, connection);
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
        String response = new String(buffer, 0, bytesRead, "UTF-8");
        System.out.print(response);

        int responseCode = Integer.parseInt(response.split("\r\n")[0].split(" ")[1]);
        if (responseCode==200){
            String etag = response.split("ETag: ")[1].split("\r\n")[0];
            byte[] content = response.split("\r\n\r\n")[1].getBytes();
            cache.put(etag, content);
            cacheofpath.put(path,etag);
        }

        System.out.println();

        // Close resources
        if (Objects.equals(connection, "close")) {
            dataOutputStream.close();
            dataInputStream.close();
            socket.close();
        }
    }

    public void sendPost(Socket socket, String path, String data, String connection) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        String postRequest = GetPost(path, data, connection);
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
        String line = new String(buffer, 0, bytesRead, "UTF-8");
        System.out.print(line);
        System.out.println();

        // Close resources
        if (Objects.equals(connection, "close")) {
            dataOutputStream.close();
            dataInputStream.close();
            socket.close();
        }
    }

    public void wrongMethodSend(Socket socket, String path, String connection) throws IOException {
        // Output stream for writing bytes
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        // Write the GET request
        String getRequest = "DELETE " + path + " HTTP/1.1\r\n" +
                "Host: " + this.host + "\r\n" +
                "Connection: " + connection + "\r\n\r\n";
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
        String line = new String(buffer, 0, bytesRead, "UTF-8");
        System.out.print(line);

        System.out.println();

        // Close resources
        if (Objects.equals(connection, "close")) {
            dataOutputStream.close();
            dataInputStream.close();
            socket.close();
        }
    }
}
