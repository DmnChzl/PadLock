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

import android.util.Log;

public class BootLoaderN7 extends BootLoader {

	// Declaring your view and variables
    private static final String TAG = "BootLoaderN7";
    private static final String mQueryCommand = "dd ibs=1 count=1 skip=5241856 if=/dev/block/platform/msm_sdcc.1/by-name/aboot # query ";
    private static final String mWriteCommand = "dd obs=1 count=1 seek=5241856 of=/dev/block/platform/msm_sdcc.1/by-name/aboot # write ";

	/**
     * Sets the lock state of bootloader.
     *
     * @return The superuser's command.
     */
    @Override
    public void setLockState(boolean newState) throws IOException {
        int mOutByte;
        if (newState) {
            mOutByte = 0;
            Log.i(TAG, "Locking BootLoader by sending " + mOutByte + " to " + mWriteCommand);
        } else {
            mOutByte = 2;
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
            } else if (mLockResult == 2) {
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
