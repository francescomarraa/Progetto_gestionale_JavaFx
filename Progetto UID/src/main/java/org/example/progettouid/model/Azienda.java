package org.example.progettouid.model;

//Classe creata per la memorizzazione di un'azienda nel DB e in Impostazioni
//Rende dinamica l'informazione aziendale nella Home
/*Tutto quello che riguarda Impostazioni creato da: Francesco Molinaro*/
/*Tutto quello che riguarda Home creato da: Francesco Molinaro*/

public class Azienda {

    private String nome;
    private String piva;
    private String indirizzo;
    private String telefono;

    public Azienda() {}

    public Azienda(String nome, String piva, String indirizzo, String telefono) {
        this.nome = nome;
        this.piva = piva;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPiva() {
        return piva;
    }

    public void setPiva(String piva) {
        this.piva = piva;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
