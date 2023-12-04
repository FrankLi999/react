'use client';
import { createInstance, i18n } from 'i18next';
import { I18nextProvider } from 'react-i18next';
import { i18n } from 'i18next';
import initTranslations from '@/i18n/i18n';
export default function TranslationsProvider({
  children,
  locale,
  namespaces,
  resources
} :{
    children : React.ReactNode,
    locale: string,
    namespaces: string[],
    resources: any
}) {
  const i18nInstance: i18n = createInstance();
  initTranslations(locale, namespaces, i18nInstance, resources);

  return <I18nextProvider i18n={i18nInstance}>{children}</I18nextProvider>;
}