package cn.tsuki.namecraft;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by tsuki on 2015/10/28.
 */
public class GameAcitivity extends Activity {
    private ImageView talentbtn;
    private ImageView skillbtn;
    private ImageView pvpbtn;
    private ImageView pvebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        talentbtn=(ImageView)findViewById(R.id.talent);
        skillbtn=(ImageView)findViewById(R.id.skill);
        pvpbtn=(ImageView)findViewById(R.id.pvp);
        pvebtn=(ImageView)findViewById(R.id.pve);
        talentbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

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

}
