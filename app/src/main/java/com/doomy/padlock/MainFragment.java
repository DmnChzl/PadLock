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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.EventListener;

public class MainFragment extends Fragment {

    // Declaring your view and variables
	private static final String TAG = "MainFragment";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
	private ArrayList<String> mTitleSet;
	private ArrayList<Integer> mColorSet;
	private ArrayList<String> mInfoSet;
	private ArrayList<Integer> mLogoSet;
	private BootLoader mBootLoader = null;
    private String mCommand = "reboot bootloader";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate
     * the fragment (e.g. upon screen orientation changes).
     */
    public MainFragment() {
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
        View mView = inflater.inflate(R.layout.fragment_main,container,false);

		final FloatingActionsMenu mMultipleActions = (FloatingActionsMenu) mView.findViewById(R.id.multipleActions);
        mMultipleActions.setAlpha(0);
		
        final FloatingActionButton mActionLock = (FloatingActionButton) mView.findViewById(R.id.actionLock);
		mActionLock.setSize(FloatingActionButton.SIZE_MINI);
        mActionLock.setIcon(R.drawable.img_lock);
        mActionLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLockBootloader();
                mMultipleActions.collapse();
                int mColor = getResources().getColor(R.color.red);
                snackBar(mColor);
            }
        });

        final FloatingActionButton mActionUnlock = (FloatingActionButton) mView.findViewById(R.id.actionUnlock);
		mActionUnlock.setSize(FloatingActionButton.SIZE_MINI);
        mActionUnlock.setIcon(R.drawable.img_unlock);
        mActionUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUnlockBootloader();
                mMultipleActions.collapse();
                int mColor = getResources().getColor(R.color.green);
                snackBar(mColor);
            }
        });

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // Use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        prepareDefaultSet();

        return mView;
    }
	
	public void prepareLockSet() {
		
		mTitleSet = new ArrayList<>();
		mColorSet = new ArrayList<>();
		mInfoSet = new ArrayList<>();
		mLogoSet = new ArrayList<>();
		
		mTitleSet.add(getString(R.string.state));
		mColorSet.add(R.color.redDark);
		mInfoSet.add(getString(R.string.locked));
		mLogoSet.add(R.drawable.ic_lock);

        mAdapter = new CardViewAdapter(mTitleSet, mColorSet, mInfoSet, mLogoSet);
        mRecyclerView.setAdapter(mAdapter);
	}
	
	public void prepareUnlockSet() {

        mTitleSet = new ArrayList<>();
        mColorSet = new ArrayList<>();
        mInfoSet = new ArrayList<>();
        mLogoSet = new ArrayList<>();
		
		mTitleSet.add(getString(R.string.state));
		mColorSet.add(R.color.greenDark);
		mInfoSet.add(getString(R.string.unlocked));
		mLogoSet.add(R.drawable.ic_unlock);

        mAdapter = new CardViewAdapter(mTitleSet, mColorSet, mInfoSet, mLogoSet);
        mRecyclerView.setAdapter(mAdapter);
	}
	
	public void prepareDefaultSet() {

        mTitleSet = new ArrayList<>();
        mColorSet = new ArrayList<>();
        mInfoSet = new ArrayList<>();
        mLogoSet = new ArrayList<>();
		
		mTitleSet.add(getString(R.string.state));
		mColorSet.add(R.color.orangeDark);
		mInfoSet.add(getString(R.string.loading));
		mLogoSet.add(R.drawable.ic_default);

        mAdapter = new CardViewAdapter(mTitleSet, mColorSet, mInfoSet, mLogoSet);
        mRecyclerView.setAdapter(mAdapter);
	}
	
	public void prepareErrorSet() {

        mTitleSet = new ArrayList<>();
        mColorSet = new ArrayList<>();
        mInfoSet = new ArrayList<>();
        mLogoSet = new ArrayList<>();
		
		mTitleSet.add(getString(R.string.state));
		mColorSet.add(R.color.deepOrangeDark);
		mInfoSet.add(getString(R.string.error));
		mLogoSet.add(R.drawable.ic_warning);

        mAdapter = new CardViewAdapter(mTitleSet, mColorSet, mInfoSet, mLogoSet);
        mRecyclerView.setAdapter(mAdapter);
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
        Log.v(TAG, "Handling onStart");
        Boolean setState = false;
        Boolean desiredState = false;
        mBootLoader = BootLoader.makeBootLoader();

        new AsyncBootLoader().execute(setState, desiredState);
    }

    public void doUnlockBootloader() {
        Boolean setState = true;
        Boolean desiredState = false;
        new AsyncBootLoader().execute(setState, desiredState);
    }

    public void doLockBootloader() {
        Boolean setState = true;
        Boolean desiredState = true;
        new AsyncBootLoader().execute(setState, desiredState);
    }

    private class AsyncBootLoader extends AsyncTask<Boolean, Void, Integer> {

        @Override
        protected Integer doInBackground(Boolean... booleans) {
            Boolean setState = booleans[0];
            Boolean desiredState = booleans[1];

            if (mBootLoader == null) {
                return Integer.valueOf(BootLoader.BL_UNSUPPORTED_DEVICE);
            }

            if (setState) {
                try {
                    mBootLoader.setLockState(desiredState);
                } catch (IOException e) {
                    if (desiredState) {
                        Log.e(TAG, "Caught IOException Locking : " + e);
                    } else {
                        Log.e(TAG, "Caught IOException Unlocking : " + e);
                    }
                }

                try {
                    Thread.sleep(BootLoader.mDelayAfterChange);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return Integer.valueOf(mBootLoader.getBootLoaderState());
        }

        @Override
        protected void onPostExecute(Integer resultObj) {
            int mResult = resultObj.intValue();

            FloatingActionsMenu mMultipleActions = (FloatingActionsMenu) getView().findViewById(R.id.multipleActions);
            FloatingActionButton mActionLock = (FloatingActionButton) getView().findViewById(R.id.actionLock);
            FloatingActionButton mActionUnlock = (FloatingActionButton) getView().findViewById(R.id.actionUnlock);

            if (mResult == BootLoader.BL_UNLOCKED) {
                prepareUnlockSet();
                mMultipleActions.setAlpha(1);
                mActionLock.setVisibility(View.VISIBLE);
                mActionUnlock.setVisibility(View.GONE);
            } else if (mResult == BootLoader.BL_LOCKED) {
                prepareLockSet();
                mMultipleActions.setAlpha(1);
                mActionLock.setVisibility(View.GONE);
                mActionUnlock.setVisibility(View.VISIBLE);
            } else if (mResult == BootLoader.BL_UNSUPPORTED_DEVICE) {
                prepareErrorSet();
                mMultipleActions.setAlpha(0);
            } else {
                prepareErrorSet();
                mMultipleActions.setAlpha(0);
            }
        }
    }

    public void snackBar(int mColor) {
        final FloatingActionsMenu mMultipleActions = (FloatingActionsMenu) getView().findViewById(R.id.multipleActions);
        SnackbarManager.show(
                Snackbar.with(getActivity())
                        .text(getString(R.string.reboot))
                        .textColor(getResources().getColor(R.color.material))
                        .color(getResources().getColor(R.color.greyEmpty))
                        .eventListener(new EventListener() {
                            @Override
                            public void onShow(Snackbar snackbar) {
                                if (android.os.Build.DEVICE.equals("mako")) {
                                    mMultipleActions.animate().translationY(-snackbar.getHeight()); // Nexus 4
                                } else if (android.os.Build.DEVICE.equals("hammerhead")) {
                                    mMultipleActions.animate().translationY(-snackbar.getHeight()); // Nexus 5
                                } else if (android.os.Build.DEVICE.equals("bacon")||android.os.Build.DEVICE.equals("A0001")) {
									mMultipleActions.animate().translationY(-snackbar.getHeight()); // OnePlus One
								}
                            }

                            @Override
                            public void onShown(Snackbar snackbar) {

                            }

                            @Override
                            public void onDismiss(Snackbar snackbar) {
                                if (android.os.Build.DEVICE.equals("mako")) {
                                    mMultipleActions.animate().translationY(0); // Nexus 4
                                } else if (android.os.Build.DEVICE.equals("hammerhead")) {
                                    mMultipleActions.animate().translationY(0); // Nexus 5
                                } else if (android.os.Build.DEVICE.equals("bacon")||android.os.Build.DEVICE.equals("A0001")) {
									mMultipleActions.animate().translationY(0); // OnePlus One
								}
                            }

                            @Override
                            public void onDismissed(Snackbar snackbar) {

                            }
                        })
                        .actionLabel(getString(R.string.yes))
                        .actionLabelTypeface(Typeface.DEFAULT_BOLD)
                        .actionColor(mColor)
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                superUserRebootBootloader(mCommand);
                            }
                        }).duration(Snackbar.SnackbarDuration.LENGTH_LONG));
    }

    public void superUserRebootBootloader(String mCommand) {
        Runtime mRuntime = Runtime.getRuntime();
        Process mProcess = null;
        OutputStreamWriter mWrite = null;

        try {
            mProcess = mRuntime.exec("su");
            mWrite = new OutputStreamWriter(mProcess.getOutputStream());
            mWrite.write(mCommand + "\n");
            mWrite.flush();
            mWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
