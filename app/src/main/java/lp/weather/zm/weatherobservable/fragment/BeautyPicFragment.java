package lp.weather.zm.weatherobservable.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import java.util.ArrayList;
import java.util.List;

import lp.weather.zm.weatherobservable.R;
import lp.weather.zm.weatherobservable.adapter.PicturesAdapter;
import lp.weather.zm.weatherobservable.bean.BeautyPic;
import lp.weather.zm.weatherobservable.bean.WeatherBean;
import lp.weather.zm.weatherobservable.network.WeatherService;
import lp.weather.zm.weatherobservable.utils.Constans;
import lp.weather.zm.weatherobservable.utils.ToastUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ASUS on 2018/5/24.
 */

public class BeautyPicFragment extends Fragment{
    private static final int LOADMORE=1;
    private static final int REFRESH=0;
    private View rootView;
    private RecyclerView recyclerView;
    private PicturesAdapter picturesAdapter;
    private List<BeautyPic.ImageBean> imageBeanList;
    private  int mPage=2;
    private MaterialRefreshLayout materialRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.fragment_beauty_pic,container,false);
        initViews(rootView);
        initDatas();
        return rootView;
    }

    private void initDatas() {
        requreImgFromNetWork(REFRESH);
        initRefreshLayout();
    }

    private void initRefreshLayout() {
      materialRefreshLayout.setLoadMore(true);
      materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
          @Override
          public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                mPage=2;
                requreImgFromNetWork(REFRESH);
          }

          @Override
          public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
              mPage++;
              if(mPage>20){
                  ToastUtils.Show(getContext(),"没有更多图片了...");
                  return;
              }
              requreImgFromNetWork(LOADMORE);
          }
      });

    }

    private void requreImgFromNetWork(final int action) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constans.WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService.BlobService service = retrofit.create(WeatherService.BlobService.class);
        Call<BeautyPic> call = service.getImages(mPage);
        call.enqueue(new Callback<BeautyPic>() {
            @Override
            public void onResponse(Call<BeautyPic> call, Response<BeautyPic> response) {
                if(response.body().getCode()==200){
                   showImages(response.body().getData(),action);
                }else{
                    ToastUtils.Show(getContext(),response.body().getMsg());
                }

            }

            @Override
            public void onFailure(Call<BeautyPic> call, Throwable t) {
                ToastUtils.Show(getContext(),"加载失败!");
            }
        });
    }

    private void showImages(List<BeautyPic.ImageBean> data,int action) {
      if(action==REFRESH){
          this.imageBeanList.clear();
          this.imageBeanList.addAll(data);
          materialRefreshLayout.finishRefresh();
      }else{
          this.imageBeanList.addAll(data);
          materialRefreshLayout.finishRefreshLoadMore();
      }

      picturesAdapter.notifyDataSetChanged();

    }

    private void initViews(View rootView) {
       recyclerView=rootView.findViewById(R.id.beauty_recycle);
       materialRefreshLayout=rootView.findViewById(R.id.refresh_view);

       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       recyclerView.setItemAnimator(new DefaultItemAnimator());

       imageBeanList=new ArrayList<>();
       picturesAdapter=new PicturesAdapter(imageBeanList,getContext());
       recyclerView.setAdapter(picturesAdapter);
    }
}
