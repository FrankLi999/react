'use client';
import { Dispatch, createContext, useContext, useMemo, useReducer, useState } from 'react';
import { initialStates, rootReducer } from './reducer/rootReducer';
// import { GlobalReducer, initialStates } from './GlobalReducer';

export interface IGlobalContextProps {
    states?: any;
    dispatch?: Dispatch<any>;
}
// Create a new context
export const GlobalStateContext = createContext<IGlobalContextProps>({});

export const GlobalStateProvider = ({ children }: {
    children: React.ReactNode;
  }) => {

    const [states, dispatch] = useReducer(rootReducer, initialStates);

    const ContextValue = useMemo(() => {
        return { states, dispatch };
    }, [states, dispatch]);

    return (
        <GlobalStateContext.Provider value={ContextValue}>
            {children}
        </GlobalStateContext.Provider>
    )
}

export const useGlobalStateContext = () => {
    return useContext(GlobalStateContext)
}