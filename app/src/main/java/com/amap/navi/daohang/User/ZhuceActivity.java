package com.amap.navi.daohang.User;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.navi.daohang.R;


import cn.bmob.v3.BmobUser;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import me.leefeng.promptlibrary.PromptDialog;


public class ZhuceActivity extends AppCompatActivity {
    private EditText username,password,email,code;
    public int flag=0;
    public int yanzheng=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuce);
        username=(EditText) findViewById(R.id.et_usernamezhuce);
        password=(EditText) findViewById(R.id.et_passwordzhuce);
        email=(EditText) findViewById(R.id.et_phone);
//        code=(EditText) findViewById(R.id.et_code);
        // String username1=username.getText().toString();
        //String password1=password.getText().toString();
        //String phone1=phone.getText().toString();
        //String code1=code.getText().toString();

        Myuser.toolbartitle="信息";
//        Button btn_code=(Button)findViewById(R.id.btn_code);
//        btn_code.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                if (!TextUtils.isEmpty(phone.getText())) {
//                    if (phone.getText().length() == 11) {
//
//                        sendCode("86", phone.getText().toString()); // 发送验证码给号码的 phoneNumber 的手机
//                        //Toast.makeText(getApplicationContext(), "验证码发送成功！", Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        Toast.makeText(ZhuceActivity.this, "请输入完整的电话号码", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(ZhuceActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        Button btn_register=(Button)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String username1=username.getText().toString();
                final String password1=password.getText().toString();
                final String email1=email.getText().toString();
                if(username1.equals("")||password1.equals("")||email1.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "信息不能为空！", Toast.LENGTH_SHORT).show();
                }
                else if(password1.length()<6)
                {
                    Toast.makeText(getApplicationContext(), "密码不能小于6位！", Toast.LENGTH_SHORT).show();
                }
                // 给bnt1添加点击响应事件
                else {
                    final PromptDialog promptDialog = new PromptDialog(ZhuceActivity.this);
                    promptDialog.showLoading("正在注册");

                    //启动
                    BmobUser bu = new BmobUser();
                    bu.setUsername(username1);
                    bu.setPassword(password1);
                    bu.setEmail(email1);

                    //注意：不能用save方法进行注册
                    bu.signUp(new SaveListener<Myuser>() {
                        @Override
                        public void done(Myuser s, BmobException e) {
                            if(e==null){
                                //Toast.makeText(getApplicationContext(), "注册成功！", Toast.LENGTH_SHORT).show();

                                BmobUser.requestEmailVerify(email1, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            //Intent intent = new Intent(registerActivity.this, loginActivity.class);
                                            //startActivity(intent);
                                            User userinfo = new User();

                                            userinfo.setUsername(username1);
                                            userinfo.setPassword(password1);
                                            userinfo.setEmail(email1);
                                            userinfo.save(new SaveListener<String>() {

                                                @Override
                                                public void done(String objectId, BmobException e) {
                                                    if(e==null){
                                                        //Toast.makeText(getApplicationContext(), "请求验证邮件成功，请到" + email1 + "邮箱中进行激活。", Toast.LENGTH_SHORT).show();
                                                        promptDialog.dismiss();
                                                        Toast.makeText(getApplicationContext(), "注册成功！请到" + email1 + "邮箱中进行激活。", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(ZhuceActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                        //  toast("创建数据成功：" + objectId);
                                                    }else{
                                                        promptDialog.dismiss();
                                                        Toast.makeText(getApplicationContext(), "注册失败！"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        }else{
                                            promptDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "请求验证邮件失败！"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                //toast("注册成功:" +s.toString());
                            }else{
                                Toast.makeText(getApplicationContext(), "此用户名或邮箱已注册！", Toast.LENGTH_SHORT).show();
                                //loge(e);
                            }
                        }
                    });

                }
//                if (flag == 1) {
//                    if (username.getText().toString().equals("") || password.getText().toString().equals("") || phone.getText().toString().equals("")) {
//                        Toast.makeText(getApplicationContext(), "信息不能为空！", Toast.LENGTH_SHORT).show();
//                    } else if (code.getText().toString().equals("")) {
//                        Toast.makeText(getApplicationContext(), "验证码不能为空！", Toast.LENGTH_SHORT).show();
//                    } else if (password.getText().toString().length() < 6) {
//                        Toast.makeText(getApplicationContext(), "密码不能小于6位！", Toast.LENGTH_SHORT).show();
//                    }
//                    // 给bnt1添加点击响应事件
//                    else {
//
//                        submitCode("86", phone.getText().toString(), code.getText().toString());
//                        //启动
//                        if(yanzheng==1) {
//                            BmobUser bu = new BmobUser();
//                            bu.setUsername(username.getText().toString());
//                            bu.setPassword(password.getText().toString());
//                            bu.setMobilePhoneNumber(phone.getText().toString());
//                            //注意：不能用save方法进行注册
//                            bu.signUp(new SaveListener<Myuser>() {
//                                @Override
//                                public void done(Myuser s, BmobException e) {
//                                    if (e == null) {
//                                        User user = new User();
//                                        user.setUsername(username.getText().toString());
//                                        user.setPassword(password.getText().toString());
//                                        user.setPhone(phone.getText().toString());
//                                        user.save(new SaveListener<String>() {
//                                            @Override
//                                            public void done(String objectId, BmobException e) {
//                                                if (e == null) {
//                                                    Toast.makeText(getApplicationContext(), "注册成功！", Toast.LENGTH_SHORT).show();
//                                                    Intent intent = new Intent(ZhuceActivity.this, LoginActivity.class);
//                                                    startActivity(intent);
//                                                } else {
//                                                    Toast.makeText(getApplicationContext(), "注册失败！" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                    finish();
//                                                    startActivity(getIntent());
//                                                }
//                                            }
//                                        });
//                                    } else {
//                                        Toast.makeText(getApplicationContext(), "此用户名或手机号已注册！", Toast.LENGTH_SHORT).show();
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
//                    }
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(), "请获取验证码！", Toast.LENGTH_SHORT).show();
//                }
            }

        });


    }
    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public void sendCode(String country, String phone) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        flag=1;
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达

                    //Toast.makeText(ZhuceActivity.this, "验证码发送成功！", Toast.LENGTH_SHORT).show();
                } else{
                    // TODO 处理错误的结果
                    //Toast.makeText(ZhuceActivity.this, "验证码发送失败！", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ZhuceActivity.this, "验证成功！", Toast.LENGTH_SHORT).show();
                } else{
                    // TODO 处理错误的结果
                    yanzheng=0;
                    Toast.makeText(ZhuceActivity.this, "验证失败！", Toast.LENGTH_SHORT).show();
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
