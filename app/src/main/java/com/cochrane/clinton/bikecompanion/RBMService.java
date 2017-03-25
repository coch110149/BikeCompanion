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
    private ArrayList<Group> mGroups;
    private long mMinuite = 1;
    private RideActivity mRide;
    private DatabaseHandler db = new DatabaseHandler(this);


    @Override public void onCreate()
        {
            super.onCreate();
        }


    @Override public int onStartCommand(Intent intent, int flags, int startId)
        {
            ArrayList<Group> groups = intent.getParcelableArrayListExtra("GroupObject");
            testNotifications(groups);
            return super.onStartCommand(intent, flags, startId);
        }


    private void testNotifications(ArrayList<Group> _groups)
        {
            //get a list of all contacts in each group
            DatabaseHandler db = new DatabaseHandler(this);
            ArrayList<Contact> contacts = new ArrayList<>();
            for(Group group : _groups)
            {
                contacts.addAll(db.getAllContactsInGroup(group.getId()));
            }
            for(Contact contact : contacts)
            {
                String message = "Hey, this is just a test message from the bike companion app";
                sendMessage(contact, message);
            }
            stopSelf();
        }


    public void sendMessage(Contact _contact, String message)
        {
            if(!("").equals(message))
            {
                final SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(_contact.getPhoneNumber(), null, message, null, null);
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


    @Override
    public IBinder onBind(final Intent intent)
        {
            return mBinder;
        }


    public void beginRide(RideActivity ride)
        {
            mGroups = (ArrayList<Group>) db.getAllGroups(true);
            if((mGroups != null) && (!mGroups.isEmpty()))
            {
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
                        checkGroupTime();
                        mMinuite++;
                        handler.postDelayed(this, 60000);
                    }
            });
        }


    private void checkGroupTime()
        {
            for(Group group : mGroups)
            {
                if((mMinuite % group.getPeriodicDelay()) == 0)
                {
                    for(final Contact contact : db.getAllContactsInGroup(group.getId()))
                    {
                        sendMessage(contact, mRide.getMessage());
                    }
                }
            }
        }


    public class RBMBinder extends Binder
    {
        RBMService getService()
            {
                return RBMService.this;
            }
    }
}
