# Architecture

## 1. Obiettivo del documento

Questo documento definisce l'architettura applicativa di Civitas e la separazione delle responsabilità tra i livelli del sistema.

## 2. Architettura generale

Civitas segue un'architettura MVC / 3-tier coerente con Servlet, JSP e JDBC.

```text
Presentation Layer -> Business Logic Layer -> Data Access Layer -> Database
```

## 3. Presentation Layer

Contiene le parti responsabili della visualizzazione e dell'interazione utente.

Componenti:
- JSP;
- HTML;
- CSS;
- JavaScript.

Responsabilità:
- mostrare dati ricevuti dalle Servlet;
- presentare form e messaggi;
- non contenere SQL;
- non contenere logica applicativa complessa;
- non accedere direttamente al database.


## 4. Business Logic Layer

Contiene il controllo del flusso applicativo.

Componenti:
- Servlet;
- Service semplici;
- classi di validazione;
- logica di autorizzazione.

Responsabilità delle Servlet:
- ricevere request HTTP;
- leggere e validare parametri;
- controllare sessione e permessi;
- chiamare Service o DAO;
- scegliere tra forward e redirect;
- preparare attributi per le JSP.

Le Servlet non devono generare HTML e non devono contenere SQL.

## 5. Data Access Layer

Contiene l'accesso ai dati persistenti.

Componenti:
- DAO;
- JDBC;
- query SQL;
- mapping tra ResultSet e oggetti Java.

Responsabilità:
- eseguire query;
- gestire PreparedStatement e ResultSet;
- costruire oggetti model;
- nascondere i dettagli SQL al resto dell'applicazione.

Le Servlet non devono usare direttamente JDBC.

## 6. Model

Il package `model` contiene classi semplici che rappresentano le entità principali del dominio.

Esempi:
- `User`;
- `Nation`;
- `Membership`;
- `Announcement`;
- `Law`;
- `Vote`;
- `DecisionLog`;
- `NationalNewspaper`.

I model non devono contenere logica di accesso al database.

## 7. Package Java

Struttura prevista:
```text
it.polimi.tiw.civitas
├── controller
├── dao
├── model
├── service
└── util
```

Significato:
- `controller`: Servlet;
- `dao`: classi JDBC;
- `model`: entità del dominio;
- `service`: logica applicativa non banale;
- `util`: classi di supporto.

## 8. Uso dei Service

I Service vengono usati solo quando una logica non appartiene chiaramente né alla Servlet né al DAO.

Esempi coerenti:
- controllo autorizzazioni;
- transizione degli stati di una legge;
- generazione del giornale nazionale;
- validazioni applicative complesse.

Non bisogna creare Service inutili solo per rendere il progetto più enterprise.

## 9. Principi architetturali

- Le JSP visualizzano dati.
- Le Servlet orchestrano il flusso.
- I DAO accedono al database.
- I Service contengono logica applicativa riusabile.
- Le autorizzazioni vengono controllate lato server.
- Gli input vengono sempre validati lato server.
