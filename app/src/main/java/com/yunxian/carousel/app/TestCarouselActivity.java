package com.yunxian.carousel.app;

import android.content.Intent;
import android.view.View;

import com.yunxian.carousel.BaseCarouselAdapter;
import com.yunxian.carousel.CarouselView;
import com.yunxian.carousel.app.adapter.TestCustomAdapter;
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

    private CarouselView mCarouselViewOne;
    private CarouselView mCarouselViewTwo;
    private CarouselView mCarouselViewThree;
    private CarouselView mCarouselViewFour;
    private CarouselView mCarouselViewFive;
    private CarouselView mCarouselViewSix;

    private int startIndex;
    private final List<String> mCustomData = new ArrayList<>();
    private BaseCarouselAdapter mCustomAdapter;

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
        findViewById(R.id.btn).setOnClickListener(mBaseCommonListener);
        findViewById(R.id.btn_notify).setOnClickListener(mBaseCommonListener);
    }

    @Override
    protected void onBindContent() {
//        mCarouselViewOne.setAdapter(new TestNotEnoughAdapter(this));
//        mCarouselViewTwo.setAdapter(new TestJustEnoughAdapter(this));
//        mCarouselViewThree.setAdapter(new TestOneLineAdapter(this));
//        mCarouselViewFour.setAdapter(new TestTwoLineEnoughAdapter(this));
//        mCarouselViewFive.setAdapter(new TestTwoLineNotEnoughAdapter(this));

        startIndex = 0;
        for (int i = startIndex; i < startIndex + 2; i++) {
            mCustomData.add(String.format(Locale.US, "Test--->%1$d", i));
        }
        startIndex += 2;
        mCustomAdapter = new TestCustomAdapter(this, mCustomData);
        mCarouselViewSix.setAdapter(mCustomAdapter);
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


}
