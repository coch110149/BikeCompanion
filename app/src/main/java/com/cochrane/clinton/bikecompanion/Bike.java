package com.cochrane.clinton.bikecompanion;

import android.os.Parcel;
import android.os.Parcelable;


public class Bike implements Parcelable
	{
	public static final Parcelable.Creator<Bike> CREATOR = new Parcelable.Creator<Bike>()
		{
			public Bike createFromParcel( final Parcel in )
				{
					return new Bike(in);
				}


			public Bike[] newArray( final int size )
				{
					return new Bike[size];
				}
		};
	private int _ID;
	private String _BikeName;
	private String _BikeMake;
	private String _BikeYear;
	private String _BikeModel;
	private String _BikeDescription;
	private Double _TotalBikeDistance;
	private String _LastRideDate;


	public Bike()
		{
			this(-1, "", "", "", "", "", 0, "You have not ridden me yet ");
		}


	public Bike( int id, String name, String make, String bikeYear, String model, String description,
			           double distance, String rideDate )
		{
			this._ID = id;
			this._BikeName = name;
			this._BikeMake = make;
			this._BikeYear = bikeYear;
			this._BikeModel = model;
			this._TotalBikeDistance = distance;
			this._BikeDescription = description;
			this._LastRideDate = rideDate;
		}


	/**
	 * Use when reconstruction Bike Object from Parcel. This will be used only by the "creator"
	 *
	 * @param in a parcel used to create the object
	 */
	private Bike( Parcel in )
		{
			this._ID = in.readInt();
			this._BikeYear = in.readString();
			this._BikeName = in.readString();
			this._BikeMake = in.readString();
			this._BikeModel = in.readString();
			this._BikeDescription = in.readString();
			this._TotalBikeDistance = in.readDouble();
		}


	public String getLastRideDate()
		{
			return this._LastRideDate;
		}


	public void setLastRideDate( String _LastRideDate )
		{
			this._LastRideDate = _LastRideDate;
		}


	String getBikeYear()
		{
			return this._BikeYear;
		}


	void setBikeYear( String mBikeYear )
		{
			this._BikeYear = mBikeYear;
		}


	int getID()
		{
			return this._ID;
		}


	void setID( int mID )
		{
			this._ID = mID;
		}


	String getBikeName()
		{
			return this._BikeName;
		}


	void setBikeName( String mBikeName )
		{
			this._BikeName = mBikeName;
		}


	String getBikeMake()
		{
			return this._BikeMake;
		}


	void setBikeMake( String mBikeMake )
		{
			this._BikeMake = mBikeMake;
		}


	String getBikeModel()
		{
			return this._BikeModel;
		}


	void setBikeModel( String mBikeModel )
		{
			this._BikeModel = mBikeModel;
		}


	String getBikeDescription()
		{
			return this._BikeDescription;
		}
	//private Components[] components


	void setBikeDescription( String mBikeDescription )
		{
			this._BikeDescription = mBikeDescription;
		}


	Double getTotalBikeDistance()
		{
			return this._TotalBikeDistance;
		}


	void setTotalBikeDistance( Double mTotalBikeDistance )
		{
			this._TotalBikeDistance = mTotalBikeDistance;
		}
	//Insert Method here


	@Override
	public int describeContents()
		{
			return 0;
		}


	/**
	 * Object Serialization happens here, Write object content to parcel one by one, reading
	 * should be done according to this writing order
	 *
	 * @param dest parcel
	 * @param flags additional flags about how the object should be written
	 */
	@Override
	public void writeToParcel( Parcel dest, int flags )
		{
			dest.writeInt(_ID);
			dest.writeString(_BikeYear);
			dest.writeString(_BikeName);
			dest.writeString(_BikeMake);
			dest.writeString(_BikeModel);
			dest.writeString(_BikeDescription);
			dest.writeDouble(_TotalBikeDistance);
			dest.writeString(_LastRideDate);
		}


	@Override
	public boolean equals( Object obj )
		{
			Boolean isEqual;
			isEqual = obj instanceof Bike && this._ID == ((Bike) obj)._ID;
			return isEqual;
		}


	public String getHeading()
		{
			return this._BikeName + " " + this._BikeYear + " " + this._BikeModel + " " + this._BikeMake;
		}


	public String getAdditionalInfo1()
		{
			return "Total Distance: " + this._TotalBikeDistance;
		}


	public String getAdditionalInfo2()
		{
			return "Last Ride Date: " + this._LastRideDate;
		}
	}


