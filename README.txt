
# Project Name: TournamentUserMVSC

## Description

Briefly describe the purpose of the microservice, its primary functionality, and its importance within the larger application or system. 

---

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [Environment Variables](#environment-variables)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)

---

## Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/ZhiLinR/SMU-CS203.git
   cd project-name
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

---

## Usage

1. **Start the server:**
   ```bash
   npm start
   ```
   
2. **Run in development mode with hot-reloading (optional):**
   ```bash
   npm run dev
   ```

3. The server will run on `http://localhost:3000` by default, unless specified otherwise.

---

## Configuration

- **Port:** Modify the default port in `config/default.json` or use an environment variable.
- **Database** (optional): Update the database connection details if applicable.

---

## Environment Variables

List the required environment variables here. For example:

| Variable          | Description                 | Default       |
|-------------------|-----------------------------|---------------|
| `PORT`            | Port for the server         | `32781`        |
| `User`	    | Username			  | `TournamentAccess`|
| `Password`	    |Password for database	  | `aaV22EsNZggD3gkaR9CE`|

---

## API Endpoints

###Endpoint
- **URL:** `/tournaments/matchups/:tournamentId`
- **Method:** `GET`
- **Description:** Get matchup of the user.
- **Response:** JSON format example

```json
{
    "message": "Tournament Matchups:",
    "success": true,
    "content": [
        {
            "player1": "cf23bee7-7cc6-11ef-b4bc-0242ac110002",
            "player2": "e566f910-799f-11ef-b4bc-0242ac110002",
            "playerWon": "e566f910-799f-11ef-b4bc-0242ac110002",
            "tournamentID": "d53ce78b-799f-11ef-b4bc-0242ac110002",
            "roundNum": 2
        },
        {
            "player1": "e566f910-799f-11ef-b4bc-0242ac110002",
            "player2": "b25746cd-7b31-11ef-b4bc-0242ac110002",
            "playerWon": null,
            "tournamentID": "d53ce78b-799f-11ef-b4bc-0242ac110002",
            "roundNum": 3
        },
        {
            "player1": "e566f910-799f-11ef-b4bc-0242ac110002",
            "player2": "null",
            "playerWon": "e566f910-799f-11ef-b4bc-0242ac110002",
            "tournamentID": "d53ce78b-799f-11ef-b4bc-0242ac110002",
            "roundNum": 1
        }
    ]
}
```



## API Endpoints

###Endpoint
- **URL:** `/tournaments/signup/:UUID`
- **Method:** `POST`
- **Description:** Sign up user for the tournament.
- **Response:** JSON format example

```json
{
    "message": "Successfully signed up for the tournament",
    "success": true,
    "content": {
        "UUID": "e566f910-799f-11ef-b4bc-0242ac110002",
        "tournamentID": "c6193074-8a13-11ef-8c7b-0242ac110003",
        "elo": "1600"
    }
}
```

## API Endpoints

###Endpoint
- **URL:** `/tournaments/quit/:UUID`
- **Method:** `DELETE`
- **Description:** Allow user to quit from the signups they registered for a tournament.
- **Response:** JSON format example

```json
{
    "message": "Successfully quit from the tournament",
    "success": true,
    "content": null
}
```

## API Endpoints

###Endpoint
- **URL:** `/tournaments/view/upcoming`
- **Method:** `GET`
- **Description:** Get upcoming tournament.
- **Response:** JSON format example

```json
{
    "message": "Upcoming tournaments",
    "success": true,
    "content": [
        [
            {
                "tournamentID": "09062b64-8a12-11ef-8c7b-0242ac110003",
                "startDate": "2024-11-19T16:00:00.000Z",
                "endDate": "2024-11-28T16:00:00.000Z",
                "location": "MMMM",
                "playerLimit": 10,
                "name": "ZZZZ",
                "status": "Upcoming"
            },
            {
                "tournamentID": "5cd8250b-8a01-11ef-8c7b-0242ac110003",
                "startDate": "2024-10-29T16:00:00.000Z",
                "endDate": "2024-11-14T16:00:00.000Z",
                "location": "Nevada",
                "playerLimit": 10,
                "name": "James Tournament",
                "status": "Upcoming"
            }
                   ],
        {
            "fieldCount": 0,
            "affectedRows": 0,
            "insertId": 0,
            "info": "",
            "serverStatus": 34,
            "warningStatus": 0,
            "changedRows": 0
        }
    ]
}
```

## API Endpoints

###Endpoint
- **URL:** `/tournaments/view/inprogress`
- **Method:** `GET`
- **Description:** Get inprogress tournament.
- **Response:** JSON format example

```json
{
    "message": "In-progress tournaments",
    "success": true,
    "content": [
        [
            {
                "tournamentID": "4621a0b6-8a10-11ef-8c7b-0242ac110003",
                "startDate": "2024-10-19T16:00:00.000Z",
                "endDate": "2024-11-08T16:00:00.000Z",
                "location": "GGGG",
                "playerLimit": 10,
                "name": "GGKia",
                "status": "InProgress"
            },
            {
                "tournamentID": "d53ce211-799f-11ef-b4bc-0242ac110002",
                "startDate": "2024-10-21T16:00:00.000Z",
                "endDate": "2024-10-27T16:00:00.000Z",
                "location": "Riverside Park",
                "playerLimit": 18,
                "name": "Riverside Community Cup",
                "status": "InProgress"
            }
        ],
        {
            "fieldCount": 0,
            "affectedRows": 0,
            "insertId": 0,
            "info": "",
            "serverStatus": 34,
            "warningStatus": 0,
            "changedRows": 0
        }
    ]
}
```

## API Endpoints

###Endpoint
- **URL:** `/tournaments/ranking/:tournamentId`
- **Method:** `GET`
- **Description:** Get user ranking of that tournament.
- **Response:** JSON format example

```json
{
    "message": "Current Tournament Ranking:",
    "success": true,
    "content": [
        {
            "playerWon": "cf23bee7-7cc6-11ef-b4bc-0242ac110002",
            "winCount": 2,
            "ranking": 1
        }
    ]
}
```

## API Endpoints

###Endpoint
- **URL:** `/tournaments/players/:tournamentId`
- **Method:** `GET`
- **Description:** Get a list of players in that tournament.
- **Response:** JSON format example

```json
{
  "status": "success",
  "data": {
    // Sample response data
  }
}
```

## API Endpoints

###Endpoint
- **URL:** `/player/:playerUUID/tournaments`
- **Method:** `GET`
- **Description:** Get all tournaments that user signed up before.
- **Response:** JSON format example

```json
{
  "status": "success",
  "data": {
    // Sample response data
  }
}
```

## API Endpoints

###Endpoint
- **URL:** `/player/:playerUUID/tournaments`
- **Method:** `GET`
- **Description:** Get Tournaments that player signedup for.
- **Response:** JSON format example

```json
{
    "message": "Player tournaments retrieved successfully",
    "success": true,
    "content": [
        {
            "tournamentID": "d53ce78b-799f-11ef-b4bc-0242ac110002",
            "tournamentName": "City Central Year-End Championship",
            "startDate": "2024-12-23T16:00:00.000Z",
            "endDate": "2024-12-29T16:00:00.000Z",
            "location": "City Central",
            "playerID": "cf23bee7-7cc6-11ef-b4bc-0242ac110002",
            "status": "Upcoming",
            "wonLastMatch": 1
        }
    ]
}
```

## API Endpoints

###Endpoint
- **URL:** `/tournaments/:UUID`
- **Method:** `GET`
- **Description:** Get the list of completed tournaments.
- **Response:** JSON format example

```json
{
    "message": "Completed tournaments",
    "success": true,
    "content": [
        [
            {
                "tournamentID": "d53ce095-799f-11ef-b4bc-0242ac110002",
                "startDate": "2024-10-14T16:00:00.000Z",
                "endDate": "2024-10-20T16:00:00.000Z",
                "location": "Sports Complex",
                "playerLimit": 12,
                "name": "Youth Sports Festival",
                "status": "Completed"
            }
	]
}
```
Repeat this for each endpoint.

---

## Testing

1. **Run unit tests:**
   ```bash
   npm test
   ```


Testing framework: Jest, Mocha, or any other tool you are using.

---


