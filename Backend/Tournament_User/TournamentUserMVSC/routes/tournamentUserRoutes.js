const express = require('express');
const router = express.Router();
const TournamentUserController = require('../controllers/tournamentUserController');

// Define routes and map them to controller methods
router.get('/tournaments', TournamentUserController.getTournaments);
router.get('/tournaments/GetTournamentMatchups/:tournamentId', TournamentUserController.getTournamentMatchups);
router.post('/tournaments/signup/:UUID', TournamentUserController.signUpForTournament);
router.delete('/tournaments/quit/:UUID', TournamentUserController.quitTournament);
router.get('/tournaments/upComing', TournamentUserController.getUpcomingTournaments);
router.get('/tournaments/inProgress', TournamentUserController.getInProgressTournaments);

module.exports = router;
