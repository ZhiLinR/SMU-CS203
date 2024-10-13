require('dotenv').config();
const express = require('express');
const publicUserRoutes = require('./routes/publicUserRoutes');

const app = express();

app.use(express.json()); // Middleware for parsing JSON bodies

app.use('/api/public', publicUserRoutes); // Public routes for users

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
