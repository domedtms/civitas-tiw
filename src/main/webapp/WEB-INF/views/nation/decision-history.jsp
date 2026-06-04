<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.polimi.tiw.civitas.model.Nation" %>
<%@ page import="it.polimi.tiw.civitas.model.DecisionLog" %>
<%@ page import="it.polimi.tiw.civitas.util.HtmlUtil" %>
<%@ page import="java.util.List" %>
<%
    Nation nation = (Nation) request.getAttribute("nation");
    List<DecisionLog> decisionLogs = (List<DecisionLog>) request.getAttribute("decisionLogs");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Storico decisionale - Civitas</title>
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

        <h1>Storico decisionale</h1>

        <p class="muted">
            Micro-nazione:
            <strong>
                <%= HtmlUtil.escape(nation.getFlagSymbol()) %>
                <%= HtmlUtil.escape(nation.getName()) %>
            </strong>
        </p>

        <hr>

        <% if (decisionLogs == null || decisionLogs.isEmpty()) { %>
            <p class="muted">Non sono ancora presenti eventi decisionali per questa micro-nazione.</p>
        <% } else { %>
            <div class="history-list">
                <% for (DecisionLog log : decisionLogs) { %>
                    <article class="history-card">
                        <div class="history-card-header">
                            <h2><%= HtmlUtil.escape(HtmlUtil.label(log.getAction())) %></h2>

                            <% if (log.getCreatedAt() != null) { %>
                                <span class="status-pill">
                                    <%= HtmlUtil.escape(HtmlUtil.formatDateTime(log.getCreatedAt())) %>
                                </span>
                            <% } %>
                        </div>

                        <p><%= HtmlUtil.escape(log.getDescription()) %></p>

                        <div class="history-meta">
                            <span>
                                ID legge:
                                <strong><%= log.getLawId() != null ? log.getLawId() : "-" %></strong>
                            </span>

                            <span>
                                ID attore:
                                <strong><%= log.getActorId() != null ? log.getActorId() : "-" %></strong>
                            </span>
                        </div>
                    </article>
                <% } %>
            </div>
        <% } %>
    </section>
</main>
</body>
</html>
