
## html-pdf
https://github.com/westy92/html-pdf-chrome


# install pm2 globally
npm install -g pm2
# start Chrome and be sure to specify a port to use in the html-pdf-chrome options.
pm2 start google-chrome \
  --interpreter none \
  -- \
  --headless \
  --disable-gpu \
  --disable-translate \
  --disable-extensions \
  --disable-background-networking \
  --safebrowsing-disable-auto-update \
  --disable-sync \
  --metrics-recording-only \
  --disable-default-apps \
  --no-first-run \
  --mute-audio \
  --hide-scrollbars \
  --disable-web-security \
  --remote-debugging-port=9222

pm2 start "C:\Program Files\Google\Chrome\Application\chrome" --interpreter none -- --headless remote-debugging-port=9222

  pm2 start "C:\Program Files\Google\Chrome\Application\chrome" --interpreter none -- --headless --disable-gpu --disable-translate --disable-extensions --disable-background-networking --safebrowsing-disable-auto-update --disable-sync --metrics-recording-only --disable-default-apps --no-first-run --mute-audio --hide-scrollbars --disable-web-security --remote-debugging-port=29222

https://github.com/marcbachmann/node-html-pdf

## puppeteer
https://apitemplate.io/blog/how-to-convert-html-to-pdf-using-node-js/

https://apitemplate.io/blog/tips-for-generating-pdfs-with-puppeteer/

const puppeteer = require('puppeteer'); 

  const browser = await puppeteer.launch({
  headless: true,
  args: [   '--disable-features=IsolateOrigins',
            '--disable-site-isolation-trials',
            '--autoplay-policy=user-gesture-required',
            '--disable-background-networking',
            '--disable-background-timer-throttling',
            '--disable-backgrounding-occluded-windows',
            '--disable-breakpad',
            '--disable-client-side-phishing-detection',
            '--disable-component-update',
            '--disable-default-apps',
            '--disable-dev-shm-usage',
            '--disable-domain-reliability',
            '--disable-extensions',
            '--disable-features=AudioServiceOutOfProcess',
            '--disable-hang-monitor',
            '--disable-ipc-flooding-protection',
            '--disable-notifications',
            '--disable-offer-store-unmasked-wallet-cards',
            '--disable-popup-blocking',
            '--disable-print-preview',
            '--disable-prompt-on-repost',
            '--disable-renderer-backgrounding',
            '--disable-setuid-sandbox',
            '--disable-speech-api',
            '--disable-sync',
            '--hide-scrollbars',
            '--ignore-gpu-blacklist',
            '--metrics-recording-only',
            '--mute-audio',
            '--no-default-browser-check',
            '--no-first-run',
            '--no-pings',
            '--no-sandbox',
            '--no-zygote',
            '--password-store=basic',
            '--use-gl=swiftshader',
            '--use-mock-keychain']
})

Turning React apps into PDFs with Next.js, NodeJS and puppeteer
https://dev.to/jordykoppen/turning-react-apps-into-pdfs-with-nextjs-nodejs-and-puppeteer-mfi
https://github.com/puppeteer/puppeteer





https://squad.renovationman.com/article/why-we-chose-chromium-to-generate-our-pdf-documents

    app.route('/html-to-pdf').post(express.text(), (req, res) => {
    const html = req.body;
    const filename = Math.random().toString(36).substring(7);
    const filepath = `${__dirname}/${filename}`;
    const htmlfilepath = filepath + '.html';
    const pdffilepath = filepath + '.pdf';
    fs.writeFile(htmlfilepath, html, () => {
        childProc.exec(
        `chromium-browser --headless --use-gl=swiftshader --disable-software-rasterizer --disable-dev-shm-usage --disable-gpu --hide-scrollbars --no-sandbox --print-to-pdf="${pdffilepath}" ${htmlfilepath}`,
        (error, stdout, stderr) => {
            if (error) {
            return res.status(500).send('Chrome command error: ' + error);
            }
            res.sendFile(pdffilepath, () => {
            fs.unlink(pdffilepath, () => {});
            fs.unlink(htmlfilepath, () => {});
            });
        }
        );
    });
    });

## PDFKit
https://pdfkit.org/docs/accessibility.html
https://github.com/foliojs/pdfkit

### html tables
https://github.com/bpampuch/pdfmake
https://github.com/natancabral/pdfkit-table
https://stackoverflow.com/questions/23625988/html-table-in-pdfkit-expressjs-nodejs

## Ref
Server-side PDF generation example using Next.js

[Medium Article - PDF generation with React Componenets using Next.js at Server Side](https://medium.com/@stanleyfok/pdf-generation-with-react-componenets-using-next-js-at-server-side-ee9c2dea06a7)

## Getting Start

```
npm install
npm run dev
```

### To see HTML version:
http://localhost:3000/

### To see PDF version:
http://localhost:3000/?exportPDF=true

