# Demo orale Civitas — Regno dei Bug

## Obiettivo della demo

Il **Regno dei Bug** deve essere usato come micro-nazione completa per mostrare al professore il funzionamento end-to-end di Civitas.

La demo deve dimostrare il ciclo completo:

```text
creazione micro-nazione
→ adesione utenti
→ gestione ruoli
→ comunicati
→ proposta legge
→ voto
→ chiusura votazione
→ aggiornamento risorse
→ storico decisionale
→ dashboard AJAX
→ ranking
→ generazione giornale nazionale
→ archivio e dettaglio giornale
```

---

# 1. Identità della micro-nazione

## Nome

```text
Regno dei Bug
```

## Bandiera

```text
🐞
```

## Descrizione consigliata

```text
Micro-nazione tecnologica fondata sulla correzione collaborativa degli errori, sulla stabilità del sistema e sulla partecipazione dei cittadini alla manutenzione del codice civico.
```

---

# 2. Utenti da usare

Usare sempre i tre account demo:

## Founder

```text
email: founder@test.it
password: Password123
```

## Minister

```text
email: minister@test.it
password: Password123
```

## Citizen

```text
email: citizen@test.it
password: Password123
```

---

# 3. Preparazione ruoli

## Operazioni da fare

1. Accedere come `minister@test.it`.
2. Entrare nel **Regno dei Bug**.
3. Fare join.
4. Eseguire logout.

Poi:

1. Accedere come `citizen@test.it`.
2. Entrare nel **Regno dei Bug**.
3. Fare join.
4. Eseguire logout.

Poi:

1. Accedere come `founder@test.it`.
2. Entrare nel **Regno dei Bug**.
3. Promuovere `minister` a `MINISTER`.

## Evento generato nello storico

```text
ROLE_UPDATED
```

---

# 4. Comunicati ufficiali

Creare almeno **due comunicati ufficiali**.

Servono per mostrare:

- gestione comunicati;
- permessi Founder/Minister;
- aggiornamento risorse;
- storico decisionale;
- contenuto usato poi nel giornale nazionale.

---

## Comunicato 1

### Titolo

```text
Apertura del Regno dei Bug
```

### Contenuto

```text
Il governo comunica l’apertura ufficiale del Regno dei Bug. La micro-nazione nasce con l’obiettivo di migliorare la stabilità del sistema attraverso collaborazione, segnalazioni e decisioni condivise.
```

---

## Comunicato 2

### Titolo

```text
Piano di stabilizzazione del sistema
```

### Contenuto

```text
Il Ministero informa i cittadini che sono aperte le consultazioni per proporre leggi dedicate alla riduzione degli errori, alla manutenzione del codice civico e alla qualità delle decisioni nazionali.
```

---

## Eventi attesi

```text
RESOURCE_UPDATED
RESOURCE_UPDATED
```

---

# 5. Prima legge: approvata

Questa legge serve per mostrare il caso positivo del workflow legislativo.

## Legge 1

### Titolo

```text
Legge sul Debug Civico
```

### Descrizione

```text
Ogni cittadino può segnalare simbolicamente un bug istituzionale e proporre una soluzione migliorativa. Le segnalazioni vengono discusse pubblicamente e contribuiscono alla stabilità del Regno dei Bug.
```

## Voti consigliati

```text
founder  -> YES
minister -> YES
citizen  -> ABSTAIN
```

## Operazione finale

Chiudere la votazione come `founder` o `minister`.

## Risultato atteso

```text
APPROVED
```

## Eventi generati

```text
LAW_PROPOSED
LAW_APPROVED
RESOURCE_UPDATED
```

## Effetto sulle risorse

```text
coins +20
culture_points +10
energy_points +5
```

---

# 6. Seconda legge: respinta

Questa legge serve per mostrare il caso negativo del workflow.

## Legge 2

### Titolo

```text
Legge sul Riavvio Obbligatorio Giornaliero
```

### Descrizione

```text
Ogni cittadino dovrebbe sospendere le attività della micro-nazione una volta al giorno per simulare un riavvio completo del sistema istituzionale.
```

## Voti consigliati

```text
founder  -> NO
minister -> NO
citizen  -> YES
```

## Operazione finale

Chiudere la votazione come `founder` o `minister`.

## Risultato atteso

```text
REJECTED
```

## Eventi generati

```text
LAW_PROPOSED
LAW_REJECTED
RESOURCE_UPDATED
```

## Effetto sulle risorse

```text
coins -5
energy_points -2
```

## Frase pronta per l’orale

```text
In questo secondo caso mostro che il workflow non gestisce solo approvazioni, ma anche leggi respinte. La regola applicativa è semplice: se i voti YES non superano i voti NO, la proposta viene respinta.
```

---

# 7. Terza legge: approvata e poi abrogata

Questa legge serve per mostrare la transizione:

```text
APPROVED → REPEALED
```

---

## Legge 3

### Titolo

```text
Legge sulla Modalità Sicura
```

### Descrizione

```text
In caso di instabilità istituzionale, il Regno dei Bug può attivare una modalità sicura simbolica, limitando temporaneamente nuove decisioni e concentrandosi sulla manutenzione delle risorse nazionali.
```

## Voti consigliati

```text
founder  -> YES
minister -> YES
citizen  -> YES
```

