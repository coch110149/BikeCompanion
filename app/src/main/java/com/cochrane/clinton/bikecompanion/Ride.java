package com.cochrane.clinton.bikecompanion;

import android.os.Parcel;
import android.os.Parcelable;


class Ride implements Parcelable
{
    /**
     * This field is needed for Android to be able to create new objects, individually or as arrays
     */
    public static final Parcelable.Creator<Ride> CREATOR = new Parcelable.Creator<Ride>()
    {
        @Override public Ride createFromParcel(final Parcel in)
            {
                return new Ride(in);
            }


        @Override public Ride[] newArray(final int size)
            {
                return new Ride[size];
            }
    };
    private final int mId;
    //private variables
    private int mBikeId;
    private double mAvgSpeed;
    private double mMaxSpeed;
    private double mDistance;
    private String mDuration;
    private String mRideDate;
    private double mElevationLoss;
    private double mElevationGain;


    Ride()
        {
            this(-1, -1, 0, 0, 0, 0, 0, "not started", "not started");
        }


    Ride(final int id, final int bikeID, final double avgSpeed, final double maxSpeed,
         final double distance, final double elevationGain, final double elevationLoss,
         final String duration, final String rideDate)
        {
            mId = id;
            mBikeId = bikeID;
            mAvgSpeed = avgSpeed;
            mMaxSpeed = maxSpeed;
            mDistance = distance;
            mElevationGain = elevationGain;
            mElevationLoss = elevationLoss;
            mDuration = duration;
            mRideDate = rideDate;
        }


    /**
     * Use when reconstructing Ride Object from parcel. This will be used only by the "creator"
     *
     * @param incomingParcel a parcel used to read this object
     */
    private Ride(final Parcel incomingParcel)
        {
            mId = incomingParcel.readInt();
            mBikeId = incomingParcel.readInt();
            mAvgSpeed = incomingParcel.readDouble();
            mMaxSpeed = incomingParcel.readDouble();
            mDistance = incomingParcel.readDouble();
            mElevationGain = incomingParcel.readDouble();
            mElevationLoss = incomingParcel.readDouble();
            mDuration = incomingParcel.readString();
            mRideDate = incomingParcel.readString();
        }


    int getBikeId()
        {
            return mBikeId;
        }
//	public void setID(int id)
//		{
//			this.mId = id;
//		}


    void setBikeId(final int bikeId)
        {
            mBikeId = bikeId;
        }


    double getAvgSpeed()
        {
            return mAvgSpeed;
        }


    void setAvgSpeed(final double avgSpeed)
        {
            mAvgSpeed = avgSpeed;
        }


    double getMaxSpeed()
        {
            return mMaxSpeed;
        }


    void setMaxSpeed(final double maxSpeed)
        {
            mMaxSpeed = maxSpeed;
        }


    public double getDistance()
        {
            return mDistance;
        }


    public void setDistance(final double distance)
        {
            mDistance = distance;
        }


    double getElevationLoss()
        {
            return mElevationLoss;
        }


    void setElevationLoss(final double elevationLoss)
        {
            mElevationLoss = elevationLoss;
        }


    double getElevationGain()
        {
            return mElevationGain;
        }


    void setElevationGain(final double elevationGain)
        {
            mElevationGain = elevationGain;
        }


    String getDuration()
        {
            return mDuration;
        }


    void setDuration(final String duration)
        {
            mDuration = duration;
        }


    String getRideDate()
        {
            return mRideDate;
        }


    void setRideDate(final String rideDate)
        {
            mRideDate = rideDate;
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
            dest.writeInt(mBikeId);
            dest.writeDouble(mAvgSpeed);
            dest.writeDouble(mMaxSpeed);
            dest.writeDouble(mDistance);
            dest.writeDouble(mElevationGain);
            dest.writeDouble(mElevationLoss);
            dest.writeString(mDuration);
            dest.writeString(mRideDate);
        }


    @Override
    public boolean equals(final Object obj)
        {
            final Boolean isEqual;
            //noinspection CallToSimpleGetterFromWithinClass
            isEqual = (obj instanceof Ride) && (mId == ((Ride) obj).getId());
            return isEqual;
        }


    int getId()
        {
            return mId;
        }


    @Override public String toString()
        {
            return mId + "," + mBikeId + "," + mAvgSpeed + "," + mMaxSpeed + "," + mDistance +
                   mElevationLoss + "," + mElevationGain + "," + mDuration + "," + mRideDate;
        }
}
