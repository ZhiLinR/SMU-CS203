const userService = require('../userService/userService');

// Controller for user sign-up
// const signUpUser = async (req, res) => {
//     const { name, email, password } = req.body;

//     try {
//         // Call the service to sign up the user
//         const result = await userService.signUpUser(name, email, password);
//         res.status(201).json(result); // success message from the service
//     } catch (error) {
//         res.status(500).json({ message: 'Error signing up user', error: error.message });
//     }
// };

// Controller to get all tournaments (example)
const getAllActiveTournaments = async (req, res) => {
    try {
        const tournaments = await userService.getAllActiveTournaments();
        res.status(200).json(tournaments);
    } catch (error) {
        res.status(500).json({ message: 'Error fetching tournaments', error: error.message });
    }
};

// Controller to get a specific tournament (example)
const getTournamentById = async (req, res) => {
    const tournamentId = req.params.id;
    try {
        const tournament = await userService.getTournamentById(tournamentId);
        res.status(200).json(tournament);
    } catch (error) {
        res.status(500).json({ message: 'Error fetching tournament', error: error.message });
    }
};

module.exports = {
    getAllActiveTournaments,
    getTournamentById,
};
