# Simple Shop - Spring Boot (minimal)

## Overview
Minimal Spring Boot project with:
- Index page showing products
- Register & Login (simple session-based, no Spring Security)
- Admin product add (image files saved to /uploads)
- Add to cart, checkout with address, orders created

## Setup
1. Install Java 17 and Maven.
2. Create MySQL database:
   - `CREATE DATABASE simple_shop;`
3. Update `src/main/resources/application.properties` with your MySQL username/password.
4. Build and run:
   - `mvn clean package`
   - `java -jar target/simple-shop-0.0.1-SNAPSHOT.jar`
5. Admin user:
   - Register via /register, then manually set role to ROLE_ADMIN in DB to access `/admin/add` or pre-insert a user.
6. Uploaded images are saved to `uploads/` folder next to the running JAR.

