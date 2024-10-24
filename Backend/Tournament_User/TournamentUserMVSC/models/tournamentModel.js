const db = require('../config/db');  // Import the database connection

/**
 * Retrieves the tournament history for a given user by their UUID.
 * @param {string} UUID - The UUID of the user.
 * @returns {Promise<Array>} A promise that resolves to an array of tournaments the user has participated in.
 * @throws {Error} If the database query fails.
 */
exports.getTournaments = (UUID) => {
    return new Promise((resolve, reject) => {
        const query = 'CALL GetTournamentHistory(?)';  // Stored procedure to get tournament history
        db.query(query, [UUID], (err, results) => {
            if (err) {
                return reject(err);  // Return the error to the service layer
            }

            // The first result set is the actual data, the second set is metadata
            const tournaments = results[0];

            // If no tournaments were found, return null
            if (!tournaments || tournaments.length === 0) {
                return resolve(null);
            }

            resolve(tournaments);  // Return the tournaments to the service layer
        });
    });
};

/**
 * Retrieves the matchups for a specific tournament for a given user.
 * @param {string} tournamentId - The ID of the tournament.
 * @param {string} UUID - The UUID of the user.
 * @returns {Promise<Array>} A promise that resolves to an array of matchups in the tournament.
 * @throws {Error} If the database query fails.
 */

exports.getTournamentMatchups = (tournamentId,UUID) => {
    return new Promise((resolve, reject) => {
        const query = 'CALL GetTournamentUsingID(?,?)';  // Stored procedure to get tournament matchups by ID
        db.query(query, [tournamentId,UUID], (err, results) => {
            if (err) {
                return reject(err);  // Return the error to the service layer
            }
            resolve(results[0]);  // Return the matchups to the service layer
        });
    });
};


/**
 * Checks if a user is already signed up for a specific tournament.
 * @param {string} UUID - The UUID of the user.
 * @param {string} tournamentID - The ID of the tournament.
 * @param {number} elo - The user's Elo rating.
 * @returns {Promise<Object>} A promise that resolves with the result of the signup check.
 * @throws {Error} If the database query fails.
 */

exports.checkUserSignup = (UUID, tournamentID,elo) => {
    return new Promise((resolve, reject) => {
        const query = 'CALL SignUpForTournament(?, ?, ?)';  // Custom stored procedure to check signup
        db.query(query, [UUID, tournamentID,elo], (err, results) => {
            if (err) {
                return reject(err);  // Return the error to the service layer
            }
            resolve(results);  // Return the results to the service layer
        });
    });
};

/**
 * Signs up a user for a specific tournament.
 * @param {string} UUID - The UUID of the user.
 * @param {string} tournamentID - The ID of the tournament.
 * @param {number} elo - The user's Elo rating.
 * @returns {Promise<Object>} A promise that resolves when the user is successfully signed up.
 * @throws {Error} If the database query fails.
 */

exports.signUpForTournament = (UUID, tournamentID, elo) => {
    return new Promise((resolve, reject) => {
        const query = 'CALL SignUpForTournament(?, ?, ?)';  // Stored procedure to sign up a user
        db.query(query, [UUID, tournamentID, elo], (err, results) => {
            if (err) {
                return reject(err);  // Return the error to the service layer
            }
            resolve(results);  // Return the success result to the service layer
        });
    });
};

/**
 * Allows a user to quit a tournament they have signed up for.
 * @param {string} UUID - The UUID of the user.
 * @param {string} tournamentID - The ID of the tournament.
 * @returns {Promise<Object>} A promise that resolves when the user successfully quits the tournament.
 * @throws {Error} If the database query fails.
 */
exports.quitTournament = (UUID, tournamentID) => {
    return new Promise((resolve, reject) => {
        const query = 'CALL UserQuit(?, ?)';  // Stored procedure to remove a user from a tournament
        db.query(query, [UUID, tournamentID], (err, results) => {
            if (err) {
                return reject(err);  // Return the error to the service layer
            }
            resolve(results);  // Return the success result to the service layer
        });
    });
};

/**
 * Retrieves a list of upcoming tournaments.
 * @returns {Promise<Array>} A promise that resolves to an array of upcoming tournaments.
 * @throws {Error} If the database query fails.
 */
