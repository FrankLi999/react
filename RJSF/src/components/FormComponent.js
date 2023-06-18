import Form from "@rjsf/core";
import React from "react";

function FormComponent() {
    const schema = require("../jsonFiles/schema.json")
    const formData = require("../jsonFiles/formData.json")
    const uiSchema = require("../jsonFiles/uiSchema.json")

    return (
        <div className="form">
            <Form
                schema={schema}
                uiSchema={uiSchema}
                formData={formData}
            >

            </Form>
        </div>
    )
}

export default FormComponent
