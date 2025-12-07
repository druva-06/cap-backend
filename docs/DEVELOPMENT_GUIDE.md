# Development Guide

## Getting Started

This guide helps developers set up their local development environment and understand the development workflow.

## Prerequisites

### Required Software

1. **Java Development Kit (JDK)**

   - Version: 17 or higher
   - Download: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [Amazon Corretto](https://aws.amazon.com/corretto/)

   ```bash
   # Verify installation
   java -version
   javac -version
   ```

2. **Maven**

   - Version: 3.6+
   - Download: [Apache Maven](https://maven.apache.org/download.cgi)

   ```bash
   # Verify installation
   mvn -version
   ```

3. **MySQL**

   - Version: 8.0+
   - Download: [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)

   ```bash
   # Verify installation
   mysql --version
   ```

4. **IDE** (Choose one)

   - [IntelliJ IDEA](https://www.jetbrains.com/idea/) (Recommended)
   - [Eclipse](https://www.eclipse.org/downloads/)
   - [VS Code](https://code.visualstudio.com/) with Java extensions

5. **Git**
   ```bash
   git --version
   ```

### Recommended Tools

- **Postman**: For API testing
- **MySQL Workbench**: For database management
- **Docker Desktop**: For containerized development
- **AWS CLI**: For AWS services interaction

## Project Setup

### 1. Clone Repository

```bash
git clone https://github.com/your-org/cap-backend.git
cd cap-backend
```

### 2. Database Setup

```bash
# Login to MySQL
mysql -u root -p

# Create database
CREATE DATABASE cap_dev;

# Create user
CREATE USER 'cap_user'@'localhost' IDENTIFIED BY 'cap_password';
GRANT ALL PRIVILEGES ON cap_dev.* TO 'cap_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;

# Run migrations
mysql -u cap_user -p cap_dev < database/migrations/001_create_leads_table.sql
```

### 3. Configuration

**Create local configuration**:

```bash
cp src/main/resources/application-dev.properties.example \
   src/main/resources/application-dev.properties
```

**Update `application-dev.properties`**:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/cap_dev
spring.datasource.username=cap_user
spring.datasource.password=cap_password

# AWS Cognito (get from team lead)
aws.cognito.userPoolId=us-east-1_XXXXXXXX
aws.cognito.clientId=xxxxxxxxxxxxxxxxxxxx
aws.cognito.clientSecret=xxxxxxxxxxxxxxxxxxxxxx

# Logging
logging.level.com.consultancy.education=DEBUG
```

### 4. Build Project

```bash
# Clean and build
mvn clean install

# Skip tests for faster build
mvn clean install -DskipTests
```

### 5. Run Application

```bash
# Run with Maven
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Or run the JAR
java -jar target/education-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

**Application will start on**: `http://localhost:8080`

## IDE Setup

### IntelliJ IDEA

1. **Import Project**:

   - File → Open → Select `cap-backend` folder
   - IntelliJ will auto-detect Maven project

2. **Configure JDK**:

   - File → Project Structure → Project
   - Set SDK to Java 17

3. **Enable Annotation Processing**:

   - Settings → Build → Compiler → Annotation Processors
   - Check "Enable annotation processing" (for Lombok)

4. **Install Plugins**:

   - Lombok Plugin
   - Spring Boot Assistant
   - AWS Toolkit

5. **Run Configuration**:
   - Run → Edit Configurations
   - Add new "Spring Boot" configuration
   - Main class: `com.consultancy.education.EducationApplication`
   - Active profiles: `dev`

### VS Code

1. **Install Extensions**:

   - Extension Pack for Java
   - Spring Boot Extension Pack
   - Lombok Annotations Support

2. **Configure `launch.json`**:

```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Spring Boot",
      "request": "launch",
      "mainClass": "com.consultancy.education.EducationApplication",
      "projectName": "education",
      "args": "--spring.profiles.active=dev"
    }
  ]
}
```

## Development Workflow

### 1. Feature Development

```bash
# Create feature branch
git checkout -b feature/lead-management

# Make changes
# ... code ...

# Run tests
mvn test

# Commit changes
git add .
git commit -m "feat: add lead management functionality"

# Push branch
git push origin feature/lead-management

# Create Pull Request
```

### 2. Commit Message Convention

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

**Types**:

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation
- `style`: Formatting, missing semicolons
- `refactor`: Code restructuring
- `test`: Adding tests
- `chore`: Maintenance tasks

**Examples**:

```bash
git commit -m "feat(leads): add create lead API"
git commit -m "fix(auth): resolve token expiration issue"
git commit -m "docs: update API documentation"
```

### 3. Code Review Process

**Before Creating PR**:

- [ ] Code compiles without errors
- [ ] All tests pass
- [ ] No console warnings
- [ ] Code follows style guide
- [ ] Added/updated tests
- [ ] Updated documentation

**PR Description Template**:

```markdown
## Description

Brief description of changes

## Type of Change

- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing

- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [ ] Manual testing completed

## Checklist

- [ ] Code follows style guidelines
- [ ] Self-review completed
- [ ] Documentation updated
- [ ] No new warnings
```

## Code Style Guide

### Java Code Style

**Naming Conventions**:

```java
// Classes - PascalCase
public class LeadService { }

// Methods - camelCase
public void createLead() { }

// Variables - camelCase
private String firstName;

// Constants - UPPER_SNAKE_CASE
private static final int MAX_RETRIES = 3;

// Packages - lowercase
package com.consultancy.education.service;
```

**Code Organization**:

```java
@Service
public class LeadServiceImpl implements LeadService {

    // 1. Static fields
    private static final Logger log = LoggerFactory.getLogger(LeadServiceImpl.class);

    // 2. Instance fields
    private final LeadRepository leadRepository;
    private final UserRepository userRepository;

    // 3. Constructor
    public LeadServiceImpl(LeadRepository leadRepository,
                           UserRepository userRepository) {
        this.leadRepository = leadRepository;
        this.userRepository = userRepository;
    }

    // 4. Public methods
    @Override
    public LeadResponseDto createLead(LeadRequestDto dto) {
        // Implementation
    }

    // 5. Private helper methods
    private void validateLead(LeadRequestDto dto) {
        // Validation logic
    }
}
```

**Lombok Usage**:

```java
@Data                           // Generates getters, setters, toString, equals, hashCode
@Builder                        // Builder pattern
@NoArgsConstructor              // No-args constructor
@AllArgsConstructor             // All-args constructor
@FieldDefaults(level = AccessLevel.PRIVATE)  // All fields private by default
@Slf4j                         // Logger field
public class Lead {
    Long id;
    String firstName;
    String lastName;
}
```

### Package Structure

```
com.consultancy.education/
├── api/                    # External API clients
├── config/                 # Configuration classes
├── controller/             # REST controllers
├── DTOs/
│   ├── requestDTOs/       # Request DTOs
│   └── responseDTOs/      # Response DTOs
├── enums/                 # Enumerations
├── exception/             # Custom exceptions
├── helper/                # Helper classes
├── model/                 # JPA entities
├── queries/               # Custom queries
├── repository/            # Data access layer
├── response/              # Response wrappers
├── security/              # Security classes
├── service/               # Service interfaces
│   └── impl/             # Service implementations
├── transformer/           # DTO ↔ Entity transformers
├── utils/                 # Utility classes
└── validations/           # Custom validators
```

## Testing

### Unit Tests

**Location**: `src/test/java/`

**Example**:

```java
@ExtendWith(MockitoExtension.class)
class LeadServiceImplTest {

    @Mock
    private LeadRepository leadRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LeadServiceImpl leadService;

    @Test
    void shouldCreateLeadSuccessfully() {
        // Given
        LeadRequestDto requestDto = LeadRequestDto.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john@example.com")
            .phoneNumber("+919876543210")
            .build();

        User creator = new User();
        creator.setId(1L);
        creator.setEmail("admin@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(creator);
        when(leadRepository.findByEmailOrPhoneNumber(anyString(), anyString()))
            .thenReturn(Collections.emptyList());
        when(leadRepository.save(any(Lead.class)))
            .thenAnswer(i -> i.getArgument(0));

        // When
        LeadResponseDto result = leadService.createLead(requestDto, "admin@example.com");

        // Then
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(leadRepository).save(any(Lead.class));
    }

    @Test
    void shouldThrowExceptionForDuplicateLead() {
        // Given
        LeadRequestDto requestDto = LeadRequestDto.builder()
            .email("existing@example.com")
            .phoneNumber("+919876543210")
            .build();

        Lead existingLead = new Lead();
        existingLead.setId(1L);

        when(leadRepository.findByEmailOrPhoneNumber(anyString(), anyString()))
            .thenReturn(List.of(existingLead));

        // When/Then
        assertThrows(CustomException.class, () ->
            leadService.createLead(requestDto, "admin@example.com")
        );
    }
}
```

**Run Tests**:

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=LeadServiceImplTest

# Run specific test method
mvn test -Dtest=LeadServiceImplTest#shouldCreateLeadSuccessfully

# Run with coverage
mvn clean verify
```

### Integration Tests

```java
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LeadControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateLead() throws Exception {
        LeadRequestDto requestDto = LeadRequestDto.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john@example.com")
            .phoneNumber("+919876543210")
            .build();

        mockMvc.perform(post("/leads/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .header("Authorization", "Bearer " + getTestToken()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.firstName").value("John"))
            .andExpect(jsonPath("$.data.lastName").value("Doe"));
    }
}
```

## Debugging

### IntelliJ IDEA

1. Set breakpoints by clicking on line numbers
2. Run in Debug mode (Shift + F9)
3. Use debug controls:
   - Step Over (F8)
   - Step Into (F7)
   - Step Out (Shift + F8)
   - Resume (F9)

### Remote Debugging

**Application JVM Args**:

```bash
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar app.jar
```

**IntelliJ Configuration**:

- Run → Edit Configurations → Remote JVM Debug
- Host: localhost
- Port: 5005

### Logging

```java
@Slf4j
public class LeadService {
    public void createLead() {
        log.debug("Creating lead with email: {}", email);
        log.info("Lead created successfully with ID: {}", id);
        log.warn("Duplicate lead detected: {}", email);
        log.error("Failed to create lead: {}", e.getMessage(), e);
    }
}
```

## Database Management

### Viewing Data

**MySQL Workbench**:

- Connect to localhost:3306
- Browse tables and data
- Run queries

**Command Line**:

```bash
mysql -u cap_user -p cap_dev

# List tables
SHOW TABLES;

# View table structure
DESCRIBE leads;

# Query data
SELECT * FROM leads WHERE status = 'HOT';
```

### Running Migrations

```bash
# Run new migration
mysql -u cap_user -p cap_dev < database/migrations/002_add_column.sql

# Rollback (if rollback script provided)
mysql -u cap_user -p cap_dev < database/migrations/002_add_column_rollback.sql
```

## API Testing

### Using Postman

1. Import collection from `docs/api/CAP_Backend.postman_collection.json`
2. Set environment variables:
   - `base_url`: http://localhost:8080
   - `token`: (obtained from login)

### Using cURL

```bash
# Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "test123"
  }'

# Create Lead (with token)
curl -X POST http://localhost:8080/leads/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "first_name": "John",
    "last_name": "Doe",
    "email": "john@example.com",
    "phone_number": "+919876543210"
  }'
```

## Hot Reload

### Spring Boot DevTools

**Add dependency**:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

**IntelliJ Settings**:

- Settings → Build → Compiler → "Build project automatically"
- Settings → Advanced Settings → "Allow auto-make to start"

Changes to Java files will auto-reload on save.

## Common Issues

### Port Already in Use

```bash
# Find process on port 8080
lsof -i :8080

# Kill process
kill -9 <PID>
```

### Database Connection Failed

- Check MySQL is running
- Verify credentials in application-dev.properties
- Check firewall settings

### Maven Build Failed

```bash
# Clean Maven cache
mvn dependency:purge-local-repository

# Reimport dependencies
mvn clean install -U
```

### Lombok Not Working

- Install Lombok plugin in IDE
- Enable annotation processing
- Restart IDE

## Performance Profiling

### JProfiler

- Attach to running application
- Monitor CPU, memory, threads
- Identify bottlenecks

### Spring Boot Actuator

```properties
# Enable actuator endpoints
management.endpoints.web.exposure.include=*
```

**Endpoints**:

- `/actuator/health` - Health status
- `/actuator/metrics` - Application metrics
- `/actuator/env` - Environment properties

## Documentation

### Generate JavaDoc

```bash
mvn javadoc:javadoc

# View at: target/site/apidocs/index.html
```

### API Documentation

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI spec: http://localhost:8080/v3/api-docs

## Best Practices

1. **Always run tests before committing**
2. **Keep commits small and focused**
3. **Write meaningful commit messages**
4. **Add logging for important operations**
5. **Handle exceptions gracefully**
6. **Validate input data**
7. **Write tests for new features**
8. **Update documentation**
9. **Review your own code first**
10. **Ask for help when stuck**

## Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Lombok Documentation](https://projectlombok.org/features/)
- [JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)

## Getting Help

- **Team Chat**: Slack #cap-backend-dev
- **Tech Lead**: tech-lead@cap.com
- **Documentation**: https://wiki.cap.com
- **Issue Tracker**: JIRA Cap-Backend project
