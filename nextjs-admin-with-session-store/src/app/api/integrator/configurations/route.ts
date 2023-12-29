
import { maskData, IMaskDataOptions }  from '@/lib/data-guard';
import log4js from 'log4js';
import { ConfigData } from "@/dto/ConfigData";
import * as configurationRepo from '@/repo/ConfigurationRepo';
import { logger } from "@/logger";
import { NextRequest } from 'next/server';
const contextPath = `/api/configurations`;
const log = log4js.getLogger("integrator:api:configurations");
// log.level = "debug";
export async function GET(request: Request) {
  logger.info(`>>>>windston>>>>>>>>> /api/configurations>>>> get:${request.url}`);
  log.info(`>>>log4js>>>>>>>>>> /api/configurations>>>> get:${request.url}`);
  // // const apiResponse =
  // return apiRequest({
  //   method: 'GET',
  //   url: contextPath,
  //   headers: {
  //     Accept: 'application/json'
  //   },
  // });
  // console.log('create connection poll >>>>>');
  // await oracleDBUtil.init();
  // console.log('created connection poll >>>>>');
  try {
    const result = await configurationRepo.findAll();
    const resultJson = JSON.stringify(result);
    const customMaskingConfig: Partial<IMaskDataOptions> = {
      keyCheck: (key: string) => {
        // add your custom logic here. Return true for keys you want to mask
        return ['propKey', 'propValue'].includes(key);
      },
      maskingChar: '#',
      maskLength: 3,
  };
    const maskedJsonOutput2 = maskData(result, customMaskingConfig);
    logger.info("winston api configurations >>>>> ");
    logger.info(maskedJsonOutput2);
    log.info("log4js api configurations >>>>> ");
    log.info(maskedJsonOutput2);
    return new Response(resultJson, {status: 200});
  } catch (err: any) {
    return new Response(err, {status: 500});
  }
}

export async function POST(request: Request) {
  // const apiResponse = await apiRequest({
  //   method: 'POST',
  //   url: contextPath,
  //   headers: { 'Content-Type': 'application/json', 'Accept': '*/*' },
  //   data: request.body
  // });
  // const responseBody = await apiResponse.json();
  // console.log(
  //   `POST ${contextPath} result is: `,
  //   JSON.stringify(responseBody),
  // );

  // return new Response(responseBody, {status: apiResponse.status});
  try {
    const data  = await request.json() as ConfigData[];
    console.log(`import data: `, JSON.stringify(data));
    await configurationRepo.saveAll(data);
    console.log(
      `POST ${contextPath} result is successful `,
    );
    return new Response(JSON.stringify({success: true}), {status: 201});
  } catch (err: any) {
    return new Response(err, {status: 500});
  }
}

export async function PUT(request: Request) {
  try {
    const data  = await request.json() as ConfigData[];
    console.log(`import data: `, JSON.stringify(data));
    await configurationRepo.deleteProfiles(data);
    await configurationRepo.saveAll(data);
    console.log(
      `POST ${contextPath} result is successful `,
    );

    return new Response(JSON.stringify({success: true}), {status: 200});
  } catch (err: any) {
    return new Response(err, {status: 500});
  }
  // const apiResponse = await apiRequest({
  //   method: 'PUT',
  //   url: contextPath,
  //   headers: { 'Content-Type': 'application/json', 'Accept': '*/*' },
  //   data: request.body
  // });
  // const responseBody = await apiResponse.json();
  // console.log(
  //   `PUT ${contextPath} result is: `,
  //   JSON.stringify(responseBody),
  // );

  // return new Response(responseBody, {status: apiResponse.status});
}

export async function DELETE(request: Request) {
  // const apiResponse = await apiRequest({
  //   method: 'DELETE',
  //   url: contextPath,
  //   headers: { 'Content-Type': 'application/json', 'Accept': '*/*' },
  //   data: request.body
  // });
  // const responseBody = await apiResponse.json();
  // console.log(
  //   `DELETE ${contextPath} result is: `,
  //   JSON.stringify(responseBody),
  // );

  // return new Response(responseBody, {status: apiResponse.status});

  try {
    const configData  = await request.json() as ConfigData[];
    console.log(`import data: `, JSON.stringify(configData));
    await configurationRepo.deleteProfiles(configData);
    const responseBody =  await configurationRepo.findAll();
    console.log(
      'GET /api/import  result is: ',
      responseBody,
    );

    return new Response(JSON.stringify(responseBody), {status: 200});
  } catch (err: any) {
    return new Response(err, {status: 500});
  }
}
