const userService = require('../userService/userService');



/**
 * Controller to get all active tournaments.
 * 
 * @param {Object} req - The request object.
 * @param {Object} res - The response object.
 * @returns {Promise<void>} A promise that resolves to sending a JSON response with the list of tournaments.
 */

// Controller to get all tournaments (example)
const getAllActiveTournaments = async (req, res) => {
    try {
        const tournaments = await userService.getAllActiveTournaments();
        res.status(200).json({
            "success": true,
            "status" : 200,
            "message": "Active Tournaments successfully fetched",
            "content": 
        }
        );
    } catch (error) {
        res.status(500).json({ message: 'Error fetching tournaments', error: error.message });
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
const getTournamentById = async (req, res) => {
    const tournamentId = req.params.id;
    try {
        const tournament = await userService.getTournamentById(tournamentId);
        res.status(200).json(tournament);
    } catch (error) {
        res.status(500).json({ message: 'Error fetching tournament', error: error.message });
    }
};

module.exports = {
    getAllActiveTournaments,
    getTournamentById,
};
