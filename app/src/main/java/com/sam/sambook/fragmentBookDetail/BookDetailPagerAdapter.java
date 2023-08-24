package com.sam.sambook.fragmentBookDetail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class BookDetailPagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;
    public BookDetailPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentInformation fragmentInformation = new FragmentInformation();
                return fragmentInformation;
            case 1:
                FragmentReview fragmentReview = new FragmentReview();
                return fragmentReview;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
