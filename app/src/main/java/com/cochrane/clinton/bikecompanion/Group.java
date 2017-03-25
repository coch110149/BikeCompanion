package com.cochrane.clinton.bikecompanion;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import static java.lang.Math.abs;


/**
 * Created by Clint on 16/03/2017.
 */
public class Group implements Parcelable
{
    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>()
    {
        @Override public Group createFromParcel(final Parcel in)
            {
                return new Group(in);
            }


        @Override public Group[] newArray(final int size)
            {
                return new Group[size];
            }
    };
    private int mId;
    private String mName;
    private int mPeriodicDelay;
    private int mMovementWaitTime;
    private int mStopPeriodicDelay;
    private boolean mPauseButtonStopsService;
    private boolean mIsSelected;


    private Group(final Parcel in)
        {
            mId = in.readInt();
            mName = in.readString();
            mPeriodicDelay = in.readInt();
            mMovementWaitTime = in.readInt();
            mStopPeriodicDelay = in.readInt();
            mPauseButtonStopsService = (Boolean) in.readValue(getClass().getClassLoader());
        }


    public Group()
        {
            this(-1, "", 0, 0, 0, true, false);
        }


    public Group(final int id, final String name, final int periodicDelay,
                 final int movementWaitTime, final int stopPeriodicDelay,
                 final boolean pauseButtonStopsService, final boolean isSelected)
        {
            mId = id;
            mName = name;
            mPeriodicDelay = periodicDelay;
            mMovementWaitTime = movementWaitTime;
            mStopPeriodicDelay = stopPeriodicDelay;
            mPauseButtonStopsService = pauseButtonStopsService;
            mIsSelected = isSelected;
        }


    boolean isSelected()
        {
            return mIsSelected;
        }


    public void setSelected(final boolean selected)
        {
            mIsSelected = selected;
        }


    public String getName()
        {
            return mName;
        }


    public void setName(final String name)
        {
            mName = name;
        }


    int getPeriodicDelay()
        {
            return mPeriodicDelay;
        }


    public void setPeriodicDelay(final int periodicDelay)
        {
            mPeriodicDelay = periodicDelay;
        }


    void setPeriodicDelayPositive(final int periodicDelay, final Boolean activate)
        {
            mPeriodicDelay = activate ? abs(periodicDelay) : (abs(periodicDelay) * -1);
        }


    int getMovementWaitTime()
        {
            return mMovementWaitTime;
        }


    public void setMovementWaitTime(final int movementWaitTime)
        {
            mMovementWaitTime = movementWaitTime;
        }


    int getStopPeriodicDelay()
        {
            return mStopPeriodicDelay;
        }


    public void setStopPeriodicDelay(final int stopPeriodicDelay)
        {
            mStopPeriodicDelay = stopPeriodicDelay;
        }


    boolean isPauseButtonStopsService()
        {
            return mPauseButtonStopsService;
        }


    public void setPauseButtonStopsService(final boolean pauseButtonStopsService)
        {
            mPauseButtonStopsService = pauseButtonStopsService;
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
            dest.writeString(mName);
            dest.writeInt(mPeriodicDelay);
            dest.writeInt(mMovementWaitTime);
            dest.writeInt(mStopPeriodicDelay);
            dest.writeValue(mPauseButtonStopsService);
        }


    @Override
    public boolean equals(final Object obj)
        {
            final Boolean isEqual;
            //noinspection CallToSimpleGetterFromWithinClass
            isEqual = (obj instanceof Group) && (mId == ((Group) obj).getId());
            return isEqual;
        }


    public int getId()
        {
            return mId;
        }


    public void setId(final int id)
        {
            mId = id;
        }


    void swapSelected()
        {
            mIsSelected = !mIsSelected;
        }


    String getHeading()
        {
            return "Group Name: " + mName;
        }


    public void setPeriodicDelay(int _delay, boolean _checked)
        {
            mPeriodicDelay = _checked ? abs(_delay) : (abs(_delay) * -1);
        }


    public void setStopPeriodicDelay(int _stopDelay, boolean _checked)
        {
            mStopPeriodicDelay = _checked ? abs(_stopDelay) : (abs(_stopDelay) * -1);
        }


    public void setMovementWaitTime(int _waitDelay, boolean _checked)
        {
            mMovementWaitTime = _checked ? abs(_waitDelay) : (abs(_waitDelay) * -1);
        }


    public Contact addRemoveContact(String _contactId, Context _context)
        {
            DatabaseHandler db = new DatabaseHandler(_context);
            Contact contact = db.getContact(Integer.parseInt(_contactId));
            if(db.getContactGroupRelation(Integer.parseInt(_contactId), mId))
            {
                db.removeContactFromGroup(Integer.parseInt(_contactId), mId);
            }else
            {
                db.addContactToGroup(Integer.parseInt(_contactId), mId);
            }
            return contact;
        }
}
