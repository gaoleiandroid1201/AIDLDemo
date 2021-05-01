package com.gl.abridge_server;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sjtu.yifei.AbridgeMessengerCallBack;
import com.sjtu.yifei.IBridge;

public class ServerMessengerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TestMessengerActivity";
    public final static int ACTIVITYID = 0X0002;
    private EditText et_message;
    private TextView tv_show_message;

    private AbridgeMessengerCallBack callBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        et_message = findViewById(R.id.et_message);
        tv_show_message = findViewById(R.id.tv_show_message);
        findViewById(R.id.btn_add).setOnClickListener(this);
        //注意这里的packagename，需要通信的多个app只能使用一个packagename
        //即使用一个app作为server启动这个共享服务来进行通信
        IBridge.init(getApplication(), "com.sjtu.yifei.service", IBridge.AbridgeType.MESSENGER);
        IBridge.registerMessengerCallBack(callBack = new AbridgeMessengerCallBack() {
            @Override
            public void receiveMessage(Message message) {
                if (message.arg1 == ACTIVITYID) {
                    //客户端接受服务端传来的消息
                    String str = (String) message.getData().get("content");
                    tv_show_message.setText(str);
                    Log.d("ServerMessengerActivity", "receiveMessage str: " + str);

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_add) {
            String messageStr = "server :" + et_message.getText().toString();
            Message message = Message.obtain();
            message.arg1 = ACTIVITYID;
            //注意这里，把`Activity`的`Messenger`赋值给了`message`中，当然可能你已经发现这个就是`Service`中我们调用的`msg.replyTo`了。
            Bundle bundle = new Bundle();
            bundle.putString("content", messageStr);
            message.setData(bundle);
            IBridge.sendMessengerMessage(message);
        }
    }

    @Override
    protected void onDestroy() {
        IBridge.uRegisterMessengerCallBack(callBack);
        super.onDestroy();
    }
}
