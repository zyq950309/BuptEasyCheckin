package bupt.com.bupte;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;


public class ExploreFragment extends BottomSheetDialogFragment {
    public ExploreFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private View view;
    private TextView nametext;
    private TextView notetext;
    private Button walknavbtn;
    private Button arbtn;
    //    private ImageView img1;
//    private ImageView img2;
//    private ImageView img3;
    private static final String TAG = "ExploreFragment";
    private WalkNavigateHelper mNaviHelper;

    private double a1 = 0;//目的地坐标
    private double a2 = 0;
    private double b1 = 0;//目的地坐标
    private double b2 = 0;
    private int order=0;
    private WalkNaviLaunchParam walkParam;
    private boolean Tag1;
    public LocationClient mLocationClient;//定位器
    private BDLocationListener mylistener = new MyLocationListener();//定位监听器

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    SpannableString strNav1 = new SpannableString("步行导航\n全程"+MyToolClass.getDistance1()+"米 "+MyToolClass.getTime1()+"分钟");
                    int length1 = strNav1.length();
                    strNav1.setSpan(new RelativeSizeSpan(1.2f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    strNav1.setSpan(new RelativeSizeSpan(0.8f), 5, length1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    walknavbtn.setText(strNav1);
                    break;
                case 2:
                    SpannableString strNav2 = new SpannableString("步行导航\n全程"+MyToolClass.getDistance2()+"米 "+MyToolClass.getTime2()+"分钟");
                    int length2 = strNav2.length();
                    strNav2.setSpan(new RelativeSizeSpan(1.2f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    strNav2.setSpan(new RelativeSizeSpan(0.8f), 5, length2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    walknavbtn.setText(strNav2);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_explore, container, false);
        nametext = (TextView)view.findViewById(R.id.text_name_explore);
        notetext = (TextView)view.findViewById(R.id.text_note_explore);
        walknavbtn = (Button)view.findViewById(R.id.btn_walknav_explore);
//        requestLocation();
        arbtn = (Button)view.findViewById(R.id.btn_ar_explore);
//        img1 = (ImageView)view.findViewById(R.id.image_explore1);
//        img2 = (ImageView)view.findViewById(R.id.image_explore2);
//        img3 = (ImageView)view.findViewById(R.id.image_explore3);
        SpannableString strAr = new SpannableString("AR看彩蛋\n扫一扫");
        strAr.setSpan(new RelativeSizeSpan(1.2f), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        strAr.setSpan(new RelativeSizeSpan(0.8f), 6, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        arbtn.setText(strAr);
        arbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),WalkNavActivity.class);
//                startActivity(intent);
            }
        });
        Bundle bundle = getArguments();
        int order = bundle.getInt("order_explore");
        initView(order);
        return view;
    }

    public View getView(){
        return view;
    }

    private void initLocation() {//初始化定位器
        mLocationClient = new LocationClient(getActivity());
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
            MyToast.makeText(getActivity(), "GPS Locate 失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void search_ifo(int i) {
        final int orderi=i;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (Tag1){
                    Message msg=new Message();
                    msg.what=orderi;
                    handler.sendMessage(msg);

                    try{
                        Thread.sleep(3000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onStart() {
        super.onStart();
        Tag1=true;
        search_ifo(order);
    }

    @Override
    public void onStop() {
        super.onStop();
//        Log.d("wenti","stop detail");
        Tag1=false;
    }

    private void initView(int order){
//        将实例化控件部分放在initView中仍然没有效果
        switch(order){
            case 1:
                nametext.setText("南门");
                notetext.setText("沙河校区彩蛋一");

                SpannableString strNav = new SpannableString("步行导航\n全程"+MyToolClass.getDistance1()+"米 "+MyToolClass.getTime1()+"分钟");
                int length = strNav.length();
                strNav.setSpan(new RelativeSizeSpan(1.2f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                strNav.setSpan(new RelativeSizeSpan(0.8f), 5, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                walknavbtn.setText(strNav);
                walknavbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        b1=40.163299;
//                        b2=116.290664;
                        b1=28.421957;
                        b2=117.608362;
                        requestLocation();
                    }
                });

//                img1.setImageResource(R.drawable.timg3);
//                img2.setImageResource(R.drawable.timg2);
//                img3.setImageResource(R.drawable.timg1);
                break;
            case 2:
                nametext.setText("大礼堂");
                notetext.setText("沙河校区彩蛋二");

                SpannableString strNav2 = new SpannableString("步行导航\n全程"+MyToolClass.getDistance2()+"米 "+MyToolClass.getTime2()+"分钟");
                int length2 = strNav2.length();
                strNav2.setSpan(new RelativeSizeSpan(1.2f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                strNav2.setSpan(new RelativeSizeSpan(0.8f), 5, length2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                walknavbtn.setText(strNav2);
                walknavbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        b1=40.164519;
//                        b2=116.296436;
                        b1=28.421957;
                        b2=117.608362;
                        requestLocation();
                    }
                });

//                img1.setImageResource(R.drawable.timg3);
//                img2.setImageResource(R.drawable.timg2);
//                img3.setImageResource(R.drawable.timg1);
                break;
            default:
                break;
        }
    }

    private void startWalkNavi() {
        try {
            mNaviHelper.initNaviEngine(getActivity(), new IWEngineInitListener() {
                @Override
                public void engineInitSuccess() {
                    routePlanWithWalkParam();
                }

                @Override
                public void engineInitFail() {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void routePlanWithWalkParam() {
        mNaviHelper.routePlanWithParams(walkParam, new IWRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {

            }

            @Override
            public void onRoutePlanSuccess() {
                Intent intent = new Intent(getActivity(), MaprouteActivity.class);
                intent.putExtra("b1", b1);
                intent.putExtra("b2", b2);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError error) {

            }
        });
    }

    public class MyLocationListener implements BDLocationListener {//定位监听

        @Override
        public void onReceiveLocation(BDLocation location) {
            a1=location.getLatitude();
            a2=location.getLongitude();
            Log.d("wenti","b1"+b1);
            LatLng startPt = new LatLng(a1, a2);
            LatLng endPt = new LatLng(b1, b2);
            walkParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
            try {
                mNaviHelper = WalkNavigateHelper.getInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            startWalkNavi();
            mLocationClient.stop();
        }
    }

}
