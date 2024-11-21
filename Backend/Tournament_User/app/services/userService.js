const db = require('../config/db');  // Import the database connection
const axios = require("axios");
require("dotenv").config(); // Load environment variables

// Base URL of the User MSVC
const USER_MSVC_URL = process.env.USER_MSVC_URL ;

class UserMSVCNames {
    static async getNamesByUUIDs(uuidArray) {
        try {
            const response = await axios.post(`${USER_MSVC_URL}/namelist`, {
                data: uuidArray,
            });

            // Return the mapping dictionary
            return response.data.content;
        } catch (error) {
            console.error('Error fetching names from UserService:', error);
            throw new Error('Failed to fetch names from UserService');
        }
    }
}

module.exports = UserMSVCNames;


exports.getNamesByUUIDs = (uuidInput) => {
    return new Promise((resolve, reject) => {
        // Check if uuidInput is an array or a single value
        let uuidList;

        if (Array.isArray(uuidInput)) {
            // Handle array input directly
            uuidList = uuidInput;
        } else if (typeof uuidInput === 'string') {
            // Handle single UUID (string) by converting it to an array
            uuidList = [uuidInput];
        } else {
            // Reject if uuidInput is not a valid type
            return reject(new Error('uuidInput must be a string or an array'));
        }

        // Return empty mapping if uuidList is empty
        if (uuidList.length === 0) {
            return resolve({});
        }


    });
};

