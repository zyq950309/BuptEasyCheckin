package bupt.com.bupte;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
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
import com.flipboard.bottomsheet.commons.BottomSheetFragment;


public class MyDetailFragment extends BottomSheetFragment {
    private View view;
    private ImageView closebtn;
    private TextView ordertext;
    private TextView nametext;
    private TextView detailtext;
    private TextView inlinetext;
    private Button walknavbtn;
    private Button arbtn;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private TextView manyttext;
    private OnDetailFragmentListener mdListener;

    private double a1 = 0;//当前位置坐标
    private double a2 = 0;
    private double b1 = 0;//目的地坐标
    private double b2 = 0;
    private WalkNavigateHelper mNaviHelper;
    private LatLng startPt, endPt;
    private WalkNaviLaunchParam walkParam;
    public LocationClient mLocationClient;//定位器
    private BDLocationListener mylistener = new MyDetailFragment.MyLocationListener();//定位监听器
    private boolean Tag1=false;
    private int order=0;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    SpannableString strNav1 = new SpannableString("步行导航\n全程"+MyToolClass.getDistance()+"米 "+MyToolClass.getTime()+"分钟");
                    int length1 = strNav1.length();
                    strNav1.setSpan(new RelativeSizeSpan(1.2f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    strNav1.setSpan(new RelativeSizeSpan(0.8f), 5, length1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    walknavbtn.setText(strNav1);
                    break;
                case 2:
                    SpannableString strNav2 = new SpannableString("步行导航\n全程"+MyToolClass.getDistance()+"米 "+MyToolClass.getTime()+"分钟");
                    int length2 = strNav2.length();
                    strNav2.setSpan(new RelativeSizeSpan(1.2f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    strNav2.setSpan(new RelativeSizeSpan(0.8f), 5, length2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    walknavbtn.setText(strNav2);
                    break;
                case 3:
                    SpannableString strNav3 = new SpannableString("步行导航\n全程"+MyToolClass.getDistance()+"米 "+MyToolClass.getTime()+"分钟");
                    int length3 = strNav3.length();
                    strNav3.setSpan(new RelativeSizeSpan(1.2f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    strNav3.setSpan(new RelativeSizeSpan(0.8f), 5, length3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    walknavbtn.setText(strNav3);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_my, container, false);
        closebtn = (ImageView)view.findViewById(R.id.close_btn);
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdListener.detailFragmentInteraction();
            }
        });
        ordertext = (TextView)view.findViewById(R.id.text_order1);
        nametext = (TextView)view.findViewById(R.id.text_name1);
        detailtext = (TextView)view.findViewById(R.id.text_detail1);
        inlinetext = (TextView)view.findViewById(R.id.text_inLine1);
        walknavbtn = (Button)view.findViewById(R.id.btn_walknav);
        arbtn = (Button)view.findViewById(R.id.btn_ar);
        img1 = (ImageView)view.findViewById(R.id.image_1);
        img2 = (ImageView)view.findViewById(R.id.image_2);
        img3 = (ImageView)view.findViewById(R.id.image_3);
        manyttext =(TextView)view.findViewById(R.id.text_many);

        Bundle bundle = getArguments();
        order = bundle.getInt("order");
        initView(order);
        requestLocation();
        return view;
    }

    public interface OnDetailFragmentListener{
        void detailFragmentInteraction();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailFragmentListener) {
            mdListener = (OnDetailFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDetailFragmentListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Tag1=true;
        search_ifo(order);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mdListener = null;
    }

    private void routePlanWithWalkParam() {
        mNaviHelper.routePlanWithParams(walkParam, new IWRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {

            }

            @Override
            public void onRoutePlanSuccess() {
                Intent intent = new Intent(getActivity(), MaprouteActivity.class);
                intent.putExtra("b1",b1);
                intent.putExtra("b2",b2);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError error) {

            }
        });
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

    public class MyLocationListener implements BDLocationListener {//定位监听

        @Override
        public void onReceiveLocation(BDLocation location) {
            a1=location.getLatitude();
            a2=location.getLongitude();
        }
    }

    private void initView(int order){
        inlinetext.setText(MyToolClass.getInLineNumbers(order));
        switch(order){
            case 1:
                search_ifo(order);
                ordertext.setText("1");
                nametext.setText("报到大厅(图书馆一层/马路上)");
                detailtext.setText("参照通知书带齐所需证件");
//                SpannableString strNav = new SpannableString("步行导航\n全程"+MyToolClass.getDistance(1)+"米 "+MyToolClass.getTime(1)+"分钟");
//                int length = strNav.length();
//                strNav.setSpan(new RelativeSizeSpan(1.2f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                strNav.setSpan(new RelativeSizeSpan(0.8f), 5, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                walknavbtn.setText(strNav);
                walknavbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("wentirrr",""+MyToolClass.getDistance());
                        if(MyToolClass.getDistance()<30) {
                            MyToast.makeText(getActivity(), "距离太近，无法发起导航", Toast.LENGTH_SHORT).show();
                        }else {
                            b1 = 39.967113916777636;
                            b2 = 116.36479162025452;
                            startPt = new LatLng(a1, a2);
                            endPt = new LatLng(b1, b2);
                            walkParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
                            try {
                                mNaviHelper = WalkNavigateHelper.getInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            startWalkNavi();
                        }
                    }
                });

                SpannableString strAr = new SpannableString("室内AR指引\n5处手续办理");
                strAr.setSpan(new RelativeSizeSpan(1.2f), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                strAr.setSpan(new RelativeSizeSpan(0.8f), 7, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                arbtn.setText(strAr);
                arbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(getActivity(),MaprouteActivity.class);
//                        startActivity(intent);
                    }
                });
                img1.setImageResource(R.drawable.timg3);
                img2.setImageResource(R.drawable.timg2);
                img3.setImageResource(R.drawable.timg1);
                manyttext.setText("报到大厅内手续办理：\n" +
                        "1. 学院报到\n" +
                        "2. 缴纳学费\n" +
                        "3. 办理贷款\n" +
                        "4. 转组织关系");
                break;
            case 2:
                search_ifo(order);
                ordertext.setText("2");
                nametext.setText("宿舍");
                detailtext.setText("提交缴费入住");

//                SpannableString strNav2 = new SpannableString("步行导航\n全程"+MyToolClass.getDistance(1)+"米 "+MyToolClass.getTime(1)+"分钟");
//                int length2 = strNav2.length();
//                strNav2.setSpan(new RelativeSizeSpan(1.2f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                strNav2.setSpan(new RelativeSizeSpan(0.8f), 5, length2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                walknavbtn.setText(strNav2);
                walknavbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(MyToolClass.getDistance()<30) {
                            MyToast.makeText(getActivity(), "距离太近，无法发起导航", Toast.LENGTH_SHORT).show();
                        }else {
                            b1 = 39.967113916777636;
                            b2 = 116.36479162025452;
                            startPt = new LatLng(a1, a2);
                            endPt = new LatLng(b1, b2);
                            walkParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
                            try {
                                mNaviHelper = WalkNavigateHelper.getInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            startWalkNavi();
                        }
                    }
                });

                SpannableString strAr2 = new SpannableString("室内AR指引\n5处手续办理");
                strAr2.setSpan(new RelativeSizeSpan(1.2f), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                strAr2.setSpan(new RelativeSizeSpan(0.8f), 7, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                arbtn.setText(strAr2);
                arbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(getActivity(),MaprouteActivity.class);
//                        startActivity(intent);
                    }
                });
                img1.setImageResource(R.drawable.timg3);
                img2.setImageResource(R.drawable.timg2);
                img3.setImageResource(R.drawable.timg1);
                manyttext.setText("报到大厅内手续办理：\n" +
                        "1. 学院报到\n" +
                        "2. 缴纳学费\n" +
                        "3. 办理贷款\n" +
                        "4. 转组织关系");
                break;
            case 3:
                search_ifo(order);
                ordertext.setText("3");
                nametext.setText("体检车");
                detailtext.setText("体检");
//
//                SpannableString strNav3 = new SpannableString("步行导航\n全程"+MyToolClass.getDistance(1)+"米 "+MyToolClass.getTime(1)+"分钟");
//                int length3 = strNav3.length();
//                strNav3.setSpan(new RelativeSizeSpan(1.2f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                strNav3.setSpan(new RelativeSizeSpan(0.8f), 5, length3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                walknavbtn.setText(strNav3);
                walknavbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(MyToolClass.getDistance()<30) {
                            MyToast.makeText(getActivity(), "距离太近，无法发起导航", Toast.LENGTH_SHORT).show();
                        }else {
                            b1 = 39.967113916777636;
                            b2 = 116.36479162025452;
                            startPt = new LatLng(a1, a2);
                            endPt = new LatLng(b1, b2);
                            walkParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
                            try {
                                mNaviHelper = WalkNavigateHelper.getInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            startWalkNavi();
                        }
                    }
                });

                SpannableString strAr3 = new SpannableString("室内AR指引\n5处手续办理");
                strAr3.setSpan(new RelativeSizeSpan(1.2f), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                strAr3.setSpan(new RelativeSizeSpan(0.8f), 7, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                arbtn.setText(strAr3);
                arbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(getActivity(),MaprouteActivity.class);
//                        startActivity(intent);
                    }
                });
                img1.setImageResource(R.drawable.timg3);
                img2.setImageResource(R.drawable.timg2);
                img3.setImageResource(R.drawable.timg1);
                manyttext.setText("报到大厅内手续办理：\n" +
                        "1. 学院报到\n" +
                        "2. 缴纳学费\n" +
                        "3. 办理贷款\n" +
                        "4. 转组织关系");
                break;
            default:
                break;
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
    public void onStop() {
        super.onStop();
//        Log.d("wenti","stop detail");
        Tag1=false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.d("wenti","destroy detail");
    }
}
