package com.cochrane.clinton.bikecompanion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


class SelectionAdapter extends ArrayAdapter
{
    private Context mContext = getContext();
    private Resources mRes = mContext.getResources();
    private TextView heading;
    private TextView additionalInfo1;
    private TextView additionalInfo2;
    private View listItemView;
    private String mExtraId;
    private Button primaryButton;


    SelectionAdapter(final Activity context, final ArrayList<?> objects)
        {
            //noinspection unchecked
            super(context, 0, objects);
            mContext = context;
            mRes = mContext.getResources();
        }


    SelectionAdapter(final Activity context, final ArrayList<?> objects, final String _extraId)
        {
            //noinspection unchecked
            super(context, 0, objects);
            mExtraId = _extraId;
            mContext = context;
            mRes = mContext.getResources();
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
            final Button secondButton = (Button) listItemView.findViewById(R.id.secondary_button);
            additionalInfo1 = (TextView) listItemView.findViewById(R.id.additionalInfo1);
            additionalInfo2 = (TextView) listItemView.findViewById(R.id.additionalInfo2);
            secondButton.setVisibility(View.GONE);
            primaryButton.setVisibility(View.GONE);
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
                additionalInfo1.setText(
                        mRes.getString(R.string.bike_distance, currentBike.getDistance()));
                additionalInfo2.setText(currentBike.getAdditionalInfo2());
            }
        }


    private void GroupAdapter(final Group currentGroup)
        {
            if(currentGroup != null)
            {
                if(currentGroup.isSelected())
                {
                    listItemView.setBackgroundColor(ContextCompat.getColor(
                            mContext, R.color.bright_teal));
                }else
                {
                    listItemView.setBackgroundColor(0);
                }
                heading.setText(currentGroup.getName());
                additionalInfo1.setText(currentGroup.getPeriodicDelay(mRes));
                additionalInfo2.setText(currentGroup.getStopPeriodicDelay(mRes));
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
                        primaryButton.setText(R.string.remove_from_group_exact);
                        primaryButton.setOnClickListener(new View.OnClickListener()
                        {
                            @Override public void onClick(final View v)
                                {
                                    currentContact.removeFromGroup(groupId, getContext());
                                }
                        });
                    }else
                    {
                        primaryButton.setText(R.string.add_to_group_exact);
                        primaryButton.setOnClickListener(new View.OnClickListener()
                        {
                            @Override public void onClick(final View v)
                                {
                                    currentContact.addToGroup(groupId, getContext());
                                }
                        });
                    }
                }else
                {
                    primaryButton.setText(R.string.manage_group_associations_exact);
                    primaryButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override public void onClick(final View v)
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

