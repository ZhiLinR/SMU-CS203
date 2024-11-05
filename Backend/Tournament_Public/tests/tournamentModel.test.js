const db = require('../models/config/db');  
const tournamentModel = require('../models/tournament');  

// Mock the database module
jest.mock('../models/config/db', () => ({
  query: jest.fn(),
  end: jest.fn(), // Mock the end method to close the connection
}));

describe('Tournament Model', () => {
  beforeEach(() => {
    // Clear mock data before each test (since its database, it sets up the environment)
    jest.clearAllMocks();
  });

  afterAll(() => {
    // Ensure the database connection is closed after all tests
    db.end();
  });

  describe('getAllTournaments', () => {
    it('should return all tournaments when successful', async () => {
      const mockTournaments = [
        { 
          id: '01433c4a-87aa-11ef-8c7b-0242ac1100089',
          name: 'Eastside Competition'
        },
        { 
          id: '09062b64-8a12-11ef-8c7b-0242ac1100088',
          name: 'ZZZZ'
        }
      ];

      // Mock the database response
      db.query.mockImplementation((query, callback) => {
        callback(null, mockTournaments);
      });

      const result = await tournamentModel.getAllTournaments();
      
      expect(result).toEqual(mockTournaments);
      expect(db.query).toHaveBeenCalledTimes(1);
      expect(db.query).toHaveBeenCalledWith(
        'CALL GetAllTournaments()',
        expect.any(Function)
      );
    });

    it('should reject with error when database query fails', async () => {
      const dbError = new Error('Database connection failed');

      // Mock the database error
      db.query.mockImplementation((query, callback) => {
        callback(dbError, null);
      });

      await expect(tournamentModel.getAllTournaments())
        .rejects
        .toEqual(dbError);
      
      expect(db.query).toHaveBeenCalledTimes(1);
    });
  });

  describe('getCompletedTournaments', () => {
    it('should return all past tournaments when successful', async () => {
      const mockTournaments = [
        { 
          id: '01433c4a-87aa-11ef-8c7b-0245ae933003',
          name: 'Eastside Competitionnnnn'
        },
        { 
          id: '09062b64-8a12-11ef-8c7b-0242gh1148403',
          name: 'ZZZZZZZZ'
        }
      ];

      // Mock the database response
      db.query.mockImplementation((query, callback) => {
        callback(null, mockTournaments);
      });

      const result = await tournamentModel.getCompletedTournaments();
      
      expect(result).toEqual(mockTournaments);
      expect(db.query).toHaveBeenCalledTimes(1);
      expect(db.query).toHaveBeenCalledWith(
        'CALL GetCompletedTournament()',
        expect.any(Function)
      );
    });

    it('should reject with error when database query fails', async () => {
      const dbError = new Error('Database connection failed');

      // Mock the database error
      db.query.mockImplementation((query, callback) => {
        callback(dbError, null);
      });

      await expect(tournamentModel.getCompletedTournaments())
        .rejects
        .toEqual(dbError);
      
      expect(db.query).toHaveBeenCalledTimes(1);
    });
  });

  describe('getUpcomingTournaments', () => {
    it('should return all upcoming tournaments when successful', async () => {
      const mockTournaments = [
        { 
          id: '01433c4a-87aa-11ef-8c7b-8434ag933',
          name: 'Mock1'
        },
        { 
          id: '09062b64-8a12-11ef-8c7b-0242gh1148403',
          name: 'Mock2'
        }
      ];

      // Mock the database response
      db.query.mockImplementation((query, callback) => {
        callback(null, mockTournaments);
      });

      const result = await tournamentModel.getUpcomingTournaments();
      
      expect(result).toEqual(mockTournaments);
      expect(db.query).toHaveBeenCalledTimes(1);
      expect(db.query).toHaveBeenCalledWith(
        'CALL GetUpComingTournament()',
        expect.any(Function)
      );
    });

    it('should reject with error when database query fails', async () => {
      const dbError = new Error('Database connection failed');

      // Mock the database error
      db.query.mockImplementation((query, callback) => {
        callback(dbError, null);
      });

      await expect(tournamentModel.getUpcomingTournaments())
        .rejects
        .toEqual(dbError);
      
      expect(db.query).toHaveBeenCalledTimes(1);
    });
  });

  describe('getInProgressTournaments', () => {
    it('should return all event tournaments when successful', async () => {
      const mockTournaments = [
        { 
          id: '01433c4a-87aa-11ef-389oefwsdea8',
          name: 'Mock1'
        },
        { 
          id: '09062b64-8a12-11ef-8c7b-0r39p9303',
          name: 'Mock2'
        }
      ];

      // Mock the database response
      db.query.mockImplementation((query, callback) => {
        callback(null, mockTournaments);
      });

      const result = await tournamentModel.getInProgressTournaments();
      
      expect(result).toEqual(mockTournaments);
      expect(db.query).toHaveBeenCalledTimes(1);
      expect(db.query).toHaveBeenCalledWith(
        'CALL GetInProgressTournament()',
        expect.any(Function)
      );
    });

    it('should reject with error when database query fails', async () => {
      const dbError = new Error('Database connection failed');

      // Mock the database error
      db.query.mockImplementation((query, callback) => {
        callback(dbError, null);
      });

      await expect(tournamentModel.getInProgressTournaments())
        .rejects
        .toEqual(dbError);
      
      expect(db.query).toHaveBeenCalledTimes(1);
    });
  });

  describe('getTournamentByID', () => {
    it('should return a tournament when given a valid ID', async () => {
      const tournamentId = '01433c4a-87aa-11ef-8c7b-0242ac110003';
      const mockTournament = {
        id: tournamentId,
        name: 'Test Tournament'
      };

      // Mock the database response with nested arrays to match your actual response
      db.query.mockImplementation((query, params, callback) => {
        callback(null, [[mockTournament]]);
      });

      const result = await tournamentModel.getTournamentByID(tournamentId);
      
      expect(result).toEqual(mockTournament);
      expect(db.query).toHaveBeenCalledTimes(1);
      expect(db.query).toHaveBeenCalledWith(
        'CALL GetTournamentById(?)',
        [tournamentId],
        expect.any(Function)
      );
    });

    it('should reject with error when database query fails', async () => {
      const tournamentId = '01433c4a-87aa-11ef-8c7b-0242ac110003';
      const dbError = new Error('Database connection failed');

      // Mock the database error
      db.query.mockImplementation((query, params, callback) => {
        callback(dbError, null);
      });

      await expect(tournamentModel.getTournamentByID(tournamentId))
        .rejects
        .toThrow(`Database error: ${dbError.message}`);
      
      expect(db.query).toHaveBeenCalledTimes(1);
    });

    it('should handle non-existent tournament ID', async () => {
      const tournamentId = '01433c4a-87aa-11ef-8c7b-0242ac110003';

      // Mock empty response
      db.query.mockImplementation((query, params, callback) => {
        callback(null, [[undefined]]);
      });

      const result = await tournamentModel.getTournamentByID(tournamentId);
      
      expect(result).toBeUndefined();
      expect(db.query).toHaveBeenCalledTimes(1);
    });
  });
});