import axios from "axios";
import { createContext, useContext, useEffect, useState } from "react";
import config from "../config";

export interface UserDetails {
    username: string,
    roles: string[],
}
interface AuthContextType {
    isAuthenticated: boolean;
    user: UserDetails | null;
    login: (token: string) => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | null>(null);

interface AuthProviderProps {
    children: React.ReactNode
}

export const AuthProvider: React.FC<AuthProviderProps> = ( {children} ) => {
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
    const [userDetails, setUserDetails] = useState<UserDetails | null>(null);


    const validateTokenAsync = async (token: string) : Promise<{isValid: boolean, user: UserDetails | null}> => {
        const url = `${config.authBaseUrl}/validate-token`;
        try {
            const response = await axios.post(url, { token }, {
            headers: {
                "Content-Type": "application/json",
            },
            });
            if (response.status === 200 && response.data) {
                const {username, roles} = response.data;
                if (username && roles) {
                    return {
                        isValid: true,
                        user: {username, roles} 
                    };
                }
            }
            return {isValid: false, user: null};
        } catch (error) {
            console.error("Error validating token: ", error);
            return { isValid:false, user:null};
        }
    }

    const login = async (token: string) => {
        localStorage.setItem("jwtToken", token);
        const { isValid, user } = await validateTokenAsync(token);
        if (isValid && user) {
            setIsAuthenticated(true);
            setUserDetails(user);
        } else {
            setIsAuthenticated(false);
            setUserDetails(null);
            localStorage.removeItem("jwtToken");
        }
    };

    const logout = () => {
        setIsAuthenticated(false);
        setUserDetails(null);
        localStorage.removeItem("jwtToken");
    };

    useEffect(() => {
        const token = localStorage.getItem("jwtToken");
        console.log("Token: ", token);
        if (token) {
            validateTokenAsync(token).then((result) => {
                setIsAuthenticated(result.isValid);
                setUserDetails(result.user);
                console.log("Token validation: ", result.isValid, result.user);
                if (!result.isValid) {
                    logout();
                }
            })
        }
    }, []);

    return (
        <AuthContext.Provider value= {{ isAuthenticated, user: userDetails, login, logout }}>
                {children}
        </AuthContext.Provider>
    )
}


export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === null) {
        throw new Error("useAuth must be used within AuthProvider");
    }
    return context;
}