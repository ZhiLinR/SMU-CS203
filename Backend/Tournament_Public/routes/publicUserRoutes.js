const express = require('express');
const router = express.Router();
const publicUserController = require('../controllers/publicUserController');
const healthController = require('../controllers/healthController');

/**
 * Route to get all tournaments
 *
 * @returns {Promise<Array<Object>>} A promise that resolves to an array of all tournaments.
 */
router.get('/health', healthController.getHealth);

/**
 * Route to get all tournaments
 *
 * @returns {Promise<Array<Object>>} A promise that resolves to an array of all tournaments.
 */
router.get('/view/tournaments', publicUserController.getAllTournaments);

/**
 * Route to get a list of upcoming tournaments.
 *
 * @returns {Promise<Array<Object>>} A promise that resolves to an array of upcoming tournaments.
 */
router.get('/view/upcoming-events', publicUserController.getUpcomingTournaments);

/**
 * Route to get a list of in-progress tournaments.
 *
 * @returns {Promise<Array<Object>>} A promise that resolves to an array of in-progress tournaments.
 */
router.get('/view/events', publicUserController.getOngoingTournaments);

/**
 * Route to get a list of completed tournaments.
 *
 * @returns {Promise<Array<Object>>} A promise that resolves to an array of completed tournaments.
 */
router.get('/view/past-events', publicUserController.getCompletedTournaments);

/**
 * Route to get a tournament by its ID.
 *
 * @returns {Promise<Object>} A promise that resolves to the tournament object.
 */
router.get('/view/:tournamentId', publicUserController.getTournamentById);

module.exports = router;
