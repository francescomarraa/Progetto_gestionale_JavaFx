package org.example.progettouid.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CalendarioHandler {
    private final List<Evento> eventi;
    private static final String FILE_PATH = "eventi.dat";

    public CalendarioHandler() {
        this.eventi = caricaDaFile();
    }

    public List<Evento> getEventi() {
        return eventi;
    }

    public void aggiungiEvento(Evento evento) {
        eventi.add(evento);
        salvaSuFile();
    }

    public void rimuoviEvento(Evento evento) {
        eventi.remove(evento);
        salvaSuFile();
    }

    public void clear() {
        eventi.clear();
        salvaSuFile();
    }

    private void salvaSuFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(eventi);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<Evento> caricaDaFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Evento>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore nel caricamento, creo una nuova lista.");
            return new ArrayList<>();
        }
    }

}