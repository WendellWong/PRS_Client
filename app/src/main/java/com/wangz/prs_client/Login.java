package com.wangz.prs_client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Login extends Activity {                 //登录界面活动

    public int pwdresetFlag=0;
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private Button mRegisterButton;                   //注册按钮
    private Button mLoginButton;                      //登录按钮

    private CheckBox mRememberCheck;

    private SharedPreferences login_sp;
    private String userNameValue,passwordValue;

    private View loginView;                           //登录
    private View loginSuccessView;
    private TextView loginSuccessShow;
    private TextView mChangepwdText;
    public static userData userData =new userData(); //全局userData 方便读取jwt


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        //通过id找到相应的控件
        mAccount = (EditText) findViewById(R.id.login_edit_account);
        mPwd = (EditText) findViewById(R.id.login_edit_pwd);
        mRegisterButton = (Button) findViewById(R.id.login_btn_register);
        mLoginButton = (Button) findViewById(R.id.login_btn_login);
        loginView=findViewById(R.id.login_view);
        loginSuccessView=findViewById(R.id.login_success_view);
        loginSuccessShow=(TextView) findViewById(R.id.login_success_show);

//        mChangepwdText = (TextView) findViewById(R.id.login_text_change_pwd);

        mRememberCheck = (CheckBox) findViewById(R.id.Login_Remember);

        login_sp = getSharedPreferences("userInfo", 0);
        String name=login_sp.getString("USER_NAME", "");
        String pwd =login_sp.getString("PASSWORD", "");
        boolean choseRemember =login_sp.getBoolean("mRememberCheck", false);
        boolean choseAutoLogin =login_sp.getBoolean("mAutologinCheck", false);
        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if(choseRemember){
            mAccount.setText(name);
            mPwd.setText(pwd);
            mRememberCheck.setChecked(true);
        }

        mRegisterButton.setOnClickListener(mListener);                      //采用OnClickListener方法设置不同按钮按下之后的监听事件
        mLoginButton.setOnClickListener(mListener);
        //mChangepwdText.setOnClickListener(mListener);

        ImageView image = (ImageView) findViewById(R.id.logo);             //使用ImageView显示logo
        image.setImageResource(R.drawable.logo);


    }
    OnClickListener mListener = new OnClickListener() {                  //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_btn_register:                            //登录界面的注册按钮
                    Intent intent_Login_to_Register = new Intent(Login.this,Register.class) ;    //切换Login Activity至User Activity
                    startActivity(intent_Login_to_Register);
                    finish();
                    break;
                case R.id.login_btn_login:                              //登录界面的登录按钮
                    try {
                        login();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public void login() throws IOException, JSONException {                                              //登录按钮监听事件
        if (isUserNameAndPwdValid()) {
            //向服务器发送请求
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String userName = mAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
                    String userPwd = mPwd.getText().toString().trim();
                    SharedPreferences.Editor editor =login_sp.edit();
                    try {
                        OkHttpClient client = new OkHttpClient();
                        JSONObject obj = new JSONObject();
                        obj.put("stdId", userName);
                        obj.put("password", userPwd);
                        String jsonobj = obj.toString();
                        RequestBody body = RequestBody.create(JSON, jsonobj);
                        Request request = new Request.Builder()
                                .url("http://47.100.99.193:80/php/user/Login.php")
                                .post(body)
                                .build();
                        Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
//            parseJsonWithJsonObject(responseData);
                        JSONObject jsonObject = new JSONObject(responseData);
                        String result = jsonObject.getString("result");
//                        userData userData =new userData();
                        userData.setJwt(jsonObject.getString("jwt")) ;                            //获取jwt
       //获取userData
                        String userString =jsonObject.getString("user");
                        JSONObject jsonuser = new JSONObject(userString);
                        userData.setId(jsonuser.getInt("id"));
                        userData.setStdId(jsonuser.getInt("stdId"));
                        userData.setName(jsonuser.getString("name"));
                        userData.setEmail(jsonuser.getString("email"));
                        userData.setPhone(jsonuser.getInt("phone"));
                        userData.setRole(jsonuser.getInt("role"));
                        if (result.equals("success")) {                                             //返回1说明用户名和密码均正确
                            //保存用户名和密码
                            editor.putString("USER_NAME", userName);
                            editor.putString("PASSWORD", userPwd);

                            //是否记住密码
                            if (mRememberCheck.isChecked()) {
                                editor.putBoolean("mRememberCheck", true);
                            } else {
                                editor.putBoolean("mRememberCheck", false);
                            }
                            editor.commit();

                            Intent intent = new Intent(Login.this, APP.class);    //切换Login Activity至User Activity
                            startActivity(intent);
                            finish();
                            Looper.prepare();
                            Toast.makeText(getBaseContext(), getString(R.string.login_success), Toast.LENGTH_SHORT).show();//登录成功提示
                            Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast.makeText(getBaseContext(), getString(R.string.login_fail), Toast.LENGTH_SHORT).show();  //登录失败提示
                            Looper.loop();
                        }
                    } catch (Exception e)
                    {
                        Looper.prepare();
                        Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }
            }).start();

        }
    }
//    private void parseJsonWithJsonObject(String jsonDATA){
//        try{
//            JSONArray jsonArray=new JSONArray(jsonDATA);
//            for(int i=0;i<jsonArray.length();i++){
//                JSONObject jsonObject=jsonArray.getJSONObject(i);
//                String result=jsonObject.getString("result");
//                userData userData =jsonObject.getString("stdID");
//                String jwt =jsonObject.getString("jwt");
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    public boolean isUserNameAndPwdValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
