<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.polimi.tiw.civitas.model.Nation" %>
<%@ page import="it.polimi.tiw.civitas.util.HtmlUtil" %>
<%@ page import="java.time.YearMonth" %>
<%
    Nation nation = (Nation) request.getAttribute("nation");
    String error = (String) request.getAttribute("error");
    String period = (String) request.getAttribute("period");
    String currentPeriod = YearMonth.now().toString();
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Genera giornale nazionale - Civitas</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
</head>
<body>
<main class="auth-container">
    <section class="auth-card wide-card">
        <h1>Genera giornale nazionale</h1>

        <p class="muted">
            Micro-nazione:
            <strong>
                <%= HtmlUtil.escape(nation.getFlagSymbol()) %>
                <%= HtmlUtil.escape(nation.getName()) %>
            </strong>
        </p>

        <p class="muted">
            Il contenuto sarà generato automaticamente dal sistema usando leggi, comunicati,
            risorse simboliche, ruoli e storico decisionale.
        </p>

        <% if (error != null) { %>
            <div class="alert alert-error"><%= HtmlUtil.escape(error) %></div>
        <% } %>

        <form method="post" action="<%= request.getContextPath() %>/nation/newspapers/generate" class="form">
            <input type="hidden" name="nationId" value="<%= nation.getId() %>">

            <label for="period">Periodo</label>
            <input id="period"
                   type="month"
                   name="period"
                   required
                   max="<%= HtmlUtil.escape(currentPeriod) %>"
                   value="<%= HtmlUtil.escape(period) %>">

            <p class="muted form-help">
                Puoi generare un giornale per il mese corrente o per mesi precedenti.
                Non è consentita la generazione per periodi futuri.
            </p>

            <button type="submit">Genera giornale</button>
        </form>

        <p class="auth-switch">
            <a href="<%= request.getContextPath() %>/nation?id=<%= nation.getId() %>">
                Torna alla micro-nazione
            </a>
        </p>
    </section>
</main>
</body>
</html>