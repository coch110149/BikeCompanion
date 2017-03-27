package com.cochrane.clinton.bikecompanion;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;


public class RideHistoryActivity extends AppCompatActivity
{
    //// TODO: 25/03/2017 filtering if a bike Id is passed
    @Override
    protected void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ride_history);
            final Cursor cursor = new DatabaseHandler(this).getBikeName_RideCursor();
            final ListView listView = (ListView) findViewById(R.id.list_view_ride_history);

            if(cursor.moveToFirst())
            {
                final RideAdapter rideAdapter = new RideAdapter(this, cursor);
                listView.setAdapter(rideAdapter);
            }
        }
}
