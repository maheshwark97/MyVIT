/*
 * Copyright (c) 2015 GDG VIT Vellore.
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.vit.vitio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.widget.LinearLayout;


import java.io.IOException;

import io.vit.vitio.Extras.Themes.MyTheme;
import io.vit.vitio.Extras.TypeFaceSpan;
import io.vit.vitio.Fragments.Today.TodayFragment;
import io.vit.vitio.Managers.DataHandler;
import io.vit.vitio.Navigation.NavigationDrawerFragment;
import io.vit.vitio.StartScreens.FragmentHolder;

/**
 * Created by shalini on 14-06-2015.
 */
public class HomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private LinearLayout nonDrawerView;
    private DataHandler dataHandler;
    private MyTheme theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        checkFirstTimeUser();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarFormat(0);
        NavigationDrawerFragment navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.drawer_fragment);
        navigationDrawerFragment.setUp(drawerLayout, toolbar, this);

    }


    private void init() {
        dataHandler = DataHandler.getInstance(this);
        nonDrawerView = (LinearLayout) findViewById(R.id.restdrawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        theme=new MyTheme(this);
    }

    private void initializeFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment today = new TodayFragment();
        ft.add(R.id.main_fragment_holder, today);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
        changeStatusBarColor(0);
    }

    public void slideLayout(float x) {
        nonDrawerView.setTranslationX(x);
    }

    public void setToolbarFormat(int pos) {
        theme.refreshTheme();
        if(!getSupportActionBar().isShowing()){
            getSupportActionBar().show();
        }
        String title[] = getResources().getStringArray(R.array.drawer_list_titles);
        TypedArray colorres = theme.getToolbarColorTypedArray();
        Log.d("setTooltheme", PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("theme","null"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorres.getResourceId(pos, -1))));
        SpannableString s = new SpannableString(title[pos]);
        s.setSpan(theme.getMyThemeTypeFaceSpan(), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
    }

    public void setToolbarFormat(int pos, String t) {
        theme.refreshTheme();
        if(!getSupportActionBar().isShowing()){
            getSupportActionBar().show();
        }
        String title = t;
        TypedArray colorres = theme.getToolbarColorTypedArray();
        Log.d("setTooltheme", PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("theme","null"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorres.getResourceId(pos, -1))));
        SpannableString s = new SpannableString(title);
        s.setSpan(theme.getMyThemeTypeFaceSpan(), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
    }

    public void hideToolbar() {
        getSupportActionBar().hide();
    }

    private void checkFirstTimeUser() {
        if (dataHandler.getFirstTimeUser()) {
            startActivity(new Intent(this, FragmentHolder.class));
            finish();

        } else {
            initializeFragment();
        }
    }

    public void changeStatusBarColor(int i) {
        theme.refreshTheme();
        Log.d("getStatheme", PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("theme","null"));
        Log.d("myTh", String.valueOf(theme.getMyThemeCode()));
        TypedArray colorres = theme.getStatusColorTypedArray();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(colorres.getResourceId(i, -1)));
        }
    }

    @Override
    public void onBackPressed() {
        /*if(getSupportFragmentManager().getBackStackEntryCount()!=0) {
            Log.d("cF", getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).toString());
            if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1)) {
                Log.d("instance", "today");
                finish();
            }
        }*/
        super.onBackPressed();
    }
}
