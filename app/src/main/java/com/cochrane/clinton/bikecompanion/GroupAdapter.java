package com.cochrane.clinton.bikecompanion;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Clint on 16/03/2017.
 */
class GroupAdapter extends ArrayAdapter<Group>
{
    GroupAdapter(final Activity context, final ArrayList<Group> groups)
        {
            super(context, 0, groups);
        }


    @SuppressWarnings ( "IfMayBeConditional" ) @NonNull @Override
    public View getView(final int _i, @Nullable final View _view, @NonNull final ViewGroup parent)
        {
            View listItemView = _view;
            if(listItemView == null)
            {
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.group_management_list_item, parent, false);
            }
            final Group currentGroup = getItem(_i);
            if(currentGroup != null)
            {
                final TextView groupNameView =
                        (TextView) listItemView.findViewById(R.id.group_name);
                groupNameView.setText(currentGroup.getHeading());
                final TextView periodicNotification =
                        (TextView) listItemView.findViewById(R.id.periodic_notification);
                String output;
                if(currentGroup.getPeriodicDelay() > 0)
                {
                    output = getContext().getString(R.string.notify_every) + " "
                             + String.valueOf(currentGroup.getPeriodicDelay()) + " "
                             + getContext().getString(R.string.minutes);
                }else
                {
                    output = "Periodic Alerts Are Turned Off";
                }
                periodicNotification.setText(output);
                final TextView stopSettingsText =
                        (TextView) listItemView.findViewById(R.id.stopped_moving_notification);
                if((currentGroup.getStopPeriodicDelay() > 0) &&
                   (currentGroup.getMovementWaitTime() > 0))
                {
                    output = getContext().getString(R.string.notify_every) + " " +
                             String.valueOf(currentGroup.getStopPeriodicDelay()) + " " +
                             getContext().getString(R.string.minutes) + " " +
                             getContext().getString(R.string.noMovement) + " " +
                             String.valueOf(currentGroup.getMovementWaitTime()) + " " +
                             getContext().getString(R.string.minutes);
                }else
                {
                    output = "Stopped Movement Notification Has Been Turned Off";
                }
                stopSettingsText.setText(output);
                final Button testNotificationButton =
                        (Button) listItemView.findViewById(R.id.test_notifications);
                testNotificationButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(final View v)
                        {
                            ArrayList<Group> groups = new ArrayList<Group>();
                            groups.add(currentGroup);
                            Intent intent = new Intent(getContext(), RBMService.class);
                            intent.putParcelableArrayListExtra("GroupObject", groups);
                            getContext().startService(intent);
                            //bind to service
                            //transfer group
                            //call test notification
                        }
                });
                final Button editGroupButton = (Button) listItemView.findViewById(R.id.edit_group);
                editGroupButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override public void onClick(final View v)
                        {
                            final Intent intent =
                                    new Intent(getContext(), GroupConfigurationActivity.class);
                            intent.putExtra("SelectedGroupObject", currentGroup);
                            getContext().startActivity(intent);
                        }
                });
            }
            return listItemView;
        }
}
