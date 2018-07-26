package bupt.com.bupte;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Fragment_minetour extends Fragment implements View.OnClickListener{//游客登录，下面三个按钮的“我的”功能

    private Button tologin;//“去登录”按钮

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_minetour, container, false);

        tologin=(Button)view.findViewById(R.id.tologin_button);
        tologin.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v){//点击事件
        switch (v.getId()) {
            case R.id.tologin_button:
                Intent intent=new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }
}
