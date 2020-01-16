package com.imie.a2dev.teamculte.readeo.Views.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.imie.a2dev.teamculte.readeo.Views.LibraryFragment;
import com.imie.a2dev.teamculte.readeo.Views.ProfileFragment;
import com.imie.a2dev.teamculte.readeo.Views.SocialFragment;

/**
 * Pager adapter used to define tab's main page behaviour.
 */
public final class MainPageAdapter extends FragmentPagerAdapter {
    /**
     * Default constructor.
     * @param mgr The associated fragment manager.
     */
    public MainPageAdapter(FragmentManager mgr) {
        super(mgr);
    }

    @Override
    public int getCount() {
        return (3);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SocialFragment();
            case 1:
                return new LibraryFragment();
            case 2:
                return new ProfileFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int pos) {
        switch (pos) {
            case 0:
                return "Social";
            case 1:
                return "Library";
            case 2:
                return "Profile";
            default:
                return null;
        }
    }
}
