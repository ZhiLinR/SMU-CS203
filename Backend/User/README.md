# SMU-CS203

## Workspace for User Management Microservice

### Quick Reference API Endpoints

### POST /profile

---
Registers a new user minimally using an email, a hashed password and if user is an admin, defined in request body. When isAdmin == 1, user is an admin while when isAdmin == 0, user is a normal user.

Sample Request Body:

```json
{
    "email": "user@example.com",
    "password": "hashed_password_here",
    "isAdmin": 1
}
```

Sample Success 200 Response:

```json
{"message": "User registered successfully"}
```

Sample Failed 400 Response:

```json
{"message": "Email and password are required"}
```

Sample Failed 500 Response [DB Connection Error]:

```json
{"message": "Failed to register user"}
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
{"message": "Success", "email": "user@example.com"}
```

Sample Failed 400 Response:

```json
{"message": "Email and password are required"}
```

Sample Failed 401 Response:

```json
{"message": "Invalid email or password"}
```

Failed 500 Response: DB Connection Error

### GET /profile/:uid

---
Retrieves a profile by a UUID provided in params.

Sample Success 200 Response:

```json
{
    "UUID": "aab5d5fd-70c1-11e5-a4fb-b026b977eb28",
    "email": "user@example.com",
    "name": "John Doe",
    "dob": "1990-01-01",
    "elo": "2000"
}
```

Sample Failed 404 Response:

```json
{"message": "Profile not found"}
```

### PUT /profile/:uid

---
Updates user profile data. Request body takes in a JSON definition of the changes provided following the format of:

### GET /:tournament/namelist

---
Retrieves the namelist of all users in a tournament using a tournament ID.
