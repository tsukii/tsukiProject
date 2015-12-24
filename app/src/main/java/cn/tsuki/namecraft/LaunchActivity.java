package cn.tsuki.namecraft;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by tsuki on 2015/10/28.
 */
public class LaunchActivity extends Activity{

    private final static int LAUNCH_ACTIVITY_DELAY=1000;
    private final int START=1;

    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        ((ImageView) findViewById(R.id.dynamic_item_image)).setVisibility(View.INVISIBLE);
        ((Button)findViewById(R.id.launch_confirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ImageView) findViewById(R.id.dynamic_item_image)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.launch_confirm)).setVisibility(View.GONE);
                ((EditText) findViewById(R.id.launch_IP)).setVisibility(View.GONE);
                HOSTInfo.HOST = ((EditText) findViewById(R.id.launch_IP)).getText().toString();
                new Thread(new startThread()).start();

            }
        });
        mHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.arg1){
                    case START:Intent startMain = new Intent(LaunchActivity.this,MainActivity.class);
                        startActivity(startMain);
                        finish();
                        break;
                    default:break;
                }
            }
        };
    }

    class startThread implements Runnable{
        @Override
        public void run() {
            try {
                Thread.sleep(LAUNCH_ACTIVITY_DELAY);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            Message msg = mHandler.obtainMessage();
            msg.arg1=START;
            mHandler.sendMessage(msg);
        }
    }
    //注册一个广播的内部类，当收到关闭事件时，调用finish方法结束当前的Activity
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        //在当前的activity中注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(GlobalVarable.EXIT_ACTION);
        this.registerReceiver(this.broadcastReceiver, filter);
    }

    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        this.unregisterReceiver(this.broadcastReceiver);
    }
}
