# Manual Tests - Level 1

## 1. Obiettivo

Questo documento raccoglie i test manuali da eseguire prima di chiudere la release di Livello 1.

Ambiente di test:
```text
Jetty
localhost:8080
MySQL civitas_db
```

## 2. Preparazione ambiente

Avviare MySQL:
```bash
sudo systemctl start mysql
sudo systemctl status mysql
```

Ricreare lo schema:
```bash
sudo mysql < database/schema.sql
```

Verificare le tabelle:
```bash
sudo mysql -e "USE civitas_db; SHOW TABLES;"
```

Avviare l'applicazione con Jetty e aprire:
```text
localhost:8080
```

## 3. Test autenticazione

### 3.1 Registrazione valida

Passi:
1. Aprire `/register`.
2. Inserire username valido.
3. Inserire email non ancora usata.
4. Inserire password valida.
5. Inviare il form.

Risultato atteso:
- utente creato nel database;
- redirect alla home;
- utente mostrato come autenticato.

Query:
```bash
sudo mysql -e "USE civitas_db; SELECT id, username, email, created_at FROM users;"
```

### 3.2 Registrazione con email duplicata

Passi:
1. Aprire `/register`.
2. Usare una email già registrata.
3. Inviare il form.

Risultato atteso:
- messaggio di errore;
- username ed email preservati;
- password non preservata;
- nessun nuovo utente creato.

### 3.3 Login valido

Passi:
1. Aprire `/login`.
2. Inserire email e password corrette.
3. Inviare il form.

Risultato atteso:
- utente salvato in sessione;
- redirect alla home;
- username visibile nella home.

### 3.4 Login non valido

Passi:
1. Aprire `/login`.
2. Inserire email valida e password errata.
3. Inviare il form.

Risultato atteso:
- messaggio di errore;
- email preservata;
- password non preservata;
- nessuna sessione autenticata.

### 3.5 Logout

Passi:
1. Effettuare login.
2. Premere logout.

Risultato atteso:
- sessione invalidata;
- redirect a `/login`;
- utente non più autenticato.

## 4. Test micro-nazioni

### 4.1 Lista micro-nazioni

Passi:
1. Aprire `/nations`.

Risultato atteso:
- pagina accessibile anche da utente anonimo;
- lista delle micro-nazioni visibile;
- se vuota, messaggio coerente.

### 4.2 Creazione micro-nazione da anonimo

Passi:
1. Da utente non autenticato aprire `/nations/create`.

Risultato atteso:
- redirect a `/login`.

### 4.3 Creazione micro-nazione valida

Passi:
1. Effettuare login.
2. Aprire `/nations/create`.
3. Inserire nome, motto, descrizione e bandiera.
4. Inviare il form.

Risultato atteso:
- creazione record in `nations`;
- creazione membership `FOUNDER`;
- inizializzazione `nation_resources`;
- redirect a `/nation?id=...`.

Query:
```bash
sudo mysql -e "USE civitas_db; SELECT id, name, founder_id FROM nations;"
sudo mysql -e "USE civitas_db; SELECT user_id, nation_id, role FROM memberships;"
sudo mysql -e "USE civitas_db; SELECT * FROM nation_resources;"
```

### 4.4 Nome micro-nazione duplicato

Passi:
1. Creare una micro-nazione con nome già esistente.

Risultato atteso:
- messaggio di errore;
- nessuna nuova nazione duplicata.

## 5. Test membership

### 5.1 Adesione utente loggato

Passi:
1. Effettuare login con un utente diverso dal fondatore.
2. Aprire `/nation?id=...`.
3. Premere "Unisciti alla micro-nazione".

Risultato atteso:
- nuova membership `CITIZEN`;
- utente visibile nella lista cittadini.

Query:
```bash
sudo mysql -e "USE civitas_db; SELECT user_id, nation_id, role, joined_at FROM memberships;"
```

### 5.2 Prevenzione membership duplicata

Passi:
1. Provare a unirsi alla stessa micro-nazione una seconda volta.

Risultato atteso:
- nessuna membership duplicata;
- messaggio o stato coerente nella pagina.

## 6. Test comunicati ufficiali

### 6.1 Visualizzazione comunicati

Passi:
1. Aprire `/nation?id=...`.

Risultato atteso:
- se presenti, i comunicati sono visibili;
- se assenti, compare un messaggio coerente.

### 6.2 Creazione comunicato da FOUNDER

Passi:
1. Accedere come fondatore della micro-nazione.
2. Aprire `/announcements/create?nationId=...`.
3. Inserire titolo e contenuto validi.
4. Inviare il form.

Risultato atteso:
- comunicato salvato in `announcements`;
- redirect alla scheda della nazione;
- comunicato visibile nella pagina.

Query:
```bash
sudo mysql -e "USE civitas_db; SELECT id, nation_id, author_id, title, created_at FROM announcements;"
```

### 6.3 Creazione comunicato da CITIZEN

Passi:
1. Accedere come cittadino semplice.
2. Provare ad aprire `/announcements/create?nationId=...`.

Risultato atteso:
- accesso negato;
- errore 403;
- nessun comunicato creato.

### 6.4 Creazione comunicato da anonimo

Passi:
1. Da utente anonimo aprire `/announcements/create?nationId=...`.

Risultato atteso:
- redirect a `/login`.

## 7. Test error handling

### 7.1 Nation ID non valido

URL:
```text
/nation?id=abc
```

Risultato atteso:
- errore 400;
- pagina errore coerente.

### 7.2 Nation inesistente

URL:
```text
/nation?id=999999
```

Risultato atteso:
- errore 404;
- pagina errore coerente.

## 8. Test finale

Eseguire:
```bash
mvn clean package
```

Risultato atteso:
```text
BUILD SUCCESS
```