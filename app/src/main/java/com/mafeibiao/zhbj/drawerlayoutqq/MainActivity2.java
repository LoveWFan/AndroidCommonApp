package com.mafeibiao.zhbj.drawerlayoutqq;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mafeibiao.zhbj.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 仿QQ6.6.0版本侧滑效果
 * <p>
 * DrawerLayout内容偏移，背景动画，主页面导航图标渐变，状态栏渐变色.
 * <p>
 * Created by Mjj on 2016/12/7.
 */
public class MainActivity2 extends AppCompatActivity {

    private SystemBarTintManager tintManager;
    private DrawerLayout mDrawerLayout;
    private ImageView ivNavigation;

    private ImageView ivDrawerBg; // 侧边有动画效果的背景图片

    private TextView tvCenter, tvRight;
    private ViewPager body;
    private ListView listView;
    private String strings[] = {"开通会员", "QQ钱包", "个性装扮", "我的收藏", "我的相册", "我的文件", "我的日程", "我的名片夹"};


    private CustomRadioGroup footer;
    private int[] itemImage={R.drawable.library,R.drawable.approve,R.drawable.aboutme};
    private int[] itemCheckedImage={R.drawable.library1,R.drawable.approve1,R.drawable.aboutme1};
    private String[] itemText={"文件库","审批","关于我"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(Color.parseColor("#14B6F6"));
        }
        setContentView(R.layout.activity_main_2);
        initView();
    }

    private void initView() {

        // 底部
        footer=(CustomRadioGroup)findViewById(R.id.main_footer);

        for (int i = 0; i < itemImage.length; i++) {
            footer.addItem(itemImage[i],itemCheckedImage[i],itemText[i]);
        }

        //主体
        body=(ViewPager)findViewById(R.id.main_body);
        body.setAdapter(new BodyPageAdapter());
        final MainBodyPageChangeListener bodyChangeListener=new MainBodyPageChangeListener(footer);

        body.addOnPageChangeListener(bodyChangeListener);

        footer.setCheckedIndex(body.getCurrentItem());
        footer.setOnItemChangedListener(new CustomRadioGroup.OnItemChangedListener() {
            public void onItemChanged() {
                body.setCurrentItem(footer.getCheckedIndex(),false);
            }
        });
        /**
         * BUG :显示不出数字。数字尺寸太大
         */
        footer.setItemNewsCount(1, 10);//设置消息数量


        ivNavigation = (ImageView) findViewById(R.id.iv_navigation_icon);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawer_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ivDrawerBg = (ImageView) findViewById(R.id.iv_drawer_bg);
            float curTranslationY = ivDrawerBg.getTranslationY();
            ObjectAnimator animator = ObjectAnimator.ofFloat(ivDrawerBg, "translationY", curTranslationY,
                    -70f, 60, curTranslationY);
            animator.setDuration(5000);
            animator.setRepeatCount(ObjectAnimator.INFINITE);
            animator.start();
        }


        tvCenter = (TextView) findViewById(R.id.tv_center);
        tvRight = (TextView) findViewById(R.id.tv_right);


        //设置监听
        ivNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });

        listView = (ListView) mDrawerLayout.findViewById(R.id.id_draw_menu_item_list_select);
        listView.setAdapter(new ArrayAdapter<String>(MainActivity2.this, android.R.layout.simple_list_item_1, strings));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity2.this, strings[i], Toast.LENGTH_SHORT).show();
            }
        });

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {

            /**
             * @param drawerView
             * @param slideOffset   偏移(0-1)
             */
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // 导航图标渐变效果
                ivNavigation.setAlpha(1 - slideOffset);
                // 判断是否左菜单并设置移动(如果不这样设置,则主页面的内容不会向右移动)
                if (drawerView.getTag().equals("left")) {
                    View content = mDrawerLayout.getChildAt(0);
                    int offset = (int) (drawerView.getWidth() * slideOffset);
                    content.setTranslationX(offset);

                    // 缩放效果(之前QQ效果)
//                    content.setTranslationX(1 - slideOffset * 0.5f);
//                    content.setTranslationY(1 - slideOffset * 0.5f);
                }

                if (tintManager != null){
                    tintManager.setStatusBarAlpha(1 - slideOffset);
                }

            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            /**
             * 当抽屉滑动状态改变的时候被调用
             * 状态值是STATE_IDLE（闲置-0），STATE_DRAGGING（拖拽-1），STATE_SETTLING（固定-2）中之一。
             * 抽屉打开的时候，点击抽屉，drawer的状态就会变成STATE_DRAGGING，然后变成STATE_IDLE.
             *
             * @param newState
             */
            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    // 设置状态栏透明状态
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 自定义NavigationIcon设置关联DrawerLayout
     */
    private void toggle() {
        int drawerLockMode = mDrawerLayout.getDrawerLockMode(GravityCompat.START);
        if (mDrawerLayout.isDrawerVisible(GravityCompat.START)
                && (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_OPEN)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }



    class BodyPageAdapter extends PagerAdapter {

        private RecyclerView mRecyclerView;
        private int[] pageLayouts={R.layout.main_body_page_a,R.layout.main_body_page_b,R.layout.main_body_page_c};
        private List<View> lists=new ArrayList<View>();

        public BodyPageAdapter() {
            for (int i = 0; i < pageLayouts.length; i++) {
                View v=getLayoutInflater().inflate(pageLayouts[i], null);
                lists.add(v);
            }
        }
        public int getCount() {
            return lists.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0==arg1;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v=lists.get(position);


            container.addView(v);
            return v;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(lists.get(position));
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        body.clearOnPageChangeListeners();
    }
}
