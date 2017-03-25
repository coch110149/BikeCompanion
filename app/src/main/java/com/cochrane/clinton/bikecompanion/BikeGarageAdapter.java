package com.cochrane.clinton.bikecompanion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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


class BikeGarageAdapter extends ArrayAdapter<Bike>
{
    final private Context mContext = getContext();
    final private Resources mRes = mContext.getResources();


    BikeGarageAdapter(final Activity context, final ArrayList<Bike> bikes)
        {
            super(context, 0, bikes);
        }


    @NonNull
    @Override
    public View getView(final int _i, @Nullable final View _view, @NonNull final ViewGroup parent)
        {
            View listItemView = _view;
            if(listItemView == null)
            {
                listItemView = LayoutInflater.from(mContext).inflate(
                        R.layout.bike_garage_list_item, parent, false);
            }
            final Bike currentBike = getItem(_i);
            if(currentBike != null)
            {
                final TextView bikeNameView = (TextView) listItemView.findViewById(R.id.bike_name);
                final TextView bikeDistanceView = (TextView) listItemView.findViewById(
                        R.id.bike_distance);
                final Button editBikeButton =
                        (Button) listItemView.findViewById(R.id.edit_bike_button);
                editBikeButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(final View v)
                        {
                            final Intent intent = new Intent(mContext, BikeConfigActivity.class);
                            intent.putExtra("SelectedBikeObject", currentBike);
                            mContext.startActivity(intent);
                        }
                });
                ///// TODO: 25/03/2017 implement
                final Button viewComponentsButton =
                        (Button) listItemView.findViewById(R.id.view_components);
                viewComponentsButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(final View v)
                        {
                            Toast.makeText(getContext(), "Coming soon", Toast.LENGTH_SHORT).show();
                        }
                });
                final Button viewRidesButton =
                        (Button) listItemView.findViewById(R.id.view_ride_button);
                viewRidesButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(final View v)
                        {
                            Toast.makeText(getContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
                        }
                });
                bikeDistanceView.setText(mRes.getString(R.string.bike_distance,
                                                        currentBike.getDistance()));
                bikeNameView.setText(mRes.getString(R.string.bike_name, currentBike.getName()));
            }
            return listItemView;
        }
}
