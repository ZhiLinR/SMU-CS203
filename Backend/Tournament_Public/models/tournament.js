const db = require('./config/db');

/**
 * Retrieves a list of All Tournaments
 * @returns {Promise<Array>} A promise that resolves to an array of tournaments.
 * @throws {Error} If the database query fails.
 */
exports.getAllTournaments = () => {
    return new Promise((resolve, reject) => {
        const query = 'CALL GetAllTournaments()';  // Stored procedure to get in-progress tournaments
        db.query(query, (err, results) => {
            if (err) {
                return reject(err);  // Return the error to the service layer
            }
            resolve(results);  // Return the in-progress tournaments to the service layer
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
exports.getOngoingTournaments = () => {
    return new Promise((resolve, reject) => {
        const query = 'CALL GetOngoingTournaments()';  // Stored procedure to get in-progress tournaments
        db.query(query, (err, results) => {
            if (err) {
                return reject(err);  // Return the error to the service layer
            }
            resolve(results);  // Return the in-progress tournaments to the service layer
        });
    });
};

/**
 * Fetches a tournament by its ID from the database.
 * 
 * @param {string} tournamentId - The ID of the tournament to fetch.
 * @returns {Promise<Object|null>} A promise that resolves to the tournament object if found, or null if not found.
 * @throws {Error} Throws an error if there is a database error.
 */

// Function to fetch a specific tournament by ID 
exports.getTournamentByID = (tournamentId) => {
    return new Promise((resolve, reject) => {
        const query = 'CALL GetTournamentById(?)';
        db.query(query, [tournamentId], (error, results) => {
            if (error) {
                return reject(new Error(`Database error: ${error.message}`));
            }
            resolve(results[0][0]);
        });
    });
};

