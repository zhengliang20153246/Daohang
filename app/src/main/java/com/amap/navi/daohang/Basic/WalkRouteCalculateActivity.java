package com.amap.navi.daohang.Basic;

import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;

import com.amap.api.navi.model.NaviInfo;


import com.amap.navi.daohang.Bianliang;

import com.amap.navi.daohang.R;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerNativeActivity;


public class WalkRouteCalculateActivity extends BaseActivity implements SensorEventListener
{

    private LinearLayout scan;
    private RelativeLayout navixinxi;

    private SensorManager sensorManager = null;
    private Sensor gyroSensor = null;

    private float[] angle = new float[3];
    private View view;
    private int flag = 0;

    private TextView luming;
    private TextView mi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_navi);

        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view1);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
        mAMapNaviView.setNaviMode(AMapNaviView.NORTH_UP_MODE);
        AMapNaviViewOptions options = mAMapNaviView.getViewOptions();
        options.setCarBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.amap_man));
        mAMapNaviView.setViewOptions(options);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ORIENTATION);
        view=mUnityPlayer.getView();
        scan=(LinearLayout)findViewById(R.id.scan);
        scan.addView(view);
        navixinxi=(RelativeLayout)findViewById(R.id.navixinxi);
        luming=(TextView)findViewById(R.id.luming);
        mi=(TextView)findViewById(R.id.mi);

    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        mAMapNavi.calculateWalkRoute(Bianliang.getStart1(),Bianliang.getEnd1());

    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        super.onCalculateRouteSuccess(ids);
        mAMapNavi.startNavi(NaviType.GPS);
    }
    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo) {
        mi.setText(naviinfo.getCurStepRetainDistance()+"米");
        luming.setText(naviinfo.getNextRoadName());



    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.values[1] < 8 && sensorEvent.values[1] > -40) {
            // 地图显示（因为是时时监测，所以定义flag来确保只是加载一次view）
            if (flag == 1) {
                /*ditu.setVisibility(View.VISIBLE);//显示地图
                activity.setVisibility(View.GONE);//隐藏
                scan.removeAllViews();
                scan.addView(activity);*/

                navixinxi.setVisibility(View.GONE);
                scan.setVisibility(View.GONE);
                mAMapNaviView.setVisibility(View.VISIBLE);
                flag = 0;
            }
        } else {
            // AR显示（因为是时时监测，所以定义flag来确保只是加载一次view）
            if (flag == 0) {
                //scan.removeView(btn);
                /*scan.addView(view);
                ditu.setVisibility(View.GONE);//隐藏
                mi.setText("25米");
                activity.setVisibility(View.VISIBLE);//显示*/
                mAMapNaviView.setVisibility(View.GONE);
                navixinxi.setVisibility(View.VISIBLE);
                scan.setVisibility(View.VISIBLE);
                flag = 1;
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
// TODO Auto-generated method stub
        super.onPause();
        sensorManager.unregisterListener(this); // 解除监听器注册
    }
    @Override
    protected void onResume() {
// TODO Auto-generated method stub
        super.onResume();
        sensorManager.registerListener(this, gyroSensor,
                SensorManager.SENSOR_DELAY_NORMAL); //为传感器注册监听器
    }
    public WalkRouteCalculateActivity() {
// TODO Auto-generated constructor stub
        angle[0] = 0;
        angle[1] = 0;
        angle[2] = 0;
    }
}
