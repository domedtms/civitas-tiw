<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.polimi.tiw.civitas.model.Nation" %>
<%@ page import="it.polimi.tiw.civitas.model.Citizen" %>
<%@ page import="it.polimi.tiw.civitas.model.User" %>
<%@ page import="it.polimi.tiw.civitas.model.Announcement" %>
<%@ page import="it.polimi.tiw.civitas.model.Law" %>
<%@ page import="it.polimi.tiw.civitas.util.HtmlUtil" %>
<%@ page import="it.polimi.tiw.civitas.model.NationResources" %>
<%@ page import="java.util.List" %>
<%
    Nation nation = (Nation) request.getAttribute("nation");
    List<Citizen> citizens = (List<Citizen>) request.getAttribute("citizens");
    List<Announcement> announcements = (List<Announcement>) request.getAttribute("announcements");
    List<Law> laws = (List<Law>) request.getAttribute("laws");

    User loggedUser = (User) session.getAttribute("loggedUser");

    boolean currentUserMember = Boolean.TRUE.equals(request.getAttribute("currentUserMember"));
    boolean canCreateAnnouncement = Boolean.TRUE.equals(request.getAttribute("canCreateAnnouncement"));

    String joinError = (String) request.getAttribute("joinError");
    NationResources resources = (NationResources) request.getAttribute("resources");
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
            <a class="button secondary" href="<%= request.getContextPath() %>/nation/history?id=<%= nation.getId() %>">
                Storico decisionale
            </a>
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
            <h2>Risorse simboliche</h2>
            <p class="muted">Stato sintetico della micro-nazione.</p>

            <% if (resources == null) { %>
                <p class="muted">Risorse non ancora inizializzate.</p>
            <% } else { %>
                <div class="resource-grid">
                    <article class="resource-card">
                        <strong><%= resources.getCoins() %></strong>
                        <span>Coins</span>
                    </article>

                    <article class="resource-card">
                        <strong><%= resources.getCulturePoints() %></strong>
                        <span>Culture</span>
                    </article>

                    <article class="resource-card">
                        <strong><%= resources.getEnergyPoints() %></strong>
                        <span>Energy</span>
                    </article>
                </div>
            <% } %>
        </section>

        <hr>

        <section>
            <div class="section-title-row">
                <div>
                    <h2>Leggi</h2>
                    <p class="muted">Proposte legislative della micro-nazione.</p>
                </div>

                <% if (currentUserMember) { %>
                    <a class="button" href="<%= request.getContextPath() %>/laws/create?nationId=<%= nation.getId() %>">
                        Proponi legge
                    </a>
                <% } %>
            </div>

            <% if (laws == null || laws.isEmpty()) { %>
                <p class="muted">Non ci sono ancora leggi proposte.</p>
            <% } else { %>
                <div class="law-list">
                    <% for (Law law : laws) { %>
                        <article class="law-card">
                            <div class="law-card-header">
                                <h3><%= HtmlUtil.escape(law.getTitle()) %></h3>
                                <span class="status-pill"><%= HtmlUtil.escape(law.getStatus().name()) %></span>
                            </div>

                            <p class="muted"><%= HtmlUtil.escape(law.getDescription()) %></p>

                            <a class="button secondary" href="<%= request.getContextPath() %>/law?id=<%= law.getId() %>">
                                Apri legge
                            </a>
                        </article>
                    <% } %>
                </div>
            <% } %>
        </section>

        <hr>

        <section>
            <div class="section-title-row">
                <div>
                    <h2>Comunicati ufficiali</h2>
                    <p class="muted">Aggiornamenti pubblicati dagli utenti autorizzati.</p>
                </div>

                <% if (canCreateAnnouncement) { %>
                    <a class="button" href="<%= request.getContextPath() %>/announcements/create?nationId=<%= nation.getId() %>">
                        Nuovo comunicato
                    </a>
                <% } %>
            </div>

            <% if (announcements == null || announcements.isEmpty()) { %>
                <p class="muted">Non ci sono ancora comunicati ufficiali.</p>
            <% } else { %>
                <div class="announcement-list">
                    <% for (Announcement announcement : announcements) { %>
                        <article class="announcement-card">
                            <h3><%= HtmlUtil.escape(announcement.getTitle()) %></h3>
                            <p><%= HtmlUtil.escape(announcement.getContent()) %></p>
                            <p class="muted">
                                Autore ID: <%= announcement.getAuthorId() %>
                                <% if (announcement.getCreatedAt() != null) { %>
                                    · <%= HtmlUtil.escape(announcement.getCreatedAt().toString()) %>
                                <% } %>
                            </p>
                        </article>
                    <% } %>
                </div>
            <% } %>
        </section>

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