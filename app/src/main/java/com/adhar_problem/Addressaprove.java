package com.adhar_problem;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Addressaprove extends AppCompatActivity {
    TextView validator_uid,landlord_uid,landlord_address,validator_address,msg;
    android.widget.Button acceptRequest,cancelRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addressaprove);
        validator_address=findViewById(R.id.new_address);
        landlord_address=findViewById(R.id.landlord_address);
        landlord_uid=findViewById(R.id.landlord_uid);
        validator_uid=findViewById(R.id.uid);
        msg=findViewById(R.id.message);

        acceptRequest=findViewById(R.id.addressUpdate);
        cancelRequest=findViewById(R.id.cancel_update_request);

        Intent intent=getIntent();
        String landlordUID=intent.getStringExtra("Uid");
        LandlordDetails(landlordUID);


        acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void LandlordDetails(String UID){
        RequestQueue queue = Volley.newRequestQueue(this);
        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("uid", UID);

        String url ="https://api96.herokuapp.com/landlord_details";


        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.i("Response",response.getString("uid"));




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @SuppressLint("LongLogTag")
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(getApplicationContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                Log.e("request_address_update_err","Something went wrong");
            }

        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


    public void setAcceptRequest(String uid){

    }
}