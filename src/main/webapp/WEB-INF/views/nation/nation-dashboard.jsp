<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.polimi.tiw.civitas.model.Nation" %>
<%@ page import="it.polimi.tiw.civitas.util.HtmlUtil" %>
<%
    Nation nation = (Nation) request.getAttribute("nation");
    String apiUrl = request.getContextPath() + "/api/nation/stats?id=" + nation.getId();
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Civitas</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
    <script src="<%= request.getContextPath() %>/js/nation-dashboard.js" defer></script>
</head>
<body>
<main class="page-container">
    <section class="home-card">
        <div class="actions">
            <a class="button secondary" href="<%= request.getContextPath() %>/nation?id=<%= nation.getId() %>">
                Torna alla micro-nazione
            </a>
            <a class="button secondary" href="<%= request.getContextPath() %>/nation/newspapers?nationId=<%= nation.getId() %>">
                Giornali
            </a>
            <a class="button secondary" href="<%= request.getContextPath() %>/nations">
                Micro-nazioni
            </a>
            <a class="button secondary" href="<%= request.getContextPath() %>/ranking">
                Classifica
            </a>
        </div>

        <h1>
            Dashboard -
            <%= HtmlUtil.escape(nation.getFlagSymbol()) %>
            <%= HtmlUtil.escape(nation.getName()) %>
        </h1>

        <p class="muted">
            Stato dinamico della micro-nazione caricato tramite endpoint JSON e JavaScript asincrono.
        </p>

        <div id="dashboard-root"
             data-api-url="<%= HtmlUtil.escape(apiUrl) %>">

            <div id="dashboard-loading" class="alert">
                Caricamento statistiche in corso...
            </div>

            <div id="dashboard-error" class="alert alert-error hidden">
                Impossibile caricare le statistiche della micro-nazione.
            </div>

            <section id="dashboard-content" class="hidden">
                <hr>

                <section>
                    <h2>Popolazione e ruoli</h2>

                    <div class="dashboard-grid">
                        <article class="dashboard-card">
                            <strong id="citizensCount">-</strong>
                            <span>Cittadini totali</span>
                        </article>

                        <article class="dashboard-card">
                            <strong id="foundersCount">-</strong>
                            <span>Founder</span>
                        </article>

                        <article class="dashboard-card">
                            <strong id="ministersCount">-</strong>
                            <span>Ministri</span>
                        </article>

                        <article class="dashboard-card">
                            <strong id="regularCitizensCount">-</strong>
                            <span>Citizen</span>
                        </article>
                    </div>
                </section>

                <hr>

                <section>
                    <h2>Attività legislativa</h2>

                    <div class="dashboard-grid">
                        <article class="dashboard-card">
                            <strong id="lawsCount">-</strong>
                            <span>Leggi totali</span>
                        </article>

                        <article class="dashboard-card">
                            <strong id="proposedLawsCount">-</strong>
                            <span>Proposte</span>
                        </article>

                        <article class="dashboard-card">
                            <strong id="approvedLawsCount">-</strong>
                            <span>Approvate</span>
                        </article>

                        <article class="dashboard-card">
                            <strong id="rejectedLawsCount">-</strong>
                            <span>Respinte</span>
                        </article>

                        <article class="dashboard-card">
                            <strong id="repealedLawsCount">-</strong>
                            <span>Abrogate</span>
                        </article>
                    </div>
                </section>

                <hr>

                <section>
                    <h2>Risorse simboliche</h2>

                    <div class="dashboard-grid">
                        <article class="dashboard-card">
                            <strong id="coins">-</strong>
                            <span>Coins</span>
                        </article>

                        <article class="dashboard-card">
                            <strong id="culturePoints">-</strong>
                            <span>Culture</span>
                        </article>

                        <article class="dashboard-card">
                            <strong id="energyPoints">-</strong>
                            <span>Energy</span>
                        </article>

                        <article class="dashboard-card highlight-card">
                            <strong id="score">-</strong>
                            <span>Score ranking</span>
                        </article>
                    </div>
                </section>

                <hr>

                <section>
                    <h2>Comunicazione e storico</h2>

                    <div class="dashboard-grid">
                        <article class="dashboard-card">
                            <strong id="announcementsCount">-</strong>
                            <span>Comunicati</span>
                        </article>

                        <article class="dashboard-card">
                            <strong id="decisionEventsCount">-</strong>
                            <span>Eventi storici</span>
                        </article>
                    </div>
                </section>

                <hr>

                <section>
                    <h2>Lettura sintetica</h2>
                    <p id="dashboard-summary" class="dashboard-summary">
                        -
                    </p>
                </section>
            </section>
        </div>
    </section>
</main>
</body>
</html>