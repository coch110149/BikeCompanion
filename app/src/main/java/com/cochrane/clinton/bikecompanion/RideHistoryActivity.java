package com.cochrane.clinton.bikecompanion;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(final AdapterView<?> parent, final View view,
                                        final int position, final long id)
                    {
                        final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        final Ride ride = db.getRide((int) id);
                        final Intent intent = new Intent(RideHistoryActivity.this,
                                                         RideSummaryActivity.class);
                        intent.putExtra("CurrentRideObject", ride);
                        startActivity(intent);
                    }
            });
        }
}
