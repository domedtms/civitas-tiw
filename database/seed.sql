USE civitas_db;

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
    )
ON DUPLICATE KEY UPDATE
    username = VALUES(username),
    email = VALUES(email),
    password_hash = VALUES(password_hash);

INSERT INTO nations (id, name, motto, description, flag_symbol, founder_id)
VALUES
    (
        1,
        'Repubblica del Caffè',
        'Un espresso, un voto',
        'Micro-nazione dimostrativa usata per validare il Livello 1 di Civitas.',
        '☕',
        1
    )
ON DUPLICATE KEY UPDATE
    motto = VALUES(motto),
    description = VALUES(description),
    flag_symbol = VALUES(flag_symbol),
    founder_id = VALUES(founder_id);

INSERT INTO memberships (user_id, nation_id, role)
VALUES
    (1, 1, 'FOUNDER'),
    (2, 1, 'CITIZEN')
ON DUPLICATE KEY UPDATE
    role = VALUES(role);

INSERT INTO nation_resources (nation_id, coins, culture_points, energy_points)
VALUES
    (1, 100, 50, 25)
ON DUPLICATE KEY UPDATE
    coins = VALUES(coins),
    culture_points = VALUES(culture_points),
    energy_points = VALUES(energy_points);

INSERT INTO announcements (nation_id, author_id, title, content)
VALUES
    (
        1,
        1,
        'Fondazione ufficiale della Repubblica del Caffè',
        'La Repubblica del Caffè viene fondata come micro-nazione dimostrativa per testare comunicati, cittadini e autorizzazioni.'
    );