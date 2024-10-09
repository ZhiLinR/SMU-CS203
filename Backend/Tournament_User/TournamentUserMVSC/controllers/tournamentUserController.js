const TournamentUserService = require('../services/tournamentUserService');  // Import service

// Get tournament history for a user
exports.getTournaments = async (req, res, next) => {
    try {
        const tournaments = await TournamentUserService.getTournaments();
        if (!tournaments || tournaments.length === 0) {
            const error = new Error('No tournaments found');
            error.statusCode = 404;
            return next(error);  // Pass error to middleware
        }
        res.status(200).json(tournaments);
    } catch (err) {
        console.error('Error retrieving tournaments:', err);
        next(err);  // Pass the error to the middleware
    }
};


// Get tournament matchups by tournament ID
exports.getTournamentMatchups = async (req, res, next) => {
    const { tournamentId } = req.params;

    try {
        const matchups = await TournamentUserService.getTournamentMatchups(tournamentId);
        if (!matchups || matchups.length === 0) {
            const error = new Error('No matchups found for the provided tournament ID');
            error.statusCode = 404;
            return next(error);
        }
        res.status(200).json(matchups);
    } catch (err) {
        console.error('Error fetching matchups:', err);
        next(err);  // Pass the error to the middleware
    }
};


// Sign up for a tournament
exports.signUpForTournament = async (req, res, next) => {
    const { UUID } = req.params;
    const { tournamentID, elo } = req.body;

    if (!UUID || !tournamentID || !elo) {
        const error = new Error('Missing required fields (UUID, tournamentID, or elo)');
        error.statusCode = 400;
        return next(error);  // Pass error to middleware
    }

    try {
        await TournamentUserService.signUpForTournament(UUID, tournamentID, elo);
        res.status(201).json({
            message: 'Successfully signed up for the tournament',
            details: { UUID, tournamentID, elo }
        });
    } catch (err) {
        if (err.sqlState === '45000') {
            return res.status(400).send(err.message);  // Custom error from SIGNAL
        }
        console.error('Error during tournament signup:', err);
        next(err);  // Pass the error to the middleware
    }
};

// Quit a tournament
exports.quitTournament = async (req, res, next) => {
    const { UUID } = req.params;
    const { tournamentID } = req.body;

    if (!UUID || !tournamentID) {
        const error = new Error('Missing required fields (UUID or tournamentID)');
        error.statusCode = 400;
        return next(error);
    }

    try {
        const result = await TournamentUserService.quitTournament(UUID, tournamentID);
        if (result.affectedRows === 0) {
            const error = new Error('No signup found for the provided UUID and tournamentID');
            error.statusCode = 404;
            return next(error);  // Pass error to middleware
        }

        res.status(200).json({
            message: 'Successfully quit from the tournament'
        });
    } catch (err) {
        console.error('Error quitting tournament:', err);
        next(err);  // Pass the error to the middleware
    }
};


// Get upcoming tournaments
exports.getUpcomingTournaments = async (req, res, next) => {
    try {
        const upcomingTournaments = await TournamentUserService.getUpcomingTournaments();
        if (!upcomingTournaments || upcomingTournaments.length === 0) {
            const error = new Error('No upcoming tournaments');
            error.statusCode = 404;
            return next(error);
        }
        res.status(200).json({
            message: 'Upcoming tournaments',
            tournaments: upcomingTournaments
        });
    } catch (err) {
        console.error('Error retrieving upcoming tournaments:', err);
        next(err);  // Pass the error to the middleware
    }
};


// Get in-progress tournaments
exports.getInProgressTournaments = async (req, res, next) => {
    try {
        const inProgressTournaments = await TournamentUserService.getInProgressTournaments();
        if (!inProgressTournaments || inProgressTournaments.length === 0) {
            const error = new Error('No in-progress tournaments');
            error.statusCode = 404;
            return next(error);
        }
        res.status(200).json({
            message: 'In-progress tournaments',
            tournaments: inProgressTournaments
        });
    } catch (err) {
        console.error('Error retrieving in-progress tournaments:', err);
        next(err);  // Pass the error to the middleware
    }
};
