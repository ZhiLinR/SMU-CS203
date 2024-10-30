// successHandler.js
const successHandler = (req, res, next) => {
    if (res.locals.data) {
        const { statusCode = 200, message = 'Request successful', success = true, content = null } = res.locals.data;

        // Send a standardized JSON response for successful requests
        res.status(statusCode).json({
            message,
            success,
            content,
        });
    } else {
        // If no success response is set, proceed to the next middleware
        next();
    }
};

module.exports = successHandler;
