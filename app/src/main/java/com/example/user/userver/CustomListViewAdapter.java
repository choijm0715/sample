package com.example.user.userver;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 2016-07-07.
 */
public class CustomListViewAdapter extends BaseAdapter{

    private Context context;
    private List<CustomDataModel> lists;
    public CustomListViewAdapter(Context ctx) {
        context = ctx;
    }

    public void setListData(List listData) {
        lists = listData;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.content_request_list_item, null);
        }

        TextView company = (TextView)convertView.findViewById(R.id.company);
        TextView explanation = (TextView)convertView.findViewById(R.id.explanation);
        TextView date = (TextView)convertView.findViewById(R.id.date);
        TextView situation = (TextView)convertView.findViewById(R.id.situation);
        TextView money = (TextView)convertView.findViewById(R.id.money);

        company.setText(lists.get(position).company);
        explanation.setText(lists.get(position).explanation);
        date.setText(lists.get(position).date);
        situation.setText(lists.get(position).situation);
        money.setText(lists.get(position).money);

        return convertView;
    }
}
