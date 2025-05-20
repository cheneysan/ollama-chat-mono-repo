import React, {useState} from "react";
import {useNavigate} from "react-router";

let AuthContext = React.createContext(null);

export function AuthProvider({children}) {
    let [token, setToken] = useState(null);
    let navigate = useNavigate();

    const signin = (newToken, callback) => {
        setToken(newToken);
        callback();
    }

    const signout = () => {
        setToken(null);
        navigate('/signin');
    }

    let value = { token, signin, signout };

    return <AuthContext.Provider value={value}>{ children }</AuthContext.Provider>;
}

export function useAuth() {
    return React.useContext(AuthContext);
}