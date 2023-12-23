import apiRequest  from '@/utils/ApiRequest';

const contextPath = `/api/configurations`;

export async function GET(request: Request) {
  console.log(">>>>>>>>>>>>> /api/configurations>>>> get", request.url);
  const apiResponse = await apiRequest({
    method: 'GET',
    url: contextPath,
    headers: {
      Accept: 'application/json'
    },
  });
  const responseBody = await apiResponse.json();
  console.log(
    `GET ${contextPath} result is: `,
    JSON.stringify(responseBody),
  );

  return new Response(responseBody, {status: apiResponse.status});
}

export async function POST(request: Request) {
  const apiResponse = await apiRequest({
    method: 'POST',
    url: contextPath,
    headers: { 'Content-Type': 'application/json', 'Accept': '*/*' },
    data: request.body
  });
  const responseBody = await apiResponse.json();
  console.log(
    `POST ${contextPath} result is: `,
    JSON.stringify(responseBody),
  );

  return new Response(responseBody, {status: apiResponse.status});
}

export async function PUT(request: Request) {
  const apiResponse = await apiRequest({
    method: 'PUT',
    url: contextPath,
    headers: { 'Content-Type': 'application/json', 'Accept': '*/*' },
    data: request.body
  });
  const responseBody = await apiResponse.json();
  console.log(
    `PUT ${contextPath} result is: `,
    JSON.stringify(responseBody),
  );

  return new Response(responseBody, {status: apiResponse.status});
}

export async function DELETE(request: Request) {
  const apiResponse = await apiRequest({
    method: 'DELETE',
    url: contextPath,
    headers: { 'Content-Type': 'application/json', 'Accept': '*/*' },
    data: request.body
  });
  const responseBody = await apiResponse.json();
  console.log(
    `DELETE ${contextPath} result is: `,
    JSON.stringify(responseBody),
  );

  return new Response(responseBody, {status: apiResponse.status});
}
