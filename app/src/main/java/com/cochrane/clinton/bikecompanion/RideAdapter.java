package com.cochrane.clinton.bikecompanion;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Locale;


class RideAdapter extends CursorAdapter
	{
	RideAdapter( Context context, Cursor cursor )
		{
			super(context, cursor, 0);
		}


	@Override public View newView( Context context, Cursor cursor, ViewGroup parent )
		{
			return LayoutInflater.from(context).inflate(R.layout.ride_history_list_item, parent, false);
		}


	@Override public void bindView( View view, Context context, Cursor cursor )
		{
			TextView dateView = (TextView) view.findViewById(R.id.ride_history_date);
			dateView.setText(cursor.getString(3));
			TextView bikeName = (TextView) view.findViewById(R.id.bike_name_information);
			bikeName.setText(cursor.getString(4));
			TextView distanceView = (TextView) view.findViewById(R.id.ride_history_distance);
			distanceView.setText(String.format(Locale.UK, "%.1f", cursor.getDouble(1)));
			TextView durationView = (TextView) view.findViewById(R.id.ride_history_duration);
			durationView.setText(cursor.getString(2));
		}
	}
