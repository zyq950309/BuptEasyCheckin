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
import android.util.Log;
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

        Intent intent=getIntent();
        b1=intent.getDoubleExtra("b1",39.967113916777636);
        b2=intent.getDoubleExtra("b2",116.36479162025452);
        initView();

        myview=(MyView)findViewById(R.id.myview) ;

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new ARrouteActivity.MyLocationListener());
        initPlan();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelerometer==null){
            MyToast.makeText(ARrouteActivity.this,"没有加速传感器",Toast.LENGTH_SHORT).show();
        }
        magnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(magnetic==null){
            MyToast.makeText(ARrouteActivity.this,"没有磁电传感器",Toast.LENGTH_SHORT).show();
        }

        if (!CommonLY.checkPermission(this, Manifest.permission.CAMERA)) {
            CommonLY.applyPermission(this, Manifest.permission.CAMERA, 0);
        }
    }

    private void initPlan() {
        routeSearch = RoutePlanSearch.newInstance();
        routeSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                List<WalkingRouteLine> routeLines = walkingRouteResult.getRouteLines();
                if (routeLines != null) {
                    WalkingRouteLine line = walkingRouteResult.getRouteLines().get(0);
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

    @Override
    protected void onResume() {
        mSensorManager.registerListener(new MySensorEventListener(),
                accelerometer, Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(new MySensorEventListener(), magnetic,
                Sensor.TYPE_MAGNETIC_FIELD);
        super.onResume();
        requestLocation();
        ThreadTest newThread=new ThreadTest();
        newThread.start();
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(new MySensorEventListener());
        super.onPause();
        TAG2=false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stop();
    }

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
                    if (GetJuLi(a1, a2, points2.get(Num).latitude, points2.get(Num).longitude) > 15) {
                        dis = GetJuLi(a1, a2, points2.get(Num).latitude, points2.get(Num).longitude);
                        angles = dirc.get(Num);
                    } else {
                        dis = GetJuLi(a1, a2, points2.get(Num).latitude, points2.get(Num).longitude);
                        angles = dirc.get(Num);
                        Num += 1;
                    }
                } else if (Num == size0 - 1) {
                    if (GetJuLi(a1, a2, b1, b2) > 15) {
                        dis = GetJuLi(a1, a2, b1, b2);
                        angles = dirc.get(Num);
                    } else {
                        angles = dirc.get(Num);
                        Num += 1;
                    }
                }
                try {
                    Thread.sleep(500);
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
            MyToast.makeText(ARrouteActivity.this,"GPS Locate 失败",Toast.LENGTH_SHORT).show();
        }
    }

//    private  double GetJiaoDu(double lat_a, double lng_a, double lat_b, double lng_b) {
//
//        double y = Math.sin(lng_b-lng_a) * Math.cos(lat_b);
//        double x = Math.cos(lat_a)*Math.sin(lat_b) - Math.sin(lat_a)*Math.cos(lat_b)*Math.cos(lng_b-lng_a);
//        double brng = Math.atan2(y, x);
//
//        brng = Math.toDegrees(brng);
//        if(brng < 0)
//            brng = brng +360;
//        return brng;
//
//    }

    private double GetJuLi(double lat_a, double lng_a, double lat_b, double lng_b){
        double lat1 = (Math.PI / 180) * lat_a;
        double lat2 = (Math.PI / 180) * lat_b;
        double lon1 = (Math.PI / 180) * lng_a;
        double lon2 = (Math.PI / 180) * lng_b;
        double R = 6371;
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
        return d * 1000;
    }

    private  double GetJiaoDu(double lat_a, double lng_a, double lat_b, double lng_b) {

        double y = Math.sin(lng_b-lng_a) * Math.cos(lat_b);
        double x = Math.cos(lat_a)*Math.sin(lat_b) - Math.sin(lat_a)*Math.cos(lat_b)*Math.cos(lng_b-lng_a);
        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        if(brng < 0)
            brng = brng +360;
        return brng;

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
            // TODO Auto-generated method stub

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
            if(location.getLocType()==BDLocation.TypeGpsLocation) {
                if(TAG==0) {
                    MyToast.makeText(ARrouteActivity.this, "GPS Locate OK", Toast.LENGTH_SHORT).show();
                    TAG = 1;
                }else{
                    Log.d("do","do nothing");
                }
            }else{
                if(TAG1==0) {
                    MyToast.makeText(ARrouteActivity.this, "GPS Locate NO,请前往开阔地", Toast.LENGTH_SHORT).show();
                    TAG1 = 1;
                }else{
                    Log.d("do","do nothing");
                }
            }
            Message msg2=new Message();
            msg2.what=msg_okl;
            mHandler.sendMessage(msg2);
        }
    }
}
