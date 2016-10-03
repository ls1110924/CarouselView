package com.yunxian.carousel;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * 公告轮播器控件
 * <p>
 * 注意：<font color="red">该公告轮播器控件对于轮播器控件本身的点击事件监听器
 * 和ItemView的点击事件监听器冲突，只可选择绑定其中一个</font>
 *
 * @author A Shuai
 * @email ls1110924@163.com
 * @date 2016/9/18 17:03
 */
public class CarouselView extends FrameLayout {

    private static final String TAG = CarouselView.class.getSimpleName();

    // 默认轮播间隔时长5秒钟
    private static final int DEFAULT_CAROUSEL_INTERVAL = 5000;

    // 主视图
    private LinearLayout mMainView;
    // 备用视图
    private LinearLayout mReserveView;

    private final CommonCallbackListener mCommonListener = new CommonCallbackListener();
    // item事件监听器
    private OnItemClickListener mOnItemClickListener;

    private CarouselAdapter mAdapter;
    private DataSetObserver mDataSetObserver;

    private int startIndex;

    // 切换动画
    private final Animation mInAnimation;
    private final Animation mOutAnimation;
    // 轮播间隔时长
    private int mCarouselInterval;

    // 通知执行轮播的Handler以及轮播通知
    private final Handler mUpdateHandler = new Handler(Looper.getMainLooper());
    private final Runnable mCarouselRunnable = new CarouselRunnable();

    // 构造函数
    public CarouselView(Context context) {
        this(context, null, 0);
    }

