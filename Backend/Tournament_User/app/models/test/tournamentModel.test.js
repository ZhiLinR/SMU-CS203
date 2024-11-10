const db = require('../../config/db'); // Mocked database connection
const TournamentModel = require('../tournamentModel'); // Model to test

// Mock the database connection
jest.mock('../../config/db', () => ({
    query: jest.fn(),
}));

describe('TournamentModel', () => {
    afterEach(() => {
        jest.clearAllMocks(); // Reset mocks after each test
    });

    describe('signUpForTournament', () => {
        it('should resolve with results if the query is successful', async () => {
            const UUID = 'user-123';
            const tournamentID = 'tournament-456';
            const elo = 1500;

            const mockResults = { affectedRows: 1 }; // Mock query result

            // Mock the db.query implementation
            db.query.mockImplementation((query, params, callback) => {
                expect(query).toBe('CALL SignUpForTournament(?, ?, ?)'); // Ensure correct query
                expect(params).toEqual([UUID, tournamentID, elo]); // Ensure parameters are passed correctly
                callback(null, mockResults); // Simulate a successful query
            });

            // Call the model function
            const result = await TournamentModel.signUpForTournament(UUID, tournamentID, elo);

            // Assertions
            expect(result).toEqual(mockResults);
            expect(db.query).toHaveBeenCalledWith(
                'CALL SignUpForTournament(?, ?, ?)',
                [UUID, tournamentID, elo],
                expect.any(Function)
            );
        });

        it('should reject with an error if the query fails', async () => {
            const UUID = 'user-123';
            const tournamentID = 'tournament-456';
            const elo = 1500;

            const mockError = new Error('Database error'); // Mock query error

            // Mock the db.query implementation
            db.query.mockImplementation((query, params, callback) => {
                callback(mockError, null); // Simulate a failed query
            });

            // Call the model function and expect it to reject
            await expect(TournamentModel.signUpForTournament(UUID, tournamentID, elo)).rejects.toThrow('Database error');

            // Ensure the database query was called
            expect(db.query).toHaveBeenCalledWith(
                'CALL SignUpForTournament(?, ?, ?)',
                [UUID, tournamentID, elo],
                expect.any(Function)
            );
        });
    });


    //quit tournament model


    describe('quitTournament', () => {
        it('should resolve with results if the query is successful', async () => {
            const UUID = 'user-123';
            const tournamentID = 'tournament-456';

            const mockResults = { affectedRows: 1 }; // Mock query result

            // Mock the db.query implementation
            db.query.mockImplementation((query, params, callback) => {
                expect(query).toBe('CALL UserQuit(?, ?)'); // Ensure correct query
                expect(params).toEqual([UUID, tournamentID]); // Ensure parameters are passed correctly
                callback(null, mockResults); // Simulate a successful query
            });

            // Call the model function
            const result = await TournamentModel.quitTournament(UUID, tournamentID);

            // Assertions
            expect(result).toEqual(mockResults);
            expect(db.query).toHaveBeenCalledWith(
                'CALL UserQuit(?, ?)',
                [UUID, tournamentID],
                expect.any(Function)
            );
        });

        it('should reject with an error if the query fails', async () => {
            const UUID = 'user-123';
            const tournamentID = 'tournament-456';

            const mockError = new Error('Database error'); // Mock query error

            // Mock the db.query implementation
            db.query.mockImplementation((query, params, callback) => {
                callback(mockError, null); // Simulate a failed query
            });

            // Call the model function and expect it to reject
            await expect(TournamentModel.quitTournament(UUID, tournamentID)).rejects.toThrow('Database error');

            // Ensure the database query was called
            expect(db.query).toHaveBeenCalledWith(
                'CALL UserQuit(?, ?)',
                [UUID, tournamentID],
                expect.any(Function)
            );
        });
    });


    //test check tournament exist in db anot
    describe('checkTournamentExists', () => {
        afterEach(() => {
            jest.clearAllMocks();
        });

        it('should return true if the tournament exists', async () => {
            const mockResult = [{ count: 1 }];

            db.query.mockImplementation((query, params, callback) => {
                callback(null, mockResult);
            });

            const result = await TournamentModel.checkTournamentExists(123); // Pass a mock tournamentId
            expect(result).toBe(true);
            expect(db.query).toHaveBeenCalledWith(
                'SELECT COUNT(*) AS count FROM Tournament WHERE tournamentID = ?',
                [123],
                expect.any(Function)
            );
        });

        it('should return false if the tournament does not exist', async () => {
            const mockResult = [{ count: 0 }];

            db.query.mockImplementation((query, params, callback) => {
                callback(null, mockResult);
            });

            const result = await TournamentModel.checkTournamentExists(123);
            expect(result).toBe(false);
        });

        it('should throw an error if the database query fails', async () => {
            const mockError = new Error('Database connection failed');

            db.query.mockImplementation((query, params, callback) => {
                callback(mockError, null); // Simulate database error
            });

            await expect(TournamentModel.checkTournamentExists(123))
                .rejects.toThrow('Database connection failed'); // Match the raw error message
        });

    });


    //test for players in tournament
    describe('quitTournament', () => {
        it('should resolve with results if the query is successful', async () => {
            const UUID = 'user-123';
            const tournamentID = 'tournament-456';

            const mockResults = { affectedRows: 1 }; // Mock query result

            // Mock the db.query implementation
            db.query.mockImplementation((query, params, callback) => {
                expect(query).toBe('CALL UserQuit(?, ?)'); // Ensure correct query
                expect(params).toEqual([UUID, tournamentID]); // Ensure parameters are passed correctly
                callback(null, mockResults); // Simulate a successful query
            });

            // Call the model function
            const result = await TournamentModel.quitTournament(UUID, tournamentID);

            // Assertions
            expect(result).toEqual(mockResults);
            expect(db.query).toHaveBeenCalledWith(
                'CALL UserQuit(?, ?)',
                [UUID, tournamentID],
                expect.any(Function)
            );
        });

        it('should reject with an error if the query fails', async () => {
            const UUID = 'user-123';
            const tournamentID = 'tournament-456';

            const mockError = new Error('Database error'); // Mock query error

            // Mock the db.query implementation
            db.query.mockImplementation((query, params, callback) => {
                callback(mockError, null); // Simulate a failed query
            });

            // Call the model function and expect it to reject
            await expect(TournamentModel.quitTournament(UUID, tournamentID)).rejects.toThrow('Database error');

            // Ensure the database query was called
            expect(db.query).toHaveBeenCalledWith(
                'CALL UserQuit(?, ?)',
                [UUID, tournamentID],
                expect.any(Function)
            );
        });
    });

});





