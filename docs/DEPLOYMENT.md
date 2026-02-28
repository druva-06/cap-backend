# Deployment Guide

## Overview

This guide covers deployment procedures for the CAP Backend application across different environments.

## Prerequisites

### System Requirements

- **Java**: 17 or higher
- **Maven**: 3.6+
- **MySQL**: 8.0+
- **Docker**: 20.10+ (for containerized deployment)
- **Kubernetes**: 1.21+ (for K8s deployment)

### AWS Services

- **ECS**: Elastic Container Service
- **RDS**: Relational Database Service (MySQL)
- **Secrets Manager**: For storing credentials
- **ECR**: Elastic Container Registry
- **CloudWatch**: For monitoring and logging
- **Application Load Balancer**: For traffic distribution

## Environment Configuration

### 1. Development Environment

**Setup**:

```bash
# Clone repository
git clone <repository-url>
cd meritcap-backend

# Configure database
cp src/main/resources/application-dev.properties.example \
   src/main/resources/application-dev.properties

# Update database credentials
vim src/main/resources/application-dev.properties

# Run application
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Database Setup**:

```bash
# Create database
mysql -u root -p
CREATE DATABASE meritcap_dev;
CREATE USER 'cap_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON meritcap_dev.* TO 'cap_user'@'localhost';
FLUSH PRIVILEGES;

# Run migrations
mysql -u cap_user -p meritcap_dev < database/migrations/001_create_leads_table.sql
```

### 2. UAT Environment

**AWS Infrastructure**:

- ECS Cluster: `cap-uat-cluster`
- RDS Instance: `cap-uat-db` (db.t3.medium)
- Load Balancer: `cap-uat-alb`
- ECR Repository: `meritcap-backend-uat`

**Deployment Steps**:

```bash
# Build application
mvn clean package -DskipTests

# Build Docker image
docker build -t meritcap-backend:uat .

# Tag for ECR
aws ecr get-login-password --region us-east-1 | \
  docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com

docker tag meritcap-backend:uat <account-id>.dkr.ecr.us-east-1.amazonaws.com/meritcap-backend-uat:latest

# Push to ECR
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/meritcap-backend-uat:latest

# Update ECS service
aws ecs update-service --cluster cap-uat-cluster \
  --service meritcap-backend-service --force-new-deployment
```

### 3. Production Environment

**AWS Infrastructure**:

- ECS Cluster: `cap-prod-cluster` (Multi-AZ)
- RDS Instance: `cap-prod-db` (db.r5.large, Multi-AZ)
- Load Balancer: `cap-prod-alb`
- ECR Repository: `meritcap-backend-prod`
- Auto Scaling: Min 2, Max 10 tasks

**Deployment Steps**:

```bash
# Same as UAT but with production tags
docker build -t meritcap-backend:prod .
docker tag meritcap-backend:prod <account-id>.dkr.ecr.us-east-1.amazonaws.com/meritcap-backend-prod:v1.0.0
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/meritcap-backend-prod:v1.0.0

# Update production service (with gradual rollout)
aws ecs update-service --cluster cap-prod-cluster \
  --service meritcap-backend-service \
  --task-definition meritcap-backend:v1.0.0 \
  --deployment-configuration "maximumPercent=200,minimumHealthyPercent=100"
```

## Docker Deployment

### Building the Image

**Dockerfile**:

```dockerfile
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Build Command**:

```bash
docker build -t meritcap-backend:latest .
```

### Running the Container

**Basic Run**:

```bash
docker run -d \
  --name meritcap-backend \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=mysql-host \
  -e DB_PORT=3306 \
  -e DB_NAME=cap_prod \
  -e DB_USER=cap_user \
  -e DB_PASSWORD=secure_password \
  meritcap-backend:latest
```

### Docker Compose

**docker-compose.yml**:

