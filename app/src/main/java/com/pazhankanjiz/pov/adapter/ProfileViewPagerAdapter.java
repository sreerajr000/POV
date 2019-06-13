package com.pazhankanjiz.pov.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pazhankanjiz.pov.fragment.AnswersFragment;
import com.pazhankanjiz.pov.fragment.QuestionsFragment;

public class ProfileViewPagerAdapter extends FragmentPagerAdapter {

    private int totalTabs;
    private Context mCtx;

    public ProfileViewPagerAdapter(@NonNull FragmentManager fm, int totalTabs, Context mCtx) {
        super(fm);
        this.totalTabs = totalTabs;
        this.mCtx = mCtx;
    }

    public ProfileViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                QuestionsFragment fragment = new QuestionsFragment();
                return fragment;
            case 1:
                AnswersFragment answersFragment = new AnswersFragment();
                return answersFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
