// Error-handling middleware for Express
const errorHandler = (err, req, res, next) => {
    console.error(err.stack);  // Log the error for debugging purposes


    // Use the statusCode and message passed from the controller (if available)
    const statusCode = err.statusCode || 500;  // Default to 500 if no status code is set
    const message = err.message || 'Internal Server Error';  // Default to a generic error message

    // Send the error response
    res.status(statusCode).json({
        
        message: message,
        success: false,
        content: null
    });
};

module.exports = errorHandler;
