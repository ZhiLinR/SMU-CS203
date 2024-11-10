# SMU-CS203

## Quick Links

1. [Workspace for Matchmaking Microservice](#workspace-for-matchmaking-microservice)
2. [Quick Reference API Endpoints](#quick-reference-api-endpoints)
3. [API Endpoints with Requests and Responses](#api-endpoints-with-requests-and-responses)

## Workspace for Matchmaking Microservice

To start the microservice, run the commands below in order

```console
cd Backend\Matchmaking
mvn clean install
mvn spring-boot:run
```

## Quick Reference API Endpoints

### Health Check Endpoint

| HTTP Method | Endpoint               | Description                                     |
|-------------|-----------------------|-------------------------------------------------|
| `GET`       |  [/health](#get-health) | Check the health of the application.            |

### Matchmaking Endpoint

| HTTP Method | Endpoint                             | Description                                            |
|-------------|-------------------------------------|--------------------------------------------------------|
| `GET`       | [/matchmaking/{TournamentID}](#get-matchmakingtournamentid) | Matchmake users for the most recent round of the tournament with the given Tournament ID. |
| `GET`       | [/ranking/{TournamentID}](#get-rankingtournamentid) | Generate and return player names in order of rank and update participants' Elo given Tournament ID. |

## API Endpoints with Requests and Responses

### GET /health

---
Check the health of the application

Sample Success 200 Response:

```json
{
    "success": true,
    "message": "Health Check Success",
    "content": null
}
```

Sample Success 500 Response:

```json
{
    "success": false,
    "message": "Error: <Error Message>",
    "content": null
}
```

### GET /matchmaking/{TournamentID}

---
Matchmake users for the most recent round for the tournament with the given Tournament ID.

Sample Success 200 Response:

```json
{
    "success": true,
    "message": "Players matched successfully",
    "content": null
}
```

Sample Failed 400 Response:

```json
{
    "success": false,
    "message": "TournamentID must not be null or empty.",
    "content": null
}
```

Sample Failed 404 Response:

```json
{
    "success": false,
    "message": "Tournament not found.",
    "content": null
}
```

### GET /ranking/{TournamentID}

---
Generate and return rankings of a tournament and update participants' Elo given Tournament ID.

Sample Success 200 Response:

```json
{
    "success": true,
    "message": "Players ranked successfully",
    "content": {
        "results": [
            "name-1",
            "name-2",
            "name-3"
        ]
    }
}
```

Sample Failed 400 Response:

```json
{
    "success": false,
    "message": "TournamentID must not be null or empty.",
    "content": null
}
```

Sample Failed 404 Response:

```json
{
    "success": false,
    "message": "Tournament not found.",
    "content": null
}
```
