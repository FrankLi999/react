// import { renderToStaticMarkup } from 'react-dom/server';
import puppeteer from 'puppeteer';



const generatePDF = (url: string) => 
  new Promise(async (resolve, reject) => {    
    console.log("Will lunch browser....");
    const browser = await puppeteer.launch();
    console.log("Will lunched browser....");
    const page = await browser.newPage();
    await page.goto(url);
    page.pdf({ tagged: true, format: "A4" })
      .then(async (pdf) => {
        console.log("generated pdf ");
        await browser.close();
        return resolve(pdf);
      },
      async (err) => {
        console.error("Failed to generated PDF: ", err);
        // await browser.close();
        return reject(err);
      });
  });

// Usage
// generatePDF('https://google.com', 'google.pdf')
//   .then(() => console.log('PDF generated successfully'))
//   .catch(err => console.error('Error generating PDF:', err));


// const generatePDFfromHTML = (htmlContent: string) =>
//   new Promise(async (resolve, reject) => {  
//     console.log("Will lunch browser....");
//     const browser = await puppeteer.launch();
//     console.log("Will lunched browser....");
//     const page = await browser.newPage();
//     await page.setContent(htmlContent);
//     page.pdf({ tagged: true, format: "A4" })
//       .then(pdf => {
//         console.log("generated pdf ");
//         // await browser.close();
//         return resolve(pdf);
//       },
//       err => {
//         console.error("Failed to generated PDF: ", err);
//         // await browser.close();
//         return reject(err);
//       });
//   });

  const generatePDFfromHTML = async (htmlContent: string) => {
  
    console.log("Will lunch browser....");
    const browser = await puppeteer.launch();
    console.log("Will lunched browser....");
    const page = await browser.newPage();
    await page.setContent(htmlContent);
    const pdf =await  page.pdf({ tagged: true, format: "A4" });
    await browser.close();
    return pdf;
  };

// Usage
// const htmlContent = '<h1>Hello World</h1><p>This is custom HTML content.</p>';
// generatePDFfromHTML(htmlContent, 'custom.pdf')
//   .then(() => console.log('PDF generated successfully'))
//   .catch(err => console.error('Error generating PDF:', err));


export default {
  generatePDF,
  generatePDFfromHTML
}