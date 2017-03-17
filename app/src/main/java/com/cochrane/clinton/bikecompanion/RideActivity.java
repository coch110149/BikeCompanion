package com.cochrane.clinton.bikecompanion;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
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
import java.util.Locale;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;


public class RideActivity extends AppCompatActivity
		implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
				           LocationListener
	{
	public static final int REQUEST_CODE = 666;
	public static final double ACCURACY_IN_METERS = 15.0;
	protected static final String TAG = "BC-riding-activity";
	private static final long UPDATE_INTERVAL_IN_MS = 2000;
	private static final long FASTEST_UPDATE_INTERVAL_IN_MS = UPDATE_INTERVAL_IN_MS / 2;
	public Ride ride = new Ride();
	private float mSpeedSum = 0;
	private boolean mBound = false;
	private long mTimeWhenPaused = 0;
	private int mNumberOfLocationUpdates = 0;
	private Location mCurrentLocation;
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	private RBMService mService;
	//ui widgets
	private Button mStopRideButton;
	private Button mStartRideButton;
	private Button mPauseRideButton;
	private TextView mSpeedTextView;
	private TextView mDistanceTextView;
	private TextView mMaxSpeedTextView;
	private TextView mAvgSpeedTextView;
	private TextView mElevationTextView;
	private TextView mElevationGainTextView;
	private TextView mElevationLossTextView;
	private Chronometer mDurationTextView;
	private ServiceConnection mConnection = new ServiceConnection()
		{
			@Override public void onServiceConnected( ComponentName name, IBinder service )
				{
					RBMService.RBMBinder binder =
							(RBMService.RBMBinder) service;
					mService = binder.getService();
					mBound = true;
				}


			@Override public void onServiceDisconnected( ComponentName name )
				{
					mBound = false;
				}
		};
	private boolean stopped;


	@Override protected void onCreate( Bundle savedInstanceState )
		{
			Intent intent = getIntent();
			ride.setBikeID(intent.getIntExtra("SelectableBikeID", -1));
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_ride);
			mSpeedTextView = (TextView) findViewById(R.id.Speed_Information);
			mStopRideButton = (Button) findViewById(R.id.StopRideButton);
			mStartRideButton = (Button) findViewById(R.id.StartRideButton);
			mPauseRideButton = (Button) findViewById(R.id.PauseRideButton);
			mDistanceTextView = (TextView) findViewById(R.id.Distance_Information);
			mMaxSpeedTextView = (TextView) findViewById(R.id.MaxSpeed_Information);
			mAvgSpeedTextView = (TextView) findViewById(R.id.AvgSpeed_Information);
			mDurationTextView = (Chronometer) findViewById(R.id.Duration_Information);
			mElevationTextView = (TextView) findViewById(R.id.Elevation_Information);
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
			if(mPauseRideButton.getVisibility() != View.VISIBLE)
			{
				mStartRideButton.setVisibility(View.VISIBLE);
			}
		}


	public void StartRide( View v )
		{
			mDurationTextView.setBase(SystemClock.elapsedRealtime() + mTimeWhenPaused);
			mDurationTextView.start();
			mPauseRideButton.setVisibility(View.VISIBLE);
			mStartRideButton.setVisibility(View.GONE);
			mStopRideButton.setVisibility(View.GONE);
			if(runtimePermissions())
			{
				startLocationUpdates();
				if(!mBound)
				{
					Intent intent = new Intent(this, RBMService.class);
					bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
				}
			}
		}


	public void PauseRide( View v )
		{
			mTimeWhenPaused = (mDurationTextView.getBase() - SystemClock.elapsedRealtime());
			ride.setDuration(mDurationTextView.getText().toString());
			mStartRideButton.setVisibility(View.VISIBLE);
			mStopRideButton.setVisibility(View.VISIBLE);
			mPauseRideButton.setVisibility(View.GONE);
			mDurationTextView.stop();
			stopLocationUpdates();
			if(mBound)
			{
				unbindService(mConnection);
				mBound = false;
			}
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
				mCurrentLocation = FusedLocationApi.getLastLocation(mGoogleApiClient);
				FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
						mLocationRequest,
						this);
			}
		}


	private void stopLocationUpdates()
		{
			LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
		}


	@Override public void onLocationChanged( Location location )
		{
			if(location.getAccuracy() <= ACCURACY_IN_METERS && location.getAccuracy() > 0.0)
			{
				Location mPreviousLocation = mCurrentLocation;
				mCurrentLocation = location;
				float distanceCovered;
				float currentSpeed;
				float elapsedTime = mCurrentLocation.getElapsedRealtimeNanos() -
						                    mPreviousLocation.getElapsedRealtimeNanos();
				elapsedTime /= 1000000000.0;
				distanceCovered = mCurrentLocation.distanceTo(mPreviousLocation) / 1000;
				/**
				 * check that the gps is still connected --in this if block it is
				 * if distance covered is less than one
				 * has the speed dropped by quite a bit?
				 * is the speed lower than 1 or above 200
				 */
				ride.setDistance(ride.getDistance() + distanceCovered);
				Float previousSpeed = mSpeedTextView.getText().toString().equals("") ?
						                      0 : Float.parseFloat(mSpeedTextView.getText().toString());
				currentSpeed = calculateSpeed(distanceCovered * 1000, elapsedTime, previousSpeed);
				calculateElevationChange(mPreviousLocation);
				updateUI(currentSpeed);
				hasTheRiderStoppedMoving(distanceCovered, previousSpeed, currentSpeed,
						location.getAccuracy());
				hasTheRiderStartedMoving(distanceCovered, previousSpeed, currentSpeed,
						location.getAccuracy());
			}
		}


	public void StartMovingManualLogging( View v )
		{
			String debug = "USER START," + mCurrentLocation.getLatitude() + "," +
					               mCurrentLocation.getLongitude() + "," + mCurrentLocation.getSpeed() +
					               "," + mCurrentLocation.getAccuracy();
			Log.d("BIKEcompanionSTOP", debug);
		}


	public void stoppedMovingManualLogging( View v )
		{
			String debug = "USER STOP," + mCurrentLocation.getLatitude() + "," +
					               mCurrentLocation.getLongitude() + "," + mCurrentLocation.getSpeed() +
					               "," + mCurrentLocation.getAccuracy();
			Log.d("BIKEcompanionSTOP", debug);
		}


	private void hasTheRiderStoppedMoving( float distanceCovered, Float previousSpeed,
			                                     float currentSpeed, float accuracy )
		{
			float speedDelta = previousSpeed * 100 - currentSpeed * 100;
			if(!stopped)
			{
				if((distanceCovered / 1000 < accuracy && currentSpeed < 2) || speedDelta > 5)
				{
					stopped = true;
					String debug = "METHOD STOP," +
							               mCurrentLocation.getLatitude() +
							               "," +
							               mCurrentLocation.getLongitude() +
							               "," +
							               mCurrentLocation.getSpeed() +
							               "," +
							               mCurrentLocation.getAccuracy();
					Log.d("BIKEcompanionSTOP", debug);
					Toast.makeText(this, debug, Toast.LENGTH_SHORT).show();
				}
			}
		}


	private void hasTheRiderStartedMoving( float distanceCovered, Float previousSpeed,
			                                     float currentSpeed, float accuracy )
		{
			//if the user was previously stopped, but now is moving
			if(stopped)
			{
				if(distanceCovered / 1000 >= accuracy && currentSpeed >= 2)
				{
					stopped = false;
					String debug = "METHOD START," +
							               mCurrentLocation.getLatitude() +
							               "," +
							               mCurrentLocation.getLongitude() +
							               "," +
							               mCurrentLocation.getSpeed() +
							               "," +
							               mCurrentLocation.getAccuracy();
					Log.d("BIKEcompanionSTOP", debug);
					Toast.makeText(this, debug, Toast.LENGTH_SHORT).show();
				}
			}
		}


	private float calculateSpeed( float distanceCovered, float elapsedTime, float previousSpeed )
		{
			float currentSpeed = (18.0f / 5.0f);
			currentSpeed *= distanceCovered / elapsedTime;
			//max acceceleration?
			//max speed is 150kmh
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
