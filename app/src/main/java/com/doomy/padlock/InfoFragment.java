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

import java.io.File;
import java.util.ArrayList;

public class InfoFragment extends Fragment {

    // Declaring your view and variables
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
	private ArrayList<Card> mCardSet;

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
        mAdapter = new CardViewAdapter(mCardSet);
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

        mCardSet = new ArrayList<>();

        Card applicationCard = new Card(getString(R.string.application), R.color.amberDark, R.color.amber,
                getApplicationVersion(), R.drawable.ic_application);

        Card deviceCard = new Card(getString(R.string.device), R.color.tealDark, R.color.teal,
                setCapitalize(android.os.Build.MANUFACTURER)+" "+android.os.Build.MODEL+" ("+setCapitalize(android.os.Build.BRAND)+")", R.drawable.ic_device);

        Card ipCard = new Card(getString(R.string.ip_adress), R.color.greyDark, R.color.grey,
                MainActivity.getIPAdressWifi(), R.drawable.ic_ip);

        Card designationCard = new Card(getString(R.string.designation), R.color.brownDark, R.color.brown,
                setCapitalize(android.os.Build.DEVICE)+" ("+setCapitalize(android.os.Build.PRODUCT)+")", R.drawable.ic_designation);

        Card storageCard = new Card(getString(R.string.storage), R.color.pinkDark, R.color.pink,
                getAvailableInternalMemorySize()+" ("+getString(R.string.free)+") / " + getTotalInternalMemorySize(), R.drawable.ic_storage);

        Card ramCard = new Card(getString(R.string.ram), R.color.indigoDark, R.color.indigo,
                getRAMSize(), R.drawable.ic_ram);

        Card androidCard = new Card(getString(R.string.android), R.color.lightGreenDark, R.color.lightGreen,
                android.os.Build.VERSION.RELEASE, R.drawable.ic_android);

        Card buildCard = new Card(getString(R.string.build), R.color.blueDark, R.color.blue,
                android.os.Build.DISPLAY, R.drawable.ic_build);

        Card kernelCard = new Card(getString(R.string.kernel), R.color.purpleDark, R.color.purple,
                System.getProperty("os.version"), R.drawable.ic_kernel);

        Card bootloaderCard = new Card(getString(R.string.bootloader), R.color.cyanDark, R.color.cyan,
                android.os.Build.BOOTLOADER.toUpperCase(), R.drawable.ic_bootloader);

        Card basebandCard = new Card(getString(R.string.baseband), R.color.limeDark, R.color.lime,
                getRadioVersion(), R.drawable.ic_baseband);

        Card serialCard = new Card(getString(R.string.serial), R.color.blueGreyDark, R.color.blueGrey,
                android.os.Build.SERIAL.toUpperCase(), R.drawable.ic_serial);

        mCardSet.add(applicationCard);
        mCardSet.add(deviceCard);
        mCardSet.add(ipCard);
        mCardSet.add(designationCard);
        mCardSet.add(storageCard);
        mCardSet.add(ramCard);
        mCardSet.add(androidCard);
        mCardSet.add(buildCard);
        mCardSet.add(kernelCard);
        mCardSet.add(bootloaderCard);
        mCardSet.add(basebandCard);
        mCardSet.add(serialCard);
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

        if (getRadioVersion().equals(getString(R.string.unknown))) {
            mCardSet.remove(10);
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
