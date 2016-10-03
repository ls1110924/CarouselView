package com.yunxian.carousel.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunxian.carousel.AbsBasicCarouselAdapter;
import com.yunxian.carousel.app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author A Shuai
 * @email ls1110924@163.com
 * @date 2016/9/19 1:01
 */
public class TestJustEnoughAdapter extends AbsBasicCarouselAdapter<TestJustEnoughAdapter.ViewHolder> {

    private static final int AMOUNT = 2;

    private final List<String> mDataSet = new ArrayList<>();

    public TestJustEnoughAdapter(@NonNull Context mContext) {
        super(mContext);
        for (int i = 0; i < AMOUNT; i++) {
            mDataSet.add(String.format(Locale.US, "Test--->%1$d", i));
        }
    }

    @Override
    public int getCount() {
        return mDataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSet.get(position);
    }

    @NonNull
    @Override
    protected View inflaterView(LayoutInflater mInflater, @NonNull ViewGroup parent) {
        return mInflater.inflate(R.layout.item_carousel, parent, false);
    }

    @NonNull
    @Override
    protected ViewHolder buildViewHolder() {
        return new ViewHolder();
    }

    @Override
    protected void findViews(View convertView, ViewHolder mViewHolder) {
        mViewHolder.mTV = (TextView) convertView.findViewById(R.id.textview);
    }

    @Override
    protected void bindContent(int position, ViewHolder mViewHolder) {
        mViewHolder.mTV.setText(mDataSet.get(position));
    }

    @Override
    public int getItemViewCountOnSinglePage() {
        return 2;
    }

    public static class ViewHolder extends AbsBasicCarouselAdapter.ViewHolder {

        TextView mTV;

    }

}
