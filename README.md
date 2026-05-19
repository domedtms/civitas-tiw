# Civitas - Governo di Micro-Nazioni Immaginarie

Civitas è un'applicazione web Java MVC sviluppata per il progetto di Tecnologie Informatiche per il Web.

L'applicazione permette agli utenti di creare e gestire micro-nazioni immaginarie, unirsi come cittadini, pubblicare comunicati ufficiali, proporre leggi, votarle e consultare lo storico decisionale della comunità.

## Stack tecnologico

- Java Servlet
- JSP
- JDBC
- Database relazionale
- HTML
- CSS
- JavaScript
- Maven
- Architettura MVC / 3-tier

## Architettura

Il progetto segue una separazione a livelli:

- Presentation Layer: JSP, HTML, CSS, JavaScript
- Business Logic Layer: Servlet e Service
- Data Access Layer: DAO, JDBC, SQL

## Livelli funzionali previsti

### Livello 1

- Registrazione e autenticazione utenti
- Creazione micro-nazioni
- Adesione a micro-nazioni
- Visualizzazione scheda pubblica
- Pubblicazione comunicati ufficiali
- Visualizzazione cittadini

### Livello 2

- Ruoli interni: fondatore, ministro, cittadino
- Proposta di leggi
- Votazione delle leggi
- Stati delle leggi
- Registro storico decisionale
- Risorse simboliche
- Classifica micro-nazioni

### Livello 3 previsto

- Dashboard dello stato della nazione
- Endpoint JSON consultabili via JavaScript/AJAX

## Stato progetto

Versione iniziale: v0.1  
Fase corrente: setup repository e progettazione architetturale.