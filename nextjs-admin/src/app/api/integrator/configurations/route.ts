import apiRequest  from '@/utils/ApiRequest';
import { maskData, maskString, maskArguments } from 'data-guardian';
import log4js from 'log4js';
import { ConfigurationModel } from '../../../../type/ConfigurationModel';
import * as oracleDBUtil from '@/utils/oracleDBUtil'
import * as configurationRepo from '@/repo/ConfigurationRepo';
import { logger } from "@/logger";
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
    const customMaskingConfig = {
      keyCheck: (key) => {
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
  } catch (err) {
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
    const data = request.body as ConfigData [];
    console.log(`import data: `, JSON.stringify(data));
    await configurationRepo.saveAll(data);
    console.log(
      `POST ${contextPath} result is successful `,
    );

    return res.status(201).json({success: true});
  } catch (err) {
    return res.status(500).json(err);
  }
}

export async function PUT(request: Request) {
  try {
    const data = request.body as ConfigData [];
    console.log(`import data: `, JSON.stringify(data));
    await configurationRepo.deleteProfiles(data);
    await configurationRepo.saveAll(data);
    console.log(
      `POST ${contextPath} result is successful `,
    );

    return res.status(200).json({success: true});
  } catch (err) {
    return res.status(500).json(err);
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
    const configData = request.body as ConfigData [];
    console.log(`import data: `, JSON.stringify(data));
    await configurationRepo.deleteProfiles(configData);
    const responseBody =  await configurationRepo.findAll();
    console.log(
      'GET /api/import  result is: ',
      responseBody,
    );

    return res.status(200).json(responseBody);
  } catch (err) {
    return res.status(500).json(err);
  }
}
