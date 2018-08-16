package com.android.melo.kaoqinfuxiao.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.melo.kaoqinfuxiao.R;
import com.android.melo.kaoqinfuxiao.db.UserInfo;
import com.android.melo.kaoqinfuxiao.utils.LogUtils;

import java.util.List;

/**
 * Created by melo on 2018/04/13.
 * 学校人员信息适配
 */
public class SchoolNumMsgListAdapter extends BaseAdapter {

    public static final String TAG = SchoolNumMsgListAdapter.class.getSimpleName();

    private List<UserInfo> infoList;
    private Context context;
    private ViewHolder holder = null;

    public SchoolNumMsgListAdapter(Context context, List<UserInfo> infoList) {
        this.context = context;
        this.infoList = infoList;
    }

    @Override
    public int getCount() {
        return infoList.size();
    }

    @Override
    public Object getItem(int position) {
        return infoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_schoolnum, null);
            holder = new ViewHolder();
            holder.mTvName = convertView.findViewById(R.id.tv_dialog_schoolnum_name);
            holder.mTvIdcard = convertView.findViewById(R.id.tv_dialog_schoolnum_idcard);
            holder.mTvClass = convertView.findViewById(R.id.tv_dialog_schoolnum_className);
            holder.mTvIdentity = convertView.findViewById(R.id.tv_dialog_schoolnum_identity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position%2==0){
            //单行
            holder.mTvName.setBackgroundColor(context.getResources().getColor(R.color.datalist_item_A));
            holder.mTvIdcard.setBackgroundColor(context.getResources().getColor(R.color.datalist_item_B));
            holder.mTvClass.setBackgroundColor(context.getResources().getColor(R.color.datalist_item_A));
            holder.mTvIdentity.setBackgroundColor(context.getResources().getColor(R.color.datalist_item_B));
        }else {
            //多行
            holder.mTvName.setBackgroundColor(context.getResources().getColor(R.color.datalist_item_C));
            holder.mTvIdcard.setBackgroundColor(context.getResources().getColor(R.color.datalist_item_D));
            holder.mTvClass.setBackgroundColor(context.getResources().getColor(R.color.datalist_item_C));
            holder.mTvIdentity.setBackgroundColor(context.getResources().getColor(R.color.datalist_item_D));
        }

        holder.mTvName.setText(infoList.get(position).getUserName());
        holder.mTvIdcard.setText(infoList.get(position).getIdCard());
        holder.mTvClass.setText(infoList.get(position).getClassName());
        holder.mTvIdentity.setText(infoList.get(position).getIdentity());

        return convertView;
    }

    class ViewHolder {
        TextView mTvName, mTvIdcard, mTvClass, mTvIdentity;
    }
}
