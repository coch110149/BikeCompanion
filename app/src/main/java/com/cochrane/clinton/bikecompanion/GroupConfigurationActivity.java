package com.cochrane.clinton.bikecompanion;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GroupConfigurationActivity extends AppCompatActivity
{
    private static final Pattern MIN =
            Pattern.compile(" min", Pattern.LITERAL);
    private Group group = new Group();
    private EditText groupNameEdit;
    private Switch periodicUpdatesSwitch;
    private Switch moveUpdateSwitch;
    private EditText periodicTime;
    private EditText stopPeriodicTime;
    private EditText stopWaitTime;


    @Override
    protected void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_group_configuration);
            final Bundle bundle = getIntent().getExtras();
            if(bundle != null)
            {
                group = bundle.getParcelable("SelectedGroupObject");
            }
            groupNameEdit = (EditText) findViewById(R.id.group_name);
            periodicTime = (EditText) findViewById(R.id.periodic_time);
            stopWaitTime = (EditText) findViewById(R.id.stopped_wait_time_edit);
            stopPeriodicTime = (EditText) findViewById(R.id.stopped_periodic_time_edit);
            periodicUpdatesSwitch = (Switch) findViewById(R.id.periodic_updates_switch);
            moveUpdateSwitch = (Switch) findViewById(R.id.stopped_updates_switch);
            final Button manageGroupButton = (Button) findViewById(
                    R.id.manage_group_contacts);
            manageGroupButton.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View v)
                    {
                        setUpContacts();
                    }
            });
            final Button deleteGroupButton = (Button) findViewById(
                    R.id.delete_group);
            final Button saveGroupButton = (Button) findViewById(
                    R.id.save_group);
            assert group != null;
            groupNameEdit.setText(group.getName());
            periodicTime.setText(Math.abs(group.getPeriodicDelay()) + " min");
            stopWaitTime.setText(Math.abs(group.getMovementWaitTime()) + " min");
            stopPeriodicTime.setText(Math.abs(group.getStopPeriodicDelay()) + " min");
            if(group.getPeriodicDelay() < 0)
            {
                periodicUpdatesSwitch.setChecked(false);
            }else if(group.getPeriodicDelay() > 0)
            {
                periodicUpdatesSwitch.setChecked(true);
            }else
            {
                periodicTime.setText("");
            }
            if((group.getMovementWaitTime() < -1) || (group.getStopPeriodicDelay() < -1))
            {
                moveUpdateSwitch.setChecked(false);
            }else if((group.getMovementWaitTime() > 1) || (group.getStopPeriodicDelay() > 1))
            {
                moveUpdateSwitch.setChecked(true);
            }else
            {
                if(group.getMovementWaitTime() == 0)
                {
                    stopWaitTime.setText("");
                }
                if(group.getStopPeriodicDelay() == 0)
                {
                    stopPeriodicTime.setText("");
                }
            }
            deleteGroupButton.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View v)
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
            Intent intent = new Intent(GroupConfigurationActivity.this,
                                       ContactManagementActivity.class);
            intent.putExtra("GroupId", "" + group.getId());
            startActivity(intent);
        }


    private void deleteGroup()
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.confirm_delete_group)
                               + group.getName());
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(final DialogInterface dialog, final int which)
                    {
                        final DatabaseHandler db =
                                new DatabaseHandler(GroupConfigurationActivity.this);
                        db.deleteGroup(group);
                        final Intent intent = new Intent(GroupConfigurationActivity.this,
                                                         GroupManagementActivity.class);
                        startActivity(intent);
                    }
            });
            //noinspection AnonymousInnerClassMayBeStatic
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(final DialogInterface dialog, final int which)
                    {
                        dialog.dismiss();
                    }
            });
            final AlertDialog deleteGroupDialog = builder.create();
            deleteGroupDialog.show();
        }


    private void SaveGroup()
        {
            //check if there are contacts in. Contacts are required
            if("".equals(groupNameEdit.getText().toString().trim()))
            {
                groupNameEdit.setError("Group Name Is Required");
                groupNameEdit.setText(" ");  //contactCount
            }else
            {
                if(periodicInputIsAcceptable() && stoppedMovementPeriodicInputIsAcceptable() &&
                   movementMessagingDelayInputIsAcceptable())
                {
                    group.setName(groupNameEdit.getText().toString().trim());
                    final DatabaseHandler db = new DatabaseHandler(this);
                    db.addGroup(group);
                    startActivity(new Intent(GroupConfigurationActivity.this,
                                             GroupManagementActivity.class));
                }
            }
        }


    private boolean periodicInputIsAcceptable()
        {
            boolean output = true;
            try
            {
                final String input = MIN.matcher(periodicTime.getText().toString())
                                             .replaceAll(Matcher.quoteReplacement(""));
                final int delay = "".equals(input) ? 0 : Integer.parseInt(input);
                group.setPeriodicDelay(delay, periodicUpdatesSwitch.isChecked());
            }catch(NumberFormatException _e)
            {
                periodicTime.setError("Enter a whole number");
                output = false;
            }
            return output;
        }


    private boolean stoppedMovementPeriodicInputIsAcceptable()
        {
            boolean output = true;
            try
            {
                final String stop = MIN.matcher(stopPeriodicTime.getText().toString())
                                            .replaceAll(Matcher.quoteReplacement(""));
                final int stopDelay = "".equals(stop) ? 0 : Integer.parseInt(stop);
                group.setStopPeriodicDelay(stopDelay, moveUpdateSwitch.isChecked());
            }catch(NumberFormatException _e)
            {
                stopPeriodicTime.setError("Whole Numbers only");
                output = false;
            }
            return output;
        }


    private boolean movementMessagingDelayInputIsAcceptable()
        {
            boolean output = true;
            try
            {
                final String stopWait = MIN.matcher(stopWaitTime.getText().toString())
                                                .replaceAll(Matcher.quoteReplacement(""));
                final int waitDelay = "".equals(stopWait) ? 0 : Integer.parseInt(stopWait);
                group.setMovementWaitTime(waitDelay, moveUpdateSwitch.isChecked());
            }catch(NumberFormatException _e)
            {
                stopWaitTime.setError("Whole Numbers only");
                output = false;
            }
            return output;
        }
}
