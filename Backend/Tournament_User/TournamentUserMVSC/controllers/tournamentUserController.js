const TournamentUserService = require('../services/tournamentUserService');  // Import service


/**
 * Get participated tournament history for a user
 * @async
 * @param {Object} req - The request object.
 * @param {Object} req.params - The request parameters
 * @param {string} req.params.UUID - The ID of the User.
 * @param {Object} res - The response object.
 * @param {Function} next - The next middleware function.
 * @returns {Json} Returns a list of tournament names.
 * @throws {error} If UUID invalid.
 */


exports.getTournaments = async (req, res, next) => {
    const { UUID } = req.params;

    try {
        const tournaments = await TournamentUserService.getTournaments(UUID);

        // If no tournaments were found, return a 404 response
        if (!tournaments) {
            const error = new Error('No tournaments found');
            error.statusCode = 404;
            return next(error);  // Pass error to middleware
        }

        // Extract names from the tournaments and put to a map.
        const tournamentNames = tournaments.map(tournament => tournament.name);

        res.status(200).json({
            message: 'Tournament Found',
            success: true,
            content: tournamentNames 

        });
    } catch (err) {
        console.error('Error retrieving tournaments:', err);
        next(err);  // Pass the error to the middleware
    }
};

/**
 * Get tournament matchups for a user
 * @async
 * @param {Object} req - The request object.
 * @param {Object} req.params - The request parameters
 * @param {string} req.params.UUID - The ID of the User.
 * @param {string} req.params.tournamentId - The ID of the tournament.
 * @param {Object} res - The response object.
 * @param {Function} next - The next middleware function.
 * @returns {Json}} Returns all matchups of the specified tournament that involve the tournament user.Shows player1, player2,playerwon,tournamentID,round number.
 * @throws {error} If UUID invalid.
 */

// Get tournament matchups by tournament ID
exports.getTournamentMatchups = async (req, res, next) => {
    const { tournamentId } = req.params;
    const {UUID} = req.body;

    try {
        const matchups = await TournamentUserService.getTournamentMatchups(tournamentId,UUID);
        //If no matchups found return error message.
        if (!matchups || matchups.length === 0) {
            const error = new Error('No matchups found for the provided tournament ID/UUID');
            error.statusCode = 404;
            return next(error);
        }
        //Matchup Found
        res.status(200).json({
            message: 'Tournament Matchups:',
            success: true,
            content: matchups
        });
    } catch (err) {
        console.error('Error fetching matchups:', err);
        next(err);  // Pass the error to the middleware
    }
};

/**
 * Sign up for a tournament
 * @async
 * @param {Object} req - The request object.
 * @param {Object} req.params - The request parameters
 * @param {string} req.params.UUID - The ID of the User.
 * @param {string} req.params.tournamentId - The ID of the tournament.
 * @param {int} req.params.elo - The elo of the user.
 * @param {Object} res - The response object.
 * @param {Function} next - The next middleware function.
 * @returns {Json} Return User's input as well as success message.
 * @throws {error} If input invalid or missing fields.
 */

exports.signUpForTournament = async (req, res, next) => {
    const { UUID } = req.params;
    const { tournamentID, elo } = req.body;

    if (!UUID || !tournamentID || !elo) {
        const error = new Error('Missing required fields (UUID, tournamentID, or elo)');
        error.statusCode = 400;
        return next(error);  // Pass error to middleware
    }

    try {
        await TournamentUserService.signUpForTournament(UUID, tournamentID, elo);
        res.status(201).json({
            message: 'Successfully signed up for the tournament',
            success: true,
            content: { UUID, tournamentID, elo }
        });
    } catch (err) {
        if (err.sqlState === '45000') {
            return res.status(400).send(err.message);  // Custom error from SIGNAL
        }
        console.error('Error during tournament signup:', err);
        next(err);  // Pass the error to the middleware
    }
};

/**
 * Quit tournament
 * @async
 * @param {Object} req - The request object.
 * @param {Object} req.params - The request parameters
 * @param {string} req.params.UUID - The ID of the User.
 * @param {string} req.params.tournamentId - The ID of the tournament.
 * @param {Object} res - The response object.
 * @param {Function} next - The next middleware function.
 * @returns {Json} Return success message for quitting.
 * @throws {error} If input invalid or missing fields
 */

