package cn.tsuki.namecraft;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.Thread;
import java.lang.Runnable;


public class MainActivity extends Activity {

    private final int CONNECT_FAILED=0;
    private final int CONNECT_SUCCESS=1;
    private final int CONNECTING=2;
    private final int FIRSTCONNECT=4;
    private Timer timer;
    private TimerTask task;
    Handler mHandler;
    private RelativeLayout relayout;
    private TextView txtview;
    private int alpha;
    private int textsize;
    //private info minfo;
    private int login_status;


    final private String HOST = "192.168.1.103";
    final private int PORT = 9002;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relayout=(RelativeLayout)findViewById(R.id.main_layout);
        txtview=(TextView)findViewById(R.id.main_startbtn);
        login_status=0;
        relayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText)findViewById(R.id.Main_edit)).getText().toString();
                if(login_status==FIRSTCONNECT){
                    if(name.equals("")){
                        Toast.makeText(getApplicationContext(), "昵称不能为空",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        getIDPassword thread_getID = new getIDPassword(HOST, PORT, name);
                        new Thread(thread_getID).start();
                    }
                }else{
                    connectThread thread_connect = new connectThread(HOST,PORT);
                    new Thread(thread_connect).start();
                }
            }
        });
        textTwinkle();
        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                switch (msg.arg1){
                    case CONNECTING:txtview.setText("正在连接...");relayout.setClickable(false);break;
                    case CONNECT_FAILED:txtview.setText("连接失败,点击屏幕重新连接");relayout.setClickable(true);break;
                    case CONNECT_SUCCESS:txtview.setText("连接成功");startGameActivity((info) msg.obj);break;
                    case FIRSTCONNECT:txtview.setText("您是第一次玩这个游戏，所以请输入昵称");
                        findViewById(R.id.MainTitle).setVisibility(View.INVISIBLE);
                        //findViewById(R.id.main_layout).clearAnimation();
                        findViewById(R.id.Main_editName).setVisibility(View.VISIBLE);
                        login_status=FIRSTCONNECT;break;
                    default:break;
                }
            }
        };
    }

    private void textTwinkle(){
        alpha=0;
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtview.setTextColor(Color.argb(alpha,0,0,0));
                        alpha=(alpha+20)%256;
                    }
                });
            }
        };
        timer.schedule(task, 1, 100);
    }

    private void timerTaskCancel(){
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private void startGameActivity(info gameinfo){
        timerTaskCancel();
        Intent in = new Intent(MainActivity.this,GameActivity.class);
        Bundle mBundle= new Bundle();
        mBundle.putString("Name",gameinfo.getName());
        mBundle.putInt("lv", gameinfo.getLevel());
        mBundle.putInt("exp",gameinfo.getExp());
        mBundle.putInt("str",gameinfo.getStr());
        mBundle.putInt("intel",gameinfo.getIntel());
        mBundle.putInt("agi",gameinfo.getAgi());
        mBundle.putInt("luc",gameinfo.getLuc());
        mBundle.putInt("atk",gameinfo.getAtk());
        mBundle.putInt("def",gameinfo.getDef());
        in.putExtras(mBundle);
        startActivity(in);
        this.finish();
    }

    class getIDPassword implements Runnable{
        private String host;
        private int port;
        private String ID;
        private String password;
        private String name;

        public getIDPassword(String host, int port,String name) {
            this.host = host;
            this.port = port;
            this.name = name;
        }

        @Override
        public void run() {
            Socket mSocket =null;
            try{
                mSocket=new Socket(host,port);
            }catch (IOException e){
                Message msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
            }
            Message msg = mHandler.obtainMessage();
            msg.arg1=CONNECTING;
            mHandler.sendMessage(msg);
            JSONObject jsonobj = new JSONObject();
            try{
                jsonobj.put("type", "login_0");
                jsonobj.put("name", this.name);
            }catch (JSONException e){
                Log.v("json trans",e.getCause().toString());
            }
            String jsonString=jsonobj.toString();
            Log.v("jsonString:",jsonString);
            try {
                OutputStream os = mSocket.getOutputStream();
                os.write(jsonString.getBytes());
                os.flush();
                mSocket.shutdownOutput();
            }catch (UnknownHostException e) {
                Log.v("output Unknown",e.getCause().toString());
            }catch (IOException e){
                Log.v("output IOException",e.getCause().toString());
            }
            try{
                BufferedReader bf =  new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                jsonString = bf.readLine();
            }catch (UnknownHostException e) {
                Log.v("input Unknown",e.getCause().toString());
            }catch (IOException e){
                Log.v("input IOException",e.getCause().toString());
            }
            try {
                jsonobj = new JSONObject(jsonString);
                ID=jsonobj.getString("ID");
                password=jsonobj.getString("password");
            }catch (JSONException e){
                Log.v("get ID and password",ID+" "+password);
            }
            SharedPreferences pref = getSharedPreferences("setting", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("ID",ID);
            editor.putString("password",password);
            editor.apply();
            try {
                mSocket.close();
            }catch (IOException e){
                e.printStackTrace();
                Log.v("getIDPassword:",e.getCause().toString());
            }
        }
    }

    class connectThread implements Runnable{
        public connectThread(String host, int port) {
            this.host = host;
            this.port = port;
            this.name = "";
        }
        private String host;
        private int port;
        private String ID;
        private String password;
        private String name;

        @Override
        public void run() {

            SharedPreferences pref = getSharedPreferences("setting", Context.MODE_PRIVATE);
            ID=pref.getString("ID","");
            password=pref.getString("password","");
            Log.v("ID psd",ID+" "+password);
            if(ID.equals("")||password.equals("")){//如果没有预先存储的用户名和密码，就首先获取
                Log.v("First login",ID+" "+password);
                Message msg = mHandler.obtainMessage();
                msg.arg1=FIRSTCONNECT;
                mHandler.sendMessage(msg);
            }
            else {
                Log.v("else","");
                //得到ID和密码后登录
                Socket mSocket =null;
                try{
                    mSocket=new Socket(host,port);
                }catch (IOException e){
                    Message msg = mHandler.obtainMessage();
                    msg.arg1=CONNECT_FAILED;
                    mHandler.sendMessage(msg);
                    return;
                }
                Message msg = mHandler.obtainMessage();
                msg.arg1=CONNECTING;
                mHandler.sendMessage(msg);
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class info{
        public info(String name, int level, int exp, int str, int intel, int agi, int luc, int atk, int def) {
            this.name = name;
            this.level = level;
            this.exp = exp;
            this.str = str;
            this.intel = intel;
            this.agi = agi;
            this.luc = luc;
            this.atk = atk;
            this.def = def;
        }

        public String getName() {
            return name;
        }

        public int getLevel() {
            return level;
        }

        public int getExp() {
            return exp;
        }

        public int getStr() {
            return str;
        }

        public int getIntel() {
            return intel;
        }

        public int getAgi() {
            return agi;
        }

        public int getLuc() {
            return luc;
        }

        public int getAtk() {
            return atk;
        }

        public int getDef() {
            return def;
        }

        private String name;
        private int level;
        private int exp;
        private int str;
        private int intel;
        private int agi;
        private int luc;
        private int atk;
        private int def;


    }
}
