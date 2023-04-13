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

        totalcomida = findViewById(R.id.TVtotalcomidas);

        preferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        editor = preferences.edit();

        pDialog = new SweetAlertDialog(FoodList.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(true);
        pDialog.show();

        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.statusbar);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);
        PuntosRecycler();


    }


    private void PuntosRecycler() {

        pDialog.show();
        String RH;
        String url;
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

                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject cajas = array.getJSONObject(i);

                        Foodlist.add(new Food(
                                cajas.getInt("Id"),
                                cajas.getString("Nombre"),
                                cajas.getDouble("Precio"),
                                cajas.getString("Mesero"),
                                ""


                        ));

                        Total = Total + cajas.getDouble("Precio");
                    }

                    totalcomida.setText(Total + "$");


                    pDialog.dismiss();
                    if (Objects.equals(response, "[]")) {

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
                        AdapterFood adapterFood = new AdapterFood(Foodlist, FoodList.this);
                        RecyclerView recyclerView = findViewById(R.id.RV_food);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(FoodList.this));
                        recyclerView.setAdapter(adapterFood);
                    }

                } catch (JSONException e) {
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

                            new SweetAlertDialog(FoodList.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Algo Salio Mal..")
                                    .setContentText("No Hemos Podido Cargar Las Comidas...")
                                    .show();

                        } catch (Exception e) {
                            pDialog.dismiss();
                            new SweetAlertDialog(FoodList.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Algo Salio Mal..")
                                    .setContentText("Ubo Un Fallo En La App... Contacte Con El Equipo De Soporte....")
                                    .show();
                        }
                    }
                });

        Volley.newRequestQueue(FoodList.this).add(stringRequest);

    }

}