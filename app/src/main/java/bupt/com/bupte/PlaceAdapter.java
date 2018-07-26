package bupt.com.bupte;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lenovo on 2018/7/3.
 */

public class PlaceAdapter extends ArrayAdapter<Place> {

    private int resourceId;

    public PlaceAdapter(Context context, int id, List<Place> objects){
        super(context, id, objects);
        resourceId = id;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Place place = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.order = (TextView) view.findViewById(R.id.text_order);
            viewHolder.name = (TextView) view.findViewById(R.id.text_name);
            viewHolder.note = (TextView) view.findViewById(R.id.text_note);
            viewHolder.detail = (TextView) view.findViewById(R.id.text_detail);
            viewHolder.inLine = (TextView) view.findViewById(R.id.text_inLine);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.order.setText(place.getOrder()+"");
        viewHolder.name.setText(place.getName());
        viewHolder.note.setText(place.getNote());
        viewHolder.inLine.setText(place.getInLine());
        viewHolder.detail.setText(place.getDetail());
        return view;
    }

    class ViewHolder{
        TextView order;
        TextView name;
        TextView note;
        TextView detail;
        TextView inLine;
    }
}
