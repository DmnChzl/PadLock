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

package com.doomy.padlock.Devices;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BootLoader {

	// Declaring your view and variables
	private static final String TAG = "BootLoader";
    public static final int BL_UNSUPPORTED_DEVICE = -2;
    public static final int BL_UNKNOWN = -1;
    public static final int BL_UNLOCKED = 0;
    public static final int BL_LOCKED = 1;
    public static final long mDelayAfterChange = 200; // 200ms
    private static final long mLaunchDelay = 30; // 30ms

    public static BootLoader makeBootLoader() {
        Log.v(TAG, "Device : " + android.os.Build.DEVICE);
        if (android.os.Build.DEVICE.equals("manta")) {
            return new BootLoaderN10(); // Nexus 10
        } else if (android.os.Build.DEVICE.equals("mako")) {
            return new BootLoaderN4(); // Nexus 4
        } else if (android.os.Build.DEVICE.equals("hammerhead")) {
            return new BootLoaderN5(); // Nexus 5
        } else if (android.os.Build.DEVICE.equals("flo")) {
            return new BootLoaderN7(); // Nexus 7 (2013)
        } else if (android.os.Build.DEVICE.equals("deb")) {
            return new BootLoaderN7(); // Nexus 7 (2013) LTE
        } else if (android.os.Build.DEVICE.equals("bacon")) {
            return new BootLoaderOnePlusOne(); // OnePlus One
        } else if (android.os.Build.DEVICE.equals("A0001")) {
            return new BootLoaderOnePlusOne(); // OnePlus One
        } else {
            return null;
        }
    }

    public void setLockState(boolean newState) throws IOException {
        // We override this in subclasses
        return;
    }

    public int getBootLoaderState() {
        // We override this in subclasses
        return BL_UNKNOWN;
    }

    public void superUserCommandWithDataByte(String mCommand, int mDataByte) throws IOException {
        Process mProcess = Runtime.getRuntime().exec("su");
        DataOutputStream mWrite = new DataOutputStream(mProcess.getOutputStream());
        mWrite.writeBytes(mCommand + mDataByte + "\n");
        mWrite.flush();

        try {
            Thread.sleep(mLaunchDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mWrite.writeByte(mDataByte);
        mWrite.flush();
        mWrite.close();
    }

    public int superUserCommandWithByteResult(String mCommand) throws IOException {
        Process mProcess = Runtime.getRuntime().exec("su");
        DataOutputStream mWrite = new DataOutputStream(mProcess.getOutputStream());
        DataInputStream mRead = new DataInputStream(mProcess.getInputStream());

        mWrite.writeBytes(mCommand + "\n");
        mWrite.flush();
        int mResultByte = mRead.readByte();
        mWrite.close();
        return mResultByte;
    }
}
