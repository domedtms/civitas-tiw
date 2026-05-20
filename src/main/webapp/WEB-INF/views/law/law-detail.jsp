<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.polimi.tiw.civitas.model.Law" %>
<%@ page import="it.polimi.tiw.civitas.model.Nation" %>
<%@ page import="it.polimi.tiw.civitas.util.HtmlUtil" %>
<%
    Law law = (Law) request.getAttribute("law");
    Nation nation = (Nation) request.getAttribute("nation");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title><%= HtmlUtil.escape(law.getTitle()) %> - Civitas</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
</head>
<body>
<main class="page-container">
    <section class="home-card">
        <div class="actions">
            <a class="button secondary" href="<%= request.getContextPath() %>/nation?id=<%= nation.getId() %>">
                Torna alla micro-nazione
            </a>
            <a class="button secondary" href="<%= request.getContextPath() %>/nations">
                Micro-nazioni
            </a>
        </div>

        <p class="badge"><%= HtmlUtil.escape(law.getStatus().name()) %></p>

        <h1><%= HtmlUtil.escape(law.getTitle()) %></h1>

        <p class="muted">
            Micro-nazione:
            <strong><%= HtmlUtil.escape(nation.getName()) %></strong>
        </p>

        <p><%= HtmlUtil.escape(law.getDescription()) %></p>

        <hr>

        <p class="muted">
            Proponente ID: <%= law.getProposerId() %>
            <% if (law.getCreatedAt() != null) { %>
                · Proposta il <%= HtmlUtil.escape(law.getCreatedAt().toString()) %>
            <% } %>
        </p>

        <% if (law.getClosedAt() != null) { %>
            <p class="muted">Chiusa il <%= HtmlUtil.escape(law.getClosedAt().toString()) %></p>
        <% } %>
    </section>
</main>
</body>
</html>