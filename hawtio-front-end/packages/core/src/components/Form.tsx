import { ElementType, FormEvent, ReactNode, useEffect, useState } from 'react';
import {
  createSchemaUtils,
  CustomValidator,
  // deepEquals,
  ErrorSchema,
  ErrorTransformer,
  FormContextType,
  GenericObjectType,
  getTemplate,
  getUiOptions,
  IdSchema,
  isObject,
  mergeObjects,
  NAME_KEY,
  PathSchema,
  StrictRJSFSchema,
  Registry,
  RegistryFieldsType,
  RegistryWidgetsType,
  RJSFSchema,
  RJSFValidationError,
  RJSF_ADDITIONAL_PROPERTIES_FLAG,
  SchemaUtilsType,
  SUBMIT_BTN_OPTIONS_KEY,
  TemplatesType,
  toErrorList,
  UiSchema,
  UI_GLOBAL_OPTIONS_KEY,
  UI_OPTIONS_KEY,
  ValidationData,
  validationDataMerge,
  ValidatorType,
  Experimental_DefaultFormStateBehavior,
} from '@react-jsf/utils';
import _forEach from 'lodash-es/forEach';
import _get from 'lodash-es/get';
import _isEmpty from 'lodash-es/isEmpty';
import _pick from 'lodash-es/pick';
import _toPath from 'lodash-es/toPath';

import getDefaultRegistry from '../getDefaultRegistry';

