package com.cochrane.clinton.bikecompanion;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

public class RideActivity extends AppCompatActivity implements
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
				LocationListener

	{

	public static final long UPDATE_INTERVAL_IN_MS = 5000;
	public static final long FASTEST_UPDATE_INTERVAL_IN_MS = UPDATE_INTERVAL_IN_MS / 2;
	protected static final String TAG = "BC-riding-activity";
	//keys for storing activity state in bundle.
	protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
	protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
	protected final static String LOCATION_KEY = "location-key";
	protected final static String STARTING_LOCATION = "starting-location";
	private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
	//represents a geographical location
	protected Location mCurrentLocation;
	//entry point for Google Play Services
	protected GoogleApiClient mGoogleApiClient;
	//time when the location was last updated
	protected String mLastUpdateTime;
	//tracks status of the location updates request
	protected Boolean mRequestingLocationUpdates;
	//Stores parameters for requests to the FusedLocationProviderApi
	protected LocationRequest mLocationRequest;
	//ui widgets
	protected Button mStartRideButton;
	protected TextView mSpeedTextView;
	protected TextView mMaxSpeedTextView;
	protected TextView mAvgSpeedTextView;
	protected TextView mDistanceTextView;
	protected TextView mElevationTextView;
	protected TextView mElevationLossTextView;
	protected TextView mElevationGainTextView;
	protected Chronometer mDurationTextView;
	protected Location mPreviousLocation;
	protected float mTotalDistance;
	protected float mCurrentSpeed;
	protected float mMaxSpeed = 0;
	protected int mNumberOfLocationUpdates = 0;
	protected float mSpeedSum = 0;
	protected float mElevationLoss = 0;
	protected float mElevationGain = 0;
	protected long timeWhenStopped = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
		{
			Log.i(TAG, "On Create");
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_riding);

			//Locate UI widgets
			mStartRideButton = (Button) findViewById(R.id.StartRideButton);
			mSpeedTextView = (TextView) findViewById(R.id.Speed_Information);
			mMaxSpeedTextView = (TextView) findViewById(R.id.MaxSpeed_Information);
			mAvgSpeedTextView = (TextView) findViewById(R.id.AvgSpeed_Information);
			mDurationTextView = (Chronometer) findViewById(R.id.Duration_Information);
			mDistanceTextView = (TextView) findViewById(R.id.Distance_Information);
			mElevationTextView = (TextView) findViewById(R.id.Elevation_Information);
			mElevationLossTextView = (TextView) findViewById(R.id.ElevationLoss_Information);
			mElevationGainTextView = (TextView) findViewById(R.id.ElevationGain_Information);

			mRequestingLocationUpdates = false;
			mLastUpdateTime = "";
			updateValuesFromBundle(savedInstanceState);
			//start process of building a GoogleApiClient and requesting LocationServices
			buildGoogleApiClient();
		}

	/**
	 * updates field based on data stored in the bundle
	 *
	 * @param savedInstanceState The activity state saved in the Bundle
	 */
	private void updateValuesFromBundle(Bundle savedInstanceState)
		{
			Log.i(TAG, "UpdatingValuesFromBundle");
			if (savedInstanceState != null)
			{
				if (savedInstanceState.keySet().contains(LOCATION_KEY))
				{
					//Since LocationKey was found, we know that mCurrentLocation is not null
					mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
				}
				if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY))
				{
					mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
				}
				if (savedInstanceState.keySet().contains(STARTING_LOCATION))
				{
					mPreviousLocation = savedInstanceState.getParcelable(STARTING_LOCATION);
				}
				updateUI();
			}
		}

	/**
	 * Builds a GoogleApiClient uses {@code #addApi} method to request the LocationServices Api
	 */
	protected synchronized void buildGoogleApiClient()
		{
			Log.i(TAG, "Building API Client");
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					                   .addConnectionCallbacks(this)
					                   .addOnConnectionFailedListener(this)
					                   .addApi(LocationServices.API)
					                   .build();
			createLocationRequest();
		}

	/**
	 * sets up location request. use of {@code ACCESS_FINE_LOCATION} is for more exact location
	 * information. This permission has been defined in the AndroidManifest.XML
	 */
	protected void createLocationRequest()
		{
			mLocationRequest = new LocationRequest();
			mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MS);
			mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MS);
			mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		}

	/**
	 * Handles the "ride" button. This will start the requests of location updates.
	 */
	public void StartRide(View v)
		{

			if (mDurationTextView.isActivated() == false)
			{
				mDurationTextView.setBase((SystemClock.elapsedRealtime() + timeWhenStopped));
				mDurationTextView.start();
			}

			if (!mRequestingLocationUpdates)
			{
				startLocationUpdates();
			}
		}

	public void StopRide(View v)
		{
			timeWhenStopped = 0;
			mDurationTextView.stop();
			
			stopLocationUpdates();
		}

	public void PauseRide(View v)
		{
			if (mGoogleApiClient.isConnected())
			{
				stopLocationUpdates();
			}
			timeWhenStopped = mDurationTextView.getBase() - SystemClock.elapsedRealtime();
			mDurationTextView.stop();

		}


	/**
	 * requests location updates from the FusedLocationAPI. Checks that the application has the
	 * correct permission to ACCESS_FINE_LOCATION. If it does not, it will request it.
	 */
	protected void startLocationUpdates()
		{
			Log.i(TAG, "startLocationUpdates");
			
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
					    != PackageManager.PERMISSION_GRANTED)
			{

				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
						PERMISSION_ACCESS_FINE_LOCATION);

			} else
			{
				mRequestingLocationUpdates = true;
				mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
				mPreviousLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
				LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
						mLocationRequest, this);

			}
		}

	/**
	 * Removes location updates from the FusedLocationAPI for when the activity is in the stopped or
	 * paused state.
	 */
	protected void stopLocationUpdates()
		{
			mRequestingLocationUpdates = false;
			LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
		}


	private void updateUI()
		{
			mElevationTextView.setText(String.format("%f", mCurrentLocation.getAltitude()));
			mDistanceTextView.setText(String.format("%.1f", mTotalDistance / 1000));
			mSpeedTextView.setText(String.format("%.2f", mCurrentSpeed));
			mMaxSpeedTextView.setText((String.format("%.2f%s", mMaxSpeed, "kph")));
			mAvgSpeedTextView.setText(String.format("%.0f", mSpeedSum / mNumberOfLocationUpdates));
			mElevationLossTextView.setText(String.format("%.1f", mElevationLoss));
			mElevationGainTextView.setText(String.format("%.1f", mElevationGain));
		}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
	                                       @NonNull int[] grantResults)
		{
			switch (requestCode)
			{
				case PERMISSION_ACCESS_FINE_LOCATION:
					if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
					{
						startLocationUpdates();
					} else
					{
						Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
						stopLocationUpdates();
					}
					break;
			}
		}

	@Override
	protected void onStart()
		{
			Log.i(TAG, "On Start");
			super.onStart();
			if (mGoogleApiClient != null)
			{
				mGoogleApiClient.connect();
			}
		}

	@Override
	protected void onResume()
		{
			Log.i(TAG, "On Resume");
			super.onResume();
			//when activity is paused, location updates are paused as well. We resume location
			//updates here
			if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates)
			{
				startLocationUpdates();
			}
		}

