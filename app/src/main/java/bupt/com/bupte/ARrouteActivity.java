package bupt.com.bupte;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.FloatProperty;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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

import java.util.ArrayList;
import java.util.List;

public class ARrouteActivity extends AppCompatActivity {//AR导航功能页面

    private MySurfaceView mySurfaceView;
    private MyView myview;
//    private ThreadTest newThread=new ThreadTest();

    private SensorManager mSensorManager;
    private Sensor accelerometer; // 加速度传感器
    private Sensor magnetic; // 地磁场传感器
    private float angle;
    private float angles=0;
    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];

    private RoutePlanSearch routeSearch;
    public LocationClient mLocationClient;
    //    private double a1=39.967113916777636;
//    private double a2=116.36479162025452;
    private double a1=0;
    private double a2=0;

    private PlanNode stNode,enNode;
    private double b1=0;
    private double b2=0;
    private int TAG=0,TAG1=0;
    private TextView textView,textView1,textView2;
    private List<LatLng> points = null;
    private List<LatLng> points1=null;
    private List<LatLng> points2=null;
    private ArrayList<Integer> dirc=new ArrayList<Integer>();
    private double dis=0;
    private int Num=0;
    private int size=0;
    private boolean TAG2=true;
    private int size0=0;
    private final int msg_okl=2;
    private long mExitTime;
    private ImageView room_show;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case msg_okl:
                    stNode = PlanNode.withLocation(new LatLng(a1,a2));
                    enNode = PlanNode.withLocation(new LatLng(b1,b2));
                    routeSearch.walkingSearch((new WalkingRoutePlanOption())
                            .from(stNode)
                            .to(enNode));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arroute);
        requestPermission();
        Intent intent=getIntent();
//        b1=intent.getDoubleExtra("b1",39.967113916777636);
//        b2=intent.getDoubleExtra("b2",116.36479162025452);
        b1=intent.getDoubleExtra("b1",39.967113916777636);
        b2=intent.getDoubleExtra("b2",116.36479162025452);

        initView();
        room_show=(ImageView)findViewById(R.id.imageView5);
        Button roomloc=(Button)findViewById(R.id.button_roomloc);
        roomloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                room_show.setVisibility(View.VISIBLE);
            }
        });

        myview=(MyView)findViewById(R.id.myview) ;
//        mLocationClient = new LocationClient(getApplicationContext());
//        mLocationClient.registerLocationListener(new ARrouteActivity.MyLocationListener());

//        initPlan();
        initSensor();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void requestPermission(){
        if (!CommonLY.checkPermission(this, Manifest.permission.CAMERA)) {
            CommonLY.applyPermission(this, Manifest.permission.CAMERA, 0);
        }
    }

    private void initSensor(){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelerometer==null){
            MyToast.makeText(ARrouteActivity.this,"没有加速传感器",Toast.LENGTH_SHORT).show();
        }
        magnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(magnetic==null){
            MyToast.makeText(ARrouteActivity.this,"没有磁电传感器",Toast.LENGTH_SHORT).show();
        }
    }

    private void initPlan() {
        routeSearch = RoutePlanSearch.newInstance();
        routeSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                List<WalkingRouteLine> routeLines = walkingRouteResult.getRouteLines();
                MyToast.makeText(ARrouteActivity.this, "定位成功", Toast.LENGTH_SHORT).show();
//                Log.d("wentis","zheli");
                Log.d("wentid",""+TAG2);
                if (routeLines != null) {
                    WalkingRouteLine line = walkingRouteResult.getRouteLines().get(0);
                    if(line.getDistance()>10000){
                        TAG2=false;
                        MyToast.makeText(ARrouteActivity.this, "离目的地太远", Toast.LENGTH_SHORT).show();
                    }
                    List<WalkingRouteLine.WalkingStep> steps = line.getAllStep();
                    size0=steps.size();
                    points1=new ArrayList<LatLng>();
                    points2=new ArrayList<LatLng>();
                    for(int i=0;i<size0;i++){
                        dirc.add(steps.get(i).getDirection());
                        points1.add(steps.get(i).getWayPoints().get(0));
                        int size2=steps.get(i).getWayPoints().size();
                        points2.add(steps.get(i).getWayPoints().get(size2-1));
                    }
                    points = steps.get(0).getWayPoints();
                    for(int i=1;i<size0;i++){
                        points.addAll(steps.get(i).getWayPoints());
                    }
                    size=points.size();
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
        });
    }

//    @Override
//    protected void onResume() {
//        mSensorManager.registerListener(new MySensorEventListener(),
//                accelerometer, Sensor.TYPE_ACCELEROMETER);
//        mSensorManager.registerListener(new MySensorEventListener(), magnetic,
//                Sensor.TYPE_MAGNETIC_FIELD);
//        super.onResume();
//        requestLocation();
//        ThreadTest newThread=new ThreadTest();
//        newThread.start();
//    }

