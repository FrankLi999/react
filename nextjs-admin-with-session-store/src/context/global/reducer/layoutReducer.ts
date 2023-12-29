export interface LayoutStateReducerState {
    isSidebarCollapsed: boolean;
    topNavCollapse: boolean;
    dataHover: boolean;
}

export const layoutInitialStates = {
    isSidebarCollapsed: false,
    topNavCollapse: false,
    dataHover: false,
}
export interface LayoutStateReducerAction {
    type: string;
    topNavCollapse?: boolean;
    dataHover?: boolean;
}
const layoutReducer = (state: LayoutStateReducerState = layoutInitialStates, action: LayoutStateReducerAction): LayoutStateReducerState => {
    switch (action.type) {
        case "sidebar_toggle": {
            return {
                ...state,
                isSidebarCollapsed: !state.isSidebarCollapsed
            };
        };
        case 'collapse_sidebar':
            return {
                ...state,
                isSidebarCollapsed: true,
            };
        case 'expand_sidebar':
            return {
                ...state,
                isSidebarCollapsed: false,
            };
        case "top_nav_toggle":
            return {
                ...state,
                topNavCollapse: !state.topNavCollapse
            };
        case "data_hover":
            return {
                ...state,
                dataHover: action.dataHover as boolean
            };
        default:
            return state;
    }
};

export default layoutReducer
