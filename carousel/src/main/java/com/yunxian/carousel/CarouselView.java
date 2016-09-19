package com.yunxian.carousel;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * @author A Shuai
 * @email lishuai.ls@alibaba-inc.com
 * @date 2016/9/18 17:03
 */
public class CarouselView extends FrameLayout {

    private static final String TAG = CarouselView.class.getSimpleName();

    // 默认轮播间隔时长5秒钟
    private static final int CAROUSEL_INTERVAL = 5000;

    // 主视图
    private LinearLayout mMainView;
    // 备用视图
    private LinearLayout mReserveView;

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

        mInAnimation = AnimationUtils.loadAnimation(context, R.anim.carousel_slide_bottom_in);
        mOutAnimation = AnimationUtils.loadAnimation(context, R.anim.carousel_slide_bottom_out);

        init(context);
    }

    private void init(Context context) {
        mMainView = new LinearLayout(context);
        mMainView.setOrientation(LinearLayout.VERTICAL);
        addView(mMainView,
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        mReserveView = new LinearLayout(context);
        mReserveView.setOrientation(LinearLayout.VERTICAL);
        mReserveView.setVisibility(INVISIBLE);
        addView(mReserveView,
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        mCarouselInterval = CAROUSEL_INTERVAL;
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
        mMainView.setWeightSum(mAdapter.getItemViewCountOnSinglePage());
        mReserveView.setWeightSum(mAdapter.getItemViewCountOnSinglePage());

        startIndex = 0;

        if (mAdapter.getCount() <= mAdapter.getItemViewCountOnSinglePage()) {

            if (rebuild) {
                buildItemViewForContain(mMainView, startIndex, mAdapter.getCount(), mAdapter.getCount(), mAdapter);
            } else {
                fillItemView(mMainView, startIndex, mAdapter.getCount(), mAdapter.getCount(), mAdapter);
            }

            return false;
        } else {
            final int pageCount = mAdapter.getItemViewCountOnSinglePage();

            if (rebuild) {
                buildItemViewForContain(mMainView, startIndex, pageCount, mAdapter.getCount(), mAdapter);
                startIndex += pageCount;
                buildItemViewForContain(mReserveView, startIndex, pageCount, mAdapter.getCount(), mAdapter);
            } else {
                fillItemView(mMainView, startIndex, pageCount, mAdapter.getCount(), mAdapter);
                startIndex += pageCount;
            }

            return true;
        }
    }

    /**
     * 构建子视图
     *
     * @param mContain 被填充子视图的容器
     * @param start    起始索引值
     * @param count    添加的子视图数量
     * @param amount   子视图总数
     * @param mAdapter 适配器对象
     */
    private void buildItemViewForContain(LinearLayout mContain, int start, int count, int amount, CarouselAdapter mAdapter) {

        for (int i = 0; i < count; i++) {
            int itemIndex = (start + i) % amount;
            View mChildView = mAdapter.getView(itemIndex, null, mContain);
            if (mChildView == null) {
                throw new NullPointerException();
            }
            setItemViewLayoutParams(mChildView, itemIndex);
            mContain.addView(mChildView);
        }

    }

    /**
     * 填充子视图内容
     *
     * @param mContain 被填充子视图的容器
     * @param start    起始索引值
     * @param count    添加的子视图数量
     * @param amount   子视图总数
     * @param mAdapter 适配器对象
     */
    private void fillItemView(LinearLayout mContain, int start, int count, int amount, CarouselAdapter mAdapter) {

        for (int i = 0; i < count; i++) {
            int itemIndex = (start + i) % amount;
            View mChildView = mContain.getChildAt(i);
            mChildView = mAdapter.getView(itemIndex, mChildView, mContain);
            if (mChildView == null) {
                throw new NullPointerException();
            }
            setItemViewLayoutParams(mChildView, itemIndex);
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
            mCarouselInterval = CAROUSEL_INTERVAL;
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

        @Override
        public void onChanged() {
            super.onChanged();
            reinit();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            reinit();
        }

        void reinit() {

        }
    }

    private CarouselView.ItemLayoutParams generateDefaultCarouselLayoutParams() {
        return new CarouselView.ItemLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0);
    }

    private CarouselView.ItemLayoutParams generateCarouselLayoutParams(ViewGroup.LayoutParams p) {
        return new ItemLayoutParams(p);
    }

    private boolean checkItemLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof CarouselView.ItemLayoutParams;
    }

    /**
     * 子视图添加到父容器中的布局参数
     */
    public static class ItemLayoutParams extends LinearLayout.LayoutParams {

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
