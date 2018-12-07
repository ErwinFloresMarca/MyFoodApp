package com.myfoodapp.seminarioproyect.myfoodapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myfoodapp.seminarioproyect.myfoodapp.utils.Data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class RegisterRestaurant extends AppCompatActivity implements OnMapReadyCallback {
    private MapView map;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private TextView street;
    private Button next;
    private LatLng mainposition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_restaurant);
        map = findViewById(R.id.mapView);
        map.onCreate(savedInstanceState);
        map.onResume();
        MapsInitializer.initialize(this);
        map.getMapAsync(this);
        geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        street = findViewById(R.id.streetRe);
        //Button
        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataRest();
            }
        });


    }

    public void sendDataRest () {
        TextView name = findViewById(R.id.nameRe);
        TextView nit = findViewById(R.id.nitRe);
        TextView owner = findViewById(R.id.ownerRe);
        TextView phone = findViewById(R.id.phoneRe);
        TextView street = findViewById(R.id.streetRe);

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("authorization", Data.TOKEN);
        RequestParams params = new RequestParams();
        params.add("name", name.getText().toString());
        params.add("nit", nit.getText().toString());
        params.add("owner", owner.getText().toString());
        params.add("phone", phone.getText().toString());
        params.add("street", street.getText().toString());
        params.add("lat", String.valueOf(mainposition.latitude));
        params.add("log", String.valueOf(mainposition.longitude));

        client.post(Data.REGISTER_RESTAURANT, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                 /*AlertDialog alertDialog = new AlertDialog.Builder(RegisterRestaurant.this).create();
                 try {
                     String msn = response.getString("msn");
                     alertDialog.setTitle("RESPONSE SERVER");
                     alertDialog.setMessage(msn);
                     alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK",new DialogInterface.OnClickListener(){

                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             dialog.dismiss();
                         }
                     });
                     alertDialog.show();
                 }catch (JSONException e){
                     e.printStackTrace();
                 }*/

                Intent camera = new Intent(RegisterRestaurant.this, CameraPhoto.class);
                RegisterRestaurant.this.startActivity(camera);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //-19.5597641,-65.7633884
        //-19.5730936,-65.7559122
        LatLng potosi = new LatLng(-19.5730936, -65.7559122);
        mainposition = potosi;
        mMap.addMarker(new MarkerOptions().position(potosi).title("Lugar").zIndex(18).draggable(true));
        mMap.setMinZoomPreference(15);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(potosi));
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                mainposition = marker.getPosition();
                String street_string = getStreet(marker.getPosition().latitude, marker.getPosition().longitude);
                street.setText(street_string);
            }
        });
    }

    public String getStreet (Double lat, Double log) {
        List<Address> addresses;
        String result = "";
        try {
            addresses = geocoder.getFromLocation(lat, log, 1);
            result += addresses.get(0).getThoroughfare();

            /*for (int i = 0; i < addresses.size(); i++) {
                if (addresses.get(i).getThoroughfare()!=null)
                result += addresses.get(i).getThoroughfare() + ",";
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }
}
