package com.example.sony.isa1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;



public class buscar_Activity extends AppCompatActivity {
    private String URL = "https://proyecto2018.herokuapp.com/api_usuarios?user_hash=12345&action=get&";
    private String getProductosURL = "";
    private String queryParams = "";
    private Button btn_buscar;
    private EditText et_id_usuarios;
    private TextView tv_usuario;
    private TextView tv_nombre;
    private TextView tv_direcion;
    private TextView tv_telefono;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        et_id_usuarios = (EditText) findViewById(R.id.et_id_usuarios);
        tv_usuario = (TextView) findViewById(R.id.tv_usuario);
        tv_nombre = (TextView) findViewById(R.id.tv_nombre);
        tv_direcion = (TextView) findViewById(R.id.tv_direcion);
        tv_telefono = (TextView) findViewById(R.id.tv_telefono);

        btn_buscar = (Button) findViewById(R.id.btn_buscar);
        btn_buscar.setOnClickListener(onClickListener);
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btn_buscar)
                btn_buscar_onClick();
        }
    };


    private void btn_buscar_onClick() {
        String id_usuarios = et_id_usuarios.getText().toString();
        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter("id_usuario", id_usuarios);
        queryParams = builder.build().getEncodedQuery();
        getProductosURL = URL;
        getProductosURL += queryParams;
        Log.d("Parametros", queryParams);
        Log.d("Consulta", getProductosURL);
        webServiceRest(getProductosURL);
    }

    private void webServiceRest(String requestURL){
        try{
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            String webServiceResult="";
            while ((line = bufferedReader.readLine()) != null){
                webServiceResult += line;
            }
            bufferedReader.close();
            parseInformation(webServiceResult);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void parseInformation(String jsonResult){
        JSONArray jsonArray = null;
        String id_usuarios;
        String usuarios;
        String nombre;
        String direcion;
        String telefono;

        try{
            jsonArray = new JSONArray(jsonResult);
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (jsonArray != null){
            Log.d("jsonArray ",""+jsonArray.length());
            for(int i=0;i<jsonArray.length();i++){
                try{
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    id_usuarios = jsonObject.getString("id_usuario");
                    usuarios = jsonObject.getString("usuario");
                    nombre = jsonObject.getString("nombre");
                    direcion = jsonObject.getString("direcion");
                     telefono= jsonObject.getString("telefono");

                    tv_usuario.setText(usuarios);
                    tv_nombre.setText(nombre);
                    tv_direcion.setText(direcion);
                    tv_telefono.setText(telefono);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }else{
            tv_usuario.setText("No encontrado");
            tv_nombre.setText("No encontrado");
            tv_direcion.setText("No encontrado");
            tv_telefono.setText("No encontrado");

            Message("Error","Registro no encontrado");
        }
    }

    private void Message(String title, String message){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.show();
    }
}

