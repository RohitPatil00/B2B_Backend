Postman Runner / Newman

This folder includes a Postman collection and environment for the Hyderabad Backend.

Files
- `hyderabad-backend.postman_collection.json` — Postman collection with requests for Auth, Catalog, Cart, Orders, Profile, Addresses.
- `hyderabad-backend.postman_environment.json` — Environment containing `baseUrl`, `dev_user`, `dev_pass`, `jwt`, etc.
- `run-postman.ps1` — PowerShell script to run the collection using `newman` (or `npx newman` fallback).

Quick run (Windows PowerShell)
1. Ensure Node.js installed if you want to use `npx newman` (optional). To install newman globally:
   ```powershell
   npm install -g newman newman-reporter-html
   ```
2. From this directory run:
   ```powershell
   .\run-postman.ps1
   ```
3. Reports will be written to `postman/reports/newman-report.json` and `postman/reports/newman-report.html`.

Notes
- The collection expects the backend to be running at `http://localhost:8080` (change `baseUrl` in the environment if different).
- To use the OTP flow in dev: call `Auth -> OTP - Request`, copy the `otp` value from the response, then run `Auth -> OTP - Verify` with that value. The collection will store `jwt` and `userId` in the environment where applicable.
