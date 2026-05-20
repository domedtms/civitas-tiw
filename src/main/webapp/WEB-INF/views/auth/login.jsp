<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Login - Civitas</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
</head>
<body>
<main class="auth-container">
    <section class="auth-card">
        <h1>Accedi</h1>
        <p class="muted">Entra in Civitas e gestisci le tue micro-nazioni.</p>

        <% if (error != null) { %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>

        <form method="post" action="<%= request.getContextPath() %>/login" class="form">
            <label for="email">Email</label>
            <input id="email" type="email" name="email" required>

            <label for="password">Password</label>
            <input id="password" type="password" name="password" required>

            <button type="submit">Accedi</button>
        </form>

        <p class="auth-switch">
            Non hai un account?
            <a href="<%= request.getContextPath() %>/register">Registrati</a>
        </p>
    </section>
</main>
</body>
</html>