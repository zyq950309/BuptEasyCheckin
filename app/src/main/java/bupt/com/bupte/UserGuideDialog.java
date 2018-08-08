package bupt.com.bupte;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

public class UserGuideDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.unity_user_guide,container);
        Button readBtn = (Button)view.findViewById(R.id.user_guide_button);
        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.Mdialog);
    }

    @Override
    public void onStart() {
        getDialog().getWindow().getAttributes().width=getResources().getDisplayMetrics().widthPixels;
        getDialog().getWindow().getAttributes().height=getResources().getDisplayMetrics().heightPixels;
        getDialog().getWindow().setGravity(Gravity.BOTTOM);//对齐方式
        super.onStart();
    }

}

