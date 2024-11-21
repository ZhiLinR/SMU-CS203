# SMU-CS203

## Workspace for Tournament Admin Microservice

To start the microservice, run the commands below in order

```console
cd Backend\Tournament_Admin\TournamentAdminService
mvn clean install
mvn spring-boot:run
```

### Quick Reference API Endpoints

## API Endpoints

### Tournament Endpoints

| HTTP Method | Endpoint                          | Description                                         |
|-------------|-----------------------------------|-----------------------------------------------------|
| `POST`      | `/api/tournaments`                | Create a new tournament.                            |
| `GET`       | `/api/tournaments`                | Retrieve a list of all tournaments.                 |
| `GET`       | `/api/tournaments/{tournamentId}` | Retrieve details of a specific tournament by ID.    |
| `PUT`       | `/api/tournaments/{tournamentId}` | Update an existing game result.                     |
| `DELETE`    | `/api/tournaments/{tournamentId}` | Delete a specific game result.                      |
| `GET`       | `/api/tournaments/health`         | Checks if the application is running successfully.  |

### Example Request and Response

### 1. Get All Tournaments

**Endpoint:** `GET /api/tournaments`

**Description:** Retrieves a list of all tournaments.

**Response:**
```json
{
    "message": "Tournaments retrieved successfully",
    "success": true,
    "content": [
        {
            "tournamentID": "01433c4a-87aa-11ef-8c7b-0242ac110003",
            "startDate": "2024-10-10",
            "endDate": "2024-10-12",
            "location": "Pungul",
            "playerLimit": 10,
            "status": "Completed",
            "descOID": "ID073",
            "name": "NorthEastSlummers"
        }
    ]
}
```

### 2. Get tournament by tournament ID

**Endpoint:** `GET /api/tournaments/{tournamentId}`

**Description:** Retrieves a tournament by its tournament ID.

**Response:**
```json
{
    "message": "Tournament retrieved successfully",
    "success": true,
    "content": {
        "tournamentID": "0e6e34ea-8a02-11ef-8c7b-0242ac110003",
        "startDate": "2024-10-30",
        "endDate": "2024-11-15",
        "location": "Nevada",
        "playerLimit": 10,
        "status": "Upcoming",
        "descOID": "ID045",
        "name": "GP Tournament"
    }
}
```

### 3. Update tournament based on tournament ID

**Endpoint:** `PUT /api/tournaments/{tournamentId}`

**Description:** Updates a tournament by its tournament ID.

**Response:**
```json
{
    "message": "Tournament updated successfully",
    "success": true,
    "content": {
        "tournamentID": "0e6e34ea-8a02-11ef-8c7b-0242ac110003",
        "startDate": "2024-10-30",
        "endDate": "2024-11-15",
        "location": "Nevada",
        "playerLimit": 10,
        "status": "Upcoming",
        "descOID": "ID044",
        "name": "GP Tournament"
    }
}
```

### 4. Delete tournament by tournament ID

**Endpoint:** `DELETE /api/tournaments/{tournamentId}`

**Description:** Deletes a tournament by its tournament ID.

**Response:**
```json
{
    "message": "Tournament deleted successfully",
    "success": true,
    "content": null
}
```

### 5. Create a tournament

**Endpoint:** `POST /api/tournaments`

**Description:** Create a tournament

**Request:**
```json
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

**Response:**
```json
{
    "message": "Tournament created successfully",
    "success": true,
    "content": null
}
```

### 6. Health check

**Endpoint:** `GET /api/tournaments/health`

**Description:** Checks if the service is healthy

**Response:**
```json
{
    "message": "Application running successfully",
    "success": true,
    "content": null
}
```

### Game Results Endpoints

| HTTP Method | Endpoint                                    | Description                                        |
|-------------|---------------------------------------------|----------------------------------------------------|
| `POST`      | `/api/matchups/results`                     | Create a new game result.                          |
| `GET`       | `/api/matchups/participants/{tournamentId}` | Retrieve player list  for a specific tournament.   |
| `GET`       | `/api/mathcups/results/{tournamentId}`      | Retrieve game results for a specific tournament.   |
| `PUT`       | `/api/matchups`                             | Update an existing game result.                    |
| `DELETE`    | `/api/matchups`                             | Delete a specific game result.                     |
| `GET`       | `/api/matchups/health`                      | Checks if the application is running successfully. |



### Example Request and Response

### 1. Create new game result

**Endpoint:** `POST /api/matchups/results`

**Description:** Creates a new game result for a tournament

**Request:**
```json
{
    "playerWon": "John Doe",
    "tournamentID": "123",
    "roundNum": 1
}
```

**Response:**
```json
{
    "message": "Game result created successfully",
    "success": true,
    "content": null
}
```

### 2. Get player list

**Endpoint:** `GET /api/matchups/participants/{tournamentId}`

**Description:** Retrieve player list for a specific tournament.

**Response:**
```json
{
    "message": "Participants retrieved successfully",
    "success": true,
    "content": [
        "John Doe",
        "Jane Smith",
        "Alice",
        "Bob"
    ]
}
```

### 3. Get game results for a particular tournament

**Endpoint:** `GET /api/matchups/results/{tournamentId}`

**Description:** Retrieve game results for a specific tournament

**Response:**
```json
{
    "message": "Game results retrieved successfully",
    "success": true,
    "content": [
        {
            "tournamentID": 1,
            "playerWon": "John Doe",
            "roundNum": 1
        },
        {
            "tournamentID": 2,
            "playerWon": "Alice",
            "roundNum": 2
        }
    ]
}
```

### 4. Update game results

**Endpoint:** `PUT /api/matchups/results`

**Description:** Updates game results

**Request:**
```json
{
    "playerWon": "Jane Doe",
    "tournamentID": "123",
    "roundNum": 1
}
```

**Response:**
```json
{
    "message": "Game result updated successfully",
    "success": true,
    "content": null
}
```

### 5. Delete game results

**Endpoint:** `DELETE /api/matchups/results`

**Description:** Deletes a game result

**Request:**
```json
{
    "playerWon": "John Doe",
    "tournamentID": "123",
    "roundNum": 1
}
```

**Response:**
```json
{
    "message": "Game result deleted successfully",
    "success": true,
    "content": null
}
```

### 6. Health check

**Endpoint:** `GET /api/matchups/health`

**Description:** Checks if the service is healthy

**Response:**
```json
{
    "message": "Service is running",
    "success": true,
    "content": null
}
```

