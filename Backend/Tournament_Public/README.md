# Tournament Public Microservice

## Overview

The **Tournament Public Microservice** provides public endpoints to retrieve information about tournaments. The service includes endpoints to fetch all tournaments, upcoming tournaments, ongoing tournaments, completed tournaments, and specific tournaments by their unique IDs.

## Workspace for Tournament_Public Microservice

To start the microservice, run the commands below in order

```console
cd Backend\Tournament_Public
node server.js
```

## Quick Reference API Endpoints

### Health Check Endpoint

| HTTP Method | Endpoint               | Description                                     |
|-------------|-----------------------|-------------------------------------------------|
| `GET`       |  [/health](#get-health) | Check the health of the application.            |

### Public Tournament Endpoint

| HTTP Method | Endpoint                             | Description                                            |
|-------------|-------------------------------------|--------------------------------------------------------|
| `GET`       | [/tournaments](#get-tournaments) | Retrieves all tournaments|
| `GET`       | [/upcoming-events](#get-upcomingevents) | Fetches upcoming tournaments that haven't started |
| `GET`       | [/events](#get-events) | Returns currently ongoing tournaments|
| `GET`       | [/past-events](#get-pastevents) | Retrieves tournaments that have been completed|
| `GET`       | [/{tournamentID}](#get-tournamentid) | Fetches a specific tournament by its ID |


## API Endpoints with Requests and Responses

### GET /health

---
Check the health of the application

Sample Success 200 Response:

```json
{
    "success": true,
    "status": 200,
    "message": "Public Tournament microservice is healthy",
    "content": {
        "timestamp": "2024-11-09T12:34:56.789Z"
    }
}
```

Sample Failed 500 Response:

```json
{
    "success": false,
    "status": 500,
    "message": "Error: <Error Message>",
    "content": {
        "timestamp": "2024-11-09T12:34:56.789Z"
    }
}
```

### GET  /tournaments

---
Retrieves all tournaments in the system.

Sample Success 200 Response:

```json
{
    "success": true,
    "status": 200,
    "message": "successfully retrieved all tournament",
    "content": [[
         {
                "tournamentID": "01433c4a-87aa-11ef-8c7b-0242ac110003",
                "startDate": "2024-10-10T00:00:00.000Z",
                "endDate": "2024-10-12T00:00:00.000Z",
                "location": "Punguol",
                "playerLimit": 8,
                "status": "Completed",
                "descOID": "ID073",
                "name": "Eastside Competition"
        },
    ]]
}
```

Sample Failed 404 Response:

```json
{
    "success": false,
    "status": 404,
    "message": "Error retrieving completed tournaments:",
    "content": null,
}
```

### GET  /upcoming-events

---
Fetches upcoming tournaments that haven't started.

Sample Success 200 Response:

```json
{
    "success": true,
    "status": 200,
    "message": "successfully retrieved upcoming tournaments",
    "content": [[
          {
                "tournamentID": "09062b64-8a12-11ef-8c7b-0242ac110003",
                "startDate": "2024-11-20T00:00:00.000Z",
                "endDate": "2024-11-29T00:00:00.000Z",
                "location": "SMU",
                "playerLimit": 8,
                "name": "Athirah is Winning",
                "status": "Upcoming"
            },
    ]]
}
```

Sample Failed 404 Response:

```json
{
    "success": false,
    "status": 404,
    "message": "Error retrieving upcoming tournaments:",
    "content": null,
}
```


### GET  /events

---
Returns currently ongoing tournaments.

Sample Success 200 Response:

```json
{
    "success": true,
    "status": 200,
    "message": "successfully retrieved ongoing tournaments",
    "content": [
        [
           {
                "tournamentID": "062448c9-9dd9-11ef-ab94-0242ac110003",
                "startDate": "2024-11-06T16:00:00.000Z",
                "endDate": "2024-11-10T16:00:00.000Z",
                "location": "test",
                "playerLimit": 4,
                "name": "tst",
                "status": "Ongoing"
            }
    ]
    ]
}
```

Sample Failed 404 Response:

```json
{
    "success": false,
    "status": 404,
    "message": "Error retrieving ongoing tournaments:",
    "content": null,
}
```
### GET  /past-events

---
Retrieves tournaments that have been completed.

Sample Success 200 Response:

```json
{
    "success": true,
    "status": 200,
    "message": "successfully retrieved completed tournaments",
    "content": [
        [
           {
                "tournamentID": "d53ce211-799f-11ef-b4bc-0242ac110002",
                "startDate": "2024-10-21T16:00:00.000Z",
                "endDate": "2024-10-27T16:00:00.000Z",
                "location": "Riverside Park",
                "playerLimit": 18,
                "name": "Riverside Community Cup",
                "status": "Completed"
            }
    ]
    ]
}
```

Sample Failed 404 Response:

```json
{
    "success": false,
    "status": 404,
    "message": "Error retrieving completed tournaments:",
    "content": null,
}
```
### GET  /{TournamentID}

---
Fetches a specific tournament by its ID.

Sample Success 200 Response:

```json
{
    "success": true,
    "status": 200,
    "message": "successfully retrieved tournament based on tournament ID",
    "content": {
        "tournamentID": "318b2d4c-7a86-11ef-b4bc-0242ac110002",
        "startDate": "2024-09-24T16:00:00.000Z",
        "endDate": "2024-10-19T16:00:00.000Z",
        "location": "Splashies",
        "playerLimit": 10,
        "status": "Completed",
        "descOID": "ID024",
        "name": "Jomama"
    }
}

```

Sample Failed 400 Response:

```json
{
    "success": false,
    "status": 400,
    "message": "Invalid Tournament ID",
    "content": null
}
```

Sample Failed 404 Response:

```json
{
    "success": false,
    "status": 404,
    "message": "Error retrieving tournament:",
    "content": null,
}
```
