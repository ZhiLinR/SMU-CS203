const express = require('express');
const router = express.Router();
const TournamentUserController = require('../controllers/tournamentUserController');

// Define routes and map them to controller methods
router.get('/tournaments/:UUID', TournamentUserController.getTournaments);
router.get('/tournaments/GetTournamentMatchups/:tournamentId', TournamentUserController.getTournamentMatchups); // might not use because this one views every matchup in tournament.
router.post('/tournaments/signup/:UUID', TournamentUserController.signUpForTournament);
router.delete('/tournaments/quit/:UUID', TournamentUserController.quitTournament);
router.get('/tournaments/upComing', TournamentUserController.getUpcomingTournaments);
router.get('/tournaments/inProgress', TournamentUserController.getInProgressTournaments);
router.get('/tournaments/getTournamentCurrentRanking/:tournamentId', TournamentUserController.getUserTournamentGameRank);
router.get('tournaments/')

module.exports = router;
