# SMU-CS203

## Workspace for User Management Microservice

---
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
{"message": "User registered successfully"}
```

Sample Failed 400 Response:

```json
{"message": "Email and password are required"}
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

### POST /logout

---
Logout user using UUID provided in request body

Sample Request Body:

```json
{"uuid": "sample-user-uuid"}
```

Sample Success 200 Response:

```json
{"message": "User logged out successfully."}
```

Sample Failed 404 Response:

```json
{
    "message": "Error: User not found"
}
```

Sample Failed 500 Response:

```json
{
    "message": "Error: Logout failed. Either no JWT token exists for this user or the user is already logged out."
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
    "message": "Profile found",
    "data": {
        "email": "user@example.com",
        "name": "User Name",
        "dob": "YYYY-MM-DD",
        "elo": "User Elo Rating"
    }
}
```

Sample Failed 404 Response:

```json
{
    "message": "Error: User not found"
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
    "message": "User updated successfully"
}

```

Sample Success 400 Response:

```json
{
    "message": "Email is required"
}
```

Sample Success 404 Response:

```json
{
    "message": "User not found"
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
    "message": "User ELO updated successfully"
}
```

Sample Failed 404 Response:

```json
{
    "message": "User not found"
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
