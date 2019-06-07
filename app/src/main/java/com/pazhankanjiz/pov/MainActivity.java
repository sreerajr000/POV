package com.pazhankanjiz.pov;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener{

    private static final String TAG = "MainActivity";

    private CustomViewPager viewPager;

    private Toolbar toolbar;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem searchMenu = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) searchMenu.getActionView();
        ImageView imageView = (ImageView) menu.findItem(R.id.action_favorite).getActionView();
        mSearchView.setQueryHint("Search");
        if (viewPager.getCurrentItem() == 0) {
            searchMenu.setVisible(false);
//            Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
//            fadeOut.reset();
//            imageView.startAnimation(fadeOut);
        } else {
            searchMenu.setVisible(true);
//            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
//            fadeIn.reset();
//            imageView.startAnimation(fadeIn);
        }

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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
            Toast.makeText(MainActivity.this, "Action clicked", Toast.LENGTH_LONG).show();
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
        final BottomNavigationView navView = findViewById(R.id.nav_view);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.like_green_24dp);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);


        viewPager = findViewById(R.id.fragment_viewpager);
        viewPager.disableScroll(true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navView.setSelectedItemId(R.id.navigation_home);
                        viewPager.disableScroll(true);
                        break;
                    case 1:
                        navView.setSelectedItemId(R.id.navigation_search);
                        viewPager.disableScroll(false);
                        break;
                    case 2:
                        navView.setSelectedItemId(R.id.navigation_newpost);
                        viewPager.disableScroll(false);
                        break;

                    case 3:
                        navView.setSelectedItemId(R.id.navigation_notifications);
                        viewPager.disableScroll(false);
                        break;

                    case 4:
                        navView.setSelectedItemId(R.id.navigation_profile);
                        viewPager.disableScroll(false);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        addTabs(viewPager);


        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new HomeFragment(), "HOME");
        adapter.addFrag(new SearchFragment(), "SEARCH");
        adapter.addFrag(new NewPostFragment(), "NEWPOST");
        adapter.addFrag(new NotificationFragment(), "NOTIFICATION");
        adapter.addFrag(new ProfileFragment(), "PROFILE");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
