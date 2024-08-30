import React, { useEffect, useState } from 'react';
import { Route, Routes} from 'react-router-dom';
import { CookiesProvider } from 'react-cookie';
import {MyConfigProvider} from "../../utils/config/context";

// import { useCookies } from 'react-cookie';
import {
  Button,
  Card,
  Divider,
  Pagination,
  PaginationVariant,
  PageSection,
  PageSectionVariants,
  Text,
  TextContent,
  PageGroup
} from '@patternfly/react-core';

import { Table, Thead, Tr, Th, Tbody, Td, ThProps } from '@patternfly/react-table';
import { useNavigate } from "react-router-dom";
// import { useErrorBoundary } from '../../utils/error-boundary/useErrorBoundary';
import axios from 'axios';
import { ConfigurationModel } from "./ConfigurationModel";
import DeleteConfirmation from "./DeleteConfirmation";
import ImportConfiguration from './ImportConfiguration';
import ConfigurationDataEditForm from './ConfigurationDataEditForm';
import ConfigurationDataCreateForm from './ConfigurationDataCreateForm';
import ConfigurationAppDetails from './ConfigurationAppDetails';
import ErrorBoundaryContextProvider from '../../utils/error-boundary/ErrorBoundaryContextProvider';
import ErrorBoundary from '../../utils/error-boundary/ErrorBoundary';
// interface Translation {
//   [key: string]: any;
// }

