package com.andview.example.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andview.example.DensityUtil;
import com.andview.example.IndexPageAdapter;
import com.andview.example.R;
import com.andview.example.moduler.Person;
import com.andview.example.recylerview.SimpleAdapter;
import com.andview.example.ui.BannerViewPager;
import com.andview.example.ui.CustomGifHeader;
import com.andview.example.ui.ImageCycleView;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.SimpleXRefreshListener;
import com.andview.refreshview.XRefreshViewFooter;
import com.andview.example.ui.ImageCycleView.ImageCycleViewListener;

import java.util.ArrayList;
import java.util.List;

public class BannerRecyclerViewActivityWithIndicater extends Activity {
    RecyclerView recyclerView;
    SimpleAdapter adapter;
    List<Person> personList = new ArrayList<Person>();
    XRefreshView xRefreshView;
    int lastVisibleItem = 0;
    GridLayoutManager layoutManager;
    private boolean isBottom = false;
    private int mLoadCount = 0;

    private ImageCycleView imageCycleView;
    private ImageCycleViewListener cycleViewListener ;

    List<String> imageUrlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recylerview);
        xRefreshView = (XRefreshView) findViewById(R.id.xrefreshview);
        xRefreshView.setPullLoadEnable(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_test_rv);
        recyclerView.setHasFixedSize(true);

        cycleViewListener = new ImageCycleViewListener() {
            @Override
            public void onImageClick(String url, int position) {
                Toast.makeText(BannerRecyclerViewActivityWithIndicater.this,
                        "position = " + position,Toast.LENGTH_SHORT).show();
            }
        };

        initData();
        adapter = new SimpleAdapter(personList, this);
        // 设置静默加载模式
//		xRefreshView.setSlienceLoadMore();
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        imageCycleView = new ImageCycleView(this,null);

        LinearLayout.LayoutParams parm = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                DensityUtil.dip2px(this,150));
        imageCycleView.setLayoutParams(parm);

        adapter.setHeaderView(imageCycleView, recyclerView);

        imageCycleView.setImageResources(imageUrlList , cycleViewListener,1);
        //imageCycleView.updataData(imageUrlList);

        CustomGifHeader header = new CustomGifHeader(this);
        xRefreshView.setCustomHeaderView(header);
        recyclerView.setAdapter(adapter);
//        xRefreshView.setAutoLoadMore(true);
        xRefreshView.setPinnedTime(1000);
        xRefreshView.setMoveForHorizontal(true);
//        adapter.setHeaderView(headerView, recyclerView);
        adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));

//		xRefreshView.setPullLoadEnable(false);
        //设置静默加载时提前加载的item个数
//		xRefreshView.setPreLoadCount(2);

        xRefreshView.setXRefreshViewListener(new SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        xRefreshView.stopRefresh();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        for (int i = 0; i < 6; i++) {
                            adapter.insert(new Person("More ", "21"),
                                    adapter.getAdapterItemCount());
                        }
                        mLoadCount++;
                        if (mLoadCount >= 3) {
                            xRefreshView.setLoadComplete(true);
                        } else {
                            // 刷新完成必须调用此方法停止加载
                            xRefreshView.stopLoadMore();
                        }
                    }
                }, 1000);
            }
        });
    }

    private void initData() {
        imageUrlList = new ArrayList<>();
        imageUrlList.add("http://www.baidu.com/img/bdlogo.png");
        imageUrlList.add("http://img1.imgtn.bdimg.com/it/u=2356128680,3896784927&fm=21&gp=0.jpg");
        imageUrlList.add("http://c.hiphotos.baidu.com/zhidao/pic/item/a9d3fd1f4134970a8acbdc3897cad1c8a7865d69.jpg");
        imageUrlList.add("http://img1.imgtn.bdimg.com/it/u=2178528477,2356507610&fm=21&gp=0.jpg");

        for (int i = 0; i < 30; i++) {
            Person person = new Person("name" + i, "" + i);
            personList.add(person);
        }
    }



}