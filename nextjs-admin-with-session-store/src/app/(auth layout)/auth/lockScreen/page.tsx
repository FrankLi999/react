"use client"
import Link from "next/link";
import React from "react";

import {
    Card,
    Container,
    Col,
    Form,
    Row
} from "react-bootstrap";

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
                            <Card.Title as="h4">Integrator</Card.Title>
                            <Form.Group>
                                <Form.Control
                                    placeholder="Enter Password"
                                    type="password"
                                ></Form.Control>
                            </Form.Group>
                        </Card.Body>
                        <Card.Footer className="card-footer text-center">
                            <div className="small form-group d-flex align-items-center justify-content-between mt-4 mb-0">
                                <Link href={`/`} className="btn btn-lg btn-primary btn-rounded">
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
