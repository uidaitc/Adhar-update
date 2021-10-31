package com.adhar_problem;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ImageView captcha_img;
    EditText uidNumber,captchaInput;
    ProgressBar progressBar_of_send_otp;
    android.widget.Button sendOtp;
    String TxnId,chaptchaTxnId;
    String otpTxnId;
    TextView msgBox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //hide action bar
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }

        captcha_img=findViewById(R.id.captcha_img);
        uidNumber=findViewById(R.id.getuidnumber);
        captchaInput=findViewById(R.id.captcha_input);
        progressBar_of_send_otp=findViewById(R.id.progressBarOfMain);
        sendOtp=findViewById(R.id.sendotp);
        msgBox=findViewById(R.id.message);
        msgBox.setVisibility(View.INVISIBLE);


        //captcha img load
        getCaptchaImg();

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               String uid_number=getInput(uidNumber);
               String captcha_Input=getInput(captchaInput);
               TxnId="7b47ae3d-7fa5-43cd-8dea-b32d7a4aca35" ;
               Log.i("TxnId","Txn id="+TxnId);
               Log.i("cp_value",captcha_Input);
               sendOtp(uid_number,chaptchaTxnId,captcha_Input,TxnId);






            }
        });



    }


    //captcha from api
    public void getCaptchaImg(){

        RequestQueue queue = Volley.newRequestQueue(this);
        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("langCode", "en");
        params.put("captchaLength", "3");
        params.put("captchaType", "2");


        String url ="https://stage1.uidai.gov.in/unifiedAppAuthService/api/v2/get/captcha";


        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            String url=response.getString("captchaBase64String");
                            StringToImg(url);

                            chaptchaTxnId=response.getString("captchaTxnId");
                            Log.i("cp",chaptchaTxnId);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                Log.e("chaptcha_err","SOmething went wrong");
            }

        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);



    }

    public String getInput(EditText edit){

        String input=edit.getText().toString();
        return input;

    }

    private void StringToImg(String s) {

        String base64String = s;

        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        captcha_img.setImageBitmap(decodedByte);


    }

    public  void sendOtp(String uidNumber,String cpTxnId,String captchaValue,String transactionId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("uidNumber", uidNumber);
        params.put("captchaTxnId", cpTxnId);
        params.put("captchaValue", captchaValue);
        params.put("transactionId", "MYAADHAAR:"+transactionId);



        String url = "https://stage1.uidai.gov.in/unifiedAppAuthService/api/v2/generate/aadhaar/otp";


        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.i(TAG, "status: "+response.getString("status"));

                          otpTxnId=response.getString("txnId");

                            Log.i(TAG, "OtpTxnId: "+otpTxnId);

                            msgBox.setVisibility(View.VISIBLE);
                            msgBox.setText("OTP generated Successfully");
                            Toast.makeText(getApplicationContext(), "OTP generated Successfully", Toast.LENGTH_SHORT).show();



                            //start second screen

                            Intent otpActivity = new Intent(MainActivity.this, otpActivity.class);
                            otpActivity.putExtra("uid_number",uidNumber);
                            otpActivity.putExtra("TxnId",otpTxnId);

                            startActivity(otpActivity);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error in OTP generation");
                msgBox.setVisibility(View.VISIBLE);
                msgBox.setText("Error in OTP generation");

                Toast.makeText(getApplicationContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();

            }
        }){    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.i("headers_in_request","Headers");
                Map<String, String> params1 = new HashMap<String, String>();
                params1.put("appid", "MYAADHAAR");
                params1.put("content-type", "application/json");
                params1.put("x-request-id",TxnId);
                params1.put("Accept-Language","en_in");

                return params1;
            }
        };
        queue.add(stringRequest);
    }

}