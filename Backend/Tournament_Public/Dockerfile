# Use Node.js LTS (Long Term Support) as the base image
FROM node:20-slim

# Set working directory
WORKDIR /Backend/Tournament_Public

# Copy package files
COPY package*.json ./

# Install dependencies
# npm ci is preferred for production as it's more reliable than npm install
RUN npm ci --only=production

# Copy application source code
COPY . .

# Create a non-root user for security
RUN addgroup --system --gid 1001 nodejs
RUN adduser --system --uid 1001 microservice
RUN chown -R microservice:nodejs /Backend/Tournament_Public
USER microservice

# Expose the port your app runs on
EXPOSE 3000

# Define environment variables
ENV NODE_ENV=production

# Start the application
CMD ["node", "server.js"]