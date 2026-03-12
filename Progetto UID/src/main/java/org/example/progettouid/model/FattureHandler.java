package org.example.progettouid.model;

import org.example.progettouid.model.Prodotto;

import java.util.ArrayList;
import java.util.List;

public class FattureHandler {

    private List<Prodotto> prodottiFattura;
    private int totaleIva;
    private float totalePrezzo;
    private float sconto;

    public FattureHandler() {
        prodottiFattura = new ArrayList<>();
        totaleIva = 0;
        totalePrezzo = 0;
        sconto = 0;
    }

    public void addProdotto(Prodotto prodotto, int quantita, int iva) {
        Prodotto p = new Prodotto();
        p.setCodiceProdotto(prodotto.getCodiceProdotto());
        p.setNomeProdotto(prodotto.getNomeProdotto());
        p.setPrezzoUnitario(prodotto.getPrezzoUnitario());
        p.setQuantitaProdotto(quantita);
        p.setCategoria(prodotto.getCategoria());
        p.setIdFornitore(prodotto.getIdFornitore());
        p.setDataCarico(prodotto.getDataCarico());
        p.setImmagine(prodotto.getImmagine());
        p.setIva(iva);

        prodottiFattura.add(p);

        totalePrezzo += quantita * prodotto.getPrezzoUnitario();
        totaleIva += (quantita * prodotto.getPrezzoUnitario()) * iva / 100;
    }

    public List<Prodotto> getProdottiFattura() {
        return prodottiFattura;
    }

    public int getTotaleIva() {
        return totaleIva;
    }

    public float getImponibile(){
        return totalePrezzo;
    }

    public void setSconto(float sconto){
        if (sconto < 0) {
            this.sconto = 0;
        } else if (sconto > 100) {
            this.sconto = 100;
        } else {
            this.sconto = sconto;
        }
    }

    public float getValoreSconto() {
        return totalePrezzo * sconto / 100;
    }

    public float getImponibileScontato() {
        return totalePrezzo - getValoreSconto();
    }

    public float getTotalePrezzo() {
        return totalePrezzo;
    }

    public float getTotaleIvaScontata() {
        return totaleIva * (1 - sconto / 100);
    }

    public float getTotaleFattura() {
        return getImponibileScontato() + getTotaleIvaScontata();
    }

    public void clearProdottiEnd() {
        prodottiFattura.clear();
        totaleIva = 0;
        totalePrezzo = 0;
    }
}