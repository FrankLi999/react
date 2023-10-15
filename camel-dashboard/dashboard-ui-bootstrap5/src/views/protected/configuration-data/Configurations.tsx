import React, { useEffect, useState } from 'react';
import { Container, Card, Col, Row } from "react-bootstrap";
import { Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from "react-bootstrap-table2-paginator";
import { ConfigurationModel } from "./ConfigurationModel";
import DeleteConfirmation from "./DeleteConfirmation";
function Configurations() {
    const navigate = useNavigate(); 
    const [displayConfirmationModal, setDisplayConfirmationModal] = useState(false);
    const [deleteRow, setDeleteRow] = useState<ConfigurationModel|null>(null);
    const [configurations, setconfigurations] = useState<ConfigurationModel[]>([]);
    const [loading, setLoading] = useState(false);
    const hideConfirmationModal = () => {
      setDisplayConfirmationModal(false);
    };
    const detailsLink = (cell, row, rowIndex, formatExtraData) => {
      return (
        <>
        <Button
          onClick={() => {
            editAppConfiurationDetails(row);
          }}
        >
          Edit
        </Button>
        <Button
          onClick={() => {
            showAppConfiurationDetails(row);
          }}
        >
          Config
        </Button>
        <Button
          onClick={() => {
            showDeleteModal(row);
          }}
        >
          Delete
        </Button>
        </>
      );
      
    };
    const addAppConfiurationDetails = () => {
      navigate("/integrator/configuration-form-create")
    }
    const editAppConfiurationDetails = (row: ConfigurationModel) => {
      navigate("/integrator/configuration-form-edit", {state: {...row}})
    }
    const showAppConfiurationDetails = (row: ConfigurationModel) => {
      navigate("/integrator/configuration-app-details", {state: {...row}}) 
    }
    
    const showDeleteModal = (row: ConfigurationModel) => {
      setDeleteRow(row);
      setDisplayConfirmationModal(true);
    }

    async function deleteAppConfiurationDetails(row: ConfigurationModel) {
      console.log('delete app profile', row)
      try {
          const requestOptions = {
              method: 'DELETE',
              headers: { 'Content-Type': 'application/json', 'accept': '*/*' },
              body: JSON.stringify([{application: row.application, profile: row.profile}]),
          };
          setLoading(true);
          await fetch("/api/configurations", requestOptions)
                    .then(response => response.json())
                    .then((data: ConfigurationModel[]) => {
                      setconfigurations(data);
                      setLoading(false);
                      setDisplayConfirmationModal(false);
                    });
          console.log("deleted");
      } catch (err) {
          console.log(err);
      }
    }
    const configurationTableColumns = [
      {
        dataField: "application",
        text: "Application",
        sort: true,
        editable: false
      },
      {
        dataField: "profile",
        text: "Profile",
        sort: false,
        editable: false
      },
      {
        dataField: "label",
        text: "Label",
        sort: false,
        editable: false
      },
      {
        dataField: "actions",
        text: "Details",
        sort: false,
        isDummyField: true,
        editable: false,
        formatter: detailsLink,

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
      totalSize: configurations.length,
      sizePerPageList: [ {
        text: '5', value: 5
      }, {
        text: '10', value: 10
      }, {
        text: '30', value: 10
      },{
        text: 'All', value: configurations.length
      } ]
    };

    useEffect(() => {
      setLoading(true);
  
      fetch('api/configurations')
        .then(response => response.json())
        .then((data: ConfigurationModel[]) => {
          setconfigurations(data);
          setLoading(false);
        })
    }, []);
    
    if (loading) {
      return <p>Loading...</p>;
    } else {
      return (
        <>
          <Container fluid>
            <Row>
                <Col md="12">
                    <Card>
                        <Card.Header>
                            <Card.Title as="h4">Camel Integrator Configurations</Card.Title>
                            <p className="card-category">Application profiles</p>
                            <div className="small form-group d-flex align-items-center justify-content-between mt-4 mb-0">
                              <Button onClick={() => {
                                  addAppConfiurationDetails();
                                }}>
                                Add
                              </Button>
                            </div>
                        </Card.Header>
                        <Card.Body>
                            <Row>
                                <Col md="12">
                                  <BootstrapTable
                                    bootstrap4
                                    keyField="key"
                                    data={configurations}
                                    columns={configurationTableColumns}
                                    pagination={paginationFactory(paginationOptions)}
                                    noDataIndication={"Table is empty"}
                                  />
                                </Col>
                            </Row>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
            <DeleteConfirmation showModal={displayConfirmationModal} confirmModal={deleteAppConfiurationDetails} hideModal={hideConfirmationModal} 
              row={deleteRow} message={`Are you sure to delete configurations for application for ${deleteRow?.application}/${deleteRow?.profile}`}  />
        </Container>
        </>
      );
    }
}      

export default Configurations;