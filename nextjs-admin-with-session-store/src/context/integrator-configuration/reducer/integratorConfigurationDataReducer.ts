// import { ConfigurationModel } from '@/type/ConfigurationModel';

import { ConfigurationModel } from "@/type/ConfigurationModel";

// ConfigurationModel[]
export interface ConfigurationDataState {
    configurations: ConfigurationModel [];
    currentRow: ConfigurationModel | null;
}

export interface ConfigurationDataAction {
    type: string;
    configurations?: ConfigurationModel[];
    currentRow?: ConfigurationModel | null;

}
export const integratorConfigurationDataInitialStates = {
    configurations: [],
    currentRow: null
}

export const integratorConfigurationDataReducer = (state: ConfigurationDataState = integratorConfigurationDataInitialStates, action: ConfigurationDataAction): ConfigurationDataState => {
    switch (action.type) {
        case "set_configurations": {
            return {
                ...state,
                configurations: action.configurations as ConfigurationModel[],
                currentRow: null
            };
        };
        case "set_current_row": {
            return {
                ...state,
                currentRow: action.currentRow as ConfigurationModel
            };
        };
        default:
            return state;
    }
};

// export default integratorConfigurationDataReducer
