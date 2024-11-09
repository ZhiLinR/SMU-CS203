const TournamentUserService = require('../services/tournamentUserService');  // Import service
const TournamentService = require('../services/tournamentService');  // Import service


/**
 * Get participated tournament history for a user
 * @param {Object} req.params - The request parameters TournamentID
 * @param {string} req.params.UUID - The ID of the User.
 * @returns {Json} Returns a list of tournament names.
 * @throws {error} If UUID invalid.
 */


exports.getTournaments = async (req, res, next) => {
    const { UUID } = req.params;

    try {
        let result =  await TournamentUserService.getTournaments(UUID);

        // If no tournaments were found, return a 404 response
        if (!result) {
            throw new Error('No tournaments found');
        }


        // Extract names from the tournaments and put to a map.
        const tournamentNames = result.map(tournament => tournament.name);



        // Set the success response in res.locals.data and call next()
        res.locals.data = {
            statusCode: 200,
            message: 'Tournament Found',
            success: true,
            content: tournamentNames,
        };

        next();

    } catch (err) {

        if(err.message === 'No tournaments found'){
            err.statusCode = 404;
        }

        console.error('Error retrieving tournaments:', err);
        next(err);  // Pass the error to the middleware
    }
};

/**
 * Get tournament matchups for a user
 * @param {Object} req.params - The request parameters
 * @param {string} req.params.UUID - The ID of the User.
 * @param {string} req.params.tournamentId - The ID of the tournament.
 * @returns {Json}} Returns all matchups of the specified tournament that involve the tournament user.Shows player1, player2,playerwon,tournamentID,round number.
 * @throws {error} If UUID invalid.
 */

// Get tournament matchups by tournament ID
exports.getTournamentMatchups = async (req, res, next) => {
    const { tournamentId } = req.params;
    const {UUID} = req.body;

    try {
        let result = await TournamentUserService.getTournamentMatchups(tournamentId,UUID);
        //If no matchups found return error message.
        if (!result || result.length === 0) {
            throw new Error('No matchups found for the provided tournament ID/UUID');
        }
        //Matchup Found

        res.locals.data = {
            statusCode: 200,
            message: 'Tournament Matchups:',
            success: true,
            content: result
        };

        next();

    } catch (err) {
        if(err.message === 'No matchups found for the provided tournament ID/UUID'){
            err.statusCode = 404;
        }
        console.error('Error fetching matchups:', err);
        next(err);  // Pass the error to the middleware
    }
};

/**
 * Sign up for a tournament
 * @param {Object} req.params - The request parameters
 * @param {string} req.params.UUID - The ID of the User.
 * @param {string} req.params.tournamentId - The ID of the tournament.
 * @param {int} req.params.elo - The elo of the user.
 * @returns {Json} Return User's input as well as success message.
 * @throws {error} If input invalid or missing fields.
 */

exports.signUpForTournament =  async (req, res, next) => {
    const { UUID } = req.params;
    const { tournamentID, elo } = req.body;

    try {
        // Check for missing fields
        const missingFields = [];
        if (!UUID) missingFields.push('UUID');
        if (!tournamentID) missingFields.push('tournamentID');
        if (!elo) missingFields.push('elo');

        // Throw an error if any fields are missing
        if (missingFields.length > 0) {
            throw new Error(`Missing required fields: ${missingFields.join(', ')}`);
        }

        // Proceed with the signup process if all fields are present
        await TournamentUserService.signUpForTournament(UUID, tournamentID, elo);

        // Success response
        res.locals.data = {
            statusCode: 200,
            message: 'Successfully signed up for the tournament',
            success: true,
            content: { UUID, tournamentID, elo }
        };

        next();  // Pass control to the success handler middleware

    } catch (err) {
        // Check if the error is due to missing fields and set a 400 status code
        if (err.message.startsWith('Missing required fields')) {
            err.statusCode = 400;  // Bad Request
        }
        
        console.error('Error during tournament signup:', err);
        next(err);  // Pass the error to the error-handling middleware
    }
};


