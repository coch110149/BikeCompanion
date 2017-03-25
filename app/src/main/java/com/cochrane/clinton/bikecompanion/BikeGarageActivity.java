package com.cochrane.clinton.bikecompanion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;


@SuppressWarnings ( "UnusedParameters" ) public class BikeGarageActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_bike_garage);
            final DatabaseHandler db = new DatabaseHandler(this);
            final ArrayList<Bike> bikes = (ArrayList<Bike>) db.getAllBikes();
            final BikeGarageAdapter bikeGarageAdapter = new BikeGarageAdapter(this, bikes);
            final ListView listView = (ListView) findViewById(R.id.list_view_bike);
            listView.setAdapter(bikeGarageAdapter);
        }


    public void AddNewBike(final View _view)
        {
            final Intent intent = new Intent(BikeGarageActivity.this, BikeConfigActivity.class);
            startActivity(intent);
        }
}