export const SpringConfig: React.FunctionComponent = () => {
    // const [cookies] = useCookies(['XSRF-TOKEN']);
    // const { showBoundary } = useErrorBoundary();
    const [displayDeleteConfirmationModal, setDisplayDeleteConfirmationModal] = useState(false);
    const [displayImportConfirmationModal, setDisplayImportConfirmationModal] = useState(false);
    const [displayImportSqlConfirmationModal, setDisplayImportSqlConfirmationModal] = useState(false);
    const [deleteRow, setDeleteRow] = useState<ConfigurationModel|null>(null);
    const [configurations, setconfigurations] = useState<ConfigurationModel[]>([]);
    const [loading, setLoading] = useState(false);
    const [page, setPage] = React.useState(1);
    const [perPage, setPerPage] = React.useState(10);
    const [paginatedRows, setPaginatedRows] = React.useState<ConfigurationModel[]>([]);
    const [activeSortIndex, setActiveSortIndex] = React.useState<number | undefined>(undefined);
    // Sort direction of the currently sorted column
    const [activeSortDirection, setActiveSortDirection] = React.useState<'asc' | 'desc' | undefined>(undefined);
    const navigate = useNavigate(); 
    console.log("1111111111111111111>>>", paginatedRows)
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
              'content-type': 'multipart/form-data',
              // 'X-XSRF-TOKEN': cookies['XSRF-TOKEN']
          },
      };
      axios.post("/my-camel/admin/api/spring-config/import", formData, config).then((response) => {
          console.log(">>>>>>1>>>>>>>>", response.data);
          setconfigurations(() => response.data);
          setPaginatedRows(() => configurations.slice((page - 1) * perPage, page * perPage));
          setDisplayImportConfirmationModal(() => false);
      }).catch((error) => {
          console.error("Error uploading spring config: ", error);
          // showBoundary(error);
          setDisplayImportConfirmationModal(() =>false);
      });
    }

    const importSqlConfigurations = (configIle: File) => {
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
      //     await fetch("/s2i-integrator/config/spring/admin/api/configurations/json", requestOptions);
      // } catch (err) {
      //     console.log(err);
      // }
      const config = {
          headers: {
              'content-type': 'multipart/form-data',
              'accept': '*/*',
              // 'X-XSRF-TOKEN': cookies['XSRF-TOKEN']
          },
      };
      axios.post("/my-camel/admin/api/spring-config/sql", formData, config).then((response) => {
          console.log(response.data);
          setconfigurations(() => response.data);
          setPaginatedRows(() => configurations.slice((page - 1) * perPage, page * perPage));
          setDisplayImportSqlConfirmationModal(() => false);
      }).catch((error) => {
          console.error("Error uploading spring config: ", error);
          // showBoundary(error);
          setDisplayImportSqlConfirmationModal(false);
      });
    }
    const hideImportConfigurationModal = () => {
      setDisplayImportConfirmationModal(false);
    };
    const hideImportSqlConfigurationModal = () => {
      setDisplayImportSqlConfirmationModal(false);
    };
    const hideDeleteConfirmationModal = () => {
      setDisplayDeleteConfirmationModal(false);
    };
    const detailsLink = (row: any): JSX.Element => {
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
      navigate("/spring-config/create")
    }
    const editAppConfiurationDetails = (row: ConfigurationModel) => {
      navigate("/spring-config/edit", {state: {...row}})
    }
    const showAppConfiurationDetails = (row: ConfigurationModel) => {
      navigate("/spring-config/details", {state: {...row}}) 
    }
    const showImportConfigurationModal = () => {
      setDisplayImportConfirmationModal(true);
    }

    const showImportSqlConfigurationModal = () => {
      setDisplayImportSqlConfirmationModal(true);
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
              headers: { 
                'Content-Type': 'application/json', 
                'accept': '*/*', 
                // 'X-XSRF-TOKEN': cookies['XSRF-TOKEN'] 
              },
              body: JSON.stringify([{application: row.application, profile: row.profile}]),
          };
          setLoading(() => true);
          await fetch("/my-camel/admin/api/spring-config/configurations", requestOptions)
                    .then(response => response.json())
                    .then((data: ConfigurationModel[]) => {
                      setconfigurations(() => data);
                      setPaginatedRows(() => configurations.slice((page - 1) * perPage, page * perPage));
                      setLoading(() => false);
                      setDisplayDeleteConfirmationModal(() => false);
                    });
          console.log("deleted");
      } catch (err) {
          console.log(err);
          // showBoundary(err);
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

    console.log("222222222222222>>>>>>", paginatedRows)
    const handleSetPage = (
      _evt: React.MouseEvent | React.KeyboardEvent | MouseEvent,
      newPage: number,
      _perPage: number | undefined,
      startIdx: number | undefined,
      endIdx: number | undefined
    ) => {
      setPaginatedRows(configurations.slice(startIdx, endIdx));
      setPage(newPage);
    };
    const handlePerPageSelect = (
      _evt: React.MouseEvent | React.KeyboardEvent | MouseEvent,
      newPerPage: number,
      _newPage: number,
      startIdx: number | undefined,
      endIdx: number | undefined
    ) => {
      setPaginatedRows(configurations.slice(startIdx, endIdx));
      setPerPage(newPerPage);
    };
    const renderPagination = (variant: PaginationVariant) => {
      return (
        <Pagination
          itemCount={configurations.length}
          page={page}
          perPage={perPage}
          onSetPage={handleSetPage}
          onPerPageSelect={handlePerPageSelect}
          variant={variant}
        />
      );
    };
    const renderColum = (column: any, row: any) => {
      if (column.formatter) {
        return <>{column['formatter'](row)}</>
      } else {
        return <>{row[column['dataField']]}</>
      }
    }
    const getSortParams = (columnIndex: number): ThProps['sort'] => ({
      sortBy: {
        index: activeSortIndex,
        direction: activeSortDirection,
        defaultDirection: 'asc' // starting sort direction when first sorting a column. Defaults to 'asc'
      },
      onSort: (_event, index, direction) => {
        setActiveSortIndex(index);
        setActiveSortDirection(direction);
      },
      columnIndex
    });

    useEffect(() => {
      setLoading(() => true);
      fetch('/my-camel/admin/api/spring-config/configurations')
        .then(response => response.json())
        .then((data: ConfigurationModel[]) => {
          setconfigurations(() => data);
          console.log(">>>>>>>>>>>>>>>33333", page, perPage, data);
          setPaginatedRows(() =>data.slice((page - 1) * perPage, page * perPage));
          setLoading(() => false);
        }).catch(error => {
          // showBoundary(error);
          console.log('... load config error', error);
        });
    }, []);
    
    const navItems = [
      { id: 'create', title: 'Create', element: ConfigurationDataCreateForm },
      { id: 'edit', title: 'Edit', element: ConfigurationDataEditForm },
      { id: 'details', title: 'Details', element: ConfigurationAppDetails},
    ]
  
    const routes = navItems.map(({ id, element }) => (
      <Route key={id} path={id} element={React.createElement(element)} />
    ))
  
    console.log(">>>>>>>>paginatedRows>>>>>>>>", paginatedRows);
    console.log(">>>>>>>>configurations>>>>>>>>", configurations);
    console.log(">>>>>>>>perPage>>>>>>>>", perPage);
    console.log(">>>>>>>>page>>>>>>>>", page);

    const springConfigContent = () => {
      return <>
         <PageGroup>
           <PageSection variant={PageSectionVariants.light}>
             <TextContent>
               <Text component="h1">Camel Integrator Configurations</Text>
               <Text component="p">Spring Application profiles</Text>
             </TextContent>
             <div className="small form-group d-flex align-items-center justify-content-start mt-4 mb-0">
                 <Button className="ml-1" onClick={() => {
                     addAppConfiurationDetails();
                   }}>
                   Add Application Profile
                 </Button>
                 <Button className="ml-1" style={{ 'marginLeft': '12px' }} onClick={exportConfig}>Export All Configurations</Button>
                 <Button className="ml-1" style={{ 'marginLeft': '12px' }} onClick={() => { showImportConfigurationModal();}}>Import All Configurations</Button>
                 <Button className="ml-1" style={{ 'marginLeft': '12px' }} onClick={() => { showImportSqlConfigurationModal();}}>Import SQL</Button>
               </div>
           </PageSection>
           <Divider />
           <PageSection>
             <Card>
             <Table variant="compact" aria-label="Spring configurations">
               <Thead>
                 <Tr>
                   {configurationTableColumns.map((column, columnIndex) => (
                     <Th key={columnIndex} sort={column.sort ? getSortParams(columnIndex) : undefined}>{column.text}</Th>
                   ))}
                 </Tr>
               </Thead>
               <Tbody>
               {paginatedRows.map((row: any, rowIndex: number) => (
                 <Tr key={rowIndex}>
                   {configurationTableColumns.map((column) => (
                     <Td key={column.dataField}>
                         {renderColum(column, row)}
                     </Td>
                   ))}
                 </Tr>
               ))}
               </Tbody>
             </Table>
               {renderPagination(PaginationVariant.bottom)}
             </Card>
             </PageSection>
             <Divider />
             </PageGroup>
             <DeleteConfirmation showModal={displayDeleteConfirmationModal} confirmModal={deleteAppConfiurationDetails} hideModal={hideDeleteConfirmationModal}
               row={deleteRow} message={`Are you sure to delete configurations for application for ${deleteRow?.application}/${deleteRow?.profile}`}  />
             <ImportConfiguration showModal={displayImportConfirmationModal} importConfiguration={importConfigurations} hideModal={hideImportConfigurationModal}  />
             <ImportConfiguration showModal={displayImportSqlConfirmationModal} importConfiguration={importSqlConfigurations} hideModal={hideImportSqlConfigurationModal}  />
         <PageSection id='connect-main' variant={PageSectionVariants.light}>
              <Routes>
                {routes}
              </Routes>
         </PageSection>
      </>;
    }
    if (loading) {
      return <p>Loading...</p>;
    } else {
      return (
        <>
          <CookiesProvider>
            <MyConfigProvider>
              <ErrorBoundaryContextProvider>
                <ErrorBoundary>
                  {springConfigContent()}
                </ErrorBoundary>
              </ErrorBoundaryContextProvider>
            </MyConfigProvider>
          </CookiesProvider>

        </>
      );
    }
}