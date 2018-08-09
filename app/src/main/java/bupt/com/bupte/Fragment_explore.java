package bupt.com.bupte;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Fragment_explore extends Fragment{//下面三个按钮的“探索”功能

    private Button toUnity;
    private int Tag=0;
    private OnMyFragmentExpListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Intent intent = new Intent(getActivity(), UnityPlayerActivity.class);
        startActivity(intent);
        mListener.myFragmentExpInteraction();
        View view = inflater.inflate(R.layout.fragment_exp, container, false);
        return view;
    }

    public interface OnMyFragmentExpListener{
        void myFragmentExpInteraction();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Fragment_explore.OnMyFragmentExpListener) {
            mListener = (Fragment_explore.OnMyFragmentExpListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMyFragmentExpListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("wentiexp","destroy");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("wentiexp","stop");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("wentiexp","resume");
    }
}
