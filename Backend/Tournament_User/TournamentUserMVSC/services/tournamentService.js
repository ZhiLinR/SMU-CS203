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
exports.getInProgressTournaments = () => {
    return TournamentModel.getInProgressTournaments();  // Call the model
};