# Development Workflow

## 1. Obiettivo del documento

Questo documento definisce il workflow di sviluppo adottato per il progetto **Civitas - Governo di Micro-Nazioni Immaginarie**.

L'obiettivo è mantenere il progetto ordinato, tracciabile e facilmente revisionabile, separando lo sviluppo delle singole funzionalità dalla versione stabile dell'applicazione.

Il workflow è pensato per un progetto universitario TIW basato su:

- Java Servlet
- JSP
- JDBC
- database relazionale
- architettura MVC / 3-tier
- sviluppo incrementale tramite Git e GitHub

## 2. Branch principali

Il progetto usa tre livelli di branch:

- `main`: contiene solo versioni stabili, testate e presentabili.
- `sandbox`: contiene funzionalità integrate e testate prima del merge finale in `main`.
- `feature/*`: contiene lo sviluppo di singole funzionalità isolate.

La separazione tra questi branch permette di evitare modifiche dirette sulla versione stabile e rende più semplice controllare l'evoluzione del progetto.

## 3. Regola generale

Non si lavora direttamente su `main`.

Ogni funzionalità deve essere sviluppata in una branch dedicata, creata a partire da `sandbox`.

Formato consigliato:

```text
feature/nome-funzionalita
```

## 4. Flusso di lavoro

Il flusso corretto è il seguente:

1. Creare una issue GitHub.
2. Creare una branch `feature/*` collegata alla issue.
3. Implementare la funzionalità.
4. Testare localmente.
5. Aprire una Pull Request verso `sandbox`.
6. Revisionare il codice e correggere eventuali problemi.
7. Eseguire test su `sandbox`.
8. Aprire una Pull Request da `sandbox` verso `main`.
9. Effettuare il merge solo quando il progetto è stabile e funzionante.

Schema sintetico:

```text
feature/* -> sandbox -> main
```

## 5. Comandi Git principali

### Allinearsi a `sandbox`

```bash
git checkout sandbox
git pull origin sandbox
```

### Creare una nuova branch feature

```bash
git checkout -b feature/nome-funzionalita
```

### Salvare le modifiche

```bash
git status
git add .
git commit -m "feat: add authentication structure"
```

### Pubblicare la branch su GitHub

```bash
git push -u origin feature/nome-funzionalita
```

### Dopo il merge della Pull Request

```bash
git checkout sandbox
git pull origin sandbox
```

## 6. Convenzione commit

Ogni commit deve essere chiaro, breve e descrivere il tipo di modifica effettuata.

Formato consigliato:

```text
type: descrizione breve
```

Tipi principali:

- `docs`: modifiche alla documentazione
- `feat`: nuova funzionalità
- `fix`: correzione di un errore
- `refactor`: riorganizzazione del codice senza cambiare il comportamento
- `style`: modifiche grafiche o di formattazione
- `test`: aggiunta o aggiornamento di test manuali o documentazione di test
- `chore`: attività di setup, configurazione o manutenzione


## 7. Pull Request

Ogni Pull Request deve contenere:

- descrizione sintetica della funzionalità;
- issue collegata;
- elenco delle modifiche principali;
- test eseguiti;
- eventuali problemi noti.

Template consigliato:

```md
## Obiettivo

Descrivere brevemente cosa introduce questa Pull Request.

## Modifiche principali

- ...
- ...
- ...

## Test eseguiti

- ...
- ...
- ...

## Note

Eventuali limiti, problemi noti o decisioni progettuali.
```

## 8. Issue GitHub

Ogni funzionalità deve essere tracciata con una issue.

Esempio di issue:

```md
# Implement user authentication

## Obiettivo

Permettere agli utenti di registrarsi, effettuare login e logout.

## Task

- Creare tabella `users`
- Creare classe `User`
- Creare `UserDAO`
- Implementare servlet di registrazione
- Implementare servlet di login
- Implementare servlet di logout
- Creare JSP per login e registrazione
- Gestire errori di validazione
- Testare sessione utente

## Criteri di completamento

- Un utente può registrarsi
- Un utente può fare login
- Un utente autenticato viene salvato in sessione
- Il logout invalida la sessione
- Input non validi vengono rifiutati lato server
```

## 9. Regole architetturali

Durante lo sviluppo devono essere rispettate le seguenti regole:

- Le JSP devono solo presentare dati e renderizzare interfacce.
- Le Servlet devono ricevere richieste HTTP, validare input, orchestrare la logica e scegliere forward o redirect.
- I DAO devono contenere SQL, JDBC, `PreparedStatement`, `ResultSet` e mapping verso gli oggetti Java.
- La logica di autorizzazione deve essere controllata lato server.
- Non deve esserci SQL nelle JSP.
- Non deve esserci HTML generato dentro le Servlet.
- Non deve esserci JDBC direttamente nelle Servlet.

Separazione prevista:

```text
Presentation Layer
- JSP
- HTML
- CSS
- JavaScript

Business Logic Layer
- Servlet
- Service

Data Access Layer
- DAO
- JDBC
- SQL
```

## 10. Regole per `main`

La branch `main` deve contenere solo codice:

- compilabile;
- testato;
- coerente con l'architettura MVC;
- privo di errori bloccanti;
- presentabile all'orale.

Non si devono fare commit diretti su `main`.

Ogni modifica deve arrivare tramite Pull Request.

## 11. Regole per `sandbox`

La branch `sandbox` è usata come ambiente di integrazione.

Qui vengono unite le funzionalità sviluppate nelle branch `feature/*`.

Prima di aprire una Pull Request verso `main`, bisogna verificare che su `sandbox`:

- il progetto compili;
- il database sia coerente;
- le funzionalità principali funzionino;
- non ci siano regressioni evidenti;
- il README e la documentazione siano aggiornati se necessario.


## 12. Motivazione del workflow

Questo workflow permette di sviluppare Civitas in modo progressivo e controllato.

La branch `main` rappresenta la versione stabile del progetto, mentre `sandbox` viene usata per integrare e testare le funzionalità prima del rilascio stabile.

Le branch `feature/*` permettono di isolare ogni modifica, semplificando debug, review e spiegazione del lavoro svolto.

Questa organizzazione è coerente con un progetto universitario ben gestito e permette di dimostrare padronanza non solo del codice, ma anche del processo di sviluppo.