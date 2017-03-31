package com.cochrane.clinton.bikecompanion;

import android.app.Activity;
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


/**
 * Created by Clint on 24/03/2017.
 */
class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder>
{
    private final OnItemClickListener mListener;
    private String mGroupId;
    private Activity mActivity;
    private ArrayList<Contact> mContacts;


    public ContactAdapter(ArrayList<Contact> _contacts, Activity _activity,
                          OnItemClickListener _listener)
        {
            mContacts = _contacts;
            mActivity = _activity;
            mListener = _listener;
        }


    public void addGroupId(String _groupId)
        {
            mGroupId = _groupId;
            ///// TODO: 30/03/2017 do things with the group
        }


    @Override public ContactAdapter.ContactHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View inflatedView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.contact_management_list_item, parent, false);
            return new ContactHolder(inflatedView);
        }


    @Override public void onBindViewHolder(ContactAdapter.ContactHolder holder, int position)
        {
            Contact contact = mContacts.get(position);
            holder.bindContact(contact, mGroupId, mListener);
        }


    @Override public int getItemCount()
        {
            return mContacts.size();
        }


    public interface OnItemClickListener
    {
        void onItemClick(Contact _contact);
    }



    public static class ContactHolder extends RecyclerView.ViewHolder
    {
        private TextView mName;
        private TextView mPhone;
        private Button mPrimaryButton;
        private Button mSecondaryButton;
        private Context mContext;
        private Resources mResources;
        private OnItemClickListener mListener;
        private String mGroupID;
        private Contact mContact;


        public ContactHolder(final View _view)
            {
                super(_view);
                mName = (TextView) _view.findViewById(R.id.contact_name);
                mPhone = (TextView) _view.findViewById(R.id.contact_phone);
                mPrimaryButton = (Button) _view.findViewById(R.id.primary_button);
                mSecondaryButton = (Button) _view.findViewById(R.id.secondary_button);
            }


        public void bindContact(final Contact _contact, final String _groupId,
                                final OnItemClickListener _listener)
            {
                mListener = _listener;
                mGroupID = _groupId;
                mContact = _contact;
                mContext = itemView.getContext();
                mResources = mContext.getResources();
                mName.setText(mResources.getString(R.string.contact_name, _contact.getName()));
                mPhone.setText(mResources.getString(R.string.phone_number, _contact.getNumber()));
                mPrimaryButton.setText(mResources.getString(R.string.edit_exact));
                mPrimaryButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override public void onClick(View v)
                        {
                            final Intent intent = new Intent(mContext, ContactConfigActivity.class);
                            intent.putExtra("SelectedContactObject", mContact);
                            mContext.startActivity(intent);
                        }
                });
                if(_groupId != null)
                {
                    mSecondaryButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override public void onClick(View v)
                            {
                                mListener.onItemClick(mContact);
                                if(_contact.in(Integer.parseInt(_groupId), mContext))
                                {
                                    mSecondaryButton
                                            .setText(mResources.getString(R.string.remove_exact));
                                }else
                                {
                                    mSecondaryButton
                                            .setText(mResources.getString(R.string.add_exact));
                                }
                            }
                    });
                    if(_contact.in(Integer.parseInt(_groupId), mContext))
                    {
                        mSecondaryButton
                                .setText(mResources.getString(R.string.remove_exact));
                    }else
                    {
                        mSecondaryButton
                                .setText(mResources.getString(R.string.add_exact));
                    }
                }else
                {
                    mSecondaryButton.setText(mResources.getString(R.string.manage_groups_exact));
                    mSecondaryButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override public void onClick(View v)
                            {
                                final Intent intent =
                                        new Intent(mContext, SelectionActivity.class);
                                intent.putExtra("TypeOfRequest", "Group");
                                intent.putExtra("ContactId", (String.valueOf(_contact.getId())));
                                mContext.startActivity(intent);
                            }
                    });
                }
            }
    }
}
//    ContactAdapter(final Activity context, final ArrayList<Contact> contacts)
//        {
//            super(context, 0, contacts);
//            mContext = context;
//            mRes = mContext.getResources();
//        }
//
//
//    ContactAdapter(final Activity context, final ArrayList<Contact> contacts, final String
// _groupId)
//        {
//            super(context, 0, contacts);
//            mGroupId = Integer.parseInt(_groupId);
//            mContext = context;
//            mRes = mContext.getResources();
//        }
//
//
//    @NonNull @Override public View getView(final int position, @Nullable final View convertView,
//                                           @NonNull final ViewGroup parent)
//        {
//            View listItemView = convertView;
//            if(listItemView == null)
//            {
//                listItemView = LayoutInflater.from(getContext()).inflate(
//                        R.layout.contact_management_list_item, parent, false);
//            }
//            final Contact currentContact = getItem(position);
//            if(currentContact != null)
//            {
//                final TextView contactName = (TextView)
//                                                     listItemView.findViewById(R.id.contact_name);
//                contactName.setText(currentContact.getName());
//                final TextView contactPhone = (TextView)
//                                                      listItemView.findViewById(R.id
// .contact_phone);
//                contactPhone.setText(currentContact.getPhoneNumber());
//                final Button secondaryButton = (Button) listItemView.findViewById(
//                        R.id.secondary_button);
//                secondaryButton.setOnClickListener(new View.OnClickListener()
//                {
//                    @Override public void onClick(final View v)
//                        {
//                            final Intent intent = new Intent(getContext(),
//                                                             ContactConfigActivity.class);
//                            intent.putExtra("SelectedContactObject", currentContact);
//                            getContext().startActivity(intent);
//                        }
//                });
//                mPrimaryButton = (Button) listItemView.findViewById(R.id.primary_button);
//                setUpButtons(currentContact);
//            }
//            return listItemView;
//        }
//
//
//    private void setUpButtons(final Contact currentContact)
//        {
//            if(mGroupId != -1)
//            {
//                final View view = (View) mPrimaryButton.getParent();
//                if(currentContact.in(mGroupId, getContext()))
//                {
//                    mPrimaryButton.setText(mRes.getString(R.string.remove_exact));
//                    view.setBackgroundColor(ContextCompat.getColor(mContext, R.color
// .bright_teal));
//                }else
//                {
//                    mPrimaryButton.setText(mRes.getString(R.string.add_exact));
//                }
//                mPrimaryButton.setOnClickListener(new View.OnClickListener()
//                {
//                    @Override public void onClick(final View v)
//                        {
//                            if("add".equals(mPrimaryButton.getText().toString().toLowerCase()))
//                            {
//                                currentContact.addToGroup(mGroupId, getContext());
//                                mPrimaryButton.setText(mRes.getString(R.string.add_exact));
//                                view.setBackgroundColor(
//                                        ContextCompat.getColor(mContext, R.color.bright_teal));
//                            }else
//                            {
//                                currentContact.removeFromGroup(mGroupId, getContext());
//                                mPrimaryButton.setText((mRes.getString(R.string.add_exact)));
//                                view.setBackgroundColor(0);
//                            }
//                        }
//                });
//            }else
//            {
//                mPrimaryButton.setText(mRes.getString(R.string.manage_groups_exact));
//                mPrimaryButton.setOnClickListener(new View.OnClickListener()
//                {
//                    @Override public void onClick(final View v)
//                        {
//                            final Intent intent =
//                                    new Intent(getContext(), SelectionActivity.class);
//                            intent.putExtra("TypeOfRequest", "Group");
//                            intent.putExtra("ContactId", String.valueOf(currentContact.getId()));
//                            getContext().startActivity(intent);
//                        }
//                });
//            }
//        }
//}
