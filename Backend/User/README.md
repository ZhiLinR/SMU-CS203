# SMU-CS203

## Quick Links
---
1. [Workspace for Matchmaking Microservice](#workspace-for-matchmaking-microservice)
2. [Quick Reference API Endpoints](#quick-reference-api-endpoints)
3. [API Endpoints with Requests and Responses](#api-endpoints-with-requests-and-responses)


## Workspace for User Management Microservice

To start the microservice, run the commands below in order

```console
cd Backend\User
mvn clean install
mvn spring-boot:run
```

## Quick Reference API Endpoints

### Health Check Endpoint

### Quick Reference API Endpoints

### Health Check Endpoint

| HTTP Method | Endpoint | Description                                     |
|-------------|----------|-------------------------------------------------|
| `GET`       | [/health](#get-health) | Check the health of the application.            |

### User Authentication Endpoints

| HTTP Method | Endpoint | Description                                     |
|-------------|----------|-------------------------------------------------|
| `POST`      | [/register](#post-register) | Registers a new user with an email, hashed password, and admin status. |
| `POST`      | [/login](#post-login) | Validates an existing user using their email and hashed password. |
| `POST`      | [/logout](#post-logout) | Logs out a user using their UUID.               |

### User Profile Endpoints

| HTTP Method | Endpoint | Description                                     |
|-------------|----------|-------------------------------------------------|
| `POST`      | [/profile](#post-profile) | Retrieves a user profile by UUID.               |
| `PUT`       | [/profile](#put-profile) | Updates user profile data.                   |
| `PUT`       | [/profile/elo](#put-profileelo) | Updates a user's ELO rating.              |
| `POST`      | [/profile/namelist](#post-profilenamelist) | Retrieves a list of user names by UUIDs. |



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
    "success": true,
    "message": "Error: <Error Message>",
    "content": null
}
```

### POST /register

---
Registers a new user minimally using an email, a hashed password and if user is an admin, defined in request body. When isAdmin == 1, user is an admin while when isAdmin == 0, user is a normal user.

Sample Request Body:

```json
{
    "email": "user@example.com",
    "password": "hashed_password_here",
    "name": "John Doe",
    "isAdmin": 1
}
```

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

### POST /login

---
Validates existing user using their email and a hashed password, defined in request body.

Sample Request Body:

```json
{
    "email": "user@example.com",
    "password": "hashed_password_here"
}
```

Sample Success 200 Response:

```json
{
    "success": true,
    "message": "Successful Login",
    "content": {
        "token": "JWT_VALUE",
        "email": "user@example.com"
    }
}
```

Sample Failed 400 Response:

```json
{
    "success": false,
    "message": "Password is required",
    "content": null
}
```

Sample Failed 401 Response:

```json
{
    "success": false,
    "message": "Invalid email or password",
    "content": null
}
```

### POST /logout

---
Logout user using UUID provided in request body

Sample Request Body:

```json
{"uuid": "sample-user-uuid"}
```

Sample Success 200 Response:

```json
{
    "success": true,
    "message": "User logged out successfully.",
    "content": null
}
```

Sample Failed 404 Response:

```json
{
    "success": false,
    "message": "Error: User not found",
    "content": null
}
```

Sample Failed 500 Response:

```json
{
    "success": false,
    "message": "Error: Logout failed. Either no JWT token exists for this user or the user is already logged out.",
    "content": null
}
```

### POST /profile

---
Retrieves a profile by a UUID provided in params.

Sample Request Body:

```json
{"uuid": "sample-user-uuid"}
```

Sample Success 200 Response:

```json
{
    "success": true,
    "message": "Profile found",
    "content": {
        "email": "user@example.com",
        "name": "Name_1",
        "dob": "2000-01-01",
        "elo": "1000"
    }
}
```

Sample Failed 404 Response:

```json
{
    "success": false,
    "message": "Error: User not found",
    "content": null
}
```

### PUT /profile

---
Updates user profile data. Request body takes in a JSON definition of the changes provided

Sample Request Body:

```json
{
    "uuid": "user-uuid",
    "email": "user@example.com",
    "password": "hashed_password_here",
    "name": "New Name",
    "isAdmin": 1,
    "dob": "2000-01-01"
}

```

Sample Success 200 Response:

```json
{
    "success": true,
    "message": "User updated successfully",
    "content": null
}
```

Sample Success 400 Response:

```json
{
    "success": false,
    "message": "Email is required",
    "content": null
}
```

Sample Success 404 Response:

```json
{
    "success": false,
    "message": "User not found",
    "content": null
}
```

### PUT /profile/elo

---
Updates user ELO. Request body takes in a JSON definition of uuid and new ELO

Sample Request Body:

```json
{
    "uuid": "user-uuid",
    "elo": 200,
}
```

Sample Success 200 Response:

```json
{
    "success": true,
    "message": "User ELO updated successfully",
    "content": null
}
```

Sample Failed 404 Response:

```json
{
    "success": false,
    "message": "User not found",
    "content": null
}
```

### POST /profile/namelist

---
Retrieves the namelist of all users in a list given an array of UUIDs. Returns a dictionary where the key is UUID and value is name.

Sample Request Body:

```json
{
    "data": [
        "uuid_1",
        "uuid_2",
        "uuid_3"
    ]
}
```

Sample Success 200 Response:

```json
{
    "success": true,
    "message": "Names retrieved successfully",
    "content": {
        "uuid_1": "name_1",
        "uuid_2": "name_2",
        "uuid_3": "name_3"
    }
}
```

Sample Success 500 Response:

```json
{
    "success": false,
    "message": "Error: Unable to retrieve names",
    "content": null
}
```
