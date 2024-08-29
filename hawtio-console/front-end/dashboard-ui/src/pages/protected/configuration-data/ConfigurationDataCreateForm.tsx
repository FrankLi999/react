import React from "react";
import { useCookies } from 'react-cookie';
import { CookiesProvider } from 'react-cookie';
import { Link } from "react-router-dom";
import validator from '@react-jsf/ajv';
import Form from '@react-jsf/bootstrap5';
import ConfigDataSchema from './form/config-data-schema';
import ConfigDataFormUISchemaCreate from './form/config-data-form-uischema-create';
import ConfigDataFormData from './form/config-data-form-data';

import {
    Card,
    Container,
    Row,
    Col
} from "react-bootstrap";

function ConfigurationDataCreateForm() {
    const [cookies] = useCookies(['XSRF-TOKEN']);
    const log = (type: any) => console.log.bind(console, type);
    async function submitData(data: any) {
        console.log('submitted data', data)
        console.log('submitted for data', data.formData)
        try {
            const requestOptions = {
                method: 'POST',
                headers: { 'Content-Type': 'application/json', 'accept': 'application/json', 'X-XSRF-TOKEN': cookies['XSRF-TOKEN'] },
                body: JSON.stringify([data.formData]),
            };
            await fetch('/my-camel/admin/api/spring-config/configurations', requestOptions);
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
                            <p className="card-category">Create configurations for an application profile</p>
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
                                            schema={ConfigDataSchema}
                                            uiSchema={ConfigDataFormUISchemaCreate}
                                            formData={ConfigDataFormData}
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
export default ConfigurationDataCreateForm;
