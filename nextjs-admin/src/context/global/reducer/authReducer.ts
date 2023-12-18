
export const authInitilalStates = {
    authenticated: true
}

const authReducer = (state = authInitilalStates, action) => {
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