package com.adhar_problem;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;

public class otpActivity extends AppCompatActivity {
    android.widget.Button confirmOtp;
    EditText otpInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        confirmOtp=findViewById(R.id.confirmOtp);
        otpInput=findViewById(R.id.otpInput);
        Intent intent=getIntent();
        String TxnID=intent.getStringExtra("TxnId");
        String UID=intent.getStringExtra("uid_number");
        String shareCode="1234";


        //Confirm Button event
        confirmOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp_input=otpInput.getText().toString();
                Log.i("TXNID",TxnID);
                ConfirmOtp(otp_input,TxnID,UID,shareCode);


            }
        });
    }

    public void ConfirmOtp(String otp,String TxnId, String Uid, String shareCode){

        RequestQueue queue = Volley.newRequestQueue(this);
        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("txnNumber", TxnId);
        params.put("otp",otp);
        params.put("shareCode",shareCode);
        params.put("uid", Uid);




        String url = "https://stage1.uidai.gov.in/eAadhaarService/api/downloadOfflineEkyc";


        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String eKycXMl=response.getString("eKycXML");
                            String fileName=response.getString("fileName");
                            String status=response.getString("status");
                            Log.i("ekycXML",eKycXMl);
                            Log.i("Status",status);
                            Log.i("fileName",fileName);



                            //start 3rd screen

//                            Intent otpActivity = new Intent(MainActivity.this, otpActivity.class);
//                            otpActivity.putExtra("uid_number",uidNumber);
//                            otpActivity.putExtra("TxnId",otpTxnId);
//
//                            startActivity(otpActivity);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error Something");

                Toast.makeText(getApplicationContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);


    }
}