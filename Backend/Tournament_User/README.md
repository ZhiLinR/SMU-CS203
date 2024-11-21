
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

3. The server will run on `http://localhost:32781` by default, unless specified otherwise.

---

## Configuration

- **Port:** Modify the default port in `config/default.json` or use an environment variable.
- **Database** (optional): Update the database connection details if applicable.

---

## Environment Variables

List the required environment variables here. For example:

| Variable          | Description                 | Default       |
|-------------------|-----------------------------|---------------|


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
            "player1": "Sample",
            "player2": "Sample",
            "playerWon": "Sample",
            "tournamentID": "d53ce78b-799f-11ef-b4bc-0242ac110002",
            "roundNum": 2
        },
        {
            "player1": "Sample",
            "player2": "Sample",
            "playerWon": null,
            "tournamentID": "d53ce78b-799f-11ef-b4bc-0242ac110002",
            "roundNum": 3
        },
        {
            "player1": "Sample",
            "player2": "null",
            "playerWon": "Sample",
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
        "UUID": "Sample",
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
                   ]
    ]
}
```

## API Endpoints

###Endpoint
- **URL:** `/tournaments/view/ongoing`
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
                "tournamentID": "062448c9-9dd9-11ef-ab94-0242ac110003",
                "startDate": "2024-11-06T16:00:00.000Z",
                "endDate": "2024-11-10T16:00:00.000Z",
                "location": "test",
                "name": "tst"
            },
            {
                "tournamentID": "0e6e34ea-8a02-11ef-8c7b-0242ac110003",
                "startDate": "2024-10-29T16:00:00.000Z",
                "endDate": "2024-11-14T16:00:00.000Z",
                "location": "Nevada",
                "name": "GP Tournament"
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
            "Name": "Sample",
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
    "message": "Players in the tournament:",
    "success": true,
    "content": [
        {
            "UUID": "Sample",
            "name": "Sample"
        },
        {
            "UUID": "Sample",
            "name": "Sample"
        },
        {
            "UUID": "Sample",
            "name": "Sample"
        }
    ]
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
            "playerID": "Sample",
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
    "content": 
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


## API Endpoints

###Endpoint
- **URL:** `/tournaments/view/ranking/:tournamentId`
- **Method:** `GET`
- **Description:** Get the ranking for a specific tournament
- **Response:** JSON format example

```json
{
    "message": "Tournament ranking retrieved successfully",
    "success": true,
    "content": [
        {
            "name": "Sample",
            "winCount": 3,
            "ranking": 1
        },
        {
            "name": "Sample",
            "winCount": 2,
            "ranking": 2
        },
        {
            "name": "Sample",
            "winCount": 1,
            "ranking": 3
        }
    ]
}
```

## API Endpoints

###Endpoint
- **URL:** `/tournaments/view/ranking/:tournamentId`
- **Method:** `GET`
- **Description:** Get the ranking for a specific tournament
- **Response:** JSON format example

```json
{
    "message": "Tournament matchups retrieved successfully",
    "success": true,
    "content": [
        {
            "player1Name": "Sample",
            "player2Name": "Sample",
            "playerWonName": "Sample",
            "tournamentID": "d53ce78b-799f-11ef-b4bc-0242ac110002",
            "roundNum": 1
        },
        {
            "player1Name": "Sample",
            "player2Name": "Sample",
            "playerWonName": "Sample",
            "tournamentID": "d53ce78b-799f-11ef-b4bc-0242ac110002",
            "roundNum": 1
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


