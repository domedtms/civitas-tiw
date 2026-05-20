<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.polimi.tiw.civitas.model.Law" %>
<%@ page import="it.polimi.tiw.civitas.model.Nation" %>
<%@ page import="it.polimi.tiw.civitas.model.User" %>
<%@ page import="it.polimi.tiw.civitas.model.VoteValue" %>
<%@ page import="it.polimi.tiw.civitas.util.HtmlUtil" %>
<%@ page import="java.util.Map" %>
<%
    Law law = (Law) request.getAttribute("law");
    Nation nation = (Nation) request.getAttribute("nation");
    User loggedUser = (User) session.getAttribute("loggedUser");

    Map<VoteValue, Integer> voteCounts = (Map<VoteValue, Integer>) request.getAttribute("voteCounts");

    int yesCount = voteCounts != null && voteCounts.get(VoteValue.YES) != null ? voteCounts.get(VoteValue.YES) : 0;
    int noCount = voteCounts != null && voteCounts.get(VoteValue.NO) != null ? voteCounts.get(VoteValue.NO) : 0;
    int abstainCount = voteCounts != null && voteCounts.get(VoteValue.ABSTAIN) != null ? voteCounts.get(VoteValue.ABSTAIN) : 0;

    boolean currentUserMember = Boolean.TRUE.equals(request.getAttribute("currentUserMember"));
    boolean alreadyVoted = Boolean.TRUE.equals(request.getAttribute("alreadyVoted"));
    boolean canVote = Boolean.TRUE.equals(request.getAttribute("canVote"));
    boolean canCloseLaw = Boolean.TRUE.equals(request.getAttribute("canCloseLaw"));

    String voteError = (String) request.getAttribute("voteError");
    String workflowError = (String) request.getAttribute("workflowError");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title><%= HtmlUtil.escape(law.getTitle()) %> - Civitas</title>
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

        <p class="badge"><%= HtmlUtil.escape(law.getStatus().name()) %></p>

        <h1><%= HtmlUtil.escape(law.getTitle()) %></h1>

        <p class="muted">
            Micro-nazione:
            <strong><%= HtmlUtil.escape(nation.getName()) %></strong>
        </p>

        <p><%= HtmlUtil.escape(law.getDescription()) %></p>

        <hr>

        <section>
            <h2>Risultati voto</h2>

            <div class="vote-counts">
                <div class="vote-count-card">
                    <strong><%= yesCount %></strong>
                    <span>YES</span>
                </div>

                <div class="vote-count-card">
                    <strong><%= noCount %></strong>
                    <span>NO</span>
                </div>

                <div class="vote-count-card">
                    <strong><%= abstainCount %></strong>
                    <span>ABSTAIN</span>
                </div>
            </div>
        </section>

        <hr>

        <section class="vote-panel">
            <h2>Vota questa legge</h2>

            <% if (voteError != null && !voteError.isBlank()) { %>
                <div class="alert alert-error"><%= HtmlUtil.escape(voteError) %></div>
            <% } %>

            <% if (loggedUser == null) { %>
                <p class="muted">Accedi per votare questa legge.</p>
                <a class="button" href="<%= request.getContextPath() %>/login">Accedi</a>
            <% } else if (!currentUserMember) { %>
                <p class="muted">Solo i cittadini di questa micro-nazione possono votare.</p>
            <% } else if (alreadyVoted) { %>
                <p class="badge">Hai già votato questa legge</p>
            <% } else if (!canVote) { %>
                <p class="muted">Questa legge non è aperta al voto.</p>
            <% } else { %>
                <form method="post" action="<%= request.getContextPath() %>/law/vote" class="vote-form">
                    <input type="hidden" name="lawId" value="<%= law.getId() %>">

                    <button type="submit" name="voteValue" value="YES" class="button">YES</button>
                    <button type="submit" name="voteValue" value="NO" class="button secondary">NO</button>
                    <button type="submit" name="voteValue" value="ABSTAIN" class="button secondary">ABSTAIN</button>
                </form>
            <% } %>
        </section>

        <hr>

        <section class="workflow-panel">
            <h2>Gestione votazione</h2>

            <% if (workflowError != null && !workflowError.isBlank()) { %>
                <div class="alert alert-error"><%= HtmlUtil.escape(workflowError) %></div>
            <% } %>

            <% if (canCloseLaw) { %>
                <p class="muted">
                    Questa legge è ancora in stato <strong>PROPOSED</strong>.
                    Puoi chiudere la votazione e calcolare l'esito.
                </p>

                <form method="post" action="<%= request.getContextPath() %>/law/close">
                    <input type="hidden" name="lawId" value="<%= law.getId() %>">
                    <button type="submit" class="button">Chiudi votazione</button>
                </form>
            <% } else if ("PROPOSED".equals(law.getStatus().name())) { %>
                <p class="muted">
                    Solo FOUNDER o MINISTER possono chiudere la votazione.
                </p>
            <% } else { %>
                <p class="badge">
                    Votazione chiusa: <%= HtmlUtil.escape(law.getStatus().name()) %>
                </p>
            <% } %>
        </section>

        <hr>

        <p class="muted">
            Proponente ID: <%= law.getProposerId() %>
            <% if (law.getCreatedAt() != null) { %>
                · Proposta il <%= HtmlUtil.escape(law.getCreatedAt().toString()) %>
            <% } %>
        </p>

        <% if (law.getClosedAt() != null) { %>
            <p class="muted">Chiusa il <%= HtmlUtil.escape(law.getClosedAt().toString()) %></p>
        <% } %>
    </section>
</main>
</body>
</html>