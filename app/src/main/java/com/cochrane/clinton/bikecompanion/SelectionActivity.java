package com.cochrane.clinton.bikecompanion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class SelectionActivity extends AppCompatActivity
	{
	private ListView listView;
	private ArrayList<?> objects;
	private String typeOfRequest;
	private DatabaseHandler db = new DatabaseHandler(this);
	private Button okayButton;


	@Override
	protected void onCreate( Bundle savedInstanceState )
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_selection);
			typeOfRequest = getIntent().getStringExtra("TypeOfRequest");
			objects = db.getObjects(typeOfRequest);
			SelectionAdapter selectionAdapter = new SelectionAdapter(this, objects);
			listView = (ListView) findViewById(R.id.object_list_view);
			okayButton = (Button) findViewById(R.id.okay_button);
			listView.setAdapter(selectionAdapter);
			if(typeOfRequest.equals("Bike"))
			{
				bikeSelection();
			} else if(typeOfRequest.equals("Group"))
			{
				groupSelection();
			}
		}


	private void bikeSelection()
		{
			okayButton.setVisibility(View.GONE);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
				{
					@Override
					public void onItemClick( AdapterView<?> parent, View view, int position, long id )
						{
							Intent intent = new Intent();
							Bike bike = (Bike) objects.get(position);
							intent.setData(Uri.parse(Integer.toString(bike.getID())));
							setResult(RESULT_OK, intent);
							finish();
						}
				});
		}


	private void groupSelection()
		{
			okayButton.setVisibility(View.VISIBLE);
			okayButton.setText("All Done");
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
				{
					@Override
					public void onItemClick( AdapterView<?> parent, View view, int position, long id )
						{
							Group group = (Group) objects.get(position);
							group.swapSelected();
							//db.updateGroup(groups.get(position));
						}
				});
			okayButton.setOnClickListener(new View.OnClickListener()
				{
					@Override public void onClick( View v )
						{
							Intent intent = new Intent();
							objects = (ArrayList<Group>) db.getAllGroups(true);
							Bundle bundle = new Bundle();
							bundle.putSerializable("activated groups", objects);
							intent.putExtras(bundle);
							setResult(RESULT_OK, intent);
							finish();
						}
				});
		}
	}
