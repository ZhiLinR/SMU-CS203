const db = require('../config/db');  // Import the database connection


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

        // SQL query to fetch names for UUIDs
        const query = 'CALL UserMSVC.GetNamesByUUIDs(?)';
        db.query(query, [uuidList.join(',')], (err, results) => {
            if (err) {
                return reject(err);
            }

            // Convert results into a mapping of UUIDs to names
            const uuidNameMapping = {};
            results[0].forEach((row) => {
                uuidNameMapping[row.UUID] = row.name;
            });

            resolve(uuidNameMapping);
        });
    });
};

