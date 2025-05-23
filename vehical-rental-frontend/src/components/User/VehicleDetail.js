import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { api } from '../../axiosConfig';
import UserNavbar from '../../components/Common/UserNavbar';
import '../VehicleDetail.css';

import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

import {
  Phone,
  MapPin,
  IndianRupee,
  User2,
  Car,
  CalendarClock,
  Clock4,
  ShieldCheck,
  AlertTriangle,
  Map,
} from 'lucide-react';

delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
  iconUrl: require('leaflet/dist/images/marker-icon.png'),
  shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
});

const VehicleDetail = () => {
  const { id } = useParams();
  const [vehicle, setVehicle] = useState(null);
  const [location, setLocation] = useState({ lat: 22.7196, lng: 75.8577 });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [startTime, setStartTime] = useState('');
  const [endTime, setEndTime] = useState('');

  const [rentalAmount, setRentalAmount] = useState(0);
  const [totalPrice, setTotalPrice] = useState(0);
  const [warning, setWarning] = useState('');
  const [availabilityWarning, setAvailabilityWarning] = useState('');

  const securityDeposit = 1000;

  useEffect(() => {
    const fetchVehicleDetails = async () => {
      try {
        const response = await api.get(`/vehicle/details/${id}`);
        const data = {
          ...response.data,
          photoUrls: JSON.parse(response.data.photosJson || '[]')
        };
        setVehicle(data);

        const address = `${data.ownerAddress}, ${data.ownerCity}`;
        const geo = await fetch(`https://api.opencagedata.com/geocode/v1/json?q=${encodeURIComponent(address)}&key=YOUR_OPENCAGE_API_KEY`);
        const geoData = await geo.json();

        if (geoData?.results?.[0]?.geometry) {
          setLocation({
            lat: geoData.results[0].geometry.lat,
            lng: geoData.results[0].geometry.lng
          });
        }

      } catch (err) {
        console.error(err);
        setError("Failed to fetch vehicle details.");
      } finally {
        setLoading(false);
      }
    };

    fetchVehicleDetails();
  }, [id]);

  useEffect(() => {
    const now = new Date();
    const later = new Date(now.getTime() + 60 * 60 * 1000);

    const format = (n) => (n < 10 ? `0${n}` : n);

    setStartDate(now.toISOString().split('T')[0]);
    setEndDate(later.toISOString().split('T')[0]);
    setStartTime(`${format(now.getHours())}:${format(now.getMinutes())}`);
    setEndTime(`${format(later.getHours())}:${format(later.getMinutes())}`);
  }, []);

  useEffect(() => {
    if (startDate && endDate && startTime && endTime && vehicle) {
      const start = new Date(`${startDate}T${startTime}`);
      const end = new Date(`${endDate}T${endTime}`);

      if (end <= start) {
        setWarning("End time must be after start time.");
        setRentalAmount(0);
        setTotalPrice(0);
        return;
      }

      const hours = Math.ceil((end - start) / (1000 * 60 * 60));
      if (hours < 5) {
        setWarning("Minimum booking is 5 hours.");
        setRentalAmount(0);
        setTotalPrice(0);
      } else {
        setWarning('');
        let rental = hours * (vehicle.pricePerDay / 24);
        if (hours === 5) rental += 50;
        rental = Math.ceil(rental);
        setRentalAmount(rental);
        setTotalPrice(rental + securityDeposit);
      }
    }
  }, [startDate, endDate, startTime, endTime, vehicle]);

  useEffect(() => {
    const checkAvailability = async () => {
      if (!vehicle || rentalAmount === 0) return;

      try {
        const res = await api.get('/booking/check-availability', {
          params: {
            vehicleId: vehicle.id,
            startDate,
            endDate
          }
        });

        if (!res.data) {
          setAvailabilityWarning("Vehicle is already booked for the selected dates.");
        } else {
          setAvailabilityWarning('');
        }
      } catch (err) {
        console.error("Availability check failed", err);
        setAvailabilityWarning("Could not check availability. Try again.");
      }
    };

    checkAvailability();
  }, [startDate, endDate, startTime, endTime, vehicle, rentalAmount]);

  const handlePayment = async () => {
    if (!startDate || !endDate || !startTime || !endTime) {
      alert("Please fill in all booking details.");
      return;
    }

    if (totalPrice === 0 || availabilityWarning) {
      alert("Booking is not valid.");
      return;
    }

    try {
      const confirmBooking = window.confirm(`Are you sure you want to pay ₹${totalPrice} for booking?`);
      if (!confirmBooking) return;

      const bookingData = {
        userId: 1, // Replace with actual userId
        vehicleId: vehicle.id,
        startDate: `${startDate}T${startTime}`,
        endDate: `${endDate}T${endTime}`,
      };

      const response = await api.post('/booking/create', bookingData);
      alert(response.data);
    } catch (error) {
      console.error("Error creating booking:", error);
      alert("Something went wrong. Please try again later.");
    }
  };

  if (loading) return <p>Loading vehicle details...</p>;
  if (error) return <p>{error}</p>;
  if (!vehicle) return <p>No vehicle details available.</p>;

  const durationHours = Math.ceil((new Date(`${endDate}T${endTime}`) - new Date(`${startDate}T${startTime}`)) / (1000 * 60 * 60));

  return (
    <div>
      <UserNavbar />
      <div className="vehicle-detail-container">
        <div className="two-column-layout">
          <div className="left-column">
            <div className="booking-section">
              <h2><CalendarClock size={20} /> Book This Vehicle</h2>
              <div className="booking-inputs">
                <label>Start Date:<input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} /></label>
                <label>Start Time:<input type="time" value={startTime} onChange={(e) => setStartTime(e.target.value)} /></label>
                <label>End Date:<input type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)} /></label>
                <label>End Time:<input type="time" value={endTime} onChange={(e) => setEndTime(e.target.value)} /></label>
              </div>

              {warning && <p className="warning-text"><AlertTriangle size={16} /> {warning}</p>}
              {availabilityWarning && <p className="warning-text"><AlertTriangle size={16} /> {availabilityWarning}</p>}

              {totalPrice > 0 && !availabilityWarning && (
                <div className="total-price">
                  <p><IndianRupee size={16} /> Rental Price: ₹{rentalAmount}</p>
                  <p><Clock4 size={16} /> Duration: <strong>{durationHours} hours</strong></p>
                  <p><ShieldCheck size={16} /> Security Deposit: ₹{securityDeposit} <span style={{ fontSize: '12px', color: '#888' }}>(Refundable)</span></p>
                  <hr style={{ margin: '10px 0' }} />
                  <p><strong>Total Payable Now: ₹{totalPrice}</strong></p>
                  <p style={{ fontSize: '13px', color: '#777' }}>
                    Security deposit will be refunded after vehicle inspection.<br />
                    Deductions may apply for late return, fuel shortage, or any damage.
                  </p>
                </div>
              )}

              <button className="payment-button" onClick={handlePayment}>Proceed to Payment</button>
            </div>

            <div className="map-wrapper">
              <h2><Map size={20} /> Vehicle Location</h2>
              <MapContainer center={[location.lat, location.lng]} zoom={13} scrollWheelZoom={false} style={{ height: '300px', width: '100%', borderRadius: '12px' }}>
                <TileLayer
                  attribution='&copy; OpenStreetMap contributors'
                  url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                <Marker position={[location.lat, location.lng]}>
                  <Popup>
                    {vehicle.brand} {vehicle.model} Location
                  </Popup>
                </Marker>
              </MapContainer>
              <a href={`https://www.google.com/maps?q=${location.lat},${location.lng}`} target="_blank" rel="noopener noreferrer" style={{ fontSize: '14px', color: '#1a73e8', display: 'block', marginTop: '8px' }}>
                Open in Google Maps →
              </a>
            </div>
          </div>

          <div className="right-column">
            <div className="vehicle-detail-card">
              <h2><Car size={20} /> Vehicle Details</h2>
              <p><strong>Brand:</strong> {vehicle.brand}</p>
              <p><strong>Model:</strong> {vehicle.model}</p>
              <p><IndianRupee size={16} /> <strong>Price Per Day:</strong> ₹{vehicle.pricePerDay}</p>
              <p><strong>Status:</strong> {vehicle.status}</p>
              {vehicle.photoUrls?.length > 0 && (
                <div className="vehicle-images-gallery">
                  {vehicle.photoUrls.map((url, index) => (
                    <img
                      key={index}
                      src={`http://localhost:8080${url}`}
                      alt={`Vehicle ${index + 1}`}
                      className="vehicle-detail-image"
                      onError={(e) => { e.target.src = '/placeholder.jpg' }}
                    />
                  ))}
                </div>
              )}
            </div>

            <div className="owner-detail-card">
              <h2><User2 size={20} /> Owner Details</h2>
              <p><Phone size={16} /> <strong>Phone:</strong> {vehicle.ownerPhone}</p>
              <p><MapPin size={16} /> <strong>City:</strong> {vehicle.ownerCity}</p>
              <p><MapPin size={16} /> <strong>Address:</strong> {vehicle.ownerAddress}</p>
            </div>

            <div className="rental-instructions">
              <h3><ShieldCheck size={18} /> Rental Instructions:</h3>
              <ul>
                <li>Minimum booking: 5 hours</li>
                <li>5-hour bookings have ₹50 short duration charge</li>
                <li>Pricing is hourly (based on daily rate)</li>
                <li>15-minute grace period for returns</li>
                <li>₹100/hour late return charge</li>
                <li>No refund for early return</li>
                <li>Security deposit ₹1000 taken at booking</li>
                <li>Deposit refundable after damage/fuel check</li>
                <li>Photos or proof shared for any deduction</li>
                <li>Refund processed within 3-5 working days</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default VehicleDetail;
