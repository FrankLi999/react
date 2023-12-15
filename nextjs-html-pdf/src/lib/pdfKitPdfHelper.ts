// import { renderToStaticMarkup } from 'react-dom/server';
// import * as htmlPdf from 'html-pdf-chrome';
// import PDFDocument  from 'pdfkit-table';
import PDFDocumentWithTables from '@/lib/pdfkit-table';
import BlobStream from 'blob-stream';

// const PDFDocument = require('pdfkit');
// // const PDFDocument = require('pdfkit-table');
// const blobStream  = require('blob-stream');

const generatePDF = async () => {

  // create a document the same way as above
  
  const doc = new PDFDocumentWithTables({
    lang: 'en-CA',
    tagged: true,
    displayTitle: true,
    pdfVersion: "1.5"
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
        .fontSize(20)
        .text('HOT and DDR Invoice', 100, 90, {align: 'center'});
    })
  );


  doc.font('Palatino').fontSize(11);
  const headerSection = doc.struct('Sect', [
    () => { doc.text('Invoice #: 123', { continued: false }); },
    () => { doc.text('Created: January 1, 2023', { continued: false }); },
    () => { doc.text('Due: February 1, 2023', { continued: false }); },
    doc.struct('Link', () => {
        doc.text("Check online", { link: "http://www.example.com/", continued: false });
    }),
    doc.struct(
      'Figure',
      {
        alt: 'Service Ontaro Lojo. '
      },
      () => {
        doc.image('images/service ontario logo.png', {
          width: 120, height: 30,
          x: 450, y: 120, align: 'right'
        });
      }
    )
  ]);
  headerSection.end();
  struct.add(headerSection);

  console.log(">>>>>>>>>>>>>>>>>>>>2.2");
  // doc.font('Palatino').fontSize(11);
  const infoSection = doc.struct('Sect', [
      () => { doc.text('GSIC', 100, 180,{ continued: false }); },
      () => { doc.text('12345 HOT Road', 100, 190, { continued: false }); },
      () => { doc.text('Toronto, ON P1P1P1', 100, 200, { continued: false }); },
      () => { doc.text('Acme Corp', 440, 180, {align: 'right'}); },
      () => { doc.text('John Doe', 440, 190, { align: 'right'}); },
      () => { doc.text('john@example.com', 440, 200, { align: 'right'}); }
  ]);
  infoSection.end();
  struct.add(infoSection);


const table = {
    title: "Order Details",
    subtitle: "Order Items and Total",
    headers: ["Item", "Cost"],
    rows: [
      ["HOT Application", "$0.00"],
      ["HOT Renewal", "$180.00"],
      ["DDR Registration", "$120.00"],
      ["Total", "$300.00"],
    ],
  };
  const tableSection = doc.struct('Sect');
  struct.add(tableSection);
  
  const orderDetailTable = doc.markStructureContent('Sect');
  tableSection.add(orderDetailTable);
  // const tableSection = doc.struct('Sect', [
  doc.table(table, { 
    // A4 595.28 x 841.89 (portrait) (about width sizes)
    x: 100,
    y: 230,
    width: 400,
    //columnsSize: [ 200, 100, 100 ],
  }); 
  
  tableSection.end();
  // tableSection.end();
  // struct.add(tableSection);

  console.log(">>>>>>>>>>>>>>>>>>>>2.3");
  doc.end();
  
  const blob = await new Promise(resolve => stream.on("finish", () => { 
    resolve(stream.toBlob('application/pdf'));
   }));
  console.log(">>>>>>>>>>>>> get blob:", blob);
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