FROM node:20

ENV LANG C.UTF-8

COPY . /app/
WORKDIR /app/
RUN rm package-lock.json || true
RUN rm -rf node_modules || true
RUN npm install
ENTRYPOINT [ "npm", "run", "dev"]
