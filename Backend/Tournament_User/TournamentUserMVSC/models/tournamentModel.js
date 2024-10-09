const db = require('../config/db');  // Import the database connection

// Model to get tournament history for a user
exports.getTournaments = () => {
    return new Promise((resolve, reject) => {
        const query = 'CALL GetTournamentHistory()';  // Stored procedure to get tournament history
        db.query(query, (err, results) => {
            if (err) {
                return reject(err);  // Return the error to the service layer
            }
            resolve(results);  // Return the results to the service layer
        });
    });
};

// Model to get matchups for a specific tournament
exports.getTournamentMatchups = (tournamentId) => {
    return new Promise((resolve, reject) => {
        const query = 'CALL GetTournamentUsingID(?)';  // Stored procedure to get tournament matchups by ID
        db.query(query, [tournamentId], (err, results) => {
            if (err) {
                return reject(err);  // Return the error to the service layer
            }
            resolve(results);  // Return the matchups to the service layer
        });
    });
};

// Model to check if a user is already signed up for a tournament
exports.checkUserSignup = (UUID, tournamentID) => {
    return new Promise((resolve, reject) => {
        const query = 'CALL CheckUserSignup(?, ?)';  // Custom stored procedure to check signup
        db.query(query, [UUID, tournamentID], (err, results) => {
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
