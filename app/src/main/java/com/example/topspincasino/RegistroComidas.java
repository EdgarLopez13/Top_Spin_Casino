package com.example.topspincasino;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.topspincasino.Organizadores.Food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegistroComidas extends AppCompatActivity {

    Button BTNlimpiar,BTNcomprar;

    EditText ETplatillo,ETprecio;
    SweetAlertDialog pDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_comidas);

        ETplatillo = findViewById(R.id.etPlatillocomida);
        ETprecio = findViewById(R.id.etCostocomida);

        BTNlimpiar = findViewById(R.id.BTNlimpiarcomida);
        BTNcomprar = findViewById(R.id.BTNnuevacomida);

        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.statusbar);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);

        pDialog = new SweetAlertDialog(RegistroComidas.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(true);

        preferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        editor = preferences.edit();


        BTNlimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ETplatillo.setText("");
                ETprecio.setText("");
            }
        });

        BTNcomprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar();
            }
        });
    }

    public void validar(){
        if(Objects.equals(ETplatillo.getText().toString(),"")){

            new SweetAlertDialog(RegistroComidas.this,
                    SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("CAMPO VACIO!!")
                    .setContentText("Favor De Registrar Nombre Del Platillo")
                    .show();

        } else if(Objects.equals(ETprecio.getText().toString(),"")){

            new SweetAlertDialog(RegistroComidas.this,
                    SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("CAMPO VACIO!!")
                    .setContentText("Favor De Registrar El Precio Del Platillo")
                    .show();

        } else{
            RegistrarComida(ETplatillo.getText().toString(),ETprecio.getText().toString());


        }
    }

    public void RegistrarComida(String nombre,String precio) {

        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://topspincasino.000webhostapp.com/TopSpin_Cocina/agregar_comida_nueva.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    pDialog.dismiss();
                    if (Objects.equals(response, "Registro Exitoso")) {
                        new SweetAlertDialog(RegistroComidas.this,
                                SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Registrado!!")
                                .setContentText("Se A Registrado El Nuevo Platillo")
                                .show();
                        ETplatillo.setText("");
                        ETprecio.setText("");

                    } else if (Objects.equals(response, "Comida Existente")) {
                        new SweetAlertDialog(RegistroComidas.this,
                                SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("COMIDA EXISTENTE!!!")
                                .setContentText("Esta Comida Ya Ha Sido Registrada")
                                .show();
                        ETplatillo.setText("");
                        ETprecio.setText("");
                    }
                    else {
                        new SweetAlertDialog(RegistroComidas.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Algo Salio Mal..")
                                .setContentText("No Hemos Podido Registrar La Comida...")
                                .show();
                    }
                } catch (Exception e) {
                    pDialog.dismiss();
                    new SweetAlertDialog(RegistroComidas.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Algo Salio Mal..")
                            .setContentText("Ubo Un Fallo En La App... Contacte Con El Equipo De Soporte....")
                            .show();
                }
            }


        }, new com.android.volley.Response.ErrorListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    pDialog.dismiss();

                    new SweetAlertDialog(RegistroComidas.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Algo Salio Mal..")
                            .setContentText("No Hemos Podido Registrar La Comida...")
                            .show();

                } catch (Exception e) {
                    pDialog.dismiss();
                    new SweetAlertDialog(RegistroComidas.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Algo Salio Mal..")
                            .setContentText("Ubo Un Fallo En La App... Contacte Con El Equipo De Soporte....")
                            .show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();


                params.put("nombre", nombre);
                params.put("precio", precio);



                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}