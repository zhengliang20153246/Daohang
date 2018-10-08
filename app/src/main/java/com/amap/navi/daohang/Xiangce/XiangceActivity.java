package com.amap.navi.daohang.Xiangce;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.navi.daohang.Camera.Jisuan;
import com.amap.navi.daohang.Camera.MySurfaceView;
import com.amap.navi.daohang.R;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import me.leefeng.promptlibrary.OnAdClickListener;
import me.leefeng.promptlibrary.PromptDialog;


public class XiangceActivity extends Activity implements SensorEventListener {
    private ProgressDialog progressDialog;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    public AMapLocation myLocation;
    public String sql;
    public List<Xiangce> xiangceList=new ArrayList<Xiangce>();
    private ImageView imageView1;
    private ImageView imageView2;
    private FrameLayout showtupian;
    private FrameLayout xiangce_camera;
    private RelativeLayout xinagce_rel;
    private SensorManager sensorManager = null;
    private Sensor gyroSensor = null;
    private Camera camera = null;
    private MySurfaceView mySurfaceView = null;
    double width;
    double height;
    double flag;
    int lie[] = new int[38];
    RelativeLayout.LayoutParams params;
    private Jisuan jisuan=new Jisuan();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiangce);

        StrictMode.setThreadPolicy(new
                StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(
                new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        progressDialog = new ProgressDialog(XiangceActivity.this);
        progressDialog.setMessage("正在加载.....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        showtupian=(FrameLayout) findViewById(R.id.showtupian);
        xiangce_camera=(FrameLayout) findViewById(R.id.xiangce_camera);
        xinagce_rel=(RelativeLayout) findViewById(R.id.xinagce_rel);
        params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ORIENTATION);

        if(cameraIsCanUse()==true) {


            for (int i = 0; i < 38; i++) {
                lie[i] = 0;
            }
            WindowManager manager = getWindowManager();
            DisplayMetrics metrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(metrics);
            width = metrics.widthPixels;  //以要素为单位
            height = metrics.heightPixels;//以要素为单位
            flag= width / 30;
        }
        else
        {
            Toast.makeText(this,"请打开相机权限的访问！",Toast.LENGTH_SHORT).show();
        }

        mLocationClient = new AMapLocationClient(getApplicationContext());
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        mLocationOption.setOnceLocation(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
        AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        myLocation=amapLocation;
                        sql = "select * from Xiangce where address='"+myLocation.getAoiName()+"'";//我把你的测试的username=1去了这是登录的username
                        zhao();
                        progressDialog.dismiss();

                    }else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Toast.makeText(XiangceActivity.this,"定位失败",Toast.LENGTH_SHORT).show();
                    }
                }

            }

        };
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);

    }

    public  void zhao()
    {
        new BmobQuery<Xiangce>().doSQLQuery(sql,new SQLQueryListener<Xiangce>(){

            @Override
            public void done(BmobQueryResult<Xiangce> result, BmobException e) {
                if(e ==null)
                {
                    System.out.println("SQL"+sql);
                    List<Xiangce> list = (List<Xiangce>) result.getResults();
                    xiangceList=list;
                    if(list!=null && list.size()>0)
                    {
                        for (Xiangce xiangce : xiangceList) {
                            xiangce.setLujing(xiangce.getTupian().getFileUrl());
                            int jiaodu = (int) Math.ceil(jisuan.getAngle1(myLocation.getLatitude(), myLocation.getLongitude(),Double.parseDouble (xiangce.getLat().toString()),Double.parseDouble ( xiangce.getLng().toString())));
                            System.out.println(jiaodu);
                            lie[jiaodu / 10]++;
                            xinagce_rel.addView(createImage(jiaodu, 0, lie[jiaodu / 10],xiangce), params);
                            if ((jiaodu >= 0 && jiaodu < 10)) {
                                lie[36]++;
                                xinagce_rel.addView(createImage(jiaodu, 1, lie[36],xiangce), params);
                            }
                            if ((jiaodu >= 350 && jiaodu < 360)) {
                                lie[37]++;
                                xinagce_rel.addView(createImage(jiaodu, 1, lie[37],xiangce), params);
                            }

                        }

                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "附件没有人发表动态哦！", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.print("日志"+e.getMessage());
                }
            }
        });
    }

    public Bitmap getImageBitmap(String url) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected View createImage(final int r, int f, final int num, final Xiangce xiangcepoi) {
        final ImageView img = new ImageView(this);
//        btn.setFocusable(true);
//        btn.setFocusableInTouchMode(true);
//        btn.requestFocus();
//        btn.requestFocusFromTouch();
//        btn.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//        btn.setMarqueeRepeatLimit(-1);
        final Random rand = new Random();
        final Random rand1 = new Random();
        int rotatoion = rand.nextInt(45);
        int rr = rand.nextInt(2);
        if(rr==1)
            rotatoion=0-rotatoion;

        final Matrix matrix = new Matrix();
        //matrix.postScale(0.5f,1f,img.getWidth() / 2,img.getHeight() / 2);
        matrix.setScale(1, -1);
        matrix.postTranslate(0, img.getHeight());

        img.setImageBitmap(getImageBitmap(xiangcepoi.getLujing()));
//        img.setMaxHeight((int) (width / 5));
//        img.setMaxWidth((int) (height / 5));

        img.setRotation(rotatoion);
//        img.setImageMatrix(matrix);
        final int rflag = r / 10;

        if (f == 0) {
            //lie[rflag]++;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //params = new RelativeLayout.LayoutParams(new ViewGroup.LayoutParams((int)(7*flag), 160));
                        //设置图片的位置
                        ViewGroup.MarginLayoutParams margin9 = new ViewGroup.MarginLayoutParams(
                                img.getLayoutParams());
                        margin9.setMargins((int) (width / 2 + rflag * 10 * flag), (int) height / 13 + num * 600, 0, 0);
                        RelativeLayout.LayoutParams layoutParams9 = new RelativeLayout.LayoutParams(margin9);
                        layoutParams9.height = 600;//设置图片的高度
                        layoutParams9.width = 800; //设置图片的宽度
                        img.setLayoutParams(layoutParams9);
//                        params = new RelativeLayout.LayoutParams(new ViewGroup.LayoutParams((int)(7*flag), 160));
//                        params.height=200;
//                        params.width=100;
//                        //left, top ,right, bottom
//                        params.setMargins((int) (width / 2 + rflag * 10 * flag), (int) height / 13 + rand.nextInt(7) * 195, 0, 0);
//                        img.setLayoutParams(params);
                        //img.setMaxHeight((int) (width / 5));
                        //img.setMaxWidth((int) (height / 5));
                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final PromptDialog promptDialog = new PromptDialog(XiangceActivity.this);
                                promptDialog.getDefaultBuilder().backAlpha(150);
                                Glide.with(XiangceActivity.this).load(xiangcepoi.getLujing())
                                        .into(promptDialog.showAd(true, new OnAdClickListener() {
                                            @Override
                                            public void onAdClick() {
                                                Toast.makeText(getApplicationContext(), xiangcepoi.getIntruduce(), Toast.LENGTH_SHORT).show();                                            }
                                        }));

                            }
                        });
                        img.getBackground().setAlpha(0);//0~255透明度值
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
//
        }

        if (f == 1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (rflag == 0) {
                            //lie[36]++;
                            //设置图片的位置
                            ViewGroup.MarginLayoutParams margin9 = new ViewGroup.MarginLayoutParams(
                                    img.getLayoutParams());
                            margin9.setMargins((int) (width / 2 + rflag * 10 * flag), (int) height / 13 + rand.nextInt(7) * 195, 0, 0);
                            RelativeLayout.LayoutParams layoutParams9 = new RelativeLayout.LayoutParams(margin9);
                            layoutParams9.height = 600;//设置图片的高度
                            layoutParams9.width = 800; //设置图片的宽度
                            img.setLayoutParams(layoutParams9);
                            //params = new RelativeLayout.LayoutParams(new ViewGroup.LayoutParams((int)(7*flag), 160));
//                            params = new RelativeLayout.LayoutParams(new ViewGroup.LayoutParams((int)(7*flag), 160));
//                            params.height=200;
//                            params.width=100;
//                            params.setMargins((int) (width / 2 + 36 * 10 * flag), (int) height / 16 + rand.nextInt(7) * 195, 0, 0);
//                            img.setLayoutParams(params);
                           // img.setMaxHeight((int) (width / 5));
                           // img.setMaxWidth((int) (height / 5));
                            img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getApplicationContext(), xiangcepoi.getIntruduce(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            img.getBackground().setAlpha(0);//0~255透明度值
                        } else {
                            //lie[37]++;
                            //设置图片的位置
                            ViewGroup.MarginLayoutParams margin9 = new ViewGroup.MarginLayoutParams(
                                    img.getLayoutParams());
                            margin9.setMargins((int) (width / 2 + rflag * 10 * flag), (int) height / 13 + rand.nextInt(7) * 195, 0, 0);
                            RelativeLayout.LayoutParams layoutParams9 = new RelativeLayout.LayoutParams(margin9);
                            layoutParams9.height = 600;//设置图片的高度
                            layoutParams9.width = 800; //设置图片的宽度
                            img.setLayoutParams(layoutParams9);
                            //params = new RelativeLayout.LayoutParams(new ViewGroup.LayoutParams((int)(7*flag), 160));
//                            params = new RelativeLayout.LayoutParams(new ViewGroup.LayoutParams((int)(7*flag), 160));
//                            params.height=200;
//                            params.width=100;
//                            //left, top ,right, bottom
//                            params.setMargins((int) (width / 2 - 10 * flag), (int) height / 16 + rand.nextInt(7) * 195, 0, 0);
//                            img.setLayoutParams(params);
                           // img.setMaxHeight((int) (width / 5));
                            //img.setMaxWidth((int) (height / 5));
                            img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getApplicationContext(), xiangcepoi.getIntruduce(), Toast.LENGTH_SHORT).show();                                }
                            });
                            img.getBackground().setAlpha(0);//0~255透明度值
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        return img;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        switch (sensorType) {
            case Sensor.TYPE_ORIENTATION:
                // values[0]  ：直接通过方向感应器数据范围是（0～359）360/0表示正北，90表示正东，180表示正南，270表示正西。
                xinagce_rel.scrollTo((int) (sensorEvent.values[0] * flag), (int)(-(-85-sensorEvent.values[1])*(flag/1.5)));
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
        if(cameraIsCanUse()==true) {
            if (camera == null) {
                camera = getCameraInstance();
            }
            //必须放在onResume中，不然会出现Home键之后，再回到该APP，黑屏
            mySurfaceView = new MySurfaceView(getApplicationContext(), camera);
            //RelativeLayout preview1 = (RelativeLayout) findViewById(R.id.activity_main);
            xiangce_camera.addView(mySurfaceView);
            //mySurfaceView.setVisibility(View.GONE);

        }
        else{
            Toast.makeText(this,"请打开相机权限的访问！",Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this); // 解除监听器注册
        if(camera!=null)
        {
            camera.release();
        }
        camera = null;

    }
    //相机********************
    /*得到一相机对象*/
    private Camera getCameraInstance(){
        Camera camera = null;
        final int orientation = this.getResources().getConfiguration().orientation;
        try{
            if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
                camera = camera.open();
                camera.setDisplayOrientation(180);
            }else if(orientation == Configuration.ORIENTATION_PORTRAIT) {
                camera = camera.open();
                camera.setDisplayOrientation(90);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return camera;
    }
    public boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

}
