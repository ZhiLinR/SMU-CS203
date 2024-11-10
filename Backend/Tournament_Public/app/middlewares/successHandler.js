/**
 * Helper function for sending a success response.
 *
 * @param {Object} res - The response object
 * @param {Number} statusCode - The HTTP status code
 * @param {String} message - A success message
 * @param {Object} content - The data/content to be sent in the response
 */
exports.sendSuccessResponse = (res, statusCode, message, content) => {
    res.status(statusCode).json({
        "success": true,
        "status": statusCode,
        "message": message,
        "content": content,
    });
};