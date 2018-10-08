package com.amap.navi.daohang;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.PoiInputItemWidget;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.navi.daohang.Basic.WalkRouteCalculateActivity;
import com.amap.navi.daohang.overlay.WalkRouteOverlay;
import com.autonavi.tbt.TrafficFacilityInfo;

import java.util.List;

public class DaohangActivity extends AppCompatActivity implements AMapNaviListener,RouteSearch.OnRouteSearchListener
{
    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<RouteOverLay>();
    private boolean calculateSuccess = false;
    private AutoCompleteTextView qidian;
    private AutoCompleteTextView zhongdian;
    private MapView mRouteMapView;
    private Marker mStartMarker;
    private Marker mEndMarker;
    private AMap mAmap;
    //private AMapNavi mAMapNavi;

    private NaviLatLng endLatlng;
    private NaviLatLng startLatlng;


    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private RouteSearch routeSearch;
    private RouteSearch.FromAndTo fromAndTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daohang);
        qidian = (AutoCompleteTextView) findViewById(R.id.search_input1);
        qidian.setFocusable(false);
        zhongdian = (AutoCompleteTextView) findViewById(R.id.search_input2);
        zhongdian.setFocusable(false);



        zhongdian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eintent = new Intent(DaohangActivity.this, SearchActivity.class);
                Bundle ebundle = new Bundle();
                ebundle.putInt("pointType", PoiInputItemWidget.TYPE_DEST);
                eintent.putExtras(ebundle);
                startActivityForResult(eintent, 200);
            }
        });
        mRouteMapView = (MapView) findViewById(R.id.navi_view);
        mRouteMapView.onCreate(savedInstanceState);
        mAmap = mRouteMapView.getMap();
        // 初始化Marker添加到地图
        mStartMarker = mAmap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.start))));
        mEndMarker = mAmap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.end))));

        //mAMapNavi = AMapNavi.getInstance(getApplicationContext());

        mLocationClient = new AMapLocationClient(getApplicationContext());
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
        AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        try {
                            MyLocationStyle myLocationStyle;
                            myLocationStyle = new MyLocationStyle();
                            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
                            mAmap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
                            mAmap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false
                            Bianliang.setCity(amapLocation.getCity());
                            startLatlng = new NaviLatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                            Bianliang.setStart1(startLatlng);
                            Bianliang.setLatLonPointstart(new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude()));
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(DaohangActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            System.out.print("错误："+e.getMessage());
                        }


                    }else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Toast.makeText(DaohangActivity.this,"定位失败",Toast.LENGTH_SHORT).show();
                    }
                }

            }


        };
        CameraUpdate cameraUpdate=new CameraUpdateFactory().zoomTo(17);
        mAmap.moveCamera(cameraUpdate);
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        //mAMapNavi.addAMapNaviListener(this);
        routeSearch=new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);

    }



            //从搜索界面获取数据
            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                if (data != null && data.getParcelableExtra("poi") != null) {

                    Poi poi = data.getParcelableExtra("poi");
                    if (requestCode == 200) {//终点选择完成
                        //Toast.makeText(this, "200", Toast.LENGTH_SHORT).show();
                        mAmap.clear();
                        mAmap.setMyLocationEnabled(false);
                        mStartMarker.setPosition(new LatLng(startLatlng.getLatitude(), startLatlng.getLongitude()));
                        mEndMarker.setPosition(new LatLng(poi.getCoordinate().latitude, poi.getCoordinate().longitude));
                        zhongdian.setText(poi.getName());
                        Bianliang.setEnd1(new NaviLatLng(poi.getCoordinate().latitude, poi.getCoordinate().longitude));
                        Bianliang.setLatLonPointend(new LatLonPoint(poi.getCoordinate().latitude, poi.getCoordinate().longitude));
                        //mAMapNavi.calculateWalkRoute(startLatlng,endLatlng);
                        fromAndTo=new RouteSearch.FromAndTo(Bianliang.getLatLonPointstart(),Bianliang.getLatLonPointend());
                        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo,RouteSearch.WALK_MULTI_PATH);
                        routeSearch.calculateWalkRouteAsyn(query);
                    }


                }
            }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mRouteMapView.onDestroy();
        //mAMapNavi.removeAMapNaviListener(this);
        //mAMapNavi.destroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mRouteMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mRouteMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mRouteMapView.onSaveInstanceState(outState);
    }


    private void drawRoutes(int routeId, AMapNaviPath path) {
        calculateSuccess = true;
        mAmap.moveCamera(CameraUpdateFactory.changeTilt(0));
        RouteOverLay routeOverLay = new RouteOverLay(mAmap, path, this);
        routeOverLay.setTrafficLine(false);
        routeOverLay.addToMap();
        routeOverlays.put(routeId, routeOverLay);
    }

    @Override
    public void onInitNaviFailure() {


    }

    @Override
    public void onInitNaviSuccess() {
        Toast.makeText(this, "初始化成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {


    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
        if(i==1000) {
            List<WalkPath> walkPathList = walkRouteResult.getPaths();
            WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(this, mAmap, walkPathList.get(0), Bianliang.getLatLonPointstart(), Bianliang.getLatLonPointend());
            walkRouteOverlay.addToMap();
            Snackbar.make(mRouteMapView,"导航",Snackbar.LENGTH_INDEFINITE)
                    .setAction("确认", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(DaohangActivity.this, WalkRouteCalculateActivity.class);
                            startActivity(intent);
                        }
                    }).show();
        }
        else
        {
            Toast.makeText(this, "距离太长不适合步行，请重新选点！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
