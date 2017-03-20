package com.cochrane.clinton.bikecompanion;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Clint on 16/03/2017.
 */
public class Group implements Parcelable
	{
	public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>()
		{
			public Group createFromParcel( final Parcel in )
				{
					return new Group(in);
				}


			public Group[] newArray( final int size )
				{
					return new Group[size];
				}
		};
	private int id;
	private String name;
	private int periodicDelay;
	private int movementWaitTime;
	private int stopPeriodicDelay;
	private boolean pauseButtonStopsService;
	private boolean isSelected;


	private Group( Parcel in )
		{
			this.id = in.readInt();
			this.name = in.readString();
			this.periodicDelay = in.readInt();
			this.movementWaitTime = in.readInt();
			this.stopPeriodicDelay = in.readInt();
			this.pauseButtonStopsService = (Boolean) in.readValue(getClass().getClassLoader());
		}


	public Group()
		{
			this(-1, "", -1, -1, -1, true, false);
		}


	public Group( int id, String name, int periodicDelay, int movementWaitTime, int stopPeriodicDelay,
			            boolean pauseButtonStopsService, boolean isSelected )
		{
			this.id = id;
			this.name = name;
			this.periodicDelay = periodicDelay;
			this.movementWaitTime = movementWaitTime;
			this.stopPeriodicDelay = stopPeriodicDelay;
			this.pauseButtonStopsService = pauseButtonStopsService;
		}


	public boolean isSelected()
		{
			return isSelected;
		}


	public void setSelected( boolean selected )
		{
			isSelected = selected;
		}


	public int getID()
		{
			return id;
		}


	public void setID( int id )
		{
			this.id = id;
		}


	public String getName()
		{
			return name;
		}


	public void setName( String name )
		{
			this.name = name;
		}


	public int getPeriodicDelay()
		{
			return periodicDelay;
		}


	public void setPeriodicDelay( int periodicDelay )
		{
			this.periodicDelay = periodicDelay;
		}


	public int getMovementWaitTime()
		{
			return movementWaitTime;
		}


	public void setMovementWaitTime( int movementWaitTime )
		{
			this.movementWaitTime = movementWaitTime;
		}


	public int getStopPeriodicDelay()
		{
			return stopPeriodicDelay;
		}


	public void setStopPeriodicDelay( int stopPeriodicDelay )
		{
			this.stopPeriodicDelay = stopPeriodicDelay;
		}


	public boolean isPauseButtonStopsService()
		{
			return pauseButtonStopsService;
		}


	public void setPauseButtonStopsService( boolean pauseButtonStopsService )
		{
			this.pauseButtonStopsService = pauseButtonStopsService;
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
			dest.writeInt(id);
			dest.writeString(name);
			dest.writeInt(periodicDelay);
			dest.writeInt(movementWaitTime);
			dest.writeInt(stopPeriodicDelay);
			dest.writeValue(pauseButtonStopsService);
		}


	@Override
	public boolean equals( Object obj )
		{
			Boolean isEqual;
			isEqual = obj instanceof Group && this.id == ((Group) obj).id;
			return isEqual;
		}


	public void swapSelected()
		{
			this.isSelected = !this.isSelected;
		}
	}
