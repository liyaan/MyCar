package com.liyaan.car.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.liyaan.car.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class MyPhotoAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mDatas;
    private LayoutInflater mInflater;
    private OnDelImgListion delLis;
    public MyPhotoAdapter(Context context,List<String> datas){
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    public void setDelLis(OnDelImgListion del) {
        this.delLis = del;
    }

    @Override
    public int getCount() {
        if (mDatas.size()<9){
            return mDatas.size()+1;
        }
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
            convertView = mInflater.inflate(R.layout.item_photo,parent,false);
            head = new MyHand();
            head.img = convertView.findViewById(R.id.item_img_photo);
            head.del = convertView.findViewById(R.id.item_img_del);
            head.select = convertView.findViewById(R.id.item_img_photo_select);
            head.noSelect = convertView.findViewById(R.id.item_img_photo_img);
            convertView.setTag(head);
        }else{
            head = (MyHand) convertView.getTag();
        }
        if (position == mDatas.size()){
            if (mDatas.size()>=9){
                head.select.setVisibility(View.GONE);
            }else{
                head.select.setVisibility(View.VISIBLE);
            }
            head.noSelect.setVisibility(View.GONE);
        }else{
            head.select.setVisibility(View.GONE);
            head.noSelect.setVisibility(View.VISIBLE);
            //设置图片圆角角度
            RoundedCorners roundedCorners = new RoundedCorners(4);
            RequestOptions options = new RequestOptions()
                    .error(R.mipmap.ic_launcher).bitmapTransform(roundedCorners)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).override(70,70);
            Glide.with(mContext).load(mDatas.get(position))
                    .apply(options)
                    .into(head.img);
//            ImageLoaderManager.getInstance(mContext).diaplay(head.img,mDatas.get(position));
            if (delLis!=null){
                head.del.setOnClickListener(new MyClick(position));
            }
        }
        return convertView;
    }
    class MyHand{
        ImageView del;
        ImageView img;
        RelativeLayout select,noSelect;
    }
    class  MyClick implements View.OnClickListener{
        private int posi;

        public MyClick(int posi) {
            this.posi = posi;
        }

        @Override
        public void onClick(View v) {
            delLis.delImg(posi);
        }
    }
    /**
     * 加载本地图片
     * @param url
     * @return
     */
    private Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    public interface OnDelImgListion{
        void delImg(int id);
    }
}
