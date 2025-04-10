import React, { useEffect, useState } from 'react';
import axios from 'axios';

const MyBookings = () => {
  const [bookings, setBookings] = useState([]);
  const userId = localStorage.getItem("userId");
  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchBookings = async () => {
      try {
        if (!userId || !token) {
          console.warn("Missing user ID or token");
          return;
        }

        const res = await axios.get(
          `http://localhost:8080/booking/my-detailed-bookings-by-user/${userId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        setBookings(res.data);
      } catch (err) {
        console.error("Failed to fetch bookings", err);
      }
    };

    fetchBookings();
  }, [userId, token]);

  return (
    <div className="container mt-4">
      <h2 className="text-center mb-4">My Bookings</h2>
      <div className="row">
        {bookings.length === 0 ? (
          <p className="text-center">No bookings found.</p>
        ) : (
          bookings.map((booking, index) => (
            <BookingCard key={index} booking={booking} />
          ))
        )}
      </div>
    </div>
  );
};

const BookingCard = ({ booking }) => {
  const {
    brand,
    model,
    number,
    image,
    pricePerDay,
    totalAmount,
    ownerCity,
    ownerName,
    ownerPhone,
    startDate,
    endDate,
    status,
  } = booking;

  return (
    <div className="col-md-4 mb-4">
      <div className="card shadow-sm h-100">
        {image && (
          <img
            src={`http://localhost:8080${image}`}
            className="card-img-top"
            alt="Vehicle"
            style={{ height: "200px", objectFit: "cover" }}
          />
        )}
        <div className="card-body">
          <h5 className="card-title">
            {brand} {model}
          </h5>
          <p className="card-text">
            <strong>Number Plate:</strong> {number}
            <br />
            <strong>Price/Day:</strong> ₹{pricePerDay}
            <br />
            <strong>Total Amount:</strong> ₹{totalAmount}
            <br />
            <strong>Location:</strong> {ownerCity}
            <br />
            <strong>Owner:</strong> {ownerName}
            <br />
            <strong>Owner Phone:</strong> {ownerPhone}
            <br />
            <strong>Start Date:</strong> {startDate}
            <br />
            <strong>End Date:</strong> {endDate}
            <br />
            <strong>Status:</strong>{" "}
            <span
              className={`badge ${
                status === "CONFIRMED"
                  ? "bg-success"
                  : status === "CANCELED"
                  ? "bg-danger"
                  : "bg-secondary"
              }`}
            >
              {status}
            </span>
          </p>
        </div>
      </div>
    </div>
  );
};

export default MyBookings;
