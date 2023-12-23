import apiRrequest  from '@/utils/ApiRequest';

export async function GET() {
  const contextPath = `/api/imports`;
  const apiResponse = apiRequest({
    method: 'GET',
    url: '/api/import',
    headers: {
      Accept: 'application/json'
    },
  });

  console.log(
    '/api/import]  result is: ',
    JSON.stringify(apiResponse.json()),
  );

  return new Response(apiResponse.json(), {status: apiResponse.status()});
}
