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

import java.io.IOException;

public class BootLoaderN10 extends BootLoader {

	// Declaring your view and variables
    private static final String TAG = "BootLoaderN10";
    private static final String mQueryCommand = "dd ibs=1 count=1 skip=548 if=/dev/block/platform/dw_mmc.0/by-name/param # query ";
    private static final String mWriteCommand = "dd obs=1 count=1 seek=548 of=/dev/block/platform/dw_mmc.0/by-name/param # write ";

	/**
     * Sets the lock state of bootloader.
     *
     * @return The superuser's command.
     */
    @Override
    public void setLockState(boolean myNewState) throws IOException {
        int mOutByte;
        if (myNewState) {
            mOutByte = 0;
            Log.i(TAG, "Locking BootLoader by sending " + mOutByte + " to " + mWriteCommand);
        } else {
            mOutByte = 1;
            Log.i(TAG, "Unlocking BootLoader by sending " + mOutByte + " to " + mWriteCommand);
        }
        superUserCommandWithDataByte(mWriteCommand, mOutByte);
    }

	/**
     * Gets the lock state of bootloader.
     *
     * @return The lock state of bootloader.
     */
    @Override
    public int getBootLoaderState() {
        try {
            Log.v(TAG, "Getting BootLoader lock state with " + mQueryCommand);
            int mLockResult = superUserCommandWithByteResult(mQueryCommand);
            Log.v(TAG, "Got lock value " + mLockResult);

            if (mLockResult == 0) {
                return BL_LOCKED;
            } else if (mLockResult == 1) {
                return BL_UNLOCKED;
            } else {
                return BL_UNKNOWN;
            }
        } catch (IOException e) {
            Log.v(TAG, "Caught IOException while querying : " + e);
            return BL_UNKNOWN;
        }
    }
}
