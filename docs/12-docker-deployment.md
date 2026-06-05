# Docker Deployment

## Current status

Docker runtime support is not implemented yet.

## Configuration contract prepared in PH1-02

The backend datasource configuration already uses environment variable names that can be reused when Docker Compose is added later:

- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USERNAME`
- `DB_PASSWORD`

This ticket does not provide containers or a runnable Compose stack. It only establishes the backend configuration contract so later Docker work does not need to rename database settings.
