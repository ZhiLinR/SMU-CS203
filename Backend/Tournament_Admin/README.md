# SMU-CS203

## Workspace for User Management Microservice

To start the microservice, run the commands below in order

```console
cd Backend\Tournament_Admin\TournamentAdminService
mvn clean install
mvn spring-boot:run
```

### Quick Reference API Endpoints

## API Endpoints

### Tournament Endpoints

| HTTP Method | Endpoint                             | Description                                         |
|-------------|-------------------------------------|-----------------------------------------------------|
| `POST`      | `/api/tournaments`                 | Create a new tournament.                            |
| `GET`       | `/api/tournaments`                 | Retrieve a list of all tournaments.                 |
| `GET`       | `/api/tournaments/{tournamentId}`  | Retrieve details of a specific tournament by ID.   |
| `PUT`       | `/api/tournaments/{tournamentId}`  | Update an existing tournament by ID.                |
| `DELETE`    | `/api/tournaments/{tournamentId}`  | Delete a specific tournament by ID.                 |

### Game Results Endpoints

| HTTP Method | Endpoint                             | Description                                         |
|-------------|-------------------------------------|-----------------------------------------------------|
| `GET`       | `/api/matchups/participants/{tournamentId}` | Retrieve player list  for a specific tournament. |
| `GET`       | `/api/mathcups/results/{tournamentId}` | Retrieve game results for a specific tournament.    |
| `PUT`       | `/api/matchups/update`  | Update an existing game result.               |
| `DELETE`    | `/api/matchups/delete`  | Delete a specific game result.             |

### Example Request and Response

#### Create a Tournament
- **Request:**
    ```http
    POST /api/tournaments
    Content-Type: application/json

    {
        "name": "Championship",
        "startDate": "2024-10-10",
        "endDate": "2024-10-12",
        "location": "Stadium A",
        "playerLimit": 10,
        "isActive": true,
        "descOID": "Description of the Championship"
    }
    ```

#### Retrieve results for a specific tournament
- **Response:** 
    ```json
    {
        "tournamentID": "generated_id",
        "startDate": "2024-10-10",
        "endDate": "2024-10-12",
        "location": "Stadium A",
        "playerLimit": 10,
        "isActive": true,
        "descOID": "Description of the Championship",
        "name": "Championship",
        "status": "Completed"
    }
    ```
