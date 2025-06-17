package com.carlosdev.lab6_20210535;

import java.util.Date;

public class MovimientoLimaPass {
    private String id;
    private String idTarjeta;
    private String paraderoEntrada;
    private String paraderoSalida;
    private String fecha;
    private String idUsuario;

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public MovimientoLimaPass() {
    }

    public MovimientoLimaPass(String idMovimiento, String idTarjeta, String fecha, String paraderoEntrada, String paraderoSalida, String idUsuario) {
        this.id = idMovimiento;
        this.idTarjeta = idTarjeta;
        this.fecha = fecha;
        this.paraderoEntrada = paraderoEntrada;
        this.paraderoSalida = paraderoSalida;
        this.idUsuario = idUsuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdTarjeta() {
        return idTarjeta;
    }

    public void setIdTarjeta(String idTarjeta) {
        this.idTarjeta = idTarjeta;
    }

    public String getParaderoEntrada() {
        return paraderoEntrada;
    }

    public void setParaderoEntrada(String paraderoEntrada) {
        this.paraderoEntrada = paraderoEntrada;
    }

    public String getParaderoSalida() {
        return paraderoSalida;
    }

    public void setParaderoSalida(String paraderoSalida) {
        this.paraderoSalida = paraderoSalida;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
