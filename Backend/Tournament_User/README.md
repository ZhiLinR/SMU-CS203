# SMU-CS203
## Workspace for Tournament/User Microservice

### Quick Reference API Simple Endpoints
#### GET /tournaments
---
Retrieves all upcoming and inprogress tournaments. 

Sample Success 200 Response: TODO

### GET /tournaments/:uid
---
Retrieves the user's tournament history. 

Sample Success 200 Response: TODO

### POST /tournaments
---
Allows the user to sign up for tournaments, requiring the user's UID and the tournament ID in the request body.

Sample Request Body: TODO

Sample Success 200 Response: TODO

### PUT /tournament
---
Allows the user to quit tournaments. This endpoint makes a request to the database to flag that the user has quit (boolean) the competition,  requiring the user's UID and the tournament ID in the request body. 

Sample Request Body: TODO

Sample Success 200 Response: TODO

