# CAP Backend Documentation

This directory contains all technical documentation for the MeritCap (CAP) backend system.

## Directory Structure

```
docs/
├── README.md                      # This file
├── ARCHITECTURE.md                # System architecture and design patterns
├── DATABASE_SCHEMA.md             # Complete database schema documentation
├── DEPLOYMENT.md                  # Deployment guide for all environments
├── DEVELOPMENT_GUIDE.md           # Developer setup and workflow guide
│
├── api/                           # API endpoint documentation
│   ├── API_REFERENCE.md           # Complete API reference
│   └── LEAD_API_DOCUMENTATION.md  # Lead management API details
│
└── models/                        # Data model design documentation
    └── LEAD_MODEL_DESIGN.md       # Lead model architecture
```

## Quick Links

### For Developers

- 🚀 [Development Guide](DEVELOPMENT_GUIDE.md) - Setup and workflow
- 🏗️ [Architecture](ARCHITECTURE.md) - System design and patterns
- 🗄️ [Database Schema](DATABASE_SCHEMA.md) - Database structure

### For DevOps

- 🚢 [Deployment Guide](DEPLOYMENT.md) - Deploy to any environment
- 📊 [Monitoring](DEPLOYMENT.md#monitoring) - CloudWatch and logging

### For API Users

- 📡 [API Reference](api/API_REFERENCE.md) - Complete API documentation
- 🎯 [Lead API](api/LEAD_API_DOCUMENTATION.md) - Lead management endpoints

### For Architects

- 🏛️ [Architecture Overview](ARCHITECTURE.md) - High-level design
- 📐 [Data Models](models/LEAD_MODEL_DESIGN.md) - Entity designs

## Documentation Overview

### Core Documentation

#### 1. [ARCHITECTURE.md](ARCHITECTURE.md)

**Purpose**: System architecture, design patterns, and technical decisions

**Contents**:

- Architecture diagrams
- Layer-by-layer breakdown
- Design patterns used
- Technology choices and rationale
- Data flow examples
- Security considerations
- Scalability strategies
- Future enhancements

**Audience**: Architects, Senior Developers, Tech Leads

---

#### 2. [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md)

**Purpose**: Complete database documentation

**Contents**:

- Entity Relationship Diagrams
- Table definitions with all fields
- Indexes and constraints
- Foreign key relationships
- Query optimization tips
- Backup and recovery procedures
- Migration strategy
- Performance monitoring

**Audience**: Database Administrators, Backend Developers

---

#### 3. [DEPLOYMENT.md](DEPLOYMENT.md)

**Purpose**: Deployment procedures for all environments

**Contents**:

- Environment configuration (Dev, UAT, Prod)
- Docker deployment
- Kubernetes deployment
- AWS ECS deployment
- CI/CD pipeline setup
- Health checks and monitoring
- Rollback procedures
- Troubleshooting guide

**Audience**: DevOps Engineers, System Administrators

---

#### 4. [DEVELOPMENT_GUIDE.md](DEVELOPMENT_GUIDE.md)

**Purpose**: Developer onboarding and workflow

**Contents**:

- Prerequisites and setup
- IDE configuration
- Running locally
- Code style guide
- Testing strategies
- Debugging techniques
- API testing
- Common issues and solutions

**Audience**: All Developers

---

### API Documentation

#### 1. [api/API_REFERENCE.md](api/API_REFERENCE.md)

**Purpose**: Complete API endpoint reference

**Contents**:

- All API endpoints
- Authentication methods
- Request/response formats
- Error codes
- Rate limiting
- CORS configuration
- Testing examples (cURL, Postman)
- SDK information

**Audience**: Frontend Developers, API Consumers, Integration Partners

---

#### 2. [api/LEAD_API_DOCUMENTATION.md](api/LEAD_API_DOCUMENTATION.md)

**Purpose**: Detailed lead management API documentation

**Contents**:

- Lead creation workflow
- Encrypted data handling
- Duplicate detection logic
- Frontend integration notes
- Example requests and responses
- Business logic explanation

**Audience**: Frontend Developers, Business Analysts

---

### Model Documentation

#### 1. [models/LEAD_MODEL_DESIGN.md](models/LEAD_MODEL_DESIGN.md)

**Purpose**: Lead model architecture and design

**Contents**:

- Model structure
- Encrypted vs plain fields
- Data flow diagrams
- Security benefits
- Duplicate detection strategy
- Lead workflow states
- Next steps and enhancements

**Audience**: Backend Developers, System Architects

---

## Document Maintenance

### When to Update Documentation

**ARCHITECTURE.md**:

- Major architectural changes
- New design patterns introduced
- Technology stack updates
- Security improvements

**DATABASE_SCHEMA.md**:

- New tables or columns
- Index changes
- Relationship modifications
- Migration additions

**DEPLOYMENT.md**:

- Infrastructure changes
- New deployment targets
- CI/CD pipeline updates
- Environment configuration changes

**DEVELOPMENT_GUIDE.md**:

- Setup process changes
- New tools or dependencies
- Code style updates
- Testing strategy changes

**API Documentation**:

- New endpoints
- Endpoint modifications
- Authentication changes
- Request/response format changes

**Model Documentation**:

- New entities
- Field additions/removals
- Relationship changes
- Business logic updates

## Contributing to Documentation

### Documentation Standards

1. **Use Markdown Format**

   - Clear headings
   - Code blocks with syntax highlighting
   - Tables for structured data
   - Lists for sequences

2. **Include Examples**

   ```bash
   # Good: Concrete example
   curl -X POST http://localhost:8080/api/endpoint

   # Bad: Abstract description
   Make a POST request to the API
   ```

3. **Keep It Updated**

   - Update docs in the same PR as code changes
   - Add changelog entries
   - Review docs during code review

4. **Be Clear and Concise**

   - Write for your audience
   - Avoid jargon when possible
   - Explain complex concepts
   - Use diagrams when helpful

5. **Provide Context**
   - Why, not just what
   - Link to related docs
   - Include troubleshooting tips

### Documentation Review Process

1. **Self-review**

   - Read through your changes
   - Check links work
   - Verify examples run

2. **Peer Review**

   - Request review from teammate
   - Address feedback
   - Update as needed

3. **Final Check**
   - Spell check
   - Grammar check
   - Formatting consistency

## Documentation Templates

### API Endpoint Template

```markdown
### Endpoint Name

**Endpoint**: `METHOD /path`
**Description**: Brief description
**Access**: Role requirements
**Request Body**: Example JSON
**Response**: Example JSON
**Errors**: Possible error codes
```

### Model Documentation Template

```markdown
# Model Name

## Overview

Brief description

## Entity Structure

- Field details
- Relationships
- Constraints

## Database Schema

SQL definition

## Business Logic

How it's used

## Examples

Usage examples
```

## Getting Help

If you need help with documentation:

1. **Check Existing Docs**: Browse this directory
2. **Ask the Team**: Slack #meritcap-backend-docs
3. **Check Wiki**: https://wiki.cap.com
4. **Contact Documentation Lead**: docs-lead@cap.com

## Useful Links

### Internal Resources

- [Project Wiki](https://wiki.cap.com)
- [JIRA Board](https://jira.cap.com)
- [Confluence](https://confluence.cap.com)

### External Resources

- [Spring Boot Docs](https://docs.spring.io/spring-boot/)
- [MySQL Docs](https://dev.mysql.com/doc/)
- [AWS Docs](https://docs.aws.amazon.com/)
- [Docker Docs](https://docs.docker.com/)

## Version History

- **v1.0** (2025-12-07) - Initial documentation structure
  - Architecture documentation
  - Database schema
  - Deployment guide
  - Development guide
  - API reference
  - Lead model design
