const express = require('express');
const router = express.Router();
const TournamentUserController = require('../controllers/tournamentUserController');

// Define routes and map them to controller methods
router.get('/tournaments/matchups/:tournamentId', TournamentUserController.getTournamentMatchups); //fixed
router.post('/tournaments/signup/:UUID', TournamentUserController.signUpForTournament);
router.delete('/tournaments/quit/:UUID', TournamentUserController.quitTournament);
router.get('/tournaments/view/upcoming', TournamentUserController.getUpcomingTournaments);
router.get('/tournaments/view/ongoing', TournamentUserController.getOnGoingTournaments);
router.get('/tournaments/ranking/:tournamentId', TournamentUserController.getUserTournamentGameRank);//fixed
router.get('/tournaments/players/:tournamentId',TournamentUserController.getPlayersInTournament); // fixed

router.get('/tournaments/view/completed', TournamentUserController.getCompletedTournaments);
router.get('/tournaments/view/ranking/:tournamentId', TournamentUserController.getTournamentRanking); 
router.get('/tournamentusermsvc/health', TournamentUserController.getHealthStatus);
router.get('/tournaments/getAllMatchups/:tournamentId', TournamentUserController.getAllTournamentMatchups); //invoke user to get list of uuid
router.get('/player/:playerUUID/tournaments', TournamentUserController.getPlayerTournamentsByStatus);


module.exports = router;
