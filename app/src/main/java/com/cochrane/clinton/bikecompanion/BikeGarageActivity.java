package com.cochrane.clinton.bikecompanion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;


public class BikeGarageActivity extends Activity
{
    private ArrayList<Bike> mBikes;
    private RecyclerView mRecyclerView;
    private BikeGarageAdapter mBikeGarageAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private DatabaseHandler mDb = new DatabaseHandler(this);


    @Override protected void onCreate(@Nullable Bundle savedInstanceState)
        {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_bike_garage);
            mLinearLayoutManager = new LinearLayoutManager(this);
            mBikes = mDb.getAllBikes();
            mBikeGarageAdapter = new BikeGarageAdapter(mBikes);
            mRecyclerView = (RecyclerView) findViewById(R.id.garage_recycle_view);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mRecyclerView.setAdapter(mBikeGarageAdapter);
        }


    public void AddNewBike(final View _view)
        {
            final Intent intent = new Intent(BikeGarageActivity.this, BikeConfigActivity.class);
            startActivity(intent);
        }
}
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ListView;
//
//import java.util.ArrayList;
//
//
//@SuppressWarnings ( "UnusedParameters" ) public class BikeGarageActivity extends Activity
//{
//    @Override
//    protected void onCreate(final Bundle savedInstanceState)
//        {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_bike_garage);
//            final DatabaseHandler db = new DatabaseHandler(this);
//            final ArrayList<Bike> bikes = (ArrayList<Bike>) db.getAllBikes();
//            final BikeGarageAdapter bikeGarageAdapter = new BikeGarageAdapter(this, bikes);
//            final ListView listView = (ListView) findViewById(R.id.list_view_bike);
//            listView.setAdapter(bikeGarageAdapter);
//        }
//
//
//    public void AddNewBike(final View _view)
//        {
//            final Intent intent = new Intent(BikeGarageActivity.this, BikeConfigActivity.class);
//            startActivity(intent);
//        }
//}
