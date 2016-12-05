package com.example.wincber.sockettalkclient;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.et_input) EditText input;
    @BindView(R.id.tv_id) TextView socketId;
    @BindView(R.id.tv_content) TextView content;
    @BindView(R.id.bt_send) Button send;
    @BindView(R.id.bt_clear) Button clear;
    Handler mHandler;
    ClientThread mClientThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Message msg = new Message();
                    msg .what = 0x11;
                    msg.obj = (input.getText().toString());
                    mClientThread.mRevHandler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setText("");
            }
        });
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 0x66){
                    content.append("\n"+msg.obj.toString());
                }
                if(msg.what == 0x22){
                    socketId.setText(" Your ID is  " + msg.obj.toString());
                }
            }
        };
        mClientThread = new ClientThread(mHandler);
        new Thread(mClientThread).start();
    }
}
