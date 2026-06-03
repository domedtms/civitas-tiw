# Level 2 Test Plan — Civitas

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

## 4. Credenziali di test

### Founder
```text
Email: founder@civitas.test
Password: Password123!
```

### Minister
```text
Email: minister@civitas.test
Password: Password123!
```

### Citizen
```text
Email: citizen@civitas.test
Password: Password123!
```

## 5. Test ruoli
- accedere come founder;
- aprire una micro-nazione;
- verificare lista cittadini;
- promuovere un citizen a minister;
- verificare ruolo `MINISTER`;
- verificare evento `ROLE_UPDATED` in `decision_logs`;
- accedere come minister;
- verificare permessi da minister;
- degradare minister a citizen;
- verificare rimozione permessi;
- verificare che il founder non sia modificabile.

## 6. Test proposta legge
- accedere come membro della micro-nazione;
- aprire pagina dettaglio micro-nazione;
- cliccare “Proponi legge”;
- creare una legge valida;
- verificare stato iniziale `PROPOSED`;
- verificare evento `LAW_PROPOSED`;
- verificare legge visibile nella scheda micro-nazione;
- verificare pagina dettaglio legge.

## 7. Test votazione
- accedere come membro;
- aprire una legge `PROPOSED`;
- votare `YES`, `NO` oppure `ABSTAIN`;
- verificare conteggi;
- tentare voto duplicato;
- verificare blocco voto duplicato;
- verificare tabella `votes`.

## 8. Test chiusura votazione
- accedere come founder o minister;
- aprire una legge `PROPOSED`;
- chiudere la votazione;
- verificare:
  - `YES > NO` produce `APPROVED`;
  - `NO >= YES` produce `REJECTED`;
  - `closed_at` valorizzato;
  - evento `LAW_APPROVED` o `LAW_REJECTED`;
  - legge chiusa non votabile.

## 9. Test abrogazione
- creare o usare una legge `APPROVED`;
- accedere come founder o minister;
- abrogare la legge;
- verificare stato `REPEALED`;
- verificare evento `LAW_REPEALED`;
- verificare che la legge non sia votabile;
- verificare che non sia chiudibile di nuovo.

## 10. Test risorse simboliche
Verificare le regole:
- nuova micro-nazione: `coins = 100`;
- legge approvata: `coins +20`, `culture_points +10`, `energy_points +5`;
- legge respinta: `coins -5`, `energy_points -2`;
- legge abrogata: `coins -10`, `culture_points -5`;
- comunicato ufficiale: `coins +3`, `culture_points +2`.

Verificare che:
- le risorse non vadano sotto zero;
- le risorse siano mostrate nella scheda micro-nazione;
- gli eventi `RESOURCE_UPDATED` siano salvati in `decision_logs`.

## 11. Test storico decisionale
- aprire `/nation/history?id=...`;
- verificare eventi ordinati dal più recente;
- verificare eventi:
  - `LAW_PROPOSED`;
  - `LAW_APPROVED`;
  - `LAW_REJECTED`;
  - `LAW_REPEALED`;
  - `RESOURCE_UPDATED`;
  - `ROLE_UPDATED`;
- verificare stato vuoto;
- verificare id non valido;
- verificare nazione inesistente.

## 12. Test classifica
- aprire `/ranking`;
- verificare ordinamento per score decrescente;
- verificare formula:

```text
score = coins + culture_points + energy_points + approved_laws * 5 + citizens_count * 2
```

- verificare link alla scheda micro-nazione;
- verificare coerenza con dati database.

## 13. Test sicurezza e autorizzazioni
- utente anonimo non può votare;
- utente anonimo non può proporre leggi;
- utente anonimo non può chiudere votazioni;
- citizen non può chiudere votazioni;
- citizen non può abrogare leggi;
- citizen non può gestire ruoli;
- minister può pubblicare comunicati;
- minister può chiudere votazioni;
- minister può abrogare leggi;
- solo founder può gestire ruoli.