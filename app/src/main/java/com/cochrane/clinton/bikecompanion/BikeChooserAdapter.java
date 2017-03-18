//package com.cochrane.clinton.bikecompanion;
//
//import android.app.Activity;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//
//class BikeChooserAdapter extends ArrayAdapter<Bike>
//	{
//	BikeChooserAdapter( Activity context, ArrayList<Bike> bikes )
//		{
//			super(context, 0, bikes);
//		}
//
//
//	@NonNull
//	@Override
//	public View getView( int position, @Nullable View convertView, @NonNull ViewGroup parent )
//		{
//			View listItemView = convertView;
//			if(listItemView == null)
//			{
//				final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
//				listItemView = layoutInflater.inflate(R.layout.bike_chooser_list_item, parent, false);
//			}
//			final Bike currentBike = getItem(position);
//			if(currentBike != null)
//			{
//				TextView bikeNameView = (TextView) listItemView.findViewById(R.id.bike_name_information);
//				String bikeName = currentBike.getBikeName();
//				bikeName += "[" + currentBike.getBikeYear() + " "
//						            + currentBike.getBikeMake() + " "
//						            + currentBike.getBikeModel() + "]";
//				if(bikeName.equals(""))
//				{
//					bikeName = "Bike Number: " + currentBike.getID();
//				}
//				bikeNameView.setText(bikeName);
//			}
//			return listItemView;
//		}
//	}
