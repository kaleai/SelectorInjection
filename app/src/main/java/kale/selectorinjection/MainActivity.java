package kale.selectorinjection;

import java.util.Arrays;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PagerTitleStrip pagerTitleStrip = findViewById(R.id.pager_strip);
        pagerTitleStrip.setTextColor(Color.WHITE);
        
        ViewPager viewPager = findViewById(R.id.viewpager);

        final List<BaseFragment> fragments = Arrays.asList(
                new ImageFragment(),
                new RadioFragment(),
                new ButtonActivity(),
                new TextFragment(),
                new RippleFragment(),
                new SvgFragment()
        );

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return fragments.get(position).getName();
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }

}
