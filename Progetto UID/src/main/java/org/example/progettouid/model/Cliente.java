package org.example.progettouid.model;
/*Tutto quello che riguarda Cliente creato da: Francesco Molinaro*/

public class Cliente {
    private int id;
    private String nome, cognome, telefono, citta;
    private String partitaIVA;

    public Cliente(int id, String nome, String cognome, String telefono, String citta){
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
        this.citta = citta;
    }

    public Cliente(String nome, String cognome, String telefono, String citta) {
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
        this.citta = citta;
    }

    public Cliente(){}


    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getNome(){
        return nome;
    }

    public String getCognome(){
        return cognome;
    }

    public String getCitta(){
        return citta;
    }

    public String getTelefono(){
        return telefono;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setCognome(String cognome){
        this.cognome = cognome;
    }

    public void setCitta(String citta){
        this.citta = citta;
    }

    public void setTelefono(String telefono){
        this.telefono = telefono;
    }

    public String getPartitaIVA() {
        return partitaIVA;
    }

    public void setPartitaIVA(String partitaIVA) {
        this.partitaIVA = partitaIVA;
    }

    @Override
    public String toString(){
        return nome + " " + cognome;
    }
}
