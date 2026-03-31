
# Distributed Movie Booking System (B2B & B2C)

A high-performance Spring Boot application for movie ticket bookings with integrated discount engines and multi-tenant theatre management.

### Key Features:
* **B2C Flow:** Browse shows by city/date, seat selection, and automated discount application.
* **B2B Flow:** Theatre and inventory management (bulk creation/updates).
* **Architecture:** * **Optimistic & Pessimistic Locking** to prevent double-booking.
  * **Strategy Pattern** for flexible discount rules (Afternoon & Bulk discounts).
  * **Transactional Integrity** using Spring Data JPA.

### Discounts Implemented:
1. **Afternoon Discount:** 20% off for shows between 12 PM - 4 PM.
2. **Third Ticket Discount:** 50% off the third ticket in a single booking.
