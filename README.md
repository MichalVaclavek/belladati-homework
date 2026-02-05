# BellaDati Homework (Tapestry 5)

Java 21 web application built on Apache Tapestry 5.8.4 with BellaDati SDK integration.

## Requirements

- Java 21
- Maven 3.8+

## Quick Start

1. Set configuration values (see Configuration below).
2. Run the development server:

```bash
mvn jetty:run
```

Open `http://localhost:8080/`.


## Configuration

Configuration is provided via system properties or environment variables. System properties
take precedence over environment variables. Secrets are not stored in the repository.

### Environment Variables (recommended)

- `BELLADATI_URL`
- `BELLADATI_CONSUMER_KEY`
- `BELLADATI_CONSUMER_SECRET`
- `BELLADATI_USERNAME`
- `BELLADATI_PASSWORD`
- `BELLADATI_DATASET_ID`

Example (PowerShell):

```powershell
$env:BELLADATI_URL = "https://belladati-demo.belladati.com/"
$env:BELLADATI_CONSUMER_KEY = "yourKey"
$env:BELLADATI_CONSUMER_SECRET = "yourSecret"
$env:BELLADATI_USERNAME = "yourUser"
$env:BELLADATI_PASSWORD = "yourPass"
$env:BELLADATI_DATASET_ID = "576"
mvn jetty:run
```

### System Properties (alternative)

```bash
mvn jetty:run \
  -Dbelladati.url=https://belladati-demo.belladati.com/ \
  -Dbelladati.consumerKey=yourKey \
  -Dbelladati.consumerSecret=yourSecret \
  -Dbelladati.username=yourUser \
  -Dbelladati.password=yourPass \
  -Dbelladati.datasetId=576
```

## Common Commands

```bash
# Compile
mvn compile

# Run tests
mvn test

# Build WAR (skip tests)
mvn package -DskipTests

# Build WAR (with tests)
mvn package

# Clean
mvn clean
```

## Project Structure

```
src/
  main/
    java/com/belladati/homework/
      components/
      pages/
      services/
    resources/com/belladati/homework/
      components/
      pages/
    webapp/
      WEB-INF/
      css/
  test/
    java/com/belladati/homework/
```

## Security Notes

- Do not commit credentials.
- Prefer environment variables in local development and CI.

## Troubleshooting

- If the app fails to start, confirm all `BELLADATI_*` values are set or provided as `-D`.
- If authentication fails, check the URL and credentials for the target environment.
