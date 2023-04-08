import { useAppDispatch } from '@/state/hooks';
import { setErrorApi, setHttpError } from '@/state/systemSlice';
import axios, { AxiosError, AxiosRequestHeaders } from 'axios';

import { HTTP_FAILURE_CODES, isErrorApi } from '@/utils/constants';

const useHttpRequest = () => {
  const dispatch = useAppDispatch();

  return async (params: {
    method: string;
    url: string;
    data: any;
    headers: AxiosRequestHeaders;
  }) => {
    dispatch(setErrorApi(null));
    dispatch(setHttpError(null));

    let result = null;

    try {
      result = await axios({
        method: params.method,
        url: params.url,
        data: params.data,
        headers: params.headers,
      });
    } catch (error) {
      console.log('>>> error in useHttpRequest', error);

      const err = error as AxiosError;
      if (err.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        const statusCode = err.response.status;
        if (HTTP_FAILURE_CODES.includes(statusCode)) {
          dispatch(setHttpError(err.message));

          return { data: err.message, status: statusCode };
        } else {
          result = err.response;
        }
        if (err.response.data && isErrorApi(err.response.data)) {
          dispatch(setErrorApi(err.response.data));
        }
        //console.log(err.response.status)
        //console.log(err.response.data)
      } else if (err.request) {
        dispatch(setHttpError(err.message));

        // The request was made but no response was received
        // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
        // http.ClientRequest in node.js
        //result = err.request;
        return { data: err.message, status: err.request.status };
      } else {
        dispatch(setHttpError(err.message));

        // Something happened in setting up the request that triggered an Error
        console.log('Error', err.message);
        return { data: err.message, status: 700 };
      }
    }

    return result;
  };
};

export default useHttpRequest;
