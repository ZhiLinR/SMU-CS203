const TournamentUserService = require('../userService/userService');
const { sendSuccessResponse } = require('../middlewares/successHandler');
const { errorHandler } = require('../middlewares/errorHandler');
const { validationResult } = require('express-validator');
const { validateTournamentId } = require('../middlewares/validationHandler'); // Import the validation




/** Controller to get all tournaments
 * @async
 * @param {Object} req - The request object.
 * @param {Object} res - The response object.
 * @param {Function} next - The next middleware function.
 * @returns {Json} Return Upcoming tournament based on upcoming dates.
 * @throws {error} If input invalid or missing fields.
 */

exports.getAllTournaments = async (req, res, next) => {
    try {
        const allTournaments = await TournamentUserService.getAllTournaments();
        if (!allTournaments || allTournaments.length === 0) {
            const error = new Error('No tournaments found');
            error.statusCode = 404;
            return next(error);
        }
        sendSuccessResponse(res, 200, 'successfully retrieved all tournaments', allTournaments);

    } catch (err) {
        console.error('Error retrieving tournaments:', err);
        next(err);  // Pass the error to the middleware
    }
};

/** Controller to get upcoming tournaments
 * @async
 * @param {Object} req - The request object.
 * @param {Object} res - The response object.
 * @param {Function} next - The next middleware function.
 * @returns {Json} Return Upcoming tournament based on upcoming dates.
 * @throws {error} If input invalid or missing fields.
 */

exports.getUpcomingTournaments = async (req, res, next) => {
    try {
        const upcomingTournaments = await TournamentUserService.getUpcomingTournaments();
        if (!upcomingTournaments || upcomingTournaments.length === 0) {
            const error = new Error('No upcoming tournaments');
            error.statusCode = 404;
            return next(error);
        }
        sendSuccessResponse(res, 200, 'successfully retrieved upcoming tournamentst', upcomingTournaments);

    } catch (err) {
        console.error('Error retrieving upcoming tournaments:', err);
        next(err);  // Pass the error to the middleware
    }
};

/**  Controller to get ongoing tournaments
 * @async
 * @param {Object} req - The request object.
 * @param {Object} res - The response object.
 * @param {Function} next - The next middleware function.
 * @returns {Json} Return incoming tournament based on upcoming dates.
 * @throws {error} If input invalid or missing fields.
 */

exports.getOngoingTournaments = async (req, res, next) => {
    try {
        const ongoingTournaments = await TournamentUserService.getOngoingTournaments();
        if (!ongoingTournaments || ongoingTournaments.length === 0) {
            const error = new Error('No ongoing tournaments');
            error.statusCode = 404;
            return next(error);
        }
        sendSuccessResponse(res, 200, 'successfully retrieved ongoing tournaments', ongoingTournaments);

    } catch (err) {
        console.error('Error retrieving ongoing tournaments:', err);
        next(err);  // Pass the error to the middleware
    }
};


/**
* Controller to get all completed tournaments.
* @param {Object} res - Express response object
* @param {Function} next - Express next middleware function
*/

exports.getCompletedTournaments = async (req, res, next) => {
    try {
        const completedTournaments = await TournamentUserService.getCompletedTournaments();
        if (!completedTournaments || completedTournaments.length === 0) {
            const error = new Error('No completed tournaments');
            error.statusCode = 404;
            return next(error);
        }
        sendSuccessResponse(res, 200, 'successfully retrieved completed tournaments', completedTournaments);

    } catch (err) {
        console.error('Error retrieving completed tournaments:', err);
        next(err);  // Pass the error to the middleware
    }
};

/**
 * Controller to get a specific tournament by ID.
 *
 * @param {Object} req - The request object.
 * @param {Object} req.params - The parameters from the request URL.
 * @param {string} req.params.tournamentId - The ID of the tournament to retrieve.
 * @param {Object} res - The response object.
 * @returns {Promise<void>} A promise that resolves to sending a JSON response with the tournament details.
 */

exports.getTournamentById = [
   // First, use the validation middleware
   validateTournamentId,

   // Then, handle the logic for the controller
   async (req, res, next) => {
       // Check if validation failed
       const errors = validationResult(req);
       if (!errors.isEmpty()) {
           // If validation fails, return a 400 response with the errors
           const error = new Error('Invalid Tournament ID');
           error.statusCode = 400;
           return next(error);
       }

       const { tournamentId } = req.params;

       try {
           // Query your database or service to retrieve the tournament
           const specificTournament = await TournamentUserService.getTournamentByID(tournamentId);

           // If no tournament is found, return a 404 response
           if (!specificTournament) {
               const error = new Error('No tournament with that id is found');
               error.statusCode = 404;
               return next(error);  // Pass error to the middleware
           }

           // Send the success response
           sendSuccessResponse(res, 200, 'successfully retrieved tournament based on tournament ID', specificTournament);

       } catch (err) {
           console.error('Error retrieving tournament:', err);
           next(err);  // Pass the error to the middleware
       }
   },
];
