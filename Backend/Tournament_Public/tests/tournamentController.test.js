const { validationResult } = require('express-validator');
const TournamentUserService = require('../userService/userService');
const tournamentController = require('../controllers/publicUserController');
const { sendSuccessResponse } = require('../middlewares/successHandler');

// Mock the required modules
jest.mock('express-validator', () => ({
  validationResult: jest.fn(),
  param: () => ({
    isUUID: () => ({
      withMessage: () => ({
        notEmpty: () => ({
          withMessage: jest.fn()
        })
      })
    })
  })
}));

jest.mock('../userService/userService', () => ({
  getAllTournaments: jest.fn(),
  getUpcomingTournaments: jest.fn(),
  getOngoingTournaments: jest.fn(),
  getCompletedTournaments: jest.fn(),
  getTournamentByID: jest.fn(),
}));

// Mock the success handler
jest.mock('../middlewares/successHandler', () => ({
  sendSuccessResponse: jest.fn()
}));

// Spy on console.error
const consoleSpy = jest.spyOn(console, 'error');

describe('Tournament Controller', () => {
  let mockReq;
  let mockRes;
  let mockNext;

  beforeEach(() => {
    mockReq = {};
    mockRes = {
      status: jest.fn().mockReturnThis(),
      json: jest.fn().mockReturnThis()
    };
    mockNext = jest.fn();
  });

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

      await tournamentController.getAllTournaments(mockReq, mockRes, mockNext);

      expect(TournamentUserService.getAllTournaments).toHaveBeenCalledTimes(1);
      expect(sendSuccessResponse).toHaveBeenCalledWith(
        mockRes,
        200,
        'successfully retrieved all tournaments',
        mockTournaments
      );
      expect(mockNext).not.toHaveBeenCalled();
    });

    it('should return 404 when no tournaments are found', async () => {
      TournamentUserService.getAllTournaments.mockResolvedValue([]);

      await tournamentController.getAllTournaments(mockReq, mockRes, mockNext);

      expect(TournamentUserService.getAllTournaments).toHaveBeenCalledTimes(1);
      expect(mockNext).toHaveBeenCalledWith(expect.any(Error));
      expect(mockNext.mock.calls[0][0]).toMatchObject({
        message: 'No tournaments found',
        statusCode: 404,
      });
    });

    it('should handle errors', async () => {
      const mockError = new Error('Database connection failed');
      TournamentUserService.getAllTournaments.mockRejectedValue(mockError);

      await tournamentController.getAllTournaments(mockReq, mockRes, mockNext);

      expect(TournamentUserService.getAllTournaments).toHaveBeenCalledTimes(1);
      expect(consoleSpy).toHaveBeenCalledWith('Error retrieving tournaments:', mockError);
      expect(mockNext).toHaveBeenCalledWith(mockError);
    });
  });

  describe('getUpcomingTournaments', () => {
    it('should return upcoming tournaments successfully', async () => {
      const mockTournaments = [
        { id: '01433c4a-87aa-11ef-8c7b-0242ac110003', name: 'Upcoming Tournament 1' },
        { id: '09062b64-8a12-11ef-8c7b-0242ac110003', name: 'Upcoming Tournament 2' },
      ];

      TournamentUserService.getUpcomingTournaments.mockResolvedValue(mockTournaments);

      await tournamentController.getUpcomingTournaments(mockReq, mockRes, mockNext);

      expect(TournamentUserService.getUpcomingTournaments).toHaveBeenCalledTimes(1);
      expect(sendSuccessResponse).toHaveBeenCalledWith(
        mockRes,
        200,
        'successfully retrieved upcoming tournamentst',
        mockTournaments
      );
      expect(mockNext).not.toHaveBeenCalled();
    });

    it('should return 404 when no upcoming tournaments are found', async () => {
      TournamentUserService.getUpcomingTournaments.mockResolvedValue([]);

      await tournamentController.getUpcomingTournaments(mockReq, mockRes, mockNext);

      expect(TournamentUserService.getUpcomingTournaments).toHaveBeenCalledTimes(1);
      expect(mockNext).toHaveBeenCalledWith(expect.any(Error));
      expect(mockNext.mock.calls[0][0]).toMatchObject({
        message: 'No upcoming tournaments',
        statusCode: 404,
      });
    });

    it('should handle errors', async () => {
      const mockError = new Error('Database connection failed');
      TournamentUserService.getUpcomingTournaments.mockRejectedValue(mockError);

      await tournamentController.getUpcomingTournaments(mockReq, mockRes, mockNext);

      expect(TournamentUserService.getUpcomingTournaments).toHaveBeenCalledTimes(1);
      expect(consoleSpy).toHaveBeenCalledWith('Error retrieving upcoming tournaments:', mockError);
      expect(mockNext).toHaveBeenCalledWith(mockError);
    });
  });

  describe('getOngoingTournaments', () => {
    it('should return ongoing tournaments successfully', async () => {
      const mockTournaments = [
        { id: '01433c4a-87aa-11ef-8c7b-0242ac110003', name: 'Tournament 1' },
        { id: '09062b64-8a12-11ef-8c7b-0242ac110003', name: 'Tournament 2' },
      ];

      TournamentUserService.getOngoingTournaments.mockResolvedValue(mockTournaments);

      await tournamentController.getOngoingTournaments(mockReq, mockRes, mockNext);

      expect(TournamentUserService.getOngoingTournaments).toHaveBeenCalledTimes(1);
      expect(sendSuccessResponse).toHaveBeenCalledWith(
        mockRes,
        200,
        'successfully retrieved ongoing tournaments',
        mockTournaments
      );
      expect(mockNext).not.toHaveBeenCalled();
    });

    it('should return 404 when no ongoing tournaments are found', async () => {
      TournamentUserService.getOngoingTournaments.mockResolvedValue([]);

      await tournamentController.getOngoingTournaments(mockReq, mockRes, mockNext);

      expect(TournamentUserService.getOngoingTournaments).toHaveBeenCalledTimes(1);
      expect(mockNext).toHaveBeenCalledWith(expect.any(Error));
      expect(mockNext.mock.calls[0][0]).toMatchObject({
        message: 'No ongoing tournaments',
        statusCode: 404,
      });
    });

    it('should handle errors', async () => {
      const mockError = new Error('Database connection failed');
      TournamentUserService.getOngoingTournaments.mockRejectedValue(mockError);

      await tournamentController.getOngoingTournaments(mockReq, mockRes, mockNext);

      expect(TournamentUserService.getOngoingTournaments).toHaveBeenCalledTimes(1);
      expect(consoleSpy).toHaveBeenCalledWith('Error retrieving ongoing tournaments:', mockError);
      expect(mockNext).toHaveBeenCalledWith(mockError);
    });
  });

  describe('getCompletedTournaments', () => {
    it('should return completed tournaments successfully', async () => {
      const mockTournaments = [
        { id: '01433c4a-87aa-11ef-8c7b-0242ac110003', name: 'Tournament 1' },
        { id: '09062b64-8a12-11ef-8c7b-0242ac110003', name: 'Tournament 2' },
      ];

      TournamentUserService.getCompletedTournaments.mockResolvedValue(mockTournaments);

      await tournamentController.getCompletedTournaments(mockReq, mockRes, mockNext);

      expect(TournamentUserService.getCompletedTournaments).toHaveBeenCalledTimes(1);
      expect(sendSuccessResponse).toHaveBeenCalledWith(
        mockRes,
        200,
        'successfully retrieved completed tournaments',
        mockTournaments
      );
      expect(mockNext).not.toHaveBeenCalled();
    });

    it('should return 404 when no completed tournaments are found', async () => {
      TournamentUserService.getCompletedTournaments.mockResolvedValue([]);

      await tournamentController.getCompletedTournaments(mockReq, mockRes, mockNext);

      expect(TournamentUserService.getCompletedTournaments).toHaveBeenCalledTimes(1);
      expect(mockNext).toHaveBeenCalledWith(expect.any(Error));
      expect(mockNext.mock.calls[0][0]).toMatchObject({
        message: 'No completed tournaments',
        statusCode: 404,
      });
    });

    it('should handle errors', async () => {
      const mockError = new Error('Database connection failed');
      TournamentUserService.getCompletedTournaments.mockRejectedValue(mockError);

      await tournamentController.getCompletedTournaments(mockReq, mockRes, mockNext);

      expect(TournamentUserService.getCompletedTournaments).toHaveBeenCalledTimes(1);
      expect(consoleSpy).toHaveBeenCalledWith('Error retrieving completed tournaments:', mockError);
      expect(mockNext).toHaveBeenCalledWith(mockError);
    });
  });

  describe('getTournamentById', () => {
    beforeEach(() => {
      validationResult.mockImplementation(() => ({
        isEmpty: () => true,
        array: () => []
      }));
    });

    it('should return a tournament when given a valid ID', async () => {
      const tournamentId = '01433c4a-87aa-11ef-8c7b-0242ac110003';
      const mockTournament = {
        id: tournamentId,
        name: 'Test Tournament'
      };

      TournamentUserService.getTournamentByID.mockResolvedValue(mockTournament);
      mockReq.params = { tournamentId };

      // Get the controller function from the array
      const controllerFunction = tournamentController.getTournamentById[1];
      await controllerFunction(mockReq, mockRes, mockNext);

      expect(TournamentUserService.getTournamentByID).toHaveBeenCalledWith(tournamentId);
      expect(sendSuccessResponse).toHaveBeenCalledWith(
        mockRes,
        200,
        'successfully retrieved tournament based on tournament ID',
        mockTournament
      );
      expect(mockNext).not.toHaveBeenCalled();
    });

    it('should return 400 when validation fails', async () => {
      validationResult.mockImplementation(() => ({
        isEmpty: () => false,
        array: () => [{ msg: 'Invalid tournament ID' }]
      }));

      mockReq.params = { tournamentId: 'invalid-id' };

      const controllerFunction = tournamentController.getTournamentById[1];
      await controllerFunction(mockReq, mockRes, mockNext);

      expect(mockNext).toHaveBeenCalledWith(expect.any(Error));
      expect(mockNext.mock.calls[0][0]).toMatchObject({
        message: 'Invalid Tournament ID',
        statusCode: 400,
      });
    });

    it('should return 404 when no tournament is found', async () => {
      const tournamentId = '01433c4a-87aa-11ef-8c7b-0242ac110003';
      TournamentUserService.getTournamentByID.mockResolvedValue(null);
      mockReq.params = { tournamentId };

      const controllerFunction = tournamentController.getTournamentById[1];
      await controllerFunction(mockReq, mockRes, mockNext);

      expect(TournamentUserService.getTournamentByID).toHaveBeenCalledWith(tournamentId);
      expect(mockNext).toHaveBeenCalledWith(expect.any(Error));
      expect(mockNext.mock.calls[0][0]).toMatchObject({
        message: 'No tournament with that id is found',
        statusCode: 404,
      });
    });

    it('should handle errors', async () => {
      const tournamentId = '01433c4a-87aa-11ef-8c7b-0242ac110003';
      const mockError = new Error('Database connection failed');
      TournamentUserService.getTournamentByID.mockRejectedValue(mockError);
      mockReq.params = { tournamentId };

      const controllerFunction = tournamentController.getTournamentById[1];
      await controllerFunction(mockReq, mockRes, mockNext);

      expect(TournamentUserService.getTournamentByID).toHaveBeenCalledWith(tournamentId);
      expect(consoleSpy).toHaveBeenCalledWith('Error retrieving tournament:', mockError);
      expect(mockNext).toHaveBeenCalledWith(mockError);
    });
  });
});