## Prima operazione finale

Chiudere la votazione.

## Primo risultato atteso

```text
APPROVED
```

## Seconda operazione finale

Come `founder` o `minister`, abrogare la legge.

## Risultato finale

```text
REPEALED
```

## Eventi generati

```text
LAW_PROPOSED
LAW_APPROVED
RESOURCE_UPDATED
LAW_REPEALED
RESOURCE_UPDATED
```

## Effetto abrogazione

```text
coins -10
culture_points -5
```

---

# 8. Storico decisionale

Dopo le operazioni precedenti, aprire:

```text
/nation/history?id=ID_DEL_REGNO_DEI_BUG
```

## Eventi che devono essere visibili

```text
ROLE_UPDATED
RESOURCE_UPDATED
LAW_PROPOSED
LAW_APPROVED
LAW_REJECTED
LAW_REPEALED
```

---

# 9. Dashboard AJAX

Aprire:

```text
/nation/dashboard?id=ID_DEL_REGNO_DEI_BUG
```

## Dati da mostrare

La dashboard deve mostrare:

```text
- numero cittadini;
- founder;
- minister;
- citizen;
- leggi totali;
- leggi proposte;
- leggi approvate;
- leggi respinte;
- leggi abrogate;
- comunicati;
- eventi storici;
- coins;
- culture_points;
- energy_points;
- score.
```

## Flusso tecnico da spiegare

```text
Browser
→ /nation/dashboard?id=...
→ JSP base
→ nation-dashboard.js
→ fetch('/api/nation/stats?id=...')
→ NationStatsServlet
→ NationStatsService
→ NationStatsDAO
→ JSON
→ aggiornamento DOM
```

---

# 10. Ranking

Aprire:

```text
/ranking
```

## Formula da spiegare

```text
score = coins + culture_points + energy_points + approved_laws * 5 + citizens_count * 2
```

---

# 11. Giornale nazionale

Dopo aver creato comunicati, leggi e storico, generare il giornale.

Aprire:

```text
/nation/newspapers/generate?nationId=ID_DEL_REGNO_DEI_BUG
```

Generare il giornale per il mese corrente.

Poi aprire:

```text
/nation/newspapers?nationId=ID_DEL_REGNO_DEI_BUG
```

e poi il dettaglio:

```text
/nation/newspaper?id=ID_GIORNALE
```

---

# 12. Situazione demo finale ideale

Alla fine il **Regno dei Bug** dovrebbe avere:

```text
Utenti:
- founder: FOUNDER
- minister: MINISTER
- citizen: CITIZEN

Comunicati:
- Apertura del Regno dei Bug
- Piano di stabilizzazione del sistema

Leggi:
- Legge sul Debug Civico → APPROVED
- Legge sul Riavvio Obbligatorio Giornaliero → REJECTED
- Legge sulla Modalità Sicura → REPEALED

Storico:
- ROLE_UPDATED
- RESOURCE_UPDATED
- LAW_PROPOSED
- LAW_APPROVED
- LAW_REJECTED
- LAW_REPEALED

Livello 3:
- dashboard popolata
- ranking aggiornato
- giornale nazionale generato
- archivio giornali consultabile
```

---

# 13. Sequenza precisa da presentare al professore

Durante l’orale, partire dalla UI e non dal database.

---

## Step 1 — Lista micro-nazioni

Aprire:

```text
/nations
```

---

## Step 2 — Dettaglio Regno dei Bug

Aprire il dettaglio della micro-nazione.


---

## Step 3 — Ruoli

Mostrare Founder, Minister e Citizen.

---

## Step 4 — Leggi

Aprire una legge approvata, una respinta e una abrogata.

---

## Step 5 — Storico

Aprire lo storico decisionale.

---

## Step 6 — Dashboard AJAX

Aprire la dashboard.

---

## Step 7 — Ranking

Aprire il ranking.

---

## Step 8 — Giornale

Aprire archivio e dettaglio giornale.

---

# 14. Query finali per controllare il Regno dei Bug

Prima dell’orale, trovare l’ID della micro-nazione:

```bash
sudo mysql -e "USE civitas_db; SELECT id, name FROM nations;"
```

Poi sostituire l’ID nelle query successive.

---

## Controllo ruoli

```bash
sudo mysql -e "USE civitas_db; SELECT user_id, nation_id, role FROM memberships WHERE nation_id = ID_DEL_REGNO_DEI_BUG;"
```

## Controllo leggi

```bash
sudo mysql -e "USE civitas_db; SELECT id, title, status FROM laws WHERE nation_id = ID_DEL_REGNO_DEI_BUG;"
```

## Controllo storico

```bash
sudo mysql -e "USE civitas_db; SELECT action, description, created_at FROM decision_logs WHERE nation_id = ID_DEL_REGNO_DEI_BUG ORDER BY created_at DESC;"
```

## Controllo risorse

```bash
sudo mysql -e "USE civitas_db; SELECT * FROM nation_resources WHERE nation_id = ID_DEL_REGNO_DEI_BUG;"
```

## Controllo giornali

```bash
sudo mysql -e "USE civitas_db; SELECT id, period, title FROM national_newspapers WHERE nation_id = ID_DEL_REGNO_DEI_BUG;"
```
