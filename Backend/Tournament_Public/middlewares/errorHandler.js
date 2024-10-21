/**
 * Middleware to handle errors.
 * 
 * @param {Error} err - The error object.
 * @param {Object} req - The request object.
 * @param {Object} res - The response object.
 * @param {Function} next - The next middleware function.
 */

// Error-handling middleware for Express

const errorHandler = (err, res,) => {
    console.error(err.stack);  

    if (err.sqlState === '45000') {
        return res.status(400).json({ message: err.message });
    }

    const statusCode = err.statusCode || 500;  
    const message = err.message || 'Internal Server Error';  

    res.status(statusCode).json({
        status: 'error',
        message: message,
    });
};

module.exports = errorHandler;
