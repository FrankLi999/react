# react-nextjs


What project structure are we using?
What rendering strategy are we using?

    an internal dashboard application: a single-page application is more than enough.

    customer-facing application that should also be public and SEO-friendly, we should think about server-side rendering or static generation, depending on how often the data on the pages are being updated.

    ==================================================
    Server side: Good for SEO
    client side
    static generation: SEO
    ====================================================

What state management solution are we using?
    Redux, MobX, Zustand, Recoil, 

    frequent update: Recoil or Jotai.
    different components to share the same state: then Redux with Redux Toolkit is a good option.

    if we do not have a lot of global states and don’t update it very often:
        then Zustand or React Context API, in combination with hooks, are good choices.

    ======================================================

    Local state: component state
        const [state, setState] = useState(initialState);
    Global State: Zustand
        react hook's Context APIs
    Server State: store response from api, React Query
    Form State: React Hook Form
    URL State
    ======================================================

What styling solution are we using?
    vanilla CSS, SCSS - build time
    utility-first CSS libraries such as Tailwind
    css in js: runtime solution
        Styled Components, 
        Emotion

    Chakra UI - Emotion under the hood

What data fetching approach are we using?

How are we going to handle user authentication?
    httpOnly cookies to prevent cross-site scripting (XSS) attacks.

What testing strategies are we going to use?
    unit test: Jest
    Integration Test: React Test Library
    End-to-End: Cypress


ref:
    https://github.com/PacktPublishing/Learn-React-with-TypeScript-2nd-Edition
    https://github.com/PacktPublishing/React-Application-Architecture-for-Production
    https://www.nodejsdesignpatterns.com/blog/5-ways-to-install-node-js
    https://nextjs.org/docs

    nextjs:
        https://learning.oreilly.com/library/view/real-world-next-js/9781801073493/

micro-front-end communication:

    https://sharvishi9118.medium.com/cross-micro-frontend-communication-techniques-a10fedc11c59

        example: A Catalog and A Cart.

        1. Using props
                build time micro frontend integration pattern as the application needs to render the micro frontends as components only and pass the respective props to those.


                import {Catalog} from "@mfe/catalog";
                import {Cart} from "@mfe/cart";

                const App = () => {
                    const [productsInCartCount, setProductsInCartCount] = useState(0);

                    const addToCart = () => {
                        setProductsInCartCount(productsInCartCount + 1);
                    }

                    return (
                        <>
                            <Catalog onAddToCart={addToCart} />
                            <Cart productsCount={productsInCartCount} />
                        </>
                    )
                }
        2.  Using Platform Storage Apis

            Local Storage in browsers and Async Storage in cross-platform solutions like react native for mobile micro frontends also.

        3. Using Custom Events - good for browser but Not achievable in the case of mobile micro frontends
  
            utilize the browser-inbuilt custom events APIs to publish the events with the data from one micro frontend and the other micro frontends 
            subscribe to the events to get the data. This technique is closest to the events-driven architecture in the microservices world.


                    // Catalog MFE
                    const Catalog = () => {
                        // fetch initial count first
                        const [productsInCartCount, setProductsInCartCount] = useState(initialCount || 0);

                        const addToCart = () => {    
                            const addToCartEvent = new CustomEvent('ADD_TO_CART', { detail: { count: productsInCartCount + 1 } });
                            window.dispatchEvent(addToCartEvent);
                        }

                        return (
                            <Product onAddToCart={addToCart} />
                        )
                    }

                    // Cart MFE
                    const Cart = () => {
                        // fetch initial count first
                        const [productsInCartCount, setProductsInCartCount] = useState(initialCount || 0);

                        useEffect(() => {
                            const listener = ({ detail }) => {
                            setProductsInCartCount(detail.count);
                            }

                            window.addEventListener('ADD_TO_CART', listener)

                            return () => {
                            event.unsubscribe('ADD_TO_CART', listener);
                            }
                        }, []);

                        return (
                            <NumberOfProductsAdded count={productsInCartCount} />
                        )
                    };
        4. Using a custom Message bus

               // Message bus library
                class MessageBus {
                private publishedEventRegistry = {};

                publishEvent(eventName, data) {
                    const registeredEvent = this.publishedEventRegistry[eventName];

                    this.publishedEventRegistry = {
                    ...this.publishedEventRegistry,
                    [eventName]: {
                        ...registeredEvent,
                        name: eventName,
                        data: data
                    }
                    };
                    this.publishedEventRegistry[eventName].handlers.forEach(handler => {
                    handler(this.publishedEventRegistry[eventName].data);
                    });
                }

                subscribeEvent(eventName, handler) {
                    const registeredEvent = this.publishedEventRegistry[eventName];

                    this.publishedEventRegistry = {
                    ...this.publishedEventRegistry,
                    [eventName]: {
                        ...registeredEvent,
                        name: eventName,
                        handlers: [...registeredEvent.handlers, handler]
                    }
                    };
                }

                unsubscribe(eventName) {
                    this.publishedEventRegistry = {
                    ...this.publishedEventRegistry,
                    [eventName]: undefined
                    }
                }
                }

                // App.js
                import {Catalog} from "@mfe/catalog";
                import {Cart} from "@mfe/cart";
                import {MessageBus} from "@mfe-message-bus";

                cont App = () => {
                const messageBus = new MessageBus();

                return (
                    <>
                    <Catalog messageBus={messageBus} />
                    <Cart messageBus={messageBus} />
                    </>
                )  
                }

                // Catalog MFE
                const Catalog = ({ messageBus }) => {
                const addToCart = () => {    
                    // get initial count first
                    const [productsInCartCount, setProductsInCartCount] = useState(initialCount || 0);

                    messageBus.publishEvent('ADD_TO_CART', { count: productsInCartCount + 1 });
                }

                return (
                    <Product onAddToCart={addToCart} />
                )
                }

                // Cart MFE
                const Cart = ({ messageBus }) => {
                // get initial count first
                const [productsInCartCount, setProductsInCartCount] = useState(initialCount || 0);

                useEffect(() => {
                    const listener = (event) => {
                    setProductsInCartCount(event.data.count);
                    }

                    messageBus.subscribeEvent('ADD_TO_CART', listener);

                    return () => {
                    messageBus.unsubscribeEvent('ADD_TO_CART', listener);
                    }
                }, []);

                return (
                    <NumberOfProductsAdded count={productsInCartCount} />
                )
                };
        5. Using Post Message passing in iFrames
