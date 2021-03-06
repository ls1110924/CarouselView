package com.yunxian.carousel.app;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yunxian.carousel.BaseCarouselAdapter;
import com.yunxian.carousel.CarouselView;
import com.yunxian.carousel.app.adapter.TestCustomAdapter;
import com.yunxian.carousel.app.adapter.TestCustomTaobaoAdapter;
import com.yunxian.carousel.app.adapter.TestJustEnoughAdapter;
import com.yunxian.carousel.app.adapter.TestNotEnoughAdapter;
import com.yunxian.carousel.app.adapter.TestOneLineAdapter;
import com.yunxian.carousel.app.adapter.TestTwoLineEnoughAdapter;
import com.yunxian.carousel.app.adapter.TestTwoLineNotEnoughAdapter;
import com.yunxian.carousel.app.utils.BaseFragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author A Shuai
 * @email ls1110924@163.com
 * @date 2016/9/19 0:30
 */
public class TestCarouselActivity extends BaseFragmentActivity {

    private static final String TAG = TestCarouselActivity.class.getSimpleName();

    private CarouselView mCarouselViewOne;
    private CarouselView mCarouselViewTwo;
    private CarouselView mCarouselViewThree;
    private CarouselView mCarouselViewFour;
    private CarouselView mCarouselViewFive;
    private CarouselView mCarouselViewSix;
    private CarouselView mCarouselViewSeven;

    private int startIndex;
    private final List<String> mCustomData = new ArrayList<>();
    private BaseCarouselAdapter mCustomAdapter;

    private final CommonCallbackListener mCommonListener = new CommonCallbackListener();

    @Override
    protected void onSetContentView() {
        setContentView(R.layout.activity_test_carousel);
    }

    @Override
    protected void onFindViews() {
        mCarouselViewOne = findView(R.id.carousel_one);
        mCarouselViewTwo = findView(R.id.carousel_two);
        mCarouselViewThree = findView(R.id.carousel_three);
        mCarouselViewFour = findView(R.id.carousel_four);
        mCarouselViewFive = findView(R.id.carousel_five);
        mCarouselViewSix = findView(R.id.carousel_six);
        mCarouselViewSeven = findView(R.id.carousel_seven);
        findViewById(R.id.btn).setOnClickListener(mBaseCommonListener);
        findViewById(R.id.btn_notify).setOnClickListener(mBaseCommonListener);
    }

    @Override
    protected void onBindContent() {
        mCarouselViewOne.setAdapter(new TestNotEnoughAdapter(this));
        mCarouselViewTwo.setAdapter(new TestJustEnoughAdapter(this));
        mCarouselViewThree.setAdapter(new TestOneLineAdapter(this));
        mCarouselViewFour.setAdapter(new TestTwoLineEnoughAdapter(this));
        mCarouselViewFive.setAdapter(new TestTwoLineNotEnoughAdapter(this));

        startIndex = 0;
        for (int i = startIndex; i < startIndex + 3; i++) {
            mCustomData.add(String.format(Locale.US, "Test--->%1$d", i));
        }
        startIndex += 3;
        mCustomAdapter = new TestCustomAdapter(this, mCustomData);
        mCarouselViewSix.setAdapter(mCustomAdapter);

        mCarouselViewSeven.setAdapter(new TestCustomTaobaoAdapter(this));
        mCarouselViewSeven.setOnItemClickListener(mCommonListener);
    }

    @Override
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.btn: {
                Intent mIntent = new Intent(this, EmptyActivity.class);
                startActivity(mIntent);
                break;
            }
            case R.id.btn_notify: {
                for (int i = startIndex; i < startIndex + 2; i++) {
                    mCustomData.add(String.format(Locale.US, "Test--->%1$d", i));
                }
                startIndex += 2;
                mCustomAdapter.notifyDataSetChanged();
                break;
            }
            default:
                super.onViewClick(v);
                break;
        }
    }

    @Override
    protected boolean onBackKeyDown() {
        finish();
        return true;
    }

    private class CommonCallbackListener implements CarouselView.OnItemClickListener {

        @Override
        public void onItemClick(CarouselView parent, View view, int position, long id) {
            Log.d(TAG, "Click--->" + position);
            Toast.makeText(TestCarouselActivity.this, "Click--->" + position, Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onItemLongClick(CarouselView parent, View view, int position, long id) {
            Log.d(TAG, "LongClick--->" + position);
            return true;
        }
    }

}
