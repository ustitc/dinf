# Schema

``` mermaid
erDiagram
    USERS {
        id INTEGER PK
        name TEXT
        created_at DATE
        updated_at DATE
    }
    LOGIN_EMAIL_PASSWORDS {
        id INTEGER PK
        password TEXT
        created_at DATE
        updated_at DATE
    }
    LOGIN_OAUTH_GOOGLE {
        dice INTEGER FK
        google_id TEXT
        created_at DATE
    }
    USERS ||--o| LOGIN_EMAIL_PASSWORDS : "logins by"
    USERS ||--o| LOGIN_OAUTH_GOOGLE : "logins by"
    
    DICE_OWNERS {
        dice INTEGER FK
        user INTEGER FK
    }
    DICES {
        id INTEGER PK
        name TEXT
        created_at DATE
        updated_at DATE
    }
    
   EDGES {
        id INTEGER PK
        value TEXT
        dice INTEGER FK
    }
   
    USERS ||--o{ DICE_OWNERS : "can be"
    DICES ||--|| DICE_OWNERS : "owned by"
    DICES ||--o{ EDGES : "contains"

```
