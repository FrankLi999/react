import { createContext,  useContext} from 'react';
export const MyConfigContext = createContext({config: {}});

export const useMyConfigContext = () => {
  return useContext(MyConfigContext);
}