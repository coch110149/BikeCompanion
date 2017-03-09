package com.cochrane.clinton.bikecompanion;

import android.os.Parcel;
import android.os.Parcelable;


class Bike implements Parcelable
	{
	public static final Parcelable.Creator<Bike> CREATOR = new Parcelable.Creator<Bike>()
		{
			public Bike createFromParcel(Parcel in)
				{
					return new Bike(in);
				}

			public Bike[] newArray(int size)
				{
					return new Bike[size];
				}
		};

	private int mID;
	private String mBikeName;
	private String mBikeMake;
	private String mBikeModel;
	private String mBikeDescription;
	private Double mTotalBikeDistance;

	//private Components[] components

	Bike()
		{
			this(0, "", "", "", "", 0);
		}

	Bike(int id, String name, String make, String model, String description, double distance)
		{
			mID = id;
			mBikeName = name;
			mBikeMake = make;
			mBikeModel = model;
			mTotalBikeDistance = distance;
			mBikeDescription = description;
		}

	/**
	 * Use when reconstruction Bike Object from Parcel. This will be used only by the "creator"
	 *
	 * @param in a parcel used to create the object
	 */
	private Bike(Parcel in)
		{
			mID = in.readInt();
			mBikeName = in.readString();
			mBikeMake = in.readString();
			mBikeModel = in.readString();
			mTotalBikeDistance = in.readDouble();
			mBikeDescription = in.readString();
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
	 * @param dest  parcel
	 * @param flags additional flags about how the object should be written
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags)
		{
			dest.writeInt(mID);
			dest.writeString(mBikeName);
			dest.writeString(mBikeMake);
			dest.writeString(mBikeModel);
			dest.writeDouble(mTotalBikeDistance);
			dest.writeString(mBikeDescription);
		}

	@Override
	public boolean equals(Object obj)
		{
			Boolean isEqual;
			isEqual = obj instanceof Bike && this.mID == ((Bike) obj).mID;
			return isEqual;
		}
	}