//    @Override
//    protected void onPause() {
//        mSensorManager.unregisterListener(new MySensorEventListener());
//        super.onPause();
//        TAG2=false;
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        mLocationClient.stop();
//    }

    class ThreadTest extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (TAG2) {
                if (Num < size0 - 1) {
                    if (GetJuLi(a1, a2, points2.get(Num).latitude, points2.get(Num).longitude) > 20) {
                        dis = GetJuLi(a1, a2, points2.get(Num).latitude, points2.get(Num).longitude);
                        angles = dirc.get(Num);
                    } else {
                        dis = GetJuLi(a1, a2, points2.get(Num).latitude, points2.get(Num).longitude);
                        angles = dirc.get(Num);
                        Num += 1;
                    }
                } else if (Num == size0 - 1) {
                    if (GetJuLi(a1, a2, b1, b2) > 20) {
                        //以下改动
                        dis = GetJuLi(a1, a2, b1, b2);
                        angles= (float)(getJiaoDu(a1,a2,b1,b2));
//                        angles = dirc.get(Num);
                    } else {
                        //以下改动
                        MyToast.makeText(ARrouteActivity.this, "到达目的地", Toast.LENGTH_SHORT).show();
                        angles= (float)(getJiaoDu(a1,a2,b1,b2));
//                        angles = dirc.get(Num);
                        Num += 1;
                    }
                }
                try {
                    Thread.sleep(1000);
                    myview.setangle(angle, angles);
                    myview.postInvalidate();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private  void requestLocation(){
        initLocation();
        try{
            mLocationClient.start();
        }catch (Exception e)
        {
            MyToast.makeText(ARrouteActivity.this,"定位失败",Toast.LENGTH_SHORT).show();
        }
    }

    private double GetJuLi(double lat_a, double lng_a, double lat_b, double lng_b){
        double lat1 = (Math.PI / 180) * lat_a;
        double lat2 = (Math.PI / 180) * lat_b;
        double lon1 = (Math.PI / 180) * lng_a;
        double lon2 = (Math.PI / 180) * lng_b;
        double R = 6371;
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
        return d * 1000;
    }

//    private double GetJiaoDu(double lat_a, double lng_a, double lat_b, double lng_b){
//        double x1 = lng_a;
//        double y1 = lat_a;
//        double x2 = lng_b;
//        double y2 = lat_b;
//        double pi = Math.PI;
//        double w1 = y1 / 180 * pi;
//        double j1 = x1 / 180 * pi;
//        double w2 = y2 / 180 * pi;
//        double j2 = x2 / 180 * pi;
//        double ret;
//        if (j1 == j2) {
//            if (w1 > w2)
//                return 270; // 北半球的情况，南半球忽略
//            else if (w1 < w2)
//                return 90;
//            else
//                return -1;// 位置完全相同
//        }
//        ret = 4* Math.pow(Math.sin((w1 - w2) / 2), 2)- Math.pow(
//                Math.sin((j1 - j2) / 2) * (Math.cos(w1) - Math.cos(w2)),2);
//        ret = Math.sqrt(ret);
//        double temp = (Math.sin(Math.abs(j1 - j2) / 2) * (Math.cos(w1) + Math
//                .cos(w2)));
//        ret = ret / temp;
//        ret = Math.atan(ret) / pi * 180;
//        if (j1 > j2){ // 1为参考点坐标
//            if (w1 > w2)
//                ret += 180;
//            else
//                ret = 180 - ret;
//        } else if (w1 > w2)
//            ret = 360 - ret;
//        return ret;
//    }

    private double getJiaoDu(double lat_a, double lng_a, double lat_b, double lng_b){
        double k1 = lng_b-lng_a;
        double k2= lat_b-lat_a;
        double str=0;
        if( 0 == k1){
            if(k2>0){
                str=0;
            }
            else if( k2<0){
                str =180;
            }
            else if( k2 == 0){
                str=0;
            }
        }else if( 0 == k2){
            if(k1>0){
                str=90;
            }
            else if( k1<0){
                str=270;
            }
        }else{
            double k=k2/k1;
            if(k2>0){
                if(k1>0){
                    str=45;
                }else if(k1<0){
                    str= 315;
                }
            }else if(k2<0){
                if(k1<0){
                    str = 225;
                }
                else if(k1>0){
                    str=135;
                }
            }
        }
        return str;
    }

    private void initLocation(){
        LocationClientOption option =new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
    }

    private void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues,
                magneticFieldValues);
        SensorManager.getOrientation(R, values);
        values[0] = (float) Math.toDegrees(values[0]);
        if(values[0]<0){
            angle=360+values[0];
        }else {
            angle=values[0];
        }
    }

    class MySensorEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                accelerometerValues = event.values;
            }
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                magneticFieldValues = event.values;
            }
            calculateOrientation();
//            angle1=angles;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    private void initView() {
        mySurfaceView = (MySurfaceView) findViewById(R.id.mysurfaceview);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            a1=location.getLatitude();
            a2=location.getLongitude();
            if(TAG==0) {
                MyToast.makeText(ARrouteActivity.this, "正在定位中", Toast.LENGTH_LONG).show();
                TAG = 1;
                Message msg2=new Message();
                msg2.what=msg_okl;
                mHandler.sendMessage(msg2);
            }
//            Message msg2=new Message();
//            msg2.what=msg_okl;
//            mHandler.sendMessage(msg2);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(room_show.getVisibility()==View.VISIBLE){
                room_show.setVisibility(View.INVISIBLE);
            }else if ((System.currentTimeMillis() - mExitTime) > 2000) {
                MyToast.makeText(this, "确认退出？", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return true;
    }
}
