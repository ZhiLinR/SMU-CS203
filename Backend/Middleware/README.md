# SMU-CS203

## Workspace for Middleware Management Microservice

To start the microservice, run the commands below in order

```console
cd Backend\Middleware
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

### POST /middleware/checkjwt

---
Checks validity of JWT and values and returns the UUID and Role stored in the JWT.

Sample Request Body:

```json
{
    "jwt": "jwt-value"
}
```

Sample Success 200 Response:

```json
{
    "success": true,
    "message": "JWT validation successful.",
    "content": {
        "uuid": "uuid-value",
        "isAdmin": "0"
    }
}
```

Sample Failed 401 Response:

```json
{
    "success": false,
    "message": "Invalid JWT or session expired.",
    "content": null
}
```

Sample Failed 404 Response:

```json
{
    "success": false,
    "message": "User not found for the provided UUID.",
    "content": null
}
```

Sample Failed 500 Response:

```json
{
    "success": false,
    "message": "An unexpected error occurred.",
    "content": null
}
```
