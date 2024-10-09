const TournamentModel = require('../models/tournamentModel');  // Import the model

// Service to get tournament history for a user
exports.getTournaments = () => {
    return TournamentModel.getTournaments();  // Just call the model
};

// Service to get matchups for a specific tournament
exports.getTournamentMatchups = (tournamentId) => {
    return TournamentModel.getTournamentMatchups(tournamentId);  // Call the model
};

// Service to sign up a user for a tournament (with additional logic)
exports.signUpForTournament = async (UUID, tournamentID, elo) => {
    // Check if the user is already signed up for the tournament (business logic)
    const existingSignUp = await TournamentModel.checkUserSignup(UUID, tournamentID);
    if (existingSignUp.length > 0) {
        // If the user is already signed up, throw an error (business rule)
        const error = new Error('User is already signed up for this tournament');
        error.statusCode = 400;
        throw error;
    }

    // If user is not signed up, proceed with sign up
    return TournamentModel.signUpForTournament(UUID, tournamentID, elo);
};

// Service to quit a tournament
exports.quitTournament = (UUID, tournamentID) => {
    return TournamentModel.quitTournament(UUID, tournamentID);  // Call the model
};

// Service to get upcoming tournaments
exports.getUpcomingTournaments = () => {
    return TournamentModel.getUpcomingTournaments();  // Call the model
};

// Service to get in-progress tournaments
exports.getInProgressTournaments = () => {
    return TournamentModel.getInProgressTournaments();  // Call the model
};
