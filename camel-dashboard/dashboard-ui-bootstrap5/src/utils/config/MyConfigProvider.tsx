import { useEffect, useMemo, useState } from 'react';
import { MyConfigContext } from './context';

export function MyConfigProvider({ children }) {
  const [config, setConfig] = useState({});
  useEffect(() => {
    // fetch data
    const dataFetch = async () => {
      const data = await (
        await fetch(
          '/api/myconfig',
        )
      ).json();
      console.log("myConfig: ", data);
      // set state when the data received
      setConfig(data.config);
    };

    dataFetch();
  }, []);
  const value = useMemo(
    () => ({
      config: {
        ...config
      }
    }),
    [config],
  );
  return (
    <MyConfigContext.Provider value = {value}>
      {children},
    </MyConfigContext.Provider>
  );
}
export default MyConfigProvider;