package com.cochrane.clinton.bikecompanion;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import static com.cochrane.clinton.bikecompanion.R.id.additionalInfo1;
import static com.cochrane.clinton.bikecompanion.R.id.additionalInfo2;
import static com.cochrane.clinton.bikecompanion.R.id.heading;


class SelectionAdapter extends RecyclerView.Adapter<SelectionAdapter.ObjectHolder>
{
    private final OnItemClickListener mListener;
    private ArrayList<?> mObjects;
    private Activity mContext;
    private Contact mContact;


    public SelectionAdapter(ArrayList<?> _objects, Activity _context, OnItemClickListener _listener)
        {
            mObjects = _objects;
            mContext = _context;
            mListener = _listener;
        }


    public void addContact(Contact _contact)
        {
            mContact = _contact;
        }


    @Override public SelectionAdapter.ObjectHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType)
        {
            View inflatedView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.selection_chooser_list_item, parent, false);
            return new ObjectHolder(inflatedView);
        }


    @Override public void onBindViewHolder(SelectionAdapter.ObjectHolder holder, int position)
        {
            Object object = mObjects.get(position);
            holder.bindObject(object, mContext, mListener, mContact);
        }


    @Override public int getItemCount()
        {
            return mObjects.size();
        }


    public interface OnItemClickListener
    {
        void onItemClick(Group item);
    }



    public static class ObjectHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener
    {
        private TextView mHeading;
        private TextView mAdditionalInfo1;
        private TextView mAdditionalInfo2;
        private Group mGroup;
        private Activity mActivity;
        private Bike mBike;
        private Contact mContact;
        private OnItemClickListener mListener;


        public ObjectHolder(View _view)
            {
                super(_view);
                mHeading = (TextView) _view.findViewById(heading);
                mAdditionalInfo1 = (TextView) _view.findViewById(additionalInfo1);
                mAdditionalInfo2 = (TextView) _view.findViewById(additionalInfo2);
                _view.setOnClickListener(this);
            }


        public void bindObject(Object _o, Activity _Activity, final OnItemClickListener
                                                                      _listener, Contact _contact)
            {
                Resources resources = itemView.getContext().getResources();
                mActivity = _Activity;
                mListener = _listener;
                mContact = _contact;
                try
                {
                    mGroup = (Group) _o;
                    mHeading.setText(mGroup.getHeading(resources));
                    mAdditionalInfo1.setText(mGroup.getPeriodicDelay(resources));
                    mAdditionalInfo2.setText(mGroup.getStopPeriodicDelay(resources));
                    View mHeadingParent = (View) mHeading.getParent();
                    if(mContact != null)
                    {
                        if(mContact.in(mGroup.getId(), itemView.getContext()))
                        {
                            mHeadingParent.setBackgroundColor(0xFF00FF00);
                        }
                    }else
                    {
                        if(mGroup.isSelected())
                        {
                            mHeadingParent.setBackgroundColor(0xFF00FF00);
                        }
                    }
                }catch(final ClassCastException _e)
                {
                    mBike = (Bike) _o;
                    mHeading.setText(mBike.getHeading(resources));
                    mAdditionalInfo1.setText(mBike.getAdditionalInfo1(resources));
                    mAdditionalInfo2.setText(mBike.getAdditionalInfo2(resources));
                }
            }


        @Override public void onClick(View v)
            {
                if(mBike != null)
                {
                    final Intent intent = new Intent();
                    intent.setData(Uri.parse(Integer.toString(mBike.getId())));
                    mActivity.setResult(Activity.RESULT_OK, intent);
                    mActivity.finish();
                }else
                {
                    mListener.onItemClick(mGroup);
                    View mHeadingParent = (View) mHeading.getParent();
                    if(mContact != null)
                    {
                        if(mContact.in(mGroup.getId(), itemView.getContext()))
                        {
                            mHeadingParent.setBackgroundColor(0xFF00FF00);
                        }else
                        {
                            mHeadingParent.setBackgroundColor(0);
                        }
                    }else
                    {
                        if(mGroup.isSelected())
                        {
                            mHeadingParent.setBackgroundColor(0xFF00FF00);
                        }else
                        {
                            mHeadingParent.setBackgroundColor(0);
                        }
                    }
                }
            }
    }
}


