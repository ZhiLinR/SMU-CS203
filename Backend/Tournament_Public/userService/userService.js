const bcrypt = require('bcrypt');
const userModel = require('../models/tournament');

// Service to handle user sign-up with hashed password
const signUpUser = async (name, email, password) => {
    try {
        // Hash the password before storing it in the database
        const hashedPassword = await bcrypt.hash(password, 10); // 10 salt rounds for security

        // Call the model function to insert the user
        await userModel.insertUser(name, email, hashedPassword);

        return { success: true, message: 'User signed up successfully' };
    } catch (error) {
        throw new Error(`Sign-up error: ${error.message}`);
    }
};

// Service to get all tournaments (example)
const getAllTournaments = async () => {
    try {
        const tournaments = await userModel.getAllTournaments();
        return tournaments;
    } catch (error) {
        throw new Error(`Error fetching tournaments: ${error.message}`);
    }
};


// Service to get a specific tournament
const getTournamentById = async (tournamentId) => {
    try {
        const tournament = await userModel.getTournamentById(tournamentId);
        if (!tournament) {
            throw new Error('Tournament not found');
        }
        return tournament;
    } catch (error) {
        throw new Error(`Error fetching tournament: ${error.message}`);
    }
};


module.exports = {
    signUpUser,
    getAllTournaments,
    getTournamentById
};
