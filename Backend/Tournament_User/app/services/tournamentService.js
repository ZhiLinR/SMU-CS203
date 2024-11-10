const TournamentModel = require('../models/tournamentModel');  // Import the model

// Service to get playerlist of a tournament (Based on signups not matchups).
exports.getPlayersInTournament = (tournamentId)=> {
    return TournamentModel.getPlayersInTournament(tournamentId); // call the model
};

// Service to get checkTournamentExists for a specific tournament
exports.checkTournamentExists = (tournamentId) => {
    return TournamentModel.checkTournamentExists(tournamentId);  // Call the model
};

// Service to get upcoming tournaments
exports.getUpcomingTournaments = () => {
    return TournamentModel.getUpcomingTournaments();  // Call the model
};

// Service to get in-progress tournaments
exports.getOnGoingTournaments = () => {
    return TournamentModel.getOnGoingTournaments();  // Call the model
};

exports.getTournamentRanking = (tournamentId) => {
    return TournamentModel.getTournamentRanking(tournamentId); // Pass tournamentId to the model
};


exports.getAllTournamentMatchups = (tournamentId) => {
    return TournamentModel.getAllTournamentMatchups(tournamentId); // Pass tournamentId to the model
};
