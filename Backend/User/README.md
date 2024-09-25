# SMU-CS203

## Workspace for User Management Microservice

### Quick Reference API Endpoints

### POST /profile

---
Registers a new user minimally using an email, a hashed password and if user is an admin, defined in request body. When isAdmin == 1, user is an admin while when isAdmin == 0, user is a normal user. ***WORKS***

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
Validates existing user using their email and a hashed password, defined in request body. ***WORKS***

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
    "message": "Success",
    "data": {
        "email": "user@example.com",
        "jwt": "JWT_TOKEN_VALUE"
    }
}
```

Sample Failed 401 Response:

```json
{"message": "Invalid email or password"}
```

Failed 500 Response:

```json
{
    "message": "Error: Authentication failed"
}
```

### POST /logout

---
Logout user using UUID provided in request body ***WORKS***

Sample Request Body:

```json
{"uuid": "sample-user-uuid"}
```

Sample Success 200 Response:

```json
{"message": "User logged out successfully."}
```

### GET /profile/:uid

---
Retrieves a profile by a UUID provided in params. ***WORKS***

Sample Success 200 Response:

```json
{
    "message": "Profile found",
    "data": {
        "email": "user@example.com",
        "name": "User Name",
        "dob": "YYYY-MM-DD",
        "elo": "User Elo Rating",
        "isAdmin": 1
    }
}
```

Sample Failed 500 Response:

```json
{
    "message": "Error: User not found"
}
```

### POST /profile/update

---
Updates user profile data. Request body takes in a JSON definition of the changes provided following the format of: ***WORKS***

```json
{
    "uuid": "user-uuid",
    "email": "user@example.com",
    "password": "hashed_password_here",
    "name": "New Name",
    "isAdmin": 1,
    "dob": "2000-01-01",
    "elo": "1500"
}

```

Sample Success 200 Response:

```json
{
    "message": "User updated successfully"
}

```

Sample Success 400 Response:

```json
{
    "message": "Invalid input data"
}

```

Sample Success 500 Response:

```json
{
    "message": "Failed to update user"
}

```

### POST /namelist

---
Retrieves the namelist of all users in a list given an array of UUIDs. Returns a dictionary where the key is UUID and value is name.

Sample Success 200 Response:

```json
{
    "message": "Names retrieved successfully",
    "data": {
        "uuid1": "Name1",
        "uuid2": "Name2",
        "uuid3": "Name3"
    }
}
```

Sample Success 500 Response:

```json
{
    "message": "Error: Unable to retrieve names"
}
```
