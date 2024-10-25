const TournamentUserService = require('../userService/userService');



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
            const error = new Error('No tournaments retrieved');
            error.statusCode = 404;
            return next(error);
        }
        res.status(200).json({
            "success": true,
            "status" : 200,
            "message": "successfully retrieved all tournaments",
            "content": allTournaments
        });
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
        res.status(200).json({
            "success": true,
            "status" : 200,
            "message": "successfully retrieved upcoming tournaments",
            "content": upcomingTournaments
        });
    } catch (err) {
        console.error('Error retrieving upcoming tournaments:', err);
        next(err);  // Pass the error to the middleware
    }
};

/**  Controller to get in-progress tournaments
 * @async
 * @param {Object} req - The request object.
 * @param {Object} res - The response object.
 * @param {Function} next - The next middleware function.
 * @returns {Json} Return incoming tournament based on upcoming dates.
 * @throws {error} If input invalid or missing fields.
 */

exports.getInProgressTournaments = async (req, res, next) => {
    try {
        const inProgressTournaments = await TournamentUserService.getInProgressTournaments();
        if (!inProgressTournaments || inProgressTournaments.length === 0) {
            const error = new Error('No in-progress tournaments');
            error.statusCode = 404;
            return next(error);
        }
        res.status(200).json({
            "success": true,
            "status" : 200,
            "message": "successfully retrieved in-progress tournaments",
            "content": inProgressTournaments
        });
    } catch (err) {
        console.error('Error retrieving in-progress tournaments:', err);
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
        res.status(200).json({
            "success": true,
            "status" : 200,
            "message": "successfully retrieved completed tournaments",
            "content": completedTournaments
        });
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
 * @param {string} req.params.id - The ID of the tournament to retrieve.
 * @param {Object} res - The response object.
 * @returns {Promise<void>} A promise that resolves to sending a JSON response with the tournament details.
 */

// Controller to get a specific tournament (example)
exports.getTournamentById = async (req, res, next) => {
    const {tournamentId} = req.params;
    try {
        const specificTournament = await TournamentUserService.getTournamentByID(tournamentId);
         // If no tournament is found, return a 404 response
         if (!specificTournament) {
            const error = new Error('No tournament with that id is found');
            error.statusCode = 404;
            return next(error);  // Pass error to middleware
        }
        res.status(200).json({
            "success": true,
            "status" : 200,
            "message": "successfully retrieved tournament based on tournament ID",
            "content": specificTournament
        });
    } catch (error) {
        res.status(500).json({ 
            message: 'Error fetching tournament', 
            error: error.message });
    }
};

