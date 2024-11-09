# Use an official Node.js image
FROM node:18-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application
COPY . .

# Expose the port your application listens on
EXPOSE 32781

# Default command (can be overridden in docker-compose.yml)
CMD ["node", "server.js"]