```yaml
version: "3.8"

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: prod
      DB_HOST: mysql
      DB_PORT: 3306
      DB_NAME: cap_db
      DB_USER: cap_user
      DB_PASSWORD: secure_password
    depends_on:
      - mysql
    networks:
      - cap-network

  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root_password
      MYSQL_DATABASE: cap_db
      MYSQL_USER: cap_user
      MYSQL_PASSWORD: secure_password
    volumes:
      - mysql-data:/var/lib/mysql
      - ./database/migrations:/docker-entrypoint-initdb.d
    networks:
      - cap-network

networks:
  cap-network:
    driver: bridge

volumes:
  mysql-data:
```

**Running**:

```bash
docker-compose up -d
```

## Kubernetes Deployment

### Prerequisites

**Install kubectl**:

```bash
# macOS
brew install kubectl

# Linux
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
```

### Configuration Files

**meritcap-deployment.yaml**:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: meritcap-backend
  labels:
    app: meritcap-backend
spec:
  replicas: 3
  selector:
    matchLabels:
      app: meritcap-backend
  template:
    metadata:
      labels:
        app: meritcap-backend
    spec:
      containers:
        - name: meritcap-backend
          image: <account-id>.dkr.ecr.us-east-1.amazonaws.com/meritcap-backend:latest
          ports:
            - containerPort: 8080
          env:
            - name: SPRING_PROFILES_ACTIVE
              value: "prod"
            - name: DB_HOST
              valueFrom:
                secretKeyRef:
                  name: cap-secrets
                  key: db-host
            - name: DB_USER
              valueFrom:
                secretKeyRef:
                  name: cap-secrets
                  key: db-user
            - name: DB_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: cap-secrets
                  key: db-password
          resources:
            requests:
              memory: "512Mi"
              cpu: "250m"
            limits:
              memory: "1Gi"
              cpu: "500m"
          livenessProbe:
            httpGet:
              path: /actuator/health
              port: 8080
            initialDelaySeconds: 60
            periodSeconds: 10
          readinessProbe:
            httpGet:
              path: /actuator/health
              port: 8080
            initialDelaySeconds: 30
            periodSeconds: 5
```

**meritcap-service.yaml**:

```yaml
apiVersion: v1
kind: Service
metadata:
  name: meritcap-backend-service
spec:
  type: LoadBalancer
  selector:
    app: meritcap-backend
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
```

**Secrets**:

```bash
# Create secrets
kubectl create secret generic cap-secrets \
  --from-literal=db-host=mysql.example.com \
  --from-literal=db-user=cap_user \
  --from-literal=db-password=secure_password
```

**Deploy**:

```bash
kubectl apply -f k8s/meritcap-deployment.yaml
kubectl apply -f k8s/meritcap-service.yaml

# Check status
kubectl get deployments
kubectl get pods
kubectl get services

# View logs
kubectl logs -f deployment/meritcap-backend
```

### Scaling

**Manual Scaling**:

```bash
kubectl scale deployment meritcap-backend --replicas=5
```

**Auto Scaling**:

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: meritcap-backend-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: meritcap-backend
  minReplicas: 2
  maxReplicas: 10
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 70
```

## AWS ECS Deployment

### Task Definition

**cap-task-definition.json**:

```json
{
  "family": "meritcap-backend",
  "networkMode": "awsvpc",
  "requiresCompatibilities": ["FARGATE"],
  "cpu": "512",
  "memory": "1024",
  "containerDefinitions": [
    {
      "name": "meritcap-backend",
      "image": "<account-id>.dkr.ecr.us-east-1.amazonaws.com/meritcap-backend:latest",
      "portMappings": [
        {
          "containerPort": 8080,
          "protocol": "tcp"
        }
      ],
      "environment": [
        {
          "name": "SPRING_PROFILES_ACTIVE",
          "value": "prod"
        }
      ],
      "secrets": [
        {
          "name": "DB_PASSWORD",
          "valueFrom": "arn:aws:secretsmanager:region:account:secret:cap-db-password"
        }
      ],
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-group": "/ecs/meritcap-backend",
          "awslogs-region": "us-east-1",
          "awslogs-stream-prefix": "ecs"
        }
      },
      "healthCheck": {
        "command": [
          "CMD-SHELL",
          "curl -f http://localhost:8080/actuator/health || exit 1"
        ],
        "interval": 30,
        "timeout": 5,
        "retries": 3,
        "startPeriod": 60
      }
    }
  ]
}
```

