package com.yunxian.carousel;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 公告轮播器控件的基础适配器的包装类适配器，封装完毕视图复用逻辑等
 *
 * @author A Shuai
 * @email ls1110924@163.com
 * @date 2016/9/24 1:51
 */
public abstract class AbsBasicCarouselAdapter<T extends AbsBasicCarouselAdapter.ViewHolder> extends BaseCarouselAdapter {

    private static final int TAG_ID = R.id.viewholder_tag;

    protected final Context mContext;
    protected final LayoutInflater mInflater;
    protected final Resources mResources;

    public AbsBasicCarouselAdapter(@NonNull Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        mResources = mContext.getResources();
    }


    @Override
    public Object getItem(int position) {
        return position;
    }

    @NonNull
    @Override
    public final View createView(ViewGroup parent) {
        View convertView = inflaterView(mInflater, parent);
        T mViewHolder = buildViewHolder();
        findViews(convertView, mViewHolder);
        convertView.setTag(TAG_ID, mViewHolder);
        return convertView;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void fillView(int position, @NonNull View convertView) {
        T mViewHolder = (T) convertView.getTag(TAG_ID);
        mViewHolder.index = position;
        bindContent(position, mViewHolder);
    }

    /**
     * 构造一个ItemView的子视图
     *
     * @param mInflater 解析XML layout
     * @param parent    父容器
     * @return 返回值不可为空
     */
    @NonNull
    protected abstract View inflaterView(LayoutInflater mInflater, @NonNull ViewGroup parent);

    /**
     * 子类请务必返回一个BasicViewHolderc的子类对象{@link ViewHolder}
     *
     * @return 不可为空
     */
    @NonNull
    protected abstract T buildViewHolder();

    /**
     * 各子类根据情况自行查找convertView中的内容视图控件并填充到ViewHolder中
     *
     * @param convertView 由{@link #inflaterView(LayoutInflater, ViewGroup)}实例化的内容视图
     * @param mViewHolder 由{@link #buildViewHolder()} 构造的ViewHolder对象
     */
    protected abstract void findViews(View convertView, T mViewHolder);

    /**
     * 根据提供的当前item的索引位置，自行填充ViewHolder中的内容视图控件的内容
     *
     * @param position    当前正在填充的itemview的位置
     * @param mViewHolder 查找完必要的子视图后的ViewHolder对象
     */
    protected abstract void bindContent(int position, T mViewHolder);

    /**
     * 根据提供的根视图和ID，从根视图中找出ID对应的视图
     *
     * @param mView 待查找视图所在的容器视图
     * @param id    待查找的视图ID
     * @param <T>   待查找的视图类型
     * @return 待查找的视图对象
     */
    @SuppressWarnings("unchecked")
    protected static <T extends View> T findView(View mView, int id) {
        try {
            return (T) mView.findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }

    /**
     * ItemView的基本ViewHolder
     */
    public static class ViewHolder {

        public int index;

    }

}
