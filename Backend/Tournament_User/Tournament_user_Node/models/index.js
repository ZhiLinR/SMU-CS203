const { Sequelize } = require('sequelize');

// Configure Sequelize to connect to AWS RDS MySQL instance(to change)
const sequelize = new Sequelize('TournamentMSVC', 'User', 'UnZ7Sqx26eFpwfBsLXC8KE', {
    host: '202.166.134.202',
    dialect: 'mysql',
    logging: false
});

// Test connection
sequelize.authenticate()
    .then(() => console.log('Connected to AWS RDS MySQL'))
    .catch(err => console.error('Unable to connect:', err));

module.exports = sequelize;
