package bupt.com.bupte;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.unity3d.player.*;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import static bupt.com.bupte.MainActivity.JiaoSan;
import static bupt.com.bupte.MainActivity.KeYan;

public class UnityPlayerActivity extends AppCompatActivity
{
    private  View fragView;
    private static final int CONFIG_CHANGE = 3;
    private static final int INIT_VIEW = 4;
    private static final int SHOW_FRAG = 5;
    public static UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code
    private FrameLayout frameLayout;
    private static final String TAG = "UnityPlayerActivity";

    // Setup activity layout
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

        mUnityPlayer = new MyUnityPlayer(this);
        setContentView(R.layout.unity_layout);
        frameLayout = (FrameLayout) findViewById(R.id.unity_player);
        frameLayout.addView(mUnityPlayer.getView());
        mUnityPlayer.requestFocus();

    }

    public void showJiaosanNavi(){
        showExploreFragment(JiaoSan);
    }

    public void showKeyanNavi(){
        showExploreFragment(KeYan);
//        Toast.makeText(UnityPlayerActivity.this, "you clicked 科研楼", Toast.LENGTH_SHORT).show();
    }

    public void showExploreFragment(int order){
        Bundle bundle = new Bundle();
        bundle.putInt("order_explore",order);
        ExploreFragment exploreFragment = new ExploreFragment();
        exploreFragment.setArguments(bundle);
        exploreFragment.show(getSupportFragmentManager(),R.id.unity_bottomsheet);
    }



    @Override protected void onNewIntent(Intent intent)
    {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
    }

    // Quit Unity
    @Override protected void onDestroy ()
    {
        Log.i(TAG, "onDestroy: ");
        mUnityPlayer.quit();
        super.onDestroy();
        Process.killProcess(Process.myPid());
    }

    // Pause Unity
    @Override protected void onPause()
    {
        Log.i(TAG, "onPause: ");
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override protected void onResume()
    {
        Log.i(TAG, "onResume: ");
        super.onResume();
        mUnityPlayer.resume();
    }

    @Override protected void onStart()
    {
        Log.i(TAG, "onStart: ");
        super.onStart();
        mUnityPlayer.start();
    }

    @Override protected void onStop()
    {
        Log.i(TAG, "onStop: ");
        super.onStop();
        mUnityPlayer.stop();
    }

    // Low Memory Unity
    @Override public void onLowMemory()
    {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL)
        {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
//    @Override public boolean onKeyUp(int keyCode, KeyEvent event)     {
//        return mUnityPlayer.injectEvent(event);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event){
//        mUnityPlayer.quit();
//        return true;
//    }

    @Override public boolean onTouchEvent(MotionEvent event)          {
        return mUnityPlayer.injectEvent(event);
    }

    /*API12*/ public boolean onGenericMotionEvent(MotionEvent event)
    { return mUnityPlayer.injectEvent(event);
    }


}
