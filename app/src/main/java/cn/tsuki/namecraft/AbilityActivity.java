package cn.tsuki.namecraft;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import cn.tsuki.namecraft.clientJson.Allocate;
import cn.tsuki.namecraft.clientJson.GetHeroAttribute;
import cn.tsuki.namecraft.jsonTools.Jsons;

/**
 * Created by 下一次 on 2015/12/21.
 */
public class AbilityActivity extends Activity {


    final private String HOST = HOSTInfo.HOST;
    final private int PORT = HOSTInfo.PORT;

    private final int CONNECTING=1;
    private final int CONNECT_FAILED=2;
    private final int CONNECT_SUCCESS=3;

    private int AbiliAvailable;
    private int Ability_1;
    private int Ability_2;
    private int HeroType;

    private ImageView Ability_img_1;
    private ImageView Ability_img_2;

    private TextView Ability_info_1;
    private TextView Ability_info_2;
    private TextView Ability_txt_1;
    private TextView Ability_txt_2;

    private TextView AbiliAvailable_txt;


    private Bundle mBundle;
    private Handler mHandler;

    private String Ab_txt_1;
    private String Ab_txt_2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ability);

        mBundle = getIntent().getExtras();
        AbiliAvailable = mBundle.getInt("AbiliAvailable");
        HeroType = mBundle.getInt("type");

        ((TextView)findViewById(R.id.ability_name)).setText(mBundle.getString("Name"));
        ((TextView)findViewById(R.id.ability_exp)).setText("lv." + mBundle.getInt("lv") + " " + mBundle.getInt("exp"));


        Ability_img_1 = (ImageView)findViewById(R.id.Ability_1_img);
        Ability_img_2 = (ImageView)findViewById(R.id.Ability_2_img);

        Ability_info_1 = (TextView)findViewById(R.id.Ability_1_lv);
        Ability_info_2 = (TextView)findViewById(R.id.Ability_2_lv);
        Ability_txt_1 = (TextView)findViewById(R.id.Ability_1_txt);
        Ability_txt_2 = (TextView)findViewById(R.id.Ability_2_txt);

        AbiliAvailable_txt = (TextView)findViewById(R.id.Ab_remainTalent);
        AbiliAvailable_txt.setText(""+AbiliAvailable);

        if(HeroType==1){
            Ab_txt_1 = getResources().getString(R.string.Ab1_1);
            Ab_txt_2 = getResources().getString(R.string.Ab1_2);
            Ability_1 = mBundle.getInt("AbilityLevel1");
            Ability_info_1.setText(Ab_txt_1+"\n"+Ability_1);
            Ability_txt_1.setText(R.string.Ab1_1_txt);
            if(Ability_1<5) {
                Ability_img_1.setImageDrawable(getResources().getDrawable(R.drawable.ab1_1_1));
            }else{
                Ability_img_1.setImageDrawable(getResources().getDrawable(R.drawable.ab1_1_2));
            }
            Ability_2 = mBundle.getInt("AbilityLevel2");
            Ability_info_2.setText(Ab_txt_2+"\n"+Ability_2);
            Ability_txt_2.setText(R.string.Ab1_2_txt);
            Ability_img_2.setImageDrawable(getResources().getDrawable(R.drawable.ab1_2));

        }else if(HeroType==2){
            Ab_txt_1 = getResources().getString(R.string.Ab2_1);
            Ab_txt_2 = getResources().getString(R.string.Ab2_2);
            Ability_1 = mBundle.getInt("AbilityLevel3");
            Ability_info_1.setText(Ab_txt_1+"\n"+Ability_1);
            Ability_txt_1.setText(R.string.Ab2_1_txt);
            if(Ability_1<5) {
                Ability_img_1.setImageDrawable(getResources().getDrawable(R.drawable.ab2_1_1));
            }else{
                Ability_img_1.setImageDrawable(getResources().getDrawable(R.drawable.ab2_1_2));
            }
            Ability_2 = mBundle.getInt("AbilityLevel4");
            Ability_info_2.setText(Ab_txt_2+"\n"+Ability_2);
            Ability_txt_2.setText(R.string.Ab2_2_txt);
            Ability_img_2.setImageDrawable(getResources().getDrawable(R.drawable.ab2_2));

        }else{
            Ab_txt_1 = getResources().getString(R.string.Ab3_1);
            Ab_txt_2 = getResources().getString(R.string.Ab3_2);
            Ability_1 = mBundle.getInt("AbilityLevel5");
            Ability_info_1.setText(Ab_txt_1+"\n"+Ability_1);
            Ability_txt_1.setText(R.string.Ab3_1_txt);
            if(Ability_1<5) {
                Ability_img_1.setImageDrawable(getResources().getDrawable(R.drawable.ab3_1_1));
            }else{
                Ability_img_1.setImageDrawable(getResources().getDrawable(R.drawable.ab3_1_2));
            }
            Ability_2 = mBundle.getInt("AbilityLevel6");
            Ability_info_2.setText(Ab_txt_2+"\n"+Ability_2);
            Ability_txt_2.setText(R.string.Ab3_2_txt);
            Ability_img_2.setImageDrawable(getResources().getDrawable(R.drawable.ab3_2));

        }

        Ability_img_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AbiliAvailable>0){
                    Ability_1++;
                    AbiliAvailable--;
                    Ability_info_1.setText(Ab_txt_1+"\n"+Ability_1);
                    AbiliAvailable_txt.setText(""+AbiliAvailable);
                    if(Ability_1==5) {
                        if (HeroType == 1) {
                            Ability_img_1.setImageDrawable(getResources().getDrawable(R.drawable.ab1_1_2));
                        } else if (HeroType == 2) {
                            Ability_img_1.setImageDrawable(getResources().getDrawable(R.drawable.ab2_1_2));
                        } else {
                            Ability_img_1.setImageDrawable(getResources().getDrawable(R.drawable.ab3_1_2));
                        }
                    }
                }
            }
        });

        Ability_img_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AbiliAvailable>0){
                    Ability_2++;
                    AbiliAvailable--;
                    Ability_info_2.setText(Ab_txt_2+"\n"+Ability_2);
                    AbiliAvailable_txt.setText(""+AbiliAvailable);
                }
            }
        });

        ((Button)findViewById(R.id.Ab_confirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a1=0,a2=0,a3=0,a4=0,a5=0,a6=0;
                if(HeroType==1){
                    a1 =Ability_1;
                    a2 =Ability_2;
                }else if(HeroType==2){
                    a3 =Ability_1;
                    a4 =Ability_2;
                }else{
                    a5 =Ability_1;
                    a6 =Ability_2;
                }
                confirmThread  confirmTh = new confirmThread(mBundle.getString("ID")
                        ,mBundle.getInt("AttriAvailable")
                        ,mBundle.getInt("Power")
                        ,mBundle.getInt("Intel")
                        ,mBundle.getInt("Agi")
                        ,mBundle.getInt("Lucky")
                        ,AbiliAvailable
                        ,a1
                        ,a2
                        ,a3
                        ,a4
                        ,a5
                        ,a6
                        ,PORT
                        ,HOST) ;
                new Thread(confirmTh).start();
            }
        });
        ((Button)findViewById(R.id.Ab_return)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(AbilityActivity.this, GameActivity.class);
                in.putExtras(mBundle);
                startActivity(in);
                finish();
            }
        });
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.arg1){
                    case CONNECTING:findViewById(R.id.Ab_bottomRlayout).setVisibility(View.GONE);
                        findViewById(R.id.Ab_mid_layout).setVisibility(View.GONE);
                        findViewById(R.id.ability_connect).setVisibility(View.VISIBLE);break;
                    case CONNECT_SUCCESS:startGameActivity((JSONObject)msg.obj);break;
                    case CONNECT_FAILED:
                        ((TextView)findViewById(R.id.ability_connect)).setText("连接失败，即将重启应用");
                        Intent in =new Intent(AbilityActivity.this,LaunchActivity.class);
                        startActivity(in);
                        finish();
                        break;
                    default:break;
                }
            }
        };

    }

    private void startGameActivity(JSONObject gameinfo){
        Intent in = new Intent(AbilityActivity.this,GameActivity.class);
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
            Log.d("json Convert", "filed");
        }
        in.putExtras(mBundle);
        startActivity(in);
        this.finish();
    }

    class confirmThread implements Runnable{
        private String host;
        private int port;
        private Allocate allocate;

        private GetHeroAttribute getHeroAttribute;
        public confirmThread(String roleid,int attotal, int power, int intel, int agi, int lucky, int abtotal, int abilityLevel1,
                             int abilityLevel2, int abilityLevel3, int abilityLevel4, int abilityLevel5, int abilityLevel6, int port, String host) {
            allocate = new Allocate(roleid,attotal,power, intel,  agi,  lucky,  abtotal,  abilityLevel1,
                    abilityLevel2,  abilityLevel3,  abilityLevel4,  abilityLevel5,  abilityLevel6);
            getHeroAttribute = new GetHeroAttribute(getSharedPreferences("setting", Context.MODE_PRIVATE).getString("ID", ""));
            this.port = port;
            this.host = host;
        }

        @Override
        public void run() {
            Message msg = mHandler.obtainMessage();
            msg.arg1=CONNECTING;
            mHandler.sendMessage(msg);

            Socket mSocket =null;
            try{
                mSocket=new Socket(host,port);
                mSocket.setSoTimeout(1000);
            }catch (IOException e){
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
                //mSocket.close();
                return;
            }

            try {
                ArrayList<Allocate> alist = new ArrayList<>();
                alist.add(allocate);
                String jsonString = Jsons.buildJson(1, "Allocate", 1, Jsons.buildJsonArray(alist).toString());
                Log.d("jsons", jsonString);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
                writer.write(jsonString);writer.newLine();
                writer.flush();
            }catch (JSONException e){
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
                return;
            }catch (IOException e){
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
                return;
            }

            try{
                BufferedReader bf =  new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                String jsonString = bf.readLine();
                Log.d("bfr", jsonString);
                JSONObject jobject = new JSONObject(jsonString);
                if((jobject.getInt("JsonType")!=1)){
                    throw new JSONException("");
                }
                String recString=jobject.getString("Content");
                JSONArray jsonArray = new JSONArray(recString);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                if(jsonObject.getInt("ReturnNum")==0){
                    throw new JSONException("");
                }
            }catch (IOException e){
                Log.v("bR IOException","");
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
                return;
            }catch (JSONException e){
                Toast.makeText(getApplicationContext(), "校验失败", Toast.LENGTH_SHORT).show();
                //return;
            }

            try {
                ArrayList<GetHeroAttribute> alist = new ArrayList<>();
                alist.add(getHeroAttribute);
                String jsonString = Jsons.buildJson(3, "GetHeroAttribute", 1, Jsons.buildJsonArray(alist).toString());
                Log.d("jsons", jsonString);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
                writer.write(jsonString);writer.newLine();
                writer.flush();
            }catch (JSONException e){
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
                return;
            }catch (IOException e){
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
                return;
            }

            JSONObject jsonObject=null;
            try{
                BufferedReader bf =  new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                String jsonString = bf.readLine();
                Log.d("bfr", jsonString);
                JSONObject jobject = new JSONObject(jsonString);
                if((jobject.getInt("JsonType")!=3)){
                    throw new JSONException("");
                }
                String recString=jobject.getString("Content");
                JSONArray jsonArray = new JSONArray(recString);
                jsonObject = jsonArray.getJSONObject(0);
            }catch (IOException e){
                Log.v("bR IOException","");
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
                return;
            }catch (JSONException e){
                Toast.makeText(getApplicationContext(), "校验失败", Toast.LENGTH_SHORT).show();
                return;
            }finally {
                try {
                    mSocket.close();
                }catch (IOException e){

                }
            }

            msg= mHandler.obtainMessage();
            msg.arg1=CONNECT_SUCCESS;
            msg.obj=jsonObject;
            mHandler.sendMessage(msg);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent in =new Intent(AbilityActivity.this,GameActivity.class);
            in.putExtras(mBundle);
            startActivity(in);
            finish();
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
