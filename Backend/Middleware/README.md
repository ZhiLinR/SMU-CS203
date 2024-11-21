# SMU-CS203

## Quick Links

1. [Workspace for Middleware Microservice](#workspace-for-middleware-microservice)
2. [Quick Reference API Endpoints](#quick-reference-api-endpoints)
3. [API Endpoints with Requests and Responses](#api-endpoints-with-requests-and-responses)

## Workspace for Middleware Management Microservice

To start the microservice, run the commands below in order

```console
cd Backend\Middleware
mvn clean install
mvn spring-boot:run
```

## Quick Reference API Endpoints

### Health Check Endpoint

| HTTP Method | Endpoint               | Description                                     |
|-------------|-----------------------|-------------------------------------------------|
| `GET`       |  [/auth/health](#get-authhealth) | Check the health of the application. |

### Middleware Endpoint

| HTTP Method | Endpoint                             | Description                                            |
|-------------|-------------------------------------|--------------------------------------------------------|
| `GET`       | [/auth/jwt](#get-authjwt) | Checks validity of JWT and values and returns the UUID and Role stored in the JWT. |

## API Endpoints with Requests and Responses

### GET /auth/health

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

### GET /auth/jwt

**Header - Authorization: < jwt-value >**

---
Checks validity of JWT and values and returns the UUID and Role stored in the JWT.

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
