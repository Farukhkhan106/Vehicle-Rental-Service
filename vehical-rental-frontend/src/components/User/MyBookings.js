import React, { useEffect, useState } from 'react';
import { api } from '../../axiosConfig';
import UserNavbar from '../../components/Common/UserNavbar';
import '../MyBookings.css';

const MyBookings = () => {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchBookings = async () => {
      try {
        const response = await api.get('/booking/my-bookings');
        setBookings(response.data);
      } catch (err) {
        setError('Failed to fetch your bookings.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchBookings();
  }, []);

  return (
    <div className="my-bookings-container">
      <UserNavbar />
      <h2>My Bookings</h2>

      {loading && <p className="loading-text">Loading your bookings...</p>}
      {error && <p className="error-text">{error}</p>}
      {!loading && bookings.length === 0 && <p>No bookings found.</p>}

      <div className="bookings-list">
        {bookings.map(booking => (
          <div key={booking.id} className="booking-card">
            <h3>{booking.vehicle.brand} {booking.vehicle.model}</h3>
            <p><strong>Price:</strong> ₹{booking.vehicle.pricePerDay}/day</p>
            <p><strong>Location:</strong> {booking.vehicle.location}</p>

            <p className={`status ${booking.status.toLowerCase()}`}>
              🚗 Status: {booking.status}
            </p>

            <hr />
            <h4>Owner Details</h4>
            <p><strong>📞 Contact:</strong> {booking.vehicle.ownerPhone}</p>
            <p><strong>🏠 Address:</strong> {booking.vehicle.ownerAddress}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default MyBookings;
