package com.cochrane.clinton.bikecompanion;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Clint on 09/03/2017.
 */
public class Component implements Parcelable
	{
	public static final Parcelable.Creator<Component> CREATOR = new Parcelable.Creator<Component>()
		{
			public Component createFromParcel( Parcel in )
				{
					return new Component(in);
				}


			public Component[] newArray( int size )
				{
					return new Component[size];
				}
		};
	private int mID;
	private int mYear;
	private String mType;
	private String mMake;
	private String mModel;
	private String mPartNumber;
	private double mTotalDistance;
	private String mDateInstalled;
	private String mLastDateInspected;


	public Component()
		{
			this(0, 0, "", "", "", "", 0, "", "");
		}


	Component( int mID, int mYear, String mType, String mMake, String mModel, String mPartNumber,
			         double mTotalDistance, String mDateInstalled, String mLastDateInspected )
		{
			this.mID = mID;
			this.mYear = mYear;
			this.mType = mType;
			this.mMake = mMake;
			this.mModel = mModel;
			this.mPartNumber = mPartNumber;
			this.mTotalDistance = mTotalDistance;
			this.mDateInstalled = mDateInstalled;
			this.mLastDateInspected = mLastDateInspected;
		}


	/**
	 * Use when reconstructing Component Object from parcel. This will be used only by the "creator"
	 *
	 * @param incomingParcel a parcel used to read this object
	 */
	private Component( Parcel incomingParcel )
		{
			this.mID = incomingParcel.readInt();
			this.mYear = incomingParcel.readInt();
			this.mType = incomingParcel.readString();
			this.mMake = incomingParcel.readString();
			this.mModel = incomingParcel.readString();
			this.mPartNumber = incomingParcel.readString();
			this.mTotalDistance = incomingParcel.readDouble();
			this.mDateInstalled = incomingParcel.readString();
			this.mLastDateInspected = incomingParcel.readString();
		}


	public int getID()
		{
			return mID;
		}


	public void setID( int mID )
		{
			this.mID = mID;
		}


	public int getYear()
		{
			return mYear;
		}


	public void setYear( int mYear )
		{
			this.mYear = mYear;
		}


	public String getType()
		{
			return mType;
		}


	public void setType( String mType )
		{
			this.mType = mType;
		}


	public String getMake()
		{
			return mMake;
		}


	public void setMake( String mMake )
		{
			this.mMake = mMake;
		}


	public String getModel()
		{
			return mModel;
		}


	public void setModel( String mModel )
		{
			this.mModel = mModel;
		}


	public String getPartNumber()
		{
			return mPartNumber;
		}


	public void setmPartNumber( String mPartNumber )
		{
			this.mPartNumber = mPartNumber;
		}


	public double getTotalDistance()
		{
			return mTotalDistance;
		}


	public void setTotalDistance( double mTotalDistance )
		{
			this.mTotalDistance = mTotalDistance;
		}


	public String getDateInstalled()
		{
			return mDateInstalled;
		}


	public void setDateInstalled( String mDateInstalled )
		{
			this.mDateInstalled = mDateInstalled;
		}


	public String getLastDateInspected()
		{
			return mLastDateInspected;
		}


	public void setLastDateInspected( String mLastDateInspected )
		{
			this.mLastDateInspected = mLastDateInspected;
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
			dest.writeInt(mID);
			dest.writeInt(mYear);
			dest.writeString(mType);
			dest.writeString(mMake);
			dest.writeString(mModel);
			dest.writeString(mPartNumber);
			dest.writeDouble(mTotalDistance);
			dest.writeString(mDateInstalled);
			dest.writeString(mLastDateInspected);
		}


	@Override
	public boolean equals( Object obj )
		{
			Boolean isEqual;
			isEqual = obj instanceof Component && this.mID == ((Component) obj).mID;
			return isEqual;
		}
	}
