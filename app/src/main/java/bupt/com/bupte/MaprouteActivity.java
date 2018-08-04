package bupt.com.bupte;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mapapi.search.core.PriceInfo;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWNaviStatusListener;
import com.baidu.mapapi.walknavi.adapter.IWRouteGuidanceListener;
import com.baidu.mapapi.walknavi.adapter.IWTTSPlayer;
import com.baidu.mapapi.walknavi.model.RouteGuideKind;
import com.baidu.platform.comapi.walknavi.WalkNaviModeSwitchListener;
import com.baidu.platform.comapi.walknavi.widget.ArCameraView;

public class MaprouteActivity extends Activity implements View.OnClickListener{//地图导航功能页面

    private WalkNavigateHelper mNaviHelper;
    private LinearLayout mapLayout;
    private int Tag=0;
    private double b1=39.967113916777636;
    private double b2=116.36479162025452;
    private Button toAR;

    private int a=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maproutelayout);

        Intent newIntent = getIntent();
        b1 = newIntent.getDoubleExtra("b1", 0);
        b2 = newIntent.getDoubleExtra("b2", 0);

        toAR = (Button) findViewById(R.id.button_toAR);
        toAR.setOnClickListener(this);

        mNaviHelper = WalkNavigateHelper.getInstance();
//        Log.d("wenti", "" + mNaviHelper);
        try {
            View view = mNaviHelper.onCreate(MaprouteActivity.this);
            mapLayout = (LinearLayout) this.findViewById(R.id.layout_map);
            if (view != null) {
                mapLayout.addView(view);
            }

            View child=mapLayout.getChildAt(1);
            View child1=((ViewGroup)child).getChildAt(2);
            View child2=((ViewGroup)child1).getChildAt(9);
            traversalView((ViewGroup)child2);

            View child0=mapLayout.getChildAt(1);
            View child01=((ViewGroup)child0).getChildAt(2);
            View child02=((ViewGroup)child01).getChildAt(12);
            traversalView((ViewGroup)child02);

        } catch (Exception e) {
            e.printStackTrace();
        }

        mNaviHelper.setWalkNaviStatusListener(new IWNaviStatusListener() {
            @Override
            public void onWalkNaviModeChange(int mode, WalkNaviModeSwitchListener listener) {
                if(Tag==1){
                    Intent intent = new Intent(MaprouteActivity.this, ARrouteActivity.class);
                    intent.putExtra("b1", b1);
                    intent.putExtra("b2", b2);
                    startActivity(intent);
                }
                Tag=1;
            }

            @Override
            public void onNaviExit() {

            }
        });

        mNaviHelper.startWalkNavi(MaprouteActivity.this);

        mNaviHelper.setTTsPlayer(new IWTTSPlayer() {
            @Override
            public int playTTSText(final String s, boolean b) {
                return 0;
            }
        });

        mNaviHelper.setRouteGuidanceListener(this, new IWRouteGuidanceListener() {
            @Override
            public void onRouteGuideIconUpdate(Drawable drawable) {

            }

            @Override
            public void onRouteGuideKind(RouteGuideKind routeGuideKind) {

            }

            @Override
            public void onRoadGuideTextUpdate(CharSequence charSequence, CharSequence charSequence1) {

            }

            @Override
            public void onRemainDistanceUpdate(CharSequence charSequence) {

            }

            @Override
            public void onRemainTimeUpdate(CharSequence charSequence) {

            }

            @Override
            public void onGpsStatusChange(CharSequence charSequence, Drawable drawable) {

            }

            @Override
            public void onRouteFarAway(CharSequence charSequence, Drawable drawable) {

            }

            @Override
            public void onRoutePlanYawing(CharSequence charSequence, Drawable drawable) {

            }

            @Override
            public void onReRouteComplete() {

            }

            @Override
            public void onArriveDest() {

            }

            @Override
            public void onVibrate() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNaviHelper.quit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mNaviHelper.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNaviHelper.pause();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_toAR:
                Intent intent=new Intent(MaprouteActivity.this,ARrouteActivity.class);
                intent.putExtra("b1", b1);
                intent.putExtra("b2", b2);
                startActivity(intent);
                break;
        }
    }

    public void traversalView(ViewGroup viewGroup) {
        int count = viewGroup.getChildCount();
//        Log.d("wenti",""+count);
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                traversalView((ViewGroup) view);
            } else {
                doView(view);
            }
        }
    }

    private void doView(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ArCameraView.WALK_AR_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                MyToast.makeText(MaprouteActivity.this, "没有相机权限,请打开后重试", Toast.LENGTH_SHORT).show();
            } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mNaviHelper.startCameraAndSetMapView(MaprouteActivity.this);
            }
        }
    }
}
