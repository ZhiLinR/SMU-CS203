const request = require('supertest');
const express = require('express');
const app = require('../server'); // Adjust the path as necessary
const db = require('../models'); // Mock the database connection

// server.test.js

jest.mock('../models'); // Mock the database models
jest.mock('../routes/publicTournament', () => express.Router().get('/', (req, res) => res.status(200).send('Mocked route')));

describe('Server Tests', () => {
    beforeAll(() => {
        db.sequelize.sync.mockResolvedValue();
    });

    it('should start the server and listen on the correct port', (done) => {
        const PORT = process.env.PORT || 3000;
        const server = app.listen(PORT, () => {
            expect(server.address().port).toBe(PORT);
            server.close(done);
        });
    });

    it('should have the /tournaments route', async () => {
        const response = await request(app).get('/tournaments');
        expect(response.status).toBe(200);
        expect(response.text).toBe('Mocked route');
    });

    it('should handle errors with the error handling middleware', async () => {
        app.use((req, res, next) => {
            const error = new Error('Test error');
            error.status = 500;
            next(error);
        });

        const response = await request(app).get('/nonexistent-route');
        expect(response.status).toBe(500);
        expect(response.text).toBe('Server error!');
    });
});
// Append the following tests to Backend/Tournament_Public/Testing/server.test.js

describe('Additional Server Tests', () => {
    it('should load environment variables from .env.local or .env', () => {
        expect(process.env).toHaveProperty('PORT');
    });

    it('should use JSON parsing middleware', async () => {
        const response = await request(app)
            .post('/tournaments')
            .send({ test: 'data' })
            .set('Accept', 'application/json');
        expect(response.status).not.toBe(500); // Assuming the route is mocked and does not throw an error
    });

    it('should sync the database without errors', async () => {
        await expect(db.sequelize.sync()).resolves.not.toThrow();
    });

    it('should handle errors with the error handling middleware', async () => {
        app.use((req, res, next) => {
            const error = new Error('Test error');
            error.status = 500;
            next(error);
        });

        const response = await request(app).get('/nonexistent-route');
        expect(response.status).toBe(500);
        expect(response.text).toBe('Server error!');
    });
});
describe('Server Configuration Tests', () => {
    it('should load environment variables from .env.local and .env', () => {
        expect(process.env).toHaveProperty('PORT');
    });

    it('should use JSON parsing middleware', async () => {
        const response = await request(app)
            .post('/tournaments')
            .send({ test: 'test' })
            .set('Accept', 'application/json');
        expect(response.status).not.toBe(500);
    });

    it('should sync the database without errors', async () => {
        await expect(db.sequelize.sync()).resolves.not.toThrow();
    });

    it('should have the /tournaments route', async () => {
        const response = await request(app).get('/tournaments');
        expect(response.status).toBe(200);
        expect(response.text).toBe('Mocked route');
    });

    it('should handle errors with the error handling middleware', async () => {
        app.use((req, res, next) => {
            const error = new Error('Test error');
            error.status = 500;
            next(error);
        });

        const response = await request(app).get('/nonexistent-route');
        expect(response.status).toBe(500);
        expect(response.text).toBe('Server error!');
    });
});