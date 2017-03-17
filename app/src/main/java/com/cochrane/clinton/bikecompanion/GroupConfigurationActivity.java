package com.cochrane.clinton.bikecompanion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;


public class GroupConfigurationActivity extends AppCompatActivity
	{
	Group group = new Group();
	EditText groupNameEdit;
	Switch periodicUpdatesSwitch;
	Switch stoppedMovingPeriodicUpdatesSwitch;
	EditText stoppedPeriodicTimeEdit;
	EditText stoppedWaitTimeEdit;
	EditText periodicTime;
	Button deleteGroupButton;
	Button saveGroupButton;
	Button manageGroupButton;


	@Override
	protected void onCreate( Bundle savedInstanceState )
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_group_configuration);
			groupNameEdit = (EditText) findViewById(R.id.group_name);
			periodicTime = (EditText) findViewById(R.id.periodic_time);
			stoppedWaitTimeEdit = (EditText) findViewById(R.id.stopped_wait_time_edit);
			stoppedPeriodicTimeEdit = (EditText) findViewById(R.id.stopped_periodic_time_edit);
			stoppedMovingPeriodicUpdatesSwitch = (Switch) findViewById(R.id.stopped_updates_switch);
			periodicUpdatesSwitch = (Switch) findViewById(R.id.periodic_notifications_switch);
			manageGroupButton = (Button) findViewById(R.id.manage_group_contacts);
			deleteGroupButton = (Button) findViewById(R.id.delete_group);
			saveGroupButton = (Button) findViewById(R.id.save_group);
			Bundle bundle = getIntent().getExtras();
			if(bundle != null)
			{
				group = bundle.getParcelable("SelectedGroupObject");
			}
			groupNameEdit.setText(group.getName());
			if(group.getPeriodicDelay() <= 0)
			{
				periodicUpdatesSwitch.setChecked(false);
				//periodicUpdatesSwitch.setText("Off");
				periodicTime.setText("");
			} else
			{
				periodicTime.setText(group.getPeriodicDelay());
				periodicUpdatesSwitch.setChecked(true);
				//periodicUpdatesSwitch.setText("On");
			}
			if(group.getMovementWaitTime() <= -1 || group.getStopPeriodicDelay() <= 0)
			{
				stoppedWaitTimeEdit.setText("");
				stoppedPeriodicTimeEdit.setText("");
				stoppedMovingPeriodicUpdatesSwitch.setChecked(true);
				//stoppedMovingPeriodicUpdatesSwitch.setText("Off");
			} else
			{
				stoppedWaitTimeEdit.setText(group.getMovementWaitTime());
				stoppedPeriodicTimeEdit.setText(group.getStopPeriodicDelay());
				stoppedMovingPeriodicUpdatesSwitch.setChecked(true);
				//stoppedMovingPeriodicUpdatesSwitch.setText("On");
			}
			stoppedMovingPeriodicUpdatesSwitch.setOnCheckedChangeListener(
					new CompoundButton.OnCheckedChangeListener()
						{
							@Override
							public void onCheckedChanged( CompoundButton buttonView, boolean isChecked )
								{
									stoppedWaitTimeEdit.setActivated(isChecked);
									stoppedPeriodicTimeEdit.setActivated(isChecked);
								}
						});
			periodicUpdatesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
				{
					@Override public void onCheckedChanged( CompoundButton button, boolean isChecked )
						{
							periodicTime.setActivated(isChecked);
						}
				});
			saveGroupButton.setOnClickListener(new View.OnClickListener()
				{
					@Override public void onClick( View v )
						{
							SaveGroup();
						}
				});
		}


	public void SaveGroup()
		{
			//check if there are contacts in. Contacts are required
			if(groupNameEdit.getText().toString().trim().equals(""))
			{
				groupNameEdit.setError("Group Name Is Required");
				groupNameEdit.setText(group.getID());  //contactCount
			} else
			{
				group.setName(groupNameEdit.getText().toString().trim());
				if(!stoppedPeriodicTimeEdit.isActivated())
				{
					group.setMovementWaitTime(-1);
					group.setStopPeriodicDelay(-1);
				} else
				{
					group.setMovementWaitTime(
							Integer.parseInt(stoppedWaitTimeEdit.getText().toString()));
					group.setStopPeriodicDelay(
							Integer.parseInt(stoppedPeriodicTimeEdit.getText().toString()));
				}
				if(!periodicTime.isActivated())
				{
					group.setPeriodicDelay(-1);
				} else
				{
					group.setPeriodicDelay(Integer.parseInt(periodicTime.getText().toString()));
				}
				DatabaseHandler db = new DatabaseHandler(this);
				db.addGroup(group);
				Intent i = new Intent(GroupConfigurationActivity.this, GroupManagementActivity.class);
				startActivity(i);
			}
		}
	}
