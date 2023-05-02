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
import android.widget.TextView;

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

public class FoodList extends AppCompatActivity {

    SweetAlertDialog pDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView totalcomida;

    int id_usuario;
    public static ArrayList<Food> Foodlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        declaraciones();


    }

    private void declaraciones() {

        //declaracion de objetos
        totalcomida = findViewById(R.id.TVtotalcomidas);

        //declaracion del objeto de guardado de datos
        preferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        editor = preferences.edit();

        //declaracion del dialogo de carga
        pDialog = new SweetAlertDialog(FoodList.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(true);
        pDialog.show();

        //Eliminar la barra de estado
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.statusbar);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);
        PuntosRecycler();
    }


    //Metodo que rellena el recyclerview con los datos de las comidas del empleado
    private void PuntosRecycler() {

        pDialog.show();
        String RH;
        String url;

        //verificar si el usuario es el que inicio sesion o Recursos Humanos
        if (preferences.getLong("id_RH", 0L) == 0L) {
            url = "https://topspincasino.000webhostapp.com/TopSpin_Cocina/Buscar_comida.php?id=" +
                    preferences.getLong("id", 0L);
            RH = "si";
        } else {
            url = "https://topspincasino.000webhostapp.com/TopSpin_Cocina/Buscar_comida.php?id=" +
                    preferences.getLong("id_RH", 0L);
            RH = "no";
        }


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Double Total = 0.0;

                    Foodlist.clear();

                    //convierte el JSON de los datos obtenidos de la API a un array para poder extraerlos
                    JSONArray array = new JSONArray(response);


                    //Ciclo que enlista las comidas del empleado
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject cajas = array.getJSONObject(i);

                        Foodlist.add(new Food(
                                cajas.getInt("Id"),
                                cajas.getString("Nombre"),
                                cajas.getDouble("Precio"),
                                cajas.getString("Mesero"),
                                ""


                        ));

                        //total de la cantidad gastada en comidas
                        Total = Total + cajas.getDouble("Precio");
                    }

                    totalcomida.setText(Total + "$");


                    pDialog.dismiss();

                    //si el campo esta vacion es porque el usuario no tiene comidas en esa semana
                    if (Objects.equals(response, "[]")) {

                        //valida si el que busca la comida es el mismo usuario o Recursos Humanos
                        if (RH == "si") {
                            new SweetAlertDialog(FoodList.this)
                                    .setTitleText("No Cuenta Con Comidas..")
                                    .setContentText("No Cuenta Con Comidas Esta Semana :D!!")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            finish();
                                        }
                                    })
                                    .show();
                        } else {
                            new SweetAlertDialog(FoodList.this)
                                    .setTitleText("No Cuenta Con Comidas..")
                                    .setContentText("Este Usuario No Tiene Comidas Esta Semana :D!!")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            finish();
                                        }
                                    })
                                    .show();
                        }
                    } else {

                        //pasa los valores de la lista al daptador  que llena el recyclerview con los datos
                        AdapterFood adapterFood = new AdapterFood(Foodlist, FoodList.this);
                        RecyclerView recyclerView = findViewById(R.id.RV_food);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(FoodList.this));
                        recyclerView.setAdapter(adapterFood);
                    }

                } catch (JSONException e) {

                    //atrapa un posible error evitando que este provoque el cierre de la app y muestra un mensaje
                    pDialog.dismiss();
                    new SweetAlertDialog(FoodList.this, SweetAlertDialog.ERROR_TYPE)
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

                            //si la API responde con un error es porque no se pudo procesar la accion y muestra un mensaje de error
                            new SweetAlertDialog(FoodList.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Algo Salio Mal..")
                                    .setContentText("No Hemos Podido Cargar Las Comidas...")
                                    .show();

                        } catch (Exception e) {

                            //atrapa un posible error evitando que este provoque el cierre de la app y muestra un mensaje
                            pDialog.dismiss();
                            new SweetAlertDialog(FoodList.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Algo Salio Mal..")
                                    .setContentText("Ubo Un Fallo En La App... Contacte Con El Equipo De Soporte....")
                                    .show();
                        }
                    }
                });

        //Realiza la accion atravez de un VOLLEY
        Volley.newRequestQueue(FoodList.this).add(stringRequest);

    }

}