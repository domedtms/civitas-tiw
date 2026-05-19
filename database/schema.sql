CREATE DATABASE IF NOT EXISTS civitas_db
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE civitas_db;

DROP TABLE IF EXISTS national_newspapers;
DROP TABLE IF EXISTS decision_logs;
DROP TABLE IF EXISTS votes;
DROP TABLE IF EXISTS laws;
DROP TABLE IF EXISTS announcements;
DROP TABLE IF EXISTS nation_resources;
DROP TABLE IF EXISTS memberships;
DROP TABLE IF EXISTS nations;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_users_username UNIQUE (username),
    CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE TABLE nations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    motto VARCHAR(150),
    description TEXT,
    flag_symbol VARCHAR(20),
    founder_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_nations_name UNIQUE (name),
    CONSTRAINT fk_nations_founder
        FOREIGN KEY (founder_id)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE memberships (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    nation_id INT NOT NULL,
    role ENUM('FOUNDER', 'MINISTER', 'CITIZEN') NOT NULL DEFAULT 'CITIZEN',
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_memberships_user_nation UNIQUE (user_id, nation_id),

    CONSTRAINT fk_memberships_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_memberships_nation
        FOREIGN KEY (nation_id)
        REFERENCES nations(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE nation_resources (
    nation_id INT PRIMARY KEY,
    coins INT NOT NULL DEFAULT 0,
    culture_points INT NOT NULL DEFAULT 0,
    energy_points INT NOT NULL DEFAULT 0,

    CONSTRAINT fk_nation_resources_nation
        FOREIGN KEY (nation_id)
        REFERENCES nations(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE announcements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nation_id INT NOT NULL,
    author_id INT NOT NULL,
    title VARCHAR(120) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_announcements_nation
        FOREIGN KEY (nation_id)
        REFERENCES nations(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_announcements_author
        FOREIGN KEY (author_id)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE laws (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nation_id INT NOT NULL,
    proposer_id INT NOT NULL,
    title VARCHAR(120) NOT NULL,
    description TEXT NOT NULL,
    status ENUM('PROPOSED', 'APPROVED', 'REJECTED', 'REPEALED') NOT NULL DEFAULT 'PROPOSED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    closed_at TIMESTAMP NULL,

    CONSTRAINT fk_laws_nation
        FOREIGN KEY (nation_id)
        REFERENCES nations(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_laws_proposer
        FOREIGN KEY (proposer_id)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE votes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    law_id INT NOT NULL,
    user_id INT NOT NULL,
    vote_value ENUM('YES', 'NO', 'ABSTAIN') NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_votes_law_user UNIQUE (law_id, user_id),

    CONSTRAINT fk_votes_law
        FOREIGN KEY (law_id)
        REFERENCES laws(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_votes_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE decision_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nation_id INT NOT NULL,
    law_id INT NULL,
    actor_id INT NULL,
    action VARCHAR(80) NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_decision_logs_nation
        FOREIGN KEY (nation_id)
        REFERENCES nations(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_decision_logs_law
        FOREIGN KEY (law_id)
        REFERENCES laws(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,

    CONSTRAINT fk_decision_logs_actor
        FOREIGN KEY (actor_id)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

CREATE TABLE national_newspapers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nation_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    content TEXT NOT NULL,
    generated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_national_newspapers_nation
        FOREIGN KEY (nation_id)
        REFERENCES nations(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE INDEX idx_memberships_user_id ON memberships(user_id);
CREATE INDEX idx_memberships_nation_id ON memberships(nation_id);

CREATE INDEX idx_announcements_nation_created
    ON announcements(nation_id, created_at);

CREATE INDEX idx_laws_nation_status
    ON laws(nation_id, status);

CREATE INDEX idx_votes_law_id
    ON votes(law_id);

CREATE INDEX idx_decision_logs_nation_created
    ON decision_logs(nation_id, created_at);

CREATE INDEX idx_national_newspapers_nation_generated
    ON national_newspapers(nation_id, generated_at);