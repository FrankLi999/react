import apiRequest  from '@/utils/ApiRequest';

const contextPath = `/api/configurations`;

export async function GET() {

  const apiResponse = await apiRequest({
    method: 'GET',
    url: contextPath,
    headers: {
      Accept: 'application/json'
    },
  });

  console.log(
    `GET ${contextPath} result is: `,
    JSON.stringify(apiResponse.json()),
  );

  return new Response(apiResponse.json(), {status: apiResponse.status()});
}

export async function POST(request: Request) {
  const apiResponse = await apiRequest({
    method: 'POST',
    url: contextPath,
    headers: { 'Content-Type': 'application/json', 'Accept': '*/*' },
    body: request.body
  });

  console.log(
    `POST ${contextPath} result is: `,
    JSON.stringify(apiResponse.json()),
  );

  return new Response(apiResponse.json(), {status: apiResponse.status()});
}

export async function PUT(request: Request) {
  const apiResponse = await apiRequest({
    method: 'PUT',
    url: contextPath,
    headers: { 'Content-Type': 'application/json', 'Accept': '*/*' },
    body: request.body
  });

  console.log(
    `POST ${contextPath} result is: `,
    JSON.stringify(apiResponse.json()),
  );

  return new Response(apiResponse.json(), {status: apiResponse.status()});
}

export async function DELETE(request: Request) {
  const apiResponse = await apiRequest({
    method: 'DELETE',
    url: contextPath,
    headers: { 'Content-Type': 'application/json', 'Accept': '*/*' },
    body: request.body
  });

  console.log(
    `POST ${contextPath} result is: `,
    JSON.stringify(apiResponse.json()),
  );

  return new Response(apiResponse.json(), {status: apiResponse.status()});
}
