import { ChangeEvent, FocusEvent } from 'react';
import { FormGroup, Radio } from '@patternfly/react-core';
import {
  ariaDescribedByIds,
  enumOptionsIsSelected,
  enumOptionsValueForIndex,
  optionId,
  FormContextType,
  RJSFSchema,
  StrictRJSFSchema,
  WidgetProps,
} from '@react-jsf/utils';

export default function RadioWidget<T = any, S extends StrictRJSFSchema = RJSFSchema, F extends FormContextType = any>({
  id,
  options,
  value,
  disabled,
  readonly,
  onChange,
  onBlur,
  onFocus,
}: WidgetProps<T, S, F>) {
  const { enumOptions, enumDisabled, emptyValue } = options;

  const _onChange = ({ target: { value } }: ChangeEvent<HTMLInputElement>) =>
    onChange(enumOptionsValueForIndex<S>(value, enumOptions, emptyValue));
  const _onBlur = ({ target }: FocusEvent<HTMLInputElement>) =>
    onBlur(id, enumOptionsValueForIndex<S>(target && target.value, enumOptions, emptyValue));
  const _onFocus = ({ target }: FocusEvent<HTMLInputElement>) =>
    onFocus(id, enumOptionsValueForIndex<S>(target && target.value, enumOptions, emptyValue));

  const inline = Boolean(options && options.inline);

  return (
    <FormGroup className='pf-v5-u-mb-sm' role="radiogroup" isInline={inline} isStack={!inline}>
      {Array.isArray(enumOptions) &&
        enumOptions.map((option, index) => {
          const itemDisabled = Array.isArray(enumDisabled) && enumDisabled.indexOf(option.value) !== -1;
          const checked = enumOptionsIsSelected<S>(option.value, value);

          const radio = (
            <Radio
              label={option.label}
              id={optionId(id, index)}
              key={index}
              name={id}
              isDisabled={disabled || itemDisabled || readonly}
              checked={checked}
              value={String(index)}
              onChange={() => _onChange}
              onBlur={_onBlur}
              onFocus={_onFocus}
              aria-describedby={ariaDescribedByIds<T>(id)}
            />
          );
          return radio;
        })}
    </FormGroup>
  );
}
