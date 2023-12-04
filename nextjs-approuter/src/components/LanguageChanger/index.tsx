'use client';

import { useRouter } from 'next/navigation';
import Link from 'next/link'
import { usePathname } from 'next/navigation';
import { useTranslation } from 'react-i18next';
import { i18nConfig } from '@/i18n/i18nConfig';

export default function LanguageChanger() {
  const { i18n } = useTranslation();
  const currentLocale = i18n.language;
  const router = useRouter();
  const currentPathname = usePathname();

  const handleChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const newLocale = e.target.value;
    // set cookie for next-i18n-router
    const days = 30;
    const date = new Date();
    date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
    const expires = '; expires=' + date.toUTCString();
    document.cookie = `NEXT_LOCALE=${newLocale};expires=${expires};path=/`;
    console.log(">>>>>>> i18nConfig", i18nConfig);
    // redirect to the new locale path
    if (
      currentLocale === i18nConfig.defaultLocale &&
      !i18nConfig.prefixDefault
    ) {
      console.log(">>>>>>> 1111", currentPathname)
      router.push('/' + newLocale + currentPathname);
    } else {
      console.log(">>>>>>> 22222", currentPathname.replace(`/${currentLocale}`, `/${newLocale}`))
      router.push(
        currentPathname.replace(`/${currentLocale}`, `/${newLocale}`)
      );
    }
    console.log(">>>>>>> 33333");
    router.refresh();
  };

  return (
      <>
      <select  value={currentLocale} onChange={handleChange }>
        <option value="en">English</option>
        <option value="fr">French</option>
      </select>  
      <ul>
      <li>
        <Link href="/">Home</Link>
      </li>
      <li>
        <Link href="/">English</Link>
      </li>
      <li>
        <Link href="/fr">French</Link>
      </li>
    </ul>
      </>
  );
}