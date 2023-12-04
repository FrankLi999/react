import * as React from 'react';

interface NextJsRemoteProps {
    firstName: string,
    lastName: string
    // setDetails: (details: any) => void 
 };

const NextjsRemoteComponent: React.FC<NextJsRemoteProps> = (props) => {
  return (
    <p>
      Hello from Remote Nextjs component. First name: {props.firstName}, Last name: {props.lastName}
    </p>
  );
};

export default NextjsRemoteComponent;