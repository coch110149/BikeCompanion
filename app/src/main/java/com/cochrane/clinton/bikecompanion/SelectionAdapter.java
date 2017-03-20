package com.cochrane.clinton.bikecompanion;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class SelectionAdapter extends ArrayAdapter
	{
	protected TextView heading;
	protected TextView additionalInfo1;
	protected TextView additionalInfo2;
	protected View listItemView;
	protected ArrayList<?> objects;


	SelectionAdapter( Activity context, ArrayList<?> objects )
		{
			super(context, 0, objects);
			this.objects = objects;
		}


	@NonNull
	@Override
	public View getView( int position, @Nullable View convertView, @NonNull ViewGroup parent )
		{
			listItemView = convertView;
			if(listItemView == null)
			{
				final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
				listItemView =
						layoutInflater.inflate(R.layout.selection_chooser_list_item, parent, false);
			}
			heading = (TextView) listItemView.findViewById(R.id.heading);
			additionalInfo1 = (TextView) listItemView.findViewById(R.id.additionalInfo1);
			additionalInfo2 = (TextView) listItemView.findViewById(R.id.additionalInfo2);
			try
			{
				final Bike currentBike = (Bike) getItem(position);
				BikeAdapter(currentBike);
			} catch (Exception e)
			{
				Log.d("SelectionAdapter", "Try Catch failed" + e);
			}
			return listItemView;
		}


	private void BikeAdapter( Bike currentBike )
		{
			if(currentBike != null)
			{
				heading.setText(currentBike.getHeading());
				additionalInfo1.setText(currentBike.getAdditionalInfo1());
				additionalInfo2.setText(currentBike.getAdditionalInfo2());
			}
		}
//	private View GroupAdapter()
//		{
//			final Bike currentBike = getItem(position);
//			if(currentBike != null)
//			{
//				heading.setText(currentBike.getHeading());
//				additionalInfo1.setText(currentBike.getAdditionalInfo1());
//				additionalInfo2.setText(currentBike.getAdditionalInfo2());
//			}
//			return listItemView;
//		}
	}
