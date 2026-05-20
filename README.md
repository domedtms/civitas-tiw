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

## Avvio locale

### Requisiti

- Java 17
- Maven
- MySQL Server
- Jetty

### Setup database

Avviare MySQL:
```bash
sudo systemctl start mysql
```

Creare lo schema:
```bash
sudo mysql < database/schema.sql
```

Verificare le tabelle:
```bash
sudo mysql -e "USE civitas_db; SHOW TABLES;"
```

### Configurazione database

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

Il file `db.properties` non deve essere versionato.

### Build

```bash
mvn clean package
```

### Esecuzione locale

L'applicazione è stata testata localmente con Jetty su:
```text
localhost:8080
```

tramite:
```bash
mvn jetty:run
```


## Credenziali di test

Le credenziali possono essere create tramite la pagina di registrazione.

Esempio consigliato:

```text
Founder
email: founder@test.it
password: Password123

Citizen
email: citizen@test.it
password: Password123
```

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