const express = require('express');
const { getAllActiveTournaments, getTournamentById } = require('../controllers/publicUserController');

const router = express.Router();


/**
 * Route to get all active tournaments.
 * 
 * @name GET /tournaments
 * @function
 * @memberof module:routes/publicUserRoutes
 * @inner
 * @param {express.Request} req - The request object.
 * @param {express.Response} res - The response object.
 */
router.get('/tournaments', getAllActiveTournaments);

/**
 * Route to get a specific tournament by ID.
 * 
 * @name GET /tournaments/:id
 * @function
 * @memberof module:routes/publicUserRoutes
 * @inner
 * @param {express.Request} req - The request object.
 * @param {express.Response} res - The response object.
 */
router.get('/tournaments/:id', getTournamentById);

module.exports = router;