/**
 * Quit tournament
 * @param {Object} req.params - The request parameters
 * @param {string} req.params.UUID - The ID of the User.
 * @param {string} req.params.tournamentId - The ID of the tournament.
 * @returns {Json} Return success message for quitting.
 * @throws {error} If input invalid or missing fields
 */

exports.quitTournament = async (req, res, next) => {
    const { UUID } = req.params;
    const { tournamentID } = req.body;

   

    try {
        if (!UUID || !tournamentID) {
            throw new Error('Missing required fields (UUID or tournamentID)');
        }

        let result = await TournamentUserService.quitTournament(UUID, tournamentID);
        if (result.affectedRows === 0) {
            throw new Error('No signup found for the provided UUID and tournamentID');
        }

        res.locals.data ={
            statusCode: 200,
            message: 'Successfully quit from the tournament',
            success: true
        };

        next();//pass to the next middleware

    } catch (err) {

        if(err.message === 'Missing required fields (UUID or tournamentID)'){
            err.statusCode = 400;
        }

        if(err.message === 'No signup found for the provided UUID and tournamentID'){
            err.statusCode = 404;
        }

        console.error('Error quitting tournament:', err);
        next(err);  // Pass the error to the middleware
    }
};
/**
 * Get upcoming tournaments
 * @param {Object} req.params - The request parameters
 * @returns {Json} Return Upcoming tournament based on upcoming dates.
 * @throws {error} If input invalid or missing fields.
 */


exports.getUpcomingTournaments = async (req, res, next) => {
    try {
        const result = await TournamentService.getUpcomingTournaments();
        if (!result || result.length === 0) {
            throw new Error('No upcoming tournaments');
        }



        res.locals.data = {
            statusCode: 200,
            message: 'Upcoming tournaments',
            success: true,
            content : result
        };

        next();


    } catch (err) {

        if(err.message === 'No upcoming tournaments'){
            err.statusCode = 404;
        }

        console.error('Error retrieving upcoming tournaments:', err);
        next(err);  // Pass the error to the middleware
    }
};

/**
 * Get in-progress tournaments
 * @param {Object} req.params - The request parameters
 * @returns {Json} Return incoming tournament based on upcoming dates.
 * @throws {error} If input invalid or missing fields.
 */

exports.getInProgressTournaments = async (req, res, next) => {
    try {
        let result = await TournamentService.getInProgressTournaments();
        if (!result || result.length === 0) {
            throw new Error('No in-progress tournaments');
        }

        res.locals.data = {
            statusCode: 200,
            message: 'In-progress tournaments',
            success: true,
            content: result
        };

        next();

    } catch (err) {

        if(err.message === 'No in-progress tournaments'){
            err.statusCode = 404;
        }
        console.error('Error retrieving in-progress tournaments:', err);
        next(err);  // Pass the error to the middleware
    }
};


/**
 * Get UserTournamentGameResult 
 * @param {Object} req.params - The request parameters
 * @param {string} req.params.UUID - The ID of the User.
 * @param {string} req.params.tournamentId - The ID of the tournament.
 * @returns {Json} Return incoming tournament based on upcoming dates.
 * @throws {error} If input invalid or missing fields.
 */

exports.getUserTournamentGameRank = async (req, res, next) => {
    const { UUID } = req.body;
    const { tournamentId } = req.params;

    try {
        // Step 1: Check if tournament exists
        const tournamentExists = await TournamentService.checkTournamentExists(tournamentId);
        
        if (!tournamentExists) {
            throw new Error('Tournament not found.');
        }

        // Step 2: Get user's tournament game rank if tournament exists
        let result = await  TournamentUserService.getUserTournamentGameRank(tournamentId, UUID);
        
        if (!result || result.length === 0) {
           throw new Error('No Game result found for the provided tournamentID.');
        }

        // Pass to success handler
        res.locals.data = {
            statusCode: 200,
            message: 'Current Tournament Ranking:',
            success: true,
            content: result
        };

        next();

    } catch (err) {
        if(err.message === 'Tournament not found.' || 'No Game result found for the provided tournamentID.'){
            err.statusCode = 404;
        }

        console.error('Error retrieving current ranking in the tournament:', err);
        next(err);  // Pass the error to the middleware
    }
};


