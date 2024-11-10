require('dotenv').config();
const express = require('express');
const app = express();
const tournamentUserRoutes = require('./routes/tournamentUserRoutes');
const errorHandler = require('./middlewares/errorHandler');  // Import error-handling middleware
const successHandler = require('./middlewares/successHandler');// Import success-handling middleware


// Middleware to parse incoming JSON
app.use(express.json());

// Tournament user routes
app.use('/api', tournamentUserRoutes);


// use the success-handling middleware
app.use(successHandler);

// Use the error-handling middleware after routes
app.use(errorHandler);



const PORT = process.env.DB_PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