exports.quitTournament = async (req, res, next) => {
    const { UUID } = req.params;
    const { tournamentID } = req.body;

    if (!UUID || !tournamentID) {
        const error = new Error('Missing required fields (UUID or tournamentID)');
        error.statusCode = 400;
        return next(error);
    }

    try {
        const result = await TournamentUserService.quitTournament(UUID, tournamentID);
        if (result.affectedRows === 0) {
            const error = new Error('No signup found for the provided UUID and tournamentID');
            error.statusCode = 404;
            return next(error);  // Pass error to middleware
        }

        res.status(200).json({
            message: 'Successfully quit from the tournament',
            success: true
        });
    } catch (err) {
        console.error('Error quitting tournament:', err);
        next(err);  // Pass the error to the middleware
    }
};
/**
 * Get upcoming tournaments
 * @async
 * @param {Object} req - The request object.
 * @param {Object} req.params - The request parameters
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
            message: 'Upcoming tournaments',
            success: true,
            content : upcomingTournaments
        });
    } catch (err) {
        console.error('Error retrieving upcoming tournaments:', err);
        next(err);  // Pass the error to the middleware
    }
};

/**
 * Get in-progress tournaments
 * @async
 * @param {Object} req - The request object.
 * @param {Object} req.params - The request parameters
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
            message: 'In-progress tournaments',
            success: true,
            content: inProgressTournaments
        });
    } catch (err) {
        console.error('Error retrieving in-progress tournaments:', err);
        next(err);  // Pass the error to the middleware
    }
};


/**
 * Get UserTournamentGameResult 
 * @async
 * @param {Object} req - The request object.
 * @param {Object} req.params - The request parameters
 * @param {string} req.params.UUID - The ID of the User.
 * @param {string} req.params.tournamentId - The ID of the tournament.
 * @param {Object} res - The response object.
 * @param {Function} next - The next middleware function.
 * @returns {Json} Return incoming tournament based on upcoming dates.
 * @throws {error} If input invalid or missing fields.
 */

exports.getUserTournamentGameRank = async (req, res, next) => {
    const { UUID } = req.body;
    const { tournamentId } = req.params;

    try {
        // Step 1: Check if tournament exists
        const tournamentExists = await TournamentUserService.checkTournamentExists(tournamentId);
        
        if (!tournamentExists) {
            const error = new Error('Tournament not found.');
            error.statusCode = 404;  // Not Found
            return next(error);  // Pass the error to middleware
        }

        // Step 2: Get user's tournament game rank if tournament exists
        const getUserTournamentGameRank = await TournamentUserService.getUserTournamentGameRank(tournamentId, UUID);
        
        if (!getUserTournamentGameRank || getUserTournamentGameRank.length === 0) {
            const error = new Error('No Game result found for the provided tournamentID.');
            error.statusCode = 404;  // Not Found
            return next(error);
        }

        // Respond with the ranking information
        res.status(200).json({
            
            message: 'Current Tournament Ranking:',
            success: true,
            content: getUserTournamentGameRank
        });
    } catch (err) {
        console.error('Error retrieving current ranking in the tournament:', err);
        next(err);  // Pass the error to the middleware
    }
};


/**
 * Get the list of UUID players in the specified tournament.
 * @async
 * @param {Object} req - The request object.
 * @param {Object} req.params - The request parameters
 * @param {string} req.params.tournamentId - The ID of the tournament.
 * @param {Object} res - The response object.
 * @param {Function} next - The next middleware function.
 * @returns {Json} Returns a list of UUIDs participating in the tournament.
 * @throws {error} If tournamentID invalid.
 */


// Get upcoming tournaments
exports.GetPlayersInTournament = async (req, res, next) => {
    const { tournamentId } = req.params;

    if (!tournamentId) {
        const error = new Error('Missing required fields tournamentID');
        error.statusCode = 400;
        return next(error);
    }
    try {
        //Check if tournament exist else return relavant error message.
        const tournamentExists = await TournamentUserService.checkTournamentExists(tournamentId);
        if (!tournamentExists) {
            const error = new Error('Invalid tournamentID: Tournament not found.');
            error.statusCode = 404;
            return next(error);
        }


        const GetPlayersInTournament = await TournamentUserService.GetPlayersInTournament(tournamentId);
        if (GetPlayersInTournament.length === 0) {
            return res.status(200).json({
                message: 'No players have signed up for this tournament yet.',
                success: true,
                content: []  // Return an empty array
            });
        }

        // Map over the result and extract UUID values into an array
        const uuidArray = GetPlayersInTournament.map(player => player.UUID);

        res.status(200).json({
            message: 'Players in the tournament:',
            success: true,
            content : uuidArray
        });
    } catch (err) {
        console.error('Error retrieving players name:', err);
        next(err);  // Pass the error to the middleware
    }
};

/**
* Controller to get all tournaments (upcoming, completed, in progress) for a specific player.
* @param {Object} req - Express request object containing playerUUID in params
* @param {Object} res - Express response object
* @param {Function} next - Express next middleware function
*/
exports.getPlayerTournaments = async (req, res, next) => {
    try {
        const { playerUUID } = req.params;
        
        // Validate playerUUID
        if (!playerUUID) {
            const error = new Error('Player UUID is required');
            error.statusCode = 400;
            return next(error);
        }
 
        const tournaments = await TournamentUserService.GetPlayerTournamentsByStatus(playerUUID);
        
        if (!tournaments || tournaments.length === 0) {
            const error = new Error('No tournaments found for this player');
            error.statusCode = 404;
            return next(error);
        }
 
        res.status(200).json({
            "success": true,
            "status": 200,
            "message": 'Player tournaments retrieved successfully',
            "content": tournaments
        });
        
    } catch (err) {
        console.error('Error retrieving player tournaments:', err);
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
            message: 'Completed tournaments',
            success: true,
            content: completedTournaments
        });
    } catch (err) {
        console.error('Error retrieving completed tournaments:', err);
        next(err);  // Pass the error to the middleware
    }
};