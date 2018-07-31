package bupt.com.bupte;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(Tag==0) {
            Intent intent = new Intent(getActivity(), UnityPlayerActivity.class);
            startActivity(intent);
            Tag = 1;
        }
        View view = inflater.inflate(R.layout.fragment_exp, container, false);

        toUnity=(Button)view.findViewById(R.id.toUnity);
//        toUnity.setOnClickListener(this);
        toUnity.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
