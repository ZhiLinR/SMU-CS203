require('dotenv').config();
const express = require('express');
const cors = require('cors');
const app = express();
const tournamentUserRoutes = require('./routes/tournamentUserRoutes');
const errorHandler = require('./middlewares/errorHandler');  // Import error-handling middleware
const successHandler = require('./middlewares/successHandler');// Import success-handling middleware
const corsMiddleware = require('./middlewares/corsMiddleware');

const corsOptions = {
    origin: process.env.ALLOWED_ORIGINS?.split(',').map(origin => origin.trim()),
    optionsSuccessStatus: 200 // some legacy browsers (IE11, various SmartTVs) choke on 204
  }
app.use(cors(corsOptions))
// Middleware to parse incoming JSON
app.use(express.json());

// Tournament user routes
app.use('/api', tournamentUserRoutes);

// Apply custom CORS middleware
app.use(corsMiddleware);



// use the success-handling middleware
app.use(successHandler);

// Use the error-handling middleware after routes
app.use(errorHandler);



const PORT = process.env.DB_PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
    console.log('Allowed Origins:', process.env.ALLOWED_ORIGINS?.split(',').map(origin => origin.trim()));
});
