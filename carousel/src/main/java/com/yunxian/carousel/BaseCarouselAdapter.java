package com.yunxian.carousel;

import android.database.DataSetObservable;
import android.database.DataSetObserver;

/**
 * 公告轮播器适配器的公共基础实现类
 *
 * @author A Shuai
 * @email ls1110924@163.com
 * @date 2016/9/18 19:59
 */
public abstract class BaseCarouselAdapter implements CarouselAdapter {

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    public void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }
}
