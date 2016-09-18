package com.yunxian.carousel.app.utils;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;

/**
 * 基础的FragmentActivity类，封装了一些基本常用的方法，如无特殊需求可对此类进行继承扩展。
 *
 * @author A Shuai
 */
public abstract class BaseFragmentActivity extends FragmentActivity {

    /**
     * FragmentManager对象 *
     */
    protected FragmentManager mFragmentManager;

    /**
     * 公共回调监听器对象，子类可直接使用。根据自身情况自行覆写{@link #onViewClick(View)}、
     * {@link #onViewLongClick(View)}、{@link #onAdapterViewItemClick(AdapterView, View, int, long)}、
     * {@link #onAdapterViewItemLongClick(AdapterView, View, int, long)}这四个方法即可。
     */
    protected final CommonCallbackListener mCommonListener = new CommonCallbackListener();

    /**
     * 不提供覆写本方法，若需覆写请覆写{@link #onCreateImpl(Bundle)}
     *
     * @param savedInstanceState 用于状态恢复的Bundle数据集
     */
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentManager = getSupportFragmentManager();

        onCreateImpl(savedInstanceState);
        onSetContentView();
        onFindViews();
        onBindContent();

    }


    /**
     * 设置布局
     */
    protected abstract void onSetContentView();

    /**
     * 查找必要的子视图控件
     */
    protected abstract void onFindViews();

    /**
     * 将数据与视图进行绑定，以展示数据
     */
    protected abstract void onBindContent();

    /**
     * 用于替代{@link #onCreate(Bundle)}方法
     *
     * @param savedInstanceState 用于状态恢复的Bundle数据集
     */
    protected void onCreateImpl(Bundle savedInstanceState) {

    }

    /**
     * Back键按下回调事件
     *
     * @return true 表示事件被消费
     */
    protected abstract boolean onBackKeyDown();

    /**
     * Menu键按下的回调事件，各子类Activity可根据需要自行覆写该方法即可
     *
     * @return ture 表示事件被消费
     */
    protected boolean onMenuKeyDown() {
        return false;
    }

    /**
     * 禁止覆写此方法，
     * 需要监听返回按钮和菜单按钮事件的请覆写{@link #onBackKeyDown()}和{@link #onMenuKeyDown()}；
     * 需要监听另外的物理按钮请覆写{@link #onKeyDown(int, KeyEvent, int)}方法
     */
    @Override
    public final boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (onBackKeyDown()) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_MENU:
                if (onMenuKeyDown()) {
                    return true;
                }
                break;
            default:
                break;
        }
        return onKeyDown(keyCode, event, 1);
    }

    /**
     * 物理按键事件的处理方法，flag标示的形参不具有任何意义，
     * 仅作与{@link #onKeyDown(int, KeyEvent)}的区分
     *
     * @param keyCode 按键事件代码
     * @param event   事件对象
     * @param flag    无意义，仅作为区分标志位
     * @return true表示事件被处理
     */
    public boolean onKeyDown(int keyCode, KeyEvent event, int flag) {
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 从当前布局中查询指定ID控件，消除了类型转换的麻烦
     *
     * @param id  指定控件的ID
     * @param <T> 待进行类型转换的目标类型
     * @return 返回指定ID和指定目标类型的视图
     */
    @SuppressWarnings("unchecked")
    protected final <T extends View> T findView(int id) {
        try {
            return (T) findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }

    public void onViewClick(View v) {
    }

    public boolean onViewLongClick(View v) {
        return false;
    }

    public void onAdapterViewItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    public boolean onAdapterViewItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    /**
     * 公共回调监听器
     */
    private class CommonCallbackListener implements View.OnClickListener, View.OnLongClickListener,
            AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

        @Override
        public void onClick(View v) {
            onViewClick(v);
        }

        @Override
        public boolean onLongClick(View v) {
            return onViewLongClick(v);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            onAdapterViewItemClick(parent, view, position, id);
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            return onAdapterViewItemLongClick(parent, view, position, id);
        }

    }

}
