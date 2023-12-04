import * as React from 'react';

interface NextJsRemote1Props {
    firstName: string,
    lastName: string,
    // setDetails: (details: any) => void 
 };
const AnotherComponent: React.FC<NextJsRemote1Props> = (props) => {
  return (
    <p>Hello from Remote Nextjs component. {props.firstName}</p>    
  );
};

export default AnotherComponent;