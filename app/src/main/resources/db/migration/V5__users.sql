CREATE TABLE users(
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL
);

CREATE TABLE user_passwords(
    id INTEGER PRIMARY KEY,
    password TEXT NOT NULL,
    user INTEGER NOT NULL,
    FOREIGN KEY (user) REFERENCES users(id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE INDEX users_email_index ON users(email);
CREATE INDEX user_passwords_user_index ON user_passwords(user);
