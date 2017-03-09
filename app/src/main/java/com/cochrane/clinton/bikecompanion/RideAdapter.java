package com.cochrane.clinton.bikecompanion;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class RideAdapter extends ArrayAdapter<Ride>
	{
	RideAdapter(Activity context, ArrayList<Ride> rides)
		{
			super(context, 0, rides);
		}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
		{
			View listItemView = convertView;
			if (listItemView == null)
			{
				listItemView = LayoutInflater.from(getContext()).inflate(
						R.layout.ride_history_list_item, parent, false);
			}

			Ride currentRide = getItem(position);
			if (currentRide != null)
			{
				TextView dateView = (TextView) listItemView.findViewById(R.id.ride_history_date);
				dateView.setText(currentRide.getRideDate());

				TextView bikeNameView = (TextView) listItemView.findViewById(R.id.ride_history_bike_name);
				bikeNameView.setText(Integer.toString(currentRide.getID()));

				TextView distanceView = (TextView) listItemView.findViewById(R.id.ride_history_distance);
				distanceView.setText(Double.toString(currentRide.getDistance()));

				TextView durationView = (TextView) listItemView.findViewById(R.id.ride_history_duration);
				durationView.setText(currentRide.getDuration());
			}
			return listItemView;
		}
	}
