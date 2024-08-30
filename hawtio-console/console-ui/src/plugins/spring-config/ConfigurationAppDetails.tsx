import React from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import {
  Button,
  Card,
  CardBody,
  CardFooter,
  Pagination,
  PaginationVariant,
  PageSection,
  PageSectionVariants,
  Text,
  TextContent
} from '@patternfly/react-core';

import { Table, Thead, Tr, Th, Tbody, Td, ThProps } from '@patternfly/react-table';
import { ConfigurationProperty } from "./ConfigurationModel";

function ConfigurationAppDetails() {
  const { state } = useLocation();
  const [page, setPage] = React.useState(1);
  const [perPage, setPerPage] = React.useState(5);
  const [paginatedRows, setPaginatedRows] = React.useState<ConfigurationProperty[]>(state.props.slice((page - 1) * perPage, page * perPage));
  const [activeSortIndex, setActiveSortIndex] = React.useState<number | undefined>(undefined);
  // Sort direction of the currently sorted column
  const [activeSortDirection, setActiveSortDirection] = React.useState<'asc' | 'desc' | undefined>(undefined);
  const navigate = useNavigate();
  const editConfig = () => {
    navigate("/spring-config/edit", {state: {...state}})
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

  const handleSetPage = (
    _evt: React.MouseEvent | React.KeyboardEvent | MouseEvent,
    newPage: number,
    _perPage: number | undefined,
    startIdx: number | undefined,
    endIdx: number | undefined
  ) => {
    setPaginatedRows(state.props.slice(startIdx, endIdx));
    setPage(newPage);
  };

  const handlePerPageSelect = (
    _evt: React.MouseEvent | React.KeyboardEvent | MouseEvent,
    newPerPage: number,
    _newPage: number,
    startIdx: number | undefined,
    endIdx: number | undefined
  ) => {
    setPaginatedRows(state.props.slice(startIdx, endIdx));
    setPerPage(newPerPage);
  };

  const renderPagination = (variant: PaginationVariant) => {
    return (
      <Pagination
        itemCount={state.props.length}
        page={page}
        perPage={perPage}
        onSetPage={handleSetPage}
        onPerPageSelect={handlePerPageSelect}
        variant={variant}
      />
    );
  };

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


  const renderColum = (column: any, row: any) => {
    return <>{row[column['dataField']]}</>
  }

  return (
      <>
      <PageSection variant={PageSectionVariants.light}>
        <TextContent>
          <Text component="h4">Application Configuration: {state.application}/{state.profile} </Text><Button onClick={() => {editConfig();}}>Edit</Button>
        </TextContent>
      </PageSection>
      <PageSection>
        <Card>
          <CardBody>

            <Table variant="compact" aria-label="Spring configurations">
              <Thead>
                <Tr>
                  {appConfigColumns.map((column, columnIndex) => (
                  <Th key={columnIndex} sort={column.sort ? getSortParams(columnIndex) : undefined}>{column.text}</Th>
                  ))}
                </Tr>
              </Thead>
              <Tbody>
                {paginatedRows.map((row: any, rowIndex: number) => (
                <Tr key={rowIndex}>
                  {appConfigColumns.map((column) => (

                  <Td key={column.dataField}>
                  {renderColum(column, row)}
                  </Td>
                  ))}
                </Tr>
                ))}
              </Tbody>
            </Table>
            {renderPagination(PaginationVariant.bottom)}
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
  );
}

export default ConfigurationAppDetails;
