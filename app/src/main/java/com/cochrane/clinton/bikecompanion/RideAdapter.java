package com.cochrane.clinton.bikecompanion;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Locale;


class RideAdapter extends CursorAdapter
{
    RideAdapter(final Context context, final Cursor cursor)
        {
            super(context, cursor, 0);
        }


    @Override public View newView(final Context context, final Cursor cursor,
                                  final ViewGroup parent)
        {
            return LayoutInflater.from(context).inflate(R.layout.ride_history_list_item, parent,
                                                        false);
        }


    @Override public void bindView(final View view, final Context context, final Cursor cursor)
        {
            final TextView dateView = (TextView) view.findViewById(R.id.ride_history_date);
            dateView.setText(cursor.getString(3));
            final TextView bikeName = (TextView) view.findViewById(R.id.bike_name);
            bikeName.setText(cursor.getString(4));
            final TextView distanceView = (TextView) view.findViewById(R.id.ride_history_distance);
            distanceView.setText(String.format(Locale.UK, "%.1f", cursor.getDouble(1)));
            final TextView durationView = (TextView) view.findViewById(R.id.ride_history_duration);
            durationView.setText(cursor.getString(2));
            view.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View v)
                    {
                        final DatabaseHandler db = new DatabaseHandler(context);
                        final Ride ride = db.getRide(cursor.getInt(0));
                        final Intent intent = new Intent(context,
                                                         RideSummaryActivity.class);
                        intent.putExtra("CurrentRideObject", ride);
                        context.startActivity(intent);
                    }
            });
        }
}