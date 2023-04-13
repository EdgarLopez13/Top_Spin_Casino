package com.example.topspincasino.Organizadores;

public class Employers {
    Long id;
    String Nombre;
    String Puesto;
    double credito;
    String Foto;

    public Employers(Long id, String nombre, String puesto, double credito, String foto) {
        this.id = id;
        Nombre = nombre;
        Puesto = puesto;
        this.credito = credito;
        Foto = foto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getPuesto() {
        return Puesto;
    }

    public void setPuesto(String puesto) {
        Puesto = puesto;
    }

    public double getCredito() {
        return credito;
    }

    public void setCredito(double credito) {
        this.credito = credito;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    @Override
    public String toString() {
        return "Employers{" +
                "id=" + id +
                ", Nombre='" + Nombre + '\'' +
                ", Puesto='" + Puesto + '\'' +
                ", credito=" + credito +
                ", Foto='" + Foto + '\'' +
                '}';
    }
}