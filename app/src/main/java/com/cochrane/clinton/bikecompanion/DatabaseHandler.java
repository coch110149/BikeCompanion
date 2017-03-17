package com.cochrane.clinton.bikecompanion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


class DatabaseHandler extends SQLiteOpenHelper
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
	private static final String KEY_STOP_PERIDOIC_DELAY = "Stop_Periodic_Delay";
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


	DatabaseHandler( Context context )
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}


	@Override
	public void onCreate( SQLiteDatabase db )
		{
			db.execSQL("PRAGMA foreign_keys=ON");
			String CREATE_RIDE_TABLE = "CREATE TABLE "
					                           + TABLE_RIDES + " ("
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
			String CREATE_BIKE_TABLE = "CREATE TABLE "
					                           + TABLE_BIKES + " ("
					                           + KEY_BIKE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					                           + KEY_BIKE_NAME + " TEXT, "
					                           + KEY_BIKE_MAKE + " TEXT, "
					                           + KEY_BIKE_YEAR + " TEXT, "
					                           + KEY_BIKE_MODEL + " TEXT, "
					                           + KEY_BIKE_DESCRIPTION + " TEXT, "
					                           + KEY_BIKE_TOTAL_DISTANCE + " TEXT);";
			db.execSQL(CREATE_BIKE_TABLE);
			String CREATE_COMPONENT_TABLE = "CREATE TABLE "
					                                + TABLE_COMPONENTS + " ("
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
			db.execSQL(CREATE_COMPONENT_TABLE);
			String CREATE_GROUP_TABLE = "CREATE TABLE " + TABLE_GROUPS + " ("
					                            + KEY_GROUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					                            + KEY_GROUP_NAME + " TEXT, "
					                            + KEY_PERIODIC_DELAY + " TEXT, "
					                            + KEY_MOVEMENT_WAIT_TIME + " TEXT, "
					                            + KEY_STOP_PERIDOIC_DELAY + " TEXT, "
					                            + KEY_PAUSE_BUTTON_STOPS_SERVICE + " TEXT, "
					                            + KEY_GROUP_IS_ACTIVATED + " TEXT);";
			db.execSQL(CREATE_GROUP_TABLE);
			String CREATE_CONTACT_TABLE = "CREATE TABLE " + TABLE_CONTACT + " (" + KEY_CONTACT_ID
					                              + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					                              + KEY_CONTACT_NAME + " TEXT, "
					                              + KEY_CONTACT_PHONE + " TEXT); ";
			db.execSQL(CREATE_CONTACT_TABLE);
			String CREATE_GROUP_CONTACT_RELATION = "CREATE TABLE " + RELATION_CONTACT_GROUP + " (" +
					                                       KEY_CONTACT_ID
					                                       + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					                                       + FOREIGN_KEY_CONTACT_ID + " TEXT, "
					                                       + FOREIGN_KEY_GROUP_ID + " TEXT, "
					                                       + "FOREIGN KEY (" + FOREIGN_KEY_CONTACT_ID
					                                       + ") " + "REFERENCES " + TABLE_CONTACT +
					                                       "(" + KEY_CONTACT_ID + "), "
					                                       + " FOREIGN KEY" + " (" +
					                                       FOREIGN_KEY_GROUP_ID + ") " + "REFERENCES "
					                                       + TABLE_GROUPS + "(" +
					                                       KEY_GROUP_ID + "));";
			db.execSQL(CREATE_GROUP_CONTACT_RELATION);
			//db.close();
		}


	@Override
	public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
		{
			db.execSQL("DROP TABLE IF EXISTS" + TABLE_RIDES);
			db.execSQL("DROP TABLE IF EXISTS" + TABLE_BIKES);
			db.execSQL("DROP TABLE IF EXISTS" + TABLE_GROUPS);
			db.execSQL("DROP TABLE IF EXISTS" + TABLE_CONTACT);
			db.execSQL("DROP TABLE IF EXISTS" + RELATION_CONTACT_GROUP);
			//create tables again
			onCreate(db);
		}


	void addRide( Ride ride )
		{
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(KEY_BIKE_USED_ID, ride.getBikeID());
			values.put(KEY_AVG_SPEED, ride.getAvgSpeed());
			values.put(KEY_MAX_SPEED, ride.getMaxSpeed());
			values.put(KEY_DURATION, ride.getDuration());
			values.put(KEY_DISTANCE, ride.getDistance());
			values.put(KEY_ELE_LOSS, ride.getElevationLoss());
			values.put(KEY_ELE_GAIN, ride.getElevationGain());
			values.put(KEY_RIDE_DATE, ride.getRideDate());
			db.insert(TABLE_RIDES, null, values);
			//update bike used's distance
			//db.close();
		}


	Ride getRide( int id )
		{
			SQLiteDatabase db = this.getReadableDatabase();
			String selectQuery = "SELECT * FROM " + TABLE_RIDES + " WHERE " + KEY_RIDE_ID + " = " +
					                     String.valueOf(id);
			Cursor cursor = db.rawQuery(selectQuery, null);
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


	public List<Ride> getAllRides()
		{
			List<Ride> rideList = new ArrayList<>();
			String selectQuery = "SELECT * FROM " + TABLE_RIDES;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			if(cursor.moveToFirst())
			{
				do
				{
					Ride ride = new Ride(
							                    cursor.getInt(0),       //id
							                    cursor.getInt(1),       //bike id
							                    cursor.getDouble(2),    //avgSpeed
							                    cursor.getDouble(3),    //MaxSpeed
							                    cursor.getDouble(4),    //Distance
							                    cursor.getDouble(5),    //EleGain
							                    cursor.getDouble(6),    //EleLoss
							                    cursor.getString(7),    //Duration
							                    cursor.getString(8));   //Date
					rideList.add(ride);
				} while (cursor.moveToNext());
			}
			cursor.close();
			//db.close();
			return rideList;
		}


	public int getRideCount()
		{
			String countQuery = "SELECT * FROM " + TABLE_RIDES;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			cursor.close();
			//db.close();
			return cursor.getCount();
		}


	int updateRide( Ride ride )
		{
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(KEY_BIKE_USED_ID, ride.getBikeID());
			values.put(KEY_AVG_SPEED, ride.getAvgSpeed());
			values.put(KEY_MAX_SPEED, ride.getMaxSpeed());
			values.put(KEY_DURATION, ride.getDuration());
			values.put(KEY_DISTANCE, ride.getDistance());
			values.put(KEY_ELE_LOSS, ride.getElevationLoss());
			values.put(KEY_ELE_GAIN, ride.getElevationGain());
			values.put(KEY_RIDE_DATE, ride.getRideDate());
			return db.update(TABLE_RIDES, values, KEY_RIDE_ID + " =?",
					new String[]{String.valueOf(ride.getID())});
		}


	void deleteRide( Ride ride )
		{
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_RIDES, KEY_RIDE_ID + "=?", new String[]{String.valueOf(ride.getID())});
			//db.close();
		}


	private void removeBikeFromRide( int bikeID )
		{
			String selectQuery = "SELECT * FROM " + TABLE_RIDES + " WHERE " + KEY_BIKE_USED_ID +
					                     " = " + bikeID;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			{
				if(cursor.moveToFirst())
				{
					do
					{
						updateRide(new Ride(
								                   cursor.getInt(0),       //id
								                   -1,                     //bike id
								                   cursor.getDouble(2),    //avgSpeed
								                   cursor.getDouble(3),    //MaxSpeed
								                   cursor.getDouble(4),    //Distance
								                   cursor.getDouble(5),    //EleGain
								                   cursor.getDouble(6),    //EleLoss
								                   cursor.getString(7),    //Duration
								                   cursor.getString(8)));   //Date
					} while (cursor.moveToNext());
				}
				cursor.close();
				//db.close();
			}
		}


	Cursor getBikeName_RideCursor()
		{
			String selectQuery = "SELECT " + TABLE_RIDES + "." + KEY_RIDE_ID + " ," + KEY_DISTANCE + ", "
					                     + KEY_DURATION + ", " + KEY_RIDE_DATE + ", " + KEY_BIKE_NAME +
					                     " FROM " + TABLE_RIDES + " LEFT JOIN " + TABLE_BIKES + " ON " +
					                     TABLE_RIDES + "." + KEY_BIKE_USED_ID + " = " + TABLE_BIKES +
					                     "." + KEY_BIKE_ID;
			SQLiteDatabase db = this.getReadableDatabase();
			return db.rawQuery(selectQuery, null);
		}


	void addBike( Bike bike )
		{
			if(getBike(bike.getID()) == null)
			{
				SQLiteDatabase db = this.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(KEY_BIKE_NAME, bike.getBikeName());
				values.put(KEY_BIKE_MAKE, bike.getBikeMake());
				values.put(KEY_BIKE_YEAR, bike.getBikeYear());
				values.put(KEY_BIKE_MODEL, bike.getBikeModel());
				values.put(KEY_BIKE_DESCRIPTION, bike.getBikeDescription());
				values.put(KEY_BIKE_TOTAL_DISTANCE, bike.getTotalBikeDistance());
				db.insert(TABLE_BIKES, null, values);
				//db.close();
			} else
			{
				updateBike(bike);
			}

		}


	Bike getBike( int id )
		{
			SQLiteDatabase db = this.getReadableDatabase();
			String selectQuery = "SELECT * FROM " + TABLE_BIKES + " WHERE " + KEY_BIKE_ID + " = " +
					                     String.valueOf(id);
			Cursor cursor = db.rawQuery(selectQuery, null);
			Bike bike = null;
			if(cursor != null)
			{
				if(cursor.moveToFirst())
				{
					bike = new Bike(
							               cursor.getInt(0),    //id
							               cursor.getString(1), //name
							               cursor.getString(2), //make
							               cursor.getString(3), //year
							               cursor.getString(4), //model
							               cursor.getString(5), //Description
							               cursor.getDouble(6)); //Distance
				}
				cursor.close();
			}
			//db.close();
			return bike;
		}


	List<Bike> getAllBikes()
		{
			List<Bike> bikeList = new ArrayList<>();
			String selectQuery = "SELECT * FROM " + TABLE_BIKES;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			if(cursor.moveToFirst())
			{
				do
				{
					Bike bike = new Bike();
					bike.setID(Integer.parseInt(cursor.getString(0)));
					bike.setBikeName(cursor.getString(1));
					bike.setBikeMake(cursor.getString(2));
					bike.setBikeYear(cursor.getString(3));
					bike.setBikeModel(cursor.getString(4));
					bike.setBikeDescription(cursor.getString(5));
					bike.setTotalBikeDistance(Double.parseDouble(cursor.getString(6)));
					bikeList.add(bike);
				} while (cursor.moveToNext());
			}
			cursor.close();
			//db.close();
			return bikeList;
		}


	int getBikeCount()
		{
			String countQuery = "SELECT * FROM " + TABLE_BIKES;
			SQLiteDatabase db = this.getReadableDatabase();
			return db.rawQuery(countQuery, null).getCount();
		}


	private int updateBike( Bike bike )
		{
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(KEY_BIKE_ID, bike.getID());
			values.put(KEY_BIKE_NAME, bike.getBikeName());
			values.put(KEY_BIKE_MAKE, bike.getBikeMake());
			values.put(KEY_BIKE_YEAR, bike.getBikeYear());
			values.put(KEY_BIKE_MODEL, bike.getBikeModel());
			values.put(KEY_BIKE_DESCRIPTION, bike.getBikeDescription());
			values.put(KEY_BIKE_TOTAL_DISTANCE, bike.getTotalBikeDistance());
			return db.update(TABLE_BIKES, values, KEY_BIKE_ID + " =?",
					new String[]{String.valueOf(bike.getID())});
		}


	void deleteBike( Bike bike )
		{
			removeBikeFromRide(bike.getID());
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_BIKES, KEY_BIKE_ID + "=?", new String[]{String.valueOf(bike.getID())});
			//db.close();
		}


	Bike getMostRecentlyRiddenBike()
		{
			SQLiteDatabase db = this.getReadableDatabase();
			int bikeId = 1;
			String selectQuery = "SELECT " + KEY_BIKE_USED_ID + " FROM " + TABLE_RIDES +
					                     " ORDER BY " + KEY_RIDE_DATE + " DESC LIMIT 1";
			Cursor cursor = db.rawQuery(selectQuery, null);
			if(cursor.moveToFirst())
			{
				bikeId = cursor.getInt(0);
			}
			cursor.close();
			//db.close();
			return getBike(bikeId);
		}


	void addGroup( Group group )
		{
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			if(getGroup(group.getID()) == null)
			{
				values.put(KEY_GROUP_NAME, group.getName());
				values.put(KEY_PERIODIC_DELAY, group.getPeriodicDelay());
				values.put(KEY_MOVEMENT_WAIT_TIME, group.getMovementWaitTime());
				values.put(KEY_STOP_PERIDOIC_DELAY, group.getStopPeriodicDelay());
				values.put(KEY_PAUSE_BUTTON_STOPS_SERVICE, group.isPauseButtonStopsService());
				values.put(KEY_GROUP_IS_ACTIVATED, group.isSelected());
				db.insert(TABLE_GROUPS, null, values);
			} else
			{
				updateGroup(group);
			}
			//db.close();
		}


	Group getGroup( int id )
		{
			SQLiteDatabase db = this.getReadableDatabase();
			String selectQuery = "SELECT * FROM " + TABLE_GROUPS + " WHERE " + KEY_GROUP_ID + " = " +
					                     String.valueOf(id);
			Cursor cursor = db.rawQuery(selectQuery, null);
			Group group = null;
			if(cursor != null)
			{
				if(cursor.moveToFirst())
				{
					group = new Group(
							                 cursor.getInt(0),        //id
							                 cursor.getString(1),    //name
							                 cursor.getInt(2),      //periodicDelay
							                 cursor.getInt(3),     //movementWaitTime
							                 cursor.getInt(4),    //stopPeriodicDelay
							                 Boolean.getBoolean(cursor.getString(5)),//pauseButton
							                 Boolean.getBoolean(cursor.getString(6))); //activated
				}
				cursor.close();
			}
			//db.close();
			return group;
		}


	public List<Group> getAllGroups()
		{
			List<Group> groupList = new ArrayList<>();
			String selectQuery = "SELECT * FROM " + TABLE_GROUPS;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			if(cursor.moveToFirst())
			{
				do
				{
					Group group = new Group(
							                       cursor.getInt(0),        //id
							                       cursor.getString(1),    //name
							                       cursor.getInt(2),      //periodicDelay
							                       cursor.getInt(3),     //movementWaitTime
							                       cursor.getInt(4),    //stopPeriodicDelay
							                       Boolean.getBoolean(cursor.getString(5)), //pause
							                       Boolean.getBoolean(cursor.getString(6)));//activated
					groupList.add(group);
				} while (cursor.moveToNext());
			}
			cursor.close();
			//db.close();
			return groupList;
		}


	public List<Group> getAllGroups( boolean activated )
		{
			List<Group> groupList = new ArrayList<>();
			String selectQuery = "SELECT * FROM " + TABLE_GROUPS + " WHERE " + KEY_GROUP_IS_ACTIVATED
					                     + "=" + "true";
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			if(cursor.moveToFirst())
			{
				do
				{
					Group group = new Group(
							                       cursor.getInt(0),        //id
							                       cursor.getString(1),    //name
							                       cursor.getInt(2),      //periodicDelay
							                       cursor.getInt(3),     //movementWaitTime
							                       cursor.getInt(4),    //stopPeriodicDelay
							                       Boolean.getBoolean(cursor.getString(5)), //pause
							                       Boolean.getBoolean(cursor.getString(6)));//activated
					groupList.add(group);
				} while (cursor.moveToNext());
			}
			cursor.close();
			//db.close();
			return groupList;
		}


	int getGroupCount()
		{
			String countQuery = "SELECT * FROM " + TABLE_GROUPS;
			SQLiteDatabase db = this.getReadableDatabase();
			return db.rawQuery(countQuery, null).getCount();
		}


	private int updateGroup( Group group )
		{
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(KEY_GROUP_ID, group.getID());
			values.put(KEY_GROUP_NAME, group.getName());
			values.put(KEY_PERIODIC_DELAY, group.getPeriodicDelay());
			values.put(KEY_MOVEMENT_WAIT_TIME, group.getMovementWaitTime());
			values.put(KEY_STOP_PERIDOIC_DELAY, group.getStopPeriodicDelay());
			values.put(KEY_PAUSE_BUTTON_STOPS_SERVICE, group.isPauseButtonStopsService());
			values.put(KEY_GROUP_IS_ACTIVATED, group.isSelected());
			return db.update(TABLE_GROUPS, values, KEY_GROUP_ID + " =?",
					new String[]{String.valueOf(group.getID())});
		}


	void deleteGroup( Group group )
		{
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_GROUPS, KEY_GROUP_ID + "=?", new String[]{String.valueOf(group.getID())});
			db.delete(RELATION_CONTACT_GROUP, FOREIGN_KEY_GROUP_ID + "=?",
					new String[]{String.valueOf(group.getID())});
			//db.close();
		}


	void addContact( Contact contact )
		{
			if(getContact(contact.getID()) == null)
			{
				SQLiteDatabase db = this.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(KEY_CONTACT_NAME, contact.getName());
				values.put(KEY_CONTACT_PHONE, contact.getPhoneNumber());
				db.insert(TABLE_CONTACT, null, values);
				//db.close();
			} else
			{
				updateContact(contact);
			}
		}


	Contact getContact( int id )
		{
			SQLiteDatabase db = this.getReadableDatabase();
			String selectQuery = "SELECT * FROM " + TABLE_CONTACT + " WHERE " + KEY_CONTACT_ID + " = " +
					                     String.valueOf(id);
			Cursor cursor = db.rawQuery(selectQuery, null);
			Contact contact = null;
			if(cursor != null)
			{
				if(cursor.moveToFirst())
				{
					contact = new Contact(
							                     cursor.getInt(0),        //id
							                     cursor.getString(1),    //name
							                     cursor.getString(2));  //phoneNumber
				}
				cursor.close();
			}
			//db.close();
			return contact;
		}


	public List<Contact> getAllContacts()
		{
			List<Contact> contactList = new ArrayList<>();
			String selectQuery = "SELECT * FROM " + TABLE_CONTACT;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			if(cursor.moveToFirst())
			{
				do
				{
					Contact contact = new Contact(
							                             cursor.getInt(0),        //id
							                             cursor.getString(1),    //name
							                             cursor.getString(2));  //phoneNumber
					contactList.add(contact);
				} while (cursor.moveToNext());
			}
			cursor.close();
			//db.close();
			return contactList;
		}


	private int updateContact( Contact contact )
		{
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(KEY_CONTACT_ID, contact.getID());
			values.put(KEY_CONTACT_NAME, contact.getName());
			values.put(KEY_CONTACT_PHONE, contact.getPhoneNumber());
			return db.update(TABLE_CONTACT, values, KEY_CONTACT_ID + " =?",
					new String[]{String.valueOf(contact.getID())});
		}


	void deleteContact( Contact contact )
		{
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_CONTACT, KEY_CONTACT_ID + "=?",
					new String[]{String.valueOf(contact.getID())});
			db.delete(RELATION_CONTACT_GROUP, FOREIGN_KEY_CONTACT_ID + "=?",
					new String[]{String.valueOf(contact.getID())});
			//db.close();
		}


	void insertContactToGroup( int contactID, int groupID )
		{
			if(getContactGroupRelation(contactID, groupID) == null)
			{
				SQLiteDatabase db = this.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(FOREIGN_KEY_CONTACT_ID, contactID);
				values.put(FOREIGN_KEY_GROUP_ID, groupID);
				db.insert(TABLE_CONTACT, null, values);
				//db.close();
			}
		}


	Object getContactGroupRelation( int contactID, int groupID )
		{
			String returnValue = null;
			String selectQuery = "SELECT " + "*" + " FROM " + RELATION_CONTACT_GROUP + " WHERE " +
					                     groupID + "=" + RELATION_CONTACT_GROUP + "." +
					                     FOREIGN_KEY_GROUP_ID + " AND " + contactID + "=" +
					                     RELATION_CONTACT_GROUP + "." + FOREIGN_KEY_CONTACT_ID;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			if(cursor.moveToFirst())
			{
				returnValue = "Y";
			}
			cursor.close();
			return returnValue;
		}


	List<Contact> getAllContactsInGroup( int groupID )
		{
			String selectQuery;
			selectQuery = "SELECT " + TABLE_CONTACT + "." + KEY_CONTACT_NAME + ", " + TABLE_CONTACT +
					              "." + KEY_CONTACT_PHONE;
			selectQuery += " FROM " + RELATION_CONTACT_GROUP + ", " + TABLE_CONTACT;
			selectQuery += " WHERE " + groupID + "=" + RELATION_CONTACT_GROUP + "." +
					               FOREIGN_KEY_GROUP_ID + " AND " + RELATION_CONTACT_GROUP + "." +
					               FOREIGN_KEY_CONTACT_ID + "=" + TABLE_CONTACT + "." + KEY_CONTACT_ID;
			List<Contact> contactList = new ArrayList<>();
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			if(cursor.moveToFirst())
			{
				do
				{
					Contact contact = new Contact(
							                             cursor.getInt(0),        //id
							                             cursor.getString(1),    //name
							                             cursor.getString(2));  //phoneNumber
					contactList.add(contact);
				} while (cursor.moveToNext());
			}
			cursor.close();
			//db.close();
			return contactList;
		}


	int getCountOfContactsInGroup( int groupID )
		{
			String countQuery = "SELECT * FROM " + RELATION_CONTACT_GROUP + " WHERE " + groupID + "=" +
					                    RELATION_CONTACT_GROUP + "." + FOREIGN_KEY_GROUP_ID;
			SQLiteDatabase db = this.getReadableDatabase();
			return db.rawQuery(countQuery, null).getCount();
		}
	}