=======================
Next.js

    Client-side rendering
    Server-side rendering
    Static site generation
    Incremental static regeneration

Performance optimizations: Next.js is built with web performance in mind. It implements performance optimization techniques such as the following:
    Code splitting
    Lazy loading
    Prefetching
    Image optimization    


npx create-next-app@latest jobs-app-nextjs --typescript    


Folder structure:

    Here is the structure of our src folder:

        - components // (1)
        - config // (2)
        - features // (3)  Contains all the feature-based modules. Application specific code
            auth
                - api // (1)
                - components // (2)
                - types // (3)
                - index.ts // (4)
            jobs

        - layouts // (4)
        - lib // (5)
        - pages // (6)
        - providers // (7)
        - stores or state// (8)
        - testing // (9)
        - types // (10)
        - utils // (11)


    othe folders:
        hook
        state


===========================================================================
Building and Documenting Components In React, everything is a component

    Chakra UI
    Building components
    Storybook
    Documenting components

    

=====================================================================
entry point

    he entry point of this React app is in the index.js file


    import { StrictMode } from 'react';
    import { createRoot } from 'react-dom/client';
    import App from './App';
    const rootElement = document.getElementById('root');
    const root = createRoot(rootElement);
    root.render(
        <StrictMode>
            <!-- App component from the App.js file -->
            <App />
        </StrictMode>
    );


    nextjs

        Next.js is built around the concept of pages. A page is a React Component exported from a .js, .jsx, .ts, or .tsx file in the pages directory. 
        You can even add dynamic route parameters with the filename.

        Inside the pages directory, add the index.js file to get started. This is the page that is rendered when the user visits the root of your 
        application.

======================================================================

Storybook
    Storybook is a tool that allows us to develop and test UI components in isolation. We can think of it as a tool for making catalogs of all the components we have. 
    It is great for documenting components. 

    Component Story Format (CSF)



======================================================================

