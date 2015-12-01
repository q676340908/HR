package com.zhongyu.hr.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongyu.hr.HRApp;
import com.zhongyu.hr.R;
import com.zhongyu.hr.event.NavigationEvent;
import com.zhongyu.hr.model.MyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/30.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private static int TYPE_CATEGORY = 1;
    private static int TYPE_ITEM = 2;

    private List<MyInfo> mInfos = new ArrayList<>();
    private Context mContext;

    public MyAdapter(Context context){
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == TYPE_CATEGORY){
            view = LayoutInflater.from(mContext).inflate(R.layout.list_category_my,null);
        }else{
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_my,null);
        }

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyInfo info = mInfos.get(position);
        if(info.type != MyInfo.TYPE_CATEGORY){
            Resources res = mContext.getResources();
            int indentify = res.getIdentifier(mContext.getPackageName()+":drawable/"+info.iconResName, null, null);
            if(indentify > 0){
              Drawable icon = res.getDrawable(indentify);
                holder.icon.setImageDrawable(icon);
            }

            indentify = res.getIdentifier(mContext.getPackageName() + ":string/" + info.titleResName, null, null);
            if(indentify > 0){
              String  title = res.getString(indentify);
                holder.title.setText(title);
            }
        }
        holder.setTag(info.type);
    }

    @Override
    public int getItemCount() {
        return mInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mInfos.get(position).type == MyInfo.TYPE_CATEGORY){
            return TYPE_CATEGORY;
        }
        return TYPE_ITEM;
    }

    public void setMyInfoList(List<MyInfo> infos){
        mInfos = infos;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected TextView title;
        protected ImageView icon;
        protected View itemView;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            icon = (ImageView)itemView.findViewById(R.id.icon);
            this.itemView = itemView;

            itemView.setOnClickListener(this);
        }

        public void setTag(int type){
            this.itemView.setTag(type);
        }

        @Override
        public void onClick(View v) {
            int type = (int)v.getTag();
            HRApp.getInstance().getRxBus().postEvent(new NavigationEvent(type));
        }
    }
}
