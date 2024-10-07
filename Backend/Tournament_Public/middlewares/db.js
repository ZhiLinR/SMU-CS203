require('dotenv').config({ path: '../../.env' });  // Load .env file into process.env
const mysql = require('mysql2');

const connection = mysql.createConnection({
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_NAME,
    port: process.env.DB_PORT
});

connection.connect((err) => {
    if (err) {
        console.error('Error connecting to the database:', err);
        return;
    }
    console.log('Connected to the MySQL server.');

    // Perform a test query
    connection.query('SELECT * FROM TournamentMSVC.public_tournaments', (error, results) => {
        if (error) {
            console.error('Error executing the query:', error);
            return;
        }
        console.log('Query Results:', results);
    });
});

module.exports = connection;
