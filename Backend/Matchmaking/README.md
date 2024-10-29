# SMU-CS203

## Workspace for Matchmaking Microservice

To start the microservice, run the commands below in order

```console
cd Backend\Matchmaking
mvn clean install
mvn spring-boot:run
```

### Quick Reference API Endpoints

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
    "success": true,
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
