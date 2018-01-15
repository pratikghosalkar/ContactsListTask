package com.test.contactslist.api;

import com.test.contactslist.model.ContactsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by pratik on 1/15/2018.
 */

public interface ContactsAPIClient {

    @GET("/users")
    Call<ArrayList<ContactsResponse>> getContacts();
}
