'use client'
import React from "react";
import Link from "next/link";
import { useRouter } from 'next/navigation'

// import { Link, useLocation, useNavigate } from "react-router-dom";
import { Container, Card, Col, Row } from "react-bootstrap";
import { Button } from "react-bootstrap";
import BootstrapTable from "react-bootstrap-table-next";
import cellEditFactory from 'react-bootstrap-table2-editor';
import paginationFactory from "react-bootstrap-table2-paginator";
import { useIntegratorConfigurationDataContext } from "@/context/integrator-configuration/IntegratorConfigurationDataProvider";

function ConfigurationAppDetails() {
  // const navigate = useNavigate(); 
  const router = useRouter();
  const { states, dispatch } = useIntegratorConfigurationDataContext();
  console.log("Will show details1111", states.currentRow, states.currentRow.props.length);
  // const { state } = useLocation();
  const editConfig = () => {
    // navigate("/integrator/configuration-form-edit", {state: {...state}})
    // router.push("/integrator/configuration-form-edit", {state: {...state}})

    router.push('/integrator/configuration-form-edit');
  }
  
  const appConfigColumns = [
    {
      dataField: "propKey",
      text: "Property",
      sort: true,
      editable: false
    },
    {
      dataField: "propValue",
      text: "Profile",
      sort: false,
      editable: false
    }
  ];
  const paginationOptions = {
    // custom: true,
    sizePerPage: 5,
    paginationSize: 5,
    pageStartIndex: 1,
    firstPageText: "First",
    prePageText: "Back",
    nextPageText: "Next",
    lastPageText: "Last",
    nextPageTitle: "First page",
    prePageTitle: "Pre page",
    firstPageTitle: "Next page",
    lastPageTitle: "Last page",
    showTotal: false,
    // totalSize: 3
    totalSize: states.currentRow.props.length,
    sizePerPageList: [ {
      text: '5', value: 5
    }, {
      text: '10', value: 10
    }, {
      text: '30', value: 10
    },{
      text: 'All', value: states.currentRow.props.length
    } ]
  };

  console.log("Will show detail222222", paginationOptions);
  return (
    <>
      <Container>
        <Row className="justify-content-center">
          <Col lg="10" md="8">
            <Card>
              <Card.Header>
                <Card.Title
                  as="h4"
                  className="text-center font-weight-light my-4"
                >
                  Application Configuration: {states.currentRow.application}/{states.currentRow.profile} <Button onClick={() => {editConfig();}}>Edit</Button>
                </Card.Title>
              </Card.Header>
              <Card.Body>
                <div className="text-center">
                  <BootstrapTable
                    bootstrap4
                    keyField="propKey"
                    data={states.props}
                    columns={appConfigColumns}
                    pagination={paginationFactory(paginationOptions)}
                    noDataIndication={"Table is empty"}
                    cellEdit={ cellEditFactory({ mode: 'dbclick' }) }
                  />
                </div>
              </Card.Body>
              <Card.Footer className="card-footer text-center">
                <div className="small form-group d-flex align-items-center justify-content-between mt-4 mb-0">
                  <Link href={`/integrator/configuration-data`}>                    
                    Go to configurations page
                  </Link>
                </div>
              </Card.Footer>
            </Card>
          </Col>
        </Row>
      </Container>
    </>
  );
}

export default ConfigurationAppDetails;
