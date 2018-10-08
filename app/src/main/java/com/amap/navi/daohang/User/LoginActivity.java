package com.amap.navi.daohang.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.navi.daohang.Bianliang;
import com.amap.navi.daohang.MainActivity;
import com.amap.navi.daohang.R;
import com.amap.navi.daohang.Xiangce.AddxiangceActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import me.leefeng.promptlibrary.PromptDialog;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private EditText name1, password1;
    private SharedPreferences.Editor editor;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       /* BmobConfig config =new BmobConfig.Builder(this)
                ////设置appkey
                .setApplicationId("9074cf2ee4e2b1c77e66b8b3d2f512d5")
                ////请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                ////文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024*1024)
                ////文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);*/
        Myuser.toolbartitle="信息";
        BmobUser.logOut();   //清除缓存用户对象

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        name1 = (EditText) findViewById(R.id.et_username);

        final String account = pref.getString("account", "");
        name1.setText(account);

        //注册
        Button btn_register = (Button)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ZhuceActivity.class);
                startActivity(i);
            }
        });
        //忘记密码
        Button wangjimima = (Button)findViewById(R.id.wangjimima);
        wangjimima.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, WangjimimaActivity.class);
                startActivity(i);
            }
        });
        //登录
        Button btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //进度
                /*
                final ProgressDialog progressDialog = new ProgressDialog(loginActivity.this);
                progressDialog.setMessage("正在登录......");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
*/
                final PromptDialog promptDialog = new PromptDialog(LoginActivity.this);
                promptDialog.showLoading("正在登录");
                final EditText name, password;
                name = (EditText) findViewById(R.id.et_username);
                password = (EditText) findViewById(R.id.et_password);
                final String a = name.getText().toString();
                final String b = password.getText().toString();

                if (a.equals("") || b.equals("")) {
                    //progressDialog.dismiss();
                    promptDialog.showError("用户名或密码不能为空！");
                    //Toast.makeText(getApplicationContext(), "用户名密码不能为空！", Toast.LENGTH_SHORT).show();
                }
                else{
                    BmobUser bu2 = new BmobUser();
                    bu2.setUsername(a);
                    bu2.setPassword(b);
                    bu2.login(new SaveListener<BmobUser>() {

                        @Override
                        public void done(BmobUser bmobUser, BmobException e) {
                            if(e==null){
                                promptDialog.showSuccess("登陆成功");
                                Bianliang.setUserID(a);
                                Intent intent = new Intent(LoginActivity.this, AddxiangceActivity.class);
                                startActivity(intent);
                                //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                                //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                            }else{
                                promptDialog.showError("登录失败！");
                            }
                        }
                    });
                }
                // 给bnt1添加点击响应事件
//                else {
//                    BmobQuery<User> query1 = new BmobQuery<User>();
//                    query1.addWhereEqualTo("username", name.getText().toString());
//                    query1.findObjects(new FindListener<User>(){
//                        @Override
//                        public void done(List<User> list, BmobException e) {
//                            if(e==null)
//                            {
//                                if(list.get(0).getPassword().toString().equals(password.getText().toString()))
//                                {
//                                    Bianliang.setUser(list.get(0));
//                                    promptDialog.showSuccess("登陆成功");
//                                    Intent intent = new Intent(LoginActivity.this, AddxiangceActivity.class);
//                                    startActivity(intent);
//                                }
//                                else
//                                {
//                                    promptDialog.showError("密码不正确！");
//                                }
//                                /*BmobQuery<User> query2 = new BmobQuery<User>();
//                                query2.addWhereEqualTo("password", password.getText().toString());
//                                query2.findObjects(new FindListener<User>() {
//                                    @Override
//                                    public void done(List<User> list, BmobException e) {
//                                        if(e==null)
//                                        {
//                                            promptDialog.showSuccess("登陆成功");
//                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                            startActivity(intent);
//                                        }
//                                        else
//                                        {
//                                            promptDialog.showError("密码不正确！");
//                                        }
//                                    }
//                                });*/
//
//                            }
//                            else
//                            {
//                                promptDialog.showError("不存在该用户！");
//                            }
//                        }
//                    });
//
//                }
            }
        });
    }

}

