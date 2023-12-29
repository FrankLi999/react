'use client'
import React, { useEffect, useState } from 'react';
import { Container, Card, Col, Row } from "react-bootstrap";
import { Button } from "react-bootstrap";
//import { useNavigate } from "react-router-dom";
import { useRouter } from 'next/navigation'
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from "react-bootstrap-table2-paginator";
import { ConfigurationModel } from "@/type/ConfigurationModel";
import DeleteConfirmation from "./DeleteConfirmation";
import ImportConfiguration from './ImportConfiguration';
import { useIntegratorConfigurationDataContext} from '@/context/integrator-configuration/IntegratorConfigurationDataProvider';
function Configurations() {
    const { states, dispatch } = useIntegratorConfigurationDataContext();
    console.log(">>>>>>>>> config data:>>>>>>> 0000", states.configurations);
    // const navigate = useNavigate(); 
    const router = useRouter();
    const [displayDeleteConfirmationModal, setDisplayDeleteConfirmationModal] = useState(false);
    const [displayImportConfirmationModal, setDisplayImportConfirmationModal] = useState(false);
    
    const [deleteRow, setDeleteRow] = useState<ConfigurationModel|null>(null);
   const [configurations, setConfigurations] = useState<ConfigurationModel[]>([]);
    const [loading, setLoading] = useState(false);

    const importConfigurations = async (configIle: File) => {
      // data: ConfigurationModel[]| null
      
      console.log(">>>>>>>> import configuration:", configIle);
      const formData = new FormData();
      formData.append('file', configIle);      
      
      try {
          const requestOptions = {
              method: 'POST',
              headers: {'accept': 'application/json' },
              // headers: {'content-type': 'multipart/form-data; boundary=MyBoundary', 'accept': 'application/json' },
              body: formData
          };
          const apiResponse: Response = await fetch("/api/integrator/imports", requestOptions);
          // if (!apiResponse.ok()) {
          //   router.replace(`/${CURRENT_PAGE}#topOfErrors`);
          // } else {
            const configurations: ConfigurationModel[] = await apiResponse.json() as ConfigurationModel[];
            console.log(configurations);
            console.log(states.configurations);
            setConfigurations(configurations);
            dispatch({ type: "set_configurations", configurations: configurations});
            setDisplayImportConfirmationModal(false);
          // }
      } catch (err) {
          console.error("Error uploading spring config: ", err);
          setDisplayImportConfirmationModal(false);
      }

    }

    const hideImportConfigurationModal = () => {
      setDisplayImportConfirmationModal(false);
    };

    const hideDeleteConfirmationModal = () => {
      setDisplayDeleteConfirmationModal(false);
    };
    const detailsLink = (cell: any, row: any, rowIndex: number, formatExtraData: any) => {
      return (
        <>
        <Button
          onClick={() => {
            editAppConfiurationDetails(row as ConfigurationModel) ;
          }}
        >
          Edit
        </Button>
        <Button
          onClick={() => {
            showAppConfiurationDetails(row as ConfigurationModel);
          }}
        >
          Config
        </Button>
        <Button
          onClick={() => {
            showDeleteModal(row as ConfigurationModel);
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
      // navigate("/integrator/configuration-form-create")
      router.push('/integrator/configuration-form-create');
    }
    const editAppConfiurationDetails = (row: ConfigurationModel) => {
      // navigate("/integrator/configuration-form-edit", {state: {...row}})
      dispatch({ type: "set_current_row", currentRow: {...row}});
      // router.push('/integrator/configuration-form-edit', {state: {...row}});
      router.push('/integrator/configuration-form-edit');
    }
    const showAppConfiurationDetails = (row: ConfigurationModel) => {
      // navigate("/integrator/configuration-app-details", {state: {...row}}) 
      console.log("Will show details", row);
      dispatch({ type: "set_current_row", currentRow: {...row}});
      router.push('/integrator/configuration-app-details');
    }
    
    const showImportConfigurationModal = () => {
      setDisplayImportConfirmationModal(true);
    }

    const showDeleteModal = (row: ConfigurationModel|null) => {
      setDeleteRow(() => row);
      setDisplayDeleteConfirmationModal(() => true);
    }

    async function deleteAppConfiurationDetails(row: ConfigurationModel|null) {
      console.log('delete app profile', row)
      try {
          const requestOptions = {
              method: 'DELETE',
              headers: { 'Content-Type': 'application/json', 'Accept': '*/*' },
              body: JSON.stringify([{application: row?.application, profile: row?.profile}]),
          };
          setLoading(() => true);
          const apiResponse = await fetch("/api/integrator/configurations", requestOptions);
          // if (!apiResponse.ok()) {
          //   router.replace(`/${CURRENT_PAGE}#topOfErrors`);
          // } else {
            const data = await apiResponse.json() as ConfigurationModel[];
            dispatch({ type: "set_configurations", configurations: data });
            setConfigurations(() => data);
            console.log(states.configurations);
            setLoading(() => false);
            setDisplayDeleteConfirmationModal(() => false);
            console.log("deleted");
          // }
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
    
    const paginationOptions = configurations.length > 0 ? {
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
      }, {
        text: 'All', value: configurations.length
      } ]
    } : {
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
      totalSize: 3,
      sizePerPageList: [ {
        text: '5', value: 5
      }, {
        text: '10', value: 10
      }, {
        text: '30', value: 10
      } ]
    };
    useEffect(() => {
      console.log(">>>>>>>>>wii load data:>>>>>>> ", configurations);
      if (configurations === null || configurations.length === 0) {
        setLoading(() => true);
      }
      const requestOptions = {
          method: 'GET',
          headers: {'Accept': '*/*' },    
      }; 
      fetch('/api/integrator/configurations', requestOptions)
        .then(response => response.json())
        .then((data: ConfigurationModel[]) => {
          console.log(">>>>>>>>>>>>>>>> configuration data>>>", data);
          dispatch({ type: "set_configurations", configurations: data });
          setConfigurations(() => data);          
          console.log(">>>>>>>>> config data:>>>>>>> ", states.configurations);
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
                                  {configurations.length > 0 && <BootstrapTable
                                    bootstrap4
                                    keyField="key"
                                    data={configurations}
                                    columns={configurationTableColumns}
                                    pagination={paginationFactory(paginationOptions)}
                                    noDataIndication={"Table is empty"}
                                  />}
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
