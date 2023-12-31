# Getting Started

### Project:

Backend for the new online coffee place startup business, where customers can order drinks with toppings.
In the meantime, admins can access the most used toppings report and create, update, and delete drinks and toppings.

### Start:

The following guides illustrate how to use some features concretely:

Run:

`docker compose up -d`

url: `http://localhost` Port: `8080`

### Docker:

##### docker-compose:

`docker-compose -f docker-compose.yml up -d`

_it will download all needed requirement for ready service._

## DOC:

#### Swagger:

Address:
`http://localhost:8080/swagger-ui/index.html`

#### Scheme APIs:

| Http Method | Path                                     | Description                       |
|-------------|------------------------------------------|-----------------------------------|
| GET         | /api/v1/products/{id}/                   | Get a product ( drink / topping ) |
| GET         | /api/v1/products/all/                    | Get All products                  |
| POST        | /api/v1/products/admin/create/           | Create product by admin           |
| PUT         | /api/v1/products/admin/{id}/edit/        | Update product by admin           |
| DELETE      | /api/v1/products/admin/{id}/delete/      | Delete product by admin           |
|             |                                          |                                   |
| POST        | /api/v1/orders/finalize/{id}/user/       | Register an order                 |
|             |                                          |                                   |
| GET         | /api/v1/reports/admin/topping/most-used/ | Report by most used toppings.     |
|             |                                          |                                   |

### Features:

* _Java_ 17
* _Springboot_. 3.1.1
* _Maven_.
* _Swagger_.
* _Api Tests + Service Tests [ { **26 unit test** } ]_
* _Dockerized - Docker + Docker Compose_.
* _jacoco_.
* _CheckStyle_.
* _HTTP request all provided { product, order, report }_
* _Exception Handling according to Springboot 3_





