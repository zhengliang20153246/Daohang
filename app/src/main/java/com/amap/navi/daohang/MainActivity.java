package com.amap.navi.daohang;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.navi.daohang.User.LoginActivity;
import com.amap.navi.daohang.Xiangce.AddxiangceActivity;
import com.amap.navi.daohang.Xiangce.XiangceActivity;

import cn.bmob.v3.Bmob;

import static cn.bmob.newim.util.Utils.isNetworkAvailable;

public class MainActivity extends AppCompatActivity {

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Button btn9;
    private Button btn10;
    private Button btn11;
    private Button btn12;

    private long firstTime = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "4e62fdaa944fe6716dcb9593e83e29dd");

        init();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isNetworkAvailable(MainActivity.this))
                {
                    Intent intent = new Intent(MainActivity.this,DaohangActivity.class);
                    startActivity(intent);
                }
                else
                {
                    dialogshow();
                }

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isNetworkAvailable(MainActivity.this))
                {
                    Intent intent = new Intent(MainActivity.this,XiangceActivity.class);
                    startActivity(intent);
                }
                else
                {
                    dialogshow();
                }

            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(MainActivity.this))
                {
                    if(Bianliang.getUserID()==null)
                    {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(MainActivity.this, AddxiangceActivity.class);
                        startActivity(intent);

                    }
                }
                else
                {
                    dialogshow();
                }

            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(MainActivity.this))
                {
                    Intent intent = new Intent(MainActivity.this,FuwuSearchActivity.class);
                    Bianliang.setSearchpoi("餐馆");
                    startActivity(intent);
                }
                else
                {
                    dialogshow();
                }

            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(MainActivity.this))
                {
                    Intent intent = new Intent(MainActivity.this,FuwuSearchActivity.class);
                    Bianliang.setSearchpoi("银行");
                    startActivity(intent);
                }
                else
                {
                    dialogshow();
                }

            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(MainActivity.this))
                {
                    Intent intent = new Intent(MainActivity.this,FuwuSearchActivity.class);
                    Bianliang.setSearchpoi("酒店");
                    startActivity(intent);
                }
                else
                {
                    dialogshow();
                }

            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(MainActivity.this))
                {
                    Intent intent = new Intent(MainActivity.this,FuwuSearchActivity.class);
                    Bianliang.setSearchpoi("电影院");
                    startActivity(intent);
                }
                else
                {
                    dialogshow();
                }

            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(MainActivity.this))
                {
                    Intent intent = new Intent(MainActivity.this,FuwuSearchActivity.class);
                    Bianliang.setSearchpoi("医院");
                    startActivity(intent);
                }
                else
                {
                    dialogshow();
                }

            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(MainActivity.this))
                {
                    Intent intent = new Intent(MainActivity.this,FuwuSearchActivity.class);
                    Bianliang.setSearchpoi("KTV");
                    startActivity(intent);
                }
                else
                {
                    dialogshow();
                }

            }
        });
        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(MainActivity.this))
                {
                    Intent intent = new Intent(MainActivity.this,FuwuSearchActivity.class);
                    Bianliang.setSearchpoi("景点");
                    startActivity(intent);
                }
                else
                {
                    dialogshow();
                }

            }
        });
        btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(MainActivity.this))
                {
                    Intent intent = new Intent(MainActivity.this,FuwuSearchActivity.class);
                    Bianliang.setSearchpoi("超市");
                    startActivity(intent);
                }
                else
                {
                    dialogshow();
                }

            }
        });
        btn12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(MainActivity.this))
                {
                    Intent intent = new Intent(MainActivity.this,FuwuSearchActivity.class);
                    Bianliang.setSearchpoi("厕所");
                    startActivity(intent);
                }
                else
                {
                    dialogshow();
                }

            }
        });

    }

    public void init(){
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);
        btn5 = (Button)findViewById(R.id.btn5);
        btn6 = (Button)findViewById(R.id.btn6);
        btn7 = (Button)findViewById(R.id.btn7);
        btn8 = (Button)findViewById(R.id.btn8);
        btn9 = (Button)findViewById(R.id.btn9);
        btn10 = (Button)findViewById(R.id.btn10);
        btn11 = (Button)findViewById(R.id.btn11);
        btn12 = (Button)findViewById(R.id.btn12);
    }



    public void dialogshow()
    {
        AlertDialog dialog=new AlertDialog.Builder(MainActivity.this).create();
        dialog.setTitle("提示");
        dialog.setIcon(R.drawable.jinggao);
        dialog.setMessage("网络未连接，请连接网络.....");
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,"确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
        Button btnPositive = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
        btnNegative.setTextColor(Color.BLACK);
        btnNegative.setTextSize(15);
        btnPositive.setTextColor(Color.BLACK);
        btnPositive.setTextSize(15);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}