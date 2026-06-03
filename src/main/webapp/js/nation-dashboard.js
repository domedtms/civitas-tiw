document.addEventListener("DOMContentLoaded", function () {
    const root = document.getElementById("dashboard-root");

    if (!root) {
        return;
    }

    const apiUrl = root.dataset.apiUrl;
    const loadingElement = document.getElementById("dashboard-loading");
    const errorElement = document.getElementById("dashboard-error");
    const contentElement = document.getElementById("dashboard-content");

    fetch(apiUrl, {
        method: "GET",
        headers: {
            "Accept": "application/json"
        }
    })
        .then(function (response) {
            if (!response.ok) {
                throw new Error("Stats request failed with status " + response.status);
            }

            return response.json();
        })
        .then(function (stats) {
            updateDashboard(stats);

            loadingElement.classList.add("hidden");
            errorElement.classList.add("hidden");
            contentElement.classList.remove("hidden");
        })
        .catch(function () {
            loadingElement.classList.add("hidden");
            contentElement.classList.add("hidden");
            errorElement.classList.remove("hidden");
        });
});

function updateDashboard(stats) {
    setText("citizensCount", stats.citizensCount);
    setText("foundersCount", stats.foundersCount);
    setText("ministersCount", stats.ministersCount);
    setText("regularCitizensCount", stats.regularCitizensCount);

    setText("lawsCount", stats.lawsCount);
    setText("proposedLawsCount", stats.proposedLawsCount);
    setText("approvedLawsCount", stats.approvedLawsCount);
    setText("rejectedLawsCount", stats.rejectedLawsCount);
    setText("repealedLawsCount", stats.repealedLawsCount);

    setText("coins", stats.coins);
    setText("culturePoints", stats.culturePoints);
    setText("energyPoints", stats.energyPoints);
    setText("score", stats.score);

    setText("announcementsCount", stats.announcementsCount);
    setText("decisionEventsCount", stats.decisionEventsCount);

    setText("dashboard-summary", buildSummary(stats));
}

function setText(elementId, value) {
    const element = document.getElementById(elementId);

    if (element) {
        element.textContent = value;
    }
}

function buildSummary(stats) {
    const parts = [];

    if (stats.approvedLawsCount > stats.rejectedLawsCount) {
        parts.push("La micro-nazione mostra una fase legislativa positiva, con più leggi approvate che respinte.");
    } else if (stats.rejectedLawsCount > stats.approvedLawsCount) {
        parts.push("La micro-nazione attraversa una fase di maggiore conflitto decisionale, con più leggi respinte che approvate.");
    } else {
        parts.push("La micro-nazione presenta un equilibrio tra leggi approvate e respinte.");
    }

    if (stats.energyPoints < 10) {
        parts.push("Il livello di energia è basso e potrebbe richiedere nuove decisioni favorevoli.");
    } else {
        parts.push("Il livello di energia è stabile.");
    }

    if (stats.ministersCount > 0) {
        parts.push("La presenza di ministri indica una struttura di governo più articolata.");
    } else {
        parts.push("Il governo è ancora concentrato sul fondatore.");
    }

    if (stats.announcementsCount > 3) {
        parts.push("La comunicazione istituzionale risulta attiva.");
    }

    return parts.join(" ");
}