package com.wangz.prs_client;

import android.app.Activity;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    private EditText mAccount;                        //用户名编辑
    private EditText mStdId;                        //学号编辑
    private EditText mPhone;                        //电话编辑
    private EditText mEmail;                        //邮箱编辑
    private EditText mPwd;                            //密码编辑
    private EditText mPwdCheck;                       //密码编辑
    private Button mSureButton;                       //确定按钮
    private Button mCancelButton;                     //取消按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mAccount =  findViewById(R.id.register_edit_name);
        mPwd =  findViewById(R.id.register_edit_pwd_old);
        mPwdCheck = findViewById(R.id.register_edit_pwd_new);

        mStdId = findViewById(R.id.register_edit_stdID);
        mEmail = findViewById(R.id.register_edit_email);
        mPhone = findViewById(R.id.register_edit_phone);
        mSureButton = (Button) findViewById(R.id.register_btn_sure);
        mCancelButton = (Button) findViewById(R.id.register_btn_cancel);

        mSureButton.setOnClickListener(m_register_Listener);      //注册界面两个按钮的监听事件
        mCancelButton.setOnClickListener(m_register_Listener);


    }
    View.OnClickListener m_register_Listener = new View.OnClickListener() {    //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register_btn_sure:                       //确认按钮的监听事件
                    try {
                        register_check();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.register_btn_cancel:                     //取消按钮的监听事件,由注册界面返回登录界面
                    Intent intent_Register_to_Login = new Intent(Register.this,Login.class) ;    //切换User Activity至Login Activity
                    startActivity(intent_Register_to_Login);
                    finish();
                    break;
            }
        }
    };
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public void register_check() throws JSONException, IOException {                                //确认按钮的监听事件
        if (isUserNameAndPwdValid()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String userName = mAccount.getText().toString().trim();
                    String userStdId = mStdId.getText().toString().trim();
                    String userPhone = mPhone.getText().toString().trim();
                    String userEmail = mEmail.getText().toString().trim();
                    String userPwd = mPwd.getText().toString().trim();
                    String userPwdCheck = mPwdCheck.getText().toString().trim();
                    if(!userPwd.equals(userPwdCheck)){     //两次密码输入不一样
                        Looper.prepare();
                        Toast.makeText(getBaseContext(), getString(R.string.pwd_not_the_same),Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else {
                        try{
                            OkHttpClient client =new OkHttpClient();
                            JSONObject obj = new JSONObject();
                            obj.put("name",userName);
                            obj.put("stdId",userStdId);
                            obj.put("phone",userPhone);
                            obj.put("email",userEmail);
                            obj.put("password",userPwd);
                            String jsonobj=obj.toString();
                            RequestBody body =  RequestBody.create(JSON ,jsonobj);
                            Request request =new Request.Builder()
                                    .url("http://47.100.99.193/api/web/user/register")
                                    .post(body)
                                    .build();
                            Response response = client.newCall(request).execute();
                            String responseData = response.body().string();
                            //解析jason
                            JSONObject jsonObject=new JSONObject(responseData);
                            String status=jsonObject.getString("status");
                            String payloaduser = jsonObject.getString("payload");
                            JSONObject payloadjwtuser= new JSONObject(payloaduser);
                            String jwt =payloadjwtuser.getString("jwt");            //获取jwt
                            if (!status.equals("200")) {
                                Looper.prepare();
                                Toast.makeText(getBaseContext(), getString(R.string.register_fail),Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }else {
                                Intent intent_Register_to_Login = new Intent(Register.this, Login.class);    //切换Register Activity至Login Activity
                                startActivity(intent_Register_to_Login);
                                finish();
                                Looper.prepare();
                                Toast.makeText(getBaseContext(), getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                                Looper.loop();
                                }
                        }catch (Exception e){
                            Looper.prepare();
                            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                }
            }).start();
        }
    }
    public boolean isUserNameAndPwdValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if(mPwdCheck.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_check_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
