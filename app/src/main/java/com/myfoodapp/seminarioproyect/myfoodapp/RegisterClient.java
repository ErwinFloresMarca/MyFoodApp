package com.myfoodapp.seminarioproyect.myfoodapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.myfoodapp.seminarioproyect.myfoodapp.utils.Data;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegisterClient extends AppCompatActivity {
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);
        register=findViewById(R.id.btn_registerCl);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataClient();

            }
        });
    }
    private void sendDataClient(){
        TextView firstName=findViewById(R.id.firstNameCl);
        TextView lastName=findViewById(R.id.lastNameCl);
        TextView ci=findViewById(R.id.ciCl);
        TextView email=findViewById(R.id.emailCl);
        TextView phone=findViewById(R.id.phoneCl);
        TextView password=findViewById(R.id.passwordCl);
        TextView confPassword=findViewById(R.id.confPasswordCl);
        if(!password.getText().toString().equals(confPassword.getText().toString())){
            AlertDialog alertDialog = new AlertDialog.Builder(RegisterClient.this).create();
            alertDialog.setTitle("ERROR CONTRASEÑA");
            alertDialog.setMessage("las contraseñas no son identicas");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK",new DialogInterface.OnClickListener(){

                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     dialog.dismiss();
                 }
            });
            alertDialog.show();
        }
        else{
            AsyncHttpClient client=new AsyncHttpClient();
            RequestParams params=new RequestParams();
            params.add("name",firstName.getText().toString());
            //params.add("lastname",lastName.getText().toString());
            params.add("ci",ci.getText().toString());
            params.add("email",email.getText().toString());
            params.add("phone",phone.getText().toString());
            params.add("password",password.getText().toString());
            client.post(Data.HOST + Data.REGISTER_CLIENT, params, new JsonHttpResponseHandler(){

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    if(response.has("_id")){
                        AlertDialog alertDialog = new AlertDialog.Builder(RegisterClient.this).create();
                        alertDialog.setTitle("SERVER RESPONSE");
                        alertDialog.setMessage("Registro Exitoso!!!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK",new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                    else{

                    }
                }
            });
        }
    }
}
