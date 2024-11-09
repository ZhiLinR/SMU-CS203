const dotenv = require('dotenv');
const path = require('path');

// Load environment variables from the default .env file
const loadEnvVariables = () => {
    const envFilePath = path.resolve(__dirname, '../.env');

    const result = dotenv.config({ path: envFilePath });

    if (result.error) {
        throw new Error(`Failed to load environment variables from ${envFilePath}`);
    }

    console.log(`Environment variables loaded from ${envFilePath}`);
};

module.exports = loadEnvVariables; // Export function to load environment variables
