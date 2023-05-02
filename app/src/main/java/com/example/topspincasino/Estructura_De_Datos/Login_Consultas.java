package com.example.topspincasino.Estructura_De_Datos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.topspincasino.Login_2;
import com.example.topspincasino.Recursos_Humanos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login_Consultas {

    static String Puesto;





    //FTP para realizar la consulta a la bd
    public static boolean SESION(String Email_usuario, String Contraseña_usuario, String url, Context context) {

        Login_2.pDialog.show();

        SharedPreferences preferences = context.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Login_2.pDialog.dismiss();


                try {

                    //validacion de los datos obtenidos
                    if (Objects.equals(response, "Correo Incorrecto")) {

                        SweetAlertDialog error = new SweetAlertDialog(context,
                                SweetAlertDialog.ERROR_TYPE);
                        error.setTitleText("Op...Algo Salio Mal...");
                        error.setContentText("Este Usuario No Es Valido.....");
                        error.show();


                    } else if (Objects.equals(response, "Password Incorrecta")) {
                        SweetAlertDialog error = new SweetAlertDialog(context,
                                SweetAlertDialog.ERROR_TYPE);
                        error.setTitleText("Op...Algo Salio Mal...");
                        error.setContentText("La Contraseña Es Incorrecta.....");
                        error.show();

                    } else {

                        JSONArray array = new JSONArray(response);

                        //LLENA EL ARREGLO CON LOS DATOS DE LA CONSULTA
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject cajas = array.getJSONObject(i);

                            //alojar en un archivo local los datos necesarios de usuario

                            editor.putLong("id", cajas.getLong("idusuario"));
                            editor.putString("Nombre", cajas.getString("Nombre"));
                            editor.putString("Apellido",cajas.getString("Apellido"));
                            editor.putString("usuario", Email_usuario);
                            editor.putString("password", Contraseña_usuario);
                            editor.putString("Puesto", cajas.getString("Puesto"));
                            editor.commit();

                            Puesto = cajas.getString("Puesto");





                        }


                        //validar el guardar la sesion
                        if (Login_2.sesion.isChecked() == true) {
                            boolean sesion = true;
                            editor.putBoolean("sesion_usuario", sesion);
                            editor.commit();

                        }



                        //VALIDA EL PUESTO DE USUARIO PARA REDIRECCIONARLO

                        if (Objects.equals(Puesto, "RH")) {
                            context.startActivity(new Intent(context.getApplicationContext(), Recursos_Humanos.class));

                        } else {
                            context.startActivity(new Intent(context.getApplicationContext(), Recursos_Humanos.class));

                            editor.putLong("id_RH", 0L);

                            editor.commit();
                        }


                    }

                } catch (JSONException e) {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Algo Salio Mal..")
                            .setContentText("No Hemos Podido Iniciar Su Sesion....")
                            .show();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Login_2.pDialog.dismiss();
                new SweetAlertDialog(context,
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

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

        if (!Objects.equals(preferences.getLong("id",0L),0l)){
            return true;
        }else {
            return false;
        }
    }
}
