package com.yunxian.carousel.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunxian.carousel.BaseCarouselAdapter;
import com.yunxian.carousel.app.R;

import java.util.List;

/**
 * @author A Shuai
 * @email ls1110924@163.com
 * @date 2016/9/20 23:48
 */
public class TestCustomAdapter extends BaseCarouselAdapter{

    private final LayoutInflater mInflater;
    private final List<String> mDataSet;

    public TestCustomAdapter(@NonNull Context mContext, @NonNull List<String> mDataSet) {
        mInflater = LayoutInflater.from(mContext);
        this.mDataSet = mDataSet;
    }

    @Override
    public int getCount() {
        return mDataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSet.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;

        if (convertView == null) {
            mViewHolder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.item_carousel, parent, false);
            mViewHolder.mTV = (TextView) convertView.findViewById(R.id.textview);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mTV.setText(mDataSet.get(position));

        return convertView;
    }

    @Override
    public int getItemViewCountOnSinglePage() {
        return 2;
    }

    private static class ViewHolder {

        TextView mTV;

    }
}
