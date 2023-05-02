package com.example.topspincasino.Estructura_De_Datos;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.topspincasino.Adapters.AdapterEmployers;
import com.example.topspincasino.EmployedList;
import com.example.topspincasino.Login_2;
import com.example.topspincasino.Organizadores.Employers;
import com.example.topspincasino.R;
import com.example.topspincasino.Recursos_Humanos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Employers_Consultas {


    public static void Empleados(ArrayList<Employers> Employedist, Context context) {


        String url = "https://topspincasino.000webhostapp.com/TopSpin_Cocina/Buscar_empleados.php?tipo="+ Recursos_Humanos.tipo;



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


                    EmployedList.pDialog.dismiss();
                    if (Objects.equals(response, "[]")) {
                        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("No hay empleados..")
                                .setContentText("No se encontraron empleados registrados")
                                .show();
                    } else {
                        AdapterEmployers employersList = new AdapterEmployers(Employedist,context);
                        RecyclerView empleados = EmployedList.Empleados_Lista;
                        empleados.setHasFixedSize(true);
                        empleados.setLayoutManager(new LinearLayoutManager(context));
                        empleados.setAdapter(employersList);
                    }

                } catch (JSONException e) {
                    EmployedList.pDialog.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
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
                            EmployedList.pDialog.dismiss();

                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Algo Salio Mal..")
                                    .setContentText("No Hemos Podido Cargar Los empleados...")
                                    .show();

                        } catch (Exception e) {
                            EmployedList.pDialog.dismiss();
                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Algo Salio Mal..")
                                    .setContentText("Ubo Un Fallo En La App... Contacte Con El Equipo De Soporte....")
                                    .show();
                        }
                    }
                });

        Volley.newRequestQueue(context).add(stringRequest);

    }
}
