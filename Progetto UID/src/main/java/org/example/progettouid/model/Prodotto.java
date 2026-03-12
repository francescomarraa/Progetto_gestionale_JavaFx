package org.example.progettouid.model;

public class Prodotto {
    private String codiceProdotto;
    private String nomeProdotto;
    private float prezzoUnitario;
    private int quantitaProdotto;
    private String categoria;
    private String idFornitore;
    private String dataCarico;
    private String immagine;

    private int iva;

    public Prodotto(String codiceProdotto, String nomeProdotto, float prezzoUnitario, int quantitaProdotto, String categoria, String idFornitore, String dataCarico, String immagine) {
        this.codiceProdotto = codiceProdotto;
        this.nomeProdotto = nomeProdotto;
        this.prezzoUnitario = prezzoUnitario;
        this.quantitaProdotto = quantitaProdotto;
        this.categoria = categoria;
        this.idFornitore = idFornitore;
        this.dataCarico = dataCarico;
        this.immagine = immagine;
    }

    public Prodotto() {
    }

    public String getCodiceProdotto() {
        return codiceProdotto;
    }

    public void setCodiceProdotto(String codiceProdotto) {
        this.codiceProdotto = codiceProdotto;
    }

    public String getNomeProdotto() {
        return nomeProdotto;
    }

    public void setNomeProdotto(String nomeProdotto) {
        this.nomeProdotto = nomeProdotto;
    }

    public float getPrezzoUnitario() {
        return prezzoUnitario;
    }

    public void setPrezzoUnitario(float prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }

    public int getQuantitaProdotto() {
        return quantitaProdotto;
    }

    public void setQuantitaProdotto(int quantitaProdotto) {
        this.quantitaProdotto = quantitaProdotto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getIdFornitore() {
        return idFornitore;
    }

    public void setIdFornitore(String idFornitore){
        this.idFornitore = idFornitore;
    }
    public String getDataCarico() {
        return dataCarico;
    }

    public void setDataCarico(String dataCarico) {
        this.dataCarico = dataCarico;
    }

    public String getImmagine() {
        return immagine;
    }
    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public int getIva() {
        return iva;
    }

    public void setIva(int iva) {
        this.iva = iva;
    }

    @Override
    public String toString() {
        return nomeProdotto;
    }

}