Next.js routing

        a default Next.js project ships with a pages/ directory. Every file inside that folder represents a new page/route for your application.

        Navigate with:

            router.push
                if (!loggedIn) {

                    router.push('/login')

                }
    page:
        const Page = () => {
            return <div>Welcome to the page!</div>
        }
        export default Page;

        Dynamic User Profile page:
            // pages/users/[userId].tsx
            import { useRouter } from 'next/router';
            const UserProfile = () => {
                const router = useRouter();
                const userId = router.query.userId;
                return <div>User: {userId}</div>;
            }
            export default UserProfile


            To get the ID and load the data dynamically, we can define a generic user profile page in pages/users/[userId].tsx

    Customizing _app.js and _document.js pages

        so that every time we render a page, Next.js will need to run certain operations before sending the resulting HTML to the client. To do that, the framework 
        allows us to create two new files, called _app.js and _document.js, inside our pages/ directory.
            page/_app.js
                take control over page initialization

                import '../styles/globals.css'

                function MyApp({ Component, pageProps }) {

                    return <Component {...pageProps} />

                }

                export default MyApp

            page/_document.ts       

                When we're writing Next.js page components, we don't need to define fundamental HTML tags, such as <head>, <html>, or <body>.
                In order to render those two essential tags, Next.js uses a built-in class called Document, and it allows us to extend it by creating a new file called _document.js 
                inside our pages/ directory, just like we do for our _app.js file:

                        import Document,{

                            Html,

                            Head,

                            Main,

                            NextScript

                        } from 'next/document';

                        class MyDocument extends Document {

                        static async getInitialProps(ctx) {

                            const initialProps =

                            await Document.getInitialProps(ctx);

                            return { ...initialProps };

                        }

                        render() {

                            return (

                            <Html>

                                <Head />

                                <body>

                                <Main />

                                <NextScript />

                                </body>

                            </Html>

                            );

                        }

                        }

                        export default MyDocument;

Next.js rendering strategies

    Client-side rendering
    Server-side rendering: export  getServerSideProps

            // pages/users/[userId].tsx
            import { useRouter } from 'next/router';
            import { getUser } from './api';
            const UserProfile = ({ user }) => {
                const router = userRouter();
                const userId = router.query;
                const { user } = useUser(userId);
                if(!user) return <div>User not found!</div>;
                return <div>User: {user.name}</div>;
            }
            export const getServerSideProps = async ({ params }) => {
                const userId = params.userId;
                const user = await getUser(userId);
                return {
                    props: {
                        user
                    }
                }
            }

    Static site generation
        getStaticProps

    Incremental static regeneration

Next.js SEO
    add some meta tags and the title of the page and inject them into the page. 
    
        // seo.tsx
        import Head from 'next/head';
        export type SeoProps = {
            title: string;
        };
        export const Seo = ({ title }: SeoProps) => {
            return (
                <Head>
                <title>{title}</title>
                </Head>
            );
        };

Layouts

Building the pages


==============================================================================================================================
Context API: https://reactjs.org/docs/context.html.

    a straightforward way to share data between all the components inside a given context without explicitly having to pass it via props from one 
    component to another, even from children to a parent component.

        // components/context/cartContext.js:
        import { createContext } from 'react';

        const ShoppingCartContext = createContext({
            items: {},
            setItems: () => null,
        });

        export default ShoppingCartContext;


        //_app.ts
        import { useState } from 'react';

        import Head from 'next/head';

        import CartContext from

        '../components/context/cartContext';

        import Navbar from '../components/Navbar';

        function MyApp({ Component, pageProps }) {

        const [items, setItems] = useState({});

        return (

            <>

            <Head>

                <link

        href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css"

                rel="stylesheet"

                />

            </Head>

            <CartContext.Provider value={{ items, setItems }}>

                <Navbar />

                <div className="w-9/12 m-auto pt-10">

                <Component {...pageProps} />

                </div>

            </CartContext.Provider>

            </>

        );

        }

        export default MyApp;


        //pages/index.js
        import ProductCard from '../components/ProductCard';

        import products from '../data/items';

        function Home() {

        return (

            <div className="grid grid-cols-4 gap-4">

            {products.map((product) => (

                <ProductCard key={product.id} {...product} />

            ))}

            </div>

        );

        }

        export default Home;

            ----------------------------------------------------------------
                import { useContext } from 'react';

                import cartContext from '../components/context/cartContext';

                function ProductCard({ id, name, price, picture })

                const { setItems, items } = useContext(cartContext);

                const productAmount = id in items ? items[id] : 0;

                // ...

=======================================================================================================
Styled JSX

                export default function FancyButton(props) {

                return (

                    <>

                    <button className="button">{props.children}</button>

                    <style jsx>{`

                        .button {

                        padding: 2em;

                        border-radius: 2em;

                        background: purple;

                        color: white;

                        font-size: bold;

                        border: pink solid 2px;

                        }

                    `}</style>

                    </>

                );

                }


                // global style
                export default function Highlight(props) {

                return (

                    <>

                    <span>{props.text}</span>

                    <style jsx global>{`

                        span {

                        background: yellow;

                        font-weight: bold;

                        }

                    `}</style>

                    </>

                )

                }
CSS modules: bbter than Styled JSX
         https://github.com/css-modules/css-modules.
         
        // added local stypes

        import styles from '../styles/Home.module.css';

        export default function Home() {

            return (

                <div className={styles.homepage}>

                <h1> Welcome to the CSS Modules example </h1>

                </div>

            );

        }


        //global styles
