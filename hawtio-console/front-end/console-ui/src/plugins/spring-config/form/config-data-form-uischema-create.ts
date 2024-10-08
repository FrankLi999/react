const ConfigDataFormUISchemaCreate = {
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
    "ui:autocomplete": "my-camel-integrator"
  },
  "profile": {
    "ui:title": "Profile",
    "ui:emptyValue": "",
    "ui:autocomplete": "default",
    "ui:placeholder": "Spring profile"
  },
  "label": {
    "ui:title": "Label",
    "ui:emptyValue": "",
    "ui:autocomplete": "master",
    "ui:placeholder": "master"
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
export default ConfigDataFormUISchemaCreate;
