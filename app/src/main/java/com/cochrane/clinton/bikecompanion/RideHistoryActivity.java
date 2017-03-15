package com.cochrane.clinton.bikecompanion;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class RideHistoryActivity extends AppCompatActivity
	{
	@Override
	protected void onCreate( Bundle savedInstanceState )
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_ride_history);
			Cursor cursor = new DatabaseHandler(this).getBikeName_RideCursor();
			ListView listView = (ListView) findViewById(R.id.listview_ride_history);
			if(cursor.moveToFirst())
			{
				RideAdapter rideAdapter = new RideAdapter(this, cursor);
				listView.setAdapter(rideAdapter);
			}
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
				{
					@Override
					public void onItemClick( AdapterView<?> parent, View view, int position, long id )
						{
							DatabaseHandler db = new DatabaseHandler(getApplicationContext());
							Ride ride = db.getRide((int) id);
							Intent intent = new Intent(RideHistoryActivity.this,
									                          RideSummaryActivity.class);
							intent.putExtra("CurrentRideObject", ride);
							startActivity(intent);
						}
				});
		}
	}
