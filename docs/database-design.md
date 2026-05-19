# Database Design

## 1. Obiettivo del documento

Questo documento descrive il modello dati iniziale di Civitas, le entità principali, le relazioni e i vincoli fondamentali.

## 2. Entità principali

Le entità principali sono:
- utenti;
- micro-nazioni;
- appartenenze;
- comunicati;
- leggi;
- voti;
- storico decisionale;
- risorse simboliche;
- giornali nazionali.

## 3. Tabelle previste

### 3.1 users

Rappresenta gli utenti registrati.

Attributi principali:
- `id` PK;
- `username`;
- `email`;
- `password_hash`;
- `created_at`.

Vincoli:
- `email` unica;
- `username` unico.

### 3.2 nations

Rappresenta una micro-nazione.

Attributi principali:
- `id` PK;
- `name`;
- `motto`;
- `description`;
- `flag_symbol`;
- `founder_id` FK verso `users.id`;
- `created_at`.

Relazione:
- una nazione ha un fondatore;
- un utente può fondare più nazioni, se consentito dalla logica applicativa.

### 3.3 memberships

Rappresenta l'appartenenza di un utente a una micro-nazione.

Attributi principali:
- `id` PK;
- `user_id` FK verso `users.id`;
- `nation_id` FK verso `nations.id`;
- `role`;
- `joined_at`.

Ruoli previsti:
- `FOUNDER`;
- `MINISTER`;
- `CITIZEN`.

Vincoli:
- `UNIQUE(user_id, nation_id)`.

Motivazione:
Il ruolo non viene salvato direttamente in `users` perché lo stesso utente può avere ruoli diversi in micro-nazioni diverse.

### 3.4 announcements

Rappresenta i comunicati ufficiali di una micro-nazione.

Attributi principali:
- `id` PK;
- `nation_id` FK;
- `author_id` FK;
- `title`;
- `content`;
- `created_at`.

Relazione:
- una nazione può avere molti comunicati;
- un comunicato appartiene a una sola nazione.

### 3.5 laws

Rappresenta le leggi proposte in una micro-nazione.

Attributi principali:
- `id` PK;
- `nation_id` FK;
- `proposer_id` FK;
- `title`;
- `description`;
- `status`;
- `created_at`;
- `closed_at`.

Stati previsti:
- `PROPOSED`;
- `APPROVED`;
- `REJECTED`;
- `REPEALED`.

### 3.6 votes

Rappresenta il voto di un cittadino su una legge.

Attributi principali:
- `id` PK;
- `law_id` FK;
- `user_id` FK;
- `vote_value`;
- `created_at`.

Valori previsti:
- `YES`;
- `NO`;
- `ABSTAIN`.

Vincoli:
- `UNIQUE(law_id, user_id)`.

Motivazione:
Il vincolo impedisce a un utente di votare più volte la stessa legge.

### 3.7 decision_logs

Rappresenta lo storico degli eventi rilevanti.

Attributi principali:
- `id` PK;
- `nation_id` FK;
- `law_id` FK nullable;
- `actor_id` FK nullable;
- `action`;
- `description`;
- `created_at`.

Uso:
- proposta legge;
- chiusura votazione;
- approvazione o respingimento;
- abrogazione;
- generazione giornale.

### 3.8 nation_resources

Rappresenta le risorse simboliche di una micro-nazione.

Attributi principali:
- `nation_id` PK/FK;
- `coins`;
- `culture_points`;
- `energy_points`.

Relazione:
- una micro-nazione ha un solo record di risorse simboliche.

### 3.9 national_newspapers

Rappresenta i giornali nazionali generati dal sistema.

Attributi principali:
- `id` PK;
- `nation_id` FK;
- `title`;
- `period_start`;
- `period_end`;
- `content`;
- `generated_at`.

Relazione:
- una micro-nazione può avere molti giornali nazionali.

## 4. Relazioni principali

```text
users 1---N nations
users N---N nations tramite memberships
nations 1---N announcements
nations 1---N laws
laws 1---N votes
nations 1---N decision_logs
nations 1---1 nation_resources
nations 1---N national_newspapers
```

## 5. Vincoli fondamentali

- email utente unica;
- username unico;
- una sola membership per coppia utente/nazione;
- un solo voto per coppia utente/legge;
- stati gestiti con valori controllati;
- foreign key per mantenere consistenza referenziale.

## 6. Scelte progettuali

Il modello separa utenti, ruoli e appartenenze per gestire correttamente il caso in cui uno stesso utente partecipi a più micro-nazioni.

Lo storico decisionale è salvato in una tabella dedicata per non perdere gli eventi avvenuti anche quando lo stato corrente delle entità cambia.
