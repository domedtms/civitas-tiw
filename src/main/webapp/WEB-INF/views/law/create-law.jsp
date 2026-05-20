<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.polimi.tiw.civitas.util.HtmlUtil" %>
<%
    String error = (String) request.getAttribute("error");
    Integer nationId = (Integer) request.getAttribute("nationId");
    String titleValue = (String) request.getAttribute("title");
    String descriptionValue = (String) request.getAttribute("description");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Proponi legge - Civitas</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
</head>
<body>
<main class="auth-container">
    <section class="auth-card wide-card">
        <h1>Proponi una legge</h1>
        <p class="muted">Crea una nuova proposta legislativa per la micro-nazione.</p>

        <% if (error != null) { %>
            <div class="alert alert-error"><%= HtmlUtil.escape(error) %></div>
        <% } %>

        <form method="post" action="<%= request.getContextPath() %>/laws/create" class="form">
            <input type="hidden" name="nationId" value="<%= nationId %>">

            <label for="title">Titolo</label>
            <input id="title" type="text" name="title" required minlength="3" maxlength="120"
                   value="<%= HtmlUtil.escape(titleValue) %>">

            <label for="description">Descrizione</label>
            <textarea id="description" name="description" rows="8" required minlength="10" maxlength="5000"><%= HtmlUtil.escape(descriptionValue) %></textarea>

            <button type="submit">Proponi legge</button>
        </form>

        <p class="auth-switch">
            <a href="<%= request.getContextPath() %>/nation?id=<%= nationId %>">Torna alla micro-nazione</a>
        </p>
    </section>
</main>
</body>
</html>