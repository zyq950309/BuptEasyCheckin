package bupt.com.bupte;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.UiSettings;
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
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.BottomSheetFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Fragment_check extends Fragment implements View.OnClickListener{//下面三个按钮的“报道”功能

    private Button locate_button, check_button,flush_button;//定位按钮和报道按钮
    private MapView mMapView;//地图显示控件
    private BaiduMap mBaiduMap;//地图控件
    private double s1 = 39.967113916777636;//地图中心点坐标
    private double s2 = 116.36479162025452;
    //    private double s1 = 40.163271;//地图中心点坐标
//    private double s2 = 116.294819;
    private double a1 = 0;//当前位置坐标
    private double a2 = 0;
    private double b1 = 0;//目的地坐标
    private double b2 = 0;
    private PlanNode stNode, enNode;//定位的初始点和终点
    public LocationClient mLocationClient;//定位器
    private List<LatLng> points1 = new ArrayList<LatLng>();//每一小段路的初始点集合
    private List<LatLng> points2 = new ArrayList<LatLng>();//每一小段路的终点集合
    private List<LatLng> points_site = new ArrayList<LatLng>();//用于保存报道节点的集合
    private List<String> latitude=new ArrayList<String>();//保存纬度
    private List<String> longitude=new ArrayList<String>();//保存经度
    private int size0 = 0;//points1和points2的size
    private List<LatLng> points = new ArrayList<LatLng>();//总的路线的所有点集合
    private BDLocationListener mylistener = new MyLocationListener();//定位监听器
    private List<LatLng> site = new ArrayList<LatLng>();//记录所有报道地点的位置
    private WalkNavigateHelper mNaviHelper;
    private LatLng startPt, endPt;
    private WalkNaviLaunchParam walkParam;
    private int order;
    BottomSheetLayout bottomSheetLayout;
    private int Tag;
    private View fragView;
    private View fragViewLogin;
    private Boolean IsStudent;
    private List<String> number;
    private Boolean Tag1=true;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    firstRefresh();
                    Tag=1;
                    break;
                case 2:
                    Toast.makeText(getActivity(), "数据库连接错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check, container, false);

        bottomSheetLayout = (BottomSheetLayout)view.findViewById(R.id.bottomsheet);
//        Log.d("wenti6",""+bottomSheetLayout.getState());
        bottomSheetLayout.setPeekSheetTranslation(600);
//        View fragView = LayoutInflater.from(getActivity()).inflate(R.layout.empty, bottomSheetLayout, false);
        Bundle bundle=getArguments();
        order=bundle.getInt("order");
        IsStudent=bundle.getBoolean("IsStudent");
        if(IsStudent){
            fragView = LayoutInflater.from(getActivity()).inflate(R.layout.empty, bottomSheetLayout, false);
            bottomSheetLayout.showWithSheetView(fragView);
            MyFragment myFragment = new MyFragment();
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, myFragment).addToBackStack(null).commit();
        }else {
            fragViewLogin = LayoutInflater.from(getActivity()).inflate(R.layout.empty, bottomSheetLayout, false);
            bottomSheetLayout.showWithSheetView(fragViewLogin);

            LoginFragment myLoginFragment = new LoginFragment();
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, myLoginFragment).addToBackStack(null).commit();
        }

        switch (order){
            case 1:
                fragView = LayoutInflater.from(getActivity()).inflate(R.layout.empty, bottomSheetLayout, false);
                bottomSheetLayout.showWithSheetView(fragView);
                MyDetailFragment myDetailFragment1 = new MyDetailFragment();
                Bundle bundleDetail1 = new Bundle();
                bundleDetail1.putInt("order",order);
                myDetailFragment1.setArguments(bundleDetail1);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, myDetailFragment1).addToBackStack(null).commit();
//                fragView=myFragment;
                break;
            case 2:
                fragView = LayoutInflater.from(getActivity()).inflate(R.layout.empty, bottomSheetLayout, false);
                bottomSheetLayout.showWithSheetView(fragView);
                MyDetailFragment myDetailFragment2 = new MyDetailFragment();
                Bundle bundleDetail2 = new Bundle();
                bundleDetail2.putInt("order",order);
                myDetailFragment2.setArguments(bundleDetail2);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, myDetailFragment2).addToBackStack(null).commit();
