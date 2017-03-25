package com.cochrane.clinton.bikecompanion;

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
    private String mBikeName;
    private String mBikeMake;
    private String mBikeYear;
    private String mBikeModel;
    private String mBikeDescription;
    private Double mTotalBikeDistance;
    private String mLastRideDate;


    public Bike()
        {
            this(-1, "", "", "", "", "", 0, "You have not ridden me yet ");
        }


    public Bike(final int id, final String name, final String make, final String bikeYear,
                final String model, final String description, final double distance,
                final String rideDate)
        {
            mId = (id);
            mBikeName = name;
            mBikeMake = make;
            mBikeYear = bikeYear;
            mBikeModel = model;
            mTotalBikeDistance = distance;
            mBikeDescription = description;
            mLastRideDate = rideDate;
        }


    /**
     * Use when reconstruction Bike Object from Parcel. This will be used only by the "creator"
     *
     * @param in a parcel used to create the object
     */
    private Bike(final Parcel in)
        {
            mId = (in.readInt());
            mBikeYear = in.readString();
            mBikeName = in.readString();
            mBikeMake = in.readString();
            mBikeModel = in.readString();
            mBikeDescription = in.readString();
            mTotalBikeDistance = in.readDouble();
        }


    String getLastRideDate()
        {
            return mLastRideDate;
        }


    void setLastRideDate(final String _LastRideDate)
        {
            mLastRideDate = _LastRideDate;
        }


    String getBikeYear()
        {
            return mBikeYear;
        }


    void setBikeYear(final String mBikeYear)
        {
            this.mBikeYear = mBikeYear;
        }


    String getBikeName()
        {
            return mBikeName;
        }


    void setBikeName(final String mBikeName)
        {
            this.mBikeName = mBikeName;
        }


    String getBikeMake()
        {
            return mBikeMake;
        }


    void setBikeMake(final String mBikeMake)
        {
            this.mBikeMake = mBikeMake;
        }


    String getBikeModel()
        {
            return mBikeModel;
        }


    void setBikeModel(final String mBikeModel)
        {
            this.mBikeModel = mBikeModel;
        }


    String getBikeDescription()
        {
            return mBikeDescription;
        }


    void setBikeDescription(final String mBikeDescription)
        {
            this.mBikeDescription = mBikeDescription;
        }


    Double getTotalBikeDistance()
        {
            return mTotalBikeDistance;
        }


    void setTotalBikeDistance(final Double mTotalBikeDistance)
        {
            this.mTotalBikeDistance = mTotalBikeDistance;
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
            dest.writeString(mBikeYear);
            dest.writeString(mBikeName);
            dest.writeString(mBikeMake);
            dest.writeString(mBikeModel);
            dest.writeString(mBikeDescription);
            dest.writeDouble(mTotalBikeDistance);
            dest.writeString(mLastRideDate);
        }


    @Override
    public boolean equals(final Object obj)
        {
            final Boolean isEqual;
            isEqual = (obj instanceof Bike) && (mId == ((Bike) obj).getId());
            return isEqual;
        }


    int getId()
        {
            return mId;
        }


    void setId(final int mID)
        {
            mId = mID;
        }


    String getHeading()
        {
            return mBikeName + " " + mBikeYear + " " + mBikeModel + " " + mBikeMake;
        }


    String getAdditionalInfo1()
        {
            return "Total Distance: " + mTotalBikeDistance;
        }


    String getAdditionalInfo2()
        {
            return "Last Ride Date: " + mLastRideDate;
        }
}


