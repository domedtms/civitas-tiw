<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.polimi.tiw.civitas.model.User" %>
<%@ page import="it.polimi.tiw.civitas.util.HtmlUtil" %>
<%
    User loggedUser = (User) session.getAttribute("loggedUser");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Civitas</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
</head>
<body>
<main class="home-container">
    <section class="home-card">
        <h1>Civitas</h1>
        <p class="muted">Governo di Micro-Nazioni Immaginarie</p>

        <% if (loggedUser != null) { %>
            <p>Accesso effettuato come <strong><%= HtmlUtil.escape(loggedUser.getUsername()) %></strong>.</p>

            <div class="actions">
                <a class="button" href="<%= request.getContextPath() %>/nations">Micro-nazioni</a>
                <a class="button secondary" href="<%= request.getContextPath() %>/nations/create">Crea micro-nazione</a>
                <a class="button secondary" href="<%= request.getContextPath() %>/ranking">Classifica</a>

                <form method="post" action="<%= request.getContextPath() %>/logout">
                    <button type="submit" class="button">Logout</button>
                </form>
            </div>
        <% } else { %>
            <p>Accedi o registrati per iniziare a usare Civitas.</p>

            <div class="actions">
                <a class="button" href="<%= request.getContextPath() %>/login">Login</a>
                <a class="button secondary" href="<%= request.getContextPath() %>/register">Registrati</a>
                <a class="button secondary" href="<%= request.getContextPath() %>/nations">Esplora micro-nazioni</a>
                <a class="button secondary" href="<%= request.getContextPath() %>/ranking">Classifica</a>
            </div>
        <% } %>
    </section>
</main>
</body>
</html>