l progetto è un'applicazione desktop di gestione aziendale denominata OmniManage, sviluppata in Java con JavaFX. È concepito come un sistema ERP/CRM leggero per piccole e medie imprese, con particolare attenzione all'usabilità dell'interfaccia (come suggerisce il titolo).

Le sue funzionalità chiave includono:
Gestione Anagrafiche: Moduli dedicati per l'inserimento, la modifica e la consultazione di Clienti, Fornitori e Prodotti (Magazzino).
Contabilità e Utilità: Strumenti per l'emissione di fatture con calcolo di IVA e gestione sconti, con la possibilità di generare documenti PDF (tramite la libreria OpenPDF). Include anche un'utilità di calendario per la gestione degli eventi.
Analisi Dati: Sezioni Dashboard con grafici (BarChart e PieChart) per visualizzare statistiche aggregate su clienti (es. per città), prodotti (quantità e valore per categoria) e fornitori.
Infrastruttura: I dati sono gestiti localmente tramite un database SQLite. L'accesso è sicuro, con funzioni di login e registrazione che utilizzano il criptaggio delle password con BCrypt.
Supporto AI: L'integrazione di un chatbot, PippoBot, che sfrutta l'API Gemini per fornire assistenza agli utenti all'interno dell'applicazione.
In sintesi, l'applicazione offre una soluzione modulare e completa per le necessità operative e analitiche di un'attività commerciale.
