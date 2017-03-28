package com.cochrane.clinton.bikecompanion;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


@SuppressWarnings ( "UnusedParameters" ) public class MainActivity extends Activity
{
    static final int PICK_RIDING_BIKE = 1;
    private static final int SELECTED_RIDING_GROUPS = 2;
    private TextView groupsActivatedText;
    private TextView bikeDistanceText;
    private Button addNewGroupButton;
    private Button addNewBikeButton;
    private TextView bikeNameText;
    private DatabaseHandler mDb;
    private int bikeId = -1;
    private Resources mRes;
    private Bike bike;


    @Override
    protected void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            mRes = getResources();
            bikeDistanceText = (TextView) findViewById(R.id.bike_distance);
            addNewGroupButton = (Button) findViewById(R.id.add_new_groups);
            bikeNameText = (TextView) findViewById(R.id.bike_name);
            addNewBikeButton = (Button) findViewById(R.id.add_new_bike_button);
            groupsActivatedText = (TextView) findViewById(R.id.Riding_Buddy_Activated_Groups_List);
            //// TODO: 25/03/2017 implement this button
            final android.widget.Button testNotificationsButton =
                    (Button) findViewById(R.id.Riding_Buddy_Test_Notifications_Button);
            testNotificationsButton.setVisibility(View.GONE);
            final Button visitGarageButton = (Button) findViewById(R.id.Visit_Bike_Garage_Button);
            visitGarageButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                    {
                        startActivity(new Intent(MainActivity.this, BikeGarageActivity.class));
                    }
            });
            final Button visitRidingBuddyButton =
                    (Button) findViewById(R.id.Visit_Buddy_Management_Button);
            visitRidingBuddyButton.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(final View v)
                    {
                        startActivity(new Intent(MainActivity.this, GroupManagementActivity.class));
                    }
            });
        }


    @Override
    protected void onResume()
        {
            mDb = new DatabaseHandler(this);
            setBikeInformationView();
            setGroupInformationView();
            super.onResume();
        }


    @Override protected void onPause()
        {
            mDb.close();
            super.onPause();
        }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
        {
            if(requestCode == PICK_RIDING_BIKE)
            {
                if(resultCode == RESULT_OK)
                {
                    bikeId = Integer.parseInt(data.getData().toString());
                }
            }
        }


    private void setBikeInformationView()
        {
            bikeDistanceText.setVisibility(View.GONE);
            addNewBikeButton.setVisibility(View.VISIBLE);
            bikeNameText.setText(mRes.getString(R.string.no_bikes_found));
            addNewBikeButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                    {
                        startActivity(new Intent(MainActivity.this, BikeConfigActivity.class));
                    }
            });
            if(mDb.getBikeCount() > 0)
            {
                bikeNameText.setText("");
                addNewBikeButton.setVisibility(View.GONE);
                bikeDistanceText.setVisibility(View.VISIBLE);
                bike = (bikeId == -1) ? mDb.getMostRecentlyRiddenBike() : mDb.getBike(bikeId);
                if(bike != null)
                {
                    bikeNameText.setText(mRes.getString(R.string.bike_name, bike.getName()));
                    bikeDistanceText.setText(
                            mRes.getString(R.string.bike_distance, bike.getDistance()));
                    bikeNameText.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(final View v)
                            {
                                final Intent intent =
                                        new Intent(MainActivity.this, SelectionActivity.class);
                                intent.putExtra("TypeOfRequest", "Bike");
                                startActivityForResult(intent, PICK_RIDING_BIKE);
                            }
                    });
                    //// TODO: 25/03/2017 set up bike alerts for health of components
                }
            }
        }


    private void setGroupInformationView()
        {
            addNewGroupButton.setVisibility(View.VISIBLE);
            addNewGroupButton.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(final View v)
                    {
                        startActivity(new Intent(MainActivity.this, GroupConfigActivity.class));
                    }
            });
            if(mDb.getGroupCount() > 0)
            {
                addNewGroupButton.setVisibility(View.GONE);
                groupsActivatedText.setOnClickListener(new View.OnClickListener()
                {
                    @Override public void onClick(final View v)
                        {
                            final Intent intent =
                                    new Intent(MainActivity.this, SelectionActivity.class);
                            intent.putExtra("TypeOfRequest", "Group");
                            startActivityForResult(intent, SELECTED_RIDING_GROUPS);
                        }
                });
                String activatedGroupNames = "No Groups Selected";
                final ArrayList<Group> groups = (ArrayList<Group>) mDb.getAllGroups();
                if((groups != null) && !groups.isEmpty())
                {
                    activatedGroupNames = " ";
                    for(final Group group : groups)
                    {
                        activatedGroupNames += group.getName() + " ";
                    }
                }
                groupsActivatedText.setText(
                        mRes.getString(R.string.activated_groups, activatedGroupNames));
            }
        }


    public void beginRideActivity(final View _view)
        {
            final Intent intent = new Intent(this, RideActivity.class);
            if(bike != null)
            {
                intent.putExtra("SelectableBikeID", bike.getId());
            }
            startActivity(intent);
        }
}

