<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.polimi.tiw.civitas.model.Nation" %>
<%@ page import="it.polimi.tiw.civitas.model.NationalNewspaper" %>
<%@ page import="it.polimi.tiw.civitas.util.HtmlUtil" %>
<%
    Nation nation = (Nation) request.getAttribute("nation");
    NationalNewspaper newspaper = (NationalNewspaper) request.getAttribute("newspaper");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title><%= HtmlUtil.escape(newspaper.getTitle()) %> - Civitas</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
</head>
<body>
<main class="page-container">
    <article class="home-card newspaper-detail">
        <div class="actions">
            <a class="button secondary" href="<%= request.getContextPath() %>/nation/newspapers?nationId=<%= nation.getId() %>">
                Archivio giornali
            </a>
            <a class="button secondary" href="<%= request.getContextPath() %>/nation?id=<%= nation.getId() %>">
                Micro-nazione
            </a>
            <a class="button secondary" href="<%= request.getContextPath() %>/nation/dashboard?id=<%= nation.getId() %>">
                Cruscotto
            </a>
        </div>

        <p class="badge">Giornale nazionale</p>

        <h1><%= HtmlUtil.escape(newspaper.getTitle()) %></h1>

        <p class="muted">
            Micro-nazione:
            <strong>
                <%= HtmlUtil.escape(nation.getFlagSymbol()) %>
                <%= HtmlUtil.escape(nation.getName()) %>
            </strong>
            · Periodo:
            <strong><%= HtmlUtil.escape(newspaper.getPeriod()) %></strong>
            · Generato da ID utente:
            <strong><%= newspaper.getGeneratedBy() %></strong>
        </p>

        <% if (newspaper.getCreatedAt() != null) { %>
            <p class="muted">
                Data generazione:
                <%= HtmlUtil.escape(HtmlUtil.formatDateTime(newspaper.getCreatedAt())) %>
            </p>
        <% } %>

        <div class="newspaper-notice">
            Questo giornale è stato generato automaticamente dal sistema usando dati reali della micro-nazione:
            leggi, comunicati, risorse simboliche, ruoli e storico decisionale.
        </div>

        <hr>

        <section class="newspaper-section">
            <h2>Editoriale</h2>
            <p><%= HtmlUtil.escape(newspaper.getEditorial()) %></p>
        </section>

        <section class="newspaper-section">
            <h2>Stato politico</h2>
            <p><%= HtmlUtil.escape(newspaper.getPoliticalSummary()) %></p>
        </section>

        <section class="newspaper-section">
            <h2>Risorse simboliche</h2>
            <p><%= HtmlUtil.escape(newspaper.getResourcesSummary()) %></p>
        </section>

        <section class="newspaper-section">
            <h2>Attività legislativa</h2>
            <p><%= HtmlUtil.escape(newspaper.getLegislativeSummary()) %></p>
        </section>

        <section class="newspaper-section">
            <h2>Comunicati ufficiali</h2>
            <p><%= HtmlUtil.escape(newspaper.getAnnouncementsSummary()) %></p>
        </section>

        <section class="newspaper-section">
            <h2>Storico decisionale</h2>
            <p><%= HtmlUtil.escape(newspaper.getHistoricalSummary()) %></p>
        </section>
    </article>
</main>
</body>
</html>
