# Use Node.js LTS (Long Term Support) as the base image
FROM node:20-slim

COPY TournamentUserMVSC/ /Tournament_User_App/
WORKDIR /Tournament_User_App/

# Install dependencies
RUN npm install --omit=dev

# Expose the port your app runs on
EXPOSE 8080

# Start the application
CMD ["node", "server.js"]