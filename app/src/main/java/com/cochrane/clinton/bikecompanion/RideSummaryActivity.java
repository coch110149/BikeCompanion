package com.cochrane.clinton.bikecompanion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class RideSummaryActivity extends AppCompatActivity
	{

	private Ride ride;

	@Override
	protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_ride_summary);
			Bundle bundle = getIntent().getExtras();
			ride = bundle.getParcelable("CurrentRideObject");

			TextView avgPaceTextView = (TextView) findViewById(R.id.AvgPace_Information);
			TextView durationTextView = (TextView) findViewById(R.id.Duration_Information);
			TextView distanceTextView = (TextView) findViewById(R.id.Distance_Information);
			TextView maxSpeedTextView = (TextView) findViewById(R.id.MaxSpeed_Information);
			TextView avgSpeedTextView = (TextView) findViewById(R.id.AvgSpeed_Information);
			TextView elevLossTextView = (TextView) findViewById(R.id.ElevationLoss_Information);
			TextView elevGainTextView = (TextView) findViewById(R.id.ElevationGain_Information);

			avgPaceTextView.setText(ride.getAvgPace());
			durationTextView.setText(ride.getDuration());
			distanceTextView.setText(String.format(Locale.UK, "%.1f", ride.getDistance()));
			maxSpeedTextView.setText((String.format(Locale.UK, "%.2f", ride.getMaxSpeed())));
			avgSpeedTextView.setText(String.format(Locale.UK, "%.0f", ride.getAvgSpeed()));
			elevLossTextView.setText(String.format(Locale.UK, "%.1f", ride.getElevationLoss()));
			elevGainTextView.setText(String.format(Locale.UK, "%.1f", ride.getElevationGain()));

			DatabaseHandler db = new DatabaseHandler(this);
			//db.dropDB();
			Log.d ("Insert:" , "Inserting...");



			db.addRide(ride);

			db.getRide(ride.getID());

			Log.d("Reading: ", "Reading All Rides...");
			List<Ride> rides = db.getAllRides();
			for (Ride ride1 : rides)
			{
				String log = "ID: " + ride1.getID() + " ,BikeID: " + ride1.getBikeID() +
									 ", avgSpeed: " + ride1.getAvgSpeed() + ", maxSpeed: " +
									 ride1.getMaxSpeed() + ", distance: " + ride1.getDistance() +
									 ", elevationLoss: " + ride1.getElevationLoss() +
						             ", elevationGain: " + ride1.getElevationGain() + ", AvgPace: " +
									 ride1.getAvgPace() + ", duration: " +  ride1.getDuration() +
									 ", rideDate: " + ride1.getRideDate();
				Log.d("DBTest: ", log);
			}


		}
	}
