<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
    String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");

    String title = "Errore";
    String message = "Si è verificato un errore inatteso.";

    if (statusCode != null) {
        if (statusCode == 400) {
            title = "Richiesta non valida";
            message = "La richiesta inviata non è valida o contiene parametri mancanti.";
        } else if (statusCode == 401) {
            title = "Accesso richiesto";
            message = "Devi effettuare il login per accedere a questa risorsa.";
        } else if (statusCode == 403) {
            title = "Accesso non autorizzato";
            message = "Non hai i permessi necessari per eseguire questa operazione.";
        } else if (statusCode == 404) {
            title = "Risorsa non trovata";
            message = "La risorsa richiesta non esiste.";
        } else if (statusCode == 500) {
            title = "Errore interno";
            message = "Si è verificato un errore lato server.";
        }
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title><%= title %> - Civitas</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
</head>
<body>
<main class="auth-container">
    <section class="auth-card">
        <h1><%= title %></h1>
        <p class="muted"><%= message %></p>

        <% if (statusCode != null) { %>
            <p class="muted">Codice errore: <strong><%= statusCode %></strong></p>
        <% } %>

        <% if (requestUri != null) { %>
            <p class="muted">Percorso: <code><%= requestUri %></code></p>
        <% } %>

        <div class="actions">
            <a class="button" href="<%= request.getContextPath() %>/index.jsp">Torna alla home</a>
            <a class="button secondary" href="<%= request.getContextPath() %>/nations">Micro-nazioni</a>
        </div>
    </section>
</main>
</body>
</html>