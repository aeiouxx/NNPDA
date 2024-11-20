FROM node:18-alpine

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm install

COPY . .

RUN npm run build
RUN npm install -g server
EXPOSE 5000

CMD ["server", "-s", "build", "-l", "5000"]
