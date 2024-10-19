const db = require('./config/db');

// Function to insert a new user (via stored procedure)
// const insertUser = (name, email, hashedPassword) => {
//     return new Promise((resolve, reject) => {
    //dont do like this. you have to abstract this database.
//         const query = `CALL UserMSVC.sign_up_user(?, ?, ?)`;
//         db.query(query, [name, email, hashedPassword], (error, results) => {
//             if (error) {
//                 return reject(new Error(`Database error: ${error.message}`));
//             }
//             resolve(results);
//         });
//     });
// };



// Function to fetch all tournaments, retrieve only the isActive ones
const getAllActiveTournaments = () => {
    return new Promise((resolve, reject) => {
        const query = `CALL getAllActiveTournaments()`;
        db.query(query, (error, results) => {
            if (error) {
                return reject(new Error(`Database error: ${error.message}`));
            }
            resolve(results[0]);
        });
    });
};

// Function to fetch a specific tournament by ID 
const getTournamentById = (id) => {
    return new Promise((resolve, reject) => {
        const query = `CALL GetTournamentById(?)`;
        db.query(query, [id], (error, results) => {
            if (error) {
                return reject(new Error(`Database error: ${error.message}`));
            }
            resolve(results[0][0]);
        });
    });
};

// Model to get matchups for a specific tournament when user is in public
// const getTournamentMatchups = (tournamentId, UUID) => {
//     return new Promise((resolve, reject) => {
//         const query = 'CALL GetTournamentUsingID(?,?)';  // Stored procedure to get tournament matchups by ID
//         db.query(query, [tournamentId, UUID], (err, results) => {
//             if (err) {
//                 return reject(new Error(`Database error: ${err.message}`));  // Return the error to the service layer
//             }
//             resolve(results[0]);  // Return the matchups to the service layer
//         });
//     });
// };

module.exports = {
    getAllActiveTournaments,
    getTournamentById,
};