import { ChangeEvent, FocusEvent } from 'react';
import { ariaDescribedByIds, FormContextType, RJSFSchema, StrictRJSFSchema, WidgetProps } from '@react-jsf/utils';
import { FormGroup, TextArea } from '@patternfly/react-core';
type CustomWidgetProps<T = any, S extends StrictRJSFSchema = RJSFSchema, F extends FormContextType = any> = WidgetProps<
  T,
  S,
  F
> & {
  options: any;
};

export default function TextareaWidget<
  T = any,
  S extends StrictRJSFSchema = RJSFSchema,
  F extends FormContextType = any
>({
  id,
  placeholder,
  value,
  required,
  disabled,
  autofocus,
  readonly,
  onBlur,
  onFocus,
  onChange,
  options,
}: CustomWidgetProps<T, S, F>) {
  const _onChange = ({ target: { value } }: ChangeEvent<HTMLTextAreaElement>) =>
    onChange(value === '' ? options.emptyValue : value);
  const _onBlur = ({ target }: FocusEvent<HTMLTextAreaElement>) => onBlur(id, target && target.value);
  const _onFocus = ({ target }: FocusEvent<HTMLTextAreaElement>) => onFocus(id, target && target.value);

  return (
    <FormGroup fieldId={id} >
      <TextArea
        id={id}
        name={id}
        placeholder={placeholder}
        isDisabled={disabled}
        readOnly={readonly}
        value={value}
        isRequired={required}
        autoFocus={autofocus}
        rows={options.rows || 5}
        onChange={() => _onChange}
        onBlur={() => _onBlur}
        onFocus={() => _onFocus}
        aria-describedby={ariaDescribedByIds<T>(id)}
      />
    </FormGroup>
  );
}
