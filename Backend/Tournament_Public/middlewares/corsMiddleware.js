const allowedOrigins = [
    'http://localhost:5173',  // Development frontend
    'http://localhost:3000',  // Development backend
  ];
  
  const corsMiddleware = (req, res, next) => {
    const origin = req.headers.origin;
  
    // During development, allow requests with no origin 
    // (like Postman or direct browser requests)
    if (!origin || allowedOrigins.includes(origin)) {
      res.setHeader('Access-Control-Allow-Origin', origin || '*');
      res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
      res.setHeader('Access-Control-Allow-Headers', [
        'Content-Type',
        'Authorization',
        'X-Requested-With',
        'Accept',
        'Origin'
      ].join(', '));
      res.setHeader('Access-Control-Allow-Credentials', 'true');
      res.setHeader('Access-Control-Max-Age', '86400');
      
      // Handle preflight requests
      if (req.method === 'OPTIONS') {
        return res.status(204).end();
      }
    }
  
    next();
  };
  
  module.exports = corsMiddleware;