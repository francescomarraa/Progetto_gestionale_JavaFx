package org.example.progettouid.model;

import java.util.ArrayList;

/*Tutto quello che riguarda Fattura/Utilità creato da: Francesco Molinaro*/

public class Fattura {
    private String numeroRicevuta;
    private String numeroOrdine;
    private String dataOrdine;
    private Cliente cliente;
    private ArrayList<Prodotto> prodotti;
    private String dataScadenzaPagamento;
    private String imponibile;
    private String sconto;
    private int totaleIva;
    private int totalePrezzo;

    public Fattura(String numeroRicevuta, String numeroOrdine, String dataOrdine, Cliente cliente,
                   ArrayList<Prodotto> prodotti, String dataScadenzaPagamento, String imponibile,
                   String sconto, int totaleIva, int totalePrezzo) {
        this.numeroRicevuta = numeroRicevuta;
        this.numeroOrdine = numeroOrdine;
        this.dataOrdine = dataOrdine;
        this.cliente = cliente;
        this.prodotti = prodotti;
        this.dataScadenzaPagamento = dataScadenzaPagamento;
        this.imponibile = imponibile;
        this.sconto = sconto;
        this.totaleIva = totaleIva;
        this.totalePrezzo = totalePrezzo;
    }

    // getter e setter
    public String getNumeroRicevuta() { return numeroRicevuta; }
    public void setNumeroRicevuta(String numeroRicevuta) { this.numeroRicevuta = numeroRicevuta; }

    public String getNumeroOrdine() { return numeroOrdine; }
    public void setNumeroOrdine(String numeroOrdine) { this.numeroOrdine = numeroOrdine; }

    public String getDataOrdine() { return dataOrdine; }
    public void setDataOrdine(String dataOrdine) { this.dataOrdine = dataOrdine; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public ArrayList<Prodotto> getProdotti() { return prodotti; }
    public void setProdotti(ArrayList<Prodotto> prodotti) { this.prodotti = prodotti; }

    public String getDataScadenzaPagamento() { return dataScadenzaPagamento; }
    public void setDataScadenzaPagamento(String dataScadenzaPagamento) { this.dataScadenzaPagamento = dataScadenzaPagamento; }

    public String getImponibile() { return imponibile; }
    public void setImponibile(String imponibile) { this.imponibile = imponibile; }

    public String getSconto() { return sconto; }
    public void setSconto(String sconto) { this.sconto = sconto; }

    public int getTotaleIva() { return totaleIva; }
    public void setTotaleIva(int totaleIva) { this.totaleIva = totaleIva; }

    public int getTotalePrezzo() { return totalePrezzo; }
    public void setTotalePrezzo(int totalePrezzo) { this.totalePrezzo = totalePrezzo; }
}

