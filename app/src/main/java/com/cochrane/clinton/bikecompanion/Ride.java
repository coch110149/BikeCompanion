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
			public Ride createFromParcel( Parcel in )
				{
					return new Ride(in);
				}


			public Ride[] newArray( int size )
				{
					return new Ride[size];
				}
		};
	//private variables
	private int _id;
	private int _bike_id;
	private double _avg_speed;
	private double _max_speed;
	private double _distance;
	private double _elevation_loss;
	private double _elevation_gain;
	private String _duration;
	private String _ride_date;


	Ride()
		{
			this(-1, -1, 0, 0, 0, 0, 0, "not started", "not started");
		}


	Ride( int id, int bikeID, double avgSpeed, double maxSpeed, double distance,
			    double elevationGain, double elevationLoss, String duration, String rideDate )
		{
			this._id = id;
			this._bike_id = bikeID;
			this._avg_speed = avgSpeed;
			this._max_speed = maxSpeed;
			this._distance = distance;
			this._elevation_gain = elevationGain;
			this._elevation_loss = elevationLoss;
			this._duration = duration;
			this._ride_date = rideDate;
		}


	/**
	 * Use when reconstructing Ride Object from parcel. This will be used only by the "creator"
	 *
	 * @param incomingParcel a parcel used to read this object
	 */
	private Ride( Parcel incomingParcel )
		{
			this._id = incomingParcel.readInt();
			this._bike_id = incomingParcel.readInt();
			this._avg_speed = incomingParcel.readDouble();
			this._max_speed = incomingParcel.readDouble();
			this._distance = incomingParcel.readDouble();
			this._elevation_gain = incomingParcel.readDouble();
			this._elevation_loss = incomingParcel.readDouble();
			this._duration = incomingParcel.readString();
			this._ride_date = incomingParcel.readString();
		}


	int getID()
		{
			return this._id;
		}
//	public void setID(int id)
//		{
//			this._id = id;
//		}


	int getBikeID()
		{
			return this._bike_id;
		}


	void setBikeID( int bikeID )
		{
			this._bike_id = bikeID;
		}


	double getAvgSpeed()
		{
			return this._avg_speed;
		}


	void setAvgSpeed( double avgSpeed )
		{
			this._avg_speed = avgSpeed;
		}


	double getMaxSpeed()
		{
			return this._max_speed;
		}


	void setMaxSpeed( double maxSpeed )
		{
			this._max_speed = maxSpeed;
		}


	public double getDistance()
		{
			return this._distance;
		}


	public void setDistance( double distance )
		{
			this._distance = distance;
		}


	double getElevationLoss()
		{
			return this._elevation_loss;
		}


	void setElevationLoss( double elevationLoss )
		{
			this._elevation_loss = elevationLoss;
		}


	double getElevationGain()
		{
			return this._elevation_gain;
		}


	void setElevationGain( double elevationGain )
		{
			this._elevation_gain = elevationGain;
		}


	String getDuration()
		{
			return this._duration;
		}


	void setDuration( String duration )
		{
			this._duration = duration;
		}


	String getRideDate()
		{
			return this._ride_date;
		}


	void setRideDate( String rideDate )
		{
			this._ride_date = rideDate;
		}


	@Override public String toString()
		{
			return _id + "," + _bike_id + "," + _avg_speed + "," + _max_speed + "," + _distance +
					       _elevation_loss + "," + _elevation_gain + "," + _duration + "," + _ride_date;
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
	 * @param dest parcel
	 * @param flags additional flags about how the object should be written
	 */
	@Override
	public void writeToParcel( Parcel dest, int flags )
		{
			dest.writeInt(_id);
			dest.writeInt(_bike_id);
			dest.writeDouble(_avg_speed);
			dest.writeDouble(_max_speed);
			dest.writeDouble(_distance);
			dest.writeDouble(_elevation_gain);
			dest.writeDouble(_elevation_loss);
			dest.writeString(_duration);
			dest.writeString(_ride_date);
		}


	@Override
	public boolean equals( Object obj )
		{
			Boolean isEqual;
			isEqual = obj instanceof Ride && this._id == ((Ride) obj)._id;
			return isEqual;
		}
	}
