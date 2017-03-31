package com.cochrane.clinton.bikecompanion;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;


public class RBMService extends Service
{
    private final IBinder mBinder = new RBMBinder();
    private final DatabaseHandler mDb = new DatabaseHandler(this);
    private long mMinute = 1;
    private RideActivity mRide;
    private boolean running;
    private ArrayList<Group> mGroups;
    //// TODO: 25/03/2017 spawn a thread


    @Override public int onStartCommand(final Intent intent, final int flags, final int startId)
        {
            final ArrayList<Group> groups = intent.getParcelableArrayListExtra("GroupObject");
            testNotifications(groups);
            return super.onStartCommand(intent, flags, startId);
        }


    private void testNotifications(final ArrayList<Group> _groups)
        {
            //get a list of all contacts in each group
            final ArrayList<Contact> contacts = new ArrayList<>();
            for(final Group group : _groups)
            {
                contacts.addAll(mDb.getAllContactsInGroup(group.getId()));
            }
            for(final Contact contact : contacts)
            {
                final String message = "Hey, just testing notifications with " +
                                       " the bike companion app";
                sendMessage(contact, message);
            }
            stopSelf();
        }


    private void sendMessage(final Contact _contact, final String message)
        {
            if(!("").equals(message))
            {
                final SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(_contact.getNumber(), null, message, null, null);
                Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
                /**
                 * drift error = accuracy + (speed*time)
                 * currentLocation.distanceTo(PreviousLocation) - driftError <=5 [indicates no
                 * movement)
                 *
                 * chose 5 because US government claims most smart phones are accurate within 4.9m
                 */
            }
        }


    @Override public void onDestroy()
        {
            mDb.close();
            super.onDestroy();
        }


    @Override
    public IBinder onBind(final Intent intent)
        {
            return mBinder;
        }


    public void beginRide(final RideActivity ride)
        {
            mGroups = (ArrayList<Group>) mDb.getAllGroups();
            if((mGroups != null) && (!mGroups.isEmpty()))
            {
                running = true;
                mRide = ride;
                messageTimer();
            }
        }


    private void messageTimer()
        {
            final Handler handler = new Handler();
            handler.post(new Runnable()
            {
                @Override
                public void run()
                    {
                        if(running)
                        {
                            checkGroupTime();
                            mMinute++;
                            handler.postDelayed(this, 60000);
                        }

                    }
            });
        }


    private void checkGroupTime()
        {
            for(final Group group : mGroups)
            {
                if((mMinute % group.getPeriodicDelay()) == 0)
                {
                    for(final Contact contact : mDb.getAllContactsInGroup(group.getId()))
                    {
                        sendMessage(contact, mRide.getMessage());
                    }
                }
            }
        }


    public void stopRide()
        {
            running = false;
        }



    public class RBMBinder extends Binder
    {
        RBMService getService()
            {
                return RBMService.this;
            }
    }
}
