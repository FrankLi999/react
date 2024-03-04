
## browser side:
  my/MyBrowserEnv.tsx
    import { GetServerSideProps } from 'next';
    import { createContext, useContext, ReactNode } from 'react';

    export const MyEnvContext = createContext<Record<string, string>>({});

    interface MyEvnProviderProps {
      children: ReactNode;
      browserSideEnv: Record<string, string>;
    }

    export const MyBrowserEnv = ({
      children,
      browserSideEnv,
    }: MyEvnProviderProps) => {
      console.log('>>>>>> >>>> MyBrowserEnv env:', browserSideEnv);
      return (
        <MyEnvContext.Provider value={browserSideEnv}>
          {children}
        </MyEnvContext.Provider>
      );
    };

  pages/_app.tsx:
    MyApp.getInitialProps = async (appContext: AppContext) => {
      const { ctx } = appContext;
      const pageProps = await App.getInitialProps(appContext);

      const browserSideEnv = Object.keys(process.env)
        // could any condition here for vars are to be used at browser side
        .filter((key) => key.endsWith('_URL') || key.startsWith('MY_') || key.startsWith('NEXT_PUBLIC_'))
        .reduce(
          (env, key) => ({
            ...env,
            [key]: process.env[key],
          }),
          {} as NodeJS.ProcessEnv,
        );
        //console.log(">>>>>>resolved  browser side env:", browserSideEnv);

      return { ...pageProps, browserSideEnv };
    };


    function MyApp({ Component, pageProps, browserSideEnv }: AppProps & MyBrowserEnvProps) {
      const {NEXT_PUBLIC_MY_ID} = browserSideEnv;
      return (
        <MyBrowserEnv browserSideEnv = {browserSideEnv}>
        </MyBrowserEnv>

      );
    }
  component:
      import { MyEnvContext } from '@/my/MyBrowserEnv';

      const { MY_OIDC_DDR_ADMIN_GROUP } = useContext(MyEnvContext);

  within function: it cheat nextjs build so that it won't replace process env value at build time.
     utils/request
        export const process_env = (key: string) => {
          return process.env[key];
        };
      within function:
        targetUrl = `${process_env('HOT_SURVEY_BASE_URL')}/my/remote/mf`;
