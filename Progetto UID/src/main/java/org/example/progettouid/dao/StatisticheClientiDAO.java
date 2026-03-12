package org.example.progettouid.dao;

import org.example.progettouid.model.Cliente;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticheClientiDAO {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    public int countClienti() {
        return clienteDAO.countClienti();
    }

    public Map<String, Integer> getClientiPerCitta() {
        Map<String, Integer> mappa = new HashMap<>();
        List<Cliente> clienti = clienteDAO.getClienti();
        for (Cliente c : clienti) {
            String citta = c.getCitta() != null ? c.getCitta() : "Non specificata";
            mappa.put(citta, mappa.getOrDefault(citta, 0) + 1);
        }
        return mappa;
    }

    public Map<String, Integer> getClientiPerNome() {
        Map<String, Integer> mappa = new HashMap<>();
        List<Cliente> clienti = clienteDAO.getClienti();
        for (Cliente c : clienti) {
            String nomeCompleto = c.getNome() + " " + c.getCognome();
            mappa.put(nomeCompleto, 1);
        }
        return mappa;
    }
}