/** The properties that are passed to the `Form` */
export interface FormProps<T = any, S extends StrictRJSFSchema = RJSFSchema, F extends FormContextType = any> {
  /** The JSON schema object for the form */
  schema: S;
  /** An implementation of the `ValidatorType` interface that is needed for form validation to work */
  validator: ValidatorType<T, S, F>;
  /** The optional children for the form, if provided, it will replace the default `SubmitButton` */
  children?: ReactNode;
  /** The uiSchema for the form */
  uiSchema?: UiSchema<T, S, F>;
  /** The data for the form, used to prefill a form with existing data */
  formData?: T;
  // Form presentation and behavior modifiers
  /** You can provide a `formContext` object to the form, which is passed down to all fields and widgets. Useful for
   * implementing context aware fields and widgets.
   *
   * NOTE: Setting `{readonlyAsDisabled: false}` on the formContext will make the antd theme treat readOnly fields as
   * disabled.
   */
  formContext?: F;
  /** To avoid collisions with existing ids in the DOM, it is possible to change the prefix used for ids;
   * Default is `root`
   */
  idPrefix?: string;
  /** To avoid using a path separator that is present in field names, it is possible to change the separator used for
   * ids (Default is `_`)
   */
  idSeparator?: string;
  /** It's possible to disable the whole form by setting the `disabled` prop. The `disabled` prop is then forwarded down
   * to each field of the form. If you just want to disable some fields, see the `ui:disabled` parameter in `uiSchema`
   */
  disabled?: boolean;
  /** It's possible to make the whole form read-only by setting the `readonly` prop. The `readonly` prop is then
   * forwarded down to each field of the form. If you just want to make some fields read-only, see the `ui:readonly`
   * parameter in `uiSchema`
   */
  readonly?: boolean;
  // Form registry
  /** The dictionary of registered fields in the form */
  fields?: RegistryFieldsType<T, S, F>;
  /** The dictionary of registered templates in the form; Partial allows a subset to be provided beyond the defaults */
  templates?: Partial<Omit<TemplatesType<T, S, F>, 'ButtonTemplates'>> & {
    ButtonTemplates?: Partial<TemplatesType<T, S, F>['ButtonTemplates']>;
  };
  /** The dictionary of registered widgets in the form */
  widgets?: RegistryWidgetsType<T, S, F>;
  // Callbacks
  /** If you plan on being notified every time the form data are updated, you can pass an `onChange` handler, which will
   * receive the same args as `onSubmit` any time a value is updated in the form. Can also return the `id` of the field
   * that caused the change
   */
  onChange?: (data: IChangeEvent<T, S, F>, id?: string) => void;
  /** To react when submitted form data are invalid, pass an `onError` handler. It will be passed the list of
   * encountered errors
   */
  onError?: (errors: RJSFValidationError[]) => void;
  /** You can pass a function as the `onSubmit` prop of your `Form` component to listen to when the form is submitted
   * and its data are valid. It will be passed a result object having a `formData` attribute, which is the valid form
   * data you're usually after. The original event will also be passed as a second parameter
   */
  onSubmit?: (data: IChangeEvent<T, S, F>, event: FormEvent<any>) => void;
  /** Sometimes you may want to trigger events or modify external state when a field has been touched, so you can pass
   * an `onBlur` handler, which will receive the id of the input that was blurred and the field value
   */
  onBlur?: (id: string, data: any) => void;
  /** Sometimes you may want to trigger events or modify external state when a field has been focused, so you can pass
   * an `onFocus` handler, which will receive the id of the input that is focused and the field value
   */
  onFocus?: (id: string, data: any) => void;
  // <form /> HTML attributes
  /** The value of this prop will be passed to the `accept-charset` HTML attribute on the form
   * @deprecated replaced with `acceptCharset` which will supercede this value if both are specified
   */
  acceptcharset?: string;
  /** The value of this prop will be passed to the `accept-charset` HTML attribute on the form */
  acceptCharset?: string;
  /** The value of this prop will be passed to the `action` HTML attribute on the form
   *
   * NOTE: this just renders the `action` attribute in the HTML markup. There is no real network request being sent to
   * this `action` on submit. Instead, react-jsonschema-form catches the submit event with `event.preventDefault()`
   * and then calls the `onSubmit` function, where you could send a request programmatically with `fetch` or similar.
   */
  action?: string;
  /** The value of this prop will be passed to the `autocomplete` HTML attribute on the form */
  autoComplete?: string;
  /** The value of this prop will be passed to the `class` HTML attribute on the form */
  className?: string;
  /** The value of this prop will be passed to the `enctype` HTML attribute on the form */
  enctype?: string;
  /** The value of this prop will be passed to the `id` HTML attribute on the form */
  id?: string;
  /** The value of this prop will be passed to the `name` HTML attribute on the form */
  name?: string;
  /** The value of this prop will be passed to the `method` HTML attribute on the form */
  method?: string;
  /** It's possible to change the default `form` tag name to a different HTML tag, which can be helpful if you are
   * nesting forms. However, native browser form behaviour, such as submitting when the `Enter` key is pressed, may no
   * longer work
   */
  tagName?: ElementType;
  /** The value of this prop will be passed to the `target` HTML attribute on the form */
  target?: string;
  // Errors and validation
  /** Formerly the `validate` prop; Takes a function that specifies custom validation rules for the form */
  customValidate?: CustomValidator<T, S, F>;
  /** This prop allows passing in custom errors that are augmented with the existing JSON Schema errors on the form; it
   * can be used to implement asynchronous validation. By default, these are non-blocking errors, meaning that you can
   * still submit the form when these are the only errors displayed to the user.
   */
  extraErrors?: ErrorSchema<T>;
  /** If set to true, causes the `extraErrors` to become blocking when the form is submitted */
  extraErrorsBlockSubmit?: boolean;
  /** If set to true, turns off HTML5 validation on the form; Set to `false` by default */
  noHtml5Validate?: boolean;
  /** If set to true, turns off all validation. Set to `false` by default
   *
   * @deprecated - In a future release, this switch may be replaced by making `validator` prop optional
   */
  noValidate?: boolean;
  /** If set to true, the form will perform validation and show any validation errors whenever the form data is changed,
   * rather than just on submit
   */
  liveValidate?: boolean;
  /** If `omitExtraData` and `liveOmit` are both set to true, then extra form data values that are not in any form field
   * will be removed whenever `onChange` is called. Set to `false` by default
   */
  liveOmit?: boolean;
  /** If set to true, then extra form data values that are not in any form field will be removed whenever `onSubmit` is
   * called. Set to `false` by default.
   */
  omitExtraData?: boolean;
  /** When this prop is set to `top` or 'bottom', a list of errors (or the custom error list defined in the `ErrorList`) will also
   * show. When set to false, only inline input validation errors will be shown. Set to `top` by default
   */
  showErrorList?: false | 'top' | 'bottom';
  /** A function can be passed to this prop in order to make modifications to the default errors resulting from JSON
   * Schema validation
   */
  transformErrors?: ErrorTransformer<T, S, F>;
  /** If set to true, then the first field with an error will receive the focus when the form is submitted with errors
   */
  focusOnFirstError?: boolean | ((error: RJSFValidationError) => void);
  /** Optional string translation function, if provided, allows users to change the translation of the RJSF internal
   * strings. Some strings contain replaceable parameter values as indicated by `%1`, `%2`, etc. The number after the
   * `%` indicates the order of the parameter. The ordering of parameters is important because some languages may choose
   * to put the second parameter before the first in its translation.
   */
  translateString?: Registry['translateString'];
  /** Optional configuration object with flags, if provided, allows users to override default form state behavior
   * Currently only affecting minItems on array fields and handling of setting defaults based on the value of
   * `emptyObjectFields`
   */
  experimental_defaultFormStateBehavior?: Experimental_DefaultFormStateBehavior;
  // Private
  /**
   * _internalFormWrapper is currently used by the semantic-ui theme to provide a custom wrapper around `<Form />`
   * that supports the proper rendering of those themes. To use this prop, one must pass a component that takes two
   * props: `children` and `as`. That component, at minimum, should render the `children` inside of a <form /> tag
   * unless `as` is provided, in which case, use the `as` prop in place of `<form />`.
   * i.e.:
   * ```
   * export default function InternalForm({ children, as }) {
   *   const FormTag = as || 'form';
   *   return <FormTag>{children}</FormTag>;
   * }
   * ```
   *
   * Use at your own risk as this prop is private and may change at any time without notice.
   */
  _internalFormWrapper?: ElementType;
  /** Support receiving a React formRef to the Form
   */
  // formRef?: Ref<Form<T, S, F>>;
  formRef?: any;
}

