package com.busterlo.app.calculation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fm= getSupportFragmentManager();
    Button btn001 ;
    Rect fab_loc = new Rect();
    Rect chicken_loc = new Rect();
    int chickenWidth = 0,chickenHeight = 0;
    int fabWidth = 0,fabHeight = 0;
    long exitTime;//退出时间
    Boolean start = false;
    ImageView chicken;
    Fragment fragmentMain,fragmentSlide;

    protected void onCreate(Bundle savedInstanceState) {
        fm.findFragmentById(R.id.frame);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        chicken = (ImageView) findViewById(R.id.im_chicken);


//        getSupportActionBar().show();
//        getSupportActionBar().hide();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {

//            Intent i= new Intent(Intent.ACTION_MAIN);  //主启动，不期望接收数据
//
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);       //新的activity栈中开启，或者已经存在就调到栈前
//
//            i.addCategory(Intent.CATEGORY_HOME);            //添加种类，为设备首次启动显示的页面
//
//            startActivity(i);s
            if (System.currentTimeMillis() - exitTime > 1000) {
                Toast.makeText(this, "已经屏蔽返回键，再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                this.finish();
            }
            return;
        }
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode==KeyEvent.KEYCODE_BACK){
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
//        FragmentManager fm= getSupportFragmentManager();
//        Fragment fragment = fm.findFragmentById(R.id.frame);

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Toast.makeText( this,"clicked camera", LENGTH_SHORT).show();
            if(fragmentMain == null)
                fragmentMain = new CompareFragment();
            fm.beginTransaction().replace(R.id.frame,fragmentMain)
                    .commit();
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this,MapActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            Toast.makeText( this,"Slide Test", LENGTH_SHORT).show();
            if(fragmentSlide == null)
                fragmentSlide = new SlideFragment();
            fm.beginTransaction().replace(R.id.frame,fragmentSlide)
                    .commit();

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}


