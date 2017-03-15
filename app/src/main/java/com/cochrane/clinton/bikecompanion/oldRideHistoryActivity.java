//package com.cochrane.clinton.bikecompanion;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//
//public class RideHistoryActivity extends AppCompatActivity
//	{
//	@Override
//	protected void onCreate( Bundle savedInstanceState )
//		{
//			super.onCreate(savedInstanceState);
//			setContentView(R.layout.activity_ride_history);
//			DatabaseHandler db = new DatabaseHandler(this);
//			final ArrayList<Ride> rides = (ArrayList<Ride>) db.getAllRides();
//			RideAdapter rideAdapter = new RideAdapter(this, rides);
//			ListView listView = (ListView) findViewById(R.id.listview_ride_history);
//			TextView bikeName = (TextView) listView.findViewById(R.id.bike_name_information);
//			bikeName.setText(db.getBike(rides.getBikeID()).getBikeName());
//
//			listView.setAdapter(rideAdapter);
//			listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
//				{
//					@Override
//					public void onItemClick( AdapterView<?> parent, View view, int position, long id )
//						{
//							Ride ride = rides.get(position);
//							Intent intent = new Intent(RideHistoryActivity.this,
//									                          RideSummaryActivity.class);
//							intent.putExtra("CurrentRideObject", ride);
//							startActivity(intent);
//						}
//				});
//		}
//	}
