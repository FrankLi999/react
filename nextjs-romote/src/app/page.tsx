'use client'
import { useState} from 'react';
import AnotherComponent from '@/components/AnotherComponent';
import NextjsRemoteComponent from '@/components/nextjs-remote-component';

export default function Home() {
  const [name, setName] = useState({firstName: 'fn', lastName: 'ln'});
  return (
    <div>
        
        <p>xxxx</p>
        <AnotherComponent firstName ={name.firstName} lastName={name.lastName}/>
        <NextjsRemoteComponent firstName ={name.firstName} lastName={name.lastName} />
        { /* 
        
        
        */}

    </div>
  );
}