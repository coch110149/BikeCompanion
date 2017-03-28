package com.cochrane.clinton.bikecompanion;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.Locale;


@SuppressWarnings ( "UnusedParameters" ) public class BikeConfigActivity extends AppCompatActivity
{
    private EditText mBikeDescriptionEdit;
    private EditText mBikeDistance;
    private Bike mBike = new Bike();
    private EditText mBikeModelEdit;
    private EditText mBikeYearEdit;
    private EditText mBikeMakeEdit;
    private EditText mBikeNameEdit;
    private DatabaseHandler mDb;
    private Resources mRes;


    @Override
    protected void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            final Bundle bundle = getIntent().getExtras();
            mRes = getResources();
            setContentView(R.layout.activity_bike_configuration);
            mBikeMakeEdit = (EditText) findViewById(R.id.bike_make_edit);
            mBikeNameEdit = (EditText) findViewById(R.id.bike_name_edit);
            mBikeYearEdit = (EditText) findViewById(R.id.bike_year_edit);
            mBikeModelEdit = (EditText) findViewById(R.id.bike_model_edit);
            mBikeDescriptionEdit = (EditText) findViewById(R.id.bike_description_edit);
            mBikeDistance = (EditText) findViewById(R.id.total_bike_distance_edit);
            if(bundle != null){mBike = bundle.getParcelable("SelectedBikeObject");}
            Log.d("BC-BikeConfig", "mBikeDescription");
            mBikeDescriptionEdit.setText(mBike.getDescription());
            mBikeModelEdit.setText(mBike.getModel());
            mBikeMakeEdit.setText(mBike.getMake());
            mBikeYearEdit.setText(mBike.getYear());
            mBikeNameEdit.setText(mBike.getName());
            if(mBike.getDistance() > 0.0)
            {
                mBikeDistance.setText(
                        String.format(Locale.getDefault(), "%.1f", mBike.getDistance()));
            }
        }


    @Override protected void onPause()
        {
            mDb.close();
            super.onPause();
        }


    @Override protected void onResume()
        {
            mDb = new DatabaseHandler(this);
            super.onResume();
        }


    public void SaveBike(final View _view)
        {
            Boolean shouldSaveBike = true;
            if("".equals(mBikeNameEdit.getText().toString().trim()))
            {
                mBikeNameEdit.setError("Bike Name is required!");
            }else
            {
                try
                {
                    mBike.setName(mBikeNameEdit.getText().toString());
                    mBike.setMake(mBikeMakeEdit.getText().toString());
                    mBike.setYear(mBikeYearEdit.getText().toString());
                    mBike.setModel(mBikeModelEdit.getText().toString());
                    mBike.setDescription(mBikeDescriptionEdit.getText().toString());
                    mBike.setDistance(Double.parseDouble(mBikeDistance.getText().toString()));
                }catch(final NumberFormatException _e)
                {
                    if("".equals(mBikeDistance.getText().toString()))
                    {
                        mBike.setDistance(0.0);
                    }else
                    {
                        mBikeDistance.setError("Must be a number");
                        shouldSaveBike = false;
                    }
                }finally
                {
                    if(shouldSaveBike)
                    {
                        mDb.addBike(mBike);
                        startActivity(
                                new Intent(BikeConfigActivity.this, BikeGarageActivity.class));
                    }
                }
            }
        }


    public void DeleteBike(final View _view)
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(mRes.getString(R.string.confirm_delete, mBike.getName()));
            builder.setPositiveButton(R.string.yes_exact, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(final DialogInterface dialog, final int which)
                    {
                        mDb.deleteBike(mBike);
                        startActivity(
                                new Intent(BikeConfigActivity.this, BikeGarageActivity.class));
                    }
            });
            //noinspection AnonymousInnerClassMayBeStatic
            builder.setNegativeButton(R.string.no_exact, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(final DialogInterface dialog, final int which)
                    {
                        dialog.dismiss();
                    }
            });
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
}
