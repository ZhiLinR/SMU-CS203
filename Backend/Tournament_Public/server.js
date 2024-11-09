require('dotenv').config();
const express = require('express');
const cors = require('cors');
const publicUserRoutes = require('./routes/publicUserRoutes');
const errorHandler = require('./middlewares/errorHandler');


const app = express();

// Enable CORS for the specific origin defined in the .env file
const corsOptions = {
    origin: process.env.CORS_ORIGIN || '*', // Default to '*' if not set
    methods: ['GET'],
    allowedHeaders: ['Content-Type', 'Authorization'],
};

app.use(cors(corsOptions)); // Use the CORS middleware with the defined options

app.use(express.json()); // Middleware for parsing JSON bodies


app.use('/api/public', publicUserRoutes); // Public routes for users
app.use(errorHandler); // Middleware for error handling


const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});