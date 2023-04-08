1. Typescript:


   > high oder function: function with parameter as functions

   > Triple-Slash Directives

        Triple-slash directives are single-line comments containing a single XML tag. The contents of the comment are used as compiler directives.
            /// <reference path="..." />
                 It serves as a declaration of dependency between files.
            /// <reference types="..." />
                /// <reference types="node" /> refers to @types/node/index.d.ts
            /// <reference lib="..." />
                /// <reference lib="es2017.string" /> to one of the files in a compilation is equivalent to compiling with --lib es2017.string.

   > Symbol

        Starting with ECMAScript 2015, symbol is a primitive data type, just like number and string.

        symbol values are created by calling the Symbol constructor.


        const getClassNameSymbol = Symbol();
        class C {
            [getClassNameSymbol]() {
                return "C";
            }
        }
        let c = new C();
        let className = c[getClassNameSymbol](); // "C"

   > Utility types:

        Awaited<Type>
        Partial<Type>
            Constructs a type with all properties of Type set to optional. This utility will return a type that 
            represents all subsets of a given type.

                interface Todo {
                    title: string;
                    description: string;
                    }
                
                function updateTodo(todo: Todo, fieldsToUpdate: Partial<Todo>) {
                return { ...todo, ...fieldsToUpdate };
                }
        Required<Type>
            Constructs a type consisting of all properties of Type set to required. 
        Readonly<Type>
        Record<Keys, Type>
        Pick<Type, Keys>
            Constructs a type by picking the set of properties Keys (string literal or union of string literals) from Type.
        
                interface Todo {
                    title: string;
                    description: string;
                    completed: boolean;
                }
                
                type TodoPreview = Pick<Todo, "title" | "completed">;
        Omit<Type, Keys>
            Constructs a type by picking all properties from Type and then removing Keys (string literal or union of string literals).
        Exclude<UnionType, ExcludedMembers>
        Extract<Type, Union>
        NonNullable<Type>
        Parameters<Type>
        ConstructorParameters<Type>
        ReturnType<Type>
        InstanceType<Type>
        ThisParameterType<Type>
        OmitThisParameter<Type>OmitThisParameter<Type>
        ThisType<Type>

   >other advanced types:

    Indexed Access Types
        type Person = { age: number; name: string; alive: boolean };
        type Age = Person["age"];

        type I1 = Person["age" | "name"];
     
        type I1 = string | number
        
        type I2 = Person[keyof Person];
            
        type I2 = string | number | boolean

    Conditional Types:
        interface Animal {
         live(): void;
        }
        interface Dog extends Animal {
           woof(): void;
        }
 
        type Example1 = Dog extends Animal ? number : string;

    keyof / index type: 

        type Point = { x: number; y: number };
        type P = keyof Point

        function choose<U, K extends keyof U>(o: U, propNames: K[]): U[K][] {
            return propNames.map(n => o[n]);
        }



        const obj = {
            a: 1,
            b: 2,
            c: 3
        }
        choose(obj, ['a', 'b'])

        returns 
        [1, 2]

    typeof:
        type Predicate = (x: unknown) => boolean;
        type K = ReturnType<Predicate>;


        function f() {
            return { x: 10, y: 3 };
        }
        type P = ReturnType<typeof f>;

    Map:

        type OptionsFlags<Type> = {
            [Property in keyof Type]: boolean;
        };

        interface DynamicObject<T> {
           [key: string]: T;
        }    


        type FeatureFlags = {
          darkMode: () => void;
            newUserProfile: () => void;
        };
        
        type FeatureOptions = OptionsFlags<FeatureFlags>;
                
            type FeatureOptions = {
                darkMode: boolean;
                newUserProfile: boolean;
            }

        type MappedTypeWithNewProperties<Type> = {
            [Properties in keyof Type as NewKeyType]: Type[Properties]
        }


        type Getters<Type> = {
            [Property in keyof Type as `get${Capitalize<string & Property>}`]: () => Type[Property]
        };
        
        interface Person {
            name: string;
            age: number;
            location: string;
        }
        
               type LazyPerson = {
                    getName: () => string;
                    getAge: () => number;
                    getLocation: () => string;
                } 

        type LazyPerson = Getters<Person>;

        
        let obj: DynamicObject<number> = {
            foo: 1,
            bar: 2
        };
        let key: keyof DynamicObject<number> = 'foo';
        let value: DynamicObject<number>['foo'] = obj[key];

    Template Literal Types
        type EmailLocaleIDs = "welcome_email" | "email_heading";
        type FooterLocaleIDs = "footer_title" | "footer_sendoff";
        
        type AllLocaleIDs = `${EmailLocaleIDs | FooterLocaleIDs}_id`;
    Generics:
        function identity<T>(arg: T): T {
            return arg;
        }

        function identities<T, U>(arg1: T, arg2: U): T {
            return arg1;
        }

        interface Identities<V, W> {
            id1: V,
            id2: W
        }