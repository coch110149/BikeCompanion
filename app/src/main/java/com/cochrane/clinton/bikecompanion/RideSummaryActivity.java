package com.cochrane.clinton.bikecompanion;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

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

			TextView durationTextView = (TextView) findViewById(R.id.Duration_Information);
			TextView distanceTextView = (TextView) findViewById(R.id.Distance_Information);
			TextView maxSpeedTextView = (TextView) findViewById(R.id.MaxSpeed_Information);
			TextView avgSpeedTextView = (TextView) findViewById(R.id.AvgSpeed_Information);
			TextView elevLossTextView = (TextView) findViewById(R.id.ElevationLoss_Information);
			TextView elevGainTextView = (TextView) findViewById(R.id.ElevationGain_Information);

			durationTextView.setText(ride.getDuration());
			distanceTextView.setText(String.format(Locale.UK, "%.1f", ride.getDistance()));
			maxSpeedTextView.setText((String.format(Locale.UK, "%.2f", ride.getMaxSpeed())));
			avgSpeedTextView.setText(String.format(Locale.UK, "%.0f", ride.getAvgSpeed()));
			elevLossTextView.setText(String.format(Locale.UK, "%.1f", ride.getElevationLoss()));
			elevGainTextView.setText(String.format(Locale.UK, "%.1f", ride.getElevationGain()));
		}

	public void SaveRide(View view)
		{

			DatabaseHandler db = new DatabaseHandler(this);
			if (db.getRide(ride.getID()) == null && !ride.getDuration().equals("not started"))
			{
				db.addRide(ride);
			}

			Intent intent = new Intent(RideSummaryActivity.this, RideHistoryActivity.class);
			startActivity(intent);
			//Log.d("long ride" ,Double.toString(db.getRide(0).getDistance()));
		}

	public void DeleteRide(View view)
		{
			AlertDialog.Builder stopRideDialogBuilder = new AlertDialog.Builder(this);
			stopRideDialogBuilder.setMessage(R.string.confirm_delete_ride);
			stopRideDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
						{
							DatabaseHandler db = new DatabaseHandler(RideSummaryActivity.this);
							db.deleteRide(ride);
							Intent intent = new Intent(RideSummaryActivity.this, RideHistoryActivity.class);
							startActivity(intent);
						}
				});
			stopRideDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
						{
							dialog.dismiss();
						}
				});
			AlertDialog stopRideDialog = stopRideDialogBuilder.create();
			stopRideDialog.show();


		}
	}
