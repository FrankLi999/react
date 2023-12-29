'use client';
import { Dispatch, createContext, useContext, useMemo, useReducer} from 'react';
import { initialStates, rootReducer, RootReducerAction, RootReducerState } from './reducer/rootReducer';

export interface IGlobalContextProps {
    states: RootReducerState;
    dispatch: Dispatch<RootReducerAction>;
}
// Create a new context
export const GlobalStateContext = createContext<IGlobalContextProps>({states: initialStates, dispatch: () => {type: "initial"}});

export const GlobalStateProvider = ({ children }: {
    children: React.ReactNode;
  }) => {

    const [states, dispatch] = useReducer(rootReducer, initialStates);

    const contextValue = useMemo(() => {
        return { states, dispatch };
    }, [states, dispatch]);

    return (
        <GlobalStateContext.Provider value={contextValue}>
            {children}
        </GlobalStateContext.Provider>
    )
}

export const useGlobalStateContext = () => {
    return useContext(GlobalStateContext)
}
