package os.morning.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import os.morning.app.R;
import os.morning.app.api.UserFunctionsAPI;

/**
 * 处理用户登录
 * @author ZiQin
 * @version 1.0
 * @created 2014/7/18
 */
public class AutoLogin extends Activity {

    private Button cancelButton;
    private String Lusername,Lpassword;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.auto_login);

        login();

    }

    /**
     * 用户登录
     */
    private void login() {
        //progressBar = (ProgressBar) findViewById(R.id.pgBar);
        cancelButton = (Button) findViewById(R.id.btn_back);
        sp = getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        Lusername = getIntent().getExtras().getString("username");
        Lpassword = getIntent().getExtras().getString("password");

        new Signin(Lusername,Lpassword).execute();

        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (sp.getBoolean("AUTO_LOGIN",false)){
                    sp.edit().putBoolean("AUTO_LOGIN",false);
                }

                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent);
                finish();
            }
        });
    }


    //处理用户登录异步进程
    private class Signin extends AsyncTask<String,String,JSONObject> {

        private String KEY_RESULT = "result";
        private String username,password;

        protected Signin(){
            super();
        }
        protected Signin(String username,String password){
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected JSONObject doInBackground(String... args) {
            UserFunctionsAPI userFunction = new UserFunctionsAPI();
            JSONObject json = userFunction.loginUser(username, password);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            SharedPreferences.Editor editor = sp.edit();
            try {
                if (json.getString(KEY_RESULT) != null) {

                    String res = json.getString(KEY_RESULT);
                    //用户登录成功，判断是否需要记住密码，或取消已记住的密码; 失败则取消记住密码以及自动登录状态
                    if(res.equalsIgnoreCase("success")){

                        Toast.makeText(AutoLogin.this, "登录成功", Toast.LENGTH_SHORT).show();
                        if(sp.getBoolean("REMEMBER_PASSWD",false))
                        {
                            //记住用户名、密码、
                            if(!username.equals(sp.getString("USER_NAME","")))
                            editor.putString("USER_NAME", username);
                            if(!password.equals(sp.getString("PASSWORD","")))
                            editor.putString("PASSWORD",password);
                        }else {
                            editor.putBoolean("REMEMBER_PASSWD", false);
                            editor.putString("PASSWORD","");
                        }
                        editor.commit();

                        Intent index_page = new Intent(getApplicationContext(), Main.class);
                        startActivity(index_page);
                        finish();
                    }else{
                        if(sp.getBoolean("REMEMBER_PASSWD",false)) {
                            editor.putBoolean("REMEMBER_PASSWD", false);
                        }
                        if (sp.getBoolean("AUTO_LOGIN",false)) {
                            editor.putBoolean("AUTO_LOGIN", false);
                        }
                        Toast.makeText(getApplicationContext(),
                                res, Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
