const express = require('express');
const app = express();
const mysql = require('mysql2');

// Set up the MySQL connection
const db = mysql.createConnection({
    host: '202.166.134.202',
    user: 'root',   // Replace with your DB username
    password: 'UnZ7Sqx26eFpwfBsLXC8KE',   // Replace with your DB password
    port: 32781,
    database: 'TournamentMSVC' // Replace with your DB name
});

// Connect to MySQL
db.connect((err) => {
    if (err) {
        console.error('Error connecting to the database:', err);
        return;
    }
    console.log('Connected to the MySQL database');
});

// Middleware to parse incoming JSON
app.use(express.json());

// Sample route to get tournament history
app.get('/tournaments', (req, res) => {
    const sql = `CALL GetTournamentHistory();`;
    
    db.query(sql, (err, results) => {
        if (err) {
            res.status(500).send('Error retrieving data from database');
        } else {
            res.status(200).json(results);
        }
    });
});

//Route to get tournamentId 

app.get('/tournaments/GetTournamentMatchups/:tournamentId',(req,res)=>{
    const tournamentId = req.params.tournamentId;
    const getTournamentIdsql = 'CALL GetTournamentUsingID(?)';

    db.query(getTournamentIdsql, [tournamentId], (err, results) => {
        if (err) {
            console.error('Error fetching matchups from the database:', err);
            res.status(500).send('Failed to retrieve matchups');
        } else if (results.length === 0) {
            res.status(404).send('No matchups found for the provided tournament ID');
        } else {
            res.status(200).json(results);
        }
    });

});

// Middleware to parse incoming JSON
app.use(express.json());





// Route to sign up a user for a tournament using a stored procedure
app.post('/tournaments/signup/:UUID', (req, res) => {
    const UUID = req.params.UUID;   // UUID parsed from the URL
    const { tournamentID, elo } = req.body;  // tournamentID and elo from the request body

    // Check if required parameters are provided
    if (!UUID || !tournamentID || !elo) {
        return res.status(400).json({ error: 'Missing required fields (UUID, tournamentID, or elo)' });
    }

    // Call the stored procedure for signing up
    const sql = 'CALL SignUpForTournament(?, ?, ?)';
    
    db.query(sql, [UUID, tournamentID, elo], (err, results) => {
        if (err) {
            // Handle MySQL errors, including the errors raised by SIGNAL in the stored procedure
            if (err.sqlState === '45000') {
                return res.status(400).send(err.message);  // Custom error from SIGNAL
            }
            console.error('Error during tournament signup:', err);
            return res.status(500).send('Signup failed. Database error occurred');
        }

        // Respond with a success message
        res.status(201).json({
            message: 'Successfully signed up for the tournament',
            details: { UUID, tournamentID, elo }
        });
    });
});




// Route to let user quit from a tournament
app.delete('/tournaments/quit/:UUID', (req, res) => {
    const UUID = req.params.UUID;   // UUID parsed from the URL
    const { tournamentID } = req.body;  // tournamentID from the request body

    // Check if required parameters are provided
    if (!UUID || !tournamentID) {
        return res.status(400).json({ error: 'Missing required fields (UUID or tournamentID)' });
    }

    // SQL query to delete the user from the Signups table
    const deleteQuery = 'CALL UserQuit(?,?)';

    // Execute the query to remove the user
    db.query(deleteQuery, [UUID, tournamentID], (err, result) => {
        if (err) {
            console.error('Error deleting signup from the database:', err);
            return res.status(500).send('Failed to remove the user from the tournament');
        }

        if (result.affectedRows === 0) {
            return res.status(404).send('No signup found for the provided UUID and tournamentID');
        }

        // Respond with a confirmation message
        res.status(200).json({
            message: 'Successfully quit from the tournament'
        });
    });
});


app.get('/tournaments/upComing',(req ,res)=>{
    const upcomingQuery = 'CALL GetUpComingTournament()'

    db.query(upcomingQuery,(err,results)=>{
        if(err){
            console.error('Error retrieving upcoming tournament.');
            return res.status(500).send('Database error occurred');
        }

        if(results.length===0){
            return res.status(404).send('No upcoming tournament.')
        }

        // Respond with the list of upcoming tournaments
        res.status(200).json({
            message: 'Upcoming tournaments',
            tournaments: results
        });

    });
});

app.get('/tournaments/inProgress',(req,res)=>{
    const inProgressQuery = 'CALL GetInProgressTournament()'

    db.query(inProgressQuery,(err,results)=>{
        if(err){
            console.error('Error retrieving inProgress tournament');
            return res.status(500).send('Database Error occured.');
        }

        if(results.length===0){
            return res.status(404).send('No inprogress tournament')
        }

        res.status(200).json({
            message:'incoming tournament',
            tournament: results
        });
    });
});





// Start the server
const PORT = process.env.PORT || 3303;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
