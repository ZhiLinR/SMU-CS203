const getAllowedOrigins = () => {
    // Get origins from environment variable and split into array
    const originsFromEnv = process.env.ALLOWED_ORIGINS?.split(',') || [];
  
    // Filter out empty strings and trim whitespace
    return originsFromEnv
      .filter(origin => origin)
      .map(origin => origin.trim());
  };
  
  const corsMiddleware = (req, res, next) => {
    const origin = req.headers.origin;
    const allowedOrigins = getAllowedOrigins();
  
    // Allow requests with no origin or if origin is in allowed list
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
    } else {
      // Optional: Log denied origins for debugging
      const sanitizedOrigin = origin.replace(/\n|\r/g, "");
      console.warn(`Blocked request from unauthorized origin: ${sanitizedOrigin}`);
      return res.status(403).json({
        error: 'Not allowed by CORS'
      });
    }
  
    next();
  };
  
  module.exports = corsMiddleware;