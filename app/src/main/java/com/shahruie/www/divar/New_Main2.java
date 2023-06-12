package com.shahruie.www.divar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.shahruie.www.divar.fragments.FourFragment;
import com.shahruie.www.divar.fragments.OneFragment;
import com.shahruie.www.divar.fragments.ThreeFragment;
import com.shahruie.www.divar.fragments.TwoFragment;
import com.shahruie.www.divar.helper.About;
import com.shahruie.www.divar.helper.DatabaseAccess;

import java.util.ArrayList;
import java.util.List;

public class New_Main2 extends AppCompatActivity   implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_home,
            R.drawable.ic_list,
            R.drawable.ic_action_search,
            R.drawable.ic_adv
    };
    int city_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        Button btnadd = (Button) toolbar.findViewById(R.id.btnadd);
        final Animation shakeV = AnimationUtils.loadAnimation(this,R.anim.shake);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(shakeV);
            }
        });

        shakeV.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent newActivity = new Intent(New_Main2.this,Add.class);
                newActivity.putExtra("key",1);
                startActivity(newActivity);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open,R.string.drawer_close) {
//
            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                if (item != null && item.getItemId() == android.R.id.home) {
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        drawer.openDrawer(GravityCompat.START);
                    }
                }
                return false;
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_city) {
            show_city_list();
        } else if (id == R.id.my_adv) {

        } else if (id == R.id.search) {

        } else if (id == R.id.about) {

            About about=new About(New_Main2.this);
            about.show();
        } else if (id == R.id.favorite) {

        } else if (id == R.id.category) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OneFragment(), "خانه");
        adapter.addFrag(new TwoFragment(), "لیست");
        adapter.addFrag(new ThreeFragment(), "جستجو");
        adapter.addFrag(new FourFragment(), "آگهی من");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    //===============================================
    private   void  show_city_list(){
        final Dialog city_dialog = new Dialog(New_Main2.this);
        city_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        city_dialog.setContentView(R.layout.city_list_dialog);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(New_Main2.this);
        databaseAccess.open();
        List<String> citys = databaseAccess.get_city_name();
        databaseAccess.close();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(New_Main2.this,R.layout.city_list_row,R.id.city_name, citys);
        ListView city_list=(ListView)city_dialog.findViewById(R.id.city_list);
        city_list.setAdapter(adapter);


        city_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences pref;
                SharedPreferences.Editor editor;
                pref = getApplicationContext().getSharedPreferences("setting", MODE_PRIVATE);
                editor = pref.edit();
                editor.putInt("city_id",++position);
                editor.commit();
                city_id=position;
                Toast.makeText(New_Main2.this,
                        "شهر تغییر یافت!"+city_id+"",Toast.LENGTH_LONG).show();
                city_dialog.dismiss();
            }
        });
        EditText et_city_search = (EditText) city_dialog.findViewById(R.id.et_city_search);
        et_city_search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }
        });

        city_dialog.show();

        city_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {


               OneFragment.get_All_Adv(city_id+"");

            }
        });

    }
    //==================================================
}
