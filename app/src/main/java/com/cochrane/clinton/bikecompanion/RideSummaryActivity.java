package com.cochrane.clinton.bikecompanion;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import static com.cochrane.clinton.bikecompanion.MainActivity.PICK_RIDING_BIKE;


public class RideSummaryActivity extends AppCompatActivity
{
    private final DatabaseHandler db = new DatabaseHandler(this);
    private android.widget.TextView bikeNameTextView;
    private Ride ride;


    @Override protected void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ride_summary);
            final Bundle bundle = getIntent().getExtras();
            ride = bundle.getParcelable("CurrentRideObject");
            final TextView durationTextView = (TextView) findViewById(R.id.Duration_Information);
            final TextView distanceTextView = (TextView) findViewById(R.id.Distance_Information);
            final TextView maxSpeedTextView = (TextView) findViewById(R.id.MaxSpeed_Information);
            final TextView avgSpeedTextView = (TextView) findViewById(R.id.AvgSpeed_Information);
            final TextView elevLossTextView =
                    (TextView) findViewById(R.id.ElevationLoss_Information);
            final TextView elevGainTextView =
                    (TextView) findViewById(R.id.ElevationGain_Information);
            bikeNameTextView = (TextView) findViewById(R.id.bike_name_information);
            bikeNameTextView.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(final View v)
                    {
                        final Intent intent = new Intent(RideSummaryActivity.this,
                                                         SelectionActivity.class);
                        intent.putExtra("TypeOfRequest", "Bike");
                        startActivityForResult(intent, PICK_RIDING_BIKE);
                    }
            });
            findViewById(R.id.bike_name_label).setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(final View v)
                    {
                        final Intent intent = new Intent(RideSummaryActivity.this,
                                                         SelectionActivity.class);
                        intent.putExtra("TypeOfRequest", "Bike");
                        startActivityForResult(intent, PICK_RIDING_BIKE);
                    }
            });
            durationTextView.setText(ride.getDuration());
            distanceTextView.setText(String.format(Locale.UK, "%.1f", ride.getDistance()));
            avgSpeedTextView.setText(String.format(Locale.UK, "%.0f", ride.getAvgSpeed()));
            maxSpeedTextView
                    .setText(String.format(java.util.Locale.UK, "%.2f", ride.getMaxSpeed()));
            elevLossTextView.setText(String.format(Locale.UK, "%.1f", ride.getElevationLoss()));
            elevGainTextView.setText(String.format(Locale.UK, "%.1f", ride.getElevationGain()));
        }


    public void SaveRide(final View view)
        {
            if(!"not started".equals(ride.getDuration()))
            {
                if(db.getRide(ride.getId()) == null)
                {
                    db.addRide(ride);
                }else
                {
                    db.updateRide(ride);
                }
            }
            final Intent intent = new Intent(RideSummaryActivity.this, RideHistoryActivity.class);
            startActivity(intent);
            //Log.d("long ride" ,Double.toString(db.getRide(0).getDistance
            // ()));
        }


    public void DeleteRide(final View view)
        {
            final AlertDialog.Builder deleteRideDialogBuilder = new AlertDialog.Builder(this);
            deleteRideDialogBuilder.setMessage(R.string.confirm_delete_ride);
            deleteRideDialogBuilder
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                    {
                        @Override public void onClick(final DialogInterface dialog, final int which)
                            {
                                final DatabaseHandler db =
                                        new DatabaseHandler(RideSummaryActivity.this);
                                db.deleteRide(ride);
                                final Intent intent = new Intent(RideSummaryActivity.this,
                                                                 RideHistoryActivity.class);
                                startActivity(intent);
                            }
                    });
            //noinspection AnonymousInnerClassMayBeStatic
            deleteRideDialogBuilder
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
                    {
                        @Override public void onClick(final DialogInterface dialog, final int which)
                            {
                                dialog.dismiss();
                            }
                    });
            final AlertDialog deleteRideDialog = deleteRideDialogBuilder.create();
            deleteRideDialog.show();
        }


    @Override protected void onActivityResult(final int requestCode, final int resultCode,
                                              final Intent data)
        {
            if(requestCode == PICK_RIDING_BIKE)
            {
                if(resultCode == RESULT_OK)
                {
                    final Bike oldBike = db.getBike(ride.getBikeId());
                    oldBike.setTotalBikeDistance(
                            oldBike.getTotalBikeDistance() - ride.getDistance());
                    ride.setBikeId(Integer.parseInt(data.getData().toString()));
                    final Bike newBike = db.getBike(ride.getBikeId());
                    newBike.setTotalBikeDistance(
                            newBike.getTotalBikeDistance() + ride.getDistance());
                }else
                {
                    android.widget.Toast
                            .makeText(this, "result not okay", android.widget.Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }


    @Override protected void onResume()
        {
            if(ride.getBikeId() != -1)
            {
                final Bike bike = db.getBike(ride.getBikeId());
                bikeNameTextView.setText(bike.getBikeName());
            }
            super.onResume();
        }
}

