package bupt.com.bupte;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
    private TextView notetext;
    private TextView detailtext;
    private TextView inlinetext;
    private Button walknavbtn;
    private Button arbtn;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private TextView manyttext;
    private ImageView flowchart;
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
        notetext = (TextView)view.findViewById(R.id.text_note1);
        detailtext = (TextView)view.findViewById(R.id.text_detail1);
        inlinetext = (TextView)view.findViewById(R.id.text_inLine1);
        walknavbtn = (Button)view.findViewById(R.id.btn_walknav);
        arbtn = (Button)view.findViewById(R.id.btn_ar);
        img1 = (ImageView)view.findViewById(R.id.image_1);
        img2 = (ImageView)view.findViewById(R.id.image_2);
        img3 = (ImageView)view.findViewById(R.id.image_3);
        manyttext =(TextView)view.findViewById(R.id.text_many);
        flowchart =(ImageView)view.findViewById(R.id.img_flowchart);

        Bundle bundle = getArguments();
        int order = bundle.getInt("order");

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
            Toast.makeText(getActivity(), "GPS Locate 失败", Toast.LENGTH_SHORT).show();
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
                ordertext.setText("1");
                nametext.setText("体育馆");
                notetext.setText("资料提交");
                detailtext.setText("需：身份证复印件(1份) 录取通知书复印件(1份)");

                SpannableString strNav = new SpannableString("步行导航\n全程"+MyToolClass.getDistance(1)+"米 "+MyToolClass.getTime(1)+"分钟");
                int length = strNav.length();
                strNav.setSpan(new RelativeSizeSpan(1.2f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                strNav.setSpan(new RelativeSizeSpan(0.8f), 5, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                walknavbtn.setText(strNav);
                walknavbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

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
                flowchart.setImageResource(R.drawable.flow);
                manyttext.setText("具体流程如下");
                break;
            case 2:
                ordertext.setText("2");
                nametext.setText("校医院");
                notetext.setText("提交缴费");
                detailtext.setText("需：身份证复印件(1份) 录取通知书复印件(1份)");

                SpannableString strNav2 = new SpannableString("步行导航\n全程"+MyToolClass.getDistance(1)+"米 "+MyToolClass.getTime(1)+"分钟");
                int length2 = strNav2.length();
                strNav2.setSpan(new RelativeSizeSpan(1.2f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                strNav2.setSpan(new RelativeSizeSpan(0.8f), 5, length2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                walknavbtn.setText(strNav2);
                walknavbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                flowchart.setImageResource(R.drawable.flow);
                manyttext.setText("具体流程如下");
                break;
            case 3:
                ordertext.setText("3");
                nametext.setText("行政楼");
                notetext.setText("资料提交");
                detailtext.setText("需：身份证复印件(1份) 录取通知书复印件(1份)");

                SpannableString strNav3 = new SpannableString("步行导航\n全程"+MyToolClass.getDistance(1)+"米 "+MyToolClass.getTime(1)+"分钟");
                int length3 = strNav3.length();
                strNav3.setSpan(new RelativeSizeSpan(1.2f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                strNav3.setSpan(new RelativeSizeSpan(0.8f), 5, length3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                walknavbtn.setText(strNav3);
                walknavbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                flowchart.setImageResource(R.drawable.flow);
                manyttext.setText("具体流程如下");
                break;
            case 4:
                ordertext.setText("4");
                nametext.setText("教学楼");
                notetext.setText("资料提交");
                detailtext.setText("需：身份证复印件(1份) 录取通知书复印件(1份)");

                SpannableString strNav4 = new SpannableString("步行导航\n全程"+MyToolClass.getDistance(1)+"米 "+MyToolClass.getTime(1)+"分钟");
                int length4 = strNav4.length();
                strNav4.setSpan(new RelativeSizeSpan(1.2f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                strNav4.setSpan(new RelativeSizeSpan(0.8f), 5, length4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                walknavbtn.setText(strNav4);
                walknavbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                });

                SpannableString strAr4 = new SpannableString("室内AR指引\n5处手续办理");
                strAr4.setSpan(new RelativeSizeSpan(1.2f), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                strAr4.setSpan(new RelativeSizeSpan(0.8f), 7, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                arbtn.setText(strAr4);
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
                flowchart.setImageResource(R.drawable.flow);
                manyttext.setText("具体流程如下");
                break;
            case 5:
                ordertext.setText("5");
                nametext.setText("宿舍楼");
                notetext.setText("资料提交");
                detailtext.setText("需：身份证复印件(1份) 录取通知书复印件(1份)");

                SpannableString strNav5 = new SpannableString("步行导航\n全程"+MyToolClass.getDistance(1)+"米 "+MyToolClass.getTime(1)+"分钟");
                int length5 = strNav5.length();
                strNav5.setSpan(new RelativeSizeSpan(1.2f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                strNav5.setSpan(new RelativeSizeSpan(0.8f), 5, length5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                walknavbtn.setText(strNav5);
                walknavbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                });

                SpannableString strAr5 = new SpannableString("室内AR指引\n5处手续办理");
                strAr5.setSpan(new RelativeSizeSpan(1.2f), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                strAr5.setSpan(new RelativeSizeSpan(0.8f), 7, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                arbtn.setText(strAr5);
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
                flowchart.setImageResource(R.drawable.flow);
                manyttext.setText("具体流程如下");
                break;
            case 6:
                ordertext.setText("6");
                nametext.setText("行政楼");
                notetext.setText("资料提交");
                detailtext.setText("需：身份证复印件(1份) 录取通知书复印件(1份)");

                SpannableString strNav6 = new SpannableString("步行导航\n全程"+MyToolClass.getDistance(1)+"米 "+MyToolClass.getTime(1)+"分钟");
                int length6 = strNav6.length();
                strNav6.setSpan(new RelativeSizeSpan(1.2f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                strNav6.setSpan(new RelativeSizeSpan(0.8f), 5, length6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                walknavbtn.setText(strNav6);
                walknavbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                });

                SpannableString strAr6 = new SpannableString("室内AR指引\n5处手续办理");
                strAr6.setSpan(new RelativeSizeSpan(1.2f), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                strAr6.setSpan(new RelativeSizeSpan(0.8f), 7, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                arbtn.setText(strAr6);
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
                flowchart.setImageResource(R.drawable.flow);
                manyttext.setText("具体流程如下");
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.d("wemti","destroy detail");
    }
}
