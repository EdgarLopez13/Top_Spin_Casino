package com.example.topspincasino;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Recursos_Humanos extends AppCompatActivity {

    public static int tipo = 0;
    ConstraintLayout CLcomidas, CLnominas, CLnuevo, CLsemana, CLperfil, CLcocina,CLnuevacomida,CLusuarios;
    SweetAlertDialog pDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    CardView CVcomidas, CVnominas, CVnuevo, CVsemana, CVperfil, CVcocina,CVnuevacomida,CVusuarios;
    EditText NUnombre, NUapellido, NUusuario, NUcontraseña, NUpuesto;
    public static BottomSheetDialog bottomSheetDialog, bottomSheetDialogfecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recursos_humanos);

        preferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        editor = preferences.edit();


        CLnominas = findViewById(R.id.CLnominas);
        CLcomidas = findViewById(R.id.CLcomidas);
        CLnuevo = findViewById(R.id.CLnuevo);
        CLsemana = findViewById(R.id.CLsemana);
        CLperfil = findViewById(R.id.CLperfil);
        CLcocina = findViewById(R.id.CLcocina);
        CLnuevacomida = findViewById(R.id.CLnuevacomida);
        CLusuarios = findViewById(R.id.CLusuarios);

        CVnominas = findViewById(R.id.CVnominas);
        CVnuevo = findViewById(R.id.CVnuevo);
        CVsemana = findViewById(R.id.CVsemana);
        CVcocina = findViewById(R.id.CVcocina);
        CVnuevacomida = findViewById(R.id.CVnuevacomida);
        CVusuarios = findViewById(R.id.CVUsuarios);

        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.statusbar);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);

        pDialog = new SweetAlertDialog(Recursos_Humanos.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(true);

        CLcomidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), FoodList.class));

                editor.putLong("id_RH", 0L);
                editor.commit();
            }
        });

        CLperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Perfil.class));

            }
        });

        CLnominas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo = 1;
                startActivity(new Intent(getApplicationContext(), EmployedList.class));

            }
        });


        CLnuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.show();

                BottomSheetBehavior<View> bottomSheetBehavior;
                bottomSheetDialog = new BottomSheetDialog
                        (Recursos_Humanos.this, R.style.BottomSheetDialog);
                View bottomSheetView = LayoutInflater.from(Recursos_Humanos.this).inflate(
                        R.layout.new_user, null
                );
                bottomSheetDialog.setContentView(bottomSheetView);
                LinearLayout contenedor1 = bottomSheetDialog.findViewById(R.id.contenedorusuario);
                bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.92);

                assert contenedor1 != null;
                contenedor1.setMinimumHeight(height);
                bottomSheetBehavior.setMaxHeight(height);

                NUnombre = bottomSheetDialog.findViewById(R.id.ETnombre);
                NUapellido = bottomSheetDialog.findViewById(R.id.ETapellido);
                NUusuario = bottomSheetDialog.findViewById(R.id.ETusuario);
                NUcontraseña = bottomSheetDialog.findViewById(R.id.etcontraseña);
                NUpuesto = bottomSheetDialog.findViewById(R.id.etpuesto);

                Button agregarusuario = bottomSheetDialog.findViewById(R.id.BTNguardarusuario);

                agregarusuario.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RegistrarUsuario(NUnombre.getText().toString(), NUapellido.getText().toString(), NUusuario.getText().toString(),
                                NUcontraseña.getText().toString(), NUpuesto.getText().toString());
                    }
                });

                bottomSheetDialog.show();
                pDialog.dismiss();
            }
        });

        CLsemana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CLsemana.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        semana();

                    }
                });
            }
        });

        CLcocina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        CLnuevacomida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistroComidas.class));
            }
        });

        CLusuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo = 2;
                startActivity(new Intent(getApplicationContext(), EmployedList.class));
            }
        });



        if (!Objects.equals(preferences.getString("Puesto", ""), "RH")) {
            CVnuevo.setVisibility(View.INVISIBLE);
            CVnominas.setVisibility(View.INVISIBLE);
            CVsemana.setVisibility(View.INVISIBLE);
            CVnuevacomida.setVisibility(View.INVISIBLE);
            CVusuarios.setVisibility(View.INVISIBLE);
        }
        if (Objects.equals(preferences.getString("Puesto", ""), "Mesero")) {
            CVcocina.setVisibility(View.VISIBLE);
            CVnuevacomida.setVisibility(View.VISIBLE);

        }
    }


    public void semana(){
        pDialog.show();

        BottomSheetBehavior<View> bottomSheetBehavior;
        bottomSheetDialogfecha = new BottomSheetDialog
                (Recursos_Humanos.this, R.style.BottomSheetDialog);
        View bottomSheetView = LayoutInflater.from(Recursos_Humanos.this).inflate(
                R.layout.fecha, null
        );
        bottomSheetDialogfecha.setContentView(bottomSheetView);
        LinearLayout contenedor1 = bottomSheetDialogfecha.findViewById(R.id.LLfechas);
        bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);

        assert contenedor1 != null;
        contenedor1.setMinimumHeight(height);
        bottomSheetBehavior.setMaxHeight(height);
        bottomSheetDialogfecha.show();
        pDialog.dismiss();

        EditText fecha_inicial = bottomSheetDialogfecha.findViewById(R.id.etfechainicial);
        EditText fecha_final = bottomSheetDialogfecha.findViewById(R.id.etfechafinal);

        Button añadirfecha = bottomSheetDialogfecha.findViewById(R.id.BTNguardarfecha);

        añadirfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrarFecha(fecha_inicial.getText().toString(),fecha_final.getText().toString());
            }
        });


        fecha_inicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        Recursos_Humanos.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                fecha_inicial.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });

        fecha_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        Recursos_Humanos.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                fecha_final.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });
    }

    public void RegistrarUsuario(String nombre, String apellido, String usuario, String contraseña, String puesto) {

        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://topspincasino.000webhostapp.com/TopSpin_Cocina/agregar_usuario.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    pDialog.dismiss();
                    if (!Objects.equals(response, "Error")) {
                        new SweetAlertDialog(Recursos_Humanos.this,
                                SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Registrado!!")
                                .setContentText("El Codigo De Usuario Es: " + response)
                                .show();
                        bottomSheetDialog.dismiss();


                    } else {
                        new SweetAlertDialog(Recursos_Humanos.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Algo Salio Mal..")
                                .setContentText("No Hemos Podido Registrar El Usuario...")
                                .show();
                    }
                } catch (Exception e) {
                    pDialog.dismiss();
                    new SweetAlertDialog(Recursos_Humanos.this, SweetAlertDialog.ERROR_TYPE)
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

                    new SweetAlertDialog(Recursos_Humanos.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Algo Salio Mal..")
                            .setContentText("No Hemos Podido Registrar El Usuario...")
                            .show();

                } catch (Exception e) {
                    pDialog.dismiss();
                    new SweetAlertDialog(Recursos_Humanos.this, SweetAlertDialog.ERROR_TYPE)
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
                params.put("apellido", apellido);
                params.put("usuario", usuario);
                params.put("contraseña", contraseña);
                params.put("puesto", puesto);


                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void RegistrarFecha(String inicio, String end) {

        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://topspincasino.000webhostapp.com/TopSpin_Cocina/agregar_fecha.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    pDialog.dismiss();
                    if (Objects.equals(response, "Registro Exitoso")) {
                        new SweetAlertDialog(Recursos_Humanos.this,
                                SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Registrado!!")
                                .setContentText("La Semana Se Ha Registrado Correctamente!!")
                                .show();
                        bottomSheetDialogfecha.dismiss();


                    } else {
                        new SweetAlertDialog(Recursos_Humanos.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Algo Salio Mal..")
                                .setContentText("No Hemos Podido Registrar La Fecha..."+response)
                                .show();
                    }
                } catch (Exception e) {
                    pDialog.dismiss();
                    new SweetAlertDialog(Recursos_Humanos.this, SweetAlertDialog.ERROR_TYPE)
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

                    new SweetAlertDialog(Recursos_Humanos.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Algo Salio Mal..")
                            .setContentText("No Hemos Podido Registrar La Fecha...")
                            .show();

                } catch (Exception e) {
                    pDialog.dismiss();
                    new SweetAlertDialog(Recursos_Humanos.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Algo Salio Mal..")
                            .setContentText("Ubo Un Fallo En La App... Contacte Con El Equipo De Soporte....")
                            .show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();


                params.put("inicio", inicio);
                params.put("final", end);

                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}