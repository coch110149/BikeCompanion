package com.cochrane.clinton.bikecompanion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.getBoolean;


@SuppressWarnings ( {"MethodCallInLoopCondition", "ObjectAllocationInLoop"} ) class DatabaseHandler
        extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "rideManager.db";
    //Rides Table
    private static final String TABLE_RIDES = "rides";
    private static final String KEY_RIDE_ID = "_id";
    private static final String KEY_BIKE_USED_ID = "bike_used_id";
    private static final String KEY_AVG_SPEED = "Average_Speed";
    private static final String KEY_MAX_SPEED = "Max_Speed";
    private static final String KEY_DURATION = "Ride_Duration";
    private static final String KEY_DISTANCE = "Ride_Distance";
    private static final String KEY_ELE_LOSS = "Elevation_Loss";
    private static final String KEY_ELE_GAIN = "Elevation_Gain";
    private static final String KEY_RIDE_DATE = "Ride_Date";
    //Bikes Table
    private static final String TABLE_BIKES = "bikes";
    private static final String KEY_BIKE_ID = "_id";
    private static final String KEY_BIKE_NAME = "Name";
    private static final String KEY_BIKE_MAKE = "Make";
    private static final String KEY_BIKE_YEAR = "Year";
    private static final String KEY_BIKE_MODEL = "Model";
    private static final String KEY_BIKE_DESCRIPTION = "Description";
    private static final String KEY_BIKE_TOTAL_DISTANCE = "Total_Distance";
    private static final String KEY_BIKE_LAST_RIDE_DATE = "Last_Ride_Date";
    //Components Table
    private static final String TABLE_COMPONENTS = "components";
    private static final String KEY_COMPONENT_ID = "_id";
    private static final String KEY_COMPONENT_YEAR = "Year";
    private static final String KEY_COMPONENT_TYPE = "Type";
    private static final String KEY_COMPONENT_MAKE = "Make";
    private static final String KEY_COMPONENT_MODEL = "Model";
    private static final String KEY_COMPONENT_PART_NUMBER = "Part_Number";
    private static final String KEY_COMPONENT_TOTAL_DISTANCE = "Total_Distance";
    private static final String KEY_COMPONENT_DATE_INSTALLED = "Date_Installed";
    private static final String KEY_COMPONENT_LAST_DATE_INSPECTED = "Last_Inspected_Date";
    //Group Table
    private static final String TABLE_GROUPS = "groups";
    private static final String KEY_GROUP_ID = "_id";
    private static final String KEY_GROUP_NAME = "Group_Name";
    private static final String KEY_PERIODIC_DELAY = "Periodic_Delay";
    private static final String KEY_MOVEMENT_WAIT_TIME = "Movement_Wait_Time";
    private static final String KEY_STOP_PERIODIC_DELAY = "Stop_Periodic_Delay";
    private static final String KEY_PAUSE_BUTTON_STOPS_SERVICE = "Pause_Button_Stops_Service";
    private static final String KEY_GROUP_IS_ACTIVATED = "Is_Group_Activated";
    //Contact Table
    private static final String TABLE_CONTACT = "contacts";
    private static final String KEY_CONTACT_ID = "_id";
    private static final String KEY_CONTACT_NAME = "Contact_Name";
    private static final String KEY_CONTACT_PHONE = "Phone_Number";
    //Group + Contact Relation Table
    private static final String RELATION_CONTACT_GROUP = "Group_Contact_Table";
    private static final String FOREIGN_KEY_CONTACT_ID = "contact_id";
    private static final String FOREIGN_KEY_GROUP_ID = "group_id";


    DatabaseHandler(final Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


    void addRide(final Ride ride)
        {
            if(getRide(ride.getId()) == null)
            {
                final SQLiteDatabase db = getWritableDatabase();
                final ContentValues values = new ContentValues();
                values.put(KEY_BIKE_USED_ID, ride.getBikeId());
                values.put(KEY_AVG_SPEED, ride.getAvgSpeed());
                values.put(KEY_MAX_SPEED, ride.getMaxSpeed());
                values.put(KEY_DURATION, ride.getDuration());
                values.put(KEY_DISTANCE, ride.getDistance());
                values.put(KEY_ELE_LOSS, ride.getElevationLoss());
                values.put(KEY_ELE_GAIN, ride.getElevationGain());
                values.put(KEY_RIDE_DATE, ride.getRideDate());
                db.insert(TABLE_RIDES, null, values);
            }else
            {
                updateRide(ride);
            }
            //update bike used's distance
            //db.close();
        }


    Ride getRide(final int id)
        {
            final SQLiteDatabase db = getReadableDatabase();
            final String selectQuery =
                    "SELECT * FROM " + TABLE_RIDES + " WHERE " + KEY_RIDE_ID + " = " +
                    String.valueOf(id);
            final Cursor cursor = db.rawQuery(selectQuery, null);
            Ride ride = null;
            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    ride = new Ride(
                                           cursor.getInt(0),    //id
                                           cursor.getInt(1),    //bike id
                                           cursor.getDouble(2), //avgSpeed
                                           cursor.getDouble(3), //MaxSpeed
                                           cursor.getDouble(4), //Distance
                                           cursor.getDouble(5), //EleGain
                                           cursor.getDouble(6), //EleLoss
                                           cursor.getString(7), //Duration
                                           cursor.getString(8));//Date
                }
                cursor.close();
            }
            //db.close();
            return ride;
        }


    void updateRide(final Ride ride)
        {
            final SQLiteDatabase db = getWritableDatabase();
            final ContentValues values = new ContentValues();
            values.put(KEY_DURATION, ride.getDuration());
            values.put(KEY_DISTANCE, ride.getDistance());
            values.put(KEY_AVG_SPEED, ride.getAvgSpeed());
            values.put(KEY_MAX_SPEED, ride.getMaxSpeed());
            values.put(KEY_RIDE_DATE, ride.getRideDate());
            values.put(KEY_BIKE_USED_ID, ride.getBikeId());
            values.put(KEY_ELE_LOSS, ride.getElevationLoss());
            values.put(KEY_ELE_GAIN, ride.getElevationGain());
            db.update(TABLE_RIDES, values, KEY_RIDE_ID + " =?",
                      new String[]{String.valueOf(ride.getId())});
        }


    public List<Ride> getAllRides()
        {
            final List<Ride> rideList = new ArrayList<>();
            final String selectQuery = "SELECT * FROM " + TABLE_RIDES;
            final SQLiteDatabase db = getReadableDatabase();
            final Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.moveToFirst())
            {
                do
                {
                    final Ride ride = new Ride(cursor.getInt(0),       //id
                                               cursor.getInt(1),       //bike id
                                               cursor.getDouble(2),    //avgSpeed
                                               cursor.getDouble(3),    //MaxSpeed
                                               cursor.getDouble(4),    //Distance
                                               cursor.getDouble(5),    //EleGain
                                               cursor.getDouble(6),    //EleLoss
                                               cursor.getString(7),    //Duration
                                               cursor.getString(8));   //Date
                    rideList.add(ride);
                } while(cursor.moveToNext());
            }
            cursor.close();
            //db.close();
            return rideList;
        }


    public int getRideCount()
        {
            final String countQuery = "SELECT * FROM " + TABLE_RIDES;
            final SQLiteDatabase db = getReadableDatabase();
            final Cursor cursor = db.rawQuery(countQuery, null);
            cursor.close();
            //db.close();
            return cursor.getCount();
        }


    void deleteRide(final Ride ride)
        {
            final SQLiteDatabase db = getWritableDatabase();
            db.delete(TABLE_RIDES, KEY_RIDE_ID + "=?", new String[]{String.valueOf(ride.getId())});
            //db.close();
        }


    Cursor getBikeName_RideCursor()
        {
            final String selectQuery = "SELECT " + TABLE_RIDES + "." + KEY_RIDE_ID + " ,"
                                       + KEY_DISTANCE + ", " + KEY_DURATION + ", " + KEY_RIDE_DATE
                                       + ", " + KEY_BIKE_NAME + " FROM " + TABLE_RIDES
                                       + " LEFT JOIN " + TABLE_BIKES + " ON " + TABLE_RIDES
                                       + "." + KEY_BIKE_USED_ID + " = " + TABLE_BIKES + "."
                                       + KEY_BIKE_ID;
            final SQLiteDatabase db = getReadableDatabase();
            return db.rawQuery(selectQuery, null);
        }


    void addBike(final Bike bike)
        {
            if(getBike(bike.getId()) == null)
            {
                final ContentValues values = new ContentValues();
                values.put(KEY_BIKE_NAME, bike.getName());
                values.put(KEY_BIKE_MAKE, bike.getMake());
                values.put(KEY_BIKE_YEAR, bike.getYear());
                values.put(KEY_BIKE_MODEL, bike.getModel());
                values.put(KEY_BIKE_DESCRIPTION, bike.getDescription());
                values.put(KEY_BIKE_LAST_RIDE_DATE, bike.getLastRideDate());
                values.put(KEY_BIKE_TOTAL_DISTANCE, bike.getDistance());
                getWritableDatabase().insert(TABLE_BIKES, null, values);
            }else
            {
                updateBike(bike);
            }
        }


    Bike getBike(final int id)
        {
            final SQLiteDatabase db = getReadableDatabase();
            final String selectQuery =
                    "SELECT * FROM " + TABLE_BIKES + " WHERE " + KEY_BIKE_ID + " = " +
                    String.valueOf(id);
            final Cursor cursor = db.rawQuery(selectQuery, null);
            Bike bike = null;
            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    bike = new Bike(cursor.getInt(0),    //id
                                    cursor.getString(1), //name
                                    cursor.getString(2), //make
                                    cursor.getString(3), //year
                                    cursor.getString(4), //model
                                    cursor.getString(5), //Description
                                    cursor.getDouble(6), //Distance
                                    cursor.getString(7)); //Last Ride
                }
                cursor.close();
            }
            //db.close();
            return bike;
        }


    void updateBike(final Bike bike)
        {
            final SQLiteDatabase db = getWritableDatabase();
            final ContentValues values = new ContentValues();
            values.put(KEY_BIKE_ID, bike.getId());
            values.put(KEY_BIKE_NAME, bike.getName());
            values.put(KEY_BIKE_MAKE, bike.getMake());
            values.put(KEY_BIKE_YEAR, bike.getYear());
            values.put(KEY_BIKE_MODEL, bike.getModel());
            values.put(KEY_BIKE_DESCRIPTION, bike.getDescription());
            values.put(KEY_BIKE_LAST_RIDE_DATE, bike.getLastRideDate());
            values.put(KEY_BIKE_TOTAL_DISTANCE, bike.getDistance());
            db.update(TABLE_BIKES, values, KEY_BIKE_ID + " =?",
                      new String[]{String.valueOf(bike.getId())});
        }


    void updateBike(final Ride _ride)
        {
            final SQLiteDatabase db = getWritableDatabase();
            final ContentValues values = new ContentValues();
            values.put(KEY_BIKE_TOTAL_DISTANCE, _ride.getDistance());
            values.put(KEY_BIKE_LAST_RIDE_DATE, _ride.getRideDate());
            db.update(TABLE_BIKES, values, KEY_BIKE_ID + " =?",
                      new String[]{String.valueOf(_ride.getBikeId())});
        }


    int getBikeCount()
        {
            final String countQuery = "SELECT * FROM " + TABLE_BIKES;
            final SQLiteDatabase db = getReadableDatabase();
            return db.rawQuery(countQuery, null).getCount();
        }


    void deleteBike(final Bike bike)
        {
            removeBikeFromRide(bike.getId());
            final SQLiteDatabase db = getWritableDatabase();
            db.delete(TABLE_BIKES, KEY_BIKE_ID + "=?", new String[]{String.valueOf(bike.getId())});
            //db.close();
        }


    private void removeBikeFromRide(final int bikeID)
        {
            final String selectQuery = "SELECT * FROM " + TABLE_RIDES +
                                       " WHERE " + KEY_BIKE_USED_ID + " = " + bikeID;
            final SQLiteDatabase db = getReadableDatabase();
            final Cursor cursor = db.rawQuery(selectQuery, null);
            {
                if(cursor.moveToFirst())
                {
                    do
                    {
                        updateRide(new Ride(cursor.getInt(0),       //id
                                            cursor.getInt(1),       //bike id
                                            cursor.getDouble(2),    //avgSpeed
                                            cursor.getDouble(3),    //MaxSpeed
                                            cursor.getDouble(4),    //Distance
                                            cursor.getDouble(5),    //EleGain
                                            cursor.getDouble(6),    //EleLoss
                                            cursor.getString(7),    //Duration
                                            cursor.getString(8)));   //Date
                    } while(cursor.moveToNext());
                }
                cursor.close();
            }
        }


    Bike getMostRecentlyRiddenBike()
        {
            final SQLiteDatabase db = getReadableDatabase();
            int bikeId = 1;
            final String selectQuery = "SELECT " + KEY_BIKE_USED_ID + " FROM " + TABLE_RIDES +
                                       " ORDER BY " + KEY_RIDE_DATE + " DESC LIMIT 1";
            final Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.moveToFirst())
            {
                bikeId = cursor.getInt(0);
            }
            cursor.close();
            return getBike(bikeId);
        }


    void addGroup(final Group group)
        {
            final SQLiteDatabase db = getWritableDatabase();
            final ContentValues values = new ContentValues();
            if(getGroup(group.getId()) == null)
            {
                values.put(KEY_GROUP_NAME, group.getName());
                values.put(KEY_GROUP_IS_ACTIVATED, group.isSelected());
                values.put(KEY_PERIODIC_DELAY, group.getPeriodicDelay());
                values.put(KEY_MOVEMENT_WAIT_TIME, group.getMovementWaitTime());
                values.put(KEY_STOP_PERIODIC_DELAY, group.getStopPeriodicDelay());
                values.put(KEY_PAUSE_BUTTON_STOPS_SERVICE, group.isPauseButtonStopsService());
                db.insert(TABLE_GROUPS, null, values);
            }else
            {
                updateGroup(group);
            }
        }


    private Group getGroup(final int id)
        {
            final SQLiteDatabase db = getReadableDatabase();
            final String selectQuery = "SELECT * FROM " + TABLE_GROUPS +
                                       " WHERE " + KEY_GROUP_ID + " = " + String.valueOf(id);
            final Cursor cursor = db.rawQuery(selectQuery, null);
            Group group = null;
            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    group = new Group(cursor.getInt(0),        //id
                                      cursor.getString(1),    //name
                                      cursor.getInt(2),      //periodicDelay
                                      cursor.getInt(3),     //movementWaitTime
                                      cursor.getInt(4),    //stopPeriodicDelay
                                      getBoolean(cursor.getString(5)),//pauseButton
                                      getBoolean(cursor.getString(6))); //activated
                }
                cursor.close();
            }
            //db.close();
            return group;
        }


    void updateGroup(final Group group)
        {
            final SQLiteDatabase db = getWritableDatabase();
            final ContentValues values = new ContentValues();
            values.put(KEY_GROUP_ID, group.getId());
            values.put(KEY_GROUP_NAME, group.getName());
            values.put(KEY_GROUP_IS_ACTIVATED, group.isSelected());
            values.put(KEY_PERIODIC_DELAY, group.getPeriodicDelay());
            values.put(KEY_MOVEMENT_WAIT_TIME, group.getMovementWaitTime());
            values.put(KEY_STOP_PERIODIC_DELAY, group.getStopPeriodicDelay());
            values.put(KEY_PAUSE_BUTTON_STOPS_SERVICE, group.isPauseButtonStopsService());
            db.update(TABLE_GROUPS, values, KEY_GROUP_ID + " =?",
                      new String[]{String.valueOf(group.getId())});
        }


    List<Group> getAllGroups(final Boolean _boolean)
        {
            final List<Group> groupList = new ArrayList<>();
            final String selectQuery = "SELECT * FROM " + TABLE_GROUPS +
                                       " WHERE " + KEY_GROUP_IS_ACTIVATED + "=" + "1";
            final SQLiteDatabase db = getReadableDatabase();
            final Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.moveToFirst())
            {
                do
                {
                    final boolean group_is_activated = _boolean;
                    boolean pauseControl = false;
                    if("1".equals(cursor.getString(5)))
                    {
                        pauseControl = true;
                    }
                    final Group group = new Group(cursor.getInt(0),         //id
                                                  cursor.getString(1),     //name
                                                  cursor.getInt(2),       //periodicDelay
                                                  cursor.getInt(3),      //movementWaitTime
                                                  cursor.getInt(4),     //stopPeriodicDelay
                                                  pauseControl,        //pause
                                                  group_is_activated);//activated
                    groupList.add(group);
                } while(cursor.moveToNext());
            }
            cursor.close();
            return groupList;
        }


    int getGroupCount()
        {
            final String countQuery = "SELECT * FROM " + TABLE_GROUPS;
            final SQLiteDatabase db = getReadableDatabase();
            return db.rawQuery(countQuery, null).getCount();
        }


    void deleteGroup(final Group group)
        {
            final SQLiteDatabase db = getWritableDatabase();
            db.delete(TABLE_GROUPS, KEY_GROUP_ID + "=?",
                      new String[]{String.valueOf(group.getId())});
            db.delete(RELATION_CONTACT_GROUP, FOREIGN_KEY_GROUP_ID + "=?",
                      new String[]{String.valueOf(group.getId())});
            //db.close();
        }


    void addContact(final Contact contact)
        {
            if(getContact(contact.getId()) == null)
            {
                final SQLiteDatabase db = getWritableDatabase();
                final ContentValues values = new ContentValues();
                values.put(KEY_CONTACT_NAME, contact.getName());
                values.put(KEY_CONTACT_PHONE, contact.getPhoneNumber());
                db.insert(TABLE_CONTACT, null, values);
                //db.close();
            }else
            {
                updateContact(contact);
            }
        }


    Contact getContact(final int id)
        {
            final SQLiteDatabase db = getReadableDatabase();
            final String selectQuery = "SELECT * FROM " + TABLE_CONTACT +
                                       " WHERE " + KEY_CONTACT_ID + " = " + String.valueOf(id);
            final Cursor cursor = db.rawQuery(selectQuery, null);
            Contact contact = null;
            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    contact = new Contact(cursor.getInt(0),        //id
                                          cursor.getString(1),    //name
                                          cursor.getString(2));  //phoneNumber
                }
                cursor.close();
            }
            //db.close();
            return contact;
        }


    private void updateContact(final Contact contact)
        {
            final SQLiteDatabase db = getWritableDatabase();
            final ContentValues values = new ContentValues();
            values.put(KEY_CONTACT_ID, contact.getId());
            values.put(KEY_CONTACT_NAME, contact.getName());
            values.put(KEY_CONTACT_PHONE, contact.getPhoneNumber());
            db.update(TABLE_CONTACT, values, KEY_CONTACT_ID + " =?",
                      new String[]{String.valueOf(contact.getId())});
        }


    public List<Contact> getAllContacts()
        {
            final List<Contact> contactList = new ArrayList<>();
            final String selectQuery = "SELECT * FROM " + TABLE_CONTACT;
            final SQLiteDatabase db = getReadableDatabase();
            final Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.moveToFirst())
            {
                do
                {
                    final Contact contact = new Contact(cursor.getInt(0),        //id
                                                        cursor.getString(1),    //name
                                                        cursor.getString(2));  //phoneNumber
                    contactList.add(contact);
                } while(cursor.moveToNext());
            }
            cursor.close();
            //db.close();
            return contactList;
        }


    void deleteContact(final Contact contact)
        {
            final SQLiteDatabase db = getWritableDatabase();
            db.delete(TABLE_CONTACT, KEY_CONTACT_ID + "=?",
                      new String[]{String.valueOf(contact.getId())});
            db.delete(RELATION_CONTACT_GROUP, FOREIGN_KEY_CONTACT_ID + "=?",
                      new String[]{String.valueOf(contact.getId())});
            //db.close();
        }


    void insertContactToGroup(final int contactID, final int groupID)
        {
            if(getContactGroupRelation(contactID, groupID))
            {
                final SQLiteDatabase db = getWritableDatabase();
                final ContentValues values = new ContentValues();
                values.put(FOREIGN_KEY_CONTACT_ID, contactID);
                values.put(FOREIGN_KEY_GROUP_ID, groupID);
                db.insert(TABLE_CONTACT, null, values);
                //db.close();
            }
        }


    boolean getContactGroupRelation(final int contactID, final int groupID)
        {
            Boolean returnValue = false;
            final String selectQuery = "SELECT " + "*" + " FROM " + RELATION_CONTACT_GROUP +
                                       " WHERE " + RELATION_CONTACT_GROUP + "." +
                                       FOREIGN_KEY_GROUP_ID + "=" + groupID + " AND " +
                                       RELATION_CONTACT_GROUP + "." + FOREIGN_KEY_CONTACT_ID + "=" +
                                       contactID;
            final SQLiteDatabase db = getReadableDatabase();
            final Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.moveToFirst())
            {
                returnValue = true;
            }
            cursor.close();
            return returnValue;
        }


    void removeContactFromGroup(final int contactId, final int groupId)
        {
//            final String deleteQuery = "DELETE FROM " + RELATION_CONTACT_GROUP +
//                                       " WHERE " + RELATION_CONTACT_GROUP + "." +
//                                       FOREIGN_KEY_GROUP_ID + "=" + groupId + " AND " +
//                                       RELATION_CONTACT_GROUP + "." + FOREIGN_KEY_CONTACT_ID +
// "=" +
//                                       contactId;
            final SQLiteDatabase db = getWritableDatabase();
//            db.rawQuery(deleteQuery, null);
            db.delete(RELATION_CONTACT_GROUP, FOREIGN_KEY_CONTACT_ID + " = " + contactId + " AND " +
                                              FOREIGN_KEY_GROUP_ID + " = " + groupId, null);
        }


    void addContactToGroup(final int contactId, final int groupId)
        {
            if(!getContactGroupRelation(contactId, groupId))
            {
                final SQLiteDatabase db = getWritableDatabase();
                final ContentValues values = new ContentValues();
                values.put(FOREIGN_KEY_CONTACT_ID, contactId);
                values.put(FOREIGN_KEY_GROUP_ID, groupId);
                db.insert(RELATION_CONTACT_GROUP, null, values);
            }
        }


    List<Contact> getAllContactsInGroup(final int groupID)
        {
            final String selectQuery = "SELECT " + TABLE_CONTACT + "." + KEY_CONTACT_ID + ", " +
                                       TABLE_CONTACT + "." + KEY_CONTACT_NAME + ", "
                                       + TABLE_CONTACT + "." + KEY_CONTACT_PHONE + " FROM "
                                       + RELATION_CONTACT_GROUP + ", " + TABLE_CONTACT + " WHERE "
                                       + groupID + "=" + RELATION_CONTACT_GROUP + "."
                                       + FOREIGN_KEY_GROUP_ID + " AND " + RELATION_CONTACT_GROUP
                                       + "." + FOREIGN_KEY_CONTACT_ID + "=" + TABLE_CONTACT + "."
                                       + KEY_CONTACT_ID;
            final List<Contact> contactList = new ArrayList<>();
            final SQLiteDatabase db = getReadableDatabase();
            final Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.moveToFirst())
            {
                do
                {
                    final Contact contact = new Contact(cursor.getInt(0),        //id
                                                        cursor.getString(1),    //name
                                                        cursor.getString(2));  //phoneNumber
                    contactList.add(contact);
                } while(cursor.moveToNext());
            }
            cursor.close();
            //db.close();
            return contactList;
        }


    int getCountOfContactsInGroup(final int groupID)
        {
            final String countQuery =
                    "SELECT * FROM " + RELATION_CONTACT_GROUP + " WHERE " + groupID + "=" +
                    RELATION_CONTACT_GROUP + "." + FOREIGN_KEY_GROUP_ID;
            final SQLiteDatabase db = getReadableDatabase();
            return db.rawQuery(countQuery, null).getCount();
        }


    ArrayList<?> getObjects(final String typeOfRequest)
        {
            return "Bike".equals(typeOfRequest) ? (java.util.ArrayList<?>) getAllBikes() :
                   (java.util.ArrayList<?>) getAllGroups();
        }


    List<Bike> getAllBikes()
        {
            final List<Bike> bikeList = new ArrayList<>();
            final String selectQuery = "SELECT * FROM " + TABLE_BIKES;
            final SQLiteDatabase db = getReadableDatabase();
            final Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.moveToFirst())
            {
                do
                {
                    final Bike bike = new Bike();
                    bike.setId(Integer.parseInt(cursor.getString(0)));
                    bike.setName(cursor.getString(1));
                    bike.setMake(cursor.getString(2));
                    bike.setYear(cursor.getString(3));
                    bike.setModel(cursor.getString(4));
                    bike.setDescription(cursor.getString(5));
                    bike.setDistance(Double.parseDouble(cursor.getString(6)));
                    bike.setLastRideDate(cursor.getString(7));
                    bikeList.add(bike);
                } while(cursor.moveToNext());
            }
            cursor.close();
            //db.close();
            return bikeList;
        }


    List<Group> getAllGroups()
        {
            final List<Group> groupList = new ArrayList<>();
            final String selectQuery = "SELECT * FROM " + TABLE_GROUPS;
            final SQLiteDatabase db = getReadableDatabase();
            final Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.moveToFirst())
            {
                do
                {
                    boolean group_is_activated = false;
                    if("1".equals(cursor.getString(6)))
                    {
                        group_is_activated = true;
                    }
                    boolean pauseControl = false;
                    if("1".equals(cursor.getString(5)))
                    {
                        pauseControl = true;
                    }
                    final Group group = new Group(cursor.getInt(0),         //id
                                                  cursor.getString(1),     //name
                                                  cursor.getInt(2),       //periodicDelay
                                                  cursor.getInt(3),      //movementWaitTime
                                                  cursor.getInt(4),     //stopPeriodicDelay
                                                  pauseControl,        //pause
                                                  group_is_activated);//activated
                    groupList.add(group);
                } while(cursor.moveToNext());
            }
            cursor.close();
            //db.close();
            return groupList;
        }


    @Override public void onCreate(final SQLiteDatabase db)
        {
            db.execSQL("PRAGMA foreign_keys=ON");
            final String CREATE_RIDE_TABLE = "CREATE TABLE " + TABLE_RIDES + " ("
                                             + KEY_RIDE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                             + KEY_BIKE_USED_ID + " TEXT, "
                                             + KEY_AVG_SPEED + " TEXT, "
                                             + KEY_MAX_SPEED + " TEXT, "
                                             + KEY_DISTANCE + " TEXT, "
                                             + KEY_ELE_GAIN + " TEXT, "
                                             + KEY_ELE_LOSS + " TEXT, "
                                             + KEY_DURATION + " TEXT, "
                                             + KEY_RIDE_DATE + " TEXT);";
            db.execSQL(CREATE_RIDE_TABLE);
            final String CREATE_BIKE_TABLE = "CREATE TABLE " + TABLE_BIKES + " ("
                                             + KEY_BIKE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                             + KEY_BIKE_NAME + " TEXT, "
                                             + KEY_BIKE_MAKE + " TEXT, "
                                             + KEY_BIKE_YEAR + " TEXT, "
                                             + KEY_BIKE_MODEL + " TEXT, "
                                             + KEY_BIKE_DESCRIPTION + " TEXT, "
                                             + KEY_BIKE_TOTAL_DISTANCE + " TEXT, "
                                             + KEY_BIKE_LAST_RIDE_DATE + " TEXT);";
            db.execSQL(CREATE_BIKE_TABLE);
            final String CREATE_COMPONENT = "CREATE TABLE " + TABLE_COMPONENTS + " ("
                                            + KEY_COMPONENT_ID + " INTEGER PRIMARY KEY " +
                                            "AUTOINCREMENT, "
                                            + KEY_COMPONENT_YEAR + " TEXT, "
                                            + KEY_COMPONENT_TYPE + " TEXT, "
                                            + KEY_COMPONENT_MAKE + " TEXT, "
                                            + KEY_COMPONENT_MODEL + " TEXT, "
                                            + KEY_COMPONENT_PART_NUMBER + " TEXT, "
                                            + KEY_COMPONENT_TOTAL_DISTANCE + " TEXT, "
                                            + KEY_COMPONENT_DATE_INSTALLED + " TEXT, "
                                            + KEY_COMPONENT_LAST_DATE_INSPECTED + " TEXT);";
            db.execSQL(CREATE_COMPONENT);
            final String CREATE_GROUP_TABLE = "CREATE TABLE " + TABLE_GROUPS + " ("
                                              + KEY_GROUP_ID +
                                              " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                              + KEY_GROUP_NAME + " TEXT, "
                                              + KEY_PERIODIC_DELAY + " TEXT, "
                                              + KEY_MOVEMENT_WAIT_TIME + " TEXT, "
                                              + KEY_STOP_PERIODIC_DELAY + " TEXT, "
                                              + KEY_PAUSE_BUTTON_STOPS_SERVICE + " BOOLEAN, "
                                              + KEY_GROUP_IS_ACTIVATED + " BOOLEAN);";
            db.execSQL(CREATE_GROUP_TABLE);
            final String CREATE_CONTACT_TABLE = "CREATE TABLE " + TABLE_CONTACT + " ("
                                                + KEY_CONTACT_ID
                                                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                + KEY_CONTACT_NAME + " TEXT, "
                                                + KEY_CONTACT_PHONE + " TEXT); ";
            db.execSQL(CREATE_CONTACT_TABLE);
            final String CREATE_GROUP_CONTACT_RELATION = "CREATE TABLE " + RELATION_CONTACT_GROUP
                                                         + " ("
                                                         + KEY_CONTACT_ID
                                                         + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                         + FOREIGN_KEY_CONTACT_ID + " TEXT, "
                                                         + FOREIGN_KEY_GROUP_ID + " TEXT, "
                                                         + "FOREIGN KEY ("
                                                         + FOREIGN_KEY_CONTACT_ID + ") "
                                                         + "REFERENCES " + TABLE_CONTACT
                                                         + "(" + KEY_CONTACT_ID + "), "
                                                         + " FOREIGN KEY" + " ("
                                                         + FOREIGN_KEY_GROUP_ID + ")"
                                                         + " REFERENCES " + TABLE_GROUPS
                                                         + "(" + KEY_GROUP_ID + "));";
            db.execSQL(CREATE_GROUP_CONTACT_RELATION);
            //db.close();
        }


    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_RIDES);
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_BIKES);
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_GROUPS);
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_CONTACT);
            db.execSQL("DROP TABLE IF EXISTS" + RELATION_CONTACT_GROUP);
            //create tables again
            onCreate(db);
        }
}
