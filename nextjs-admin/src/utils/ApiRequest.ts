import https from 'https';
import http from 'http';

const apiRequest = async (params: {
    method: string,
    url: string, 
    data: any,
    headers:Record<string, string> | Headers | [string, string][]
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
    
    const httpsAgent = new https.Agent({
      rejectUnauthorized: false,
      // keepAlive: true
    });
    // const httpAgent = new http.Agent({
    //   keepAlive: true
    // });
    const httpAgent = null;
    try {
        const requestOptions = {
            method: params.method,
            headers: params.headers,
            body: (typeof params.data === 'string' || params.data instanceof String) ? params.data: JSON.stringify(params.data),
            agent: (url) => url.protocol === 'https' ? httpsAgent : httpAgent
        };
        const apiUrl = `${process.env.API_BASE_URL}/${params.url}`;
        const response = await fetch(apiUrl, requestOptions);
        if (!response.ok) {
            console.log("API Error Response,", `${response.status} ${response.statusText}`, response.json());
        }
        return Response.json(response.json(), {status: response.status});
    } catch (err) {
        console.log("Unexpected API Error", err);
        return Response.json({ err }, {status: 500})
    }
};
export default apiRequest;