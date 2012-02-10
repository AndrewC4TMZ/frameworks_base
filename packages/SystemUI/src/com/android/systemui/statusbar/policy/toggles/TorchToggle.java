/*
 * Copyright (C) 2010 The Android Open Source Project
 * Copyright 2011 Colin McDonough
 * 
 * Modified for AOKP by Mike Wilson (Zaphod-Beeblebrox)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.statusbar.policy.toggles;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.app.PendingIntent;
import com.android.systemui.R;
/**
 * TODO: Fix the WakeLock
 */
public class TorchToggle extends Toggle implements OnSharedPreferenceChangeListener {

    private static final String TAG = "TorchToggle";
    
    public static final String KEY_TORCH_ON = "torch_on";
    public static final String INTENT_TORCH_ON = "com.aokp.romcontrol.INTENT_TORCH_ON";
    public static final String INTENT_TORCH_OFF = "com.aokp.romcontrol.INTENT_TORCH_OFF";

    private boolean mIsTorchOn;
    private Context mContext;
    
    SharedPreferences prefs;
    
    PendingIntent torchIntent;
    
    public TorchToggle(Context context) {
        super(context);
        setLabel(R.string.toggle_torch);
        setIcon(R.drawable.toggle_torch);
        mContext = context;
        prefs = mContext.getSharedPreferences("torch", Context.MODE_WORLD_READABLE);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void updateInternalToggleState() {
        mToggle.setChecked(mIsTorchOn);
    }

    @Override
    protected void onCheckChanged(boolean isChecked) {
        if (isChecked) {
            Log.d(TAG, "Starting Torch_ON Intent");
            try {
            	Intent i = new Intent(mContext, Class.forName("com.android.systemui.TorchService"));
            	i.setAction(INTENT_TORCH_ON);
            	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            	//torchIntent = PendingIntent.getService(mContext, 0, i,Intent.FLAG_ACTIVITY_NEW_TASK); 
            	mContext.startService(i);
            	Log.d(TAG, "ending Torch_ON Intent");
            } catch ( ClassNotFoundException e ) {
            	Log.e(TAG,"Uh oh .. ClassNotFound .. that ain't good");
            }
        }
        else {
        	Log.d(TAG, "Starting Torch_OFF Intent");
        	try {
        		Intent i = new Intent(mContext, Class.forName("com.android.systemui.TorchService"));
        		i.setAction(INTENT_TORCH_OFF);
        		//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		//torchIntent = PendingIntent.getService(mContext, 0, i,Intent.FLAG_ACTIVITY_NEW_TASK);
        		mContext.startService(i);
        		Log.d(TAG, "ending Torch_OFF Intent");
        	} catch ( ClassNotFoundException e ) {
            	Log.e(TAG,"Uh oh .. ClassNotFound .. that ain't good");
        	
        	}
        }
    }

    @Override
    protected boolean onLongPress() {
        return false;
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key) 
    {
      Log.d(TAG, "Caught Torch preference change");
      mIsTorchOn = sharedPreferences.getBoolean(KEY_TORCH_ON,false);
      updateInternalToggleState();
    }
}
