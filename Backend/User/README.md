# SMU-CS203
## Workspace for User Management Microservice

### Quick Reference API Endpoints
#### POST /profile
---
Registers a new user minimally using an email and a hashed password, defined in request body.

Sample Request Body:

### GET /profile/:uid
---
Retrieves a profile by a UUID provided in params.

Sample Success 200 Response:

### PUT /profile/:uid
---
Updates user profile data. Request body takes in a JSON definition of the changes provided following the format of:

### GET /:tournament/namelist
---
Retrieves the namelist of all users in a tournament using a tournament ID. 

