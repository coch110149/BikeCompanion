package com.cochrane.clinton.bikecompanion;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by Clint on 16/03/2017.
 */
class GroupAdapter extends ArrayAdapter<Group>
	{
	GroupAdapter( Activity context, ArrayList<Group> groups )
		{
			super(context, 0, groups);
		}


	@NonNull @Override
	public View getView( int position, @Nullable View convertView, @NonNull ViewGroup parent )
		{
			View listItemView = convertView;
			if(listItemView == null)
			{
				listItemView = LayoutInflater.from(getContext()).inflate(
						R.layout.group_management_list_item, parent, false);
			}
			final Group currentGroup = getItem(position);
			if(currentGroup != null)
			{
				String output;
				TextView groupNameView = (TextView) listItemView.findViewById(R.id.group_name);
				groupNameView.setText(currentGroup.getName());
				TextView periodicNotification = (TextView) listItemView.findViewById(
						R.id.periodic_notification);
				output = R.string.notify_every + Integer.toString(currentGroup.getPeriodicDelay())
						         + R.string.minutes;
				periodicNotification.setText(output);
				TextView stopSettingsText = (TextView) listItemView.findViewById(
						R.id.stopped_moving_notification);
				output = R.string.notify_every + Integer.toString(currentGroup.getStopPeriodicDelay())
						         + R.string.minutes + R.string.noMovement +
						         Integer.toString(currentGroup.getMovementWaitTime()) + R.string.minutes;
				stopSettingsText.setText(output);
				Button editGroupButton = (Button) listItemView.findViewById(R.id.test_notifications);
				editGroupButton.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick( View v )
							{
								//call to service to send messages
								Toast.makeText(getContext(), "Test Notifications for group" +
										                             currentGroup.getName(),
										Toast.LENGTH_SHORT).show();
							}
					});
			}
			return listItemView;
		}
	}
