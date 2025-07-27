import { generateService } from '@umijs/openapi'

generateService({
  requestLibPath: "import request from '@/request/request'",
  schemaPath: 'http://localhost:8080/v3/api-docs',
  serversPath: './src',
})
