# Civitas - Governo di Micro-Nazioni Immaginarie

Civitas è un'applicazione web Java MVC sviluppata per il progetto di **Tecnologie Informatiche per il Web**.

L'applicazione permette agli utenti di creare e gestire micro-nazioni immaginarie, unirsi come cittadini, pubblicare comunicati ufficiali, proporre leggi, votarle, consultare lo storico decisionale della comunità e visualizzare lo stato della nazione tramite dashboard e giornali nazionali generati automaticamente.

---

## Stack tecnologico

- Java Servlet
- JSP
- JDBC
- MySQL
- HTML
- CSS
- JavaScript
- Maven
- Jetty
- Architettura MVC / 3-tier

---

## Architettura

Il progetto segue una separazione a livelli coerente con il corso di Tecnologie Informatiche per il Web.

```text
Presentation Layer
- JSP
- HTML
- CSS
- JavaScript

Controller Layer
- Servlet

Business Logic Layer
- Service

Data Access Layer
- DAO
- JDBC
- SQL
```

Le principali regole architetturali adottate sono:

- le JSP si occupano della presentazione dei dati;
- le Servlet gestiscono richieste HTTP, sessione, forward e redirect;
- i Service contengono la logica applicativa;
- i DAO isolano l'accesso al database;
- le query SQL sono concentrate nei DAO;
- le operazioni modificative usano redirect dopo POST;
- gli input sono validati lato server;
- le query usano `PreparedStatement`.

---

## Avvio locale

### Requisiti

Per eseguire il progetto sono necessari:

- Java 17
- Maven
- MySQL Server
- Jetty tramite Maven plugin
- Browser web moderno

---

## Setup database

Avviare MySQL, se non già attivo:

```bash
sudo systemctl start mysql
```

Creare lo schema del database:

```bash
sudo mysql < database/schema.sql
```

Caricare i dati dimostrativi già pronti per la valutazione:

```bash
sudo mysql < database/demo_data.sql
```

Il file `schema.sql` ricrea la struttura del database, mentre `demo_data.sql` popola il sistema con utenti, micro-nazioni, ruoli, leggi, voti, storico decisionale, risorse simboliche e giornali nazionali di esempio.

Verificare le tabelle:

```bash
sudo mysql -e "USE civitas_db; SHOW TABLES;"
```

---

## Configurazione database

Creare il file locale:

```text
src/main/resources/db.properties
```

partendo da:

```text
src/main/resources/db.properties.example
```

Esempio:

```properties
db.url=jdbc:mysql://localhost:3306/civitas_db?serverTimezone=UTC
db.user=civitas_user
db.password=civitas_password
```

Il file `db.properties` contiene credenziali locali e non deve essere versionato.

---

## Build

Eseguire:

```bash
mvn clean package
```

---

## Esecuzione locale

Avviare l'applicazione con Jetty:

```bash
mvn jetty:run
```

L'applicazione sarà disponibile all'indirizzo:

```text
http://localhost:8080/civitas
```

---

## Credenziali di test

Il database dimostrativo contiene i seguenti account.

### Founder

```text
email: founder@test.it
password: Password123
```

### Minister

```text
email: minister@test.it
password: Password123
```

### Citizen

```text
email: citizen@test.it
password: Password123
```

Gli stessi account possono essere ricreati manualmente tramite la pagina di registrazione, ma per la valutazione è consigliato usare il file `database/demo_data.sql`.

---

## Dati dimostrativi

Il file `database/demo_data.sql` contiene uno scenario dimostrativo completo, utile per valutare le principali funzionalità dell'applicazione.

Sono presenti micro-nazioni di esempio, tra cui:

- Repubblica del Caffè
- Regno dei Bug

In particolare, il **Regno dei Bug** è stato popolato per mostrare il funzionamento completo del progetto:

- membri con ruoli `FOUNDER`, `MINISTER` e `CITIZEN`;
- comunicati ufficiali;
- leggi proposte;
- votazioni;
- leggi approvate;
- leggi respinte;
- leggi abrogate;
- storico decisionale;
- aggiornamento delle risorse simboliche;
- dashboard AJAX;
- ranking;
- giornale nazionale generato automaticamente.

---

## Percorso demo consigliato

Per valutare il progetto è possibile seguire questo percorso:

1. accedere con `founder@test.it`;
2. aprire la lista delle micro-nazioni;
3. entrare nel **Regno dei Bug**;
4. visualizzare membri e ruoli;
5. consultare comunicati e leggi;
6. aprire una legge approvata, una respinta e una abrogata;
7. consultare lo storico decisionale;
8. aprire la dashboard AJAX;
9. consultare il ranking;
10. aprire archivio e dettaglio del giornale nazionale.

Questo percorso permette di verificare il funzionamento completo dell'applicazione e delle estensioni di livello 3.

---

## Livelli funzionali implementati

### Livello 1 — Gestione base

- Registrazione e autenticazione utenti
- Login e logout
- Gestione sessione
- Creazione micro-nazioni
- Adesione a micro-nazioni
- Visualizzazione scheda pubblica
- Pubblicazione comunicati ufficiali
- Visualizzazione cittadini

### Livello 2 — Governo interno

- Ruoli interni: `FOUNDER`, `MINISTER`, `CITIZEN`
- Promozione di un cittadino a ministro
- Proposta di leggi
- Votazione delle leggi con `YES`, `NO`, `ABSTAIN`
- Blocco del voto duplicato
- Stati delle leggi: `PROPOSED`, `APPROVED`, `REJECTED`, `REPEALED`
- Chiusura delle votazioni
- Abrogazione delle leggi approvate
- Registro storico decisionale
- Risorse simboliche
- Classifica delle micro-nazioni

### Livello 3 — Estensioni avanzate

- Dashboard dello stato della nazione
- Endpoint JSON consultabile via JavaScript/AJAX
- Aggiornamento dinamico del DOM tramite `fetch()`
- Generazione automatica di giornale nazionale periodico
- Archivio dei giornali nazionali
- Dettaglio del giornale nazionale
- Blocco generazione giornali per periodi futuri
- Blocco duplicati per stessa micro-nazione e stesso periodo

---

## Note sui dati demo

Per rendere `demo_data.sql` portabile, il file:

- contiene `USE civitas_db;`;
- non contiene impostazioni globali MySQL come `GTID_PURGED`;
- deve essere importato dopo `schema.sql`.

Procedura completa consigliata:

```bash
sudo mysql < database/schema.sql
sudo mysql < database/demo_data.sql
mvn clean package
mvn jetty:run
```

---

## Stato progetto

Ultima versione: `v3.0.1`

Fase corrente: consegna finale del progetto

Release principale:

- `v1.0.0` — Level 1 Release
- `v2.0.0` — Level 2 Release
- `v3.0.0` — Final Project Release
- `v3.0.1` — Italian UI and Date Format Patch
