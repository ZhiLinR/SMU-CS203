const {
    getTournaments,
    getTournamentMatchups,
    signUpForTournament,
    quitTournament,
    getPlayersInTournament
} = require('../tournamentUserController'); // Import the controller functions

const TournamentUserService = require('../../services/tournamentUserService'); // Import the service
const TournamentService = require('../../services/tournamentService'); // Import the service
const UserService = require('../../services/userService'); // Correctly import UserService

const db = require('../../config/db'); // Import the database configuration

// Mock services and database
jest.mock('../../services/tournamentUserService'); // Mock the service layer
jest.mock('../../services/tournamentService');
jest.mock('../../services/UserService');
jest.mock('../../config/db', () => ({
    query: jest.fn(),
    connect: jest.fn(),
}));

// Suppress logs to avoid clutter during testing
jest.spyOn(console, 'error').mockImplementation(() => {});
jest.spyOn(console, 'log').mockImplementation(() => {});




// Test for getTournaments route
//if no tournament exist
describe('getTournaments Controller', () => {
    afterEach(() => {
        jest.clearAllMocks(); // Reset mocks after each test
    });

    it('should return a list of tournaments for valid UUID', async () => {
        const req = { params: { UUID: 'valid-uuid' } }; // Mock request
        const res = { locals: {}, status: jest.fn().mockReturnThis(), json: jest.fn() }; // Mock response
        const next = jest.fn(); // Mock next middleware function

        // Mock service response
        TournamentUserService.getTournaments.mockResolvedValue([
            { name: 'Tournament A' },
            { name: 'Tournament B' },
        ]);

        // Call the controller
        await getTournaments(req, res, next);

        // Assertions
        expect(res.locals.data).toEqual({
            statusCode: 200,
            message: 'Tournament Found',
            success: true,
            content: ['Tournament A', 'Tournament B'],
        });
        expect(next).toHaveBeenCalled(); // Ensure next was called
    });

    it('should handle no tournaments found', async () => {
        const req = { params: { UUID: 'valid-uuid' } }; // Mock request
        const res = { locals: {}, status: jest.fn().mockReturnThis(), json: jest.fn() }; // Mock response
        const next = jest.fn(); // Mock next middleware function

        // Mock service response
        TournamentUserService.getTournaments.mockResolvedValue(null);

        // Call the controller
        await getTournaments(req, res, next);

        // Assertions
        expect(next).toHaveBeenCalledWith(expect.objectContaining({ statusCode: 404 })); // Ensure error passed to next
    });
});




// Test case for getTournamentMatchups
//test no matchup found
//handle service error
describe('getTournamentMatchups Controller', () => {
    afterEach(() => {
        jest.clearAllMocks(); // Clear mocks after each test
    });

    it('should return a list of matchups with enriched player names', async () => {
        const req = { params: { tournamentId: 'tournament-123' }, body: { UUID: 'user-456' } }; // Mock request
        const res = { locals: {}, status: jest.fn().mockReturnThis(), json: jest.fn() }; // Mock response
        const next = jest.fn(); // Mock next middleware function

        // Mock service responses
        TournamentUserService.getTournamentMatchups.mockResolvedValue([
            { player1: 'player-1', player2: 'player-2', playerWon: 'player-1', tournamentID: 'tournament-123', roundNum: 1 },
            { player1: 'player-3', player2: 'player-4', playerWon: 'player-4', tournamentID: 'tournament-123', roundNum: 2 },
        ]);

        UserService.getNamesByUUIDs.mockResolvedValue({
            'player-1': 'Alice',
            'player-2': 'Bob',
            'player-3': 'Charlie',
            'player-4': 'Diana',
        });

        // Call the controller
        await getTournamentMatchups(req, res, next);

        // Assertions
        expect(res.locals.data).toEqual({
            statusCode: 200,
            message: 'Tournament Matchups:',
            success: true,
            content: [
                {
                    player1Name: 'Alice',
                    player2Name: 'Bob',
                    playerWonName: 'Alice',
                    tournamentID: 'tournament-123',
                    roundNum: 1,
                },
                {
                    player1Name: 'Charlie',
                    player2Name: 'Diana',
                    playerWonName: 'Diana',
                    tournamentID: 'tournament-123',
                    roundNum: 2,
                },
            ],
        });
        expect(next).toHaveBeenCalled(); // Ensure next middleware was called
    });

    it('should handle no matchups found', async () => {
        const req = { params: { tournamentId: 'tournament-123' }, body: { UUID: 'user-456' } }; // Mock request
        const res = { locals: {}, status: jest.fn().mockReturnThis(), json: jest.fn() }; // Mock response
        const next = jest.fn(); // Mock next middleware function

        // Mock service response
        TournamentUserService.getTournamentMatchups.mockResolvedValue([]); // No matchups found

        // Call the controller
        await getTournamentMatchups(req, res, next);

        // Assertions
        expect(next).toHaveBeenCalledWith(expect.objectContaining({
            message: 'No matchups found for the provided tournament ID/UUID',
            statusCode: 404,
        })); // Ensure error is passed to next middleware
    });

    it('should handle service errors gracefully', async () => {
        const req = { params: { tournamentId: 'tournament-123' }, body: { UUID: 'user-456' } }; // Mock request
        const res = { locals: {}, status: jest.fn().mockReturnThis(), json: jest.fn() }; // Mock response
        const next = jest.fn(); // Mock next middleware function

        // Mock service to throw an error
        TournamentUserService.getTournamentMatchups.mockRejectedValue(new Error('Database error'));

        // Call the controller
        await getTournamentMatchups(req, res, next);

        // Assertions
        expect(next).toHaveBeenCalledWith(expect.objectContaining({
            message: 'Database error',
        })); // Ensure error is passed to next middleware
    });
});


