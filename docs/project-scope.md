# Project Scope

## 1. Obiettivo del progetto

Civitas è un'applicazione web Java MVC che permette agli utenti di creare e gestire micro-nazioni immaginarie.

L'applicazione consente agli utenti di:
- registrarsi e autenticarsi;
- creare una micro-nazione;
- unirsi a micro-nazioni esistenti;
- consultare la scheda pubblica di una micro-nazione;
- visualizzare cittadini e ruoli interni;
- pubblicare comunicati ufficiali, se autorizzati;
- proporre leggi;
- votare leggi;
- consultare lo storico decisionale della micro-nazione;
- visualizzare una dashboard con statistiche sintetiche.


## 2. Visione funzionale

Civitas non deve essere trattato come un gioco gestionale complesso, ma come una piattaforma web di governance simulata.

La logica centrale è:

> Un utente può fondare o unirsi a una micro-nazione. All'interno della micro-nazione può assumere un ruolo, pubblicare contenuti se autorizzato, proporre leggi, votare e partecipare allo stato decisionale della comunità.

Il valore tecnico del progetto deriva da:
- gestione di utenti e sessioni;
- appartenenza molti-a-molti tra utenti e micro-nazioni;
- ruoli locali alla micro-nazione;
- workflow delle leggi;
- votazioni con vincoli di unicità;
- storico persistente delle decisioni;
- dashboard statistica tramite endpoint JSON e JavaScript.

## 3. Stack tecnologico

Il progetto utilizza tecnologie coerenti con il corso TIW:
- Java Servlet;
- JSP;
- JDBC;
- database relazionale;
- HTML;
- CSS;
- JavaScript;
- Maven;
- architettura MVC / 3-tier.


## 4. Architettura generale

Il progetto segue una separazione a tre livelli.

### 4.1 Presentation Layer

Responsabilità:
- rendering delle pagine;
- visualizzazione dei dati;
- form HTML;
- CSS;
- JavaScript leggero;
- chiamate AJAX alla dashboard.

Componenti:
- JSP;
- HTML;
- CSS;
- JavaScript.

Le JSP non devono contenere SQL, JDBC o logica applicativa complessa.

### 4.2 Business/Application Layer

Responsabilità:
- ricezione delle request HTTP;
- validazione input;
- gestione sessione;
- controllo autorizzazioni;
- orchestrazione delle operazioni;
- scelta tra forward e redirect;
- chiamata a DAO e servizi applicativi.

Componenti:
- Servlet;
- Service semplici;
- classi di validazione.

Le Servlet non devono contenere SQL diretto né generare HTML.

### 4.3 Data Access Layer

Responsabilità:
- accesso al database;
- query SQL;
- gestione PreparedStatement;
- gestione ResultSet;
- mapping da righe SQL a oggetti Java;
- controllo di vincoli dati quando opportuno.

Componenti:
- DAO;
- JDBC;
- SQL.


## 5. Scope Livello 1

Il Livello 1 realizza il nucleo funzionale dell'applicazione.

### 5.1 Funzionalità incluse

#### Autenticazione

- registrazione utente;
- login;
- logout;
- gestione sessione;
- protezione delle pagine riservate.

#### Micro-nazioni

- creazione di una micro-nazione con:
  - nome;
  - motto;
  - descrizione;
  - simbolo o bandiera testuale;
- visualizzazione elenco micro-nazioni;
- visualizzazione dettaglio micro-nazione;
- visualizzazione cittadini.

#### Membership

- possibilità per un utente autenticato di unirsi a una micro-nazione;
- assegnazione automatica del ruolo `CITIZEN`;
- assegnazione automatica del ruolo `FOUNDER` al creatore della micro-nazione;
- impedimento di doppia iscrizione alla stessa micro-nazione.

#### Comunicati ufficiali

- creazione di comunicati ufficiali;
- visualizzazione dei comunicati nella scheda della micro-nazione;
- pubblicazione consentita solo a utenti autorizzati.

### 5.2 Obiettivo Livello 1

Realizzare una piattaforma base per creare comunità fittizie, gestire cittadini e pubblicare informazioni ufficiali.

## 6. Scope Livello 2

Il Livello 2 introduce la parte più significativa del progetto: ruoli, leggi, votazioni e stato persistente.

### 6.1 Funzionalità incluse

#### Ruoli interni

Ruoli previsti:
- `FOUNDER`;
- `MINISTER`;
- `CITIZEN`.

I ruoli sono locali alla micro-nazione. Lo stesso utente può avere ruoli diversi in micro-nazioni diverse.

#### Gestione leggi

- creazione proposta di legge;
- visualizzazione leggi di una micro-nazione;
- dettaglio legge;
- stati della legge:
  - `PROPOSED`;
  - `APPROVED`;
  - `REJECTED`;
  - `REPEALED`.

#### Votazioni

