<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.polimi.tiw.civitas.util.HtmlUtil" %>
<%
    String error = (String) request.getAttribute("error");
    String name = (String) request.getAttribute("name");
    String motto = (String) request.getAttribute("motto");
    String description = (String) request.getAttribute("description");
    String flagSymbol = (String) request.getAttribute("flagSymbol");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Crea micro-nazione - Civitas</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
</head>
<body>
<main class="auth-container">
    <section class="auth-card wide-card">
        <h1>Crea micro-nazione</h1>
        <p class="muted">Definisci l'identità iniziale della tua comunità immaginaria.</p>

        <% if (error != null) { %>
            <div class="alert alert-error"><%= HtmlUtil.escape(error) %></div>
        <% } %>

        <form method="post" action="<%= request.getContextPath() %>/nations/create" class="form">
            <label for="name">Nome</label>
            <input id="name" type="text" name="name" required minlength="3" maxlength="100"
                   value="<%= HtmlUtil.escape(name) %>">

            <label for="motto">Motto</label>
            <input id="motto" type="text" name="motto" maxlength="150"
                   value="<%= HtmlUtil.escape(motto) %>">

            <label for="flagSymbol">Bandiera testuale o emoji</label>
            <input id="flagSymbol" type="text" name="flagSymbol" maxlength="20"
                   value="<%= HtmlUtil.escape(flagSymbol) %>">

            <label for="description">Descrizione</label>
            <textarea id="description" name="description" rows="6" maxlength="3000"><%= HtmlUtil.escape(description) %></textarea>

            <button type="submit">Crea micro-nazione</button>
        </form>

        <p class="auth-switch">
            <a href="<%= request.getContextPath() %>/nations">Torna all'elenco</a>
        </p>
    </section>
</main>
</body>
</html>