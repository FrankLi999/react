// import { renderToStaticMarkup } from 'react-dom/server';
import * as htmlPdf from 'html-pdf-chrome';

const generatePDFfromHTML = async (htmlContent: string) => {
  
    const options: htmlPdf.CreateOptions = {
      port: 29222, // port Chrome is listening on
      printOptions: {
        generateTaggedPDF: true
      }
    };
    const pdf = await htmlPdf.create(htmlContent, options);
    const buffer = pdf.toBuffer();  
    return buffer;
};


// Usage
// const htmlContent = '<h1>Hello World</h1><p>This is custom HTML content.</p>';
// generatePDFfromHTML(htmlContent, 'custom.pdf')
//   .then(() => console.log('PDF generated successfully'))
//   .catch(err => console.error('Error generating PDF:', err));


export default {
  // generatePDF,
  generatePDFfromHTML
}