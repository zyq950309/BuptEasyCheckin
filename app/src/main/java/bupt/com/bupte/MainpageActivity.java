package bupt.com.bupte;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.baidu.mapapi.SDKInitializer;

import java.util.ArrayList;

public class MainpageActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,MyDetailFragment.OnDetailFragmentListener,MyFragment.OnMyFragmentListener{//登录后的主页

    private RadioGroup navigationBar;
    private RadioButton button_check, button_explore, button_mine;
    private Fragment fragment_check, fragment_explore, fragment_mine,fragment_minetour;
    private Fragment mFragment;
    private boolean IsStudent=false;
    private Student student=null;
    private static boolean isPermissionRequested = false;
    private int order=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_mainpage);

        Intent intent=getIntent();
        IsStudent=intent.getBooleanExtra("IsStudent",false);//是否学生，默认false
        student=(Student)intent.getSerializableExtra("student");//学生信息，游客登录时是空的学生信息，可不用管
        requestPermission();
        initViews();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.main_fragment,fragment_check).commit();
        mFragment = fragment_check;
    }

    private void initViews() {
        navigationBar = (RadioGroup) findViewById(R.id.navigation_btn);
        button_check = (RadioButton) findViewById(R.id.check_button);
        button_explore = (RadioButton) findViewById(R.id.explore_button);
        button_mine = (RadioButton) findViewById(R.id.mine_button);
        navigationBar.setOnCheckedChangeListener(this);

        fragment_check = new Fragment_check();
        fragment_explore = new Fragment_explore();
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

        Bundle bundle1 = new Bundle();
        bundle1.putInt("order",0);
        fragment_check.setArguments(bundle1);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.check_button:
                button_check.setChecked(true);
                button_explore.setChecked(false);
                button_mine.setChecked(false);
                switchFragment(fragment_check);
                break;
            case R.id.explore_button:
                button_check.setChecked(false);
                button_explore.setChecked(true);
                button_mine.setChecked(false);
                switchFragment(fragment_explore);
                break;
            case R.id.mine_button:
                if(IsStudent){
                    button_check.setChecked(false);
                    button_explore.setChecked(false);
                    button_mine.setChecked(true);
                    switchFragment(fragment_mine);
                    break;
                }else {
                    button_check.setChecked(false);
                    button_explore.setChecked(false);
                    button_mine.setChecked(true);
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
        }else {
            getSupportFragmentManager().beginTransaction().hide(mFragment).show(fragment).commit();
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

            if (permissions.size() == 0) {
                return;
            } else {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
            }
        }
    }

//    @Override
//    public void myFragmentInteraction(int order) {
//        this.order=order;
//        Bundle bundle1 = new Bundle();
//        bundle1.putInt("order",order);
//        getSupportFragmentManager().beginTransaction().remove(fragment_check).commit();
//        fragment_check.onDestroy();
//        fragment_check=new Fragment_check();
//        fragment_check.setArguments(bundle1);
//        switchFragment(fragment_check);
//    }

    @Override
    public void myFragmentInteraction(int order) {
        MyDetailFragment frag = new MyDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("order",order);
        frag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag).addToBackStack(null)
                .commit();
    }

    @Override
    public void detailFragmentInteraction() {
        MyFragment frag = new MyFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag).addToBackStack(null)
                .commit();
    }
}
