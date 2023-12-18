package com.example.examenprimertrimestredi;

import com.example.examenprimertrimestredi.models.Coche;

import java.util.ArrayList;

public class Sesion {
    private static ArrayList<Coche> coches = new ArrayList<>(0);

    public static ArrayList<Coche> getCoches() {
        return coches;
    }

    public static void setCoches(ArrayList<Coche> coches) {
        Sesion.coches = coches;
    }

}
