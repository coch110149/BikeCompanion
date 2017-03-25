package com.cochrane.clinton.bikecompanion;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ContactConfigurationActivity extends AppCompatActivity
{
    private Contact mContact;
    private String mGroupId;
    private EditText mContactName;
    private EditText mPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_contact_configuration);
            final Bundle bundle = getIntent().getExtras();
            if(bundle != null)
            {
                mContact = bundle.getParcelable("SelectedContactObject");
                mGroupId = bundle.getString("GroupId");
            }
            if(mContact == null){mContact = new Contact();}
            mContactName = (EditText) findViewById(R.id.contact_name);
            mContactName.setText(mContact.getName());
            mPhoneNumber = (EditText) findViewById(R.id.contact_phone);
            mPhoneNumber.setText(mContact.getPhoneNumber());
            final Button saveContact = (Button) findViewById(R.id.save_contact);
            saveContact.setText(R.string.save_contact);
            final Button deleteContact = (Button) findViewById(R.id.delete_contact);
            deleteContact.setText(R.string.delete_contact);
            final Button manageGroups = (Button) findViewById(R.id.manage_groups);
            deleteContact.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View v)
                    {
                        deleteContact();
                    }
            });
            saveContact.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(final View v)
                    {
                        saveContact();
                    }
            });
        }


    private void deleteContact()
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.confirm_delete_group)
                               + mContact.getName());
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(final DialogInterface dialog, final int which)
                    {
                        final DatabaseHandler db =
                                new DatabaseHandler(ContactConfigurationActivity.this);
                        db.deleteContact(mContact);
//                        final Intent intent = new Intent(ContactConfigurationActivity.this,
//                                                         GroupManagementActivity.class);
//                        startActivity(intent);
                        finish();
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


    private void saveContact()
        {
            if("".equals(mContactName.getText().toString().trim()))
            {
                mContactName.setError("Name is required");
            }else if("".equals(mPhoneNumber.getText().toString().trim()))
            {
                mPhoneNumber.setError("Phone Number is Required");
            }else
            {
                mContact.setName(mContactName.getText().toString().trim());
                mContact.setPhoneNumber(mPhoneNumber.getText().toString().trim());
                final DatabaseHandler db = new DatabaseHandler(this);
                db.addContact(mContact);
                if(mGroupId != null)
                {
                    mContact.addToGroup(Integer.parseInt(mGroupId), this);
                }
//                startActivity(new Intent(ContactConfigurationActivity.this,
//                                         ContactManagementActivity.class));
                finish();
            }
        }
}
