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
	BikeGarageAdapter( Activity context, ArrayList<Bike> bikes )
		{
			super(context, 0, bikes);
		}


	@NonNull
	@Override
	public View getView( int position, @Nullable View convertView, @NonNull ViewGroup parent )
		{
			View listItemView = convertView;
			if(listItemView == null)
			{
				listItemView = LayoutInflater.from(getContext()).inflate(
						R.layout.bike_garage_list_item, parent, false);
			}
			final Bike currentBike = getItem(position);
			if(currentBike != null)
			{
				TextView bikeNameView = (TextView) listItemView.findViewById(R.id.bike_name_information);
				bikeNameView.setText(currentBike.getBikeName());
				TextView bikeDistanceView = (TextView) listItemView.findViewById(R.id
						                                                                 .total_bike_distance);
				bikeDistanceView.setText(currentBike.getTotalBikeDistance().toString());
				Button editBikeButton = (Button) listItemView.findViewById(R.id.edit_bike_button);
				editBikeButton.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick( View v )
							{
								Context context = getContext();
								Intent intent = new Intent(context, BikeConfigurationActivity.class);
								intent.putExtra("SelectedBikeObject", currentBike);
								context.startActivity(intent);
							}
					});
				Button viewComponentsButton = (Button) listItemView.findViewById(R.id.view_components);
				viewComponentsButton.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick( View v )
							{
								Toast.makeText(getContext(),
										"View Components",
										Toast.LENGTH_SHORT).show();
							}
					});
				Button viewRidesButton = (Button) listItemView.findViewById(R.id.view_ride_button);
				viewRidesButton.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick( View v )
							{
								Toast.makeText(getContext(), "View Rides", Toast.LENGTH_SHORT).show();
							}
					});
			}
			return listItemView;
		}
	}
