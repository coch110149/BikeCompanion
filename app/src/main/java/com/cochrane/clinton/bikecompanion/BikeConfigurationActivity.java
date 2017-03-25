package com.cochrane.clinton.bikecompanion;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.Locale;


public class BikeConfigurationActivity extends AppCompatActivity
{
    private Bike mBike = new Bike();
    private EditText mBikeYearEdit;
    private EditText mBikeMakeEdit;
    private EditText mBikeNameEdit;
    private EditText mBikeModelEdit;
    private EditText mBikeDistanceEdit;
    private EditText mBikeDescriptionEdit;


    @Override
    protected void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_bike_configuration);
            final Bundle bundle = getIntent().getExtras();
            if(bundle != null)
            {
                mBike = bundle.getParcelable("SelectedBikeObject");
            }
            mBikeMakeEdit = (EditText) findViewById(R.id.bike_make_edit);
            mBikeMakeEdit.setText(mBike.getBikeMake());
            mBikeNameEdit = (EditText) findViewById(R.id.bike_name_edit);
            mBikeNameEdit.setText(mBike.getBikeName());
            mBikeYearEdit = (EditText) findViewById(R.id.bike_year_edit);
            mBikeYearEdit.setText(mBike.getBikeYear());
            mBikeModelEdit = (EditText) findViewById(R.id.bike_model_edit);
            mBikeModelEdit.setText(mBike.getBikeModel());
            mBikeDescriptionEdit = (EditText) findViewById(R.id.bike_description_edit);
            mBikeDescriptionEdit.setText(mBike.getBikeDescription());
            mBikeDistanceEdit = (EditText) findViewById(R.id.total_bike_distance_edit);
            if(mBike.getTotalBikeDistance() > 0.0)
            {
                mBikeDistanceEdit
                        .setText(String.format(Locale.UK, "%.1f", mBike.getTotalBikeDistance()));
            }
        }


    public void SaveBike(final View view)
        {
            Boolean shouldSaveBike = true;
            if("".equals(mBikeNameEdit.getText().toString().trim()))
            {
                mBikeNameEdit.setError("Bike Name is required!");
                mBikeNameEdit.setText(R.string.default_bike_name);
            }else
            {
                try
                {
                    mBike.setBikeName(mBikeNameEdit.getText().toString());
                    mBike.setBikeMake(mBikeMakeEdit.getText().toString());
                    mBike.setBikeYear(mBikeYearEdit.getText().toString());
                    mBike.setBikeModel(mBikeModelEdit.getText().toString());
                    mBike.setBikeDescription(mBikeDescriptionEdit.getText().toString());
                    mBike.setTotalBikeDistance(
                            Double.parseDouble(mBikeDistanceEdit.getText().toString()));
                }catch(final NumberFormatException _e)
                {
                    if("".equals(mBikeDistanceEdit.getText().toString()))
                    {
                        mBike.setTotalBikeDistance(0.0);
                    }else
                    {
                        mBikeDistanceEdit.setError("Must be a number");
                        shouldSaveBike = false;
                    }
                }finally
                {
                    if(shouldSaveBike)
                    {
                        final DatabaseHandler db = new DatabaseHandler(this);
                        db.addBike(mBike);
                        final Intent intent =
                                new Intent(BikeConfigurationActivity.this, BikeGarage.class);
                        startActivity(intent);
                    }
                }
            }
        }


    public void DeleteBike(final View view)
        {
            final AlertDialog.Builder deleteBikeDialogBuilder = new AlertDialog.Builder(this);
            deleteBikeDialogBuilder.setMessage(R.string.confirm_delete_bike + mBike.getBikeName());
            deleteBikeDialogBuilder
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which)
                            {
                                final DatabaseHandler db =
                                        new DatabaseHandler(BikeConfigurationActivity.this);
                                db.deleteBike(mBike);
                                final Intent intent = new Intent(BikeConfigurationActivity.this,
                                                                 BikeGarage.class);
                                startActivity(intent);
                            }
                    });
            //noinspection AnonymousInnerClassMayBeStatic
            deleteBikeDialogBuilder
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
                                       {
                                           @Override
                                           public void onClick(final DialogInterface dialog,
                                                               final int which)
                                               {
                                                   dialog.dismiss();
                                               }
                                       }
                                      );
            final AlertDialog deleteBikeDialog = deleteBikeDialogBuilder.create();
            deleteBikeDialog.show();
        }
}
