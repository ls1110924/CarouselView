package com.yunxian.carousel.app.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.SoftReference;

/**
 * 子类如果作为内部类，则需使用静态内部类，
 * 覆写onMessageExecute方法进行处理消息即可。
 *
 * @param <T> 任意类型
 * @author A Shuai
 */
public abstract class AbsHandler<T> extends Handler {

    private final SoftReference<T> mParaRef;

    /**
     * 使用当前线程所关联的Looper对象
     *
     * @param mPara T类型参数
     */
    public AbsHandler(T mPara) {
        super(Looper.getMainLooper());
        mParaRef = new SoftReference<>(mPara);
    }

    /**
     * 使用指定的Looper对象进行构造
     *
     * @param mPara   T类型参数
     * @param mLooper Looper对象
     */
    public AbsHandler(T mPara, Looper mLooper) {
        super(mLooper);
        mParaRef = new SoftReference<>(mPara);
    }

    /**
     * 不可覆写，若对需对消息处理可对{@link #handleMessage(T, Message, Bundle)}进行覆写
     *
     * @param msg Message消息对象
     */
    @Override
    public final void handleMessage(Message msg) {
        T mPara = mParaRef.get();
        if (mPara == null) {
            return;
        }
        handleMessage(mPara, msg, msg.getData());
    }

    /**
     * 主要的消息处理逻辑
     *
     * @param mPara   类型参数T所指定的Activity对象
     * @param msg     Message消息对象
     * @param mBundle 可以为null，子类使用时需注意
     */
    protected abstract void handleMessage(T mPara, Message msg, Bundle mBundle);

}

