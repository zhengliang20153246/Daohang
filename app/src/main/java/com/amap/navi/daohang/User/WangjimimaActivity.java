package com.amap.navi.daohang.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.navi.daohang.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import me.leefeng.promptlibrary.PromptDialog;
public class WangjimimaActivity extends AppCompatActivity {
    private EditText username,password,phone,code;
    private String id;
    private int flag=0;
    private int yanzheng=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wangjimima);
//        username=(EditText) findViewById(R.id.wangjimima_username);
//        password=(EditText) findViewById(R.id.wangjimima_password);
//        phone=(EditText) findViewById(R.id.wangjimima_phone);
//        code=(EditText) findViewById(R.id.wangjimima_code);
        // String username1=username.getText().toString();
        //String password1=password.getText().toString();
        //String phone1=phone.getText().toString();
//        //String code1=code.getText().toString();
//        Button btn_code=(Button)findViewById(R.id.zhaomima_code);
//        btn_code.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                if (!TextUtils.isEmpty(phone.getText())) {
//                    if (phone.getText().length() == 11) {
//
//                        sendCode("86", phone.getText().toString()); // 发送验证码给号码的 phoneNumber 的手机
//                        //Toast.makeText(getApplicationContext(), "验证码发送成功！", Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        Toast.makeText(getApplicationContext(), "请输入完整的电话号码", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), "请输入电话号码", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        Button btn_next=(Button)findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText mail;
                mail=(EditText) findViewById(R.id.email);
                final String b=mail.getText().toString();
                if(b.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "输入不能为空！", Toast.LENGTH_SHORT).show();
                }
                else {
                    final String email = b;
                    BmobUser.resetPasswordByEmail(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Toast.makeText(getApplicationContext(), "重置密码请求成功，请到" + email + "邮箱进行密码重置操作", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(WangjimimaActivity.this, LoginActivity.class);
                                //启动
                                startActivity(intent);
                                finish();

                            }else{
                                Toast.makeText(getApplicationContext(), "重置密码失败！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }
//                if(flag==1) {
//                    if (username.getText().toString().equals("") || password.getText().toString().equals("") || phone.getText().toString().equals("")) {
//                        Toast.makeText(getApplicationContext(), "信息不能为空！", Toast.LENGTH_SHORT).show();
//                    } else if (code.getText().toString().equals("")) {
//                        Toast.makeText(getApplicationContext(), "请输入验证码！", Toast.LENGTH_SHORT).show();
//                    } else if (password.getText().toString().length() < 6) {
//                        Toast.makeText(getApplicationContext(), "密码不能小于6位！", Toast.LENGTH_SHORT).show();
//                    }
//                    // 给bnt1添加点击响应事件
//                    else {
//
//                        submitCode("86", phone.getText().toString(), code.getText().toString());
//                        //启动
//                        if(yanzheng==1) {
//                            BmobQuery<User> query = new BmobQuery<User>();
//                            query.addWhereEqualTo("phone", phone.getText().toString());
//                            query.findObjects(new FindListener<User>() {
//                                @Override
//                                public void done(List<User> list, BmobException e) {
//                                    if (e == null) {
//                                        id = list.get(0).getObjectId().toString();
//                                        User myuser = new User();
//                                        myuser.setUsername(username.getText().toString());
//                                        myuser.setPassword(password.getText().toString());
//                                        myuser.update(id, new UpdateListener() {
//                                            @Override
//                                            public void done(BmobException e) {
//                                                if (e == null) {
//                                                    Toast.makeText(getApplicationContext(), "操作成功！", Toast.LENGTH_SHORT).show();
//                                                    Intent intent = new Intent(WangjimimaActivity.this, LoginActivity.class);
//                                                    startActivity(intent);
//                                                } else {
//                                                    Toast.makeText(getApplicationContext(), "操作失败！" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                    finish();
//                                                    startActivity(getIntent());
//                                                }
//                                            }
//                                        });
//                                    } else {
//                                        Toast.makeText(getApplicationContext(), "该手机号未注册！", Toast.LENGTH_SHORT).show();
//                                        finish();
//                                        startActivity(getIntent());
//                                    }
//                                }
//                            });
//                        }
//                        else
//                        {
//                            Toast.makeText(getApplicationContext(), "验证码错误！", Toast.LENGTH_SHORT).show();
//                        }
//
//
//                    }
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(), "请获取手机验证码！", Toast.LENGTH_SHORT).show();
//                }
            }
        });


    }
    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public void sendCode(String country, String phone) {
        flag=1;
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    Toast.makeText(WangjimimaActivity.this, "验证码发送成功！", Toast.LENGTH_SHORT).show();
                } else{
                    // TODO 处理错误的结果
                    Toast.makeText(WangjimimaActivity.this, "验证码发送失败！", Toast.LENGTH_SHORT).show();
                }

            }
        });
        // 触发操作
        SMSSDK.getVerificationCode("86", phone);
    }

    // 提交验证码，其中的code表示验证码，如“1357”
    public void submitCode(String country, String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证成功的结果
                    yanzheng=1;
                    Toast.makeText(WangjimimaActivity.this, "验证成功！", Toast.LENGTH_SHORT).show();
                } else{
                    // TODO 处理错误的结果
                    yanzheng=0;
                    Toast.makeText(WangjimimaActivity.this, "验证失败！", Toast.LENGTH_SHORT).show();
                }

            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    };


    /*public static File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 1024) {  //循环判断如果压缩后图片是否大于1024kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String filename = format.format(date);
        File file = new File(Environment.getExternalStorageDirectory(),filename+".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //recycleBitmap(bitmap);
        return file;
    }*/
}

