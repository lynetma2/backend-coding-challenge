# backend-coding-challenge
This is my solution repo for the coding challenge.

# Running the API
Please use this command in a terminal from the root of the repo to start the backend.

Windows (Command Prompt):
```
gradlew.bat bootRun
```

macOS / Linux:
```
./gradlew bootRun
```

# Automatic tests
This project includes JaCoCO for coverage on automatic tests.
The tests can be run with the following command:
```
./gradlew clean test jacocoTestReport
```

# Manual tests
This project includes openapi specification as documentation of the endpoints.
It can be used to create test requests.

## Curl commands.
Opening a new account:
```
curl -X 'POST' \
  'http://localhost:8080/account' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "firstName": "John",
  "lastName": "Doe"
}'
```

Getting account information based on the account number (Expected 1):
```
curl -X 'GET' \
  'http://localhost:8080/account/1' \
  -H 'accept: application/json'
```

Deposit money to the given account number (This example account number 1, and 500 DKK)
```
curl -X 'POST' \
  'http://localhost:8080/account/1/deposit' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "amountDKK": -500
}'
```

Transfer money from one account to another (This example account number 1 as source and account number 2 as destination, 250 DKK)
```
curl -X 'POST' \
  'http://localhost:8080/account/1/transfer' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "destinationAccountNumber": 2,
  "amountDKK": 250
}'
```

Retrieve the current exchange rate for 100DKK to USD.
```
curl -X 'GET' \
  'http://localhost:8080/conversion-rate' \
  -H 'accept: application/json'
```

Historical data, This has not been tested as my API key does not allow for this feature.
```
curl -X 'GET' \
  'http://localhost:8080/history-rate' \
  -H 'accept: application/json'
```