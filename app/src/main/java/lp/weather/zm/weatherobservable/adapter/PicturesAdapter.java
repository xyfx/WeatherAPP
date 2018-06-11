package lp.weather.zm.weatherobservable.adapter;

import android.content.Context;
import android.graphics.Picture;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import lp.weather.zm.weatherobservable.R;
import lp.weather.zm.weatherobservable.bean.BeautyPic;
import lp.weather.zm.weatherobservable.utils.DeviceUtils;

/**
 * Created by ASUS on 2018/5/27.
 */

public class PicturesAdapter extends RecyclerView.Adapter<PicturesAdapter.ViewHolder>{
   private List<BeautyPic.ImageBean> imageBeans;
   private Context mContext;

   public PicturesAdapter(List<BeautyPic.ImageBean> imageBeans,Context context){
       this.imageBeans=imageBeans;
       this.mContext=context;
   }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= View.inflate(mContext, R.layout.item_beauty_recycle,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(mContext)
                .load(imageBeans.get(position).getUrl())
                .placeholder(R.mipmap.ic_launcher)
                .resize(DeviceUtils.getScreenWidth(mContext),500)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageBeans.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.item_beauty_img);
        }
    }
}
