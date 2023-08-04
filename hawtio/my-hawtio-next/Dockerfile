FROM node:18-alpine
RUN mkdir -p /app
WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH
COPY . .
RUN yarn install

EXPOSE 3000
CMD ["yarn", "start"]
