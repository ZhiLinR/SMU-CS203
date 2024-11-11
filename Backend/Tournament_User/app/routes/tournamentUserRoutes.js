const express = require('express');
const router = express.Router();
const TournamentUserController = require('../controllers/tournamentUserController');

// Define routes and map them to controller methods
router.get('/tournaments/matchups/:tournamentId', TournamentUserController.getTournamentMatchups); //change uuid to name oso
router.post('/tournaments/signup/:UUID', TournamentUserController.signUpForTournament);
router.delete('/tournaments/quit/:UUID', TournamentUserController.quitTournament);
router.get('/tournaments/view/upcoming', TournamentUserController.getUpcomingTournaments);
router.get('/tournaments/view/ongoing', TournamentUserController.getOnGoingTournaments);
router.get('/tournaments/ranking/:tournamentId', TournamentUserController.getUserTournamentGameRank);
router.get('/tournaments/players/:tournamentId',TournamentUserController.getPlayersInTournament); // change to nameUsersdfer

router.get('/tournaments/view/completed', TournamentUserController.getCompletedTournaments);
router.get('/tournaments/view/ranking/:tournamentId', TournamentUserController.getTournamentRanking); //change to name
router.get('/tournaments/player/health', TournamentUserController.getHealthStatus);
router.get('/tournaments/getAllMatchups/:tournamentId', TournamentUserController.getAllTournamentMatchups);
router.get('/player/:playerUUID/tournaments', TournamentUserController.getPlayerTournamentsByStatus);


module.exports = router;
