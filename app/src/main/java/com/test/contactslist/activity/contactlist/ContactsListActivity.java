package com.test.contactslist.activity.contactlist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.test.contactslist.R;
import com.test.contactslist.api.ContactsAPIClient;
import com.test.contactslist.model.ContactsResponse;
import com.test.contactslist.network.ServiceGenerator;
import com.test.contactslist.util.Constants;
import com.test.contactslist.util.HelperMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private FloatingActionButton fabSort;
    ContactsAPIClient contactsAPIClient;
    ContactsAdapter contactsAdapter;
    boolean isAscending = false;
    ArrayList<ContactsResponse> contactsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacst_list);

        initViews();

        contactsAPIClient = ServiceGenerator.createService(ContactsAPIClient.class);

        if (savedInstanceState != null) {
            contactsList = savedInstanceState.getParcelableArrayList(Constants.intent_extra_contact_list);
            setAdapter();
        } else {
            checkInternetAndFetchContacts();
        }
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabSort = (FloatingActionButton) findViewById(R.id.fab);
        fabSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortContactsList();
                contactsAdapter.notifyDataSetChanged();
            }
        });

        recyclerView = findViewById(R.id.recyclerContact);
        LinearLayoutManager llManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llManager);
    }

    private void checkInternetAndFetchContacts() {
        if (HelperMethods.isConnected(this)) {
            fetchContactsList();
        } else {
            showAlert();
        }
    }

    private void showAlert() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(ContactsListActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(getString(R.string.no_internet_available));
        builder.setPositiveButton(getString(R.string.button_try_again), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkInternetAndFetchContacts();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void fetchContactsList() {
        HelperMethods.showDialog(this, getString(R.string.dialog_please_wait));

        Call<ArrayList<ContactsResponse>> contactsResponseCall = contactsAPIClient.getContacts();

        contactsResponseCall.enqueue(new Callback<ArrayList<ContactsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ContactsResponse>> call, Response<ArrayList<ContactsResponse>> response) {
                HelperMethods.hideDialog();
                if (response.errorBody() != null) {
                    Toast.makeText(ContactsListActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                } else if (response.body() != null) {
                    contactsList = response.body();
                    sortContactsList();
                    setAdapter();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ContactsResponse>> call, Throwable t) {
                HelperMethods.hideDialog();
                Toast.makeText(ContactsListActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter() {
        contactsAdapter = new ContactsAdapter(this, contactsList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(contactsAdapter);
    }

    private void sortContactsList() {

        if (isAscending) {
            Collections.sort(contactsList, new Comparator<ContactsResponse>() {
                @Override
                public int compare(ContactsResponse contactsResponse, ContactsResponse t1) {
                    return t1.getName().compareToIgnoreCase(contactsResponse.getName());
                }
            });
            isAscending = false;
        } else {
            Collections.sort(contactsList, new Comparator<ContactsResponse>() {
                @Override
                public int compare(ContactsResponse contactsResponse, ContactsResponse t1) {
                    return contactsResponse.getName().compareToIgnoreCase(t1.getName());
                }
            });
            isAscending = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacst_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            isAscending = false;
            checkInternetAndFetchContacts();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.intent_extra_contact_list, contactsList);
    }
}
