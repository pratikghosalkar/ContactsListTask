package com.test.contactslist.activity.contactlist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.contactslist.R;
import com.test.contactslist.activity.contactdetails.ContactDetailsActivity;
import com.test.contactslist.model.ContactsResponse;
import com.test.contactslist.util.Constants;

import java.util.ArrayList;

/**
 * Created by pratik on 1/15/2018.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {
    Context context;
    private ArrayList<ContactsResponse> contactList;
    View view;

    public ContactsAdapter(Context context, ArrayList<ContactsResponse> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_contact_list, parent, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        holder.txtName.setText(contactList.get(position).getName());
        holder.txtPhone.setText(contactList.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPhone;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPhone = itemView.findViewById(R.id.txtPhone);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (contactList.get(getAdapterPosition()) != null) {
                        Intent intent = new Intent(context, ContactDetailsActivity.class);
                        intent.putExtra(Constants.intent_extra_contact, contactList.get(getAdapterPosition()));
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

}


