import authReducer, { authInitilalStates } from "./authReducer"
import layoutReducer, { layoutInitialStates } from "./layoutReducer"

export const initialStates = {
    layoutState: layoutInitialStates,
    authState: authInitilalStates
}

export const rootReducer = (state, action) => {
    return {
        layoutState: layoutReducer(state.layoutState, action),
        authState: authReducer(state.authState, action)
    }
}