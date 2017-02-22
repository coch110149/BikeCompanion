package com.cochrane.clinton.bikecompanion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity
	{

	@Override
	protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			/**
			 *  populate bike name, alerts
			 *      if list is empty prompt user to fill in information
			 *  populate groups that are activated
			 *      if list is empty prompt user to fill in information
			 *  set up buttons (bike info, riding buddy, and fab)
			 */
		}

	public void ShowRideActivity(View view)
		{
			Intent intent = new Intent(this, RideActivity.class);
			startActivity(intent);
		}
	}
