# Database Documentation

This directory contains database-related files for the CAP Backend project.

## Structure

```
database/
├── README.md           # This file
└── migrations/         # SQL migration scripts
    └── 001_create_leads_table.sql
```

## Migrations

Migration files are numbered sequentially and should be executed in order.

### Naming Convention

```
{number}_{description}.sql
```

Example: `001_create_leads_table.sql`

### Current Migrations

1. **001_create_leads_table.sql** - Creates the leads table with all necessary indexes and foreign key constraints

## Running Migrations

### Manually

Execute the SQL files in order against your database:

```bash
mysql -u username -p database_name < database/migrations/001_create_leads_table.sql
```

### Using Flyway or Liquibase

If using a migration tool, place these files in the appropriate directory for your tool.

## Database Configuration

Database configuration is managed through Spring Boot application properties:

- `application.yaml` - Default configuration
- `application-dev.properties` - Development environment
- `application-prod.properties` - Production environment
- `application-uat.yaml` - UAT environment

## Best Practices

1. **Never modify existing migrations** - Create new ones for changes
2. **Test migrations** on development database first
3. **Include rollback scripts** when possible
4. **Document breaking changes** in migration comments
5. **Keep migrations idempotent** when possible
