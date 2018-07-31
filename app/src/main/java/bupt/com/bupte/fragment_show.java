package bupt.com.bupte;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.unity3d.player.UnityPlayer;

public class Fragment_show extends Fragment {//不采用这种方法，使用activity跳转

    protected UnityPlayer mUnityPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().getWindow().takeSurface(null);
        getActivity().setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);
        getActivity().getWindow().setFormat(PixelFormat.RGB_565);
        mUnityPlayer = new MyUnityPlayer(getActivity());
        if (mUnityPlayer.getSettings ().getBoolean ("hide_status_bar", true)) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        int glesMode = mUnityPlayer.getSettings().getInt("gles_mode", 1);
        boolean trueColor8888 = false;
        mUnityPlayer.init(glesMode, trueColor8888);

        mUnityPlayer.windowFocusChanged(true);
//        mUnityPlayer.requestFocus();
        View playerView = mUnityPlayer.getView();
        return playerView;

    }

    @Override
    public void onDestroy () {
        mUnityPlayer.quit();
        super.onDestroy();
        Log.d("wenti2","destroy");
    }

    @Override
    public void onPause() {
        super.onPause();
        mUnityPlayer.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mUnityPlayer.resume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        Log.d("wenti1","destroy");
    }
}