**Register Task Definition**:

```bash
aws ecs register-task-definition --cli-input-json file://cap-task-definition.json
```

**Create/Update Service**:

```bash
aws ecs create-service \
  --cluster cap-prod-cluster \
  --service-name meritcap-backend-service \
  --task-definition meritcap-backend \
  --desired-count 3 \
  --launch-type FARGATE \
  --network-configuration "awsvpcConfiguration={subnets=[subnet-xxx,subnet-yyy],securityGroups=[sg-xxx],assignPublicIp=ENABLED}" \
  --load-balancers "targetGroupArn=arn:aws:elasticloadbalancing:region:account:targetgroup/cap-tg,containerName=meritcap-backend,containerPort=8080"
```

## CI/CD Pipeline

### AWS CodeBuild

**buildspec.yml**:

```yaml
version: 0.2

phases:
  pre_build:
    commands:
      - echo Logging in to Amazon ECR...
      - aws ecr get-login-password --region $AWS_DEFAULT_REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com
      - REPOSITORY_URI=$AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com/$IMAGE_REPO_NAME
      - COMMIT_HASH=$(echo $CODEBUILD_RESOLVED_SOURCE_VERSION | cut -c 1-7)
      - IMAGE_TAG=${COMMIT_HASH:=latest}

  build:
    commands:
      - echo Build started on `date`
      - echo Building the Docker image...
      - docker build -t $REPOSITORY_URI:latest .
      - docker tag $REPOSITORY_URI:latest $REPOSITORY_URI:$IMAGE_TAG

  post_build:
    commands:
      - echo Build completed on `date`
      - echo Pushing the Docker images...
      - docker push $REPOSITORY_URI:latest
      - docker push $REPOSITORY_URI:$IMAGE_TAG
      - echo Writing image definitions file...
      - printf '[{"name":"meritcap-backend","imageUri":"%s"}]' $REPOSITORY_URI:$IMAGE_TAG > imagedefinitions.json

artifacts:
  files: imagedefinitions.json
```

### GitHub Actions

**.github/workflows/deploy.yml**:

```yaml
name: Deploy to AWS ECS

on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v2

      - name: Configure AWS credentials
        uses: aws-actions/configure-aws-credentials@v1
        with:
          aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
          aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          aws-region: us-east-1

      - name: Login to Amazon ECR
        id: login-ecr
        uses: aws-actions/amazon-ecr-login@v1

      - name: Build, tag, and push image to Amazon ECR
        env:
          ECR_REGISTRY: ${{ steps.login-ecr.outputs.registry }}
          ECR_REPOSITORY: meritcap-backend
          IMAGE_TAG: ${{ github.sha }}
        run: |
          docker build -t $ECR_REGISTRY/$ECR_REPOSITORY:$IMAGE_TAG .
          docker push $ECR_REGISTRY/$ECR_REPOSITORY:$IMAGE_TAG

      - name: Deploy to ECS
        run: |
          aws ecs update-service \
            --cluster cap-prod-cluster \
            --service meritcap-backend-service \
            --force-new-deployment
```

## Database Migration

### Pre-Deployment

**Backup Database**:

```bash
# Create backup
mysqldump -h $DB_HOST -u $DB_USER -p$DB_PASSWORD $DB_NAME > backup_$(date +%Y%m%d_%H%M%S).sql

# Upload to S3
aws s3 cp backup_*.sql s3://cap-backups/pre-deployment/
```

**Run Migrations**:

