import { Theme as Bootstrap5Theme } from '@react-jsf/bootstrap5';
import { Theme as PatternFlyTheme } from '@react-jsf/patternfly';
import v8Validator, { customizeValidator } from '@react-jsf/ajv';
import localize_es from 'ajv-i18n/localize/es';
import Ajv2019 from 'ajv/dist/2019.js';
import Ajv2020 from 'ajv/dist/2020.js';

import Layout from './layout';
import Playground, { PlaygroundProps } from './components';

// @ts-expect-error todo: error TS2345: Argument of type 'Localize' is not assignable to parameter of type 'Localizer'.
const esV8Validator = customizeValidator({}, localize_es);
const AJV8_2019 = customizeValidator({ AjvClass: Ajv2019 });
const AJV8_2020 = customizeValidator({ AjvClass: Ajv2020 });
const AJV8_DISC = customizeValidator({ ajvOptionsOverrides: { discriminator: true } });

const validators: PlaygroundProps['validators'] = {
  AJV8: v8Validator,
  'AJV8 (discriminator)': AJV8_DISC,
  AJV8_es: esV8Validator,
  AJV8_2019,
  AJV8_2020
};

const themes: PlaygroundProps['themes'] = {
  default: {
    stylesheet: 'https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css',
    theme: Bootstrap5Theme,
  },
  'bootstrap-5': {
    stylesheet: 'https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css',
    theme: Bootstrap5Theme,
  },
  'patternfly': {
    stylesheet: '/css/patternfly.css',
    theme: PatternFlyTheme,
  }
};

export default function App() {
  return (
    <Layout>
      <Playground themes={themes} validators={validators} />
    </Layout>
  );
}
