package com.yunxian.carousel.app.utils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

/**
 * 基础的Fragment类
 * 
 * @author A Shuai
 *
 */
public abstract class BaseFragment extends Fragment {

    //可供子类使用的Context对象
    protected Activity mBaseActivity;

    protected FragmentManager mFragmentManager;

    //此Fragment提供的视图的根视图
    private View mRootView;

    @Override
    public void onAttach(Activity mActivity) {
        super.onAttach(mActivity);
        this.mBaseActivity = mActivity;
    }

    /**
     * 子类不可覆写此方法，以免破坏onCreate的初始化逻辑
     * 子类可覆写{@link #onCreateImpl(Bundle)}方法完成自定义的初始化逻辑
     *
     * @param savedInstanceState 用于恢复状态的Bundle数据集
     */
    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if( mBaseActivity == null ){
            mBaseActivity = getActivity();
        }

        mFragmentManager = getChildFragmentManager();

        onCreateImpl(savedInstanceState);

        onInitParameter();
    }

    /**
     * 子类可覆写此方法完成自定义的初始化逻辑
     *
     * @param savedInstanceState 用于恢复状态的Bundle数据集
     */
    public void onCreateImpl(Bundle savedInstanceState) {
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = onInflaterRootView(inflater, container, savedInstanceState);
            if (mRootView == null) {
                throw new NullPointerException("the root view should not be null");
            }
            onFindViews(mRootView);
        } else {
            ViewGroup mRootParent = (ViewGroup) mRootView.getParent();
            if (mRootParent != null) {
                mRootParent.removeView(mRootView);
            }
        }

        onBindContent();

        return mRootView;
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        mBaseActivity = null;
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化一些必要的参数
     * 需要初始化参数的子类自行覆写
     */
    protected void onInitParameter() {
        
    }

    /**
     * 请务必在此方法中返回此Fragment提供的根视图，返回结果不可为空
     *
     * @param inflater           用于实例化layout文件的Inflater
     * @param container          父容器
     * @param savedInstanceState 有可能为空，使用之前请先进行判断
     * @return 不可为空
     */
    protected abstract View onInflaterRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 根据提供的根视图，找到当前页面需要使用的一些视图控件
     *
     * @param mRootView 提供给子类查找视图控件所用
     */
    protected abstract void onFindViews(View mRootView);

    /**
     * 将数据与视图控件进行绑定，以显示内容
     */
    protected abstract void onBindContent();
    
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

}