    public CarouselView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarouselView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CarouselView);

            mCarouselInterval = a.getInteger(R.styleable.CarouselView_interval_duration, DEFAULT_CAROUSEL_INTERVAL);
            if (mCarouselInterval < 0) {
                mCarouselInterval = DEFAULT_CAROUSEL_INTERVAL;
            }

            int mAnimResID = a.getResourceId(R.styleable.CarouselView_slide_in, R.anim.carousel_slide_bottom_in);
            mInAnimation = AnimationUtils.loadAnimation(context, mAnimResID);

            mAnimResID = a.getResourceId(R.styleable.CarouselView_slide_out, R.anim.carousel_slide_bottom_out);
            mOutAnimation = AnimationUtils.loadAnimation(context, mAnimResID);

            a.recycle();
        } else {
            mCarouselInterval = DEFAULT_CAROUSEL_INTERVAL;

            mInAnimation = AnimationUtils.loadAnimation(context, R.anim.carousel_slide_bottom_in);
            mOutAnimation = AnimationUtils.loadAnimation(context, R.anim.carousel_slide_bottom_out);
        }

        init(context);
    }

    private void init(Context context) {
        mMainView = new LinearLayout(context);
        mMainView.setOrientation(LinearLayout.VERTICAL);
        addView(mMainView,
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        mReserveView = new LinearLayout(context);
        mReserveView.setOrientation(LinearLayout.VERTICAL);
        mReserveView.setVisibility(INVISIBLE);
        addView(mReserveView,
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

    }

    /**
     * 公告轮播器控件的item点击事件监听器
     */
    public interface OnItemClickListener {

        /**
         * 当公告轮播器控件中的一条item被点击时触发的回调方法
         *
         * @param parent   轮播器控件自己
         * @param view     被点击了item view
         * @param position 位置索引
         * @param id       行号，一般等同于position
         */
        void onItemClick(CarouselView parent, View view, int position, long id);

        /**
         * 当公告轮播器控件中的一条item被长按时触发的回调方法
         *
         * @param parent   轮播器控件自己
         * @param view     被长按了的item view
         * @param position 位置索引
         * @param id       行号，一般等同于position
         * @return true表示消费了该长按事件，否则为false
         */
        boolean onItemLongClick(CarouselView parent, View view, int position, long id);

    }

    /**
     * 设置处理item的事件监听器
     * <p>
     * 注意item的事件监听器与{@link #setOnClickListener(OnClickListener)}轮播器控件的点击事件冲突，
     * 二者只可取其一。
     *
     * @param mOnItemClickListener item事件监听器
     */
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        // 如果形参为空，表示要清除ItemView的点击事件监听器
        if (mOnItemClickListener == null) {
            this.mOnItemClickListener = null;
            iterateClearItemViewOnClickListener(mMainView);
            iterateClearItemViewOnClickListener(mReserveView);
            return;
        }

        if (ViewCompat.hasOnClickListeners(this)) {
            throw new IllegalStateException("the OnItemClickListener and the OnClickListener must be set one of them!");
        }
        this.mOnItemClickListener = mOnItemClickListener;

        iterateSetItemViewOnClickListener(mMainView);
        iterateSetItemViewOnClickListener(mReserveView);
    }

    /**
     * 覆写公告轮播器控件的设置点击事件监听器，保证轮播器监听器与Item监听器只有一个
     *
     * @param l 公告轮播器控件点击事件监听器
     */
    @Override
    public void setOnClickListener(OnClickListener l) {
        if (mOnItemClickListener != null) {
            throw new IllegalStateException("the OnItemClickListener and the OnClickListener must be set one of them!");
        }
        super.setOnClickListener(l);

        iterateClearItemViewOnClickListener(mMainView);
        iterateClearItemViewOnClickListener(mReserveView);
    }

    /**
     * 迭代给ItemView设置点击和长按监听器
     *
     * @param mContain 容器
     */
    private void iterateSetItemViewOnClickListener(LinearLayout mContain) {
        for (int i = 0; i < mContain.getChildCount(); i++) {
            View mChildView = mContain.getChildAt(i);
            mChildView.setOnClickListener(mCommonListener);
            mChildView.setOnLongClickListener(mCommonListener);
        }
    }

    /**
     * 迭代给ItemView清除点击和长按监听器
     *
     * @param mContain 容器
     */
    private void iterateClearItemViewOnClickListener(LinearLayout mContain) {
        for (int i = 0; i < mContain.getChildCount(); i++) {
            View mChildView = mContain.getChildAt(i);
            mChildView.setOnClickListener(null);
            mChildView.setOnLongClickListener(null);
            mChildView.setClickable(false);
        }
    }

    private void performOnItemClick(View mChildView) {
        if (mOnItemClickListener != null) {
            ItemLayoutParams lp = (ItemLayoutParams) mChildView.getLayoutParams();
            mOnItemClickListener.onItemClick(this, mChildView, lp.position, lp.itemId);
        }
    }

    private boolean performOnItemLongClick(View mChildView) {
        if (mCommonListener != null) {
            ItemLayoutParams lp = (ItemLayoutParams) mChildView.getLayoutParams();
            return mOnItemClickListener.onItemLongClick(this, mChildView, lp.position, lp.itemId);
        }
        return false;
    }

    /**
     * 设置适配器
     *
     * @param adapter 轮播控件适配器对象
     */
    public void setAdapter(CarouselAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        closeCarousel();

        startIndex = 0;

        mAdapter = adapter;

        mMainView.removeAllViews();
        mMainView.setVisibility(VISIBLE);

        mReserveView.removeAllViews();
        mReserveView.setVisibility(INVISIBLE);

        if (mAdapter != null) {
            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);

            startCarousel(true);
        }

    }

    /**
     * 重构轮播子视图。当设置了新的Adapter时，需要重新构建新的轮播内容视图，
     * 否则复用旧轮播内容视图。
     *
     * @param mAdapter 适配器对象
     * @param rebuild  是否重新构建新的轮播内容视图
     * @return 内容是否足够多，需要开启轮播通知
     */
    private boolean rebuildItemView(@NonNull CarouselAdapter mAdapter, boolean rebuild) {
        int sizeOfPage = mAdapter.getItemViewCountOnSinglePage();

        mMainView.setWeightSum(sizeOfPage);
        mReserveView.setWeightSum(sizeOfPage);

        // 如果需要重建视图或视图尚未初始化，且adapter配置的子视图数量大于1时
        if (rebuild) {
            mMainView.removeAllViews();
            mReserveView.removeAllViews();

            buildItemViewForContain(mMainView, sizeOfPage, mAdapter);
            buildItemViewForContain(mReserveView, sizeOfPage, mAdapter);
        }

        startIndex = 0;

        if (mAdapter.getCount() <= sizeOfPage) {
            fillItemView(mMainView, startIndex, mAdapter.getCount(), sizeOfPage, mAdapter.getCount(), mAdapter);
            return false;
        } else {
            fillItemView(mMainView, startIndex, sizeOfPage, mAdapter.getCount(), mAdapter);
            startIndex += sizeOfPage;
            return true;
        }
    }

    /**
     * 不管Adapter是否能提供超过一页数量的子视图，都先初始化够足够的子视图，其后就只管复用子视图即可
     *
     * @param mContain  要添加子视图的容器
     * @param pageCount 一页的子视图大小
     * @param mAdapter  适配器对象
     */
    private void buildItemViewForContain(LinearLayout mContain, int pageCount, CarouselAdapter mAdapter) {
        for (int i = 0; i < pageCount; i++) {
            View mChildView = mAdapter.createView(mContain);
            setItemViewLayoutParams(mChildView, 0);

            if (mOnItemClickListener != null) {
                mChildView.setOnClickListener(mCommonListener);
                mChildView.setOnLongClickListener(mCommonListener);
            }

            mContain.addView(mChildView);
        }
    }

    /**
     * 填充子视图内容
     *
     * @param mContain  被填充子视图的容器
     * @param start     起始索引值
     * @param pageCount 填充内容的子视图数量等于一页的子视图数量
     * @param amount    子视图总数
     * @param mAdapter  适配器对象
     */
    private void fillItemView(LinearLayout mContain, int start, int pageCount, int amount, CarouselAdapter mAdapter) {
        fillItemView(mContain, start, pageCount, pageCount, amount, mAdapter);
    }

    /**
     * 填充子视图内容
     *
     * @param mContain  被填充子视图的容器
     * @param start     起始索引值
     * @param count     添加的子视图数量
     * @param pageCount 每页有多少个子视图
     * @param amount    子视图总数
     * @param mAdapter  适配器对象
     */
    private void fillItemView(LinearLayout mContain, int start, int count, int pageCount, int amount, CarouselAdapter mAdapter) {

        for (int i = 0; i < count; i++) {
            int itemIndex = (start + i) % amount;
            View mChildView = mContain.getChildAt(i);
            mAdapter.fillView(itemIndex, mChildView);
            setItemViewLayoutParams(mChildView, itemIndex);
            mChildView.setVisibility(VISIBLE);
        }

        // 隐藏多余的视图
        for (int i = count; i < pageCount; i++) {
            View mChildView = mContain.getChildAt(i);
            if (mChildView != null) {
                mChildView.setVisibility(GONE);
            }
        }

    }


    /**
     * 获取轮播间隔时长，单位：毫秒
     *
     * @return 间隔时长
     */
    public int getCarouselInterval() {
        return mCarouselInterval;
    }

    /**
     * 设置轮播间隔时长，单位：好像
     *
     * @param mCarouselInterval 间隔时长
     */
    public void setCarouselInterval(int mCarouselInterval) {
        if (mCarouselInterval <= 0) {
            return;
        }
        if (this.mCarouselInterval == mCarouselInterval) {
            return;
        }
        this.mCarouselInterval = mCarouselInterval;

        closeCarousel();
        startCarousel(false);
    }


    // 是否正在轮播状态的标记变量
    private volatile boolean carouseling = false;

    /**
     * 启动轮播。当重新设置了新的Adapter时，需要重新构建新的视图
     *
     * @param rebuild 是否需要重新构建子视图
     */
    private void startCarousel(boolean rebuild) {
        // 如果已经启动了，就无需再启动
        if (carouseling) {
            return;
        }

        if (mAdapter == null) {
            return;
        }

        startIndex = 0;

        if (rebuildItemView(mAdapter, rebuild)) {
            mUpdateHandler.postDelayed(mCarouselRunnable, mCarouselInterval);
        }

        carouseling = true;
    }

    /**
     * 关闭轮播
     */
    private void closeCarousel() {
        if (carouseling) {
            mUpdateHandler.removeCallbacks(mCarouselRunnable);

            carouseling = false;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mAdapter != null && mDataSetObserver == null) {
            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }

    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            onViewVisible();
        } else {
            onViewInvisible();
        }
    }

    private void onViewVisible() {
        startCarousel(false);
    }

    private void onViewInvisible() {
        closeCarousel();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
            mDataSetObserver = null;
        }

        closeCarousel();
    }

    private void setItemViewLayoutParams(View child, int position) {
        final ViewGroup.LayoutParams vlp = child.getLayoutParams();
        ItemLayoutParams lp;
        if (vlp == null) {
            lp = generateDefaultCarouselLayoutParams();
        } else if (checkLayoutParams(vlp)) {
            lp = (ItemLayoutParams) vlp;
        } else {
            lp = generateCarouselLayoutParams(vlp);
        }

        lp.weight = 1;
        lp.position = position;
        lp.itemId = mAdapter.getItemId(position);
        if (lp != vlp) {
            child.setLayoutParams(lp);
        }
    }

    /**
     * 轮播任务
     */
    private class CarouselRunnable implements Runnable {

        @Override
        public void run() {

            mUpdateHandler.removeCallbacks(mCarouselRunnable);

            if (mAdapter == null || mAdapter.getCount() <= mAdapter.getItemViewCountOnSinglePage()) {
                return;
            }

            startIndex = startIndex % mAdapter.getCount();
            fillItemView(mReserveView, startIndex, mAdapter.getItemViewCountOnSinglePage(), mAdapter.getCount(), mAdapter);
            startIndex += mAdapter.getItemViewCountOnSinglePage();

            mMainView.startAnimation(mOutAnimation);

            mReserveView.setVisibility(VISIBLE);
            mInAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mMainView.setVisibility(INVISIBLE);

                    LinearLayout temp = mMainView;
                    mMainView = mReserveView;
                    mReserveView = temp;

                    mInAnimation.setAnimationListener(null);

                    mUpdateHandler.postDelayed(CarouselRunnable.this, mCarouselInterval);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mReserveView.startAnimation(mInAnimation);

        }
    }

    /**
     * 适配器的观察者
     */
    private class AdapterDataSetObserver extends DataSetObserver {

        // 继续使用旧数据集
        @Override
        public void onChanged() {
            super.onChanged();

            mMainView.setVisibility(VISIBLE);
            mReserveView.setVisibility(INVISIBLE);

            closeCarousel();
            startCarousel(false);
        }

        // 清空数据集重建
        @Override
        public void onInvalidated() {
            super.onInvalidated();

            mMainView.setVisibility(VISIBLE);
            mReserveView.setVisibility(INVISIBLE);

            closeCarousel();
            startCarousel(true);
        }

    }

    private class CommonCallbackListener implements OnClickListener, OnLongClickListener {

        @Override
        public void onClick(View view) {
            performOnItemClick(view);
        }

        @Override
        public boolean onLongClick(View view) {
            return performOnItemLongClick(view);
        }
    }

    private ItemLayoutParams generateDefaultCarouselLayoutParams() {
        return new ItemLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0);
    }

    private ItemLayoutParams generateCarouselLayoutParams(ViewGroup.LayoutParams p) {
        return new ItemLayoutParams(p);
    }

    private boolean checkItemLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof ItemLayoutParams;
    }

    /**
     * 子视图添加到父容器中的布局参数
     */
    public static class ItemLayoutParams extends LinearLayout.LayoutParams {

        // 在公告列表中位置索引
        int position;
        // 该位置索引数据对应在Adapter中的ID
        long itemId;

        public ItemLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public ItemLayoutParams(int width, int height) {
            super(width, height);
        }

        public ItemLayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public ItemLayoutParams(int width, int height, long itemId) {
            super(width, height);
            this.itemId = itemId;
        }
    }

}
