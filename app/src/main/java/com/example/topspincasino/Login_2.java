package com.example.topspincasino;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.topspincasino.Estructura_De_Datos.Login_Consultas;
import com.example.topspincasino.Estructura_De_Datos.Login_Validaciones;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login_2 extends AppCompatActivity {

    //variables globales
    public static CheckBox sesion;
    public static SweetAlertDialog pDialog,CambiarSesion;

    Button InicioSesion;

    EditText etEmail, etContraseña;

    String str_email, str_password, url;
    Long id;
    String Nombre1, Apellido1, Puesto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        declaraciones();

        onclick();
    }

    private void declaraciones() {

        //DECLARA LA RUTA DEL URL
        url = "https://topspincasino.000webhostapp.com/TopSpin_Cocina/login.php";

        //DECLARA LOS OBJETOS QUE SE UTILIZARAN
        InicioSesion = findViewById(R.id.BTNsesion);
        etEmail = findViewById(R.id.ETcorreo);
        etContraseña = findViewById(R.id.ETcontraseña);
        sesion = findViewById(R.id.CBSesion);


        //cuadro de dialogo
        pDialog = new SweetAlertDialog(Login_2.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(true);

        //ELIMINA EL BARRA DE ESTADO
        Window window = getWindow();
        Drawable background = getResources().getDrawable(R.drawable.statusbar);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));
        window.setBackgroundDrawable(background);
    }

    private void onclick() {
        InicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login_Validaciones.validacion(etEmail,etContraseña,sesion,Login_2.this,url);
            }
        });
    }



}