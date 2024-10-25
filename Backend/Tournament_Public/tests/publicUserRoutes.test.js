const httpMocks = require('node-mocks-http');
const tournamentController = require('../controllers/publicUserController');
const TournamentUserService = require('../userService/userService');

// Mock TournamentUserService methods
jest.mock('../userService/userService');

describe('Tournament Controller', () => {
    let req, res, next;

    beforeEach(() => {
        req = httpMocks.createRequest();
        res = httpMocks.createResponse();
        next = jest.fn();
    });

    describe('getAllTournaments', () => {
        it('should return all tournaments with a 200 status', async () => {
            const tournaments = [{ id: '1', name: 'Tournament A' }];
            TournamentUserService.getAllTournaments.mockResolvedValue(tournaments);

            await tournamentController.getAllTournaments(req, res, next);

            expect(res.statusCode).toBe(200);
            expect(res._getJSONData()).toEqual({
                success: true,
                status: 200,
                message: "successfully retrieved all tournaments",
                content: tournaments
            });
        });

        it('should return 404 if no tournaments found', async () => {
            TournamentUserService.getAllTournaments.mockResolvedValue([]);

            await tournamentController.getAllTournaments(req, res, next);

            expect(next).toHaveBeenCalledWith(expect.objectContaining({
                message: 'No tournaments retrieved',
                statusCode: 404
            }));
        });

        it('should handle errors and call next with error', async () => {
            const errorMessage = 'Database error';
            TournamentUserService.getAllTournaments.mockRejectedValue(new Error(errorMessage));

            await tournamentController.getAllTournaments(req, res, next);

            expect(next).toHaveBeenCalledWith(expect.objectContaining({
                message: errorMessage
            }));
        });
    });

    describe('getUpcomingTournaments', () => {
        it('should return upcoming tournaments with a 200 status', async () => {
            const tournaments = [{ id: '2', name: 'Upcoming Tournament' }];
            TournamentUserService.getUpcomingTournaments.mockResolvedValue(tournaments);

            await tournamentController.getUpcomingTournaments(req, res, next);

            expect(res.statusCode).toBe(200);
            expect(res._getJSONData()).toEqual({
                success: true,
                status: 200,
                message: "successfully retrieved upcoming tournaments",
                content: tournaments
            });
        });

        it('should return 404 if no upcoming tournaments found', async () => {
            TournamentUserService.getUpcomingTournaments.mockResolvedValue([]);

            await tournamentController.getUpcomingTournaments(req, res, next);

            expect(next).toHaveBeenCalledWith(expect.objectContaining({
                message: 'No upcoming tournaments',
                statusCode: 404
            }));
        });

        it('should handle errors and call next with error', async () => {
            const errorMessage = 'Service error';
            TournamentUserService.getUpcomingTournaments.mockRejectedValue(new Error(errorMessage));

            await tournamentController.getUpcomingTournaments(req, res, next);

            expect(next).toHaveBeenCalledWith(expect.objectContaining({
                message: errorMessage
            }));
        });
    });

    describe('getInProgressTournaments', () => {
        it('should return in-progress tournaments with a 200 status', async () => {
            const tournaments = [{ id: '3', name: 'In-Progress Tournament' }];
            TournamentUserService.getInProgressTournaments.mockResolvedValue(tournaments);

            await tournamentController.getInProgressTournaments(req, res, next);

            expect(res.statusCode).toBe(200);
            expect(res._getJSONData()).toEqual({
                success: true,
                status: 200,
                message: "successfully retrieved in-progress tournaments",
                content: tournaments
            });
        });

        it('should return 404 if no in-progress tournaments found', async () => {
            TournamentUserService.getInProgressTournaments.mockResolvedValue([]);

            await tournamentController.getInProgressTournaments(req, res, next);

            expect(next).toHaveBeenCalledWith(expect.objectContaining({
                message: 'No in-progress tournaments',
                statusCode: 404
            }));
        });

        it('should handle errors and call next with error', async () => {
            const errorMessage = 'Unexpected error';
            TournamentUserService.getInProgressTournaments.mockRejectedValue(new Error(errorMessage));

            await tournamentController.getInProgressTournaments(req, res, next);

            expect(next).toHaveBeenCalledWith(expect.objectContaining({
                message: errorMessage
            }));
        });
    });

    describe('getCompletedTournaments', () => {
        it('should return completed tournaments with a 200 status', async () => {
            const tournaments = [{ id: '4', name: 'Completed Tournament' }];
            TournamentUserService.getCompletedTournaments.mockResolvedValue(tournaments);

            await tournamentController.getCompletedTournaments(req, res, next);

            expect(res.statusCode).toBe(200);
            expect(res._getJSONData()).toEqual({
                success: true,
                status: 200,
                message: "successfully retrieved completed tournaments",
                content: tournaments
            });
        });

        it('should return 404 if no completed tournaments found', async () => {
            TournamentUserService.getCompletedTournaments.mockResolvedValue([]);

            await tournamentController.getCompletedTournaments(req, res, next);

            expect(next).toHaveBeenCalledWith(expect.objectContaining({
                message: 'No completed tournaments',
                statusCode: 404
            }));
        });

        it('should handle errors and call next with error', async () => {
            const errorMessage = 'Error retrieving data';
            TournamentUserService.getCompletedTournaments.mockRejectedValue(new Error(errorMessage));

            await tournamentController.getCompletedTournaments(req, res, next);

            expect(next).toHaveBeenCalledWith(expect.objectContaining({
                message: errorMessage
            }));
        });
    });

    describe('getTournamentById', () => {
        it('should return tournament by ID with a 200 status', async () => {
            const tournament = { id: '5', name: 'Specific Tournament' };
            req.params.tournamentId = '5';
            TournamentUserService.getTournamentByID.mockResolvedValue(tournament);

            await tournamentController.getTournamentById(req, res, next);

            expect(res.statusCode).toBe(200);
            expect(res._getJSONData()).toEqual({
                success: true,
                status: 200,
                message: "successfully retrieved tournament based on tournament ID",
                content: tournament
            });
        });

        it('should return 404 if tournament by ID is not found', async () => {
            req.params.tournamentId = '6';
            TournamentUserService.getTournamentByID.mockResolvedValue(null);

            await tournamentController.getTournamentById(req, res, next);

            expect(next).toHaveBeenCalledWith(expect.objectContaining({
                message: 'No tournament with that id is found',
                statusCode: 404
            }));
        });

        it('should handle errors and return a 500 status', async () => {
            const errorMessage = 'Error fetching tournament';
            req.params.tournamentId = '7';
            TournamentUserService.getTournamentByID.mockRejectedValue(new Error(errorMessage));

            await tournamentController.getTournamentById(req, res, next);

            expect(res.statusCode).toBe(500);
            expect(res._getJSONData()).toEqual({
                message: 'Error fetching tournament',
                error: errorMessage
            });
        });
    });
});