```bash
# Test migrations on copy first
mysql -h $DB_HOST -u $DB_USER -p$DB_PASSWORD $DB_NAME_COPY < database/migrations/002_new_migration.sql

# Run on production
mysql -h $DB_HOST -u $DB_USER -p$DB_PASSWORD $DB_NAME < database/migrations/002_new_migration.sql
```

## Health Checks

### Application Health

**Endpoint**: `GET /actuator/health`

**Response**:

```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    },
    "diskSpace": {
      "status": "UP"
    }
  }
}
```

### Load Balancer Health Check

**Configuration**:

- Path: `/actuator/health`
- Port: 8080
- Protocol: HTTP
- Interval: 30 seconds
- Timeout: 5 seconds
- Healthy threshold: 2
- Unhealthy threshold: 3

## Monitoring

### CloudWatch Metrics

**Key Metrics**:

- CPU Utilization
- Memory Utilization
- Request Count
- Response Time
- Error Rate

**Alarms**:

```bash
# CPU Alarm
aws cloudwatch put-metric-alarm \
  --alarm-name meritcap-backend-high-cpu \
  --alarm-description "Alert when CPU exceeds 80%" \
  --metric-name CPUUtilization \
  --namespace AWS/ECS \
  --statistic Average \
  --period 300 \
  --threshold 80 \
  --comparison-operator GreaterThanThreshold \
  --evaluation-periods 2
```

### Application Logs

**View Logs**:

```bash
# ECS logs
aws logs tail /ecs/meritcap-backend --follow

# Kubernetes logs
kubectl logs -f deployment/meritcap-backend

# Docker logs
docker logs -f meritcap-backend
```

## Rollback Procedures

### ECS Rollback

```bash
# List task definitions
aws ecs list-task-definitions --family-prefix meritcap-backend

# Update to previous version
aws ecs update-service \
  --cluster cap-prod-cluster \
  --service meritcap-backend-service \
  --task-definition meritcap-backend:5  # previous version
```

### Kubernetes Rollback

```bash
# View rollout history
kubectl rollout history deployment/meritcap-backend

# Rollback to previous version
kubectl rollout undo deployment/meritcap-backend

# Rollback to specific revision
kubectl rollout undo deployment/meritcap-backend --to-revision=2
```

### Database Rollback

```bash
# Restore from backup
mysql -h $DB_HOST -u $DB_USER -p$DB_PASSWORD $DB_NAME < backup_20251207_100000.sql
```

## Security Checklist

- [ ] Update secrets in AWS Secrets Manager
- [ ] Configure security groups (allow only necessary ports)
- [ ] Enable SSL/TLS on load balancer
- [ ] Update IAM roles and policies
- [ ] Enable CloudWatch logging
- [ ] Configure VPC and subnets
- [ ] Enable database encryption at rest
- [ ] Configure backup retention
- [ ] Set up WAF rules
- [ ] Review and update CORS settings

## Post-Deployment Verification

**Checklist**:

1. Check application health endpoint
2. Verify database connectivity
3. Test authentication flow
4. Check API endpoints
5. Monitor error logs
6. Verify metrics in CloudWatch
7. Test rollback procedure
8. Update documentation

**Smoke Tests**:

```bash
# Health check
curl https://api.cap.com/actuator/health

# Login test
curl -X POST https://api.cap.com/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"test123"}'

# Load test
ab -n 1000 -c 10 https://api.cap.com/actuator/health
```

## Troubleshooting

### Common Issues

**Container Won't Start**:

```bash
# Check task logs
aws ecs describe-tasks --cluster cap-prod-cluster --tasks <task-id>

# Check container logs
aws logs tail /ecs/meritcap-backend --since 10m
```

**Database Connection Failed**:

- Check security group rules
- Verify RDS endpoint
- Check credentials in Secrets Manager
- Test connection from ECS task

**High CPU Usage**:

- Check for memory leaks
- Review slow queries
- Check for infinite loops
- Scale up if needed

## Support

For deployment support:

- DevOps Team: devops@cap.com
- On-call: +1-xxx-xxx-xxxx
- Runbook: https://wiki.cap.com/runbook
