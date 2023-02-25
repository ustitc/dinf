DROP TABLE login_oauth_google;

CREATE TABLE login_oauth_google(
    user INTEGER UNIQUE NOT NULL,
    google_id TEXT UNIQUE NOT NULL,
    created_at TEXT NOT NULL DEFAULT (date('now')),
    FOREIGN KEY (user) REFERENCES users(id) ON DELETE CASCADE ON UPDATE NO ACTION
);
