package os.morning.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import os.morning.app.R;

/**
 * 系统登录窗口处理
 * @author ZiQin
 * @version 1.0
 * @created 2014/7/18
 */
public class SignIn extends Activity {

    private EditText uname;
    private EditText pword;
    private Button btnLogin ;

    private SharedPreferences sp;
    private SharedPreferences.Editor spedit;
    private CheckBox cb_keepPass, cb_autoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().hide();
        setContentView(R.layout.sign_in);

        doPreLogin();

    }

    /**
     * 处理已存储用户信息，以及记住密码、自动登录状态
     */
    private void doPreLogin() {
        btnLogin =(Button) findViewById(R.id.btnSingIn);

        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        spedit = sp.edit();
        cb_keepPass = (CheckBox) findViewById(R.id.cb_keepPass);
        cb_autoLogin = (CheckBox) findViewById(R.id.cb_autoLogin);
        uname = (EditText) findViewById(R.id.etUserName);
        pword = (EditText) findViewById(R.id.etPass);

        //若已有用户成功登陆，则获取并设置USERNAME 无责已“”代替
        String keep_username = sp.getString("USER_NAME", "");
        uname.setText(keep_username);

        if(sp.getBoolean("REMEMBER_PASSWD", false))
        {
            //设置默认是记录密码状态
            cb_keepPass.setChecked(true);
            String keep_password = sp.getString("PASSWORD", "");
            pword.setText(keep_password);
            //判断自动登陆多选框状态
            if(sp.getBoolean("AUTO_LOGIN", false))
            {
                //设置默认是自动登录状态
                cb_autoLogin.setChecked(true);

                if("".equalsIgnoreCase(keep_username) || "".equalsIgnoreCase(keep_password)){
                    spedit.putBoolean("REMEMBER_PASSWD", false);
                    spedit.putBoolean("AUTO_LOGIN", false);
                    spedit.commit();
                }else{
                    doLogin(keep_username, keep_password);
                }
            }
        }

        // register keep password event
        cb_keepPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (cb_keepPass.isChecked()) {
                    spedit.putBoolean("REMEMBER_PASSWD", true);
                }else {
                    cb_autoLogin.setChecked(false);
                    spedit.putBoolean("REMEMBER_PASSWD", false);
                    spedit.putBoolean("AUTO_LOGIN", false);
                }
                spedit.commit();
            }
        });

        //register auto login event
        cb_autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (cb_autoLogin.isChecked()) {
                    if(!cb_keepPass.isChecked()){
                        cb_keepPass.setChecked(true);
                    }
                    spedit.putBoolean("AUTO_LOGIN", true);
                }else{
                    spedit.putBoolean("AUTO_LOGIN", false);
                }
                spedit.commit();
            }
        });


        //register user login event
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if ((!uname.getText().toString().equals("")) && (!pword.getText().toString().equals(""))) {
                    doLogin(uname.getText().toString(), pword.getText().toString());
                } else if ((!uname.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Password field empty", Toast.LENGTH_SHORT).show();
                } else if ((!pword.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Username field empty", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Username and Password field are empty", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    /**
     * 跳转登录
     * @param Lusername
     * @param Lpassword
     */
    private void doLogin(String Lusername,String Lpassword){
        Intent logining = new Intent(getApplicationContext(),AutoLogin.class);
        logining.putExtra("username",Lusername);
        logining.putExtra("password",Lpassword);
        startActivity(logining);
    }
}
