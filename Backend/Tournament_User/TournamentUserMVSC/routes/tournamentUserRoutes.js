const express = require('express');
const router = express.Router();
const TournamentUserController = require('../controllers/tournamentUserController');

// Define routes and map them to controller methods
router.get('/tournaments/:UUID', TournamentUserController.getTournaments);
router.get('/tournaments/GetTournamentMatchups/:tournamentId', TournamentUserController.getTournamentMatchups); 
router.post('/tournaments/signup/:UUID', TournamentUserController.signUpForTournament);
router.delete('/tournaments/quit/:UUID', TournamentUserController.quitTournament);
router.get('/tournaments/view/upComing', TournamentUserController.getUpcomingTournaments);
router.get('/tournaments/view/inProgress', TournamentUserController.getInProgressTournaments);
router.get('/tournaments/getTournamentCurrentRanking/:tournamentId', TournamentUserController.getUserTournamentGameRank);
router.get('tournaments/');
router.get('/tournaments/GetPlayers/:tournamentId',TournamentUserController.GetPlayersInTournament)

module.exports = router;
