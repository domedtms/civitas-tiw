<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.polimi.tiw.civitas.model.Nation" %>
<%@ page import="it.polimi.tiw.civitas.util.HtmlUtil" %>
<%
    Nation nation = (Nation) request.getAttribute("nation");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title><%= HtmlUtil.escape(nation.getName()) %> - Civitas</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
</head>
<body>
<main class="page-container">
    <section class="home-card">
        <div class="actions">
            <a class="button secondary" href="<%= request.getContextPath() %>/nations">Torna alle micro-nazioni</a>
        </div>

        <h1>
            <%= HtmlUtil.escape(nation.getFlagSymbol()) %>
            <%= HtmlUtil.escape(nation.getName()) %>
        </h1>

        <% if (nation.getMotto() != null) { %>
            <p class="motto">“<%= HtmlUtil.escape(nation.getMotto()) %>”</p>
        <% } %>

        <% if (nation.getDescription() != null) { %>
            <p><%= HtmlUtil.escape(nation.getDescription()) %></p>
        <% } else { %>
            <p class="muted">Questa micro-nazione non ha ancora una descrizione.</p>
        <% } %>

        <hr>

        <p class="muted">
            ID fondatore: <%= nation.getFounderId() %>
        </p>
    </section>
</main>
</body>
</html>