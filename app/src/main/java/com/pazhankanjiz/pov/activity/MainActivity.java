package com.pazhankanjiz.pov.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.facebook.accountkit.AccountKit;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pazhankanjiz.pov.constant.DatabaseConstants;
import com.pazhankanjiz.pov.custom.CustomViewPager;
import com.pazhankanjiz.pov.R;
import com.pazhankanjiz.pov.adapter.ViewPagerAdapter;
import com.pazhankanjiz.pov.fragment.HomeFragment;
import com.pazhankanjiz.pov.fragment.NewPostFragment;
import com.pazhankanjiz.pov.fragment.NotificationFragment;
import com.pazhankanjiz.pov.fragment.ProfileFragment;
import com.pazhankanjiz.pov.fragment.SearchFragment;
import com.pazhankanjiz.pov.model.HomeCardModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.transition.TransitionManager;
import androidx.viewpager.widget.ViewPager;

import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.concurrent.FutureTask;

import bolts.Task;

import static com.pazhankanjiz.pov.constant.FragmentConstants.FRAGMENT_HOME;
import static com.pazhankanjiz.pov.constant.FragmentConstants.FRAGMENT_NEWPOST;
import static com.pazhankanjiz.pov.constant.FragmentConstants.FRAGMENT_NOTIFICATION;
import static com.pazhankanjiz.pov.constant.FragmentConstants.FRAGMENT_PROFILE;
import static com.pazhankanjiz.pov.constant.FragmentConstants.FRAGMENT_SEARCH;
import static com.pazhankanjiz.pov.constant.UserInfoConstants.ACCOUNT_ID;
import static com.pazhankanjiz.pov.constant.UserInfoConstants.PHONE_NUMBER_STRING;
import static com.pazhankanjiz.pov.constant.UserInfoConstants.USER_INFO;
import static com.pazhankanjiz.pov.constant.UserInfoConstants.USER_LOGGED_IN;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";

    private CustomViewPager viewPager;

    public static String LOGGED_IN_USER = "";

    private boolean saveToHistory;
    private int currentPage = 0;

    private Toolbar toolbar;

    private BottomNavigationView navView;

    private Stack<Integer> pageHistory = new Stack<>();

    private SearchView mSearchView;
    private SearchView.SearchAutoComplete searchAutoComplete;

    private SharedPreferences sharedPreferences;


    private Fragment homeFragment, searchFragment, newPostFragment, notificationFragment, profileFragment;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem searchMenu = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMenu.getActionView();

        //TODO Autocomplete modify here
        searchAutoComplete = mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        String dataArr[] = {"Apple", "Orange", "Amd", "Amazon", "Google"};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);
        searchAutoComplete.setAdapter(arrayAdapter);
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSearchView.setQuery(arrayAdapter.getItem(position), false);
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: ");
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("query", mSearchView.getQuery() + "");
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: ");
                return false;
            }
        });


        ImageView imageView = (ImageView) menu.findItem(R.id.action_favorite).getActionView();
        mSearchView.setQueryHint("Search");
        if (viewPager.getCurrentItem() == 0) {
            searchMenu.setVisible(false);
        } else {
            searchMenu.setVisible(true);
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            logOut();
            return true;
        } else if (id == R.id.action_search) {
            TransitionManager.beginDelayedTransition(toolbar);
            MenuItemCompat.expandActionView(item);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            invalidateOptionsMenu();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_search:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(3);
                    return true;
                case R.id.navigation_newpost:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_profile:
                    viewPager.setCurrentItem(4);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO Strict mode remove
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().detectDiskReads().detectDiskWrites().detectNetwork()
//                .penaltyDeath().penaltyLog().build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectLeakedSqlLiteObjects()
//                .detectLeakedClosableObjects()
//                .penaltyDeath()
//                .penaltyLog()
//                .build());

        navView = findViewById(R.id.nav_view);

        Initializer init = new Initializer();
        init.execute();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);


        viewPager = findViewById(R.id.fragment_viewpager);
        viewPager.disableScroll(true);
        saveToHistory = true;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (saveToHistory) {
                    pageHistory.push(currentPage);
                    currentPage = position;
                }

                viewPager.disableScroll(false);
                toolbar.setVisibility(View.VISIBLE);
                navView.setVisibility(View.VISIBLE);
                switch (position) {
                    case 0:
                        navView.setSelectedItemId(R.id.navigation_home);
                        viewPager.disableScroll(true);
                        break;
                    case 1:
                        navView.setSelectedItemId(R.id.navigation_search);
                        break;
                    case 2:
                        navView.setSelectedItemId(R.id.navigation_newpost);
                        Animation fadeOutDown = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out_down);
                        Animation fadeOutUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out_up);
                        toolbar.startAnimation(fadeOutUp);
                        navView.startAnimation(fadeOutDown);
                        toolbar.setVisibility(View.GONE);
                        navView.setVisibility(View.GONE);
                        break;

                    case 3:
                        navView.setSelectedItemId(R.id.navigation_notifications);
                        break;

                    case 4:
                        navView.setSelectedItemId(R.id.navigation_profile);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        addTabs(viewPager);


        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        /*
        final View activityRootView = findViewById(R.id.container);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(getApplicationContext(), 200)) {
                    setNavigationVisibility(false);
                } else {
                    setNavigationVisibility(true);
                }
            }
        });
         **/
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }


    private void addTabs(ViewPager viewPager) {
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        newPostFragment = new NewPostFragment(viewPager);
        notificationFragment = new NotificationFragment();
        profileFragment = new ProfileFragment();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(homeFragment, FRAGMENT_HOME);
        adapter.addFrag(searchFragment, FRAGMENT_SEARCH);
        adapter.addFrag(newPostFragment, FRAGMENT_NEWPOST);
        adapter.addFrag(notificationFragment, FRAGMENT_NOTIFICATION);
        adapter.addFrag(profileFragment, FRAGMENT_PROFILE);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        if (pageHistory.empty()) {
            super.onBackPressed();
        } else {
            saveToHistory = false;
            viewPager.setCurrentItem(pageHistory.pop().intValue());
            saveToHistory = true;
        }
        currentPage = viewPager.getCurrentItem();
    }

    public void logOut() {
        AccountKit.logOut();
        SharedPreferences.Editor editor = getSharedPreferences(USER_INFO, MODE_PRIVATE).edit();
        editor.putString(ACCOUNT_ID, "");
        editor.putString(PHONE_NUMBER_STRING, "");
        editor.putBoolean(USER_LOGGED_IN, false);
        editor.apply();
    }

    public void setNavigationVisibility(boolean visible) {
        if (navView.isShown() && !visible) {
            navView.setVisibility(View.GONE);
        } else if (!navView.isShown() && visible) {
            navView.setVisibility(View.VISIBLE);
        }
    }


    public void addFirstHome(int size, HomeCardModel item) {
        ((HomeFragment) homeFragment).addFirst(size, item);
    }


    public void paginateHome(List secondList) {
        ((HomeFragment) homeFragment).paginate(secondList);
    }

    public void reloadHome(List newList) {
        ((HomeFragment) homeFragment).reload(newList);
    }

    public void addLastHome(int size, List<HomeCardModel> modelList) {
        ((HomeFragment) homeFragment).addLast(size, modelList);
    }

    public void removeFirstHome(int size) {
        ((HomeFragment) homeFragment).removeFirst(size);
    }

    public void removeLastHome(int size) {
        ((HomeFragment) homeFragment).removeLast(size);
    }

    public void replaceHome() {
        ((HomeFragment) homeFragment).replace();
    }

    public void swapHome() {
        ((HomeFragment) homeFragment).swap();
    }


    private class Initializer extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
            LOGGED_IN_USER = sharedPreferences.getString(DatabaseConstants.ID, "");
            return null;
        }
    }
}
