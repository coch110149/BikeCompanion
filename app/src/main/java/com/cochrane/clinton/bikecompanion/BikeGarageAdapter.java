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
import android.widget.Toast;

import java.util.ArrayList;


class BikeGarageAdapter extends ArrayAdapter<Bike>
{
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
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.bike_garage_list_item, parent, false);
            }
            final Bike currentBike = getItem(_i);
            if(currentBike != null)
            {
                final TextView bikeNameView =
                        (TextView) listItemView.findViewById(R.id.bike_name_information);
                bikeNameView.setText(currentBike.getBikeName());
                final TextView bikeDistanceView =
                        (TextView) listItemView.findViewById(R.id.total_bike_distance);
                bikeDistanceView.setText(currentBike.getTotalBikeDistance().toString());
                final Button editBikeButton =
                        (Button) listItemView.findViewById(R.id.edit_bike_button);
                editBikeButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(final View v)
                        {
                            final Context context = getContext();
                            final Intent intent =
                                    new Intent(context, BikeConfigurationActivity.class);
                            intent.putExtra("SelectedBikeObject", currentBike);
                            context.startActivity(intent);
                        }
                });
                final Button viewComponentsButton =
                        (Button) listItemView.findViewById(R.id.view_components);
                viewComponentsButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(final View v)
                        {
                            Toast.makeText(getContext(),
                                           "View Components",
                                           Toast.LENGTH_SHORT).show();
                        }
                });
                final Button viewRidesButton =
                        (Button) listItemView.findViewById(R.id.view_ride_button);
                viewRidesButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(final View v)
                        {
                            Toast.makeText(getContext(), "View Rides", Toast.LENGTH_SHORT).show();
                        }
                });
            }
            return listItemView;
        }
}
