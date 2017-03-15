package com.cochrane.clinton.bikecompanion;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.Locale;


public class BikeConfigurationActivity extends AppCompatActivity
	{
	Bike bike = new Bike();
	EditText bikeYearEdit;
	EditText bikeMakeEdit;
	EditText bikeNameEdit;
	EditText bikeModelEdit;
	EditText bikeDistanceEdit;
	EditText bikeDescriptionEdit;


	@Override
	protected void onCreate( Bundle savedInstanceState )
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_bike_configuration);
			Bundle bundle = getIntent().getExtras();
			if(bundle != null)
			{
				bike = bundle.getParcelable("SelectedBikeObject");
			}
			bikeMakeEdit = (EditText) findViewById(R.id.bike_make_edit);
			bikeMakeEdit.setText(bike.getBikeMake());
			bikeNameEdit = (EditText) findViewById(R.id.bike_name_edit);
			bikeNameEdit.setText(bike.getBikeName());
			bikeYearEdit = (EditText) findViewById(R.id.bike_year_edit);
			bikeYearEdit.setText(bike.getBikeYear());
			bikeModelEdit = (EditText) findViewById(R.id.bike_model_edit);
			bikeModelEdit.setText(bike.getBikeModel());
			bikeDescriptionEdit = (EditText) findViewById(R.id.bike_description_edit);
			bikeDescriptionEdit.setText(bike.getBikeDescription());
			bikeDistanceEdit = (EditText) findViewById(R.id.total_bike_distance_edit);
			bikeDistanceEdit.setText(String.format(Locale.UK, "%.1f", bike.getTotalBikeDistance()));
		}


	public void SaveBike( View view )
		{
			if(bikeNameEdit.getText().toString().trim().equals(""))
			{
				bikeNameEdit.setError("Bike Name is required!");
				bikeNameEdit.setText("Sparkling Unicorn");
			} else
			{
				bike.setBikeName(bikeNameEdit.getText().toString());
				bike.setBikeMake(bikeMakeEdit.getText().toString());
				bike.setBikeYear(bikeYearEdit.getText().toString());
				bike.setBikeModel(bikeModelEdit.getText().toString());
				bike.setBikeDescription(bikeDescriptionEdit.getText().toString());
				bike.setTotalBikeDistance(Double.parseDouble(bikeDistanceEdit.getText().toString()));
				DatabaseHandler db = new DatabaseHandler(this);
				db.addBike(bike);
				db.close();
				Intent intent = new Intent(BikeConfigurationActivity.this, BikeGarage.class);
				startActivity(intent);
			}
		}


	public void DeleteBike( View view )
		{
			AlertDialog.Builder deleteBikeDialogBuilder = new AlertDialog.Builder(this);
			deleteBikeDialogBuilder.setMessage(R.string.confirm_delete_bike + bike.getBikeName());
			deleteBikeDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick( DialogInterface dialog, int which )
						{
							DatabaseHandler db = new DatabaseHandler(BikeConfigurationActivity.this);
							db.deleteBike(bike);
							Intent intent = new Intent(BikeConfigurationActivity.this, BikeGarage.class);
							db.close();
							startActivity(intent);
						}
				});
			deleteBikeDialogBuilder.setNegativeButton(R.string.no, new MyOnClickListener());
			AlertDialog deleteBikeDialog = deleteBikeDialogBuilder.create();
			deleteBikeDialog.show();
		}


	private static class MyOnClickListener implements DialogInterface.OnClickListener
		{
			@Override
			public void onClick( DialogInterface dialog, int which )
				{
					dialog.dismiss();
				}
		}
	}
