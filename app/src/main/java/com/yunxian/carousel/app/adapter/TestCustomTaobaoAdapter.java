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

/**
 * @author A Shuai
 * @email ls1110924@163.com
 * @date 2016/10/3 11:45
 */
public class TestCustomTaobaoAdapter extends AbsBasicCarouselAdapter<TestCustomTaobaoAdapter.ViewHolder> {

    private final List<String> mDataSet = new ArrayList<>();

    public TestCustomTaobaoAdapter(@NonNull Context mContext) {
        super(mContext);

        mDataSet.add("三星猛撞南墙！S7 Edge又炸了");
        mDataSet.add("男生这样穿，最受女生喜欢");
        mDataSet.add("黄易小编，无节操");
        mDataSet.add("黄易小编，尼玛炸了");
    }

    @NonNull
    @Override
    protected View inflaterView(LayoutInflater mInflater, @NonNull ViewGroup parent) {
        return mInflater.inflate(R.layout.item_custom_taobao, parent, false);
    }

    @NonNull
    @Override
    protected ViewHolder buildViewHolder() {
        return new ViewHolder();
    }

    @Override
    protected void findViews(View convertView, ViewHolder mViewHolder) {
        mViewHolder.mType = findView(convertView, R.id.type);
        mViewHolder.mTitle = findView(convertView, R.id.title);
    }

    @Override
    protected void bindContent(int position, ViewHolder mViewHolder) {
        if (position % 2 == 0) {
            mViewHolder.mType.setText("热评");
        } else {
            mViewHolder.mType.setText("热文");
        }

        mViewHolder.mTitle.setText(mDataSet.get(position));
    }

    @Override
    public int getCount() {
        return mDataSet.size();
    }

    @Override
    public int getItemViewCountOnSinglePage() {
        return 2;
    }

    public static class ViewHolder extends AbsBasicCarouselAdapter.ViewHolder {

        TextView mType;
        TextView mTitle;

    }

}
