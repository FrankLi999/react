import Form, { IChangeEvent } from '@react-jsf/core';
import { GenericObjectType, RJSFSchema, UiSchema } from '@react-jsf/utils';
import localValidator from '@react-jsf/ajv';

interface ValidatorSelectorProps {
  validator: string;
  validators: GenericObjectType;
  select: (validator: string) => void;
}

export default function ValidatorSelector({ validator, validators, select }: ValidatorSelectorProps) {
  const schema: RJSFSchema = {
    type: 'string',
    title: 'Validator',
    enum: Object.keys(validators),
  };

  const uiSchema: UiSchema = {
    'ui:placeholder': 'Select validator',
  };

  return (
    <Form
      className='form_rjsf_validatorSelector'
      idPrefix='rjsf_validatorSelector'
      schema={schema}
      uiSchema={uiSchema}
      formData={validator}
      validator={localValidator}
      onChange={({ formData }: IChangeEvent) => formData && select(formData)}
    >
      <div />
    </Form>
  );
}
