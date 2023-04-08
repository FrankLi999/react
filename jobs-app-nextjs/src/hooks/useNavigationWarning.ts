import { useRouter } from 'next/router';
import { useAppDispatch } from '@/state/hooks';

const useNavigationWarning = () => {
  const router = useRouter();
  const dispatch = useAppDispatch();

  //return () => console.log('useNavigationWarning is disabled');

  return () => {
    const warningText = 'Are you sure you wish to leave this page?';
    const handleWindowClose = (e: BeforeUnloadEvent) => {
      //if (!unsavedChanges) return;
      e.preventDefault();
      return (e.returnValue = warningText);
    };
    const handleBrowseAway = () => {
      //if (!unsavedChanges) return;
      //console.log(router.asPath);
      if (router.asPath.includes('step') || window.confirm(warningText)) return;
      router.events.emit('routeChangeError');
      throw 'routeChange aborted.';
    };
    window.addEventListener('beforeunload', handleWindowClose);
    router.events.on('routeChangeStart', handleBrowseAway);

    window.onbeforeunload = (e) => {
      //console.log('>>> onbeforeunload called');
    };
    window.onunload = (e) => {
      // dispatch(deleteSelectedServices());
    };

    return () => {
      window.removeEventListener('beforeunload', handleWindowClose);
      router.events.off('routeChangeStart', handleBrowseAway);
    };
  };
};

export default useNavigationWarning;
