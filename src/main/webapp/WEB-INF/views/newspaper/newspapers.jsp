<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.polimi.tiw.civitas.model.Nation" %>
<%@ page import="it.polimi.tiw.civitas.model.NationalNewspaper" %>
<%@ page import="it.polimi.tiw.civitas.util.HtmlUtil" %>
<%@ page import="java.util.List" %>
<%
    Nation nation = (Nation) request.getAttribute("nation");
    List<NationalNewspaper> newspapers = (List<NationalNewspaper>) request.getAttribute("newspapers");
    boolean canGenerateNewspaper = Boolean.TRUE.equals(request.getAttribute("canGenerateNewspaper"));
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Giornali nazionali - Civitas</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
</head>
<body>
<main class="page-container">
    <section class="home-card">
        <div class="actions">
            <a class="button secondary" href="<%= request.getContextPath() %>/nation?id=<%= nation.getId() %>">
                Torna alla micro-nazione
            </a>
            <a class="button secondary" href="<%= request.getContextPath() %>/nation/dashboard?id=<%= nation.getId() %>">
                Dashboard
            </a>
            <a class="button secondary" href="<%= request.getContextPath() %>/nations">
                Micro-nazioni
            </a>

            <% if (canGenerateNewspaper) { %>
                <a class="button" href="<%= request.getContextPath() %>/nation/newspapers/generate?nationId=<%= nation.getId() %>">
                    Genera giornale
                </a>
            <% } %>
        </div>

        <h1>
            Giornali nazionali —
            <%= HtmlUtil.escape(nation.getFlagSymbol()) %>
            <%= HtmlUtil.escape(nation.getName()) %>
        </h1>

        <p class="muted">
            Archivio dei giornali generati automaticamente dal sistema per questa micro-nazione.
        </p>

        <hr>

        <% if (newspapers == null || newspapers.isEmpty()) { %>
            <p class="muted">Non sono ancora presenti giornali nazionali.</p>

            <% if (canGenerateNewspaper) { %>
                <a class="button" href="<%= request.getContextPath() %>/nation/newspapers/generate?nationId=<%= nation.getId() %>">
                    Genera il primo giornale
                </a>
            <% } %>
        <% } else { %>
            <div class="newspaper-list">
                <% for (NationalNewspaper newspaper : newspapers) { %>
                    <article class="newspaper-card">
                        <div class="newspaper-card-header">
                            <div>
                                <h2><%= HtmlUtil.escape(newspaper.getTitle()) %></h2>
                                <p class="muted">
                                    Periodo:
                                    <strong><%= HtmlUtil.escape(newspaper.getPeriod()) %></strong>
                                    · Generato da utente ID:
                                    <strong><%= newspaper.getGeneratedBy() %></strong>
                                </p>
                            </div>

                            <% if (newspaper.getCreatedAt() != null) { %>
                                <span class="status-pill">
                                    <%= HtmlUtil.escape(newspaper.getCreatedAt().toString()) %>
                                </span>
                            <% } %>
                        </div>

                        <p><%= HtmlUtil.escape(newspaper.getEditorial()) %></p>

                        <a class="button secondary" href="<%= request.getContextPath() %>/nation/newspaper?id=<%= newspaper.getId() %>">
                            Leggi giornale
                        </a>
                    </article>
                <% } %>
            </div>
        <% } %>
    </section>
</main>
</body>
</html>