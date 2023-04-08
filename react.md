https://learning.oreilly.com/library/view/learn-react-with/9781804614204/

custom hook:
    https://react.dev/reference/react
    https://blog.bitsrc.io/step-by-step-guide-on-building-a-custom-react-hook-in-typescript-167e243045a4
    Custom hooks let us abstract away the logic of React components and re-use them! I suggest only doing this with logic that actually gets reused a ton throughout your web application.

        A custom Hook is a JavaScript function whose name starts with ”use” and that may call other Hooks.

    If you have code in a component that you feel would make sense to extract, either for reuse elsewhere or to keep the component simpler, you can pull that out into a function. 

        // common behavir to set page title
        function useTitle(title) {
            useEffect(() => {
                document.title = title;
            }, [title]);
        }

# create a project
    npx create-react-app myreactapp --template typescript
    npm i -D prettier
    npm i -D eslint-config-prettier eslint-plugin-prettier

    add "plugin:prettier/recommended" to eslint section in package.json:
        "eslintConfig": {
            "extends": [
            "react-app",
            "react-app/jest",
            "plugin:prettier/recommended"
            ]
        },
    .prettierrc.json
        {
            "printWidth": 100,
            "singleQuote": true,
            "semi": true,
            "tabWidth": 2,
            "trailingComma": "all",
            "endOfLine": "auto"
        }


# react hooks

    The rules of Hooks
        There are some rules that all Hooks must obey:

            A Hook can only be called at the top level of a function component. So, a Hook can’t be called in a loop or in a nested function such as an event handler.
            A Hook can’t be called conditionally.
            A Hook can only be used in function components and not class components.


    Using the effect Hook
            Fetching data using the effect Hook
        The effect Hook is used for component side effects. A component side effect is something executed outside the scope of the component such as a web service request.

            function SomeOtherComponent({ search }) {
                useEffect(() => {
                    console.log("An effect dependent on a search prop",       search);
                }, [search]);
                Return ...;
            }


            with cleanup:
                function ExampleComponent({ onClickAnywhere }) {
                    useEffect(() => {
                        function handleClick() {
                            onClickAnywhere();
                        }
                        document.addEventListener("click", listener);
                        return function cleanup() {
                            document.removeEventListener("click", listener);
                        };
                    });
                    return ...;
                }

            fetch data:
                export function PersonScore() {
                    useEffect(() => {
                        getPerson().then((person) => console.log(person));
                    }, []);
                    return null;
                }

                useEffect(async () => {
                    const person = await getPerson();
                    console.log(person);
                }, []);

            
    Using state/reducer Hooks - for component local state
         
        const [state, setState] = useState(initialState);

        useReducer is an alternative method of managing state. It uses a reducer function for state changes, which takes in the 
        current state value and returns the new state value.
               const [state, dispatch] = useReducer(reducer, initialState);
    
                function reducer(state: State, action: Action): State {
                    switch (action.type) {
                        case 'add':
                            return { ...state, total: state.total + action.amount };
                        case ...
                        ...
                        default:
                            return state;
                    }
                }


                useEffect(() => {
                    getPerson().then(({ name }) =>
                        dispatch({ type: 'initialize', name })
                    );
                }, []);

                <button onClick={() => dispatch({ type: 'increment' })}>
                    Add
                </button>

    Using the ref Hook
        
        The ref Hook is called useRef and it returns a variable whose value is persisted for the lifetime of a component. This means 
        that the variable doesn’t lose its value when a component re-renders.

            const ref = useRef(initialValue);
            const ref = useRef<Ref>(initialValue);

            The value of the ref is accessed via its current property:

                console.log("Current ref value", ref.current);

            The value of the ref can be updated via its current property as well:

                ref.current = newValue;    

            A common use of the useRef Hook is to access HTML elements imperatively. HTML elements have a ref attribute in JSX that can be assigned to a ref. The following is an example of this:

                function MyComponent() {
                    const inputRef = useRef<HTMLInputElement>(null);
                    function doSomething() {
                        console.log(
                        "All the properties and methods of the input",
                        inputRef.current
                        );
                    }
                    return <input ref={inputRef} type="text" />;
                }


                useEffect(() => {
                    getPerson().then(({ name }) => {
                        dispatch({ type: 'initialize', name });
                        addButtonRef.current?.focus();
                    });
                }, []);
    Using the memo Hook
        The memo Hook creates a memoized value and is beneficial for values that have computationally expensive calculations. The Hook is 
        called useMemo and the syntax is as follows:

            const memoizedValue = useMemo(() => expensiveCalculation(), []);

            const memoizedValue = useMemo(
                () => expensiveCalculation(a, b),
                [a, b]
            );

    Using the callback Hook
        The callback Hook memoizes a function so that it isn’t recreated on each render.

            const memoizedCallback = useCallback(
                () => someFunction(a, b),
                [a, b]
            );
