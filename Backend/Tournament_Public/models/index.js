const { Sequelize } = require('sequelize');
require('dotenv').config();

// Configure Sequelize to connect to AWS RDS MySQL instance using environment variables
const sequelize = new Sequelize(process.env.DB_NAME, process.env.DB_USER, process.env.DB_PASSWORD,  {
    host: process.env.DB_HOST,
    port: process.env.DB_PORT,
    dialect: 'mysql',
    logging: false
});

// Test connection
sequelize.authenticate()
    .then(() => console.log('Connected to AWS RDS MySQL'))
    .catch(err => console.error('Unable to connect:', err));

module.exports = sequelize;
