package com.cochrane.clinton.bikecompanion;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupHolder>
{
    private ArrayList<Group> mGroups;


    public GroupAdapter(ArrayList<Group> _groups)
        {
            mGroups = _groups;
        }


    @Override public GroupAdapter.GroupHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View inflatedView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.group_management_list_item, parent, false);
            return new GroupHolder(inflatedView);
        }


    @Override public void onBindViewHolder(GroupAdapter.GroupHolder holder, int position)
        {
            Group group = mGroups.get(position);
            holder.bindGroup(group);
        }


    @Override public int getItemCount()
        {
            return mGroups.size();
        }


    public static class GroupHolder extends RecyclerView.ViewHolder
    {
        private TextView mStoppedMovingNotification;
        private TextView mPeriodicNotification;
        private TextView mGroupName;
        private Button mEditGroup;
        private Button mTestNotification;


        public GroupHolder(final View _view)
            {
                super(_view);
                mEditGroup = (Button) _view.findViewById(R.id.edit_group);
                mGroupName = (TextView) _view.findViewById(R.id.group_name);
                mTestNotification = (Button) _view.findViewById(R.id.test_notifications);
                mPeriodicNotification = (TextView) _view.findViewById(R.id.periodic_time);
                mStoppedMovingNotification = (TextView) _view.findViewById(
                        R.id.stopped_moving_notification);
            }


        public void bindGroup(final Group _group)
            {
                final Context context = itemView.getContext();
                Resources resources = context.getResources();
                mGroupName.setText(resources.getString(R.string.name, _group.getName()));
                mPeriodicNotification.setText(_group.getPeriodicDelay(resources));
                mStoppedMovingNotification.setText(_group.getStopPeriodicDelay(resources));
                mEditGroup.setOnClickListener(new View.OnClickListener()
                {
                    @Override public void onClick(View v)
                        {
                            context.startActivity(new Intent(context, GroupConfigActivity.class)
                                                          .putExtra("SelectedGroup", _group));
                        }
                });
            }
    }
}
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//
///**
// * Created by Clint on 16/03/2017.
// */
//class GroupAdapter extends ArrayAdapter<Group>
//{
//    final private Context mContext = getContext();
//    final private Resources mRes = mContext.getResources();
//
//
//    GroupAdapter(final Activity context, final ArrayList<Group> groups)
//        {
//            super(context, 0, groups);
//        }
//
//
//    @NonNull @Override
//    public View getView(final int _i, @Nullable final View _view, @NonNull final ViewGroup parent)
//        {
//            View listItemView = _view;
//            if(listItemView == null)
//            {
//                listItemView = LayoutInflater.from(mContext).inflate(
//                        R.layout.group_management_list_item, parent, false);
//            }
//            final Group currentGroup = getItem(_i);
//            if(currentGroup != null)
//            {
//                final TextView periodicTime =
//                        (TextView) listItemView.findViewById(R.id.periodic_time);
//                final Button testNotificationButton =
//                        (Button) listItemView.findViewById(R.id.test_notifications);
//                final TextView stopPeriodicTime =
//                        (TextView) listItemView.findViewById(R.id.stopped_moving_notification);
//                final TextView NameView = (TextView) listItemView.findViewById(R.id.group_name);
//                final Button editGroupButton = (Button) listItemView.findViewById(R.id
// .edit_group);
//                stopPeriodicTime.setText(currentGroup.getStopPeriodicDelay(mRes));
//                periodicTime.setText(currentGroup.getPeriodicDelay(mRes));
//                NameView.setText(currentGroup.getName());
//                testNotificationButton.setOnClickListener(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(final View v)
//                        {
//                            final ArrayList<Group> groups = new ArrayList<>();
//                            groups.add(currentGroup);
//                            final Intent intent = new Intent(getContext(), RBMService.class);
//                            intent.putParcelableArrayListExtra("GroupObject", groups);
//                            mContext.startService(intent);
//                        }
//                });
//                editGroupButton.setOnClickListener(new View.OnClickListener()
//                {
//                    @Override public void onClick(final View v)
//                        {
//                            final Intent intent =
//                                    new Intent(getContext(), GroupConfigActivity.class);
//                            intent.putExtra("SelectedGroupObject", currentGroup);
//                            mContext.startActivity(intent);
//                        }
//                });
//            }
//            return listItemView;
//        }
//}
