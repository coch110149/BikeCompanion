package com.cochrane.clinton.bikecompanion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class GroupManagementActivity extends AppCompatActivity
	{
	@Override
	protected void onCreate( Bundle savedInstanceState )
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_group_management);
			DatabaseHandler db = new DatabaseHandler(this);
			final ArrayList<Group> groups = (ArrayList<Group>) db.getAllGroups();
			GroupAdapter groupAdapter = new GroupAdapter(this, groups);
			ListView listView = (ListView) findViewById(R.id.list_view_group);
			listView.setAdapter(groupAdapter);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
				{
					@Override
					public void onItemClick( AdapterView<?> parent, View view, int position, long id )
						{
							Group group = groups.get(position);
							Intent intent = new Intent(GroupManagementActivity.this,
									                          GroupConfigurationActivity.class);
							intent.putExtra("SelectedGroupObject", group);
							startActivity(intent);
						}
				});
		}
	}
