const TournamentModel = require('../models/tournament');

/**
 * Service to get all tournaments.
 *
 * @returns {Promise<Array<Object>>} A promise that resolves to an array of tournaments.
 */
exports.getAllTournaments = () => {
    return TournamentModel.getAllTournaments(); // call the model
};

/**
 * Service to get all completed tournaments.
 *
 * @returns {Promise<Array<Object>>} A promise that resolves to an array of completed tournaments.
 */
exports.getCompletedTournaments = () => {
    return TournamentModel.getCompletedTournaments(); // call the model
};

/**
 * Service to get upcoming tournaments.
 *
 * @returns {Promise<Array<Object>>} A promise that resolves to an array of upcoming tournaments.
 */
exports.getUpcomingTournaments = () => {
    return TournamentModel.getUpcomingTournaments();  // Call the model
};

/**
 * Service to get in-progress tournaments.
 *
 * @returns {Promise<Array<Object>>} A promise that resolves to an array of in-progress tournaments.
 */
exports.getInProgressTournaments = () => {
    return TournamentModel.getInProgressTournaments();  // Call the model
};


/**
 * Route to get a tournament by its ID.
 *
 * @returns {Promise<Object>} A promise that resolves to the tournament object.
 */
exports.getTournamentByID = (tournamentId) => {
    return TournamentModel.getTournamentByID(tournamentId);  // Call the model
};


