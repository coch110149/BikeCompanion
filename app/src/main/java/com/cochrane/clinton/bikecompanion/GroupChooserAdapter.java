package com.cochrane.clinton.bikecompanion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by Clint on 17/03/2017.
 */
public class GroupChooserAdapter extends BaseAdapter
	{
	Context mContext;
	LayoutInflater mInflater;
	private List<Group> mGroupsList = null;
	private ArrayList<Group> mGroupArrayList;


	public GroupChooserAdapter( Context context, List<Group> groupsList )
		{
			mContext = context;
			this.mGroupsList = groupsList;
			mInflater = LayoutInflater.from(mContext);
			this.mGroupArrayList = new ArrayList<>();
			this.mGroupArrayList.addAll(mGroupsList);
		}


	@Override public int getCount()
		{
			return mGroupsList.size();
		}


	@Override public Group getItem( int position )
		{
			return mGroupsList.get(position);
		}


	@Override public long getItemId( int position )
		{
			return position;
		}


	@Override public View getView( final int position, View convertView, ViewGroup parent )
		{
			final ViewHolder holder;
			if(convertView == null)
			{
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.group_chooser_list_item, null);
				holder.mGroupNameText = (TextView) convertView.findViewById(R.id.group_name);
				holder.mNoMovementRuleText = (TextView) convertView.findViewById(R.id.no_movement_rule);
				holder.mCheck = (CheckBox) convertView.findViewById(R.id.activateGroupCheckBox);
				holder.mPeriodicText = (TextView) convertView.findViewById(R.id.periodic_interval);
				convertView.setTag(holder);
				convertView.setTag(R.id.group_name, holder.mGroupNameText);
				convertView.setTag(R.id.no_movement_rule, holder.mNoMovementRuleText);
				convertView.setTag(R.id.periodic_interval, holder.mPeriodicText);
				convertView.setTag(R.id.activateGroupCheckBox, holder.mCheck);
//				holder.mCheck.setOnCheckedChangeListener(
//						new CompoundButton.OnCheckedChangeListener()
//							{
//								@Override
//								public void onCheckedChanged( CompoundButton button, boolean isChecked )
//									{
//										int getPosition = (Integer) button.getTag();
//										mGroupsList.get(getPosition).setSelected(button.isChecked());
//									}
//							});
			} else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.mCheck.setTag(position);
			holder.mGroupNameText.setText(mGroupsList.get(position).getName());
			int PeriodicDelay = mGroupsList.get(position).getPeriodicDelay();
			int StopPeriodicDelay = mGroupsList.get(position).getStopPeriodicDelay();
			int MovementWaitTime = mGroupsList.get(position).getMovementWaitTime();
			String output = "";
			if(PeriodicDelay > 0)
			{
				output = mContext.getString(R.string.notify_every) + " " + String.valueOf(PeriodicDelay)
						         + "" + mContext.getString(R.string.minutes);
			} else
			{
				output = "Periodic Alerts Are Turned Off";
			}
			holder.mPeriodicText.setText(output);
			if(StopPeriodicDelay > 0 && MovementWaitTime > 0)
			{
				output = mContext.getString(R.string.notify_every) +
						         " " +
						         String.valueOf(StopPeriodicDelay)
						         +
						         " " +
						         mContext.getString(R.string.noMovement) +
						         " " +
						         String.valueOf(MovementWaitTime) +
						         " " +
						         mContext.getString(R.string.minutes);
			} else
			{
				output = "Stopped Movement Notification Has Been Turned Off";
			}
			holder.mNoMovementRuleText.setText(output);
			holder.mCheck.setChecked(mGroupsList.get(position).isSelected());
			return convertView;
		}


	public void filter( String charText )
		{
			charText = charText.toLowerCase(Locale.getDefault());
			mGroupsList.clear();
			if(charText.length() == 0)
			{
				mGroupsList.addAll(mGroupArrayList);
			} else
			{
				for (Group wp : mGroupArrayList)
				{
					if(wp.getName().toLowerCase(Locale.getDefault()).contains(charText))
					{
						mGroupsList.add(wp);
					}
				}
			}
			notifyDataSetChanged();
		}


	static class ViewHolder
		{
			protected TextView mNoMovementRuleText;
			protected TextView mPeriodicText;
			protected TextView mGroupNameText;
			protected CheckBox mCheck;
		}
	}
