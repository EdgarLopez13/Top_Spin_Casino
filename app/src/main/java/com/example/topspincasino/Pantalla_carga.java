package com.example.topspincasino;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

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

public class Pantalla_carga extends AppCompatActivity {

    private static ObjectAnimator animacionDesvanecido;
    private static ObjectAnimator animacionRotation;
    ImageView logo;

    String url = "https://topspincasino.000webhostapp.com/TopSpin_Cocina/login.php";
    Long id;
    String Nombre1, Apellido1, Puesto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_carga);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        logo = findViewById(R.id.IVlogocarga);


        animacionDesvanecido = ObjectAnimator.ofFloat(logo, View.ALPHA, 0.0f, 1.0f);
        animacionDesvanecido.setDuration(2500);
        animacionRotation = ObjectAnimator.ofFloat(logo, "y", 400f);
        animacionRotation.setDuration(2500);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animacionDesvanecido, animacionRotation);
        animatorSet.start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                //Redireccionamiento en base al puesto
                if (preferences.getBoolean("sesion_usuario", false) != false) {


                    Validar(preferences.getString("usuario", ""), preferences.getString("password", ""));

                } else {

                    Intent intent = new Intent(Pantalla_carga.this, Login_2.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);

    }

    public void Validar(String Email_usuario, String Contraseña_usuario) {

//cuadro de dialogo
        SweetAlertDialog pDialog = new SweetAlertDialog(Pantalla_carga.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(true);
        pDialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();

                SharedPreferences preferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                try {

                    //validacion de los datos obtenidos
                    if (Objects.equals(response, "Correo Incorrecto")) {

                        SweetAlertDialog error = new SweetAlertDialog(Pantalla_carga.this,
                                SweetAlertDialog.ERROR_TYPE);
                        error.setTitleText("Op...Algo Salio Mal...");
                        error.setContentText("Este Usuario No Es Valido.....");
                        error.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                boolean sesion = false;
                                editor.putBoolean("sesion_usuario", sesion);
                                editor.commit();

                                startActivity(new Intent(getApplicationContext(), Login_2.class));
                                finish();
                            }
                        });
                        error.show();


                    } else if (Objects.equals(response, "Password Incorrecta")) {
                        SweetAlertDialog error = new SweetAlertDialog(Pantalla_carga.this,
                                SweetAlertDialog.ERROR_TYPE);
                        error.setTitleText("Op...Algo Salio Mal...");
                        error.setContentText("La Contraseña Es Incorrecta.....");
                        error.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                boolean sesion = false;
                                editor.putBoolean("sesion_usuario", sesion);
                                editor.commit();

                                startActivity(new Intent(getApplicationContext(), Login_2.class));
                                finish();
                            }
                        });
                        error.show();


                    } else {

                        JSONArray array = new JSONArray(response);

                        //LLENA EL ARREGLO CON LOS DATOS DE LA CONSULTA
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject cajas = array.getJSONObject(i);

                            id = cajas.getLong("idusuario");
                            Nombre1 = cajas.getString("Nombre");
                            Apellido1 = cajas.getString("Apellido");
                            Puesto = cajas.getString("Puesto");


                        }


                        //alojar en un archivo local los datos necesarios de usuario

                        editor.putLong("id1", id);
                        editor.putString("Nombre", Nombre1);
                        editor.putString("Apellido", Apellido1);
                        editor.putString("Puesto", Puesto);
                        editor.commit();

                        if (Objects.equals(Puesto, "RH")) {
                            startActivity(new Intent(getApplicationContext(), Recursos_Humanos.class));
                            finish();
                        } else {
                            startActivity(new Intent(getApplicationContext(), Recursos_Humanos.class));
                            finish();
                            editor.putLong("id_RH", 0L);

                            editor.commit();
                        }


                    }

                } catch (JSONException e) {
                    new SweetAlertDialog(Pantalla_carga.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Algo Salio Mal..")
                            .setContentText("No Hemos Podido Iniciar Su Sesion....")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(new Intent(getApplicationContext(), Login_2.class));
                                    finish();
                                }
                            })
                            .show();


                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                new SweetAlertDialog(Pantalla_carga.this,
                        SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Ops...Algo Salio Mal..")
                        .setContentText("No Hemos Podido Iniciar Sesion...")
                        .show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", Email_usuario);
                params.put("password", Contraseña_usuario);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Pantalla_carga.this);
        requestQueue.add(request);


    }
}