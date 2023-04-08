import { AppProps } from 'next/app';
import { Provider } from 'react-redux';
import { store } from '@/state/store';
import { appWithTranslation } from 'next-i18next';

import Head from 'next/head';
import BaseLayout from '../components/common/layout';
import GlobalStyle, { fonts } from '../theme/index';

function MyApp({ Component, pageProps }: AppProps) {
  return (
    <>
      <Provider store={store}>
        <Head>
          <meta
            name="viewport"
            content="initial-scale=1.0, width=device-width"
          />
          <meta httpEquiv="X-UA-Compatible" content="IE=edge" />
          <meta name="format-detection" content="telephone=no" />
          <link
            rel="shortcut icon"
            type="image/x-icon"
            href="/images/ontario-favicon.ico"
          />
          <style>{fonts}</style>
        </Head>
        <GlobalStyle />
        <BaseLayout>
          <Component {...pageProps} />
        </BaseLayout>
      </Provider>
    </>
  );
}

export default appWithTranslation(MyApp);