//	@Override
//	protected void onPause()
//		{
//			Log.i(TAG, "On Pause");
//			super.onPause();
//			//Stop Location updates to save battery, but do not disconnect GoogleAPIClient Object
//			if (mGoogleApiClient.isConnected())
//			{
//				stopLocationUpdates();
//			}
//		}

	@Override
	protected void onStop()
		{
			Log.i(TAG, "On Stop");
			mGoogleApiClient.disconnect();
			super.onStop();
		}

	/**
	 * runs when the GoogleAPIClient object successfully connects
	 */
	@Override
	public void onConnected(@Nullable Bundle bundle)
		{
			Log.i(TAG, "Connected to Google Play Services!");
//			if (mRequestingLocationUpdates)
//			{
//				startLocationUpdates();
//			}

		}

	/**
	 * Used to calculate currentSpeed, MaxSpeed, Avg. Speed. Called each time
	 * onLocationChanged is called. Tests if location api is able to return the speed, if not
	 * divides distance covered by elapsedTime.
	 *
	 * @param location        new location detected by {@code onLocationChanged}
	 * @param distanceCovered new location - old location uses {@code location.distanceTo()}
	 * @param elapsedTime     time new location was retrieved - time old location was retrieved. uses
	 *                        {@code getElapsedTimeNanos()}
	 */
	private void calculateSpeed(Location location, float distanceCovered, float elapsedTime)
		{
			mCurrentSpeed = location.getSpeed();
			if (location.hasSpeed() == false)
			{
				mCurrentSpeed = (distanceCovered) / elapsedTime;
			}
			mCurrentSpeed = (mCurrentSpeed * 18) / 5;
			mNumberOfLocationUpdates += 1;
			mSpeedSum += mCurrentSpeed;
			if (mMaxSpeed < mCurrentSpeed)
			{
				mMaxSpeed = mCurrentSpeed;
			}
		}

	/**
	 * used to calculate Elevation Events. Elevation will only report if GPS connection is good
	 * if GPS is not good, {@code getAltitude will return 0}. Want to only update the loss and
	 * gain if GPS is good.
	 * <p>
	 * if currentLocation is higher than the previousLocation, the user has gained altitude
	 * if currentLocation is lower than the previousLocation, the user has lost altitude
	 */
	private void calculateElevation()
		{
			if (mCurrentLocation.getAltitude() != 0 && mPreviousLocation.getAltitude() != 0)
			{

				if (mCurrentLocation.getAltitude() > mPreviousLocation.getAltitude())
				{
					mElevationGain += mCurrentLocation.getAltitude() - mPreviousLocation.getAltitude();
				}
				if (mCurrentLocation.getAltitude() < mPreviousLocation.getAltitude())
				{
					mElevationLoss += mPreviousLocation.getAltitude() - mCurrentLocation.getAltitude();
				}
			}
		}

	@Override
	public void onLocationChanged(Location location)
		{
			mPreviousLocation = mCurrentLocation;
			mCurrentLocation = location;

			//Get elapsedTime between the last check and this check.
			//divide by 1000000000.0 to convert from Nano to Seconds
			float elapsedTime = mCurrentLocation.getElapsedRealtimeNanos() -
					                    mPreviousLocation.getElapsedRealtimeNanos();
			elapsedTime /= 1000000000.0;

			float distanceCovered = mCurrentLocation.distanceTo(mPreviousLocation);
			mTotalDistance += distanceCovered;

			calculateSpeed(location, distanceCovered, elapsedTime);
			calculateElevation();
			updateUI();
		}

	@Override
	public void onConnectionSuspended(int i)
		{
			Log.i(TAG, "Connection Suspended " + i);
			mGoogleApiClient.connect();
		}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
		{
			Log.i(TAG, "Connection failed with errorCode: " + connectionResult.getErrorCode());
		}

	/**
	 * Stores activity data in the bundle
	 */
	public void onSaveInstance(Bundle savedInstanceState)
		{
			savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
			savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
			savedInstanceState.putParcelable(STARTING_LOCATION, mPreviousLocation);
			savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
			super.onSaveInstanceState(savedInstanceState);
		}
	}


