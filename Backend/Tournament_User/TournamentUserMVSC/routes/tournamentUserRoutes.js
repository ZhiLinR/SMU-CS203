const express = require('express');
const router = express.Router();
const TournamentUserController = require('../controllers/tournamentUserController');

// Define routes and map them to controller methods
router.get('/tournaments/matchups/:tournamentId', TournamentUserController.getTournamentMatchups); 
router.post('/tournaments/signup/:UUID', TournamentUserController.signUpForTournament);
router.delete('/tournaments/quit/:UUID', TournamentUserController.quitTournament);
router.get('/tournaments/view/upcoming', TournamentUserController.getUpcomingTournaments);
router.get('/tournaments/view/inprogress', TournamentUserController.getInProgressTournaments);
router.get('/tournaments/ranking/:tournamentId', TournamentUserController.getUserTournamentGameRank);
router.get('/tournaments/players/:tournamentId',TournamentUserController.getPlayersInTournament);
router.get('/player/:playerUUID/tournaments', TournamentUserController.getPlayerTournaments);
router.get('/tournaments/view/completed', TournamentUserController.getCompletedTournaments);

module.exports = router;
