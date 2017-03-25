package com.cochrane.clinton.bikecompanion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class BikeGarage extends AppCompatActivity
{
    @Override
    protected void onCreate(final Bundle savedInstanceState)
        {
            Log.i("BikeGarage", "onCreate");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_bike_garage);
            final DatabaseHandler db = new DatabaseHandler(this);
            final ArrayList<Bike> bikes = (ArrayList<Bike>) db.getAllBikes();
            final BikeGarageAdapter bikeGarageAdapter = new BikeGarageAdapter(this, bikes);
            final ListView listView = (ListView) findViewById(R.id.list_view_bike);
            listView.setAdapter(bikeGarageAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(final AdapterView<?> parent, final View view,
                                        final int position, final long id)
                    {
                        final Bike bike = bikes.get(position);
                        final Intent intent =
                                new Intent(BikeGarage.this, BikeConfigurationActivity.class);
                        intent.putExtra("SelectedBikeObject", bike);
                        startActivity(intent);
                    }
            });
        }


    public void AddNewBike(final View view)
        {
            final Intent intent = new Intent(BikeGarage.this, BikeConfigurationActivity.class);
            startActivity(intent);
        }
}
