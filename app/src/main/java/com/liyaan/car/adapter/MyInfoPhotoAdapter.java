package com.liyaan.car.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.liyaan.car.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class MyInfoPhotoAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mDatas;
    private LayoutInflater mInflater;
    private List<String> checkData;
    public MyInfoPhotoAdapter(Context context, List<String> datas){
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
        checkData = new ArrayList<>();
    }

    public List<String> getCheckData(){
        return checkData;
    }

    @Override
    public int getCount() {
        return mDatas.size();

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHand head;
        if (convertView==null){
            convertView = mInflater.inflate(R.layout.item_info_photo,parent,false);
            head = new MyHand();
            head.img = convertView.findViewById(R.id.item_img_photo);
            head.checkBox = convertView.findViewById(R.id.checkBox);
            convertView.setTag(head);
        }else{
            head = (MyHand) convertView.getTag();
        }

        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(4);
        RequestOptions options = new RequestOptions()
                .error(R.mipmap.ic_launcher).bitmapTransform(roundedCorners)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(mContext).load(mDatas.get(position))
                .apply(options)
                .into(head.img);
        if (checkData.contains(mDatas.get(position))){
            head.checkBox.setChecked(true);
        }else{
            head.checkBox.setChecked(false);
        }
        head.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData.contains(mDatas.get(position))){
                    checkData.remove(mDatas.get(position));
                }else{
                    if (checkData.size()>=1){
                        Toast.makeText(mContext,"只能单张分享",Toast.LENGTH_LONG).show();
                        notifyDataSetChanged();
                        return;
                    }
                    checkData.add(mDatas.get(position));
                }
                notifyDataSetChanged();
            }
        });
//            ImageLoaderManager.getInstance(mContext).diaplay(head.img,mDatas.get(position));


        return convertView;
    }
    class MyHand{
        ImageView img;
        CheckBox checkBox;
    }

}
