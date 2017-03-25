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


public class ContactConfigActivity extends AppCompatActivity
{
    final private DatabaseHandler mDb = new DatabaseHandler(this);
    private EditText mPhoneNumber;
    private Contact mContact;
    private String mGroupId;
    private EditText mName;
    private Resources mRes;


    @Override
    protected void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_contact_configuration);
            mRes = getResources();
            final Bundle bundle = getIntent().getExtras();
            if(bundle != null)
            {
                mGroupId = bundle.getString("GroupId");
                mContact = bundle.getParcelable("SelectedContactObject");
            }
            if(mContact == null){mContact = new Contact();}
            final Button saveContact = (Button) findViewById(R.id.save_contact);
            final Button manageGroups = (Button) findViewById(R.id.manage_groups);
            final Button deleteContact = (Button) findViewById(R.id.delete_contact);
            saveContact.setText(R.string.save_contact);
            deleteContact.setText(R.string.delete_contact);
            mName = (EditText) findViewById(R.id.contact_name);
            mPhoneNumber = (EditText) findViewById(R.id.contact_phone);
            mName.setText(mRes.getString(R.string.contact_name, mContact.getName()));
            mPhoneNumber.setText(mRes.getString(R.string.contact_name, mContact.getPhoneNumber()));
            deleteContact.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(final View v)
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
            builder.setMessage(mRes.getString(R.string.confirm_delete, mContact.getName()));
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(final DialogInterface dialog, final int which)
                    {
                        mDb.deleteContact(mContact);
                        startActivity(new Intent(ContactConfigActivity.this,
                                                 ContactManagementActivity.class));
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
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }


    private void saveContact()
        {
            if("".equals(mName.getText().toString().trim()))
            {
                mName.setError("Name is required");
            }else if("".equals(mPhoneNumber.getText().toString().trim()))
            {
                mPhoneNumber.setError("Phone Number is Required");
            }else
            {
                mContact.setName(mName.getText().toString().trim());
                mContact.setPhoneNumber(mPhoneNumber.getText().toString().trim());
                mDb.addContact(mContact);
                if(mGroupId != null){mContact.addToGroup(Integer.parseInt(mGroupId), this);}
                finish();
            }
        }
}
