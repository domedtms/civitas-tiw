# Level 3 Test Plan

## 1. Ambiente

- Java 17
- Maven
- MySQL Server
- Jetty su `localhost:8080`

## 2. Setup database

```bash
sudo mysql < database/schema.sql
sudo mysql < database/seed.sql
```

## 3. Build

```bash
mvn clean package
```

Risultato atteso: `BUILD SUCCESS`

## 4. Funzionalità Livello 3

Il Livello 3 include:
- dashboard dello stato della nazione;
- endpoint JSON per statistiche aggregate;
- caricamento AJAX tramite `fetch()`;
- generazione automatica di un giornale nazionale periodico;
- archivio giornali;
- dettaglio giornale;
- blocco generazione giornali per periodi futuri;
- blocco duplicati per stesso periodo e stessa micro-nazione.

## 5. Test endpoint JSON

Verificare:
```text
/api/nation/stats?id=1
/api/nation/stats?id=abc
/api/nation/stats?id=999999
```

Risultato atteso:
- nazione valida: risposta JSON;
- id non valido: errore 400;
- nazione inesistente: errore 404.

La risposta JSON deve includere:
- cittadini;
- ruoli;
- leggi per stato;
- comunicati;
- eventi decisionali;
- coins;
- culture points;
- energy points;
- score ranking.

## 6. Test dashboard AJAX

Aprire:
```text
/nation/dashboard?id=1
```

Verificare:
- caricamento pagina JSP;
- messaggio di loading;
- chiamata `fetch()` a `/api/nation/stats?id=1`;
- risposta JSON in DevTools;
- aggiornamento dinamico del DOM;
- visualizzazione popolazione;
- visualizzazione ruoli;
- visualizzazione leggi;
- visualizzazione risorse;
- visualizzazione comunicati;
- visualizzazione eventi decisionali;
- visualizzazione score;
- sintesi testuale automatica.

## 7. Test generazione giornale nazionale

Accedere come `FOUNDER` o `MINISTER`.

Aprire:
```text
/nation/newspapers/generate?nationId=1
```

Verificare:
- form generazione visibile;
- periodo in formato `YYYY-MM`;
- periodo massimo uguale al mese corrente;
- generazione consentita per mese corrente;
- generazione consentita per mesi precedenti;
- generazione non consentita per mesi futuri;
- generazione duplicata bloccata per stesso periodo;
- redirect al dettaglio giornale dopo generazione.

## 8. Test autorizzazioni giornale

Verificare:
- `FOUNDER` può generare giornali;
- `MINISTER` può generare giornali;
- `CITIZEN` non può generare giornali;
- utente anonimo viene reindirizzato al login;
- non membri ricevono errore 403.

## 9. Test archivio giornali

Aprire:
```text
/nation/newspapers?nationId=1
```

Verificare:
- lista giornali della micro-nazione;
- ordinamento per periodo decrescente;
- stato vuoto per micro-nazioni senza giornali;
- link al dettaglio giornale;
- link alla dashboard;
- link alla scheda micro-nazione;
- pulsante genera giornale se utente autorizzato.

## 10. Test dettaglio giornale

Aprire:
```text
/nation/newspaper?id=...
```

Verificare presenza di:
- titolo;
- periodo;
- micro-nazione;
- utente generatore;
- data generazione;
- editoriale;
- stato politico;
- risorse simboliche;
- attività legislativa;
- comunicati ufficiali;
- storico decisionale.

## 11. Test error handling giornali

Verificare:
```text
/nation/newspapers?nationId=abc
/nation/newspapers?nationId=999999
/nation/newspaper?id=abc
/nation/newspaper?id=999999
/nation/newspapers/generate?nationId=abc
/nation/newspapers/generate?nationId=999999
```

Risultato atteso:
- id non valido: 400;
- risorsa inesistente: 404;
- utente non autorizzato: 403;
- utente anonimo su generazione: redirect login.

## 12. Test navigazione Livello 3

Verificare navigazione tra:
- scheda micro-nazione;
- dashboard;
- storico decisionale;
- archivio giornali;
- dettaglio giornale;
- classifica;
- generazione giornale.

## 13. Regression completa Livelli 1 e 2

Ritestare almeno:
- registrazione;
- login;
- logout;
- creazione micro-nazione;
- adesione;
- lista cittadini;
- gestione ministro;
- comunicati ufficiali;
- proposta legge;
- voto legge;
- chiusura votazione;
- abrogazione legge;
- storico decisionale;
- risorse simboliche;
- classifica.

## 14. Controllo architetturale

Verificare che:
- le JSP non contengano SQL;
- le Servlet non contengano JDBC diretto;
- i DAO contengano SQL;
- i Service contengano business logic;
- JavaScript sia usato solo per la dashboard AJAX;
- il giornale venga generato lato server;
- il progetto non sia una SPA;
- il progetto sia coerente con Servlet/JSP/JDBC.
