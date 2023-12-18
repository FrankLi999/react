'use client';
import { Dispatch, createContext, useContext, useMemo, useReducer, useState } from 'react';
import { integratorConfigurationDataReducer, integratorConfigurationDataInitialStates } from './reducer/integratorConfigurationDataReducer';

export interface IIntegratorConfigurationDataContextProps {
    states?: any;
    dispatch?: Dispatch<any>;
}
// Create a new context
export const IntegratorConfigurationDataContext = createContext<IIntegratorConfigurationDataContextProps>({});

export const IntegratorConfigurationDataProvider = ({ children }: {
    children: React.ReactNode;
  }) => {

    const [states, dispatch] = useReducer(integratorConfigurationDataReducer, integratorConfigurationDataInitialStates);

    const ContextValue = useMemo(() => {
        return { states, dispatch };
    }, [states, dispatch]);

    return (
        <IntegratorConfigurationDataContext.Provider value={ContextValue}>
            {children}
        </IntegratorConfigurationDataContext.Provider>
    )
}

export const useIntegratorConfigurationDataContext = () => {
    return useContext(IntegratorConfigurationDataContext)
}