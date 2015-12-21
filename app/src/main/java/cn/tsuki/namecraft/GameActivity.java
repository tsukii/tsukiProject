package cn.tsuki.namecraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Handler;

/**
 * Created by tsuki on 2015/10/28.
 */
public class GameActivity extends Activity {
    private final int CONNECTING=1;
    private final int CONNECT_FAILED=2;
    private final int CONNECT_SUCCESS=3;



    private ImageView talentbtn;
    private ImageView skillbtn;
    private ImageView pvpbtn;
    private ImageView pvebtn;


    private LinearLayout dialog;
    private TextView dialog_txt;
    private ListView dialog_listView;


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
        name = mainBundle.getString("name");
        lv=mainBundle.getInt("lv");
        exp=mainBundle.getInt("exp");
        ((TextView)findViewById(R.id.game_name)).setText(name+" "+hp);
        ((TextView)findViewById(R.id.game_exp)).setText("lv."+lv+" "+exp);
        power=mainBundle.getInt("Power");
        intel=mainBundle.getInt("Intel");
        agi=mainBundle.getInt("Agi");
        lucky=mainBundle.getInt("Lucky");
        atk=mainBundle.getInt("Atk");
        defense=mainBundle.getInt("Defense");
        AbiliAvailable=mainBundle.getInt("AbiliAvailable");
        AttriAvailable=mainBundle.getInt("AttriAvailable");
        ((TextView)findViewById(R.id.game_intel)).setText(intel);
        ((TextView)findViewById(R.id.game_agi)).setText(agi);
        ((TextView)findViewById(R.id.game_lucky)).setText(lucky);
        ((TextView)findViewById(R.id.game_atk)).setText(atk);
        ((TextView)findViewById(R.id.game_defense)).setText(defense);
        ((TextView)findViewById(R.id.game_power)).setText(power);
        talentbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(GameActivity.this,TalentActivity.class);
                        in.putExtras(mainBundle);
                        startNewActivity(in);
                    }
                }
        );
        skillbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );
        pvpbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GameActivity.this,PVPactivity.class);

                    }
                }
        );
        pvebtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );



    }

    private void startNewActivity(Intent in){
        startActivity(in);
        this.finish();
    }
    

}
