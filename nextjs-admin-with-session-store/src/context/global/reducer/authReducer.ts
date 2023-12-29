
export interface AuthState {
    authenticated: boolean;
}
export interface AuthStateReducerAction {
    type: string
}
export const authInitilalStates: AuthState = {
    authenticated: true
}

const authReducer = (state: AuthState = authInitilalStates, action: AuthStateReducerAction): AuthState => {
    switch (action.type) {
        case "login": {
            return {
                ...state,
                authenticated: true
            };
        };
        case 'logout':
            return {
                ...state,
                authenticated: false,
            };
        default:
            return state;
    }
};

export default authReducer
