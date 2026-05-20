<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.polimi.tiw.civitas.model.Nation" %>
<%@ page import="it.polimi.tiw.civitas.model.Citizen" %>
<%@ page import="it.polimi.tiw.civitas.model.User" %>
<%@ page import="it.polimi.tiw.civitas.util.HtmlUtil" %>
<%@ page import="java.util.List" %>
<%
    Nation nation = (Nation) request.getAttribute("nation");
    List<Citizen> citizens = (List<Citizen>) request.getAttribute("citizens");
    User loggedUser = (User) session.getAttribute("loggedUser");
    boolean currentUserMember = Boolean.TRUE.equals(request.getAttribute("currentUserMember"));
    String joinError = (String) request.getAttribute("joinError");
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
            <a class="button secondary" href="<%= request.getContextPath() %>/index.jsp">Home</a>
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

        <% if (joinError != null && !joinError.isBlank()) { %>
            <div class="alert alert-error"><%= HtmlUtil.escape(joinError) %></div>
        <% } %>

        <div class="join-panel">
            <% if (loggedUser == null) { %>
                <p class="muted">Accedi per unirti a questa micro-nazione.</p>
                <a class="button" href="<%= request.getContextPath() %>/login">Accedi</a>
            <% } else if (currentUserMember) { %>
                <p class="badge">Sei cittadino di questa micro-nazione</p>
            <% } else { %>
                <form method="post" action="<%= request.getContextPath() %>/nation/join">
                    <input type="hidden" name="nationId" value="<%= nation.getId() %>">
                    <button type="submit" class="button">Unisciti alla micro-nazione</button>
                </form>
            <% } %>
        </div>

        <hr>

        <section>
            <h2>Cittadini</h2>

            <% if (citizens == null || citizens.isEmpty()) { %>
                <p class="muted">Non ci sono ancora cittadini registrati.</p>
            <% } else { %>
                <div class="table-wrapper">
                    <table class="data-table">
                        <thead>
                        <tr>
                            <th>Username</th>
                            <th>Ruolo</th>
                            <th>Ingresso</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (Citizen citizen : citizens) { %>
                            <tr>
                                <td><%= HtmlUtil.escape(citizen.getUsername()) %></td>
                                <td><%= HtmlUtil.escape(citizen.getRole().name()) %></td>
                                <td>
                                    <%= citizen.getJoinedAt() != null ? HtmlUtil.escape(citizen.getJoinedAt().toString()) : "-" %>
                                </td>
                            </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>
        </section>

        <hr>

        <p class="muted">
            ID fondatore: <%= nation.getFounderId() %>
        </p>
    </section>
</main>
</body>
</html>