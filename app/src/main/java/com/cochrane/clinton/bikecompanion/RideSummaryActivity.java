package com.cochrane.clinton.bikecompanion;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static com.cochrane.clinton.bikecompanion.MainActivity.PICK_RIDING_BIKE;


public class RideSummaryActivity extends AppCompatActivity
	{
	TextView durationTextView;
	TextView distanceTextView;
	TextView maxSpeedTextView;
	TextView avgSpeedTextView;
	TextView elevLossTextView;
	TextView elevGainTextView;
	TextView bikeNameTextView;
	private Ride ride;
	private DatabaseHandler db = new DatabaseHandler(this);


	@Override protected void onCreate( Bundle savedInstanceState )
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_ride_summary);
			Bundle bundle = getIntent().getExtras();
			ride = bundle.getParcelable("CurrentRideObject");
			durationTextView = (TextView) findViewById(R.id.Duration_Information);
			distanceTextView = (TextView) findViewById(R.id.Distance_Information);
			maxSpeedTextView = (TextView) findViewById(R.id.MaxSpeed_Information);
			avgSpeedTextView = (TextView) findViewById(R.id.AvgSpeed_Information);
			elevLossTextView = (TextView) findViewById(R.id.ElevationLoss_Information);
			elevGainTextView = (TextView) findViewById(R.id.ElevationGain_Information);
			bikeNameTextView = (TextView) findViewById(R.id.bike_name_information);
			bikeNameTextView.setOnClickListener(new View.OnClickListener()
				{
					@Override public void onClick( View v )
						{
							Intent intent = new Intent(RideSummaryActivity.this, SelectionActivity
									                                                     .class);
							intent.putExtra("TypeOfRequest", "Bike");
							startActivityForResult(intent, PICK_RIDING_BIKE);
						}
				});
			durationTextView.setText(ride.getDuration());
			distanceTextView.setText(String.format(Locale.UK, "%.1f", ride.getDistance()));
			avgSpeedTextView.setText(String.format(Locale.UK, "%.0f", ride.getAvgSpeed()));
			maxSpeedTextView.setText((String.format(Locale.UK, "%.2f", ride.getMaxSpeed())));
			elevLossTextView.setText(String.format(Locale.UK, "%.1f", ride.getElevationLoss()));
			elevGainTextView.setText(String.format(Locale.UK, "%.1f", ride.getElevationGain()));
		}


	@Override protected void onResume()
		{
			if(ride.getBikeID() != -1)
			{
				Bike bike = db.getBike(ride.getBikeID());
				bikeNameTextView.setText(bike.getBikeName());
			}
			super.onResume();
		}


	public void SaveRide( View view )
		{
			if(!ride.getDuration().equals("not started"))
			{
				if(db.getRide(ride.getID()) == null)
				{
					db.addRide(ride);
				} else
				{
					db.updateRide(ride);
				}
			}
			Intent intent = new Intent(RideSummaryActivity.this, RideHistoryActivity.class);
			startActivity(intent);
			//Log.d("long ride" ,Double.toString(db.getRide(0).getDistance
			// ()));
		}


	public void DeleteRide( View view )
		{
			AlertDialog.Builder deleteRideDialogBuilder = new AlertDialog.Builder(this);
			deleteRideDialogBuilder.setMessage(R.string.confirm_delete_ride);
			deleteRideDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
				{
					@Override public void onClick( DialogInterface dialog, int which )
						{
							DatabaseHandler db = new DatabaseHandler(RideSummaryActivity.this);
							db.deleteRide(ride);
							Intent intent = new Intent(RideSummaryActivity.this,
									                          RideHistoryActivity.class);
							startActivity(intent);
						}
				});
			deleteRideDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
				{
					@Override public void onClick( DialogInterface dialog, int which )
						{
							dialog.dismiss();
						}
				});
			AlertDialog deleteRideDialog = deleteRideDialogBuilder.create();
			deleteRideDialog.show();
		}


	@Override protected void onActivityResult( int requestCode, int resultCode, Intent data )
		{
			if(requestCode == PICK_RIDING_BIKE)
			{
				if(resultCode == RESULT_OK)
				{
					Bike oldBike = db.getBike(ride.getBikeID());
					oldBike.setTotalBikeDistance(oldBike.getTotalBikeDistance() - ride.getDistance());
					ride.setBikeID(Integer.parseInt(data.getData().toString()));
					Bike newBike = db.getBike(ride.getBikeID());
					newBike.setTotalBikeDistance(newBike.getTotalBikeDistance() + ride.getDistance());
				}
				Toast.makeText(this, "result not okay", Toast.LENGTH_SHORT).show();
			}
		}
	}

