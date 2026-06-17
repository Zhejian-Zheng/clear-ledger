# Clear Ledger

Clear Ledger is a simple Spring Boot ledger API for recording income and expense transactions.

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 Database
- Maven

## Run The App

From the project root:

```bash
./mvnw spring-boot:run
```

The API will start at:

```text
http://localhost:8080
```

## Run Tests

```bash
./mvnw test
```

## H2 Database Console

After the app starts, open:

```text
http://localhost:8080/h2-console
```

Use these settings:

```text
JDBC URL: jdbc:h2:mem:clearledger
User Name: sa
Password:
```

Leave the password empty.

## API Endpoints

### Health Check

```bash
curl http://localhost:8080/health
```

Expected response:

```text
Clear Ledger is running
```

### Create An Income Transaction

```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{"amount":1000,"type":"INCOME","description":"salary","category":"Work","transactionDate":"2026-06-17"}'
```

### Create An Expense Transaction

```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{"amount":25.5,"type":"EXPENSE","description":"lunch","category":"Food","transactionDate":"2026-06-17"}'
```

### Get All Transactions

```bash
curl http://localhost:8080/transactions
```

### Get One Transaction

Replace `1` with the transaction id.

```bash
curl http://localhost:8080/transactions/1
```

### Delete A Transaction

Replace `1` with the transaction id.

```bash
curl -X DELETE http://localhost:8080/transactions/1
```

### Get Summary

```bash
curl http://localhost:8080/summary
```

Example response:

```json
{
  "totalIncome": 1000,
  "totalExpense": 25.5,
  "balance": 974.5
}
```

## Project Structure

```text
src/main/java/com/clearledger/clearledger
├── controller
├── dto
├── entity
├── repository
└── service
```

## Notes

- `Transaction` is the database entity.
- `TransactionRepository` reads and writes transactions.
- `TransactionService` contains the business logic.
- `TransactionController` exposes transaction APIs.
- `SummaryController` exposes the balance summary API.
