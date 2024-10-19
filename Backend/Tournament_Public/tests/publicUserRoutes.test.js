const request = require('supertest');
const express = require('express');
const publicUserRoutes = require('../routes/publicUserRoutes');
const { getAllActiveTournaments, getTournamentById } = require('../controllers/publicUserController');

// Mock the controller functions
jest.mock('../controllers/publicUserController');

const app = express();
app.use(express.json());
app.use('/api/public', publicUserRoutes);

describe('Public User Routes', () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    // Test for GET /tournaments
    it('should get all tournaments', async () => {
        const mockTournaments = [
            { tournament_id: '01433c4a-87aa-11ef-8c7b-0242ac110003', name: 'Active Tournament 1', isactive: 1, startdate: '2024-01-01', enddate: '2024-01-10', location: 'City A', playerlimit: 100 },
            { tournament_id: '01433c4a-87aa-11ef-8c7b-0242ac110004', name: 'Active Tournament 2', isactive: 1, startdate: '2024-02-01', enddate: '2024-02-15', location: 'City B', playerlimit: 150 }
        ];
        getAllActiveTournaments.mockImplementation((req, res) => {
            res.status(200).json(mockTournaments);
        });

        const response = await request(app).get('/api/public/tournaments');
        expect(response.status).toBe(200);
        expect(response.body).toEqual(mockTournaments);
        expect(response.body[0]).toHaveProperty('tournament_id');
        expect(response.body[0]).toHaveProperty('name');
        expect(response.body[0]).toHaveProperty('isactive');
        expect(response.body[0]).toHaveProperty('startdate');
        expect(response.body[0]).toHaveProperty('enddate');
        expect(response.body[0]).toHaveProperty('location');
        expect(response.body[0]).toHaveProperty('playerlimit');
    });

    // Test for GET /tournaments/:id
    it('should get a specific tournament by ID', async () => {
        const mockTournament = { tournament_id: '01433c4a-87aa-11ef-8c7b-0242ac110004', name: 'Active Tournament 2', isactive: 1, startdate: '2024-02-01', enddate: '2024-02-15', location: 'City B', playerlimit: 150 };
        getTournamentById.mockImplementation((req, res) => {
            res.status(200).json(mockTournament);
        });

        const response = await request(app).get('/api/public/tournaments/01433c4a-87aa-11ef-8c7b-0242ac110004');
        expect(response.status).toBe(200);
        expect(response.body).toEqual(mockTournament);
    });

    // Test for POST /signup
    // it('should sign up a new user', async () => {
    //     const mockUser = { id: '1', name: 'User 1', email: 'user1@example.com' };
    //     signUpUser.mockImplementation((req, res) => {
    //         res.status(201).json(mockUser);
    //     });

    //     const response = await request(app)
    //         .post('/api/signup')
    //         .send({ name: 'User 1', email: 'user1@example.com', password: 'password123' });
    //     expect(response.status).toBe(201);
    //     expect(response.body).toEqual(mockUser);
    // });
});