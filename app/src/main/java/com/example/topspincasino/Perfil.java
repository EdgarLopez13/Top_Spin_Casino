package com.example.topspincasino;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Perfil extends AppCompatActivity {

    Button btnlogin, BTNcambiardatos;
    SweetAlertDialog pDialog;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    EditText ETnombre, ETusuario, ETapellido, ETcontraseña, ETpuesto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        preferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        editor = preferences.edit();

        pDialog = new SweetAlertDialog(Perfil.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(true);

        btnlogin = findViewById(R.id.btnLogin);
        BTNcambiardatos = findViewById(R.id.BTNcambiardatos);

        ETapellido = findViewById(R.id.ETApellidoUsuario);
        ETcontraseña = findViewById(R.id.ETContraseñaUsuario);
        ETusuario = findViewById(R.id.ETUsuario);
        ETnombre = findViewById(R.id.ETnombreusuario);
        ETpuesto = findViewById(R.id.ETPuestoUsuario);


        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.statusbar);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);

        Sesion();


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                boolean sesion = false;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("sesion_usuario", sesion);
                editor.commit();
                startActivity(new Intent(getApplicationContext(), Login_2.class));
                finish();
            }
        });

        BTNcambiardatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CambiarDatos(ETcontraseña.getText().toString());
            }
        });

    }

    public void Sesion() {

//cuadro de dialogo

        pDialog.show();


        StringRequest request = new StringRequest(Request.Method.GET, "https://topspincasino.000webhostapp.com/TopSpin_Cocina/perfil.php?id=" + preferences.getLong("id", 0L), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();


                try {


                    JSONArray array = new JSONArray(response);

                    //LLENA EL ARREGLO CON LOS DATOS DE LA CONSULTA
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject cajas = array.getJSONObject(i);

                        ETnombre.setText(cajas.getString("Nombre"));
                        ETapellido.setText(cajas.getString("Apellido"));
                        ETpuesto.setText(cajas.getString("puesto"));
                        ETusuario.setText(cajas.getString("usuario"));
                        ETcontraseña.setText(cajas.getString("contraseña"));

                    }

                } catch (JSONException e) {
                    new SweetAlertDialog(Perfil.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Algo Salio Mal..")
                            .setContentText("No Hemos Podido Cargar Sus Datos....")
                            .show();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                new SweetAlertDialog(Perfil.this,
                        SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Ops...Algo Salio Mal..")
                        .setContentText("No Hemos Podido Cargar Sus Datos...")
                        .show();
            }
        }

        );

        RequestQueue requestQueue = Volley.newRequestQueue(Perfil.this);
        requestQueue.add(request);


    }

    public void CambiarDatos(String Contraseña_usuario) {

//cuadro de dialogo
        SweetAlertDialog pDialog = new SweetAlertDialog(Perfil.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(true);
        pDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, "https://topspincasino.000webhostapp.com/TopSpin_Cocina/cambiar_datos.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();


                //validacion de los datos obtenidos
                if (Objects.equals(response, "Cambio Exitoso")) {

                    SweetAlertDialog error = new SweetAlertDialog(Perfil.this,
                            SweetAlertDialog.SUCCESS_TYPE);
                    error.setTitleText("COMPLETADO!!");
                    error.setContentText("CAMBIO COMPLETADO CON EXITO!!!!");
                    error.show();
                } else {
                    new SweetAlertDialog(Perfil.this,
                            SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Ops...Algo Salio Mal..")
                            .setContentText("No Hemos Podido Cambiar Los Datos...")
                            .show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                new SweetAlertDialog(Perfil.this,
                        SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Ops...Algo Salio Mal..")
                        .setContentText("No Hemos Podido Cambiar Los Datos...")
                        .show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", preferences.getLong("id", 0L) + "");
                params.put("password", Contraseña_usuario);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Perfil.this);
        requestQueue.add(request);


    }
}