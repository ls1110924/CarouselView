package com.yunxian.carousel;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

/**
 * 轮播图适配器
 *
 * @author A Shuai
 * @email ls1110924@163.com
 * @date 2016/9/18 17:32
 */
public interface CarouselAdapter {


    void registerDataSetObserver(DataSetObserver observer);


    void unregisterDataSetObserver(DataSetObserver observer);


    int getCount();


    Object getItem(int position);


    long getItemId(int position);


    View getView(int position, View convertView, ViewGroup parent);

    /**
     * 每一个轮播页上有多少个条目
     *
     * @return 每一页上的条目数
     */
    int getItemViewCountOnSinglePage();


    boolean isEmpty();

}
