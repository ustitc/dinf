CREATE TABLE login_oauth_google(
    user INTEGER UNIQUE NOT NULL,
    email TEXT UNIQUE NOT NULL,
    created_at TEXT NOT NULL DEFAULT (date('now')),
    FOREIGN KEY (user) REFERENCES users(id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE login_email_passwords(
    id INTEGER PRIMARY KEY,
    password TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    user INTEGER UNIQUE NOT NULL,
    created_at NOT NULL DEFAULT (date('now')),
    updated_at NOT NULL DEFAULT (date('now')),
    FOREIGN KEY (user) REFERENCES users(id) ON DELETE CASCADE ON UPDATE NO ACTION
);

INSERT INTO login_email_passwords (password, email, user)
SELECT user_passwords.password, users.email, users.id
FROM users
JOIN user_passwords ON users.id = user_passwords.user;

DROP INDEX user_passwords_user_index;
DROP TABLE user_passwords;

CREATE TABLE users_v2(
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    created_at NOT NULL DEFAULT (date('now')),
    updated_at NOT NULL DEFAULT (date('now'))
);

INSERT INTO users_v2 (id, name)
SELECT id, name
FROM users;

DROP INDEX users_email_index;
DROP TABLE users;

ALTER TABLE users_v2 RENAME to users;
