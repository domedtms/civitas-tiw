<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.polimi.tiw.civitas.model.Nation" %>
<%@ page import="it.polimi.tiw.civitas.model.User" %>
<%@ page import="it.polimi.tiw.civitas.util.HtmlUtil" %>
<%@ page import="java.util.List" %>
<%
    User loggedUser = (User) session.getAttribute("loggedUser");
    List<Nation> nations = (List<Nation>) request.getAttribute("nations");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Micro-nazioni - Civitas</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
</head>
<body>
<main class="page-container">
    <section class="page-header">
        <div>
            <h1>Micro-nazioni</h1>
            <p class="muted">Esplora le comunità immaginarie create dagli utenti.</p>
        </div>

        <div class="actions">
            <a class="button secondary" href="<%= request.getContextPath() %>/index.jsp">Pagina iniziale</a>
            <a class="button secondary" href="<%= request.getContextPath() %>/ranking">Classifica</a>
            <% if (loggedUser != null) { %>
                <a class="button" href="<%= request.getContextPath() %>/nations/create">Crea micro-nazione</a>
            <% } else { %>
                <a class="button" href="<%= request.getContextPath() %>/login">Accedi</a>
            <% } %>
        </div>
    </section>

    <section class="grid">
        <% if (nations == null || nations.isEmpty()) { %>
            <article class="empty-card">
                <h2>Nessuna micro-nazione presente</h2>
                <p class="muted">Accedi e crea la prima micro-nazione di Civitas.</p>
            </article>
        <% } else { %>
            <% for (Nation nation : nations) { %>
                <article class="nation-card">
                    <h2>
                        <%= HtmlUtil.escape(nation.getFlagSymbol()) %>
                        <%= HtmlUtil.escape(nation.getName()) %>
                    </h2>

                    <% if (nation.getMotto() != null) { %>
                        <p class="motto">“<%= HtmlUtil.escape(nation.getMotto()) %>”</p>
                    <% } %>

                    <% if (nation.getDescription() != null) { %>
                        <p class="muted"><%= HtmlUtil.escape(nation.getDescription()) %></p>
                    <% } %>

                    <a class="button secondary" href="<%= request.getContextPath() %>/nation?id=<%= nation.getId() %>">
                        Apri scheda
                    </a>
                    <a class="button secondary" href="<%= request.getContextPath() %>/nation/dashboard?id=<%= nation.getId() %>">
                        Cruscotto
                    </a>
                </article>
            <% } %>
        <% } %>
    </section>
</main>
</body>
</html>