// Test case for signUpForTournament
//test missing fields
//test handle service error
describe('signUpForTournament Controller', () => {
    afterEach(() => {
        jest.clearAllMocks(); // Reset mocks after each test
    });

    it('should successfully sign up for a tournament', async () => {
        const req = {
            params: { UUID: 'user-123' }, // Mock request params
            body: { tournamentID: 'tournament-456', elo: 1500 }, // Mock request body
        };
        const res = { locals: {}, status: jest.fn().mockReturnThis(), json: jest.fn() }; // Mock response
        const next = jest.fn(); // Mock next middleware function

        // Mock service response (successful signup)
        TournamentUserService.signUpForTournament.mockResolvedValue();

        // Call the controller
        await signUpForTournament(req, res, next);

        // Assertions
        expect(TournamentUserService.signUpForTournament).toHaveBeenCalledWith('user-123', 'tournament-456', 1500);
        expect(res.locals.data).toEqual({
            statusCode: 200,
            message: 'Successfully signed up for the tournament',
            success: true,
            content: { UUID: 'user-123', tournamentID: 'tournament-456', elo: 1500 },
        });
        expect(next).toHaveBeenCalled(); // Ensure next was called
    });

    it('should handle missing fields', async () => {
        const req = {
            params: {}, // Missing UUID
            body: { tournamentID: 'tournament-456' }, // Missing elo
        };
        const res = { locals: {}, status: jest.fn().mockReturnThis(), json: jest.fn() }; // Mock response
        const next = jest.fn(); // Mock next middleware function

        // Call the controller
        await signUpForTournament(req, res, next);

        // Assertions
        expect(next).toHaveBeenCalledWith(expect.objectContaining({
            message: 'Missing required fields: UUID, elo',
            statusCode: 400, // Bad Request
        }));
        expect(TournamentUserService.signUpForTournament).not.toHaveBeenCalled(); // Service should not be called
    });

    it('should handle service errors', async () => {
        const req = {
            params: { UUID: 'user-123' },
            body: { tournamentID: 'tournament-456', elo: 1500 },
        };
        const res = { locals: {}, status: jest.fn().mockReturnThis(), json: jest.fn() }; // Mock response
        const next = jest.fn(); // Mock next middleware function

        // Mock service to throw an error
        TournamentUserService.signUpForTournament.mockRejectedValue(new Error('Database error'));

        // Call the controller
        await signUpForTournament(req, res, next);

        // Assertions
        expect(next).toHaveBeenCalledWith(expect.objectContaining({
            message: 'Database error',
        }));
    });
});



