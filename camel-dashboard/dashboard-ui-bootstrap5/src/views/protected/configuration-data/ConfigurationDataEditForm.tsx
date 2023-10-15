import React from "react";
import { Link, useLocation } from "react-router-dom";
import { RJSFSchema } from '@rjsf/utils';
import validator from '@rjsf/validator-ajv8';
// import Form from "@rjsf/bootstrap-4";
import Form from "../../../components/bootstrap5/Form";
// import schemaForm from './jsonFiles/schema.json';
// import formData from './jsonFiles/formData.json';
// import uiSchema from './jsonFiles/uiSchema.json';

import {
    Card,
    Container,
    Row,
    Col
} from "react-bootstrap";

function ConfigurationDataEditForm() {
    const schema: RJSFSchema = require("./json-files/config-data-schema.json");
    const uiSchema = require("./json-files/config-data-form-uischema-edit.json");
    const { state } = useLocation();
    const formData = {...state};
    const log = (type: any) => console.log.bind(console, type);
    // const schema: RJSFSchema = schemaForm;
    // const formData = require("./json-files/config-data-form-data.json")
    async function submitData(data: any) {
        console.log('submitted data', data)
        console.log('submitted for data', data.formData)
        try {
            const requestOptions = {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json', 'accept': 'application/json' },
                body: JSON.stringify([data.formData]),
            };
            await fetch("/api/configurations", requestOptions);
        } catch (err) {
            console.log(err);
        }
    }
    return (
        <>
        <Container fluid>
            <Row>
                <Col md="12">
                    <Card>
                        <Card.Header>
                            <Card.Title as="h4">Camel Integrator Configurations</Card.Title>
                            <p className="card-category">Edit configurations for an application profile</p>
                            <div className="small form-group d-flex align-items-center justify-content-between mt-4 mb-0">
                                <Link to="/integrator/configuration-data">                    
                                    Go to configurations page
                                </Link>
                            </div>
                        </Card.Header>
                        <Card.Body>
                            <Row>
                                <Col md="12">
                                    <div className="form">
                                        <Form
                                            schema={schema}
                                            uiSchema={uiSchema}
                                            formData={formData}
                                            validator={validator}
                                            onSubmit={data => submitData(data)}
                                        >
                                        </Form>
                                    </div>
                                </Col>
                            </Row>
                        </Card.Body>
                        <Card.Footer className="card-footer text-center">
                            <div className="small form-group d-flex align-items-center justify-content-between mt-4 mb-0">
                                <Link to="/integrator/configuration-data">                    
                                    Go to configurations page
                                </Link>
                            </div>
                        </Card.Footer>
                    </Card>
                </Col>
            </Row>
        </Container>
        </>
    )
}

export default ConfigurationDataEditForm;