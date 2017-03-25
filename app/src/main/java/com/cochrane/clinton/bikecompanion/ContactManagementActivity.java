package com.cochrane.clinton.bikecompanion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;


public class ContactManagementActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_contact_management);
        }


    @Override protected void onStart()
        {
            Log.d("ContactMangementActivit", "OnStart");
            super.onStart();
        }


    @Override protected void onResume()
        {
            final DatabaseHandler db = new DatabaseHandler(this);
            final ArrayList<Contact> contacts = (ArrayList<Contact>) db.getAllContacts();
            final String mGroupId = getIntent().getStringExtra("GroupId");
            ContactAdapter contactAdapter = (mGroupId == null) ? new ContactAdapter(
                                                                                           this,
                                                                                           contacts) :
                                            new ContactAdapter(this, contacts, mGroupId);
            final ListView listView = (ListView) findViewById(R.id.list_view_contact);
            listView.setAdapter(contactAdapter);
            (findViewById(R.id.add_contact)).setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View v)
                    {
                        final Intent intent = new Intent(ContactManagementActivity.this,
                                                         ContactConfigurationActivity.class);
                        intent.putExtra("GroupId", mGroupId);
                        startActivity(intent);
                    }
            });
            super.onResume();
        }
}
