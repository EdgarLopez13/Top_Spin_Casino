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
import android.widget.CheckBox;
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

public class Login_2 extends AppCompatActivity {

    Button InicioSesion;
    SweetAlertDialog CambiarSesion;

    CheckBox sesion;

    EditText etEmail,etContraseña;

    String str_email,str_password,url="https://topspincasino.000webhostapp.com/TopSpin_Cocina/login.php";
    Long id;
    String Nombre1,Apellido1,Puesto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        InicioSesion = findViewById(R.id.BTNsesion);
        etEmail = findViewById(R.id.ETcorreo);
        etContraseña = findViewById(R.id.ETcontraseña);
        sesion = findViewById(R.id.CBSesion);

        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.statusbar);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);

        InicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validacion();
            }
        });
    }


    public void validacion() {

        //VALIDA QUE LOS CAMPOS ESTEN RELLENADOS
        if (etEmail.getText().toString().equals("")) {
            SweetAlertDialog error = new SweetAlertDialog(Login_2.this,
                    SweetAlertDialog.ERROR_TYPE);
            error.setTitleText("Campo Vacio!!...");
            error.setContentText("Por Favor Intruduzca Un Usuario...");
            error.show();
        } else if (etContraseña.getText().toString().equals("")) {
            SweetAlertDialog error = new SweetAlertDialog(Login_2.this,
                    SweetAlertDialog.ERROR_TYPE);
            error.setTitleText("Campo Vacio!!...");
            error.setContentText("Por Favor Introduzca Una Contraseña...");
            error.show();
        } else if (sesion.isChecked() == false) {
            CambiarSesion = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            CambiarSesion.setTitleText("La Casilla De Mantener Sesion No Esta Marcada...");
            CambiarSesion.setContentText("¿Desea Mantener Su Sesion Activa?");
            CambiarSesion.setConfirmText("SI");
            CambiarSesion.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sesion.setChecked(true);
                    str_email = etEmail.getText().toString().trim();
                    str_password = etContraseña.getText().toString().trim();

                    Validar(str_email, str_password);
                    CambiarSesion.dismiss();

                }
            });
            CambiarSesion.setCancelText("NO");
            CambiarSesion.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    str_email = etEmail.getText().toString().trim();
                    str_password = etContraseña.getText().toString().trim();

                    Validar(str_email, str_password);
                    CambiarSesion.dismiss();

                }
            });
            CambiarSesion.show();
        } else if (sesion.isChecked() == true) {
            str_email = etEmail.getText().toString().trim();
            str_password = etContraseña.getText().toString().trim();

            Validar(str_email, str_password);
        }
    }


    //FTP para realizar la consulta a la bd
    public void Validar(String Email_usuario, String Contraseña_usuario) {

//cuadro de dialogo
        SweetAlertDialog pDialog = new SweetAlertDialog(Login_2.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(true);
        pDialog.show();

        str_email = etEmail.getText().toString().trim();
        str_password = etContraseña.getText().toString().trim();


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();


                try {

                    //validacion de los datos obtenidos
                    if (Objects.equals(response, "Correo Incorrecto")) {

                        SweetAlertDialog error = new SweetAlertDialog(Login_2.this,
                                SweetAlertDialog.ERROR_TYPE);
                        error.setTitleText("Op...Algo Salio Mal...");
                        error.setContentText("Este Usuario No Es Valido.....");
                        error.show();


                    }else if(Objects.equals(response, "Password Incorrecta")){
                        SweetAlertDialog error = new SweetAlertDialog(Login_2.this,
                                SweetAlertDialog.ERROR_TYPE);
                        error.setTitleText("Op...Algo Salio Mal...");
                        error.setContentText("La Contraseña Es Incorrecta.....");
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


                        etEmail.setText("");
                        etContraseña.setText("");


                        //validar el guardar la sesion
                        if (sesion.isChecked() == true) {
                            SharedPreferences preferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                            boolean sesion = true;
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("sesion_usuario", sesion);
                            editor.commit();

                        }

                        //alojar en un archivo local los datos necesarios de usuario
                        SharedPreferences preferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putLong("id", id);
                        editor.putString("Nombre", Nombre1);
                        editor.putString("Apellido", Apellido1);
                        editor.putString("usuario",Email_usuario);
                        editor.putString("password",Contraseña_usuario);
                        editor.putString("Puesto", Puesto);
                        editor.commit();

                        //VALIDA EL PUESTO DE USUARIO PARA REDIRECCIONARLO

                        if (Objects.equals(Puesto, "RH")){
                            startActivity(new Intent(getApplicationContext(), Recursos_Humanos.class));
                            finish();
                        } else{
                            startActivity(new Intent(getApplicationContext(), Recursos_Humanos.class));
                            finish();
                            editor.putLong("id_RH", 0L);

                            editor.commit();
                        }


                    }

                } catch (JSONException e) {
                    new SweetAlertDialog(Login_2.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Algo Salio Mal..")
                            .setContentText("No Hemos Podido Iniciar Su Sesion....")
                            .show();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                new SweetAlertDialog(Login_2.this,
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

        RequestQueue requestQueue = Volley.newRequestQueue(Login_2.this);
        requestQueue.add(request);


    }

    /*public static String getCadena(String s, int start, int end){
        String resultado="";
        try{
            resultado = s.substring(start-1, end);
        } catch (IndexOutOfBoundsException e) {
        }
        return resultado;
    }*/
}