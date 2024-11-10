const { param } = require('express-validator');

/**
 * Validation middleware for tournamentId.
 * This checks if the tournamentId is a valid integer (for SQL).
 * If you're using UUIDs, you can replace .isInt() with .isUUID().
 */

const validateTournamentId = [
    param('tournamentId')
        .isUUID().withMessage('Tournament ID must be a valid UUID') // Use this if your ID is an integer
        .notEmpty().withMessage('Tournament ID is required')

];

module.exports = {
    validateTournamentId
};