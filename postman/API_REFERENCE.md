# Hyderabad Backend — API Reference

Base URL
- Default: `http://localhost:8080` (use `{{baseUrl}}` in Postman environment)

Auth
- Use header `Authorization: Bearer <token>` for authenticated endpoints. Postman collection stores token in `{{jwt}}` after login/OTP verify.
- Note: OTP endpoints return the OTP in the response in the dev profile for testing.

How to use
- Import the Postman collection `hyderabad-backend.postman_collection.json` and the environment `hyderabad-backend.postman_environment.json` found in this `postman/` folder.
- Select the imported environment in Postman so `{{baseUrl}}` and `{{jwt}}` resolve.

Endpoints

## Auth

- POST `/auth/login/password`
  - Auth: public
  - Request JSON: `{"phoneNumber":"9999999999","password":"password"}`
  - Success: `{"accessToken":"<jwt>","userId":123}`
  - curl:
    ```bash
    curl -X POST "{{baseUrl}}/auth/login/password" \
      -H "Content-Type: application/json" \
      -d '{"phoneNumber":"9999999999","password":"password"}'
    ```

- POST `/auth/otp/request`
  - Auth: public
  - Request JSON: `{"phoneNumber":"9999999999"}`
  - Success (dev): `{"phoneNumber":"9999999999","otp":"123456"}`
  - curl:
    ```bash
    curl -X POST "{{baseUrl}}/auth/otp/request" \
      -H "Content-Type: application/json" \
      -d '{"phoneNumber":"9999999999"}'
    ```

- POST `/auth/otp/verify`
  - Auth: public
  - Request JSON: `{"phoneNumber":"9999999999","otp":"123456"}`
  - Success: `{"accessToken":"<jwt>","userId":123}`
  - curl:
    ```bash
    curl -X POST "{{baseUrl}}/auth/otp/verify" \
      -H "Content-Type: application/json" \
      -d '{"phoneNumber":"9999999999","otp":"123456"}'
    ```

- POST `/auth/register-shop`
  - Auth: public (currently)
  - Request JSON: `{"phoneNumber":"9999999999","name":"Owner","businessName":"Shop","email":"owner@example.com","password":"password"}`
  - Success: created seller object (includes `id` and `role":"SELLER"`)
  - curl:
    ```bash
    curl -X POST "{{baseUrl}}/auth/register-shop" \
      -H "Content-Type: application/json" \
      -d '{"phoneNumber":"9999999999","name":"Owner","businessName":"Shop","email":"owner@example.com","password":"password"}'
    ```

- GET `/auth/me`
  - Auth: Bearer required
  - Success: current user object (id, phoneNumber, name, email, role, businessName)
  - curl:
    ```bash
    curl -X GET "{{baseUrl}}/auth/me" -H "Authorization: Bearer <paste-jwt>"
    ```

- POST `/auth/logout` (optional)
  - Auth: Bearer required (no-op in current implementation)

## Categories & Subcategories

- GET `/categories`
  - Auth: public
  - Success: array of categories
  - curl: `curl -X GET "{{baseUrl}}/categories"`

- GET `/categories/{categoryId}/subcategories`
  - Auth: public
  - Success: array of subcategories for given category
  - curl: `curl -X GET "{{baseUrl}}/categories/1/subcategories"`

## Products

- GET `/products`
  - Auth: public
  - Query params supported: `categoryId`, `search`, `page`, `size`
  - curl: `curl -X GET "{{baseUrl}}/products"`

- GET `/products/{productId}`
  - Auth: public
  - curl: `curl -X GET "{{baseUrl}}/products/101"`

## Cart

- GET `/cart`
  - Auth: Bearer required
  - Returns cart summary for current user
  - curl: `curl -X GET "{{baseUrl}}/cart" -H "Authorization: Bearer <jwt>"`

- POST `/cart/items`
  - Auth: Bearer required
  - Request JSON: `{"productId":101,"quantity":2}`
  - curl:
    ```bash
    curl -X POST "{{baseUrl}}/cart/items" \
      -H "Authorization: Bearer <jwt>" \
      -H "Content-Type: application/json" \
      -d '{"productId":101,"quantity":2}'
    ```

- PATCH `/cart/items/{productId}`
  - Auth: Bearer required
  - Request JSON: `{"quantity":3}`
  - curl:
    ```bash
    curl -X PATCH "{{baseUrl}}/cart/items/101" \
      -H "Authorization: Bearer <jwt>" \
      -H "Content-Type: application/json" \
      -d '{"quantity":3}'
    ```

- DELETE `/cart/items/{productId}`
  - Auth: Bearer required
  - curl: `curl -X DELETE "{{baseUrl}}/cart/items/101" -H "Authorization: Bearer <jwt>"`

- DELETE `/cart` (optional)
  - Auth: Bearer required

## Orders

- POST `/orders`
  - Auth: Bearer required
  - Request JSON: `{"paymentMethod":"COD","addressId":10,"notes":"Leave at door"}`
  - curl:
    ```bash
    curl -X POST "{{baseUrl}}/orders" \
      -H "Authorization: Bearer <jwt>" \
      -H "Content-Type: application/json" \
      -d '{"paymentMethod":"COD","addressId":10,"notes":"Leave at door"}'
    ```

- GET `/orders`
  - Auth: Bearer required
  - curl: `curl -X GET "{{baseUrl}}/orders" -H "Authorization: Bearer <jwt>"`

- GET `/orders/{orderId}`
  - Auth: Bearer required
  - curl: `curl -X GET "{{baseUrl}}/orders/1001" -H "Authorization: Bearer <jwt>"`

## Profile / Account

- GET `/profile`
  - Auth: Bearer required
  - curl: `curl -X GET "{{baseUrl}}/profile" -H "Authorization: Bearer <jwt>"`

- PUT `/profile`
  - Auth: Bearer required
  - Request JSON example: `{"name":"New Name","email":"new@example.com"}`
  - curl:
    ```bash
    curl -X PUT "{{baseUrl}}/profile" \
      -H "Authorization: Bearer <jwt>" \
      -H "Content-Type: application/json" \
      -d '{"name":"New Name","email":"new@example.com"}'
    ```

## Addresses

- GET `/addresses`
  - Auth: Bearer required
  - curl: `curl -X GET "{{baseUrl}}/addresses" -H "Authorization: Bearer <jwt>"`

- POST `/addresses`
  - Auth: Bearer required
  - Request JSON example: `{"line1":"Street","city":"Hyderabad","state":"Telangana","pincode":"500001","country":"India","isDefault":true}`
  - curl:
    ```bash
    curl -X POST "{{baseUrl}}/addresses" \
      -H "Authorization: Bearer <jwt>" \
      -H "Content-Type: application/json" \
      -d '{"line1":"Street","city":"Hyderabad","state":"Telangana","pincode":"500001","country":"India","isDefault":true}'
    ```

- PUT `/addresses/{addressId}`
  - Auth: Bearer required
  - curl:
    ```bash
    curl -X PUT "{{baseUrl}}/addresses/10" \
      -H "Authorization: Bearer <jwt>" \
      -H "Content-Type: application/json" \
      -d '{"line1":"New Street","city":"Hyderabad","pincode":"500002","isDefault":false}'
    ```

- DELETE `/addresses/{addressId}`
  - Auth: Bearer required
  - curl: `curl -X DELETE "{{baseUrl}}/addresses/10" -H "Authorization: Bearer <jwt>"`

---

If you want this file to include example responses for every endpoint or to be added to the project README, tell me and I will extend it. The file is saved as `postman/API_REFERENCE.md`.
