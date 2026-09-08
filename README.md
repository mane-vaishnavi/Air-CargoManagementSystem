# Shivray International – Air Cargo Management System

MCA Final Year Java Swing + JDBC + MySQL project.

## Requirements
- JDK 17+
- MySQL 8+
- Maven 3.9+
- IntelliJ IDEA / Eclipse / NetBeans

## Database setup
1. Open MySQL Workbench.
2. Run `database/shivray_air_cargo.sql` completely.
3. Open `src/main/resources/app.properties` and set your MySQL username/password.
4. The application connects to database `shivray_air_cargo`.

Demo users:
- admin / admin (ADMIN)
- staff / staff (STAFF)

## Run
From project root:
`mvn clean package`
then run the generated jar, or run `com.shivray.cargo.Main` from your IDE.

## Features
- Role-based login
- Cargo flight schedule and capacity control
- Shipper/exporter registration with email/phone validation
- Transactional cargo booking and automatic capacity deduction
- Unique PNR and booking number generation
- Shipment tracking/history
- Cancellation with 80% refund and transactional inventory restoration
- Air Waybill database generation
- 12 report actions with PDF output
- Live REST exchange-rate integration using Java HttpClient + Jackson
- FlatLaf modern Swing look and feel
- Prepared statements, indexes, foreign keys, checks, and transaction handling

## Important
The SQL files supplied with the original airline passenger project were intentionally not used as the final schema because they model passenger reservations (`passenger`, `reservation`, `flight`, `cancel`). This project uses a normalized cargo schema while retaining the useful domain concepts such as PNR and cancellation.

## Production hardening checklist
For deployment, move DB credentials to environment variables/secrets, replace demo passwords with BCrypt/Argon2 hashes, configure HTTPS API endpoints, add database backups, logging, audit trails, and package signed releases.
