package com.busterlo.app.calculation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;


/**
 *
 * Description:显示隐藏布局的属性动画(铺展)
 * Date:2017/3/30.
 */
public class HiddenAnimUtils {

    private int mHeight;//伸展高度

    private View hideView,down;//需要展开隐藏的布局，开关控件

    private RotateAnimation animation;//旋转动画

    private int duration_rotate,duration_hide;
    /**
     * 构造器(可根据自己需要修改传参)
     * @param context 上下文
     * @param hideView 需要隐藏或显示的布局view
     * @param down 按钮开关的view
     * @param height 布局展开的高度(根据实际需要传)
     *
     */
    public static HiddenAnimUtils newInstance(Context context,View hideView,View down,int height){
        return new HiddenAnimUtils(context,hideView,down,height);
    }

    private HiddenAnimUtils(Context context,View hideView,View down,int height){
        this.hideView = hideView;
        this.down = down;
        this.duration_hide = 300;
        this.duration_rotate = 300;
//        float mDensity = context.getResources().getDisplayMetrics().density;
//        mHeight = (int) (mDensity * height + 0.5);//伸展高度
        mHeight = (int) (height);//伸展高度
    }
    /**
     * 修改动画时间
     */
    private void setDurationRotate(int ms){
        this.duration_rotate = ms;
    }
    private void setDurationHide(int ms){
        this.duration_hide =  ms;
    }
    /**
     * 开关
     */
    public void toggle(){
        startAnimation();
        if (View.VISIBLE == hideView.getVisibility()) {
            closeAnimate(hideView);//布局隐藏
        } else {
            openAnim(hideView);//布局铺开
        }
    }

    /**
     * 开关旋转动画
     */
    private void startAnimation() {
        if (View.VISIBLE == hideView.getVisibility()) {
            animation = new RotateAnimation(-90, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            animation = new RotateAnimation(0, -90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        animation.setDuration(this.duration_rotate);//设置动画持续时间
        animation.setInterpolator(new LinearInterpolator());
//        animation.setRepeatMode(Animation.REVERSE);//设置反方向执行
        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        down.startAnimation(animation);
    }

    private void openAnim(View v) {
        v.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(v, 0, mHeight);
        animator.start();
    }

    private void closeAnimate(final View view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
        animator.start();
    }

    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(this.duration_hide);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}

