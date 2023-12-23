// import https from 'https';
// import http from 'http';

const apiRequest = async (params: {
    method: string,
    url: string, 
    data?: any,
    headers?:Record<string, string> | Headers | [string, string][]
}) => {

    // const requestOptions = {
    //     method: 'DELETE',
    //     headers: { 'Content-Type': 'application/json', 'accept': '*/*' },
    //     body: JSON.stringify([{application: row.application, profile: row.profile}]),
        //    agent: new require('https').Agent({
        //      cert: fs.readFileSync(certificate_path, "utf8"),
        //      key: fs.readFileSync(private_key_path, "utf8")
        //    })
    // };
    // process.env['NODE_TLS_REJECT_UNAUTHORIZED'] = '0';
    // const sslConfiguredAgent = new https.Agent({
    // //   cert: fs.readFileSync(path.resolve(__dirname, './path/to/public-cert.pem'), `utf-8`,  ),
    // //   key: fs.readFileSync(path.resolve(__dirname, './path/to/private-key.key'), 'utf-8', ),  
    //   rejectUnauthorized: false,
    //   keepAlive: true
    // });
    // const httpAgent = new http.Agent({
    //   keepAlive: true
    // });
    // const httpAgent = null;
    try {
        const requestOptions = {
            method: params.method,
            headers: params.headers,
            // agent: sslConfiguredAgent
        };
        if (params.data) {
            requestOptions.body = (typeof params.data === 'string' || params.data instanceof String) ? params.data: JSON.stringify(params.data);
        }
        const API_BASE_URL = 'API_BASE_URL';
        const apiUrl = `${process.env[API_BASE_URL]}${params.url}`;
        console.log(">>>>>>>>>apiUrl>>>>>>agent", apiUrl);
        const response = await fetch(apiUrl, requestOptions);
        const responseBody = await response.json();
        console.log(">>>>>>>>>response>>---->>>> ok ", response.ok);
        console.log(">>>>>>>>>response>>---->>>> status", response.status);
        console.log(">>>>>>>>>response>>---->>>> statusText", response.statusText);
        if (!response.ok) {
            console.log("API Error Response,", `${response.status} ${response.statusText}`, responseBody);
        }
        return Response.json(responseBody, {status: response.status});
    } catch (err) {
        console.log("Unexpected API Error", err);
        return Response.json({ err }, {status: 500})
    }
};
export default apiRequest;
