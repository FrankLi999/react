import React, { useEffect, useState } from 'react';
import { Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from "react-bootstrap-table2-paginator";
import { ConfigurationModel } from "./ConfigurationModel";
import SimpleCard from "../../../components/card/SimpleCard";


function Configurations() {
    const navigate = useNavigate(); 
  
    const [configurations, setconfigurations] = useState<ConfigurationModel[]>([]);
    const [loading, setLoading] = useState(false);
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
        </>
      );
      
    };
    const editAppConfiurationDetails = (row: ConfigurationModel) => {
      navigate("/integrator/configuration-form", {state: {...row}})
    }
    const showAppConfiurationDetails = (row: ConfigurationModel) => {
      navigate("/integrator/configuration-app-details", {state: {...row}}) 
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
      return <SimpleCard title="Integrator Applications">
        <BootstrapTable
          bootstrap4
          keyField="key"
          data={configurations}
          columns={configurationTableColumns}
          pagination={paginationFactory(paginationOptions)}
          noDataIndication={"Table is empty"}
        />
      </SimpleCard>
    }
}      

export default Configurations;