const express = require('express');
const router = express.Router();
const sequelize = require('../models/index');
const Tournament = require('../models/tournament');
const User = require('../models/user');
const auth = require('../middleware/auth');
// const crypto = require('crypto');
// const secretkey = crypto.randomBytes(64).toString('hex');
// console.log(secret);



/**
 * Public Routes
 */

// View specific tournament information (public access)
router.get('/public/:id', async (req, res) => {
    try {
        const tournamentId = req.params.id;

        // Find the tournament by ID
        const tournament = await Tournament.findByPk(tournamentId);

        if (!tournament) {
            return res.status(404).json({ message: 'Tournament not found' });
        }

        res.status(200).json(tournament);
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Server error' });
    }
});

// List all available tournaments (public access)
router.get('/public/list', async (req, res) => {
    try {
        // Fetch all tournaments
        const tournaments = await Tournament.findAll();

        res.status(200).json(tournaments);
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Server error' });
    }
});


/**
 * Authenticated Routes
 */

//view round matchups


//view player list


//view game results


// List all tournaments
// router.get('/list', auth, async (req, res) => {
//     try {
//         const tournaments = await Tournament.findAll({
//             include: [{
//                 model: User,
//                 attributes: ['name', 'email']
//             }]
//         });
//         res.status(200).json(tournaments);
//     } catch (error) {
//         res.status(500).json({ error: 'Server error' });
//     }
// });

// module.exports = router;
