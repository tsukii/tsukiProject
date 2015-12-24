package cn.tsuki.namecraft;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.Thread;
import java.lang.Runnable;

import cn.tsuki.namecraft.clientJson.CreateHero;
import cn.tsuki.namecraft.clientJson.GetHeroAttribute;
import cn.tsuki.namecraft.clientJson.Login;
import cn.tsuki.namecraft.jsonTools.Jsons;
import cn.tsuki.namecraft.serverJson.RAllocate;
import cn.tsuki.namecraft.serverJson.RGetHeroAttribute;


public class MainActivity extends Activity {

    private final int CONNECT_FAILED=0;
    private final int CONNECT_SUCCESS=1;
    private final int CONNECTING=2;
    private final int FIRST_CONNECT=4;
    private final int GET_MESSAGE=5;
    private final int TEXT_CLICK_DIS=6;
    private final int TEXT_CLICK_ABL=7;
    private final int NEW_ACTIVITY=8;
    private Timer timer;
    private TimerTask task;
    Handler mHandler;
    private RelativeLayout relayout;
    private TextView txtview;
    private int alpha;
    private int textsize;
    //private info minfo;
    private int login_status;
    private int hero_type;
    private TextView role1;
    private TextView role2;
    private TextView role3;

    final private String HOST = HOSTInfo.HOST;
    final private int PORT = HOSTInfo.PORT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relayout=(RelativeLayout)findViewById(R.id.main_layout);
        txtview=(TextView)findViewById(R.id.main_startbtn);
        role1=(TextView)findViewById(R.id.main_role_1);
        role2=(TextView)findViewById(R.id.main_role_2);
        role3=(TextView)findViewById(R.id.main_role_3);

        login_status=0;
        hero_type=0;

