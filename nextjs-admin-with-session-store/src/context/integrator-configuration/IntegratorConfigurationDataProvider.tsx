'use client';
import { Dispatch, createContext, useContext, useMemo, useReducer } from 'react';
import { integratorConfigurationDataReducer, integratorConfigurationDataInitialStates, ConfigurationDataState, ConfigurationDataAction } from './reducer/integratorConfigurationDataReducer';

export interface IIntegratorConfigurationDataContextProps {
    states: ConfigurationDataState;
    dispatch: Dispatch<ConfigurationDataAction>;
}
// Create a new context
export const IntegratorConfigurationDataContext = createContext<IIntegratorConfigurationDataContextProps>({states: integratorConfigurationDataInitialStates, dispatch: () => {type: "initial"}});

export const IntegratorConfigurationDataProvider = ({ children }: {
    children: React.ReactNode;
  }) => {

    const [states, dispatch] = useReducer(integratorConfigurationDataReducer, integratorConfigurationDataInitialStates);

    const contextValue = useMemo(() => {
        return { states, dispatch };
    }, [states, dispatch]);

    return (
        <IntegratorConfigurationDataContext.Provider value={contextValue}>
            {children}
        </IntegratorConfigurationDataContext.Provider>
    )
}

export const useIntegratorConfigurationDataContext = (): IIntegratorConfigurationDataContextProps => {
    return useContext(IntegratorConfigurationDataContext)
}
