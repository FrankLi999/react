## dev and build

set PUBLIC_URL=/my-camel/admin/dashboard
pnpm run start
pnpm run build

# Error handling 
## react-error-boundary

  ref https://github.com/bvaughn/react-error-boundary.  
    const {showBundary} = useErrorBoundary() for errors from async function, event handlers 
    withErrorBoundary function as High Order Components

    const {resetBundary} = useErrorBoundary() for dismiss the nearest error boundary.
       ```
        import { withErrorBoundary } from 'react-error-boundary'

        function MyComponent() {
        // Your component logic
        }

        const MyComponentWithErrorBoundary = withErrorBoundary(MyComponent, {
        FallbackComponent: ErrorFallback,
        onError: logErrorToService,
        onReset: handleReset,
        resetKeys: ['someKey']
        });

        function App() {
         return <MyComponentWithErrorBoundary someKey={someKey} />
        }
       ```
 
## Error Boundary with fall back ui



No way to write ErrorBoundary as a functional component.  



Error boundary catches only errors that happen during React lifecycle. Things that happen outside of it, like resolved promises, async code with setTimeout, various callbacks and event handlers, will just disappear if not dealt with explicitly.

```
const Component = () => {
  useEffect(() => {
    // this one will be caught by ErrorBoundary component
    throw new Error('Destroy everything!');
  }, [])

  const onClick = () => {
    // this error will just disappear into the void
    throw new Error('Hulk smash!');
  }

  useEffect(() => {
    // if this one fails, the error will also disappear
    fetch('/bla')
  }, [])

  return <button onClick={onClick}>click me</button>
}

const ComponentWithBoundary = () => {
  return (
    <ErrorBoundary>
      <Component />
    </ErrorBoundary>
  )
}
```

The common recommendation here is to use regular try/catch for that kind of errors.
```
const Component = () => {
  const [hasError, setHasError] = useState(false);

  // most of the errors in this component and in children will be caught by the ErrorBoundary

  const onClick = () => {
    try {
      // this error will be caught by catch
      throw new Error('Hulk smash!');
    } catch(e) {
      setHasError(true);
    }
  }

  if (hasError) return 'something went wrong';

  return <button onClick={onClick}>click me</button>
}

const ComponentWithBoundary = () => {
  return (
    <ErrorBoundary fallback={"Oh no! Something went wrong"}>
      <Component />
    </ErrorBoundary>
  )
}
```
## Try...Catch

for synchronous functions,
```
try {
  // if we're doing something wrong, this might throw an error
  doSomething();
} catch (e) {
  // if error happened, catch it and do something with it without stopping the app
  // like sending this error to some logging service
}
```

for async functions,
```
try {
  await fetch('/bla-bla');
} catch (e) {
  // oh no, the fetch failed! We should do something about it!
}
```
or for promise
```
fetch('/bla-bla').then((result) => {
  // if a promise is successful, the result will be here
  // we can do something useful with it
}).catch((e) => {
  // oh no, the fetch failed! We should do something about it!
})
```

bad sample,
```
const SomeComponent = () => {
  const [hasError, setHasError] = useState(false);

  useEffect(() => {
    try {
      // do something like fetching some data
    } catch(e) {
      // oh no! the fetch failed, we have no data to render!
      // don't do that! will cause infinite loop in case of an error
      // see codesandbox below with live example
      setHasError(true);
    }
  })

  // something happened during fetch, lets render some nice error screen
  if (hasError) return <SomeErrorScreen />

  // all's good, data is here, let's render it
  return <SomeComponentContent {...datasomething} />
}
```

## combine ErrorBoundary and Try...Catch

too much additional code
```
const Component = ({ onError }) => {
  const onClick = () => {
    try {
      throw new Error('Hulk smash!');
    } catch(e) {
      // just call a prop instead of maintaining state here
      onError();
    }
  }

  return <button onClick={onClick}>click me</button>
}

const ComponentWithBoundary = () => {
  const [hasError, setHasError] = useState();
  const fallback = "Oh no! Something went wrong";

  if (hasError) return fallback;

  return (
    <ErrorBoundary fallback={fallback}>
      <Component onError={() => setHasError(true)} />
    </ErrorBoundary>
  )
}
```

## Catching async errors with ErrorBoundary
```
const Component = () => {
  // create some random state that we'll use to throw errors
  const [state, setState] = useState();

  const onClick = () => {
    try {
      // something bad happened
    } catch (e) {
      // trigger state update, with updater function as an argument
      setState(() => {
        // re-throw this error within the updater function
        // it will be triggered during state update
        throw e;
      })
    }
  }
}
```

generalize it,
```
const useThrowAsyncError = () => {
  const [state, setState] = useState();

  return (error) => {
    setState(() => throw error)
  }
}
```

use it like,
```
const Component = () => {
  const throwAsyncError = useThrowAsyncError();

  useEffect(() => {
    fetch('/bla').then().catch((e) => {
      // throw async error here!
      throwAsyncError(e)
    })
  })
}
```

or 
```
const useCallbackWithErrorHandling = (callback) => {
  const [state, setState] = useState();

  return (...args) => {
    try {
      callback(...args);
    } catch(e) {
      setState(() => throw e);
    }
  }
}
```
and 
```
const Component = () => {
  const onClick = () => {
    // do something dangerous here
  }

  const onClickWithErrorHandler = useCallbackWithErrorHandling(onClick);

  return <button onClick={onClickWithErrorHandler}>click me!</button>
}
```

# Getting Started with Create React App

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

## Available Scripts

In the project directory, you can run:

### `npm start`

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.\
You will also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.\
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run build`

 set PUBLIC_URL=/my/camel/
 npm run build

### `npm run eject`

**Note: this is a one-way operation. Once you `eject`, you can’t go back!**

If you aren’t satisfied with the build tool and configuration choices, you can `eject` at any time. This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (webpack, Babel, ESLint, etc) right into your project so you have full control over them. All of the commands except `eject` will still work, but they will point to the copied scripts so you can tweak them. At this point you’re on your own.

You don’t have to ever use `eject`. The curated feature set is suitable for small and middle deployments, and you shouldn’t feel obligated to use this feature. However we understand that this tool wouldn’t be useful if you couldn’t customize it when you are ready for it.

## Learn More

You can learn more in the [Create React App documentation](https://facebook.github.io/create-react-app/docs/getting-started).

To learn React, check out the [React documentation](https://reactjs.org/).
