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
    boolean canRepealLaw = Boolean.TRUE.equals(request.getAttribute("canRepealLaw"));

    String voteError = (String) request.getAttribute("voteError");
    String workflowError = (String) request.getAttribute("workflowError");
    String statusClass = "status-pill status-" + law.getStatus().name().toLowerCase();
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

        <p class="<%= statusClass %>"><%= HtmlUtil.escape(HtmlUtil.label(law.getStatus())) %></p>

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
                    <span><%= HtmlUtil.escape(HtmlUtil.label(VoteValue.YES)) %></span>
                </div>

                <div class="vote-count-card">
                    <strong><%= noCount %></strong>
                    <span><%= HtmlUtil.escape(HtmlUtil.label(VoteValue.NO)) %></span>
                </div>

                <div class="vote-count-card">
                    <strong><%= abstainCount %></strong>
                    <span><%= HtmlUtil.escape(HtmlUtil.label(VoteValue.ABSTAIN)) %></span>
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
                <% if (!law.getStatus().name().equals("PROPOSED")) { %>
                    <p class="muted">
                        Questa legge non è più votabile perché si trova nello stato
                        <strong><%= HtmlUtil.escape(HtmlUtil.label(law.getStatus())) %></strong>.
                    </p>
                <% } else { %>
                    <p class="muted">Questa legge non è aperta al voto.</p>
                <% } %>
            <% } else { %>
                <form method="post" action="<%= request.getContextPath() %>/law/vote" class="vote-form">
                    <input type="hidden" name="lawId" value="<%= law.getId() %>">

                    <button type="submit" name="voteValue" value="YES" class="button"><%= HtmlUtil.escape(HtmlUtil.label(VoteValue.YES)) %></button>
                    <button type="submit" name="voteValue" value="NO" class="button secondary"><%= HtmlUtil.escape(HtmlUtil.label(VoteValue.NO)) %></button>
                    <button type="submit" name="voteValue" value="ABSTAIN" class="button secondary"><%= HtmlUtil.escape(HtmlUtil.label(VoteValue.ABSTAIN)) %></button>
                </form>
            <% } %>
        </section>

        <hr>

        <section class="workflow-panel">
            <h2>Gestione legge</h2>

            <% if (workflowError != null && !workflowError.isBlank()) { %>
                <div class="alert alert-error"><%= HtmlUtil.escape(workflowError) %></div>
            <% } %>

            <% if (canCloseLaw) { %>
                <p class="muted">
                    Questa legge è ancora in stato <strong>proposta</strong>.
                    Puoi chiudere la votazione e calcolare l'esito.
                </p>

                <form method="post" action="<%= request.getContextPath() %>/law/close">
                    <input type="hidden" name="lawId" value="<%= law.getId() %>">
                    <button type="submit" class="button">Chiudi votazione</button>
                </form>

            <% } else if (canRepealLaw) { %>
                <p class="muted">
                    Questa legge è attualmente <strong>approvata</strong>.
                    Puoi abrogarla se non è più valida per la micro-nazione.
                </p>

                <form method="post" action="<%= request.getContextPath() %>/law/repeal">
                    <input type="hidden" name="lawId" value="<%= law.getId() %>">
                    <button type="submit" class="button danger-button">Abroga legge</button>
                </form>

            <% } else if (law.getStatus().name().equals("PROPOSED")) { %>
                <p class="muted">
                    Solo il fondatore o un ministro possono chiudere la votazione.
                </p>

            <% } else if (law.getStatus().name().equals("APPROVED")) { %>
                <p class="badge">
                    Legge approvata.
                </p>

            <% } else if (law.getStatus().name().equals("REPEALED")) { %>
                <p class="badge">
                    Legge abrogata.
                </p>

            <% } else { %>
                <p class="badge">
                    Votazione chiusa: <%= HtmlUtil.escape(HtmlUtil.label(law.getStatus())) %>
                </p>
            <% } %>
        </section>

        <hr>

        <p class="muted">
            ID proponente: <%= law.getProposerId() %>
            <% if (law.getCreatedAt() != null) { %>
                · Proposta il <%= HtmlUtil.escape(HtmlUtil.formatDateTime(law.getCreatedAt())) %>
            <% } %>
        </p>

        <% if (law.getClosedAt() != null) { %>
            <p class="muted">Chiusa il <%= HtmlUtil.escape(HtmlUtil.formatDateTime(law.getClosedAt())) %></p>
        <% } %>
    </section>
</main>
</body>
</html>
