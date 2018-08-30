package bupt.com.bupte;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.unity3d.player.*;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import static com.baidu.mapapi.BMapManager.getContext;

public class UnityPlayerActivity extends AppCompatActivity
{
    private static final String FIRST = "first";
    private static final String FIRST_FLAG = "flag";
    private boolean isFirst ;
    private static final String TAG = "UnityPlayerActivity";
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code
    // Setup activity layout
    private double a1 = 0;//当前位置坐标
    private double a2 = 0;
    private double b1 = 0;//目的地坐标
    private double b2 = 0;
    int id=0;
    private int Tag=0;
    private PlanNode stNode, enNode;//定位的初始点和终点
    public LocationClient mLocationClient;//定位器
    private BDLocationListener mylistener = new MyLocationListener();//定位监听器

    private Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg){
        switch (msg.what){
            case 1:
                initRoutePlan(40.163299,116.290664,1);
                Tag=1;
                break;
            case 2:
                initRoutePlan(28.421957,117.608362,2);
                break;
        }
    }
};

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        requestLocation();
        getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy
        mUnityPlayer = new MyUnityPlayer(this);

        setContentView(mUnityPlayer);
        mUnityPlayer.requestFocus();
       class MyThread implements Runnable{
           @Override
           public void run() {
               try {
                   Thread.sleep(5000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               UserGuideDialog userGuideDialog = new UserGuideDialog();
               userGuideDialog.show(getFragmentManager(),"");
           }
       }
        SharedPreferences sp = getSharedPreferences(FIRST,0);
        isFirst = sp.getBoolean(FIRST_FLAG,true);
        if(isFirst){
            MyThread myThread = new MyThread();
            new Thread(myThread).start();
        }
        SharedPreferences settings = getSharedPreferences(FIRST,0);
        SharedPreferences.Editor editor = settings.edit();
        if(isFirst){
            editor.putBoolean(FIRST_FLAG,false);
        }
        editor.apply();


    }

    class MyRouteListener implements OnGetRoutePlanResultListener {

        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {//根据目的地画出路线
            List<WalkingRouteLine> routeLines = walkingRouteResult.getRouteLines();
//            mBaiduMap.clear();
            if (routeLines != null) {
                if(id==1) {
                    WalkingRouteLine line = walkingRouteResult.getRouteLines().get(0);
                    MyToolClass.setDistance1(line.getDistance());
                    MyToolClass.setTime1(line.getDuration() / 60);
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }else {
                    WalkingRouteLine line = walkingRouteResult.getRouteLines().get(0);
                    MyToolClass.setDistance2(line.getDistance());
                    MyToolClass.setTime2(line.getDuration() / 60);
                }
            }
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    }

    public void initRoutePlan(double b1, double b2,int id) {//根据目的地开启定位，并规划线路

        this.id=id;
        RoutePlanSearch newInstance = RoutePlanSearch.newInstance();
        newInstance.setOnGetRoutePlanResultListener(new MyRouteListener());

        stNode = PlanNode.withLocation(new LatLng(a1, a2));
        enNode = PlanNode.withLocation(new LatLng(b1, b2));
        newInstance.walkingSearch((new WalkingRoutePlanOption())
                .from(stNode)
                .to(enNode));
    }

    private void initLocation() {//初始化定位器
        mLocationClient = new LocationClient(getContext());
        mLocationClient.registerLocationListener(mylistener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(200000);
        mLocationClient.setLocOption(option);
    }

    private void requestLocation() {
        initLocation();//初始化定位器
        try {
            mLocationClient.start();//开启定位器
        } catch (Exception e) {
            MyToast.makeText(UnityPlayerActivity.this, "定位失败", Toast.LENGTH_SHORT).show();
        }
    }

    public class MyLocationListener implements BDLocationListener {//定位监听

        @Override
        public void onReceiveLocation(BDLocation location) {
            a1 = location.getLatitude();
            a2 = location.getLongitude();
            if(Tag==0){
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }
    }

    public void showDalitangNavi(){
        ExploreFragment exploreFragment = new ExploreFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("order_explore",2);
        exploreFragment.setArguments(bundle);
        exploreFragment.show(getSupportFragmentManager(),exploreFragment.getTag());
    }
    public void showNanmenNavi(){
        ExploreFragment exploreFragment = new ExploreFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("order_explore",1);
        exploreFragment.setArguments(bundle);
        exploreFragment.show(getSupportFragmentManager(),exploreFragment.getTag());
    }
    @Override protected void onNewIntent(Intent intent)
    {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
    }

    // Quit Unity
    @Override protected void onDestroy ()
    {
        mUnityPlayer.quit();
        super.onDestroy();
    }

    // Pause Unity
    @Override protected void onPause()
    {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override protected void onResume()
    {
        super.onResume();
        mUnityPlayer.resume();
    }

    @Override protected void onStart()
    {
        super.onStart();
        mUnityPlayer.start();
    }

    @Override protected void onStop()
    {
        super.onStop();
        mUnityPlayer.stop();
    }

    // Low Memory Unity
    @Override public void onLowMemory()
    {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL)
        {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
//    @Override public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
//    @Override public boolean onKeyDown(int keyCode, KeyEvent event)   { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
    /*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }
}
