package com.cochrane.clinton.bikecompanion;

import android.content.Context;
import android.content.res.Resources;
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
    private boolean mIsSelected;
    private int mMovementWaitTime;
    private int mStopPeriodicDelay;
    private boolean mPauseButtonStopsService;


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


    public Group(final int _id, final String _name, final int _periodicDelay,
                 final int _movementWaitTime, final int _stopPeriodicDelay,
                 final boolean _pauseButtonStopsService, final boolean _isSelected)
        {
            mId = _id;
            mName = _name;
            mIsSelected = _isSelected;
            mPeriodicDelay = _periodicDelay;
            mMovementWaitTime = _movementWaitTime;
            mStopPeriodicDelay = _stopPeriodicDelay;
            mPauseButtonStopsService = _pauseButtonStopsService;
        }


    boolean isSelected()
        {
            return mIsSelected;
        }


    public void setSelected(final boolean _selected)
        {
            mIsSelected = _selected;
        }


    public String getName()
        {
            return mName;
        }


    public void setName(final String _name)
        {
            mName = _name;
        }


    int getPeriodicDelay()
        {
            return mPeriodicDelay;
        }


    public void setPeriodicDelay(final int _periodicDelay)
        {
            mPeriodicDelay = _periodicDelay;
        }


    void setPeriodicDelayPositive(final int _periodicDelay, final Boolean _activate)
        {
            mPeriodicDelay = _activate ? abs(_periodicDelay) : (abs(_periodicDelay) * -1);
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


    void setStopPeriodicDelay(final int stopPeriodicDelay)
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


    String getPeriodicDelay(final Resources mRes)
        {
            String output = "Periodic Alerts Are Turned Off";
            if(mPeriodicDelay > 0)
            {
                output = mRes.getQuantityString(R.plurals.notify_every, mPeriodicDelay,
                                                mPeriodicDelay);
            }
            return output;
        }


    String getStopPeriodicDelay(final Resources mRes)
        {
            String output = "Stopped Movement Notification Has Been Turned Off";
            if((mStopPeriodicDelay > 0) && (mMovementWaitTime > 0))
            {
                output = mRes.getQuantityString(R.plurals.notify_every, mStopPeriodicDelay,
                                                mMovementWaitTime);
                output += mRes.getQuantityString(R.plurals.stop, mMovementWaitTime,
                                                 mMovementWaitTime);
            }
            return output;
        }


    public void setPeriodicDelay(final int _delay, final boolean _checked)
        {
            mPeriodicDelay = _checked ? abs(_delay) : (abs(_delay) * -1);
        }


    public void setStopPeriodicDelay(final int _stopDelay, final boolean _checked)
        {
            mStopPeriodicDelay = _checked ? abs(_stopDelay) : (abs(_stopDelay) * -1);
        }


    public void setMovementWaitTime(final int _waitDelay, final boolean _checked)
        {
            mMovementWaitTime = _checked ? abs(_waitDelay) : (abs(_waitDelay) * -1);
        }


    public Contact addRemoveContact(final String _contactId, final Context _context)
        {
            final DatabaseHandler db = new DatabaseHandler(_context);
            final Contact contact = db.getContact(Integer.parseInt(_contactId));
            if(db.getContactGroupRelation(Integer.parseInt(_contactId), mId))
            {
                db.removeContactFromGroup(Integer.parseInt(_contactId), mId);
            }else
            {
                db.addContactToGroup(Integer.parseInt(_contactId), mId);
            }
            db.close();
            return contact;
        }
}
