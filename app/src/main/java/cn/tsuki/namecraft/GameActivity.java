package cn.tsuki.namecraft;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.os.Handler;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.tsuki.namecraft.clientJson.GetHeroAttribute;
import cn.tsuki.namecraft.clientJson.GetUserList;
import cn.tsuki.namecraft.clientJson.PVE;
import cn.tsuki.namecraft.clientJson.PVP;
import cn.tsuki.namecraft.jsonTools.Jsons;

/**
 * Created by tsuki on 2015/10/28.
 */
public class GameActivity extends Activity {
    private final int CONNECTING=1;
    private final int CONNECT_FAILED=2;
    private final int CONNECT_SUCCESS=3;
    private final int PVP_START=4;
   // private final int PVE_START=5;


    final private String HOST = HOSTInfo.HOST;
    final private int PORT = HOSTInfo.PORT;


    private ImageView talentbtn;
    private ImageView skillbtn;
    private ImageView pvpbtn;
    private ImageView pvebtn;


    private LinearLayout dialog;
    private TextView dialog_txt;
    private ListView dialog_listView;
    private LinearLayout dialog_dif_layout;

    private Button difficulty_easy;
    private Button difficulty_normal;
    private Button difficulty_difficult;

    private int power;
    private int intel;
    private int agi;
    private int lucky;
    private int atk;
    private int defense;
    private int hp;
    private int AbiliAvailable;
    private int AttriAvailable;
    private int lv;
    private int exp;

    private String name;
    private String ID;

    private Handler mHandler;
    private Bundle mainBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mainBundle = getIntent().getExtras();
        talentbtn=(ImageView)findViewById(R.id.talent);
        skillbtn=(ImageView)findViewById(R.id.skill);
        pvpbtn=(ImageView)findViewById(R.id.pvp);
        pvebtn=(ImageView)findViewById(R.id.pve);
        hp = mainBundle.getInt("HP");
        name = mainBundle.getString("Name");
        lv=mainBundle.getInt("lv");
        exp=mainBundle.getInt("exp");
        ((TextView)findViewById(R.id.game_name)).setText(name+" "+hp);
        ((TextView)findViewById(R.id.game_exp)).setText("lv."+lv+" | "+exp);
        power=mainBundle.getInt("Power");
        intel=mainBundle.getInt("Intel");
        agi=mainBundle.getInt("Agi");
        lucky=mainBundle.getInt("Lucky");
        atk=mainBundle.getInt("Atk");
        defense=mainBundle.getInt("Defense");
        AbiliAvailable=mainBundle.getInt("AbiliAvailable");
        AttriAvailable=mainBundle.getInt("AttriAvailable");
        ID = mainBundle.getString("ID");
        ((TextView)findViewById(R.id.game_intel)).setText(""+intel);
        ((TextView)findViewById(R.id.game_agi)).setText(""+agi);
        ((TextView)findViewById(R.id.game_lucky)).setText(""+lucky);
        ((TextView)findViewById(R.id.game_atk)).setText(""+atk);
        ((TextView)findViewById(R.id.game_defense)).setText(""+defense);
        ((TextView)findViewById(R.id.game_power)).setText(""+power);
        dialog = (LinearLayout)findViewById(R.id.game_dialog);
        dialog_txt = (TextView)findViewById(R.id.game_dialog_txt);
        dialog_listView = (ListView)findViewById(R.id.game_dialog_listView);
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        dialog.getLayoutParams().height=outMetrics.heightPixels*3/4;

