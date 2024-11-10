const { sendSuccessResponse } = require('../middlewares/successHandler');
const { errorHandler } = require('../middlewares/errorHandler');


/** Controller for health check
 * @async
 * @param {Object} req - The request object.
 * @param {Object} res - The response object.
 * @param {Function} next - The next middleware function.
 * @returns {Json} A JSON response indicating the health of the API.
 * @throws {error} If any error occurs during the health check process.
 */
exports.getHealth = async (req, res, next) => {
    try {


        sendSuccessResponse(res, 200, 'Public Tournament microservice is healthy', {
            timestamp: new Date().toISOString(),
        });
    } catch (err) {
        console.error('Error in health check:', err);
        errorHandler(res, 500, `Error: ${err.message}`, {
            timestamp: new Date().toISOString(),
        });
    }
};