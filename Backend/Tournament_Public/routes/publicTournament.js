const express = require('express');
const router = express.Router();
// const sequelize = require('../models/index');
// const Tournament = require('../models/tournament');
// const User = require('../models/User');
const auth = require('../middlewares/auth');
const connection = require('../middlewares/db');

/**
 * Public Routes
 */

//get all tournaments
// List all available tournaments (public access)
// router.get('/list', async (req, res) => {
//     try {
//         // Fetch all tournaments
//         const tournaments = await Tournament.findAll();

//         res.status(200).json(tournaments);
//     } catch (error) {
//         console.error(error);
//         res.status(500).json({ error: 'Server error' });
//     }
// });

router.get('/list', (req, res) => {
    const query = 'SELECT * FROM public_tournaments';
    connection.query(query, (error, results) => {
        if (error) {
            console.error('Error fetching tournaments:', error);
            return res.status(500).json({ error: 'Server error' });
        }
        res.status(200).json(results);
    });
});

// View specific tournament information (public access)
// router.get('/:id', async (req, res) => {
//     try {
//         const tournamentId = req.params.id;

//         // Find the tournament by ID
//         const tournament = await Tournament.findByPk(tournamentId);

//         if (!tournament) {
//             return res.status(404).json({ message: 'Tournament not found' });
//         }

//         res.status(200).json(tournament);} catch (error) {
//             console.error(error);
//             res.status(500).json({ error: 'Server error' });
//         }
//     });
router.get('/:id', (req, res) => {
    const tournamentId = req.params.id;
    const query = 'SELECT * FROM public_tournaments WHERE tournamentId = ?';
    connection.query(query, [tournamentId], (error, results) => {
        if (error) {
            console.error('Error fetching tournament:', error);
            return res.status(500).json({ error: 'Server error' });
        }
        if (results.length === 0) {
            return res.status(404).json({ message: 'Tournament not found' });
        }
        res.status(200).json(results[0]);
    });
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
    
module.exports = router;