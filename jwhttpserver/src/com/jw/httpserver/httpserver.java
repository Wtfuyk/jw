package com.jw.httpserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class httpserver {

    private final HashMap<String, servlet> servletMap = new HashMap<>();
    /**
     *
     * @param port
     */
    public httpserver(int port) throws IOException {
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Port must be between 1 and 65535");
        }
        ServerSocket serverSocket = new ServerSocket(port);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        System.out.println("Server started on port " + port);
        while (true) {
            Socket socket = serverSocket.accept();
            if (socket != null && !socket.isClosed()) {
                Runnable task = () -> {
                    try {
                        handleRequest(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                };
                executorService.submit(task);
            }
        }
    }

    private synchronized servlet getServlet(String servletName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        servletName = "com.jw.httpserver." + servletName + "servlet";
        if (servletMap.containsKey(servletName)) {
            return servletMap.get(servletName);
        }else {
            Class servletClass = Class.forName(servletName);
            servlet servlet = (servlet) servletClass.newInstance();
            servletMap.put(servletName, servlet);
            return servlet;
        }
    }

    private void handleRequest(Socket socket) throws IOException {
        InputStream clientIn = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        //clientIn.available() == 0
        //read the request
        byte[] requestBuffer = new byte[2048];
        clientIn.read(requestBuffer);

        //when the requestBuffer is null eventually, the string will not be null
        String request = new String(requestBuffer);
        System.out.println(request);
        String firstLines = request.split("\r\n")[0];

        String method = firstLines.split(" ")[0];
        if (!Objects.equals(method, "GET") && !Objects.equals(method, "POST")) {
            writeToClient(outputStream, 405, "Method Not Allowed", "text/html", "", "<h1>405 Method Not Allowed</h1>".getBytes());
            return;
        }

        String requesturl = firstLines.split(" ")[1];
        if (requesturl.indexOf("/servlet") != -1) {
            String servletName = null;
            if (requesturl.indexOf("?") != -1) {
                servletName = requesturl.substring(requesturl.indexOf("/servlet") + 9, requesturl.indexOf("?"));
            } else {
                servletName = requesturl.substring(requesturl.indexOf("/servlet") + 9);
            }

            servletName = servletName.trim();
            if (servletName.equals("")) {
                writeToClient(outputStream, 404, "Not Found", "text/html", "", "<h1>404 File Not Found</h1>".getBytes());
                return;
            }

            try{
                servlet servlet = getServlet(servletName);
                String content = servlet.doRequest(method, requesturl, request.trim());
                writeToClient(outputStream, 200, "OK", "text/html", "", content.getBytes());
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
                writeToClient(outputStream, 500, "Internal Server Error", "text/html", "", "<h1>500 Internal Server Error</h1>".getBytes());
            }
            return;
        }

        if (Objects.equals(requesturl, "/favicon.ico")) {
            writeToClient(outputStream, 200, "OK", "text/html", "", "<hi>favicon.ico</hi>".getBytes());
            return;
        }
        String contentType = "text/html";
        if (requesturl.endsWith(".css")) {
            contentType = "text/css";
        } else if (requesturl.endsWith(".js")) {
            contentType = "application/javascript";
        } else if (requesturl.endsWith(".png")) {
            contentType = "image/png";
        } else if (requesturl.endsWith(".jpg") || requesturl.endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (requesturl.endsWith(".gif")) {
            contentType = "image/gif";
        }
        String resoursePath = requesturl.equals("/") ? "index.html" : requesturl.substring(1);

        if (resoursePath.equals("test4.html")) {
            writeToClient(outputStream, 302, "Found", "text/html", "http://192.168.3.108:8080/test1.html", "<h1>302 Found</h1>".getBytes());
            return;
        }
        if (resoursePath.equals("test5.html")) {
            writeToClient(outputStream, 301, "Moved Permanently", "text/html", "http://192.168.3.108:8080/test2.html", "<h1>301 Moved Permanently</h1>".getBytes());
            return;
        }

        URL url = getClass().getClassLoader().getResource(resoursePath);

        if (url == null) {
            writeToClient(outputStream, 404, "Not Found", contentType, "", "<h1>404 File Not Found</h1>".getBytes());
            return;
        }

        byte[] content = null;
        try(BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(url.getPath()))) {
            content = bufferedInputStream.readAllBytes();
            }

        writeToClient(outputStream, 200, "OK", contentType, "", content);

    }

    private void writeToClient(OutputStream outputStream, int responseCode, String responseDes, String contentType, String location, byte[] content) throws IOException {
        outputStream.write(("HTTP/1.1 " + responseCode + " " + responseDes + "\r\n").getBytes());
        outputStream.write(("Content-Type: " + contentType + "\r\n").getBytes());
        outputStream.write(("Location: " + location + "\r\n").getBytes());
        outputStream.write("\r\n".getBytes());
        outputStream.write(content);
        outputStream.flush();
        outputStream.close();
    }
    public static void main(String[] args) throws IOException {
        new httpserver(8080);
    }
}