        difficulty_easy = (Button)findViewById(R.id.game_dialog_diffi1);
        difficulty_normal=(Button)findViewById(R.id.game_dialog_diffi2);
        difficulty_difficult=(Button)findViewById(R.id.game_dialog_diffi3);
        dialog_dif_layout = (LinearLayout)findViewById(R.id.game_dialog_difficult);
        difficulty_easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pveThread pveThread = new pveThread(HOST,PORT,ID,1);
                new Thread(pveThread).start();
            }
        });
        difficulty_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pveThread pveThread = new pveThread(HOST,PORT,ID,2);
                new Thread(pveThread).start();
            }
        });
        difficulty_difficult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pveThread pveThread = new pveThread(HOST,PORT,ID,3);
                new Thread(pveThread).start();
            }
        });
        talentbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(GameActivity.this,TalentActivity.class);
                        in.putExtras(mainBundle);
                        startActivity(in);
                        finish();
                    }
                }
        );
        skillbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in =new Intent(GameActivity.this,AbilityActivity.class);
                        in.putExtras(mainBundle);
                        startNewActivity(in);
                    }
                }
        );
        pvpbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.setVisibility(View.VISIBLE);
                        getUserListThread getUserListThread = new getUserListThread(HOST,PORT,ID);
                        new Thread(getUserListThread).start();

                    }
                }
        );
        pvebtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_txt.setText("请选择难度");
                        dialog.setVisibility(View.VISIBLE);
                        dialog_listView.setVisibility(View.GONE);
                        dialog_dif_layout.setVisibility(View.VISIBLE);
                    }
                }
        );

        ((TextView)findViewById(R.id.game_about)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) findViewById(R.id.game_about_layout)).setVisibility(View.VISIBLE);
            }
        });

        ((Button)findViewById(R.id.game_about_layout_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) findViewById(R.id.game_about_layout)).setVisibility(View.GONE);
            }
        });

        dialog_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,Object> hashMap = (HashMap)dialog_listView.getItemAtPosition(position);
                String enemyID = hashMap.get("game_lsv_id").toString();
                Log.v("Hashmap",enemyID);

                pvpThread pvpThread = new pvpThread(HOST,PORT,ID,enemyID);
                new Thread(pvpThread).start();

            }
        });
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.arg1){
                    case CONNECT_FAILED:
                        Toast.makeText(getApplicationContext(),"连接失败，请重新启动应用",Toast.LENGTH_SHORT).show();
                        Intent in =new Intent(GameActivity.this,LaunchActivity.class);
                        startNewActivity(in);break;
                    case CONNECT_SUCCESS:
                        dialog_txt.setText("请选择对手");
                        dialog_listView.setVisibility(View.VISIBLE);
                        setListView((JSONArray) msg.obj);break;
                    case PVP_START:
                        mainBundle.putString("fight", (String) msg.obj);
                        Intent intopvp =new Intent(GameActivity.this,PVPactivity.class);
                        intopvp.putExtras(mainBundle);
                        startNewActivity(intopvp);break;
                    default:break;
                }
            }
        };



    }


    private void setListView(JSONArray jsonArray){
        String HeroName;
        String RoleID;
        int Level;
        int HeroType;
        List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
        for(int i=0;i<jsonArray.length();i++){
            HeroName =null;
            RoleID =null;
            Level =0;
            HeroType=0;
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                HeroName = jsonObject.getString("HeroName");
                RoleID = jsonObject.getString("RoleID");
                Level = jsonObject.getInt("Level");
                HeroType = jsonObject.getInt("HeroType");
            }catch (JSONException e){

            }
            //game_lsv_name
            //game_lsv_lv
            //game_lsv_hero
            //game_lsv_id
            Map<String,Object> listitem = new HashMap<String, Object>();
            listitem.put("game_lsv_name",HeroName);
            listitem.put("game_lsv_lv",Level);
            if(HeroType==1){
                listitem.put("game_lsv_hero","战士");
            }else if(HeroType==2){
                listitem.put("game_lsv_hero","忍者");
            }else{
                listitem.put("game_lsv_hero", "法师");
            }
            //listitem.put("game_lsv_hero", HeroType);
            listitem.put("game_lsv_id", RoleID);
            listItems.add(listitem);

            SimpleAdapter adapter = new SimpleAdapter(GameActivity.this,listItems,R.layout.game_pvplist
                    ,new String[]{"game_lsv_name","game_lsv_lv","game_lsv_hero","game_lsv_id"},
                    new int[]{R.id.game_lsv_name,R.id.game_lsv_lv,R.id.game_lsv_hero,R.id.game_lsv_id});

            dialog_listView.setAdapter(adapter);

        }
    }



    private void startNewActivity(Intent in){
        startActivity(in);
        this.finish();
    }
    

    class getUserListThread implements Runnable{


        String host;
        int port;
        GetUserList getUserList;


        public getUserListThread(String host, int port, String userID) {
            this.host = host;
            this.port = port;
            getUserList = new GetUserList(userID);
        }

        @Override
        public void run() {
            Message msg;
            Socket mSocket =null;
            try{
                mSocket=new Socket(host,port);
            }catch (IOException e){
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
                return;
            }

            try {
                ArrayList<GetUserList> alist = new ArrayList<>();
                alist.add(getUserList);
                String jsonString = Jsons.buildJson(4, "GetUserList", 1, Jsons.buildJsonArray(alist).toString());
                Log.d("jsons", jsonString);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
                writer.write(jsonString);writer.newLine();
                writer.flush();
            }catch (JSONException e){
                e.printStackTrace();
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
                return;
            }catch (IOException e){
                e.printStackTrace();
                Log.v("socketos", "");
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
                return;
            }


            String recString;
            JSONArray jsonArray =null;
            try{
                BufferedReader bf =  new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                String jsonString = bf.readLine();
                Log.d("bfr", jsonString);
                JSONObject jobject = new JSONObject(jsonString);
                if((jobject.getInt("JsonType")!=4)){
                    throw new JSONException("");
                }
                recString=jobject.getString("Content");
                jsonArray = new JSONArray(recString);
            }catch (IOException e){
                Log.v("bR IOException","");
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
                return;
            }catch (JSONException e){
                Toast.makeText(getApplicationContext(), "校验失败", Toast.LENGTH_SHORT).show();
                return;
            }


            msg =mHandler.obtainMessage();
            msg.arg1=CONNECT_SUCCESS;
            msg.obj=jsonArray;
            mHandler.sendMessage(msg);

        }
    }


    class pvpThread implements Runnable{
        public pvpThread(String host, int port, String userID, String enemyID) {
            this.host = host;
            this.port = port;
            pvp= new PVP(userID,enemyID);
        }

        String host;
        int port;
        PVP pvp;
        @Override
        public void run() {

            Message msg;
            Socket mSocket =null;
            try{
                mSocket=new Socket(host,port);
            }catch (IOException e){
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
                return;
            }

            try {
                ArrayList<PVP> alist = new ArrayList<>();
                alist.add(pvp);
                String jsonString = Jsons.buildJson(7, "PVP", 1, Jsons.buildJsonArray(alist).toString());
                Log.d("jsons", jsonString);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
                writer.write(jsonString);writer.newLine();
                writer.flush();
            }catch (JSONException e){
                e.printStackTrace();
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
                return;
            }catch (IOException e){
                e.printStackTrace();
                Log.v("socketos", "");
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
                return;
            }


            String recString;
            JSONArray jsonArray =null;
            try{
                BufferedReader bf =  new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                String jsonString = bf.readLine();
                Log.d("bfr", jsonString);
                JSONObject jobject = new JSONObject(jsonString);
                if((jobject.getInt("JsonType")!=7)){
                    throw new JSONException("");
                }
                recString=jobject.getString("Content");
            }catch (IOException e){
                Log.v("bR IOException","");
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
                return;
            }catch (JSONException e){
                Toast.makeText(getApplicationContext(), "校验失败", Toast.LENGTH_SHORT).show();
                return;
            }


            msg =mHandler.obtainMessage();
            msg.arg1=PVP_START;
            msg.obj=recString;
            mHandler.sendMessage(msg);
        }
    }

    class pveThread implements Runnable{
        public pveThread(String host, int port, String userID, int Difficulty) {
            this.host = host;
            this.port = port;
            pve= new PVE(Difficulty,userID);
        }

        String host;
        int port;
        PVE pve;
        @Override
        public void run() {

            Message msg;
            Socket mSocket =null;
            try{
                mSocket=new Socket(host,port);
            }catch (IOException e){
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
                return;
            }

            try {
                ArrayList<PVE> alist = new ArrayList<>();
                alist.add(pve);
                String jsonString = Jsons.buildJson(6, "PVE", 1, Jsons.buildJsonArray(alist).toString());
                Log.d("jsons", jsonString);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
                writer.write(jsonString);writer.newLine();
                writer.flush();
            }catch (JSONException e){
                e.printStackTrace();
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
                return;
            }catch (IOException e){
                e.printStackTrace();
                Log.v("socketos", "");
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
                return;
            }


            String recString;
            JSONArray jsonArray =null;
            try{
                BufferedReader bf =  new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                String jsonString = bf.readLine();
                Log.d("bfr", jsonString);
                JSONObject jobject = new JSONObject(jsonString);
                if((jobject.getInt("JsonType")!=6)){
                    throw new JSONException("");
                }
                recString=jobject.getString("Content");
            }catch (IOException e){
                Log.v("bR IOException","");
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
                return;
            }catch (JSONException e){
                Toast.makeText(getApplicationContext(), "校验失败", Toast.LENGTH_SHORT).show();
                return;
            }catch (NullPointerException e){
                Log.v("bR IOException","");
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
                mHandler.sendMessage(msg);
                return;
            }


            msg =mHandler.obtainMessage();
            msg.arg1=PVP_START;
            msg.obj=recString;
            mHandler.sendMessage(msg);
        }
    }
}
