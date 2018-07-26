package bupt.com.bupte;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.commons.BottomSheetFragment;

import java.util.ArrayList;
import java.util.List;

public class MyFragment extends BottomSheetFragment {

    private View view;
    private ListView lv;
    private TextView headtext;
    private List<Place> placeList = new ArrayList<Place>();
    private OnMyFragmentListener mListener;
//    private int site_id;

    private static final String TAG = "MyFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, container, false);
        headtext = (TextView)view.findViewById(R.id.head);
        String text = "Hi,"+ MyToolClass.getName() + ",你的报到流程如下:";
        headtext.setText(text);
        initPlaces();
        PlaceAdapter myAdapter = new PlaceAdapter(view.getContext(),R.layout.place_item, placeList);
        lv = (ListView)view.findViewById(R.id.list_view);
        lv.setAdapter(myAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Place place = placeList.get(position);
                mListener.myFragmentInteraction(place.getOrder());
            }
        });

        return view;
    }

    public interface OnMyFragmentListener{
        void myFragmentInteraction(int Order);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMyFragmentListener) {
            mListener = (OnMyFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMyFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.d("wenti","1114");
        Log.d("wenti4",""+R.id.container);
    }

    private void initPlaces() {
//        for (int i = 0; i < 2; i++) {
        String inline_gym = MyToolClass.getInLineNumbers(1);
        Place gym = new Place(1, "体育馆", "资料提交", inline_gym, "需：身份证复印件(1份) 录取通知书复印件(1份)");
        placeList.add(gym);
        String inline_hospital = MyToolClass.getInLineNumbers(2);
        Place hospital = new Place(2, "校医院", "提交缴费", inline_hospital, "需：身份证复印件(1份) 录取通知书复印件(1份)");
        placeList.add(hospital);
        String inline_office = MyToolClass.getInLineNumbers(3);
        Place office = new Place(3, "行政楼", "资料提交", inline_office, "需：身份证复印件(1份) 录取通知书复印件(1份)");
        placeList.add(office);
        String inline_educate = MyToolClass.getInLineNumbers(4);
        Place educate = new Place(4, "教学楼", "资料提交", inline_educate, "需：身份证复印件(1份) 录取通知书复印件(1份)");
        placeList.add(educate);
        String inline_apartment = MyToolClass.getInLineNumbers(5);
        Place apartment = new Place(5, "宿舍楼", "资料提交", inline_apartment, "需：身份证复印件(1份) 录取通知书复印件(1份)");
        placeList.add(apartment);
        String inline_office1 = MyToolClass.getInLineNumbers(6);
        Place office1 = new Place(6, "行政楼", "资料提交", inline_office1, "需：身份证复印件(1份) 录取通知书复印件(1份)");
        placeList.add(office1);

//        }
    }
}
