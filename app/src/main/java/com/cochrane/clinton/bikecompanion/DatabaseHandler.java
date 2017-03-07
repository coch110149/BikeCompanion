package com.cochrane.clinton.bikecompanion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clint on 04/03/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper
	{
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "rideManager";

	//Rides Table
	private static final String TABLE_RIDES = "rides";
	private static final String KEY_ID = "id";
	private static final String KEY_BIKE_ID = "bike_id";
	private static final String KEY_AVG_SPEED = "Average_Speed";
	private static final String KEY_MAX_SPEED = "Max_Speed";
	private static final String KEY_AVG_PACE = "Average_Pace";
	private static final String KEY_DURATION = "Ride_Duration";
	private static final String KEY_DISTANCE = "Ride_Distance";
	private static final String KEY_ELE_LOSS = "Elevation_Loss";
	private static final String KEY_ELE_GAIN = "Elevation_Gain";
	private static final String KEY_RIDE_DATE = "Ride_Date";

	public DatabaseHandler(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

	@Override
	public void onCreate(SQLiteDatabase db)
		{
			String CREATE_RIDES_TABLE = "CREATE TABLE "
					                            + TABLE_RIDES + " ("
					                            + KEY_ID + " INTEGER PRIMARY KEY, "
					                            + KEY_BIKE_ID + " TEXT, "
					                            + KEY_AVG_SPEED + " TEXT, "
					                            + KEY_MAX_SPEED + " TEXT, "
					                            + KEY_AVG_PACE + " TEXT, "
					                            + KEY_DURATION + " TEXT, "
					                            + KEY_DISTANCE + " TEXT, "
					                            + KEY_ELE_LOSS + " TEXT, "
					                            + KEY_ELE_GAIN + " TEXT, "
					                            + KEY_RIDE_DATE + " TEXT);";
			db.execSQL(CREATE_RIDES_TABLE);
		}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS" + TABLE_RIDES);
			//create tables again
			onCreate(db);
		}

	public void addRide(Ride ride)
		{
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(KEY_BIKE_ID, ride.getBikeID());
			values.put(KEY_AVG_SPEED, ride.getAvgSpeed());
			values.put(KEY_MAX_SPEED, ride.getMaxSpeed());
			values.put(KEY_AVG_PACE, ride.getAvgPace());
			values.put(KEY_DURATION, ride.getDuration());
			values.put(KEY_DISTANCE, ride.getDistance());
			values.put(KEY_ELE_LOSS, ride.getElevationLoss());
			values.put(KEY_ELE_GAIN, ride.getElevationGain());
			values.put(KEY_RIDE_DATE, ride.getRideDate());

			db.insert(TABLE_RIDES, null, values);
			db.close();
		}

	public Ride getRide(int id)
		{
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_RIDES, new String[]{KEY_ID, KEY_BIKE_ID, KEY_AVG_SPEED,
							KEY_MAX_SPEED, KEY_AVG_PACE, KEY_DURATION, KEY_DISTANCE, KEY_ELE_LOSS,
							KEY_ELE_GAIN, KEY_RIDE_DATE}, KEY_ID + "=?", new String[]{String.valueOf(id)},
					null, null, null, null);
			Ride ride = null;
			if (cursor != null)
			{
				if(cursor.moveToFirst())
				{
					ride = new Ride(
							               Integer.parseInt(cursor.getString(0)),          //id
							               Integer.parseInt(cursor.getString(1)),   //bike id
							               Double.parseDouble(cursor.getString(2)), //avgSpeed
							               Double.parseDouble(cursor.getString(3)), //MaxSpeed
							               Double.parseDouble(cursor.getString(4)), //Distance
							               Double.parseDouble(cursor.getString(5)), //EleGain
							               Double.parseDouble(cursor.getString(6)), //EleLoss
							               cursor.getString(7), cursor.getString(8),//AvgPace, Duration
							               cursor.getString(9));                    //Date
				}

			}
			return ride;

		}

	public List<Ride> getAllRides()
		{
			List<Ride> rideList = new ArrayList<>();
			String selectQuery = "SELECT * FROM " + TABLE_RIDES;
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst())
			{
				do
				{
					Ride ride = new Ride();
					ride.setID(Integer.parseInt(cursor.getString(0)));
					ride.setBikeID(Integer.parseInt(cursor.getString(1)));
					ride.setAvgSpeed(Double.parseDouble(cursor.getString(2)));
					ride.setMaxSpeed(Double.parseDouble(cursor.getString(3)));
					ride.setDistance(Double.parseDouble(cursor.getString(4)));
					ride.setElevationGain(Double.parseDouble(cursor.getString(5)));
					ride.setElevationLoss(Double.parseDouble(cursor.getString(6)));
					ride.setAvgPace(cursor.getString(7));
					ride.setDuration(cursor.getString(8));
					ride.setRideDate(cursor.getString(9));

					rideList.add(ride);
				} while (cursor.moveToNext());
			}

			return rideList;
		}

	public int getRideCount()
		{
			String countQuery = "SELECT * FROM " + TABLE_RIDES;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			cursor.close();
			return cursor.getCount();
		}

	public int updateRide(Ride ride)
		{
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(KEY_BIKE_ID, ride.getBikeID());
			values.put(KEY_AVG_SPEED, ride.getAvgSpeed());
			values.put(KEY_MAX_SPEED, ride.getMaxSpeed());
			values.put(KEY_AVG_PACE, ride.getAvgPace());
			values.put(KEY_DURATION, ride.getDuration());
			values.put(KEY_DISTANCE, ride.getDistance());
			values.put(KEY_ELE_LOSS, ride.getElevationLoss());
			values.put(KEY_ELE_GAIN, ride.getElevationGain());
			values.put(KEY_RIDE_DATE, ride.getRideDate());

			return db.update(TABLE_RIDES, values, KEY_ID + " =?",
					new String[]{String.valueOf(ride.getID())});
		}

	public void deleteRide (Ride ride)
		{
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_RIDES, KEY_ID + "=?", new String [] {String.valueOf(ride.getID())} );
			db.close();
		}


	/**
	 * each ride save will update year, month, and week currant tables
	 * month
	 *      On the first on each month, copy current month to previous month
	 *
	 *
	 *
	 */

	}
