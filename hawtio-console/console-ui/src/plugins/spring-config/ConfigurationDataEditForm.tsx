import React from "react";
import { useCookies } from 'react-cookie';
import { Link, useLocation } from "react-router-dom";
import { RJSFSchema } from '@rjsf/utils';
import { 
    Card,
    CardBody,
    CardFooter,
    PageSection,
    PageSectionVariants,
    Text,
    TextContent,
  } from '@patternfly/react-core';
import validator from '@rjsf/validator-ajv8';
import Form from "../../components/form/Form";

function ConfigurationDataEditForm() {
    const [cookies] = useCookies(['XSRF-TOKEN']);
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
                headers: { 'Content-Type': 'application/json', 'accept': 'application/json', 'X-XSRF-TOKEN': cookies['XSRF-TOKEN']  },
                body: JSON.stringify([data.formData]),
            };
            await fetch("/my-camel/admin/api/spring-config/configurations", requestOptions);
        } catch (err) {
            console.log(err);
        }
    }
    return (
        <>
          <PageSection variant={PageSectionVariants.light}>
            <TextContent>
              <Text component="h4">Camel Integrator Configurations: {state.application}/{state.profile} </Text>
              <Text component="p">Edit configurations for an application profile</Text>
            </TextContent>
            <div className="small form-group d-flex align-items-center justify-content-between mt-4 mb-0">
              <Link to="/spring-config">
                Go to configurations page
              </Link>
            </div>
          </PageSection>
          <PageSection>
            <Card>
              <CardBody>
                <Form schema={schema} uiSchema={uiSchema} formData={formData} validator={validator} onSubmit={data => submitData(data)} />
              </CardBody>
              <CardFooter>
                <div className="small form-group d-flex align-items-center justify-content-between mt-4 mb-0">
                  <Link to="/spring-config">
                    Go to configurations page
                  </Link>
                </div>
              </CardFooter>
            </Card>
          </PageSection>
        </>
    )
}

export default ConfigurationDataEditForm;
