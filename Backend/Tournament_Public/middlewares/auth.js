//to be confirmed
const jwt = require('jsonwebtoken');

// Middleware to authenticate user using JWT
function auth(req, res, next) {
    const token = req.header('Authorization');
    if (!token) return res.header(401).json({ message: 'Unauthorized' });

    try {
        const decoded = jwt.verify(token, 'secretkey'); // Replace 'secretkey' with your actual JWT secret
        req.user = decoded;
        next();
    } catch (error) {
        return res.status(401).json({ message: 'Invalid token' });
    }
}

module.exports = auth;