package com.adhar_problem;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class AddressUpdate extends AppCompatActivity {
    EditText landlord_uid,address;
    android.widget.Button addressUpdate,cancelRequest;
    TextView msg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_address_update);
        landlord_uid=findViewById(R.id.landlord_uid);
        address=findViewById(R.id.new_address);
        addressUpdate=findViewById(R.id.addressUpdate);
        cancelRequest=findViewById(R.id.cancel_update_request);
        msg=findViewById(R.id.message);
        Intent intent=getIntent();
        String UID=intent.getStringExtra("UID");


        addressUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String landlordUid=getInput(landlord_uid);
                String Address=getInput(address);
                requestAddressUpdate(UID,landlordUid,Address);

            }
        });

    }


    public String getInput(EditText edit){

        String input=edit.getText().toString();
        return input;

    }

    public  void requestAddressUpdate(String UID,String landlord_uid,String newAddress){
        RequestQueue queue = Volley.newRequestQueue(this);
        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("uid", UID);
        params.put("landlord_uid", landlord_uid);
        params.put("address_new", newAddress);


        String url ="https://api96.herokuapp.com/sendApproverequest";


        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.i("Response",response.getString("uid"));
                            Log.i("Request","Send Request suceessfull");
                            msg.setText("Your Address Updation Request sended");
                            msg.setVisibility(View.VISIBLE);





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @SuppressLint("LongLogTag")
            @Override
            public void onErrorResponse(VolleyError error) {
                msg.setText("Something went Wrong");
                msg.setVisibility(View.VISIBLE);


                Toast.makeText(getApplicationContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                Log.e("request_address_update_err","SOmething went wrong");
            }

        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

}
}