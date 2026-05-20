# Level 2 Design - Laws and Voting Workflow

## 1. Obiettivo

Questo documento definisce il workflow del Livello 2 di Civitas relativo a leggi, votazioni, storico decisionale, risorse simboliche e confronto tra micro-nazioni.

Il documento serve come riferimento prima dell'implementazione di model, DAO, Service, Servlet e JSP.

## 2. Scope Livello 2

Il Livello 2 introduce:
- ruoli interni;
- proposta di leggi;
- votazione delle leggi;
- stati delle leggi;
- registro storico delle decisioni;
- risorse simboliche;
- classifica o confronto tra micro-nazioni.

## 3. Ruoli

Ruoli già previsti:
- `FOUNDER`;
- `MINISTER`;
- `CITIZEN`.

Regole:
- `FOUNDER`: fondatore della micro-nazione, può proporre leggi, votare, chiudere votazioni, abrogare leggi e pubblicare comunicati.
- `MINISTER`: ruolo amministrativo interno, può proporre leggi, votare, chiudere votazioni, abrogare leggi e pubblicare comunicati.
- `CITIZEN`: cittadino ordinario, può proporre leggi e votare.

Nel Livello 2 non è ancora obbligatorio implementare la gestione completa della nomina dei ministri. Il sistema deve però mantenere il ruolo `MINISTER` perché già previsto dallo schema e dalle autorizzazioni.

## 4. Stati delle leggi

Gli stati ammessi sono:
- `PROPOSED`;
- `APPROVED`;
- `REJECTED`;
- `REPEALED`.

Significato:
- `PROPOSED`: legge proposta e ancora votabile.
- `APPROVED`: legge approvata dopo chiusura della votazione.
- `REJECTED`: legge respinta dopo chiusura della votazione.
- `REPEALED`: legge precedentemente approvata e poi abrogata.

## 5. Transizioni di stato

Transizioni ammesse:
```text
PROPOSED -> APPROVED
PROPOSED -> REJECTED
APPROVED -> REPEALED
```

Transizioni non ammesse:
```text
REJECTED -> APPROVED
REJECTED -> REPEALED
REPEALED -> APPROVED
REPEALED -> REJECTED
```

## 6. Proposta di legge

Regola:
- solo un utente membro della micro-nazione può proporre una legge.

Campi principali:
- micro-nazione;
- proponente;
- titolo;
- descrizione;
- stato iniziale `PROPOSED`;
- data creazione.

Evento storico generato:
```text
LAW_PROPOSED
```

## 7. Votazione

Valori ammessi:
- `YES`;
- `NO`;
- `ABSTAIN`.

Regole:
- solo i membri della micro-nazione possono votare;
- una legge può essere votata solo se è in stato `PROPOSED`;
- ogni utente può votare una sola volta per legge;
- il vincolo database `UNIQUE(law_id, user_id)` impedisce voti duplicati.

## 8. Chiusura votazione

Regola:
- solo `FOUNDER` o `MINISTER` possono chiudere una votazione.

Algoritmo:
```text
if YES > NO:
    status = APPROVED
else:
    status = REJECTED
```

Note:
- `ABSTAIN` viene conteggiato ma non decide l'esito;
- in caso di parità tra `YES` e `NO`, la legge viene respinta;
- alla chiusura viene valorizzato `closed_at`.

Eventi storici generati:
```text
LAW_APPROVED
LAW_REJECTED
```

## 9. Abrogazione

Regola:
- solo `FOUNDER` o `MINISTER` possono abrogare;
- solo una legge in stato `APPROVED` può essere abrogata.

Effetto:
```text
APPROVED -> REPEALED
```

Evento storico generato:
```text
LAW_REPEALED
```

## 10. Registro storico decisionale

Il registro storico salva gli eventi rilevanti della micro-nazione.

Eventi previsti nel Livello 2:
- `LAW_PROPOSED`;
- `LAW_APPROVED`;
- `LAW_REJECTED`;
- `LAW_REPEALED`;
- `RESOURCE_UPDATED`.

Ogni evento contiene:
- micro-nazione;
- eventuale legge collegata;
- eventuale utente attore;
- azione;
- descrizione;
- data creazione.

## 11. Risorse simboliche

Le risorse simboliche rappresentano lo stato astratto della micro-nazione.

Risorse:
- `coins`;
- `culture_points`;
- `energy_points`.

Regole iniziali:
- legge approvata: `culture_points + 10`, `energy_points + 5`;
- legge respinta: `energy_points - 2`, minimo `0`;
- legge abrogata: `culture_points - 5`, minimo `0`.

Le risorse non devono andare sotto zero.

## 12. Classifica micro-nazioni

La classifica confronta le micro-nazioni tramite uno score calcolato.

Formula iniziale:
```text
score = coins + culture_points + energy_points + approved_laws * 5 + citizens_count * 2
```

La formula deve restare semplice e spiegabile.

## 13. Pagine previste

Pagine principali:
- dettaglio micro-nazione con elenco leggi;
- form proposta legge;
- dettaglio legge;
- storico decisionale;
- classifica micro-nazioni.

## 14. Endpoint previsti

Endpoint principali:
```text
GET  /laws/create?nationId=...
POST /laws/create
GET  /law?id=...
POST /law/vote
POST /law/close
POST /law/repeal
GET  /nation/history?id=...
GET  /ranking
```
