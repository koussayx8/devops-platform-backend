# 🏂 Ski Station Management API

A comprehensive Spring Boot 3.x REST API for managing a ski station with complete Swagger documentation.

## 🚀 Features

- **Complete CRUD Operations** for all entities
- **Professional Swagger Documentation** with interactive testing
- **Spring Boot 3.x** with Jakarta EE compatibility
- **JPA/Hibernate** integration with MySQL database
- **RESTful API Design** following best practices
- **Comprehensive Error Handling** with proper HTTP status codes

## 📋 Entities

- **Skiers** - Manage skier profiles, subscriptions, and course registrations
- **Courses** - Handle different types of skiing courses (individual, collective)
- **Instructors** - Manage instructor profiles and course assignments
- **Pistes** - Track ski slopes with different difficulty levels
- **Registrations** - Handle course registrations and scheduling
- **Subscriptions** - Manage different subscription types and periods

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.3.4**
- **Spring Data JPA**
- **Hibernate 6.5.3**
- **MySQL Database**
- **Maven**
- **Swagger/OpenAPI 3.0**
- **Lombok**

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/koussayx8/devops-platform-backend.git
   cd devops-platform-backend
   ```

2. **Configure Database**
   - Create a MySQL database named `stationSki`
   - Update `src/main/resources/application.properties` with your database credentials

3. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the API**
   - **Base URL**: `http://localhost:8089/api`
   - **Swagger UI**: `http://localhost:8089/api/swagger-ui/index.html`
   - **OpenAPI JSON**: `http://localhost:8089/api/v3/api-docs`

## 📚 API Documentation

### Swagger UI
Visit `http://localhost:8089/api/swagger-ui/index.html` to access the interactive API documentation where you can:
- View all available endpoints
- Test API calls directly from the browser
- See request/response examples
- Download the OpenAPI specification

### Available Endpoints

#### Skiers Management (`/api/skier/`)
- `POST /add` - Add a new skier
- `POST /addAndAssign/{numCourse}` - Add skier and assign to course
- `PUT /assignToSub/{numSkier}/{numSub}` - Assign skier to subscription
- `PUT /assignToPiste/{numSkier}/{numPiste}` - Assign skier to piste
- `GET /getSkiersBySubscription` - Get skiers by subscription type
- `GET /get/{id-skier}` - Get skier by ID
- `DELETE /delete/{id-skier}` - Delete skier by ID
- `GET /all` - Get all skiers

#### Courses Management (`/api/course/`)
- `POST /add` - Add a new course
- `GET /all` - Get all courses
- `PUT /update` - Update course
- `GET /get/{id-course}` - Get course by ID

#### Instructors Management (`/api/instructor/`)
- `POST /add` - Add a new instructor
- `PUT /addAndAssignToCourse/{numCourse}` - Add instructor and assign to course
- `GET /all` - Get all instructors
- `PUT /update` - Update instructor
- `GET /get/{id-instructor}` - Get instructor by ID

#### Pistes Management (`/api/piste/`)
- `POST /add` - Add a new piste
- `GET /all` - Get all pistes
- `GET /get/{id-piste}` - Get piste by ID
- `DELETE /delete/{id-piste}` - Delete piste by ID

#### Registrations Management (`/api/registration/`)
- `PUT /addAndAssignToSkier/{numSkieur}` - Add registration and assign to skier
- `PUT /assignToCourse/{numRegis}/{numSkieur}` - Assign registration to course
- `PUT /addAndAssignToSkierAndCourse/{numSkieur}/{numCourse}` - Add registration and assign to skier and course
- `GET /numWeeks/{numInstructor}/{support}` - Get instructor teaching weeks by support

#### Subscriptions Management (`/api/subscription/`)
- `POST /add` - Add a new subscription
- `GET /get/{id-subscription}` - Get subscription by ID
- `GET /all/{typeSub}` - Get subscriptions by type
- `PUT /update` - Update subscription
- `GET /all/{date1}/{date2}` - Get subscriptions by date range

## 🗄️ Database Schema

The application automatically creates the following tables:
- `skier` - Skier information
- `course` - Course details
- `instructor` - Instructor profiles
- `piste` - Ski slope information
- `registration` - Course registrations
- `subscription` - Subscription details
- `excursion` - Skier-Piste relationships
- `instructor_courses` - Instructor-Course relationships

## 🔧 Configuration

### Application Properties
```properties
# Server configuration
server.servlet.context-path=/api
server.port=8089

# Database configuration
spring.datasource.url=jdbc:mysql://localhost:3306/stationSki?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=

# JPA/Hibernate configuration
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

## 🧪 Testing

You can test the API using:
1. **Swagger UI** - Interactive testing interface
2. **Postman** - Import the OpenAPI specification
3. **curl** - Command line testing
4. **Any HTTP client** - The API follows REST standards

## 📝 Example Usage

### Create a Skier
```bash
curl -X POST "http://localhost:8089/api/skier/add" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "dateOfBirth": "1990-05-15",
    "city": "Chamonix"
  }'
```

### Get All Skiers
```bash
curl -X GET "http://localhost:8089/api/skier/all"
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Koussay** - [GitHub Profile](https://github.com/koussayx8)

## 🙏 Acknowledgments

- Spring Boot team for the amazing framework
- OpenAPI/Swagger community for the documentation tools
- All contributors who helped improve this project

---

**Happy Coding! 🎿⛷️**
