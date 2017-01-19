package self.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.List;

/**
 * AutoDirectionalViewPager（四方体） 本质其实就是一个ViewPager，继承该类做了一个我们所需要的四方体实现。
 * 所以普通的viewpager怎么添加和显示数据都是一样的，没有任何区别，如果不需要改变当前实现效果不需要改动此类的实现。
 */
public class MainActivity extends FragmentActivity {
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AutoDirectionalViewPager viewPager = (AutoDirectionalViewPager) findViewById(R.id.viewpager);
        //AutoDirectionalViewPager 继承了InfiniteViewPager 无限滑动均在此类实现配合InfinitePagerAdapter达到无线滑动效果。
        InfinitePagerAdapter infinitePagerAdapter = new InfinitePagerAdapter(new MyPagerAdapter(getSupportFragmentManager(), fillData()));
        viewPager.setAdapter(infinitePagerAdapter);

    }

    /**
     * 这里只是假设有六条数据，实际上和普通viewpager一样可以加载任意多的数据。不用担心内存泄漏的问题（FragmentStatePagerAdapter已经处理了）。
     * @return
     */
    private List<Fragment> fillData() {
        //这里是控制每个页面的圆角半径
        //黄色圆角半径请在round_corner_background.xml里设置。
        int redius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                5, getResources().getDisplayMetrics());

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(VideoFragment.newInstance(redius));
        fragments.add(ImageFragment.newInstance(redius));
        fragments.add(VideoFragment.newInstance(redius));
        fragments.add(ImageFragment.newInstance(redius));
        return fragments;
    }

    /**
     * FragmentStatePagerAdapter 是一个内存优化设计，实际他不会加载所有的数据。这样的好处就是不会占用大量内存。
     * 所以一个四方体并不是加载全部的面，在滑动的时候动态加载显示的。
     */
    private static class MyPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> data;

        private MyPagerAdapter(FragmentManager fm, List<Fragment> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }

        @Override
        public int getCount() {
            return data.size();
        }

    }

}
