package bupt.com.bupte;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Fragment_mine extends Fragment implements View.OnClickListener{//下面三个按钮的“我的”功能

    private TextView name_show,sid_show,depmt_show,prof_show,room_show;
    private Button about_button,setting_button,logout_button;
    private String depmt_out,prof_out;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        name_show=(TextView)view.findViewById(R.id.name_show);
        sid_show=(TextView)view.findViewById(R.id.id_show);
        depmt_show=(TextView)view.findViewById(R.id.department_show);
        prof_show=(TextView)view.findViewById(R.id.profession_show);
        room_show=(TextView)view.findViewById(R.id.room_show);

        about_button=(Button) view.findViewById(R.id.about_button);
        setting_button=(Button) view.findViewById(R.id.setting_button);
        logout_button=(Button) view.findViewById(R.id.logout_button);

        about_button.setOnClickListener(this);
        setting_button.setOnClickListener(this);
        logout_button.setOnClickListener(this);

        int depmt=getArguments().getInt("depmt");//根据int选择学院显示
        switch (depmt){
            case 2:
                depmt_out="未知";
                break;
        }

        int prof=getArguments().getInt("prof");//根据int选择专业显示
        switch (prof){
            case 2:
                prof_out="未知";
                break;
        }

        name_show.setText(""+getArguments().getString("name")+"");//显示学生信息
        sid_show.setText("学号："+getArguments().getInt("sid")+"");
        //暂时改动
//        depmt_show.setText("院系："+depmt_out+"");
//        prof_show.setText("专业："+prof_out+"");
//        room_show.setText("宿舍：学生公寓"+getArguments().getInt("building")+"号楼"+getArguments().getInt("room")+"");
        depmt_show.setText("院系："+"未知");
        prof_show.setText("专业："+"未知");
        room_show.setText("宿舍："+"未知");

        return view;
    }

    @Override
    public void onClick(View v){//点击事件
        switch (v.getId()){
            case R.id.about_button:
                break;
            case R.id.setting_button:
                break;
            case R.id.logout_button:
                Intent intent=new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
//                Log.d("wenti",""+getActivity());
                getActivity().finish();//这个在探索界面存在的时候好像不能finish
                break;
        }
    }
}
