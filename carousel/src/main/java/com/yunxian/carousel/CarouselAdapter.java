package com.yunxian.carousel;

import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * 公告轮播器控件适配器
 * <p/>
 * 注意，本控件不支持多子视图布局
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

    /**
     * 构造一个孩子内容视图
     *
     * @param parent 父容器
     * @return 子视图，不可为空
     */
    @NonNull
    View createView(ViewGroup parent);

    /**
     * 填充子视图内容
     *
     * @param position    当前参数子视图对应的索引值
     * @param convertView 子视图
     */
    void fillView(int position, @NonNull View convertView);

    /**
     * 每一个轮播页上有多少个条目
     *
     * @return 每一页上的条目数
     */
    int getItemViewCountOnSinglePage();

    /**
     * 是否为空
     *
     * @return true表示Adapter数据为空
     */
    boolean isEmpty();

}
