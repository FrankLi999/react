'use client'

import Link from 'next/link'
import { useTranslation } from '../../i18n/client'
import { Header } from '../components/Header'
import { Footer } from '../components/Footer/client'
import { useState } from 'react'

export default function Page({ params: { lng } }: {
  params: {
    lng: string;
  };
}) {
  const { t } = useTranslation(lng, 'client-page')
  const [counter, setCounter] = useState(0)
  const handleChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    console.log(">>>>>>> handleChange", e);
    const newLocale = e.target.value;
    console.log(">>>>>>> new locale", newLocale);
    // set cookie for next-i18n-router
    const days = 30;
    const date = new Date();
    date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
    const expires = '; expires=' + date.toUTCString();
    document.cookie = `NEXT_LOCALE=${newLocale};expires=${expires};path=/`;

    // // redirect to the new locale path
    // if (
    //   currentLocale === i18nConfig.defaultLocale &&
    //   !i18nConfig.prefixDefault
    // ) {
    //   console.log(">>>>>>> 1111", i18nConfig)
    //   router.push('/' + newLocale + currentPathname);
    // } else {
    //   console.log(">>>>>>> 22222", currentPathname.replace(`/${currentLocale}`, `/${newLocale}`))
    //   router.push(
    //     currentPathname.replace(`/${currentLocale}`, `/${newLocale}`)
    //   );
    // }

    // router.refresh();
  };
  return (
    <>
      <main>
        <Header heading={t('h1')} />
        <p>{t('counter', { count: counter })}</p>
        <div>
          <button onClick={() => setCounter(Math.max(0, counter - 1))}>-</button>
          <button onClick={() => setCounter(Math.min(10, counter + 1))}>+</button>
        </div>
        <Link href={`/${lng}/second-client-page`}>
          {t('to-second-client-page')}
        </Link>
        <Link href={`/${lng}`}>
          <button type="button">
            {t('back-to-home')}
          </button>
        </Link>
      </main>
      <Footer lng={lng} path="/client-page" />
      <p>
      <select  value={lng} onChange={handleChange }>
        <option value="en">English</option>
        <option value="fr">French</option>
      </select>  
      </p>
    </>
  )
}