        role1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hero_type=1;
                role1.setTextColor(Color.parseColor("#000000"));
                role2.setTextColor(Color.parseColor("#888888"));
                role3.setTextColor(Color.parseColor("#888888"));
            }
        });
        role2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hero_type=2;
                role2.setTextColor(Color.parseColor("#000000"));
                role1.setTextColor(Color.parseColor("#888888"));
                role3.setTextColor(Color.parseColor("#888888"));
            }
        });
        role3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hero_type=3;
                role3.setTextColor(Color.parseColor("#000000"));
                role2.setTextColor(Color.parseColor("#888888"));
                role1.setTextColor(Color.parseColor("#888888"));
            }
        });
        relayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login_status==NEW_ACTIVITY){
                    getAttiThread getAttiThread = new getAttiThread(HOST,PORT);
                    new Thread(getAttiThread).start();
                }else if(login_status==FIRST_CONNECT){
                    String name = ((EditText)findViewById(R.id.Main_edit)).getText().toString();
                    if(name.equals("")){
                        Toast.makeText(getApplicationContext(),"名字不能为空",Toast.LENGTH_SHORT).show();
                    }else if(hero_type==0){
                        Toast.makeText(getApplicationContext(),"请选择职业",Toast.LENGTH_SHORT).show();
                    }else{
                        createHeroThread th_create = new createHeroThread(HOST, PORT, name, hero_type);
                        new Thread(th_create).start();
                    }
                }else {
                    connectThread thread_connect = new connectThread(HOST, PORT);
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
                    case CONNECTING:txtview.setText("正在连接...,若没有反应，请再点一次");relayout.setClickable(false);break;
                    case CONNECT_FAILED:txtview.setText("连接失败,点击屏幕重新连接");relayout.setClickable(true);break;
                    case CONNECT_SUCCESS:txtview.setText("连接成功,若没有反应，请再点一次");break;
                    case FIRST_CONNECT:txtview.setText("请输入您的名字并选择职业");
                        findViewById(R.id.MainTitle).setVisibility(View.INVISIBLE);
                        //findViewById(R.id.main_layout).clearAnimation();
                        findViewById(R.id.Main_editName).setVisibility(View.VISIBLE);
                        login_status=FIRST_CONNECT;break;
                    case GET_MESSAGE:txtview.setText("正在获取信息。。。");break;
                    case TEXT_CLICK_DIS:relayout.setClickable(false);break;
                    case TEXT_CLICK_ABL:relayout.setClickable(true);break;
                    case NEW_ACTIVITY:startGameActivity((JSONObject)msg.obj);break;
                    default:break;
                }
                switch (msg.arg2){
                    case TEXT_CLICK_DIS:relayout.setClickable(false);break;
                    case TEXT_CLICK_ABL:relayout.setClickable(true);break;
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

    private void startGameActivity(JSONObject gameinfo){
        timerTaskCancel();
        Intent in = new Intent(MainActivity.this,GameActivity.class);
        Bundle mBundle= new Bundle();
        try{
            mBundle.putString("Name", gameinfo.getString("RoleName"));
            mBundle.putString("ID", gameinfo.getString("RoleID"));
            mBundle.putInt("type", gameinfo.getInt("HeroType"));
            mBundle.putInt("lv", gameinfo.getInt("Level"));
            mBundle.putInt("exp",gameinfo.getInt("Exp"));
            mBundle.putInt("Power",gameinfo.getInt("Power"));
            mBundle.putInt("Intel",gameinfo.getInt("Intel"));
            mBundle.putInt("Agi",gameinfo.getInt("Agi"));
            mBundle.putInt("Lucky",gameinfo.getInt("Lucky"));
            mBundle.putInt("Atk",gameinfo.getInt("Atk"));
            mBundle.putInt("Defense",gameinfo.getInt("Defense"));
            mBundle.putInt("HP",gameinfo.getInt("Hp"));
            mBundle.putInt("AbiliAvailable",gameinfo.getInt("AbiliAvailable"));
            mBundle.putInt("AttriAvailable", gameinfo.getInt("AttriAvailable"));
            mBundle.putInt("AbilityLevel1",gameinfo.getInt("AbilityLevel1"));
            mBundle.putInt("AbilityLevel2",gameinfo.getInt("AbilityLevel2"));
            mBundle.putInt("AbilityLevel3",gameinfo.getInt("AbilityLevel3"));
            mBundle.putInt("AbilityLevel4", gameinfo.getInt("AbilityLevel4"));
            mBundle.putInt("AbilityLevel5", gameinfo.getInt("AbilityLevel5"));
            mBundle.putInt("AbilityLevel6", gameinfo.getInt("AbilityLevel6"));
        }catch (JSONException e){
            Log.d("json Convert","filed");
        }
        in.putExtras(mBundle);
        startActivity(in);
        this.finish();
    }

    class createHeroThread implements Runnable{
        private String host;
        private int port;
        private String ID;
        private String name;
        private int type;

        public createHeroThread(String host, int port,  String name, int type) {
            this.host = host;
            this.port = port;
            this.name = name;
            this.type = type;
            this.ID=getSharedPreferences("setting", Context.MODE_PRIVATE).getString("ID", "");
            //ID=pref.getString("ID","");
        }

        @Override
        public void run() {

            Message msg=mHandler.obtainMessage();
            msg.arg1=CONNECTING;
            msg.arg2=TEXT_CLICK_DIS;
            mHandler.sendMessage(msg);
            Socket mSocket =null;
            try{
                mSocket=new Socket(host,port);
            }catch (IOException e){
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                msg.arg2=TEXT_CLICK_ABL;
                mHandler.sendMessage(msg);
                return;
            }

            CreateHero createHero = new CreateHero(name,ID,type);
            try {
                ArrayList<CreateHero> alist = new ArrayList<>();
                alist.add(createHero);
                String jsonString = Jsons.buildJson(2, "CreateHero", 1, Jsons.buildJsonArray(alist).toString());
                Log.d("jsons", jsonString);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
                writer.write(jsonString);writer.newLine();
                writer.flush();
            }catch (JSONException e){
                e.printStackTrace();
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                msg.arg2=TEXT_CLICK_ABL;
                mHandler.sendMessage(msg);
                return;
            }catch (IOException e){
                e.printStackTrace();
                //Log.v("socketos", e.getCause().toString());
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                msg.arg2=TEXT_CLICK_ABL;
                mHandler.sendMessage(msg);
                return;
            }

            String recString=null;
            try{
                BufferedReader bf =  new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                String jsonString = bf.readLine();
                Log.d("bfr", jsonString);
                JSONObject jobject = new JSONObject(jsonString);
                if((jobject.getInt("JsonType")!=2)){
                    throw new JSONException("");
                }
            }catch (IOException e){
               // Log.v("bR IOException",e.getCause().toString());
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                msg.arg2=TEXT_CLICK_ABL;
                mHandler.sendMessage(msg);
                return;
            }catch (JSONException e){
                Toast.makeText(getApplicationContext(), "校验失败", Toast.LENGTH_SHORT).show();
                //return;
            }finally {
                try {
                    mSocket.close();
                }catch (IOException e){
                    //return;
                }
            }

            msg = mHandler.obtainMessage();
            msg.arg1 = TEXT_CLICK_ABL;
            mHandler.sendMessage(msg);

            login_status=NEW_ACTIVITY;

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

            Message msg =mHandler.obtainMessage();
            msg.arg1=TEXT_CLICK_DIS;
            mHandler.sendMessage(msg);
            Login login = new Login(0,null,null);
            SharedPreferences pref = getSharedPreferences("setting", Context.MODE_PRIVATE);
            ID=pref.getString("ID","");
            password=pref.getString("password","");
            Log.v("ID psd",ID+" "+password);
            if(ID.equals("")||password.equals("")){//如果没有预先存储的用户名和密码，就首先获取
                Log.v("First login",ID+" "+password);
                login_status=FIRST_CONNECT;
                login.setFirstLogin(1);
                //Log.v("fff","");
            }else{
                login.setFirstLogin(0);
                login.setUserId(ID);
                login.setUserPassword(password);
                login_status=NEW_ACTIVITY;
            }


            Log.v("else","hhhhh");
            //登录

            Socket mSocket =null;
            msg = mHandler.obtainMessage();
            msg.arg1=CONNECTING;
            mHandler.sendMessage(msg);
            Log.v("IP",host+" "+port);
            try{
                mSocket=new Socket(host,port);
                mSocket.setSoTimeout(1000);
            }catch (IOException e){
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                msg.arg2=TEXT_CLICK_ABL;
                mHandler.sendMessage(msg);
                //mSocket.close();
                return;
            }
            msg = mHandler.obtainMessage();
            msg.arg1=CONNECT_SUCCESS;
            mHandler.sendMessage(msg);
            //TODO 发送login请求
            try {
                List<Login> alist = new ArrayList<>();
                alist.add(login);
                String jsonString = Jsons.buildJson(5, "Login", 1, Jsons.buildJsonArray(alist).toString());
                Log.d("jsons", jsonString);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
                writer.write(jsonString);writer.newLine();
                writer.flush();
                //mSocket.shutdownOutput();
            }catch (JSONException e){
                e.printStackTrace();
                //Log.v("buildjosn", e.getCause().toString());
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                msg.arg2=TEXT_CLICK_ABL;
                mHandler.sendMessage(msg);
                return;
            }catch (IOException e){
                e.printStackTrace();
                //Log.v("socketos", e.getCause().toString());
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                msg.arg2=TEXT_CLICK_ABL;
                mHandler.sendMessage(msg);
                return;
            }




            String recString=null;
            try{
                BufferedReader bf =  new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                String jsonString = bf.readLine();
                Log.d("bfr", jsonString);
                JSONObject jobject = new JSONObject(jsonString);
                if((jobject.getInt("JsonType")!=5)){
                    throw new JSONException("");
                }
                recString=jobject.getString("Content");
            }catch (IOException e){
                //Log.v("bR IOException",e.getCause().toString());
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                msg.arg2=TEXT_CLICK_ABL;
                mHandler.sendMessage(msg);
                return;
            }catch (JSONException e){
                Toast.makeText(getApplicationContext(), "校验失败", Toast.LENGTH_SHORT).show();
                //return;
            }finally {
                try {
                    mSocket.close();
                }catch (IOException e){
                    //return;
                }
            }

            try {
                JSONArray jsonArray = new JSONArray(recString);
                JSONObject jobject = jsonArray.getJSONObject(0);
                Log.v("test","test"+jobject.getInt("ReturnNum"));
                if(jobject.getInt("ReturnNum")==0){
                    ID="";
                    password="";
                    login_status=0;
                }else {
                    ID = jobject.getString("UserId");
                    password = jobject.getString("UserPassword");
                }
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("ID",ID);
                editor.putString("password",password);
                editor.apply();
            }catch (JSONException e){
                Toast.makeText(getApplicationContext(), "校验失败", Toast.LENGTH_SHORT).show();
                //return;
            }finally {
                msg=mHandler.obtainMessage();
                msg.arg1=TEXT_CLICK_ABL;
                mHandler.sendMessage(msg);
            }
//            msg = mHandler.obtainMessage();
//            msg.arg1=GET_MESSAGE;
//            mHandler.sendMessage(msg);
            if(login_status==FIRST_CONNECT){
                msg = mHandler.obtainMessage();
                msg.arg1=FIRST_CONNECT;
                mHandler.sendMessage(msg);
            }



        }

    }

    class getAttiThread implements Runnable{
        private String ID;
        private String host;
        private int port;

        public  getAttiThread(String host,int port){
            this.host=host;
            this.port=port;
            this.ID=getSharedPreferences("setting", Context.MODE_PRIVATE).getString("ID", "");
        }

        @Override
        public void run() {
            Message msg=mHandler.obtainMessage();
            msg.arg1=CONNECTING;
            msg.arg2=TEXT_CLICK_DIS;
            mHandler.sendMessage(msg);
            Socket mSocket =null;
            try{
                mSocket=new Socket(host,port);
            }catch (IOException e){
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                msg.arg2=TEXT_CLICK_ABL;
                mHandler.sendMessage(msg);
                return;
            }

            GetHeroAttribute getHeroAttribute = new GetHeroAttribute(ID);
            try {
                ArrayList<GetHeroAttribute> alist = new ArrayList<>();
                alist.add(getHeroAttribute);
                String jsonString = Jsons.buildJson(3, "GetHeroAttribute", 1, Jsons.buildJsonArray(alist).toString());
                Log.d("jsons", jsonString);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
                writer.write(jsonString);writer.newLine();
                writer.flush();
            }catch (JSONException e){
                e.printStackTrace();
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                msg.arg2=TEXT_CLICK_ABL;
                mHandler.sendMessage(msg);
                return;
            }catch (IOException e){
                e.printStackTrace();
               // Log.v("socketos", e.getCause().toString());
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                msg.arg2=TEXT_CLICK_ABL;
                mHandler.sendMessage(msg);
                return;
            }

            String recString;
            JSONObject jsonObject=null;
            try{
                BufferedReader bf =  new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                String jsonString = bf.readLine();
                Log.d("bfr", jsonString);
                JSONObject jobject = new JSONObject(jsonString);
                if((jobject.getInt("JsonType")!=3)){
                    throw new JSONException("");
                }
                recString=jobject.getString("Content");
                JSONArray jsonArray = new JSONArray(recString);
                jsonObject = jsonArray.getJSONObject(0);
            }catch (IOException e){
                //Log.v("bR IOException",e.getCause().toString());
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                msg.arg2=TEXT_CLICK_ABL;
                mHandler.sendMessage(msg);
                return;
            }catch (JSONException e){
                Toast.makeText(getApplicationContext(), "校验失败", Toast.LENGTH_SHORT).show();
                return;
            }finally {
                try {
                    mSocket.close();
                }catch (IOException e){
                    //return;
                }
                msg=mHandler.obtainMessage();
                msg.arg1=NEW_ACTIVITY;
                msg.arg2=TEXT_CLICK_ABL;
                msg.obj=(Object)jsonObject;
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            System.exit(0);


            return true;
        }
        return super.onKeyDown(keyCode, event);
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
