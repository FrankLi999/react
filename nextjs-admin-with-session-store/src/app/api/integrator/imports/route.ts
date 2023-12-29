import * as configurationRepo from '@/repo/ConfigurationRepo';
import { ConfigData } from '@/dto/ConfigData';
export async function POST(request: Request) {
  // const contextPath = `/api/imports`;
  // const apiResponse = apiRequest({
  //   method: 'POST',
  //   url: '/api/imports',
  //   headers: { 'Content-Type': 'application/json', 'Accept': '*/*' },
  //   data: request.body
  // });
  try {
    // const form = new multiparty.Form();
    // const configData = await new Promise((resolve, reject) => {
    //   form.parse(req, function (err, fields, files) {
    //     if (err) reject({ err });
    //     resolve({ fields, files });
    //   });
    // }) as ConfigData [];
     console.log(`import data: 1`);
    const formData = await request.formData();
    console.log(`import data: 2`);
    const dataFile = formData.get('file') as File;
    console.log(`import data: 3`, dataFile);
    const dataconfigJsonString = Buffer.from(await dataFile.arrayBuffer()).toString();
    
    console.log(`import data: `, dataconfigJsonString);
    const configData = JSON.parse(dataconfigJsonString) as ConfigData[];
    // await configurationRepo.deleteProfiles(configData);
    // await configurationRepo.saveAll(configData);
    // thenMany(findAll());
  
    const responseBody =  await configurationRepo.findAll();
    console.log(
      'GET /api/import  result is: ',
      responseBody,
    );

    return new Response(JSON.stringify(responseBody), {status: 200});
  } catch (err: any) {
    return new Response(err, {status: 500});
  }
  // return new Response(responseBody, {status: apiResponse.status});
}
