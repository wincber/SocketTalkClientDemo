package com.example.wincber.sockettalkclient;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by wincber on 12/1/2016.
 */

public class ClientThread implements Runnable {
    Handler mHandler;  // 服务器通信 的handler
    public Handler mRevHandler; // 与UI线程通信的handler
    BufferedReader reader;
    BufferedWriter writer;
    Socket mSocket;
    public ClientThread(Handler handler){
        mHandler = handler;
    }
    @Override
    public void run() {
        try{
            mSocket = new Socket();
            mSocket.connect(new InetSocketAddress("192.168.191.1",23333),60000);
            Log.d("socket connecting","connecting ...");
            reader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String content = null;
                    try {
                        while((content = reader.readLine() )!= null ){
                            Message msg = new Message();
                            Message msg2 = new Message();
                            msg.what = 0x66;
                            msg2.what = 0x22;
                            msg.obj = content ;
                            msg2.obj = mSocket.hashCode();
                            mHandler.sendMessage(msg);
                            mHandler.sendMessage(msg2);
                            Log.d("Received message:" ,content);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Looper.prepare();
            mRevHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if(msg.what == 0x11){
                        try {
                            writer.write(msg.obj.toString()+" from "+ mSocket.hashCode()+"\n");
                            writer.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            Looper.loop();
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try {
                reader.close();
                writer.close();
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