/** The data that is contained within the state for the `Form` */
export interface FormState<T = any, S extends StrictRJSFSchema = RJSFSchema, F extends FormContextType = any> {
  /** The JSON schema object for the form */
  schema: S;
  /** The uiSchema for the form */
  uiSchema: UiSchema<T, S, F>;
  /** The `IdSchema` for the form, computed from the `schema`, the `rootFieldId`, the `formData` and the `idPrefix` and
   * `idSeparator` props.
   */
  idSchema: IdSchema<T>;
  /** The schemaUtils implementation used by the `Form`, created from the `validator` and the `schema` */
  schemaUtils: SchemaUtilsType<T, S, F>;
  /** The current data for the form, computed from the `formData` prop and the changes made by the user */
  formData?: T;
  /** Flag indicating whether the form is in edit mode, true when `formData` is passed to the form, otherwise false */
  edit: boolean;
  /** The current list of errors for the form, includes `extraErrors` */
  errors: RJSFValidationError[];
  /** The current errors, in `ErrorSchema` format, for the form, includes `extraErrors` */
  errorSchema: ErrorSchema<T>;
  /** The current list of errors for the form directly from schema validation, does NOT include `extraErrors` */
  schemaValidationErrors: RJSFValidationError[];
  /** The current errors, in `ErrorSchema` format, for the form directly from schema validation, does NOT include
   * `extraErrors`
   */
  schemaValidationErrorSchema: ErrorSchema<T>;
  // Private
  /** @description result of schemaUtils.retrieveSchema(schema, formData). This a memoized value to avoid re calculate at internal functions (getStateFromProps, onChange) */
  retrievedSchema: S;
}


/** The event data passed when changes have been made to the form, includes everything from the `FormState` except
 * the schema validation errors. An additional `status` is added when returned from `onSubmit`
 */
export interface IChangeEvent<T = any, S extends StrictRJSFSchema = RJSFSchema, F extends FormContextType = any>
  extends Omit<FormState<T, S, F>, 'schemaValidationErrors' | 'schemaValidationErrorSchema'> {
  /** The status of the form when submitted */
  status?: 'submitted';
}

