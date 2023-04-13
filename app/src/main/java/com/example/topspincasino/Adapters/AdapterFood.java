package com.example.topspincasino.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topspincasino.Organizadores.Employers;
import com.example.topspincasino.Organizadores.Food;
import com.example.topspincasino.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdapterFood extends RecyclerView.Adapter<AdapterFood.ViewHolder> {

     ArrayList<Food> FoodList;
    private  Context context;
    SharedPreferences preferences;
     ArrayList<Food> Modificar;

    BottomSheetDialog bottomSheetDialog, bottomSheetDialogmarker;

    EditText etnombre,etapellido,etgrupo,etemail,etasistencia,etmatricula;
    ImageView fotousuario;

    Button Scanear;

    SweetAlertDialog Eliminar_Marcador_recycler, pDialog;


    public AdapterFood(ArrayList<Food> FoodList, Context context ) {

        this.FoodList = FoodList;
        this.context = context;
        this.Modificar = new ArrayList<>();
        Modificar.addAll(FoodList);

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Cargando ...");
        pDialog.setCancelable(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.Mesero.setText(FoodList.get(position).getMesero());
        holder.Comida.setText(FoodList.get(position).getComida());
        holder.Precio.setText(FoodList.get(position).getPrecio()+"$");
        holder.CBcomidadia.setVisibility(View.GONE);


        preferences = context.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        holder.Empleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return FoodList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Mesero,Comida,Precio;
        ConstraintLayout Empleado;
        CheckBox CBcomidadia;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Mesero = itemView.findViewById(R.id.TV_Mesero);

            Comida = itemView.findViewById(R.id.TV_COMIDA);
            Empleado = itemView.findViewById(R.id.Layout_food);
            Precio = itemView.findViewById(R.id.TV_precio);
            CBcomidadia = itemView.findViewById(R.id.CBcomidadia);

        }
    }

    public  ArrayList<Food> filtrado(String Buscar) {

        try {
            int longitud = Buscar.length();

            if (longitud == 0 || Buscar == null || Buscar == "" || Buscar.isEmpty()) {

                Modificar.clear();
                Modificar.addAll(FoodList);

            } else {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    List<Food> collection = FoodList.stream().filter(i -> i.getComida().toLowerCase().
                                    contains(Buscar.toLowerCase()))
                            .collect(Collectors.toList());
                    Modificar.clear();

                    if (Objects.equals(Modificar, "[]")) {
                        Modificar.addAll(FoodList);
                    } else {
                        Modificar.addAll(collection);
                    }

                    ArrayList<Food> vacio = new ArrayList<Food>();
                    vacio.clear();

                }

            }
        } catch (Exception e) {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Algo Salio Mal..")
                    .setContentText("Ubo Un Fallo En La App... Contacte Con El Equipo De Soporte....")
                    .show();
        }
        return Modificar;


    }

}
