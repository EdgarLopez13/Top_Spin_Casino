package com.example.topspincasino.Estructura_De_Datos;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.topspincasino.Login_2;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login_Validaciones {

    public static SweetAlertDialog CambiarSesion = Login_2.CambiarSesion;
    public static boolean condicion;


    //VALIDA QUE LOS CAMPOS ESTEN RELLENADOS
    public static boolean validacion(EditText etEmail, EditText etContraseña, CheckBox sesion, Context context, String url) {

        //VALIDA QUE EL CAMPO DE EMAIL NO ESTE VACIO
        if (etEmail.getText().toString().equals("")) {

            //EN CASO DE ESTAR VACIO MANDA UN MENSAJE DE ADVERTENCIA LA USUARIO PARA QUE RELLENE EL CAMPO
            SweetAlertDialog error = new SweetAlertDialog(context,
                    SweetAlertDialog.ERROR_TYPE);
            error.setTitleText("Campo Vacio!!...");
            error.setContentText("Por Favor Intruduzca Un Usuario...");
            error.show();

            //VALIDA QUE EL CAMPO DE CONTRASEÑA NO ESTE VACIO
        } else if (etContraseña.getText().toString().equals("")) {

            //EN CASO DE ESTAR VACIO MANDA UN MENSAJE DE ADVERTENCIA LA USUARIO PARA QUE RELLENE EL CAMPO
            SweetAlertDialog error = new SweetAlertDialog(context,
                    SweetAlertDialog.ERROR_TYPE);
            error.setTitleText("Campo Vacio!!...");
            error.setContentText("Por Favor Introduzca Una Contraseña...");
            error.show();

            //VALIDA QUE EL CHECKBOX ESTE MARCADO
        } else if (sesion.isChecked() == false) {

            //EN CASO DE NO ESTAR MARCADO MANDA UN MENSAJE INFORMATIVO AL USUARIO PARA MANTENER SU SESION INICIADA O NO
            CambiarSesion = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
            CambiarSesion.setTitleText("La Casilla De Mantener Sesion No Esta Marcada...");
            CambiarSesion.setContentText("¿Desea Mantener Su Sesion Activa?");
            CambiarSesion.setConfirmText("SI");
            CambiarSesion.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sesion.setChecked(true);


                    condicion = Login_Consultas.SESION(etEmail.getText().toString(), etContraseña.getText().toString(), url, context);

                    CambiarSesion.dismiss();

                }
            });
            CambiarSesion.setCancelText("NO");
            CambiarSesion.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {


                     condicion = Login_Consultas.SESION(etEmail.getText().toString(), etContraseña.getText().toString(), url, context);

                    CambiarSesion.dismiss();

                }
            });
            CambiarSesion.show();

            //EN EL CASO DE SI ESTAR MARCADO ESTE LLAMA AL METODO DE SESION AUTOMATICAMENTE
        } else if (sesion.isChecked() == true) {

             condicion = Login_Consultas.SESION(etEmail.getText().toString(), etContraseña.getText().toString(), url, context);

        }

        return condicion;
    }


}
