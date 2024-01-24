package com.jw.httpserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class httpserver {

    private final HashMap<String, servlet> servletMap = new HashMap<>();
    private final HashMap<String, byte[]> etagMap = new HashMap<>();

    /**
     *
     * @param port
     */
    public httpserver(int port) throws IOException {
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Port must be between 1 and 65535");
        }
        ServerSocket serverSocket = new ServerSocket(port);
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        System.out.println("Server started on port " + port);
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Connection accepted");
//            while(!socket.isClosed()) {
                Runnable task = () -> {
                    try {
                        handleRequest(socket);
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                };
                executorService.submit(task);
//            }
        }
    }

    private synchronized servlet getServlet(String servletName)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        servletName = "com.jw.httpserver." + servletName + "servlet";
        if (servletMap.containsKey(servletName)) {
            return servletMap.get(servletName);
        } else {
            Class servletClass = Class.forName(servletName);
            servlet servlet = (servlet) servletClass.newInstance();
            servletMap.put(servletName, servlet);
            return servlet;
        }
    }

    private void handleRequest(Socket socket) throws IOException {

        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        // dataInputStream.available() == 0
        // read the request
        byte[] requestBuffer = new byte[2048];
        while (dataInputStream.read(requestBuffer) != -1) {
            // when the requestBuffer is null eventually, the string will not be null
            String request = new String(requestBuffer);
//            System.out.println(request);
            String firstLines = request.split("\r\n")[0];

            String requesturl = firstLines.split(" ")[1];

            String method = firstLines.split(" ")[0];

            String connection = request.split("Connection: ")[1].split("\r\n")[0];

            if (!Objects.equals(method, "GET") && !Objects.equals(method, "POST")) {
                writeToClient(dataOutputStream, 405, "Method Not Allowed", "text/html", "", "",
                        "<h1>405 Method Not Allowed</h1>".getBytes());
                if (Objects.equals(connection, "close")) {
                    break;
                }
            }

            if (requesturl.indexOf("/servlet") != -1) {
                String servletName = null;
                if (requesturl.indexOf("?") != -1) {
                    servletName = requesturl.substring(requesturl.indexOf("/servlet") + 9, requesturl.indexOf("?"));
                } else {
                    servletName = requesturl.substring(requesturl.indexOf("/servlet") + 9);
                }

                servletName = servletName.trim();
                if (servletName.equals("")) {
                    writeToClient(dataOutputStream, 404, "Not Found", "text/html", "", "",
                            "<h1>404 File Not Found</h1>".getBytes());
                    if (Objects.equals(connection, "close")) {
                        break;
                    }
                }

                try {
                    servlet servlet = getServlet(servletName);
                    String content = servlet.doRequest(method, requesturl, request.trim());
                    writeToClient(dataOutputStream, 200, "OK", "text/html", "", "", content.getBytes());
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                    writeToClient(dataOutputStream, 500, "Internal Server Error", "text/html", "", "",
                            "<h1>500 Internal Server Error</h1>".getBytes());
                }
//                if (Objects.equals(connection, "close")) {
                    break;
//                }
            }

            if (Objects.equals(requesturl, "/favicon.ico")) {
                writeToClient(dataOutputStream, 200, "OK", "text/html", "", "", "<hi>favicon.ico</hi>".getBytes());
//                if (Objects.equals(connection, "close")) {
                    break;
//                }
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

            if (Objects.equals(method, "GET") && requesturl.indexOf("?") == -1 && request.contains("If-None-Match")) {
                String etag = request.split("If-None-Match: ")[1].split("\r\n")[0];
                URL url = getClass().getClassLoader().getResource(resoursePath);

                byte[] content = null;
                try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(url.getPath()))) {
                    content = bufferedInputStream.readAllBytes();
                }

                byte[] rawContent = etagMap.get(etag);
                if (new String(rawContent).equals(new String(content))) {
                    writeToClient(dataOutputStream, 304, "Not Modified", "text/html", etag, "",
                            "<h1>304 Not Modified</h1>".getBytes());
                    if (Objects.equals(connection, "close")) {
                        break;
                    }
                }

                String newEtag = String.valueOf(content.hashCode());
                etagMap.put(newEtag, content);
                writeToClient(dataOutputStream, 200, "OK", "text/html", newEtag, "", content);
                if (Objects.equals(connection, "close")) {
                    break;
                }
            }
            if (resoursePath.equals("test4.html")) {
                writeToClient(dataOutputStream, 302, "Found", "text/html", "", "http://localhost:8080/test1.html",
                        "<h1>302 Found</h1>".getBytes());
                if (Objects.equals(connection, "close")) {
                    break;
                }
            }
            if (resoursePath.equals("test5.html")) {
                writeToClient(dataOutputStream, 301, "Moved Permanently", "text/html", "", "http://localhost:8080/test2.html",
                        "<h1>301 Moved Permanently</h1>".getBytes());
                if (Objects.equals(connection, "close")) {
                    break;
                }
            }

            URL url = getClass().getClassLoader().getResource(resoursePath);

            if (url == null) {
                writeToClient(dataOutputStream, 404, "Not Found", contentType, "", "", "<h1>404 File Not Found</h1>".getBytes());
                if (Objects.equals(connection, "close")) {
                    break;
                }
            }

            byte[] content = null;
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(url.getPath()))) {
                content = bufferedInputStream.readAllBytes();
            }

            String etag = String.valueOf(content.hashCode());
            if (Objects.equals(method, "GET")) {
                etagMap.put(etag, content);
            }

            writeToClient(dataOutputStream, 200, "OK", contentType, etag, "", content);
            if (Objects.equals(connection, "close")) {
                break;
            }
        }
        dataInputStream.close();
//        dataOutputStream.flush();
        dataOutputStream.close();
        socket.close();

    }

    private void writeToClient(DataOutputStream dataOutputStream, int responseCode, String responseDes, String contentType, String etag,
            String location, byte[] content) throws IOException {
        dataOutputStream.write(("HTTP/1.1 " + responseCode + " " + responseDes + "\r\n" + "Content-Type: " + contentType + "\r\n" + "ETag: " + etag + "\r\n" + "Date: " + new Date() + "\r\n" + "Location: " + location + "\r\n\r\n" + new String(content)).getBytes());
//        dataOutputStream.flush();
//        dataOutputStream.close();
    }

    public static void main(String[] args) throws IOException {
        new httpserver(8080);
    }
}
