const API_URL = "http://localhost:8080/api/v1";

/**
 * Registers a new user by sending their details to the authentication service.
 *
 * This function performs a POST request to create a new user account with the
 * provided email, display name, and password. If the operation is unsuccessful,
 * it throws an error containing the response text.
 *
 * @async
 * @function
 * @param {Object} userDetails - The details of the user to register.
 * @param {string} userDetails.email - The email address of the new user. Leading and trailing whitespace is removed.
 * @param {string} userDetails.displayName - The display name of the new user. Leading and trailing whitespace is removed.
 * @param {string} userDetails.password - The password for the new user account.
 * @throws {Error} Throws an error if the server responds with a non-OK status.
 */
export const registerNewUser = async ({email, displayName, password}) => {
    const response = await fetch(`${API_URL}/auth/register`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: email.trim(),
            displayName: displayName.trim(),
            password: password,
        })
    });

    if (!response.ok) {
        throw new Error(await response.text());
    }
}

/**
 * Performs a user sign-in operation by sending a POST request to the authentication endpoint.
 *
 * @async
 * @function signin
 * @param {Object} credentials - User credentials for authentication.
 * @param {string} credentials.email - The email address of the user.
 * @param {string} credentials.password - The password of the user.
 * @returns {Promise<string>} Resolves with an authentication token if the sign-in is successful.
 * @throws {Error} Throws an error if the sign-in request fails.
 */
export const signin = async ({email, password}) => {
    const response = await fetch(`${API_URL}/auth/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: email.trim(),
            password: password,
        })
    });

    if (response.ok) {
        let json = await response.json();
        return json.token;
    } else {
        throw new Error(await response.text());
    }
}

/**
 * Asynchronously creates a new chat by sending a request to the server.
 *
 * @param {string} token - The token for authenticating the request.
 * @param {Object} init - An object containing chat creation details.
 * @param {string} init.name - The name or title of the chat.
 * @param {string} init.message - The initial message of the chat.
 * @returns {Promise<Object>} A promise that resolves to the response data if the request is successful.
 * @throws {Error} Throws an error if the request fails or the server returns an error response.
 */
export const createNewChat = async (token, {name, message}) => {
    const response = await fetch(`${API_URL}/chat`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token,
        },
        body: JSON.stringify({
            title: name.trim(),
            message: message.trim(),
        })
    });

    console.log(response);

    if(response.ok) {
        return await response.json();
    } else {
        throw new Error(await response.text());
    }
}

/**
 * Fetches the chat history from the given API endpoint.
 *
 * The function performs an asynchronous GET request to the `/chat` endpoint using the provided token for authorization.
 * If the request is successful, the chat history is returned as a JSON object.
 * Otherwise, it throws an error with the response text from the server.
 *
 * @param {string} token - The authorization token to be included in the request header.
 * @returns {Promise<Object>} A promise that resolves to the chat history data as a JSON object.
 * @throws {Error} Throws an error if the request fails or the response is not successful.
 */
export const getChatHistory = async (token) => {
    const response = await fetch(`${API_URL}/chat`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token,
        },
    });

    if(response.ok) {
        return await response.json();
    } else {
        throw new Error(await response.text());
    }
}

/**
 * Fetches chat data from the server using the provided token and chat ID.
 *
 * @param {string} token - The authentication token for accessing the API.
 * @param {string} chatId - The unique identifier of the chat to retrieve.
 * @returns {Promise<Object>} A promise that resolves to the chat data in JSON format.
 * @throws {Error} Throws an error if the API response is not successful.
 */
export const getChat = async (token, chatId) => {
    const response = await fetch(`${API_URL}/chat/${chatId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token,
        },
    });

    if (response.ok) {
        return await response.json();
    } else {
        throw new Error(await response.text());
    }
}

/**
 * Sends a message to a specified chat via an API request.
 *
 * @param {string} token - The authentication token used for authorization.
 * @param {Object} params - The parameters for the message.
 * @param {string} params.chatId - The unique identifier of the chat to which the message will be sent.
 * @param {string} params.message - The content of the message to be sent.
 * @returns {Promise<Object>} A promise that resolves to the API response in JSON format if the request is successful.
 * @throws {Error} Throws an error if the API request fails, containing the response text.
 */
export const sendMessage = async (token, {chatId, message}) => {
    const response = await fetch(`${API_URL}/chat/${chatId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token,
        },
        body: JSON.stringify({
            message: message.trim(),
        })
    });

    if (response.ok) {
        return await response.json();
    } else {
        throw new Error(await response.text());
    }
}