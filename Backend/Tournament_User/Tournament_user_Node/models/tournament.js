const { DataTypes } = require('sequelize');
const sequelize = require('./index');
const User = require('./user');

const Tournament = sequelize.define('Tournament', {
    tournamentId: {
        type: DataTypes.UUID,
        primaryKey: true,
        allowNull: false
    },
    startDate: {
        type: DataTypes.DATE,
        allowNull: false
    },
    endDate: {
        type: DataTypes.DATE,
        allowNull: false
    },
    location: {
        type: DataTypes.STRING(255),
        allowNull: false
    },
    playerLimit: {
        type: DataTypes.INTEGER,
        allowNull: true
    },
    isActive: {
        type: DataTypes.BOOLEAN,  // TINYINT(1) is mapped to BOOLEAN in Sequelize
        allowNull: false,
        defaultValue: true  // Assuming tournaments are active by default
    },
    descOID: {
        type: DataTypes.STRING(255),
        allowNull: true  // Assuming this can be nullable
    }, name: {
        type: DataTypes.STRING(255),
        allowNull: false,
        unique: true  // Unique constraint as per your schema
    }
}, {
    timestamps: true,  // Enable timestamps
  
});

// Many-to-many relationship (users can join multiple tournaments)
Tournament.belongsToMany(User, { through: 'UserTournaments' });
User.belongsToMany(Tournament, { through: 'UserTournaments' });

module.exports = Tournament;
