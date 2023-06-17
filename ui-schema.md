# Available elements

https://jsonforms.io/docs/uischema/  

- Controls
- Layouts
- Rules

## Controls
schema:  
{
  "properties": {
    "name": {
      "type": "string"
    }
  }
}

uischema.json:  
{
  "type": "Control",
  "scope": "#/properties/name" // reference to schema field
   "label": "First name" // or boolean

}

## Layouts
- HorizontalLayout
- VertitalLayout
- Group
- Categorization
### HorizontalLayout
{
  "type": "HorizontalLayout",
  "elements": [
    {
      "type": "Control",
      "label": "Name",
      "scope": "#/properties/name"
    },
    {
      "type": "Control",
      "label": "Birth Date",
      "scope": "#/properties/birthDate"
    }
  ]
}
### VerticalLayout
{
  "type": "VerticalLayout",
  "elements": [
    {
      "type": "Control",
      "label": "Name",
      "scope": "#/properties/name"
    },
    {
      "type": "Control",
      "label": "Birth Date",
      "scope": "#/properties/birthDate"
    }
  ]
}
### Group
{
  "type": "Group",
  "label": "My Group",
  "elements": [
    {
      "type": "Control",
      "label": "Name",
      "scope": "#/properties/name"
    },
    {
      "type": "Control",
      "label": "Birth Date",
      "scope": "#/properties/birthDate"
    }
  ]
}

### Categorization

{
  "type": "Categorization",
  "elements": [
    {
      "type": "Category",
      "label": "Personal Data",
      "elements": [
        {
          "type": "Control",
          "scope": "#/properties/firstName"
        },
        {
          "type": "Control",
          "scope": "#/properties/lastName"
        },
        {
          "type": "Control",
          "scope": "#/properties/age"
        },
        {
          "type": "Control",
          "scope": "#/properties/height"
        },
        {
          "type": "Control",
          "scope": "#/properties/dateOfBirth"
        }
      ]
    },
    {
      "type": "Category",
      "label": "Dietary requirements",
      "elements": [
        {
          "type": "Control",
          "scope": "#/properties/diet"
        },
        {
          "type": "Control",
          "scope": "allergicToPeanuts"
        }
      ]
    }
  ]
}

## Rules

"rule": {
  "effect": "HIDE" | "SHOW" | "ENABLE" | "DISABLE",
  "condition": {
    "scope": "<UI Schema scope>",
    "schema": JSON Schema
  }
}