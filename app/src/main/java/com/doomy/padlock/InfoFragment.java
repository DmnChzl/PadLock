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

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InfoFragment extends Fragment {

    // Declaring your view and variables
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
	private ArrayList<String> mTitleSet;
	private ArrayList<Integer> mColorSet;
	private ArrayList<String> mInfoSet;
	private ArrayList<Integer> mLogoSet;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate
     * the fragment (e.g. upon screen orientation changes).
     */
    public InfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView =inflater.inflate(R.layout.fragment_info,container,false);

		prepareArraySet();
        // Verify Baseband
        removeBaseband();

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // Use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Specify an adapter (see also next example)
        mAdapter = new CardViewAdapter(mTitleSet, mColorSet, mInfoSet, mLogoSet);
        mRecyclerView.setAdapter(mAdapter);
		
        return mView;
    }
	
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
	
	private void prepareArraySet() {
		
		mTitleSet = new ArrayList<>();
		mTitleSet.add(getString(R.string.application));
		mTitleSet.add(getString(R.string.device));
		mTitleSet.add(getString(R.string.designation));
        mTitleSet.add(getString(R.string.storage));
        mTitleSet.add(getString(R.string.ram));
		mTitleSet.add(getString(R.string.android));
		mTitleSet.add(getString(R.string.build));
        mTitleSet.add(getString(R.string.kernel));
		mTitleSet.add(getString(R.string.bootloader));
		mTitleSet.add(getString(R.string.baseband));
		mTitleSet.add(getString(R.string.serial));
		
		mColorSet = new ArrayList<>();
        mColorSet.add(R.color.amberDark);
        mColorSet.add(R.color.tealDark);
        mColorSet.add(R.color.brownDark);
        mColorSet.add(R.color.pinkDark);
        mColorSet.add(R.color.indigoDark);
        mColorSet.add(R.color.lightGreenDark);
        mColorSet.add(R.color.blueDark);
		mColorSet.add(R.color.purpleDark);
        mColorSet.add(R.color.cyanDark);
        mColorSet.add(R.color.limeDark);
        mColorSet.add(R.color.blueGreyDark);

		mInfoSet = new ArrayList<>();
        mInfoSet.add(getApplicationVersion());
        mInfoSet.add(setCapitalize(android.os.Build.MANUFACTURER)+" "+android.os.Build.MODEL+" ("+setCapitalize(android.os.Build.BRAND)+")");
        mInfoSet.add(setCapitalize(android.os.Build.DEVICE)+" ("+setCapitalize(android.os.Build.PRODUCT)+")");
        mInfoSet.add(getAvailableInternalMemorySize()+" ("+getString(R.string.free)+") / " + getTotalInternalMemorySize());
        mInfoSet.add(getRAMSize());
        mInfoSet.add(android.os.Build.VERSION.RELEASE);
        mInfoSet.add(android.os.Build.DISPLAY);
        mInfoSet.add(System.getProperty("os.version"));
        mInfoSet.add(android.os.Build.BOOTLOADER.toUpperCase());
        mInfoSet.add(getRadioVersion());
        mInfoSet.add(android.os.Build.SERIAL.toUpperCase());
		
		mLogoSet = new ArrayList<>();
        mLogoSet.add(R.drawable.ic_application);
        mLogoSet.add(R.drawable.ic_device);
        mLogoSet.add(R.drawable.ic_designation);
        mLogoSet.add(R.drawable.ic_storage);
        mLogoSet.add(R.drawable.ic_ram);
        mLogoSet.add(R.drawable.ic_android);
        mLogoSet.add(R.drawable.ic_build);
        mLogoSet.add(R.drawable.ic_kernel);
        mLogoSet.add(R.drawable.ic_bootloader);
        mLogoSet.add(R.drawable.ic_baseband);
        mLogoSet.add(R.drawable.ic_serial);
	}

    /**
     * Gets the radio version.
     *
     * @return The radio version.
     */
    private String getRadioVersion() {
        String radioVersion;
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            radioVersion = TextUtils.isEmpty(android.os.Build.getRadioVersion()) ? getString(R.string.unknown) :
                    android.os.Build.getRadioVersion();
        } else {
            radioVersion = android.os.Build.RADIO;
        }
        return radioVersion;
    }
	
	/**
     * Gets the application version.
     *
     * @return The application version.
     */
	private String getApplicationVersion() {
		String applicationVersion = "";
        try {
            PackageInfo mPackageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            applicationVersion = mPackageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // Auto-generated catch block
            e.printStackTrace();
        }
		return applicationVersion;
	}

    /**
     * Sets the word with first letter capitalize.
     *
     * @return The word with first letter capitalize.
     */
    private String setCapitalize(String mWord) {
        if (mWord.toUpperCase().equals("LGE")) {
            mWord = "LG";
            return mWord;
        } else {
            return mWord.substring(0, 1).toUpperCase() + mWord.substring(1);
        }
    }

    private void removeBaseband() {
        int mValue = mInfoSet.indexOf(getString(R.string.unknown));
        if (getRadioVersion().equals(getString(R.string.unknown))) {
            mTitleSet.remove(mValue);
            mColorSet.remove(mValue);
            mInfoSet.remove(mValue);
            mLogoSet.remove(mValue);
        }
    }
	
	/**
     * Gets available internal memory size.
     *
     * @return The size of internal storage available.
     */
	public String getAvailableInternalMemorySize() {
        File mPath = Environment.getDataDirectory();
        StatFs mStat = new StatFs(mPath.getPath());
        long mBlockSize = mStat.getBlockSize();
        long mAvailableBlocks = mStat.getAvailableBlocks();
		return Formatter.formatFileSize(getActivity(), mAvailableBlocks * mBlockSize);
    }

	/**
     * Gets total internal memory size.
     *
     * @return The total size of internal storage.
     */
    public String getTotalInternalMemorySize() {
        File mPath = Environment.getDataDirectory();
        StatFs mStat = new StatFs(mPath.getPath());
        long mBlockSize = mStat.getBlockSize();
        long mTotalBlocks = mStat.getBlockCount();
		return Formatter.formatFileSize(getActivity(), mTotalBlocks * mBlockSize);
    }

    /**
     * Gets random access memory size.
     *
     * @return The RAM size.
     */
    public String getRAMSize() {
        ActivityManager mActivityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mMemoryInfo = new ActivityManager.MemoryInfo();
        mActivityManager.getMemoryInfo(mMemoryInfo);
        long mTotalMemory = mMemoryInfo.totalMem;
        return Formatter.formatFileSize(getActivity(), mTotalMemory);
    }
}
