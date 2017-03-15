package com.cochrane.clinton.bikecompanion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
	{
	static final int PICK_RIDING_BIKE = 1;
	//static final int PICK_RIDING_BUDDIES = 2;
	private Bike bike;
	private int bikeId = -1;
	private Button addNewBikeButton;
	private TextView bikeNameText;
	private TextView bikeDistanceText;


	@Override
	protected void onCreate( Bundle savedInstanceState )
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			bikeNameText = (TextView) findViewById(R.id.bike_name_information);
			addNewBikeButton = (Button) findViewById(R.id.add_new_bike_button);
			bikeDistanceText = (TextView) findViewById(R.id.bike_distance);
			Button visitGarageButton = (Button) findViewById(R.id.Visit_Bike_Garage_Button);
			visitGarageButton.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick( View v )
						{
							Intent intent = new Intent(MainActivity.this, BikeGarage.class);
							startActivity(intent);
						}
				});
		}


	@Override
	protected void onResume()
		{
			setBikeInformationView();
			super.onResume();
		}


	public void ShowRideActivity( View view )
		{
			Intent intent = new Intent(this, RideActivity.class);
			if(bike != null)
			{
				intent.putExtra("SelectableBikeID", bike.getID());
			}
			startActivity(intent);
		}


	private void setBikeInformationView()
		{
			final DatabaseHandler db = new DatabaseHandler(this);
			bikeNameText.setText("I could not find any bikes in your garage, let's add one");
			bikeDistanceText.setVisibility(View.GONE);
			addNewBikeButton.setVisibility(View.VISIBLE);
			addNewBikeButton.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick( View v )
						{
							Intent intent = new Intent(MainActivity.this,
									                          BikeConfigurationActivity.class);
							startActivity(intent);
						}
				});
			if(db.getBikeCount() > 0)
			{
				addNewBikeButton.setVisibility(View.GONE);
				bike = bikeId == -1 ? db.getMostRecentlyRiddenBike() : db.getBike(bikeId);
				if(bike != null)
				{
					bikeNameText.setText("Bike Name: " + bike.getBikeName());
					bikeDistanceText.setText("Bike's Distance: "
							                         + bike.getTotalBikeDistance().toString());
					bikeNameText.setOnClickListener(new View.OnClickListener()
						{
							@Override
							public void onClick( View v )
								{
									Intent intent = new Intent(MainActivity.this,
											                          BikeChooserActivity.class);
									startActivityForResult(intent, PICK_RIDING_BIKE);
								}
						});
					/**
					 * set alerts
					 */
				}
			}
		}


	@Override
	protected void onActivityResult( int requestCode, int resultCode, Intent data )
		{
			if(requestCode == PICK_RIDING_BIKE)
			{
				if(resultCode == RESULT_OK)
				{
					bikeId = Integer.parseInt(data.getData().toString());
				}
				Toast.makeText(this, "result not okay", Toast.LENGTH_SHORT).show();
			}
		}
	}

