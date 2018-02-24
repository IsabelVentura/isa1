package com.example.sony.isa1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.os.StrictMode;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class lista_activity extends AppCompatActivity {

    private ListView lv_contacts_list;
    private ArrayAdapter adapter;
    private String getAllContactsURL = "https://proyecto2018.herokuapp.com/api_usuarios?user_hash=12345&action=get";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());


        lv_contacts_list = (ListView)findViewById(R.id.lv_contacts_list);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        lv_contacts_list.setAdapter(adapter);
        webServiceRest(getAllContactsURL);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        String id_usuario;
        String usuario;
        String nombre;
        String direcion;
        String telefono;
        try{
            jsonArray = new JSONArray(jsonResult);
        }catch (JSONException e){
            e.printStackTrace();
        }
        for(int i=0;i<jsonArray.length();i++){
            try{
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                id_usuario = jsonObject.getString("id_usuario");
                usuario = jsonObject.getString("usuario");
                nombre = jsonObject.getString("nombre");
                direcion = jsonObject.getString("direcion");
                telefono = jsonObject.getString("telefono");
                adapter.add(id_usuario + ":"  + usuario);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
