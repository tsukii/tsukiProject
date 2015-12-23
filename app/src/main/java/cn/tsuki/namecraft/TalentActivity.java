package cn.tsuki.namecraft;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import cn.tsuki.namecraft.clientJson.Login;
import cn.tsuki.namecraft.jsonTools.Jsons;

/**
 * Created by tsuki on 2015/11/09.
 */
public class TalentActivity extends Activity{

    final private String HOST = HOSTInfo.HOST;
    final private int PORT = HOSTInfo.PORT;

    private final int CONNECTING=1;
    private final int CONNECT_FAILED=2;
    private final int CONNECT_SUCCESS=3;


    private int AttriAvailable;
    private int bePower;
    private int beIntel;
    private int beAgi;
    private int beLucky;
    private int afPower;
    private int afIntel;
    private int afAgi;
    private int afLucky;

    

    private Button increasebtn;
    private Button decreasebtn;


    private TextView  txt_AttriAvailable;
    private TextView  txt_bePower;
    private TextView  txt_beIntel;
    private TextView  txt_beAgi;
    private TextView  txt_beLucky;
    private TextView  txt_afPower;
    private TextView  txt_afIntel;
    private TextView  txt_afAgi;
    private TextView  txt_afLucky;
    
    private Bundle mBundle;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talent);
        mBundle = getIntent().getExtras();
        ((TextView)findViewById(R.id.talent_name)).setText(mBundle.getString("Name"));
        ((TextView)findViewById(R.id.talent_experience)).setText("lv." + mBundle.getInt("lv") + " " + mBundle.getInt("exp"));
        AttriAvailable = mBundle.getInt("AttriAvailable");
        bePower=afPower=mBundle.getInt("Power");
        beIntel=afIntel=mBundle.getInt("Intel");
        beAgi=afAgi=mBundle.getInt("Agi");
        beLucky=afLucky=mBundle.getInt("Lucky");
        txt_AttriAvailable = (TextView)findViewById(R.id.remainTalent);
        txt_bePower = (TextView)findViewById(R.id.strBeforeChanged);
        txt_afPower = (TextView)findViewById(R.id.strAfterChanged);
        txt_beIntel = (TextView)findViewById(R.id.intBeforeChanged);
        txt_afIntel = (TextView)findViewById(R.id.intAfterChanged);
        txt_beAgi = (TextView)findViewById(R.id.agiBeforeChanged);
        txt_afAgi = (TextView)findViewById(R.id.agiAfterChanged);
        txt_beLucky = (TextView)findViewById(R.id.lucBeforeChanged);
        txt_afLucky = (TextView)findViewById(R.id.lucAfterChanged);

        txt_AttriAvailable.setText(""+AttriAvailable);
        txt_bePower.setText(""+bePower);
        txt_beLucky.setText(""+beLucky);
        txt_beIntel.setText(""+beIntel);
        txt_beAgi.setText(""+beAgi);
        txt_afPower.setText(""+afPower);
        txt_afIntel.setText(""+afIntel);
        txt_afAgi.setText(""+afAgi);
        txt_afLucky.setText(""+afLucky);

        increasebtn = (Button)findViewById(R.id.increasebtn1);
        increasebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AttriAvailable > 0) {
                    afPower++;
                    AttriAvailable--;
                    txt_AttriAvailable.setText(""+AttriAvailable);
                    txt_afPower.setText(""+afPower);
                }
            }
        });
        decreasebtn = (Button)findViewById(R.id.decreasebtn1);
        decreasebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (afPower > bePower) {
                    afPower--;
                    AttriAvailable++;
                    txt_AttriAvailable.setText(""+AttriAvailable);
                    txt_afPower.setText(""+afPower);
                }
            }
        });
        increasebtn = (Button)findViewById(R.id.increasebtn2);
        increasebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AttriAvailable>0){
                    afIntel++;
                    AttriAvailable--;
                    txt_AttriAvailable.setText(""+AttriAvailable);
                    txt_afIntel.setText(""+afIntel);
                }
            }
        });
        decreasebtn = (Button)findViewById(R.id.decreasebtn2);
        decreasebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(afIntel>beIntel){
                    afIntel--;
                    AttriAvailable++;
                    txt_AttriAvailable.setText(""+AttriAvailable);
                    txt_afIntel.setText(""+afIntel);
                }
            }
        });
        increasebtn = (Button)findViewById(R.id.increasebtn3);
        increasebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AttriAvailable>0){
                    afAgi++;
                    AttriAvailable--;
                    txt_AttriAvailable.setText(""+AttriAvailable);
                    txt_afAgi.setText(""+afAgi);
                }
            }
        });
        decreasebtn = (Button)findViewById(R.id.decreasebtn3);
        decreasebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(afAgi>beAgi){
                    afAgi--;
                    AttriAvailable++;
                    txt_AttriAvailable.setText(""+AttriAvailable);
                    txt_afAgi.setText(""+afAgi);
                }
            }
        });
        increasebtn = (Button)findViewById(R.id.increasebtn4);
        increasebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AttriAvailable>0){
                    afLucky++;
                    AttriAvailable--;
                    txt_AttriAvailable.setText(""+AttriAvailable);
                    txt_afLucky.setText(""+afLucky);
                }
            }
        });
        decreasebtn = (Button)findViewById(R.id.decreasebtn4);
        decreasebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(afLucky>beLucky){
                    afLucky--;
                    AttriAvailable++;
                    txt_AttriAvailable.setText(""+AttriAvailable);
                    txt_afLucky.setText(""+afLucky);
                }
            }
        });
        ((Button)findViewById(R.id.talent_confirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmThread  confirmTh = new confirmThread(mBundle.getString("ID")
                                                             ,AttriAvailable
                                                             ,afPower
                                                             ,afIntel
                                                             ,afAgi
                                                             ,afLucky
                                                             ,mBundle.getInt("AbiliAvailable")
                                                             ,mBundle.getInt("AbilityLevel1")
                                                             ,mBundle.getInt("AbilityLevel2")
                                                             ,mBundle.getInt("AbilityLevel3")
                                                             ,mBundle.getInt("AbilityLevel4")
                                                             ,mBundle.getInt("AbilityLevel5")
                                                             ,mBundle.getInt("AbilityLevel6")
                                                             ,PORT
                                                             ,HOST) ;
                new Thread(confirmTh).start();
            }
        });
        ((Button)findViewById(R.id.talent_return)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in =new Intent(TalentActivity.this,GameActivity.class);
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
                    case CONNECTING:findViewById(R.id.bottomRlayout).setVisibility(View.GONE);
                        findViewById(R.id.talent_mid_layout).setVisibility(View.GONE);
                        findViewById(R.id.talent_connect).setVisibility(View.VISIBLE);break;
                    case CONNECT_SUCCESS:startGameActivity((JSONObject)msg.obj);break;
                    case CONNECT_FAILED:
                        ((TextView)findViewById(R.id.talent_connect)).setText("连接失败，即将重启应用");
                        Intent in =new Intent(TalentActivity.this,LaunchActivity.class);
                        startActivity(in);
                        finish();
                        break;
                    default:break;
                }
            }
        };
    }


    private void startGameActivity(JSONObject gameinfo){
        Intent in = new Intent(TalentActivity.this,GameActivity.class);
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

}
