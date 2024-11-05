const TournamentUserService = require('../userService/userService');
const tournamentController = require('../controllers/publicUserController');

// Mock the TournamentUserService
jest.mock('../userService/userService', () => ({
  getAllTournaments: jest.fn(),
  getUpcomingTournaments: jest.fn(),
  getInProgressTournaments: jest.fn(),
  getCompletedTournaments: jest.fn(),
  getTournamentByID: jest.fn(),
}));

// Spy on console.error
const consoleSpy = jest.spyOn(console, 'error');

describe('Tournament Controller', () => {
  afterEach(() => {
    jest.clearAllMocks();
    consoleSpy.mockReset();
  });

  describe('getAllTournaments', () => {
    it('should return all tournaments successfully', async () => {
      const mockTournaments = [
        { id: '01433c4a-87aa-11ef-8c7b-0242ac110003', name: 'Tournament 1' },
        { id: '09062b64-8a12-11ef-8c7b-0242ac110003', name: 'Tournament 2' },
      ];

      TournamentUserService.getAllTournaments.mockResolvedValue(mockTournaments);

      const req = {};
      const res = {
        status: jest.fn().mockReturnThis(),
        json: jest.fn(),
      };
      const next = jest.fn();

      await tournamentController.getAllTournaments(req, res, next);

      expect(TournamentUserService.getAllTournaments).toHaveBeenCalledTimes(1);
      expect(res.status).toHaveBeenCalledWith(200);
      expect(res.json).toHaveBeenCalledWith({
        success: true,
        status: 200,
        message: 'successfully retrieved all tournaments',
        content: mockTournaments,
      });
      expect(next).not.toHaveBeenCalled();
    });

    it('should return 404 when no tournaments are retrieved', async () => {
      TournamentUserService.getAllTournaments.mockResolvedValue([]);

      const req = {};
      const res = {
        status: jest.fn().mockReturnThis(),
        json: jest.fn(),
      };
      const next = jest.fn();

      await tournamentController.getAllTournaments(req, res, next);

      expect(TournamentUserService.getAllTournaments).toHaveBeenCalledTimes(1);
      expect(next).toHaveBeenCalledWith(expect.any(Error));
      expect(next.mock.calls[0][0]).toMatchObject({
        message: 'No tournaments retrieved',
        statusCode: 404,
      });
    });

    it('should handle errors', async () => {
      const mockError = new Error('Database connection failed');

      TournamentUserService.getAllTournaments.mockRejectedValue(mockError);

      const req = {};
      const res = {
        status: jest.fn().mockReturnThis(),
        json: jest.fn(),
      };
      const next = jest.fn();

      await tournamentController.getAllTournaments(req, res, next);

      expect(TournamentUserService.getAllTournaments).toHaveBeenCalledTimes(1);
      expect(consoleSpy).toHaveBeenCalledWith('Error retrieving tournaments:', mockError);
      expect(next).toHaveBeenCalledWith(mockError);
    });
  });

  describe('getUpcomingTournaments', () => {
    it('should return upcoming tournaments successfully', async () => {
      const mockTournaments = [
        { id: '01433c4a-87aa-11ef-8c7b-0242ac110003', name: 'Upcoming Tournament 1' },
        { id: '09062b64-8a12-11ef-8c7b-0242ac110003', name: 'Upcoming Tournament 2' },
      ];

      TournamentUserService.getUpcomingTournaments.mockResolvedValue(mockTournaments);

      const req = {};
      const res = {
        status: jest.fn().mockReturnThis(),
        json: jest.fn(),
      };
      const next = jest.fn();

      await tournamentController.getUpcomingTournaments(req, res, next);

      expect(TournamentUserService.getUpcomingTournaments).toHaveBeenCalledTimes(1);
      expect(res.status).toHaveBeenCalledWith(200);
      expect(res.json).toHaveBeenCalledWith({
        success: true,
        status: 200,
        message: 'successfully retrieved upcoming tournaments',
        content: mockTournaments,
      });
      expect(next).not.toHaveBeenCalled();
    });

    it('should return 404 when no upcoming tournaments are retrieved', async () => {
      TournamentUserService.getUpcomingTournaments.mockResolvedValue([]);

      const req = {};
      const res = {
        status: jest.fn().mockReturnThis(),
        json: jest.fn(),
      };
      const next = jest.fn();

      await tournamentController.getUpcomingTournaments(req, res, next);

      expect(TournamentUserService.getUpcomingTournaments).toHaveBeenCalledTimes(1);
      expect(next).toHaveBeenCalledWith(expect.any(Error));
      expect(next.mock.calls[0][0]).toMatchObject({
        message: 'No upcoming tournaments',
        statusCode: 404,
      });
    });

    it('should handle errors', async () => {
      const mockError = new Error('Database connection failed');

      TournamentUserService.getUpcomingTournaments.mockRejectedValue(mockError);

      const req = {};
      const res = {
        status: jest.fn().mockReturnThis(),
        json: jest.fn(),
      };
      const next = jest.fn();

      await tournamentController.getUpcomingTournaments(req, res, next);

      expect(TournamentUserService.getUpcomingTournaments).toHaveBeenCalledTimes(1);
      expect(console.error).toHaveBeenCalledWith('Error retrieving upcoming tournaments:', mockError);
      expect(next).toHaveBeenCalledWith(mockError);
    });
  });

  describe('getInProgressTournaments', () => {
    it('should return in-progress tournaments successfully', async () => {
      const mockTournaments = [
        { id: '01433c4a-87aa-11ef-8c7b-0242ac110003', name: 'Tournament 1' },
        { id: '09062b64-8a12-11ef-8c7b-0242ac110003', name: 'Tournament 2' },
      ];

      TournamentUserService.getInProgressTournaments.mockResolvedValue(mockTournaments);

      const req = {};
      const res = {
        status: jest.fn().mockReturnThis(),
        json: jest.fn(),
      };
      const next = jest.fn();

      await tournamentController.getInProgressTournaments(req, res, next);

      expect(TournamentUserService.getInProgressTournaments).toHaveBeenCalledTimes(1);
      expect(res.status).toHaveBeenCalledWith(200);
      expect(res.json).toHaveBeenCalledWith({
        success: true,
        status: 200,
        message: 'successfully retrieved in-progress tournaments',
        content: mockTournaments,
      });
      expect(next).not.toHaveBeenCalled();
    });

    it('should return 404 when no in-progress tournaments are retrieved', async () => {
      TournamentUserService.getInProgressTournaments.mockResolvedValue([]);

      const req = {};
      const res = {
        status: jest.fn().mockReturnThis(),
        json: jest.fn(),
      };
      const next = jest.fn();

      await tournamentController.getInProgressTournaments(req, res, next);

      expect(TournamentUserService.getInProgressTournaments).toHaveBeenCalledTimes(1);
      expect(next).toHaveBeenCalledWith(expect.any(Error));
      expect(next.mock.calls[0][0]).toMatchObject({
        message: 'No in-progress tournaments',
        statusCode: 404,
      });
    });

    it('should handle errors', async () => {
      const mockError = new Error('Database connection failed');

      TournamentUserService.getInProgressTournaments.mockRejectedValue(mockError);

      const req = {};
      const res = {
        status: jest.fn().mockReturnThis(),
        json: jest.fn(),
      };
      const next = jest.fn();

      await tournamentController.getInProgressTournaments(req, res, next);

      expect(TournamentUserService.getInProgressTournaments).toHaveBeenCalledTimes(1);
      expect(console.error).toHaveBeenCalledWith('Error retrieving in-progress tournaments:', mockError);
      expect(next).toHaveBeenCalledWith(mockError);
    });
  });

  describe('getCompletedTournaments', () => {
    it('should return completed tournaments successfully', async () => {
      const mockTournaments = [
        { id: '01433c4a-87aa-11ef-8c7b-0242ac110003', name: ' Tournament 1' },
        { id: '09062b64-8a12-11ef-8c7b-0242ac110003', name: 'Tournament 2' },
      ];

      TournamentUserService.getCompletedTournaments.mockResolvedValue(mockTournaments);

      const req = {};
      const res = {
        status: jest.fn().mockReturnThis(),
        json: jest.fn(),
      };
      const next = jest.fn();

      await tournamentController.getCompletedTournaments(req, res, next);

      expect(TournamentUserService.getCompletedTournaments).toHaveBeenCalledTimes(1);
      expect(res.status).toHaveBeenCalledWith(200);
      expect(res.json).toHaveBeenCalledWith({
        success: true,
        status: 200,
        message: 'successfully retrieved completed tournaments',
        content: mockTournaments,
      });
      expect(next).not.toHaveBeenCalled();
    });

    it('should return 404 when no completed tournaments are retrieved', async () => {
      TournamentUserService.getCompletedTournaments.mockResolvedValue([]);

      const req = {};
      const res = {
        status: jest.fn().mockReturnThis(),
        json: jest.fn(),
      };
      const next = jest.fn();

      await tournamentController.getCompletedTournaments(req, res, next);

      expect(TournamentUserService.getCompletedTournaments).toHaveBeenCalledTimes(1);
      expect(next).toHaveBeenCalledWith(expect.any(Error));
      expect(next.mock.calls[0][0]).toMatchObject({
        message: 'No completed tournaments',
        statusCode: 404,
      });
    });

    it('should handle errors', async () => {
      const mockError = new Error('Database connection failed');

      TournamentUserService.getCompletedTournaments.mockRejectedValue(mockError);

      const req = {};
      const res = {
        status: jest.fn().mockReturnThis(),
        json: jest.fn(),
      };
      const next = jest.fn();

      await tournamentController.getCompletedTournaments(req, res, next);

      expect(TournamentUserService.getCompletedTournaments).toHaveBeenCalledTimes(1);
      expect(console.error).toHaveBeenCalledWith('Error retrieving completed tournaments:', mockError);
      expect(next).toHaveBeenCalledWith(mockError);
    });
  });

  describe('getTournamentById', () => {
    it('should return a tournament when given a valid ID', async () => {
      const tournamentId = '01433c4a-87aa-11ef-8c7b-0242ac110003';
      const mockTournament = {
        id: tournamentId,
        name: 'Test Tournament'
      };

      TournamentUserService.getTournamentByID.mockResolvedValue(mockTournament);

      const req = {
        params: {
          tournamentId
        }
      };
      const res = {
        status: jest.fn().mockReturnThis(),
        json: jest.fn(),
      };
      const next = jest.fn();

      await tournamentController.getTournamentById(req, res, next);

      expect(TournamentUserService.getTournamentByID).toHaveBeenCalledTimes(1);
      expect(TournamentUserService.getTournamentByID).toHaveBeenCalledWith(tournamentId);
      expect(res.status).toHaveBeenCalledWith(200);
      expect(res.json).toHaveBeenCalledWith({
        success: true,
        status: 200,
        message: 'successfully retrieved tournament based on tournament ID',
        content: mockTournament,
      });
      expect(next).not.toHaveBeenCalled();
    });

    it('should return 404 when no tournament is found', async () => {
      const tournamentId = '01433c4a-87aa-11ef-8c7b-0242ac110003';

      TournamentUserService.getTournamentByID.mockResolvedValue(undefined);

      const req = {
        params: {
          tournamentId
        }
      };
      const res = {
        status: jest.fn().mockReturnThis(),
        json: jest.fn(),
      };
      const next = jest.fn();

      await tournamentController.getTournamentById(req, res, next);

      expect(TournamentUserService.getTournamentByID).toHaveBeenCalledTimes(1);
      expect(TournamentUserService.getTournamentByID).toHaveBeenCalledWith(tournamentId);
      expect(next).toHaveBeenCalledWith(expect.any(Error));
      expect(next.mock.calls[0][0]).toMatchObject({
        message: 'No tournament with that id is found',
        statusCode: 404,
      });
    });

    it('should handle errors', async () => {
      const tournamentId = '01433c4a-87aa-11ef-8c7b-0242ac110003';
      const mockError = new Error('Database connection failed');

      TournamentUserService.getTournamentByID.mockRejectedValue(mockError);

      const req = {
        params: {
          tournamentId
        }
      };
      const res = {
        status: jest.fn().mockReturnThis(),
        json: jest.fn(),
      };
      const next = jest.fn();

      await tournamentController.getTournamentById(req, res, next);

      expect(TournamentUserService.getTournamentByID).toHaveBeenCalledTimes(1);
      expect(TournamentUserService.getTournamentByID).toHaveBeenCalledWith(tournamentId);
      expect(consoleSpy).toHaveBeenCalledWith('Error retrieving completed tournaments:', mockError);
      expect(next).toHaveBeenCalledWith(mockError);
    });
  });
});