import apiRrequest  from '@/utils/ApiRequest';

export async function GET() {
  const contextPath = `/api/import`;
  const apiResponse = apiRequest({
    method: 'GET',
    url: '/api/import',
    headers: {
      Accept: 'application/json'
    },
  });
  const responseBody = await apiResponse.json();
  console.log(
    'GET /api/import  result is: ',
    JSON.stringify(responseBody),
  );

  return new Response(responseBody, {status: apiResponse.status});
}
