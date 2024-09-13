from flask import Flask, jsonify, request
import bcrypt
from sqlalchemy import create_engine, text

app = Flask(__name__)

## Login into DB Server as profile User
# Permissions allowed: SELECT, INSERT, UPDATE on UserMSVC Tables
DATABASE_URI = 'mysql+mysqlconnector://your_db_user:your_db_password@your_db_host/your_db_name'
engine = create_engine(DATABASE_URI)


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
        with engine.connect() as conn:
            # Call the stored procedure to insert user and role
            result = conn.execute(
                text('CALL InsertUserAndRole(:email, :password, :is_admin, @status)'),
                {"email": email, "password": password, "is_admin": is_admin}
            )

            # Fetch the output parameter to check if insertion was successful
            result = conn.execute(text('SELECT @status'))
            status = result.scalar()

            if status == 1:
                return jsonify({"message": "User registered successfully"}), 200
            else:
                return jsonify({"message": "Failed to register user"}), 500

    except Exception as e:
        return jsonify({"message": f"Error: {str(e)}"}), 500



@app.route('/login', methods=['POST'])
# Sample Request Body
# {
#     "email": "user@example.com",
#     "password": "hashed_password_here"
# }
def userAuthentication():
    data = request.json

    email = data.get('email')
    password = data.get('password')

    # Validate the request data
    if not email or not password:
        return jsonify({"message": "Email and password are required"}), 400

    try:
        with engine.connect() as conn:
            # Call the stored procedure to get the hashed password
            result = conn.execute(
                text('CALL GetHashedPassword(:email, @hashed_password)'),
                {"email": email}
            )

            # Fetch the output parameter
            result = conn.execute(text('SELECT @hashed_password'))
            hashed_password = result.scalar()

            # Email doesn't exist
            if not hashed_password:
                return jsonify({"message": "Failed", "error": "Invalid email or password"}), 401

            # Check if the provided password matches the stored hashed password
            is_valid = bcrypt.checkpw(password.encode('utf-8'), hashed_password.encode('utf-8'))

            if is_valid:
                # Call the stored procedure to update the last login time
                conn.execute(
                    text('CALL UpdateLastLogin(:email)')
                )
                return jsonify({"message": "Success", "email": email}), 200

            # Password incorrect
            return jsonify({"message": "Failed", "error": "Invalid email or password"}), 401

    except Exception as e:
        return jsonify({"message": f"Error: {str(e)}"}), 500



if __name__ == '__main__':
    app.run(debug=True)
