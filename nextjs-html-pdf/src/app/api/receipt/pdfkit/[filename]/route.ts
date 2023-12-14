import pdfHelper from '@/lib/pdfKitPdfHelper';



type GetParams = {
    params: {
      filename: string;
    };
  };
  
  // export an async GET function. This is a convention in NextJS
  export async function GET(req: Request, { params }: GetParams) {
    
    // filename for the file that the user is trying to download
    const filename = params.filename;
  

    // const htmlContent = '<h1>Hello World</h1><p>This is custom HTML content.</p>';
    const blob = await pdfHelper.generatePDF();

    console.log(">>>>>>>>>>>>>>>>> pdf created");
    // return a new response but use 'content-disposition' to suggest saving the file to the user's computer
    return new Response(blob, {
      headers: {
        "content-disposition": `attachment; filename="${filename}.pdf"`,
        "Content-Type": "application/pdf"
      },
    });
  }