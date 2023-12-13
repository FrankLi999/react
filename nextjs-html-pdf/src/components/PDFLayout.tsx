'use client'
import React from 'react';
import PropTypes from 'prop-types';

const renderPDFFooter = () => (
  <div
    id="pageFooter"
    style={{
      fontSize: '10px',
      color: '#666'
    }}
  >
    This is a sample footer
  </div>
);

const PDFLayout = ({ children }: {
  children: React.ReactNode
}) => (
  <html>
    <head>
      <meta charSet="utf8" />
      <link rel="stylesheet" href="@/public/pdf.css" />
    </head>
    <body>
      {children}
      {renderPDFFooter()}
    </body>
  </html>
);

PDFLayout.propTypes = {
  children: PropTypes.node,
};

export default PDFLayout;
