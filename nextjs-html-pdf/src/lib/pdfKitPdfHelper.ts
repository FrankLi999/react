// import { renderToStaticMarkup } from 'react-dom/server';
// import * as htmlPdf from 'html-pdf-chrome';
// import PDFDocument  from 'pdfkit-table';
import PDFDocument from 'pdfkit/js/pdfkit';
import BlobStream from 'blob-stream';

// const PDFDocument = require('pdfkit');
// // const PDFDocument = require('pdfkit-table');
// const blobStream  = require('blob-stream');

const generatePDF = async () => {

  // create a document the same way as above
  
  const doc = new PDFDocument({
    lang: 'en-CA',
    tagged: true,
    displayTitle: true
  });
  // pipe the document to a blob
  const stream = doc.pipe(BlobStream());
  doc.info['Title'] = 'HOT and DDR Invoice';
  doc.info['Author'] = 'GSIC';

  // Initialise document logical structure
  var struct = doc.struct('Document');
  doc.addStructure(struct);

  // Register a font name for use later
  doc.registerFont('Palatino', 'fonts/PalatinoBold.ttf');

  // Set the font and draw some text
  struct.add(
    doc.struct('P', () => {
      doc
        .font('Palatino')
        .fontSize(25)
        .text('Some text with an embedded font! ', 100, 100);
    })
  );


  let col1LeftPos = 50;
  let row1Top = 50;
  let colWidth = 450;
  let row2Top = 150;
  let row3Top = 150;
  let col2LeftPos = colWidth + col1LeftPos + 400;

  // Set the font back and draw tiger line art based on an SVG
  var headerSection = doc.struct('Sect');
  struct.add(headerSection);

  headerSection.add(
    doc.struct('H1', () => {
      doc
        .font('Palatino', 25)
        .text('Rendering logo...', 100, 100)
        .translate(220, 300);
    })
  );

  headerSection.add(
    doc.struct(
      'Figure',
      {
        alt: 'Service Ontaro Lojo. '
      },
      () => {
        doc.image('images/interlaced-rgb-alpha-8bit.png', 100, 160, {
          width: 300
        });
      }
    )
  );


  let introColLeftPos = 650;
  console.log(">>>>>>>>>>>>>>>>>>>>1");
  doc.fontSize(16)   ;
  console.log(">>>>>>>>>>>>>>>>>>>>2");
    doc.text('Invoice #: 123', introColLeftPos, row1Top, {width: colWidth, structParent: headerSection})
    .text('Created: January 1, 2023', introColLeftPos, row2Top, {width: colWidth, structParent: headerSection})
    .text('Due: February 1, 2023', introColLeftPos, row3Top, {width: colWidth, structParent: headerSection})
  headerSection.end();
  console.log(">>>>>>>>>>>>>>>>>>>>3");
  var infoSection = doc.struct('Sect');
  struct.add(infoSection);

  doc.fontSize(16)   
    .text('GSIC', col1LeftPos, row1Top, {width: colWidth, structParent: infoSection})
    .text('12345 HOT Road', col1LeftPos, row2Top, {width: colWidth, structParent: infoSection})
    .text('Toronto, ON P1P1P1', col1LeftPos, row3Top, {width: colWidth, structParent: infoSection})
  doc.fontSize(16)   
    .text('Acme Corp.', col2LeftPos, row1Top, {width: colWidth, structParent: infoSection})
    .text('John Doe.', col2LeftPos, row2Top, {width: colWidth, structParent: infoSection})
    .text('john@example.com', col2LeftPos, row3Top, {width: colWidth, structParent: infoSection})
  infoSection.end();

  doc.end();
  

  // var doc = new PDFDocument({ bufferPages: true });
  // const stream = doc.pipe(BlobStream());
  // doc.addPage();
  // doc.addPage();
  // doc.switchToPage(0);
  // doc.text('First Page', { paragraphGap: 4 });
  // doc.text('Click here to go to Second Page', { link: 1, paragraphGap: 4 });
  // doc
  //   .text('Continued text ', { continued: true })
  //   .text('with a link inside', {
  //     continued: true,
  //     link: 'http://pdfkit.org',
  //     underline: true
  //   })
  //   .text(' and remaining text', {
  //     continued: false,
  //     link: null,
  //     underline: false
  //   });
  // doc.switchToPage(1);
  // doc.text('Go To First Page', { link: 0 });
  // doc.text('Link to google', { link: 'https://www.google.com/' });
  // doc.end();
  
  const blob = await new Promise(resolve => stream.on("finish", () => { 
    resolve(stream.toBlob('application/pdf'));
   }));
  console.log(">>>>>>>>>>>>> get blob:", blob);
  // let notFinished = true;
  // while (notFinished) {
  //   stream.on('finish', function() {
  //     blob = stream.toBlob('application/pdf');
  //     notFinished = false;
  //   });
  // }
  return blob;
};


// Usage
// const htmlContent = '<h1>Hello World</h1><p>This is custom HTML content.</p>';
// generatePDFfromHTML(htmlContent, 'custom.pdf')
//   .then(() => console.log('PDF generated successfully'))
//   .catch(err => console.error('Error generating PDF:', err));


export default {
  // generatePDF,
  generatePDF
}