package bupt.com.bupte;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/7/3.
 */

public class PlaceAdapter extends ArrayAdapter<Place> {

    private final int WITH_DETAIL = 1;
    private final int WITHOUT_DETAIL = 0;
    private int resourceId;
    private  final int STYLE_TYPE_COUNT = 2;

    public PlaceAdapter(Context context, int id, List<Place> objects){
        super(context, id, objects);
        resourceId = id;
    }

//    @Override
//    public int getCount() {
//        return mList.size();
//    }

//    @Override
//    public long getItemId(int position) {
//        return position;
//    }

//    @Nullable
//    @Override
//    public Place getItem(int position) {
//        return mList.get(position);
//    }

    @Override
    public int getViewTypeCount() {
        return STYLE_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getStyle_tag();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Place place = getItem(position);
        View view;
        MyViewHolder viewHolder = null;
        Another_ViewHolder anotherHolder = null;
        int style_type = place.getStyle_tag();
        if(convertView==null){
            switch (style_type){
                case WITH_DETAIL:
                    viewHolder = new MyViewHolder();
                    convertView = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
                    viewHolder.order = (TextView) convertView.findViewById(R.id.text_order);
                    viewHolder.name = (TextView) convertView.findViewById(R.id.text_name);
                    viewHolder.detail = (TextView) convertView.findViewById(R.id.text_detail);
                    convertView.setTag(viewHolder);
                    break;
                case WITHOUT_DETAIL:
                    anotherHolder = new Another_ViewHolder();
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.place_item1,parent,false);
                    anotherHolder.order = (TextView) convertView.findViewById(R.id.text_order_no);
                    anotherHolder.name = (TextView) convertView.findViewById(R.id.text_name_no);
                    convertView.setTag(anotherHolder);
                    break;
                default:
                    break;
            }

        }else {
            switch (style_type) {
                case WITH_DETAIL:
                    viewHolder = (MyViewHolder) convertView.getTag();
                    break;
                case WITHOUT_DETAIL:
                    anotherHolder = (Another_ViewHolder) convertView.getTag();
                    break;
                default:
                    break;
            }
        }
        if (style_type == WITHOUT_DETAIL) {
            setItemValue(anotherHolder, position);
        } else {
            setItemValue(viewHolder, position);
        }

        return convertView;
    }

    private void setItemValue(ViewHolder viewHolder,int position){
        viewHolder.order.setText(getItem(position).getOrder()+"");
        viewHolder.name.setText(getItem(position).getName());
        if(getItem(position).getStyle_tag()==1){
            viewHolder.detail.setText(getItem(position).getDetail());
        }
    }


    private class ViewHolder{
        TextView order;
        TextView name;
        TextView detail;
    }
    private class MyViewHolder extends ViewHolder{

    }
    private class Another_ViewHolder extends ViewHolder{

    }

}
