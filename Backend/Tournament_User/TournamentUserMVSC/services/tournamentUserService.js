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

// Service to get upcoming tournaments
exports.getUpcomingTournaments = () => {
    return TournamentModel.getUpcomingTournaments();  // Call the model
};

// Service to get in-progress tournaments
exports.getInProgressTournaments = () => {
    return TournamentModel.getInProgressTournaments();  // Call the model
};

// Service to get UserTournamentGameResult for a specific tournament
exports.getUserTournamentGameRank = (tournamentId,UUID) => {
    return TournamentModel.getUserTournamentGameRank(tournamentId,UUID);  // Call the model
};

// Service to get checkTournamentExists for a specific tournament
exports.checkTournamentExists = (tournamentId) => {
    return TournamentModel.checkTournamentExists(tournamentId);  // Call the model
};

// Service to get playerlist of a tournament (Based on signups not matchups).
exports.GetPlayersInTournament = (tournamentId)=> {
    return TournamentModel.GetPlayersInTournament(tournamentId); // call the model
};

