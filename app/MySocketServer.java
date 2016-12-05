package com.wincber.socketserver;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by wincber on 11/23/2016.
 */
public class MySocketServer {
    public static void main(String[] args) {
        MySocketServer socketServer = new MySocketServer();
        socketServer.startServer();
    }
    public void startServer() {
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            String ip;
            InetAddress adr = InetAddress.getLocalHost();
            ip = adr.getHostAddress();
            System.out.println(ip);
            serverSocket = new ServerSocket(23333);
            System.out.println("server start>>");
            while(true){
                socket = serverSocket.accept();
                setServerInput(socket);
                manageConnection(socket);
            }

         /*   System.out.println("the socket "+ socket.hashCode() + " connected ");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String receivedMsg;
            while((receivedMsg = reader.readLine()) != null) {
                System.out.println(receivedMsg);
                writer.write("server received : "+ receivedMsg + "\n");
                writer.flush();
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
              //  socket.close();
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public void setServerInput(final Socket socket){
        new Thread(new Runnable() {
            BufferedReader input = null;
            BufferedWriter writer = null;
            @Override
            public void run() {
                try {
                    input = new BufferedReader(new InputStreamReader(System.in));
                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    String content;
                    while((content = input.readLine())!= null){
                        writer.write(content+"\n");
                        writer.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        input.close();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    public void manageConnection(final Socket socket) {

        new Thread(new Runnable() {
            BufferedReader reader = null;
            BufferedWriter writer = null;
            @Override
            public void run() {
                try {
                    System.out.println("Socket " + socket.hashCode() + " has connected");
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    String receivedMsg;
                    while ((receivedMsg = reader.readLine()) != null) {
                        System.out.println(receivedMsg);
                        writer.write("server received : " + receivedMsg+"\n");
                        writer.flush();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally{
                    try {
                        reader.close();
                        writer.close();
                    }catch (Exception e ){
                        e.printStackTrace();
                    }
                    }
            }
        }).start();
    }
}
