USE civitas_db;

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
        'Micro-nazione dimostrativa usata per validare il Livello 2 di Civitas.',
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

-- 5. Laws
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
    title = VALUES(title),
    description = VALUES(description),
    status = VALUES(status),
    closed_at = VALUES(closed_at);

-- 6. Votes
INSERT INTO votes (law_id, user_id, vote_value)
VALUES
    (1, 1, 'YES'),
    (1, 2, 'YES'),
    (1, 3, 'ABSTAIN'),
    (2, 1, 'YES'),
    (2, 2, 'NO')
ON DUPLICATE KEY UPDATE
    vote_value = VALUES(vote_value);

-- 7. Decision logs
INSERT INTO decision_logs (nation_id, law_id, actor_id, action, description)
VALUES
    (
        1,
        1,
        1,
        'LAW_PROPOSED',
        'Seed event: law proposed.'
    ),
    (
        1,
        1,
        1,
        'LAW_APPROVED',
        'Seed event: law approved.'
    ),
    (
        1,
        1,
        NULL,
        'RESOURCE_UPDATED',
        'Seed event: resources updated after law approval.'
    ),
    (
        1,
        NULL,
        1,
        'ROLE_UPDATED',
        'Seed event: user promoted to minister.'
    );