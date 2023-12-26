import apiRrequest  from '@/utils/ApiRequest';
import multiparty from "multiparty";
import { NextApiRequest, NextApiResponse } from 'next';
import * as configurationRepo from '@/repo/ConfigurationRepo';
import { ConfigData } from '@/dto/ConfigData';
export async function POST(req: NextApiRequest, res: NextApiResponse) {
  // const contextPath = `/api/imports`;
  // const apiResponse = apiRequest({
  //   method: 'POST',
  //   url: '/api/imports',
  //   headers: { 'Content-Type': 'application/json', 'Accept': '*/*' },
  //   data: request.body
  // });
  try {
    const form = new multiparty.Form();
    const data = await new Promise((resolve, reject) => {
      form.parse(req, function (err, fields, files) {
        if (err) reject({ err });
        resolve({ fields, files });
      });
    }) as ConfigData [];
    console.log(`import data: `, JSON.stringify(data));
    await configurationRepo.deleteProfiles(configData);
    await configurationRepo.saveAll(configData);
    // thenMany(findAll());
  
    const responseBody =  await configurationRepo.findAll();
    console.log(
      'GET /api/import  result is: ',
      responseBody,
    );

    return res.status(200).json(responseBody);
  } catch (err) {
    return res.status(500).json(err);
  }
  // return new Response(responseBody, {status: apiResponse.status});
}
