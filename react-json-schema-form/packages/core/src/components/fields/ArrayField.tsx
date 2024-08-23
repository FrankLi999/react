import { MouseEvent, useState, useEffect, ComponentType } from 'react';
import {
  getTemplate,
  getWidget,
  getUiOptions,
  isFixedItems,
  allowAdditionalItems,
  isCustomWidget,
  optionsList,
  ArrayFieldTemplateProps,
  ErrorSchema,
  FieldProps,
  FormContextType,
  IdSchema,
  RJSFSchema,
  StrictRJSFSchema,
  TranslatableString,
  UiSchema,
  ITEMS_KEY,
  Registry,
  ArrayFieldTemplateItemType,
} from '@react-jsf/utils';
import cloneDeep from 'lodash-es/cloneDeep';
import get from 'lodash-es/get';
import isObject from 'lodash-es/isObject';
import set from 'lodash-es/set';
import { nanoid } from 'nanoid';

/** Type used to represent the keyed form data used in the state */
type KeyedFormDataType<T> = { key: string; item: T };

/** Type used for the state of the `ArrayField` component */
type ArrayFieldState<T> = {
  /** The keyed form data elements */
  keyedFormData: KeyedFormDataType<T>[];
  /** Flag indicating whether any of the keyed form data has been updated */
  updatedKeyedFormData: boolean;
  index: number | undefined;
  newIndex: number | undefined;
  operation: 'add' | 'drop' | 'copy' | 'reorder'| undefined;
};

/** Used to generate a unique ID for an element in a row */
function generateRowId() {
  return nanoid();
}

/** Converts the `formData` into `KeyedFormDataType` data, using the `generateRowId()` function to create the key
 *
 * @param formData - The data for the form
 * @returns - The `formData` converted into a `KeyedFormDataType` element
 */
function generateKeyedFormData<T>(formData: T[]): KeyedFormDataType<T>[] {
  return !Array.isArray(formData)
    ? []
    : formData.map((item) => {
        return {
          key: generateRowId(),
          item,
        };
      });
}

/** Converts `KeyedFormDataType` data into the inner `formData`
 *
 * @param keyedFormData - The `KeyedFormDataType` to be converted
 * @returns - The inner `formData` item(s) in the `keyedFormData`
 */
function keyedToPlainFormData<T>(keyedFormData: KeyedFormDataType<T> | KeyedFormDataType<T>[]): T[] {
  if (Array.isArray(keyedFormData)) {
    return keyedFormData.map((keyedItem) => keyedItem.item);
  }
  return [];
}

