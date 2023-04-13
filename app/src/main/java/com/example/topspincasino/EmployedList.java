package com.example.topspincasino;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.topspincasino.Adapters.AdapterEmployers;
import com.example.topspincasino.Adapters.AdapterFood;
import com.example.topspincasino.Organizadores.Employers;
import com.example.topspincasino.Organizadores.Food;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EmployedList extends AppCompatActivity {


    public Employers employers;

    SweetAlertDialog pDialog;
    SharedPreferences preferences;

    public static ArrayList<Employers> Employedist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employed_list);

        preferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        pDialog = new SweetAlertDialog(EmployedList.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(true);
        pDialog.show();

        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.statusbar);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);
        Recycler();

    }


    private void Recycler() {


           String url = "https://topspincasino.000webhostapp.com/TopSpin_Cocina/Buscar_empleados.php?tipo="+Recursos_Humanos.tipo;



        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    Employedist.clear();

                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject cajas = array.getJSONObject(i);

                        Employedist.add(new Employers(
                                cajas.getLong("Id"),
                                cajas.getString("Nombre")+" "+cajas.getString("Apellido"),
                                cajas.getString("Puesto"),
                                cajas.getDouble("Credito"),
                                cajas.getString("Foto")
                        ));


                    }


                    pDialog.dismiss();
                    if (Objects.equals(response, "[]")) {
                        new SweetAlertDialog(EmployedList.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("No hay empleados..")
                                .setContentText("No se encontraron empleados registrados")
                                .show();
                    } else {
                        AdapterEmployers employersList = new AdapterEmployers(Employedist,EmployedList.this);
                        RecyclerView recyclerView = findViewById(R.id.RV_Empleados);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(EmployedList.this));
                        recyclerView.setAdapter(employersList);
                    }

                } catch (JSONException e) {
                    pDialog.dismiss();
                    new SweetAlertDialog(EmployedList.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Algo Salio Mal..")
                            .setContentText("Ubo Un Fallo En La App... Contacte Con El Equipo De Soporte...." +e)
                            .show();
                }
            }
        },
                new Response.ErrorListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            pDialog.dismiss();

                            new SweetAlertDialog(EmployedList.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Algo Salio Mal..")
                                    .setContentText("No Hemos Podido Cargar Los empleados...")
                                    .show();

                        } catch (Exception e) {
                            pDialog.dismiss();
                            new SweetAlertDialog(EmployedList.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Algo Salio Mal..")
                                    .setContentText("Ubo Un Fallo En La App... Contacte Con El Equipo De Soporte....")
                                    .show();
                        }
                    }
                });

        Volley.newRequestQueue(EmployedList.this).add(stringRequest);

    }

}