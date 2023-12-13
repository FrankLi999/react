// import Article from '@/components/Article';
// import PDFLayout from '@/components/PDFLayout';
import pdfHelper from '@/lib/pdfHelper';

type GetParams = {
    params: {
      filename: string;
    };
  };
  
  // export an async GET function. This is a convention in NextJS
  export async function GET(req: Request, { params }: GetParams) {
    
    // filename for the file that the user is trying to download
    const filename = params.filename;
  
    // const buffer = await pdfHelper.componentToPDFBuffer(
    //   <PDFLayout><Article/></PDFLayout>
    // );


    const buffer = await pdfHelper.componentToPDFBuffer();


    // return a new response but use 'content-disposition' to suggest saving the file to the user's computer
    return new Response(buffer, {
      headers: {
        "content-disposition": `attachment; filename="${filename}.pdf"`,
        "Content-Type": "application/pdf"
      },
    });
  }