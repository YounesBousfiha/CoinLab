# CoinLab Project


## Overview
CoinLab is a sophisticated Java application designed for financial transaction management and data processing. The project leverages modern Java technologies and follows clean architecture principles.

## 🚀 Features
- Transaction management
- CSV data export
- Robust logging
- Comprehensive testing
- PostgreSQL database integration

## 🔧 Technologies
- **Language**: Java 8
- **Build Tool**: Maven
- **Database**: PostgreSQL
- **Testing**: JUnit 5, Mockito
- **Logging**: SLF4J, Logback
- **CSV Processing**: OpenCSV
- 
## 🏗️ Domain-Driven Design (DDD) Architecture

The project follows Domain-Driven Design principles, organizing the codebase into clear, meaningful layers:

### Domain Layer
- **`domain/entity/`**: Core domain objects representing key business concepts
- **`domain/repository/`**: Abstract interfaces for data persistence
- **`domain/service/`**: Domain logic and business rules
- **`domain/exception/`**: Custom domain-specific exceptions
- **`domain/enums/`**: Domain-specific enumeration types
- **`domain/util/`**: Domain utility classes

### Infrastructure Layer
- **`infrastructure/persistance/`**: Concrete repository implementations
- **`infrastructure/config/`**: Configuration and setup classes
- **`infrastructure/strategy/`**: Strategy pattern implementations
- **`infrastructure/exports/`**: Data export mechanisms

### Application Layer
- **`application/service/`**: Application-level services
- **`application/dto/`**: Data Transfer Objects
- **`application/mapper/`**: Object mapping utilities

### Presentation Layer
- **`presentation/controller/`**: User interface controllers
- **`presentation/response/`**: Response models
- **`presentation/util/`**: Presentation-related utilities

## 🔍 Design Patterns
The project implements several key design patterns to enhance flexibility, maintainability, and scalability:
* Singleton Pattern
* Builder Pattern
* Repository Pattern
* Strategy Pattern


## 📋 Prerequisites
- Java Development Kit (JDK) 8
- Maven 3.6+
- PostgreSQL Database

## 🛠️ Installation
### Local Setup
1. Clone the repository
```shell
git clone https://github.com/yourusername/coinlab.git && cd coinlab
```

2. Build the project

```shell
mvn clean package
```

### Docker Setup
```shell
docker build -t coinlab .
docker run coinlab 
```

## 📦 Dependencies
- PostgreSQL Driver
- OpenCSV
- JUnit Jupiter
- Mockito

## 🧪 Running Tests
```bash
mvn test
```

## 📊 Code Coverage
```shell
mvn verify
```

## 🔒 Configuration
- Configuration files located in `src/main/resources`
- Database connection settings in file `.env`

## 👥 Contributing
1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 💬 Contact

For questions or support, feel free to reach out:  
📧 **younescoder@gmail.com**

