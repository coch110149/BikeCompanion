package com.cochrane.clinton.bikecompanion;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Clint on 24/03/2017.
 */
public class ContactAdapter extends ArrayAdapter<Contact>
{
    private int mGroupId = -1;
    private Button mPrimaryButton;


    ContactAdapter(final Activity context, final ArrayList<Contact> contacts)
        {
            super(context, 0, contacts);
        }


    ContactAdapter(final Activity context, final ArrayList<Contact> contacts, String _groupId)
        {
            super(context, 0, contacts);
            mGroupId = Integer.parseInt(_groupId);
        }


    @NonNull @Override public View getView(int position, @Nullable View convertView,
                                           @NonNull ViewGroup parent)
        {
            View listItemView = convertView;
            if(listItemView == null)
            {
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.contact_management_list_item, parent, false);
            }
            final Contact currentContact = getItem(position);
            if(currentContact != null)
            {
                final TextView contactName = (TextView)
                                                     listItemView.findViewById(R.id.contact_name);
                contactName.setText(currentContact.getName());
                final TextView contactPhone = (TextView)
                                                      listItemView.findViewById(R.id.contact_phone);
                contactPhone.setText(currentContact.getPhoneNumber());
                final Button secondaryButton = (Button) listItemView.findViewById(
                        R.id.secondary_button);
                secondaryButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override public void onClick(View v)
                        {
                            final Intent intent = new Intent(getContext(),
                                                             ContactConfigurationActivity.class);
                            intent.putExtra("SelectedContactObject", currentContact);
                            getContext().startActivity(intent);
                        }
                });
                mPrimaryButton = (Button) listItemView.findViewById(R.id.primary_button);
                setUpButtons(currentContact);
            }
            return listItemView;
        }


    private void setUpButtons(final Contact currentContact)
        {
            if(mGroupId != -1)
            {
                final View view = (View) mPrimaryButton.getParent();
                if(currentContact.in(mGroupId, getContext()))
                {
                    mPrimaryButton.setText("remove");
                    view.setBackgroundColor(view.getResources().getColor(R.color.colorPrimary));
                }else
                {
                    mPrimaryButton.setText("add");
                }
                mPrimaryButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override public void onClick(View v)
                        {
                            if("add".equals(mPrimaryButton.getText().toString().toLowerCase()))
                            {
                                currentContact.addToGroup(mGroupId, getContext());
                                mPrimaryButton.setText("remove");
                                view.setBackgroundColor(
                                        view.getResources().getColor(R.color.colorPrimary));
                            }else
                            {
                                currentContact.removeFromGroup(mGroupId, getContext());
                                mPrimaryButton.setText("add");
                                view.setBackgroundColor(0);
                            }
                        }
                });
            }else
            {
                mPrimaryButton.setText("manage groups");
                mPrimaryButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override public void onClick(View v)
                        {
                            final Intent intent =
                                    new Intent(getContext(), SelectionActivity.class);
                            intent.putExtra("TypeOfRequest", "Group");
                            intent.putExtra("ContactId", "" + currentContact.getId());
                            getContext().startActivity(intent);
                        }
                });
            }
        }
}
