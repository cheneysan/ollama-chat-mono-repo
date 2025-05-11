const API_URL = "http://localhost:8080/api/v1";

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

export const createNewChat = async ({name, message}) => {
    const response = await fetch(`${API_URL}/chat`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            name: name.trim(),
            message: message.trim(),
        })
    });

    if(!response.ok) {
        return await response.json();
    } else {
        throw new Error(await response.text());
    }
}