package com.example.topspincasino;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.topspincasino.Adapters.AdapterFood;
import com.example.topspincasino.Adapters.Comidas_MeseroAdapter;
import com.example.topspincasino.Organizadores.Food;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    static Context context;
    static SweetAlertDialog pDialog;
    static SharedPreferences preferences;
    EditText Nombre;
    String Usuario;
    public static EditText Etcomida, Etprecio;
    Button btnEscaner, BTNguardar, btncomidas, BTNlimpiar;
    Comidas_MeseroAdapter foodadapter;


    public static BottomSheetDialog bottomSheetDialog, bottomSheetDialogmarker;

    SharedPreferences.Editor editor;
    public static ArrayList<Food> Foodlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Nombre = findViewById(R.id.etUsuario);
        btnEscaner = findViewById(R.id.BTNscanear);
        BTNguardar = findViewById(R.id.BTNnuevacomida);
        btncomidas = findViewById(R.id.BTNcomida);
        BTNlimpiar = findViewById(R.id.BTNlimpiarcomida);
        Etcomida = findViewById(R.id.etPlatillocomida);
        Etprecio = findViewById(R.id.etCostocomida);

        foodadapter = new Comidas_MeseroAdapter(Foodlist, MainActivity.this);
        context = MainActivity.this;

        pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Cargando ...");
        pDialog.setCancelable(false);

        preferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putLong("id_comida", 0L);

        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.statusbar);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);


        BTNguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar();
            }
        });
        BTNlimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Etcomida.setText("");
                Etprecio.setText("");
                Nombre.setText("");
                editor.putLong("id_comida", 0);
                editor.commit();
                Usuario = "";
            }
        });
        btnEscaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //escaneo de codigos
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setOrientationLocked(false);
                integrator.setPrompt("ASEGURATE DE ENFOCAR BIEN EL CODIGO DE BARRAS");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

        btncomidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.show();

                BottomSheetBehavior<View> bottomSheetBehavior;
                bottomSheetDialog = new BottomSheetDialog
                        (MainActivity.this, R.style.BottomSheetDialog);
                View bottomSheetView = LayoutInflater.from(MainActivity.this).inflate(
                        R.layout.comida_mesera, null
                );
                bottomSheetDialog.setContentView(bottomSheetView);
                LinearLayout contenedor1 = bottomSheetDialog.findViewById(R.id.ComidasBottomShet);
                bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.92);

                assert contenedor1 != null;
                contenedor1.setMinimumHeight(height);
                bottomSheetBehavior.setMaxHeight(height);

                RecyclerView comidas = bottomSheetDialog.findViewById(R.id.RVcomidasmeseros);
                SearchView SVcomida = bottomSheetDialog.findViewById(R.id.SVcomida);
                TextView titulocomida = bottomSheetDialog.findViewById(R.id.TVtitulocomida);


                BuscarComidas();


                SVcomida.setOnSearchClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        titulocomida.setVisibility(View.INVISIBLE);


                    }
                });

                SVcomida.setOnCloseListener(new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        titulocomida.setVisibility(View.VISIBLE);
                        SVcomida.setBackgroundResource(R.color.Trasparente);
                        return false;
                    }
                });

                SVcomida.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {


                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {


                        ArrayList<Food> lista = new ArrayList<>();
                        lista = foodadapter.filtrado(newText);
                        Comidas_MeseroAdapter adapter = new Comidas_MeseroAdapter(lista, MainActivity.this);
                        comidas.setHasFixedSize(true);
                        comidas.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        comidas.setAdapter(adapter);
                        return false;
                    }

                });

            }
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Atencion!")
                        .setContentText("Haz Cancelado El Escaneo....")
                        .show();
            } else {

                Usuario(result.getContents());

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void validar() {
        if (Objects.equals(Nombre.getText().toString(), "")) {
            new SweetAlertDialog(MainActivity.this,
                    SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("AVISO!!")
                    .setContentText("Favor De Escanear A Un Usuario")
                    .show();
        } else if (Objects.equals(Etcomida.getText().toString(), "")) {
            new SweetAlertDialog(MainActivity.this,
                    SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("AVISO!!")
                    .setContentText("Favor De Seleccionar Un Platillo")
                    .show();
        } else {
            RegistrarComida(Usuario, preferences.getLong("id_comida", 0L) + "");
        }
    }


    public void RegistrarComida(String id_usuario, String idcomida) {

        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://topspincasino.000webhostapp.com/TopSpin_Cocina/agregar_comida_usuario.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    pDialog.dismiss();
                    if (Objects.equals(response, "Registro Exitoso")) {
                        new SweetAlertDialog(MainActivity.this,
                                SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Registrado")
                                .setContentText("Se a Registrado Correctamente El Pedido!!")
                                .show();

                        Etcomida.setText("");
                        Etprecio.setText("");
                        Nombre.setText("");
                        editor.putLong("id_comida", 0L);
                        editor.commit();

                    } else {
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Algo Salio Mal..")
                                .setContentText("No Hemos Podido Registrar Su Comida...")
                                .show();
                        editor.putLong("id_comida", 0L);
                        editor.commit();
                    }
                } catch (Exception e) {
                    pDialog.dismiss();
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Algo Salio Mal..")
                            .setContentText("Ubo Un Fallo En La App... Contacte Con El Equipo De Soporte....")
                            .show();
                    editor.putLong("id_comida", 0L);
                    editor.commit();
                }
            }


        }, new com.android.volley.Response.ErrorListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    pDialog.dismiss();

                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Algo Salio Mal..")
                            .setContentText("No Hemos Podido Registrar Su Comida...")
                            .show();
                    editor.putLong("id_comida", 0L);
                    editor.commit();

                } catch (Exception e) {
                    pDialog.dismiss();
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Algo Salio Mal..")
                            .setContentText("Ubo Un Fallo En La App... Contacte Con El Equipo De Soporte....")
                            .show();
                    editor.putLong("id_comida", 0L);
                    editor.commit();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();


                params.put("idcomida", idcomida);
                params.put("iduser", id_usuario);
                params.put("mesero", preferences.getString("Nombre", ""));
                params.put("comida_dia", preferences.getString("comida_dia", ""));

                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void Usuario(String id_usuario) {


        String url = "https://topspincasino.000webhostapp.com/TopSpin_Cocina/empleado.php?codigo=" + id_usuario;


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject cajas = array.getJSONObject(i);

                        Nombre.setText(cajas.getString("Nombre") + " " + cajas.getString("Apellido"));
                        Usuario = cajas.getString("Id");


                    }


                    pDialog.dismiss();
                    if (Objects.equals(response, "[]")) {
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("No hay empleados..")
                                .setContentText("No se encontraron empleados registrados")
                                .show();
                    } else {

                    }

                } catch (JSONException e) {
                    pDialog.dismiss();
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Algo Salio Mal..")
                            .setContentText("Ubo Un Fallo En La App... Contacte Con El Equipo De Soporte....")
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

                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Algo Salio Mal..")
                                    .setContentText("No Hemos Podido Cargar Los empleados...")
                                    .show();

                        } catch (Exception e) {
                            pDialog.dismiss();
                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Algo Salio Mal..")
                                    .setContentText("Ubo Un Fallo En La App... Contacte Con El Equipo De Soporte....")
                                    .show();
                        }
                    }
                });

        Volley.newRequestQueue(MainActivity.this).add(stringRequest);

    }


    public static void BuscarComidas() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://topspincasino.000webhostapp.com/TopSpin_Cocina/Buscar_comidas_meseros.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    Foodlist.clear();

                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject cajas = array.getJSONObject(i);

                        Foodlist.add(new Food(
                                cajas.getInt("Id"),
                                cajas.getString("Nombre"),
                                cajas.getDouble("Precio"),
                                preferences.getString("Nombre", ""),
                                cajas.getString("comida_dia")


                        ));


                    }


                    pDialog.dismiss();
                    if (Objects.equals(response, "[]")) {


                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("No Se Encontraron Comidas..")
                                .setContentText("No Se Encontraro Comidas....")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        bottomSheetDialog.dismiss();
                                    }
                                })
                                .show();

                    } else {

                        Comidas_MeseroAdapter comidas_MeseroAdapter = new Comidas_MeseroAdapter(Foodlist, context);
                        RecyclerView comidas = bottomSheetDialog.findViewById(R.id.RVcomidasmeseros);
                        comidas.setHasFixedSize(true);
                        comidas.setLayoutManager(new LinearLayoutManager(context));
                        comidas.setAdapter(comidas_MeseroAdapter);
                        bottomSheetDialog.show();
                    }

                } catch (JSONException e) {
                    pDialog.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Algo Salio Mal..")
                            .setContentText("Ubo Un Fallo En La App... Contacte Con El Equipo De Soporte....")
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

                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Algo Salio Mal..")
                                    .setContentText("No Hemos Podido Cargar Las Comidas...")
                                    .show();

                        } catch (Exception e) {
                            pDialog.dismiss();
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