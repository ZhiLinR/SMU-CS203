const db = require('./config/db');






/**
 * Fetches all active tournaments from the database.
 * 
 * @returns {Promise<Array>} A promise that resolves to an array of active tournaments.
 * @throws {Error} Throws an error if there is a database error.
 */

// Function to fetch all tournaments, retrieve only the isActive ones
const getAllActiveTournaments = () => {
    return new Promise((resolve, reject) => {
        const query = 'CALL getAllActiveTournaments()';
        db.query(query, (error, results) => {
            if (error) {
                return reject(new Error(`Database error: ${error.message}`));
            }
            resolve(results[0]);
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
const getTournamentById = (id) => {
    return new Promise((resolve, reject) => {
        const query = 'CALL GetTournamentById(?)';
        db.query(query, [id], (error, results) => {
            if (error) {
                return reject(new Error(`Database error: ${error.message}`));
            }
            resolve(results[0][0]);
        });
    });
};


module.exports = {
    getAllActiveTournaments,
    getTournamentById,
};