//                fragView=myFragment;
                break;
            case 3:
                fragView = LayoutInflater.from(getActivity()).inflate(R.layout.empty, bottomSheetLayout, false);
                bottomSheetLayout.showWithSheetView(fragView);
                MyDetailFragment myDetailFragment3 = new MyDetailFragment();
                Bundle bundleDetail3 = new Bundle();
                bundleDetail3.putInt("order",order);
                myDetailFragment3.setArguments(bundleDetail3);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, myDetailFragment3).addToBackStack(null).commit();
//                fragView=myFragment;
                break;
            case 4:
                fragView = LayoutInflater.from(getActivity()).inflate(R.layout.empty, bottomSheetLayout, false);
                bottomSheetLayout.showWithSheetView(fragView);
                MyDetailFragment myDetailFragment4 = new MyDetailFragment();
                Bundle bundleDetail4 = new Bundle();
                bundleDetail4.putInt("order",order);
                myDetailFragment4.setArguments(bundleDetail4);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, myDetailFragment4).addToBackStack(null).commit();
//                fragView=myFragment;
                break;
            case 5:
                fragView = LayoutInflater.from(getActivity()).inflate(R.layout.empty, bottomSheetLayout, false);
                bottomSheetLayout.showWithSheetView(fragView);
                MyDetailFragment myDetailFragment5 = new MyDetailFragment();
                Bundle bundleDetail5 = new Bundle();
                bundleDetail5.putInt("order",order);
                myDetailFragment5.setArguments(bundleDetail5);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, myDetailFragment5).addToBackStack(null).commit();
//                fragView=myFragment;
                break;
            case 6:
                fragView = LayoutInflater.from(getActivity()).inflate(R.layout.empty, bottomSheetLayout, false);
                bottomSheetLayout.showWithSheetView(fragView);
                MyDetailFragment myDetailFragment6 = new MyDetailFragment();
                Bundle bundleDetail6 = new Bundle();
                bundleDetail6.putInt("order",order);
                myDetailFragment6.setArguments(bundleDetail6);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, myDetailFragment6).addToBackStack(null).commit();
