package com.cochrane.clinton.bikecompanion;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Date;


@SuppressWarnings ( "UnusedParameters" ) public class RideActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
                           LocationListener
{
    private static final int REQUEST_CODE = 666;
    private static final double ACCURACY_IN_METERS = 21.0;
    private static final String TAG = "BC-riding-activity";
    private static final long UPDATE_INTERVAL_IN_MS = 2000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MS = UPDATE_INTERVAL_IN_MS / 2;
    private final Ride ride = new Ride();
    private String debuggingInformationthatwillbedeletedlater =
            "Latitude,Longitude,Elevation,Speed,Accuracy," +
            "provider,time stamp,Extras," +
            "elapsedTime,distanceCovered," +
            "currentSpeed,_id,bike_id,_avg_speed," +
            "_max speed,_distance,_elevation_loss," +
            "_elevation_gain,_duration,ride_date";
    private int mNumberOfLocationUpdates;
    private long mTimeWhenPaused;
    private float mSpeedSum;
    private boolean mBound = false;
    private Resources mRes;
    private RBMService mService;
    private final ServiceConnection mConnection = new ServiceConnection()
    {
        @Override public void onServiceConnected(final ComponentName name, final IBinder service)
            {
                final RBMService.RBMBinder binder = (RBMService.RBMBinder) service;
                mService = binder.getService();
                mBound = true;
            }


        @Override public void onServiceDisconnected(final ComponentName name)
            {
                mBound = false;
            }
    };
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private TextView mElevationGainText;
    private TextView mElevationLossText;
    private Location mCurrentLocation;
    private Button mStartRideButton;
    private Button mPauseRideButton;
    private TextView mElevationText;
    private TextView mDistanceText;
    private TextView mMaxSpeedText;
    private TextView mAvgSpeedText;
    private Button mStopRideButton;
    private Chronometer mDuration;
    private TextView mSpeedText;
    //ui widgets
    private Button start_moving_manual_log_debugging_button_that_will_be_deleted;


    @Override protected void onResume()
        {
            super.onResume();
            if(!mBound)
            {
                final Intent intent = new Intent(this, RBMService.class);
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            }
        }


    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults)
        {
            if(requestCode == REQUEST_CODE)
            {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    startLocationUpdates();
                }else
                {
                    //// TODO: 25/03/2017 need to be a snack bar with option to go into settings
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }
            }
        }


    @SuppressWarnings ( {"MissingPermission"} ) private void startLocationUpdates()
        {
            if(runtimePermissions())
            {
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
            }
        }


    private boolean runtimePermissions()
        {
            boolean response = true;
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
               != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                response = false;
            }
            return response;
        }


    @Override protected void onCreate(final Bundle savedInstanceState)
        {
            final Intent intent = getIntent();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ride);
            ride.setBikeId(intent.getIntExtra("SelectableBikeID", -1));
            mSpeedText = (TextView) findViewById(R.id.Speed_Information);
            mStopRideButton = (Button) findViewById(R.id.StopRideButton);
            mStartRideButton = (Button) findViewById(R.id.StartRideButton);
            mPauseRideButton = (Button) findViewById(R.id.PauseRideButton);
            mDuration = (Chronometer) findViewById(R.id.Duration_Information);
            mMaxSpeedText = (TextView) findViewById(R.id.MaxSpeed_Information);
            mDistanceText = (TextView) findViewById(R.id.Distance_Information);
            mAvgSpeedText = (TextView) findViewById(R.id.AvgSpeed_Information);
            mElevationText = (TextView) findViewById(R.id.Elevation_Information);
            mElevationLossText = (TextView) findViewById(R.id.ElevationLoss_Information);
            mElevationGainText = (TextView) findViewById(R.id.ElevationGain_Information);
            mStopRideButton.setVisibility(View.GONE);
            mStartRideButton.setVisibility(View.GONE);
            mPauseRideButton.setVisibility(View.GONE);
            mRes = getResources();
            start_moving_manual_log_debugging_button_that_will_be_deleted =
                    (Button) findViewById(R.id.start_moving_manual_log);
            start_moving_manual_log_debugging_button_that_will_be_deleted.setVisibility(View.GONE);
        }


    @Override protected void onStart()
        {
            super.onStart();
            buildGoogleApiClient();
            if(mGoogleApiClient != null)
            {
                mGoogleApiClient.connect();
            }
        }


    private synchronized void buildGoogleApiClient()
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                                       .addConnectionCallbacks(this)
                                       .addOnConnectionFailedListener(this)
                                       .addApi(LocationServices.API).build();
            createLocationRequest();
        }


    private void createLocationRequest()
        {
            mLocationRequest = new LocationRequest()
                                       .setInterval(UPDATE_INTERVAL_IN_MS)
                                       .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MS)
                                       .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }


    @SuppressWarnings ( "EmptyMethod" )
    @Override protected void onStop()
        {
            super.onStop();
        }


    @Override public void onConnectionFailed(@NonNull final ConnectionResult connectionResult)
        {
            final String msg = "ConnectionFailed with errorCode: " + connectionResult.getErrorCode()
                               + "\n errorMessage: " + connectionResult.getErrorMessage();
            Log.i(TAG, msg);
        }


    @Override public void onConnected(@Nullable final Bundle bundle)
        {
            if(mPauseRideButton.getVisibility() != View.VISIBLE)
            {
                mStartRideButton.setVisibility(View.VISIBLE);
            }
        }


    @Override public void onConnectionSuspended(final int i)
        {
            mGoogleApiClient.connect();
        }


    public void StartRide(final View _view)
        {
            mDuration.setBase(SystemClock.elapsedRealtime() + mTimeWhenPaused);
            mPauseRideButton.setVisibility(View.VISIBLE);
            mStartRideButton.setVisibility(View.GONE);
            mStopRideButton.setVisibility(View.GONE);
            mDuration.start();
            start_moving_manual_log_debugging_button_that_will_be_deleted.setVisibility(View.GONE);
            if(runtimePermissions())
            {
                startLocationUpdates();
                if(mBound)
                {
                    mService.beginRide(this);
                }
            }
        }


    public String getMessage()
        {
            String msg = "";
            if(mCurrentLocation != null)
            {
                msg += "Here is the latest information on my bike ride" + "\n" +
                       "Latitude: " + mCurrentLocation.getLatitude() + "\n" +
                       "Longitude: " + mCurrentLocation.getLongitude() + "\n" +
                       "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
                       "Bearing: " + mCurrentLocation.getBearing() + "\n" +
                       "Speed: " + mCurrentLocation.getSpeed();
            }
            return msg;
        }


    public void StartMovingManualLogging(final View _view)
        {
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_EMAIL, "clinton.edward.cochrane@gmail.com");
            intent.putExtra(Intent.EXTRA_TEXT, debuggingInformationthatwillbedeletedlater);
            if(intent.resolveActivity(getPackageManager()) != null)
            {
                startActivity(intent);
            }
        }


    public void PauseRide(final View _view)
        {
            mTimeWhenPaused = mDuration.getBase() - SystemClock.elapsedRealtime();
            ride.setDuration(mDuration.getText().toString());
            mStartRideButton.setVisibility(View.VISIBLE);
            mStopRideButton.setVisibility(View.VISIBLE);
            mPauseRideButton.setVisibility(View.GONE);
            stopLocationUpdates();
            mDuration.stop();
            start_moving_manual_log_debugging_button_that_will_be_deleted
                    .setVisibility(View.VISIBLE);
            if(mBound)
            {
                mService.stopRide();
                unbindService(mConnection);
                mBound = false;
            }
        }


    private void stopLocationUpdates()
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }


    public void StopRide(final View _view)
        {
            final AlertDialog.Builder stopRideDialogBuilder = new AlertDialog.Builder(this);
            stopRideDialogBuilder.setMessage(R.string.confirm_exit_ride);
            stopRideDialogBuilder
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                    {
                        @Override public void onClick(final DialogInterface dialog, final int which)
                            {
                                mGoogleApiClient.disconnect();
                                ride.setRideDate(new Date().toString());
                                final Intent intent =
                                        new Intent(RideActivity.this, RideSummaryActivity.class);
                                intent.putExtra("CurrentRideObject", ride);
//                                intent.putExtra("DebugMessage",
//                                                debuggingInformationthatwillbedeletedlater);
                                startActivity(intent);
                            }
                    });
            //noinspection AnonymousInnerClassMayBeStatic
            stopRideDialogBuilder
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
                    {
                        @Override public void onClick(final DialogInterface dialog, final int which)
                            {
                                dialog.dismiss();
                            }
                    });
            final AlertDialog stopRideDialog = stopRideDialogBuilder.create();
            stopRideDialog.show();
        }


    @Override public void onLocationChanged(final Location location)
        {
            debuggingInformationthatwillbedeletedlater += "\n\n\n" +
                                                          location.getLatitude() + "," +
                                                          location.getLongitude() + "," +
                                                          location.getAltitude() + "," +
                                                          location.getSpeed() + "," +
                                                          location.getAccuracy() + "," +
                                                          location.getProvider() + "," +
                                                          new Date() + "," + location.getExtras();
            final Location mPreviousLocation = mCurrentLocation;
            mCurrentLocation = location;
            if((location.getAccuracy() <= ACCURACY_IN_METERS) && (location.getAccuracy() > 0.0))
            {
                final float currentSpeed;
                final float distanceCovered;
                float elapsedTime = mCurrentLocation.getElapsedRealtimeNanos()
                                    - mPreviousLocation.getElapsedRealtimeNanos();
                elapsedTime /= 1000000000.0;
                distanceCovered = mCurrentLocation.distanceTo(mPreviousLocation) / 1000;
                ride.incrementDistance(distanceCovered);
                currentSpeed = calculateSpeed(distanceCovered * 1000, elapsedTime);
                calculateElevationChange(mPreviousLocation);
                updateUI(currentSpeed);
                debuggingInformationthatwillbedeletedlater += "," + elapsedTime + "," +
                                                              distanceCovered + "," +
                                                              currentSpeed + "," +
                                                              ride.toString();
            }
        }


    private float calculateSpeed(final float distanceCovered, final float elapsedTime)
        {
            float currentSpeed = 18.0f / 5.0f;
            if(mCurrentLocation.hasSpeed())
            {
                currentSpeed *= mCurrentLocation.getSpeed();
                if(ride.getMaxSpeed() < currentSpeed)
                {
                    ride.setMaxSpeed(currentSpeed);
                }
            }else
            {
                currentSpeed *= distanceCovered / elapsedTime;
            }
            mSpeedSum += currentSpeed;
            mNumberOfLocationUpdates += 1;
            ride.setAvgSpeed(mSpeedSum / mNumberOfLocationUpdates);
            return currentSpeed;
        }


    private void calculateElevationChange(final Location previousLocation)
        {
            final double eleChange;
            if((mCurrentLocation.getAltitude() != 0) && (previousLocation.getAltitude() != 0))
            {
                if(mCurrentLocation.getAltitude() > previousLocation.getAltitude())
                {
                    eleChange = mCurrentLocation.getAltitude() - previousLocation.getAltitude();
                    ride.setElevationGain(ride.getElevationGain() + eleChange);
                }else if(mCurrentLocation.getAltitude() < previousLocation.getAltitude())
                {
                    eleChange = previousLocation.getAltitude() - mCurrentLocation.getAltitude();
                    ride.setElevationLoss(ride.getElevationLoss() + eleChange);
                }
            }
        }


    private void updateUI(final double currentSpeed)
        {
            mMaxSpeedText.setText(mRes.getString(R.string.max_speed, (Double) ride.getMaxSpeed()));
            mDistanceText.setText(mRes.getString(R.string.distance, ride.getDistance()));
            mAvgSpeedText.setText(mRes.getString(R.string.avg_speed, ride.getAvgSpeed()));
            mSpeedText.setText(mRes.getString(R.string.speed, currentSpeed));
            mElevationText
                    .setText(mRes.getString(R.string.elevation, mCurrentLocation.getAltitude()));
            mElevationGainText
                    .setText(mRes.getString(R.string.elevation_gain, ride.getElevationGain()));
            mElevationLossText
                    .setText(mRes.getString(R.string.elevation_loss, ride.getElevationLoss()));
        }
}