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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.topspincasino.Adapters.AdapterEmployers;
import com.example.topspincasino.Adapters.AdapterFood;
import com.example.topspincasino.Estructura_De_Datos.Employers_Consultas;
import com.example.topspincasino.Organizadores.Employers;
import com.example.topspincasino.Organizadores.Food;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EmployedList extends AppCompatActivity {


    public Employers employers;

    public static SweetAlertDialog pDialog;

    public static  RecyclerView Empleados_Lista;
    SharedPreferences preferences;

    public static ArrayList<Employers> Employedist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employed_list);

        declaraciones();


    }

    private void declaraciones(){

        //Declaracion de objetos
        Empleados_Lista = findViewById(R.id.RV_Empleados);

        //Declaracion de objetos de guardado de datos
        preferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        //Declaracion de cuadro de dialogo de carga
        pDialog = new SweetAlertDialog(EmployedList.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(true);
        pDialog.show();

        //Eliinacion de barra de estado
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.statusbar);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);
        Employers_Consultas.Empleados(Employedist,this);
    }



}