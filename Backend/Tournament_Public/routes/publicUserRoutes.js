const express = require('express');
const router = express.Router();
const publicUserController = require('../controllers/publicUserController');

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
router.get('/view/upComing', publicUserController.getUpcomingTournaments);

/**
 * Route to get a list of in-progress tournaments.
 *
 * @returns {Promise<Array<Object>>} A promise that resolves to an array of in-progress tournaments.
 */
router.get('/view/inProgress', publicUserController.getInProgressTournaments);

/**
 * Route to get a list of completed tournaments.
 *
 * @returns {Promise<Array<Object>>} A promise that resolves to an array of completed tournaments.
 */
router.get('/view/completed', publicUserController.getCompletedTournaments);

/**
 * Route to get a tournament by its ID.
 *
 * @returns {Promise<Object>} A promise that resolves to the tournament object.
 */
router.get('/view/:tournamentId', publicUserController.getTournamentById);

module.exports = router;