- voto favorevole;
- voto contrario;
- astensione;
- un solo voto per utente per ogni legge;
- conteggio voti;
- chiusura votazione;
- aggiornamento stato legge.

#### Registro storico decisionale

- salvataggio degli eventi rilevanti;
- proposta legge;
- voto chiuso;
- legge approvata;
- legge respinta;
- legge abrogata;
- comunicato ufficiale pubblicato.

#### Risorse simboliche

Ogni micro-nazione possiede risorse simboliche:

- monete;
- punti cultura;
- punti energia.

Le risorse sono usate nella dashboard e nella classifica.

#### Classifica micro-nazioni

- elenco micro-nazioni ordinate per punteggio sintetico;
- punteggio calcolato sulla base di cittadini, leggi approvate e risorse simboliche.

### 6.2 Obiettivo Livello 2

Introdurre ruoli, processi decisionali, votazioni, stati e storico persistente della micro-nazione.

## 7. Scope Livello 3

Per il Livello 3 viene scelta una sola estensione significativa e coerente con TIW.

### 7.1 Estensione scelta

**Generazione automatica di un “giornale nazionale” periodico.**

### 7.2 Funzionalità incluse

- generazione di un giornale nazionale per ogni micro-nazione;
- raccolta automatica degli eventi più rilevanti avvenuti nella micro-nazione;
- inclusione delle leggi proposte, approvate o respinte;
- inclusione dei comunicati ufficiali più recenti;
- inclusione delle variazioni principali nelle risorse simboliche;
- visualizzazione del giornale nella pagina della micro-nazione;
- salvataggio persistente dei giornali generati nel database.

Contenuti previsti nel giornale:
- titolo del giornale;
- micro-nazione di riferimento;
- periodo di riferimento;
- riepilogo delle leggi discusse;
- riepilogo delle decisioni approvate o respinte;
- elenco dei comunicati ufficiali principali;
- sintesi dello stato della nazione;
- data di generazione.

### 7.3 Obiettivo Livello 3

Dimostrare la capacità di costruire una funzionalità applicativa avanzata che aggrega dati provenienti da più parti del sistema e li trasforma in un contenuto persistente e consultabile.

La generazione del giornale nazionale non introduce un framework moderno o una SPA, ma rimane coerente con l'architettura Servlet/JSP/JDBC. Le Servlet orchestrano la generazione, i DAO recuperano e salvano i dati, mentre le JSP si occupano solo della visualizzazione del giornale.


## 8. Ruoli utente

### 8.1 Utente non autenticato

Può:
- vedere homepage;
- vedere elenco micro-nazioni pubbliche;
- vedere dettaglio pubblico di una micro-nazione;
- registrarsi;
- effettuare login.

Non può:
- creare micro-nazioni;
- unirsi a micro-nazioni;
- pubblicare comunicati;
- proporre leggi;
- votare.

### 8.2 Utente autenticato

Può:
- creare micro-nazioni;
- unirsi a micro-nazioni;
- vedere dashboard personale;
- consultare le micro-nazioni di cui è membro.

### 8.3 Cittadino

Può, all'interno della micro-nazione:
- visualizzare contenuti interni;
- proporre leggi, se previsto dallo scope implementativo;
- votare leggi aperte;
- consultare lo storico decisionale.

### 8.4 Ministro

Può:
- pubblicare comunicati ufficiali;
- proporre leggi;
- eventualmente chiudere votazioni, se autorizzato.

### 8.5 Fondatore

Può:
- pubblicare comunicati ufficiali;
- proporre leggi;
- chiudere votazioni;
- abrogare leggi;
- promuovere cittadini a ministri;
- gestire aspetti principali della micro-nazione.


## 9. Vincoli principali

### 9.1 Vincoli applicativi

- non è possibile creare una micro-nazione senza autenticazione;
- non è possibile unirsi due volte alla stessa micro-nazione;
- non è possibile votare una legge se non si è cittadini della micro-nazione;
- non è possibile votare due volte la stessa legge;
- non è possibile votare una legge già chiusa;
- non è possibile pubblicare comunicati senza ruolo autorizzato;
- non è possibile abrogare una legge senza ruolo autorizzato.

### 9.2 Vincoli database

- email utente unica;
- nome micro-nazione unico o almeno controllato;
- vincolo unico su `(user_id, nation_id)` nella membership;
- vincolo unico su `(law_id, user_id)` nei voti;
- foreign key tra entità correlate.

## 10. Sicurezza

Il progetto deve gestire almeno:
- password salvate in forma non testuale;
- controllo sessione;
- controllo autorizzazioni lato server;
- validazione input lato server;
- uso di `PreparedStatement` per evitare SQL injection;
- protezione delle JSP interne tramite `WEB-INF/views`;
- redirect dopo POST per evitare duplicazioni da refresh.

Il controllo lato JSP serve solo a migliorare l'interfaccia, ma non sostituisce il controllo lato Servlet.
