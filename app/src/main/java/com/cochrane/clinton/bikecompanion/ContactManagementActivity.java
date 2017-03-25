package com.cochrane.clinton.bikecompanion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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


    @Override protected void onResume()
        {
            final DatabaseHandler db = new DatabaseHandler(this);
            final String groupId = getIntent().getStringExtra("GroupId");
            final ListView listView = (ListView) findViewById(R.id.list_view_contact);
            final ArrayList<Contact> contacts = (ArrayList<Contact>) db.getAllContacts();
            final ContactAdapter contactAdapter = (groupId == null) ?
                                                  new ContactAdapter(this, contacts) :
                                                  new ContactAdapter(this, contacts, groupId);
            listView.setAdapter(contactAdapter);
            (findViewById(R.id.add_contact)).setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(final View v)
                    {
                        final Intent intent = new Intent(ContactManagementActivity.this,
                                                         ContactConfigActivity.class);
                        intent.putExtra("GroupId", groupId);
                        startActivity(intent);
                    }
            });
            db.close();
            super.onResume();
        }
}
