package com.cochrane.clinton.bikecompanion;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;


public class Bike implements Parcelable
{
    public static final Parcelable.Creator<Bike> CREATOR = new Parcelable.Creator<Bike>()
    {
        @Override public Bike createFromParcel(final Parcel in)
            {
                return new Bike(in);
            }


        @Override public Bike[] newArray(final int size)
            {
                return new Bike[size];
            }
    };
    private int mId;
    private String mName;
    private String mMake;
    private String mYear;
    private String mModel;
    private String mDescription;
    private Double mDistance;
    private String mLastRideDate;


    public Bike()
        {
            this(-1, "", "", "", "", "", 0, "You have not ridden me yet ");
        }


    public Bike(final int _id, final String _name, final String _make, final String _year,
                final String _model, final String _description, final double _distance,
                final String _rideDate)
        {
            mId = _id;
            mName = _name;
            mMake = _make;
            mYear = _year;
            mModel = _model;
            mDistance = _distance;
            mLastRideDate = _rideDate;
            mDescription = _description;
        }


    /**
     * Use when reconstruction Bike Object from Parcel. This will be used only by the "creator"
     *
     * @param in a parcel used to create the object
     */
    private Bike(final Parcel in)
        {
            mId = (in.readInt());
            mYear = in.readString();
            mName = in.readString();
            mMake = in.readString();
            mModel = in.readString();
            mDescription = in.readString();
            mDistance = in.readDouble();
        }


    String getLastRideDate()
        {
            return mLastRideDate;
        }


    void setLastRideDate(final String _LastRideDate)
        {
            mLastRideDate = _LastRideDate;
        }


    String getYear()
        {
            return mYear;
        }


    void setYear(final String _BikeYear)
        {
            mYear = _BikeYear;
        }


    String getName()
        {
            return mName;
        }


    void setName(final String _BikeName){mName = _BikeName;}


    String getMake()
        {
            return mMake;
        }


    void setMake(final String _BikeMake){mMake = _BikeMake;}


    String getModel()
        {
            return mModel;
        }


    void setModel(final String _BikeModel)
        {
            mModel = _BikeModel;
        }


    String getDescription()
        {
            return mDescription;
        }


    void setDescription(final String _BikeDescription)
        {
            mDescription = _BikeDescription;
        }


    Double getDistance()
        {
            return mDistance;
        }


    void setDistance(final Double _TotalBikeDistance)
        {
            mDistance = _TotalBikeDistance;
        }


    @Override
    public int describeContents()
        {
            return 0;
        }


    /**
     * Object Serialization happens here, Write object content to parcel one by one, reading
     * should be done according to this writing order
     *
     * @param dest  parcel
     * @param flags additional flags about how the object should be written
     */
    @Override
    public void writeToParcel(final Parcel dest, final int flags)
        {
            dest.writeInt(mId);
            dest.writeString(mYear);
            dest.writeString(mName);
            dest.writeString(mMake);
            dest.writeString(mModel);
            dest.writeString(mDescription);
            dest.writeDouble(mDistance);
            dest.writeString(mLastRideDate);
        }


    @Override
    public boolean equals(final Object obj)
        {
            final Boolean isEqual;
            //noinspection CallToSimpleGetterFromWithinClass
            isEqual = (obj instanceof Bike) && (mId == ((Bike) obj).getId());
            return isEqual;
        }


    int getId()
        {
            return mId;
        }


    void setId(final int _Id)
        {
            mId = _Id;
        }


    String getHeading(Resources mRes)
        {
            return mRes.getString(R.string.name, mName + " " + mYear + " " + mModel + " " + mMake);
        }


    String getAdditionalInfo1(Resources mRes)
        {
            return mRes.getString(R.string.bike_distance, mDistance);
        }


    String getAdditionalInfo2(Resources mRes)
        {
            if(mLastRideDate == null)
            {
                mLastRideDate = "";
            }
            return mRes.getString(R.string.ride_date, mLastRideDate);
        }
}


