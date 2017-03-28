package com.cochrane.clinton.bikecompanion;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GroupConfigActivity extends AppCompatActivity
{
    private static final Pattern MIN = Pattern.compile(" min", Pattern.LITERAL);
    final private DatabaseHandler mDb = new DatabaseHandler(this);
    private Group mGroup = new Group();
    private Switch mMoveUpdateSwitch;
    private EditText mGroupNameEdit;
    private Switch mPeriodicSwitch;
    private EditText mPeriodicTime;
    private EditText mStopPeriodic;
    private EditText mStopWaitTime;
    private Resources mRes;


    @Override
    protected void onCreate(final Bundle savedInstanceState)
        {
            ///// TODO: 25/03/2017 set up contact count
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_group_configuration);
            mRes = getResources();
            final Bundle bundle = getIntent().getExtras();
            if(bundle != null)
            {
                mGroup = bundle.getParcelable("SelectedGroupObject");
            }
            assert mGroup != null;
            final Button manageGroupButton = (Button) findViewById(R.id.manage_group_contacts);
            final Button deleteGroupButton = (Button) findViewById(R.id.delete_group);
            final Button saveGroupButton = (Button) findViewById(R.id.save_group);
            mPeriodicSwitch = (Switch) findViewById(R.id.periodic_updates_switch);
            mMoveUpdateSwitch = (Switch) findViewById(R.id.stopped_update_switch);
            mStopWaitTime = (EditText) findViewById(R.id.stopped_wait_time_edit);
            mStopPeriodic = (EditText) findViewById(R.id.stop_periodic_time);
            mPeriodicTime = (EditText) findViewById(R.id.periodic_time);
            mGroupNameEdit = (EditText) findViewById(R.id.group_name);
            manageGroupButton.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(final View v){setUpContacts();}
            });
            mGroupNameEdit.setText(mGroup.getName());
            mPeriodicTime.setText(mRes.getString(R.string.minutes,
                                                 Math.abs(mGroup.getPeriodicDelay())));
            mStopWaitTime.setText(mRes.getString(R.string.minutes,
                                                 Math.abs(mGroup.getMovementWaitTime())));
            mStopPeriodic.setText(mRes.getString(R.string.minutes,
                                                 Math.abs(mGroup.getStopPeriodicDelay())));
            if(mGroup.getPeriodicDelay() < 0)
            {
                mPeriodicSwitch.setChecked(false);
            }else if(mGroup.getPeriodicDelay() > 0)
            {
                mPeriodicSwitch.setChecked(true);
            }else
            {
                mPeriodicTime.setText("");
            }
            if((mGroup.getMovementWaitTime() < -1) || (mGroup.getStopPeriodicDelay() < -1))
            {
                mMoveUpdateSwitch.setChecked(false);
            }else if((mGroup.getMovementWaitTime() > 1) || (mGroup.getStopPeriodicDelay() > 1))
            {
                mMoveUpdateSwitch.setChecked(true);
            }else
            {
                if(mGroup.getMovementWaitTime() == 0)
                {
                    mStopWaitTime.setText("");
                }
                if(mGroup.getStopPeriodicDelay() == 0)
                {
                    mStopPeriodic.setText("");
                }
            }
            deleteGroupButton.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(final View v)
                    {
                        deleteGroup();
                    }
            });
            saveGroupButton.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(final View v)
                    {
                        SaveGroup();
                    }
            });
        }


    private void setUpContacts()
        {
            final Intent intent = new Intent(GroupConfigActivity.this,
                                             ContactManagementActivity.class);
            intent.putExtra("GroupId", String.valueOf(mGroup.getId()));
            startActivity(intent);
        }


    private void deleteGroup()
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(mRes.getString(R.string.confirm_delete, mGroup.getName()));
            builder.setPositiveButton(R.string.yes_exact, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(final DialogInterface dialog, final int which)
                    {
                        mDb.deleteGroup(mGroup);
                        startActivity(new Intent(GroupConfigActivity.this,
                                                 GroupManagementActivity.class));
                    }
            });
            //noinspection AnonymousInnerClassMayBeStatic
            builder.setNegativeButton(R.string.no_exact, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(final DialogInterface dialog, final int which)
                    {
                        dialog.dismiss();
                    }
            });
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }


    private void SaveGroup()
        {
            if("".equals(mGroupNameEdit.getText().toString().trim()))
            {
                mGroupNameEdit.setError("Group Name Is Required");
            }else
            {
                if(periodicInputIsAcceptable() && stoppedMovementPeriodicInputIsAcceptable() &&
                   movementMessagingDelayInputIsAcceptable())
                {
                    mGroup.setName(mGroupNameEdit.getText().toString().trim());
                    mDb.addGroup(mGroup);
                    startActivity(new Intent(GroupConfigActivity.this,
                                             GroupManagementActivity.class));
                }
            }
        }


    private boolean periodicInputIsAcceptable()
        {
            boolean output = true;
            try
            {
                final String input = MIN.matcher(mPeriodicTime.getText().toString())
                                             .replaceAll(Matcher.quoteReplacement(""));
                final int delay = "".equals(input) ? 0 : Integer.parseInt(input);
                mGroup.setPeriodicDelay(delay, mPeriodicSwitch.isChecked());
            }catch(final NumberFormatException _e)
            {
                mPeriodicTime.setError("Enter a whole number");
                output = false;
            }
            return output;
        }


    private boolean stoppedMovementPeriodicInputIsAcceptable()
        {
            boolean output = true;
            try
            {
                final String stop = MIN.matcher(mStopPeriodic.getText().toString())
                                            .replaceAll(Matcher.quoteReplacement(""));
                final int stopDelay = "".equals(stop) ? 0 : Integer.parseInt(stop);
                mGroup.setStopPeriodicDelay(stopDelay, mMoveUpdateSwitch.isChecked());
            }catch(final NumberFormatException _e)
            {
                mStopPeriodic.setError("Whole Numbers only");
                output = false;
            }
            return output;
        }


    private boolean movementMessagingDelayInputIsAcceptable()
        {
            boolean output = true;
            try
            {
                final String stopWait = MIN.matcher(mStopWaitTime.getText().toString())
                                                .replaceAll(Matcher.quoteReplacement(""));
                final int waitDelay = "".equals(stopWait) ? 0 : Integer.parseInt(stopWait);
                mGroup.setMovementWaitTime(waitDelay, mMoveUpdateSwitch.isChecked());
            }catch(final NumberFormatException _e)
            {
                mStopWaitTime.setError("Whole Numbers only");
                output = false;
            }
            return output;
        }
}
