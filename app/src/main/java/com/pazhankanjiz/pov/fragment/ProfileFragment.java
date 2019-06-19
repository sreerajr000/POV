package com.pazhankanjiz.pov.fragment;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.pazhankanjiz.pov.adapter.ProfileViewPagerAdapter;
import com.pazhankanjiz.pov.constant.ResourceConstants;
import com.pazhankanjiz.pov.model.ProfileViewModel;
import com.pazhankanjiz.pov.R;

import static com.pazhankanjiz.pov.constant.UserInfoConstants.BACKGROUND;
import static com.pazhankanjiz.pov.constant.UserInfoConstants.USER_BIO;
import static com.pazhankanjiz.pov.constant.UserInfoConstants.USER_INFO;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private TextView userName;
    private TextView userBio;
    private TextView questionsCount;
    private TextView povCount;
    private TextView followingCount;
    private ImageView profileImage;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileImage = view.findViewById(R.id.profile_image);
        questionsCount = view.findViewById(R.id.questions_count);
        povCount = view.findViewById(R.id.pov_count);
        followingCount = view.findViewById(R.id.following_count);
        userBio = view.findViewById(R.id.user_bio);
//        userName = view.findViewById(R.id.username);

        SharedPreferences preferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        userBio.setText(preferences.getString(USER_BIO, ""));

        Glide.with(profileImage)
                .load(ResourceConstants.PROFILE_IMAGES.get(preferences.getInt(BACKGROUND, 0)))
                .into(profileImage);
        tabLayout = view.findViewById(R.id.profile_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Questions"));
        tabLayout.addTab(tabLayout.newTab().setText("Answers"));

        viewPager = view.findViewById(R.id.profile_view_pager);
        ProfileViewPagerAdapter adapter = new ProfileViewPagerAdapter(getChildFragmentManager(), 2, getContext());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_favorite_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_person_black_24dp);
        tabLayout.getTabAt(0).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
        tabLayout.getTabAt(1).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}
