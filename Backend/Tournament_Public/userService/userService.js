const userModel = require('../models/tournament');


/**
 * Service to get all active tournaments.
 * 
 * @returns {Promise<Array>} A promise that resolves to an array of active tournaments.
 * @throws {Error} Throws an error if there is an issue fetching the tournaments.
 */
const getAllActiveTournaments = async () => {
    try {
        const tournaments = await userModel.getAllActiveTournaments();
        return tournaments;
    } catch (error) {
        throw new Error(`Error fetching tournaments: ${error.message}`);
    }
};


/**
 * Service to get a specific tournament by ID.
 * 
 * @param {string} tournamentId - The ID of the tournament to retrieve.
 * @returns {Promise<Object|null>} A promise that resolves to the tournament object if found, or null if not found.
 * @throws {Error} Throws an error if there is an issue fetching the tournament.
 */
const getTournamentById = async (tournamentId) => {
    try {
        const tournament = await userModel.getTournamentById(tournamentId);
        if (!tournament) {
            throw new Error('Tournament not found');
        }
        return tournament;
    } catch (error) {
        throw new Error(`Error fetching tournament: ${error.message}`);
    }
};


module.exports = {
    getAllActiveTournaments,
    getTournamentById
};