/**
 * Get the list of UUID players in the specified tournament.
 * @param {Object} req.params - The request parameters
 * @param {string} req.params.tournamentId - The ID of the tournament.
 * @returns {Json} Returns a list of UUIDs participating in the tournament.
 * @throws {error} If tournamentID invalid.
 */


// Get list of players in tournament
exports.getPlayersInTournament = async (req, res, next) => {
    const { tournamentId } = req.params;


    try {
        //if tournament ID is missing then throw error and go to catch
        if (!tournamentId) {
            throw new Error('Missing required fields tournamentID');
        }
        //Check if tournament exist else return relavant error message.
        const tournamentExists = await TournamentService.checkTournamentExists(tournamentId);

        if (!tournamentExists) {
            throw new Error('Invalid tournamentID: Tournament not found.');
        }

        const result =  await TournamentService.getPlayersInTournament(tournamentId);

        if (result.length === 0) {
        res.locals.data = {
            statusCode: 200,
            message: 'No players have signed up for this tournament yet.',
            success: true,
            content: []  // Return an empty array
        };
        // Pass to success handler
        return next();
        
        }

        // Map over the result and extract UUID values into an array
        const uuidArray = result.map(player => player.UUID);


        res.locals.data = {
            statusCode: 200,
            message: 'Players in the tournament:',
            success: true,
            content : uuidArray
        };
        // Pass to success handler
        next();

        
    } catch (err) {

        if(err.message ==='Missing required fields tournamentID'){
            err.statusCode = 400;
        }

        if(err.message === 'Invalid tournamentID: Tournament not found.'){
            err.statusCode = 404;
        }

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
exports.getPlayerTournaments = async  (req, res, next) => {
    try {
        const { playerUUID } = req.params;
        
        // Validate playerUUID
        if (!playerUUID) {
            throw new Error('Player UUID is required');
        }
 
        const result = await TournamentUserService.GetPlayerTournamentsByStatus(playerUUID);
        
        if (!result || result.length === 0) {
            throw new Error('No tournaments found for this player')
        }
 
        res.locals.data = {
            statusCode : 200,
            message: 'Player tournaments retrieved successfully',
            success: true,
            content: result
        };
        // Pass to success handler
        next();
        
    } catch (err) {

        if(err.message ==='No tournaments found for this player'){
            err.statusCode = 404;
        }

        if(err.message === 'Player UUID is required'){
            err.statusCode = 400;
        }

        console.error('Error retrieving player tournaments:', err);
        next(err);  // Pass the error to the middleware
    }
 };


 /**
* Controller to get all completed tournaments.
* @param {Object} res - Express response object
* @param {Function} next - Express next middleware function
*/
exports.getCompletedTournaments = async  (req, res, next) => {
    try {
        const result = await TournamentUserService.getCompletedTournaments();

        // If no results are found, throw an error to be handled by the catch block
        if (!result || result.length === 0) {
            throw new Error('No completed tournaments');  // This goes directly to the catch block
        }

        // If result exists, send a success response
        res.locals.data={
            message: 'Completed tournaments',
            success: true,
            content: result
        };

        next();
    } catch (err) {
        // Customize error handling based on the error message or type
        if (err.message === 'No completed tournaments') {
            err.statusCode = 404;  // Set a custom status code for this specific error
        }
        
        console.error('Error retrieving completed tournaments:', err);
        next(err);  // Pass the error to the error-handling middleware
    }
};


