const express = require('express');
const { signUpUser, getAllActiveTournaments, getTournamentById } = require('../controllers/publicUserController');

const router = express.Router();

// Route for new user sign-up
//router.post('/signup', signUpUser);

// Route to get all tournaments
router.get('/tournaments', getAllActiveTournaments);

// Route to get a specific tournament by ID
router.get('/tournaments/:id', getTournamentById);

module.exports = router;
