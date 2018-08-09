package bupt.com.bupte;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.flipboard.bottomsheet.BottomSheetLayout;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainpageActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        MyDetailFragment.OnDetailFragmentListener,MyFragment.OnMyFragmentListener,
        Fragment_explore.OnMyFragmentExpListener{//登录后的主页

    private RadioGroup navigationBar;
    private RadioButton button_check, button_explore, button_mine;
    private Fragment fragment_check, fragment_explore, fragment_mine,fragment_minetour,myFragment;
    private Fragment mFragment;
    private boolean IsStudent=false;
    private Student student=null;
    private static boolean isPermissionRequested = false;
    private int order=100;

    private long mExitTime;
    private View fragView;
    BottomSheetLayout bottomSheetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_mainpage);

        Intent intent=getIntent();
        IsStudent=intent.getBooleanExtra("IsStudent",false);//是否学生，默认false
        student=(Student)intent.getSerializableExtra("student");//学生信息，游客登录时是空的学生信息，可不用管
        MyToolClass.setName(student.getName());
        requestPermission();
        initViews();
        search_site();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.main_fragment,fragment_check).commit();
        mFragment = fragment_check;
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
                            MyToolClass.setLL(site_ll.getLatitude(),site_ll.getLongitude());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initViews() {
        navigationBar = (RadioGroup) findViewById(R.id.navigation_btn);
        button_check = (RadioButton) findViewById(R.id.check_button);
        button_explore = (RadioButton) findViewById(R.id.explore_button);
        button_mine = (RadioButton) findViewById(R.id.mine_button);
        navigationBar.setOnCheckedChangeListener(this);

        Drawable drawable2 = getResources().getDrawable(R.drawable.radio_explore_style);
        drawable2.setBounds(0, 0, 100, 78);//左上右下
        button_explore.setCompoundDrawables(null, drawable2, null, null);
        Drawable drawable3 = getResources().getDrawable(R.drawable.radio_mine_style);
        drawable3.setBounds(0, 0, 100, 78);//左上右下
        button_mine.setCompoundDrawables(null, drawable3, null, null);
        Drawable drawable1 = getResources().getDrawable(R.drawable.radio_checkin_style);
        drawable1.setBounds(0, 0, 100, 78);//左上右下
        button_check.setCompoundDrawables(null, drawable1, null, null);

        fragment_check = new Fragment_check();
        Bundle bundle_check=new Bundle();
        bundle_check.putInt("order",order);
        bundle_check.putBoolean("IsStudent",IsStudent);
        fragment_check.setArguments(bundle_check);

        fragment_explore=new Fragment_explore();
        fragment_mine = new Fragment_mine();
        fragment_minetour=new Fragment_minetour();

        Bundle bundle = new Bundle();//传递学生信息
        bundle.putString("name", student.getName());
        bundle.putInt("id", student.getId());
        bundle.putInt("sid", student.getSid());
        bundle.putInt("depmt", student.getDepmt());
        bundle.putInt("prof", student.getProf());
        bundle.putInt("building", student.getBuilding());
        bundle.putInt("room", student.getRoom());
        fragment_mine.setArguments(bundle);
        fragment_minetour.setArguments(bundle);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.check_button:
                button_check.setChecked(true);
                button_explore.setChecked(false);
                button_mine.setChecked(false);
                if (fragment_check!=null) {
                    getSupportFragmentManager().beginTransaction().remove(fragment_check).commit();
                }
                Bundle bundle = new Bundle();
                bundle.putBoolean("IsStudent", IsStudent);
                bundle.putInt("order", order);
                fragment_check = new Fragment_check();
                fragment_check.setArguments(bundle);
                if (fragment_explore!=null) {
                    getSupportFragmentManager().beginTransaction().remove(fragment_explore).commit();
                }
                switchFragment(fragment_check);
                break;
            case R.id.explore_button:
                button_check.setChecked(false);
                button_explore.setChecked(true);
                button_mine.setChecked(false);
                if (fragment_check!=null) {
                    getSupportFragmentManager().beginTransaction().remove(fragment_check).commit();
                }
                if (fragment_explore!=null) {
                    getSupportFragmentManager().beginTransaction().remove(fragment_explore).commit();
                }
                fragment_explore=new Fragment_explore();
                switchFragment(fragment_explore);
                break;
            case R.id.mine_button:
                if(IsStudent){
                    button_check.setChecked(false);
                    button_explore.setChecked(false);
                    button_mine.setChecked(true);
                    if (fragment_check!=null) {
                        getSupportFragmentManager().beginTransaction().remove(fragment_check).commit();
                    }
                    if (fragment_explore!=null) {
                        getSupportFragmentManager().beginTransaction().remove(fragment_explore).commit();
                    }
                    switchFragment(fragment_mine);
                    break;
                }else {
                    button_check.setChecked(false);
                    button_explore.setChecked(false);
                    button_mine.setChecked(true);
                    if (fragment_check!=null) {
                        getSupportFragmentManager().beginTransaction().remove(fragment_check).commit();
                    }
                    if (fragment_explore!=null) {
                        getSupportFragmentManager().beginTransaction().remove(fragment_explore).commit();
                    }
                    switchFragment(fragment_minetour);
                    break;
                }
        }
    }

    private void switchFragment(Fragment fragment) {
        if(mFragment != fragment) {
            if (!fragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().hide(mFragment)
                        .add(R.id.main_fragment, fragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().hide(mFragment).show(fragment).commit();
            }
            mFragment = fragment;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionRequested) {

            isPermissionRequested = true;

            ArrayList<String> permissions = new ArrayList<>();
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            if (permissions.size() == 0) {
                return;
            } else {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
            }
        }
    }


    @Override
    public void myFragmentInteraction(int order) {
        this.onBackPressed();
        this.order=order;
        Bundle bundle1 = new Bundle();
        bundle1.putBoolean("IsStudent",IsStudent);
        bundle1.putInt("order",order);
        getSupportFragmentManager().beginTransaction().remove(fragment_check).commit();
        fragment_check=new Fragment_check();
        fragment_check.setArguments(bundle1);
        switchFragment(fragment_check);
    }

    @Override
    public void detailFragmentInteraction() {
        this.onBackPressed();
        this.order=100;
        Bundle bundle1 = new Bundle();
        bundle1.putBoolean("IsStudent",IsStudent);
        bundle1.putInt("order",order);
        getSupportFragmentManager().beginTransaction().remove(fragment_check).commit();
        fragment_check=new Fragment_check();
        fragment_check.setArguments(bundle1);
        switchFragment(fragment_check);
    }

    @Override
    public void myFragmentExpInteraction() {
        button_check.performClick();
    }

//    @Override
//    public void onBackPressed() {
//        MyToast.makeText(MainpageActivity.this, "正在退出程序", Toast.LENGTH_SHORT).show();
//        this.finish();
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                MyToast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return true;
//        return super.onKeyDown(keyCode, event);
    }
}
