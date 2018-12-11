package com.myfoodapp.seminarioproyect.myfoodapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.myfoodapp.seminarioproyect.myfoodapp.adapters.MenuAdapter;
import com.myfoodapp.seminarioproyect.myfoodapp.items.ItemMenu;
import com.myfoodapp.seminarioproyect.myfoodapp.utils.Methods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.myfoodapp.seminarioproyect.myfoodapp.utils.Data.HOST;
import static com.myfoodapp.seminarioproyect.myfoodapp.utils.Data.ID_RESTAURANT;
import static com.myfoodapp.seminarioproyect.myfoodapp.utils.Data.REGISTER_MENU;

public class RegisterMenu extends AppCompatActivity implements View.OnClickListener {
    //description
    RecyclerView recyclerMenu;
    Button btn_registerMe;
    EditText nameMenu;
    EditText priceMenu;
    ImageView imgMenu;
    Button btn_imgMenu;
    ArrayList<ItemMenu> listData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_menu);
        loadComponets();
    }

    private void loadComponets(){
        btn_registerMe=findViewById(R.id.btn_RegisterMe);
        btn_imgMenu=findViewById(R.id.btn_imgMe);
        nameMenu=findViewById(R.id.nameMe);
        priceMenu=findViewById(R.id.priceMe);
        imgMenu=findViewById(R.id.imgMe);
        btn_registerMe.setOnClickListener(this);
        btn_imgMenu.setOnClickListener(this);

        listData = new ArrayList<>();
        recyclerMenu=findViewById(R.id.reciclerMe);
        recyclerMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        MenuAdapter adapter=new MenuAdapter(this,listData);
        recyclerMenu.setAdapter(adapter);
    }
    public void getData() {
        //cargar datos de la bd
        listData.clear();
        AsyncHttpClient menu = new AsyncHttpClient();

        menu.get(HOST+REGISTER_MENU,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("result");

                    for (int i = 0; i<data.length();i++ ){
                        JSONObject item = data.getJSONObject(i);
                        Double price = item.getDouble("price");
                        String name = item.getString("name");
                        String id = item.getString("_id");
                        String idRestaurant = item.getString("idrestaurant");
                        String picture = "";

                        if(item.has("picture")){
                            picture = item.getString("picture");
                        }
                        Log.i("IMG",item.getString("picture"));
                        listData.add(new ItemMenu(idRestaurant,name, picture,price,id));
                    }

                    loadData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void loadData() {

        recyclerMenu.setLayoutManager(
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));
        MenuAdapter adapter = new MenuAdapter(this, listData);
        recyclerMenu.setAdapter(adapter);

    }

    private void sendData() {

        if (priceMenu.getText().toString().equals("") || nameMenu.getText().toString().equals("")){
            Toast.makeText(this, "Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (path == null || path.equals("") ){
            Toast.makeText(this, "Debe seleccionar una imagen", Toast.LENGTH_SHORT).show();
            return;
        }
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        File file = new File(path);
        try {
            params.put("picture", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.put("restaurant",ID_RESTAURANT);//idRestaurant
        params.put("nombre", nameMenu.getText());
        params.put("precio", priceMenu.getText());

        client.post(HOST+REGISTER_MENU,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String message = response.getString("message");
                    //String id = response.getString("_id");

                    if (message != null) {
                        Toast.makeText(RegisterMenu.this, message, Toast.LENGTH_SHORT).show();
                        path = "";
                        nameMenu.getText().clear();
                        priceMenu.getText().clear();
                        getData();
                    } else {
                        Toast.makeText(RegisterMenu.this, "ERROR", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(RegisterMenu.this, responseString, Toast.LENGTH_LONG).show();
                Log.d("message",responseString);
            }

        });

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_RegisterMe){
            sendData();
        }
        if (v.getId() == R.id.btn_imgMe){
            cargarImagen();
        }
    }
    //DESDE AQUI VA LA PARTE DE LA FOTO
    final int COD_GALERIA=10;
    final int COD_CAMERA=20;
    String path;
    private void cargarImagen() {

        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(RegisterMenu.this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    tomarFotografia();
                }else{
                    if (opciones[i].equals("Cargar Imagen")){
                        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_GALERIA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();

    }
    private void tomarFotografia() {

        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Methods.FileAndPath fileAndPath= Methods.createFile(path);
        File file = fileAndPath.getFile();
        path = fileAndPath.getPath();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            Uri fileuri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
            camera.putExtra(MediaStore.EXTRA_OUTPUT, fileuri);
        } else {
            camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        startActivityForResult(camera, COD_CAMERA);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case COD_GALERIA:
                    Uri imgPath=data.getData();
                    imgMenu.setImageURI(imgPath);
                    path = Methods.getRealPathFromURI(this,imgPath);
                    Toast.makeText(RegisterMenu.this, path, Toast.LENGTH_SHORT).show();
                    break;
                case COD_CAMERA:
                    loadImageCamera();
            }
        }
    }

    private void loadImageCamera() {
        Bitmap img = BitmapFactory.decodeFile(path);
        if(img != null) {
            imgMenu.setImageBitmap(img);

        }
    }
}
