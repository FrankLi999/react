// import { renderToStaticMarkup } from 'react-dom/server';
import pdf from 'html-pdf';

const componentToPDFBuffer = () => {
  return new Promise((resolve, reject) => {
    // const html = renderToStaticMarkup(component);
    const html = "<html><body><div>pdf test</div></body></html>";
    const options = {
      format: 'A4',
      orientation: 'portrait',
      border: '10mm',
      footer: {
        height: '10mm',
      },
      type: 'pdf',
      timeout: 30000,
    };

    const buffer = pdf.create(html, options).toBuffer((err, buffer) => {
      if (err) {
        return reject(err);
        }
    
        return resolve(buffer);
    });
  });
}

export default {
  componentToPDFBuffer
}