package com.amap.navi.daohang;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.DisplayMetrics;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.navi.daohang.Basic.WalkRouteCalculateActivity;
import com.amap.navi.daohang.Camera.Jisuan;
import com.amap.navi.daohang.Camera.MySurfaceView;

import java.util.List;



import static android.R.id.button1;


public class FuwuSearchActivity extends AppCompatActivity implements PoiSearch.OnPoiSearchListener,AMapLocationListener,GeocodeSearch.OnGeocodeSearchListener,SensorEventListener
{
    private FrameLayout getShowbianqian1;
    private MapView mMapView = null;
    private AMap aMap;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
    private AMapLocation mylocaltion;
    private List<PoiItem> poiItems;
    RelativeLayout.LayoutParams params;

    private SensorManager sensorManager = null;
    private Sensor gyroSensor = null;
    private int flag1 = 0;
    private FrameLayout showbianqian;
    private PoiResult pois;


    int lie[] = new int[38];

    RelativeLayout rel;
    private Camera camera = null;
    private MySurfaceView mySurfaceView = null;

    private byte[] buffer = null;

    private final int TYPE_FILE_IMAGE = 1;
    private final int TYPE_FILE_VEDIO = 2;
    double width;
    double height;
    double flag;
    ImageView znzImage;
    int index = 0;
    private Jisuan jisuan=new Jisuan();
    private ProgressDialog progressDialog;
    FrameLayout preview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuwu_search);
        mMapView = (MapView) findViewById(R.id.fuwumap);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        aMap=mMapView.getMap();
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        CameraUpdate cameraUpdate=new CameraUpdateFactory().zoomTo(17);
        aMap.moveCamera(cameraUpdate);
        //启动定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(this);
        mLocationClient.startLocation();
        params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ORIENTATION);
        showbianqian=(FrameLayout) findViewById(R.id.showbiaoqian);
        //getShowbianqian1=(FrameLayout) findViewById(R.id.showbiaoqian1);
        rel = (RelativeLayout) findViewById(R.id.rel);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.CAMERA}, 1);
        }
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


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
        //sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL); //为传感器注册监听器
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
        if(cameraIsCanUse()==true) {
            if (camera == null) {
                camera = getCameraInstance();
            }
            //必须放在onResume中，不然会出现Home键之后，再回到该APP，黑屏
            mySurfaceView = new MySurfaceView(getApplicationContext(), camera);
            preview = (FrameLayout) findViewById(R.id.id_textureView_camera);
            //RelativeLayout preview1 = (RelativeLayout) findViewById(R.id.activity_main);
             preview.addView(mySurfaceView);
            //mySurfaceView.setVisibility(View.GONE);

        }
        else{
            Toast.makeText(this,"请打开相机权限的访问！",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
        sensorManager.unregisterListener(this); // 解除监听器注册
        if(camera!=null)
        {
            camera.release();
        }
        camera = null;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        pois=poiResult;
        for (PoiItem item:poiResult.getPois())
        {
            LatLng latLng = new LatLng(item.getLatLonPoint().getLatitude(),item.getLatLonPoint().getLongitude());
            int jiaodu = (int) Math.ceil(jisuan.getAngle1(mylocaltion.getLatitude(), mylocaltion.getLongitude(), item.getLatLonPoint().getLatitude(), item.getLatLonPoint().getLongitude()));
            System.out.println(jiaodu);
            lie[jiaodu / 10]++;
            rel.addView(createButton(jiaodu, 0, lie[jiaodu / 10],item), params);
            if ((jiaodu >= 0 && jiaodu < 10)) {
                lie[36]++;
                rel.addView(createButton(jiaodu, 1, lie[36],item), params);
            }
            if ((jiaodu >= 350 && jiaodu < 360)) {
                lie[37]++;
                rel.addView(createButton(jiaodu, 1, lie[37],item), params);
            }
            aMap.addMarker(new MarkerOptions().position(latLng).title(item.getTitle()));
        }

        }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {


    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        mylocaltion=aMapLocation;
        if(aMapLocation!=null) {
            MyLocationStyle myLocationStyle;
            myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;
            aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
            aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

            Circle circle = aMap.addCircle(new CircleOptions().
                    center(new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude())).
                    fillColor(Color.argb(button1, 1, 1, 1)).
                    radius(1500).
                    strokeWidth(15));
            CameraUpdate cameraUpdate=new CameraUpdateFactory().zoomTo(15);
            aMap.moveCamera(cameraUpdate);
            PoiSearch.Query query = new PoiSearch.Query(Bianliang.getSearchpoi(), "",aMapLocation.getCity());
            PoiSearch poiSearch = new PoiSearch(this, query);
            poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(aMapLocation.getLatitude(),
                    aMapLocation.getLongitude()), 2000));//设置周边搜索的中心点以及半径
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn();


        }
        else
        {
            Toast.makeText(this,"定位失败",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        for (PoiItem item:regeocodeResult.getRegeocodeAddress().getPois())
        {
            LatLng latLng = new LatLng(item.getLatLonPoint().getLatitude(),item.getLatLonPoint().getLongitude());
            aMap.addMarker(new MarkerOptions().position(latLng).title(item.getTitle()));
        }
        poiItems=regeocodeResult.getRegeocodeAddress().getPois();


    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        switch (sensorType) {
            case Sensor.TYPE_ORIENTATION:
                // values[0]  ：直接通过方向感应器数据范围是（0～359）360/0表示正北，90表示正东，180表示正南，270表示正西。
                rel.scrollTo((int) (sensorEvent.values[0] * flag), (int)(-(-85-sensorEvent.values[1])*(flag/1.5)));
                break;
        }

        if (sensorEvent.values[1] < 8 && sensorEvent.values[1] > -40) {
            if (flag1 == 1) {

                mySurfaceView.setVisibility(View.GONE);
               // getShowbianqian1.setVisibility(View.GONE);
                showbianqian.setVisibility(View.GONE);
                mMapView.setVisibility(View.VISIBLE);
                flag1=0;

            }
        }
        else
        {
            if (flag1 == 0) {
                mMapView.setVisibility(View.GONE);
                mySurfaceView.setVisibility(View.VISIBLE);
                //getShowbianqian1.setVisibility(View.VISIBLE);
                showbianqian.setVisibility(View.VISIBLE);

                flag1=1;

            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    protected View createButton(final int r, int f, final int num, final PoiItem poiItem) {
        final Button btn = new Button(this);
        btn.setSingleLine(true);
//        btn.setWidth((int)(6*flag));
//        btn.setHeight(160);
//        btn.setTextSize(10);

        final int rflag = r / 10;

        Drawable img_off,button_bg;

        Resources res1 = getResources();
        button_bg = res1.getDrawable(R.drawable.button_bg);
        btn.setBackground(button_bg);
        String text=poiItem.getTitle()+"\n"+poiItem.getDistance()+"米";

        btn.setText(text);

        if (f == 0) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        params = new RelativeLayout.LayoutParams(new ViewGroup.LayoutParams((int)(7*flag), 160));
                        //left, top ,right, bottom
                        params.setMargins((int) (width / 2 + rflag * 10 * flag), (int) height / 13 + num * 195, 0, 0);
                        btn.setLayoutParams(params);
                        btn.setWidth((int)(6*flag));
                        btn.setHeight(160);
                        btn.setTextSize(10);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(FuwuSearchActivity.this);
                                builder.setIcon(R.drawable.daohang);
                                builder.setTitle("提示：");
                                builder.setMessage("目的地："+poiItem.getTitle()+"\n距离:"+poiItem.getDistance()+"米\n是否导航?");
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                builder.setPositiveButton("开始", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Bianliang.setStart1(new NaviLatLng(mylocaltion.getLatitude(),mylocaltion.getLongitude()));
                                        Bianliang.setEnd1(new NaviLatLng(poiItem.getLatLonPoint().getLatitude(),poiItem.getLatLonPoint().getLongitude()));
                                        Intent intent=new Intent(FuwuSearchActivity.this, WalkRouteCalculateActivity.class);
                                        startActivity(intent);

                                    }
                                });
                                builder.create().show();
                            }
                        });
                        btn.getBackground().setAlpha(150);//0~255透明度值
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        if (f == 1) {
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        if (rflag == 0) {
                            //lie[36]++;

                            params = new RelativeLayout.LayoutParams(new ViewGroup.LayoutParams((int)(6*flag), 192));
                            params.setMargins((int) (width / 2 + 36 * 10 * flag), (int) height / 13 + num * 195, 0, 0);
                            btn.setLayoutParams(params);
                            btn.setWidth((int)(6*flag));
                            btn.setHeight(160);
                            btn.setTextSize(10);
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(FuwuSearchActivity.this);
                                    builder.setIcon(R.drawable.daohang);
                                    builder.setTitle("提示：");
                                    builder.setMessage("目的地："+poiItem.getTitle()+"\n距离:"+poiItem.getDistance()+"米\n是否导航?");
                                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });
                                    builder.setPositiveButton("开始", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Bianliang.setStart1(new NaviLatLng(mylocaltion.getLatitude(),mylocaltion.getLongitude()));
                                            Bianliang.setEnd1(new NaviLatLng(poiItem.getLatLonPoint().getLatitude(),poiItem.getLatLonPoint().getLongitude()));
                                            Intent intent=new Intent(FuwuSearchActivity.this, WalkRouteCalculateActivity.class);
                                            startActivity(intent);

                                        }
                                    });
                                    builder.create().show();
                                }
                            });
                            btn.getBackground().setAlpha(150);//0~255透明度值
                        } else {
                            //lie[37]++;

                            params = new RelativeLayout.LayoutParams(new ViewGroup.LayoutParams((int)(6*flag), 192));
                            //left, top ,right, bottom
                            params.setMargins((int) (width / 2 - 10 * flag), (int) height / 13 + num * 195, 0, 0);
                            btn.setLayoutParams(params);
                            btn.setWidth((int)(6*flag));
                            btn.setHeight(160);
                            btn.setTextSize(10);
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(FuwuSearchActivity.this);
                                    builder.setIcon(R.drawable.daohang);
                                    builder.setTitle("提示：");
                                    builder.setMessage("目的地："+poiItem.getTitle()+"\n距离:"+poiItem.getDistance()+"米\n是否导航?");
                                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });
                                    builder.setPositiveButton("开始", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Bianliang.setStart1(new NaviLatLng(mylocaltion.getLatitude(),mylocaltion.getLongitude()));
                                            Bianliang.setEnd1(new NaviLatLng(poiItem.getLatLonPoint().getLatitude(),poiItem.getLatLonPoint().getLongitude()));
                                            Intent intent=new Intent(FuwuSearchActivity.this, WalkRouteCalculateActivity.class);
                                            startActivity(intent);

                                        }
                                    });
                                    builder.create().show();
                                }
                            });
                            btn.getBackground().setAlpha(150);//0~255透明度值
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        return btn;
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
