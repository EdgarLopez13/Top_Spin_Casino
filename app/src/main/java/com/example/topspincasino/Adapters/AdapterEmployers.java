package com.example.topspincasino.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topspincasino.EmployedList;
import com.example.topspincasino.FoodList;
import com.example.topspincasino.Login_2;
import com.example.topspincasino.MainActivity;
import com.example.topspincasino.Organizadores.Employers;
import com.example.topspincasino.R;
import com.example.topspincasino.Recursos_Humanos;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class AdapterEmployers extends RecyclerView.Adapter<AdapterEmployers.ViewHolder> {

    ArrayList<Employers> EmployersList;
    private Context context;
    SharedPreferences preferences;

    BottomSheetDialog bottomSheetDialog, bottomSheetDialogmarker;

    EditText etnombre, etapellido, etgrupo, etemail, etasistencia, etmatricula;
    ImageView fotousuario;

    Button Scanear;

    SweetAlertDialog Eliminar_Marcador_recycler, pDialog;


    public AdapterEmployers(ArrayList<Employers> EmployersList, Context context) {

        this.EmployersList = EmployersList;
        this.context = context;

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Cargando ...");
        pDialog.setCancelable(true);

        preferences = context.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employedlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.Puesto.setText(EmployersList.get(position).getPuesto());
        holder.Nombre.setText(EmployersList.get(position).getNombre());
        holder.Credito.setText(EmployersList.get(position).getCredito() + "$");


        preferences = context.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (Recursos_Humanos.tipo == 1) {

            holder.Empleado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* Intent intent = new Intent(context, EmployedList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.getApplicationContext().startActivity(intent);
                    int idgrupo = GroupsList.get(position).getId();
                    editor.putInt("idmateria", idgrupo);
                    editor.putString("nombremateria",GroupsList.get(position).getGrado());
                    editor.commit();*/

                    Long id = EmployersList.get(position).getId();

                    editor.putLong("id_RH", id);
                    editor.commit();

                    Intent intent = new Intent(context, FoodList.class);
                    context.startActivity(intent);

                }
            });
        }

        if (Recursos_Humanos.tipo == 2) {

            holder.Credito.setVisibility(View.INVISIBLE);
            holder.creditotitulo.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return EmployersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Puesto, Nombre, Credito,creditotitulo;
        ImageView Foto;
        ConstraintLayout Empleado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Puesto = itemView.findViewById(R.id.TV_Puesto);
            Foto = itemView.findViewById(R.id.IV_foto);
            Nombre = itemView.findViewById(R.id.TV_Nombre);
            Empleado = itemView.findViewById(R.id.Layout_Empleado);
            Credito = itemView.findViewById(R.id.TVcredito_gastado);
            creditotitulo = itemView.findViewById(R.id.TVcreditotitulo);

        }
    }


}


