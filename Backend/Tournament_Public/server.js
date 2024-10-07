require('dotenv').config({ path: ['.env.local', '.env'] });


const express = require('express');
const app = express();
const publicTournamentRoutes = require('./routes/publicTournament');
const db = require('./models/config/db');  // Adjusted to use the db connection

app.use(express.json());  // Middleware to parse JSON request body

// Use the public tournament routes
app.use('/tournaments', publicTournamentRoutes);

// Error handling middleware
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).send('Server error!');
});

const PORT = process.env.PORT || 3000;

// Ensure the db connection is ready before starting the server
db.query((err) => {
  if (err) {
    console.error('Error connecting to the database:', err);
    process.exit(1);  // Exit the process if unable to connect to the database
  }

  console.log('Connected to the database successfully.');

  // Start server
  app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
  });
});
