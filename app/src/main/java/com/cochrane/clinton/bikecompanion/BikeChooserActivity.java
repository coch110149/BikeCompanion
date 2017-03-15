package com.cochrane.clinton.bikecompanion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class BikeChooserActivity extends AppCompatActivity
	{
	@Override
	protected void onCreate( Bundle savedInstanceState )
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_bike_chooser);
			DatabaseHandler db = new DatabaseHandler(this);
			final ArrayList<Bike> bikes = (ArrayList<Bike>) db.getAllBikes();
			BikeChooserAdapter bikeChooserAdapter = new BikeChooserAdapter(this, bikes);
			ListView listView = (ListView) findViewById(R.id.list_view_bike);
			listView.setAdapter(bikeChooserAdapter);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
				{
					@Override
					public void onItemClick( AdapterView<?> parent, View view, int position, long id )
						{
							Intent intent = new Intent();
							Bike bike = bikes.get(position);
							intent.setData(Uri.parse(Integer.toString(bike.getID())));
							setResult(RESULT_OK, intent);
							finish();
						}
				});
		}
	}
