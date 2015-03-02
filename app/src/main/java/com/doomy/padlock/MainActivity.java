/**
 * Copyright (C) 2015 Damien Chazoule
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.doomy.padlock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity {

    // Declaring your view and variables
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private SlidingTabLayout mSlidingTabLayout;
	private int NumbOftabs = 2;
    private boolean mValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Open "Hello" dialog at the first launch
        openFirstDialog();

        // Creating the Toolbar and setting it as the Toolbar for the activity
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Adds values to the tab names
        String mAction = getString(R.string.action);
        String mInfo = getString(R.string.info);
        CharSequence Titles[] = {mAction, mInfo};

        // Creating the ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs
        mViewPagerAdapter =  new ViewPagerAdapter(getSupportFragmentManager(), Titles, NumbOftabs);

        // Assigning ViewPager View and setting the adapter
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mViewPagerAdapter);

        // Assiging the Sliding Tab Layout View
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
        mSlidingTabLayout.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.orangeDark);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		/**
         * Handle action bar item clicks here. The action bar will
         * automatically handle clicks on the Home/Up button, so long
         * as you specify a parent activity in AndroidManifest.xml.
		 */
        int id = item.getItemId();

        // NoInspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            openAboutDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

	// Create AlertDialog for the first launch
    private void openFirstDialog() {
        mValue = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("mValue", true);

        if (mValue){
            AlertDialog mAlertDialog = new AlertDialog.Builder(MainActivity.this).create();

            mAlertDialog.setTitle(getString(R.string.title));
            mAlertDialog.setMessage(getString(R.string.message));
            mAlertDialog.setButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            mAlertDialog.show();

            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("mValue", false).commit();
        }
    }

	// Create AlertDialog for the about view
    private void openAboutDialog() {
        LayoutInflater mLayoutInflater = LayoutInflater.from(this);
        View mView = mLayoutInflater.inflate(R.layout.view_about, null);
		
        ImageView mImageViewDev = (ImageView) mView.findViewById(R.id.dev);
        ImageView mImageViewGitHub = (ImageView) mView.findViewById(R.id.github);
		Drawable mDev = mImageViewDev.getDrawable();
		Drawable mGitHub = mImageViewGitHub.getDrawable();
		mDev.setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);
		mGitHub.setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);

        mImageViewGitHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                mIntent.setAction(Intent.ACTION_VIEW);
                mIntent.addCategory(Intent.CATEGORY_BROWSABLE);
                mIntent.setData(Uri.parse(getString(R.string.url)));
                startActivity(mIntent);
            }
        });

        ContextThemeWrapper mContextThemeWrapper = new ContextThemeWrapper(MainActivity.this, R.style.DialogTheme); // Material Light Theme
        AlertDialog mAlertDialog = new AlertDialog.Builder(mContextThemeWrapper).create();

        mAlertDialog.setTitle(getString(R.string.about));
        mAlertDialog.setView(mView);
        mAlertDialog.setButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mAlertDialog.show();
    }
}