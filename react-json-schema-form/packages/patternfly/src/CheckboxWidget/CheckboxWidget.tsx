import { FocusEvent } from 'react';
import {
  ariaDescribedByIds,
  descriptionId,
  getTemplate,
  labelValue,
  WidgetProps,
  schemaRequiresTrueValue,
  StrictRJSFSchema,
  RJSFSchema,
  FormContextType,
} from '@react-jsf/utils';
import { FormGroup, Checkbox } from '@patternfly/react-core';
export default function CheckboxWidget<
  T = any,
  S extends StrictRJSFSchema = RJSFSchema,
  F extends FormContextType = any
>(props: WidgetProps<T, S, F>) {
  const {
    id,
    value,
    disabled,
    readonly,
    label,
    hideLabel,
    schema,
    autofocus,
    options,
    onChange,
    onBlur,
    onFocus,
    registry,
    uiSchema,
  } = props;
  // Because an unchecked checkbox will cause html5 validation to fail, only add
  // the "required" attribute if the field value must be "true", due to the
  // "const" or "enum" keywords
  const required = schemaRequiresTrueValue<S>(schema);
  const DescriptionFieldTemplate = getTemplate<'DescriptionFieldTemplate', T, S, F>(
    'DescriptionFieldTemplate',
    registry,
    options
  );

  const _onChange = ({ target: { checked } }: FocusEvent<HTMLInputElement>) => onChange(checked);
  const _onBlur = ({ target }: FocusEvent<HTMLInputElement>) => onBlur(id, target && target.checked);
  const _onFocus = ({ target }: FocusEvent<HTMLInputElement>) => onFocus(id, target && target.checked);

  const description = options.description || schema.description;
  return (
    <FormGroup
      className={`checkbox ${disabled || readonly ? 'disabled' : ''}`}
      aria-describedby={ariaDescribedByIds<T>(id)}
    >
      {!hideLabel && !!description && (
        <DescriptionFieldTemplate
          id={descriptionId<T>(id)}
          description={description}
          schema={schema}
          uiSchema={uiSchema}
          registry={registry}
        />
      )}
      <Checkbox
        id={id}
        name={id}
        label={labelValue(label, hideLabel || !label)}
        checked={typeof value === 'undefined' ? false : value}
        isRequired={required}
        isDisabled={disabled || readonly}
        autoFocus={autofocus}
        onChange={() => _onChange}
        onBlur={_onBlur}
        onFocus={_onFocus}
      />
    </FormGroup>
  );
}
