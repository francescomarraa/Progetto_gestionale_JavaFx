package org.example.progettouid.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Evento implements Serializable {
    private static final long serialVersionUID = 1L;
    private String titolo;
    private String descrizione;
    private String luogo;
    private LocalDate data;

    public Evento(){}

    public Evento(String titolo, String descrizione, String luogo, LocalDate data){
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.luogo = luogo;
        this.data = data;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getLuogo() {
        return luogo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    @Override
    public String toString(){
        return titolo;
    }
}

