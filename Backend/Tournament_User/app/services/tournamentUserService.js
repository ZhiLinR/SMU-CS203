const TournamentModel = require('../models/tournamentModel');  // Import the model

// Service to get tournament history for a user
exports.getTournaments = (UUID) => {
    return TournamentModel.getTournaments(UUID);  // Just call the model
};

// Service to get matchups for a specific tournament
exports.getTournamentMatchups = (tournamentId,UUID) => {
    return TournamentModel.getTournamentMatchups(tournamentId,UUID);  // Call the model
};

// Service to sign up for tournament must include TournamentID and User Elo
exports.signUpForTournament = async (UUID, tournamentID, elo) => {
    return TournamentModel.signUpForTournament(UUID, tournamentID, elo);
};

// Service to quit a tournament
exports.quitTournament = (UUID, tournamentID) => {
    return TournamentModel.quitTournament(UUID, tournamentID);  // Call the model
};


// Service to get UserTournamentGameResult for a specific tournament
exports.getUserTournamentGameRank = (tournamentId,UUID) => {
    return TournamentModel.getUserTournamentGameRank(tournamentId,UUID);  // Call the model
};






// Service to get all tournaments based on status of each player
exports.getPlayerTournamentsByStatus = (playerUUID)=> {
    return TournamentModel.getPlayerTournamentsByStatus(playerUUID); // call the model
};

//service to get all completed tournaments
exports.getCompletedTournaments = ()=> {
    return TournamentModel.getCompletedTournaments(); // call the model
};