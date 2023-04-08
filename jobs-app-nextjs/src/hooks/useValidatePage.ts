import { useRouter } from 'next/router';
import { useSelectorState } from '@/state/hooks';
import { isStepReady } from '@/s2i/s2iUtils';

const useValidatePage = (page: string) => {
  const router = useRouter();
  const { system, s2i } = useSelectorState();

  return () => {
    let redirectToHome = false;
    if (!isStepReady(page, system, s2i)) {
      redirectToHome = true;
    }
    if (redirectToHome) {
      router.replace('/');
    }
  };
};

export default useValidatePage;
