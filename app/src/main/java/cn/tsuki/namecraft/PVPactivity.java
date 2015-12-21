package cn.tsuki.namecraft;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.tsuki.namecraft.clientJson.GetHeroAttribute;
import cn.tsuki.namecraft.jsonTools.Jsons;


public class PVPactivity extends ActionBarActivity {


    private final String HOST="";
    private final int PORT=8888;

    private final int APPEND=1;
    private final int ACCOMPLISHED=2;
    private final int CONNECT_FAILED=3;
    private final int NEW_ACTIVITY=4;

    private Bundle mBundle;
    private Handler mHandler;

    private JSONArray jsonArray;


    private TextView mName;
    private TextView enemyName;
    private TextView mHP;
    private TextView enemyHP;


    private ListView listView;

    private String txt_mName;
    private String txt_enemyName;
    private int mTotHP;
    private int mNowHP;
    private int enemyTotHP;
    private int enemyNowHP;

    private int roundCount;
    private int nowRound=1;


    private int heroType=0;

    private SimpleAdapter simpleAdapter;
    List<Map<String,Object>> listItems ;


    private Timer timer;
    private TimerTask task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvp);
        mBundle=getIntent().getExtras();
        try {
            jsonArray = new JSONArray(mBundle.getString("fight"));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            txt_mName = jsonObject.getString("AttackID");
            txt_enemyName = jsonObject.getString("DefenseID");
            mTotHP=mNowHP=jsonObject.getInt("HPA");
            enemyTotHP=enemyNowHP=jsonObject.getInt("HPD");
            roundCount = jsonArray.length();
        }catch (JSONException e){

        }


        heroType = mBundle.getInt("type");
        ((TextView) findViewById(R.id.pvp_mine_agi)).setText(mBundle.getInt("Agi"));
        ((TextView)findViewById(R.id.pvp_mine_atk)).setText(mBundle.getInt("Atk"));
        ((TextView)findViewById(R.id.pvp_mine_str)).setText(mBundle.getInt("Power"));
        ((TextView)findViewById(R.id.pvp_mine_int)).setText(mBundle.getInt("Intel"));
        ((TextView)findViewById(R.id.pvp_mine_luc)).setText(mBundle.getInt("Lucky"));
        ((TextView)findViewById(R.id.pvp_mine_def)).setText(mBundle.getInt("Defense"));

        listView = (ListView)findViewById(R.id.pvp_listview);
        mName = (TextView)findViewById(R.id.pvp_mine_name);
        enemyName= (TextView)findViewById(R.id.pvp_enemy_name);
        mName.setText(txt_mName);
        enemyName.setText(txt_enemyName);

        mHP = (TextView)findViewById(R.id.pvp_mine_health);
        enemyHP = (TextView)findViewById(R.id.pvp_enemy_health);
        mHP.setText(mTotHP+"/"+mNowHP);
        enemyHP.setText(enemyTotHP+"/"+enemyNowHP);

        setListView();

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.arg1){
                    case APPEND:appendListView(msg.arg2);break;
                    case ACCOMPLISHED:
                        timerTaskCancel();
                        getAttiThread getAttiThread = new getAttiThread(HOST,PORT,mBundle.getString("ID"));
                        new Thread(getAttiThread).start();
                        break;
                    case CONNECT_FAILED:Toast.makeText(getApplicationContext(),"连接失败，请重新启动应用",Toast.LENGTH_SHORT).show();
                        Intent in =new Intent(PVPactivity.this,LaunchActivity.class);
                        startNewActivity(in);break;
                    case NEW_ACTIVITY:startGameActivity((JSONObject)msg.obj);
                        break;
                    default:break;
                }
            }
        };

    }

    private void startNewActivity(Intent in){
        startActivity(in);
        this.finish();
    }

    private void startGameActivity(JSONObject gameinfo){
        Intent in = new Intent(PVPactivity.this,GameActivity.class);
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


    private void setListView(){
        listItems = new ArrayList<Map<String,Object>>();
        Map<String,Object> listitem = new HashMap<String, Object>();
        listitem.put("fight_pro",mName+"对"+enemyName+"发起了挑战");
        listItems.add(listitem);
        simpleAdapter = new SimpleAdapter(PVPactivity.this,listItems,R.layout.pvp_list_item
                ,new String[]{"fight_pro"},
                new int[]{R.id.fight_pro});
        listView.setAdapter(simpleAdapter);
    }

    private void appendListView(int round){
        if(round>roundCount) {
            return;
        }
        String AttackID=null;
        String DefenseID=null;

        int Damage=-1;
        boolean IsMiss=false;

        // Ability Effects
        int AbIndex=-1;
        int HPA=0;
        int HPD=0;
        int AbDamage=0;
        int AbCure=0;
        String txt_Fight =null;
        JSONObject jsonObject=null;
        try {
            jsonObject = jsonArray.getJSONObject(round);
            AttackID = jsonObject.getString("AttackID");
            DefenseID = jsonObject.getString("DefenseID");
            Damage = jsonObject.getInt("Damage");
            IsMiss = jsonObject.getBoolean("IsMiss");
            AbIndex = jsonObject.getInt("AbIndex");
            HPA = jsonObject.getInt("HPA");
            HPD = jsonObject.getInt("HPD");
            AbDamage = jsonObject.getInt("AbDamage");
            AbCure = jsonObject.getInt("AbCure");
        }catch (JSONException e){

        }
        if(round%2==1) {
            mHP.setText(mTotHP + "/" + HPA);
            enemyHP.setText(enemyTotHP+"/"+HPD);
        }else{
            mHP.setText(mTotHP + "/" + HPD);
            enemyHP.setText(enemyTotHP+"/"+HPA);
        }
        //TODO 战斗过程的文字叙述
        if(AbIndex==-1) {
            txt_Fight = AttackID + "对" + DefenseID + "发动了攻击";
            if(IsMiss){
                txt_Fight+=(","+DefenseID+"侧身一闪，躲过了攻击");
            }else{
                txt_Fight+=(",造成了"+Damage+"点伤害");
            }
        }else{
            txt_Fight = AttackID + "发动技能，对" + DefenseID + "造成了"+AbDamage+"点伤害";
            if(AbCure>0){
                txt_Fight+=(",回复生命值"+AbCure+"点");
            }
        }

        if(HPD<=0){
            txt_Fight+=("\n"+AttackID+"获得胜利！");
        }




        Map<String,Object> listitem = new HashMap<String, Object>();
        listitem.put("fight_pro",txt_Fight);
        listItems.add(listitem);
        simpleAdapter.notifyDataSetChanged();

    }

    private void fight(){
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(nowRound<roundCount){
                            Message msg = mHandler.obtainMessage();
                            msg.arg1=APPEND;
                            msg.arg2=nowRound;
                            mHandler.sendMessage(msg);
                            nowRound++;
                        }else{
                            try {
                                Thread.sleep(3000);
                            }catch (InterruptedException e){

                            }
                            Message msg = mHandler.obtainMessage();
                            msg.arg1=ACCOMPLISHED;
                            mHandler.sendMessage(msg);
                        }
                    }
                });
            }
        };

        timer.schedule(task, 1, 1000);
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



    class getAttiThread implements Runnable{
        private String ID;
        private String host;
        private int port;

        public  getAttiThread(String host,int port,String ID){
            this.host=host;
            this.port=port;
            this.ID=ID;
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
                mHandler.sendMessage(msg);
                return;
            }catch (IOException e){
                e.printStackTrace();
                Log.v("socketos", e.getCause().toString());
                msg = mHandler.obtainMessage();
                msg.arg1=CONNECT_FAILED;
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
                if((jobject.getInt("JsonType")!=3)&&(!"RGetHeroAttribute".equals(jobject.getString("ObjectType")))){
                    throw new JSONException("");
                }
                recString=jobject.getString("Content");
                JSONArray jsonArray = new JSONArray(recString);
                jsonObject = jsonArray.getJSONObject(0);
            }catch (IOException e){
                Log.v("bR IOException",e.getCause().toString());
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
                    //return;
                }
                msg=mHandler.obtainMessage();
                msg.arg1=NEW_ACTIVITY;
                msg.obj=jsonObject;
                mHandler.sendMessage(msg);
            }

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pvpactivity, menu);
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
}
