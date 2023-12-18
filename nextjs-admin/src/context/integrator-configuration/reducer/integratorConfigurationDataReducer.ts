// import { ConfigurationModel } from '@/type/ConfigurationModel';
// ConfigurationModel[]
export const integratorConfigurationDataInitialStates = {
    configurations: [],
    currentRow: {}
}

export const integratorConfigurationDataReducer = (state = integratorConfigurationDataInitialStates, action) => {
    switch (action.type) {
        case "set_configurations": {
            return {
                ...state,
                configurations: action.configurations,
                currentRow: {}
            };
        };
        case "set_current_row": {
            return {
                ...state,
                currentRow: action.currentRow
            };
        };
        default:
            return state;
    }
};

// export default integratorConfigurationDataReducer