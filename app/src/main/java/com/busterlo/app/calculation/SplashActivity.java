package com.busterlo.app.calculation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;

import android.graphics.Rect;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



/**
 * Created by 62361 on 2018/5/31.
 */


public class SplashActivity extends Activity {
//    Rect fab_loc = new Rect();
    Rect chicken_loc = new Rect();
    int chickenWidth = 0,chickenHeight = 0;
    int fabWidth = 0,fabHeight = 0;
    int screenHeight,screenWidth;
    long exitTime;//退出时间
    TextView tv_bg,tv_presstext;
    Boolean start = false;
    ImageView chicken;
    RelativeLayout rl_bg;
    ObjectAnimator rotate;
    Handler startNSyn;
//    FloatingActionButton fab_plus;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade().setDuration(500));
        }
        chicken = (ImageView) findViewById(R.id.im_chicken);
        final Intent intent = new Intent(this, MainActivity.class);
        rl_bg = (RelativeLayout)findViewById(R.id.rl_splash);
        tv_bg = (TextView)findViewById(R.id.tv_bg);
        tv_presstext = (TextView)findViewById(R.id.tv_longpress);
        tv_presstext.setText("长\n按");
//        new Handler().postDelayed(new Runnable(){
//            public void run(){
//                startOtherActivity(chicken);
//            }
//        },500);

        chicken.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    GuiUtils.animateRevealShow(SplashActivity.this, tv_bg, 0,
                            R.color.colorAccent, new GuiUtils.OnRevealAnimationListener() {
                                @Override
                                public void onRevealHide() {
                                    //放这里没用
                                }
                                @Override
                                public void onRevealShow() {

                                }
                            });
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        rotate.pause();
                    }
                    rotate.cancel();
                    startNSyn = new Handler();
                    startNSyn.post(new Runnable() {
                        @Override
                        public void run() {
                            GuiUtils.animateRevealHide(SplashActivity.this, tv_bg, 0,
                                    R.color.colorAccent, new GuiUtils.OnRevealAnimationListener() {
                                        @Override
                                        public void onRevealHide() {

                                        }
                                        @Override
                                        public void onRevealShow() {
                                            //放这里没用
                                        }
                                    });
                        }
                    });
                    startOtherActivity(chicken);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1000);

                }
                return false;
            }
        });

}

    @Override
    protected void onDestroy() {
        if(startNSyn!=null){
            startNSyn.removeCallbacksAndMessages(null);
        }
        super.onDestroy();

    }

    public void startOtherActivity(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, view, view.getTransitionName());
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation( getWindow().setExitTransition(new Fade().setDuration(2000)));
            //getWindow().setExitTransition(new Fade().setDuration(500));

            //ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(new Intent(this, MainActivity.class),options.toBundle());
            overridePendingTransition(0,0);

//            this.finish();
            //this.finishAfterTransition();
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    /**
    /**
     * 重写返回键，实现双击退出效果
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 1000) {
                Toast.makeText(this, "已经屏蔽返回键，再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
//        fab_plus.getGlobalVisibleRect(fab_loc);
        chicken.getGlobalVisibleRect(chicken_loc);
        chickenWidth = chicken.getWidth();
        chickenHeight = chicken.getHeight();
//        fabWidth = fab_plus.getWidth();
//        fabHeight = fab_plus.getHeight();

        if(chicken_loc.centerX() != 0 && chicken_loc.centerY() != 0 && start == false) {
            start = true;
            initAnim();
        }
    }

    private void initAnim() {

        //获取屏幕高度
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        final int screenHeight = metrics.heightPixels;
        final int screenWidth = metrics.widthPixels;
        System.out.println("windows h&w " + screenHeight + screenWidth);

        System.out.println("chickenWidth" + chickenWidth + "chickenHeight" + chickenHeight);
//        System.out.println("fab" + fab_loc.centerX() + "y:" + fab_loc.centerY());
        System.out.println("chicken x:" + chicken_loc.centerX() + "y:" + chicken_loc.centerY());
        System.out.println("chicken getY()" + chicken.getY() + "transY="  );
        final float transY  = (float)(screenHeight*0.5 - chicken_loc.centerY());
        //ivLogo向上移动 transY 的距离
        ObjectAnimator transYLogo = ObjectAnimator.ofFloat(chicken, "translationY", 0, transY);
//        ObjectAnimator transXLogo = ObjectAnimator.ofFloat(chicken, "translationX", 0, transX);
        //ivLogo在X轴和Y轴上都缩放0.75倍
        ObjectAnimator scaleXLogo = ObjectAnimator.ofFloat(chicken, "scaleX", 1f, (float) fabWidth / chickenWidth);
        ObjectAnimator scaleYLogo = ObjectAnimator.ofFloat(chicken, "scaleY", 1f, (float) fabHeight / chickenHeight);
//        Animation rotateAnimation = new RotateAnimation(R.anim.icon_rotatation);
//        chicken.startAnimation(rotateAnimation);
        ObjectAnimator rotateLogo = ObjectAnimator.ofFloat(chicken,"Rotation",0f,360f);
        AnimatorSet logoAnim = new AnimatorSet();
        logoAnim.setDuration(1000);
        logoAnim.play(transYLogo);
        logoAnim.setInterpolator(new BounceInterpolator());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            logoAnim.start();
        }

        chicken.setY(screenHeight / 2 - chickenHeight / 2 );

        logoAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //旋转动画
                rotate = ObjectAnimator.ofFloat(chicken,"Rotation",0f,360f);
                rotate.setRepeatCount(-1);
                rotate.setDuration(3000);
                LinearInterpolator lin = new LinearInterpolator();
                rotate.setInterpolator(lin);
                rotate.start();
                //
                ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(tv_presstext, "Alpha", 0,1,0.8f,1,0);
                alphaAnim.setDuration(3000);
                alphaAnim.setRepeatCount(-1);
                alphaAnim.start();
                tv_presstext.setVisibility(View.VISIBLE);

                //待ivLogo的动画结束后,开始播放底部注册、登录按钮的动画
            }
        });
    }


}
