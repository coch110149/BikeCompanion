package com.cochrane.clinton.bikecompanion;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;


public class RBMService extends Service
	{
	private final IBinder mBinder = new RBMBinder();


	@Override
	public IBinder onBind( Intent intent )
		{
			return mBinder;
		}


	public void transferGroups( ArrayList<Group> groups )
		{
			Toast.makeText(getApplicationContext(), "" + groups.size(), Toast
					                                                            .LENGTH_LONG).show();
		}


	public void isItWorking( String PhoneNumber, String msg )
		{
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(PhoneNumber, null, msg, null, null);
			Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
			/**
			 * drift error = accuracy + (speed*time)
			 * currentLocation.distanceTo(PreviousLocation) - driftError <=5 [indicates no movement)
			 * chose 5 because US government claims most smart phones are accurate within 4.9m
			 */
		}


	public class RBMBinder extends Binder
		{
			RBMService getService()
				{
					return RBMService.this;
				}
		}
	}
