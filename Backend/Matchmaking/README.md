# SMU-CS203

## Workspace for Matchmaking Microservice

To start the microservice, run the commands below in order

```console
cd Backend\Matchmaking
mvn clean install
mvn spring-boot:run
```

### Quick Reference API Endpoints

### GET /matchmaking/{TournamentID}

---
Matchmake users for the most recent round for the tournament with the given Tournament ID.

Sample Success 200 Response:

```json
{
    "success": true,
    "message": "User registered successfully",
    "content": null
}
```

Sample Failed 400 Response:

```json
{
    "success": false,
    "message": "A user with this email already exists.",
    "content": null
}
```