// TODO: replace it with functional component
// getDerivedStateFromProps
function ArrayField<T = any, S extends StrictRJSFSchema = RJSFSchema, F extends FormContextType = any>(
  props: FieldProps<T, S, F> & ArrayFieldTemplateProps<T, S, F>
) {
  // const formData  = props.formData as T[] ;
  //   /** Renders the `ArrayField` depending on the specific needs of the schema and uischema elements
  //    */
  const { formData = [], onChange, errorSchema, schema, uiSchema, idSchema, registry, disabled, hideError, idPrefix, idSeparator, readonly, formContext } = props;
  const { schemaUtils, translateString, globalUiOptions } = registry;

  const [state, setState] = useState<ArrayFieldState<T>>({
    keyedFormData: generateKeyedFormData<T>(formData as T[]),
    updatedKeyedFormData: false,
    index: undefined,
    newIndex: undefined,
    operation: undefined
  });
  const [prevState, setPrevState] = useState<ArrayFieldState<T>>(state);
  
  const { keyedFormData } = state;

  // TODO
  useEffect(() => {

    // refs #195: revalidate to ensure properly reindexing errors
    let newErrorSchema: ErrorSchema<T> = {};
    if (errorSchema) {
      for (const idx in errorSchema) {
        const i = parseInt(idx);
        if ('add' === state.operation) {
          if (state.index === undefined || i < state.index) {
            set(newErrorSchema, [i], get(errorSchema, [idx]));
          } else if (i >= state.index) {
            set(newErrorSchema, [i + 1], get(errorSchema, [idx]));
          }
        } else if ('drop' === state.operation) {
          for (const idx in errorSchema) {
            const i = parseInt(idx);
            if (i < (state.index as number)) {
              set(newErrorSchema, [i],  get(errorSchema, [idx]));
            } else if (i > (state.index as number)) {
              set(newErrorSchema, [i - 1],  get(errorSchema, [idx]));
            }
          }
        } else if ('copy' === state.operation) {
          for (const idx in errorSchema) {
            const i = parseInt(idx);
            if (i <= (state.index as number)) {
              set(newErrorSchema, [i], get(errorSchema, [idx]));
            } else if (i > (state.index as number)) {
              set(newErrorSchema, [i + 1], get(errorSchema, [idx]));
            }
          }
          
        } else if ('reorder' === state.operation) {
          if (i === state.index) {
            set(newErrorSchema, [state.newIndex as number], get(errorSchema, [state.index as number]));
          } else if (i === state.newIndex) {
            set(newErrorSchema, [state.index as number], get(errorSchema, [state.newIndex as number]));
          } else {
            set(newErrorSchema, [idx], , get(errorSchema, [i]));
          }
        }
      }
    }
    onChange(keyedToPlainFormData(keyedFormData) as T, newErrorSchema)
  });
  
  getDerivedStateFromProps();
  if (prevState !== state ) {
    setPrevState(() => state);
  }
  
  

  /** React lifecycle method that is called when the props are about to change allowing the state to be updated. It
   * regenerates the keyed form data and returns it
   *
   * @param nextProps - The next set of props data
   * @param prevState - The previous set of state data
   */
  function getDerivedStateFromProps() {
    if (prevState.updatedKeyedFormData) {
      setState({
        ...state,
        updatedKeyedFormData: false
      })
    } else {
      const nextFormData = Array.isArray(props.formData) ? props.formData : [];
      const previousKeyedFormData = prevState.keyedFormData || [];
      const newKeyedFormData =
        nextFormData.length === previousKeyedFormData.length
          ? previousKeyedFormData.map((previousKeyedFormDatum, index) => {
              return {
                key: previousKeyedFormDatum.key,
                item: nextFormData[index],
              };
            })
          : generateKeyedFormData<T>(nextFormData);
      setState({
        ...state,
        keyedFormData: newKeyedFormData
      });
    }
  }

  /** Returns the appropriate title for an item by getting first the title from the schema.items, then falling back to
   * the description from the schema.items, and finally the string "Item"
   */
  function getItemTitle() {
    return get(
      schema,
      [ITEMS_KEY, 'title'],
      get(schema, [ITEMS_KEY, 'description'], translateString(TranslatableString.ArrayItemTitle))
    );
  }

  /** Determines whether the item described in the schema is always required, which is determined by whether any item
   * may be null.
   *
   * @param itemSchema - The schema for the item
   * @return - True if the item schema type does not contain the "null" type
   */
  function isItemRequired(itemSchema: S) {
    if (Array.isArray(itemSchema.type)) {
      // While we don't yet support composite/nullable jsonschema types, it's
      // future-proof to check for requirement against these.
      return !itemSchema.type.includes('null');
    }
    // All non-null array item types are inherently required by design
    return itemSchema.type !== 'null';
  }

  /** Determines whether more items can be added to the array. If the uiSchema indicates the array doesn't allow adding
   * then false is returned. Otherwise, if the schema indicates that there are a maximum number of items and the
   * `formData` matches that value, then false is returned, otherwise true is returned.
   *
   * @param formItems - The list of items in the form
   * @returns - True if the item is addable otherwise false
   */
  function canAddItem(formItems: any[]) {
    let { addable } = getUiOptions<T[], S, F>(uiSchema as UiSchema<T[], S, F>, globalUiOptions);
    if (addable !== false) {
      // if ui:options.addable was not explicitly set to false, we can add
      // another item if we have not exceeded maxItems yet
      if (schema.maxItems !== undefined) {
        addable = formItems.length < schema.maxItems;
      } else {
        addable = true;
      }
    }
    return addable;
  }

  /** Returns the default form information for an item based on the schema for that item. Deals with the possibility
   * that the schema is fixed and allows additional items.
   */
  const _getNewFormDataRow = (): T => {
    let itemSchema = schema.items as S;
    if (isFixedItems(schema) && allowAdditionalItems(schema)) {
      itemSchema = schema.additionalItems as S;
    }
    // Cast this as a T to work around schema utils being for T[] caused by the FieldProps<T[], S, F> call on the class
    return schemaUtils.getDefaultFormState(itemSchema) as unknown as T;
  };

  /** Callback handler for when the user clicks on the add or add at index buttons. Creates a new row of keyed form data
   * either at the end of the list (when index is not specified) or inserted at the `index` when it is, adding it into
   * the state, and then returning `onChange()` with the plain form data converted from the keyed data
   *
   * @param event - The event for the click
   * @param [index] - The optional index at which to add the new data
   */
  function _handleAddClick(event: MouseEvent, index?: number) {
    if (event) {
      event.preventDefault();
    }

    const newKeyedFormDataRow: KeyedFormDataType<T> = {
      key: generateRowId(),
      item: _getNewFormDataRow(),
    };
    const newKeyedFormData = [...keyedFormData];
    if (index !== undefined) {
      newKeyedFormData.splice(index, 0, newKeyedFormDataRow);
    } else {
      newKeyedFormData.push(newKeyedFormDataRow);
    }
    setState(() => { return {
        keyedFormData: newKeyedFormData,
        updatedKeyedFormData: true,
        index: index,
        newIndex: undefined,
        operation: 'add'
    }});
  }

  /** Callback handler for when the user clicks on the add button. Creates a new row of keyed form data at the end of
   * the list, adding it into the state, and then returning `onChange()` with the plain form data converted from the
   * keyed data
   *
   * @param event - The event for the click
   */
  const onAddClick = (event: MouseEvent) => {
    _handleAddClick(event);
  };

  /** Callback handler for when the user clicks on the add button on an existing array element. Creates a new row of
   * keyed form data inserted at the `index`, adding it into the state, and then returning `onChange()` with the plain
   * form data converted from the keyed data
   *
   * @param index - The index at which the add button is clicked
   */
  const onAddIndexClick = (index: number) => {
    return (event: MouseEvent) => {
      _handleAddClick(event, index);
    };
  };

  /** Callback handler for when the user clicks on the copy button on an existing array element. Clones the row of
   * keyed form data at the `index` into the next position in the state, and then returning `onChange()` with the plain
   * form data converted from the keyed data
   *
   * @param index - The index at which the copy button is clicked
   */
  const onCopyIndexClick = (index: number) => {
    return (event: MouseEvent) => {
      if (event) {
        event.preventDefault();
      }
      const newKeyedFormDataRow: KeyedFormDataType<T> = {
        key: generateRowId(),
        item: cloneDeep(keyedFormData[index].item),
      };
      const newKeyedFormData = [...keyedFormData];
      if (index !== undefined) {
        newKeyedFormData.splice(index + 1, 0, newKeyedFormDataRow);
      } else {
        newKeyedFormData.push(newKeyedFormDataRow);
      }
      setState(() => { return {
        keyedFormData: newKeyedFormData,
        updatedKeyedFormData: true,
        index: index,
        newIndex: undefined,
        operation: 'copy'
      }});
    };
  };

  /** Callback handler for when the user clicks on the remove button on an existing array element. Removes the row of
   * keyed form data at the `index` in the state, and then returning `onChange()` with the plain form data converted
   * from the keyed data
   *
   * @param index - The index at which the remove button is clicked
   */
  const onDropIndexClick = (index: number) => {
    return (event: MouseEvent) => {
      if (event) {
        event.preventDefault();
      }
      const newKeyedFormData = keyedFormData.filter((_, i) => i !== index);
      setState(() => { return {
        keyedFormData: newKeyedFormData,
        updatedKeyedFormData: true,
        index: index,
        newIndex: undefined,
        operation: 'drop'
      }});
    };
  };

  /** Callback handler for when the user clicks on one of the move item buttons on an existing array element. Moves the
   * row of keyed form data at the `index` to the `newIndex` in the state, and then returning `onChange()` with the
   * plain form data converted from the keyed data
   *
   * @param index - The index of the item to move
   * @param newIndex - The index to where the item is to be moved
   */
  const onReorderClick = (index: number, newIndex: number) => {
    return (event: MouseEvent<HTMLButtonElement>) => {
      if (event) {
        event.preventDefault();
        event.currentTarget.blur();
      }
      let newErrorSchema: ErrorSchema<T>;
      if (errorSchema) {
        newErrorSchema = {};
        for (const idx in errorSchema) {
          const i = parseInt(idx);
          if (i == index) {
            set(newErrorSchema, [newIndex], get(errorSchema, [index]));
          } else if (i == newIndex) {
            set(newErrorSchema, [index], get(errorSchema, [newIndex]));
          } else {
            set(newErrorSchema, [idx], get(errorSchema, [i]));
          }
        }
      }
      function reOrderArray() {
        // Copy item
        const _newKeyedFormData = keyedFormData.slice();

        // Moves item from index to newIndex
        _newKeyedFormData.splice(index, 1);
        _newKeyedFormData.splice(newIndex, 0, keyedFormData[index]);

        return _newKeyedFormData;
      }
      const newKeyedFormData = reOrderArray();
      setState(() => {return {
        ...state,
        keyedFormData: newKeyedFormData,
        operation: 'reorder',
        index: index,
        newIndex: newIndex
      }})
    };
  };

  /** Callback handler used to deal with changing the value of the data in the array at the `index`. Calls the
   * `onChange` callback with the updated form data
   *
   * @param index - The index of the item being changed
   */
  const onChangeForIndex = (index: number) => {
    return (value: any, newErrorSchema?: ErrorSchema<T>, id?: string) => {
      const { formData, onChange, errorSchema } = props;
      const arrayData = Array.isArray(formData) ? formData : [];
      const newFormData = arrayData.map((item: T, i: number) => {
        // We need to treat undefined items as nulls to have validation.
        // See https://github.com/tdegrunt/jsonschema/issues/206
        const jsonValue = typeof value === 'undefined' ? null : value;
        return index === i ? jsonValue : item;
      });
      onChange(
        newFormData as T,
        errorSchema &&
          errorSchema && {
            ...errorSchema,
            [index]: newErrorSchema,
          },
        id
      );
    };
  };

  /** Callback handler used to change the value for a checkbox */
  const onSelectChange = (value: any) => {
    onChange(value, undefined, idSchema && idSchema.$id);
  };

  function renderNormalArray() {
    const {
      // schema,
      // uiSchema = {},
      // errorSchema,
      name,
      title,
      disabled = false,
      readonly = false,
      autofocus = false,
      required = false,
      registry,
      onBlur,
      onFocus,
      idPrefix,
      idSeparator = '_',
      rawErrors,
    } = props;
    const fieldTitle = schema.title || title || name;
    const { schemaUtils, formContext } = registry;
    const uiOptions = getUiOptions<T[], S, F>(uiSchema as UiSchema<T[], S, F>);
    const _schemaItems: S = isObject(schema.items) ? (schema.items as S) : ({} as S);
    const itemsSchema: S = schemaUtils.retrieveSchema(_schemaItems);
    const formData = keyedToPlainFormData(keyedFormData);
    const canAdd = canAddItem(formData);
    const arrayProps: ArrayFieldTemplateProps<T, S, F> = {
      canAdd,
      items: keyedFormData.map((keyedItem, index) => {
        const { key, item } = keyedItem;
        // While we are actually dealing with a single item of type T, the types require a T[], so cast
        const itemCast = item as unknown as T; // as T[];
        const itemSchema = schemaUtils.retrieveSchema(_schemaItems, itemCast);
        const itemErrorSchema = errorSchema ? (get(errorSchema, [index]) as ErrorSchema<T[]>) : undefined;
        const itemIdPrefix = idSchema.$id + idSeparator + index;
        const itemIdSchema = schemaUtils.toIdSchema(itemSchema, itemIdPrefix, itemCast, idPrefix, idSeparator);
        return renderArrayFieldItem({
          key,
          index,
          name: name && `${name}-${index}`,
          title: fieldTitle ? `${fieldTitle}-${index + 1}` : undefined,
          canAdd,
          canMoveUp: index > 0,
          canMoveDown: index < formData.length - 1,
          itemSchema,
          itemIdSchema,
          itemErrorSchema,
          itemData: itemCast,
          itemUiSchema: uiSchema?.items,
          autofocus: autofocus && index === 0,
          onBlur,
          onFocus,
          rawErrors,
          totalItems: keyedFormData.length,
        });
      }) as ArrayFieldTemplateItemType<T, S, F>[],
      className: `field field-array field-array-of-${itemsSchema.type}`,
      disabled,
      idSchema,
      uiSchema,
      onAddClick: onAddClick,
      readonly,
      required,
      schema,
      title: fieldTitle,
      formContext,
      formData,
      rawErrors,
      registry
    };

    const Template = getTemplate<'ArrayFieldTemplate', T, S, F>('ArrayFieldTemplate', registry as any, uiOptions as any);
    return <Template {...arrayProps} />;
  }

  /** Renders an array using the custom widget provided by the user in the `uiSchema`
   */
  function renderCustomWidget() {
    const {
      schema,
      idSchema,
      uiSchema,
      disabled = false,
      readonly = false,
      autofocus = false,
      required = false,
      hideError,
      placeholder,
      onBlur,
      onFocus,
      formData: items = [],
      registry,
      rawErrors,
      name,
    } = props;
    const { widgets, formContext, schemaUtils } = registry;
    const { widget, title: uiTitle, ...options } = getUiOptions<T[], S, F>(uiSchema as UiSchema<T[], S, F>, globalUiOptions);
    const Widget = getWidget<T[], S, F>(schema, widget, widgets as any);
    const label = uiTitle ?? schema.title ?? name;
    const displayLabel = schemaUtils.getDisplayLabel(schema, uiSchema, globalUiOptions);
    return (
      <Widget
        id={idSchema.$id}
        name={name}
        multiple
        onChange={onSelectChange}
        onBlur={onBlur}
        onFocus={onFocus}
        options={options}
        schema={schema}
        uiSchema={uiSchema as UiSchema<T[], S, F>}
        registry={registry as any}
        value={items}
        disabled={disabled}
        readonly={readonly}
        hideError={hideError}
        required={required}
        label={label}
        hideLabel={!displayLabel}
        placeholder={placeholder}
        formContext={formContext}
        autofocus={autofocus}
        rawErrors={rawErrors}
      />
    );
  }

  /** Renders an array as a set of checkboxes
   */
  function renderMultiSelect() {
    const {
      schema,
      idSchema,
      uiSchema,
      formData: items = [],
      disabled = false,
      readonly = false,
      autofocus = false,
      required = false,
      placeholder,
      onBlur,
      onFocus,
      registry,
      rawErrors,
      name,
    } = props;
    const { widgets, schemaUtils, formContext, globalUiOptions } = registry;
    const itemsSchema = schemaUtils.retrieveSchema(schema.items as S, items as T);
    const enumOptions = optionsList<S, T[], F>(itemsSchema, uiSchema as UiSchema<T[], S, F>);
    // const enumOptions = optionsList<S, T[], F>(itemsSchema, uiSchema as UiSchema<T[], S, F>);
    const { widget = 'select', title: uiTitle, ...options } = getUiOptions<T[], S, F>(uiSchema as UiSchema<T[], S, F>, globalUiOptions);
    const Widget = getWidget<T[], S, F>(schema, widget, widgets as any);
    const label = uiTitle ?? schema.title ?? name;
    const displayLabel = schemaUtils.getDisplayLabel(schema, uiSchema, globalUiOptions);
    return (
      <Widget
        id={idSchema.$id}
        name={name}
        multiple
        onChange={onSelectChange}
        onBlur={onBlur}
        onFocus={onFocus}
        options={{ ...options, enumOptions }}
        schema={schema}
        uiSchema={uiSchema as UiSchema<T[], S, F>}
        registry={registry as any}
        value={items}
        disabled={disabled}
        readonly={readonly}
        required={required}
        label={label}
        hideLabel={!displayLabel}
        placeholder={placeholder}
        formContext={formContext}
        autofocus={autofocus}
        rawErrors={rawErrors}
      />
    );
  }

  /** Renders an array of files using the `FileWidget`
   */
  function renderFiles() {
    const {
      schema,
      uiSchema,
      idSchema,
      name,
      disabled = false,
      readonly = false,
      autofocus = false,
      required = false,
      onBlur,
      onFocus,
      registry,
      formData: items = [],
      rawErrors,
    } = props;
    const { widgets, formContext, globalUiOptions, schemaUtils } = registry;
    const { widget = 'files', title: uiTitle, ...options } = getUiOptions<T[], S, F>(uiSchema, globalUiOptions);
    const Widget = getWidget<T[], S, F>(schema, widget, widgets);
    const label = uiTitle ?? schema.title ?? name;
    const displayLabel = schemaUtils.getDisplayLabel(schema, uiSchema, globalUiOptions);
    return (
      <Widget
        options={options}
        id={idSchema.$id}
        name={name}
        multiple
        onChange={onSelectChange}
        onBlur={onBlur}
        onFocus={onFocus}
        schema={schema}
        uiSchema={uiSchema as UiSchema<T[], S, F>}
        value={items}
        disabled={disabled}
        readonly={readonly}
        required={required}
        registry={registry as any}
        formContext={formContext}
        autofocus={autofocus}
        rawErrors={rawErrors}
        label={label}
        hideLabel={!displayLabel}
      />
    );
  }

  /** Renders an array that has a maximum limit of items
   */
  function renderFixedArray() {
    const {
      schema,
      uiSchema = {},
      formData = [],
      errorSchema,
      idPrefix,
      idSeparator = '_',
      idSchema,
      name,
      title,
      disabled = false,
      readonly = false,
      autofocus = false,
      required = false,
      registry,
      onBlur,
      onFocus,
      rawErrors,
    } = props;
    const { keyedFormData } = state;
    let { formData: items = [] } = props;
    const fieldTitle = schema.title || title || name;
    const uiOptions = getUiOptions<T[], S, F>(uiSchema as UiSchema<T[], S, F>);
    const { schemaUtils, formContext } = registry;
    const _schemaItems: S[] = isObject(schema.items) ? (schema.items as S[]) : ([] as S[]);
    const itemSchemas = _schemaItems.map((item: S, index: number) =>
      schemaUtils.retrieveSchema(item, get(formData, [index]))
    );
    const additionalSchema = isObject(schema.additionalItems)
      ? schemaUtils.retrieveSchema(schema.additionalItems as S, formData as T)
      : null;

    if (!items || (items as T[]).length < itemSchemas.length) {
      // to make sure at least all fixed items are generated
      items = items || [] ;
      items = ((items as T[]).concat(new Array(itemSchemas.length - (items as T[]).length)));
    }

    // These are the props passed into the render function
    const canAdd = canAddItem(items as any[]) && !!additionalSchema;
    const arrayProps: ArrayFieldTemplateProps<T, S, F> = {
      canAdd,
      className: 'field field-array field-array-fixed-items',
      disabled,
      idSchema,
      formData,
      items: keyedFormData.map((keyedItem, index) => {
        const { key, item } = keyedItem;
        // While we are actually dealing with a single item of type T, the types require a T[], so cast
        const itemCast = item as unknown as T[];
        const additional = index >= itemSchemas.length;
        const itemSchema =
          (additional && isObject(schema.additionalItems)
            ? schemaUtils.retrieveSchema(schema.additionalItems as S, itemCast as T)
            : itemSchemas[index]) || {};
        const itemIdPrefix = idSchema.$id + idSeparator + index;
        const itemIdSchema = schemaUtils.toIdSchema(itemSchema, itemIdPrefix, itemCast as T, idPrefix, idSeparator);
        const itemUiSchema = additional
          ? uiSchema.additionalItems || {}
          : Array.isArray(uiSchema.items)
          ? uiSchema.items[index]
          : uiSchema.items || {};
        const itemErrorSchema = errorSchema ? (get(errorSchema, [index]) as ErrorSchema<T[]>) : undefined;

        return renderArrayFieldItem({
          key,
          index,
          name: name && `${name}-${index}`,
          title: fieldTitle ? `${fieldTitle}-${index + 1}` : undefined,
          canAdd,
          canRemove: additional,
          canMoveUp: index >= itemSchemas.length + 1,
          canMoveDown: additional && index < items.length - 1,
          itemSchema,
          itemData: itemCast as T,
          itemUiSchema,
          itemIdSchema,
          itemErrorSchema,
          autofocus: autofocus && index === 0,
          onBlur,
          onFocus,
          rawErrors,
          totalItems: keyedFormData.length,
        });
      }),
      onAddClick: onAddClick,
      readonly,
      required,
      registry,
      schema,
      uiSchema,
      title: fieldTitle,
      formContext,
      errorSchema,
      rawErrors,
    };

    const Template = getTemplate<'ArrayFieldTemplate', T, S, F>('ArrayFieldTemplate', registry as any, uiOptions as any);
    return <Template {...arrayProps} />;
  }

  /** Renders the individual array item using a `SchemaField` along with the additional properties required to be send
   * back to the `ArrayFieldItemTemplate`.
   *
   * @param props - The props for the individual array item to be rendered
   */
  function renderArrayFieldItem(props: {
    key: string;
    index: number;
    name: string;
    title: string | undefined;
    canAdd: boolean;
    canRemove?: boolean;
    canMoveUp: boolean;
    canMoveDown: boolean;
    itemSchema: S;
    itemData: T;
    itemUiSchema: UiSchema<T[], S, F>;
    itemIdSchema: IdSchema<T>;
    itemErrorSchema?: ErrorSchema<T[]>;
    autofocus?: boolean;
    onBlur: FieldProps<T[], S, F>['onBlur'];
    onFocus: FieldProps<T[], S, F>['onFocus'];
    rawErrors?: string[];
    totalItems: number;
  }) {
    const {
      key,
      index,
      name,
      canAdd,
      canRemove = true,
      canMoveUp,
      canMoveDown,
      itemSchema,
      itemData,
      itemUiSchema,
      itemIdSchema,
      itemErrorSchema,
      autofocus,
      onBlur,
      onFocus,
      rawErrors,
      totalItems,
      title,
    } = props;
    
    const {
      fields: { ArraySchemaField, SchemaField },
      globalUiOptions,
    } = registry;
    const ItemSchemaField = ArraySchemaField || SchemaField;
    const { orderable = true, removable = true, copyable = false } = getUiOptions<T[], S, F>(uiSchema, globalUiOptions);
    const has: { [key: string]: boolean } = {
      moveUp: orderable && canMoveUp,
      moveDown: orderable && canMoveDown,
      copy: copyable && canAdd,
      remove: removable && canRemove,
      toolbar: false,
    };
    has.toolbar = Object.keys(has).some((key: keyof typeof has) => has[key]);

    return {
      children: (
        <ItemSchemaField
          name={name}
          title={title}
          index={index}
          schema={itemSchema}
          uiSchema={itemUiSchema as UiSchema<T, S, F>}
          formData={itemData as any}
          formContext={formContext}
          errorSchema={itemErrorSchema}
          idPrefix={idPrefix}
          idSeparator={idSeparator}
          idSchema={itemIdSchema}
          required={isItemRequired(itemSchema)}
          onChange={onChangeForIndex(index)}
          onBlur={onBlur}
          onFocus={onFocus}
          registry={registry}
          disabled={disabled}
          readonly={readonly}
          hideError={hideError}
          autofocus={autofocus}
          rawErrors={rawErrors}
        />
      ),
      className: 'array-item',
      disabled,
      canAdd,
      hasCopy: has.copy,
      hasToolbar: has.toolbar,
      hasMoveUp: has.moveUp,
      hasMoveDown: has.moveDown,
      hasRemove: has.remove,
      index,
      totalItems,
      key,
      onAddIndexClick: onAddIndexClick,
      onCopyIndexClick: onCopyIndexClick,
      onDropIndexClick: onDropIndexClick,
      onReorderClick: onReorderClick,
      readonly,
      registry,
      schema: itemSchema,
      uiSchema: itemUiSchema,
    };
  }
    
  if (!(ITEMS_KEY in schema)) {
    const uiOptions = getUiOptions<T[], S, F>(uiSchema as UiSchema<T[], S, F>, registry as any);
    const UnsupportedFieldTemplate = getTemplate<'UnsupportedFieldTemplate', T[], S, F>(
      'UnsupportedFieldTemplate',
      registry as any,
      uiOptions
    );

    return (
      <UnsupportedFieldTemplate
        schema={schema}
        idSchema={idSchema as IdSchema<T[]>}
        reason={translateString(TranslatableString.MissingItems)}
        registry={registry as any}
      />
    );
  }
  if (schemaUtils.isMultiSelect(schema)) {
    // If array has enum or uniqueItems set to true, call renderMultiSelect() to render the default multiselect widget or a custom widget, if specified.
    return renderMultiSelect();
  }
  if (isCustomWidget<T[], S, F>(uiSchema as UiSchema<T[], S, F>)) {
    return renderCustomWidget();
  }
  if (isFixedItems(schema)) {
    return renderFixedArray();
  }
  if (schemaUtils.isFilesArray(schema, uiSchema)) {
    return renderFiles();
  }
  return renderNormalArray();
}


/** `ArrayField` is `React.ComponentType<FieldProps<T[], S, F>>` (necessarily) but the `registry` requires things to be a
 * `Field` which is defined as `React.ComponentType<FieldProps<T, S, F>>`, so cast it to make `registry` happy.
 */
export default ArrayField;