//                fragView=myFragment;
                break;

            case 100:
                break;
        }

        mMapView = (MapView) view.findViewById(R.id.mapview);
        locate_button = (Button) view.findViewById(R.id.locate_button);
        check_button = (Button) view.findViewById(R.id.check_button);
        flush_button = (Button) view.findViewById(R.id.flush_button);

        locate_button.setOnClickListener(this);
        check_button.setOnClickListener(this);
        flush_button.setOnClickListener(this);

        search_site();
        search_sitenum();
        initMapStatus();
        requestLocation();
        return view;
    }

    public void firstRefresh(){
        switch (order){
            case 0:
                break;
            case 1:
                b1 = Double.parseDouble(latitude.get(0));
                b2 = Double.parseDouble(longitude.get(0));
                initRoutePlan(b1, b2);
                break;
            case 2:
                b1 = Double.parseDouble(latitude.get(1));
                b2 = Double.parseDouble(longitude.get(1));
                initRoutePlan(b1, b2);
                break;
            case 3:
                b1 = Double.parseDouble(latitude.get(2));
                b2 = Double.parseDouble(longitude.get(2));
                initRoutePlan(b1, b2);
                break;
            case 4:
                b1 = Double.parseDouble(latitude.get(3));
                b2 = Double.parseDouble(longitude.get(3));
                initRoutePlan(b1, b2);
                break;
            case 5:
                b1 = Double.parseDouble(latitude.get(4));
                b2 = Double.parseDouble(longitude.get(4));
                initRoutePlan(b1, b2);
                break;
            case 6:
                b1 = Double.parseDouble(latitude.get(5));
                b2 = Double.parseDouble(longitude.get(5));
                initRoutePlan(b1, b2);
                break;
        }
    }

    private void initMapStatus() {//初始化地图
        mBaiduMap = mMapView.getMap();
        UiSettings settings=mBaiduMap.getUiSettings();
        //settings.setAllGesturesEnabled(false);
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        mMapView.removeViewAt(1);
        mMapView.showZoomControls(false);
        MapStatus.Builder builder = new MapStatus.Builder();
//        builder.target(new LatLng(s1, s2)).zoom(18);
        builder.target(new LatLng(s1, s2)).zoom(17);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        mBaiduMap.setMyLocationEnabled(true);
    }

    private void initLocation() {//初始化定位器
        mLocationClient = new LocationClient(getContext());
        mLocationClient.registerLocationListener(mylistener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
    }

    private void requestLocation() {
        initLocation();//初始化定位器
        try {
            mLocationClient.start();//开启定位器
        } catch (Exception e) {
            Toast.makeText(getActivity(), "GPS Locate 失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void initRoutePlan(double b1, double b2) {//根据目的地开启定位，并规划线路
        RoutePlanSearch newInstance = RoutePlanSearch.newInstance();
        newInstance.setOnGetRoutePlanResultListener(new MyRouteListener());

        stNode = PlanNode.withLocation(new LatLng(a1, a2));
        enNode = PlanNode.withLocation(new LatLng(b1, b2));
        newInstance.walkingSearch((new WalkingRoutePlanOption())
                .from(stNode)
                .to(enNode));
    }

    public void initSite() {
        site.clear();
        int size_site=latitude.size();
        for(int i=0;i<size_site;i++) {
            site.add(new LatLng(Double.valueOf(latitude.get(i)), Double.valueOf(longitude.get(i))));
        }
    }

    public void initMarket(List<LatLng> site) {
        int size = site.size();
        for (int i = 0; i < size; i++) {
            LatLng point = site.get(i);
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.use_icon);

            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);

            OverlayOptions textOption = new TextOptions()
                    .bgColor(0x00E0EFF1)
                    .fontSize(60)
                    .fontColor(0xFF000000)
                    .text("百度地图SDK")
                    .position(point);
            mBaiduMap.addOverlay(option);
            mBaiduMap.addOverlay(textOption);
        }
    }

    @Override
    public void onClick(View v) {//点击事件
        switch (v.getId()) {
            case R.id.locate_button://显示当前位置
                navigateTo(a1, a2);
                break;
            case R.id.check_button:
                initSite();
                initMarket(site);
                break;
            case R.id.flush_button:
                if (IsStudent) {
                    switch (order){
                        case 100:
                            MyFragment myFragment = new MyFragment();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
                            bottomSheetLayout.showWithSheetView(fragView);
                            break;
                        case 1:
                            MyDetailFragment myDetailFragment = new MyDetailFragment();
                            Bundle bundleDetail = new Bundle();
                            bundleDetail.putInt("order",order);
                            myDetailFragment.setArguments(bundleDetail);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, myDetailFragment).addToBackStack(null).commit();
                            bottomSheetLayout.showWithSheetView(fragView);
                            break;
                        case 2:
                            MyDetailFragment myDetailFragment2 = new MyDetailFragment();
                            Bundle bundleDetail2 = new Bundle();
                            bundleDetail2.putInt("order",order);
                            myDetailFragment2.setArguments(bundleDetail2);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, myDetailFragment2).addToBackStack(null).commit();
                            bottomSheetLayout.showWithSheetView(fragView);
                            break;
                        case 3:
                            MyDetailFragment myDetailFragment3 = new MyDetailFragment();
                            Bundle bundleDetail3 = new Bundle();
                            bundleDetail3.putInt("order",order);
                            myDetailFragment3.setArguments(bundleDetail3);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, myDetailFragment3).addToBackStack(null).commit();
                            bottomSheetLayout.showWithSheetView(fragView);
                            break;
                        case 4:
                            MyDetailFragment myDetailFragment4 = new MyDetailFragment();
                            Bundle bundleDetail4 = new Bundle();
                            bundleDetail4.putInt("order",order);
                            myDetailFragment4.setArguments(bundleDetail4);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, myDetailFragment4).addToBackStack(null).commit();
                            bottomSheetLayout.showWithSheetView(fragView);
                            break;
                        case 5:
                            MyDetailFragment myDetailFragment5 = new MyDetailFragment();
                            Bundle bundleDetail5 = new Bundle();
                            bundleDetail5.putInt("order",order);
                            myDetailFragment5.setArguments(bundleDetail5);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, myDetailFragment5).addToBackStack(null).commit();
                            bottomSheetLayout.showWithSheetView(fragView);
                            break;
                        case 6:
                            MyDetailFragment myDetailFragment6 = new MyDetailFragment();
                            Bundle bundleDetail6 = new Bundle();
                            bundleDetail6.putInt("order",order);
                            myDetailFragment6.setArguments(bundleDetail6);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, myDetailFragment6).addToBackStack(null).commit();
                            bottomSheetLayout.showWithSheetView(fragView);
                            break;
                    }
                } else {
                    bottomSheetLayout.showWithSheetView(fragViewLogin);
                }
        }
    }

    private void search_site() {//查询数据库，报道节点经纬度信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://123.206.90.229/v1/searchll.php")
                        .build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responsedata = response.body().string();
                        Site_ll site_ll = GsonTools.getPerson(responsedata, Site_ll.class);
                        if (site_ll.getCode().equals("0")) {
                            latitude=site_ll.getLatitude();
                            longitude=site_ll.getLongitude();
                        }else{
                            Message msg = new Message();
                            msg.what = 2;
                            handler.sendMessage(msg);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void search_sitenum() {//查询数据库，报道节点经纬度信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(Tag1){
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://123.206.90.229/v1/search.php")
                            .build();
                    try {
                        Response response = okHttpClient.newCall(request).execute();
                        if (response.isSuccessful()) {
                            String responsedata = response.body().string();
                            Site_num site_num = GsonTools.getPerson(responsedata, Site_num.class);
                            if (site_num.getCode().equals("0")) {
                                number=site_num.getNumber();
                                int[] num={0,0,0,0,0,0};
                                for(int i=0;i<number.size();i++){
                                    num[i]=Integer.parseInt(number.get(i));
                                }
                                MyToolClass.setNum(num);
                            }else{
                                Message msg = new Message();
                                msg.what = 2;
                                handler.sendMessage(msg);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void navigateTo(double a1,double a2){//地图上显示当前位置点
        mBaiduMap.clear();
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(a1);
        locationBuilder.longitude(a2);
        MyLocationData locationData = locationBuilder.build();
        mBaiduMap.setMyLocationData(locationData);
    }

    public class MyLocationListener implements BDLocationListener {//定位监听

        @Override
        public void onReceiveLocation(BDLocation location) {
            a1=location.getLatitude();
            a2=location.getLongitude();
            if(Tag==0){
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }
    }

    class MyRouteListener implements OnGetRoutePlanResultListener{

        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {//根据目的地画出路线
            List<WalkingRouteLine> routeLines = walkingRouteResult.getRouteLines();
            mBaiduMap.clear();
            points.clear();
            if (routeLines != null) {
                WalkingRouteLine line = walkingRouteResult.getRouteLines().get(0);
                MyToolClass.setDistance(line.getDistance());
                MyToolClass.setTime(line.getDuration());
                List<WalkingRouteLine.WalkingStep> steps = line.getAllStep();
                size0=steps.size();
                for(int i=0;i<size0;i++){
                    Log.d("wenti12",""+steps.get(i).getWayPoints().get(0));
                    points1.add(steps.get(i).getWayPoints().get(0));
                    int size2=steps.get(i).getWayPoints().size();
                    points2.add(steps.get(i).getWayPoints().get(size2-1));
                }
            }
            LatLng pt1 = new LatLng(a1, a2);
            points.add(pt1);
            for(int i=0;i<size0;i++){
                points.add(points1.get(i));
                points.add(points2.get(i));
            }
            OverlayOptions ooPolyline = new PolylineOptions()
                    .points(points)
                    .color(Color.parseColor("#A9A9A9"))
                    .width(10)
                    .dottedLine(true);
            mBaiduMap.addOverlay(ooPolyline);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        Tag1=false;
    }
}
