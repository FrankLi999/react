import React from 'react';
// import logo from './logo.svg';
// import './App.css';
import Container from 'react-bootstrap/Container';

import ButtonsShowcase from './showcases/Buttons';
import ToastsShowcase from './showcases/Toasts';
function App() {
  return (
    <div className="App">
      {/* <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.tsx</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header> */}
      <Container className="p-3">
        <Container className="p-5 mb-4 bg-light rounded-3">
          <h1 className="header">
            Welcome To React-Bootstrap TypeScript Example
          </h1>
        </Container>
        <h2>Buttons</h2>
        <ButtonsShowcase />
        <h2>Toasts</h2>
        <ToastsShowcase />
      </Container>
    </div>
  );
}

export default App;
