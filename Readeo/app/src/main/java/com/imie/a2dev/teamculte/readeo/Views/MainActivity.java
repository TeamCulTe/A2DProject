package com.imie.a2dev.teamculte.readeo.Views;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.imie.a2dev.teamculte.readeo.R;
import com.imie.a2dev.teamculte.readeo.Utils.AppUtils;
import com.imie.a2dev.teamculte.readeo.Views.Adapters.MainPageAdapter;

/**
 * Main activity organised as a tab activity browsing between (library, social or profile fragment). 
 */
public final class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppUtils.initImageLoaderConfig();
        
        setContentView(R.layout.activity_main);

        ViewPager viewPager = this.findViewById(R.id.activity_main_viewpager);
        TabLayout tabLayout = this.findViewById(R.id.tab_content);
        
        viewPager.setAdapter(new MainPageAdapter(this.getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        
        if (tab != null) {
            tab.select();
        }
    }
}

