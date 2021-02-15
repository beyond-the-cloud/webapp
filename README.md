# webapp

Repository for a simple web application.

## Webapp with Docker

1. Build the image

```
docker build -t webapp:1.0 .
```

2. Run webapp in local

Make sure you have MySQL database set uo in your local

```
docker run -it -e MYSQL_DB_HOST=host.docker.internal -p 8080:8080 webapp:1.0
```

## User Stories

1. All API request/response payloads should be in JSON.

2. No UI should be implemented for the application.

3. As a user, I expect all APIs call to return with proper HTTP status code.

4. Your web application must only support Token-Based authentication and not Session Authentication.

5. As a user, I must provide basic authentication token when making a API call to `protected/authenticated` endpoint.

6. Create a new user

    1. As a user, I want to create an account by providing following information.
        1. Email Address (username)
        2. Password
        3. First Name
        4. Last Name
    2. `account_created` field for the user should be set to current time when user creation is successful.
    3. User should not be able to set values for `account_created` and `account_updated`. Any value provided for these fields must be ignored.
    4. `Password` field should never be returned in the response payload.
    5. As a user, I expect to use my email address as my username.
    6. Application must return `400 Bad Request` HTTP response code when a user account with the email address already exists.
    7. As a user, I expect my password to be stored securely using BCrypt password hashing scheme with salt.

7. Update user information

    1. As a user, I want to update my account information. I should only be allowed to update following fields.
        1. First Name
        2. Last Name
        3. Password
    2. Attempt to update any other field should return `400 Bad Request` HTTP response code.
    3. `account_updated` field for the user should be updated when user update is successful.
    4. A user can only update their own account information.

8. Get user information

    1. As a user, I want to get my account information. Response payload should return all fields for the user except for `password`.

9. All configuration data for the web application should be passed through environment configuration.

    1. Sensitive configuration information must be passed using Secrets and non-sensitive configuration information should be provided using ConfigMap.

10. Create a top level directory called `deploy` and store all of you Kubernetes manifests in it.
