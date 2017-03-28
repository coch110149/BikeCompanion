package com.cochrane.clinton.bikecompanion;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import static com.cochrane.clinton.bikecompanion.MainActivity.PICK_RIDING_BIKE;


@SuppressWarnings ( "LawOfDemeter" ) public class RideSummaryActivity extends AppCompatActivity
{
    private final DatabaseHandler db = new DatabaseHandler(this);
    private android.widget.TextView bikeNameTextView;
    private Ride ride;


    @Override protected void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ride_summary);
            final Bundle bundle = getIntent().getExtras();
            final Resources res = getResources();
            ride = bundle.getParcelable("CurrentRideObject");
            final TextView durationText = (TextView) findViewById(R.id.Duration_Information);
            final TextView distanceText = (TextView) findViewById(R.id.Distance_Information);
            final TextView maxSpeedText = (TextView) findViewById(R.id.MaxSpeed_Information);
            final TextView avgSpeedText = (TextView) findViewById(R.id.AvgSpeed_Information);
            final TextView elevLossText = (TextView) findViewById(R.id.ElevationLoss_Information);
            final TextView elevGainText = (TextView) findViewById(R.id.ElevationGain_Information);
            bikeNameTextView = (TextView) findViewById(R.id.bike_name);
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
            durationText.setText(res.getString(R.string.duration, ride.getDuration()));
            distanceText.setText(res.getString(R.string.distance, ride.getDistance()));
            avgSpeedText.setText(res.getString(R.string.avg_speed, ride.getAvgSpeed()));
            maxSpeedText.setText(res.getString(R.string.max_speed, ride.getMaxSpeed()));
            elevLossText.setText(res.getString(R.string.elevation_loss, ride.getElevationLoss()));
            elevGainText.setText(res.getString(R.string.elevation_gain, ride.getElevationGain()));
        }


    public void SaveRide(final View _view)
        {
            if(!"not started".equals(ride.getDuration()))
            {
                db.addRide(ride);
                db.updateBike(ride);
            }
            startActivity(new Intent(RideSummaryActivity.this, RideHistoryActivity.class));

            //Log.d("long ride" ,Double.toString(db.getRide(0).getDistance
            // ()));
        }


    public void DeleteRide(final View _view)
        {
            final AlertDialog.Builder deleteRideDialogBuilder = new AlertDialog.Builder(this);
            deleteRideDialogBuilder.setMessage(R.string.confirm_delete);
            deleteRideDialogBuilder
                    .setPositiveButton(R.string.yes_exact, new DialogInterface.OnClickListener()
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
                    .setNegativeButton(R.string.no_exact, new DialogInterface.OnClickListener()
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
                    oldBike.setDistance(oldBike.getDistance() - ride.getDistance());
                    oldBike.setLastRideDate("");
                    db.updateBike(oldBike);
                    ride.setBikeId(Integer.parseInt(data.getData().toString()));
                    final Bike newBike = db.getBike(ride.getBikeId());
                    newBike.setDistance(newBike.getDistance() + ride.getDistance());
                    newBike.setLastRideDate(ride.getRideDate());
                    db.updateBike(newBike);
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
                bikeNameTextView.setText(bike.getName());
            }
            super.onResume();
        }
}

