package com.cochrane.clinton.bikecompanion;

import android.app.Activity;
import android.content.Context;
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


class SelectionAdapter extends ArrayAdapter
{
    private static final int SELECTED_RIDING_GROUPS = 2;
    private TextView heading;
    private TextView additionalInfo1;
    private TextView additionalInfo2;
    private View listItemView;
    private String mExtraId;
    private Button primaryButton;
    private Button secondaryButton;
    private Context mContext;


    SelectionAdapter(final Activity context, final ArrayList<?> objects)
        {
            //noinspection unchecked
            super(context, 0, objects);
            mContext = context;
        }


    SelectionAdapter(final Activity context, final ArrayList<?> objects, String _extraId)
        {
            //noinspection unchecked
            super(context, 0, objects);
            mExtraId = _extraId;
            mContext = context;
        }


    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView,
                        @NonNull final ViewGroup parent)
        {
            listItemView = convertView;
            if(listItemView == null)
            {
                final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                listItemView =
                        layoutInflater.inflate(R.layout.selection_chooser_list_item, parent, false);
            }
            heading = (TextView) listItemView.findViewById(R.id.heading);
            primaryButton = (Button) listItemView.findViewById(R.id.primary_button);
            secondaryButton = (Button) listItemView.findViewById(R.id.secondary_button);
            additionalInfo1 = (TextView) listItemView.findViewById(R.id.additionalInfo1);
            additionalInfo2 = (TextView) listItemView.findViewById(R.id.additionalInfo2);
            primaryButton.setVisibility(View.GONE);
            secondaryButton.setVisibility(View.GONE);
            try
            {
                final Bike currentBike = (Bike) getItem(position);
                BikeAdapter(currentBike);
            }catch(final ClassCastException e)
            {
                try
                {
                    final Group currentGroup = (Group) getItem(position);
                    GroupAdapter(currentGroup);
                }catch(final ClassCastException _e)
                {
                    final Contact currentContact = (Contact) getItem(position);
                    contactAdapter(currentContact);
                }
            }
            return listItemView;
        }


    private void BikeAdapter(final Bike currentBike)
        {
            if(currentBike != null)
            {
                heading.setText(currentBike.getHeading());
                additionalInfo1.setText(currentBike.getAdditionalInfo1());
                additionalInfo2.setText(currentBike.getAdditionalInfo2());
            }
        }


    private void GroupAdapter(final Group currentGroup)
        {
            if(currentGroup != null)
            {
                if(currentGroup.isSelected())
                {
                    listItemView.setBackgroundColor(
                            listItemView.getResources().getColor(R.color.colorPrimary));
                }else
                {
                    listItemView.setBackgroundColor(0);
                }
                heading.setText(currentGroup.getName());
                String output;
                //noinspection IfMayBeConditional
                if(currentGroup.getPeriodicDelay() > 0)
                {
                    output = getContext().getString(R.string.notify_every) + " "
                             + String.valueOf(currentGroup.getPeriodicDelay()) + " "
                             + getContext().getString(R.string.minutes);
                }else
                {
                    output = "Periodic Alerts Are Turned Off";
                }
                additionalInfo1.setText(output);
                //noinspection IfMayBeConditional
                if((currentGroup.getStopPeriodicDelay() > 0) &&
                   (currentGroup.getMovementWaitTime() > 0))
                {
                    output = getContext().getString(R.string.notify_every) + " "
                             + String.valueOf(currentGroup.getStopPeriodicDelay()) + " "
                             + getContext().getString(R.string.noMovement) + " "
                             + String.valueOf(currentGroup.getMovementWaitTime()) + " "
                             + getContext().getString(R.string.minutes);
                }else
                {
                    output = "Stopped Movement Notification Has Been Turned Off";
                }
                additionalInfo2.setText(output);
            }
        }


    private void contactAdapter(final Contact currentContact)
        {
            if(currentContact != null)
            {
                if(mExtraId != null)
                {
                    final int groupId = Integer.parseInt(mExtraId);
                    primaryButton.setVisibility(View.VISIBLE);
                    if(currentContact.in(groupId, getContext()))
                    {
                        primaryButton.setText("Remove From Group");
                        primaryButton.setOnClickListener(new View.OnClickListener()
                        {
                            @Override public void onClick(View v)
                                {
                                    currentContact.removeFromGroup(groupId, getContext());
                                }
                        });
                    }else
                    {
                        primaryButton.setText("Add To Group");
                        primaryButton.setOnClickListener(new View.OnClickListener()
                        {
                            @Override public void onClick(View v)
                                {
                                    currentContact.addToGroup(groupId, getContext());
                                }
                        });
                    }
                }else
                {
                    primaryButton.setText("Manage Group Associations");
                    primaryButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override public void onClick(View v)
                            {
                                final Intent intent =
                                        new Intent(mContext, SelectionActivity.class);
                                intent.putExtra("TypeOfRequest", "Group_Contact");
                                mContext.startActivity(intent);
                            }
                    });
                }
            }
        }
}

