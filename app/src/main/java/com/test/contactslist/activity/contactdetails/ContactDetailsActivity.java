package com.test.contactslist.activity.contactdetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.test.contactslist.R;
import com.test.contactslist.model.ContactsResponse;
import com.test.contactslist.util.Constants;

public class ContactDetailsActivity extends AppCompatActivity {
    TextView username;
    TextView emailId;
    TextView phoneNo;
    TextView website;
    TextView companyName;
    TextView suite;
    TextView street;
    TextView city;
    TextView zipcode;
    TextView geo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ContactsResponse contactsResponse = getIntent().getParcelableExtra(Constants.intent_extra_contact);

        setTitle(contactsResponse.getName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        setUserData(contactsResponse);
    }

    private void initViews() {
        username = findViewById(R.id.tv_user_name);
        emailId = findViewById(R.id.tv_email_id);
        phoneNo = findViewById(R.id.tv_phone_no);
        website = findViewById(R.id.tv_website);
        companyName = findViewById(R.id.tv_company_name);
        suite = findViewById(R.id.tv_suite);
        street = findViewById(R.id.tv_street);
        city = findViewById(R.id.tv_city);
        zipcode = findViewById(R.id.tv_zipcode);
        geo = findViewById(R.id.tv_geo);
    }

    private void setUserData(final ContactsResponse contactsResponse) {
        username.setText(contactsResponse.getUsername());
        emailId.setText(contactsResponse.getEmail());
        phoneNo.setText(contactsResponse.getPhone());
        website.setText(contactsResponse.getWebsite());
        companyName.setText(contactsResponse.getCompany().getName());
        suite.setText("Suite - " + contactsResponse.getAddress().getSuite());
        street.setText("Street - " + contactsResponse.getAddress().getStreet());
        city.setText("City - " + contactsResponse.getAddress().getCity());
        zipcode.setText("ZipCode - " + contactsResponse.getAddress().getZipcode());

        SpannableString spanStr = new SpannableString(getString(R.string.check_location));
        spanStr.setSpan(new UnderlineSpan(), 0, spanStr.length(), 0);
        geo.setText(spanStr);

        geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri mapUri = Uri.parse("geo:0,0?q=" + contactsResponse.getAddress().getGeo().getLat() + "," + contactsResponse.getAddress().getGeo().getLng());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
