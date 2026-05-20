<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.polimi.tiw.civitas.util.HtmlUtil" %>
<%
    String error = (String) request.getAttribute("error");
    String usernameValue = (String) request.getAttribute("username");
    String emailValue = (String) request.getAttribute("email");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Registrazione - Civitas</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
</head>
<body>
<main class="auth-container">
    <section class="auth-card">
        <h1>Crea account</h1>
        <p class="muted">Registrati per fondare o unirti a una micro-nazione.</p>

        <% if (error != null) { %>
            <div class="alert alert-error"><%= HtmlUtil.escape(error) %></div>
        <% } %>

        <form method="post" action="<%= request.getContextPath() %>/register" class="form">
            <label for="username">Username</label>
            <input id="username"
                   type="text"
                   name="username"
                   required
                   minlength="3"
                   value="<%= HtmlUtil.escape(usernameValue) %>">

            <label for="email">Email</label>
            <input id="email"
                   type="email"
                   name="email"
                   required
                   value="<%= HtmlUtil.escape(emailValue) %>">

            <label for="password">Password</label>
            <input id="password"
                   type="password"
                   name="password"
                   required
                   minlength="8">

            <button type="submit">Registrati</button>
        </form>

        <p class="auth-switch">
            Hai già un account?
            <a href="<%= request.getContextPath() %>/login">Accedi</a>
        </p>
    </section>
</main>
</body>
</html>