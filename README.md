# j-WelfareSystem
Welfare Management System Design Document
## 1. Introduction
### 1.1 Purpose
The purpose of this document is to design a welfare management system that provides an organized platform for managing welfare contributions, claims, and disbursements. The system will include three levels of access: Admin (Managing Director), Clerk, and Members, each with distinct roles and responsibilities.

### 1.2 Scope
The system is designed as a microservices-based backend solution using Java Spring Boot, with PostgreSQL as the database. The system will handle:

Member registration and management.
Dependent registration.
Claim submission and approval.
Fund disbursement and replenishment.
Role-based access control and security.
### 1.3 Assumptions
The system will be deployed in a secure environment.
JWT tokens will be used for authentication.
Admin, Clerk, and Members have unique responsibilities that are non-overlapping.
## 2. System Overview
### 2.1 Architecture
The system will use a microservices architecture with two services:

Login Service: Handles user authentication using JWT.
Welfare Service: Manages member data, claims, dependents, fund disbursements, and replenishments.
### 2.2 Databases
Login Database: Stores user credentials and JWT tokens.
Welfare Database: Stores all data related to members, dependents, claims, and fund transactions.
### 2.3 Key Roles
Admin (Managing Director): Disburses funds once claims are approved.
Clerk: Approves member and dependent registrations, validates documents, and approves claims.
Members: Contribute to the welfare fund, add dependents, and submit claims.
## 3. Functional Requirements
### 3.1 User Roles and Permissions
Role	Permissions
Admin	Disburse funds, view claims, manage funds, view financial dashboard
Clerk	Approve members, approve dependents, approve claims, verify documents
Member	Add dependents, submit claims, track claim status, view contributions
### 3.2 Use Cases
### 3.2.1 Admin (Managing Director)
#### Use Case 1: Disburse Funds

After a claim is approved by the clerk, the admin disburses funds.
#### Use Case 2: View Member and Fund Status

Admin can view all claims, fund balances, member contribution statuses, and other financial details via the dashboard.
#### 3.2.2 Clerk
Use Case 1: Approve Member Registration

Clerk verifies data and documents provided by members during registration.
#### Use Case 2: Approve Dependent Registration

Clerk validates the dependents added by members based on the documents provided.
#### Use Case 3: Approve Claims

Clerk reviews death or other event reports and ensures all supporting documents are valid before approving the claim.
### 3.2.3 Member
Use Case 1: Register and Add Dependents

Member registers on the platform and adds dependents (parents, spouse, children) with required documents.
#### Use Case 2: Submit a Claim

Member reports an event (e.g., death) requiring funds and uploads necessary documentation.
#### Use Case 3: Track Claim and Contribution Status

Member views the status of claims and contributions, and whether any action is required on their part.
## 4. System Design
### 4.1 Service Architecture
Login Service
Responsibilities: User registration, login, JWT token generation and validation.
Database: Stores user credentials and roles.
Endpoints:
/api/v1/auth/register: Register new users (Admin, Clerk, Members).
/api/v1/auth/login: User login to generate JWT token.
/api/v1/auth/validate: Validate JWT token for access to other services.
Welfare Management Service
Responsibilities: Manages members, dependents, claims, and fund disbursement.
Database: Stores all member and welfare-related data.
Endpoints:
/api/v1/members: Add/view member details.
/api/v1/dependents: Manage dependents of members.
/api/v1/claims: Submit, view, and approve claims.
/api/v1/funds: Handle disbursement and replenishment of welfare funds.
### 4.2 Database Design
Login Database
User Table: Stores user credentials, roles (Admin, Clerk, Member).
Token Table: Stores JWT tokens for validation and expiry.
Welfare Database
Member Table: Stores member details (e.g., name, ID number, registration date, contribution status).
Dependent Table: Stores dependents (e.g., name, relationship, documents).
Claim Table: Stores claims submitted by members, including status (pending, approved, disbursed).
Fund Transaction Table: Records all fund transactions (disbursements and replenishments).
### 4.3 Security
JWT Authentication: Stateless authentication using JWT tokens for access control across services.
Role-Based Access Control (RBAC): Roles define what actions a user can perform (Admin, Clerk, Member).
Document Verification: Secure upload and storage of documents (IDs, death certificates) for verification by clerks.
## 5. API Design
### 5.1 Login Service API
Endpoint	Method	Description	Role
/api/v1/auth/register	POST	Register a new user	Admin
/api/v1/auth/login	POST	Login and obtain JWT	Any Role
/api/v1/auth/validate	POST	Validate JWT token	Any Role
### 5.2 Welfare Service API
Endpoint	Method	Description	Role
/api/v1/members	POST	Add a new member	Clerk
/api/v1/dependents	POST	Add a new dependent	Member
/api/v1/claims	POST	Submit a new claim	Member
/api/v1/claims/{id}	PUT	Approve a claim	Clerk
/api/v1/funds/disburse	POST	Disburse funds for an approved claim	Admin
## 6. Non-Functional Requirements
### 6.1 Performance
The system should handle multiple requests simultaneously, especially during peak times when many members may be submitting claims or checking their status.
### 6.2 Scalability
The system architecture should support horizontal scaling to accommodate an increasing number of users without significant degradation in performance.
### 6.3 Security
Use strong encryption for sensitive data (e.g., documents).
Ensure secure storage and transmission of all user data.
Implement thorough logging and audit trails for all financial transactions.
## 7. Future Enhancements
Mobile App Integration: Expose REST APIs for mobile applications to interact with the system.
Notifications: Email or SMS notifications for members when claims are processed or funds are disbursed.
Reporting and Analytics: Advanced reporting features for admin users to analyze contribution patterns and fund balances.
## 8. Conclusion
This design document outlines the key aspects of the welfare management system, including functional and non-functional requirements, system architecture, database design, and API structure. It serves as a foundation for developing the backend services, ensuring role-based access control, and securing all interactions within the system.

