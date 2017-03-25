package com.cochrane.clinton.bikecompanion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
{
    static final int PICK_RIDING_BIKE = 1;
    private static final int SELECTED_RIDING_GROUPS = 2;
    private Bike bike;
    private int bikeId = -1;
    private TextView bikeNameText;
    private ArrayList<Group> groups;
    private Button addNewBikeButton;
    private Button addNewGroupButton;
    private TextView bikeDistanceText;
    private TextView groupsActivatedText;
    private TextView activateGroupsLabelText;


    @Override
    protected void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            bikeNameText = (TextView) findViewById(R.id.bike_name_information);
            addNewBikeButton = (Button) findViewById(R.id.add_new_bike_button);
            final android.widget.Button testNotificationsButton =
                    (Button) findViewById(R.id.Riding_Buddy_Test_Notifications_Button);
            testNotificationsButton.setVisibility(View.GONE);
            activateGroupsLabelText =
                    (TextView) findViewById(R.id.Riding_Buddy_Activated_Groups_Label);
            bikeDistanceText = (TextView) findViewById(R.id.bike_distance);
            groupsActivatedText = (TextView) findViewById(R.id.Riding_Buddy_Activated_Groups_List);
            addNewGroupButton = (Button) findViewById(R.id.add_new_groups);
            final Button visitGarageButton = (Button) findViewById(R.id.Visit_Bike_Garage_Button);
            visitGarageButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                    {
                        final Intent intent = new Intent(MainActivity.this, BikeGarage.class);
                        startActivity(intent);
                    }
            });
            final Button visitRidingBuddyButton =
                    (Button) findViewById(R.id.Visit_Buddy_Management_Button);
            visitRidingBuddyButton.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(final View v)
                    {
                        final Intent intent =
                                new Intent(MainActivity.this, GroupManagementActivity.class);
                        startActivity(intent);
                    }
            });
        }


    public void ShowRideActivity(final View view)
        {
            final Intent intent = new Intent(this, RideActivity.class);
            if(bike != null)
            {
                intent.putExtra("SelectableBikeID", bike.getId());
            }
            if((groups != null) && !groups.isEmpty())
            {
                intent.putExtra("RidingBuddies", groups);
            }
            startActivity(intent);
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
                //Toast.makeText(this, "result not okay", Toast.LENGTH_SHORT).show();
            }
        }


    @Override
    protected void onResume()
        {
            setBikeInformationView();
            setGroupInformationView();
            super.onResume();
        }


    private void setBikeInformationView()
        {
            final DatabaseHandler db = new DatabaseHandler(this);
            bikeNameText
                    .setText(com.cochrane.clinton.bikecompanion.R.string.no_bikes_found_in_garage);
            bikeDistanceText.setVisibility(View.GONE);
            addNewBikeButton.setVisibility(View.VISIBLE);
            addNewBikeButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                    {
                        final Intent intent = new Intent(MainActivity.this,
                                                         BikeConfigurationActivity.class);
                        startActivity(intent);
                    }
            });
            if(db.getBikeCount() > 0)
            {
                bikeNameText.setText("");
                addNewBikeButton.setVisibility(View.GONE);
                bike = (bikeId == -1) ? db.getMostRecentlyRiddenBike() : db.getBike(bikeId);
                if(bike != null)
                {
                    bikeNameText.setText(getString(R.string.Bike_Name) + bike.getBikeName());
                    bikeDistanceText.setText(getString(R.string.Bike_Distance)
                                             + bike.getTotalBikeDistance().toString());
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
                    /**
                     * set alerts
                     */
                }
            }
        }


    private void setGroupInformationView()
        {
            final DatabaseHandler db = new DatabaseHandler(this);
            addNewGroupButton.setVisibility(View.VISIBLE);
            addNewGroupButton.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(final View v)
                    {
                        final Intent intent = new Intent(MainActivity.this,
                                                         GroupConfigurationActivity.class);
                        startActivity(intent);
                    }
            });
            if(db.getGroupCount() > 0)
            {
                addNewGroupButton.setVisibility(View.GONE);
                activateGroupsLabelText.setOnClickListener(new View.OnClickListener()
                {
                    @Override public void onClick(final View v)
                        {
                            final Intent intent =
                                    new Intent(MainActivity.this, SelectionActivity.class);
                            intent.putExtra("TypeOfRequest", "Group");
                            startActivityForResult(intent, SELECTED_RIDING_GROUPS);
                        }
                });
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
                groups = (ArrayList<Group>) db.getAllGroups(true);
                if((groups != null) && !groups.isEmpty())
                {
                    activatedGroupNames = "";
                    for(final Group group : groups)
                    {
                        activatedGroupNames += group.getName() + " ";
                    }
                }
                groupsActivatedText.setText(activatedGroupNames);
            }
        }
}

