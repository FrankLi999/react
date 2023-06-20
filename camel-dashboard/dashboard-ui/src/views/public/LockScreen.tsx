import React from "react";

import {
    Button,
    Card,
    Container,
    Col,
    Form,
    Row
} from "react-bootstrap";
import {Link} from "react-router-dom";

function LockScreen() {
    return (
        <>
            <Container>
                <Col className="mx-auto" lg="4" md="8">
                    <Card className="card-lock text-center card-plain">
                        <Card.Header>
                            <Card.Title as="h4" className="text-center font-weight-light my-4">TODO: Lock Screen</Card.Title>
                        </Card.Header>
                        <Card.Body>
                            <Card.Title as="h4">S2I Integrator</Card.Title>
                            <Form.Group>
                                <Form.Control
                                    placeholder="Enter Password"
                                    type="password"
                                ></Form.Control>
                            </Form.Group>
                        </Card.Body>
                        <Card.Footer className="card-footer text-center">
                            <div className="small form-group d-flex align-items-center justify-content-between mt-4 mb-0">
                                <Link to="/" className="btn btn-lg btn-primary btn-rounded">
                                    TODO: Unlock
                                </Link>
                            </div>
                            
                        </Card.Footer>
                    </Card>

                </Col>
            </Container> 
        </>
    );
}

export default LockScreen;