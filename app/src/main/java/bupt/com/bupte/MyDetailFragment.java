package bupt.com.bupte;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    //    private TextView inlinetext;
    private Button walknavbtn;
    private Button arbtn;
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
    private int Tag3=1;

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
//        inlinetext = (TextView)view.findViewById(R.id.text_inLine1);
        walknavbtn = (Button)view.findViewById(R.id.btn_walknav);
        arbtn = (Button)view.findViewById(R.id.btn_ar);
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
//                Log.d("wentid","?");
                if(Tag3==1) {
                    Intent intent = new Intent(getActivity(), MaprouteActivity.class);
                    intent.putExtra("b1", b1);
                    intent.putExtra("b2", b2);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), ARrouteActivity.class);
                    intent.putExtra("b1", b1);
                    intent.putExtra("b2", b2);
                    startActivity(intent);
                }
            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError error) {
//                Log.d("wentid",error.name());
            }
        });
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
//        inlinetext.setText(MyToolClass.getInLineNumbers(order));
        switch(order){
            case 1:
                search_ifo(order);
                ordertext.setText("1");
                nametext.setText("新生报到处(图书馆一层小广场)");
                detailtext.setText("参照通知书带齐所需证件");
                walknavbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(MyToolClass.getDistance()<30) {
                            MyToast.makeText(getActivity(), "距离太近，无法发起导航", Toast.LENGTH_SHORT).show();
                        }else if(MyToolClass.getDistance()>10000) {
                            MyToast.makeText(getActivity(), "距离太远，无法发起导航", Toast.LENGTH_SHORT).show();
                        } else {
                            Tag3=1;
//                            b1 = 39.967113916777636;
//                            b2 = 116.36479162025452;
                            b1=Double.parseDouble(MyToolClass.getLatitude().get(0));
                            b2=Double.parseDouble(MyToolClass.getLongitude().get(0));
//                            b1=28.421957;
//                            b2=117.608362;
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

                SpannableString strAr = new SpannableString("室内AR指引\n4处手续办理");
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
                manyttext.setText("报道大厅内手续办理：\n" +
                        "1. 学院报道\n" +
                        "2. 缴纳学费\n" +
                        "3. 办理贷款\n" +
                        "4. 转组织关系");
                break;
            case 2:
                search_ifo(order);
                ordertext.setText("2");
                nametext.setText("宿舍");
                detailtext.setText("点击对应楼号，发起导航");
                arbtn.setVisibility(View.GONE);
                walknavbtn.setVisibility(View.GONE);
                manyttext.setVisibility(View.GONE);
                RelativeLayout relativeLayout = (RelativeLayout)view.findViewById(R.id.detailfragment);
                LinearLayout ll = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.apartment1,null);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.RIGHT_OF,R.id.text_order1);
                lp.addRule(RelativeLayout.BELOW,R.id.text_detail1);
                lp.setMargins(0,20,0,20);
                relativeLayout.addView(ll,lp);
                ImageButton btnA = (ImageButton) view.findViewById(R.id.btnA);
                btnA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getActivity(),"you clicked ap!",Toast.LENGTH_SHORT).show();
                        Tag3 = 2;
                        b1 = 40.164675;
                        b2 = 116.294560;
                        startPt = new LatLng(a1, a2);
                        endPt = new LatLng(b1, b2);
                        if (GetJuLi(a1, a2, b1, b2) < 10000 && GetJuLi(a1, a2, b1, b2)>20) {
                            walkParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
                            try {
                                mNaviHelper = WalkNavigateHelper.getInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            startWalkNavi();

                        } else if(GetJuLi(a1, a2, b1, b2)>=10000){
                            MyToast.makeText(getActivity(), "距离太远，无法发起导航", Toast.LENGTH_SHORT).show();
                        }else {
                            MyToast.makeText(getActivity(), "距离太近，无法发起导航", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ImageButton btnB = (ImageButton) view.findViewById(R.id.btnB);
                btnB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getActivity(),"you clicked bp!",Toast.LENGTH_SHORT).show();
                        Tag3=2;
                        b1=40.164675;
                        b2=116.294304;
                        startPt = new LatLng(a1, a2);
                        endPt = new LatLng(b1, b2);
                        if (GetJuLi(a1, a2, b1, b2) < 10000 && GetJuLi(a1, a2, b1, b2)>20) {
                            walkParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
                            try {
                                mNaviHelper = WalkNavigateHelper.getInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            startWalkNavi();
                        } else if(GetJuLi(a1, a2, b1, b2)>=10000){
                            MyToast.makeText(getActivity(), "距离太远，无法发起导航", Toast.LENGTH_SHORT).show();
                        }else {
                            MyToast.makeText(getActivity(), "距离太近，无法发起导航", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ImageButton btnC = (ImageButton) view.findViewById(R.id.btnC);
                btnC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getActivity(),"you clicked cp!",Toast.LENGTH_SHORT).show();
                        Tag3=2;
                        b1=40.165281;
                        b2=116.29492;
                        startPt = new LatLng(a1, a2);
                        endPt = new LatLng(b1, b2);
                        if (GetJuLi(a1, a2, b1, b2) < 10000 && GetJuLi(a1, a2, b1, b2)>20) {
                            walkParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
                            try {
                                mNaviHelper = WalkNavigateHelper.getInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            startWalkNavi();
                        } else if(GetJuLi(a1, a2, b1, b2)>=10000){
                            MyToast.makeText(getActivity(), "距离太远，无法发起导航", Toast.LENGTH_SHORT).show();
                        }else {
                            MyToast.makeText(getActivity(), "距离太近，无法发起导航", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ImageButton btnD1 = (ImageButton) view.findViewById(R.id.btnD1);
                btnD1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getActivity(),"you clicked dp1!",Toast.LENGTH_SHORT).show();
                        Tag3=2;
                        b1=40.164719;
                        b2=116.295167;
                        startPt = new LatLng(a1, a2);
                        endPt = new LatLng(b1, b2);
                        if (GetJuLi(a1, a2, b1, b2) < 10000 && GetJuLi(a1, a2, b1, b2)>20) {
                            walkParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
                            try {
                                mNaviHelper = WalkNavigateHelper.getInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            startWalkNavi();
                        } else if(GetJuLi(a1, a2, b1, b2)>=10000){
                            MyToast.makeText(getActivity(), "距离太远，无法发起导航", Toast.LENGTH_SHORT).show();
                        }else {
                            MyToast.makeText(getActivity(), "距离太近，无法发起导航", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ImageButton btnD2 = (ImageButton) view.findViewById(R.id.btnD2);
                btnD2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getActivity(),"you clicked dp2!",Toast.LENGTH_SHORT).show();
                        Tag3=2;
                        b1=40.165774;
                        b2=116.294812;
                        startPt = new LatLng(a1, a2);
                        endPt = new LatLng(b1, b2);
                        if (GetJuLi(a1, a2, b1, b2) < 10000 && GetJuLi(a1, a2, b1, b2)>20) {
                            walkParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
                            try {
                                mNaviHelper = WalkNavigateHelper.getInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            startWalkNavi();
                        } else if(GetJuLi(a1, a2, b1, b2)>=10000){
                            MyToast.makeText(getActivity(), "距离太远，无法发起导航", Toast.LENGTH_SHORT).show();
                        }else {
                            MyToast.makeText(getActivity(), "距离太近，无法发起导航", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ImageButton btnE = (ImageButton) view.findViewById(R.id.btnE);
                btnE.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getActivity(),"you clicked ep!",Toast.LENGTH_SHORT).show();
                        Tag3=2;
                        b1=40.16545;
                        b2=116.293891;
                        startPt = new LatLng(a1, a2);
                        endPt = new LatLng(b1, b2);
                        if (GetJuLi(a1, a2, b1, b2) < 10000 && GetJuLi(a1, a2, b1, b2)>20) {
                            walkParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
                            try {
                                mNaviHelper = WalkNavigateHelper.getInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            startWalkNavi();
                        } else if(GetJuLi(a1, a2, b1, b2)>=10000){
                            MyToast.makeText(getActivity(), "距离太远，无法发起导航", Toast.LENGTH_SHORT).show();
                        }else {
                            MyToast.makeText(getActivity(), "距离太近，无法发起导航", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ImageButton btnS2 = (ImageButton) view.findViewById(R.id.btnS2);
                btnS2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getActivity(),"you clicked sp2!",Toast.LENGTH_SHORT).show();
                        Tag3=2;
                        b1=40.163982;
                        b2=116.295699;
                        startPt = new LatLng(a1, a2);
                        endPt = new LatLng(b1, b2);
                        if (GetJuLi(a1, a2, b1, b2) < 10000 && GetJuLi(a1, a2, b1, b2)>20) {
                            walkParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
                            try {
                                mNaviHelper = WalkNavigateHelper.getInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            startWalkNavi();
                        } else if(GetJuLi(a1, a2, b1, b2)>=10000){
                            MyToast.makeText(getActivity(), "距离太远，无法发起导航", Toast.LENGTH_SHORT).show();
                        }else {
                            MyToast.makeText(getActivity(), "距离太近，无法发起导航", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ImageButton btnS3 = (ImageButton) view.findViewById(R.id.btnS3);
                btnS3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getActivity(),"you clicked sp3!",Toast.LENGTH_SHORT).show();
                        Tag3=2;
                        b1=40.163492;
                        b2=116.295968;
                        startPt = new LatLng(a1, a2);
                        endPt = new LatLng(b1, b2);
                        if (GetJuLi(a1, a2, b1, b2) < 10000 && GetJuLi(a1, a2, b1, b2)>20) {
                            walkParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
                            try {
                                mNaviHelper = WalkNavigateHelper.getInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            startWalkNavi();
                        } else if(GetJuLi(a1, a2, b1, b2)>=10000){
                            MyToast.makeText(getActivity(), "距离太远，无法发起导航", Toast.LENGTH_SHORT).show();
                        }else {
                            MyToast.makeText(getActivity(), "距离太近，无法发起导航", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ImageButton btnS4 = (ImageButton) view.findViewById(R.id.btnS4);
                btnS4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getActivity(),"you clicked sp4!",Toast.LENGTH_SHORT).show();
                        Tag3=2;
                        b1=40.163251;
                        b2=116.296238;
                        startPt = new LatLng(a1, a2);
                        endPt = new LatLng(b1, b2);
                        if (GetJuLi(a1, a2, b1, b2) < 10000 && GetJuLi(a1, a2, b1, b2)>20) {
                            walkParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
                            try {
                                mNaviHelper = WalkNavigateHelper.getInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            startWalkNavi();
                        } else if(GetJuLi(a1, a2, b1, b2)>=10000){
                            MyToast.makeText(getActivity(), "距离太远，无法发起导航", Toast.LENGTH_SHORT).show();
                        }else {
                            MyToast.makeText(getActivity(), "距离太近，无法发起导航", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ImageButton btnS5 = (ImageButton) view.findViewById(R.id.btnS5);
                btnS5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getActivity(),"you clicked sp5!",Toast.LENGTH_SHORT).show();
                        Tag3=2;
                        b1=40.162955;
                        b2=116.296337;
                        startPt = new LatLng(a1, a2);
                        endPt = new LatLng(b1, b2);
                        if (GetJuLi(a1, a2, b1, b2) < 10000 && GetJuLi(a1, a2, b1, b2)>20) {
                            walkParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
                            try {
                                mNaviHelper = WalkNavigateHelper.getInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            startWalkNavi();
                        } else if(GetJuLi(a1, a2, b1, b2)>=10000){
                            MyToast.makeText(getActivity(), "距离太远，无法发起导航", Toast.LENGTH_SHORT).show();
                        }else {
                            MyToast.makeText(getActivity(), "距离太近，无法发起导航", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case 3:
                search_ifo(order);
                ordertext.setText("3");
                nametext.setText("体检");
                detailtext.setText("图书馆附近体检");
                arbtn.setVisibility(View.GONE);
                walknavbtn.setVisibility(View.GONE);
                manyttext.setText("体检地点：\n" +
                        "教学楼北楼(图书馆南边)一层、二层\n" +
                        "胸部X线检查:\n" +
                        "图书馆前, 移动放射车处\n");
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
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
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
