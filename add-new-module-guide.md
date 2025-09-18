# Adding New Modules to Grocery App

## Method 1: Using Spring Initializr (Recommended)

### Step 1: Generate Module via Spring Initializr

1. **Go to Spring Initializr**: https://start.spring.io/
2. **Configure Project**:
   - Project: Maven
   - Language: Java
   - Spring Boot: 3.5.5
   - Group: `com.grocery-app`
   - Artifact: `your-new-module-name` (e.g., `grocery-notification`)
   - Name: `your-new-module-name`
   - Description: Your module description
   - Package name: `com.groceryapp.notification`
   - Packaging: Jar
   - Java: 21

3. **Add Dependencies** (select as needed):
   - Spring Web
   - Spring Data MongoDB
   - Spring Security
   - Validation
   - Lombok
   - Spring Boot DevTools

4. **Generate and Download** the project

### Step 2: Integrate with Multi-Module Project

1. **Extract the downloaded module** into your grocery-app root directory
2. **Update the parent pom.xml** to include the new module:

```xml
<modules>
    <module>grocery-common</module>
    <module>grocery-persistence</module>
    <module>grocery-service</module>
    <module>grocery-security</module>
    <module>grocery-api</module>
    <module>your-new-module-name</module>  <!-- Add this line -->
</modules>
```

3. **Update the new module's pom.xml**:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <!-- Change this parent section -->
    <parent>
        <groupId>com.grocery-app</groupId>
        <artifactId>grocery-app</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    
    <artifactId>your-new-module-name</artifactId>
    <name>your-new-module-name</name>
    <description>Your module description</description>
    
    <dependencies>
        <!-- Add internal module dependencies as needed -->
        <dependency>
            <groupId>com.grocery-app</groupId>
            <artifactId>grocery-common</artifactId>
        </dependency>
        
        <!-- Other dependencies will be managed by parent POM -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <!-- Add other dependencies as needed -->
    </dependencies>
</project>
```

4. **Update parent POM dependency management** (if needed):

```xml
<dependencyManagement>
    <dependencies>
        <!-- Add your new module -->
        <dependency>
            <groupId>com.grocery-app</groupId>
            <artifactId>your-new-module-name</artifactId>
            <version>${project.version}</version>
        </dependency>
        <!-- ... other dependencies ... -->
    </dependencies>
</dependencyManagement>
```

## Method 2: Manual Module Creation

### Step 1: Create Module Structure

```bash
mkdir your-new-module-name
cd your-new-module-name
mkdir -p src/main/java/com/groceryapp/yourmodule
mkdir -p src/main/resources
mkdir -p src/test/java/com/groceryapp/yourmodule
```

### Step 2: Create pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>com.grocery-app</groupId>
        <artifactId>grocery-app</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    
    <artifactId>your-new-module-name</artifactId>
    <name>your-new-module-name</name>
    <description>Your module description</description>
    
    <dependencies>
        <!-- Add dependencies as needed -->
    </dependencies>
</project>
```

### Step 3: Update Parent POM

Add your module to the parent pom.xml modules section.

## Example: Adding a Notification Module

Let's say you want to add a notification module:

### 1. Spring Initializr Configuration:
- Group: `com.grocery-app`
- Artifact: `grocery-notification`
- Dependencies: Spring Web, Spring Boot DevTools, Lombok

### 2. Module Structure:
```
grocery-notification/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── groceryapp/
    │   │           └── notification/
    │   │               ├── service/
    │   │               ├── controller/
    │   │               └── config/
    │   └── resources/
    └── test/
        └── java/
```

### 3. Sample Service Class:
```java
package com.groceryapp.notification.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {
    
    public void sendEmail(String to, String subject, String body) {
        log.info("Sending email to: {}", to);
        // Implementation here
    }
    
    public void sendSms(String phoneNumber, String message) {
        log.info("Sending SMS to: {}", phoneNumber);
        // Implementation here
    }
}
```

## Build and Test

After adding your new module:

```bash
# From the root directory
mvn clean compile

# To run tests
mvn test

# To run the application
mvn spring-boot:run -pl grocery-api
```

## Best Practices

1. **Follow naming conventions**: Use kebab-case for module names
2. **Keep modules focused**: Each module should have a single responsibility
3. **Manage dependencies**: Use the parent POM for dependency management
4. **Package structure**: Follow the established package naming convention
5. **Documentation**: Update README.md with new module information
6. **Testing**: Add appropriate tests for your new module

## Common Module Types

- **grocery-notification**: Email/SMS notifications
- **grocery-payment**: Payment processing
- **grocery-inventory**: Inventory management
- **grocery-reporting**: Reports and analytics
- **grocery-admin**: Admin panel functionality
- **grocery-mobile-api**: Mobile-specific API endpoints