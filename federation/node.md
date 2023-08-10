# ref
typescript:
    https://tanbt.medium.com/micro-frontend-implementation-part-2-nested-react-apps-dee8d57b60f3

    https://tanbt.medium.com/micro-frontend-implementation-part-1-react-in-angular-60de386fa2af

    https://stackblitz.com/edit/module-federation-example?file=package.json

    https://dev.to/blessonabraham/building-micro-frontend-with-react-module-federation-3j46
    https://github.com/blessonabraham/micro-frontend-react


    https://ogzhanolguncu.com/blog/micro-frontends-with-module-federation
    https://github.com/ogzhanolguncu/react-typescript-module-federation

js:
https://medium.com/@samho1996/microfrontend-with-module-federation-in-react-98b72b347238

# steps
```
npx create-react-app home-app --template typescript
npx create-react-app header-app --template typescript
```

for each app:

typescript host and container: 

```
npm i -D webpack webpack-cli webpack-dev-server serve html-webpack-plugin babel-loader bundle-loader @babel/core @babel/preset-react @babel/preset-typescript 
```


js: 
```
npm install --save-dev webpack webpack-cli html-webpack-plugin webpack-dev-server babel-loader css-loader babel-loader
```

npm i -D @types/mini-css-extract-plugin @types/react @types/react-dom  

or 

```
typescript: yarn add -D @babel/core @babel/preset-react @babel/preset-typescript autoprefixer babel-loader css-loader file-loader html-webpack-plugin mini-css-extract-plugin postcss postcss-loader style-loader tailwindcss webpack 
js: yarn add -D webpack webpack-cli html-webpack-plugin webpack-dev-server babel-loader css-loader
```

statr home app
```
cd home-app
npm start
```

statr header app
```
cd header-app
npm start
```


# Webpack Configuration

## home-app/webpack.config.js
