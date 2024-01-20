package com.jw.httpclients;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URLEncoder;

public class TestDemo {
    private int port;
    private String host;
//    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public TestDemo(String host, int port) {
//        socket = new Socket();
        this.host = host;
        this.port = port;
    }

    public void sendGet() throws IOException
    {
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
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = dataInputStream.read(buffer)) != -1) {
            String line = new String(buffer, 0, bytesRead, "UTF-8");
            System.out.println(line);
        }

        // Close resources
        dataOutputStream.close();
        dataInputStream.close();
        socket.close();
//
//        OutputStreamWriter streamWriter = new OutputStreamWriter(socket.getOutputStream());
//        bufferedWriter = new BufferedWriter(streamWriter);
//
//        bufferedWriter.write("GET " + path + " HTTP/1.1\r\n");
//        bufferedWriter.write("Host: " + this.host + "\r\n");
//        bufferedWriter.write("\r\n");
//        bufferedWriter.flush();
//
//        BufferedInputStream streamReader = new BufferedInputStream(socket.getInputStream());
//        bufferedReader = new BufferedReader(new InputStreamReader(streamReader, "utf-8"));
//        String line = null;
//        while((line = bufferedReader.readLine())!= null)
//        {
//            System.out.println(line);
//        }
//        bufferedReader.close();
//        bufferedWriter.close();
//        socket.close();
    }

    public void sendPost() throws IOException
    {
        String path = "/test3.html";
        String data = "username = admin&password = 123456";
        // String data = "name=zhigang_jia";
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
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = dataInputStream.read(buffer)) != -1) {
            String line = new String(buffer, 0, bytesRead, "UTF-8");
            System.out.println(line);
        }

        // Close resources
        dataOutputStream.close();
        dataInputStream.close();
        socket.close();

//        OutputStreamWriter streamWriter = new OutputStreamWriter(socket.getOutputStream(), "utf-8");
//        bufferedWriter = new BufferedWriter(streamWriter);
//
//        bufferedWriter.write("POST " + path + " HTTP/1.1\r\n");
//        bufferedWriter.write("Host: " + this.host + "\r\n");
//        bufferedWriter.write("Content-Length: " + data.length() + "\r\n");
//        bufferedWriter.write("Content-Type: application/x-www-form-urlencoded\r\n");
//        bufferedWriter.write("\r\n");
//        bufferedWriter.write(data);
//        bufferedWriter.flush();
//        bufferedWriter.write("\r\n");
//        bufferedWriter.flush();
//
//        BufferedInputStream streamReader = new BufferedInputStream(socket.getInputStream());
//        bufferedReader = new BufferedReader(new InputStreamReader(streamReader, "utf-8"));
//        String line = null;
//        while((line = bufferedReader.readLine())!= null)
//        {
//            System.out.println(line);
//        }
//        bufferedReader.close();
//        bufferedWriter.close();
//        socket.close();
    }

    public static void main(String[] args)
    {
        TestDemo td = new TestDemo("192.168.3.108",8080);
        try {
             td.sendGet(); //send HTTP GET Request

            td.sendPost(); // send HTTP POST Request
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
