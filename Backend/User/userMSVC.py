from flask import Flask, jsonify, request
import bcrypt

app = Flask(__name__)

## Login into DB Server as profile User
# Permissions allowed: SELECT, INSERT, UPDATE on UserMSVC Tables



# @app.route('/api/hello', methods=['GET'])
# def hello_world():
#     return jsonify({"message": "Hello, World!"})

@app.route('/profile', methods=['POST'])
# Sample Request Body
# {
#     "email": "user@example.com",
#     "password": "hashed_password_here",
#     "isAdmin": 1
# }
def createProfile():
    data = request.json

    email = data.get('email')
    password = data.get('password')
    is_admin = data.get('isAdmin')

    # Validate email and password
    if not email or not password:
        return jsonify({"message": "Email and password are required"}), 400

    # Validate isAdmin
    if is_admin not in (0, 1):
        return jsonify({"message": "Invalid value for isAdmin. It must be 0 or 1."}), 400

    # Convert isAdmin to integer
    is_admin = int(is_admin)

    
    try:
        ## Insert code to insert into database
            ## Insert email, pwd into User
            ## Get UUID, insert UUID and isAdmin into Role
            ## Will return 1 or 0 if successful
            ### USE STORED PROC

        # # Establish a database connection
        # conn = mysql.connector.connect(**db_config)
        # cursor = conn.cursor()

        # # Prepare to call the stored procedure
        # cursor.callproc('InsertUserAndRole', [email, password, is_admin, 0])

        # # Get the status from the output parameter
        # for result in cursor.stored_results():
        #     status = result.fetchall()[0][0]

        status = 1
        
        if status == 1:
            return jsonify({"message": "User registered successfully"}), 200
        else:
            return jsonify({"message": "Failed to register user"}), 500
    
    except Exception as e:
        return jsonify({"message": f"Error: {str(e)}"}), 500



@app.route('/profile', methods=['POST'])
# Sample Request Body
# {
#     "email": "user@example.com",
#     "password": "hashed_password_here"
# }
def userAuthentication():
    data = request.json
    
    # Validate the request data
    if 'email' in data and 'password' in data:
        email = data['email']
        password = data['password']

        ## Insert code to insert into database
            ## Get hashed Password from User

        ##

        # converting password to array of bytes 
        bytes = password.encode('utf-8') 
        
        # check if password is correct
        isValid = bcrypt.checkpw(bytes, hash)

        ## Insert code to insert into database
            ## If successful login, add lastLogin to Role
            ## Use STORED PROC to update lastLogin to current time

        ##
        
        # Here you would normally save the data to a database or perform other actions
        # For demonstration, we just return a success message
        return jsonify({"message": "Success", "email": email}), 201
    else:
        # If email or password is missing, return a failure message
        return jsonify({"message": "Failed", "error": "Missing email or password"}), 400



if __name__ == '__main__':
    app.run(debug=True)
