# HTTP Flows

## 1. Obiettivo del documento

Questo documento descrive i principali flussi HTTP dell'applicazione, distinguendo GET, POST, forward, redirect e uso della sessione.

## 2. Regole generali

### GET

Usato per:
- visualizzare pagine;
- mostrare form;
- consultare dettagli;
- leggere dati senza modificarli.

### POST

Usato per:
- creare dati;
- modificare dati;
- eseguire azioni;
- inviare form.

## 3. Forward

Il forward viene usato quando la Servlet prepara dati e passa il controllo a una JSP.

## 4. Redirect

Il redirect viene usato dopo una POST conclusa con successo.

Motivazione:
evita il reinvio del form se l'utente aggiorna la pagina.

## 5. Sessione

La sessione viene usata per mantenere l'utente autenticato.

Dato principale:
```text
session.setAttribute("user", loggedUser)
```

Controlli tipici:
- se l'utente non è autenticato, redirect a `/login`;
- se l'utente non ha permessi, errore 403 o pagina di errore;
- al logout, invalidazione della sessione.

## 6. Flusso login

```text
GET /login
→ mostra login.jsp

POST /login
→ valida email e password
→ se corrette: salva user in sessione
→ redirect a /nations
→ se errate: forward a login.jsp con messaggio errore
```

## 7. Flusso creazione micro-nazione

```text
GET /nation/create
→ verifica autenticazione
→ mostra form create-nation.jsp

POST /nation/create
→ verifica autenticazione
→ valida input
→ crea nazione
→ crea membership con ruolo FOUNDER
→ crea record risorse iniziali
→ redirect a /nation?id={newNationId}
```

## 8. Flusso adesione a micro-nazione

```text
POST /nation/join
→ verifica autenticazione
→ legge nationId
→ controlla che la nazione esista
→ controlla che l'utente non sia già membro
→ crea membership con ruolo CITIZEN
→ redirect a /nation?id={nationId}
```

## 9. Flusso pubblicazione comunicato

```text
GET /announcement/create?nationId=3
→ verifica autenticazione
→ verifica ruolo autorizzato
→ mostra form

POST /announcement/create
→ verifica autenticazione
→ verifica ruolo autorizzato
→ valida titolo e contenuto
→ salva comunicato
→ redirect a /nation?id=3
```

## 10. Flusso proposta legge

```text
GET /law/create?nationId=3
→ verifica autenticazione
→ verifica membership
→ mostra form

POST /law/create
→ verifica autenticazione
→ verifica membership
→ valida dati
→ crea legge in stato PROPOSED
→ registra evento nello storico
→ redirect a /law?id={lawId}
```

## 11. Flusso voto legge

```text
POST /law/vote
→ verifica autenticazione
→ verifica membership nella nazione
→ controlla che la legge sia in stato PROPOSED
→ controlla che l'utente non abbia già votato
→ salva voto
→ redirect a /law?id={lawId}
```

## 12. Flusso generazione giornale nazionale

```text
POST /newspaper/generate
→ verifica autenticazione
→ verifica ruolo autorizzato
→ legge nationId e periodo
→ recupera eventi, leggi, comunicati e risorse
→ genera contenuto del giornale
→ salva il giornale nel database
→ redirect a /newspaper?id={newspaperId}
```

## 13. Errori HTTP principali

- `400 Bad Request`: parametri mancanti o non validi;
- `401 Unauthorized`: utente non autenticato;
- `403 Forbidden`: utente autenticato ma non autorizzato;
- `404 Not Found`: risorsa non esistente;
- `500 Internal Server Error`: errore inatteso lato server.
