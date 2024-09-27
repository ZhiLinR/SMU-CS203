# SMU-CS203

## Workspace for User Management Microservice

To start the microservice, run the commands below in order

```console
cd Backend\User
mvn clean install
mvn spring-boot:run
```

### Quick Reference API Endpoints

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
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYWluc0BleGFtcGxlLmNvbSIsInV1aWQiOiIzZWU5YjU3Mi03OWIwLTExZWYtYjRiYy0wMjQyYWMxMTAwMDIiLCJpc0FkbWluIjoxLCJpYXQiOjE3Mjc0Mzg0NDEsImV4cCI6MTcyNzUyNDg0MX0.BZOciW3qU1TOljhdc2XG2MB__i0FyeyGWHcvWpBLI5U",
        "email": "pains@example.com"
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
}```

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
        "email": "pains@example.com",
        "name": "Taint123",
        "dob": "2003-01-01",
        "elo": "200"
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

### PUT /profile/update

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

### PUT /profile/update/elo

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

### POST /profile/all/names-only

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
