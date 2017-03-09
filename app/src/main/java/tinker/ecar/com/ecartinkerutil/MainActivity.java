package tinker.ecar.com.ecartinkerutil;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import ecar.com.ecartinker.util.TinkerManager;

import static tinker.ecar.com.ecartinkerutil.R.id.tv_text;

public class MainActivity extends AppCompatActivity {

    private TextView tv_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_text = (TextView) findViewById(R.id.tv_text);

        testTinker();
    }

    private void testTinker() {

        TinkerManager.onReceiveUpgradePatch(getApplication(), BuildConfig.VERSION_CODE);
        common();
//        patch();
        TinkerManager.cleanPatch(this);

//
    }

    @SuppressLint("NewApi")
    private void patch() {
        tv_text.setText("补丁");
        tv_text.setTextColor(getResources().getColor(R.color.colorPrimaryDark, null));

    }

    private void common() {
        tv_text.setText("正常");
        tv_text.setTextColor(Color.BLACK);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }
}
