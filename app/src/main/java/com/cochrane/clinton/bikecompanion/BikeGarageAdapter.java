package com.cochrane.clinton.bikecompanion;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


class BikeGarageAdapter extends RecyclerView.Adapter<BikeGarageAdapter.BikeHolder>
{
    private ArrayList<Bike> mBikes;


    public BikeGarageAdapter(ArrayList<Bike> _bikes)
        {
            mBikes = _bikes;
        }


    @Override public BikeGarageAdapter.BikeHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View inflatedView = LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.bike_garage_list_item, parent, false);
            return new BikeHolder(inflatedView);
        }


    @Override public void onBindViewHolder(BikeGarageAdapter.BikeHolder holder, int position)
        {
            Bike bike = mBikes.get(position);
            holder.bindBike(bike);
        }


    @Override public int getItemCount()
        {
            return mBikes.size();
        }


    public static class BikeHolder extends RecyclerView.ViewHolder
    {
        private Button mEditBike;
        private TextView mBikeName;
        private TextView mDistance;


        public BikeHolder(final View _view)
            {
                super(_view);
                mEditBike = (Button) _view.findViewById(R.id.edit_bike_button);
                mDistance = (TextView) _view.findViewById(R.id.bike_distance);
                mBikeName = (TextView) _view.findViewById(R.id.bike_name);

            }


        public void bindBike(final Bike _bike)
                {
                    final Context context = itemView.getContext();
                    Resources resources = context.getResources();
                    mBikeName.setText(resources.getString(R.string.bike_name, _bike.getName()));
                    mDistance.setText(resources.getString(R.string.bike_distance,
                                                          _bike.getDistance()));
                    mEditBike.setOnClickListener(new View.OnClickListener()
                    {
                        @Override public void onClick(View v)
                            {
                                context.startActivity(new Intent(context, BikeConfigActivity.class)
                                                              .putExtra("SelectedBike", _bike));
                            }
                    });
                }
    }
}
//class BikeGarageAdapter extends ArrayAdapter<Bike>
//{
//    final private Context mContext = getContext();
//    final private Resources mRes = mContext.getResources();
//
//
//    BikeGarageAdapter(final Activity context, final ArrayList<Bike> bikes)
//        {
//            super(context, 0, bikes);
//        }
//
//
//    @NonNull
//    @Override
//    public View getView(final int _i, @Nullable final View _view, @NonNull final ViewGroup parent)
//        {
//            View listItemView = _view;
//            if(listItemView == null)
//            {
//                listItemView = LayoutInflater.from(mContext).inflate(
//                        R.layout.bike_garage_list_item, parent, false);
//            }
//            final Bike currentBike = getItem(_i);
//            if(currentBike != null)
//            {
//                final TextView bikeNameView = (TextView) listItemView.findViewById(R.id
// .bike_name);
//                final TextView bikeDistanceView = (TextView) listItemView.findViewById(
//                        R.id.bike_distance);
//                final Button editBikeButton =
//                        (Button) listItemView.findViewById(R.id.edit_bike_button);
//                editBikeButton.setOnClickListener(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(final View v)
//                        {
//
//                        }
//                });
//                ///// TODO: 25/03/2017 implement
//                final Button viewComponentsButton =
//                        (Button) listItemView.findViewById(R.id.view_components);
//                viewComponentsButton.setOnClickListener(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(final View v)
//                        {
//                            Toast.makeText(getContext(), "Coming soon", Toast.LENGTH_SHORT)
// .show();
//                        }
//                });
//                final Button viewRidesButton =
//                        (Button) listItemView.findViewById(R.id.view_ride_button);
//                viewRidesButton.setOnClickListener(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(final View v)
//                        {
//                            Toast.makeText(getContext(), "Coming Soon", Toast.LENGTH_SHORT)
// .show();
//                        }
//                });
//                bikeDistanceView.setText(mRes.getString(R.string.bike_distance,
//                                                        currentBike.getDistance()));
//                bikeNameView.setText(mRes.getString(R.string.bike_name, currentBike.getName()));
//            }
//            return listItemView;
//        }
//}
