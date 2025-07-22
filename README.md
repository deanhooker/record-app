# Record App

A simple app for ingesting and sorting records, provided in various delimited formats.

## Features

- Accepts records in comma, pipe and space-delimited formats.
- Supports three sorting orders:
  - By favorite color, then last name.
  - By date of birth.
  - By last name (descending).
- Provides both:
  - Command-line interface
  - HTTP REST API
  
## Usage

### CLI

From the root project directory, run the command:
```clojure -M:cli test_data/pipe-delimited.txt test_data/comma-delimited.txt test_data/space-delimited.txt```

The application will display three views of the records from each file, sorted in each of the three supported orderings.

### HTTP REST API

From the root project directory, run the command:
```clojure -M:server```

This will start a server at `http://localhost:3000/` with an empty application state.

#### Endpoints

##### Healthcheck

```GET /ping```
Returns `200 OK` "pong"

##### Submit a record

```
POST /record
Content-Type: text/plain

America|Andy|andy.america@yopmail.com|blue|2/2/1981
```

Supported delimiters are:
- `,` comma
- `|` pipe
- `" "` space

Returns `200 OK` "ok" or `400 Bad Request` with an error message.

##### Get Sorted Records

```
GET /records/color
GET /records/birthdate
GET /records/name
```

All return a `200 OK` with a JSON array of records.

### Run unit tests

From the root project directory, run the command:
``` clojure -M:test```

## Example Records

Each records must have exactly five fields in order:
last-name first-name email favorite-color date-of-birth

- "America|Andy|andy.america@yopmail.com|blue|2/2/1981"
- "Tirekicker,Ruth,ruth.tirekicker@yopmail.com,black,2/7/1984"
- "Homeowner John john.homeowner@yopmail.com white 1/1/1980""
