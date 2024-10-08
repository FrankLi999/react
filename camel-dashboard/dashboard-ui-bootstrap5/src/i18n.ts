import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import HttpApi, { HttpBackendOptions } from 'i18next-http-backend';
import LanguageDetector from 'i18next-browser-languagedetector';
//Import all translation files
// import English from "./utils/i18n/English.json";
// import French from "./utils/i18n/French.json";

// const resources = {
//     en: {
//         translation: English,
//     },
//     fr: {
//         translation: French,
//     },
// }

i18n
  // load translation using http -> see /public/locales
  // learn more: https://github.com/i18next/i18next-http-backend
  .use(HttpApi)
   // detect user language
   // learn more: https://github.com/i18next/i18next-browser-languageDetector
  .use(LanguageDetector)
  .use(initReactI18next) // passes i18n down to react-i18next
  .init<HttpBackendOptions>({
    // resources,
    backend: {
      loadPath: '/my/camel/i18n/{{lng}}/{{ns}}.json',
    },    
    fallbackLng: 'en',
    debug: true,
    load: 'languageOnly',
    // lng: "en", // language to use, more information here: https://www.i18next.com/overview/configuration-options#languages-namespaces-resources
    // you can use the i18n.changeLanguage function to change the language manually: https://www.i18next.com/overview/api#changelanguage
    // if you're using a language detector, do not define the lng option

    interpolation: {
      escapeValue: false // react already safes from xss
    }
  });

export default i18n;