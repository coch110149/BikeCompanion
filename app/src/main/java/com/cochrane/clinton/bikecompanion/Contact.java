package com.cochrane.clinton.bikecompanion;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Clint on 16/03/2017.
 */
class Contact implements Parcelable
{
    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>()
    {
        @Override public Contact createFromParcel(final Parcel in)
            {
                return new Contact(in);
            }


        @Override public Contact[] newArray(final int size)
            {
                return new Contact[size];
            }
    };
    private int mId;
    private String mName;
    private String mPhoneNumber;


    private Contact(final Parcel in)
        {
            mId = in.readInt();
            mName = in.readString();
            mPhoneNumber = in.readString();
        }


    Contact()
        {
            this(-1, "", "");
        }


    Contact(final int id, final String name, final String phoneNumber)
        {
            this.mId = id;
            this.mName = name;
            this.mPhoneNumber = phoneNumber;
        }


    public void setID(final int id)
        {
            this.mId = id;
        }


    public String getName()
        {
            return mName;
        }


    public void setName(final String name)
        {
            this.mName = name;
        }


    public String getPhoneNumber()
        {
            return mPhoneNumber;
        }


    public void setPhoneNumber(final String phoneNumber)
        {
            this.mPhoneNumber = phoneNumber;
        }


    boolean in(int _groupId, Context _context)
        {
            DatabaseHandler db = new DatabaseHandler(_context);
            return db.getContactGroupRelation(mId, _groupId);
        }


    void removeFromGroup(int _groupId, Context _context)
        {
            DatabaseHandler db = new DatabaseHandler(_context);
            db.removeContactFromGroup(mId, _groupId);
        }


    void addToGroup(int _groupId, Context _context)
        {
            DatabaseHandler db = new DatabaseHandler(_context);
            db.addContactToGroup(mId, _groupId);
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
            dest.writeString(mPhoneNumber);
        }


    @Override
    public boolean equals(final Object obj)
        {
            final Boolean isEqual;
            //noinspection CallToSimpleGetterFromWithinClass
            isEqual = (obj instanceof Contact) && (mId == ((Contact) obj).getId());
            return isEqual;
        }


    public int getId()
        {
            return mId;
        }
}
