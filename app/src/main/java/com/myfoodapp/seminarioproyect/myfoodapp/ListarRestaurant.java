package com.myfoodapp.seminarioproyect.myfoodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class ListarRestaurant extends AppCompatActivity {

    private ListView LIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_restaurant);

        loadComponents();

    }
    //
    private void loadComponents() {
        LIST =(ListView)this.findViewById(R.id.listviewRest);
        //LIST.setAdapter(adapter);
    }


}

