# URL-Shortener


## 📌 Objective

This project demonstrates how to design and implement a robust, testable REST API for shortening URLs, with production-ready features like:
- **Swagger UI** for interactive API documentation
- **Logging** with Logback to a file (`main.log`)
- **Custom exception handling**
- **Unit tests** for core logic
- A skeleton **Validation Service** for extensible input checks

---

## 🛠️ Tools Used

| Tool | Purpose |
|----------------|--------------------------------|
| **Java 21** | Programming language |
| **Spring Boot 3.4.7** | Main framework |
| **MongoDB** | NoSQL database |
| **Logback** | Logging framework |
| **Swagger (springdoc-openapi)** | API documentation |
| **JUnit & Mockito** | Unit testing |
| **Maven** | Build tool |

---

## ✅ Prerequisites

- **Java 21** installed  
- **Maven 3.8+** installed  
- **MongoDB** installed and running locally (default port `27017`)  
- Internet connection to resolve Maven dependencies

---

## 🚀 How to Run

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/url-shortener.git
   cd url-shortener
   
2. **Build the Project**

run:  mvn spring-boot:run

3. **Run the Project**
	mvn spring-boot:run


##Design Considerations

#Api Documentation

the Api endpoints can be seen via swagger by following this link:
http://localhost:8080/swagger-ui/index.html#/ 

| Method | Endpoint           | Description                       |
| ------ | ------------------ | --------------------------------- |
| `POST` | `createUrl`     | Create a new short URL            |
| `GET`  | `/api/{shortUrl}`  | Redirect to the original long URL |
| `GET`  | `/swagger-ui.html` | Access the Swagger UI             |


#Logging
Logging is configured using Logback.
All application logs are written to main.log in the project root directory.
Logs include info, warnings, and error traces for easier debugging and tracing.
