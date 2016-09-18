package com.yunxian.carousel.app;

import com.yunxian.carousel.CarouselView;
import com.yunxian.carousel.app.adapter.TestJustEnoughAdapter;
import com.yunxian.carousel.app.adapter.TestNotEnoughAdapter;
import com.yunxian.carousel.app.adapter.TestOneLineAdapter;
import com.yunxian.carousel.app.adapter.TestTwoLineEnoughAdapter;
import com.yunxian.carousel.app.adapter.TestTwoLineNotEnoughAdapter;
import com.yunxian.carousel.app.utils.BaseFragmentActivity;

/**
 * @author A Shuai
 * @email lishuai.ls@alibaba-inc.com
 * @date 2016/9/19 0:30
 */
public class TestCarouselActivity extends BaseFragmentActivity {

    private CarouselView mCarouselViewOne;
    private CarouselView mCarouselViewTwo;
    private CarouselView mCarouselViewThree;
    private CarouselView mCarouselViewFour;
    private CarouselView mCarouselViewFive;

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
    }

    @Override
    protected void onBindContent() {
        mCarouselViewOne.setAdapter(new TestNotEnoughAdapter(this));
        mCarouselViewTwo.setAdapter(new TestJustEnoughAdapter(this));
        mCarouselViewThree.setAdapter(new TestOneLineAdapter(this));
        mCarouselViewFour.setAdapter(new TestTwoLineEnoughAdapter(this));
        mCarouselViewFive.setAdapter(new TestTwoLineNotEnoughAdapter(this));
    }

    @Override
    protected boolean onBackKeyDown() {
        finish();
        return true;
    }


}
