package org.example.progettouid.model;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;

public class Fornitore {

    private int id;
    String nome, cognome, telefono, CAP, Città, Via, RagioneSociale, PartitaIVA;

    public Fornitore(String nome, String cognome, String telefono, String CAP, String città, String via, String ragioneSociale, String partitaIVA) {
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
        this.CAP = CAP;
        Città = città;
        Via = via;
        RagioneSociale = ragioneSociale;
        PartitaIVA = partitaIVA;
    }

    public Fornitore() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCAP() {
        return CAP;
    }

    public void setCAP(String CAP) {
        this.CAP = CAP;
    }

    public String getCittà() {
        return Città;
    }

    public void setCittà(String città) {
        Città = città;
    }

    public String getVia() {
        return Via;
    }

    public void setVia(String via) {
        Via = via;
    }

    public String getRagioneSociale() {
        return RagioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        RagioneSociale = ragioneSociale;
    }

    public String getPartitaIVA() {
        return PartitaIVA;
    }

    public void setPartitaIVA(String partitaIVA) {
        PartitaIVA = partitaIVA;
    }


    @Override
    public String toString() {
        return nome + " " + cognome + " (" + RagioneSociale + ")";
    }



}
