import {useState } from "react";
import {useNavigate} from "react-router-dom";
import {
    Alert,
    Button,
    Container,
    Card,
    Col,
    Form,
    InputGroup,
    Row
} from "react-bootstrap";
const Login = () => {
    const navigate = useNavigate();
    const [passwordshow, setpasswordshow] = useState(false);
    const [err, setError] = useState("");
    const [data, setData] = useState({
        "userName": "config",
        "password": "config"
    });
    const APP_OAUTH2_REDIRECT_URI = "/oauth2/redirect";
    const githubLogin = `/oauth2/authorization/github?redirect_uri=${APP_OAUTH2_REDIRECT_URI}`;
    const azureLogin = `/oauth2/authorization/azure?redirect_uri=${APP_OAUTH2_REDIRECT_URI}`;
    const { userName, password } = data;
    const changeHandler = (e: any) => {
        setData({ ...data, [e.target.name]: e.target.value });
        setError("");
    };
    const routeChange = () => {
        // const path = `${import.meta.env.BASE_URL}/integrator/configuration-data`;
        const path = `/integrator/configuration-data`;
        navigate(path);
    };
    const Login = () => {
        // authStateService.login(...data)
        if (data.userName === "config" && data.password === "config") {
            routeChange();
        }
        else {
            setError("The Auction details did not Match");
            setData({
                "userName": "config",
                "password": "config"
            });
        }
    };

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
                                {err && <Alert variant="danger">{err}</Alert>}
                                <Form>
                                    <Form.Group>
                                        <Form.Label className="small mb-1" htmlFor="inputUserName">User Name</Form.Label>
                                        <Form.Control className="form-control py-4" id="inputUserName" type="text" value={userName} onChange={changeHandler}
                                                        required placeholder="Enter user name"
                                        ></Form.Control>
                                    </Form.Group>
                                    <Form.Group>
                                        <Form.Label className="small mb-1" htmlFor="inputPassword">Password</Form.Label>
                                        <InputGroup>
                                            <Form.Control className="form-control py-4" id="inputPassword"
                                                                value={password}
                                                                type={(passwordshow) ? 'text' : "password"} 
                                                                onChange={changeHandler}
                                                                required placeholder="Enter password"
                                            ></Form.Control>
                                            <Button variant='light' className="btn btn-light" type="button" onClick={()=>setpasswordshow(!passwordshow)}
                                                            id="button-addon2"><i className={`${passwordshow ? 'i-eye-line' : 'i-eye-off-line'} align-middle`} aria-hidden="true"></i></Button>
                                        </InputGroup>
                                    </Form.Group>
                                    <Form.Check className="form-group">
                                        <Form.Check.Input defaultChecked type="checkbox"></Form.Check.Input>
                                        <Form.Label className="text-muted fw-normal" htmlFor="defaultCheck1">
                                            <span className="ms-2 form-check-sign">Remember password ?</span>
                                        </Form.Label>
                                    </Form.Check>
                                </Form>
                            </Card.Body>
                            <Card.Footer className="card-footer text-center">
                                <Row className="justify-content-center">
                                    <Col lg="5" md="8">
                                        <Button variant='primary' onClick={Login} size='lg' className="btn btn-primary">Sign In</Button>
                                    </Col>
                                </Row>
                                <Row className="justify-content-center">
                                    <Col lg="5" md="8">
                                        <div className="text-center my-3 authentication-barrier">
                                            <span>OR</span>
                                        </div>
                                    </Col>
                                </Row>
                                <Row className="justify-content-center">
                                    <Col lg="5" md="8" >
                                        <a href={githubLogin}  title="Login with Github">
                                            <i className="i-github"></i>
                                        </a>
                                        <a href={azureLogin}  title="Login with Azure">
                                            <i className="i-azure"></i>
                                        </a>
                                    </Col>
                                </Row>
                            </Card.Footer>
                        </Card>

                    </Col>
                </Row>
            </Container>
        </>
    );
}
export default Login;