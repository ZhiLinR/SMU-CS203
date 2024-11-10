/**
 * Error-handling middleware for Express.
 *
 * @param {Error} err - The error object.
 * @param {Object} req - The request object.
 * @param {Object} res - The response object.
 * @param {Function} next - The next middleware function.
 */

const errorHandler = (err, req, res, next) => {


    const statusCode = err.statusCode || 500;
    const message = err.message || 'Internal Server Error';

    // Specific handling for SQL or custom errors
    if (err.sqlState) {
        return res.status(statusCode).json({
            "success": false,
            "status": statusCode,
            "message": `Error: ${message}`,
            "content": null,
        });
    }

    // Default error response
    return res.status(statusCode).json({
        "success": false,
        "status": statusCode,
        "message": message,
        "content": null,
    });
};

module.exports = errorHandler;