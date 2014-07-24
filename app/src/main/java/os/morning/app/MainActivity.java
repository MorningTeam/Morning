package os.morning.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import os.morning.app.ui.SignIn;

/**
 *  系统启动Activity
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //直接跳转至登录界面
        Intent sign_in = new Intent(this, SignIn.class);
        startActivity(sign_in);
        finish();
    }

}
