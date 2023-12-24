'use client'
import Link from "next/link";
import {
    Container,
    Card,
    Col,
    Row
} from "react-bootstrap";
function AppHome() {
    return (

        <>
            <Container>
                <Row className="justify-content-center">
                    <Col lg="10" md="8">
                        <Card>
                            <Card.Header>
                                <Card.Title as="h4" className="text-center font-weight-light my-4">Refresh Configuration Data</Card.Title>
                            </Card.Header>
                            <Card.Body>
                                <div className="text-center">
                                    <p className="mb-5  text-muted text-18">
                                        Place holder: need to make a post call like: <br />
                                        curl -H "Content-Type: application/json" -d {} http://my-camel-integrator:8080/actuator/refresh
                                    </p>
                                </div>
                            </Card.Body>
                            <Card.Footer className="card-footer text-center">
                                <div className="small form-group d-flex align-items-center justify-content-between mt-4 mb-0">
                                    <Link href={`/integrator/configuration-data`}> Go to home page </Link>
                                </div>                                
                            </Card.Footer>
                        </Card>

                    </Col>
                </Row>
            </Container>            
        </>

    );

}
export default AppHome;