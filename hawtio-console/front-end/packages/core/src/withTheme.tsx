import { forwardRef } from 'react';
import Form, { FormProps } from './components/Form';
import { FormContextType, RJSFSchema, StrictRJSFSchema } from '@react-jsf/utils';

/** The properties for the `withTheme` function, essentially a subset of properties from the `FormProps` that can be
 * overridden while creating a theme
 */
export type ThemeProps<T = any, S extends StrictRJSFSchema = RJSFSchema, F extends FormContextType = any> = Pick<
  FormProps<T, S, F>,
  'fields' | 'templates' | 'widgets' | '_internalFormWrapper'
>;

/** A Higher-Order component that creates a wrapper around a `Form` with the overrides from the `WithThemeProps` */
export default function withTheme<T = any, S extends StrictRJSFSchema = RJSFSchema, F extends FormContextType = any>(
  themeProps: ThemeProps<T, S, F>
) {  
// ): ComponentType<FormProps<T, S, F>> {
  return forwardRef<HTMLFormElement, FormProps<T, S, F> >(
   //  Warning: Function components cannot be given refs. Attempts to access this ref will fail. Did you mean to use React.forwardRef()?
   //({ fields, widgets, templates, ...directProps }: FormProps<T, S, F>, ref: ForwardedRef<Form<T, S, F>>) => {
   ({ fields, widgets, templates, ...directProps }: FormProps<T, S, F>, ref) => {
      fields = { ...themeProps?.fields, ...fields };
      widgets = { ...themeProps?.widgets, ...widgets };
      templates = {
        ...themeProps?.templates,
        ...templates,
        ButtonTemplates: {
          ...themeProps?.templates?.ButtonTemplates,
          ...templates?.ButtonTemplates,
        },
      };
      console.log(">>>>>>>>>> with theme>>>>>>", directProps);
      return (
        <Form
          {...themeProps}
          {...directProps}
          fields={fields}
          widgets={widgets}
          templates={templates}
          formRef={ref}
        />
      );
    }
  );
}