package cn.tsuki.namecraft;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by tsuki on 2015/11/09.
 */
public class TalentActivity extends Activity{

    private int remain;

    private Button increasebtn;
    private Button decreasebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talent);
        remain = Integer.parseInt(((TextView) findViewById(R.id.remainTalent)).getText().toString());

    }
}
