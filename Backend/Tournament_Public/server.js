require('dotenv').config();
const express = require('express');
const cors = require('cors');
const publicUserRoutes = require('./routes/publicUserRoutes');
const errorHandler = require('./middlewares/errorHandler');
const corsMiddleware = require('./middlewares/corsMiddleware');


const app = express();

// Apply custom CORS middleware
app.use(corsMiddleware);


app.use(express.json()); // Middleware for parsing JSON bodies


app.use('/', publicUserRoutes); // Public routes for users
app.use(errorHandler); // Middleware for error handling


const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});