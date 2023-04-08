import { GetStaticProps } from 'next';
import { useRouter } from 'next/router';
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';
import React, { useEffect, useRef } from 'react';
import { useAppDispatch, useSelectorState } from '@/state/hooks';
import useValidatePage from '@/hooks/useValidatePage';
import useHttpRequest from '@/hooks/useHttpRequest';
import ErrorsHeader from '@/s2i/ErrorsHeader';
import S2iLandingPage from '@/s2i/landing/index';

import {
  setFlowStarted,
  setSessionId,
  setTransactionId,
  setHttpError,
  setErrorApi,
} from '@/state/systemSlice';

import {
  isErrorApi,
  HTTP_FAILURE_CODES,
  STEP_LANDING,
  STEP_INQUIRE,
} from '@/utils/constants';

const CURRENT_PAGE = STEP_LANDING;

const LandingPage = () => {
  const dispatch = useAppDispatch();
  const { system } = useSelectorState();
  const router = useRouter();
  const validatePage = useValidatePage(CURRENT_PAGE);

  const httpRequest = useHttpRequest();
  const willMount = useRef(true);
  if (willMount.current) {
    dispatch(setHttpError(null));
    dispatch(setErrorApi(null));
    willMount.current = false;
  }

  const startFlow = async () => {
    console.log('system', system);
    if (!system.sessionId || !system.transactionId) {
      try {
        let result = await httpRequest({
          method: 'post',
          url: '/api/sessionCreate',
          data: {},
          headers: null,
        });

        console.log('result', result);
        if (
          HTTP_FAILURE_CODES.includes(result.status) ||
          isErrorApi(result.data)
        ) {
          console.log('http error', result.status);
        } else {
          const { sessionId } = result.data as any;
          console.log('sessionid', sessionId);
          dispatch(setSessionId(sessionId));
          result = await httpRequest({
            method: 'post',
            url: '/api/transactionCreate',
            data: { sessionId },
            headers: null,
          });

          console.log('result', result);
          if (
            HTTP_FAILURE_CODES.includes(result.status) ||
            isErrorApi(result.data)
          ) {
          } else {
            const { transactionId } = result.data as any;
            console.log('transactionId', transactionId);
            dispatch(setTransactionId(transactionId || ''));
            await router.replace(`/${STEP_INQUIRE}/1`);
          }
        }
      } catch (err) {
        console.error(err);
      }
    } else {
      await router.replace(`/${STEP_INQUIRE}/1`);
    }
  };

  useEffect(() => {
    dispatch(setFlowStarted(true));
  }, []);

  useEffect(() => {
    validatePage();
  }, []);

  return (
    <>
      <ErrorsHeader />
      <S2iLandingPage onClick={startFlow} />
    </>
  );
};

export const getStaticProps: GetStaticProps = async ({ locale }) => {
  return {
    props: {
      ...(await serverSideTranslations(locale, [`common`])),
      locale,
    },
  };
};

export default LandingPage;
