package com.cochrane.clinton.bikecompanion;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
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
import java.util.Locale;


public class RideActivity extends AppCompatActivity implements
		GoogleApiClient.ConnectionCallbacks,
				GoogleApiClient.OnConnectionFailedListener, LocationListener
	{
	protected static final String TAG = "BC-riding-activity";
	private static final long UPDATE_INTERVAL_IN_MS = 5000;
	private static final long FASTEST_UPDATE_INTERVAL_IN_MS = UPDATE_INTERVAL_IN_MS / 2;

	public Ride ride = new Ride();
	private long mTimeWhenPaused = 0;
	private Location mCurrentLocation;
	private GoogleApiClient mGoogleApiClient;
	private int mNumberOfLocationUpdates = 0;
	private LocationRequest mLocationRequest;
	//ui widgets
	private Button mStartRideButton;
	private Button mPauseRideButton;
	private Button mStopRideButton;
	private Chronometer mDurationTextView;
	private TextView mDistanceTextView;
	private TextView mMaxSpeedTextView;
	private TextView mAvgSpeedTextView;
	private TextView mElevationTextView;
	private TextView mElevationGainTextView;
	private TextView mElevationLossTextView;
	private TextView mSpeedTextView;


	@Override
	protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_ride);
			buildGoogleApiClient();
			mStopRideButton = (Button) findViewById(R.id.StopRideButton);
			mStartRideButton = (Button) findViewById(R.id.StartRideButton);
			mPauseRideButton = (Button) findViewById(R.id.PauseRideButton);
			mSpeedTextView = (TextView) findViewById(R.id.Speed_Information);
			mDistanceTextView = (TextView) findViewById(R.id.Distance_Information);
			mMaxSpeedTextView = (TextView) findViewById(R.id.MaxSpeed_Information);
			mAvgSpeedTextView = (TextView) findViewById(R.id.AvgSpeed_Information);
			mElevationTextView = (TextView) findViewById(R.id.Elevation_Information);
			mDurationTextView = (Chronometer) findViewById(R.id.Duration_Information);
			mElevationLossTextView = (TextView) findViewById(R.id.ElevationLoss_Information);
			mElevationGainTextView = (TextView) findViewById(R.id.ElevationGain_Information);

			mStopRideButton.setVisibility(View.GONE);
			mPauseRideButton.setVisibility(View.GONE);
			mStartRideButton.setVisibility(View.GONE);

		}


	private synchronized void buildGoogleApiClient()
		{
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					                   .addConnectionCallbacks(this)
					                   .addOnConnectionFailedListener(this)
					                   .addApi(LocationServices.API)
					                   .build();
			createLocationRequest();
		}


	private void createLocationRequest()
		{
			mLocationRequest = new LocationRequest();
			mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MS);
			mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MS);
			mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		}


	@Override
	public void onConnectionSuspended(int i)
		{
			mGoogleApiClient.connect();
		}


	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
		{
			String msg = "ConnectionFailed with errorCode: " + connectionResult.getErrorCode() +
					             "\n errorMessage: " + connectionResult.getErrorMessage();
			Log.i(TAG, msg);
		}


	@Override
	public void onConnected(@Nullable Bundle bundle)
		{
			mStartRideButton.setVisibility(View.VISIBLE);
		}


	public void StartRide(View v)
		{
			mDurationTextView.setBase(SystemClock.elapsedRealtime() + mTimeWhenPaused);
			mDurationTextView.start();
			mPauseRideButton.setVisibility(View.VISIBLE);
			mStartRideButton.setVisibility(View.GONE);
			mStopRideButton.setVisibility(View.GONE);
			if (runtimePermissions())
			{
				startLocationUpdates();
			}
		}

	public void PauseRide(View v)
		{
			mTimeWhenPaused = (mDurationTextView.getBase() - SystemClock.elapsedRealtime());
			ride.setDuration(mDurationTextView.getText().toString());
			mStartRideButton.setVisibility(View.VISIBLE);
			mStopRideButton.setVisibility(View.VISIBLE);
			mPauseRideButton.setVisibility(View.GONE);
			mDurationTextView.stop();
			stopLocationUpdates();
		}


	public void StopRide(View v)
		{
			AlertDialog.Builder stopRideDialogBuilder = new AlertDialog.Builder(this);
			stopRideDialogBuilder.setMessage(R.string.confirm_exit_ride);
			stopRideDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
						{
							mGoogleApiClient.disconnect();
							ride.setRideDate(new Date().toString());
							Intent intent = new Intent(RideActivity.this, RideSummaryActivity.class);
							intent.putExtra("CurrentRideObject", ride);
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


	private boolean runtimePermissions()
		{
			boolean response;
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
					    != PackageManager.PERMISSION_GRANTED)
			{
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 666);
				response = false;
			} else
			{
				response = true;
			}
			return response;
		}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
	                                       @NonNull int[] grantResults)
		{
			if (requestCode == 666)
			{
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
				{
					startLocationUpdates();
				} else
				{
					Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
				}
			}
		}

	@SuppressWarnings({"MissingPermission"})
	private void startLocationUpdates()
		{
			if (runtimePermissions())
			{
				mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation
						                                                     (mGoogleApiClient);
				LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
						mLocationRequest, this);
			}
		}

	private void stopLocationUpdates()
		{
			LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
		}

	@Override
	public void onLocationChanged(Location location)
		{
			if (location.getAccuracy() <= 30.0)
			{
				Location mPreviousLocation = mCurrentLocation;
				mCurrentLocation = location;
				float distanceCovered;
				float currentSpeed;
				float elapsedTime = mCurrentLocation.getElapsedRealtimeNanos() -
						                    mPreviousLocation.getElapsedRealtimeNanos();
				elapsedTime /= 1000000000.0;
				distanceCovered = mCurrentLocation.distanceTo(mPreviousLocation);
				ride.setDistance((ride.getDistance() + distanceCovered) / 1000);
				currentSpeed = calculateSpeed(distanceCovered, elapsedTime);
				calculateElevationChange(mPreviousLocation);
				updateUI(currentSpeed);
			}

		}

	private float calculateSpeed(float distanceCovered, float elapsedTime)
		{
			float currentSpeed = mCurrentLocation.getSpeed();
			double speedSum = ride.getAvgSpeed() * mNumberOfLocationUpdates;
			if (!mCurrentLocation.hasSpeed())
			{
				currentSpeed = distanceCovered / elapsedTime;
			}
			currentSpeed = (currentSpeed * 18) / 5;
			mNumberOfLocationUpdates += 1;
			ride.setAvgSpeed(speedSum / mNumberOfLocationUpdates);
			if (ride.getMaxSpeed() < currentSpeed)
			{
				ride.setMaxSpeed(currentSpeed);
			}
			return currentSpeed;
		}

	private void calculateElevationChange(Location previousLocation)
		{
			double eleChange;
			if (mCurrentLocation.getAltitude() != 0 && previousLocation.getAltitude() != 0)
			{
				if (mCurrentLocation.getAltitude() > previousLocation.getAltitude())
				{
					eleChange = mCurrentLocation.getAltitude() - previousLocation.getAltitude();
					ride.setElevationGain(ride.getElevationGain() + eleChange);
				} else if (mCurrentLocation.getAltitude() < previousLocation.getAltitude())
				{
					eleChange = previousLocation.getAltitude() - mCurrentLocation.getAltitude();
					ride.setElevationGain(ride.getElevationLoss() + eleChange);
				}
			}
		}

	private void updateUI(double currentSpeed)
		{
			mMaxSpeedTextView.setText(String.format(Locale.UK, "%.1f", ride.getMaxSpeed()));
			mDistanceTextView.setText(String.format(Locale.UK, "%.1f", ride.getDistance()));
			mAvgSpeedTextView.setText(String.format(Locale.UK, "%.1f", ride.getAvgSpeed()));
			mElevationTextView.setText(String.format(Locale.UK, "%.1f", mCurrentLocation.getAltitude()));
			mElevationGainTextView.setText(String.format(Locale.UK, "%.1f", ride.getElevationGain()));
			mElevationLossTextView.setText(String.format(Locale.UK, "%.1f", ride.getElevationLoss()));
			mSpeedTextView.setText(String.format(Locale.UK, "%.1f", currentSpeed));
		}

	@Override
	protected void onStart()
		{
			super.onStart();
			if (mGoogleApiClient != null)
			{
				mGoogleApiClient.connect();
			}
		}

	@Override
	protected void onStop()
		{
			super.onStop();
		}
	}
