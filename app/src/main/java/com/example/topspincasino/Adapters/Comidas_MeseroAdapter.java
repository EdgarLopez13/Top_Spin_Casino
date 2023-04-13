package com.example.topspincasino.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.topspincasino.MainActivity;
import com.example.topspincasino.Organizadores.Food;
import com.example.topspincasino.R;
import com.example.topspincasino.Recursos_Humanos;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Comidas_MeseroAdapter extends RecyclerView.Adapter<Comidas_MeseroAdapter.ViewHolder> {

    ArrayList<Food> FoodList;
    private Context context;
    SharedPreferences preferences;

    SharedPreferences.Editor editor;

    SweetAlertDialog Eliminar_Marcador_recycler, pDialog;


    ArrayList<Food> Modificar;

    public Comidas_MeseroAdapter(ArrayList<Food> FoodList, Context context) {

        this.FoodList = FoodList;
        this.context = context;
        this.Modificar = new ArrayList<>();
        Modificar.addAll(FoodList);

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Cargando ...");
        pDialog.setCancelable(true);

        preferences = context.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.Mesero.setVisibility(View.INVISIBLE);
        holder.titulomesero.setVisibility(View.INVISIBLE);
        holder.Comida.setText(FoodList.get(position).getComida());


        if (Objects.equals(FoodList.get(position).getComida_Dia(), "si")) {
            holder.CBcomidadia.setChecked(true);
            holder.Precio.setText(FoodList.get(position).getPrecio() + "$");
            holder.Precio.setPaintFlags(holder.Precio.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            holder.tvnuevoprecio.setVisibility(View.VISIBLE);
            holder.tvnuevoprecio.setText(FoodList.get(position).getPrecio() - 25 + "$");

        } else {
            holder.CBcomidadia.setChecked(false);
            holder.Precio.setText(FoodList.get(position).getPrecio() + "$");
        }


        holder.Empleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.Etcomida.setText(FoodList.get(position).getComida());

                if (Objects.equals(FoodList.get(position).getComida_Dia(), "si")) {

                    MainActivity.Etprecio.setText(FoodList.get(position).getPrecio() - 25 + "");
                    editor.putString("comida_dia","si");
                    editor.commit();

                } else {

                    MainActivity.Etprecio.setText(FoodList.get(position).getPrecio() + "");
                }
                MainActivity.bottomSheetDialog.dismiss();

                editor.putLong("id_comida", FoodList.get(position).getId());
                editor.commit();
                editor.putString("comida_dia","no");
                editor.commit();
            }
        });


        holder.CBcomidadia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                holder.CBcomidadia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (isChecked == true) {
                            CambiarComidaDia("true", FoodList.get(position).getId() + "");
                        } else {
                            CambiarComidaDia("false", FoodList.get(position).getId() + "");
                        }
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return FoodList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Mesero, Comida, Precio, titulomesero, tvnuevoprecio;
        ConstraintLayout Empleado;
        CheckBox CBcomidadia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Mesero = itemView.findViewById(R.id.TV_Mesero);

            Comida = itemView.findViewById(R.id.TV_COMIDA);
            Empleado = itemView.findViewById(R.id.Layout_food);
            Precio = itemView.findViewById(R.id.TV_precio);
            titulomesero = itemView.findViewById(R.id.TVtitulomesero);
            CBcomidadia = itemView.findViewById(R.id.CBcomidadia);
            tvnuevoprecio = itemView.findViewById(R.id.TVnuevoprecio);

        }
    }

    public ArrayList<Food> filtrado(String Buscar) {

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


    public void CambiarComidaDia(String cambio, String id) {

        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://topspincasino.000webhostapp.com/TopSpin_Cocina/cambiar_comida_dia.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    pDialog.dismiss();
                    if (Objects.equals(response, "Registro Exitoso")) {
                        new SweetAlertDialog(context,
                                SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("ACTUALIZADO!!")
                                .setContentText("Se A Actualizado La Comida Del Dia")
                                .show();
                        MainActivity.BuscarComidas();

                    } else {
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Algo Salio Mal..")
                                .setContentText("No Se A Actualizado La Comida Del Dia...")
                                .show();
                        editor.putLong("id_comida", 0L);
                        editor.commit();
                    }
                } catch (Exception e) {
                    pDialog.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
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

                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Algo Salio Mal..")
                            .setContentText("No Se A Actualizado La Comida Del Dia...")
                            .show();
                    editor.putLong("id_comida", 0L);
                    editor.commit();

                } catch (Exception e) {
                    pDialog.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
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


                params.put("cambio", cambio);
                params.put("id", id);


                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
