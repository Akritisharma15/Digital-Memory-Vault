# Digital Memory Vault

Java + MySQL project source files.

## Important status
This archive includes the non-empty source files found in the supplied project ZIP.
The original archive had empty/missing files for `LoginHandler.java`, `VaulthHandler.java`,
and `login.html`. `App.java` refers to login and vault handlers, so the project is
not complete/buildable until those missing files are restored or implemented.

## Database configuration
No database password is included. Set these environment variables before running:
- `DB_URL` (default: `jdbc:mysql://localhost:3306/memory_vault`)
- `DB_USER` (default: `root`)
- `DB_PASSWORD` (your local MySQL password)

Do not commit real passwords, API keys, or personal data to GitHub.

## Source layout
- `src/` Java source files
- `lib/` MySQL Connector/J dependency
- `.vscode/` VS Code Java project settings
