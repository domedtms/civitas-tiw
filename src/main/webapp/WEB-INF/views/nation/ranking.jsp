<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.polimi.tiw.civitas.model.NationRankingItem" %>
<%@ page import="it.polimi.tiw.civitas.util.HtmlUtil" %>
<%@ page import="java.util.List" %>
<%
    List<NationRankingItem> ranking = (List<NationRankingItem>) request.getAttribute("ranking");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Classifica micro-nazioni - Civitas</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
</head>
<body>
<main class="page-container">
    <section class="home-card">
        <div class="actions">
            <a class="button secondary" href="<%= request.getContextPath() %>/index.jsp">Home</a>
            <a class="button secondary" href="<%= request.getContextPath() %>/nations">Micro-nazioni</a>
        </div>

        <h1>Classifica micro-nazioni</h1>
        <p class="muted">
            Confronto tra micro-nazioni basato su cittadini, leggi approvate e risorse simboliche.
        </p>

        <div class="score-formula">
            <strong>Formula score:</strong>
            coins + culture + energy + approved_laws × 5 + citizens × 2
        </div>

        <hr>

        <% if (ranking == null || ranking.isEmpty()) { %>
            <p class="muted">Non sono ancora presenti micro-nazioni da classificare.</p>
        <% } else { %>
            <div class="table-wrapper">
                <table class="data-table ranking-table">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Micro-nazione</th>
                        <th>Cittadini</th>
                        <th>Leggi approvate</th>
                        <th>Coins</th>
                        <th>Culture</th>
                        <th>Energy</th>
                        <th>Score</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% int position = 1; %>
                    <% for (NationRankingItem item : ranking) { %>
                        <tr>
                            <td><strong><%= position %></strong></td>
                            <td>
                                <a href="<%= request.getContextPath() %>/nation?id=<%= item.getNationId() %>">
                                    <%= HtmlUtil.escape(item.getFlagSymbol()) %>
                                    <%= HtmlUtil.escape(item.getNationName()) %>
                                </a>
                            </td>
                            <td><%= item.getCitizensCount() %></td>
                            <td><%= item.getApprovedLawsCount() %></td>
                            <td><%= item.getCoins() %></td>
                            <td><%= item.getCulturePoints() %></td>
                            <td><%= item.getEnergyPoints() %></td>
                            <td><strong><%= item.getScore() %></strong></td>
                        </tr>
                        <% position++; %>
                    <% } %>
                    </tbody>
                </table>
            </div>
        <% } %>
    </section>
</main>
</body>
</html>