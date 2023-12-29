import authReducer, { authInitilalStates, AuthState, AuthStateReducerAction } from "./authReducer"
import layoutReducer, { layoutInitialStates, LayoutStateReducerAction, LayoutStateReducerState } from "./layoutReducer"

export interface RootReducerState {
    layoutState: LayoutStateReducerState;
    authState: AuthState;
}
export const initialStates = {
    layoutState: layoutInitialStates,
    authState: authInitilalStates
}
export type RootReducerAction = LayoutStateReducerAction | AuthStateReducerAction;
export const rootReducer = (state: RootReducerState = initialStates, action: RootReducerAction): RootReducerState => {
    return {
        layoutState: layoutReducer(state.layoutState, action),
        authState: authReducer(state.authState, action)
    }
}
