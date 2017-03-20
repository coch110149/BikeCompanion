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


//CHECKSTYLE:OFF: checkstyle:magicnumber
public class RideActivity extends AppCompatActivity
		implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
				           LocationListener
	{
	public static final int REQUEST_CODE = 666;
	public static final double ACCURACY_IN_METERS = 30.0;
	protected static final String TAG = "BC-riding-activity";
	private static final long UPDATE_INTERVAL_IN_MS = 5000;
	private static final long FASTEST_UPDATE_INTERVAL_IN_MS = UPDATE_INTERVAL_IN_MS / 2;
	public Ride ride = new Ride();
	String debuggingInformationthatwillbedeletedlater = "Latitude,Longitude,Elevation,Speed,Accuracy," +
			                                                    "provider,time stamp,Extras," +
			                                                    "elapsedTime,distanceCovered," +
			                                                    "currentSpeed,_id,bike_id,_avg_speed," +
			                                                    "_maxspeed,_distance,_elevation_loss," +
			                                                    "_elevation_gain,_duration,ride_date";
	private long mTimeWhenPaused = 0;
	private Location mCurrentLocation;
	private GoogleApiClient mGoogleApiClient;
	private int mNumberOfLocationUpdates = 0;
	private float mSpeedSum = 0;
	private LocationRequest mLocationRequest;
	//ui widgets
	private Button mStartRideButton;
	private Button mPauseRideButton;
	private Button mStopRideButton;
	private Button start_moving_manual_log_debugging_button_that_will_be_deleted;
	private Chronometer mDurationTextView;
	private TextView mDistanceTextView;
	private TextView mMaxSpeedTextView;
	private TextView mAvgSpeedTextView;
	private TextView mElevationTextView;
	private TextView mElevationGainTextView;
	private TextView mElevationLossTextView;
	private TextView mSpeedTextView;


	@Override protected void onCreate( Bundle savedInstanceState )
		{
			Intent intent = getIntent();
			ride.setBikeID(intent.getIntExtra("SelectableBikeID", -1));
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_ride);
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
			start_moving_manual_log_debugging_button_that_will_be_deleted =
					(Button) findViewById(R.id.start_moving_manual_log);
			mStopRideButton.setVisibility(View.GONE);
			mPauseRideButton.setVisibility(View.GONE);
			mStartRideButton.setVisibility(View.GONE);
			start_moving_manual_log_debugging_button_that_will_be_deleted.setVisibility(View.GONE);
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
			mLocationRequest = new LocationRequest();
			mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MS);
			mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MS);
			mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		}


	@Override public void onConnectionSuspended( int i )
		{
			mGoogleApiClient.connect();
		}


	@Override public void onConnectionFailed( @NonNull ConnectionResult connectionResult )
		{
			String msg = "ConnectionFailed with errorCode: " +
					             connectionResult.getErrorCode() +
					             "\n errorMessage: " +
					             connectionResult.getErrorMessage();
			Log.i(TAG, msg);
		}


	@Override public void onConnected( @Nullable Bundle bundle )
		{
			mStartRideButton.setVisibility(View.VISIBLE);
		}


	public void StartRide( View v )
		{
			mDurationTextView.setBase(SystemClock.elapsedRealtime() + mTimeWhenPaused);
			mDurationTextView.start();
			mPauseRideButton.setVisibility(View.VISIBLE);
			start_moving_manual_log_debugging_button_that_will_be_deleted.setVisibility(View.GONE);
			mStartRideButton.setVisibility(View.GONE);
			mStopRideButton.setVisibility(View.GONE);
			if(runtimePermissions())
			{
				startLocationUpdates();
			}
		}


	public void StartMovingManualLogging( View v )
		{
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("*/*");
			intent.putExtra(Intent.EXTRA_EMAIL, "clinton.edward.cochrane@gmail.com");
			intent.putExtra(Intent.EXTRA_TEXT, debuggingInformationthatwillbedeletedlater);
			if(intent.resolveActivity(getPackageManager()) != null)
			{
				startActivity(intent);
			}
		}


	public void PauseRide( View v )
		{
			mTimeWhenPaused = (mDurationTextView.getBase() - SystemClock.elapsedRealtime());
			ride.setDuration(mDurationTextView.getText().toString());
			mStartRideButton.setVisibility(View.VISIBLE);
			mStopRideButton.setVisibility(View.VISIBLE);
			start_moving_manual_log_debugging_button_that_will_be_deleted.setVisibility(View.VISIBLE);
			mPauseRideButton.setVisibility(View.GONE);
			mDurationTextView.stop();
			stopLocationUpdates();
		}


	public void StopRide( View v )
		{
			AlertDialog.Builder stopRideDialogBuilder = new AlertDialog.Builder(this);
			stopRideDialogBuilder.setMessage(R.string.confirm_exit_ride);
			stopRideDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
				{
					@Override public void onClick( DialogInterface dialog, int which )
						{
							mGoogleApiClient.disconnect();
							ride.setRideDate(new Date().toString());
							Intent intent = new Intent(RideActivity.this, RideSummaryActivity.class);
							intent.putExtra("CurrentRideObject", ride);
							intent.putExtra("DebugMessage", debuggingInformationthatwillbedeletedlater);
							startActivity(intent);
						}
				});
			stopRideDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
				{
					@Override public void onClick( DialogInterface dialog, int which )
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
			if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
					   PackageManager.PERMISSION_GRANTED)
			{
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
						REQUEST_CODE);
				response = false;
			} else
			{
				response = true;
			}
			return response;
		}


	@Override
	public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions,
			                                      @NonNull int[] grantResults )
		{
			if(requestCode == REQUEST_CODE)
			{
				if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
				{
					startLocationUpdates();
				} else
				{
					Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
				}
			}
		}


	@SuppressWarnings({"MissingPermission"}) private void startLocationUpdates()
		{
			if(runtimePermissions())
			{
				mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
				LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
						mLocationRequest, this);
			}
		}


	private void stopLocationUpdates()
		{
			LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
		}


	@Override public void onLocationChanged( Location location )
		{
			debuggingInformationthatwillbedeletedlater += "\n\n\n" +
					                                              location.getLatitude() + "," +
					                                              location.getLongitude() + "," +
					                                              location.getAltitude() + "," +
					                                              location.getSpeed() + "," +
					                                              location.getAccuracy() + "," +
					                                              location.getProvider() + "," +
					                                              new Date() + "," +
					                                              location.getExtras();
			Location mPreviousLocation = mCurrentLocation;
			mCurrentLocation = location;
			if(location.getAccuracy() <= ACCURACY_IN_METERS && location.getAccuracy() > 0.0)
			{
				float distanceCovered;
				float currentSpeed;
				float elapsedTime = mCurrentLocation.getElapsedRealtimeNanos() -
						                    mPreviousLocation.getElapsedRealtimeNanos();
				elapsedTime /= 1000000000.0;
				distanceCovered = mCurrentLocation.distanceTo(mPreviousLocation) / 1000;
				ride.setDistance(ride.getDistance() + distanceCovered);
				currentSpeed = calculateSpeed(distanceCovered * 1000, elapsedTime);
				calculateElevationChange(mPreviousLocation);
				updateUI(currentSpeed);
				debuggingInformationthatwillbedeletedlater += "," + elapsedTime + "," +
						                                              distanceCovered + "," +
						                                              currentSpeed + "," +
						                                              ride.toString();
			}
		}


	private float calculateSpeed( float distanceCovered, float elapsedTime )
		{
			float currentSpeed = (18.0f / 5.0f);
			currentSpeed *= distanceCovered / elapsedTime;
			if(mCurrentLocation.hasSpeed())
			{
				currentSpeed *= mCurrentLocation.getSpeed();
				if(ride.getMaxSpeed() < currentSpeed)
				{
					ride.setMaxSpeed(currentSpeed);
				}
			}
			mSpeedSum += currentSpeed;
			mNumberOfLocationUpdates += 1;
			ride.setAvgSpeed(mSpeedSum / mNumberOfLocationUpdates);
			return currentSpeed;
		}


	private void calculateElevationChange( Location previousLocation )
		{
			double eleChange;
			if(mCurrentLocation.getAltitude() != 0 && previousLocation.getAltitude() != 0)
			{
				if(mCurrentLocation.getAltitude() > previousLocation.getAltitude())
				{
					eleChange = mCurrentLocation.getAltitude() - previousLocation.getAltitude();
					ride.setElevationGain(ride.getElevationGain() + eleChange);
				} else if(mCurrentLocation.getAltitude() < previousLocation.getAltitude())
				{
					eleChange = previousLocation.getAltitude() - mCurrentLocation.getAltitude();
					ride.setElevationLoss(ride.getElevationLoss() + eleChange);
				}
			}
		}


	private void updateUI( double currentSpeed )
		{
			mMaxSpeedTextView.setText(String.format(Locale.UK, "%.1f", ride.getMaxSpeed()));
			mDistanceTextView.setText(String.format(Locale.UK, "%.1f", ride.getDistance()));
			mAvgSpeedTextView.setText(String.format(Locale.UK, "%.1f", ride.getAvgSpeed()));
			mElevationTextView.setText(String.format(Locale.UK, "%.1f", mCurrentLocation.getAltitude()));
			mElevationGainTextView.setText(String.format(Locale.UK, "%.1f", ride.getElevationGain()));
			mElevationLossTextView.setText(String.format(Locale.UK, "%.1f", ride.getElevationLoss()));
			mSpeedTextView.setText(String.format(Locale.UK, "%.1f", currentSpeed));
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


	@Override protected void onStop()
		{
			super.onStop();
		}
	}