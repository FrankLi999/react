import axios from 'axios'
import { Request, Response } from 'express'
// import https  from 'https';
import { log } from './logger'

const https = require('https');
export async function proxy(uri: string, req: Request, res: Response) {
  log.info(">>>>>>>>>>>> calling proxy1");
  const handleError = (e: string) => {
    log.error(">>>>>>>>>>> eonError1: ", e);
    res.status(500).end(`error proxying to "${uri}: ${e}`)
  }
  delete req.headers.referer
  try {
    // log.error(">>>>>>>>>>> data: ", req.body);
    log.error(">>>>>>>>>>> header: ", req.headers);
    log.error(">>>>>>>>>>> method: ", req.method);
    log.error(">>>>>>>>>>> uri: ", uri);
    // const httpAgent = new https.Agent({ rejectUnauthorized: false });
    // log.error(">>>>>>>>>>> httpAgent: ", httpAgent);
    const res2 = await axios({
      method: req.method,
      url: uri,
      data: req,
      headers: req.headers,
      responseType: 'stream',
      httpsAgent: new https.Agent({ rejectUnauthorized: false }),
    })

    switch (res2.status) {
      case 401:
      case 403:
        log.info(
          'Authentication failed on remote server:',
          res2.status,
          res2.statusText,
          uri
        )
        log.debug('Response headers:', res2.headers)
        res.header(res2.headers).sendStatus(res2.status)
        break
      default:
        if (res2.headers['content-type']) {
          res.header('content-type', res2.headers['content-type'])
        }
        res.status(res2.status)
        log.error(">>>>>>>>>>> res2.status: ", res2.status);
        res2.data.pipe(res).on('error', handleError)
    }
  } catch (error) {
    log.error(">>>>>>>>>>> error1: ", String(error));
    handleError(String(error))
  }
  log.info(">>>>>>>>> done proxy call");
}
