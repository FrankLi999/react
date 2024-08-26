import Form, { IChangeEvent } from '@react-jsf/core';
import { RJSFSchema, UiSchema } from '@react-jsf/utils';
import localValidator from '@react-jsf/ajv';
import { SubthemesType } from './SubthemeSelector';

export interface ThemesType {
  theme: any;
  stylesheet: string;
  subthemes?: SubthemesType;
}

interface ThemeSelectorProps {
  theme: string;
  themes: { [themeName: string]: ThemesType };
  select: (themeName: string, theme: ThemesType) => void;
}

export default function ThemeSelector({ theme, themes, select }: ThemeSelectorProps) {
  const schema: RJSFSchema = {
    type: 'string',
    title: 'Theme',
    enum: Object.keys(themes),
  };

  const uiSchema: UiSchema = {
    'ui:placeholder': 'Select theme',
  };

  return (
    <Form
      className='form_rjsf_themeSelector'
      idPrefix='rjsf_themeSelector'
      schema={schema}
      uiSchema={uiSchema}
      formData={theme}
      validator={localValidator}
      onChange={({ formData }: IChangeEvent) => formData && select(formData, themes[formData])}
    >
      <div />
    </Form>
  );
}
