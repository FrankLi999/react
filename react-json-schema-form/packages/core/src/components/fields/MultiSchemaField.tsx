import { useState, useEffect } from 'react';
import get from 'lodash-es/get';
import isEmpty from 'lodash-es/isEmpty';
import omit from 'lodash-es/omit';
import {
  ANY_OF_KEY,
  deepEquals,
  ERRORS_KEY,
  FieldProps,
  FormContextType,
  getDiscriminatorFieldFromSchema,
  getUiOptions,
  getWidget,
  mergeSchemas,
  ONE_OF_KEY,
  RJSFSchema,
  StrictRJSFSchema,
  TranslatableString,
  UiSchema,
} from '@react-jsf/utils';

/** Type used for the state of the `AnyOfField` component */
type AnyOfFieldState<S extends StrictRJSFSchema = RJSFSchema> = {
  /** The currently selected option */
  selectedOption: number;
  /** The option schemas after retrieving all $refs */
  retrievedOptions: S[];
};

function AnyOfField<T = any, S extends StrictRJSFSchema = RJSFSchema, F extends FormContextType = any>(
  props: FieldProps<T, S, F> & AnyOfFieldState<S>
) {
  const {
    name,
    disabled = false,
    errorSchema = {},
    formContext,
    onBlur,
    onFocus,
    registry,
    schema,
    uiSchema,
    formData,
    options,
    idSchema
  } = props;

  const { widgets, fields, translateString, globalUiOptions, schemaUtils } = registry;
  const { SchemaField: _SchemaField } = fields;
  
  
  const retrievedOptionsFromProps = options.map((opt: S) => schemaUtils.retrieveSchema(opt, formData));
  const [state, setState] = useState<AnyOfFieldState<S>>({
     // cache the retrieved options in state in case they have $refs to save doing it later
    retrievedOptions: retrievedOptionsFromProps,
    selectedOption: getMatchingOption(0, formData, retrievedOptionsFromProps)
  });
  const [prevState, setPrevState] = useState({
    state,
    options,
    formData,
    idSchema
  });
  const { selectedOption, retrievedOptions } = state;
  /** React lifecycle method that is called when the props and/or state for this component is updated. It recomputes the
   * currently selected option based on the overall `formData`
   *
   * @param prevProps - The previous `FieldProps` for this template
   * @param prevState - The previous `AnyOfFieldState` for this template
   */
  useEffect(() => {
  // componentDidUpdate(prevProps: Readonly<FieldProps<T, S, F>>, prevState: Readonly<AnyOfFieldState>) {
    const { selectedOption } = state;
    let newState = state;
    if (!deepEquals(prevState.options, options)) {
      // re-cache the retrieved options in state in case they have $refs to save doing it later
      const retrievedOptions = options.map((opt: S) => schemaUtils.retrieveSchema(opt, formData));
      newState = { selectedOption, retrievedOptions };
    }
    if (!deepEquals(formData, prevState.formData) && idSchema.$id === prevState.idSchema.$id) {
      const { retrievedOptions } = newState;
      const matchingOption = getMatchingOption(selectedOption, formData, retrievedOptions);

      if (prevState && matchingOption !== selectedOption) {
        newState = { selectedOption: matchingOption, retrievedOptions };
      }
    }
    if (newState !== state) {
      setState(newState);
    }
  });

  /** Determines the best matching option for the given `formData` and `options`.
   *
   * @param formData - The new formData
   * @param options - The list of options to choose from
   * @return - The index of the `option` that best matches the `formData`
   */
  function getMatchingOption(selectedOption: number, formData: T | undefined, options: S[]) {
    const {
      schema,
      registry: { schemaUtils },
    } = props;

    const discriminator = getDiscriminatorFieldFromSchema<S>(schema);
    const option = schemaUtils.getClosestMatchingOption(formData, options, selectedOption, discriminator);
    return option;
  }

  /** Callback handler to remember what the currently selected option is. In addition to that the `formData` is updated
   * to remove properties that are not part of the newly selected option schema, and then the updated data is passed to
   * the `onChange` handler.
   *
   * @param option - The new option value being selected
   */
  const onOptionChange = (option?: string) => {
    const { selectedOption, retrievedOptions } = state;
    const { formData, onChange, registry } = props;
    const { schemaUtils } = registry;
    const intOption = option !== undefined ? parseInt(option, 10) : -1;
    if (intOption === selectedOption) {
      return;
    }
    const newOption = intOption >= 0 ? retrievedOptions[intOption] : undefined;
    const oldOption = selectedOption >= 0 ? retrievedOptions[selectedOption] : undefined;

    let newFormData = schemaUtils.sanitizeDataForNewSchema(newOption, oldOption, formData);
    if (newFormData && newOption) {
      // Call getDefaultFormState to make sure defaults are populated on change. Pass "excludeObjectChildren"
      // so that only the root objects themselves are created without adding undefined children properties
      newFormData = schemaUtils.getDefaultFormState(newOption, newFormData, 'excludeObjectChildren') as T;
    }
    onChange(newFormData, undefined, getFieldId());

    setState({ 
      ...state,
      selectedOption: intOption 
    });
  };

  function getFieldId() {
    const { idSchema, schema } = props;
    return `${idSchema.$id}${schema.oneOf ? '__oneof_select' : '__anyof_select'}`;
  }

  const {
    widget = 'select',
    placeholder,
    autofocus,
    autocomplete,
    title = schema.title,
    ...uiOptions
  } = getUiOptions<T, S, F>(uiSchema, globalUiOptions);
  const Widget = getWidget<T, S, F>({ type: 'number' }, widget, widgets);
  const rawErrors = get(errorSchema, ERRORS_KEY, []);
  const fieldErrorSchema = omit(errorSchema, [ERRORS_KEY]);
  const displayLabel = schemaUtils.getDisplayLabel(schema, uiSchema, globalUiOptions);

  const option = selectedOption >= 0 ? retrievedOptions[selectedOption] || null : null;
  let optionSchema: S | undefined | null;

  if (option) {
    // merge top level required field
    const { required } = schema;
    // Merge in all the non-oneOf/anyOf properties and also skip the special ADDITIONAL_PROPERTY_FLAG property
    optionSchema = required ? (mergeSchemas({ required }, option) as S) : option;
  }

  // First we will check to see if there is an anyOf/oneOf override for the UI schema
  let optionsUiSchema: UiSchema<T, S, F>[] = [];
  if (ONE_OF_KEY in schema && uiSchema && ONE_OF_KEY in uiSchema) {
    if (Array.isArray(uiSchema[ONE_OF_KEY])) {
      optionsUiSchema = uiSchema[ONE_OF_KEY];
    } else {
      console.warn(`uiSchema.oneOf is not an array for "${title || name}"`);
    }
  } else if (ANY_OF_KEY in schema && uiSchema && ANY_OF_KEY in uiSchema) {
    if (Array.isArray(uiSchema[ANY_OF_KEY])) {
      optionsUiSchema = uiSchema[ANY_OF_KEY];
    } else {
      console.warn(`uiSchema.anyOf is not an array for "${title || name}"`);
    }
  }
  // Then we pick the one that matches the selected option index, if one exists otherwise default to the main uiSchema
  let optionUiSchema = uiSchema;
  if (selectedOption >= 0 && optionsUiSchema.length > selectedOption) {
    optionUiSchema = optionsUiSchema[selectedOption];
  }

  const translateEnum: TranslatableString = title
    ? TranslatableString.TitleOptionPrefix
    : TranslatableString.OptionPrefix;
  const translateParams = title ? [title] : [];
  const enumOptions = retrievedOptions.map((opt: { title?: string }, index: number) => {
    // Also see if there is an override title in the uiSchema for each option, otherwise use the title from the option
    const { title: uiTitle = opt.title } = getUiOptions<T, S, F>(optionsUiSchema[index]);
    return {
      label: uiTitle || translateString(translateEnum, translateParams.concat(String(index + 1))),
      value: index,
    };
  });

  return (
    <div className='panel panel-default panel-body'>
      <div className='form-group'>
        <Widget
          id={getFieldId()}
          name={`${name}${schema.oneOf ? '__oneof_select' : '__anyof_select'}`}
          schema={{ type: 'number', default: 0 } as S}
          onChange={onOptionChange}
          onBlur={onBlur}
          onFocus={onFocus}
          disabled={disabled || isEmpty(enumOptions)}
          multiple={false}
          rawErrors={rawErrors}
          errorSchema={fieldErrorSchema}
          value={selectedOption >= 0 ? selectedOption : undefined}
          options={{ enumOptions, ...uiOptions }}
          registry={registry}
          formContext={formContext}
          placeholder={placeholder}
          autocomplete={autocomplete}
          autofocus={autofocus}
          label={title ?? name}
          hideLabel={!displayLabel}
        />
      </div>
      {optionSchema && <_SchemaField {...props} schema={optionSchema} uiSchema={optionUiSchema} />}
    </div>
  );
  
}

export default AnyOfField;
