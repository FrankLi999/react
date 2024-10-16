const ConfigDataFormUISchemaEdit ={
  "ui:submitButtonOptions": {
    "submitText": "Submit Configuration Data",
    "norender": false,
    "props": {
      "disabled": false,
      "className": "btn btn-info"
    }
  },
  "application": {
    "ui:title": "Application",
    "ui:autofocus": true,
    "ui:emptyValue": "",
    "ui:autocomplete": "my-camel-integrator",
    "ui:disabled": true
  },
  "profile": {
    "ui:title": "Profile",
    "ui:emptyValue": "",
    "ui:autocomplete": "default",
    "ui:placeholder": "Spring profile",
    "ui:disabled": true
  },
  "label": {
    "ui:title": "Label",
    "ui:emptyValue": "",
    "ui:autocomplete": "master",
    "ui:placeholder": "master",
    "ui:disabled": true
  },

  "props": {
    "items": {
      "propKey": {
        "ui:placeholder": "Property key"
      },
      "propValue": {
        "ui:placeholder": "Property value"
      }
    }
  }
};
export default ConfigDataFormUISchemaEdit;
