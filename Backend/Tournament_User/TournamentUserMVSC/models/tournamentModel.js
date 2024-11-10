const db = require('../config/db');  // Import the database connection

exports.getTournaments = (UUID) => {
    return new Promise((resolve, reject) => {
        const query = 'CALL GetTournamentHistory(?)';  // Stored procedure to get tournament history
        db.query(query, [UUID], (err, results) => {
            if (err) {
                return reject(err);  // Return the error to the service layer
            }

            // The first result set is the actual data, the second set is metadata
            const tournaments = results[0];

            // If no tournaments were found, return null
            if (!tournaments || tournaments.length === 0) {
                return resolve(null);
            }

            resolve(tournaments);  // Return the tournaments to the service layer
        });
    });
};


// Model to get matchups for a specific tournament
exports.getTournamentMatchups = (tournamentId,UUID) => {
    return new Promise((resolve, reject) => {
        const query = 'CALL GetTournamentUsingID(?,?)';  // Stored procedure to get tournament matchups by ID
        db.query(query, [tournamentId,UUID], (err, results) => {
            if (err) {
                return reject(err);  // Return the error to the service layer
            }
            resolve(results[0]);  // Return the matchups to the service layer
        });
    });
};

// Model to check if a user is already signed up for a tournament
exports.checkUserSignup = (UUID, tournamentID,elo) => {
    return new Promise((resolve, reject) => {
        const query = 'CALL SignUpForTournament(?, ?, ?)';  // Custom stored procedure to check signup
        db.query(query, [UUID, tournamentID,elo], (err, results) => {
            if (err) {
                return reject(err);  // Return the error to the service layer
            }
            resolve(results);  // Return the results to the service layer
        });
    });
};

// Model to sign up a user for a tournament
exports.signUpForTournament = (UUID, tournamentID, elo) => {
    return new Promise((resolve, reject) => {
        const query = 'CALL SignUpForTournament(?, ?, ?)';  // Stored procedure to sign up a user
        db.query(query, [UUID, tournamentID, elo], (err, results) => {
            if (err) {
                return reject(err);  // Return the error to the service layer
            }
            resolve(results);  // Return the success result to the service layer
        });
    });
};

// Model to quit a tournament
exports.quitTournament = (UUID, tournamentID) => {
    return new Promise((resolve, reject) => {
        const query = 'CALL UserQuit(?, ?)';  // Stored procedure to remove a user from a tournament
        db.query(query, [UUID, tournamentID], (err, results) => {
            if (err) {
                return reject(err);  // Return the error to the service layer
            }
            resolve(results);  // Return the success result to the service layer
        });
    });
};

// Model to get upcoming tournaments
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

// Model to get in-progress tournaments
exports.getInProgressTournaments = () => {
    return new Promise((resolve, reject) => {
        const query = 'CALL GetInProgressTournament()';  // Stored procedure to get in-progress tournaments
        db.query(query, (err, results) => {
            if (err) {
                return reject(err);  // Return the error to the service layer
            }
            resolve(results);  // Return the in-progress tournaments to the service layer
        });
    });
};

//Model to get game result from tournament
exports.getUserTournamentGameRank = (tournamentID, UUID) => {
    return new Promise((resolve, reject) => {
        const query = 'CALL GetUserTournamentGameRank(?,?)';  // Stored procedure to get in-progress tournaments
        db.query(query,[tournamentID,UUID], (err, results) => {
            if (err) {
                return reject(err);  // Return the error to the service layer
            }
            resolve(results[0]);  // Return the in-progress tournaments to the service layer
        });
    });
};


// Model to check if the tournament exists
exports.checkTournamentExists = (tournamentId) => {
    return new Promise((resolve, reject) => {
        const query = 'SELECT COUNT(*) AS count FROM Tournament WHERE tournamentID = ?';
        
        db.query(query, [tournamentId], (err, result) => {
            if (err) {
                return reject(err);  // Handle any database error
            }

            // If the tournament exists, resolve with `true`, otherwise `false`
            const exists = result[0].count > 0;
            resolve(exists);
        });
    });
};
