package it.polimi.tiw.civitas.util;

import it.polimi.tiw.civitas.model.DecisionLogAction;
import it.polimi.tiw.civitas.model.LawStatus;
import it.polimi.tiw.civitas.model.MembershipRole;
import it.polimi.tiw.civitas.model.VoteValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class HtmlUtil {

    private static final DateTimeFormatter DISPLAY_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private HtmlUtil() {
        // Utility class: prevents instantiation.
    }

    public static String escape(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    public static String formatDateTime(LocalDateTime value) {
        if (value == null) {
            return "";
        }

        return value.format(DISPLAY_DATE_TIME_FORMATTER);
    }

    public static String label(LawStatus status) {
        if (status == null) {
            return "";
        }

        return switch (status) {
            case PROPOSED -> "Proposta";
            case APPROVED -> "Approvata";
            case REJECTED -> "Respinta";
            case REPEALED -> "Abrogata";
        };
    }

    public static String label(MembershipRole role) {
        if (role == null) {
            return "";
        }

        return switch (role) {
            case FOUNDER -> "Fondatore";
            case MINISTER -> "Ministro";
            case CITIZEN -> "Cittadino";
        };
    }

    public static String label(VoteValue voteValue) {
        if (voteValue == null) {
            return "";
        }

        return switch (voteValue) {
            case YES -> "Sì";
            case NO -> "No";
            case ABSTAIN -> "Astenuto";
        };
    }

    public static String label(DecisionLogAction action) {
        if (action == null) {
            return "";
        }

        return switch (action) {
            case LAW_PROPOSED -> "Legge proposta";
            case LAW_APPROVED -> "Legge approvata";
            case LAW_REJECTED -> "Legge respinta";
            case LAW_REPEALED -> "Legge abrogata";
            case RESOURCE_UPDATED -> "Risorse aggiornate";
            case ROLE_UPDATED -> "Ruolo aggiornato";
        };
    }
}