/** The `Form` component renders the outer form and all the fields defined in the `schema` */
const Form = <T extends any, S extends StrictRJSFSchema = RJSFSchema, F extends FormContextType = any>(props: FormProps<T, S, F>) => {  
  /** The formRef used to hold the `form` element, this needs to be `any` because `tagName` or `_internalFormWrapper` can
   * provide any possible type here
   */
  if (!props.validator) {
    throw new Error('A validator is required for Form functionality to work');
  }
  const {
    children,
    id,
    idPrefix,
    idSeparator,
    className = '',
    tagName,
    name,
    method,
    target,
    action,
    autoComplete,
    enctype,
    acceptcharset,
    acceptCharset,
    noHtml5Validate = false,
    disabled,
    readonly,
    formContext,
    showErrorList = 'top',
    _internalFormWrapper,
    formRef
  } = props;
  
  const [state, setState] = useState<FormState<T, S, F>>(getStateFromProps(undefined, props, props.formData));
  const { schema, schemaUtils, retrievedSchema, uiSchema, formData, errorSchema, idSchema } = state;
  const registry = getRegistry();
  const { SchemaField: _SchemaField } = registry.fields;

  /**
   * `getSnapshotBeforeUpdate` is a React lifecycle method that is invoked right before the most recently rendered
   * output is committed to the DOM. It enables your component to capture current values (e.g., scroll position) before
   * they are potentially changed.
   *
   * In this case, it checks if the props have changed since the last render. If they have, it computes the next state
   * of the component using `getStateFromProps` method and returns it along with a `shouldUpdate` flag set to `true` IF
   * the `nextState` and `prevState` are different, otherwise `false`. This ensures that we have the most up-to-date
   * state ready to be applied in `componentDidUpdate`.
   *
   * If `formData` hasn't changed, it simply returns an object with `shouldUpdate` set to `false`, indicating that a
   * state update is not necessary.
   *
   * @param prevProps - The previous set of props before the update.
   * @param prevState - The previous state before the update.
   * @returns Either an object containing the next state and a flag indicating that an update should occur, or an object
   *        with a flag indicating that an update is not necessary.
   */
  // function getSnapshotBeforeUpdate(
  //   prevProps: FormProps<T, S, F>,
  //   prevState: FormState<T, S, F>
  // ): { nextState: FormState<T, S, F>; shouldUpdate: true } | { shouldUpdate: false } {
  //   if (!deepEquals(props, prevProps)) {
  //     const isSchemaChanged = !deepEquals(prevProps.schema, props.schema);
  //     const isFormDataChanged = !deepEquals(prevProps.formData, props.formData);
  //     const nextState = getStateFromProps(
  //       props,
  //       props.formData,
  //       // If the `schema` has changed, we need to update the retrieved schema.
  //       // Or if the `formData` changes, for example in the case of a schema with dependencies that need to
  //       //  match one of the subSchemas, the retrieved schema must be updated.
  //       isSchemaChanged || isFormDataChanged ? undefined : state.retrievedSchema,
  //       isSchemaChanged
  //     );
  //     const shouldUpdate = !deepEquals(nextState, prevState);
  //     return { nextState, shouldUpdate };
  //   }
  //   return { shouldUpdate: false };
  // }

  /**
   * `componentDidUpdate` is a React lifecycle method that is invoked immediately after updating occurs. This method is
   * not called for the initial render.
   *
   * Here, it checks if an update is necessary based on the `shouldUpdate` flag received from `getSnapshotBeforeUpdate`.
   * If an update is required, it applies the next state and, if needed, triggers the `onChange` handler to inform about
   * changes.
   *
   * This method effectively replaces the deprecated `UNSAFE_componentWillReceiveProps`, providing a safer alternative
   * to handle prop changes and state updates.
   *
   * @param _ - The previous set of props.
   * @param prevState - The previous state of the component before the update.
   * @param snapshot - The value returned from `getSnapshotBeforeUpdate`.
   */
  useEffect(() => {
    if (props.onChange) {
      props.onChange(state);
    }
  },[state]);
  
  /** Extracts the updated state from the given `props` and `inputFormData`. As part of this process, the
   * `inputFormData` is first processed to add any missing required defaults. After that, the data is run through the
   * validation process IF required by the `props`.
   *
   * @param props - The props passed to the `Form`
   * @param inputFormData - The new or current data for the `Form`
   * @param retrievedSchema - An expanded schema, if not provided, it will be retrieved from the `schema` and `formData`.
   * @param isSchemaChanged - A flag indicating whether the schema has changed.
   * @returns - The new state for the `Form`
   */
  function getStateFromProps(
    currentState: FormState<T, S, F> | undefined,
    localProps: FormProps<T, S, F>,
    inputFormData?: T,
    retrievedSchema?: S,
    isSchemaChanged = false
  ): FormState<T, S, F> {
    const localState: Partial<FormState<T, S, F>> = currentState || {};
    const schema = 'schema' in localProps ? localProps.schema : props.schema;
    const uiSchema: UiSchema<T, S, F> = ('uiSchema' in localProps ? localProps.uiSchema! : props.uiSchema!) || {};
    const edit = typeof inputFormData !== 'undefined';
    const liveValidate = 'liveValidate' in localProps ? localProps.liveValidate : props.liveValidate;
    const mustValidate = edit && !localProps.noValidate && liveValidate;
    const rootSchema = schema;
    const experimental_defaultFormStateBehavior =
      'experimental_defaultFormStateBehavior' in localProps
        ? localProps.experimental_defaultFormStateBehavior
        : props.experimental_defaultFormStateBehavior;
    let schemaUtils: SchemaUtilsType<T, S, F> | undefined = localState.schemaUtils;
    if (
      !schemaUtils ||
      schemaUtils.doesSchemaUtilsDiffer(localProps.validator, rootSchema, experimental_defaultFormStateBehavior)
    ) {
      schemaUtils = createSchemaUtils<T, S, F>(localProps.validator, rootSchema, experimental_defaultFormStateBehavior);
    }
    const formData: T = schemaUtils.getDefaultFormState(schema, inputFormData) as T;
    const _retrievedSchema = retrievedSchema ?? schemaUtils.retrieveSchema(schema, formData);
    const getCurrentErrors = (): ValidationData<T> => {
      // If the `props.noValidate` option is set or the schema has changed, we reset the error state.
      if (localProps.noValidate || isSchemaChanged) {
        return { errors: [], errorSchema: {} };
      } else if (!localProps.liveValidate) {
        return {
          errors: localState.schemaValidationErrors || [],
          errorSchema: localState.schemaValidationErrorSchema || {},
        };
      }
      return {
        errors: localState.errors || [],
        errorSchema: localState.errorSchema || {},
      };
    };

    let errors: RJSFValidationError[];
    let errorSchema: ErrorSchema<T> | undefined;
    let schemaValidationErrors: RJSFValidationError[] = localState.schemaValidationErrors ? localState.schemaValidationErrors : [];
    let schemaValidationErrorSchema: ErrorSchema<T> = localState.schemaValidationErrorSchema ? localState.schemaValidationErrorSchema : {};
    if (mustValidate) {
      const schemaValidation = validate(formData, schema, schemaUtils, _retrievedSchema);
      errors = schemaValidation.errors;
      // If the schema has changed, we do not merge state.errorSchema.
      // Else in the case where it hasn't changed, we merge 'state.errorSchema' with 'schemaValidation.errorSchema.' This done to display the raised field error.
      if (isSchemaChanged) {
        errorSchema = schemaValidation.errorSchema;
      } else {
        errorSchema = mergeObjects(
          currentState && currentState.errorSchema ? currentState.errorSchema : {},
          schemaValidation.errorSchema,
          'preventDuplicates'
        ) as ErrorSchema<T>;
      }
      schemaValidationErrors = errors;
      schemaValidationErrorSchema = errorSchema;
    } else {
      const currentErrors = getCurrentErrors();
      errors = currentErrors.errors;
      errorSchema = currentErrors.errorSchema;
    }
    if (localProps.extraErrors) {
      const merged = validationDataMerge({ errorSchema, errors }, props.extraErrors);
      errorSchema = merged.errorSchema;
      errors = merged.errors;
    }
    const idSchema = schemaUtils.toIdSchema(
      _retrievedSchema,
      uiSchema['ui:rootFieldId'],
      formData,
      localProps.idPrefix,
      localProps.idSeparator
    );
    const nextState: FormState<T, S, F> = {
      schemaUtils,
      schema,
      uiSchema,
      idSchema,
      formData,
      edit,
      errors,
      errorSchema,
      schemaValidationErrors,
      schemaValidationErrorSchema,
      retrievedSchema: _retrievedSchema,
    };
    return nextState;
  }


  /** Validates the `formData` against the `schema` using the `altSchemaUtils` (if provided otherwise it uses the
   * `schemaUtils` in the state), returning the results.
   *
   * @param formData - The new form data to validate
   * @param schema - The schema used to validate against
   * @param altSchemaUtils - The alternate schemaUtils to use for validation
   */
  function validate(
    formData: T | undefined,
    schema = props.schema,
    altSchemaUtils?: SchemaUtilsType<T, S, F>,
    retrievedSchema?: S
  ): ValidationData<T> {
    const schemaUtils = altSchemaUtils ? altSchemaUtils : state.schemaUtils;
    const { customValidate, transformErrors, uiSchema } = props;
    const resolvedSchema = retrievedSchema ?? schemaUtils.retrieveSchema(schema, formData);
    return schemaUtils
      .getValidator()
      .validateFormData(formData, resolvedSchema, customValidate, transformErrors, uiSchema);
  }

  /** Renders any errors contained in the `state` in using the `ErrorList`, if not disabled by `showErrorList`. */
  function renderErrors(registry: Registry<T, S, F>) {
    const { errors, errorSchema, schema, uiSchema } = state;
    const { formContext } = props;
    const options = getUiOptions<T, S, F>(uiSchema);
    const ErrorListTemplate = getTemplate<'ErrorListTemplate', T, S, F>('ErrorListTemplate', registry, options);

    if (errors && errors.length) {
      return (
        <ErrorListTemplate
          errors={errors}
          errorSchema={errorSchema || {}}
          schema={schema}
          uiSchema={uiSchema}
          formContext={formContext}
          registry={registry}
        />
      );
    }
    return null;
  }

  /** Returns the `formData` with only the elements specified in the `fields` list
   *
   * @param formData - The data for the `Form`
   * @param fields - The fields to keep while filtering
   */
  const getUsedFormData = (formData: T | undefined, fields: string[][]): T | undefined => {
    // For the case of a single input form
    if (fields.length === 0 && typeof formData !== 'object') {
      return formData;
    }

    // _pick has incorrect type definition, it works with string[][], because lodash/hasIn supports it
    const data: GenericObjectType = _pick(formData, fields as unknown as string[]);
    if (Array.isArray(formData)) {
      return Object.keys(data).map((key: string) => data[key]) as unknown as T;
    }

    return data as T;
  };

  /** Returns the list of field names from inspecting the `pathSchema` as well as using the `formData`
   *
   * @param pathSchema - The `PathSchema` object for the form
   * @param [formData] - The form data to use while checking for empty objects/arrays
   */
  const getFieldNames = (pathSchema: PathSchema<T>, formData?: T): string[][] => {
    const getAllPaths = (_obj: GenericObjectType, acc: string[][] = [], paths: string[][] = [[]]) => {
      Object.keys(_obj).forEach((key: string) => {
        if (typeof _obj[key] === 'object') {
          const newPaths = paths.map((path) => [...path, key]);
          // If an object is marked with additionalProperties, all its keys are valid
          if (_obj[key][RJSF_ADDITIONAL_PROPERTIES_FLAG] && _obj[key][NAME_KEY] !== '') {
            acc.push(_obj[key][NAME_KEY]);
          } else {
            getAllPaths(_obj[key], acc, newPaths);
          }
        } else if (key === NAME_KEY && _obj[key] !== '') {
          paths.forEach((path) => {
            const formValue = _get(formData, path);
            // adds path to fieldNames if it points to a value
            // or an empty object/array
            if (
              typeof formValue !== 'object' ||
              _isEmpty(formValue) ||
              (Array.isArray(formValue) && formValue.every((val) => typeof val !== 'object'))
            ) {
              acc.push(path);
            }
          });
        }
      });
      return acc;
    };

    return getAllPaths(pathSchema);
  };

  /** Returns the `formData` after filtering to remove any extra data not in a form field
   *
   * @param formData - The data for the `Form`
   * @returns The `formData` after omitting extra data
   */
  const doOmitExtraData = (formData?: T): T | undefined => {
    const { schema, schemaUtils } = state;
    const retrievedSchema = schemaUtils.retrieveSchema(schema, formData);
    const pathSchema = schemaUtils.toPathSchema(retrievedSchema, '', formData);
    const fieldNames = getFieldNames(pathSchema, formData);
    const newFormData = getUsedFormData(formData, fieldNames);
    return newFormData;
  };

  // Filtering errors based on your retrieved schema to only show errors for properties in the selected branch.
  function filterErrorsBasedOnSchema(schemaErrors: ErrorSchema<T>, resolvedSchema?: S, formData?: any): ErrorSchema<T> {
    const { retrievedSchema, schemaUtils } = state;
    const _retrievedSchema = resolvedSchema ?? retrievedSchema;
    const pathSchema = schemaUtils.toPathSchema(_retrievedSchema, '', formData);
    const fieldNames = getFieldNames(pathSchema, formData);
    const filteredErrors: ErrorSchema<T> = _pick(schemaErrors, fieldNames as unknown as string[]);
    // If the root schema is of a primitive type, do not filter out the __errors
    if (resolvedSchema?.type !== 'object' && resolvedSchema?.type !== 'array') {
      filteredErrors.__errors = schemaErrors.__errors;
    }
    // Removing undefined and empty errors.
    const filterUndefinedErrors = (errors: any): ErrorSchema<T> => {
      _forEach(errors, (errorAtKey, errorKey: keyof typeof errors) => {
        if (errorAtKey === undefined) {
          delete errors[errorKey];
        } else if (typeof errorAtKey === 'object' && !Array.isArray(errorAtKey.__errors)) {
          filterUndefinedErrors(errorAtKey);
        }
      });
      return errors;
    };
    return filterUndefinedErrors(filteredErrors);
  }

  /** Function to handle changes made to a field in the `Form`. This handler receives an entirely new copy of the
   * `formData` along with a new `ErrorSchema`. It will first update the `formData` with any missing default fields and
   * then, if `omitExtraData` and `liveOmit` are turned on, the `formData` will be filtered to remove any extra data not
   * in a form field. Then, the resulting formData will be validated if required. The state will be updated with the new
   * updated (potentially filtered) `formData`, any errors that resulted from validation. Finally the `onChange`
   * callback will be called if specified with the updated state.
   *
   * @param formData - The new form data from a change to a field
   * @param newErrorSchema - The new `ErrorSchema` based on the field change
   * @param id - The id of the field that caused the change
   */
  const doOnChange = (formData: T | undefined, newErrorSchema?: ErrorSchema<T>, id?: string) => {
    const { extraErrors, omitExtraData, liveOmit, noValidate, liveValidate, onChange } = props;
    
    if (isObject(formData) || Array.isArray(formData)) {
      const newState = getStateFromProps(state, props, formData, retrievedSchema);
      formData = newState.formData;
    }

    const mustValidate = !noValidate && liveValidate;
    let nextState: Partial<FormState<T, S, F>> = { formData, schema };
    let newFormData = formData;

    let _retrievedSchema: S | undefined;
    if (omitExtraData === true && liveOmit === true) {
      newFormData = doOmitExtraData(formData);
      nextState = {
        formData: newFormData,
      };
    }

    if (mustValidate) {
      const schemaValidation = validate(newFormData, schema, schemaUtils, retrievedSchema);
      let errors = schemaValidation.errors;
      let errorSchema = schemaValidation.errorSchema;
      const schemaValidationErrors = errors;
      const schemaValidationErrorSchema = errorSchema;
      if (extraErrors) {
        const merged = validationDataMerge(schemaValidation, extraErrors);
        errorSchema = merged.errorSchema;
        errors = merged.errors;
      }
      // Merging 'newErrorSchema' into 'errorSchema' to display the custom raised errors.
      if (newErrorSchema) {
        const filteredErrors = filterErrorsBasedOnSchema(newErrorSchema, retrievedSchema, newFormData);
        errorSchema = mergeObjects(errorSchema, filteredErrors, 'preventDuplicates') as ErrorSchema<T>;
      }
      nextState = {
        formData: newFormData,
        errors,
        errorSchema,
        schemaValidationErrors,
        schemaValidationErrorSchema,
      };
    } else if (!noValidate && newErrorSchema) {
      const errorSchema = extraErrors
        ? (mergeObjects(newErrorSchema, extraErrors, 'preventDuplicates') as ErrorSchema<T>)
        : newErrorSchema;
      nextState = {
        formData: newFormData,
        errorSchema: errorSchema,
        errors: toErrorList(errorSchema),
      };
    }
    if (_retrievedSchema) {
      nextState.retrievedSchema = _retrievedSchema;
    }
    setState(() => {return { ...state, ...nextState }});
    onChange && onChange({ ...state, ...nextState }, id)
  };

  /**
   * Callback function to handle reset form data.
   * - Reset all fields with default values.
   * - Reset validations and errors
   *
   */
  // const reset = () => {
  //   const newState = getStateFromProps(state, props, undefined);
  //   const newFormData = newState.formData;
  //   const nextState = {
  //     formData: newFormData,
  //     errorSchema: {},
  //     errors: [] as unknown,
  //     schemaValidationErrors: [] as unknown,
  //     schemaValidationErrorSchema: {},
  //   } as FormState<T, S, F>;

  //   setState(nextState);
  // };

  /** Callback function to handle when a field on the form is blurred. Calls the `onBlur` callback for the `Form` if it
   * was provided.
   *
   * @param id - The unique `id` of the field that was blurred
   * @param data - The data associated with the field that was blurred
   */
  const doOnBlur = (id: string, data: any) => {
    const { onBlur } = props;
    if (onBlur) {
      onBlur(id, data);
    }
  };

  /** Callback function to handle when a field on the form is focused. Calls the `onFocus` callback for the `Form` if it
   * was provided.
   *
   * @param id - The unique `id` of the field that was focused
   * @param data - The data associated with the field that was focused
   */
  const doOnFocus = (id: string, data: any) => {
    const { onFocus } = props;
    if (onFocus) {
      onFocus(id, data);
    }
  };

  /** Callback function to handle when the form is submitted. First, it prevents the default event behavior. Nothing
   * happens if the target and currentTarget of the event are not the same. It will omit any extra data in the
   * `formData` in the state if `omitExtraData` is true. It will validate the resulting `formData`, reporting errors
   * via the `onError()` callback unless validation is disabled. Finally, it will add in any `extraErrors` and then call
   * back the `onSubmit` callback if it was provided.
   *
   * @param event - The submit HTML form event
   */
  const doOnSubmit = (event: FormEvent<any>) => {
    event.preventDefault();
    if (event.target !== event.currentTarget) {
      return;
    }

    event.persist();
    const { omitExtraData, extraErrors, noValidate, onSubmit } = props;
    let { formData: newFormData } = state;

    if (omitExtraData === true) {
      newFormData = doOmitExtraData(newFormData);
    }

    if (noValidate || validateFormWithFormData(newFormData)) {
      // There are no errors generated through schema validation.
      // Check for user provided errors and update state accordingly.
      const errorSchema = extraErrors || {};
      const errors = extraErrors ? toErrorList(extraErrors) : [];
      setState(() => {
        return {
          ...state,
          formData: newFormData,
          errors,
          errorSchema,
          schemaValidationErrors: [],
          schemaValidationErrorSchema: {}
        };
      });
      if (onSubmit) {
        onSubmit({ 
          ...state, 
          formData: newFormData, 
          status: 'submitted' 
        }, event);
      }
    }
  };

  /** Returns the registry for the form */
  function getRegistry(): Registry<T, S, F> {
    const { translateString: customTranslateString, uiSchema = {} } = props;
    const { schemaUtils } = state;
    const { fields, templates, widgets, formContext, translateString } = getDefaultRegistry<T, S, F>();
    return {
      fields: { ...fields, ...props.fields },
      templates: {
        ...templates,
        ...props.templates,
        ButtonTemplates: {
          ...templates.ButtonTemplates,
          ...props.templates?.ButtonTemplates,
        },
      },
      widgets: { ...widgets, ...props.widgets },
      rootSchema: props.schema,
      formContext: props.formContext || formContext,
      schemaUtils,
      translateString: customTranslateString || translateString,
      globalUiOptions: uiSchema[UI_GLOBAL_OPTIONS_KEY],
    };
  }

  // TODO
  /** Provides a function that can be used to programmatically submit the `Form` */
  // const submit = () => {
  //   if (formRef.current) {
  //     const submitCustomEvent = new CustomEvent('submit', {
  //       cancelable: true,
  //     });
  //     submitCustomEvent.preventDefault();
  //     formRef.current.dispatchEvent(submitCustomEvent);
  //     formRef.current.requestSubmit();
  //   }
  // };

  /** Attempts to focus on the field associated with the `error`. Uses the `property` field to compute path of the error
   * field, then, using the `idPrefix` and `idSeparator` converts that path into an id. Then the input element with that
   * id is attempted to be found using the `formRef` ref. If it is located, then it is focused.
   *
   * @param error - The error on which to focus
   */
  function focusOnError(error: RJSFValidationError) {
    const { idPrefix = 'root', idSeparator = '_' } = props;
    const { property } = error;
    const path = _toPath(property);
    if (path[0] === '') {
      // Most of the time the `.foo` property results in the first element being empty, so replace it with the idPrefix
      path[0] = idPrefix;
    } else {
      // Otherwise insert the idPrefix into the first location using unshift
      path.unshift(idPrefix);
    }

    const elementId = path.join(idSeparator);
    // let field = formRef?.current?.elements[elementId];
    // if (!field) {
      // if not an exact match, try finding an input starting with the element id (like radio buttons or checkboxes)
    const field = formRef?.current?.querySelector(`input[id^=${elementId}`) as HTMLElement;
    // }
    // if (field && field.length) {
      // If we got a list with length > 0
    //   field = field[0];
    // }
    if (field) {
      field.focus();
    }
  }

  /** Validates the form using the given `formData`. For use on form submission or on programmatic validation.
   * If `onError` is provided, then it will be called with the list of errors.
   *
   * @param formData - The form data to validate
   * @returns - True if the form is valid, false otherwise.
   */
  const validateFormWithFormData = (formData?: T): boolean => {
    const { extraErrors, extraErrorsBlockSubmit, focusOnFirstError, onError } = props;
    const { errors: prevErrors } = state;
    const schemaValidation = validate(formData);
    let errors = schemaValidation.errors;
    let errorSchema = schemaValidation.errorSchema;
    const schemaValidationErrors = errors;
    const schemaValidationErrorSchema = errorSchema;
    const hasError = errors.length > 0 || (extraErrors && extraErrorsBlockSubmit);
    if (hasError) {
      if (extraErrors) {
        const merged = validationDataMerge(schemaValidation, extraErrors);
        errorSchema = merged.errorSchema;
        errors = merged.errors;
      }
      if (focusOnFirstError) {
        if (typeof focusOnFirstError === 'function') {
          focusOnFirstError(errors[0]);
        } else {
          focusOnError(errors[0]);
        }
      }
      if (onError) {
        onError(errors);
      } else {
        console.error('Form validation failed', errors);
      }
      setState(() => { return {
        ...state,
        errors,
        errorSchema,
        schemaValidationErrors,
        schemaValidationErrorSchema,
      }});
        
      
    } else if (prevErrors.length > 0) {
      setState(() => { return {
        ...state,
        errors: [],
        errorSchema: {},
        schemaValidationErrors: [],
        schemaValidationErrorSchema: {},
      }});
    }
    return !hasError;
  };

  /** Programmatically validate the form.  If `omitExtraData` is true, the `formData` will first be filtered to remove
   * any extra data not in a form field. If `onError` is provided, then it will be called with the list of errors the
   * same way as would happen on form submission.
   *
   * @returns - True if the form is valid, false otherwise.
   */
  // function validateForm() {
  //   const { omitExtraData } = props;
  //   let { formData: newFormData } = state;
  //   if (omitExtraData === true) {
  //     newFormData = doOmitExtraData(newFormData);
  //   }
  //   return validateFormWithFormData(newFormData);
  // }

  // render
  const { SubmitButton } = registry.templates.ButtonTemplates;
  // The `semantic-ui` theme has an `_internalFormWrapper` that take an `as` prop that is the
  // PropTypes.elementType to use for the inner tag, so we'll need to pass `tagName` along if it is provided.
  // NOTE, the `as` prop is native to `semantic-ui`
  const as = _internalFormWrapper ? tagName : undefined;
  const FormTag = _internalFormWrapper || tagName || 'form';
  let { [SUBMIT_BTN_OPTIONS_KEY]: submitOptions = {} } = getUiOptions<T, S, F>(uiSchema);
  if (disabled) {
    submitOptions = { ...submitOptions, props: { ...submitOptions.props, disabled: true } };
  }
  const submitUiSchema = { [UI_OPTIONS_KEY]: { [SUBMIT_BTN_OPTIONS_KEY]: submitOptions } };
  return (
    <FormTag
      className={className ? className : 'rjsf'}
      id={id}
      name={name}
      method={method}
      target={target}
      action={action}
      autoComplete={autoComplete}
      encType={enctype}
      acceptCharset={acceptCharset || acceptcharset}
      noValidate={noHtml5Validate}
      onSubmit={doOnSubmit}
      as={as}
      ref={formRef}
    >
      {showErrorList === 'top' && renderErrors(registry)}
      <_SchemaField
        name=''
        schema={schema}
        uiSchema={uiSchema}
        errorSchema={errorSchema}
        idSchema={idSchema}
        idPrefix={idPrefix}
        idSeparator={idSeparator}
        formContext={formContext}
        formData={formData}
        onChange={doOnChange}
        onBlur={doOnBlur}
        onFocus={doOnFocus}
        registry={registry}
        disabled={disabled}
        readonly={readonly}
      />

      {children ? children : <SubmitButton uiSchema={submitUiSchema} registry={registry} />}
      {showErrorList === 'bottom' && renderErrors(registry)}
    </FormTag>
  );

};
  /** Renders the `Form` fields inside the <form> | `tagName` or `_internalFormWrapper`, rendering any errors if
   * needed along with the submit button or any children of the form.
   */
  // function shouldComponentUpdate(nextProps: FormProps<T, S, F>, nextState: FormState<T, S, F>): boolean {
  //   return !deepEquals(props, nextProps) || !deepEquals(state, nextState);
  // }

// export default React.memo(Form, shouldComponentUpdate);
export default Form;