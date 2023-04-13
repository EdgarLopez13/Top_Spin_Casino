package com.example.topspincasino.Organizadores;

public class Food {

    int id;
    String Comida;
    double Precio;

    String Mesero;

   String Comida_Dia;

    public Food(int id, String comida, double precio, String mesero, String comida_Dia) {
        this.id = id;
        Comida = comida;
        Precio = precio;
        Mesero = mesero;
        Comida_Dia = comida_Dia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComida() {
        return Comida;
    }

    public void setComida(String comida) {
        Comida = comida;
    }

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double precio) {
        Precio = precio;
    }

    public String getMesero() {
        return Mesero;
    }

    public void setMesero(String mesero) {
        Mesero = mesero;
    }

    public String getComida_Dia() {
        return Comida_Dia;
    }

    public void setComida_Dia(String comida_Dia) {
        Comida_Dia = comida_Dia;
    }

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", Comida='" + Comida + '\'' +
                ", Precio=" + Precio +
                ", Mesero='" + Mesero + '\'' +
                ", Comida_Dia='" + Comida_Dia + '\'' +
                '}';
    }
}
