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

import com.flipboard.bottomsheet.commons.BottomSheetFragment;

import static bupt.com.bupte.MainActivity.JiaoSan;
import static bupt.com.bupte.MainActivity.KeYan;


public class ExploreFragment extends BottomSheetFragment {
    private View view;
    private TextView nametext;
    private TextView notetext;
    private TextView detailtext;
    private Button walknavbtn;
    private Button arbtn;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private TextView manyttext;
    private static final String TAG = "ExploreFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_explore, container, false);
        nametext = (TextView)view.findViewById(R.id.text_name_explore);
        notetext = (TextView)view.findViewById(R.id.text_note_explore);
        detailtext = (TextView)view.findViewById(R.id.text_detail_explore);
        walknavbtn = (Button)view.findViewById(R.id.btn_walknav_explore);
        arbtn = (Button)view.findViewById(R.id.btn_ar_explore);
        img1 = (ImageView)view.findViewById(R.id.image_explore1);
        img2 = (ImageView)view.findViewById(R.id.image_explore2);
        img3 = (ImageView)view.findViewById(R.id.image_explore3);
        manyttext =(TextView)view.findViewById(R.id.text_many_explore);
        SpannableString strAr = new SpannableString("AR看彩蛋\n扫一扫");
        strAr.setSpan(new RelativeSizeSpan(1.2f), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        strAr.setSpan(new RelativeSizeSpan(0.8f), 6, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        arbtn.setText(strAr);
        arbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ARrouteActivity.class);
                startActivity(intent);
            }
        });
        Bundle bundle = getArguments();
        int order = bundle.getInt("order_explore");
        Log.i(TAG, "onCreateView: "+order);
        initView(order);
        return view;
    }

    public View getView(){
        return view;
    }
    private void initView(int order){
//        将实例化控件部分放在initView中仍然没有效果
        switch(order){
            case JiaoSan:
                Log.i(TAG, "initView: 教三");
                nametext.setText("教三楼");
                notetext.setText("沙河校区彩蛋一");
                detailtext.setText("建筑物说明。。。。。。。。。。。。");

                SpannableString strNav = new SpannableString("步行导航\n全程"+MyToolClass.getDistance(1)+"米 "+MyToolClass.getTime(1)+"分钟");
                int length = strNav.length();
                strNav.setSpan(new RelativeSizeSpan(1.2f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                strNav.setSpan(new RelativeSizeSpan(0.8f), 5, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                walknavbtn.setText(strNav);
                walknavbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),ARrouteActivity.class);
                        startActivity(intent);
                    }
                });

                img1.setImageResource(R.drawable.timg3);
                img2.setImageResource(R.drawable.timg2);
                img3.setImageResource(R.drawable.timg1);
                manyttext.setText("具体流程如下");
                break;
            case KeYan:
                Log.i(TAG, "initView: 科研");
                nametext.setText("科研楼");
                notetext.setText("沙河校区彩蛋二");
                detailtext.setText("建筑物说明。。。。。。。。。。。。");

                SpannableString strNav2 = new SpannableString("步行导航\n全程"+MyToolClass.getDistance(1)+"米 "+MyToolClass.getTime(1)+"分钟");
                int length2 = strNav2.length();
                strNav2.setSpan(new RelativeSizeSpan(1.2f), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                strNav2.setSpan(new RelativeSizeSpan(0.8f), 5, length2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                walknavbtn.setText(strNav2);
                walknavbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),ARrouteActivity.class);
                        startActivity(intent);
                    }
                });

                img1.setImageResource(R.drawable.timg3);
                img2.setImageResource(R.drawable.timg2);
                img3.setImageResource(R.drawable.timg1);
                manyttext.setText("具体流程如下");
                break;
            default:
                break;
        }
    }

}
