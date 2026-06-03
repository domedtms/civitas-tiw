USE civitas_db;

-- ==================================================
-- CIVITAS SEED DATA
-- Final Project Release — v3.0.0
-- ==================================================

-- 1. Users
INSERT INTO users (id, username, email, password_hash)
VALUES
    (
        1,
        'founder',
        'founder@civitas.test',
        '65536:Y2l2aXRhcy1mb3VuZGVyLQ==:ASXi7BMr1LL+5NSMvJ2/1DdUl6M4GSjlrQtf1HV+1kg='
    ),
    (
        2,
        'citizen',
        'citizen@civitas.test',
        '65536:Y2l2aXRhcy1jaXRpemVuLQ==:Mzj7QgW17CfpIGRFC9DDjKUal00OYAkIs/seRoaS+F4='
    ),
    (
        3,
        'minister',
        'minister@civitas.test',
        '65536:Y2l2aXRhcy1mb3VuZGVyLQ==:ASXi7BMr1LL+5NSMvJ2/1DdUl6M4GSjlrQtf1HV+1kg='
    )
ON DUPLICATE KEY UPDATE
    username = VALUES(username),
    email = VALUES(email),
    password_hash = VALUES(password_hash);

-- 2. Nations
INSERT INTO nations (id, name, motto, description, flag_symbol, founder_id)
VALUES
    (
        1,
        'Repubblica del Caffè',
        'Un espresso, un voto',
        'Micro-nazione dimostrativa usata per validare il progetto Civitas completo.',
        '☕',
        1
    )
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    motto = VALUES(motto),
    description = VALUES(description),
    flag_symbol = VALUES(flag_symbol),
    founder_id = VALUES(founder_id);

-- 3. Memberships
INSERT INTO memberships (user_id, nation_id, role)
VALUES
    (1, 1, 'FOUNDER'),
    (2, 1, 'CITIZEN'),
    (3, 1, 'MINISTER')
ON DUPLICATE KEY UPDATE
    role = VALUES(role);

-- 4. Nation resources
INSERT INTO nation_resources (nation_id, coins, culture_points, energy_points)
VALUES
    (1, 120, 25, 15)
ON DUPLICATE KEY UPDATE
    coins = VALUES(coins),
    culture_points = VALUES(culture_points),
    energy_points = VALUES(energy_points);

-- 5. Announcements
INSERT INTO announcements (id, nation_id, author_id, title, content)
VALUES
    (
        1,
        1,
        1,
        'Apertura dell’assemblea nazionale',
        'La Repubblica del Caffè apre ufficialmente la propria assemblea nazionale simbolica.'
    ),
    (
        2,
        1,
        3,
        'Comunicazione del Ministero del Caffè',
        'Il ministero invita tutti i cittadini a partecipare alle prossime votazioni legislative.'
    )
ON DUPLICATE KEY UPDATE
    nation_id = VALUES(nation_id),
    author_id = VALUES(author_id),
    title = VALUES(title),
    content = VALUES(content);

-- 6. Laws
INSERT INTO laws (id, nation_id, proposer_id, title, description, status, closed_at)
VALUES
    (
        1,
        1,
        1,
        'Legge sul calendario del caffè',
        'Ogni cittadino può proporre una pausa caffè simbolica durante le assemblee nazionali.',
        'APPROVED',
        CURRENT_TIMESTAMP
    ),
    (
        2,
        1,
        2,
        'Legge sulle tazze obbligatorie',
        'Ogni cittadino dovrebbe possedere una tazza ufficiale della micro-nazione.',
        'PROPOSED',
        NULL
    )
ON DUPLICATE KEY UPDATE
    nation_id = VALUES(nation_id),
    proposer_id = VALUES(proposer_id),
    title = VALUES(title),
    description = VALUES(description),
    status = VALUES(status),
    closed_at = VALUES(closed_at);

-- 7. Votes
INSERT INTO votes (law_id, user_id, vote_value)
VALUES
    (1, 1, 'YES'),
    (1, 2, 'YES'),
    (1, 3, 'ABSTAIN'),
    (2, 1, 'YES'),
    (2, 2, 'NO')
ON DUPLICATE KEY UPDATE
    vote_value = VALUES(vote_value);

-- 8. Decision logs
INSERT INTO decision_logs (id, nation_id, law_id, actor_id, action, description)
VALUES
    (
        1,
        1,
        1,
        1,
        'LAW_PROPOSED',
        'Seed event: law proposed.'
    ),
    (
        2,
        1,
        1,
        1,
        'LAW_APPROVED',
        'Seed event: law approved.'
    ),
    (
        3,
        1,
        1,
        NULL,
        'RESOURCE_UPDATED',
        'Seed event: resources updated after law approval.'
    ),
    (
        4,
        1,
        NULL,
        1,
        'ROLE_UPDATED',
        'Seed event: user promoted to minister.'
    ),
    (
        5,
        1,
        NULL,
        1,
        'RESOURCE_UPDATED',
        'Seed event: resources updated after official announcement.'
    )
ON DUPLICATE KEY UPDATE
    nation_id = VALUES(nation_id),
    law_id = VALUES(law_id),
    actor_id = VALUES(actor_id),
    action = VALUES(action),
    description = VALUES(description);

-- 9. National newspapers
INSERT INTO national_newspapers (
    id,
    nation_id,
    generated_by,
    period,
    title,
    editorial,
    political_summary,
    resources_summary,
    legislative_summary,
    announcements_summary,
    historical_summary
)
VALUES (
    1,
    1,
    1,
    '2026-06',
    'Giornale Nazionale della Repubblica del Caffè — 2026-06',
    'Edizione dimostrativa del giornale nazionale generato automaticamente per la Repubblica del Caffè.',
    'Il quadro politico mostra una micro-nazione attiva, con leggi proposte, votazioni e decisioni istituzionali registrate.',
    'Le risorse simboliche indicano una fase di sviluppo civico, con coins, cultura ed energia utilizzati per rappresentare lo stato della nazione.',
    'L’attività legislativa comprende proposte e approvazioni che mostrano il funzionamento del processo decisionale.',
    'I comunicati ufficiali dimostrano la presenza di comunicazione istituzionale da parte degli organi della micro-nazione.',
    'Lo storico decisionale registra eventi rilevanti come proposte di legge, approvazioni, aggiornamenti risorse e modifiche di ruolo.'
)
ON DUPLICATE KEY UPDATE
    nation_id = VALUES(nation_id),
    generated_by = VALUES(generated_by),
    period = VALUES(period),
    title = VALUES(title),
    editorial = VALUES(editorial),
    political_summary = VALUES(political_summary),
    resources_summary = VALUES(resources_summary),
    legislative_summary = VALUES(legislative_summary),
    announcements_summary = VALUES(announcements_summary),
    historical_summary = VALUES(historical_summary);