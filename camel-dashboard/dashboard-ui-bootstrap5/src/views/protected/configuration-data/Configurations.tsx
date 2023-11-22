import React, { useEffect, useState } from 'react';
import { Container, Card, Col, Row } from "react-bootstrap";
import { Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from "react-bootstrap-table2-paginator";
import axios from 'axios';
import { ConfigurationModel } from "./ConfigurationModel";
import DeleteConfirmation from "./DeleteConfirmation";
import ImportConfiguration from './ImportConfiguration';

function Configurations() {
    const navigate = useNavigate(); 
    const [displayDeleteConfirmationModal, setDisplayDeleteConfirmationModal] = useState(false);
    const [displayImportConfirmationModal, setDisplayImportConfirmationModal] = useState(false);
    
    const [deleteRow, setDeleteRow] = useState<ConfigurationModel|null>(null);
    const [configurations, setconfigurations] = useState<ConfigurationModel[]>([]);
    const [loading, setLoading] = useState(false);

    const importConfigurations = (configIle: File) => {
      // data: ConfigurationModel[]| null
      
      console.log(">>>>>>>> import configuration:", configIle);
      const formData = new FormData();
      formData.append('file', configIle);
      // try {
      //     const requestOptions = {
      //         method: 'POST',
      //         headers: {
      //             'content-type': 'multipart/form-data'
      //         },
      //         body: formData,
      //     };
      //     await fetch("/api/import", requestOptions);
      // } catch (err) {
      //     console.log(err);
      // }
      const config = {
          headers: {
              'content-type': 'multipart/form-data'
          },
      };
      axios.post("/api/import", formData, config).then((response) => {
          console.log(response.data);
          setconfigurations(response.data);
          setDisplayImportConfirmationModal(false);
      }).catch((error) => {
          console.error("Error uploading spring config: ", error);
          setDisplayImportConfirmationModal(false);
      });      
    }

    const hideImportConfigurationModal = () => {
      setDisplayImportConfirmationModal(false);
    };

    const hideDeleteConfirmationModal = () => {
      setDisplayDeleteConfirmationModal(false);
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
    const exportConfig = () => {
      // const jsonURL = `data:text/json;chatset=utf-8,${encodeURIComponent(
      //   JSON.stringify(configurations)
      // )}`;
      const data = new Blob([JSON.stringify(configurations, null, 2)], { type: "text/json" });
      const jsonURL = window.URL.createObjectURL(data);
      const link = document.createElement("a");
      document.body.appendChild(link);
      link.href = jsonURL;
      link.download = "spring-config.json";
      link.click();
      document.body.removeChild(link);
    }
    const addAppConfiurationDetails = () => {
      navigate("/integrator/configuration-form-create")
    }
    const editAppConfiurationDetails = (row: ConfigurationModel) => {
      navigate("/integrator/configuration-form-edit", {state: {...row}})
    }
    const showAppConfiurationDetails = (row: ConfigurationModel) => {
      navigate("/integrator/configuration-app-details", {state: {...row}}) 
    }
    
    const showImportConfigurationModal = () => {
      setDisplayImportConfirmationModal(true);
    }

    const showDeleteModal = (row: ConfigurationModel) => {
      setDeleteRow(() => row);
      setDisplayDeleteConfirmationModal(() => true);
    }

    async function deleteAppConfiurationDetails(row: ConfigurationModel) {
      console.log('delete app profile', row)
      try {
          const requestOptions = {
              method: 'DELETE',
              headers: { 'Content-Type': 'application/json', 'accept': '*/*' },
              body: JSON.stringify([{application: row.application, profile: row.profile}]),
          };
          setLoading(() => true);
          await fetch("/api/configurations", requestOptions)
                    .then(response => response.json())
                    .then((data: ConfigurationModel[]) => {
                      setconfigurations(() => data);
                      setLoading(() => false);
                      setDisplayDeleteConfirmationModal(() => false);
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
      setLoading(() => true);
  
      fetch('api/configurations')
        .then(response => response.json())
        .then((data: ConfigurationModel[]) => {
          setconfigurations(() => data);
          setLoading(() => false);
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
                            <div className="small form-group d-flex align-items-center justify-content-start mt-4 mb-0">
                              <Button className="ml-1" onClick={() => {
                                  addAppConfiurationDetails();
                                }}>
                                Add Application Profile
                              </Button>
                              <Button className="ml-1" style={{ 'marginLeft': '12px' }} onClick={exportConfig}>Export All Configurations</Button>
                              <Button className="ml-1" style={{ 'marginLeft': '12px' }} onClick={() => { showImportConfigurationModal();}}>Import All Configurations</Button>
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
            <DeleteConfirmation showModal={displayDeleteConfirmationModal} confirmModal={deleteAppConfiurationDetails} hideModal={hideDeleteConfirmationModal} 
              row={deleteRow} message={`Are you sure to delete configurations for application for ${deleteRow?.application}/${deleteRow?.profile}`}  />
            <ImportConfiguration showModal={displayImportConfirmationModal} importConfiguration={importConfigurations} hideModal={hideImportConfigurationModal}  />  
        </Container>
        </>
      );
    }
}      

export default Configurations;