exports.getUpcomingTournaments = () => {
    return new Promise((resolve, reject) => {
        const query = 'CALL GetUpComingTournament()';  // Stored procedure to get upcoming tournaments
        db.query(query, (err, results) => {
            if (err) {
                return reject(err);  // Return the error to the service layer
            }
            resolve(results);  // Return the upcoming tournaments to the service layer
        });
    });
};

/**
 * Retrieves a list of tournaments that are currently in progress.
 * @returns {Promise<Array>} A promise that resolves to an array of in-progress tournaments.
 * @throws {Error} If the database query fails.
 */
exports.getInProgressTournaments = () => {
    return new Promise((resolve, reject) => {
        const query = 'CALL GetInProgressTournament()';  // Stored procedure to get in-progress tournaments
        db.query(query, (err, results) => {
            if (err) {
                return reject(err);  // Return the error to the service layer
            }
            resolve(results);  // Return the in-progress tournaments to the service layer
        });
    });
};

/**
 * Retrieves the ranking and win count of a user in a specific tournament.
 * @param {string} tournamentID - The ID of the tournament.
 * @param {string} UUID - The UUID of the user.
 * @returns {Promise<Object>} A promise that resolves to the user's tournament game rank and win count.
 * @throws {Error} If the database query fails.
 */
exports.getUserTournamentGameRank = (tournamentID, UUID) => {
    return new Promise((resolve, reject) => {
        const query = 'CALL GetUserTournamentGameRank(?,?)';  // Stored procedure to get in-progress tournaments
        db.query(query,[tournamentID,UUID], (err, results) => {
            if (err) {
                return reject(err);  // Return the error to the service layer
            }
            resolve(results[0]);  // Return the in-progress tournaments to the service layer
        });
    });
};


/**
 * Checks if a tournament exists by its ID.
 * @param {string} tournamentId - The ID of the tournament.
 * @returns {Promise<boolean>} A promise that resolves to `true` if the tournament exists, otherwise `false`.
 * @throws {Error} If the database query fails.
 */
exports.checkTournamentExists = (tournamentId) => {
    return new Promise((resolve, reject) => {
        const query = 'SELECT COUNT(*) AS count FROM Tournament WHERE tournamentID = ?';
        
        db.query(query, [tournamentId], (err, result) => {
            if (err) {
                return reject(err);  // Handle any database error
            }

            // If the tournament exists, resolve with `true`, otherwise `false`
            const exists = result[0].count > 0;
            resolve(exists);
        });
    });
};

/**
 * Model to get players who signed up for a tournament ( Based on signups).
 * @param {string} tournamentId - The ID of the tournament.
 * @returns {Promise<Array<{UUID: string}>>} A promise that resolves to an array of player UUIDs.
 * @throws {Error} If the database query fails.
 */

exports.GetPlayersInTournament = (tournamentId) => {
    return new Promise((resolve, reject) => {
        const query = 'Call GetPlayersInTournament(?)';
        
        db.query(query, [tournamentId], (err, result) => {
            if (err) {
                return reject(err);  // Handle any database error
            }

            console.log("Raw result from the database:", result);
            resolve(result[0]);
        });
    });
};


/**
 * Model to get all tournaments for a player with their status.
 * @param {string} playerUUID - The UUID of the player.
 * @returns {Promise<Array<{tournamentID: string, tournamentName: string, startDate: Date, endDate: Date, status: string}>>} A promise that resolves to an array of tournaments.
 * @throws {Error} If the database query fails.
 */

exports.GetPlayerTournamentsByStatus = (playerUUID) => {
    return new Promise((resolve, reject) => {
        const query = 'CALL GetPlayerTournamentsByStatus(?)';
        
        db.query(query, [playerUUID], (err, result) => {
            if (err) {
                return reject(err);  // Handle any database error
            }

            console.log("Raw result from the database:", result);
            resolve(result[0]);
        });
    });
};


/**
 * Fetches the list of completed tournaments from the database.
 * 
 * @returns {Promise<Array>} A promise that resolves to an array of completed tournaments.
 * @throws {Error} If there is an error executing the query.
 */

exports.getCompletedTournaments = () => {
    return new Promise((resolve, reject) => {
        const query = 'CALL GetCompletedTournament()';  // Stored procedure to get completed tournaments
        db.query(query, (err, results) => {
            if (err) {
                return reject(err);  // Return the error to the service layer
            }
            resolve(results);  // Return the in-progress tournaments to the service layer
        });
    });
};