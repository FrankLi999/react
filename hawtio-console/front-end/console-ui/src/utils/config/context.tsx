import { createContext, useContext, useEffect, useMemo, useState } from 'react';
export const MyConfigContext = createContext({ config: {} });

export const useMyConfigContext = () => {
  return useContext(MyConfigContext);
};

export function MyConfigProvider({ children }: { children: React.ReactNode }) {
  const [config, setConfig] = useState({});
  useEffect(() => {
    // fetch data
    const dataFetch = async () => {
      const data = await (await fetch('/my-camel/admin/api/react-config')).json();
      console.log('myConfig: ', data);
      // set state when the data received
      setConfig(data.config);
    };

    dataFetch();
  }, []);
  const value = useMemo(
    () => ({
      config: {
        ...config,
      },
    }),
    [config]
  );
  return <MyConfigContext.Provider value={value}>{children}</MyConfigContext.Provider>;
}
export default MyConfigProvider;
