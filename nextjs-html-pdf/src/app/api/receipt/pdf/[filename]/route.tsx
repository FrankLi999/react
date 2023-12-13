import pdfHelper from '@/lib/puppeteerPdfHelper';
// import pdfHelper from '@/lib/pdfHelper';

const htmlContent = `
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title>HOT and DDR invoice</title>

		<style>
			.sr-only {
				border: 0 !important;
				clip: rect(1px, 1px, 1px, 1px) !important; /* 1 */
				-webkit-clip-path: inset(50%) !important;
					clip-path: inset(50%) !important;  /* 2 */
				height: 1px !important;
				margin: -1px !important;
				overflow: hidden !important;
				padding: 0 !important;
				position: absolute !important;
				width: 1px !important;
				white-space: nowrap !important;            /* 3 */
			}
			.invoice-box {
				max-width: 800px;
				margin: auto;
				padding: 30px;
				border: 1px solid #eee;
				box-shadow: 0 0 10px rgba(0, 0, 0, 0.15);
				font-size: 16px;
				line-height: 24px;
				font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;
				color: #555;
			}

			.invoice-box table {
				width: 100%;
				line-height: inherit;
				text-align: left;
			}

			.invoice-box table td {
				padding: 5px;
				vertical-align: top;
			}

			.invoice-box table tr td:nth-child(2) {
				text-align: right;
			}

			.invoice-box table tr.top table td {
				padding-bottom: 20px;
			}

			.invoice-box table tr.top table td.title {
				font-size: 45px;
				line-height: 45px;
				color: #333;
			}

			.invoice-box table tr.information table td {
				padding-bottom: 40px;
			}

			.invoice-box table tr.heading td {
				background: #eee;
				border-bottom: 1px solid #ddd;
				font-weight: bold;
			}

			.invoice-box table tr.details td {
				padding-bottom: 20px;
			}

			.invoice-box table tr.item td {
				border-bottom: 1px solid #eee;
			}

			.invoice-box table tr.item.last td {
				border-bottom: none;
			}

			.invoice-box table tr.total td:nth-child(2) {
				border-top: 2px solid #eee;
				font-weight: bold;
			}

			@media only screen and (max-width: 600px) {
				.invoice-box table tr.top table td {
					width: 100%;
					display: block;
					text-align: center;
				}

				.invoice-box table tr.information table td {
					width: 100%;
					display: block;
					text-align: center;
				}
			}

			/** RTL **/
			.invoice-box.rtl {
				direction: rtl;
				font-family: Tahoma, 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;
			}

			.invoice-box.rtl table {
				text-align: right;
			}

			.invoice-box.rtl table tr td:nth-child(2) {
				text-align: left;
			}
		</style>
	</head>

	<body>
		<div class="invoice-box">
			<table cellpadding="0" cellspacing="0">
				<tr class="top">
					<td colspan="2">
						<table>
							<caption class="sr-only">Invoice information table caption text</caption>	
							<tr>
								<th class="title">
									<img
										src="https://www.ontario.ca/themes/ontario_2021/assets/ontario-logo--desktop.svg"
										alt="Service ontario log alt text"
										style="width: 100%; max-width: 300px"
									/>
								</th>

								<td>
									Invoice #: 123<br />
									Created: January 1, 2023<br />
									Due: February 1, 2023
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<p id="clientInfoTableDesc" class="sr-only">Client information table caption text</p>
				<tr class="information" aria-describedby="clientInfoTableDesc">
					<td colspan="2">
						<table>
							<tr>
								<td>
									GSIC.<br />
									12345 HOT Road<br />
									Toronto, ON P1P1P1
								</td>

								<td>
									Acme Corp.<br />
									John Doe<br />
									john@example.com
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>	
			<table cellpadding="0" cellspacing="0" summary="Order details table summary text">
				<caption class="sr-only">Payment information table caption text</caption>				
				<tr class="heading">
					<th>Order Item</th>
					<th>Check #</th>
				</tr>

				<tr class="details">
					<td>Check</td>

					<td>1000</td>
				</tr>
			</table>	
			<p id="orderDetailTableDesc" class="sr-only">Order Details table caption text.</p>
			<table cellpadding="0" cellspacing="0" summary="Order details table summary text" aria-describedby="orderDetailTableDesc">	
				<tr class="heading">
					<th>Item</th>

					<th>Price</th>
				</tr>

				<tr class="item">
					<td>DDR Registration</td>

					<td>$150.00</td>
				</tr>

				<tr class="item">
					<td>HOT Renewal</td>

					<td>$180.00</td>
				</tr>

				<tr class="item last">
					<td>HOT Application</td>

					<td>$0.00</td>
				</tr>

				<tr class="total">
					<td>Total</td>

					<td>$333.00</td>
				</tr>
			</table>
		</div>
	</body>
</html>
`;

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

    const buffer = await pdfHelper.generatePDFfromHTML(htmlContent);

    console.log(">>>>>>>>>>>>>>>>> pdf created");
    // return a new response but use 'content-disposition' to suggest saving the file to the user's computer
    return new Response(buffer, {
      headers: {
        "content-disposition": `attachment; filename="${filename}.pdf"`,
        "Content-Type": "application/pdf"
      },
    });
  }