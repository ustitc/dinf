# Schema

``` mermaid
erDiagram
    USERS {
        int id
        string name
        date created_at
        date updated_at
    }
    LOGIN_EMAIL_PASSWORDS {
        int id PK
        string password
        date created_at
        date updated_at
    }
    LOGIN_OAUTH_GOOGLE {
        int user FK
        string email
        date created_at
    }
    USERS ||--o| LOGIN_EMAIL_PASSWORDS : "logins by"
    USERS ||--o| LOGIN_OAUTH_GOOGLE : "logins by"
    
    DICE_OWNERS {
        int dice FK
        int user FK
    }
    DICES {
        int id PK
        string name
        date created_at
        date updated_at
    }
    EDGES {
        string text
        int dice FK
    }
    USERS ||--o{ DICE_OWNERS : "can be"
    DICES ||--|| DICE_OWNERS : "owned by"
    DICES ||--o{ EDGES : "contains"

```
