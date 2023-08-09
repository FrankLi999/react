import React from "react";
import {Link} from "react-router-dom";
import {
    Container,
    Card,
    Col,
    Form,
    Row
} from "react-bootstrap";
const Login = () => {
    return (
        <>
            <Container>
                <Row className="justify-content-center">
                    <Col lg="5" md="8">
                        <Card>
                            <Card.Header>
                                <Card.Title as="h4" className="text-center font-weight-light my-4">Login</Card.Title>
                                <p className="card-category">Login to Camel Integrator</p>
                            </Card.Header>
                            <Card.Body>
                                <Form action="" className="form" method="">
                                    <Form.Group>
                                        <label className="small mb-1" htmlFor="inputEmailAddress">Email address</label>
                                        <Form.Control className="form-control py-4" id="inputEmailAddress" type="email" placeholder="Enter email address"
                                        ></Form.Control>
                                    </Form.Group>
                                    <Form.Group>
                                        <label className="small mb-1" htmlFor="inputPassword">Password</label>
                                        <Form.Control className="form-control py-4" id="inputPassword" type="password" placeholder="Enter password"
                                        ></Form.Control>
                                    </Form.Group>
                                    <Form.Check className="form-group">
                                        <Form.Check.Label>
                                        <Form.Check.Input
                                            defaultChecked
                                            type="checkbox"
                                        ></Form.Check.Input>
                                        <span className="form-check-sign"></span>
                                        Remember password
                                        </Form.Check.Label>
                                    </Form.Check>
                                        
                                    </Form>
                            </Card.Body>
                            <Card.Footer className="card-footer text-center">
                                <div className="small form-group d-flex align-items-center justify-content-between mt-4 mb-0">
                                    <Link to="/">
                                    Go to home page
                                    </Link>
                                    <Link to="/" className="btn btn-primary">
                                    Login
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
export default Login;