//test for quitting
//test  missing field
//test no signup found
//test service error
describe('quitTournament Controller', () => {
    afterEach(() => {
        jest.clearAllMocks(); // Reset mocks after each test
    });

    it('should successfully quit a tournament', async () => {
        const req = {
            params: { UUID: 'user-123' }, // Mock request params
            body: { tournamentID: 'tournament-456' }, // Mock request body
        };
        const res = { locals: {}, status: jest.fn().mockReturnThis(), json: jest.fn() }; // Mock response
        const next = jest.fn(); // Mock next middleware function

        // Mock service response (affected rows > 0 means successful quit)
        TournamentUserService.quitTournament.mockResolvedValue({ affectedRows: 1 });

        // Call the controller
        await quitTournament(req, res, next);

        // Assertions
        expect(TournamentUserService.quitTournament).toHaveBeenCalledWith('user-123', 'tournament-456');
        expect(res.locals.data).toEqual({
            statusCode: 200,
            message: 'Successfully quit from the tournament',
            success: true,
        });
        expect(next).toHaveBeenCalled(); // Ensure next was called
    });

    it('should handle missing fields', async () => {
        const req = { params: {}, body: {} }; // No UUID or tournamentID provided
        const res = { locals: {}, status: jest.fn().mockReturnThis(), json: jest.fn() }; // Mock response
        const next = jest.fn(); // Mock next middleware function

        // Call the controller
        await quitTournament(req, res, next);

        // Assertions
        expect(next).toHaveBeenCalledWith(expect.objectContaining({
            message: 'Missing required fields (UUID or tournamentID)',
            statusCode: 400, // Bad Request
        }));
        expect(TournamentUserService.quitTournament).not.toHaveBeenCalled(); // Service should not be called
    });

    it('should handle no signup found', async () => {
        const req = {
            params: { UUID: 'user-123' }, // Mock request params
            body: { tournamentID: 'tournament-456' }, // Mock request body
        };
        const res = { locals: {}, status: jest.fn().mockReturnThis(), json: jest.fn() }; // Mock response
        const next = jest.fn(); // Mock next middleware function

        // Mock service response (no affected rows)
        TournamentUserService.quitTournament.mockResolvedValue({ affectedRows: 0 });

        // Call the controller
        await quitTournament(req, res, next);

        // Assertions
        expect(next).toHaveBeenCalledWith(expect.objectContaining({
            message: 'No signup found for the provided UUID and tournamentID',
            statusCode: 404, // Not Found
        }));
    });

    it('should handle service errors gracefully', async () => {
        const req = {
            params: { UUID: 'user-123' },
            body: { tournamentID: 'tournament-456' },
        };
        const res = { locals: {}, status: jest.fn().mockReturnThis(), json: jest.fn() }; // Mock response
        const next = jest.fn(); // Mock next middleware function

        // Mock service to throw an error
        TournamentUserService.quitTournament.mockRejectedValue(new Error('Database error'));

        // Call the controller
        await quitTournament(req, res, next);

        // Assertions
        expect(next).toHaveBeenCalledWith(expect.objectContaining({
            message: 'Database error',
        }));
    });
});


//test for get players in tournament
//test missing tournament
//test if no players are found
//test for unexpected errors

describe('getTournamentMatchups Controller', () => {
    afterEach(() => {
        jest.clearAllMocks(); // Clear mocks after each test
    });

    it('should return a list of matchups with enriched player names', async () => {
        const req = { params: { tournamentId: 'tournament-123' }, body: { UUID: 'user-456' } }; // Mock request
        const res = { locals: {}, status: jest.fn().mockReturnThis(), json: jest.fn() }; // Mock response
        const next = jest.fn(); // Mock next middleware function

        // Mock service responses
        TournamentUserService.getTournamentMatchups.mockResolvedValue([
            { player1: 'player-1', player2: 'player-2', playerWon: 'player-1', tournamentID: 'tournament-123', roundNum: 1 },
            { player1: 'player-3', player2: 'player-4', playerWon: 'player-4', tournamentID: 'tournament-123', roundNum: 2 },
        ]);

        UserService.getNamesByUUIDs.mockResolvedValue({
            'player-1': 'Alice',
            'player-2': 'Bob',
            'player-3': 'Charlie',
            'player-4': 'Diana',
        });

        // Call the controller
        await getTournamentMatchups(req, res, next);

        // Assertions
        expect(res.locals.data).toEqual({
            statusCode: 200,
            message: 'Tournament Matchups:',
            success: true,
            content: [
                {
                    player1Name: 'Alice',
                    player2Name: 'Bob',
                    playerWonName: 'Alice',
                    tournamentID: 'tournament-123',
                    roundNum: 1,
                },
                {
                    player1Name: 'Charlie',
                    player2Name: 'Diana',
                    playerWonName: 'Diana',
                    tournamentID: 'tournament-123',
                    roundNum: 2,
                },
            ],
        });
        expect(next).toHaveBeenCalled(); // Ensure next middleware was called
    });

    it('should handle no matchups found', async () => {
        const req = { params: { tournamentId: 'tournament-123' }, body: { UUID: 'user-456' } }; // Mock request
        const res = { locals: {}, status: jest.fn().mockReturnThis(), json: jest.fn() }; // Mock response
        const next = jest.fn(); // Mock next middleware function

        // Mock service response
        TournamentUserService.getTournamentMatchups.mockResolvedValue([]); // No matchups found

        // Call the controller
        await getTournamentMatchups(req, res, next);

        // Assertions
        expect(next).toHaveBeenCalledWith(expect.objectContaining({
            message: 'No matchups found for the provided tournament ID/UUID',
            statusCode: 404,
        })); // Ensure error is passed to next middleware
    });

    it('should handle service errors gracefully', async () => {
        const req = { params: { tournamentId: 'tournament-123' }, body: { UUID: 'user-456' } }; // Mock request
        const res = { locals: {}, status: jest.fn().mockReturnThis(), json: jest.fn() }; // Mock response
        const next = jest.fn(); // Mock next middleware function

        // Mock service to throw an error
        TournamentUserService.getTournamentMatchups.mockRejectedValue(new Error('Database error'));

        // Call the controller
        await getTournamentMatchups(req, res, next);

        // Assertions
        expect(next).toHaveBeenCalledWith(expect.objectContaining({
            message: 'Database error',
        })); // Ensure error is passed to next middleware
    });
});