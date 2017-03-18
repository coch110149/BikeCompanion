package com.cochrane.clinton.bikecompanion;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;


public class GroupChooserActivity extends AppCompatActivity
	{
	@Override protected void onCreate( @Nullable Bundle savedInstanceState )
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_group_chooser);
			final DatabaseHandler db = new DatabaseHandler(this);
			final ArrayList<Group> groups = (ArrayList<Group>) db.getAllGroups();
			GroupChooserAdapter groupChooserAdapter = new GroupChooserAdapter(this, groups);
			final ListView listView = (ListView) findViewById(R.id.list_view_group);
			Button okayButton = (Button) findViewById(R.id.okay_button);
			listView.setAdapter(groupChooserAdapter);
			final CheckBox activatedGroup = (CheckBox) listView.findViewById(R.id.activateGroupCheckBox);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
				{
					@Override
					public void onItemClick( AdapterView<?> parent, View view, int position, long id )
						{
							groups.get(position).swapSelected();
							db.updateGroup(groups.get(position));
							((CheckBox) listView.findViewById(R.id.activateGroupCheckBox)).setChecked
									                                                               (groups.get(
											                                                               position)
											                                                                .isSelected());
						}
				});
			okayButton.setOnClickListener(new View.OnClickListener()
				{
					@Override public void onClick( View v )
						{
							Intent intent = new Intent();
							ArrayList<Group> trueGroups = (ArrayList<Group>) db.getAllGroups(true);
							Bundle bundle = new Bundle();
							bundle.putSerializable("activated groups", trueGroups);
							intent.putExtras(bundle);
							setResult(RESULT_OK, intent);
							finish();
						}
				});
		}
	}
//	ListView listView;
//	Button buttonOk;
//	ArrayList<Group> groups = new ArrayList<>();
//
//
//	@Override
//	protected void onCreate( Bundle savedInstanceState )
//		{
//			super.onCreate(savedInstanceState);
//			setContentView(R.layout.activity_group_chooser);
//			listView = (ListView) findViewById(R.id.list_view_group);
//			buttonOk = (Button) findViewById(R.id.okay_button);
//			buttonOk.setOnClickListener(new View.OnClickListener()
//				{
//					@Override public void onClick( View v )
//						{
//							getSelectedGroups();
//						}
//				});
//			addGroupsInList();
//		}
//
//
//	private void getSelectedGroups()
//		{
//			StringBuffer sb = new StringBuffer();
//			for (Group group : groups)
//			{
//				if(group.isSelected())
//				{
//					sb.append(group.getName());
//					sb.append(",");
//				}
//			}
//			String s = sb.toString().trim();
//			if(TextUtils.isEmpty(s))
//			{
//				Toast.makeText(this, "Select at least one Contact", Toast.LENGTH_SHORT).show();
//			} else
//			{
//				s = s.substring(0, s.length() - 1);
//				Toast.makeText(this, "Selected Contacts : " + s, Toast.LENGTH_SHORT).show();
//			}
//		}
//
//		private void addGroupsInList()
//			{
//
//			}

