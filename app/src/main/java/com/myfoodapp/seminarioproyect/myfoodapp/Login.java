package com.myfoodapp.seminarioproyect.myfoodapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myfoodapp.seminarioproyect.myfoodapp.utils.Data;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

//import static android.content.DialogInterface.*;

public class Login extends AppCompatActivity {
    private Button login;
    private Button registerNewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=(Button) findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClient();
            }
        });
        registerNewClient=(Button) findViewById(R.id.btn_registerNewClient);
        registerNewClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rclient=new Intent(getApplicationContext(),RegisterClient.class);
                startActivity(rclient);
            }
        });
    }

    public void loginClient(){
        TextView email=findViewById(R.id.emailCl);
        TextView password= findViewById(R.id.passwordCl);
        AsyncHttpClient client= new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.add("email",email.getText().toString());
        params.add("password",password.getText().toString());

        client.post(Data.HOST+Data.LOGIN,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
                try{
                    String token=response.getString("token");
                    alertDialog.setTitle("RESPONSE SERVER");
                    alertDialog.setMessage("Sesion Iniciada");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                    Data.TOKEN="data "+token;
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
        //email.setText(Data.TOKEN);
    }
}
