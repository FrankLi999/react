import { ThemeProps } from '@react-jsf/core';
import { generateTemplates } from '../Templates';
import { generateWidgets } from '../Widgets';
import { FormContextType, RJSFSchema, StrictRJSFSchema } from '@react-jsf/utils';
// import {Form } from '@patternfly/react-core';
export function generateTheme<
  T = any,
  S extends StrictRJSFSchema = RJSFSchema,
  F extends FormContextType = any
>(): ThemeProps<T, S, F> {
  return {
    templates: generateTemplates<T, S, F>(),
    widgets: generateWidgets<T, S, F>(),
    //_internalFormWrapper: Form,
  };
}

export default generateTheme();
