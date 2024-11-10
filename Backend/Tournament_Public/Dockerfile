# Use Node.js LTS (Long Term Support) as the base image
FROM node:20-slim

COPY app/ /Tournament_Public_App/
WORKDIR /Tournament_Public_App/

# Install dependencies
RUN npm install --omit=dev

# Expose the port your app runs on
EXPOSE 8080

# Start the application
CMD ["node", "server.js"]